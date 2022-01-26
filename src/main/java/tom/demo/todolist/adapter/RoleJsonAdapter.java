package tom.demo.todolist.adapter;

import tom.demo.todolist.controller.json.RoleJson;
import tom.demo.todolist.domain.Role;

public class RoleJsonAdapter implements JsonAdapter<Role, RoleJson> {

	@Override
	public RoleJson convertDomainToJSON(Role role) {
		RoleJson roleJson = new RoleJson();

		roleJson.setRoleId(role.getId());
		roleJson.setRole(role.getRole());

		return roleJson;
	}

}
