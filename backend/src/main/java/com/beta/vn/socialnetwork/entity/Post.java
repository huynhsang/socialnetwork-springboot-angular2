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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="post")
public class Post implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	@JsonIgnore
	private int id;
	
	@Column(name = "title", nullable = false)
	@JsonProperty("title")
	private String title;
	
	@Column(name = "content", nullable = false)
	@JsonProperty("content")
	private String content;
	
	@Column(name = "created", nullable = false)
	@JsonProperty("created")
	private Date created;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore()
	private User user;	
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	@JsonIgnore()
	private List<Image> images;
	
	public Post(){}
	
	public Post(String title, String content, Date created, User user){
		this.title = title;
		this.content = content;
		this.created = created;
		this.user = user;
	}
	
	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public Date getCreated() {
		return created;
	}

	public User getUser() {
		return user;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

}
