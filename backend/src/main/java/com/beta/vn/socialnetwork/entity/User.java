package com.beta.vn.socialnetwork.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table(name="user")
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", nullable = false)
	@JsonProperty("id")
	private int id;
	
	@JsonProperty("email")
	@Column(name="email", nullable = false)
	private String email;
	
	@JsonProperty("password")
	@Column(name="password", nullable = false)
	private String password;
	
	@JsonProperty("givenName")
	@Column(name="givenname", nullable = false)
	private String givenName;
	
	@JsonProperty("familyName")
	@Column(name="familyname", nullable = false)
	private String familyName;
	
	@JsonProperty("born")
	@Column(name="born", nullable = false)
	private Date born;
	
	@JsonProperty("address")
	@Column(name="address", nullable = false)
	private String address;
	
	@JsonProperty("phone")
	@Column(name="phone")
	private String phone;
	
	@JsonProperty("avatar")
	@Column(name="avatar")
	private String avatar;
	
	@JsonProperty("enabled")
	@Column(name="enabled")
	private int enabled;
	
	@JsonProperty("authorities")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name="user_role",
			joinColumns = {@JoinColumn(name="user_id", referencedColumnName="id")},
			inverseJoinColumns = {@JoinColumn(name="authority_id", referencedColumnName="id")})
	private List<Authority> authorities;
	
	@JsonProperty("posts")
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Post> posts;
	
	@JsonProperty("relationships")
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Relationship> relationships;
	
	public User(){}
	
	public User(String email, String password, String givenName, String familyName, Date born,
			String address, String phone, String avatar, int enabled){
		this.email = email;
		this.password = password;
		this.givenName = givenName;
		this.familyName = familyName;
		this.born = born;
		this.address = address;
		this.phone = phone;
		this.avatar = avatar;
		this.enabled = enabled;
	}
	
	public User(int id, String email, String givenName, String familyName, Date born, String address, String phone, String avatar){
		this.id = id;
		this.email = email;
		this.givenName = givenName;
		this.familyName = familyName;
		this.born = born;
		this.address = address;
		this.phone = phone;
		this.avatar = avatar;
	}
	
	
	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public Date getBorn() {
		return born;
	}

	public void setBorn(Date born) {
		this.born = born;
	}

	public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
 
    public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

    public Boolean isEnabled() {
    	if(enabled == 1) return true;
    	else return false;
    }

    public void setEnabled(Boolean enabled) {
    	if(enabled) this.enabled = 1;
    	else this.enabled = 0;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<Relationship> getRelationships() {
		return relationships;
	}

	public void setRelationships(List<Relationship> relationships) {
		this.relationships = relationships;
	}

    
    
	/*@Override
	public String toString() {
		String info = String.format("{\"id\":%d, \"email\":\"%s\", \"password\":\"%s\", \"givenName\":\"%s\","
				+ "\"familyName\":\"%s\", \"born\":\"%s\", \"address\":\"%s\", \"phone\":\"%s\", \"enabled\":%d, \"authorities\":\"%s\"", id, email, password, givenName, familyName,
				born.toString(), address, phone, enabled, authorities.get(0).getName());
		return info;
	}*/
 
    
}
