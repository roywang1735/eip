package com.wordgod.eip.Model;

public class SubjectExchange{
	private String subjectExchange_seq;
	private String creater;
	private String update_time;
	private String subject_1_str;
	private String subject_2_str;

    public SubjectExchange(
    		String subjectExchange_seq,
    		String creater,
    		String update_time,
    		String subject_1_str,
    		String subject_2_str
    	) {
		this.subjectExchange_seq = subjectExchange_seq;
		this.creater = creater;
		this.update_time = update_time;
		this.subject_1_str = subject_1_str;
		this.subject_2_str = subject_2_str;
	}

	public String getSubjectExchange_seq() {
		return subjectExchange_seq;
	}

	public void setSubjectExchange_seq(String subjectExchange_seq) {
		this.subjectExchange_seq = subjectExchange_seq;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getSubject_1_str() {
		return subject_1_str;
	}

	public void setSubject_1_str(String subject_1_str) {
		this.subject_1_str = subject_1_str;
	}

	public String getSubject_2_str() {
		return subject_2_str;
	}

	public void setSubject_2_str(String subject_2_str) {
		this.subject_2_str = subject_2_str;
	}

}
