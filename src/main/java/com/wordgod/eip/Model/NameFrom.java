package com.wordgod.eip.Model;

public class NameFrom {
	private String nameFrom_seq;
    private String id;
    private String name;
    
    public NameFrom() {}
    public NameFrom(
    		String nameFrom_seq, 
    		String id,
    		String name
    ){
        this.nameFrom_seq = nameFrom_seq;
        this.id = id;
        this.name = name;     
    }
	public String getNameFrom_seq() {
		return nameFrom_seq;
	}
	public void setNameFrom_seq(String nameFrom_seq) {
		this.nameFrom_seq = nameFrom_seq;
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
