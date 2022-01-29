package tom.demo.todolist.controller;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tom.demo.todolist.CustomSecurityConfig;
import tom.demo.todolist.config.BeanConfig;
import tom.demo.todolist.controller.json.ListJson;
import tom.demo.todolist.domain.Item;
import tom.demo.todolist.domain.Role;
import tom.demo.todolist.domain.TodoList;
import tom.demo.todolist.domain.User;
import tom.demo.todolist.service.ItemService;
import tom.demo.todolist.service.ListService;

@WebMvcTest(ListController.class)
@Import({ CustomSecurityConfig.class, BeanConfig.class })
public class ListControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ListService listService;

	@MockBean
	ItemService itemService;

	@BeforeEach
	public void setUp() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse("2022-01-28 16:12:16", formatter);

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
		item1.setDeadline(dateTime);
		item1.setDone(false);
		item1.setSortOrder(1);
		item1.setListId(1L);

		Item item2 = new Item();
		item2.setId(2L);
		item2.setName("item2");
		item2.setDescription("item2 desc");
		item2.setDeadline(dateTime);
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
		Mockito.when(listService.getListSharedListsByUserId(anyLong())).thenReturn(lists);
		Mockito.when(itemService.getItemsByListId(anyLong(), anyString(), anyString(), anyString())).thenReturn(items);
		Mockito.when(listService.updateList(any(ListJson.class))).then(returnsFirstArg());
		Mockito.when(listService.createList(any(ListJson.class))).thenReturn(99L);
		Mockito.when(listService.deleteList(anyLong())).thenReturn(99L);
		Mockito.when(listService.shareListToUser(anyLong(), anyLong())).thenReturn(99L);
		Mockito.when(listService.revokeSharedListFromUser(anyLong(), anyLong())).thenReturn(99L);
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

	@Test
	@WithUserDetails("admin")
	public void getListSharedWithMeTest() throws Exception {
		this.mockMvc
				.perform(get("/list/sharedWithMe"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(
						"[{'id':1,'name':'list 1','userId':1}, {'id':2,'name':'list 2','userId':1}]"));
	}

	@Test
	@WithUserDetails("admin")
	public void getItemsByListIdTest() throws Exception {
		this.mockMvc
				.perform(get("/list/1?status=expired&orderBy=sortOrder&sort=asc"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(
						"[{'id':1,'name':'item1','description':'item1 desc','listId':1,'sortOrder':1,'deadline':'2022-01-28 16:12:16','done':false},"
								+ "{'id':2,'name':'item2','description':'item2 desc','listId':1,'sortOrder':2,'deadline':'2022-01-28 16:12:16','done':false}]"));
	}

	@Test
	@WithUserDetails("admin")
	public void createListTest() throws JsonProcessingException, Exception {
		ListJson json = new ListJson();
		json.setId(1L);
		json.setName("test list");
		json.setUserId(1L);

		this.mockMvc
				.perform(post("/list").contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(json)))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string("99"));
	}

	@Test
	@WithUserDetails("admin")
	public void deleteListTest() throws JsonProcessingException, Exception {
		this.mockMvc
				.perform(delete("/list/99"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string("99"));
	}

	@Test
	@WithUserDetails("admin")
	public void updateListTest() throws JsonProcessingException, Exception {
		ListJson json = new ListJson();
		json.setId(1L);
		json.setName("test list");
		json.setUserId(1L);

		this.mockMvc
				.perform(put("/list").contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(json)))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().json("{'id':1,'name':'test list','userId':1}"));
	}

	@Test
	@WithUserDetails("admin")
	public void shareListToUserTest() throws Exception {
		this.mockMvc
				.perform(put("/list/share/99/2"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string("99"));
	}

	@Test
	@WithUserDetails("admin")
	public void revokeListFromUserTest() throws Exception {
		this.mockMvc
				.perform(put("/list/revoke/99/2"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string("99"));
	}
}
