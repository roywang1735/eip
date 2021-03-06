package com.wordgod.eip.Model;

public class CounselingLimit1 {
	private String counselingLimit1_seq;
    private String counselingBaseTitle_id;
    private String slot;
    private String teacher_id;
    private String teacher_name;
    
	public CounselingLimit1(
			String counselingLimit1_seq,
			String counselingBaseTitle_id,
			String slot,
			String teacher_id,
			String teacher_name
	) {
		this.counselingLimit1_seq = counselingLimit1_seq;
		this.counselingBaseTitle_id = counselingBaseTitle_id;
		this.slot = slot;
		this.teacher_id = teacher_id;
		this.teacher_name = teacher_name;
	}

	public String getCounselingLimit1_seq() {
		return counselingLimit1_seq;
	}

	public void setCounselingLimit1_seq(String counselingLimit1_seq) {
		this.counselingLimit1_seq = counselingLimit1_seq;
	}

	public String getCounselingBaseTitle_id() {
		return counselingBaseTitle_id;
	}

	public void setCounselingBaseTitle_id(String counselingBaseTitle_id) {
		this.counselingBaseTitle_id = counselingBaseTitle_id;
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

	public String getTeacher_name() {
		return teacher_name;
	}

	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

}
