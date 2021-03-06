package com.wordgod.eip.Model;

public class ConsultCategory {
	private String consultCategory_seq;
    private String id;
    private String name;
    
    public ConsultCategory() {}
    public ConsultCategory(
    		String consultCategory_seq, 
    		String id,
    		String name
    ){
        this.consultCategory_seq = consultCategory_seq;
        this.id = id;
        this.name = name;     
    }

	public String getConsultCategory_seq() {
		return consultCategory_seq;
	}
	public void setConsultCategory_seq(String consultCategory_seq) {
		this.consultCategory_seq = consultCategory_seq;
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
