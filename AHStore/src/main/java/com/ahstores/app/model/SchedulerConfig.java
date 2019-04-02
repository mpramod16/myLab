package com.ahstores.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

@Entity
public class SchedulerConfig {
	
	
	private String jobName;
	
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public long getJobRefreshRateInMillis() {
		return jobRefreshRateInMillis;
	}

	public void setJobRefreshRateInMillis(long jobRefreshRateInMillis) {
		this.jobRefreshRateInMillis = jobRefreshRateInMillis;
	}

	public String getJobRunningOnTable() {
		return jobRunningOnTable;
	}

	public void setJobRunningOnTable(String jobRunningOnTable) {
		this.jobRunningOnTable = jobRunningOnTable;
	}

	

	@Id
    @Size(max = 128)
	private String jobId;
	
	private long jobRefreshRateInMillis;
	
	private String jobRunningOnTable;
	
	private String jobCronExpression;

	public String getJobCronExpression() {
		return jobCronExpression;
	}

	public void setJobCronExpression(String jobCronExpression) {
		this.jobCronExpression = jobCronExpression;
	}
		
}
