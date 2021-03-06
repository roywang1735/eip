package com.wordgod.eip.Model;

public class SignRecordChange {
	private String signRecordChange_seq;
	private String student_id;
	private String class_style;
    private String fromClassStr;
    private String toClassStr;
    private String makeUpNoUsed;
    private String update_time;
    private String updater;
    
	public SignRecordChange(String signRecordChange_seq,String student_id,String class_style,String fromClassStr,String toClassStr,String makeUpNoUsed,String update_time,String updater) {
		this.signRecordChange_seq = signRecordChange_seq;
		this.student_id = student_id;
		this.class_style = class_style;
		this.fromClassStr = fromClassStr;
		this.toClassStr = toClassStr;
		this.makeUpNoUsed = makeUpNoUsed;
		this.update_time = update_time;
		this.updater = updater;
	}

	public String getSignRecordChange_seq() {
		return signRecordChange_seq;
	}

	public void setSignRecordChange_seq(String signRecordChange_seq) {
		this.signRecordChange_seq = signRecordChange_seq;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public String getClass_style() {
		return class_style;
	}

	public void setClass_style(String class_style) {
		this.class_style = class_style;
	}

	public String getFromClassStr() {
		return fromClassStr;
	}

	public void setFromClassStr(String fromClassStr) {
		this.fromClassStr = fromClassStr;
	}

	public String getToClassStr() {
		return toClassStr;
	}

	public void setToClassStr(String toClassStr) {
		this.toClassStr = toClassStr;
	}

	public String getMakeUpNoUsed() {
		return makeUpNoUsed;
	}

	public void setMakeUpNoUsed(String makeUpNoUsed) {
		this.makeUpNoUsed = makeUpNoUsed;
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

}
