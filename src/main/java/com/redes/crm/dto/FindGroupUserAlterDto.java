package com.redes.crm.dto;

public class FindGroupUserAlterDto {
	private Long conversationId;
	
	private String lastMessage;
	
	private Long chatId;
	
	private Boolean visualize;
	
	private Long senderId;
	
	private Long userIdByRecipientId;
	
	private String recipientImageName;
	
	private String senderName;
	
	private String groupName;
	
	private String groupDescription;
	
	private String groupImage;

	public String getGroupImage() {
		return groupImage;
	}

	public void setGroupImage(String groupImage) {
		this.groupImage = groupImage;
	}

	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public Long getChatId() {
		return chatId;
	}

	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

	public Boolean getVisualize() {
		return visualize;
	}

	public void setVisualize(Boolean visualize) {
		this.visualize = visualize;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public Long getUserIdByRecipientId() {
		return userIdByRecipientId;
	}

	public void setUserIdByRecipientId(Long userIdByRecipientId) {
		this.userIdByRecipientId = userIdByRecipientId;
	}

	public String getRecipientImageName() {
		return recipientImageName;
	}

	public void setRecipientImageName(String recipientImageName) {
		this.recipientImageName = recipientImageName;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}
	
	
}
