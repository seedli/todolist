package tom.demo.todolist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import tom.demo.todolist.controller.json.ItemJson;
import tom.demo.todolist.controller.json.ItemMoveJson;
import tom.demo.todolist.dao.ItemDAO;
import tom.demo.todolist.dao.ListDAO;
import tom.demo.todolist.dao.UserDAO;
import tom.demo.todolist.domain.Item;
import tom.demo.todolist.domain.TodoList;
import tom.demo.todolist.domain.User;
import tom.demo.todolist.service.ItemService;
import tom.demo.todolist.util.QueryConstants;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ItemServiceImplTest {

	@MockBean
	ListDAO listDAO;

	@MockBean
	ItemDAO itemDAO;

	@MockBean
	UserDAO userDAO;

	@Autowired
	ItemService itemService;

	@BeforeEach
	public void beforeEach() {
		List<Item> items = new ArrayList<>();
		Item item1 = new Item();
		item1.setId(1L);
		item1.setName("item1");
		item1.setDescription("item1 desc");
		item1.setDeadline(LocalDateTime.now());
		item1.setDone(false);
		item1.setSortOrder(1);
		item1.setListId(1L);

		Item item2 = new Item();
		item2.setId(2L);
		item2.setName("item2");
		item2.setDescription("item2 desc");
		item2.setDeadline(LocalDateTime.now());
		item2.setDone(false);
		item2.setSortOrder(2);
		item2.setListId(1L);

		items.add(item1);
		items.add(item2);

		TodoList list = new TodoList();
		list.setId(1L);
		list.setUserId(1L);
		list.setItems(items);

		List<TodoList> lists = new ArrayList<>();
		lists.add(list);

		User tom = new User();
		tom.setId(1L);
		tom.setName("tom");
		tom.setTodoLists(lists);

		Mockito.when(itemDAO.getById(anyLong())).thenReturn(item1);
		Mockito.when(itemDAO.findById(anyLong())).thenReturn(Optional.of(item1));
		Mockito.when(itemDAO.findByListId(anyLong(), anyString(), anyString(), anyString())).thenReturn(items);
		Mockito.when(itemDAO.findMaxSortOrderByListId(anyLong())).thenReturn(5);
		Mockito.when(itemDAO.save(any(Item.class))).thenReturn(item1);
		Mockito.when(itemDAO.findByListIdOrderBySortOrderAsc(anyLong())).thenReturn(items);
		Mockito.when(userDAO.findById(anyLong())).thenReturn(Optional.of(tom));
		Mockito.when(listDAO.findById(anyLong())).thenReturn(Optional.of(list));

	}

	@Test
	public void hasPermissionOfItemTest() {
		assertTrue(itemService.hasPermissionOfItem(1L, 1L));
	}

	@Test
	public void getItemsByListIdTest() {
		assertEquals(2,
				itemService.getItemsByListId(1L, "deadline", QueryConstants.ASC, QueryConstants.EXPIRED).size());
		assertEquals(2,
				itemService.getItemsByListId(1L, "sortOrder", QueryConstants.ASC, QueryConstants.EXPIRED).size());
	}

	@Test
	public void addItemTest() {
		ItemJson json = new ItemJson();
		json.setName("new item");
		json.setDeadline(LocalDateTime.now());
		json.setDescription("new item desc");
		json.setListId(1L);
		json.setDone(false);
		json.setSortOrder(1);

		assertEquals(1L, itemService.addItem(json));
	}

	@Test
	public void updateItemTest() {
		ItemJson json = new ItemJson();
		json.setId(1L);
		json.setName("new item");
		json.setDeadline(LocalDateTime.now());
		json.setDescription("new item desc");
		json.setListId(1L);
		json.setDone(false);
		json.setSortOrder(1);

		assertEquals(json, itemService.updateItem(json));
	}

	@Test
	public void deleteItemTest() {
		assertEquals(1L, itemService.deleteItem(1L));
	}

	@Test
	public void checkItemTest() {
		assertTrue(itemService.checkItem(1L).isDone());
		assertFalse(itemService.checkItem(1L).isDone());
	}

	@Test
	public void moveItemTest() {
		ItemMoveJson json = new ItemMoveJson();
		json.setItemId(1L);
		json.setSortOrder(2);

		assertEquals(2, itemService.moveItem(json).getSortOrder());
	}
}
