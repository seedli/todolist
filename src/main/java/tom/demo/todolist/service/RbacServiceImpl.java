package tom.demo.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tom.demo.todolist.dao.ItemDAO;
import tom.demo.todolist.dao.UserDAO;
import tom.demo.todolist.domain.Item;
import tom.demo.todolist.domain.TodoList;
import tom.demo.todolist.domain.User;

@Service
public class RbacServiceImpl implements RbacService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ItemDAO itemDAO;

	@Override
	public boolean hasPermissionOfList(Long userId, Long listId) {
		User user = userDAO.findById(userId).orElse(null);
		if (user != null) {
			if (user.getTodoLists() != null) {
				for (TodoList list : user.getTodoLists()) {
					if (list.getId() == listId)
						return true;
				}
			}
			if (user.getSharedLists() != null) {
				for (TodoList list : user.getSharedLists()) {
					if (list.getId() == listId)
						return true;
				}
			}
		}
		return false;
	}

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
}
