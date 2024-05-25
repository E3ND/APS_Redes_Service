package com.redes.crm.dto;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class UpdateUserDto implements Serializable {
    private String name;

    private String password;

    private MultipartFile file;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
    
    
}
