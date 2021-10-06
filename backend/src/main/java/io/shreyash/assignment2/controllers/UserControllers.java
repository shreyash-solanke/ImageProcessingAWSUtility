package io.shreyash.assignment2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.shreyash.assignment2.models.ApiResponse;
import io.shreyash.assignment2.models.InputUserLogin;
import io.shreyash.assignment2.models.User;
import io.shreyash.assignment2.services.UserDetailsSvc;

@CrossOrigin
@RestController
public class UserControllers {

	@Autowired
	private UserDetailsSvc userDetailsSvc;
	
	@RequestMapping(value="/register", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResponse addUser(@RequestBody User user) {
		return userDetailsSvc.addUser(user);
	}
	
	@RequestMapping(value = "/delete/{userId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResponse deleteUser(@PathVariable long userId){
		return userDetailsSvc.deleteUser(userId);
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResponse showAllUsers(){
		return userDetailsSvc.showAllUsers();
	}

	@RequestMapping(value="/authenticate", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResponse authenticateUser(@RequestBody InputUserLogin input) {
		return userDetailsSvc.authenticateUser(input);
	}
	
	@RequestMapping(value = "/dashboard/user/{userId}/details", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResponse profileUser(@PathVariable long userId){
		return userDetailsSvc.profileUser(userId);
	}

}
