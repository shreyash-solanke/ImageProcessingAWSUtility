package io.shreyash.assignment2.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.sqs.AmazonSQSClient;

@Configuration
public class AWSConfiguration {

	@Value("${cloud.aws.credentials.accessKey}")
	private String accessKey;
	
	@Value("${cloud.aws.credentials.secretKey}")
	private String secretKey;

	@Value("${cloud.aws.region.static}")
	private String region;
	
	@Bean
	public BasicAWSCredentials basicAWSCredentials() {
		return new BasicAWSCredentials(accessKey, secretKey);
	}
	
	@Bean
	public AmazonS3Client amazonS3Client(AWSCredentials awsCredentials) {
		AmazonS3Client amazonS3Client = new AmazonS3Client(awsCredentials);
		amazonS3Client.setRegion(Region.getRegion(Regions.fromName(region)));
		return amazonS3Client;
	}
	
	@Bean
	public AmazonSQSClient amazonSQSClient(AWSCredentials awsCredentials) {
		AmazonSQSClient amazonSQSClient = new AmazonSQSClient(awsCredentials);
		amazonSQSClient.setRegion(Region.getRegion(Regions.fromName(region)));
		return amazonSQSClient;
	}
	
	@Bean
	public AmazonSimpleEmailServiceClient amazonSESClient() {
		
		AWSCredentials mohitCredentials = new BasicAWSCredentials("AKIAIYKZ6YU6TKBR7UXQ", "MU/H/tziISpD4lj0ZNyAiajZ2T4qKA2h2qOl40Da");
		AmazonSimpleEmailServiceClient amazonSESClient = new AmazonSimpleEmailServiceClient(mohitCredentials);
		amazonSESClient.setRegion(Region.getRegion(Regions.US_EAST_1));
		return amazonSESClient;
	}
}
