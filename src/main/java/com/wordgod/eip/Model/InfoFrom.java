package com.wordgod.eip.Model;

public class InfoFrom {
	private String infoFrom_seq;
    private String id;
    private String name;
    
    public InfoFrom() {}
    public InfoFrom(
    		String infoFrom_seq, 
    		String id,
    		String name
    ){
        this.infoFrom_seq = infoFrom_seq;
        this.id = id;
        this.name = name;     
    }
	public String getInfoFrom_seq() {
		return infoFrom_seq;
	}
	public void setInfoFrom_seq(String infoFrom_seq) {
		this.infoFrom_seq = infoFrom_seq;
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
