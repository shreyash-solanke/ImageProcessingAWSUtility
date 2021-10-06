package io.shreyash.imageaws;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ImageProcessingAwsLambda implements RequestHandler<UserInput, String> {
	
	private String operationType;
	private String email;
	private long userId;
	
	public String handleRequest(UserInput input, Context context) {
		
		SqsProcessing sqsProc = new SqsProcessing();
    	S3Processing s3Proc = new S3Processing();
    	ImageProcessing imgProc = new ImageProcessing();
    	Ec2Processing ec2Proc = new Ec2Processing();
    	SendEmail em = new SendEmail();
    
    	
    	List<SqsMessage> sqsMsgs = sqsProc.getSqsMessages();
   
    	sqsMsgs.forEach(sqsmsg-> {
    		
    		operationType = sqsmsg.getOperationType();
    		Map<String, BufferedImage> buffImgs = s3Proc.getImages(sqsmsg);
    		buffImgs.forEach(
    				(imgName,buffImg)-> 
    				{
    			
	    			BufferedImage transformedImg = null;
	    			switch (operationType) {
					case "Resize":
						transformedImg = imgProc.resizeImg(buffImg);
						break;
	
					case "Crop":
						transformedImg = imgProc.cropImg(buffImg);
						break;
						
					case "Gray":
						transformedImg = imgProc.grayScaleImg(buffImg);
						break;
						
					default:
						System.out.println("Invalid Operation");
						break;
				}
    			
    			
    			try {
					String uploadedImg = s3Proc.uploadImage(imgName,transformedImg);
					ec2Proc.updateDatabase(uploadedImg, imgName, sqsmsg.getJobId());
					em.sendmail(sqsmsg.getEmail());
					
				} catch (IOException e) {
					e.printStackTrace();
				}
    			
    		});
    		
    	});
    	
		return "Success";
	}
	
	
	
}
