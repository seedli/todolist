package tom.demo.todolist.service;

import java.util.List;

import tom.demo.todolist.controller.json.UserJson;
import tom.demo.todolist.domain.User;

public interface UserService {

	User findById(Long id);

	User findUserByName(String username);

	Long saveUser(UserJson userJson);

	List<User> findAll();
}
