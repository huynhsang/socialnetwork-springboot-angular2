package com.beta.vn.socialnetwork.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="relationship")
public class Relationship implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	@JsonIgnore
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "user_one_id", nullable = false)
	@JsonIgnore
	private User user;
	
	
	@Column(name = "user_two_id", nullable = false)
	@JsonProperty("user_two_id")
	private int friendId;
	
	@Column(name = "status", nullable = false)
	@JsonProperty("status")
	private String status;
	
	@Column(name = "action_user_id", nullable = false)
	@JsonProperty("action_user_id")
	private int actionUserId;
	
	//private User friend;
	
	public Relationship(){}
	
	public Relationship(User user, int friendId, String status, int actionUserId){
		this.user = user;
		this.friendId = friendId;
		this.status = status;
		this.actionUserId = actionUserId;
	}

	public int getFriendId() {
		return friendId;
	}

	public User getUser() {
		return user;
	}

	public String getStatus() {
		return status;
	}

	public int getActionUserId() {
		return actionUserId;
	}

	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setActionUserId(int actionUserId) {
		this.actionUserId = actionUserId;
	}

	/*public User getFriend() {
		return friend;
	}

	public void setFriend(User friend) {
		this.friend = friend;
	}*/
	
	
	
	
}

