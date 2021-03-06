package com.wordgod.eip.Model;

public class JLM_studentPay {
	private String studentPay_seq;
    private String student_no;
    private String gradeId;
    private String gradeName;
    private String saleId;
    private String salePerson;
    private String originPrice;
    private String discountPrice;
    private String needPay;
    private String paidMoney;
    private String gradeFrom;
    private String gradeTo;
    private String detail;
    private String sitNo;

    
	public JLM_studentPay(
			String studentPay_seq,
			String student_no,
			String gradeId,
			String gradeName,
			String saleId,
			String salePerson,
			String originPrice,
			String discountPrice,
			String needPay,
			String paidMoney,
			String gradeFrom,
			String gradeTo,
			String detail,
			String sitNo
	)
	{
		this.studentPay_seq = studentPay_seq;
		this.student_no = student_no;
		this.gradeId = gradeId;
		this.gradeName = gradeName;
		this.saleId = saleId;
		this.salePerson = salePerson;
		this.originPrice = originPrice;
		this.discountPrice = discountPrice;
		this.needPay = needPay;
		this.paidMoney = paidMoney;
		this.gradeFrom = gradeFrom;
		this.gradeTo = gradeTo;
		this.detail = detail;
		this.sitNo = sitNo;
	}


	public JLM_studentPay() {
		// TODO Auto-generated constructor stub
	}


	public String getStudentPay_seq() {
		return studentPay_seq;
	}


	public void setStudentPay_seq(String studentPay_seq) {
		this.studentPay_seq = studentPay_seq;
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


	public String getGradeName() {
		return gradeName;
	}


	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}


	public String getSaleId() {
		return saleId;
	}


	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}


	public String getSalePerson() {
		return salePerson;
	}


	public void setSalePerson(String salePerson) {
		this.salePerson = salePerson;
	}


	public String getOriginPrice() {
		return originPrice;
	}


	public void setOriginPrice(String originPrice) {
		this.originPrice = originPrice;
	}


	public String getDiscountPrice() {
		return discountPrice;
	}


	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}


	public String getNeedPay() {
		return needPay;
	}


	public void setNeedPay(String needPay) {
		this.needPay = needPay;
	}


	public String getPaidMoney() {
		return paidMoney;
	}


	public void setPaidMoney(String paidMoney) {
		this.paidMoney = paidMoney;
	}


	public String getGradeFrom() {
		return gradeFrom;
	}


	public void setGradeFrom(String gradeFrom) {
		this.gradeFrom = gradeFrom;
	}


	public String getGradeTo() {
		return gradeTo;
	}


	public void setGradeTo(String gradeTo) {
		this.gradeTo = gradeTo;
	}


	public String getDetail() {
		return detail;
	}


	public void setDetail(String detail) {
		this.detail = detail;
	}


	public String getSitNo() {
		return sitNo;
	}


	public void setSitNo(String sitNo) {
		this.sitNo = sitNo;
	}

}
