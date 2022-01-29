package tom.demo.todolist.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import tom.demo.todolist.controller.json.ItemJson;
import tom.demo.todolist.service.RbacService;
import tom.demo.todolist.util.UserUtilities;

@Component
@Aspect
public class ItemPermissionAspect {

	@Autowired
	RbacService rbacService;

	@Pointcut("execution(* tom.demo.todolist.service.ItemService.moveItem(..))")
	public void methodsWithItemJsonPC() {
	}

	@Pointcut("execution(* tom.demo.todolist.service.ItemService.moveItem(..))")
	public void methodsWithItemIdPC() {
	}

	@Before("methodsWithItemJsonPC()")
	public void beforeMethodsWithItemJson(JoinPoint joinPoint) {
		ItemJson itemJson = (ItemJson) joinPoint.getArgs()[0];
		checkPermissionOfItem(itemJson.getId());
	}

	@Before("methodsWithItemIdPC()")
	public void beforeMethodsWithItemId(JoinPoint joinPoint) {
		Long itemId = (Long) joinPoint.getArgs()[0];
		checkPermissionOfItem(itemId);
	}

	private void checkPermissionOfItem(Long itemId) {
		if (!UserUtilities.isAdmin() && rbacService.hasPermissionOfItem(UserUtilities.getCurrentUserId(), itemId))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	}
}
