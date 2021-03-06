package com.wordgod.eip.Model;

public class Register_outPublisher {
	private String register_outPublisher_seq;
	private String comboSale_id;
	private String Register_seq;
	private String book_id;
	private String book_name;
	private String sellPrice;
    private String isIssue;
    private String issueUpdater;
    private String issueDate;
    private String remark;
    private String update_time;
    private String comboSale_name;
    private String creater;
    private String payOffRelease;
    private String reason;
    
	public Register_outPublisher(
			String register_outPublisher_seq,
			String comboSale_id,
			String Register_seq, 
			String book_id,
			String book_name,
			String sellPrice,
			String isIssue,
			String issueUpdater,
			String issueDate,
			String remark,
			String update_time,
			String comboSale_name,
			String creater,
			String payOffRelease,
			String reason
	) {
		this.register_outPublisher_seq = register_outPublisher_seq;
		this.comboSale_id = comboSale_id;
		this.Register_seq = Register_seq;
		this.book_id = book_id;
		this.book_name = book_name;
		this.sellPrice = sellPrice;
		this.isIssue = isIssue;
		this.issueUpdater = issueUpdater;
		this.issueDate = issueDate;
		this.remark = remark;
		this.update_time = update_time;
		this.comboSale_name = comboSale_name;
		this.creater = creater; 
		this.payOffRelease = payOffRelease;
		this.reason = reason;
	}

	public String getRegister_outPublisher_seq() {
		return register_outPublisher_seq;
	}

	public void setRegister_outPublisher_seq(String register_outPublisher_seq) {
		this.register_outPublisher_seq = register_outPublisher_seq;
	}

	public String getComboSale_id() {
		return comboSale_id;
	}

	public void setComboSale_id(String comboSale_id) {
		this.comboSale_id = comboSale_id;
	}

	public String getRegister_seq() {
		return Register_seq;
	}

	public void setRegister_seq(String register_seq) {
		Register_seq = register_seq;
	}

	public String getBook_id() {
		return book_id;
	}

	public void setBook_id(String book_id) {
		this.book_id = book_id;
	}

	public String getBook_name() {
		return book_name;
	}

	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}

	public String getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getIsIssue() {
		return isIssue;
	}

	public void setIsIssue(String isIssue) {
		this.isIssue = isIssue;
	}

	public String getIssueUpdater() {
		return issueUpdater;
	}

	public void setIssueUpdater(String issueUpdater) {
		this.issueUpdater = issueUpdater;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getComboSale_name() {
		return comboSale_name;
	}

	public void setComboSale_name(String comboSale_name) {
		this.comboSale_name = comboSale_name;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getPayOffRelease() {
		return payOffRelease;
	}

	public void setPayOffRelease(String payOffRelease) {
		this.payOffRelease = payOffRelease;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
