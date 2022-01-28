package tom.demo.todolist.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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

import tom.demo.todolist.CustomWebMvcConfig;
import tom.demo.todolist.config.BeanConfig;
import tom.demo.todolist.controller.json.UserJson;
import tom.demo.todolist.domain.Role;
import tom.demo.todolist.domain.User;
import tom.demo.todolist.service.UserService;

@WebMvcTest(UserController.class)
@Import({ CustomWebMvcConfig.class, BeanConfig.class })
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@BeforeEach
	public void setup() throws Exception {
		User tom = new User();
		tom.setId(1L);
		tom.setName("tom");
		tom.setPassword("123");
		tom.setRole(new Role());

		User jerry = new User();
		jerry.setId(2L);
		jerry.setName("jerry");
		jerry.setPassword("234");
		jerry.setRole(new Role());

		List<User> users = new ArrayList<>();
		users.add(tom);
		users.add(jerry);

		Mockito.when(userService.findAll()).thenReturn(users);
		Mockito.when(userService.createUser(any(UserJson.class))).thenReturn(99L);
		Mockito.when(userService.updatePassword(any(UserJson.class))).thenReturn(99L);
		Mockito.when(userService.updateUser(any(UserJson.class))).thenReturn(99L);
		Mockito.when(userService.findById(anyLong())).thenReturn(tom);
	}

	@Test
	@WithUserDetails("admin")
	public void getAllUserTest() throws Exception {
		this.mockMvc
				.perform(get("/user/list"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().json("[{'id':1,'name':'tom'},{'id':2,'name':'jerry'}]"));
	}

	@Test
	@WithUserDetails("admin")
	public void createUserTest() throws Exception {
		UserJson userJson = new UserJson();
		userJson.setName("ashley");
		userJson.setPassword("654321");
		userJson.setRoleId(2L);

		this.mockMvc
				.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(userJson)))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string("99"));
	}

	@Test
	@WithUserDetails("admin")
	public void updateUserTest() throws Exception {
		UserJson userJson = new UserJson();
		userJson.setId(99L);
		userJson.setName("ashley");
		userJson.setPassword("654321");
		userJson.setRoleId(2L);

		this.mockMvc
				.perform(put("/user").contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(userJson)))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string("99"));
	}

	@Test
	@WithUserDetails("admin")
	public void getUserByIdTest() throws Exception {
		this.mockMvc
				.perform(get("/user/1"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().json("{'id':1,'name':'tom'}"));
	}

	@Test
	@WithUserDetails("admin")
	public void updatePasswordTest() throws JsonProcessingException, Exception {
		UserJson userJson = new UserJson();
		userJson.setId(99L);
		userJson.setPassword("654321");

		this.mockMvc
				.perform(put("/user/password").contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(userJson)))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string("99"));
	}
}
