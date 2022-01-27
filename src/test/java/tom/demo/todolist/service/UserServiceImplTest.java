package tom.demo.todolist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import tom.demo.todolist.controller.json.UserJson;
import tom.demo.todolist.dao.RoleDAO;
import tom.demo.todolist.dao.UserDAO;
import tom.demo.todolist.domain.Role;
import tom.demo.todolist.domain.User;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class UserServiceImplTest {

	@Autowired
	UserService userService;

	@MockBean
	UserDAO userDAO;

	@MockBean
	RoleDAO roleDAO;

	@MockBean
	PasswordEncoder passwordEncoder;

	@BeforeEach
	public void beforeEach() {
		User tom = new User();
		tom.setId(1L);
		tom.setName("tom");
		Role role = new Role();
		role.setId(1L);
		role.setRole("USER");
		tom.setRole(role);

		User jim = new User();
		jim.setId(2L);
		jim.setName("jim");
		jim.setRole(role);

		List<User> users = new ArrayList<>();
		users.add(tom);
		users.add(jim);

		Mockito.when(userDAO.findById(anyLong()))
				.thenReturn(Optional.of(tom));

		Mockito.when(userDAO.findByName(anyString()))
				.thenReturn(tom);

		Mockito.when(userDAO.save(tom))
				.thenReturn(tom);

		Mockito.when(userDAO.findAll())
				.thenReturn(users);

		Mockito.when(passwordEncoder.encode(anyString()))
				.thenReturn("newpassword");
	}

	@Test
	public void findByIdTest() {
		User user = userService.findById(1L);
		assertEquals(1L, user.getId());
	}

	@Test
	public void findUserByNameTest() {
		User user = userService.findUserByName("tom");
		assertEquals(1L, user.getId());
	}

	@Test
	public void updateUserTest() {
		UserJson json = new UserJson();
		json.setId(1L);
		json.setName("jim");

		assertEquals(1L, userService.updateUser(json));
	}

	@Test
	public void updatePasswordTest() {
		UserJson json = new UserJson();
		json.setId(1L);
		json.setName("tom");
		json.setPassword("abc");

		assertEquals(1L, userService.updatePassword(json));
	}

	@Test
	public void findAllTest() {
		assertEquals(2, userService.findAll().size());
	}

}
