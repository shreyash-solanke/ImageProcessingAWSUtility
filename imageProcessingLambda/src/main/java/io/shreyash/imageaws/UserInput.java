package io.shreyash.imageaws;

public class UserInput {


	private long imgId;
	private String name;
	
	public long getImgId() {
		return imgId;
	}
	public void setImgId(long imgId) {
		this.imgId = imgId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public UserInput(long imgId, String name) {
		super();
		this.imgId = imgId;
		this.name = name;
	}
	
	public UserInput() {}
	
}
