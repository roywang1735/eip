package com.wordgod.eip.Model;

public class NameFromSel {
	private String nameFromSel_seq;
    private String consultRecord_id;
    private String nameFrom_id;
    
    public NameFromSel() {}
    public NameFromSel(
    		String nameFromSel_seq, 
    		String consultRecord_id,
    		String nameFrom_id
    ){
        this.nameFromSel_seq = nameFromSel_seq;
        this.consultRecord_id = consultRecord_id;
        this.nameFrom_id = nameFrom_id;     
    }
	public String getNameFromSel_seq() {
		return nameFromSel_seq;
	}
	public void setNameFromSel_seq(String nameFromSel_seq) {
		this.nameFromSel_seq = nameFromSel_seq;
	}
	public String getConsultRecord_id() {
		return consultRecord_id;
	}
	public void setConsultRecord_id(String consultRecord_id) {
		this.consultRecord_id = consultRecord_id;
	}
	public String getNameFrom_id() {
		return nameFrom_id;
	}
	public void setNameFrom_id(String nameFrom_id) {
		this.nameFrom_id = nameFrom_id;
	}

}
