package com.wordgod.eip.Model;

public class Account {
	private String ADDRESS;
    private String EMAIL;
    
    public Account(String ADDRESS, String EMAIL) {
        this.ADDRESS = ADDRESS;
        this.EMAIL = EMAIL;
    }

	public String getADDRESS() {
		return ADDRESS;
	}

	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}

	public String getEMAIL() {
		return EMAIL;
	}

	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	} 
    

}
