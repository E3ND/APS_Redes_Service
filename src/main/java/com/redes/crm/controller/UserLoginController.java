package com.redes.crm.controller;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.redes.crm.dto.FindAllDto;
import com.redes.crm.dto.FindUserByIdDto;
import com.redes.crm.dto.UpdateUserDto;
import com.redes.crm.helpers.GenerateObjUser;
import com.redes.crm.helpers.GetTokenFormat;
import com.redes.crm.helpers.HashPassword;
import com.redes.crm.helpers.Response;
import com.redes.crm.helpers.TokenGenerate;
import com.redes.crm.model.Chat;
import com.redes.crm.model.User;
import com.redes.crm.repository.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;



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
    
    @GetMapping("/{id}")
    public ResponseEntity<Object> FindUserById (@PathVariable Long id, @RequestHeader("Authorization") String token) {
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
    	
    	List<FindUserByIdDto> User = userRepository.findUserById(id); 
		
		Response response = new Response(false, User);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader("Authorization") String token) {
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
		
		List<FindAllDto> allUsers = userRepository.findAllUsers(); 
		
		 Response response = new Response(false, allUsers);
 		
 		return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//Verificar o a geração do token, deve estar sendo criado com a senha junto
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user) {

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
    		
    		GenerateObjUser cenerateObjUser = new GenerateObjUser(existingUser.getId(), existingUser.getName(), existingUser.getImageName(), responseToken);
    		
            Response response = new Response(false, cenerateObjUser);
    		
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
            
            GenerateObjUser cenerateObjUser = new GenerateObjUser(newUser.getId(), newUser.getName(), newUser.getImageName(), responseToken);
            
            Response response = new Response(false, cenerateObjUser);
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
	public ResponseEntity<Object> update(@ModelAttribute UpdateUserDto updateUserDto, @RequestHeader("Authorization") String token) {
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
		
		String imagePath = null;
		
		if(userFind.get().getImageName() != null) {
			String caminhoImagem = userFind.get().getImageName();

	        File arquivoImagem = new File(caminhoImagem);
	        
	        arquivoImagem.delete();
		}

	    if (updateUserDto.getImage() != null) {
	    	try {
		    	String imageNameFull = updateUserDto.getImage().getOriginalFilename();
		    	int startPoint = imageNameFull.lastIndexOf('.');
		    	String extendImage = imageNameFull.substring(startPoint + 1);
		    	
		    	String imageName = updateUserDto.getName() + "_" + String.valueOf(userId) + "_" + System.currentTimeMillis() + "." + extendImage;
		    	
		    	File novaPasta = new File("src/main/resources/static/images/perfil/user_" + String.valueOf(userId));
		    	novaPasta.mkdir();
		    	
		        Path path = Paths.get("src/main/resources/static/images/perfil/user_" + String.valueOf(userId) + "/" + imageName);
		        imagePath = "images/perfil/user_" + String.valueOf(userId) + "/" + imageName;
		        
		        Files.copy(updateUserDto.getImage().getInputStream(), path);
		        
		    } catch (IOException e) {
		        e.printStackTrace();
		        Response response = new Response(true, "Erro ao fazer o upload da imagem => " + e.getMessage());
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		    }
	    }
	    
	    String hash = null;
	    
	    User newUser = userRepository.findUser(userId);
	    
	    TokenGenerate newToken = new TokenGenerate();
	    
	    if(updateUserDto.getPassword() != null) {
		    hash = hashPassword.encodePassword(updateUserDto.getPassword());
	    } else {
	    	hash = newUser.getPassword();
	    }	    
	    
	    userRepository.updateUser(userId, updateUserDto.getName(), hash, imagePath);
	    
	    String responseToken = newToken.generateToken(newUser);
	    
	    Response response = new Response(false, responseToken);
    	return ResponseEntity.status(HttpStatus.OK).body(response);	
		
	}
}

