package tom.demo.todolist.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tom.demo.todolist.controller.json.ListJson;
import tom.demo.todolist.dao.ListDAO;
import tom.demo.todolist.dao.UserDAO;
import tom.demo.todolist.domain.TodoList;
import tom.demo.todolist.domain.User;

@Service
public class ListServiceImpl implements ListService {

	@Autowired
	UserDAO userDAO;

	@Autowired
	ListDAO listDAO;

	@Override
	public List<TodoList> getListsByUserId(Long userId) {
		return listDAO.findByUserIdOrderByIdAsc(userId);
	}

	@Override
	public List<TodoList> getListSharedWithMe(Long userId) {
		User user = userDAO.findById(userId).orElse(null);
		if (user != null && user.getSharedLists() != null) {
			return new ArrayList<>(user.getSharedLists());
		}
		return null;
	}

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
	public Long createList(ListJson json) {
		TodoList list = new TodoList();
		list.setName(json.getName());
		list.setUserId(json.getUserId());
		return listDAO.save(list).getId();
	}

	@Override
	public Long deleteList(Long listId) {
		listDAO.deleteById(listId);
		return listId;
	}

	@Override
	public ListJson updateList(ListJson listJson) {
		TodoList list = listDAO.findById(listJson.getId()).orElse(null);
		if (list == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "List not found");

		list.setName(listJson.getName());
		listDAO.save(list);

		return listJson;
	}

	@Override
	@Transactional
	public Long shareListToUser(Long listId, Long userId) {
		TodoList list = listDAO.findById(listId).orElse(null);
		User user = userDAO.findById(userId).orElse(null);
		if (list != null && user != null) {
			Set<User> users = list.getSharedUsers();
			if (users == null) {
				users = new HashSet<>();
				list.setSharedUsers(users);
			}
			users.add(user);
		}
		return listId;
	}

	@Override
	@Transactional
	public Long revokeSharedListFromUser(Long listId, Long userId) {
		TodoList list = listDAO.findById(listId).orElse(null);
		User user = userDAO.findById(userId).orElse(null);
		if (list != null && user != null) {
			Set<User> users = list.getSharedUsers();
			if (users != null)
				users.remove(user);
		}
		return listId;
	}
}
