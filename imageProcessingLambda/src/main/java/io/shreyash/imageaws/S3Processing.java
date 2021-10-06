package io.shreyash.imageaws;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

public class S3Processing {

	private AmazonS3Client amazonS3Client;
	
	public S3Processing() {
		amazonS3Client = new AmazonS3Client();
	}
	
	public Map<String, BufferedImage> getImages(SqsMessage msg) {
		
		List<String> imgNames = msg.getImgNames();
		Map<String, BufferedImage> buffImages = new HashMap();
		imgNames.forEach(
				(img)-> {
					
					S3Object s3Object = amazonS3Client.getObject("bucket-1-images", img);
					InputStream objectData = s3Object.getObjectContent();
					try {
						BufferedImage srcImage = ImageIO.read(objectData);
						buffImages.put(img, srcImage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
		return buffImages;
	}
	
	public String uploadImage(String imgName, BufferedImage resizedImg) throws IOException {
		
		Random rand = new Random();
		int num = rand.nextInt(1000);
		String dstkey = num + "_" + imgName;
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(resizedImg,"png",os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		ObjectMetadata meta = new ObjectMetadata();
	    meta.setContentLength(os.size());
	    try {
	    	amazonS3Client.putObject("bucket-2-transformed-images",dstkey, is, meta);
		} catch (Exception e) {
			System.out.println(e);
		}
	    System.out.println(dstkey+" image uploaded succesfully");
	    return dstkey;
	}
	
}
