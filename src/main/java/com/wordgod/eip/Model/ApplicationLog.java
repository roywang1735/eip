package com.wordgod.eip.Model;

public class ApplicationLog {
	private String applicationLog_seq;
    private String update_time;
    private String updater;
    private String fun1;
    private String fun2;
    private String fun3;
    private String col1;
    private String col2;
    private String col3;
    private String col4;
    private String col5;
    private String col6;
    
	public ApplicationLog(
			String applicationLog_seq, 
			String update_time, 
			String updater, 
			String fun1, 
			String fun2,
			String fun3,
			String col1,
			String col2,
			String col3,
			String col4,
			String col5,
			String col6
	) {
		this.applicationLog_seq = applicationLog_seq;
		this.update_time = update_time;
		this.updater = updater;
		this.fun1 = fun1;
		this.fun2 = fun2;
		this.fun3 = fun3;
		this.col1 = col1;
		this.col2 = col2;
		this.col3 = col3;
		this.col4 = col4;
		this.col5 = col5;
		this.col6 = col6;
	}

	public String getApplicationLog_seq() {
		return applicationLog_seq;
	}

	public void setApplicationLog_seq(String applicationLog_seq) {
		this.applicationLog_seq = applicationLog_seq;
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

	public String getFun1() {
		return fun1;
	}

	public void setFun1(String fun1) {
		this.fun1 = fun1;
	}

	public String getFun2() {
		return fun2;
	}

	public void setFun2(String fun2) {
		this.fun2 = fun2;
	}

	public String getFun3() {
		return fun3;
	}

	public void setFun3(String fun3) {
		this.fun3 = fun3;
	}

	public String getCol1() {
		return col1;
	}

	public void setCol1(String col1) {
		this.col1 = col1;
	}

	public String getCol2() {
		return col2;
	}

	public void setCol2(String col2) {
		this.col2 = col2;
	}

	public String getCol3() {
		return col3;
	}

	public void setCol3(String col3) {
		this.col3 = col3;
	}

	public String getCol4() {
		return col4;
	}

	public void setCol4(String col4) {
		this.col4 = col4;
	}

	public String getCol5() {
		return col5;
	}

	public void setCol5(String col5) {
		this.col5 = col5;
	}

	public String getCol6() {
		return col6;
	}

	public void setCol6(String col6) {
		this.col6 = col6;
	}

}
