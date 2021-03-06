package com.wordgod.eip.Model;

/**
 * @author roy
 *
 */
public class orderChange {
	private String orderChange_seq;
	private String student_id;
	private String register_id ;
	private String changeType;
	private String subject_from;
	private String subject_to;
	private String gradeName_from;
	private String gradeName_to;	
	private String payMoney_from;
	private String payMoney_to;
	private String actualPrice_from;
	private String actualPrice_to;
	private String creater;
	private String updateTime;
	private String studentName;
	private String school;
	private String cancelRegister;
	private String isUpdate;

	public orderChange(String orderChange_seq,String student_id,String register_id,String changeType,String subject_from,String subject_to,String gradeName_from,String gradeName_to,String payMoney_from,String payMoney_to,String actualPrice_from,String actualPrice_to,String creater,String updateTime,String studentName,String school,String cancelRegister,String isUpdate) {
		this.orderChange_seq = orderChange_seq;
		this.student_id = student_id;
		this.register_id  = register_id ;
		this.changeType = changeType;
		this.subject_from = subject_from;
		this.subject_to = subject_to;
		this.gradeName_from = gradeName_from;
		this.gradeName_to = gradeName_to;
		this.payMoney_from = payMoney_from;
		this.payMoney_to = payMoney_to;
		this.actualPrice_from = actualPrice_from;
		this.actualPrice_to = actualPrice_to;
		this.creater = creater;
		this.updateTime = updateTime;
		this.studentName = studentName;
		this.school = school;
		this.cancelRegister = cancelRegister;
		this.isUpdate = isUpdate;
	}

	public String getOrderChange_seq() {
		return orderChange_seq;
	}

	public void setOrderChange_seq(String orderChange_seq) {
		this.orderChange_seq = orderChange_seq;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public String getRegister_id() {
		return register_id;
	}

	public void setRegister_id(String register_id) {
		this.register_id = register_id;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getSubject_from() {
		return subject_from;
	}

	public void setSubject_from(String subject_from) {
		this.subject_from = subject_from;
	}

	public String getSubject_to() {
		return subject_to;
	}

	public void setSubject_to(String subject_to) {
		this.subject_to = subject_to;
	}

	public String getGradeName_from() {
		return gradeName_from;
	}

	public void setGradeName_from(String gradeName_from) {
		this.gradeName_from = gradeName_from;
	}

	public String getGradeName_to() {
		return gradeName_to;
	}

	public void setGradeName_to(String gradeName_to) {
		this.gradeName_to = gradeName_to;
	}

	public String getPayMoney_from() {
		return payMoney_from;
	}

	public void setPayMoney_from(String payMoney_from) {
		this.payMoney_from = payMoney_from;
	}

	public String getPayMoney_to() {
		return payMoney_to;
	}

	public void setPayMoney_to(String payMoney_to) {
		this.payMoney_to = payMoney_to;
	}

	public String getActualPrice_from() {
		return actualPrice_from;
	}

	public void setActualPrice_from(String actualPrice_from) {
		this.actualPrice_from = actualPrice_from;
	}

	public String getActualPrice_to() {
		return actualPrice_to;
	}

	public void setActualPrice_to(String actualPrice_to) {
		this.actualPrice_to = actualPrice_to;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getCancelRegister() {
		return cancelRegister;
	}

	public void setCancelRegister(String cancelRegister) {
		this.cancelRegister = cancelRegister;
	}

	public String getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}
 
}
