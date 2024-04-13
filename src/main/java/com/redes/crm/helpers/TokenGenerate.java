package com.redes.crm.helpers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.redes.crm.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

public class TokenGenerate {
	//Colocar isso em env depois
	@Value("OpI3TaszkA8h6xJkNokRXHFpM7s5TdDzmGWg1YVJPz57lWWLvpmMhmsF9rmIm5U8PM8tr4Xk6E9Bm0ed8H592wJX9bqolPdiACni6sccm1f7o6ejyud8Xid0pGtLIF4Z13qsec7vtuK9zpmspCBMzPlk4nabJuwUfyPykZlSsFPdym5XE3KuxGR3KJW7PgKYFqewgzh7")
    private String secret = "OpI3TaszkA8h6xJkNokRXHFpM7s5TdDzmGWg1YVJPz57lWWLvpmMhmsF9rmIm5U8PM8tr4Xk6E9Bm0ed8H592wJX9bqolPdiACni6sccm1f7o6ejyud8Xid0pGtLIF4Z13qsec7vtuK9zpmspCBMzPlk4nabJuwUfyPykZlSsFPdym5XE3KuxGR3KJW7PgKYFqewgzh7";
	//Colocar isso em env depois
    @Value("86400000")
    private long expiration = 86400000;
    
    private Key key;
    
    @PostConstruct
    public void init() {
    	this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    public String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDetails.getId());
        claims.put("name", userDetails.getName());
        return createToken(claims, userDetails.getEmail());
    }
    
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }
    
    
    public boolean validateTokenParams(String token, User userDetails) {
        final String userEmail = extractEmail(token);
        final Long userId = extractUserId(token);

        return (userEmail.equals(userDetails.getEmail()) && userId.equals(userDetails.getId()) && !isTokenExpired(token));
    }
    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("name", String.class);
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("id", Long.class); 
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
    
    public boolean validateTokenIntegrity(String token) {
    	try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
