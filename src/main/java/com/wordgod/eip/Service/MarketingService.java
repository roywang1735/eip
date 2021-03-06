package com.wordgod.eip.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.wordgod.eip.Model.*;



@Service
public class MarketingService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	public List<ClassPromotion> getClassPromotion(String classPromotion_seq,String school_code,String enabled,String promoType,String active,String approve,String dateLimit){ 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(new Date());
		String sql = 
				 "select * from eip.classPromotion"+
				 " where 1=1";
		
		if (classPromotion_seq != null && !classPromotion_seq.isEmpty()) {sql += " and classPromotion_seq = " + classPromotion_seq;}		
		if (school_code != null && !school_code.isEmpty()) {
			sql += " and classPromotion_seq in (select classPromotion_id from eip.promoApply where school_code='"+school_code+"')";
		}
		if (enabled != null && !enabled.isEmpty()) {sql += " and enabled=" + enabled;}
		if (promoType != null && !promoType.isEmpty()) {sql += " and promoType='" +promoType+"'";}
		if (active != null && !active.isEmpty()) {sql += " and active ="+active;}
		if (approve != null && !approve.isEmpty()) {sql += " and approve ="+approve;}
		if (dateLimit != null && dateLimit.equals("1")) {
			sql += " and "+today+">=startDate and ("+today+"<=endDate || "+today+"<=extendDate)";
		}
		    sql += " order by CAST(ranking AS unsigned)";
	
		List<ClassPromotion> LClassPromotion = jdbcTemplate.query(sql,(result,rowNum)->new ClassPromotion(
		    		result.getString("classPromotion_seq"),
		    		result.getString("promoType"),
		    		result.getString("promoName"),
		    		result.getString("startDate"),
		    		result.getString("endDate"),
		    		result.getString("extendDate"),
		    		result.getString("enabled"),
		    		result.getString("updater"),
		    		result.getString("update_time"),
		    		"",
		    		result.getString("active"),
		    		result.getString("comment"),
		    		result.getString("approve"),
		    		result.getString("ranking")
                ));
		
