package com.beta.vn.socialnetwork.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.beta.vn.socialnetwork.model.UserContext;
import com.beta.vn.socialnetwork.model.token.JwtToken;
import com.beta.vn.socialnetwork.service.UserServiceImpl;
import com.beta.vn.socialnetwork.utils.JwtTokenUtils;



public class JwtAuthenticationTokenFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtTokenUtils jwtTokenUtil;
	
	@Autowired
	private UserServiceImpl userServiceImpl;


	@Value("${variable.security.jwt.header}")
    private String tokenHeader;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String header = request.getHeader(this.tokenHeader);
		String authToken=null;
        if(header != null && header.startsWith("Bearer ")){
        	authToken = header.substring(7);
        }
        String email = jwtTokenUtil.getEmailFromToken(authToken);

        /*logger.info("checking authentication fr user " + email);
        logger.info(SecurityContextHolder.getContext().getAuthentication() + " ");*/
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // It is not compelling necessary to load the use details from the database. You could also store the information
            // in the token and read it from it. It's up to you ;)
            UserContext userDetails = this.userServiceImpl.loadUserByUsername(email);

            // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
            // the database compellingly. Again it's up to you ;)
            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info("authenticated user " + email + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
	}	
}
