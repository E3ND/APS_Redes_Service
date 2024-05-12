package com.redes.crm.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.redes.crm.dto.FindAllConversationsDto;
import com.redes.crm.dto.FindAllmessagesOfConversationDto;
import com.redes.crm.dto.FindByUserIdAndRecipientIdDto;
import com.redes.crm.dto.FindChatUserByConversationDto;
import com.redes.crm.dto.GetChatByUserIdDto;
import com.redes.crm.dto.UpdateUserDto;
import com.redes.crm.helpers.GetTokenFormat;
import com.redes.crm.helpers.Response;
import com.redes.crm.helpers.TokenGenerate;
import com.redes.crm.model.Chat;
import com.redes.crm.model.ChatUser;
import com.redes.crm.model.Conversation;
import com.redes.crm.model.User;
import com.redes.crm.repository.ChatRepository;
import com.redes.crm.repository.ChatUserRepository;
import com.redes.crm.repository.ConversationRepository;
import com.redes.crm.dto.ChatCreateMessagedto;
import com.redes.crm.dto.FindAllConversationByUserDto;
import com.redes.crm.repository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private ChatUserRepository chatUserRepository;
    
    @GetMapping("/messages/user/{otherUserId}")
    public ResponseEntity<Object> getChatByUserId (@PathVariable Long otherUserId, @RequestHeader("Authorization") String token) {
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
		
		Optional <GetChatByUserIdDto> conversation = chatUserRepository.getChatByUserId(userId, otherUserId);
    	
    	Response response = new Response(false, conversation);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Transactional
    @PutMapping("/visualize/{messageId}")
    public ResponseEntity<Object> visualizeMessage (@PathVariable Long messageId, @RequestHeader("Authorization") String token) {
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
		
		chatRepository.updateVisualize(messageId);
		
    	Response response = new Response(false, "Mudado com sucesso");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/messages/{conversationId}")
    public ResponseEntity<Object> getAllMessagesConversation (@PathVariable Long conversationId, @RequestHeader("Authorization") String token) {
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
		
		List<FindChatUserByConversationDto> findChatUserByConversation = chatUserRepository.findChatUserByConversation(conversationId);
		
		Boolean existsConversationWithUser = false;
		
		for (FindChatUserByConversationDto conversation : findChatUserByConversation) {
		    if(conversation.getUserId() == userId) {
		    	existsConversationWithUser = true;
		    }
		}
		
		if(existsConversationWithUser == false) {
			Response response = new Response(true, "Conversa não encontrada");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		
		List<FindAllmessagesOfConversationDto> allMessagesOfConversation = chatRepository.findAllmessagesOfConversation(conversationId);
		
		Response response = new Response(false, allMessagesOfConversation);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping("/conversation/by-user")
    public ResponseEntity<Object> getConversationByUser (@RequestHeader("Authorization") String token) {
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
		
		List<FindAllConversationByUserDto> findAllConversationByUser = conversationRepository.findAllConversationByUser(userId);
		
		Response response = new Response(false, findAllConversationByUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    //Não lembro o Porquê criei isso aqui
    @GetMapping("/conversation")
    public ResponseEntity<Object> getAllUserConversation (@RequestHeader("Authorization") String token) {
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
		
		List<FindAllConversationsDto> conversations = conversationRepository.findAllConversations(userId);
		
		Response response = new Response(false, conversations);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

	@PostMapping("/create/{recipientId}")
	public ResponseEntity<Object> CreateMessage (@ModelAttribute ChatCreateMessagedto chatCreateMessagedto, @PathVariable Long recipientId, @RequestHeader("Authorization") String token) {

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
		
		Optional<User> recipient = userRepository.findById(recipientId);
		
		if(!recipient.isPresent()) {
			Response response = new Response(true, "Este usuário não existe");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		
		List<FindByUserIdAndRecipientIdDto> conversations  = conversationRepository.findByUserIdAndRecipientId(user.get().getId(), recipient.get().getId());

		Boolean ThisMessageRecipientExist = false;
		Long ThisconversationIdRecipient = null;

		for (FindByUserIdAndRecipientIdDto conversation : conversations) {
		    Long userIdChatUser = conversation.getUserId();
		    Long recipientIdChatUser = conversation.getRecipientId();
		    Long conversationId = conversation.getConversationId();
		    
		    if((userIdChatUser == userId && recipientIdChatUser == recipientId) || (userIdChatUser == recipientId && recipientIdChatUser == userId) ) {
		    	ThisconversationIdRecipient = conversationId;
		    	ThisMessageRecipientExist = true;
		    }
		    
		}

		if(ThisMessageRecipientExist == false) {
			Conversation conversation = new Conversation();
			ChatUser chatUser = new ChatUser();
			ChatUser chatUserRecipient = new ChatUser();
			
			chatUser.setConversationId(conversation); 
			chatUser.setUserId(user.get());

			chatUserRecipient.setConversationId(conversation); 
			chatUserRecipient.setUserId(recipient.get());
			
			Conversation consersationId = conversationRepository.save(conversation);
			chatUserRepository.save(chatUser);
			chatUserRepository.save(chatUserRecipient);
			
			String imagePath = null;
			
			if (chatCreateMessagedto.getImage() != null) {
		    	try {
			    	String imageNameFull = chatCreateMessagedto.getImage().getOriginalFilename();
			    	int startPoint = imageNameFull.lastIndexOf('.');
			    	String extendImage = imageNameFull.substring(startPoint + 1);
			    	
			    	String imageName = System.currentTimeMillis() + "." + extendImage;
			    	
			    	File novaPasta = new File("src/main/resources/static/images/chat_" + String.valueOf(consersationId.getId()));
			    	novaPasta.mkdir();
			    	
			        Path path = Paths.get("src/main/resources/static/images/chat_" + String.valueOf(consersationId.getId()) + "/" + imageName);
			        imagePath = "src/main/resources/static/images/chat_" + String.valueOf(consersationId.getId()) + "/" + imageName;
			        
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
			chatCreate.setRecipientId(recipient.get());
			chatCreate.setMessage(chatCreateMessagedto.getMessage());
			chatCreate.setImageName(imagePath);
			
			chatRepository.save(chatCreate);
			
			Response response = new Response(false, chatCreate);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} else {
			Chat chatCreate = new Chat();
			
			Conversation conversationId = conversationRepository.findConversationById(ThisconversationIdRecipient);
			
			String imagePath = null;
			
			chatCreate.setConversationId(conversationId); 
			chatCreate.setVisualize(false);
			chatCreate.setSenderId(user.get()); 
			chatCreate.setRecipientId(recipient.get());
			chatCreate.setMessage(chatCreateMessagedto.getMessage());
			chatCreate.setImageName(imagePath);
			
			if (chatCreateMessagedto.getImage() != null) {
		    	try {
			    	String imageNameFull = chatCreateMessagedto.getImage().getOriginalFilename();
			    	int startPoint = imageNameFull.lastIndexOf('.');
			    	String extendImage = imageNameFull.substring(startPoint + 1);
			    	
			    	String imageName = System.currentTimeMillis() + "." + extendImage;
			    	
			    	File novaPasta = new File("src/main/resources/static/images/chat_" + String.valueOf(conversationId.getId()));
			    	novaPasta.mkdir();

			        Path path = Paths.get("src/main/resources/static/images/chat_" + String.valueOf(conversationId.getId()) + "/" + imageName);
			        imagePath = "src/main/resources/static/images/chat_" + String.valueOf(conversationId.getId()) + "/" + imageName;
			        
			        Files.copy(chatCreateMessagedto.getImage().getInputStream(), path);
			        
			    } catch (IOException e) {
			        e.printStackTrace();
			        Response response = new Response(true, e.getMessage());
			        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			    }
		    }
			
			chatRepository.save(chatCreate);
			
			Response response = new Response(false, chatCreate);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
	}

}
