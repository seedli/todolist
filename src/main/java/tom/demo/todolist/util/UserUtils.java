package tom.demo.todolist.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {

	public static boolean isAdmin() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("ADMIN"));
	}

	public static String getCurrentUserName() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	}
}
