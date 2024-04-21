package com.redes.crm.dto;

import java.util.Date;

import com.redes.crm.model.Conversation;

public interface ConversationDto {
	Long getConversationId();
    Long getUserId();
    Date getCreatedAt();
}
