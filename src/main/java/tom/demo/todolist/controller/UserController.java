package tom.demo.todolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import tom.demo.todolist.adapter.UserJsonAdapter;
import tom.demo.todolist.controller.json.UserJson;
import tom.demo.todolist.domain.User;
import tom.demo.todolist.service.UserService;
import tom.demo.todolist.util.UserUtils;

@Api(tags = "User API")
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping()
	public Long createUser(@RequestBody UserJson userJson) {
		if (UserUtils.isAdmin() || userJson.getName().equals(UserUtils.getCurrentUserName()))
			return userService.saveUser(userJson);
		else
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Not Found.");
	}

	@GetMapping("/list")
	public List<UserJson> getAllUser() {
		return (List<UserJson>) new UserJsonAdapter().convertDomainToJSON(userService.findAll());
	}

	@GetMapping("/{id}")
	public String getNameById(@PathVariable("id") Long id) {
		User u = userService.findById(id);

		if (u != null)
			return u.getName();
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found.");

	}
}
