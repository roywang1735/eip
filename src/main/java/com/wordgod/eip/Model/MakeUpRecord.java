package com.wordgod.eip.Model;

public class MakeUpRecord {
	private String makeUpRecord_seq;
	private String student_id;
    private String type;
    private String typeName;
    private String amount;
    private String content;
    private String remark;
    private String creater;
    private String createTime;
    private String cutFromRegister_id;
    
    public MakeUpRecord() {}
    public MakeUpRecord(
    		String makeUpRecord_seq,
    		String student_id,
    		String type,
    		String typeName,
    		String amount,
    		String content,
    		String remark,
    		String creater,
    		String createTime,
    		String cutFromRegister_id
    ){
        this.makeUpRecord_seq = makeUpRecord_seq;
        this.student_id = student_id;
        this.type = type;
        this.typeName = typeName; 
        this.amount = amount;
        this.content = content;
        this.remark = remark;
        this.creater = creater;
        this.createTime = createTime;
        this.cutFromRegister_id = cutFromRegister_id;
    }

	public String getMakeUpRecord_seq() {
		return makeUpRecord_seq;
	}
	public void setMakeUpRecord_seq(String makeUpRecord_seq) {
		this.makeUpRecord_seq = makeUpRecord_seq;
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
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStudent_id() {
		return student_id;
	}
	public void setStudent_id(String student_id) {
		this.student_id = student_id;
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
	public String getCutFromRegister_id() {
		return cutFromRegister_id;
	}
	public void setCutFromRegister_id(String cutFromRegister_id) {
		this.cutFromRegister_id = cutFromRegister_id;
	}
 
}
