package com.beta.vn.socialnetwork.model;

import java.io.Serializable;

import com.beta.vn.socialnetwork.entity.Image;
import com.beta.vn.socialnetwork.entity.Post;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PostRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Post post;
	private Image[] images;
	private String[] img_load;
	
	@JsonCreator
	public PostRequest(@JsonProperty("post") Post post, @JsonProperty("images") Image[] images, @JsonProperty("img_load") String[] img_load){
		this.post = post;
		this.images = images;
		this.img_load = img_load;
	}

	public Post getPost() {
		return post;
	}

	public Image[] getImages() {
		return images;
	}

	public String[] getImg_load() {
		return img_load;
	}
	
	
}
