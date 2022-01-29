package tom.demo.todolist.controller;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import tom.demo.todolist.CustomSecurityConfig;
import tom.demo.todolist.config.BeanConfig;
import tom.demo.todolist.controller.json.ItemJson;
import tom.demo.todolist.controller.json.ItemMoveJson;
import tom.demo.todolist.domain.Item;
import tom.demo.todolist.service.ItemService;
import tom.demo.todolist.service.ListService;

@WebMvcTest(ItemController.class)
@Import({ CustomSecurityConfig.class, BeanConfig.class })
public class ItemControllerTest {

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

		Item item1 = new Item();
		item1.setId(1L);
		item1.setName("item1");
		item1.setDescription("item1 desc");
		item1.setDeadline(dateTime);
		item1.setDone(false);
		item1.setSortOrder(99);
		item1.setListId(2L);

		Mockito.when(itemService.addItem(any(ItemJson.class))).thenReturn(99L);
		Mockito.when(itemService.updateItem(any(ItemJson.class))).then(returnsFirstArg());
		Mockito.when(itemService.deleteItem(anyLong())).thenReturn(99L);
		Mockito.when(itemService.moveItem(any(ItemMoveJson.class))).thenReturn(item1);
		Mockito.when(itemService.checkItem(anyLong())).thenReturn(item1);
	}

	@Test
	@WithUserDetails("admin")
	public void addItemTest() throws JsonProcessingException, Exception {
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

		ItemJson json = new ItemJson();
		json.setName("item 1");
		json.setDescription("item 1 desc");
		json.setListId(1L);
		json.setSortOrder(1);
		json.setDone(false);
		json.setDeadline(LocalDateTime.now());

		this.mockMvc
				.perform(post("/item").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(json)))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string("99"));
	}

	@Test
	@WithUserDetails("admin")
	public void updateItemTest() throws JsonProcessingException, Exception {
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

		ItemJson json = new ItemJson();
		json.setName("item 1");
		json.setId(1L);
		json.setDescription("item 1 desc");
		json.setListId(1L);
		json.setSortOrder(1);
		json.setDone(false);
		json.setDeadline(LocalDateTime.now());

		this.mockMvc
				.perform(put("/item").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(json)))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(mapper.writeValueAsString(json)));
	}

	@Test
	@WithUserDetails("admin")
	public void deleteItemTest() throws Exception {
		this.mockMvc
				.perform(delete("/item/99"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string("99"));
	}

	@Test
	@WithUserDetails("admin")
	public void moveItemTest() throws JsonProcessingException, Exception {
		ObjectMapper mapper = new ObjectMapper();

		ItemMoveJson json = new ItemMoveJson();
		json.setItemId(1L);
		json.setSortOrder(99);
		json.setTargetListId(2L);

		this.mockMvc
				.perform(put("/item/move").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(json)))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(
						"{'id':1,'name':'item1','description':'item1 desc','listId':2,'sortOrder':99,'deadline':'2022-01-28 16:12:16','done':false}"));
	}

	@Test
	@WithUserDetails("admin")
	public void checkItemTest() throws JsonProcessingException, Exception {
		this.mockMvc
				.perform(put("/item/check/1"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(
						"{'id':1,'name':'item1','description':'item1 desc','listId':2,'sortOrder':99,'deadline':'2022-01-28 16:12:16','done':false}"));
	}
}
