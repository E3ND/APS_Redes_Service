package com.redes.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.redes.crm.model.User;

//Entidade -> tipo do Id
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
//	Aqui fornece uma implementação em tempo de execução
	Optional<User> findByEmail(String string);
	
	Optional<User> findById(Long id);
	
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE javinha.user SET user.name = :name, user.password = :password, user.image_name = :imageName "
			+ "WHERE user.id = :id")
	void updateUser(@Param("id") Long id, @Param("name") String name, @Param("password") String password, @Param("imageName") String imageName);
}
