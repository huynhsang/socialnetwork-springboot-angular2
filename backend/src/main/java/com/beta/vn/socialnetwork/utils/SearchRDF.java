package com.beta.vn.socialnetwork.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.util.FileManager;

import com.beta.vn.socialnetwork.entity.User;

public class SearchRDF {
	
	public static String[] WHO = {"ai", "nguoi", "người", "user", "who"};
	public static List<String> GIVENNAME = new ArrayList<String>();
	public static List<String> FAMILYNAME = new ArrayList<String>();
	public static String[] FRIEND = {"ban", "bạn", "friend", "friends"};
	public static String[] WHERE = {"o", "tai", "ở", "on", "at"};
	public static String[] LOCATION = {"da nang", "đà nẵng", "quang tri", "quảng trị", "hue", "huế", "quang nam", "quảng nam", "viet nam", "việt nam"};
	
	
	private static String path = System.getProperty("user.dir").toString() + "\\storage\\rdf\\db.rdf";
	
	
	
	
	public static List<User> querySearching(String queryStr, boolean actionFriend, String location){
		Model model = init();
		List<User> users = new ArrayList<User>();
		
		Query query = QueryFactory.create(queryStr);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		
		try{
			ResultSet results =  qexec.execSelect();
			while( results.hasNext()){
				QuerySolution soln = results.nextSolution();
				Resource r = null;
				if(actionFriend){
					r = soln.getResource("f");
				}else{
					r = soln.getResource("person");
				};
				if(location != null){
					String position = r.getProperty(FOAF.based_near).getString();
					if(!position.toLowerCase().contains(location)){
						r = null;
					}
				}
				if(r != null){
					users.add(getUserByResource(r));
				}
			}
		}finally{
			qexec.close();
		}
		
		return users;
	}
	
	public static List<User> handleInputSearch(String inputStr){
		boolean actionFriend = false, actionLocation = false;
		String[] input = inputStr.split(" ");
		String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
				+ "SELECT * Where { ";
		
		String str = checkVocabulary(GIVENNAME, input);
		if(str != null){
			query += " ?person foaf:givenname \""+str+"\" .";
		}
		str = checkVocabulary(FAMILYNAME, input);
		if(str != null){
			query += " ?person foaf:family_name \""+str+"\" .";
		}
		
		if(checkAction(WHERE, input)){
			query += " ?person foaf:based_near ?x .";
			str = checkLocation(LOCATION, inputStr);
			if(str != null) actionLocation = true;
		}
		
		if(checkAction(FRIEND, input)){
			query += " ?person foaf:knows ?f .";
			actionFriend = true;
		}
		query += " }";
		
		if(actionLocation) return querySearching(query, actionFriend, str);
		return querySearching(query, actionFriend, null);
		
	}
	
	private static String checkVocabulary(List<String> vocabulary, String [] inputStr){
		for(String input: inputStr){
			for(String item: vocabulary){
				if(input.toLowerCase().equals(item.toLowerCase())){
					return item;
				}
			}
		}
		return null;
	}
	
	private static boolean checkAction(String [] action, String [] inputStr){
		for(int i=0; i < inputStr.length; i++){
			for(int j=0; j<action.length; j++){
				if(inputStr[i].toLowerCase().equals(action[j])){
					return true;
				}
			}
		}
		return false;
	}
	
	private static String checkLocation(String [] location, String inputStr){
		for(String item: location){
			if(inputStr.toLowerCase().contains(item)){
				return item;
			}
		}
		return null;
	}
	
	public static List<User> getPeopleMayKnow(String email){
		Model model = init();
		
		List<String> friends = getListFriendName(model, email);
		List<User> peopleKnows = new ArrayList<User>();
		List<String> mayKnow = new ArrayList<String>();
		
		ResultSet results;
		for(int i=0; i<friends.size(); i++){
			results = queryPeopleMayKnow(model, friends.get(i), email);
			while( results.hasNext()){
				QuerySolution soln = results.nextSolution();
				Resource r = (Resource)  soln.getResource("r");
				Literal name = soln.getLiteral("x");
				String personEmail = name.toString();
				if(!isFriend(friends, personEmail) &&  !isFriend(mayKnow, personEmail)){
					mayKnow.add(personEmail);
					peopleKnows.add(getUserByResource(r));
				}
			}
		}
		
		return peopleKnows;
	}
	
	private static User getUserByResource(Resource r){
		int id = Integer.parseInt(r.toString().substring(25));
		String email = r.getProperty(FOAF.accountName).getString();
		String givenName = r.getProperty(FOAF.givenname).getString();
		String familyName = r.getProperty(FOAF.family_name).getString();
		Date born = null;
		try {
			String[] date =  r.getProperty(FOAF.birthday).getString().split(" ");
			born = new SimpleDateFormat("yyyy-MM-dd").parse(date[0]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String address = r.getProperty(FOAF.based_near).getString();
		String phone = r.getProperty(FOAF.phone).getString();
		String avatar = r.getProperty(FOAF.img).getString();
		User user = new User(id, email, givenName, familyName, born, address, phone, avatar);
		return user;
	}
	
	private static boolean isFriend(List<String> friends, String person){
		for(String str: friends){
			if(str.equals(person)){
				return true;
			}
		}
		return false;
	}
	
	private static ResultSet queryPeopleMayKnow(Model m, String email, String user){
		String queryString = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
				+ "SELECT * Where { "
				+ " ?person foaf:accountName \""+email+"\" ."
				+ " ?person foaf:knows ?r ."
				+ " ?r foaf:accountName ?x ."
				+ " FILTER( ?x != \""+user+"\") "
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, m);
		ResultSet results =  qexec.execSelect();
		return results;
	}
	
	private static List<String> getListFriendName(Model model, String email){
		List<String> friends = new ArrayList<String>();
		String queryStr = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
				+ "SELECT * Where { "
				+ " ?person foaf:accountName \""+email+"\" ."
				+ " ?person foaf:knows ?x ."
				+ "}";
		Query query = QueryFactory.create(queryStr);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try{
			ResultSet results = qexec.execSelect();
			while( results.hasNext()){
				QuerySolution soln = results.nextSolution();
				Resource name = (Resource)  soln.getResource("x");
				String friend = name.getProperty(FOAF.accountName).getString();
				friends.add(friend);
			}
		}finally{
			qexec.close();
		}
		
		return friends;
	}
	
	private static Model init(){
		FileManager.get().addLocatorClassLoader(SearchRDF.class.getClassLoader());
		Model model = FileManager.get().loadModel(path);
		return model;
	}
}
