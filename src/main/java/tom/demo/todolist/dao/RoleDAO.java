package tom.demo.todolist.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tom.demo.todolist.domain.Role;

@Repository
public interface RoleDAO extends JpaRepository<Role, Long> {
	Role findByRole(String role);
}
