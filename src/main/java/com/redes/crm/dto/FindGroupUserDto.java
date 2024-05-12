package com.redes.crm.dto;

public interface FindGroupUserDto {
	Long getConversationId();
	String getLastMessage();
	Long getChatId();
	Boolean getVisualize();
	Long getSenderId();
	Long getUserIdByRecipientId();
	String getRecipientImageName();
	String getSenderName();
}
