package com.wordgod.eip.Model;

public class Account_authority {
	private String account0;
    private String authority;
    private String authority_name;
    
    public Account_authority(String account0, String authority, String authority_name) {
        this.account0 = account0;
        this.authority = authority;
        this.authority_name = authority_name;
    }

	public Account_authority() {
		// TODO Auto-generated constructor stub
	}


	public String getAccount0() {
		return account0;
	}

	public void setAccount0(String account0) {
		this.account0 = account0;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getAuthority_name() {
		return authority_name;
	}

	public void setAuthority_name(String authority_name) {
		this.authority_name = authority_name;
	}

}
