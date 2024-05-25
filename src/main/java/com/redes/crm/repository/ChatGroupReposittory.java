package com.redes.crm.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.redes.crm.dto.GetMembersDto;
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
	
	@Query(nativeQuery = true, value = "SELECT chatUser.conversation_id as 'conversationId', chat.message as 'lastMessage', chat.id AS 'chatId', chat.visualize AS 'visualize', " 
			+"chat.sender_id as 'senderId', sender.id as 'userIdByRecipientId', sender.image_name as 'recipientImageName', sender.name AS 'senderName', chatGroup.title AS 'groupName' "
			+ "FROM javinha.chat_user chatUser "
			+ "INNER JOIN javinha.conversation conversation ON conversation.id = chatUser.conversation_id "
			+ "LEFT JOIN javinha.chat_group chatGroup ON chatGroup.conversation_id = conversation.id "
			+ "LEFT JOIN "
			+ "    (SELECT * FROM javinha.chat c1 "
			+ "     WHERE c1.created_at = (SELECT MAX(c2.created_at) "
			+ "                            FROM javinha.chat c2 "
			+ "                            WHERE c2.conversation_id = c1.conversation_id) "
			+ "    ) chat ON chat.conversation_id = conversation.id "
			+ "LEFT JOIN javinha.user sender ON chat.sender_id = sender.id "
			+ "WHERE chatUser.user_id = :userId")
	List<FindGroupUserDto> findGroupUser(@Param("userId") Long userId);
	
	@Query(nativeQuery = true, value = "SELECT chatUser.id AS 'chatUserId', user.id AS 'userId', user.name AS 'name', user.image_name AS 'userImageName' FROM javinha.chat_user chatUser "
			+ "INNER JOIN javinha.user user ON user.id = chatUser.user_id "
			+ "WHERE chatUser.conversation_id = :conversationId")
	List<GetMembersDto> getMembers(@Param("conversationId") Long conversationId);
}
