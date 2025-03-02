package com.redes.crm.helpers;

public class GenerateObjUser {
	private Long id;
	private String name;
	private String imageName;
	private String token;
	
    public GenerateObjUser(Long id, String name, String imageName, String token) {
    	this.id = id;
    	this.name = name;
    	this.imageName = imageName;
    	this.token = token;
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

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
    
    
}
