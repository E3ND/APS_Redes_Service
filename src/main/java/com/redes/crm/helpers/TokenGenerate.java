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

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public class TokenGenerate {
    private Key key;
    
    //Colocar isso em env depois
    private static final String SECRET_KEY = "OpI3TaszkA8h6xJkNokRXHFpM7s5TdDzmGWg1YVJPz57lWWLvpmMhmsF9rmIm5U8PM8tr4Xk6E9Bm0ed8H592wJX9bqolPdiACni6sccm1f7o6ejyud8Xid0pGtLIF4Z13qsec7vtuK9zpmspCBMzPlk4nabJuwUfyPykZlSsFPdym5XE3KuxGR3KJW7PgKYFqewgzh7";

    //Colocar isso em env depois
    private static final long EXPIRATION_TIME = 86400000; 
    
    @PostConstruct
    public void init() {
    	this.key = Keys.hmacShaKeyFor(this.SECRET_KEY.getBytes());
    }
    
    public String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDetails.getId());
        claims.put("name", userDetails.getName());
        claims.put("imageName", userDetails.getImageName());
        claims.put("createdAt", userDetails.getCreatedAt());
        return createToken(claims, userDetails.getEmail());
    }
    
    public static String createToken(Map<String, Object> claims, String subject) {

        Key secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());

        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey);

        return jwtBuilder.compact();
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
    	return Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
    }
    
    public boolean validateTokenIntegrity(String token) {
    	try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
