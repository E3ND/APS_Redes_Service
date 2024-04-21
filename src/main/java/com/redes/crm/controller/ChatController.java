package com.redes.crm.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redes.crm.dto.ConversationDto;
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

	@PostMapping("/create/{recipientId}")
	public ResponseEntity<Object> CreateMessage (@RequestBody Chat chat, @PathVariable Long recipientId, @RequestHeader("Authorization") String token) {

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
		
		List<ConversationDto> conversations  = conversationRepository.findByUserIdAndRecipientId(user.get().getId(), recipient.get().getId());

		Boolean ThisMessageRecipientExist = false;
		Long ThisconversationIdRecipient = null;

		for (ConversationDto conversation : conversations) {
		    Long userIdChatUser = conversation.getUserId();
		    Long conversationId = conversation.getConversationId();
		    
		    if(userIdChatUser == recipientId) {
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
				
			Chat chatCreate = new Chat();
			chatCreate.setConversationId(conversation); 
			chatCreate.setSenderId(user.get()); 
			chatCreate.setRecipientId(recipient.get());
			chatCreate.setMessage(chat.getMessage());
			
			conversationRepository.save(conversation);
			chatUserRepository.save(chatUser);
			chatUserRepository.save(chatUserRecipient);
			
			chatRepository.save(chatCreate);
			
			Response response = new Response(false, chatCreate);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} else {
			Chat chatCreate = new Chat();
			
			Conversation conversationId = conversationRepository.findConversationById(ThisconversationIdRecipient);

			chatCreate.setConversationId(conversationId); 
			chatCreate.setSenderId(user.get()); 
			chatCreate.setRecipientId(recipient.get());
			chatCreate.setMessage(chat.getMessage());
			
			chatRepository.save(chatCreate);
			
			Response response = new Response(false, chatCreate);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
	}
}
