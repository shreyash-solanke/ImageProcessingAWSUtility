package io.shreyash.assignment2.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.VerifyEmailAddressRequest;

import io.shreyash.assignment2.models.ApiResponse;
import io.shreyash.assignment2.models.InputUserLogin;
import io.shreyash.assignment2.models.JwtUser;
import io.shreyash.assignment2.models.User;
import io.shreyash.assignment2.repositories.UserRepositoy;
import io.shreyash.assignment2.security.JwtService;

@Service
public class UserDetailsSvc {

	@Autowired
	private UserRepositoy userRepositoy;
	
	@Autowired
    private JwtService jwtService;
	
	@Autowired 
	private UserValidator userValidator;
	
	@Autowired
	private AmazonSimpleEmailServiceClient amazonSESclient;
	
	public ApiResponse addUser(User user) {
		String userId = "";
	    try {
	      user = new User(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
	      userRepositoy.save(user);
	      userId = String.valueOf(user.getUserId());
	      verifyEmailAddress(user.getEmail());
	      ApiResponse aresp = new ApiResponse(true, userId, "User registered successfuly");
	      return aresp;
	    }
	    catch (Exception ex) {
	    	System.out.println(ex);
	    	ApiResponse aresp = new ApiResponse(false, ex.toString(), "User not registered");
		    return aresp;
	    }
	  
	}
	
	public ApiResponse deleteUser(long userId) {
	    try {
	      User user = new User(userId);
	      userRepositoy.delete(user);
	      
	      ApiResponse aresp = new ApiResponse(true, userId, "User deleted successfuly");
	      return aresp;
	    }
	    catch (Exception ex) {
	    	ApiResponse aresp = new ApiResponse(false, userId, "User not deleted");
		    return aresp;
	    }
	  }
	
	public ApiResponse showAllUsers() {
		
		List<User> users = new ArrayList<>();
		try {
			
			userRepositoy.findAll()
			.forEach(users::add);
			
			ApiResponse aresp = new ApiResponse(true, users, "All users displayed successfuly");
			return aresp;
		}
		catch (Exception ex) {
			ApiResponse aresp = new ApiResponse(false, null, "Error showing all users");
			return aresp;
		}
	}
	
	public ApiResponse authenticateUser(InputUserLogin input) {
		
		try {
			User user = userRepositoy.findByEmail(input.getInputEmail());
			if(user.getPassword().equals(input.getInputPassword()))
			{	
				//here we are creating JWT token and returning it in response
				JwtUser jwtUser = new JwtUser(user.getEmail(), user.getUserId());
				ApiResponse aresp = new ApiResponse(true, "Bearer "+jwtService.getToken(jwtUser), "User logged in successfuly");
			    return aresp;
			}
			else
			{
				ApiResponse aresp = new ApiResponse(false, HttpStatus.UNAUTHORIZED, "Invalid Email Address/password");
			    return aresp;
			}
		}
		catch (Exception ex) {
			ApiResponse aresp = new ApiResponse(false, null, "Invalid Email Address/password");
		    return aresp;		
		    }
	}

	public ApiResponse profileUser(long userId) {
		
		if (userValidator.validateUser(userId)) {
			
			User detailedUser = userValidator.getValidatedUser(userId);
			return new ApiResponse(true, detailedUser, "User details returned successfully");
		}
		else {
			
			return new ApiResponse(false, null, "Invalid User");
		}
		
	}
	
	//--------------- verify registered email ------------------//
	private void verifyEmailAddress(String address) {
		
		String domain = address.substring(address.indexOf("@") + 1);

		List lids = amazonSESclient.listIdentities().getIdentities();

			if (lids.contains(address) || lids.contains(domain)) {
				return;
			}

			amazonSESclient.verifyEmailAddress(new VerifyEmailAddressRequest()
					.withEmailAddress(address));
			System.out.println("Please check the email address " + address
		    + " to verify it");
		  
	}
	
}
