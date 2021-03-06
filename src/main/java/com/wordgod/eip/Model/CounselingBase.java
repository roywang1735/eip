package com.wordgod.eip.Model;

public class CounselingBase {

    private String counselingBase_seq;
    private String counselingBaseTitle_seq;
    private String setDate;
    private String slotStr;
    private String updater;
    private String update_time;
    private String witLimit;
    private String categoryName;
    
    public CounselingBase(String counselingBase_seq,String counselingBaseTitle_seq,String setDate,String slotStr,String updater,String update_time,String witLimit,String categoryName) {
        this.counselingBase_seq = counselingBase_seq;
        this.counselingBaseTitle_seq = counselingBaseTitle_seq;
        this.setDate = setDate;
        this.slotStr = slotStr;
        this.updater = updater;
        this.update_time = update_time;
        this.witLimit = witLimit;
        this.categoryName = categoryName;
    }


	public CounselingBase() {
		// TODO Auto-generated constructor stub
	}


	public String getCounselingBase_seq() {
		return counselingBase_seq;
	}


	public void setCounselingBase_seq(String counselingBase_seq) {
		this.counselingBase_seq = counselingBase_seq;
	}


	public String getCounselingBaseTitle_seq() {
		return counselingBaseTitle_seq;
	}


	public void setCounselingBaseTitle_seq(String counselingBaseTitle_seq) {
		this.counselingBaseTitle_seq = counselingBaseTitle_seq;
	}


	public String getSetDate() {
		return setDate;
	}


	public void setSetDate(String setDate) {
		this.setDate = setDate;
	}


	public String getSlotStr() {
		return slotStr;
	}


	public void setSlotStr(String slotStr) {
		this.slotStr = slotStr;
	}


	public String getUpdater() {
		return updater;
	}


	public void setUpdater(String updater) {
		this.updater = updater;
	}


	public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public String getUpdate_time() {
		return update_time;
	}


	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}


	public String getWitLimit() {
		return witLimit;
	}


	public void setWitLimit(String witLimit) {
		this.witLimit = witLimit;
	}


}
