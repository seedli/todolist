package tom.demo.todolist.dao;

import java.util.List;

import tom.demo.todolist.domain.Item;

public interface ItemDAOCustom {

	List<Item> findByListId(Long listId, String orderBy, String sort, String status);

}
