package com.wordgod.eip.Model;

public class StudentPayRecord {

	private String studentPayRecord_seq;
    private String student_no;
    private String register_id;
    private String payMoney;
    private String actualPrice;
    private String takePerson;
    private String receiptNo;
    private String payDate;
    private String payStyle;
    private String school_code;
    private String school_name;
    private String studentName;
    private String payWay;
    
	public StudentPayRecord(
			String studentPayRecord_seq, 
			String student_no,
			String register_id,
			String payMoney,
			String actualPrice,
			String takePerson,
			String receiptNo,
			String payDate,
			String payStyle,
			String school_code,
			String school_name,
			String studentName,
			String payWay
	) 
	{
		this.studentPayRecord_seq = studentPayRecord_seq;
		this.student_no = student_no;
		this.register_id = register_id;
		this.payMoney = payMoney;
		this.actualPrice = actualPrice;
		this.takePerson = takePerson;
		this.receiptNo = receiptNo;
		this.payDate = payDate;
		this.payStyle = payStyle;
		this.school_code = school_code;
		this.school_name = school_name;
		this.studentName = studentName;
		this.payWay = payWay;
	}

	public StudentPayRecord() {
		// TODO Auto-generated constructor stub
	}

	public String getStudentPayRecord_seq() {
		return studentPayRecord_seq;
	}

	public void setStudentPayRecord_seq(String studentPayRecord_seq) {
		this.studentPayRecord_seq = studentPayRecord_seq;
	}

	public String getStudent_no() {
		return student_no;
	}

	public void setStudent_no(String student_no) {
		this.student_no = student_no;
	}

	public String getRegister_id() {
		return register_id;
	}

	public void setRegister_id(String register_id) {
		this.register_id = register_id;
	}

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}

	public String getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(String actualPrice) {
		this.actualPrice = actualPrice;
	}

	public String getTakePerson() {
		return takePerson;
	}

	public void setTakePerson(String takePerson) {
		this.takePerson = takePerson;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getPayStyle() {
		return payStyle;
	}

	public void setPayStyle(String payStyle) {
		this.payStyle = payStyle;
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

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
 
}
