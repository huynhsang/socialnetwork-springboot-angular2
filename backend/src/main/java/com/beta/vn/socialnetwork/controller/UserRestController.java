package com.beta.vn.socialnetwork.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beta.vn.socialnetwork.entity.Relationship;
import com.beta.vn.socialnetwork.entity.StatusFriend;
import com.beta.vn.socialnetwork.entity.User;
import com.beta.vn.socialnetwork.model.RelationshipRequest;
import com.beta.vn.socialnetwork.model.ResponseFormat;
import com.beta.vn.socialnetwork.model.UserContext;
import com.beta.vn.socialnetwork.repository.RelationshipDAO;
import com.beta.vn.socialnetwork.repository.UserDAO;
import com.beta.vn.socialnetwork.utils.JsonUtils;
import com.beta.vn.socialnetwork.utils.JwtTokenUtils;
import com.beta.vn.socialnetwork.utils.SearchRDF;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class UserRestController {
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private RelationshipDAO relationshipDAO;
	
	@Value("$variable.security.jwt.header")
	private String header;
	
	@Autowired
    private JwtTokenUtils jwtTokenUtil;
	
	
	@RequestMapping(value = "/api/user/{id}", method = RequestMethod.GET)
	public @ResponseBody String getCurrentUser(@PathVariable int id){
		
		User user = userDAO.findOne(id);
		return JsonUtils.userDetailToJSON("user", true, user);
	}
	
	
	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
	@RequestMapping(value = "/api/user/relationship/create", method = RequestMethod.POST,  produces = "application/json")
	public ResponseEntity<?> createRelationship(@RequestBody RelationshipRequest request){
		User user1 = userDAO.findOne(request.getUser_one_id());
		User user2 = userDAO.findOne(request.getUser_two_id());
		List<Relationship> relationships = user1.getRelationships(); 

		relationships.add(new Relationship(user1, request.getUser_two_id(), request.getStatus(), request.getAction_user_id()));
		user1.setRelationships(relationships);
		userDAO.save(user1);
		
		relationships = user2.getRelationships();
		relationships.add(new Relationship(user2, request.getUser_one_id(), request.getStatus(), request.getAction_user_id()));
		
		userDAO.save(user2);
		System.out.println("Complete!");
		return ResponseEntity.ok(new ResponseFormat("relationship", "complete", true));
	}
	
	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
	@RequestMapping(value = "/api/user/relationship/delete", method = RequestMethod.POST,  produces = "application/json")
	public ResponseEntity<?> deleteRelationship(@RequestBody RelationshipRequest request){
		Relationship r1 = relationshipDAO.getByFiendId(request.getUser_one_id(), request.getUser_two_id());
		Relationship r2 = relationshipDAO.getByFiendId(request.getUser_two_id(), request.getUser_one_id());
		
		relationshipDAO.delete(r1);
		relationshipDAO.delete(r2);
		
		
		return ResponseEntity.ok(new ResponseFormat("relationship", "complete", true));
	}
	
	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
	@RequestMapping(value = "/api/user/relationship/confirm", method = RequestMethod.POST,  produces = "application/json")
	public ResponseEntity<?> updateRelationship(@RequestBody RelationshipRequest request){
		Relationship r1 = relationshipDAO.getByFiendId(request.getUser_one_id(), request.getUser_two_id());
		Relationship r2 = relationshipDAO.getByFiendId(request.getUser_two_id(), request.getUser_one_id());
		
		if(request.getStatus().equals(StatusFriend.ACCEPTED.name())){
			r1.setStatus(StatusFriend.ACCEPTED.name());
			r1.setActionUserId(request.getAction_user_id());
			r2.setStatus(StatusFriend.ACCEPTED.name());
			r2.setActionUserId(request.getAction_user_id());
			relationshipDAO.save(r1);
			relationshipDAO.save(r2);
			return ResponseEntity.ok(new ResponseFormat("relationship", "complete", true));
		}
		if(request.getStatus().equals(StatusFriend.DECLINED.name())){
			relationshipDAO.delete(r1);
			relationshipDAO.delete(r2);
		}
		return ResponseEntity.ok(new ResponseFormat("relationship", "complete", true));
	}
	
	
	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
	@RequestMapping(value = "/api/user/relationship/listfriend", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<?> listRelationship(){
		
		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Relationship> rels = relationshipDAO.getByUserId(userContext.getId());
		List<User> listFriends = new ArrayList<User>();
		Relationship temp;
		for(int i=0; i<rels.size(); i++){
			temp = rels.get(i);
			if(temp.getStatus().equals(StatusFriend.ACCEPTED.name())){
				listFriends.add(userDAO.findOne(temp.getFriendId()));
			}
		}
		
		String[] list = new String[listFriends.size()];
		
		for(int i=0; i<listFriends.size(); i++){
			list[i] = JsonUtils.userToJSON(listFriends.get(i));
		}
		
		String response = JsonUtils.arrayToJson("listFriend", true, list);
		
		
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
	@RequestMapping(value = "/api/user/peopleknows", method = RequestMethod.GET,  produces = "application/json")
	public ResponseEntity<?> getPeopleMayKnow(){
		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<User> peopleKnows = SearchRDF.getPeopleMayKnow(userContext.getEmail());
		String[] list = new String[peopleKnows.size()];
		
		for(int i=0; i<peopleKnows.size(); i++){
			list[i] = JsonUtils.userToJSON(peopleKnows.get(i));
		}
		
		String response = JsonUtils.arrayToJson("ListPeopleKnows", true, list);
		return ResponseEntity.ok(response);
	}
}
