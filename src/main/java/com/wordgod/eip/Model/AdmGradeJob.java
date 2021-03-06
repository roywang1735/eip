package com.wordgod.eip.Model;

public class AdmGradeJob {
	private String admGradeJob_seq;
	private String grade_id; 
	private String class_th;
    private String b1_jobContent;
    private String b2_jobContent;
    private String b3_jobContent;
    private String b4_jobContent;
    private String editerId;
    private String editerTime;
    private String finishName;
    
	public AdmGradeJob(String admGradeJob_seq,String grade_id,String class_th,String b1_jobContent,String b2_jobContent,String b3_jobContent,String b4_jobContent,String editerId,String editerTime,String finishName) {
		this.admGradeJob_seq = admGradeJob_seq;
		this.grade_id = grade_id;
		this.class_th = class_th;
		this.b1_jobContent = b1_jobContent;
		this.b2_jobContent = b2_jobContent;
		this.b3_jobContent = b3_jobContent;
		this.b4_jobContent = b4_jobContent;
		this.editerId = editerId;
		this.editerTime = editerTime;
		this.finishName = finishName;
	}

	public AdmGradeJob() {
		// TODO Auto-generated constructor stub
	}

	public String getAdmGradeJob_seq() {
		return admGradeJob_seq;
	}

	public void setAdmGradeJob_seq(String admGradeJob_seq) {
		this.admGradeJob_seq = admGradeJob_seq;
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

	public String getB1_jobContent() {
		return b1_jobContent;
	}

	public void setB1_jobContent(String b1_jobContent) {
		this.b1_jobContent = b1_jobContent;
	}

	public String getB2_jobContent() {
		return b2_jobContent;
	}

	public void setB2_jobContent(String b2_jobContent) {
		this.b2_jobContent = b2_jobContent;
	}

	public String getB3_jobContent() {
		return b3_jobContent;
	}

	public void setB3_jobContent(String b3_jobContent) {
		this.b3_jobContent = b3_jobContent;
	}

	public String getB4_jobContent() {
		return b4_jobContent;
	}

	public void setB4_jobContent(String b4_jobContent) {
		this.b4_jobContent = b4_jobContent;
	}

	public String getEditerId() {
		return editerId;
	}

	public void setEditerId(String editerId) {
		this.editerId = editerId;
	}

	public String getEditerTime() {
		return editerTime;
	}

	public void setEditerTime(String editerTime) {
		this.editerTime = editerTime;
	}

	public String getFinishName() {
		return finishName;
	}

	public void setFinishName(String finishName) {
		this.finishName = finishName;
	}

}
