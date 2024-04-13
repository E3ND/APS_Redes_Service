package com.redes.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.redes.crm.model.Conversation;


public interface ConversationRepository extends JpaRepository<Conversation, Long> {
	
}
