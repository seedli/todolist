package tom.demo.todolist.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tom.demo.todolist.dao.UserDAO;
import tom.demo.todolist.domain.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Override
	public Optional<User> findById(Long id) {

		return userDAO.findById(id);
	}

}
