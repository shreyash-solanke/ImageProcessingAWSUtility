package io.shreyash.assignment2.aws;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;

import io.shreyash.assignment2.models.ApiResponse;
import io.shreyash.assignment2.models.User;
import io.shreyash.assignment2.models.UserImages;
import io.shreyash.assignment2.repositories.UserImagesRepository;
import io.shreyash.assignment2.services.UserValidator;

@Service
public class S3Wrapper {

	@Autowired
	private UserImagesRepository userImagesRepository;
	
	@Autowired
	private AmazonS3Client amazonS3Client;
	
	@Autowired
	private UserValidator userValidator;
	
	@Value("${cloud.aws.s3.bucket.inputimages}")
	private String inputImagesBucket;
	
	private String transformedImgBucket = "bucket-2-transformed-images";
	
	private String imgName;
	
	/*
	 * Upload using file path
	private PutObjectResult upload(String filePath, String uploadKey) throws FileNotFoundException {
		return upload(new FileInputStream(filePath), uploadKey);
	}*/
	
	private PutObjectResult upload(InputStream inputStream, String uploadKey) {
		PutObjectRequest putObjectRequest = new PutObjectRequest(inputImagesBucket, uploadKey, inputStream, new ObjectMetadata());
		
		//if u want to make it public read
		//putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);

		PutObjectResult putObjectResult = amazonS3Client.putObject(putObjectRequest);

		IOUtils.closeQuietly(inputStream, null);

		return putObjectResult;
	}
	
	public ApiResponse upload(MultipartFile[] multipartFiles, long userId) {
		
		try {
			
			if(userValidator.validateUser(userId)) {
				
				//add current time to object name for uniqueness
				Random rand = new Random();
				int prefix = rand.nextInt(1000);
				
				//upload start
				List<PutObjectResult> putObjectResults = new ArrayList<>();
		
				Arrays.stream(multipartFiles)
						.filter(multipartFile -> !StringUtils.isEmpty(multipartFile.getOriginalFilename()))
						.forEach(multipartFile -> {
							try {
								imgName=prefix+"_"+multipartFile.getOriginalFilename();
								putObjectResults.add(upload(multipartFile.getInputStream(), imgName));
							} catch (IOException e) {
								e.printStackTrace();
							}
						});

				//**********************************************************
				//upload complete.. now add url and image name to user images table
		
				User detailedUser = userValidator.getValidatedUser(userId);
				UserImages userImages = new UserImages(detailedUser,imgName);
				userImagesRepository.save(userImages);
				return new ApiResponse(true, userImagesRepository.findByImgName(imgName),
						"User images uploaded successfuly");
			}
			else {
				return new ApiResponse(false, null, "Invalid User");
			}
		      
	    }
	    catch (Exception ex) {
	    	System.out.println(ex);
	    	return new ApiResponse(false, null, "Images not found");
	    }
		
	}
	
	public ApiResponse download(String key) throws IOException {
		GetObjectRequest getObjectRequest = new GetObjectRequest(inputImagesBucket, key);

		S3Object s3Object = amazonS3Client.getObject(getObjectRequest);

		S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

		byte[] bytes = IOUtils.toByteArray(objectInputStream);

		String fileName = URLEncoder.encode(key, "UTF-8").replaceAll("\\+", "%20");

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		httpHeaders.setContentLength(bytes.length);
		httpHeaders.setContentDispositionFormData("attachment", fileName);

		return new ApiResponse(true, bytes, "Image downloaded succesfully");
	}
	
	public ApiResponse delete(long userId, long imgId) {
		
		if (userValidator.validateUser(userId)) {
			UserImages deletedUserImage = userImagesRepository.findOne(imgId);
			String deleteImgName = deletedUserImage.getImgName();
			DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(inputImagesBucket, deleteImgName);
			
			if(deletedUserImage.getUserId(deletedUserImage)==userId)
			{	
				try {
					amazonS3Client.deleteObject(deleteObjectRequest);
					//now delete entry from table
					userImagesRepository.delete(deletedUserImage);
					return new ApiResponse(true, imgName, "image deleted successfully");
		
				} catch (AmazonServiceException e) {
		
					System.out.println("Caught amazon service excpetion");
					System.out.println("Error Message:    " + e.getMessage());
					System.out.println("HTTP Status Code: " + e.getStatusCode());
					System.out.println("AWS Error Code:   " + e.getErrorCode());
					System.out.println("Error Type:       " + e.getErrorType());
					System.out.println("Request ID:       " + e.getRequestId());
					return new ApiResponse(false, imgName, "image not deleted, amazon service error!");
		
				} catch (AmazonClientException ace) {
					System.out.println("Caught an AmazonClientException.");
					System.out.println("Error Message: " + ace.getMessage());
					return new ApiResponse(false, imgName, "image not deleted, amazon client error");
				}
			}
			else {
				return new ApiResponse(true, null, "Invalid image given");
			}
			
		}
		else {
			return new ApiResponse(false, null, "Invalid User");
		}
	}
	
	public URL generateSignedURL(String imgName, String bucketName) {
		
		java.util.Date expiration = new java.util.Date();
		long msec = expiration.getTime();
		msec += 1000 * 60 * 10; // 10 mins
		expiration.setTime(msec);
		             
		GeneratePresignedUrlRequest generatePresignedUrlRequest = 
		              new GeneratePresignedUrlRequest(bucketName, imgName);
		generatePresignedUrlRequest.setExpiration(expiration);
		             
		URL s = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest); 
		return s;
	}
	
	public List<S3ObjectSummary> list() {
		ObjectListing objectListing = amazonS3Client.listObjects(new ListObjectsRequest().withBucketName(inputImagesBucket));

		List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();

		return s3ObjectSummaries;
	}
	
}
