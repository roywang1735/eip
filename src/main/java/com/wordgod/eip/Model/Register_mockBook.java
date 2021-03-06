package com.wordgod.eip.Model;

public class Register_mockBook {
	private String register_mockDetail_seq;
    private String Register_seq;
    private String mock_id; 
    private String mockDetail_id;
    private String mockSet_id;
    private String date_mockVideo;
    private String school_mockVideo;
    private String slot_mockVideo;
    private String attend;
    
	public Register_mockBook(
		String register_mockDetail_seq,
		String Register_seq,
		String mock_id,
		String mockDetail_id,
		String mockSet_id,
		String date_mockVideo,
		String school_mockVideo,
		String slot_mockVideo,
		String attend
	){
		this.register_mockDetail_seq = register_mockDetail_seq;
		this.Register_seq = Register_seq;
		this.mock_id = mock_id;
		this.mockDetail_id = mockDetail_id;
		this.mockSet_id = mockSet_id;
		this.date_mockVideo = date_mockVideo;
		this.school_mockVideo = school_mockVideo;
		this.slot_mockVideo = slot_mockVideo;
		this.attend = attend;
	 }

	public String getRegister_mockDetail_seq() {
		return register_mockDetail_seq;
	}

	public void setRegister_mockDetail_seq(String register_mockDetail_seq) {
		this.register_mockDetail_seq = register_mockDetail_seq;
	}

	public String getRegister_seq() {
		return Register_seq;
	}

	public void setRegister_seq(String register_seq) {
		Register_seq = register_seq;
	}

	public String getMock_id() {
		return mock_id;
	}

	public void setMock_id(String mock_id) {
		this.mock_id = mock_id;
	}

	public String getMockDetail_id() {
		return mockDetail_id;
	}

	public void setMockDetail_id(String mockDetail_id) {
		this.mockDetail_id = mockDetail_id;
	}

	public String getMockSet_id() {
		return mockSet_id;
	}

	public void setMockSet_id(String mockSet_id) {
		this.mockSet_id = mockSet_id;
	}

	public String getDate_mockVideo() {
		return date_mockVideo;
	}

	public void setDate_mockVideo(String date_mockVideo) {
		this.date_mockVideo = date_mockVideo;
	}

	public String getSchool_mockVideo() {
		return school_mockVideo;
	}

	public void setSchool_mockVideo(String school_mockVideo) {
		this.school_mockVideo = school_mockVideo;
	}

	public String getSlot_mockVideo() {
		return slot_mockVideo;
	}

	public void setSlot_mockVideo(String slot_mockVideo) {
		this.slot_mockVideo = slot_mockVideo;
	}

	public String getAttend() {
		return attend;
	}

	public void setAttend(String attend) {
		this.attend = attend;
	}


}
