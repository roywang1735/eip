package com.wordgod.eip.Model;

public class School {
	private String school_seq;
	private String code;
    private String name;
	public School(String school_seq,String code,String name) {
		this.school_seq = school_seq;
		this.code = code;
		this.name = name;
	}
	
	public String getSchool_seq() {
		return school_seq;
	}

	public void setSchool_seq(String school_seq) {
		this.school_seq = school_seq;
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
