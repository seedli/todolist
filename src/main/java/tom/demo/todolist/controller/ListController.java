package tom.demo.todolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import tom.demo.todolist.adapter.ListJsonAdapter;
import tom.demo.todolist.controller.json.ListJson;
import tom.demo.todolist.service.ListService;

@Api(tags = "List API")
@RestController
@RequestMapping("/list")
public class ListController {

	@Autowired
	ListService listService;

	@GetMapping("/myList")
	public List<ListJson> getMyList() {
		return (List<ListJson>) new ListJsonAdapter().convertDomainToJSON(listService.getMyList());
	}
}
