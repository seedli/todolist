package tom.demo.todolist.adapter;

import tom.demo.todolist.controller.json.UserJson;
import tom.demo.todolist.domain.User;

public class UserJsonAdapter implements JsonAdapter<User, UserJson> {

	@Override
	public UserJson convertDomainToJSON(User user) {
		UserJson userJson = new UserJson();

		userJson.setId(user.getId());
		userJson.setName(user.getName());
		userJson.setRoleId(user.getRole().getId());

		return userJson;
	}

}
