package com.redes.crm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.redes.crm.dto.ConversationDto;
import com.redes.crm.model.Conversation;


public interface ConversationRepository extends JpaRepository<Conversation, Long> {
	@Query(nativeQuery = true, value = "SELECT conversation.id as 'conversationId', chatUser.user_id as 'userId', chatUser.id, conversation.created_at as 'createdAt' "
		    + "FROM Conversation conversation "
		    + "INNER JOIN Chat_user chatUser ON conversation.id = chatUser.conversation_id "
		    + "WHERE chatUser.user_id = :userId OR chatUser.user_id = :recipientId")
	List<ConversationDto> findByUserIdAndRecipientId(@Param("userId") Long userId, @Param("recipientId") Long recipientId);
	
	@Query(nativeQuery = true, value = "SELECT * FROM Conversation conversation WHERE conversation.id = :id")
	Conversation findConversationById(@Param("id") Long id);
}