package com.wordgod.eip.Model;

public class Register_comboSale {
	private String Register_comboSale_seq;
	private String Register_id;
	private String comboSale_id;
	private String comboSale_name;
	private String isCombo;
	private String subject_id;
	private String subject_id_virtual;
	private String subject_name;
	private String subject_virtual_name;
	private String grade_str;
	private String student_id;
	private String student_no;
	private String student_name;
	private String gradeNo;
	private String gradeNoLeft;
	private String replaceFrom;
	private String from_Register_comboSale_id;
	private String active;
	private String costShare;
	private String class_style;
	private String category_name;
	private String originSubjectName;
	private String subject_price;

	public Register_comboSale(
			String Register_comboSale_seq, 
			String Register_id, 
			String comboSale_id, 
			String comboSale_name,
			String isCombo,
			String subject_id,
			String subject_id_virtual,
			String subject_name,
			String subject_virtual_name,
			String grade_str, 
			String student_id, 
			String student_no, 
			String student_name,
			String gradeNo,
			String gradeNoLeft,
			String replaceFrom,
			String from_Register_comboSale_id,
			String active,
			String costShare,
			String class_style,
			String category_name,
			String originSubjectName,
			String subject_price
	) {
		this.Register_comboSale_seq = Register_comboSale_seq;
		this.Register_id = Register_id;
		this.comboSale_id = comboSale_id;
		this.comboSale_name = comboSale_name;
		this.isCombo = isCombo;
		this.subject_id = subject_id;
		this.subject_id_virtual = subject_id_virtual;
		this.subject_name = subject_name;
		this.subject_virtual_name = subject_virtual_name;
		this.grade_str = grade_str;
		this.student_id = student_id;
		this.student_no = student_no;
		this.student_name = student_name;
		this.gradeNo = gradeNo;
		this.gradeNoLeft = gradeNoLeft;
		this.replaceFrom = replaceFrom; 
		this.from_Register_comboSale_id = from_Register_comboSale_id;
		this.active = active;
		this.costShare = costShare;
		this.class_style = class_style;
		this.category_name = category_name;
		this.originSubjectName = originSubjectName;
		this.subject_price = subject_price;
	}

	public String getRegister_comboSale_seq() {
		return Register_comboSale_seq;
	}

	public void setRegister_comboSale_seq(String register_comboSale_seq) {
		Register_comboSale_seq = register_comboSale_seq;
	}

	public String getRegister_id() {
		return Register_id;
	}

	public void setRegister_id(String register_id) {
		Register_id = register_id;
	}

	public String getComboSale_id() {
		return comboSale_id;
	}

	public void setComboSale_id(String comboSale_id) {
		this.comboSale_id = comboSale_id;
	}

	public String getComboSale_name() {
		return comboSale_name;
	}

	public void setComboSale_name(String comboSale_name) {
		this.comboSale_name = comboSale_name;
	}

	public String getIsCombo() {
		return isCombo;
	}

	public void setIsCombo(String isCombo) {
		this.isCombo = isCombo;
	}

	public String getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}

	public String getSubject_id_virtual() {
		return subject_id_virtual;
	}

	public void setSubject_id_virtual(String subject_id_virtual) {
		this.subject_id_virtual = subject_id_virtual;
	}

	public String getSubject_name() {
		return subject_name;
	}

	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}

	public String getSubject_virtual_name() {
		return subject_virtual_name;
	}

	public void setSubject_virtual_name(String subject_virtual_name) {
		this.subject_virtual_name = subject_virtual_name;
	}

	public String getGrade_str() {
		return grade_str;
	}

	public void setGrade_str(String grade_str) {
		this.grade_str = grade_str;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public String getStudent_no() {
		return student_no;
	}

	public void setStudent_no(String student_no) {
		this.student_no = student_no;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public String getGradeNo() {
		return gradeNo;
	}

	public void setGradeNo(String gradeNo) {
		this.gradeNo = gradeNo;
	}

	public String getGradeNoLeft() {
		return gradeNoLeft;
	}

	public void setGradeNoLeft(String gradeNoLeft) {
		this.gradeNoLeft = gradeNoLeft;
	}

	public String getReplaceFrom() {
		return replaceFrom;
	}

	public void setReplaceFrom(String replaceFrom) {
		this.replaceFrom = replaceFrom;
	}

	public String getFrom_Register_comboSale_id() {
		return from_Register_comboSale_id;
	}

	public void setFrom_Register_comboSale_id(String from_Register_comboSale_id) {
		this.from_Register_comboSale_id = from_Register_comboSale_id;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getCostShare() {
		return costShare;
	}

	public void setCostShare(String costShare) {
		this.costShare = costShare;
	}

	public String getClass_style() {
		return class_style;
	}

	public void setClass_style(String class_style) {
		this.class_style = class_style;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getOriginSubjectName() {
		return originSubjectName;
	}

	public void setOriginSubjectName(String originSubjectName) {
		this.originSubjectName = originSubjectName;
	}

	public String getSubject_price() {
		return subject_price;
	}

	public void setSubject_price(String subject_price) {
		this.subject_price = subject_price;
	}
	
}
