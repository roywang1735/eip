package com.wordgod.eip.Model;

public class Register_log {
	private String register_log_seq;
    private String register_comboSale_id;
    private String Register_comboSale_grade_seq;
    private String register_status;
    private String reason_option;
    private String reason;
    private String update_time;
    private String updater;
    private String subject_name;
    private String school_name;
    private String class_start_date;
    private String classCharge;
    private String operationCharge;
    private String payStyle_2_money;
    private String payStyle_5_periods;
    private String payStyle_5_code;
    private String payStyle_5_money;
    private String payStyle_7_code;
    private String payStyle_7_money; 
    private String isUpdate;

    
	public Register_log(
			String register_log_seq, 
			String register_comboSale_id, 
			String Register_comboSale_grade_seq,
			String register_status,
			String reason,
			String reason_option,
			String update_time,
			String updater,
			String subject_name,
			String school_name,
			String class_start_date,
			String classCharge,
			String operationCharge,
		    String payStyle_2_money,
		    String payStyle_5_periods,
		    String payStyle_5_code,
		    String payStyle_5_money,
		    String payStyle_7_code,
		    String payStyle_7_money,
		    String isUpdate
	) {
		this.register_log_seq = register_log_seq;
		this.register_comboSale_id = register_comboSale_id;
		this.Register_comboSale_grade_seq = Register_comboSale_grade_seq;
		this.register_status = register_status;
		this.reason = reason;
		this.reason_option = reason_option;
		this.update_time = update_time;
		this.updater = updater;
		this.subject_name = subject_name;
		this.school_name = school_name;
		this.class_start_date = class_start_date;
		this.classCharge = classCharge;
		this.operationCharge = operationCharge;
		this.payStyle_2_money = payStyle_2_money;
		this.payStyle_5_periods = payStyle_5_periods;
		this.payStyle_5_code = payStyle_5_code;
		this.payStyle_5_money = payStyle_5_money;
		this.payStyle_7_code = payStyle_7_code;
		this.payStyle_7_money = payStyle_7_money;
		this.isUpdate = isUpdate;
	}


	public Register_log() {
		// TODO Auto-generated constructor stub
	}


	public String getRegister_log_seq() {
		return register_log_seq;
	}


	public void setRegister_log_seq(String register_log_seq) {
		this.register_log_seq = register_log_seq;
	}


	public String getRegister_comboSale_id() {
		return register_comboSale_id;
	}


	public void setRegister_comboSale_id(String register_comboSale_id) {
		this.register_comboSale_id = register_comboSale_id;
	}


	public String getRegister_comboSale_grade_seq() {
		return Register_comboSale_grade_seq;
	}


	public void setRegister_comboSale_grade_seq(String register_comboSale_grade_seq) {
		Register_comboSale_grade_seq = register_comboSale_grade_seq;
	}


	public String getRegister_status() {
		return register_status;
	}


	public void setRegister_status(String register_status) {
		this.register_status = register_status;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}


	public String getReason_option() {
		return reason_option;
	}


	public void setReason_option(String reason_option) {
		this.reason_option = reason_option;
	}


	public String getUpdate_time() {
		return update_time;
	}


	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}


	public String getUpdater() {
		return updater;
	}


	public void setUpdater(String updater) {
		this.updater = updater;
	}


	public String getSubject_name() {
		return subject_name;
	}


	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}


	public String getSchool_name() {
		return school_name;
	}


	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}


	public String getClass_start_date() {
		return class_start_date;
	}


	public void setClass_start_date(String class_start_date) {
		this.class_start_date = class_start_date;
	}


	public String getClassCharge() {
		return classCharge;
	}


	public void setClassCharge(String classCharge) {
		this.classCharge = classCharge;
	}


	public String getOperationCharge() {
		return operationCharge;
	}


	public void setOperationCharge(String operationCharge) {
		this.operationCharge = operationCharge;
	}


	public String getPayStyle_2_money() {
		return payStyle_2_money;
	}


	public void setPayStyle_2_money(String payStyle_2_money) {
		this.payStyle_2_money = payStyle_2_money;
	}


	public String getPayStyle_5_periods() {
		return payStyle_5_periods;
	}


	public void setPayStyle_5_periods(String payStyle_5_periods) {
		this.payStyle_5_periods = payStyle_5_periods;
	}


	public String getPayStyle_5_code() {
		return payStyle_5_code;
	}


	public void setPayStyle_5_code(String payStyle_5_code) {
		this.payStyle_5_code = payStyle_5_code;
	}


	public String getPayStyle_5_money() {
		return payStyle_5_money;
	}


	public void setPayStyle_5_money(String payStyle_5_money) {
		this.payStyle_5_money = payStyle_5_money;
	}


	public String getPayStyle_7_code() {
		return payStyle_7_code;
	}


	public void setPayStyle_7_code(String payStyle_7_code) {
		this.payStyle_7_code = payStyle_7_code;
	}


	public String getPayStyle_7_money() {
		return payStyle_7_money;
	}


	public void setPayStyle_7_money(String payStyle_7_money) {
		this.payStyle_7_money = payStyle_7_money;
	}


	public String getIsUpdate() {
		return isUpdate;
	}


	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

}
