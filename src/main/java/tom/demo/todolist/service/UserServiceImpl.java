package tom.demo.todolist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tom.demo.todolist.controller.json.UserJson;
import tom.demo.todolist.dao.RoleDAO;
import tom.demo.todolist.dao.UserDAO;
import tom.demo.todolist.domain.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User findById(Long id) {
		return userDAO.findById(id).orElse(null);
	}

	@Override
	public User findUserByName(String username) {
		return userDAO.findByName(username);
	}

	@Override
	public Long saveUser(UserJson userJson) {
		User user = userDAO.findById(userJson.getId()).orElse(new User());

		user.setName(userJson.getName());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(roleDAO.findById(userJson.getRoleId()).orElse(roleDAO.findByRole("USER")));
		return userDAO.save(user).getId();
	}

	@Override
	public List<User> findAll() {
		return userDAO.findAll();
	}
}
