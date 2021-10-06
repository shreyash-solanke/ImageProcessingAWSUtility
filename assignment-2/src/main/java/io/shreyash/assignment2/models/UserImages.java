package io.shreyash.assignment2.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="userImages")
public class UserImages {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long imgId;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	@NotNull
	private String imgName;

	public long getImgId() {
		return imgId;
	}

	public void setImgId(long imgId) {
		this.imgId = imgId;
	}
	
	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	
	private User getUser() {
		return user;
	}

	public long getUserId(UserImages userImages) {
		return userImages.getUser().getUserId();
	}

	public UserImages(User user, String imgName) {
		super();
		this.user = user;
		this.imgName=imgName;
	}
	
	public UserImages() {}
	
	
}
