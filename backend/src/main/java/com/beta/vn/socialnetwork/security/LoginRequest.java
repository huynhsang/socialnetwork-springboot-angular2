package com.beta.vn.socialnetwork.security;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;
	private String password;
	
	@JsonCreator
	public LoginRequest(@JsonProperty("email") String email, @JsonProperty("password") String password){
		this.email = email;
		this.password = password;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public String getPassword(){
		return this.password;
	}
	
}
