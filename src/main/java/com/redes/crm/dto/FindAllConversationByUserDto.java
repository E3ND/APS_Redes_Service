package com.redes.crm.dto;

public interface FindAllConversationByUserDto {
	Long getConversationId();
	String getLastMessage();
	Long getSenderId();
	Long getUserIdByRecipientId();
	String getRecipientImageName();
	String getSenderName();
	String getRecipientName();
	Boolean getVisualize();
	Long getChatId();
	String getUserEmail();
	String getSenderImageName();
}
