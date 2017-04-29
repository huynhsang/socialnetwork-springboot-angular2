package com.beta.vn.socialnetwork.security;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;
	private String password;
	private Date born;
	private String givenName;
	private String familyName;
	private String address;
	private String phone;
	
	@JsonCreator
	public RegisterRequest(@JsonProperty("email") String email, @JsonProperty("password") String password, @JsonProperty("born") String born,
			@JsonProperty("givenName") String givenName, @JsonProperty("familyName") String familyName, @JsonProperty("address") String address,
			@JsonProperty("phone") String phone){
		this.email = email;
		this.password = password;
		try {
			this.born = new SimpleDateFormat("yyyy/MM/dd").parse(born);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.givenName = givenName;
		this.familyName = familyName;
		this.address = address;
		this.phone = phone;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public String getPassword(){
		return this.password;
	}

	public Date getBorn() {
		return born;
	}

	public String getGivenName() {
		return givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public String getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}
	
	
	
}
