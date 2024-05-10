package com.redes.crm.dto;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class ChatCreateMessagedto implements Serializable {
	private String message;
	
	private MultipartFile image;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}
}
