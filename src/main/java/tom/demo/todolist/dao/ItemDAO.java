package tom.demo.todolist.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tom.demo.todolist.domain.Item;

@Repository
public interface ItemDAO extends JpaRepository<Item, Long>, ItemDAOCustom {

	@Query(value = "SELECT max(i.sortOrder) FROM Item i where i.listId = ?1")
	public Integer findMaxSortOrderByListId(Long listId);

	public List<Item> findByListIdOrderBySortOrderAsc(Long listId);
}
