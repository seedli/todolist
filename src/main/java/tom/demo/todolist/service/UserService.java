package tom.demo.todolist.service;

import java.util.Optional;

import tom.demo.todolist.domain.User;

public interface UserService {

	public Optional<User> findById(Long id);
}
