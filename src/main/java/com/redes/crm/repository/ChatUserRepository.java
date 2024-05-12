package com.redes.crm.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.redes.crm.dto.FindChatUserByConversationDto;
import com.redes.crm.dto.FindUserGroupDto;
import com.redes.crm.dto.UserIsPresentGroupDto;
import com.redes.crm.model.ChatUser;


public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
	//Toda busca retorna o pasword do user, ver depois como retirar isso
	@Query("SELECT cu FROM ChatUser cu WHERE cu.userId.id = :userId")
    Optional<ChatUser> findByUserId(@Param("userId") Long userId);
	
	@Query(nativeQuery = true, value = "SELECT chatUser.user_id as 'userId' FROM javinha.chat_user chatUser "
			+ "INNER JOIN javinha.conversation conversation ON chatUser.conversation_id = conversation.id "
			+ "WHERE chatUser.conversation_id = :conversationId")
	List<FindChatUserByConversationDto> findChatUserByConversation(@Param("conversationId") Long conversationId);
	
	@Query(nativeQuery = true, value = "SELECT chatGroup.id AS 'chatGroupId' ,chatGroup.conversation_id AS 'conversationId', chatUser.user_id AS 'userId' FROM javinha.chat_user chatUser "
			+ "INNER JOIN javinha.chat_group chatGroup ON chatGroup.conversation_id = chatUser.conversation_id "
			+ "WHERE chatUser.user_id = :userId AND chatUser.conversation_id = :conversationId AND chatGroup.conversation_id = :conversationId")
	Optional<FindUserGroupDto> findUserGroup(@Param("userId") Long userId, @Param("conversationId") Long conversationId);
	
	@Query(nativeQuery = true, value = "SELECT * FROM javinha.chat_user chatUser "
			+ "WHERE chatUser.user_id = :userId AND chatUser.conversation_id = :conversationId")
	Optional <UserIsPresentGroupDto> userIsPresentGroup(@Param("userId") Long userId, @Param("conversationId") Long conversationId);
}
