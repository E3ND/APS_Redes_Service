package com.redes.crm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.redes.crm.dto.FindAllConversationByUserDto;
import com.redes.crm.dto.FindAllConversationsDto;
import com.redes.crm.dto.FindByUserIdAndRecipientIdDto;
import com.redes.crm.model.Conversation;


public interface ConversationRepository extends JpaRepository<Conversation, Long> {
	@Query(nativeQuery = true, value = "SELECT conversation.id as 'conversationId', chatUser.user_id as 'userId', chatUser.id, chat.recipient_id as 'recipientId', conversation.created_at as 'createdAt' "
		    + "FROM Conversation conversation "
		    + "INNER JOIN Chat_user chatUser ON conversation.id = chatUser.conversation_id "
		    + "INNER JOIN Chat chat on chat.conversation_id = chatUser.conversation_id "
		    + "WHERE chatUser.user_id = :userId OR chatUser.user_id = :recipientId")
	List<FindByUserIdAndRecipientIdDto> findByUserIdAndRecipientId(@Param("userId") Long userId, @Param("recipientId") Long recipientId);
	
	@Query(nativeQuery = true, value = "SELECT * FROM Conversation conversation WHERE conversation.id = :id")
	Conversation findConversationById(@Param("id") Long id);
	
	@Query(nativeQuery = true, value = "SELECT conversation.id as 'conversationId', chatUser.user_id as 'userId', chat.recipient_id as 'recipientId', chat.sender_id as 'senderId' " 
			+ "FROM javinha.chat_user chatUser "
			+ "INNER JOIN javinha.conversation conversation ON javinha.conversation.id = chatUser.conversation_id "
			+ "INNER JOIN javinha.chat chat ON javinha.chat.conversation_id = javinha.conversation.id "
			+ "INNER JOIN javinha.user user ON chat.sender_id = user.id "
			+ "WHERE chatUser.user_id = :id")
	List<FindAllConversationsDto> findAllConversations(@Param("id") Long id);
	
	@Query(nativeQuery = true, value = "SELECT chatUser.conversation_id as 'conversationId', chat.message as 'lastMessage', chat.id AS 'chatId', chat.visualize AS 'visualize', chat.recipient_id as 'recipientId', chat.sender_id as 'senderId', "
			+ "user.id as 'userIdByRecipientId', user.image_name as 'recipientImageName', sender.name AS 'senderName', recipient.name AS 'recipientName' "
			+ "FROM javinha.chat_user chatUser "
			+ "INNER JOIN javinha.conversation conversation ON conversation.id = chatUser.conversation_id "
			+ "INNER JOIN javinha.chat chat ON chat.conversation_id = conversation.id "
			+ "INNER JOIN javinha.user user ON chat.recipient_id = user.id "
			+ "INNER JOIN javinha.user sender ON chat.sender_id = sender.id "
			+ "INNER JOIN javinha.user recipient ON user.id = recipient.id "
			+ "WHERE chatUser.user_id = :userId "
			+ "AND chat.created_at = (SELECT MAX(c.created_at) FROM javinha.chat c WHERE c.conversation_id = chatUser.conversation_id)")
	List<FindAllConversationByUserDto> findAllConversationByUser(@Param("userId") Long userId);
}