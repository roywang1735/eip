package com.wordgod.eip.Model;

public class HistoryMatrix {
	private String signRecordHistory_seq;
	private String mockVideoHistory_seq;
    private String mockBaseBook_seq;
    private String student_id;
    private String ch_name;
    private String attend;
    private String slot;
    private String classroom;
    private String seat;
    private String student_no;
    
    public HistoryMatrix(String signRecordHistory_seq,String mockVideoHistory_seq,String mockBaseBook_seq,String student_id,String ch_name,String attend,String slot,String classroom,String seat,String student_no) {
        this.signRecordHistory_seq = signRecordHistory_seq;
        this.mockVideoHistory_seq = mockVideoHistory_seq;
        this.mockBaseBook_seq = mockBaseBook_seq;
        this.student_id = student_id;
        this.ch_name = ch_name;
        this.attend = attend;
        this.slot = slot;
        this.classroom = classroom;
        this.seat = seat;
        this.student_no = student_no;
    }

	public String getSignRecordHistory_seq() {
		return signRecordHistory_seq;
	}

	public void setSignRecordHistory_seq(String signRecordHistory_seq) {
		this.signRecordHistory_seq = signRecordHistory_seq;
	}

	public String getMockVideoHistory_seq() {
		return mockVideoHistory_seq;
	}

	public void setMockVideoHistory_seq(String mockVideoHistory_seq) {
		this.mockVideoHistory_seq = mockVideoHistory_seq;
	}

	public String getMockBaseBook_seq() {
		return mockBaseBook_seq;
	}

	public void setMockBaseBook_seq(String mockBaseBook_seq) {
		this.mockBaseBook_seq = mockBaseBook_seq;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public String getCh_name() {
		return ch_name;
	}

	public void setCh_name(String ch_name) {
		this.ch_name = ch_name;
	}

	public String getAttend() {
		return attend;
	}

	public void setAttend(String attend) {
		this.attend = attend;
	}

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public String getClassroom() {
		return classroom;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public String getStudent_no() {
		return student_no;
	}

	public void setStudent_no(String student_no) {
		this.student_no = student_no;
	}

}
