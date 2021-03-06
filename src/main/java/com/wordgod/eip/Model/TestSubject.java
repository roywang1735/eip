package com.wordgod.eip.Model;

public class TestSubject {

	private String testSubject_seq;
    private String category_seq;
    private String name;
    private String categoryName;
    private String testRound_Str;
    private String testRoundItem_Str;
    private String noLimit_Str;
  
	public TestSubject(String testSubject_seq,String category_seq,String name,String categoryName,String testRound_Str,String testRoundItem_Str,String noLimit_Str) {
		this.testSubject_seq = testSubject_seq;
		this.category_seq = category_seq;
		this.name = name;
		this.categoryName = categoryName;
		this.testRound_Str = testRound_Str;
		this.testRoundItem_Str = testRoundItem_Str;
		this.noLimit_Str = noLimit_Str;
	}


	public String getTestSubject_seq() {
		return testSubject_seq;
	}


	public void setTestSubject_seq(String testSubject_seq) {
		this.testSubject_seq = testSubject_seq;
	}


	public String getCategory_seq() {
		return category_seq;
	}


	public void setCategory_seq(String category_seq) {
		this.category_seq = category_seq;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public String getTestRound_Str() {
		return testRound_Str;
	}


	public void setTestRound_Str(String testRound_Str) {
		this.testRound_Str = testRound_Str;
	}


	public String getTestRoundItem_Str() {
		return testRoundItem_Str;
	}


	public void setTestRoundItem_Str(String testRoundItem_Str) {
		this.testRoundItem_Str = testRoundItem_Str;
	}


	public String getNoLimit_Str() {
		return noLimit_Str;
	}


	public void setNoLimit_Str(String noLimit_Str) {
		this.noLimit_Str = noLimit_Str;
	}


}
