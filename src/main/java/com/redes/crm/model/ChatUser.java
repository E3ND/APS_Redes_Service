package com.redes.crm.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(
		name = "chatUser",
		uniqueConstraints={
				@UniqueConstraint(columnNames = {"id"})
			}
	   )
public class ChatUser {
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "userId")
    private User userId;
	
	@ManyToOne
    @JoinColumn(name = "conversationId")
	private Conversation conversationId;
	
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

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public Conversation getConversationId() {
		return conversationId;
	}

	public void setConversationId(Conversation conversationId) {
		this.conversationId = conversationId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	
}
