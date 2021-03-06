package com.wordgod.eip.Model;

public class JobContentCode {

	private String jobContentCode_seq;
    private String school_code;
    private String yearMD;
    private String jobContentCode;
    private String workerId;
    private String workTime;
    private String finished;
  
	public JobContentCode(String jobContentCode_seq,String school_code,String yearMD,String jobContentCode,String workerId,String workTime,String finished) {
		this.jobContentCode_seq = jobContentCode_seq;
		this.school_code = school_code;
		this.yearMD = yearMD;
		this.jobContentCode = jobContentCode;
		this.workerId = workerId;
		this.workTime = workTime;
		this.finished = finished;
	}

	public JobContentCode() {
		// TODO Auto-generated constructor stub
	}

	public String getJobContentCode_seq() {
		return jobContentCode_seq;
	}

	public void setJobContentCode_seq(String jobContentCode_seq) {
		this.jobContentCode_seq = jobContentCode_seq;
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

	public String getJobContentCode() {
		return jobContentCode;
	}

	public void setJobContentCode(String jobContentCode) {
		this.jobContentCode = jobContentCode;
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

	public String getFinished() {
		return finished;
	}

	public void setFinished(String finished) {
		this.finished = finished;
	}
}
