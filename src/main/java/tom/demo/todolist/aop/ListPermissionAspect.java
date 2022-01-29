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
import tom.demo.todolist.controller.json.ListJson;
import tom.demo.todolist.service.RbacService;
import tom.demo.todolist.util.UserUtilities;

@Component
@Aspect
public class ListPermissionAspect {

	@Autowired
	RbacService rbacService;

	@Pointcut("execution(* tom.demo.todolist.service.ListService.createList(..))")
	public void createListPC() {
	}

	@Pointcut("execution(* tom.demo.todolist.service.ListService.updateList(..))")
	public void updateListPC() {
	}

	@Pointcut("execution(* tom.demo.todolist.service.ListService.deleteList(..)) || "
			+ "execution(* tom.demo.todolist.service.ListService.shareListToUser(..)) ||"
			+ "execution(* tom.demo.todolist.service.ListService.revokeSharedListFromUser(..)) ||"
			+ "execution(* tom.demo.todolist.service.ItemService.getItemsByListId(..))")
	public void methodsWithListIdPC() {
	}

	@Pointcut("execution(* tom.demo.todolist.service.ItemService.addItem(..)) ||"
			+ "execution(* tom.demo.todolist.service.ItemService.updateItem(..))")
	public void methodsWithItemJsonPC() {
	}

	@Before("createListPC()")
	public void beforeCreateList(JoinPoint joinPoint) {
		ListJson listJson = (ListJson) joinPoint.getArgs()[0];

		if (!UserUtilities.isAdmin() && !UserUtilities.isCurrentUser(listJson.getUserId()))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

	}

	@Before("updateListPC()")
	public void beforeUpdateListPC(JoinPoint joinPoint) {
		ListJson listJson = (ListJson) joinPoint.getArgs()[0];
		checkPermissionOfList(listJson.getId());
	}

	@Before("methodsWithListIdPC()")
	public void beforeMethodsWithListId(JoinPoint joinPoint) {
		Long listId = (Long) joinPoint.getArgs()[0];
		checkPermissionOfList(listId);
	}

	@Before("methodsWithItemJsonPC()")
	public void beforeMethodsWithItemJson(JoinPoint joinPoint) {
		ItemJson itemJson = (ItemJson) joinPoint.getArgs()[0];
		checkPermissionOfList(itemJson.getListId());
	}

	private void checkPermissionOfList(Long listId) {
		if (!UserUtilities.isAdmin() && !rbacService.hasPermissionOfList(UserUtilities.getCurrentUserId(), listId))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

	}
}
