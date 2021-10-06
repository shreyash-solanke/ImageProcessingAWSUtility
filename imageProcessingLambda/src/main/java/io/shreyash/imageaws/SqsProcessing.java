package io.shreyash.imageaws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class SqsProcessing {

	private AmazonSQSClient amazonSQSClient;
	private String queueURL = "https://sqs.ap-south-1.amazonaws.com/021676460566/assignment-2-image-jobs";
	private String operationType;
	private List<String> imgNames;
	Map<String, MessageAttributeValue> attributes;
	
	
	public SqsProcessing() {
		amazonSQSClient = new AmazonSQSClient();
	}
	
	public List <SqsMessage> getSqsMessages() {
		
		String[] msgAttReq = {"jobId","email"};
		List<SqsMessage> sqsMsgs = new ArrayList<>();
		ReceiveMessageRequest receiveMsgReq = new ReceiveMessageRequest(queueURL)
				.withMessageAttributeNames(msgAttReq)
				.withAttributeNames("");
		boolean flag=true;
		
		List<Message> messages=new ArrayList<Message>();
		while(flag){
				
			List<Message> tempMsgs = amazonSQSClient.receiveMessage(receiveMsgReq).getMessages();
			if(tempMsgs.size()==0)
            {
                flag = false;
            }
			messages.addAll(tempMsgs);
		}	
		
		System.out.println("done getting all messages");
		System.out.println("message list size is"+messages.size());
			
		//deleting messages from queue
		messages.forEach(
				msg-> {
					System.out.println(msg);
					String receiptHandle = msg.getReceiptHandle();
					amazonSQSClient.deleteMessage(queueURL, receiptHandle);
				});

		//generating in sqs messsage format
		messages.forEach(
				(msg)-> {
					SqsMessage sqsMsg;
					String[] body = msg.getBody().split("\n",2);
					operationType = body[0];
					imgNames = Arrays.asList(body[1].split(",[ ]*"));
					attributes = msg.getMessageAttributes();
					String j = attributes.get("jobId").getStringValue();
					String email = attributes.get("email").getStringValue();
					sqsMsg = new SqsMessage(Long.valueOf(j).longValue(),
							operationType,imgNames,email);
					sqsMsgs.add(sqsMsg);
				});
		return sqsMsgs;
	}
}
