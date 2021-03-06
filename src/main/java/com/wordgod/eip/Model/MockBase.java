package com.wordgod.eip.Model;

public class MockBase {

	private String mockBase_seq;
    private String mockBaseTitle_seq;
    private String setDate;
    private String slot;
    private String slotName;
    private String round;
    private String updater;
    private String update_time;
    private String categoryName;
    private String witLimit;
    
  
	public MockBase(String mockBase_seq,String mockBaseTitle_seq,String setDate,String slot,String slotName,String round,String updater,String update_time,String categoryName,String witLimit) {
		this.mockBase_seq = mockBase_seq;
		this.mockBaseTitle_seq = mockBaseTitle_seq;
		this.setDate = setDate;
		this.slot = slot;
		this.slotName = slotName;
		this.round = round;
		this.updater = updater;
		this.update_time = update_time;
		this.categoryName = categoryName;
		this.witLimit = witLimit;
	}


	public String getMockBase_seq() {
		return mockBase_seq;
	}


	public void setMockBase_seq(String mockBase_seq) {
		this.mockBase_seq = mockBase_seq;
	}


	public String getMockBaseTitle_seq() {
		return mockBaseTitle_seq;
	}


	public void setMockBaseTitle_seq(String mockBaseTitle_seq) {
		this.mockBaseTitle_seq = mockBaseTitle_seq;
	}


	public String getSetDate() {
		return setDate;
	}


	public void setSetDate(String setDate) {
		this.setDate = setDate;
	}


	public String getSlot() {
		return slot;
	}


	public void setSlot(String slot) {
		this.slot = slot;
	}


	public String getSlotName() {
		return slotName;
	}


	public void setSlotName(String slotName) {
		this.slotName = slotName;
	}


	public String getRound() {
		return round;
	}


	public void setRound(String round) {
		this.round = round;
	}


	public String getUpdater() {
		return updater;
	}


	public void setUpdater(String updater) {
		this.updater = updater;
	}


	public String getUpdate_time() {
		return update_time;
	}


	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}


	public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public String getWitLimit() {
		return witLimit;
	}


	public void setWitLimit(String witLimit) {
		this.witLimit = witLimit;
	}


}
