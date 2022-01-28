package tom.demo.todolist.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtilities {

	public static boolean isAdmin() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("ADMIN"));
	}

	public static CustomUser getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return (CustomUser) auth.getPrincipal();
	}

	public static boolean isCurrentUser(Long userId) {
		CustomUser customUser = getCurrentUser();
		return customUser == null ? false : customUser.getUserId() == userId;
	}

	public static Long getCurrentUserId() {
		return getCurrentUser().getUserId();
	}

}
