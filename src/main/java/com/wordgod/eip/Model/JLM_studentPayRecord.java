package com.wordgod.eip.Model;

public class JLM_studentPayRecord {

	private String studentPayRecord_seq;
    private String student_no;
    private String gradeId;
    private String payMoney;
    private String takeId;   
    private String takePerson;
    private String receiptNo;
    private String payDate;
    private String payTime;
    private String payStyle;
    
	public JLM_studentPayRecord(
			String studentPayRecord_seq, 
			String student_no,
			String gradeId,
			String payMoney,
			String takeId,
			String takePerson,
			String receiptNo,
			String payDate,
			String payTime,
			String payStyle
	) 
	{
		this.studentPayRecord_seq = studentPayRecord_seq;
		this.student_no = student_no;
		this.gradeId = gradeId;
		this.payMoney = payMoney;
		this.takeId = takeId;
		this.takePerson = takePerson;
		this.receiptNo = receiptNo;
		this.payDate = payDate;
		this.payTime = payTime;
		this.payStyle = payStyle;
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

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}

	public String getTakeId() {
		return takeId;
	}

	public void setTakeId(String takeId) {
		this.takeId = takeId;
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

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getPayStyle() {
		return payStyle;
	}

	public void setPayStyle(String payStyle) {
		this.payStyle = payStyle;
	}
 
}
