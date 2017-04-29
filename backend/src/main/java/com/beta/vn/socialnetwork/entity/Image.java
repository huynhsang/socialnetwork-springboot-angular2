package com.beta.vn.socialnetwork.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="image")
public class Image implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	@JsonIgnore
	private int id;
	
	@Column(name = "name", nullable = false)
	@JsonProperty("name")
	private String name;
	
	@Column(name = "type", nullable = false)
	@JsonProperty("type")
	private String type;
	
	@Column(name = "path", nullable = false)
	@JsonProperty("path")
	private String path;
	
	@Column(name = "created", nullable = false)
	@JsonProperty("created")
	private Date created;
	
	@ManyToOne
	@JoinColumn(name = "post_id")
	@JsonIgnore
	private Post post;	
	
	public Image(){}
	
	public Image(String name, String type, String path, Date created, Post post){
		this.name = name;
		this.type = type;
		this.path = path;
		this.created = created;
		this.post = post;
	}
	
	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getPath() {
		return path;
	}

	public Date getCreated() {
		return created;
	}

	public Post getPost() {
		return post;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	

    
	
}
