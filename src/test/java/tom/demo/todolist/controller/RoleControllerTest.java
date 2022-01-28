package tom.demo.todolist.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import tom.demo.todolist.CustomWebMvcConfig;
import tom.demo.todolist.config.BeanConfig;
import tom.demo.todolist.domain.Role;
import tom.demo.todolist.service.RoleService;

@WebMvcTest(RoleController.class)
@Import({ CustomWebMvcConfig.class, BeanConfig.class })
public class RoleControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RoleService roleService;

	@BeforeEach
	public void setup() {
		List<Role> roles = new ArrayList<>();
		Role admin = new Role();
		admin.setId(1L);
		admin.setRole("ADMIN");
		Role user = new Role();
		user.setId(2L);
		user.setRole("USER");
		roles.add(admin);
		roles.add(user);

		Mockito.when(roleService.getAllRoles()).thenReturn(roles);
	}

	@Test
	@WithUserDetails("admin")
	public void getAllUserTest() throws Exception {
		this.mockMvc
				.perform(get("/role/list"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().json("[{'roleId':1,'role':'ADMIN'},{'roleId':2,'role':'USER'}]"));
	}
}
