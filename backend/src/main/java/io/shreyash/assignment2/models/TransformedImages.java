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
@Table(name="transformedImages")
public class TransformedImages {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long transImgId;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	@NotNull
	private String operationType;
	
	@NotNull
	private String srcImgName;
	
	private String transformedImgName;
	
	@NotNull
	private int flag;
	
	@NotNull
	private long jobId;

	public String getSrcImgName() {
		return srcImgName;
	}

	public void setSrcImgName(String srcImgName) {
		this.srcImgName = srcImgName;
	}
	
	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public long getTransImgId() {
		return transImgId;
	}

	public void setTransImgId(long transImgId) {
		this.transImgId = transImgId;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getTransformedImgName() {
		return transformedImgName;
	}

	public void setTransformedImgName(String transformedImgName) {
		this.transformedImgName = transformedImgName;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public TransformedImages(User user, long jobId, String operationType, String srcImgName, int flag)
	{
		super();
		this.user=user;
		this.jobId=jobId;
		this.srcImgName=srcImgName;
		this.operationType=operationType;
		this.flag=flag;
	}
	
	public TransformedImages() {}
	
}
