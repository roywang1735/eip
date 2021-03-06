package com.wordgod.eip.Model;

public class JobContent{

	private String JobContent_seq;
    private String school_code;
    private String yearMD;
    private String jobContent;
    private String workerId;
    private String workTime;
    private String finishName;
    private String category;
   
  
	public JobContent(String JobContent_seq,String school_code,String yearMD,String jobContent,String workerId,String workTime,String finishName,String category) {
		this.JobContent_seq = JobContent_seq;
		this.school_code = school_code;
		this.yearMD = yearMD;
		this.jobContent = jobContent;
		this.workerId = workerId;
		this.workTime = workTime;
		this.finishName = finishName;
		this.category = category;
	}

	public JobContent() {
		// TODO Auto-generated constructor stub
	}

	public String getJobContent_seq() {
		return JobContent_seq;
	}

	public void setJobContent_seq(String jobContent_seq) {
		JobContent_seq = jobContent_seq;
	}

	public String getSchool_code() {
		return school_code;
	}

	public void setSchool_code(String school_code) {
		this.school_code = school_code;
	}

	public String getYearMD() {
		return yearMD;
	}

	public void setYearMD(String yearMD) {
		this.yearMD = yearMD;
	}

	public String getJobContent() {
		return jobContent;
	}

	public void setJobContent(String jobContent) {
		this.jobContent = jobContent;
	}

	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public String getFinishName() {
		return finishName;
	}

	public void setFinishName(String finishName) {
		this.finishName = finishName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
