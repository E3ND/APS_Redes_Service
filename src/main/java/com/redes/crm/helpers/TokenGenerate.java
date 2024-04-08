package com.redes.crm.helpers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.redes.crm.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TokenGenerate {
	//Colocar isso em env depois
	@Value("aisgdquihdqbwnaml")
    private String secret = "OpI3TaszkA8h6xJkNokRXHFpM7s5TdDzmGWg1YVJPz57lWWLvpmMhmsF9rmIm5U8PM8tr4Xk6E9Bm0ed8H592wJX9bqolPdiACni6sccm1f7o6ejyud8Xid0pGtLIF4Z13qsec7vtuK9zpmspCBMzPlk4nabJuwUfyPykZlSsFPdym5XE3KuxGR3KJW7PgKYFqewgzh7";
	//Colocar isso em env depois
    @Value("86400000")
    private long expiration = 86400000;
    
    public String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getEmail());
    }
    
    private String createToken(Map<String, Object> claims, String subject) {
    	return Jwts.builder().setClaims(claims).setSubject(subject)
    			.setIssuedAt(new Date(System.currentTimeMillis()))
    			.setExpiration(new Date(System.currentTimeMillis() + expiration))
    			.signWith(SignatureAlgorithm.HS256, secret).compact();
    }
    
    public boolean validateToken(String token, User userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }
    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
