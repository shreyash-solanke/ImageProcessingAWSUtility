package io.shreyash.assignment2.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import io.shreyash.assignment2.models.ApiResponse;
import io.shreyash.assignment2.models.SqsMessage;
import io.shreyash.assignment2.models.TransformedImages;
import io.shreyash.assignment2.models.User;
import io.shreyash.assignment2.repositories.TransformedImageRepository;

@Service
public class AwsSqsSvc {

	@Autowired
	private AmazonSQSClient amazonSQSClient;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	TransformedImageRepository tImgRepo;
	
	private String queueName = "assignment-2-image-jobs";
	
	
	public ApiResponse sendMessage(long userId, SqsMessage message) {
		
		try {
		
			if(userValidator.validateUser(userId)){
				
				User detailedUser = userValidator.getValidatedUser(userId);
				
				GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest(queueName);
				String queueUrl = amazonSQSClient.getQueueUrl(getQueueUrlRequest).getQueueUrl();
				
				String operationType = message.getoperationType();
				String strMessage = operationType +
						"\n"+Arrays.asList(message.getImgNames()).toString();
				
				strMessage=strMessage.replaceAll("\\[", "").replaceAll("\\]","");
				
				//generating unique job id
				Date date = new Date();
				long jobId = date.getTime();
				
				Map<String,MessageAttributeValue> messageAttributes = new HashMap<>();
				messageAttributes.put("email", new MessageAttributeValue().withDataType("String")
						.withStringValue(detailedUser.getEmail()));
				
				String t = String.valueOf(jobId);
				messageAttributes.put("jobId", new MessageAttributeValue().withDataType("Number")
						.withStringValue(t));
				
				SendMessageRequest sendMessageRequest = new SendMessageRequest()
						.withQueueUrl(queueUrl)
						.withMessageBody(strMessage)
						.withMessageAttributes(messageAttributes);
				
				amazonSQSClient.sendMessage(sendMessageRequest);
				
				ArrayList<String> tempImageList = new ArrayList<>( Arrays.asList(message.getImgNames()));
				
				//for each image in message, add entry in database
				tempImageList.forEach(
						(img)-> {
							TransformedImages transformedImage = 
									new TransformedImages(detailedUser,jobId,operationType,img,0); //0 for not processed yet
							tImgRepo.save(transformedImage);
						});
				
				return new ApiResponse(true, jobId, "Message sent successfully");
	
			}
			else {
				return new ApiResponse(false, null, "Invalid User");
			}
		}
		catch (Exception ex) {
			return new ApiResponse(false, ex.toString(), "Message not sent");
		}
	}

}
