package com.redes.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.redes.crm.model.ChatUser;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

}
