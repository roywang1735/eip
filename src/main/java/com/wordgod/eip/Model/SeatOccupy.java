package com.wordgod.eip.Model;

public class SeatOccupy {
	private String seatLeave_seq;
    private String school_code;
    private String setDate;
    private String slot;
    private String classroom;
    private String seat;
    private String student_no;

	public SeatOccupy(String seatLeave_seq,String school_code,String setDate,String slot,String classroom,String seat,String student_no) {
		this.seatLeave_seq = seatLeave_seq;
		this.school_code = school_code;
		this.setDate = setDate;
		this.slot = slot;
		this.classroom = classroom;
		this.seat = seat;
		this.student_no = student_no;
	}

	public String getSeatLeave_seq() {
		return seatLeave_seq;
	}

	public void setSeatLeave_seq(String seatLeave_seq) {
		this.seatLeave_seq = seatLeave_seq;
	}

	public String getSchool_code() {
		return school_code;
	}

	public void setSchool_code(String school_code) {
		this.school_code = school_code;
	}

	public String getSetDate() {
		return setDate;
	}

	public void setSetDate(String setDate) {
		this.setDate = setDate;
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
