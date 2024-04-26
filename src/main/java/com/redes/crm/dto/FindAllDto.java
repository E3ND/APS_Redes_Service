package com.redes.crm.dto;

import java.util.Date;

public interface FindAllDto {
	Long getId();
	String getEmail();
	String getName();
	String getImageName();
	Date getCreatedAt();
}
