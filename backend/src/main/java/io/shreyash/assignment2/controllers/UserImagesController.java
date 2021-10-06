package io.shreyash.assignment2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.shreyash.assignment2.models.ApiResponse;
import io.shreyash.assignment2.services.UserImagesSvc;

@CrossOrigin
@RestController
@RequestMapping("/dashboard/user/{userId}/aws")
public class UserImagesController {
	
	@Autowired
	private UserImagesSvc userImagesSvc;
	
	@RequestMapping(value = "/allimages", method = RequestMethod.GET)
	public ApiResponse showAllImages(@PathVariable long userId){
		return userImagesSvc.showAllImages(userId);
	}
	
	@RequestMapping(value = "/getjob/{jobId}", method = RequestMethod.GET)
	public ApiResponse getJob(@PathVariable("userId") long userId, @PathVariable("jobId") long jobId){
		return userImagesSvc.getJob(userId,jobId);
	}
	
}
