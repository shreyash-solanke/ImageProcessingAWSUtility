package io.shreyash.assignment2.services;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.shreyash.assignment2.aws.S3Wrapper;
import io.shreyash.assignment2.models.ApiResponse;
import io.shreyash.assignment2.models.SignedImagesURL;
import io.shreyash.assignment2.models.TransformedImages;
import io.shreyash.assignment2.models.User;
import io.shreyash.assignment2.models.UserImages;
import io.shreyash.assignment2.repositories.TransformedImageRepository;
import io.shreyash.assignment2.repositories.UserImagesRepository;


@Service
public class UserImagesSvc {
	
	@Autowired
	private UserImagesRepository userImagesRepository;
	
	@Autowired
	private TransformedImageRepository tImgRepo;
	
	@Autowired
	private S3Wrapper s3Wrapper;
	
	@Autowired
	private UserValidator userValidator;
	
	@Value("${cloud.aws.s3.bucket.inputimages}")
	private String inputImagesBucket;
	
	private String transformedImgBucket = "bucket-2-transformed-images";
	
	int flag;
	
	public ApiResponse showAllImages(long userId) {
		
		
		try {
			
			if(userValidator.validateUser(userId)){
			  
				User detailedUser = userValidator.getValidatedUser(userId);
				List<UserImages> userImages= userImagesRepository
					  .findByUser_UserId(detailedUser.getUserId());
				List<SignedImagesURL> sUrls = new ArrayList<>();
			  
				userImages.forEach( (u)->{
					String imgName = u.getImgName();
					URL surl= s3Wrapper.generateSignedURL(imgName,inputImagesBucket);
					SignedImagesURL s = new SignedImagesURL(u,surl);
					sUrls.add(s);
				  
				});
				  
				return new ApiResponse(true, sUrls, "User images returned successfuly");
			}
			else {
				return new ApiResponse(false, null, "Invalid User");
			}
		      
		}
		catch (Exception ex) {
	    	return new ApiResponse(false, null, "Images not found");
	    }
		
	}
	
	public ApiResponse getJob(long userId, long jobId) {
			
			try {
				
				if(userValidator.validateUser(userId)){
					
					List<TransformedImages> transList = tImgRepo.findByJobId(jobId);
					List<SignedImagesURL> tUrls = new ArrayList<>();
					transList.forEach( (t)->{
						if(t.getFlag()==1){
							flag=1;
							String imgName = t.getTransformedImgName();
							URL turl= s3Wrapper.generateSignedURL(imgName,transformedImgBucket);
							SignedImagesURL s = new SignedImagesURL(t,turl);
							tUrls.add(s);
						}
						else {
							flag=0;
						}
					});
					
					if(flag==1) {
						return new ApiResponse(true, tUrls, "List of transformed images");
					}
					else {
						return new ApiResponse(false, null, "Images not transformed yet");
					}
				}
				else {
					return new ApiResponse(false, null, "Invalid User");
				}
			}
			catch (Exception e) {
				return new ApiResponse(false, e.toString(), "Invalid Job");
			}
			
	}
	
}
