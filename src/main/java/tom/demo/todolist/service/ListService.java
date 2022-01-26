package tom.demo.todolist.service;

import java.util.List;

import tom.demo.todolist.domain.TodoList;

public interface ListService {

	List<TodoList> getMyList();
}
