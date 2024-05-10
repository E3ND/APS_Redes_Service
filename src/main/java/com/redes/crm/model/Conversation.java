package com.redes.crm.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(
		name = "conversation",
		uniqueConstraints={
				@UniqueConstraint(columnNames = {"id"})
			}
	   )
public class Conversation {
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonIgnore
    @OneToMany(mappedBy = "conversationId")
    private List<Chat> chat;
    
	@JsonIgnore
    @OneToMany(mappedBy = "conversationId")
    private List<ChatUser> chatUser;
	
	@JsonIgnore
    @OneToMany(mappedBy = "conversationId")
    private List<ChatGroup> Group;
    
    private Date createdAt;
    
    @PrePersist
    protected void onCreate() {
        setCreatedAt(new Date());
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Chat> getChat() {
		return chat;
	}

	public void setChat(List<Chat> chat) {
		this.chat = chat;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
    
    
}
