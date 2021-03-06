package com.wordgod.eip.Model;

public class AdmJob {

	private String admJob_seq;
    private String school_code;
    private String yearMD;
    private String assigner;
    private String assignerTime;
   
  
	public AdmJob(String admJob_seq,String school_code,String yearMD,String assigner,String assignerTime) {
		this.admJob_seq = admJob_seq;
		this.school_code = school_code;
		this.yearMD = yearMD;
		this.assigner = assigner;
		this.assignerTime = assignerTime;
	}

	public AdmJob() {
		// TODO Auto-generated constructor stub
	}

	public String getAdmJob_seq() {
		return admJob_seq;
	}

	public void setAdmJob_seq(String admJob_seq) {
		this.admJob_seq = admJob_seq;
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

	public String getAssigner() {
		return assigner;
	}

	public void setAssigner(String assigner) {
		this.assigner = assigner;
	}

	public String getAssignerTime() {
		return assignerTime;
	}

	public void setAssignerTime(String assignerTime) {
		this.assignerTime = assignerTime;
	}

	

}
