package io.shreyash.imageaws;

import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import org.imgscalr.Scalr;

public class ImageProcessing {

	public BufferedImage resizeImg(BufferedImage img) {
		
		BufferedImage scaledImage = Scalr.resize(img, 250);
		return scaledImage;
		
	}
	
	public BufferedImage cropImg(BufferedImage img) {
		
		BufferedImage croppedImage = Scalr.crop(img, 0, 0, 250, 250 );
		return croppedImage;
		
	}
	
	public BufferedImage grayScaleImg(BufferedImage img) {
		
		BufferedImage grayImage = null;
		ColorConvertOp grayOp = Scalr.OP_GRAYSCALE;
		grayImage = grayOp.filter(img,grayImage);
		return grayImage;
		
	}
}
