package com.wordgod.eip.Model;
public class classRoom {
	private String classRoom_seq;
    private String school_code;
    private String name;
    private String isVideo;
    private String videoSel;
    private String capacity;
    private String seatFrom;
    private String seatTo;
    
	public classRoom(String classRoom_seq,String school_code,String name,String isVideo,String videoSel,String capacity,String seatFrom,String seatTo) {
		this.classRoom_seq = classRoom_seq;
		this.school_code = school_code;
		this.name = name;
		this.isVideo = isVideo;
		this.videoSel = videoSel;
		this.capacity = capacity;
		this.seatFrom = seatFrom;
		this.seatTo = seatTo;
	}

	public String getClassRoom_seq() {
		return classRoom_seq;
	}

	public void setClassRoom_seq(String classRoom_seq) {
		this.classRoom_seq = classRoom_seq;
	}

	public String getSchool_code() {
		return school_code;
	}

	public void setSchool_code(String school_code) {
		this.school_code = school_code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsVideo() {
		return isVideo;
	}

	public void setIsVideo(String isVideo) {
		this.isVideo = isVideo;
	}

	public String getVideoSel() {
		return videoSel;
	}

	public void setVideoSel(String videoSel) {
		this.videoSel = videoSel;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getSeatFrom() {
		return seatFrom;
	}

	public void setSeatFrom(String seatFrom) {
		this.seatFrom = seatFrom;
	}

	public String getSeatTo() {
		return seatTo;
	}

	public void setSeatTo(String seatTo) {
		this.seatTo = seatTo;
	}
	
	
}