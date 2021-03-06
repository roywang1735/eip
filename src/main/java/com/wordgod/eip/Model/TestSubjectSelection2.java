package com.wordgod.eip.Model;

public class TestSubjectSelection2 {

	private String testSubjectSelection2_seq;
    private String mockBaseTitle_id;
    private String slot;
    private String teacher_id;

  
	public TestSubjectSelection2(String testSubjectSelection2_seq,String mockBaseTitle_id,String slot,String teacher_id) {
		this.testSubjectSelection2_seq = testSubjectSelection2_seq;
		this.mockBaseTitle_id = mockBaseTitle_id;
		this.slot = slot;
		this.teacher_id = teacher_id;
	}


	public String getTestSubjectSelection2_seq() {
		return testSubjectSelection2_seq;
	}


	public void setTestSubjectSelection2_seq(String testSubjectSelection2_seq) {
		this.testSubjectSelection2_seq = testSubjectSelection2_seq;
	}


	public String getMockBaseTitle_id() {
		return mockBaseTitle_id;
	}


	public void setMockBaseTitle_id(String mockBaseTitle_id) {
		this.mockBaseTitle_id = mockBaseTitle_id;
	}


	public String getSlot() {
		return slot;
	}


	public void setSlot(String slot) {
		this.slot = slot;
	}


	public String getTeacher_id() {
		return teacher_id;
	}


	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}

}
