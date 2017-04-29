package com.beta.vn.socialnetwork.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RelationshipRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int user_one_id;
	private final int user_two_id;
	private final int action_user_id;
	private final String status;
	
	@JsonCreator
	public RelationshipRequest(@JsonProperty("user_one_id") int user_one_id, @JsonProperty("user_two_id") int user_two_id,
			@JsonProperty("status") String status, @JsonProperty("action_user_id") int action_user_id){
		this.user_one_id = user_one_id;
		this.user_two_id = user_two_id;
		this.status = status;
		this.action_user_id = action_user_id;
	}
	

	public int getUser_one_id() {
		return user_one_id;
	}



	public int getUser_two_id() {
		return user_two_id;
	}



	public int getAction_user_id() {
		return action_user_id;
	}



	public String getStatus() {
		return status;
	}
	
	
}