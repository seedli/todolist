package tom.demo.todolist.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import tom.demo.todolist.domain.Role;

@DataJpaTest
public class RoleDAOTest {

	@Autowired
	RoleDAO roleDAO;

	@Test
	public void findAllTest() {
		List<Role> roles = roleDAO.findAll();
		assertNotNull(roles);
		assertEquals(2, roles.size());
	}

	@Test
	public void findByIdTest() {
		Role role = roleDAO.findById(1L).orElse(null);
		assertNotNull(role);
		assertEquals("ADMIN", role.getRole());

		role = roleDAO.findById(2L).orElse(null);
		assertNotNull(role);
		assertEquals("USER", role.getRole());
	}

	@Test
	public void findByRoleTest() {
		Role role = roleDAO.findByRole("ADMIN");
		assertNotNull(role);
		assertEquals(1L, role.getId());

		role = roleDAO.findByRole("USER");
		assertNotNull(role);
		assertEquals(2L, role.getId());
	}
}
