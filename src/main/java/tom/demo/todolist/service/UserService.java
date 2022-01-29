package tom.demo.todolist.service;

import java.util.List;

import tom.demo.todolist.controller.json.UserJson;
import tom.demo.todolist.domain.User;

public interface UserService {

	User findById(Long id);

	User findUserByName(String username);

	List<User> findAll();

	Long createUser(UserJson userJson);

	Long updateUser(UserJson userJson);

	Long updatePassword(UserJson userJson);

	Long deleteUser(Long userId);
}
