package com.wordgod.eip.Model;

public class SignRecordMaterialHistory {
	private String signRecordMaterialHistory_seq;
	private String signRecordHistory_id;
	private String classesMateria_id;
	private String material_id;
	private String book_id;
	private String book_name;
	private String inputTex;
    private String update_time;
    private String updater;
    
	public SignRecordMaterialHistory (
			String signRecordMaterialHistory_seq,
			String signRecordHistory_id,
			String classesMateria_id,
			String material_id,
			String book_id,
			String book_name,
			String inputTex,
			String update_time,
			String updater
		) {
		this.signRecordMaterialHistory_seq = signRecordMaterialHistory_seq;
		this.signRecordHistory_id = signRecordHistory_id;
		this.classesMateria_id = classesMateria_id;
		this.material_id = material_id;
		this.book_id = book_id;
		this.book_name = book_name;
		this.inputTex = inputTex;
		this.update_time = update_time;
		this.updater = updater;	
	}

	public String getSignRecordMaterialHistory_seq() {
		return signRecordMaterialHistory_seq;
	}

	public void setSignRecordMaterialHistory_seq(String signRecordMaterialHistory_seq) {
		this.signRecordMaterialHistory_seq = signRecordMaterialHistory_seq;
	}

	public String getSignRecordHistory_id() {
		return signRecordHistory_id;
	}

	public void setSignRecordHistory_id(String signRecordHistory_id) {
		this.signRecordHistory_id = signRecordHistory_id;
	}

	public String getClassesMateria_id() {
		return classesMateria_id;
	}

	public void setClassesMateria_id(String classesMateria_id) {
		this.classesMateria_id = classesMateria_id;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public String getMaterial_id() {
		return material_id;
	}

	public void setMaterial_id(String material_id) {
		this.material_id = material_id;
	}

	public String getBook_id() {
		return book_id;
	}

	public void setBook_id(String book_id) {
		this.book_id = book_id;
	}

	public String getBook_name() {
		return book_name;
	}

	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}

	public String getInputTex() {
		return inputTex;
	}

	public void setInputTex(String inputTex) {
		this.inputTex = inputTex;
	}

}
