package tom.demo.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(tags = "Item API")
@RestController
@RequestMapping("/item")
public class ItemController {

	@GetMapping("/hello/{name}")
	public String hello(@PathVariable String name) {
		return "Hello " + name;
	}
}