		for(int i=0;i<LClassPromotion.size();i++) {
			List<PromoApply> LPromoApply = getPromoApply(LClassPromotion.get(i).getClassPromotion_seq());
			String school_code_Str="";
			for(int j=0;j<LPromoApply.size();j++) {
				school_code_Str +=LPromoApply.get(j).getSchool_name()+"<br>";
			}
			LClassPromotion.get(i).setChool_code_Str(school_code_Str);
			
			String startDate = LClassPromotion.get(i).getStartDate();
			if(startDate!=null && startDate.length()==8) {
				LClassPromotion.get(i).setStartDate(startDate.substring(4,6)+"/"+startDate.substring(6,8)+"/"+startDate.substring(0,4));				
			}
			String endDate   = LClassPromotion.get(i).getEndDate();
			if(endDate!=null && endDate.length()==8) {
				LClassPromotion.get(i).setEndDate(endDate.substring(4,6)+"/"+endDate.substring(6,8)+"/"+endDate.substring(0,4));				
			}
			
			String extendDate   = LClassPromotion.get(i).getExtendDate();
			if(extendDate!=null && extendDate.length()==8) {
				LClassPromotion.get(i).setExtendDate(extendDate.substring(4,6)+"/"+extendDate.substring(6,8)+"/"+extendDate.substring(0,4));				
			}				
		}
	    return LClassPromotion;
	}
	
	public List<PromoApply> getPromoApply(String classPromotion_id){ 
		String sql = "select a.*,b.name as school_name from eip.promoApply a,school b"+
				 	 " where a.school_code = b.code";
		
		if (classPromotion_id != null && !classPromotion_id.isEmpty()) {sql += " and classPromotion_id = "+classPromotion_id;}				
		sql += " order by school_code desc";
		
		List<PromoApply> LPromoApply = jdbcTemplate.query(sql,(result,rowNum)->new PromoApply(
	    		result.getString("promoApply_seq"),
	    		result.getString("classPromotion_id"),
	    		result.getString("school_code"),
	    		result.getString("school_name")
                ));
		
	    return LPromoApply;
	}	
	

	public String PromoSave(ClassPromotion classPromotion,String classPromotion_id,String promoType,String[] A_school_code,String approve) {
        //更改日期格式
		if(classPromotion.getStartDate().length()>9){
			classPromotion.setStartDate(classPromotion.getStartDate().substring(6,10)+classPromotion.getStartDate().substring(0,2)+classPromotion.getStartDate().substring(3,5));
		}
		if(classPromotion.getEndDate().length()>9){
			classPromotion.setEndDate(classPromotion.getEndDate().substring(6,10)+classPromotion.getEndDate().substring(0,2)+classPromotion.getEndDate().substring(3,5));
		}
		if(classPromotion.getExtendDate().length()>9){
			classPromotion.setExtendDate(classPromotion.getExtendDate().substring(6,10)+classPromotion.getExtendDate().substring(0,2)+classPromotion.getExtendDate().substring(3,5));
		}
		
		String classPromotion_id_new = "";
		if(classPromotion_id!=null && !classPromotion_id.equals("")) {
			jdbcTemplate.update("UPDATE eip.classPromotion SET promoName=?,startDate=?,endDate=?,extendDate=?,enabled=?,updater=?,comment=?,update_time=CURDATE(),approve=? WHERE classPromotion_seq="+classPromotion_id, 
					classPromotion.getPromoName(),
					classPromotion.getStartDate(),
					classPromotion.getEndDate(),
					classPromotion.getExtendDate(),
					classPromotion.getEnabled(),
					classPromotion.getUpdater(),
					classPromotion.getComment(),
					approve
			);
			
			//delete eip.promoApply 適用分校
			jdbcTemplate.update("delete from eip.promoApply  where classPromotion_id=?;",
					classPromotion_id
			);
		}else {     
			jdbcTemplate.update("INSERT INTO eip.classPromotion VALUES (default,?,?,?,?,?,?,?,CURDATE(),?,?,?,?)",
					promoType,
					classPromotion.getPromoName(),
			    	classPromotion.getStartDate(),
			    	classPromotion.getEndDate(),
			    	classPromotion.getExtendDate(),
			    	classPromotion.getEnabled(),
			    	classPromotion.getUpdater(),
			    	1,
			    	classPromotion.getComment(),
			    	0,
			    	""
			);
			classPromotion_id_new  = Integer.toString(jdbcTemplate.queryForObject("select last_insert_id()", Integer.class));
			classPromotion_id = classPromotion_id_new;
		}
		
		//insert eip.promoApply 適用分校
		for(int i=0;i<A_school_code.length;i++) {
			jdbcTemplate.update("INSERT INTO eip.promoApply VALUES (default,?,?)",
					classPromotion_id,
					A_school_code[i]
			);
		}	
		return classPromotion_id_new; //只有新增回傳才有值
	}
	
	public Boolean PromoApproveSave(String classPromotion_id,String approve){
		jdbcTemplate.update("UPDATE eip.classPromotion SET approve=? WHERE classPromotion_seq="+classPromotion_id, 
				approve
		);		
		return true;
	}	
	
	public List<Lecture> getLecture(String lecture_id,String category_id,String teacher_id,String active,String lectureDate_like,String school_id){ 
		String sql = 
				 "select a.*,b.name as school_name, c.name as category_name,d.name as teacher_name"+
		         " from eip.lecture a left join school b on a.school_id = b.school_seq"+
				 ",category c,teacher d"+
				 " where a.category_id=c.category_seq and a.teacher_id=d.teacher_seq";
		
		if (lecture_id != null && !lecture_id.isEmpty()) {sql += " and a.lecture_seq = "+lecture_id;}
		if (category_id != null && !category_id.isEmpty()) {sql += " and a.category_id = "+category_id;}
		if (teacher_id != null && !teacher_id.isEmpty()) {sql += " and a.teacher_id = "+teacher_id;}
		if (active != null && !active.isEmpty()) {sql += " and a.active = "+active;} 
		if (lectureDate_like != null && !lectureDate_like.isEmpty()) {sql += " and a.lectureDate like '"+lectureDate_like+"'";}
		if (school_id != null && !school_id.isEmpty()) {sql += " and a.school_id = "+school_id;}
				
		List<Lecture> LLecture = jdbcTemplate.query(sql,(result,rowNum)->new Lecture(
	    		result.getString("lecture_seq"),
	    		result.getString("school_id"),
	    		result.getString("school_name"),
	    		result.getString("teacher_id"),
	    		result.getString("teacher_name"),	    		
	    		result.getString("location"),
	    		result.getString("lectureDate"),
	    		result.getString("lectureTimeFrom"),
	    		result.getString("lectureTimeTo"),
	    		result.getString("category_id"),
	    		result.getString("category_name"),
	    		result.getString("lectureName"),
	    		result.getString("charText"),
	    		result.getString("remark"),
	    		result.getString("creater"),
	    		result.getString("createTime"),
	    		result.getString("active")
        ));
		
	    return LLecture;
	}	
	
	
	public Boolean LectureEditSave(Lecture lecture,String lecture_seq) {
		if(lecture_seq!=null && !lecture_seq.isEmpty()) {
		        jdbcTemplate.update("UPDATE eip.lecture set school_id=?,teacher_id=?,location=?,lectureDate=?,lectureTimeFrom=?,lectureTimeTo=?,category_id=?,lectureName=?,charText=?,remark=?,creater=?,createTime=NOW() where lecture_seq=?",
		 	        lecture.getSchool_id(),
		 	        lecture.getTeacher_id(),
		 	    	lecture.getLocation(),
		 	    	lecture.getLectureDate(),
		 	    	lecture.getLectureTimeFrom(),
		 	    	lecture.getLectureTimeTo(),
		 	    	lecture.getCategory_id(),
		 	    	lecture.getLectureName(),
		 	    	lecture.getCharText(),
		 	    	lecture.getRemark(),
		 	    	lecture.getCreater(),
		 	    	lecture_seq
		 	    ); 			
		}else {	
			     jdbcTemplate.update("INSERT INTO eip.lecture VALUES (default,?,?,?,?,?,?,?,?,?,?,?,NOW(),?)",
			        lecture.getSchool_id(),
			        lecture.getTeacher_id(),
			    	lecture.getLocation(),
			    	lecture.getLectureDate(),
			    	lecture.getLectureTimeFrom(),
			    	lecture.getLectureTimeTo(),
			    	lecture.getCategory_id(),
			    	lecture.getLectureName(),
			    	lecture.getCharText(),
			    	lecture.getRemark(),
			    	lecture.getCreater(),
			    	1 //active
			      );
		}
	     
	     return true;
	} 
	
		
	public List<classTrialFlow> getClassTrialFlow(String grade_id){
		String sql = "SELECT * from eip.classTrialFlow where grade_id='"+grade_id+"'";

		List<classTrialFlow> LclassTrialFlow = jdbcTemplate.query(sql,(result, rowNum) -> new classTrialFlow(
						result.getString("classTrialFlow_seq"),
						result.getString("grade_id"), 
						result.getString("beclass"),
						result.getString("beclass_edit"),
						result.getString("beclass_time"),
						result.getString("art"),
						result.getString("art_edit"),
						result.getString("art_time"),
						result.getString("market"),
						result.getString("market_edit"),
						result.getString("market_time"),
						result.getString("website"),
						result.getString("website_edit"),
						result.getString("website_time"),
						result.getString("comment"),
						result.getString("beclass_comment"),
						result.getString("art_comment"),
						result.getString("market_comment"),
						result.getString("website_comment")
		));
		return LclassTrialFlow;
	}
	
	public List<LectureFlow> getLectureFlow(String lecture_id){
		String sql = "SELECT * from eip.lectureFlow where lecture_id='"+lecture_id+"'";

		List<LectureFlow> LLectureFlow = jdbcTemplate.query(sql,(result, rowNum) -> new LectureFlow(
						result.getString("lectureFlow_seq"),
						result.getString("lecture_id"), 
						result.getString("beclass"),
						result.getString("beclass_edit"),
						result.getString("beclass_time"),
						result.getString("art"),
						result.getString("art_edit"),
						result.getString("art_time"),
						result.getString("website"),
						result.getString("website_edit"),
						result.getString("website_time"),
						result.getString("comment"),
						result.getString("beclass_comment"),
						result.getString("art_comment"),
						result.getString("website_comment")    
					  						
		));
		return LLectureFlow;
	}
	
	public Boolean updateClassTrial(classTrialFlow cTF,String functions,String editor) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String nowDate = dateFormat.format(new Date());
		
		if(functions.equals("beclass")){
	        jdbcTemplate.update("UPDATE eip.classTrialFlow set beclass=?,beclass_edit=?,beclass_time=?,beclass_comment=? where classTrialFlow_seq =?",
	        		cTF.getBeclass(),
	        		editor,
		 	        nowDate,
		 	        cTF.getBeclass_comment(),
		 	        cTF.getClassTrialFlow_seq()
		 	); 				
		}else if(functions.equals("art")){
	        jdbcTemplate.update("UPDATE eip.classTrialFlow set art=?,art_edit=?,art_time=?,art_comment=?  where classTrialFlow_seq =?",
	        		cTF.getArt(),
	        		editor,
		 	        nowDate,
		 	        cTF.getArt_comment(),
		 	        cTF.getClassTrialFlow_seq()
		 	); 				
		}else if(functions.equals("market")){
	        jdbcTemplate.update("UPDATE eip.classTrialFlow set market=?,market_edit=?,market_time=?,market_comment=?  where classTrialFlow_seq =?",
	        		cTF.getMarket(),
	        		editor,
		 	        nowDate,
		 	        cTF.getMarket_comment(),
		 	        cTF.getClassTrialFlow_seq()
		 	); 				
		}else if(functions.equals("website")){
	        jdbcTemplate.update("UPDATE eip.classTrialFlow set website=?,website_edit=?,website_time=?,website_comment=? where classTrialFlow_seq =?",
	        		cTF.getWebsite(),
	        		editor,
		 	        nowDate,
		 	        cTF.getWebsite_comment(),
		 	        cTF.getClassTrialFlow_seq()
		 	); 				
		}else if(functions.equals("comment")){
	        jdbcTemplate.update("UPDATE eip.classTrialFlow set comment=? where classTrialFlow_seq =?",
	        		cTF.getComment(),
		 	        cTF.getClassTrialFlow_seq()
		 	); 				
		}
		return true;
	};
	
	public Boolean updateLectureFlow(LectureFlow LF,String functions,String editor) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String nowDate = dateFormat.format(new Date());
		
		if(functions.equals("beclass")){
	        jdbcTemplate.update("UPDATE eip.lectureFlow set beclass=?,beclass_edit=?,beclass_time=?,beclass_comment=? where lectureFlow_seq =?",
	        		LF.getBeclass(),
	        		editor,
		 	        nowDate,
		 	        LF.getBeclass_comment(),
		 	        LF.getLectureFlow_seq()
		 	); 				
		}else if(functions.equals("art")){
	        jdbcTemplate.update("UPDATE eip.lectureFlow set art=?,art_edit=?,art_time=?,art_comment=? where lectureFlow_seq =?",
	        		LF.getArt(),
	        		editor,
		 	        nowDate,
		 	        LF.getArt_comment(),
		 	        LF.getLectureFlow_seq()
		 	); 								
		}else if(functions.equals("website")){
	        jdbcTemplate.update("UPDATE eip.lectureFlow set website=?,website_edit=?,website_time=?,website_comment=? where lectureFlow_seq =?",
	        		LF.getWebsite(),
	        		editor,
		 	        nowDate,
		 	        LF.getWebsite_comment(),
		 	        LF.getLectureFlow_seq()
		 	); 				
		}else if(functions.equals("comment")){
	        jdbcTemplate.update("UPDATE eip.lectureFlow set comment=? where lectureFlow_seq =?",
	        		LF.getComment(),
	        		LF.getLectureFlow_seq()
		 	); 				
		}
		return true;
	};	
	
	public Boolean insertClassTrial(classTrialFlow cTF,String grade_id,String functions,String editor) {
		String beclass = "";
		String beclass_edit = "";
		String beclass_time = "";
		String art = "";
		String art_edit = "";
		String art_time = "";
		String market = "";
		String market_edit = "";
		String market_time = "";
		String website = "";
		String website_edit = "";
		String website_time = "";
		String comment = "";
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String nowDate = dateFormat.format(new Date());
		String beclass_comment = "";
		String art_comment = "";
		String market_comment = "";
		String website_comment = "";		
		
		if(functions.equals("beclass")){
			 beclass = cTF.getBeclass();
			 beclass_edit = editor;
			 beclass_time = nowDate;
			 beclass_comment = cTF.getBeclass_comment();
		}else if(functions.equals("art")){
			 art = cTF.getArt();
			 art_edit = editor;
			 art_time = nowDate;
			 art_comment = cTF.getArt_comment();
		}else if(functions.equals("market")){
			 market = cTF.getMarket();
			 market_edit = editor;
			 market_time = nowDate;	
			 market_comment = cTF.getArt_comment();
		}else if(functions.equals("website")){
			 website = cTF.getWebsite();
			 website_edit = editor;
			 website_time = nowDate;	
			 website_comment = cTF.getWebsite_comment();
		}else if(functions.equals("comment")){
			comment = cTF.getComment();
		}
		
	    jdbcTemplate.update("INSERT INTO eip.classTrialFlow VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
    		    grade_id,
    		    beclass,
    		    beclass_edit,
    		    beclass_time,
    		    art,
    		    art_edit,
    		    art_time,
    		    market,
    		    market_edit,
    		    market_time,
    		    website,
    		    website_edit,
    		    website_time,
    		    comment,
    			beclass_comment,
    			art_comment,
    		    market_comment,
    			website_comment   		    
		);			
		return true;
	};	
	
	public Boolean insertLectureFlow(LectureFlow LF,String lecture_id,String functions,String editor) {
		String beclass = "";
		String beclass_edit = "";
		String beclass_time = "";
		String art = "";
		String art_edit = "";
		String art_time = "";
		String website = "";
		String website_edit = "";
		String website_time = "";
		String comment = "";
		String beclass_comment = "";
		String art_comment = "";
		String website_comment = "";	
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String nowDate = dateFormat.format(new Date());
		
		if(functions.equals("beclass")){
			 beclass = LF.getBeclass();
			 beclass_edit = editor;
			 beclass_time = nowDate; 
			 beclass_comment = LF.getBeclass_comment();
		}else if(functions.equals("art")){
			 art = LF.getArt();
			 art_edit = editor;
			 art_time = nowDate;
			 art_comment = LF.getArt_comment();
		}else if(functions.equals("website")){
			 website = LF.getWebsite();
			 website_edit = editor;
			 website_time = nowDate;
			 website_comment = LF.getWebsite_comment();
		}else if(functions.equals("comment")){
			comment = LF.getComment();
		}
		
	    jdbcTemplate.update("INSERT INTO eip.lectureFlow VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
    		    lecture_id,
    		    beclass,
    		    beclass_edit,
    		    beclass_time,
    		    art,
    		    art_edit,
    		    art_time,
    		    website,
    		    website_edit,
    		    website_time,
    		    comment,
    			beclass_comment,
    			art_comment,
    			website_comment   		    
		);			
		return true;
	};
	
	public Boolean SetRanking(String classPromotion_seq,String ranking) {
		jdbcTemplate.update("UPDATE eip.classPromotion SET ranking=? WHERE classPromotion_seq="+classPromotion_seq, 
				ranking
		);		
		return true;
	}	
}
		