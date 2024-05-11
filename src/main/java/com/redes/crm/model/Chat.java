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
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(
		name = "chat",
		uniqueConstraints={
				@UniqueConstraint(columnNames = {"id"})
			}
	   )
public class Chat {
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(name = "message", nullable = false)
	private String message;
	
	@Column(name = "visualize", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean visualize;
	
	@ManyToOne
    @JoinColumn(name = "conversationId")
	private Conversation conversationId;
	
	
	private Date createdAt;
	
	@ManyToOne
    @JoinColumn(name = "senderId")
    private User senderId;
	
	@ManyToOne
    @JoinColumn(name = "recipientId")
    private User recipientId;
	
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
    
    private String imageName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public User getSenderId() {
		return senderId;
	}

	public void setSenderId(User senderId) {
		this.senderId = senderId;
	}

	public User getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(User recipientId) {
		this.recipientId = recipientId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Boolean getVisualize() {
		return visualize;
	}

	public void setVisualize(Boolean visualize) {
		this.visualize = visualize;
	}
	
	
}
