package tom.demo.todolist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import tom.demo.todolist.dao.RoleDAO;
import tom.demo.todolist.domain.Role;
import tom.demo.todolist.service.RoleService;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class RoleServiceImplTest {

	@MockBean
	RoleDAO roleDAO;

	@Autowired
	RoleService roleService;

	@BeforeEach
	public void beforeEach() {
		List<Role> roles = new ArrayList<>();

		Role role1 = new Role();
		role1.setId(1L);
		role1.setRole("ADMIN");

		Role role2 = new Role();
		role2.setId(2L);
		role2.setRole("USER");

		roles.add(role1);
		roles.add(role2);

		Mockito.when(roleDAO.findAll()).thenReturn(roles);
	}

	@Test
	public void getAllRolesTest() {
		assertEquals(2, roleService.getAllRoles().size());
	}

}
