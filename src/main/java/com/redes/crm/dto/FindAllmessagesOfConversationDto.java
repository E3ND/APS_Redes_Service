package com.redes.crm.dto;

import java.util.Date;

public interface FindAllmessagesOfConversationDto {
	Long getId();
	String getMessage();
	String getImageName();
	Long getConversationId();
	Long getRecipientId();
	Long getSenderId();
	Date getCreatedAt();
	Date getConversationCreatedAt();
}
