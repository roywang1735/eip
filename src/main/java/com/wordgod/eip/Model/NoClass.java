package com.wordgod.eip.Model;

public class NoClass {
	private String noClass_seq;
	private String teacher_id;
	private String dateSel;
	private String timeFrom;
	private String timeTo;
	private String noClassType;
	private String slot_id;
    
    public NoClass(String noClass_seq,String teacher_id,String dateSel,String timeFrom,String timeTo,String noClassType,String slot_id) {
        this.noClass_seq = noClass_seq;
        this.teacher_id = teacher_id;
        this.dateSel = dateSel;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.noClassType = noClassType;
        this.slot_id = slot_id;
    }

	public String getNoClass_seq() {
		return noClass_seq;
	}

	public void setNoClass_seq(String noClass_seq) {
		this.noClass_seq = noClass_seq;
	}

	public String getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}

	public String getDateSel() {
		return dateSel;
	}

	public void setDateSel(String dateSel) {
		this.dateSel = dateSel;
	}

	public String getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(String timeFrom) {
		this.timeFrom = timeFrom;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}

	public String getNoClassType() {
		return noClassType;
	}

	public void setNoClassType(String noClassType) {
		this.noClassType = noClassType;
	}

	public String getSlot_id() {
		return slot_id;
	}

	public void setSlot_id(String slot_id) {
		this.slot_id = slot_id;
	}
    

}
