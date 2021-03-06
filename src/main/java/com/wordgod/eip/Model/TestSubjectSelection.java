package com.wordgod.eip.Model;

public class TestSubjectSelection {

	private String testSubjectSelection_seq;
    private String testSubject_id;
    private String testRound;
  
	public TestSubjectSelection(String testSubjectSelection_seq,String testSubject_id,String testRound) {
		this.testSubjectSelection_seq = testSubjectSelection_seq;
		this.testSubject_id = testSubject_id;
		this.testRound = testRound;
	}

	public String getTestSubjectSelection_seq() {
		return testSubjectSelection_seq;
	}

	public void setTestSubjectSelection_seq(String testSubjectSelection_seq) {
		this.testSubjectSelection_seq = testSubjectSelection_seq;
	}

	public String getTestSubject_id() {
		return testSubject_id;
	}

	public void setTestSubject_id(String testSubject_id) {
		this.testSubject_id = testSubject_id;
	}


	public String getTestRound() {
		return testRound;
	}

	public void setTestRound(String testRound) {
		this.testRound = testRound;
	}

}
