package tom.demo.todolist.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tom.demo.todolist.controller.json.ItemJson;
import tom.demo.todolist.controller.json.ItemMoveJson;
import tom.demo.todolist.dao.ItemDAO;
import tom.demo.todolist.dao.ListDAO;
import tom.demo.todolist.dao.UserDAO;
import tom.demo.todolist.domain.Item;
import tom.demo.todolist.domain.TodoList;
import tom.demo.todolist.domain.User;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	ListDAO listDAO;

	@Autowired
	ItemDAO itemDAO;

	@Autowired
	UserDAO userDAO;

	@Override
	public boolean hasPermissionOfItem(Long userId, Long itemId) {
		Item item = itemDAO.findById(itemId).orElse(null);
		User user = userDAO.findById(userId).orElse(null);
		if (item != null && user != null && user.getTodoLists() != null) {
			for (TodoList list : user.getTodoLists()) {
				if (list.getId() == item.getListId())
					return true;
			}
		}
		return false;
	}

	@Override
	public List<Item> getItemsByListId(Long listId, String orderBy, String sort, String status) {
		// default is ordering by sortOrder
		if ("deadline".equalsIgnoreCase(orderBy)) {
			return itemDAO.findByListId(listId, orderBy, sort, status);
		} else {
			return itemDAO.findByListId(listId, "sortOrder", sort, status);
		}
	}

	@Override
	public Long addItem(ItemJson json) {
		TodoList list = listDAO.findById(json.getListId()).orElse(null);
		if (list == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "List not found");

		Item item = new Item();
		item.setName(json.getName());
		item.setDone(false);
		item.setDescription(json.getDescription());
		item.setDeadline(json.getDeadline() == null ? LocalDateTime.now() : json.getDeadline());
		Integer maxSortOrder = itemDAO.findMaxSortOrderByListId(json.getListId());
		item.setSortOrder(maxSortOrder == null ? 1 : maxSortOrder + 1);
		item.setListId(json.getListId());

		return itemDAO.save(item).getId();
	}

	@Override
	public ItemJson updateItem(ItemJson json) {
		Item item = itemDAO.findById(json.getId()).orElse(null);
		if (item != null) {
			item.setName(json.getName());
			item.setDescription(json.getDescription());
			item.setDeadline(json.getDeadline());
			item.setDone(json.isDone());
			itemDAO.save(item);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
		}
		return json;
	}

	@Override
	public Long deleteItem(Long itemId) {
		itemDAO.deleteById(itemId);
		return itemId;
	}

	@Override
	public Item checkItem(Long itemId) {
		Item item = itemDAO.findById(itemId).orElse(null);
		if (item != null) {
			item.setDone(!item.isDone());
			itemDAO.save(item);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
		}
		return item;
	}

	@Override
	@Transactional
	public Item moveItem(ItemMoveJson json) {
		Item item = itemDAO.getById(json.getItemId());
		List<Item> items = null;

		Long originListId = null;
		// move to another list
		if (json.getTargetListId() != null && json.getTargetListId() != item.getListId()) {
			items = itemDAO.findByListIdOrderBySortOrderAsc(json.getTargetListId());
			originListId = item.getListId();
			item.setListId(json.getTargetListId());
		} else {
			items = itemDAO.findByListIdOrderBySortOrderAsc(item.getListId());
			items.remove(item);
		}

		if (items == null || items.isEmpty()) {
			// set item to first
			item.setSortOrder(1);
		} else if (json.getSortOrder() == null || json.getSortOrder() > items.size()) {
			// set item to the last of item
			items.add(item);
			sortItem(items);
		} else {
			// sort items
			item.setSortOrder(json.getSortOrder());
			items.add(json.getSortOrder() - 1, item);
			sortItem(items);
		}

		if (originListId != null) {
			sortItem(itemDAO.findByListIdOrderBySortOrderAsc(originListId));
		}

		return item;
	}

	private void sortItem(List<Item> items) {
		int i = 1;
		for (Item item : items) {
			item.setSortOrder(i++);
		}
	}
}
