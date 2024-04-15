package com.redes.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.redes.crm.model.Chat;
import com.redes.crm.model.User;

public interface ChatRepository extends JpaRepository<Chat, Long>{
	Optional<Chat> findFirstBySenderIdAndRecipientId(@Param("sender") User sender, @Param("recipient") User recipient);
	

}
