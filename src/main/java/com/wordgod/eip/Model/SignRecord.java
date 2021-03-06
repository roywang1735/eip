package com.wordgod.eip.Model;

public class SignRecord {
	private String signRecord_seq;
    private String student_id;
    private String grade_id;
    private String class_th;
    private String attend;
    private String takeHandout;
    private String active;
    
	public SignRecord(
			String signRecord_seq,
			String student_id,
			String grade_id,
			String class_th,
			String attend,
			String takeHandout,
			String active
	) 
	{
		this.signRecord_seq = signRecord_seq;
		this.student_id = student_id;
		this.grade_id = grade_id;
		this.class_th = class_th;
		this.attend = attend;
		this.takeHandout = takeHandout;
		this.active = active;
	}

	public String getSignRecord_seq() {
		return signRecord_seq;
	}

	public void setSignRecord_seq(String signRecord_seq) {
		this.signRecord_seq = signRecord_seq;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public String getGrade_id() {
		return grade_id;
	}

	public void setGrade_id(String grade_id) {
		this.grade_id = grade_id;
	}

	public String getClass_th() {
		return class_th;
	}

	public void setClass_th(String class_th) {
		this.class_th = class_th;
	}

	public String getAttend() {
		return attend;
	}

	public void setAttend(String attend) {
		this.attend = attend;
	}

	public String getTakeHandout() {
		return takeHandout;
	}

	public void setTakeHandout(String takeHandout) {
		this.takeHandout = takeHandout;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

}
