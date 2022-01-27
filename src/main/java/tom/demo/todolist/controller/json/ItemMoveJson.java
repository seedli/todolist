package tom.demo.todolist.controller.json;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ItemMoveJson {

	private Long itemId;

	private Long targetListId;

	private Integer sortOrder;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getTargetListId() {
		return targetListId;
	}

	public void setTargetListId(Long targetListId) {
		this.targetListId = targetListId;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}
