package com.wordgod.eip.Model;

public class JL_EIP_grade {
	private String JL_EIP_grade_seq;
    private String eip_grade_seq;
    private String JL_gradeId;
    private String class_style;
    private String subject_id;
    private String schoolAbbr;
    
	public JL_EIP_grade(String JL_EIP_grade_seq,String eip_grade_seq,String JL_gradeId,String class_style,String subject_id,String schoolAbbr) {
		this.JL_EIP_grade_seq = JL_EIP_grade_seq;
		this.eip_grade_seq = eip_grade_seq;
		this.JL_gradeId = JL_gradeId;
		this.class_style = class_style;
		this.subject_id = subject_id;
		this.schoolAbbr = schoolAbbr;
	}

	public String getJL_EIP_grade_seq() {
		return JL_EIP_grade_seq;
	}

	public void setJL_EIP_grade_seq(String jL_EIP_grade_seq) {
		JL_EIP_grade_seq = jL_EIP_grade_seq;
	}

	public String getEip_grade_seq() {
		return eip_grade_seq;
	}

	public void setEip_grade_seq(String eip_grade_seq) {
		this.eip_grade_seq = eip_grade_seq;
	}

	public String getJL_gradeId() {
		return JL_gradeId;
	}

	public void setJL_gradeId(String jL_gradeId) {
		JL_gradeId = jL_gradeId;
	}

	public String getClass_style() {
		return class_style;
	}

	public void setClass_style(String class_style) {
		this.class_style = class_style;
	}

	public String getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}

	public String getSchoolAbbr() {
		return schoolAbbr;
	}

	public void setSchoolAbbr(String schoolAbbr) {
		this.schoolAbbr = schoolAbbr;
	}

}
