package com.wordgod.eip.Model;

public class CounselingLimit {
	private String counselingLimit_seq;
    private String comboSale_id;
    private String counselingLimitClass;
    private String subjectNameStr;
    private String creater;
    private String update_time;
    
	public CounselingLimit(
			String counselingLimit_seq,
			String comboSale_id,
			String counselingLimitClass,
			String subjectNameStr,
			String creater,
			String update_time
	) {
		this.counselingLimit_seq = counselingLimit_seq;
		this.comboSale_id = comboSale_id;
		this.counselingLimitClass = counselingLimitClass;
		this.subjectNameStr = subjectNameStr;
		this.creater = creater;
		this.update_time = update_time;
	}

	public String getCounselingLimit_seq() {
		return counselingLimit_seq;
	}

	public void setCounselingLimit_seq(String counselingLimit_seq) {
		this.counselingLimit_seq = counselingLimit_seq;
	}

	public String getComboSale_id() {
		return comboSale_id;
	}

	public void setComboSale_id(String comboSale_id) {
		this.comboSale_id = comboSale_id;
	}

	public String getCounselingLimitClass() {
		return counselingLimitClass;
	}

	public void setCounselingLimitClass(String counselingLimitClass) {
		this.counselingLimitClass = counselingLimitClass;
	}

	public String getSubjectNameStr() {
		return subjectNameStr;
	}

	public void setSubjectNameStr(String subjectNameStr) {
		this.subjectNameStr = subjectNameStr;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

}
