package tom.demo.todolist.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import tom.demo.todolist.domain.TodoList;

@DataJpaTest
public class ListDAOTest {

	@Autowired
	ListDAO listDAO;

	@Test
	public void findByIdTest() {
		TodoList list = listDAO.findById(1L).orElse(null);
		assertNotNull(list);
		assertEquals("admin list 1", list.getName());
		assertEquals(1L, list.getUserId());

		list = listDAO.findById(3L).orElse(null);
		assertNotNull(list);
		assertEquals("tom list 1", list.getName());
		assertEquals(2L, list.getUserId());
	}

	@Test
	public void findAllTest() {
		List<TodoList> lists = listDAO.findAll();
		assertNotNull(lists);
		assertEquals(4, lists.size());
	}

	@Test
	public void findByUserIdOrderByIdAscTest() {
		List<TodoList> lists = listDAO.findByUserIdOrderByIdAsc(1L);
		assertNotNull(lists);
		assertEquals(2, lists.size());
		assertEquals(1L, lists.get(0).getId());
		assertNotNull(lists.get(0).getItems());
		assertEquals(4, lists.get(0).getItems().size());

		lists = listDAO.findByUserIdOrderByIdAsc(2L);
		assertNotNull(lists);
		assertEquals(2, lists.size());
		assertEquals(3L, lists.get(0).getId());
		assertNotNull(lists.get(0).getItems());
		assertEquals(3, lists.get(0).getItems().size());
	}
}
