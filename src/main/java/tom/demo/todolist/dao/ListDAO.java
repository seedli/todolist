package tom.demo.todolist.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tom.demo.todolist.domain.TodoList;

@Repository
public interface ListDAO extends JpaRepository<TodoList, Long> {

	public List<TodoList> findByUserIdOrderByIdAsc(Long userId);

}
