package com.wordgod.eip.Model;

import java.util.List;

public class SubjectTeacher {

	private String subjectTeacher_seq;
    private String subject_id;
    private String subject_name;
    private String teacher_id;
    private String gradeNo;
    private String gradeNoATime;
    private String isPreGrade;
    private String teacher_code;
    private String teacher_name;
    private String teacher_realName;
    private String updater;
    private String update_time;
    private String teacher_enabled;
    private String totalClassNo;
    private String active;

  
	public SubjectTeacher(String subjectTeacher_seq,String subject_id,String subject_name,String teacher_id,String gradeNo,String gradeNoATime,String isPreGrade,String teacher_code,String teacher_name,String teacher_realName,String updater,String update_time,String teacher_enabled,String totalClassNo,String active) {
		this.subjectTeacher_seq = subjectTeacher_seq;
		this.subject_id = subject_id;
		this.subject_name = subject_name;
		this.teacher_id = teacher_id;
		this.gradeNo = gradeNo;
		this.gradeNoATime = gradeNoATime;
		this.isPreGrade = isPreGrade;
		this.teacher_code = teacher_code;
		this.teacher_name = teacher_name;
		this.teacher_realName = teacher_realName;
		this.updater = updater;
		this.update_time = update_time;
		this.teacher_enabled = teacher_enabled;
		this.totalClassNo = totalClassNo;
		this.active = active;
	}



	public SubjectTeacher() {
		// TODO Auto-generated constructor stub
	}



	public String getSubjectTeacher_seq() {
		return subjectTeacher_seq;
	}


	public void setSubjectTeacher_seq(String subjectTeacher_seq) {
		this.subjectTeacher_seq = subjectTeacher_seq;
	}


	public String getSubject_id() {
		return subject_id;
	}


	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}


	public String getSubject_name() {
		return subject_name;
	}



	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}



	public String getTeacher_id() {
		return teacher_id;
	}


	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}


	public String getGradeNo() {
		return gradeNo;
	}


	public void setGradeNo(String gradeNo) {
		this.gradeNo = gradeNo;
	}


	public String getGradeNoATime() {
		return gradeNoATime;
	}


	public void setGradeNoATime(String gradeNoATime) {
		this.gradeNoATime = gradeNoATime;
	}


	public String getIsPreGrade() {
		return isPreGrade;
	}


	public void setIsPreGrade(String isPreGrade) {
		this.isPreGrade = isPreGrade;
	}


	public String getTeacher_code() {
		return teacher_code;
	}


	public void setTeacher_code(String teacher_code) {
		this.teacher_code = teacher_code;
	}


	public String getTeacher_name() {
		return teacher_name;
	}


	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}


	public String getTeacher_realName() {
		return teacher_realName;
	}


	public void setTeacher_realName(String teacher_realName) {
		this.teacher_realName = teacher_realName;
	}



	public String getUpdater() {
		return updater;
	}



	public void setUpdater(String updater) {
		this.updater = updater;
	}



	public String getUpdate_time() {
		return update_time;
	}



	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}



	public String getTeacher_enabled() {
		return teacher_enabled;
	}



	public void setTeacher_enabled(String teacher_enabled) {
		this.teacher_enabled = teacher_enabled;
	}



	public String getTotalClassNo() {
		return totalClassNo;
	}



	public void setTotalClassNo(String totalClassNo) {
		this.totalClassNo = totalClassNo;
	}



	public String getActive() {
		return active;
	}



	public void setActive(String active) {
		this.active = active;
	}


}
