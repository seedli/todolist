package tom.demo.todolist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import tom.demo.todolist.util.CustomUser;

@TestConfiguration
public class CustomWebMvcConfig {
	@Bean
	@Primary
	public UserDetailsService userDetailsService() {
		Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority("USER"));
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
		User basicUser = new CustomUser("user", "1234", true, true, true, true, grantedAuthorities, 2L);

		roles = new HashSet<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority("ADMIN"));
		grantedAuthorities = new ArrayList<>(roles);
		User adminUser = new CustomUser("admin", "2345", true, true, true, true, grantedAuthorities, 1L);

		return new CustomInMemoryUserDetailsManager(Arrays.asList(
				adminUser, basicUser));
	}
}
