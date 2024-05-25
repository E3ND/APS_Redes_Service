package com.redes.crm.dto;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class ChatCreateMessagedto implements Serializable {
	private String message;
	
	private MultipartFile file;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
}
