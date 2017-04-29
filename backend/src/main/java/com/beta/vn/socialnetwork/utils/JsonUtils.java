package com.beta.vn.socialnetwork.utils;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beta.vn.socialnetwork.entity.Image;
import com.beta.vn.socialnetwork.entity.Post;
import com.beta.vn.socialnetwork.entity.Relationship;
import com.beta.vn.socialnetwork.entity.User;

public class JsonUtils {
	public static String userToJSON(User object){
		
		JSONObject obj = new JSONObject();
		try{
			
			obj.put("id", object.getId());
			obj.put("email", object.getEmail());
			obj.put("givenName", object.getGivenName());
			obj.put("familyName", object.getFamilyName());
			obj.put("born", object.getBorn());
			obj.put("address", object.getAddress());
			obj.put("phone", object.getPhone());
			obj.put("avatar", object.getAvatar());
		}catch(JSONException e){
			
		}
		
		return obj.toString();
	}
	
	
	public static String userDetailToJSON(String tag, boolean status, User object){
		JSONObject main  = new JSONObject();
		JSONObject obj = new JSONObject();
		JSONArray array = new JSONArray();
		JSONArray posts = new JSONArray();
		JSONArray relationships = new JSONArray();
		try{
			main.put("tag", tag);
			main.put("status", new Boolean(status));
			List<Relationship> listRel = object.getRelationships();
			for(int r=0; r<listRel.size(); r++){
				relationships.put(relationshipToJson(listRel.get(r)));
			}
			
			List<Post> listPost = object.getPosts();
			for(int k=0; k<object.getPosts().size(); k++){
				posts.put(postToJSON(listPost.get(k)));
			}
			
			for(int i=0; i<object.getAuthorities().size(); i++){
				array.put(object.getAuthorities().get(i).getName());
			}
			obj.put("id", object.getId());
			obj.put("email", object.getEmail());
			obj.put("givenName", object.getGivenName());
			obj.put("familyName", object.getFamilyName());
			obj.put("born", object.getBorn());
			obj.put("address", object.getAddress());
			obj.put("phone", object.getPhone());
			obj.put("avatar", object.getAvatar());
			obj.put("posts", posts);
			obj.put("relationships", relationships);
			
			main.put("data", obj);
		}catch(JSONException e){
		}
		
		return main.toString();
	}
	
	public static String postToJSON(Post post){
		JSONObject obj = new JSONObject();
		JSONObject owner = new JSONObject();
		JSONArray images = new JSONArray();
		
		
		try{
			for(Iterator<Image> items = post.getImages().iterator(); items.hasNext();){
				Image item = items.next();
				JSONObject img = new JSONObject();
				img.put("id", item.getId());
				img.put("name", item.getName());
				img.put("type", item.getType());
				img.put("path", item.getPath());
				img.put("created",  item.getCreated());
				img.put("post_id", item.getPost().getId());
				images.put(img);
			}
			
			owner.put("id", post.getUser().getId());
			owner.put("givenName", post.getUser().getGivenName());
			owner.put("familyName", post.getUser().getFamilyName());
			owner.put("email", post.getUser().getEmail());
			owner.put("avatar", post.getUser().getAvatar());
			
			obj.put("id", post.getId());
			obj.put("title", post.getTitle());
			obj.put("content", post.getContent());
			obj.put("created", post.getCreated().toString());
			obj.put("owner", owner);
			obj.put("images", images);
			
		}catch(JSONException e){
			System.out.println(e.getMessage());
		}
		
		return obj.toString();
	}
	
	public static String relationshipToJson(Relationship rel){
		JSONObject obj = new JSONObject();
		try {
			obj.put("user_one_id", rel.getUser().getId());
			obj.put("user_two_id", rel.getFriendId());
			obj.put("status", rel.getStatus());
			obj.put("action_user_id", rel.getActionUserId());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj.toString();
	}
	
	public static String arrayToJson(String tag, boolean status, String[] list){
		JSONObject main = new JSONObject();
		JSONArray array = new JSONArray();
		for(String item : list){
			array.put(item);
		}
		try {
			main.put("tag", tag);
			main.put("status", status);
			main.put("data", array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return main.toString();
	}
}
