package com.redes.crm.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.redes.crm.dto.FindGroupConversationDto;
import com.redes.crm.dto.FindGroupUserDto;

import com.redes.crm.model.ChatGroup;

public interface ChatGroupReposittory extends JpaRepository<ChatGroup, Long> {

	@Query(nativeQuery = true, value = "SELECT chatGroup.id AS 'id', chatGroup.owner AS 'owner' FROM javinha.chat_group chatGroup "
			+ "WHERE chatGroup.conversation_id = :conversationId")
	Optional <FindGroupConversationDto> findGroupConversation(@Param("conversationId") Long conversationId);
	
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM javinha.chat_user "
			+ "WHERE user_id = :userId AND conversation_id = :conversationId")
	void removeUserFromGroup(@Param("userId") Long userId, @Param("conversationId") Long conversationId);
	
	@Query(nativeQuery = true, value = "SELECT chatUser.conversation_id as 'conversationId', chat.message as 'lastMessage', chat.id AS 'chatId', chat.visualize AS 'visualize', chat.sender_id as 'senderId', "
			+ "sender.id as 'userIdByRecipientId', sender.image_name as 'recipientImageName', sender.name AS 'senderName' "
			+ "FROM javinha.chat_user chatUser "
			+ "INNER JOIN javinha.conversation conversation ON conversation.id = chatUser.conversation_id "
			+ "INNER JOIN javinha.chat_group chatGroup on chatGroup.conversation_id = conversation.id "
			+ "INNER JOIN javinha.chat chat ON chat.conversation_id = conversation.id "
			+ "INNER JOIN javinha.user sender ON chat.sender_id = sender.id "
			+ "WHERE chatUser.user_id = :userId "
			+ "AND chat.created_at = (SELECT MAX(c.created_at) FROM javinha.chat c WHERE c.conversation_id = chatUser.conversation_id)")
	List<FindGroupUserDto> findGroupUser(@Param("userId") Long userId);
}
