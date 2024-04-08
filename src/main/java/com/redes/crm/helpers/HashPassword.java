package com.redes.crm.helpers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class HashPassword {
	 private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	 
	    public void PasswordEncoderService() {
	        this.passwordEncoder = new BCryptPasswordEncoder();
	    }

	    public String encodePassword(String password) {
	    	System.out.printf("Opa => ", password);
	        return passwordEncoder.encode(password);
	    }

	    public boolean matchPassword(String rawPassword, String encodedPassword) {
	        return passwordEncoder.matches(rawPassword, encodedPassword);
	    }
}
