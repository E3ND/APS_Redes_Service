package com.redes.crm.dto;

import java.util.Date;

public interface FindUserByIdDto {
	Long getId();
	String getName();
	String getEmail();
	String getImageName();
	Date getCreatedAt();
}
