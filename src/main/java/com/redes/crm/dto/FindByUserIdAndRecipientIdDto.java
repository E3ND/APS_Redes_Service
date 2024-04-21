package com.redes.crm.dto;

import java.util.Date;

import com.redes.crm.model.Conversation;

public interface FindByUserIdAndRecipientIdDto {
	Long getConversationId();
    Long getUserId();
    Long getRecipientId();
    Date getCreatedAt();
}
