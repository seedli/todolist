package tom.demo.todolist.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import tom.demo.todolist.domain.User;

@DataJpaTest
public class UserDAOImplTest {

	@Autowired
	private UserDAO userDAO;

	@Test
	public void testFindById() {
		User user = userDAO.findById(1L).orElse(null);

		assertNotNull(user);
		assertEquals(1L, user.getId());
		assertEquals("admin", user.getName());
	}
}
