package com.redes.crm.model;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Email;

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
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//Com o lombok(@Data) ele gera automaticamente os getters e setters junto com o hashCode
@Data
@Entity
@Table(
		name = "user",
		uniqueConstraints={
				@UniqueConstraint(columnNames={"email"}),
				@UniqueConstraint(columnNames = {"id"})
			}
	   )
public class User {
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@NotBlank
	@Column(name = "name", nullable = false)
	private String name;
	
	@Email
	@Column(name = "email", nullable = false)
	private String email;
	
	@NotBlank
	@Column(name = "password", nullable = false)
	private String password;
	
	@JsonIgnore
    @OneToMany(mappedBy = "senderId")
    private List<Chat> senderId;
    
	@JsonIgnore
    @OneToMany(mappedBy = "recipientId")
    private List<Chat> recipientId;
    
	@JsonIgnore
    @OneToMany(mappedBy = "userId")
    private List<ChatUser> chatUser;
	
	@JsonIgnore
    @OneToMany(mappedBy = "owner")
    private List<ChatGroup> chatGroup;
	
	private String imageName;
    
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public List<Chat> getSenderId() {
		return senderId;
	}

	public void setSenderId(List<Chat> senderId) {
		this.senderId = senderId;
	}

	public List<Chat> getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(List<Chat> recipientId) {
		this.recipientId = recipientId;
	}

	public List<ChatUser> getChatUser() {
		return chatUser;
	}

	public void setChatUser(List<ChatUser> chatUser) {
		this.chatUser = chatUser;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	
}
