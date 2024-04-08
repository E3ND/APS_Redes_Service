package com.redes.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.redes.crm.model.User;

//Entidade -> tipo do Id
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
//	Aqui fornece uma implementação em tempo de execução
	Optional<User> findByEmail(String string);
}
