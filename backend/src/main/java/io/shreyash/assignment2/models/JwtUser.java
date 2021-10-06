package io.shreyash.assignment2.models;

public class JwtUser {

	private String userEmail;
	private long userId;
	
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public JwtUser(String userEmail, long userId) {
		super();
		this.userEmail = userEmail;
		this.userId = userId;
	}
	public JwtUser(long userId) {
		super();
		this.userId = userId;
	}
	
	
	
}
