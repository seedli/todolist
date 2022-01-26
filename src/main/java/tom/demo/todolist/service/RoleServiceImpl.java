package tom.demo.todolist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tom.demo.todolist.dao.RoleDAO;
import tom.demo.todolist.domain.Role;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleDAO roleDAO;

	@Override
	public List<Role> getAllRoles() {
		return roleDAO.findAll();
	}

}
