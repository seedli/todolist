package tom.demo.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import tom.demo.todolist.domain.User;
import tom.demo.todolist.service.UserService;

@Api(tags = "User API")
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/{id}")
	public String getNameById(@PathVariable("id") Long id) {
		User u = userService.findById(id).orElse(null);

		return u == null ? "Not Found" : u.getName();
	}
}
