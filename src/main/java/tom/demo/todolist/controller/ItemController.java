package tom.demo.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import tom.demo.todolist.adapter.ItemJsonAdapter;
import tom.demo.todolist.controller.json.ItemJson;
import tom.demo.todolist.controller.json.ItemMoveJson;
import tom.demo.todolist.service.ItemService;
import tom.demo.todolist.service.ListService;
import tom.demo.todolist.util.UserUtilities;

@Api(tags = "Item API")
@RestController
@RequestMapping("/item")
public class ItemController {

	@Autowired
	ItemService itemService;

	@Autowired
	ListService listService;

	@PostMapping
	public Long addItem(@RequestBody ItemJson json) {
		if (UserUtilities.isAdmin()
				|| listService.hasPermissionOfList(UserUtilities.getCurrentUserId(), json.getListId()))
			return itemService.addItem(json);
		else
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	}

	@PutMapping
	public ItemJson updateItem(@RequestBody ItemJson json) {
		if (UserUtilities.isAdmin()
				|| listService.hasPermissionOfList(UserUtilities.getCurrentUserId(), json.getListId()))
			return new ItemJsonAdapter().convertDomainToJSON(itemService.updateItem(json));
		else
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	}

	@DeleteMapping("/{itemId}")
	public Long deleteItem(@PathVariable("itemId") Long itemId) {
		if (UserUtilities.isAdmin()
				|| itemService.hasPermissionOfItem(UserUtilities.getCurrentUserId(), itemId))
			return itemService.deleteItem(itemId);
		else
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	}

	@PutMapping("/move")
	public ItemJson moveItem(@RequestBody ItemMoveJson json) {
		if (UserUtilities.isAdmin()
				|| itemService.hasPermissionOfItem(UserUtilities.getCurrentUserId(), json.getItemId()))
			return new ItemJsonAdapter().convertDomainToJSON(itemService.moveItem(json));
		else
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	}

	@PutMapping("/check/{itemId}")
	public ItemJson checkItem(@PathVariable("itemId") Long itemId) {
		if (UserUtilities.isAdmin()
				|| itemService.hasPermissionOfItem(UserUtilities.getCurrentUserId(), itemId))
			return new ItemJsonAdapter().convertDomainToJSON(itemService.checkItem(itemId));
		else
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	}
}
