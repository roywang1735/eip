package com.wordgod.eip.Model;

public class SubjectContent {
	private String subjectContent_seq;
    private String code;
    private String name;

    
	public SubjectContent(
			String subjectContent_seq,
			String code,
			String name
		) {
		this.subjectContent_seq = subjectContent_seq;
		this.code = code;
		this.name = name;
	}


	public String getSubjectContent_seq() {
		return subjectContent_seq;
	}


	public void setSubjectContent_seq(String subjectContent_seq) {
		this.subjectContent_seq = subjectContent_seq;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
}
