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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import tom.demo.todolist.adapter.ItemJsonAdapter;
import tom.demo.todolist.adapter.ListJsonAdapter;
import tom.demo.todolist.controller.json.ItemJson;
import tom.demo.todolist.controller.json.ListJson;
import tom.demo.todolist.service.ItemService;
import tom.demo.todolist.service.ListService;
import tom.demo.todolist.util.UserUtilities;

@Api(tags = "List API")
@RestController
@RequestMapping("/list")
public class ListController {

	@Autowired
	ListService listService;

	@Autowired
	ItemService itemService;

	@GetMapping("/my")
	public List<ListJson> getMyList() {
		return (List<ListJson>) new ListJsonAdapter()
				.convertDomainToJSON(listService.getListsByUserId(UserUtilities.getCurrentUserId()));
	}

	@GetMapping("/sharedWithMe")
	public List<ListJson> getListSharedWithMe() {
		return (List<ListJson>) new ListJsonAdapter()
				.convertDomainToJSON(listService.getListSharedListsByUserId(UserUtilities.getCurrentUserId()));
	}

	@GetMapping("/{listId}")
	public List<ItemJson> getItemsByListId(
			@PathVariable("listId") Long listId,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "orderBy", required = false) String orderBy,
			@RequestParam(value = "sort", required = false) String sort) {

		return (List<ItemJson>) new ItemJsonAdapter()
				.convertDomainToJSON(itemService.getItemsByListId(listId, orderBy, sort, status));
	}

	@PostMapping
	public Long createList(@RequestBody ListJson listJson) {
		return listService.createList(listJson);
	}

	@DeleteMapping("/{listId}")
	public Long deleteList(@PathVariable("listId") Long listId) {
		return listService.deleteList(listId);
	}

	@PutMapping
	public ListJson updateList(@RequestBody ListJson listJson) {
		return listService.updateList(listJson);
	}

	@PutMapping("/share/{listId}/{userId}")
	public Long shareListToUser(@PathVariable("listId") Long listId, @PathVariable("userId") Long userId) {
		return listService.shareListToUser(listId, userId);
	}

	@PutMapping("/revoke/{listId}/{userId}")
	public Long revokeListFromUser(@PathVariable("listId") Long listId, @PathVariable("userId") Long userId) {
		return listService.revokeSharedListFromUser(listId, userId);
	}
}
