package com.beta.vn.socialnetwork.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.VCARD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.beta.vn.socialnetwork.entity.Authority;
import com.beta.vn.socialnetwork.entity.Relationship;
import com.beta.vn.socialnetwork.entity.StatusFriend;
import com.beta.vn.socialnetwork.entity.User;
import com.beta.vn.socialnetwork.repository.AuthorityDAO;
import com.beta.vn.socialnetwork.repository.UserDAO;
import com.beta.vn.socialnetwork.utils.SearchRDF;


@Component
public class DataSeedingListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AuthorityDAO authDAO;

    @Autowired
	private PasswordEncoder passwordEncoder;

    @Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		
    	createRDF(userDAO.findAll());
    	
		// Admin account
	    if (userDAO.findByEmail("admin@admin.com") == null) {
	        User admin = new User("admin@admin.com", passwordEncoder.encode("admin"), "Sang",
	        		"Huynh", parseDate("1995/11/12"), "Da Nang, Viet Nam", "0935162625", "http://localhost:8080/getImage/avatar_default.jpg", 1);
	        List<Authority> authorities = new ArrayList<Authority>();
	        authorities.add(authDAO.findOne(1));
	        admin.setAuthorities(authorities);
	        userDAO.save(admin);
	    }
	    
	    // user1 account
	    if (userDAO.findByEmail("user@user.com") == null) {
	    	//Date date = new Date();
	        User user = new User("user@user.com", passwordEncoder.encode("user"), "Gumiho",
	        		"Le", parseDate("1995/10/21"), "Da Nang, Viet Nam", "0934876654", "http://localhost:8080/getImage/avatar_default.jpg", 1);
	        List<Authority> authorities = new ArrayList<Authority>();
	        authorities.add(authDAO.findOne(2));
	        user.setAuthorities(authorities);
	        userDAO.save(user);
	    }
	    
	    // user2 account
	    if (userDAO.findByEmail("sanght@gmail.com") == null) {
	    	//Date date = new Date();
	        User user = new User("sanght@gmail.com", passwordEncoder.encode("sang"), "Sang",
	        		"Huynh", parseDate("1995/12/11"), "Da Nang, Viet Nam", "0935162625", "http://localhost:8080/getImage/avatar_default.jpg", 1);
	        List<Authority> authorities = new ArrayList<Authority>();
	        authorities.add(authDAO.findOne(2));
	        user.setAuthorities(authorities);
	        userDAO.save(user);
	    }
	    
	    // user3 account
	    if (userDAO.findByEmail("quangtd@gmail.com") == null) {
	    	//Date date = new Date();
	        User user = new User("quangtd@gmail.com", passwordEncoder.encode("quang"), "Quang",
	        		"Thai", parseDate("1995/02/28"), "Da Nang, Viet Nam", "01263752044", "http://localhost:8080/getImage/avatar_default.jpg", 1);
	        List<Authority> authorities = new ArrayList<Authority>();
	        authorities.add(authDAO.findOne(2));
	        user.setAuthorities(authorities);
	        userDAO.save(user);
	    }
	}
    
    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {
            return null;
        }
     }

	public static void createRDF(List<User> users){
    	String path = System.getProperty("user.dir").toString() + "\\storage\\rdf\\db.rdf";
    	Model model = ModelFactory.createDefaultModel();
    	
    	for(int i=0; i<users.size(); i++){
    		User user = users.get(i);
    		String uri = "http://socialnetwork.org/"+users.get(i).getId();
    		Resource resource = model.createResource(uri, FOAF.Person)
    			.addProperty(FOAF.accountName, user.getEmail())
    			.addProperty(FOAF.givenname, user.getGivenName())
    			.addProperty(FOAF.family_name, user.getFamilyName())
    			.addProperty(FOAF.birthday, user.getBorn().toString())
    			.addProperty(FOAF.img, user.getAvatar())
    			.addProperty(FOAF.based_near, user.getAddress())
    			.addProperty(FOAF.phone, user.getPhone());
    		//System.out.println(" Friends of this user: "+ user.getRelationships().size());
    		createQuestionData(user);
    		List<Relationship> rels = user.getRelationships();
    		for(int j=0; j<rels.size(); j++){
    			Relationship rel = rels.get(j);
    			if(isFriend(rel)){
    				String uri_knows = "http://socialnetwork.org/"+rel.getFriendId();
    				resource.addProperty(FOAF.knows, model.createResource(uri_knows));
    				//System.out.println(getEmailById(users, rel.getFriendId()));
    			}
    		}
    	}
    	
    	System.out.println(SearchRDF.GIVENNAME.size());
    	
    	model.setNsPrefix("foaf", FOAF.NS);
    	FileOutputStream fos = null;
    	try{
    		File file = new File(path);
    		fos = new FileOutputStream(file);
        	if(!file.exists()){
        		file.createNewFile();
        	}
        	model.write(fos, "RDF/XML");
    	}catch(IOException e){
    		e.printStackTrace();
    	}finally {
    		if(fos != null){
        		try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
		}
    }
	
	private static boolean isFriend(Relationship rel){
		if(rel.getStatus().equals(StatusFriend.ACCEPTED.name())){
			return true;
		}
		return false;
	}
	
	private static String getEmailById(List<User> users, int id){
		for(int i=(id-1); i>=0; i--){
			if(users.get(i).getId() == id){
				return users.get(i).getEmail();
			}
		}
		return null;
	}
	
	
	private static void createQuestionData(User user){
		String givenName = user.getGivenName();
		String familyName = user.getFamilyName();
		//String location = user.getAddress().toLowerCase();
		if(!checkExist(SearchRDF.GIVENNAME, givenName)){
			SearchRDF.GIVENNAME.add(givenName);
		}
		if(!checkExist(SearchRDF.FAMILYNAME, familyName)){
			SearchRDF.FAMILYNAME.add(familyName);
		}
	}
	
	private static boolean checkExist(List<String> vocabulary, String str){
		for(String item: vocabulary){
			if(item.toLowerCase().equals(str.toLowerCase())){
				return true;
			}
		}
		return false;
	}
    
}