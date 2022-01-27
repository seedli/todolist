package tom.demo.todolist.adapter;

import tom.demo.todolist.controller.json.ItemJson;
import tom.demo.todolist.domain.Item;

public class ItemJsonAdapter implements JsonAdapter<Item, ItemJson> {

	@Override
	public ItemJson convertDomainToJSON(Item item) {
		ItemJson json = new ItemJson();

		json.setId(item.getId());
		json.setDescription(item.getDescription());
		json.setName(item.getName());
		json.setSortOrder(item.getSortOrder());
		json.setListId(item.getListId());
		json.setDeadline(item.getDeadline());
		json.setDone(item.isDone());

		return json;
	}

}
