package tom.demo.todolist.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import tom.demo.todolist.domain.Item;
import tom.demo.todolist.util.QueryConstants;

@DataJpaTest
public class ItemDAOTest {

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Autowired
	ItemDAO itemDAO;

	@Test
	public void findByIdTest() {
		Item item = itemDAO.findById(1L).orElse(null);
		assertNotNull(item);
		assertEquals("dishes", item.getName());
		assertEquals("do dishes", item.getDescription());
		assertEquals(1, item.getSortOrder());
		assertEquals(true, item.isDone());
		assertEquals(LocalDateTime.parse("2022-01-20 10:00:00", formatter), item.getDeadline());
		assertEquals(1L, item.getListId());

		item = itemDAO.findById(5L).orElse(null);
		assertNotNull(item);
		assertEquals("solve bug", item.getName());
		assertEquals("debugging...", item.getDescription());
		assertEquals(1, item.getSortOrder());
		assertEquals(false, item.isDone());
		assertEquals(LocalDateTime.parse("2022-03-03 10:00:00", formatter), item.getDeadline());
		assertEquals(3L, item.getListId());
	}

	@Test
	public void findMaxSortOrderByListIdTest() {
		Integer num = itemDAO.findMaxSortOrderByListId(1L);
		assertEquals(4, num);

		num = itemDAO.findMaxSortOrderByListId(2L);
		assertNull(num);

		num = itemDAO.findMaxSortOrderByListId(3L);
		assertEquals(3, num);

		num = itemDAO.findMaxSortOrderByListId(4L);
		assertNull(num);
	}

	@Test
	public void findByListIdTest() {
		List<Item> items = itemDAO.findByListId(1L, QueryConstants.SORT_ORDER, QueryConstants.ASC, null);
		assertEquals(4, items.size());
		assertEquals(Arrays.asList(new int[] { 1, 2, 3, 4 }),
				items.stream().map(Item::getSortOrder).collect(Collectors.toList()));

		items = itemDAO.findByListId(1L, QueryConstants.DEADLINE, QueryConstants.DESC, null);
		assertEquals(4, items.size());
		assertEquals(Arrays.asList(new int[] { 4, 3, 2, 1 }),
				items.stream().map(Item::getSortOrder).collect(Collectors.toList()));

		items = itemDAO.findByListId(3L, QueryConstants.SORT_ORDER, QueryConstants.ASC, null);
		assertEquals(3, items.size());
		assertEquals(Arrays.asList(new int[] { 1, 2, 3 }),
				items.stream().map(Item::getSortOrder).collect(Collectors.toList()));

		items = itemDAO.findByListId(3L, QueryConstants.DEADLINE, QueryConstants.ASC, null);
		assertEquals(3, items.size());
		assertEquals(Arrays.asList(new int[] { 2, 1, 3 }),
				items.stream().map(Item::getSortOrder).collect(Collectors.toList()));
	}
}
