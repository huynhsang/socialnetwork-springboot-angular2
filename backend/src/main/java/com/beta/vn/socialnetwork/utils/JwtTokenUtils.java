package com.beta.vn.socialnetwork.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.beta.vn.socialnetwork.config.JwtSettings;
import com.beta.vn.socialnetwork.model.UserContext;
import com.beta.vn.socialnetwork.model.token.AccessJwtToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtils implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private JwtSettings jwtSettings;
	
	public String getEmailFromToken(String token) {
        String email;
        try {
            final Claims claims = getClaimsFromToken(token);
            email = claims.getSubject();
        } catch (Exception e) {
        	email = null;
        }
        return email;
    }
	
	public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = claims.getIssuedAt();
        } catch (Exception e) {
            created = null;
        }
        return created;
    }
	
	public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }
	
	/*public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken(token);
            audience = (String) claims.getIssuer();
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }*/
	
	public Boolean validateToken(String token, UserContext userContext) {
        final String email = getEmailFromToken(token);
        return (
                email.equals(userContext.getUsername())
                        && !isTokenExpired(token));
    }
	

	private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtSettings.getTokenSigningKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }
	
	public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    public AccessJwtToken createAccessJwtToken(UserContext userContext){
		if (StringUtils.isBlank(userContext.getUsername())) 
            throw new IllegalArgumentException("Cannot create JWT Token without username");
		if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty()) 
            throw new IllegalArgumentException("User doesn't have any privileges");
		
		Claims claims = Jwts.claims().setSubject(userContext.getUsername());
		claims.put("scopes", userContext.getAuthorities().stream().map(s->s.toString())
				.collect(Collectors.toList()));
		DateTime currentTime = new DateTime();
		
		String token = Jwts.builder()
				.setClaims(claims)
				.setIssuer(jwtSettings.getTokenIssuer())
				.setIssuedAt(currentTime.toDate())
				.setExpiration(currentTime.plusMinutes(jwtSettings.getTokenExpirationTime()).toDate())
				.signWith(SignatureAlgorithm.HS512, jwtSettings.getTokenSigningKey())
			.compact();
		
		return new AccessJwtToken(token, claims);
	}
    
    public AccessJwtToken refreshJwtToken(String token){
    	
    	
    	return null;
    }
    
    
}
