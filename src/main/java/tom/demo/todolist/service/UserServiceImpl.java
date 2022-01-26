package tom.demo.todolist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
	public Long createUser(UserJson userJson) {
		User user = new User();

		user.setName(userJson.getName());
		user.setPassword(passwordEncoder.encode(userJson.getPassword()));
		user.setRole(roleDAO.findById(userJson.getRoleId()).orElse(roleDAO.findByRole("USER")));
		return userDAO.save(user).getId();
	}

	@Override
	public Long updateUser(UserJson userJson) {
		User user = findById(userJson.getId());
		if (user == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		user.setName(userJson.getName());
		user.setPassword(passwordEncoder.encode(userJson.getPassword()));
		user.setRole(roleDAO.findById(userJson.getRoleId()).orElse(roleDAO.findByRole("USER")));
		return userDAO.save(user).getId();
	}

	@Override
	public Long updatePassword(UserJson userJson) {
		User user = findById(userJson.getId());
		if (user == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userDAO.save(user).getId();
	}

	@Override
	public List<User> findAll() {
		return userDAO.findAll();
	}
}
