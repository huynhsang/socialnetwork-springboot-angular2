package com.beta.vn.socialnetwork.controller;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beta.vn.socialnetwork.entity.Authority;
import com.beta.vn.socialnetwork.entity.User;
import com.beta.vn.socialnetwork.model.UserContext;
import com.beta.vn.socialnetwork.model.token.JwtToken;
import com.beta.vn.socialnetwork.repository.AuthorityDAO;
import com.beta.vn.socialnetwork.repository.UserDAO;
import com.beta.vn.socialnetwork.security.JwtAuthenticationResponse;
import com.beta.vn.socialnetwork.security.LoginRequest;
import com.beta.vn.socialnetwork.security.RegisterRequest;
import com.beta.vn.socialnetwork.service.UserServiceImpl;
import com.beta.vn.socialnetwork.utils.JsonUtils;
import com.beta.vn.socialnetwork.utils.JwtTokenUtils;

@Controller
public class AuthenticationRestController {

	@Autowired
	private JwtTokenUtils jwtTokenUtil;
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
    private AuthorityDAO authDAO;
	
	@Value("${variable.security.jwt.header}")
    private String tokenHeader;
	
	@RequestMapping(value = "api/auth/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken( @RequestBody LoginRequest loginRequest) throws AuthenticationException {
		UserContext userContext = null;
		try{
			userContext = userServiceImpl.loadUserByUsername(loginRequest.getEmail());
			if(!encoder.matches(loginRequest.getPassword(), userContext.getPassword())) throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
		}catch(Exception e){
			return ResponseEntity.badRequest().body("Email & Password invalid!");
		}
		/*        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );*/

        // Reload password post-security so we can generate token
        
        JwtToken token = jwtTokenUtil.createAccessJwtToken(userContext);

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token.getToken(), userContext));

	}
	
	@RequestMapping(value = "api/auth/register", method = RequestMethod.POST)
	public ResponseEntity<?> registerAccount( @RequestBody RegisterRequest registerRequest) throws AuthenticationException {
		UserContext userContext = null;
		if(userDAO.findByEmail(registerRequest.getEmail()) != null){
			return ResponseEntity.badRequest().body("Email already exists!");
		}
		User user = new User(registerRequest.getEmail(), encoder.encode(registerRequest.getPassword()), registerRequest.getGivenName(),
				registerRequest.getFamilyName(), registerRequest.getBorn(), registerRequest.getAddress(), registerRequest.getPhone(),
				"http://localhost:8080/getImage/avatar_default.jpg", 1);
		List<Authority> authorities = new ArrayList<Authority>();
        authorities.add(authDAO.findOne(2));
        user.setAuthorities(authorities);
        userDAO.save(user);
        System.out.println("Completed!");
        userContext = userServiceImpl.loadUserByUsername(registerRequest.getEmail());

        // Reload password post-security so we can generate token
        
        JwtToken token = jwtTokenUtil.createAccessJwtToken(userContext);
        
       
        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token.getToken(), userContext));
    }
	
	
	@RequestMapping(value="/api/auth/token", method = RequestMethod.GET)
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request){
		String token = request.getHeader(tokenHeader);
		String email = jwtTokenUtil.getEmailFromToken(token);
	    UserContext user = (UserContext) userServiceImpl.loadUserByUsername(email);
		if(jwtTokenUtil.isTokenExpired(token)){
			JwtToken refreshedToken = jwtTokenUtil.refreshJwtToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken.getToken(), user));
		}
		
		return ResponseEntity.badRequest().body(null);
	}

}