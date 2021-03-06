package com.wordgod.eip.Model;

public class CounselingLimitSubject {
	private String counselingLimitSubject_seq;
    private String counselingLimit_id;
    private String subject_id;
    private String subject_name;
    
	public CounselingLimitSubject(
			String counselingLimitSubject_seq,
			String counselingLimit_id,
			String subject_id,
			String subject_name
	) {
		this.counselingLimitSubject_seq = counselingLimitSubject_seq;
		this.counselingLimit_id = counselingLimit_id;
		this.subject_id = subject_id;
		this.subject_name = subject_name;
	}

	public String getCounselingLimitSubject_seq() {
		return counselingLimitSubject_seq;
	}

	public void setCounselingLimitSubject_seq(String counselingLimitSubject_seq) {
		this.counselingLimitSubject_seq = counselingLimitSubject_seq;
	}

	public String getCounselingLimit_id() {
		return counselingLimit_id;
	}

	public void setCounselingLimit_id(String counselingLimit_id) {
		this.counselingLimit_id = counselingLimit_id;
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
