package io.shreyash.assignment2.services;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.shreyash.assignment2.models.JwtUser;
import io.shreyash.assignment2.models.User;
import io.shreyash.assignment2.repositories.UserRepositoy;

@Service
public class UserValidator {
	
	@Autowired
	private UserRepositoy userRepositoy;

	
	public boolean validateUser(long userId) {
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.
				getRequestAttributes()).getRequest();
		JwtUser user = (JwtUser) request.getAttribute("jwtUser");
		
		try {
			User detailedUser = userRepositoy.findOne(user.getUserId());
			
			if(detailedUser.getUserId()==userId) {
				
				return true;
			}
			else {
				return false;
			}
	  
	    }
	    catch (Exception ex) {
	    	System.out.println(ex);
	    	return false;
	    }
		
	}
	
	public User getValidatedUser(long userId) {
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.
				getRequestAttributes()).getRequest();
		JwtUser user = (JwtUser) request.getAttribute("jwtUser");
		
		User detailedUser = userRepositoy.findOne(user.getUserId());
		return detailedUser;
	}
	
}
