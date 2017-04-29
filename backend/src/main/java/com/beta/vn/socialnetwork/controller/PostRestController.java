package com.beta.vn.socialnetwork.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.beta.vn.socialnetwork.entity.Image;
import com.beta.vn.socialnetwork.entity.Post;
import com.beta.vn.socialnetwork.entity.Relationship;
import com.beta.vn.socialnetwork.entity.StatusFriend;
import com.beta.vn.socialnetwork.entity.User;
import com.beta.vn.socialnetwork.model.PostRequest;
import com.beta.vn.socialnetwork.model.ResponseFormat;
import com.beta.vn.socialnetwork.model.UserContext;
import com.beta.vn.socialnetwork.repository.PostDAO;
import com.beta.vn.socialnetwork.repository.RelationshipDAO;
import com.beta.vn.socialnetwork.repository.UserDAO;
import com.beta.vn.socialnetwork.utils.JsonUtils;

@Controller
public class PostRestController {
	
	@Autowired
	private PostDAO postDAO;
	
	@Autowired
	private RelationshipDAO relationshipDAO;
	
	@Autowired
	private UserDAO userDAO;

	public static final String STORAGE_DIR = System.getProperty("user.dir") + "/storage/";
	
	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
	@RequestMapping(value = "/api/post", method = RequestMethod.GET)
	public ResponseEntity<String> getPosts(){
		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Relationship> rels = relationshipDAO.getByUserId(userContext.getId());
		List<Integer> listUserId = new ArrayList<Integer>();
		listUserId.add(userContext.getId());
		for(Relationship item : rels){
			listUserId.add(item.getFriendId());
		}
		
		List<Post> posts = postDAO.getByUseIds(listUserId);
		String [] listPost = new String[posts.size()];
		for(int i=0; i<listPost.length; i++){
			listPost[i] = JsonUtils.postToJSON(posts.get(i));
		}
		String response = JsonUtils.arrayToJson("post", true, listPost);
		return ResponseEntity.ok(response);
	}
	
	
	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
	@RequestMapping(value = "/api/post/upload", method = RequestMethod.POST)
	public ResponseEntity<?> uploadingPost(@RequestBody PostRequest postRequest) throws IOException {
		
		Image[] images = null;
		Post post = postRequest.getPost();
		String [] img_load = postRequest.getImg_load();
		
		
		if(img_load.length != 0){
			try{
				images = postRequest.getImages();
				for(int i=0; i<images.length; i++){
					String base64Image = img_load[i].split(",")[1];
					byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
					BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
					if(images[i].getType() != "jpg"){
						BufferedImage t_img = new BufferedImage(img.getWidth(), img.getHeight(), img.TYPE_INT_RGB);
						t_img.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
						img = t_img;
					}
					
					File file = new File(STORAGE_DIR + images[i].getName()+ ".jpg");
					ImageIO.write(img, "jpg", file);
					images[i].setPost(post);
					//images[i].setPath("http://localhost:8080/api/getImage/" + images[i].getName());
				}
				post.setImages(Arrays.asList(images));
			}catch(Exception e){
				System.out.println("Error!");
			}
		}
		
		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userDAO.findByEmail(userContext.getUsername());
		post.setUser(user);
		List<Post> currentPosts = user.getPosts();
		currentPosts.add(post);
		user.setPosts(currentPosts);
		userDAO.save(user);
		System.out.println("Completed");
		
        return ResponseEntity.ok(new ResponseFormat("upload", "success", true));
    }
	
	
	@RequestMapping(value = "/getImage/{fileName}", method = RequestMethod.GET)
    public void showImage(@PathVariable String fileName, HttpServletResponse response) throws Exception {
      ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
      try {
        BufferedImage image = ImageIO.read(new File(STORAGE_DIR+ fileName +".jpg"));
        ImageIO.write(image, "jpeg", jpegOutputStream);
      } catch (IllegalArgumentException e) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }

      byte[] imgByte = jpegOutputStream.toByteArray();

      response.setHeader("Cache-Control", "no-store");
      response.setHeader("Pragma", "no-cache");
      response.setDateHeader("Expires", 0);
      response.setContentType("image/jpeg"); //png
      ServletOutputStream responseOutputStream = response.getOutputStream();
      responseOutputStream.write(imgByte);
      responseOutputStream.flush();
      responseOutputStream.close();
    }
}
