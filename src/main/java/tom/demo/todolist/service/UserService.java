package tom.demo.todolist.service;

import tom.demo.todolist.controller.json.UserJson;
import tom.demo.todolist.domain.User;

public interface UserService {

	public User findById(Long id);

	public User findUserByName(String username);

	public Long saveUser(UserJson userJson);
}
