package com.wordgod.eip.Model;

public class ConsultReason {
	private String consultReason_seq;
    private String id;
    private String name;
    
    public ConsultReason() {}
    public ConsultReason(
    		String consultReason_seq, 
    		String id,
    		String name
    ){
        this.consultReason_seq = consultReason_seq;
        this.id = id;
        this.name = name;     
    }
	public String getConsultReason_seq() {
		return consultReason_seq;
	}
	public void setConsultReason_seq(String consultReason_seq) {
		this.consultReason_seq = consultReason_seq;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
