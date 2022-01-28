package tom.demo.todolist;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import tom.demo.todolist.util.CustomUser;

public class CustomInMemoryUserDetailsManager implements UserDetailsManager {

	private final Map<String, CustomUser> users = new HashMap<>();

	public CustomInMemoryUserDetailsManager(Collection<UserDetails> users) {
		for (UserDetails user : users) {
			createUser(user);
		}
	}

	@Override
	public void createUser(UserDetails user) {
		users.put(user.getUsername().toLowerCase(), (CustomUser) user);
	}

	@Override
	public void updateUser(UserDetails user) {
		users.put(user.getUsername().toLowerCase(), (CustomUser) user);
	}

	@Override
	public void deleteUser(String username) {
		users.remove(username.toLowerCase());
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean userExists(String username) {
		return users.containsKey(username.toLowerCase());
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		CustomUser user = users.get(username.toLowerCase());

		if (user == null) {
			throw new UsernameNotFoundException(username);
		}

		return user;
	}
}
