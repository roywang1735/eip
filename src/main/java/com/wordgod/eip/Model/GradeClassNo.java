package com.wordgod.eip.Model;


public class GradeClassNo{
	private String gradeClassNo_seq;
	private String subjectTeacher_id;
	private String gradeName;
	private String gradeClassNo;
	private String totalClassNo;

  
    public GradeClassNo(
    		String gradeClassNo_seq,
    		String subjectTeacher_id,
    		String gradeName,
    		String gradeClassNo,
    		String totalClassNo
    ){
		this.gradeClassNo_seq = gradeClassNo_seq;
		this.subjectTeacher_id = subjectTeacher_id;
		this.gradeName = gradeName;
		this.gradeClassNo = gradeClassNo;
		this.totalClassNo = totalClassNo;
	}


	public GradeClassNo() {
		// TODO Auto-generated constructor stub
	}


	public String getGradeClassNo_seq() {
		return gradeClassNo_seq;
	}


	public void setGradeClassNo_seq(String gradeClassNo_seq) {
		this.gradeClassNo_seq = gradeClassNo_seq;
	}


	public String getSubjectTeacher_id() {
		return subjectTeacher_id;
	}


	public void setSubjectTeacher_id(String subjectTeacher_id) {
		this.subjectTeacher_id = subjectTeacher_id;
	}


	public String getGradeName() {
		return gradeName;
	}


	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}


	public String getGradeClassNo() {
		return gradeClassNo;
	}


	public void setGradeClassNo(String gradeClassNo) {
		this.gradeClassNo = gradeClassNo;
	}


	public String getTotalClassNo() {
		return totalClassNo;
	}


	public void setTotalClassNo(String totalClassNo) {
		this.totalClassNo = totalClassNo;
	}


}
