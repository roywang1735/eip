package com.wordgod.eip.Model;

public class OtherSell {

	private String otherSell_seq;
    private String cat1;
    private String sell_id;
    private String sell_name;
    private String amount;
    private String sellPrice;
    private String studentNo;
    private String studentName;
    private String creater;
    private String createTime;
    private String comment;
    
  
	public OtherSell(String otherSell_seq,String cat1,String sell_id,String sell_name,String amount,String sellPrice,String studentNo,String studentName,String creater,String createTime,String comment) {
		this.otherSell_seq = otherSell_seq;
		this.cat1 = cat1;
		this.sell_id = sell_id;
		this.sell_name = sell_name;
		this.amount = amount;
		this.sellPrice = sellPrice;
		this.studentNo = studentNo;
		this.studentName = studentName;
		this.creater = creater;
		this.createTime = createTime;
		this.comment = comment;
	}

	public String getOtherSell_seq() {
		return otherSell_seq;
	}

	public void setOtherSell_seq(String otherSell_seq) {
		this.otherSell_seq = otherSell_seq;
	}

	public String getCat1() {
		return cat1;
	}

	public void setCat1(String cat1) {
		this.cat1 = cat1;
	}

	public String getSell_id() {
		return sell_id;
	}

	public void setSell_id(String sell_id) {
		this.sell_id = sell_id;
	}

	public String getSell_name() {
		return sell_name;
	}

	public void setSell_name(String sell_name) {
		this.sell_name = sell_name;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
