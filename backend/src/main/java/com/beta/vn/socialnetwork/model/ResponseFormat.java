package com.beta.vn.socialnetwork.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseFormat implements Serializable{
	
	private static final long serialVersionUID = 1250166508152483573L;
	
	private final String tag;
	private final String data;
	private final boolean status;
	
	public ResponseFormat(@JsonProperty("tag") String tag, @JsonProperty("data") String data, @JsonProperty("status") boolean status){
		this.tag = tag;
		this.data = data;
		this.status = status;
	}

	public String getTag() {
		return tag;
	}

	public String getData() {
		return data;
	}

	public boolean isStatus() {
		return status;
	}
	
	
}
