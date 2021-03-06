package com.wordgod.eip.Model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Employee {
	private String employee_seq;
	@NotBlank(message="***請輸入帳號***")
	private String account0;
	private String account;
	@NotBlank(message="***請輸入密碼***")
	private String drowssap;
	@Size(min=2, max=10, message="***請輸入中文姓名***")
	private String ch_name;
	private String en_name;
	private List<Account_authority> LAccount_authority;
	private String authority_name;
	//@NotBlank(message="***請選擇部門***")
	private String department;
	private String tel;
	private String email;
	//@NotBlank(message="***請選擇分校***")
	private String school;
	private String school_code;
	private String schoolName;
	private String creater;
	private String submit_date;
	private String enabled;
	private String fb;
	private String line;
	public Employee(String employee_seq,String account0,String account,String drowssap,String ch_name,String en_name,List<Account_authority> LAccount_authority,String authority_name,
			String department,String tel,String email,String school,String school_code,String schoolName,String creater,String enabled,String fb,String line) {
		this.employee_seq = employee_seq;
		this.account0 = account0;
		this.account = account;
		this.drowssap = drowssap;
		this.ch_name = ch_name;
		this.en_name = en_name;
		this.LAccount_authority = LAccount_authority;
		this.authority_name = authority_name;
		this.department = department;
		this.tel = tel;
		this.email = email;
		this.school = school;
		this.school_code = school_code;
		this.schoolName = schoolName;
		this.creater = creater;
		this.enabled = enabled;
		this.fb = fb;
		this.line = line;
	}

	public Employee() {
		// TODO Auto-generated constructor stub
	}

	public String getEmployee_seq() {
		return employee_seq;
	}

	public void setEmployee_seq(String employee_seq) {
		this.employee_seq = employee_seq;
	}

	public String getAccount0() {
		return account0;
	}

	public void setAccount0(String account0) {
		this.account0 = account0;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getDrowssap() {
		return drowssap;
	}

	public void setDrowssap(String drowssap) {
		this.drowssap = drowssap;
	}

	public String getCh_name() {
		return ch_name;
	}

	public void setCh_name(String ch_name) {
		this.ch_name = ch_name;
	}

	public String getEn_name() {
		return en_name;
	}

	public void setEn_name(String en_name) {
		this.en_name = en_name;
	}

	public List<Account_authority> getLAccount_authority() {
		return LAccount_authority;
	}

	public void setLAccount_authority(List<Account_authority> lAccount_authority) {
		LAccount_authority = lAccount_authority;
	}

	public String getAuthority_name() {
		return authority_name;
	}

	public void setAuthority_name(String authority_name) {
		this.authority_name = authority_name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getSchool_code() {
		return school_code;
	}

	public void setSchool_code(String school_code) {
		this.school_code = school_code;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getSubmit_date() {
		return submit_date;
	}

	public void setSubmit_date(String submit_date) {
		this.submit_date = submit_date;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getFb() {
		return fb;
	}

	public void setFb(String fb) {
		this.fb = fb;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

}