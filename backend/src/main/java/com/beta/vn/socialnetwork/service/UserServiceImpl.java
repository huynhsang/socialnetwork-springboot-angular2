package com.beta.vn.socialnetwork.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.beta.vn.socialnetwork.entity.User;
import com.beta.vn.socialnetwork.model.UserContext;
import com.beta.vn.socialnetwork.repository.UserDAO;


@Service
public class UserServiceImpl implements UserDetailsService{

	private final UserDAO userDAO;
    
    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
	
    public UserDAO getUserDAO(){
    	return userDAO;
    }
    
	@Override
	public UserContext loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userDAO.findByEmail(email);
		if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with Email '%s'.", email));
        } else {
        	return UserContext.create(user.getId(), user.getEmail(), user.getPassword(), user.getGivenName(),user.getFamilyName(),
        			user.getBorn(), user.getAddress(), user.getPhone(), user.getAvatar(), user.isEnabled(), mapAuthorities(user),
        			user.getPosts(), user.getRelationships());
        }
	}
	
	private List<GrantedAuthority> mapAuthorities(User user){
		return user.getAuthorities().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
				.collect(Collectors.toList());
	}

}
