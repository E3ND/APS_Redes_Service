package com.redes.crm.model;

import org.hibernate.validator.constraints.Email;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}
