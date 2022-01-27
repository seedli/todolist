package tom.demo.todolist.service;

import java.util.List;

import tom.demo.todolist.controller.json.ItemJson;
import tom.demo.todolist.controller.json.ItemMoveJson;
import tom.demo.todolist.domain.Item;

public interface ItemService {

	List<Item> getItemsByListId(Long listId, String orderBy, String sort, String status);

	Long addItem(ItemJson json);

	Item moveItem(ItemMoveJson json);

	Item checkItem(Long itemId);

	Item updateItem(ItemJson json);

	boolean hasPermissionOfItem(Long userId, Long itemId);

	Long deleteItem(Long itemId);

}
