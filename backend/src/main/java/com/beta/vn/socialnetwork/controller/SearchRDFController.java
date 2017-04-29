package com.beta.vn.socialnetwork.controller;



import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.beta.vn.socialnetwork.entity.User;
import com.beta.vn.socialnetwork.utils.JsonUtils;
import com.beta.vn.socialnetwork.utils.SearchRDF;

@Controller
public class SearchRDFController {
	
	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
	@RequestMapping(value = "/api/search", method = RequestMethod.POST)
	public ResponseEntity<?> handleSearch(@RequestBody String inputStr){
 		List<User> users = SearchRDF.handleInputSearch(inputStr);
 		
 		String[] list = new String[users.size()];
		
		for(int i=0; i<users.size(); i++){
			list[i] = JsonUtils.userToJSON(users.get(i));
		}
		
		String response = JsonUtils.arrayToJson("ListPeopleKnows", true, list);
		return ResponseEntity.ok(response);
	}
}
