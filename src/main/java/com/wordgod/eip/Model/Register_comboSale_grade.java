package com.wordgod.eip.Model;

public class Register_comboSale_grade {
	private String Register_comboSale_grade_seq;
	private String Register_comboSale_id;
	private String grade_id;
	private String register_status;
	private String class_style;
	private String school_code;
	private String school_name;
	private String sitNo;
	private String active;
	private String updater; //選課業務
	private String updater_time;


	public Register_comboSale_grade(
			String Register_comboSale_grade_seq, 
			String Register_comboSale_id, 
			String grade_id, 
			String register_status, 
			String class_style,
			String school_code,
			String school_name,
			String sitNo,
			String active,
			String updater,
			String updater_time
	) {
		this.Register_comboSale_grade_seq = Register_comboSale_grade_seq;
		this.Register_comboSale_id = Register_comboSale_id;
		this.grade_id = grade_id;
		this.register_status = register_status;
		this.class_style = class_style;
		this.school_code = school_code;
		this.school_name = school_name;
		this.sitNo = sitNo;
		this.active = active;
		this.updater = updater;
		this.updater_time = updater_time;
	}


	public String getRegister_comboSale_grade_seq() {
		return Register_comboSale_grade_seq;
	}


	public void setRegister_comboSale_grade_seq(String register_comboSale_grade_seq) {
		Register_comboSale_grade_seq = register_comboSale_grade_seq;
	}


	public String getRegister_comboSale_id() {
		return Register_comboSale_id;
	}


	public void setRegister_comboSale_id(String register_comboSale_id) {
		Register_comboSale_id = register_comboSale_id;
	}


	public String getGrade_id() {
		return grade_id;
	}


	public void setGrade_id(String grade_id) {
		this.grade_id = grade_id;
	}


	public String getRegister_status() {
		return register_status;
	}


	public void setRegister_status(String register_status) {
		this.register_status = register_status;
	}


	public String getClass_style() {
		return class_style;
	}


	public void setClass_style(String class_style) {
		this.class_style = class_style;
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


	public String getSitNo() {
		return sitNo;
	}


	public void setSitNo(String sitNo) {
		this.sitNo = sitNo;
	}


	public String getActive() {
		return active;
	}


	public void setActive(String active) {
		this.active = active;
	}


	public String getUpdater() {
		return updater;
	}


	public void setUpdater(String updater) {
		this.updater = updater;
	}


	public String getUpdater_time() {
		return updater_time;
	}


	public void setUpdater_time(String updater_time) {
		this.updater_time = updater_time;
	}

	
}
