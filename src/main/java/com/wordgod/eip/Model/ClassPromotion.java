package com.wordgod.eip.Model;

public class ClassPromotion {
	private String classPromotion_seq;
	private String promoType;
    private String promoName;
    private String startDate;
    private String endDate;
    private String extendDate;
    private String enabled;
    private String updater;
    private String update_time;
    private String chool_code_Str;
    private String active;
    private String comment;
    private String approve;
    private String ranking;
    
	public ClassPromotion(
			String classPromotion_seq,
			String promoType,
			String promoName,
			String startDate,
			String endDate,
			String extendDate,
			String enabled,
			String updater,
			String update_time,
			String chool_code_Str,
			String active,
			String comment,
			String approve,
			String ranking
		){
		this.classPromotion_seq = classPromotion_seq;
		this.promoType = promoType; 
		this.promoName = promoName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.enabled = enabled;
		this.extendDate = extendDate;
		this.updater = updater;
		this.updater = update_time;
		this.chool_code_Str = chool_code_Str;
		this.active = active;
		this.comment = comment;
		this.approve = approve;
		this.ranking = ranking;
	}

	public ClassPromotion() {
		// TODO Auto-generated constructor stub
	}

	public String getClassPromotion_seq() {
		return classPromotion_seq;
	}

	public void setClassPromotion_seq(String classPromotion_seq) {
		this.classPromotion_seq = classPromotion_seq;
	}

	public String getPromoType() {
		return promoType;
	}

	public void setPromoType(String promoType) {
		this.promoType = promoType;
	}

	public String getPromoName() {
		return promoName;
	}

	public void setPromoName(String promoName) {
		this.promoName = promoName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getExtendDate() {
		return extendDate;
	}

	public void setExtendDate(String extendDate) {
		this.extendDate = extendDate;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getChool_code_Str() {
		return chool_code_Str;
	}

	public void setChool_code_Str(String chool_code_Str) {
		this.chool_code_Str = chool_code_Str;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getApprove() {
		return approve;
	}

	public void setApprove(String approve) {
		this.approve = approve;
	}

	public String getRanking() {
		return ranking;
	}

	public void setRanking(String ranking) {
		this.ranking = ranking;
	}

}
