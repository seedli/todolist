package tom.demo.todolist.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import tom.demo.todolist.CustomWebMvcConfig;
import tom.demo.todolist.config.BeanConfig;
import tom.demo.todolist.domain.Item;
import tom.demo.todolist.domain.Role;
import tom.demo.todolist.domain.TodoList;
import tom.demo.todolist.domain.User;
import tom.demo.todolist.service.ItemService;
import tom.demo.todolist.service.ListService;

@WebMvcTest(ListController.class)
@Import({ CustomWebMvcConfig.class, BeanConfig.class })
public class ListControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ListService listService;

	@MockBean
	ItemService itemService;

	@BeforeEach
	public void setUp() {
		User tom = new User();
		tom.setId(2L);
		tom.setName("tom");
		tom.setPassword("123");
		tom.setRole(new Role());

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

		TodoList list1 = new TodoList();
		list1.setId(1L);
		list1.setName("list 1");
		list1.setUserId(1L);
		list1.setItems(items);
		list1.setSharedUsers(Collections.singleton(tom));

		TodoList list2 = new TodoList();
		list2.setId(2L);
		list2.setName("list 2");
		list2.setUserId(1L);
		list2.setItems(null);
		list2.setSharedUsers(Collections.singleton(tom));

		List<TodoList> lists = new ArrayList<>();
		lists.add(list1);
		lists.add(list2);

		Mockito.when(listService.getListsByUserId(anyLong())).thenReturn(lists);
	}

	@Test
	@WithUserDetails("admin")
	public void getMyListTest() throws Exception {
		this.mockMvc
				.perform(get("/list/my"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(
						"[{'id':1,'name':'list 1','userId':1}, {'id':2,'name':'list 2','userId':1}]"));
	}
}
