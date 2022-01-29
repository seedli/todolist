package tom.demo.todolist.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import tom.demo.todolist.controller.json.UserJson;
import tom.demo.todolist.util.UserUtilities;

@Component
@Aspect
public class UserPermissionAspect {

	@Pointcut("execution(* tom.demo.todolist.service.UserService.updatePassword(..))")
	public void updatePasswordPc() {
	}

	@Before("updatePasswordPc()")
	public void beforeUpdatePassword(JoinPoint joinPoint) {
		UserJson userJson = (UserJson) joinPoint.getArgs()[0];

		if (!UserUtilities.isAdmin() && !UserUtilities.isCurrentUser(userJson.getId()))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

	}
}
