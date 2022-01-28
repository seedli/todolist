package tom.demo.todolist.adapter;

import tom.demo.todolist.controller.json.ListJson;
import tom.demo.todolist.domain.TodoList;

public class ListJsonAdapter implements JsonAdapter<TodoList, ListJson> {

	@Override
	public ListJson convertDomainToJSON(TodoList todoList) {
		ListJson json = new ListJson();

		json.setId(todoList.getId());
		json.setName(todoList.getName());
		json.setUserId(todoList.getUserId());

		return json;
	}

}
