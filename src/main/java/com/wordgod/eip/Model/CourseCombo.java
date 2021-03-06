package com.wordgod.eip.Model;

public class CourseCombo {
	private String cat1;
    private String cat2;
    private String cat3;
    
    public CourseCombo(String cat1, String cat2, String cat3) {
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
    }

	public String getCat1() {
		return cat1;
	}

	public void setCat1(String cat1) {
		this.cat1 = cat1;
	}

	public String getCat2() {
		return cat2;
	}

	public void setCat2(String cat2) {
		this.cat2 = cat2;
	}

	public String getCat3() {
		return cat3;
	}

	public void setCat3(String cat3) {
		this.cat3 = cat3;
	}
}
