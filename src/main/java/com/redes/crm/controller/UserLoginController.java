package com.redes.crm.controller;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redes.crm.helpers.GetTokenFormat;
import com.redes.crm.helpers.HashPassword;
import com.redes.crm.helpers.Response;
import com.redes.crm.helpers.TokenGenerate;
import com.redes.crm.model.User;
import com.redes.crm.repository.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/user")
public class UserLoginController {
	private HashPassword hashPassword;
	
    @Autowired
    private UserRepository userRepository;
    
    public UserLoginController(UserRepository userRepository, HashPassword hashPassword) {
        this.userRepository = userRepository;
        this.hashPassword = hashPassword;
    }

//Verificar o a geração do token, deve estar sendo criado com a senha junto
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user) {
        
    	System.out.println("Email => " + user.getEmail());
    	System.out.println("Email => " + user.getPassword());

    	Optional<User> userExist = userRepository.findByEmail(user.getEmail());
    	
    	if(!userExist.isPresent()) {
    		Response response = new Response(true, "Email ou senha incorretos");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    	}
    	
    	User existingUser = userExist.get();    	
    	
    	Boolean passwordMatch = hashPassword.matchPassword(user.getPassword(), existingUser.getPassword());
    	
    	if(passwordMatch == false) {
    		Response response = new Response(true, "Email ou senha incorretos");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    	} 
    	
    	try {
    		TokenGenerate token = new TokenGenerate();
    		
    		String responseToken = token.generateToken(userExist.get());
    		
            Response response = new Response(false, responseToken);
    		
    		return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (DataIntegrityViolationException e) {
        	System.out.println("error => " + e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); 
        } catch (Exception e) {
        	System.out.println("error => " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody @Valid User user) {

        
    	Optional<User> userExist = userRepository.findByEmail(user.getEmail());
    	
    	if(userExist.isPresent()) {
    		Response response = new Response(true, "Email já está em uso");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    	}

        try {
        	TokenGenerate token = new TokenGenerate();

        	String hash = hashPassword.encodePassword(user.getPassword());
        	
        	user.setPassword(hash);
        	
            User newUser = userRepository.save(user);
            
            String responseToken = token.generateToken(newUser);
            
            Response response = new Response(false, responseToken);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DataIntegrityViolationException e) {
        	System.out.println("error => " + e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); 
        } catch (Exception e) {
        	System.out.println("error => " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Transactional
	@PutMapping("/update")
	public ResponseEntity<Object> update(@RequestBody @Valid User user, @RequestHeader("Authorization") String token) {
		GetTokenFormat getTokenFormat = new GetTokenFormat();

		String existToken = getTokenFormat.cutToken(token);
		
		TokenGenerate tokenGenerate = new TokenGenerate();
		
		boolean isValidTokenIntegrity = tokenGenerate.validateTokenIntegrity(existToken);
		
		if(isValidTokenIntegrity == false) {
			Response response = new Response(true, "Token Inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		Long userId = tokenGenerate.extractUserId(existToken);
		
		Optional<User> userFind = userRepository.findById(userId);
		
		boolean isValidTokenParams = tokenGenerate.validateTokenParams(existToken, userFind.get());
		
		if(existToken == "Not found" || isValidTokenParams == false) {
			Response response = new Response(true, "Token Inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		//Fazer o updaloa de imagem
//	    File uploadDir = new File(UPLOAD_DIR);
//	    if (!uploadDir.exists()) {
//	    	uploadDir.mkdirs();
//	    }
//	
//	    String fileName = file.getOriginalFilename();
//	
//	    Path destPath = Paths.get(UPLOAD_DIR + File.separator + fileName);
//	
//	    Files.copy(file.getInputStream(), destPath);
		// ------------------------
		
//		try {
        	String hash = hashPassword.encodePassword(user.getPassword());
        	
        	userRepository.updateUser(userId, user.getName(), hash, user.getImageName());
        	
        	Response response = new Response(false, "Atualizado com sucesso");
        	return ResponseEntity.status(HttpStatus.OK).body(response);		
//		} catch (DataIntegrityViolationException e) {
//        	System.out.println("error => " + e);
//            return ResponseEntity.status(HttpStatus.CONFLICT).build(); 
//        } catch (Exception e) {
//        	System.out.println("error => " + e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
		
//		return ResponseEntity.status(HttpStatus.CREATED).body("ok");
	}
}

