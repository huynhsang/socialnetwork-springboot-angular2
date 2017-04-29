package com.beta.vn.socialnetwork.model;


import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.beta.vn.socialnetwork.entity.Post;
import com.beta.vn.socialnetwork.entity.Relationship;

public class UserContext implements UserDetails, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int id;
	private final String email;
	private final String password;
	private final String givenName;
	private final String familyName;
	private final Date	born;
	private final String address;
	private final String phone;
	private final String avatar;
	private final boolean enabled;
	private final Collection<? extends GrantedAuthority> authorities;
	private final List<Post> posts;
	private final List<Relationship> relationships;
	
	private UserContext(int id, String email, String password, String givenName, String familyName,
			Date born, String address, String phone , String avatar, boolean enabled, Collection<? extends GrantedAuthority> authorities,
			List<Post> posts, List<Relationship> relationships){
		this.id = id;
		this.email = email;
		this.password = password;
		this.givenName = givenName;
		this.familyName = familyName;
		this.born = born;
		this.address = address;
		this.phone = phone;
		this.avatar = avatar;
		this.enabled = enabled;
		this.authorities = authorities;
		this.posts = posts;
		this.relationships = relationships;
	}
	
	public static UserContext create(int id, String email, String password, String givenName, String familyName,
			Date born, String address, String phone , String avatar, boolean enabled, Collection<? extends GrantedAuthority> authorities, List<Post> posts, List<Relationship> relationships){
		 if (StringUtils.isBlank(email)) throw new IllegalArgumentException("Email is blank: " + email);
		 return new UserContext(id, email, password, givenName, familyName, born, address, phone, avatar, enabled, authorities, posts, relationships);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.enabled;
	}

	public int getId(){
		return id;
	}
	
	public String getEmail() {
		return email;
	}

	public String getGivenName() {
		return givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public Date getBorn() {
		return born;
	}

	public String getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}
	
	public String getAvatar(){
		return avatar;
	}
	
	public List<Post> getPosts() {
		return posts;
	}

	public List<Relationship> getRelationships() {
		return relationships;
	}
	
	

}
