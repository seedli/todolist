package tom.demo.todolist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tom.demo.todolist.dao.UserDAO;
import tom.demo.todolist.domain.TodoList;
import tom.demo.todolist.domain.User;
import tom.demo.todolist.util.UserUtilities;

@Service
public class ListServiceImpl implements ListService {

	@Autowired
	UserDAO userDAO;

	@Override
	public List<TodoList> getMyList() {
		User user = userDAO.findById(UserUtilities.getCurrentUser().getUserId()).orElse(null);

		return user == null ? null : user.getTodoLists();
	}

}
