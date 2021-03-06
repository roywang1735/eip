package com.wordgod.eip.Model;


public class Pre_grade{
	private String pre_grade_seq;
	private String subject_id;
	private String grade_id;
	private String gradeName;
	private String class_start_date;
	private String video_date;
	private String grade_remark;

  
    public Pre_grade(
    		String pre_grade_seq,
    		String subject_id,
    		String grade_id,
    		String gradeName,
    		String class_start_date,
    		String video_date,
    		String grade_remark
    ){
		this.pre_grade_seq = pre_grade_seq;
		this.subject_id = subject_id;
		this.grade_id = grade_id;
		this.gradeName = gradeName;
		this.class_start_date = class_start_date;
		this.video_date = video_date;
		this.grade_remark = grade_remark;
	}


	public String getPre_grade_seq() {
		return pre_grade_seq;
	}


	public void setPre_grade_seq(String pre_grade_seq) {
		this.pre_grade_seq = pre_grade_seq;
	}


	public String getSubject_id() {
		return subject_id;
	}


	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}


	public String getGrade_id() {
		return grade_id;
	}


	public void setGrade_id(String grade_id) {
		this.grade_id = grade_id;
	}


	public String getGradeName() {
		return gradeName;
	}


	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}


	public String getClass_start_date() {
		return class_start_date;
	}


	public void setClass_start_date(String class_start_date) {
		this.class_start_date = class_start_date;
	}


	public String getVideo_date() {
		return video_date;
	}


	public void setVideo_date(String video_date) {
		this.video_date = video_date;
	}


	public String getGrade_remark() {
		return grade_remark;
	}


	public void setGrade_remark(String grade_remark) {
		this.grade_remark = grade_remark;
	}

}
