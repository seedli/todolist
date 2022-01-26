package tom.demo.todolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import tom.demo.todolist.adapter.RoleJsonAdapter;
import tom.demo.todolist.controller.json.RoleJson;
import tom.demo.todolist.service.RoleService;

@Api(tags = "Role API")
@RestController
@RequestMapping("/role")
public class RoleController {

	@Autowired
	RoleService roleService;

	@GetMapping("/list")
	public List<RoleJson> getAllRoles() {
		return (List<RoleJson>) new RoleJsonAdapter().convertDomainToJSON(roleService.getAllRoles());
	}
}
