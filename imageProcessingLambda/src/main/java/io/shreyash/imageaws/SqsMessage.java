package io.shreyash.imageaws;

import java.util.List;

public class SqsMessage {

	private long jobId;
	private String email;
	private String operationType;
	private List<String> imgNames;
	
	public SqsMessage() {}
	public SqsMessage(long jobId, String operationType, List<String> imgNames, String email) {
		super();
		this.jobId=jobId;
		this.operationType = operationType;
		this.imgNames = imgNames;
		this.email=email;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public List<String> getImgNames() {
		return imgNames;
	}
	public void setImgNames(List<String> imgNames) {
		this.imgNames = imgNames;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getJobId() {
		return jobId;
	}
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
	

	
	
}
