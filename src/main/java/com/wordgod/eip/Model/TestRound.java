package com.wordgod.eip.Model;

public class TestRound {
	private String testRound_seq;
    private String category_id;
    private String name;
    private String category_name;

    
	public TestRound(String testRound_seq,String category_id,String name,String category_name) {
		this.testRound_seq = testRound_seq;
		this.category_id = category_id;
		this.name = name;
		this.category_name = category_name;
	}




	public String getTestRound_seq() {
		return testRound_seq;
	}


	public void setTestRound_seq(String testRound_seq) {
		this.testRound_seq = testRound_seq;
	}


	public String getCategory_id() {
		return category_id;
	}


	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCategory_name() {
		return category_name;
	}


	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	
}
