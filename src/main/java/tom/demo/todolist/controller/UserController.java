package tom.demo.todolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import tom.demo.todolist.adapter.UserJsonAdapter;
import tom.demo.todolist.controller.json.UserJson;
import tom.demo.todolist.service.UserService;

@Api(tags = "User API")
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping()
	public Long createUser(@RequestBody UserJson userJson) {
		return userService.createUser(userJson);
	}

	@PutMapping()
	public Long updateUser(@RequestBody UserJson userJson) {
		return userService.updateUser(userJson);
	}

	@DeleteMapping("/{userId}")
	public Long deleteUser(@PathVariable("userId") Long userId) {
		return userService.deleteUser(userId);
	}

	@PutMapping("/password")
	public Long updateUserPassword(@RequestBody UserJson userJson) {
		return userService.updatePassword(userJson);
	}

	@GetMapping("/{userId}")
	public UserJson getUserById(@PathVariable("userId") Long userId) {
		return new UserJsonAdapter().convertDomainToJSON(userService.findById(userId));
	}

	@GetMapping("/list")
	public List<UserJson> getAllUser() {
		return (List<UserJson>) new UserJsonAdapter().convertDomainToJSON(userService.findAll());
	}

}
