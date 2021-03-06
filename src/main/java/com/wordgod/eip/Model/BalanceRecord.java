package com.wordgod.eip.Model;

public class BalanceRecord {
	private String balanceRecord_seq;
	private String student_id;
    private String type;
    private String typeName;
    private String amount;
    private String remark;
    private String creater;
    private String createTime;
    
    public BalanceRecord() {}
    public BalanceRecord(
    		String balanceRecord_seq, 
    		String student_id,
    		String type,
    		String typeName,
    		String amount,
    		String remark,
    		String creater,
    		String createTime
    ){
        this.balanceRecord_seq = balanceRecord_seq;
        this.student_id = student_id;
        this.type = type;
        this.typeName = typeName; 
        this.amount = amount;
        this.remark = remark;
        this.creater = creater;
        this.createTime = createTime;
    }
	public String getBalanceRecord_seq() {
		return balanceRecord_seq;
	}
	public void setBalanceRecord_seq(String balanceRecord_seq) {
		this.balanceRecord_seq = balanceRecord_seq;
	}
	
	public String getStudent_id() {
		return student_id;
	}
	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
 
}
