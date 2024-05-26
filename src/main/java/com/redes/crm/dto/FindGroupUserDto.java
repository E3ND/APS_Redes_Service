package com.redes.crm.dto;

public interface FindGroupUserDto {
	Long getConversationId();
	String getLastMessage();
	Long getChatId();
	int getVisualize();
	Long getSenderId();
	Long getUserIdByRecipientId();
	String getRecipientImageName();
	String getSenderName();
	String getGroupName();
	String getGroupDescription();
}