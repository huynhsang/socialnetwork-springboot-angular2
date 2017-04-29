package com.beta.vn.socialnetwork.security;

import java.io.Serializable;

import com.beta.vn.socialnetwork.model.UserContext;

public class JwtAuthenticationResponse implements Serializable{

	 private static final long serialVersionUID = 1250166508152483573L;
	 
	 private final String token;
	 private final UserContext user;
	 
	 
	 public JwtAuthenticationResponse(String token, UserContext user) {
	        this.token = token;
	        this.user = user;
	    }

    public String getToken() {
        return this.token;
    }
    
    public UserContext getUser() {
        return this.user;
    }
}
