package com.wordgod.eip.Model;

public class MockLimit {
	private String mockLimit_seq;
    private String testSubjectSelection2_id;
    private String fromx;
    private String tox;
    private String noLimit;
    private String slot;

    
	public MockLimit(String mockLimit_seq,String testSubjectSelection2_id,String fromx,String tox,String noLimit,String slot) {
		this.mockLimit_seq = mockLimit_seq;
		this.testSubjectSelection2_id = testSubjectSelection2_id;
		this.fromx = fromx;
		this.tox = tox;
		this.noLimit = noLimit;
		this.slot = slot;
	}


	public String getMockLimit_seq() {
		return mockLimit_seq;
	}


	public void setMockLimit_seq(String mockLimit_seq) {
		this.mockLimit_seq = mockLimit_seq;
	}


	public String getTestSubjectSelection2_id() {
		return testSubjectSelection2_id;
	}


	public void setTestSubjectSelection2_id(String testSubjectSelection2_id) {
		this.testSubjectSelection2_id = testSubjectSelection2_id;
	}



	public String getFromx() {
		return fromx;
	}


	public void setFromx(String fromx) {
		this.fromx = fromx;
	}


	public String getTox() {
		return tox;
	}


	public void setTox(String tox) {
		this.tox = tox;
	}


	public String getNoLimit() {
		return noLimit;
	}


	public void setNoLimit(String noLimit) {
		this.noLimit = noLimit;
	}


	public String getSlot() {
		return slot;
	}


	public void setSlot(String slot) {
		this.slot = slot;
	}


}
