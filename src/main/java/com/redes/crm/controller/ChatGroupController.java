package com.redes.crm.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redes.crm.dto.AddUserGroupDto;
import com.redes.crm.dto.ChatCreateMessagedto;
import com.redes.crm.dto.ChatGroupCreateDto;
import com.redes.crm.dto.FindGroupConversationDto;
import com.redes.crm.dto.FindGroupUserDto;
import com.redes.crm.dto.FindUserByIdDto;
import com.redes.crm.dto.FindUserGroupDto;
import com.redes.crm.dto.GetMembersDto;
import com.redes.crm.dto.UserIsPresentGroupDto;
import com.redes.crm.helpers.GetTokenFormat;
import com.redes.crm.helpers.Response;
import com.redes.crm.helpers.TokenGenerate;
import com.redes.crm.model.Chat;
import com.redes.crm.model.ChatGroup;
import com.redes.crm.model.ChatUser;
import com.redes.crm.model.Conversation;
import com.redes.crm.model.User;
import com.redes.crm.repository.ChatGroupReposittory;
import com.redes.crm.repository.ChatRepository;
import com.redes.crm.repository.ChatUserRepository;
import com.redes.crm.repository.ConversationRepository;
import com.redes.crm.repository.UserRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/group")
public class ChatGroupController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private ChatUserRepository chatUserRepository;
    @Autowired
    private ChatGroupReposittory chatGroupReposittory;
    
    @GetMapping("/members/{conversationId}")
    public ResponseEntity<Object> getMembers (@PathVariable Long conversationId, @RequestHeader("Authorization") String token) {
    	GetTokenFormat getTokenFormat = new GetTokenFormat();

		String existToken = getTokenFormat.cutToken(token);
		
		TokenGenerate tokenGenerate = new TokenGenerate();
		
		boolean isValidTokenIntegrity = tokenGenerate.validateTokenIntegrity(existToken);
		if(isValidTokenIntegrity == false) {
			Response response = new Response(true, "Token Inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		Long userId = tokenGenerate.extractUserId(existToken);
		
		Optional<User> user = userRepository.findById(userId);
		
		boolean isValidTokenParams = tokenGenerate.validateTokenParams(existToken, user.get());
		
		if(existToken == "Not found" || isValidTokenParams == false) {
			Response response = new Response(true, "Token Inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
    	
		List<GetMembersDto> members = chatGroupReposittory.getMembers(conversationId);
    	
    	Response response = new Response(false, members);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping("/conversation/by-user")
    public ResponseEntity<Object> getConversationGroupByUser (@RequestHeader("Authorization") String token) {
    	GetTokenFormat getTokenFormat = new GetTokenFormat();

		String existToken = getTokenFormat.cutToken(token);
		
		TokenGenerate tokenGenerate = new TokenGenerate();
		
		boolean isValidTokenIntegrity = tokenGenerate.validateTokenIntegrity(existToken);
		if(isValidTokenIntegrity == false) {
			Response response = new Response(true, "Token Inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		Long userId = tokenGenerate.extractUserId(existToken);
		
		Optional<User> user = userRepository.findById(userId);
		
		boolean isValidTokenParams = tokenGenerate.validateTokenParams(existToken, user.get());
		
		if(existToken == "Not found" || isValidTokenParams == false) {
			Response response = new Response(true, "Token Inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		List<FindGroupUserDto> findAllConversationGroupByUser = chatGroupReposittory.findGroupUser(userId);
    	
    	Response response = new Response(false, findAllConversationGroupByUser);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @Transactional
    @DeleteMapping("/{conversationId}/remove/{memberId}")
    public ResponseEntity<Object> CreateMessage (@PathVariable Long memberId, @PathVariable Long conversationId, @RequestHeader("Authorization") String token) {
    	GetTokenFormat getTokenFormat = new GetTokenFormat();

		String existToken = getTokenFormat.cutToken(token);
		
		TokenGenerate tokenGenerate = new TokenGenerate();
		
		boolean isValidTokenIntegrity = tokenGenerate.validateTokenIntegrity(existToken);
		
		if(isValidTokenIntegrity == false) {
			Response response = new Response(true, "Token Inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		Long userId = tokenGenerate.extractUserId(existToken);
		
		Optional<User> user = userRepository.findById(userId);
		
		boolean isValidTokenParams = tokenGenerate.validateTokenParams(existToken, user.get());
		
		if(existToken == "Not found" || isValidTokenParams == false) {
			Response response = new Response(true, "Token Inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		Optional <FindGroupConversationDto> ownerGroupIsLikeUserId = chatGroupReposittory.findGroupConversation(conversationId);
		
		if(!ownerGroupIsLikeUserId.isPresent()) {
			Response response = new Response(true, "Conversa não encontrada");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		
		if(userId != ownerGroupIsLikeUserId.get().getOwner()) {
			Response response = new Response(true, "Você não é dono deste grupo");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		try {			
			chatGroupReposittory.removeUserFromGroup(memberId, conversationId);
		} catch (Exception e) {
			// TODO: handle exception
			Response response = new Response(true, "Erro ao remover o usuário => " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
    	
    	Response response = new Response(false, "Removido com sucesso");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @PostMapping("/create/{conversationId}")
    public ResponseEntity<Object> CreateMessage (@ModelAttribute ChatCreateMessagedto chatCreateMessagedto, @PathVariable Long conversationId, @RequestHeader("Authorization") String token) {
    	GetTokenFormat getTokenFormat = new GetTokenFormat();

		String existToken = getTokenFormat.cutToken(token);
		
		TokenGenerate tokenGenerate = new TokenGenerate();
		
		boolean isValidTokenIntegrity = tokenGenerate.validateTokenIntegrity(existToken);
		
		if(isValidTokenIntegrity == false) {
			Response response = new Response(true, "Token Inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		Long userId = tokenGenerate.extractUserId(existToken);
		
		Optional<User> user = userRepository.findById(userId);
		
		boolean isValidTokenParams = tokenGenerate.validateTokenParams(existToken, user.get());
		
		if(existToken == "Not found" || isValidTokenParams == false) {
			Response response = new Response(true, "Token Inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		Conversation conversation = conversationRepository.findConversationById(conversationId);
		
		if(conversation == null) {
			Response response = new Response(true, "Conversa não encontrada");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		
		Optional <FindGroupConversationDto> thisConversationIsGroup = chatGroupReposittory.findGroupConversation(conversation.getId());
		
		if(!thisConversationIsGroup.isPresent()) {
			Response response = new Response(true, "Conversa não encontrada");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		
		Optional <UserIsPresentGroupDto> userIsPresentGroup = chatUserRepository.userIsPresentGroup(userId ,conversation.getId());
		
		if(!userIsPresentGroup.isPresent()) {
			Response response = new Response(true, "Você não pertence a esse grupo");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		String imagePath = null;

		if (chatCreateMessagedto.getImage() != null) {
	    	try {
		    	String imageNameFull = chatCreateMessagedto.getImage().getOriginalFilename();
		    	int startPoint = imageNameFull.lastIndexOf('.');
		    	String extendImage = imageNameFull.substring(startPoint + 1);
		    	
		    	String imageName = System.currentTimeMillis() + "." + extendImage;
		    	
		    	File novaPasta = new File("src/main/resources/static/images/group_" + String.valueOf(conversation.getId()));
		    	novaPasta.mkdir();
		    	
		        Path path = Paths.get("src/main/resources/static/images/group_" + String.valueOf(conversation.getId()) + "/" + imageName);
		        imagePath = "src/main/resources/static/images/group_" + String.valueOf(conversation.getId()) + "/" + imageName;
		        
		        Files.copy(chatCreateMessagedto.getImage().getInputStream(), path);
		        
		    } catch (IOException e) {
		        e.printStackTrace();
		        Response response = new Response(true, "Erro ao fazer o upload da imagem => " + e.getMessage());
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		    }
	    }
		
		Chat chatCreate = new Chat();
		chatCreate.setVisualize(false);
		chatCreate.setConversationId(conversation); 
		chatCreate.setSenderId(user.get()); 
		chatCreate.setRecipientId(null);
		chatCreate.setMessage(chatCreateMessagedto.getMessage());
		chatCreate.setImageName(imagePath);
		
		chatRepository.save(chatCreate);
    	
    	Response response = new Response(false, "Mensagem enviada");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/add/{conversationId}")
	public ResponseEntity<Object> CreateGropu (@RequestBody AddUserGroupDto addUserGroupDto, @PathVariable Long conversationId, @RequestHeader("Authorization") String token) {
    	GetTokenFormat getTokenFormat = new GetTokenFormat();

		String existToken = getTokenFormat.cutToken(token);
		
		TokenGenerate tokenGenerate = new TokenGenerate();
		
		boolean isValidTokenIntegrity = tokenGenerate.validateTokenIntegrity(existToken);
		
		if(isValidTokenIntegrity == false) {
			Response response = new Response(true, "Token Inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		Long userId = tokenGenerate.extractUserId(existToken);
		
		Optional<User> user = userRepository.findById(userId);
		
		boolean isValidTokenParams = tokenGenerate.validateTokenParams(existToken, user.get());
		
		if(existToken == "Not found" || isValidTokenParams == false) {
			Response response = new Response(true, "Token Inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		Optional <FindGroupConversationDto> ownerGroupIsLikeUserId = chatGroupReposittory.findGroupConversation(conversationId);
		
		if(!ownerGroupIsLikeUserId.isPresent()) {
			Response response = new Response(true, "Conversa não encontrada");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		
		if(userId != ownerGroupIsLikeUserId.get().getOwner()) {
			Response response = new Response(true, "Você não é dono deste grupo");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		ChatUser chatUser = new ChatUser();
		//Não deixar adicionar usuário em conversas que não são grupos
		Conversation conversation = conversationRepository.findConversationById(conversationId);
		
		if(conversation == null) {
			Response response = new Response(true, "Conversa não encontrada");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		
		User userAdd = userRepository.findUser(addUserGroupDto.getUserId());
		
		if(userAdd == null) {
			Response response = new Response(true, "Usuário não encontrado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		Optional<FindUserGroupDto> userAlreadyAddGroup = chatUserRepository.findUserGroup(addUserGroupDto.getUserId(), conversation.getId());
		
		if(userAlreadyAddGroup.isPresent()) {
			Response response = new Response(false, "Usuário já adicionado");
	        return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		
		chatUser.setConversationId(conversation);
		chatUser.setUserId(userAdd);
		
		chatUserRepository.save(chatUser);
    	
    	Response response = new Response(false, "Adicionado com sucesso");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
	
	@PostMapping("/create")
	public ResponseEntity<Object> CreateGropu (@ModelAttribute ChatGroupCreateDto chatGroupCreateDto, @RequestHeader("Authorization") String token) {
		GetTokenFormat getTokenFormat = new GetTokenFormat();

		String existToken = getTokenFormat.cutToken(token);
		
		TokenGenerate tokenGenerate = new TokenGenerate();
		
		boolean isValidTokenIntegrity = tokenGenerate.validateTokenIntegrity(existToken);
		
		if(isValidTokenIntegrity == false) {
			Response response = new Response(true, "Token Inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		Long userId = tokenGenerate.extractUserId(existToken);
		
		Optional<User> user = userRepository.findById(userId);
		
		boolean isValidTokenParams = tokenGenerate.validateTokenParams(existToken, user.get());
		
		if(existToken == "Not found" || isValidTokenParams == false) {
			Response response = new Response(true, "Token Inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		Conversation conversation = new Conversation();
		ChatUser chatUser = new ChatUser();
		ChatGroup chatGroup = new ChatGroup();
		
		chatUser.setConversationId(conversation); 
		chatUser.setUserId(user.get());

		Conversation consersationId = conversationRepository.save(conversation);
		chatUserRepository.save(chatUser);
		
		String imagePath = null;
		
		if (chatGroupCreateDto.getImage() != null) {
	    	try {
		    	String imageNameFull = chatGroupCreateDto.getImage().getOriginalFilename();
		    	int startPoint = imageNameFull.lastIndexOf('.');
		    	String extendImage = imageNameFull.substring(startPoint + 1);
		    	
		    	String imageName = "foto_de_capa_" + System.currentTimeMillis() + "." + extendImage;
		    	
		    	File novaPasta = new File("src/main/resources/static/images/group_" + String.valueOf(consersationId.getId()));
		    	novaPasta.mkdir();
		    	
		        Path path = Paths.get("src/main/resources/static/images/group_" + String.valueOf(consersationId.getId()) + "/" + imageName);
		        imagePath = "src/main/resources/static/images/group_" + String.valueOf(consersationId.getId()) + "/" + imageName;
		        
		        Files.copy(chatGroupCreateDto.getImage().getInputStream(), path);
		        
		    } catch (IOException e) {
		        e.printStackTrace();
		        Response response = new Response(true, "Erro ao fazer o upload da imagem => " + e.getMessage());
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		    }
	    }
		
		chatGroup.setTitle(chatGroupCreateDto.getTitle());
		chatGroup.setDescription(chatGroupCreateDto.getDescription());
		chatGroup.setConversationId(consersationId);
		chatGroup.setImageName(imagePath);
		chatGroup.setOwner(user.get());
		
		chatGroupReposittory.save(chatGroup);
		
		User newUserDto = user.get();
		newUserDto.setPassword(null);
		
		chatGroup.setOwner(newUserDto);
		
		Response response = new Response(false, chatGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
