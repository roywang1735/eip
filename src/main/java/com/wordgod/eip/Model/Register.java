package com.wordgod.eip.Model;

public class Register {
	private String Register_seq;
	private String student_seq;
	private String originPrice;
	private String actualPrice;
	private String discountPrice;
	private String paid;
	private String stillNeed;
	private String comment;
	private String creater;
	private String orderStatus;
	private String update_time;
	private String comboSaleString;
	private String gradeString;
	private String remarkDetail;
	private String receipDetail;
	private String cancelRegister;
	
	public Register(String Register_seq,String student_seq,String originPrice,String actualPrice,String discountPrice,String paid,String stillNeed,String comment,String creater,String orderStatus,String update_time,String comboSaleString,String gradeString,String remarkDetail,String receipDetail,String cancelRegister) {
		this.Register_seq = Register_seq;
		this.student_seq = student_seq;
		this.originPrice = originPrice;
		this.actualPrice = actualPrice;
		this.discountPrice = discountPrice;
		this.paid = paid;
		this.stillNeed = stillNeed;
		this.comment = comment;
		this.creater = creater;
		this.orderStatus = orderStatus;
		this.update_time = update_time;
		this.comboSaleString = comboSaleString;
		this.gradeString = gradeString;
		this.remarkDetail = remarkDetail;
		this.receipDetail = receipDetail;
		this.cancelRegister = cancelRegister;
	}

	public Register() {
		// TODO Auto-generated constructor stub
	}

	public String getRegister_seq() {
		return Register_seq;
	}

	public void setRegister_seq(String register_seq) {
		Register_seq = register_seq;
	}

	public String getStudent_seq() {
		return student_seq;
	}

	public void setStudent_seq(String student_seq) {
		this.student_seq = student_seq;
	}

	public String getOriginPrice() {
		return originPrice;
	}

	public void setOriginPrice(String originPrice) {
		this.originPrice = originPrice;
	}

	public String getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(String actualPrice) {
		this.actualPrice = actualPrice;
	}

	public String getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public String getStillNeed() {
		return stillNeed;
	}

	public void setStillNeed(String stillNeed) {
		this.stillNeed = stillNeed;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String flowStatus) {
		this.orderStatus = orderStatus;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getComboSaleString() {
		return comboSaleString;
	}

	public void setComboSaleString(String comboSaleString) {
		this.comboSaleString = comboSaleString;
	}

	public String getGradeString() {
		return gradeString;
	}

	public void setGradeString(String gradeString) {
		this.gradeString = gradeString;
	}

	public String getRemarkDetail() {
		return remarkDetail;
	}

	public void setRemarkDetail(String remarkDetail) {
		this.remarkDetail = remarkDetail;
	}

	public String getReceipDetail() {
		return receipDetail;
	}

	public void setReceipDetail(String receipDetail) {
		this.receipDetail = receipDetail;
	}

	public String getCancelRegister() {
		return cancelRegister;
	}

	public void setCancelRegister(String cancelRegister) {
		this.cancelRegister = cancelRegister;
	}
	
}
