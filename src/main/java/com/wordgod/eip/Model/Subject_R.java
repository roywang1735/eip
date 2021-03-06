package com.wordgod.eip.Model;

public class Subject_R {
	private String subject_R_seq;
    private String subject_id;
    private String class_style;
    private String hrPrice_R;
    private String counselingPrice_R;
    private String lagnappePrice_R;
    private String handoutPrice_R;
    private String homeworkPrice_R;
    private String mockPrice_R;
    
	public Subject_R(
			String subject_R_seq,
			String subject_id,
			String class_style,
			String hrPrice_R,
			String counselingPrice_R,
			String lagnappePrice_R,
			String handoutPrice_R,
			String homeworkPrice_R,
			String mockPrice_R
		) {
		this.subject_R_seq = subject_R_seq;
		this.subject_id = subject_id;
		this.class_style = class_style;	
		this.hrPrice_R = hrPrice_R;
		this.counselingPrice_R = counselingPrice_R;
		this.lagnappePrice_R = lagnappePrice_R;
		this.handoutPrice_R = handoutPrice_R;
		this.homeworkPrice_R = homeworkPrice_R;
		this.mockPrice_R = mockPrice_R;
	}

	public Subject_R() {
		// TODO Auto-generated constructor stub
	}

	public String getSubject_R_seq() {
		return subject_R_seq;
	}

	public void setSubject_R_seq(String subject_R_seq) {
		this.subject_R_seq = subject_R_seq;
	}

	public String getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}

	public String getClass_style() {
		return class_style;
	}

	public void setClass_style(String class_style) {
		this.class_style = class_style;
	}

	public String getHrPrice_R() {
		return hrPrice_R;
	}

	public void setHrPrice_R(String hrPrice_R) {
		this.hrPrice_R = hrPrice_R;
	}

	public String getCounselingPrice_R() {
		return counselingPrice_R;
	}

	public void setCounselingPrice_R(String counselingPrice_R) {
		this.counselingPrice_R = counselingPrice_R;
	}

	public String getLagnappePrice_R() {
		return lagnappePrice_R;
	}

	public void setLagnappePrice_R(String lagnappePrice_R) {
		this.lagnappePrice_R = lagnappePrice_R;
	}

	public String getHandoutPrice_R() {
		return handoutPrice_R;
	}

	public void setHandoutPrice_R(String handoutPrice_R) {
		this.handoutPrice_R = handoutPrice_R;
	}

	public String getHomeworkPrice_R() {
		return homeworkPrice_R;
	}

	public void setHomeworkPrice_R(String homeworkPrice_R) {
		this.homeworkPrice_R = homeworkPrice_R;
	}

	public String getMockPrice_R() {
		return mockPrice_R;
	}

	public void setMockPrice_R(String mockPrice_R) {
		this.mockPrice_R = mockPrice_R;
	}

}
