package tom.demo.todolist.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import tom.demo.todolist.domain.Item;
import tom.demo.todolist.util.QueryConstants;

public class ItemDAOCustomImpl implements ItemDAOCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Item> findByListId(Long listId, String orderBy, String sort, String status) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Item> query = cb.createQuery(Item.class);

		Root<Item> item = query.from(Item.class);

		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(item.get("listId"), listId));
		if (QueryConstants.EXPIRED.equalsIgnoreCase(status))
			predicates.add(cb.lessThan(item.get("deadline"), LocalDateTime.now()));
		else if (QueryConstants.UNEXPIRED.equalsIgnoreCase(status))
			predicates.add(cb.greaterThan(item.get("deadline"), LocalDateTime.now()));

		query.where(predicates.toArray(new Predicate[] {}));

		if (QueryConstants.DESC.equalsIgnoreCase(sort))
			query.orderBy(cb.desc(item.get(orderBy)));
		else
			query.orderBy(cb.asc(item.get(orderBy)));

		return entityManager.createQuery(query).getResultList();
	}
}
