package com.wordgod.eip.Model;

public class CounselingLimit2 {
	private String counselingLimit2_seq;
    private String counselingLimit1_id;
    private String from1;
    private String to1;
    private String noLimit;

    
	public CounselingLimit2(
			String counselingLimit2_seq,
			String counselingLimit1_id,
			String from1,
			String to1,
			String noLimit
	) {
		this.counselingLimit2_seq = counselingLimit2_seq;
		this.counselingLimit1_id = counselingLimit1_id;
		this.from1 = from1;
		this.to1 = to1;
		this.noLimit = noLimit;
	}


	public String getCounselingLimit2_seq() {
		return counselingLimit2_seq;
	}


	public void setCounselingLimit2_seq(String counselingLimit2_seq) {
		this.counselingLimit2_seq = counselingLimit2_seq;
	}


	public String getCounselingLimit1_id() {
		return counselingLimit1_id;
	}


	public void setCounselingLimit1_id(String counselingLimit1_id) {
		this.counselingLimit1_id = counselingLimit1_id;
	}


	public String getFrom1() {
		return from1;
	}


	public void setFrom1(String from1) {
		this.from1 = from1;
	}


	public String getTo1() {
		return to1;
	}


	public void setTo1(String to1) {
		this.to1 = to1;
	}


	public String getNoLimit() {
		return noLimit;
	}


	public void setNoLimit(String noLimit) {
		this.noLimit = noLimit;
	}




}
