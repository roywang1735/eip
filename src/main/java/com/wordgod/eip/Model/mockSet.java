package com.wordgod.eip.Model;

public class mockSet {
	private String mockSet_seq;
    private String category_id;
    private String school_id;
    private String testMethod;
    private String noName;
    private String slot;
    private String slotName;
    private String date_testMethod;
    
	public mockSet(String mockSet_seq,String category_id,String school_id,String testMethod,String noName,String slot,String slotName,String date_testMethod) {
		this.mockSet_seq = mockSet_seq;
		this.category_id = category_id;
		this.school_id = school_id;
		this.testMethod = testMethod;
		this.noName = noName;
		this.slot = slot;
		this.slotName = slotName;
		this.date_testMethod = date_testMethod;
	}

	public String getMockSet_seq() {
		return mockSet_seq;
	}

	public void setMockSet_seq(String mockSet_seq) {
		this.mockSet_seq = mockSet_seq;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getSchool_id() {
		return school_id;
	}

	public void setSchool_id(String school_id) {
		this.school_id = school_id;
	}

	public String getTestMethod() {
		return testMethod;
	}

	public void setTestMethod(String testMethod) {
		this.testMethod = testMethod;
	}

	public String getNoName() {
		return noName;
	}

	public void setNoName(String noName) {
		this.noName = noName;
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

	public String getDate_testMethod() {
		return date_testMethod;
	}

	public void setDate_testMethod(String date_testMethod) {
		this.date_testMethod = date_testMethod;
	}


}
