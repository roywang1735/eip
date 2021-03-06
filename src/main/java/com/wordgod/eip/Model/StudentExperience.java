package com.wordgod.eip.Model;

public class StudentExperience {

	private String studentExperience_seq;
    private String student_id;
    private String school_code;
    private String school_name;    
    private String experience_id;
    private String experience_name;
    private String experience_state;
    private String experience_content;
    private String validDate;
    private String creater;
    private String createTime;
    private String register_id;
    private String haveRead;
    private String ch_name;
    private String student_no;
    
  
	public StudentExperience(
			String studentExperience_seq,
			String student_id,
			String school_code,
			String school_name,
			String experience_id,
			String experience_name,
			String experience_state,
			String experience_content,
			String validDate,
			String creater ,
			String createTime,
			String register_id,
			String haveRead,
			String ch_name,
			String student_no
		){
		this.studentExperience_seq = studentExperience_seq;
		this.student_id = student_id;
		this.school_code = school_code;
		this.school_name = school_name;
		this.experience_id = experience_id;
		this.experience_name = experience_name;
		this.experience_state = experience_state;
		this.experience_content = experience_content;
		this.validDate = validDate;
		this.creater = creater;
		this.createTime = createTime;
		this.register_id = register_id;
		this.haveRead = haveRead;
		this.ch_name = ch_name;
		this.student_no = student_no;
	}

	public StudentExperience() {
		// TODO Auto-generated constructor stub
	}

	public String getStudentExperience_seq() {
		return studentExperience_seq;
	}

	public void setStudentExperience_seq(String studentExperience_seq) {
		this.studentExperience_seq = studentExperience_seq;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public String getSchool_code() {
		return school_code;
	}

	public void setSchool_code(String school_code) {
		this.school_code = school_code;
	}

	public String getSchool_name() {
		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}

	public String getExperience_id() {
		return experience_id;
	}

	public void setExperience_id(String experience_id) {
		this.experience_id = experience_id;
	}

	public String getExperience_name() {
		return experience_name;
	}

	public void setExperience_name(String experience_name) {
		this.experience_name = experience_name;
	}

	public String getExperience_state() {
		return experience_state;
	}

	public void setExperience_state(String experience_state) {
		this.experience_state = experience_state;
	}

	public String getExperience_content() {
		return experience_content;
	}

	public void setExperience_content(String experience_content) {
		this.experience_content = experience_content;
	}

	public String getValidDate() {
		return validDate;
	}

	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRegister_id() {
		return register_id;
	}

	public void setRegister_id(String register_id) {
		this.register_id = register_id;
	}

	public String getHaveRead() {
		return haveRead;
	}

	public void setHaveRead(String haveRead) {
		this.haveRead = haveRead;
	}

	public String getCh_name() {
		return ch_name;
	}

	public void setCh_name(String ch_name) {
		this.ch_name = ch_name;
	}

	public String getStudent_no() {
		return student_no;
	}

	public void setStudent_no(String student_no) {
		this.student_no = student_no;
	}

}
