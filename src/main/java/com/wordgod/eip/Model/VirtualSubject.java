package com.wordgod.eip.Model;


public class VirtualSubject {
	private String virtualSubject_seq;
	private String subject_id;
    private String child_subject_id;
    private String child_subject_name;
    private String child_category_name;
    private String origin_price;
    private String sale_price;

    
    public VirtualSubject(
    		String virtualSubject_seq,
    		String subject_id,
    		String child_subject_id,
    		String child_subject_name,
    		String child_category_name,
    		String origin_price,
    		String sale_price
        ) {
        this.virtualSubject_seq = virtualSubject_seq;
        this.subject_id = subject_id;
        this.child_subject_id = child_subject_id;
        this.child_subject_name = child_subject_name;
        this.child_category_name = child_category_name;
        this.origin_price  = origin_price;
        this.sale_price  = sale_price;
    }

	public VirtualSubject() {
		// TODO Auto-generated constructor stub
	}

	public String getVirtualSubject_seq() {
		return virtualSubject_seq;
	}

	public void setVirtualSubject_seq(String virtualSubject_seq) {
		this.virtualSubject_seq = virtualSubject_seq;
	}

	public String getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}

	public String getChild_subject_id() {
		return child_subject_id;
	}

	public void setChild_subject_id(String child_subject_id) {
		this.child_subject_id = child_subject_id;
	}

	public String getChild_subject_name() {
		return child_subject_name;
	}

	public void setChild_subject_name(String child_subject_name) {
		this.child_subject_name = child_subject_name;
	}

	public String getChild_category_name() {
		return child_category_name;
	}

	public void setChild_category_name(String child_category_name) {
		this.child_category_name = child_category_name;
	}

	public String getOrigin_price() {
		return origin_price;
	}

	public void setOrigin_price(String origin_price) {
		this.origin_price = origin_price;
	}

	public String getSale_price() {
		return sale_price;
	}

	public void setSale_price(String sale_price) {
		this.sale_price = sale_price;
	}

}
