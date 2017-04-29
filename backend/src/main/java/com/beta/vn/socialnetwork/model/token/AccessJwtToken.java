package com.beta.vn.socialnetwork.model.token;

import io.jsonwebtoken.Claims;

public class AccessJwtToken implements JwtToken{
	private final String accessToken;
	private Claims claims;

	public AccessJwtToken(String token, Claims claims) {
		// TODO Auto-generated constructor stub
		this.accessToken = token;
		this.claims = claims;
	}
	
    public Claims getClaims() {
        return claims;
    }


	@Override
	public String getToken() {
		// TODO Auto-generated method stub
		return accessToken;
	}

}
