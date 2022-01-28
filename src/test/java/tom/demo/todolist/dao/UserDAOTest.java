package tom.demo.todolist.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import tom.demo.todolist.domain.User;

@DataJpaTest
public class UserDAOTest {

	@Autowired
	private UserDAO userDAO;

	@Test
	public void findByIdTest() {
		User user = userDAO.findById(1L).orElse(null);

		assertNotNull(user);
		assertEquals(1L, user.getId());
		assertEquals("admin", user.getName());

		user = userDAO.findById(2L).orElse(null);

		assertNotNull(user);
		assertEquals(2L, user.getId());
		assertEquals("tom", user.getName());
	}

	@Test
	public void findByNameTest() {
		User user = userDAO.findByName("admin");

		assertNotNull(user);
		assertEquals(1L, user.getId());
		assertEquals("admin", user.getName());

		user = userDAO.findByName("tom");

		assertNotNull(user);
		assertEquals(2L, user.getId());
		assertEquals("tom", user.getName());
	}

	@Test
	public void findAllTest() {
		List<User> users = userDAO.findAll();

		assertNotNull(users);
		assertEquals(3, users.size());
	}
}
