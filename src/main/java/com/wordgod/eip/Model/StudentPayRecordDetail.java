package com.wordgod.eip.Model;

public class StudentPayRecordDetail {

	private String studentPayRecordDetail_seq;
    private String studentPayRecord_id;
    private String payStyleID;
    private String payStyleCode; 
    private String payStyleCodeName;
    private String payStyleMoney;
    private String payStyleDate;
    private String periods;
    private String register_id;
    
	public StudentPayRecordDetail(
			String studentPayRecordDetail_seq, 
			String studentPayRecord_id,
			String payStyleID,
			String payStyleCode,
			String payStyleCodeName,
			String payStyleMoney,
			String payStyleDate,
			String periods,
			String register_id
	) 
	{
		this.studentPayRecordDetail_seq = studentPayRecordDetail_seq;
		this.studentPayRecord_id = studentPayRecord_id;
		this.payStyleID = payStyleID;
		this.payStyleCode = payStyleCode;
		this.payStyleCodeName = payStyleCodeName;
		this.payStyleMoney = payStyleMoney;
		this.payStyleDate = payStyleDate;
		this.periods = periods;
		this.register_id = register_id;
	}

	public String getStudentPayRecordDetail_seq() {
		return studentPayRecordDetail_seq;
	}

	public void setStudentPayRecordDetail_seq(String studentPayRecordDetail_seq) {
		this.studentPayRecordDetail_seq = studentPayRecordDetail_seq;
	}

	public String getStudentPayRecord_id() {
		return studentPayRecord_id;
	}

	public void setStudentPayRecord_id(String studentPayRecord_id) {
		this.studentPayRecord_id = studentPayRecord_id;
	}

	public String getPayStyleID() {
		return payStyleID;
	}

	public void setPayStyleID(String payStyleID) {
		this.payStyleID = payStyleID;
	}

	public String getPayStyleCode() {
		return payStyleCode;
	}

	public void setPayStyleCode(String payStyleCode) {
		this.payStyleCode = payStyleCode;
	}

	public String getPayStyleCodeName() {
		return payStyleCodeName;
	}

	public void setPayStyleCodeName(String payStyleCodeName) {
		this.payStyleCodeName = payStyleCodeName;
	}

	public String getPayStyleMoney() {
		return payStyleMoney;
	}

	public void setPayStyleMoney(String payStyleMoney) {
		this.payStyleMoney = payStyleMoney;
	}

	public String getPayStyleDate() {
		return payStyleDate;
	}

	public void setPayStyleDate(String payStyleDate) {
		this.payStyleDate = payStyleDate;
	}

	public String getPeriods() {
		return periods;
	}

	public void setPeriods(String periods) {
		this.periods = periods;
	}

	public String getRegister_id() {
		return register_id;
	}

	public void setRegister_id(String register_id) {
		this.register_id = register_id;
	}
	
}
