package com.redes.crm.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.redes.crm.dto.FindAllmessagesOfConversationDto;
import com.redes.crm.model.Chat;
import com.redes.crm.model.User;

public interface ChatRepository extends JpaRepository<Chat, Long>{
	Optional<Chat> findFirstBySenderIdAndRecipientId(@Param("sender") User sender, @Param("recipient") User recipient);
	
	@Query(nativeQuery = true, value = "SELECT chat.id AS id, chat.message AS message, chat.conversation_id AS conversationId, chat.recipient_id AS recipientId, " 
			+ "chat.sender_id AS senderId, " 
			+ "chat.created_at AS createdAt, conversation.id AS conversationId, conversation.created_at AS conversationCreatedAt FROM javinha.chat AS chat " 
			+ "INNER JOIN javinha.conversation AS conversation ON chat.conversation_id = conversation.id " 
			+ "WHERE chat.conversation_id = :conversationId " 
			+ "ORDER BY chat.created_at ASC")
	List<FindAllmessagesOfConversationDto> findAllmessagesOfConversation(@Param("conversationId") Long conversationId);
}