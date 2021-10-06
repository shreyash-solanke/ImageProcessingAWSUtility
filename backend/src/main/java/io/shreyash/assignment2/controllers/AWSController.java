package io.shreyash.assignment2.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3ObjectSummary;

import io.shreyash.assignment2.aws.S3Wrapper;
import io.shreyash.assignment2.models.ApiResponse;
import io.shreyash.assignment2.models.SqsMessage;
import io.shreyash.assignment2.services.AwsSqsSvc;

@CrossOrigin
@RestController
@RequestMapping("/dashboard/user/{userId}/aws")
public class AWSController {

	@Autowired
	private S3Wrapper s3Wrapper;
	
	@Autowired
	private AwsSqsSvc sqs;
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ApiResponse upload(@RequestParam("file") MultipartFile[] multipartFiles, @PathVariable long userId) {
		return s3Wrapper.upload(multipartFiles, userId);
	}
	
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ApiResponse download(@RequestParam String key) throws IOException {
		return s3Wrapper.download(key);
	}

	@RequestMapping(value = "/delete/{imgId}", method = RequestMethod.GET)
	public ApiResponse delete(@PathVariable("userId") long userId, @PathVariable("imgId") long imgId) {
		return s3Wrapper.delete(userId, imgId);
	}
	
	@RequestMapping(value = "/sendjob", method = RequestMethod.POST, produces = "application/json")
	public ApiResponse sendJob(@PathVariable long userId, @RequestBody SqsMessage message) {
		return sqs.sendMessage(userId,message);
		
	}
	
	
	//testing purpose, shows all images in bucket
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<S3ObjectSummary> list() throws IOException {
		return s3Wrapper.list();
	}
	
	
}
