package com.wordgod.eip.Model;


public class SubjectExchange2{
	private String subjectExchange2_seq;
	private String subjectExchange_id;
	private String subject_id;
	private String subject_name;


    public SubjectExchange2(
    		String subjectExchange2_seq,
    		String subjectExchange_id,
    		String subject_id,
    		String subject_name
    	) {
		this.subjectExchange2_seq = subjectExchange2_seq;
		this.subjectExchange_id = subjectExchange_id;
		this.subject_id = subject_id;
		this.subject_name = subject_name;
	}


	public String getSubjectExchange2_seq() {
		return subjectExchange2_seq;
	}


	public void setSubjectExchange2_seq(String subjectExchange2_seq) {
		this.subjectExchange2_seq = subjectExchange2_seq;
	}


	public String getSubjectExchange_id() {
		return subjectExchange_id;
	}


	public void setSubjectExchange_id(String subjectExchange_id) {
		this.subjectExchange_id = subjectExchange_id;
	}


	public String getSubject_id() {
		return subject_id;
	}


	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}


	public String getSubject_name() {
		return subject_name;
	}


	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}

}
