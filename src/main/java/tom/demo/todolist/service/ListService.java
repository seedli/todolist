package tom.demo.todolist.service;

import java.util.List;

import tom.demo.todolist.controller.json.ListJson;
import tom.demo.todolist.domain.TodoList;

public interface ListService {

	boolean hasPermissionOfList(Long userId, Long listId);

	ListJson updateList(ListJson listJson);

	List<TodoList> getListsByUserId(Long userId);

	Long createList(ListJson json);

	Long deleteList(Long listId);

	Long shareListToUser(Long listId, Long userId);

	Long revokeSharedListFromUser(Long listId, Long userId);

	List<TodoList> getListSharedListsByUserId(Long userId);
}
