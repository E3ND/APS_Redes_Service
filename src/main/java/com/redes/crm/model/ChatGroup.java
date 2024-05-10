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
		name = "chatGroup",
		uniqueConstraints={
				@UniqueConstraint(columnNames = {"id"})
			}
	   )
public class ChatGroup {
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(name = "title", nullable = false)
	private String title;
	
	@NotBlank
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "imageName", nullable = true)
	private String imageName;
	
    private Date createdAt;
	
	@ManyToOne
    @JoinColumn(name = "conversationId")
	private Conversation conversationId;
	
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
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
