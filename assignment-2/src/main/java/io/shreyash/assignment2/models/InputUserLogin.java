package io.shreyash.assignment2.models;

public class InputUserLogin {

	private String inputEmail;
	private String inputPassword;
	
	public InputUserLogin() {
		
	}
	
	public InputUserLogin(String email, String password) {
		super();
		this.inputEmail = email;
		this.inputPassword = password;
	}
	
	
	public String getInputEmail() {
		return inputEmail;
	}
	public void setInpuEmail(String email) {
		this.inputEmail = email;
	}
	public String getInputPassword() {
		return inputPassword;
	}
	public void setInputPassword(String password) {
		this.inputPassword = password;
	}
	
}
