package com.wordgod.eip.Model;

public class ConsultReasonSel {
	private String consultReasonSel_seq;
    private String consultRecord_id;
    private String consultReason_id;
    
    public ConsultReasonSel() {}
    public ConsultReasonSel(
    		String consultReasonSel_seq, 
    		String consultRecord_id,
    		String consultReason_id
    ){
        this.consultReasonSel_seq = consultReasonSel_seq;
        this.consultRecord_id = consultRecord_id;
        this.consultReason_id = consultReason_id;     
    }
	public String getConsultReasonSel_seq() {
		return consultReasonSel_seq;
	}
	public void setConsultReasonSel_seq(String consultReasonSel_seq) {
		this.consultReasonSel_seq = consultReasonSel_seq;
	}
	public String getConsultRecord_id() {
		return consultRecord_id;
	}
	public void setConsultRecord_id(String consultRecord_id) {
		this.consultRecord_id = consultRecord_id;
	}
	public String getConsultReason_id() {
		return consultReason_id;
	}
	public void setConsultReason_id(String consultReason_id) {
		this.consultReason_id = consultReason_id;
	}

}
