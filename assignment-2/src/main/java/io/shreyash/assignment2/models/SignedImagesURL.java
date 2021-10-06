package io.shreyash.assignment2.models;

import java.net.URL;

public class SignedImagesURL {

	private TransformedImages transImages;
	private UserImages userimages;
	private URL imgLink;
	
	public UserImages getUserimages() {
		return userimages;
	}
	public void setUserimages(UserImages userimages) {
		this.userimages = userimages;
	}
	public URL getImgLink() {
		return imgLink;
	}
	public void setImgLink(URL imgLink) {
		this.imgLink = imgLink;
	}
	public TransformedImages getTransImages() {
		return transImages;
	}
	public void setTransImages(TransformedImages transImages) {
		this.transImages = transImages;
	}
	public SignedImagesURL(UserImages userimages, URL imgLink) {
		super();
		this.userimages = userimages;
		this.imgLink = imgLink;
	}
	public SignedImagesURL(TransformedImages transImages, URL imgLink) {
		super();
		this.transImages=transImages;
		this.imgLink = imgLink;
	}
	public SignedImagesURL() {}
	
}
