package io.shreyash.assignment2.models;


public class SqsMessage {

	
	private String operationType;
	private String[] imgNames;
	
	public String getoperationType() {
		return operationType;
	}
	public void setoperationType(String operationType) {
		this.operationType = operationType;
	}
	public String[] getImgNames() {
		return imgNames;
	}
	public void setImgNames(String[] imgNames) {
		this.imgNames = imgNames;
	}
	public SqsMessage(String operationType, String[] imgNames) {
		super();
		this.operationType = operationType;
		this.imgNames = imgNames;
	}
	
	public SqsMessage() {}
	
}
