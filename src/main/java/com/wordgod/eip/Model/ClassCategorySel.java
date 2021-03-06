package com.wordgod.eip.Model;

public class ClassCategorySel {
	private String classCategorySel_seq;
    private String consultRecord_id;
    private String category_id;
    
    public ClassCategorySel() {}
    public ClassCategorySel(
    		String classCategorySel_seq, 
    		String consultRecord_id,
    		String category_id
    ){
        this.classCategorySel_seq = classCategorySel_seq;
        this.consultRecord_id = consultRecord_id;
        this.category_id = category_id;     
    }
	public String getClassCategorySel_seq() {
		return classCategorySel_seq;
	}
	public void setClassCategorySel_seq(String classCategorySel_seq) {
		this.classCategorySel_seq = classCategorySel_seq;
	}
	public String getConsultRecord_id() {
		return consultRecord_id;
	}
	public void setConsultRecord_id(String consultRecord_id) {
		this.consultRecord_id = consultRecord_id;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}


}
