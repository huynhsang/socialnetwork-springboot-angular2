package com.beta.vn.socialnetwork.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchRequest implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private final String inputStr;
	
	@JsonCreator
	public SearchRequest(@JsonProperty("input") String inputStr){
		this.inputStr = inputStr;
	}

	public String getInputStr() {
		return inputStr;
	}
	
	
}
