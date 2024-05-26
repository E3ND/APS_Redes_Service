package com.redes.crm.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.redes.crm.dto.driveDto.CreateFileTemplateDto;
import com.redes.crm.dto.driveDto.FileDetails;
import com.redes.crm.dto.driveDto.RefreshToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GoogleDriveController {

	@Autowired
    private RestTemplate restTemplate = new RestTemplate();
	
	public String RefreshToken() {
		String url = "https://oauth2.googleapis.com/token";
		
		RefreshToken refreshToken = new RefreshToken();
		
		refreshToken.setRefresh_token("1//04aovUVbEMEupCgYIARAAGAQSNwF-L9Ir_GZOpL7Ful8SsAaOrR8r96xwxqCdvboKTVn1nOhW8BCxNzfzKhqBSIZOSA3qh-J1TSg");
		refreshToken.setClient_id("847233060309-h8b8behuqb1hhousurs8vd9jdi4hlimc.apps.googleusercontent.com");
		refreshToken.setClient_secret("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");
		refreshToken.setGrant_type("refresh_token");
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<RefreshToken> request = new HttpEntity<>(refreshToken, headers);
        
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        
        String responseBody = response.getBody();
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
		try {
			root = mapper.readTree(responseBody);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String accessToken = root.get("access_token").asText();
        
        return accessToken;
	}
	
	public String createFileTemplate(String token, String nameFile) {
		String url = "https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable";
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);
		
		CreateFileTemplateDto createFileTemplateDto = new CreateFileTemplateDto();
		
		createFileTemplateDto.setName(nameFile);
		createFileTemplateDto.setParents("1yo6TLJTb8jyxShtPT93BtHXTcRpH_uoF");
		
		String jsonBody = "{ \"name\": \"" + createFileTemplateDto.getName() + "\", \"parents\": [\"" + createFileTemplateDto.getParents() + "\"] }";
		
		HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
		
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
		
		HttpHeaders responseHeaders = response.getHeaders();

		String uploadId = responseHeaders.getFirst("X-GUploader-UploadID");
		
		return uploadId;
	}
	
	public FileDetails uploadDriveFile(String token, String fileId, byte[] fileBinary) {
		String url = "https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable&upload_id=" + fileId;
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);
		
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(fileBinary, headers), String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		String responseBody = response.getBody();
		FileDetails responseObj = null;
		
		try {
			responseObj = mapper.readValue(responseBody, FileDetails.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return responseObj;
	}
	
	public byte[] tranformFileInBinary(MultipartFile file) {
		byte[] binario = null;
    	
   	 try {
			binario = file.getBytes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	 
   	 	return binario;
	}
}
