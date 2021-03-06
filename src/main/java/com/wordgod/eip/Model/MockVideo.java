package com.wordgod.eip.Model;

public class MockVideo {
	private String mockVideo_seq;
    private String mock_id;
    private String subject_id;
    private String subject_name;

    
	public MockVideo(String mockVideo_seq,String mock_id,String subject_id,String subject_name) {
		this.mockVideo_seq = mockVideo_seq;
		this.mock_id = mock_id;
		this.subject_id = subject_id;
		this.subject_name = subject_name;
	}


	public String getMockVideo_seq() {
		return mockVideo_seq;
	}


	public void setMockVideo_seq(String mockVideo_seq) {
		this.mockVideo_seq = mockVideo_seq;
	}


	public String getMock_id() {
		return mock_id;
	}


	public void setMock_id(String mock_id) {
		this.mock_id = mock_id;
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
