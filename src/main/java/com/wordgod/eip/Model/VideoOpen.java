package com.wordgod.eip.Model;

public class VideoOpen {
	private String videoOpen_seq;
    private String grade_id;
    private String schoolCode;
   
	public VideoOpen(String videoOpen_seq,String grade_id,String schoolCode) {
		this.videoOpen_seq = videoOpen_seq;
		this.grade_id = grade_id;
		this.schoolCode = schoolCode;
	}
	public VideoOpen() {

	}	

	public String getVideoOpen_seq() {
		return videoOpen_seq;
	}

	public void setVideoOpen_seq(String videoOpen_seq) {
		this.videoOpen_seq = videoOpen_seq;
	}

	public String getGrade_id() {
		return grade_id;
	}

	public void setGrade_id(String grade_id) {
		this.grade_id = grade_id;
	}

	public String getSchoolCode() {
		return schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

}
