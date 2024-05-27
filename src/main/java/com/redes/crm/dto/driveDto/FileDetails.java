package com.redes.crm.dto.driveDto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileDetails {
    private String kind;
    private String id;
    private String name;
    private String mimeType;
    
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
    
    
}
