package com.redes.crm.dto;

public interface FindAllConversationByUserDto {
	Long getConversationId();
	String getLastMessage();
	Long getSenderId();
	Long getUserIdByRecipientId();
	String getRecipientImageName();
}
