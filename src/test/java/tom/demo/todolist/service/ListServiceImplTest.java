package tom.demo.todolist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import tom.demo.todolist.controller.json.ListJson;
import tom.demo.todolist.dao.ListDAO;
import tom.demo.todolist.dao.UserDAO;
import tom.demo.todolist.domain.TodoList;
import tom.demo.todolist.domain.User;
import tom.demo.todolist.service.ListService;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ListServiceImplTest {

	@MockBean
	UserDAO userDAO;

	@MockBean
	ListDAO listDAO;

	@Autowired
	ListService listService;

	@BeforeEach
	public void beforeEach() {
		List<TodoList> lists = new ArrayList<>();
		List<TodoList> sharedlists = new ArrayList<>();

		TodoList list1 = new TodoList();
		list1.setId(1L);
		list1.setName("List1");

		TodoList list2 = new TodoList();
		list2.setId(2L);
		list2.setName("List2");

		TodoList list3 = new TodoList();
		list3.setId(3L);
		list3.setName("List3");

		lists.add(list1);
		lists.add(list2);
		sharedlists.add(list3);

		Mockito.when(listDAO.findByUserIdOrderByIdAsc(anyLong())).thenReturn(lists);
		Mockito.when(listDAO.save(any(TodoList.class))).thenReturn(list1);
		Mockito.when(listDAO.findById(anyLong())).thenReturn(Optional.of(list1));

		User tom = new User();
		tom.setId(1L);
		tom.setName("tom");
		tom.setSharedLists(new HashSet<TodoList>(sharedlists));
		tom.setTodoLists(lists);

		Mockito.when(userDAO.findById(anyLong())).thenReturn(Optional.of(tom));
	}

	@Test
	public void getListsByUserIdTest() {
		assertEquals(2, listService.getListsByUserId(1L).size());
	}

	@Test
	public void getListSharedWithMe() {
		assertEquals(1, listService.getListSharedWithMe(1L).size());
	}

	@Test
	public void hasPermissionOfListTest() {
		assertTrue(listService.hasPermissionOfList(1L, 1L));
		assertTrue(listService.hasPermissionOfList(1L, 3L));
		assertFalse(listService.hasPermissionOfList(1L, 4L));
	}

	@Test
	public void createListTest() {
		ListJson json = new ListJson();
		json.setId(1L);
		json.setName("list new");
		json.setUserId(1L);
		assertEquals(1L, listService.createList(json));
	}

	@Test
	public void deleteListTest() {
		assertEquals(1L, listService.deleteList(1L));
	}

	@Test
	public void updateListTest() {
		ListJson json = new ListJson();
		json.setId(1L);
		json.setName("list new");
		json.setUserId(1L);

		assertEquals(json, listService.updateList(json));
	}

	@Test
	public void shareListToUserTest() {
		assertEquals(1L, listService.shareListToUser(1L, 1L));
	}

	@Test
	public void revokeSharedListFromUserTest() {
		assertEquals(1L, listService.revokeSharedListFromUser(1L, 1L));
	}
}
