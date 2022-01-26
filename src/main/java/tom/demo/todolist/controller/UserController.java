package tom.demo.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import tom.demo.todolist.controller.json.UserJson;
import tom.demo.todolist.domain.User;
import tom.demo.todolist.service.UserService;

@Api(tags = "User API")
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/")
	public Long createUser(@RequestBody UserJson userJson) {
		return userService.saveUser(userJson);
	}

	@GetMapping("/{id}")
	public String getNameById(@PathVariable("id") Long id) {
		User u = userService.findById(id);
		return u == null ? "Not Found" : u.getName();
	}
}
