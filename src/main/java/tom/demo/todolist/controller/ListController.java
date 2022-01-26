package tom.demo.todolist.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import tom.demo.todolist.domain.TodoList;

@Api(tags = "List API")
@RestController
@RequestMapping("/list")
public class ListController {

	@GetMapping("/myList")
	public Set<TodoList> getMyList() {
		return null;
	}
}
