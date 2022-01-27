package tom.demo.todolist.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import tom.demo.todolist.config.AppConfig;
import tom.demo.todolist.service.UserService;

@ContextConfiguration(classes = { AppConfig.class })
@WebMvcTest(UserController.class)
@Import(UserController.class)
public class UserControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private UserService userService;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	public void getAllUserTestOK() throws Exception {
		this.mockMvc
				.perform(get("/user/list"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));
	}
}
