package tom.demo.todolist.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tom.demo.todolist.domain.User;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {

	public User findByName(String name);
}
