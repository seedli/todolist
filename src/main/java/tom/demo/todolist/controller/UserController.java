package tom.demo.todolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import tom.demo.todolist.adapter.UserJsonAdapter;
import tom.demo.todolist.controller.json.UserJson;
import tom.demo.todolist.service.UserService;
import tom.demo.todolist.util.UserUtilities;

@Api(tags = "User API")
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public Long createUser(@RequestBody UserJson userJson) {
		if (UserUtilities.isAdmin())
			return userService.createUser(userJson);
		else
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	}

	@PutMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public Long updateUser(@RequestBody UserJson userJson) {
		if (UserUtilities.isAdmin())
			return userService.updateUser(userJson);
		else
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	}

	@PutMapping("/password")
	public Long updateUserPassword(@RequestBody UserJson userJson) {
		if (UserUtilities.isAdmin() || UserUtilities.isCurrentUser(userJson.getId()))
			return userService.updatePassword(userJson);
		else
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("/list")
	public List<UserJson> getAllUser() {
		return (List<UserJson>) new UserJsonAdapter().convertDomainToJSON(userService.findAll());
	}

}
