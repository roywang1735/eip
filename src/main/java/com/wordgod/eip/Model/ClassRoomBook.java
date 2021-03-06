package com.wordgod.eip.Model;
public class ClassRoomBook {
	private String classRoomBook_seq;
	private String school_code;
    private String bookDate;
    private String slot;
    private String bookClassRoom;
    private String bookContent;
    private String assigner;
    private String assignerTime;
    
	public ClassRoomBook(String classRoomBook_seq,String school_code,String bookDate,String slot,String bookClassRoom,String bookContent,String assigner,String assignerTime) {
		this.classRoomBook_seq = classRoomBook_seq;
		this.school_code = school_code;
		this.bookDate = bookDate;
		this.slot = slot;
		this.bookClassRoom = bookClassRoom;
		this.bookContent = bookContent;
		this.assigner = assigner;
		this.assignerTime = assignerTime;
	}

	public ClassRoomBook() {
		// TODO Auto-generated constructor stub
	}

	public String getClassRoomBook_seq() {
		return classRoomBook_seq;
	}

	public void setClassRoomBook_seq(String classRoomBook_seq) {
		this.classRoomBook_seq = classRoomBook_seq;
	}

	public String getSchool_code() {
		return school_code;
	}

	public void setSchool_code(String school_code) {
		this.school_code = school_code;
	}

	public String getBookDate() {
		return bookDate;
	}

	public void setBookDate(String bookDate) {
		this.bookDate = bookDate;
	}

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public String getBookClassRoom() {
		return bookClassRoom;
	}

	public void setBookClassRoom(String bookClassRoom) {
		this.bookClassRoom = bookClassRoom;
	}

	public String getBookContent() {
		return bookContent;
	}

	public void setBookContent(String bookContent) {
		this.bookContent = bookContent;
	}

	public String getAssigner() {
		return assigner;
	}

	public void setAssigner(String assigner) {
		this.assigner = assigner;
	}

	public String getAssignerTime() {
		return assignerTime;
	}

	public void setAssignerTime(String assignerTime) {
		this.assignerTime = assignerTime;
	}

}