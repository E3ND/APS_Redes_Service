package com.redes.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.redes.crm.model.Chat;
import com.redes.crm.model.Conversation;
import com.redes.crm.model.User;


public interface ConversationRepository extends JpaRepository<Conversation, Long> {
	
}
