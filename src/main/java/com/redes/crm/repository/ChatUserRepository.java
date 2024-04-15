package com.redes.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.redes.crm.model.ChatUser;


public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
	//Toda busca retorna o pasword do user, ver depois como retirar isso
	@Query("SELECT cu FROM ChatUser cu WHERE cu.userId.id = :userId")
    Optional<ChatUser> findByUserId(@Param("userId") Long userId);
}
