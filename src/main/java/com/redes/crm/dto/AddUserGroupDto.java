package com.redes.crm.dto;

import java.io.Serializable;

public class AddUserGroupDto implements Serializable {
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
