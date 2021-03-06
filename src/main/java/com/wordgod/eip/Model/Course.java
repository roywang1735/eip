package com.wordgod.eip.Model;

import java.util.List;

public class Course{
	private String course_seq;
	private String status;
	private String school;
    private String category;
    private String subject;
    private String subject_name;
    private String subject_abbr;
    private String issue_date;
    private String from_date;
    private String to_date;
	private String grade_name;
    private String grade_teacher;
	private List<Grade> LGrade;
	
	public Course() {
		// TODO Auto-generated constructor stub
	}
    
	public Course(String course_seq, String status, String school, String category, String subject, String subject_name, String subject_abbr,String issue_date, String from_date, String to_date, String grade_name, String grade_teacher) {
		this.course_seq = course_seq;
		this.status = status;
		this.school = school;
		this.category = category;
		this.subject = subject;
		this.subject_name = subject_name;
		this.subject_abbr = subject_abbr;
		this.issue_date = issue_date;
		this.from_date = from_date;
		this.to_date = to_date;
		this.grade_name = grade_name;
		this.grade_teacher = grade_teacher;
	}


	public String getCourse_seq() {
		return course_seq;
	}


	public void setCourse_seq(String course_seq) {
		this.course_seq = course_seq;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getSchool() {
		return school;
	}


	public void setSchool(String school) {
		this.school = school;
	}

	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getIssue_date() {
		return issue_date;
	}


	public void setIssue_date(String issue_date) {
		this.issue_date = issue_date;
	}


	public String getFrom_date() {
		return from_date;
	}


	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}


	public String getTo_date() {
		return to_date;
	}


	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}
	
    public String getGrade_name() {
		return grade_name;
	}


	public void setGrade_name(String grade_name) {
		this.grade_name = grade_name;
	}


	public String getGrade_teacher() {
		return grade_teacher;
	}


	public void setGrade_teacher(String grade_teacher) {
		this.grade_teacher = grade_teacher;
	}

	
	  public String getSubject_name() {
		return subject_name;
	}

	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}
	
	public String getSubject_abbr() {
		return subject_abbr;
	}

	public void setSubject_abbr(String subject_abbr) {
		this.subject_abbr = subject_abbr;
	}

	public List<Grade> getLGrade() { return LGrade; }
	  
	public void setLGrade(List<Grade> lGrade) { LGrade = lGrade; }
	 
	
}
