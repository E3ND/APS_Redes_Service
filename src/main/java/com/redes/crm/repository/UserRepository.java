package com.redes.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.redes.crm.dto.FindAllDto;
import com.redes.crm.dto.FindUserByIdDto;
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

	@Query(nativeQuery = true, value = "SELECT user.id as 'id', user.email as 'email', user.name as 'name', user.image_name as 'imageName', " 
			+ "user.created_at as 'createdAt' FROM javinha.user user")
	List<FindAllDto> findAllUsers();
	
	@Query(nativeQuery = true, value = "SELECT u.id as 'id', u.name as 'name', u.email as 'email', u.image_name as 'imageName', u.created_at as 'createdAt' FROM javinha.user AS u WHERE u.id = :userId")
	List<FindUserByIdDto> findUserById(@Param("userId") Long userId);

}
