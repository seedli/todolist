package tom.demo.todolist.service;

public interface RbacService {

	boolean hasPermissionOfList(Long userId, Long listId);

	boolean hasPermissionOfItem(Long userId, Long itemId);

}
