package com.redes.crm.dto;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class ChatGroupCreateDto implements Serializable {
	private String title;
	
	private String description;
	
	private MultipartFile image;

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

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}
	
	
}
