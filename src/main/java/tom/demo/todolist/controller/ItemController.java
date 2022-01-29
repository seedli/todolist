package tom.demo.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import tom.demo.todolist.adapter.ItemJsonAdapter;
import tom.demo.todolist.controller.json.ItemJson;
import tom.demo.todolist.controller.json.ItemMoveJson;
import tom.demo.todolist.service.ItemService;
import tom.demo.todolist.service.ListService;

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
		return itemService.addItem(json);
	}

	@PutMapping
	public ItemJson updateItem(@RequestBody ItemJson json) {
		return itemService.updateItem(json);
	}

	@DeleteMapping("/{itemId}")
	public Long deleteItem(@PathVariable("itemId") Long itemId) {
		return itemService.deleteItem(itemId);
	}

	@PutMapping("/move")
	public ItemJson moveItem(@RequestBody ItemMoveJson json) {
		return new ItemJsonAdapter().convertDomainToJSON(itemService.moveItem(json));
	}

	@PutMapping("/check/{itemId}")
	public ItemJson checkItem(@PathVariable("itemId") Long itemId) {
		return new ItemJsonAdapter().convertDomainToJSON(itemService.checkItem(itemId));
	}
}
