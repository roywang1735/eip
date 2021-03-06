package com.wordgod.eip.Model;

public class InfoFromSel {
	private String infoFromSel_seq;
    private String consultRecord_id;
    private String infoFrom_id;
    
    public InfoFromSel() {}
    public InfoFromSel(
    		String infoFromSel_seq, 
    		String consultRecord_id,
    		String infoFrom_id
    ){
        this.infoFromSel_seq = infoFromSel_seq;
        this.consultRecord_id = consultRecord_id;
        this.infoFrom_id = infoFrom_id;     
    }
	public String getInfoFromSel_seq() {
		return infoFromSel_seq;
	}
	public void setInfoFromSel_seq(String infoFromSel_seq) {
		this.infoFromSel_seq = infoFromSel_seq;
	}
	public String getConsultRecord_id() {
		return consultRecord_id;
	}
	public void setConsultRecord_id(String consultRecord_id) {
		this.consultRecord_id = consultRecord_id;
	}
	public String getInfoFrom_id() {
		return infoFrom_id;
	}
	public void setInfoFrom_id(String infoFrom_id) {
		this.infoFrom_id = infoFrom_id;
	}


}
