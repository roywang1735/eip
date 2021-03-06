package com.wordgod.eip.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.wordgod.eip.Model.*;



@Service
public class AdmService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	CourseSaleService courseSaleService;
	@Autowired
	CourseService courseService;
	@Autowired
	SalesService salesService;	

	public List<SignRecordHistory> getSignRecordHistory(String signRecordHistory_seq,String student_id,String grade_id,String active,String in_register_status,String attend_date,String Register_comboSale_grade_id,String in_attend,String class_style,String school_code,String class_th){ 
		String sql = "select a.*,b.class_date,b.class_seq,c.name as teacher_name from eip.signRecordHistory a,eip.classes b left join eip.teacher c on c.teacher_seq=b.teacher_id"+
				     " where a.grade_id=b.grade_id and a.class_th=b.class_th";
		if(signRecordHistory_seq!=null && !signRecordHistory_seq.isEmpty()) {sql+=" and a.signRecordHistory_seq = "+signRecordHistory_seq;}
		if(student_id!=null && !student_id.isEmpty()) {sql+=" and a.student_id = "+student_id;}
		if(grade_id!=null && !grade_id.isEmpty()) {sql+=" and a.grade_id = "+grade_id;}
		if(active!=null && !active.isEmpty()) {sql+=" and a.active = "+active;}		
		if(in_register_status!=null && !in_register_status.isEmpty()) {sql+=" and a.register_status in "+in_register_status;}
		if(attend_date!=null && !attend_date.isEmpty()) {sql+=" and a.attend_date = '"+attend_date+"'";}
		if(Register_comboSale_grade_id!=null && !Register_comboSale_grade_id.isEmpty()) {sql+=" and a.Register_comboSale_grade_id = '"+Register_comboSale_grade_id+"'";}
		if(in_attend!=null && !in_attend.isEmpty()) {sql+=" and a.attend in "+in_attend;}
		if(class_style!=null && !class_style.isEmpty()) {sql+=" and a.class_style = "+class_style;}	
		if(school_code!=null && !school_code.isEmpty()) {sql+=" and a.school_code = '"+school_code+"'";}
		if(class_th!=null && !class_th.isEmpty()) {sql+=" and a.class_th = "+class_th;}
		sql+=" order by a.class_th";

		List<SignRecordHistory> LSignRecordeHistory = jdbcTemplate.query(sql,(result,rowNum)->new SignRecordHistory(
				result.getString("signRecordHistory_seq"),
				result.getString("Register_comboSale_grade_id"),
				result.getString("register_status"),
				result.getString("student_id"),
				"",
				"",
				result.getString("class_style"),
				result.getString("school_code"),
				"",
	    		result.getString("grade_id"),
	    		result.getString("class_th"),
	    		result.getString("attend"),
	    		result.getString("active"),
	    		result.getString("attend_date"),
	    		result.getString("slot"),
	    		result.getString("update_time"),
	    		result.getString("updater"),
	    		"",
	    		"",
	    		result.getString("class_date"), //上課日期
	    		"",
	    		"",
	    		result.getString("makeUpNo"),
	    		result.getString("class_th_ex"),
	    		result.getString("teacher_name"),
	    		result.getString("class_seq"),
	    		result.getString("classroom"),
	    		result.getString("seat"),
	    		result.getString("comment"),
	    		result.getString("allowAttend"),
	    		"",
	    		result.getString("class_th_seq")

        ));
		return LSignRecordeHistory;
	}
	

	public Boolean UpdateSignRecord(String subject_abbr,String school_code,String class_th,String student_id,String attend,String takeHandout,String updater,String grade_id,String class_style,String attend_date,String student_no,String rows,String ch_name) {
		String attend_i = "0";
		if(attend.equals("Y") || attend.equals("y")) {
			attend_i = "1";
		}else if(attend.equals("N") || attend.equals("n")) {
			attend_i = "-1";
		}

		if(attend_i==null || attend_i.isEmpty() || student_id==null || student_id.isEmpty() || grade_id==null || grade_id.isEmpty() || class_th==null || class_th.isEmpty()) {
			return false;
		}else {
			int signRecordHistory_seq = -1;
			try {
				signRecordHistory_seq = jdbcTemplate.queryForObject("select signRecordHistory_seq from eip.signRecordHistory where class_style="+class_style+" and student_id="+student_id+" and grade_id="+grade_id+" and class_th="+class_th, Integer.class);
				jdbcTemplate.update("UPDATE eip.signRecordHistory set attend=?,updater=?,update_time=NOW(),attend_date=?,class_style=? where signRecordHistory_seq=?",
					attend_i,
					updater,
					attend_date,
					class_style,
					signRecordHistory_seq				
				);
			
			}catch(Exception e){
				//eip.signRecordHistory不存在
				System.out.println(rows+"? 點名單有誤(student_id)"+student_id+"(學號)"+student_no+ch_name+"(grade_id)"+grade_id+"(class_th)"+class_th);			
			}			
			
			if(takeHandout.equals("Y") || takeHandout.equals("y")) {				
				jdbcTemplate.update("INSERT INTO eip.signRecordMaterialHistory VALUES (default,?,?,NOW(),?);",
						signRecordHistory_seq,
						"-1",
						updater
				);			
			}
			return true;
		}	
	}
	
	public Boolean UpdateSignRecord2(String signRecordHistory_seq,String student_id,String[] A_classesMaterial_id,String updater,String changeToReal,String comment,String seat,String classRoom) {

		String seat_value = "";
		String classRoom_value = "";
		String comment_value = "";
		if(seat!=null && !seat.isEmpty()) {
			seat_value = seat;
		}
		if(classRoom!=null && !classRoom.isEmpty()) {
			classRoom_value = classRoom;
		}		
		if(comment!=null && !comment.isEmpty()) {
			comment_value = comment+"<br>";
		}
		
		String sql = "UPDATE eip.signRecordHistory set attend=?,updater=?,comment=CONCAT(comment,'"+comment_value+"'),update_time=NOW(),seat=?,classRoom=? where signRecordHistory_seq=? and student_id=?";
	
		//報名Video卻來上實體班
		if(changeToReal!=null && changeToReal.equals("1")) {
			java.util.Date now = new java.util.Date();
			String attend_date = new java.text.SimpleDateFormat("MM/dd/yyyy").format(now);
			sql="UPDATE eip.signRecordHistory set class_style=1,attend=?,updater=?,comment=CONCAT(comment,'"+comment_value+"'),update_time=NOW(),seat=?,classRoom=? where signRecordHistory_seq=? and student_id=?";
		}
		jdbcTemplate.update(sql,
				1,
				updater,
				seat_value,
				classRoom_value,
				signRecordHistory_seq,
				student_id
		);
		
		if(A_classesMaterial_id!=null) {
			for(int i=0;i<A_classesMaterial_id.length;i++) {
					jdbcTemplate.update("INSERT INTO eip.signRecordMaterialHistory VALUES (default,?,?,NOW(),?);",
							signRecordHistory_seq,
							A_classesMaterial_id[i],
							updater
					);					
			}
		}	
		return true;
	}

	public List<SignRecordHistory> getSignRecordHistory2(String school_code,String student_no,String class_th,String grade_id,String attend_date,String active,String in_register_status,String in_attend,String class_style,String student_id,String subject_id,String class_start_date_0,String attend_date2,String attend_date_notEmpty,String video_date,String allowAttend,String ch_name,String slot){
		String sql = "select a.* ,c.name as school_name,b.student_no,b.ch_name,e.class_start_date_0,d.name as subject_name,e.video_date,f.class_date,f.time_from,f.time_to,g.name as teacher_name"+
	                 " from eip.SignRecordHistory a,eip.student b,eip.school c,eip.subject d,eip.grade e,eip.classes f,eip.teacher g"+
				     " where e.teacher_id=g.teacher_seq and a.student_id=b.student_seq and c.code=a.school_code and a.grade_id=e.grade_seq and e.subject_id=d.subject_seq and a.grade_id=f.grade_id and a.class_th=f.class_th";

		if(school_code!=null && !school_code.isEmpty()) {sql+=" and a.school_code = '"+school_code+"'";}
		if(student_no!=null && !student_no.isEmpty()) {sql+=" and b.student_no = '"+student_no+"'";}
		if(student_id!=null && !student_id.isEmpty()) {sql+=" and b.student_seq = "+student_id;}
		if(class_th!=null && !class_th.isEmpty()) {sql+=" and a.class_th = "+class_th;}
		if(grade_id!=null && !grade_id.isEmpty()) {sql+=" and a.grade_id = "+grade_id;}
		if(allowAttend!=null && !allowAttend.isEmpty()) {sql+=" and a.allowAttend = "+allowAttend;}
		if(ch_name!=null && !ch_name.isEmpty()) {sql+=" and b.ch_name = '"+ch_name+"'";}
		if(slot!=null && !slot.isEmpty()) {sql+=" and a.slot = "+slot;}
		
		//ex. 2020/02/16
		if(attend_date!=null && !attend_date.isEmpty() && attend_date.length()==10) {
			attend_date = attend_date.substring(5,7)+"/"+attend_date.substring(8,10)+"/"+attend_date.substring(0,4);
			sql+=" and a.attend_date = '"+attend_date+"'";
		}
		//ex. 02/16/2020
		if(attend_date2!=null && !attend_date2.isEmpty()) {
			sql+=" and a.attend_date = '"+attend_date2+"'";
		}
		//video有預定日期
		if(attend_date_notEmpty!=null && attend_date_notEmpty.equals("1")) {
			sql+=" and a.attend_date != ''";
		}		
		if(active!=null && !active.isEmpty()) {sql+=" and a.active = "+active;}		
		if(in_register_status!=null && !in_register_status.isEmpty()) {sql+=" and a.register_status in "+in_register_status;}
		if(in_attend!=null && !in_attend.isEmpty()) {sql+=" and a.attend in "+in_attend;}
		if(class_style!=null && !class_style.isEmpty()) {sql+=" and a.class_style = "+class_style;}
		if(subject_id!=null && !subject_id.isEmpty()) {sql+=" and d.subject_seq = "+subject_id;}
		if(class_start_date_0!=null && !class_start_date_0.isEmpty()) {sql+=" and e.class_start_date_0 = '"+class_start_date_0+"'";}
		if(video_date!=null && !video_date.isEmpty()) {sql+=" and e.video_date = '"+video_date+"'";}
		sql+=" order by a.attend_date,a.grade_id desc LIMIT 200";
	
		
		List<SignRecordHistory> LSignRecordHistory = jdbcTemplate.query(sql,(result,rowNum)->new SignRecordHistory(
				result.getString("signRecordHistory_seq"),
				result.getString("Register_comboSale_grade_id"),
				result.getString("register_status"),
				result.getString("student_id"),
	    		result.getString("student_no"),
	    		result.getString("ch_name"),
	    		result.getString("class_style"),
	    		result.getString("school_code"),
				result.getString("school_name"),
				result.getString("grade_id"),
	    		result.getString("class_th"),
	    		result.getString("attend"),
	    		result.getString("active"),
	    		result.getString("attend_date"),
	    		result.getString("slot"),
				result.getString("update_time"),
				result.getString("updater"),
				result.getString("subject_name"),
				result.getString("class_start_date_0"),
				result.getString("class_date"),
				result.getString("time_from"),
				result.getString("time_to"),
				result.getString("makeUpNo"),
				result.getString("class_th_ex"),
				result.getString("teacher_name"),
				"",
				result.getString("classroom"),
				result.getString("seat"),
				result.getString("comment"),
				result.getString("allowAttend"),
				result.getString("video_date"),
				result.getString("class_th_seq")
        ));

		for(int i=0;i<LSignRecordHistory.size();i++) {
			if(LSignRecordHistory.get(i).getClass_start_date().length()==10) { //ex.07/24/2019				
				LSignRecordHistory.get(i).setClass_start_date(LSignRecordHistory.get(i).getClass_start_date().substring(8,10)+LSignRecordHistory.get(i).getClass_start_date().substring(0,2)+LSignRecordHistory.get(i).getClass_start_date().substring(3,5));
			}			
		}		
		return LSignRecordHistory;
	}
	
	public List<SignRecordHistory> getSignRecordHistory3(String Register_seq){ 
		String sql = "select a.* from eip.signRecordHistory a,eip.Register_comboSale b,eip.Register_comboSale_grade c"+
				     " where a.Register_comboSale_grade_id=c.Register_comboSale_grade_seq and c.Register_comboSale_id=b.Register_comboSale_seq and b.Register_id="+Register_seq;

		List<SignRecordHistory> LSignRecordeHistory = jdbcTemplate.query(sql,(result,rowNum)->new SignRecordHistory(
				result.getString("signRecordHistory_seq"),
				result.getString("Register_comboSale_grade_id"),
				result.getString("register_status"),
				result.getString("student_id"),
				"",
				"",
				result.getString("class_style"),
				result.getString("school_code"),
				"",
	    		result.getString("grade_id"),
	    		result.getString("class_th"),
	    		result.getString("attend"),
	    		result.getString("active"),
	    		result.getString("attend_date"),
	    		result.getString("slot"),
	    		result.getString("update_time"),
	    		result.getString("updater"),
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",
	    		result.getString("makeUpNo"),
	    		result.getString("class_th_ex"),
	    		"",
	    		"",
	    		result.getString("classroom"),
	    		result.getString("seat"),
	    		result.getString("comment"),
	    		result.getString("allowAttend"),
	    		"",
	    		result.getString("class_th_seq")
        ));
		return LSignRecordeHistory;
	}	
	
	public List<Suspension> getSuspension(String suspension_date){ 
		String sql = "select * from eip.Suspension"+
				     " where 1=1";
		if(suspension_date!=null && !suspension_date.isEmpty()) {sql+=" and suspension_date = '"+suspension_date.trim()+"'";}
		List<Suspension> LSuspension = jdbcTemplate.query(sql,(result,rowNum)->new Suspension(
				result.getString("suspension_seq"),
				result.getString("suspension_date"),
				result.getString("reason"),
				result.getString("update_time"),
				result.getString("updater")
        ));
		return LSuspension;
	}
	
	public List<Suspension_allow> getSuspensionAllow(String suspension_date){ 
		String sql = "select * from eip.suspension_allow"+
				     " where 1=1";
		if(suspension_date!=null && !suspension_date.isEmpty()) {sql+=" and suspension_date = '"+suspension_date+"'";}
		List<Suspension_allow> LSuspension_allow = jdbcTemplate.query(sql,(result,rowNum)->new Suspension_allow(
				result.getString("suspension_date"),
				result.getString("allow_slot")
        ));
		return LSuspension_allow;
	}	
	
	
	public Boolean SuspensionSave(String suspension_date,String reason,String updater,String[] A_allow_slot) {
		if(suspension_date!=null && !suspension_date.isEmpty()) {
			jdbcTemplate.update("DELETE from eip.suspension where suspension_date=?", suspension_date);
			jdbcTemplate.update("DELETE from eip.suspension_allow where suspension_date=?", suspension_date);
		}
		
		jdbcTemplate.update("INSERT INTO eip.suspension VALUES (default,?,?,CURDATE(),?);",
			suspension_date,
			reason,
			updater
		);	
		
		if(A_allow_slot!=null) {
				for(int i=0;i<A_allow_slot.length;i++) {
					jdbcTemplate.update("INSERT INTO eip.suspension_allow VALUES (?,?);",
							suspension_date,
							A_allow_slot[i]
					);				
				}
		}
		return true;
	}
	
	public Boolean UpdateRegisterStatus(String Register_comboSale_grade_seq,String Register_comboSale_seq,String register_status) {
         //1. eip.Register_comboSale_grade
		 jdbcTemplate.update("UPDATE eip.Register_comboSale_grade set register_status=? where Register_comboSale_grade_seq=?",
		    register_status,
		    Register_comboSale_grade_seq
	     );
		 
		 //2. eip.Register_comboSale.[gradeNoLeft] 
			 int gradeNoLeft  = jdbcTemplate.queryForObject("select gradeNoLeft from eip.Register_comboSale where Register_comboSale_seq="+Register_comboSale_seq, Integer.class);
			 
			 jdbcTemplate.update("UPDATE eip.Register_comboSale set gradeNoLeft="+(gradeNoLeft+1)+" where Register_comboSale_seq = ?",
				Register_comboSale_seq
			 ); 

		 //3. eip.signRecordHistory 
		 jdbcTemplate.update("UPDATE eip.signRecordHistory set register_status=? where Register_comboSale_grade_id=?",
				    register_status,
				    Register_comboSale_grade_seq
		 );	
		 
		 return true;
	}
	
	public Boolean SaveRegisterLog(String Register_comboSale_seq,String Register_comboSale_grade_seq,String register_status,String reason,String reason_option,String updater,String classCharge,String operationCharge,String payStyle_2_money,String payStyle_5_periods,String payStyle_5_code,String payStyle_5_money,String payStyle_7_code,String payStyle_7_money,String isUpdate) {

		jdbcTemplate.update("INSERT INTO eip.Register_log VALUES (default,?,?,?,?,?,NOW(),?,?,?,?,?,?,?,?,?,?);",
				Register_comboSale_seq,
				Register_comboSale_grade_seq,
				register_status,
				reason,
				reason_option,
				updater,
				classCharge,
				operationCharge,
				payStyle_2_money,
				payStyle_5_periods,
				payStyle_5_code,
				payStyle_5_money,
				payStyle_7_code,
				payStyle_7_money,
				isUpdate
		);		
		return true;		
	}
	
	public List<Register_log> getRegisterLog(String student_seq,String register_log_seq) {
		String sql = 
		"select a.*,d.name as subject_name,g.grade_id,e.class_start_date_0,f.name as school_name"+ 
		" from Register_log a,Register_comboSale b,subject d,grade e,school f,Register_comboSale_grade g,Register h"+
		" where"+ 
		" a.Register_comboSale_grade_seq=g.Register_comboSale_grade_seq"+
		" and e.grade_seq= g.grade_id"+ 
		" and g.Register_comboSale_id=b.Register_comboSale_seq"+ 
		" and b.subject_id=d.subject_seq"+
		" and f.code = e.school_code and e.grade_seq= g.grade_id"+
		" and b.Register_id=h.Register_seq";
		
		if(student_seq!=null && !student_seq.isEmpty()) {sql+=" and h.student_seq = "+student_seq;}
		if(register_log_seq!=null && !register_log_seq.isEmpty()) {sql+=" and a.register_log_seq = "+register_log_seq;}
		
		sql+=" order by update_time desc";
		
		List<Register_log> LRegister_log = jdbcTemplate.query(sql,(result,rowNum)->new Register_log(
			result.getString("register_log_seq"),	
			result.getString("register_comboSale_id"),
			result.getString("Register_comboSale_grade_seq"),
			result.getString("register_status"),
			result.getString("reason"),
			result.getString("reason_option"),
			result.getString("update_time"),
			result.getString("updater"),
			result.getString("subject_name"),
			result.getString("school_name"),
			result.getString("class_start_date_0"),
			result.getString("classCharge"),
			result.getString("operationCharge"),
			result.getString("payStyle_2_money"),
			result.getString("payStyle_5_periods"),
			result.getString("payStyle_5_code"),
			result.getString("payStyle_5_money"),
			result.getString("payStyle_7_code"),
			result.getString("payStyle_7_money"),
			result.getString("isUpdate")
		));
		
		for(int i=0;i<LRegister_log.size();i++) {
			if(LRegister_log.get(i).getClass_start_date().length()==10) { //ex.07/24/2019				
				LRegister_log.get(i).setClass_start_date(LRegister_log.get(i).getClass_start_date().substring(8,10)+LRegister_log.get(i).getClass_start_date().substring(0,2)+LRegister_log.get(i).getClass_start_date().substring(3,5));
			}			
		}		
		return LRegister_log;
	}

	public Boolean updateRegisterLog(String register_log_seq,String isUpdate) {
		  jdbcTemplate.update("UPDATE eip.Register_log set isUpdate=? where register_log_seq=?",
				  isUpdate,
				  register_log_seq		  
		  );		
		return true;
	}
	
	public List<mockSet> getMockSet(String category_id,String testMethod,String school_id){ 
		String sql = "select * from eip.mockSet"+
				     " where 1=1";
		if(category_id!=null && !category_id.isEmpty()) {sql+=" and category_id = "+category_id;}
		if(testMethod!=null && !testMethod.isEmpty()) {sql+=" and testMethod = "+testMethod;}
		if(school_id!=null && !school_id.isEmpty()) {sql+=" and school_id = "+school_id;}
		sql+=" order by slot";

		List<mockSet> LMockSet = jdbcTemplate.query(sql,(result,rowNum)->new mockSet(
				result.getString("mockSet_seq"),
				result.getString("category_id"),
				result.getString("school_id"),
				result.getString("testMethod"),
				result.getString("noName"),
				result.getString("slot"),
				"",
				result.getString("date_testMethod")
        ));
		for(int i=0;i<LMockSet.size();i++) {
			if(LMockSet.get(i).getSlot().equals("1")) {
				LMockSet.get(i).setSlotName("早");
			}else if(LMockSet.get(i).getSlot().equals("2")) {
				LMockSet.get(i).setSlotName("午");
			}else if(LMockSet.get(i).getSlot().equals("3")) {
				LMockSet.get(i).setSlotName("晚");
			}
		}
		return LMockSet;
	}
	
	public String signRecordSave(String grade_id,String class_th,String student_no,String updater){
		List<SignRecordHistory> LSignRecordHistory = getSignRecordHistory2("",student_no,class_th,grade_id,"","1","(1)","(0,1,-1)","","","","","","","","","","");
		if(LSignRecordHistory.size()==0) {
			return "不存在的點名紀錄, 請檢視[期別][學生][堂數]是否正確?";
		}else {
		  jdbcTemplate.update("UPDATE eip.signRecordHistory set attend=1,update_time=NOW(),updater=? where grade_id=? and class_th=? and student_id=?",
				  updater,
				  grade_id,
				  class_th,
				  LSignRecordHistory.get(0).getStudent_id()				  
		  );		
		  return "";
		}  
	}
	
	public String getRecordListStr(String grade_id,String student_seq_1,String class_style,String school_code) {
    	String recordListStr = "";
    	String student_seq = "";
    	//該班特定學生
    	if(student_seq_1!=null) {student_seq=student_seq_1;}    	
    	//取得訂該班特定or所有學生  已訂班的紀錄: register_status TINYINT, #訂班狀態 1:訂班,2:取消,3:保留
    	List<Student> LStudent = salesService.getGradeStudent("","","","","","",student_seq,grade_id,class_style,school_code,"(1,2,3)");
    	String recordTitle = "<div class='td2' style='width:130px;text-align:left;padding:0px'><div align='right'>課堂</div><div align='left'>學生</div></div>";
    	List<Classes> LClasses = courseService.getClasses(grade_id,"","","","","","","","","");
    	for(int i=0;i<LClasses.size();i++) {    		
			String classDate = "";
			if(LClasses.get(i).getClass_date()!=null && LClasses.get(i).getClass_date().length()>5) {
				classDate = LClasses.get(i).getClass_date().substring(0,5);
			}
			if(Integer.valueOf(LClasses.get(i).getClass_th())<0) {
				recordTitle +="<div class='td2' style='width:65px;text-align:center;vertical-align:bottom;font-size:small;color:#dddddd'>"+classDate+"<br>停課</div>";
			}else {
				recordTitle +="<div class='td2' style='width:65px;text-align:center;vertical-align:bottom;font-size:small'>"+classDate+"<br>第 "+LClasses.get(i).getClass_th()+" 堂</div>";
			}	
		}
    	List<String> recordList = new ArrayList<>();
    	String tmp = null;
    	String checkStr = null;
    	String materialStr = null;
    	for(int i=0;i<LStudent.size();i++) {
    		//學生是否繳清
    		String orderStatus = "";
    		Register register = salesService.getRegister2(LStudent.get(i).getStudent_seq(),grade_id).get(0);
    		if(register.getOrderStatus().equals("2")) {
    			//已結清
    		}else if(register.getOrderStatus().equals("1")) {
    			orderStatus="<font color='red'>(未結清)</font>";
    		}else if(register.getOrderStatus().equals("-1")) {
    			orderStatus="<font color='red'>(政龍資料?)</font>";
    		}

    		tmp ="<div class='td2' style='text-align:center;border-bottom:1px solid #ffffff;background-color:lightyellow'>"+
    				"<span style='font-weight:bold;font-size:small'>"+LStudent.get(i).getCh_name()+"</span>"+orderStatus+"<br>"+
    				"<span style='color:#999999;font-size:x-small'>"+LStudent.get(i).getStudent_no()+"</span>"+
    			 "</div>";
    		//取出該學生已訂班的紀錄: register_status TINYINT, #訂班狀態 1:訂班,2:取消,3:保留
    		List<SignRecordHistory> LSignRecordHistory = getSignRecordHistory("",LStudent.get(i).getStudent_seq(),grade_id,"1","(1,2,3)","","","(0,1,-1)",class_style,school_code,"");
    		
    		//點名紀錄
    		Boolean existClass; //點名紀錄課堂是否存在原期別的課堂
    		for(int z=0;z<LClasses.size();z++) {
    			existClass = false;
		       	String register_statusStr = "";    			
    			for(int j=0;j<LSignRecordHistory.size();j++) {
    			  if(!LSignRecordHistory.get(j).getClass_th().equals("-1")) {	
    				//是否取消或保留
			       	if(LSignRecordHistory.get(j).getRegister_status().equals("2")) {
			       		register_statusStr = "<span style='font-size:x-small;color:red'><img src='/images/cancel.jpg' height='10px'/>取 消</span><br>"; 
			       	}else if(LSignRecordHistory.get(j).getRegister_status().equals("3")) {
			       		register_statusStr = "<span style='font-size:x-small;color:red'><img src='/images/cancel.jpg' height='10px'/>保 留</span><br>"; 
			       	}
			       	
	    			if(LClasses.get(z).getClass_th().equals(LSignRecordHistory.get(j).getClass_th())) {
	    				existClass = true;
		    			  			
		    			checkStr = "<img src='/images/WhiteSquare.png' height='12px'/>";
		    			materialStr = "";
	
						List<SignRecordMaterialHistory> LSignRecordMaterialHistory = courseService.getSignRecordMaterialHistory(LSignRecordHistory.get(j).getSignRecordHistory_seq());
	
						for(int a=0;a<LSignRecordMaterialHistory.size();a++) {
							if(LSignRecordMaterialHistory.get(a).getMaterial_id().equals("1")) {
								materialStr +="<div style='font-size:xx-small'>"+LSignRecordMaterialHistory.get(a).getBook_name()+"</div>";
							}else if(LSignRecordMaterialHistory.get(a).getMaterial_id().equals("2")) {
								materialStr +="<div style='font-size:xx-small'>"+LSignRecordMaterialHistory.get(a).getInputTex()+"</div>";
							}
						}
							
				       	if(LSignRecordHistory.get(j).getAllowAttend().equals("1")){
			        		if(LSignRecordHistory.get(j).getAttend().equals("1")) { //出席
			        			if(LSignRecordHistory.get(j).getClass_style().equals("2")) { //video
			        				checkStr = "<img src='/images/Green_repeat.png' height='12px'/>";
			        			}else {
			        				checkStr = "<img src='/images/GreenSquare.png' height='12px'/>";
			        			}
			        		}else if(LSignRecordHistory.get(j).getAttend().equals("-1")) { //缺席
			        			if(LSignRecordHistory.get(j).getClass_style().equals("2")) { //video
			        				checkStr = "<img src='/images/Red_repeat.png' height='12px'/>";
			        			}else {
			        				checkStr = "<img src='/images/RedSquare.png' height='12px'/>";
			        			}
			        		}				       		
				       	}else {
				       		checkStr = "<span style='color:black'>&#10008;</span>";
				       	}
		    			tmp +="<div class='td2' style='text-align:center;width:60px;text-align:left;border-bottom:1px solid #ffffff;vertical-align:middle;background-color:#eeffee'>"+register_statusStr+"<A href='javascript:changeAttend("+LSignRecordHistory.get(j).getSignRecordHistory_seq()+","+LSignRecordHistory.get(j).getAttend()+")'>"+checkStr+"</A>"+materialStr+"</div>";
	    			}
    			  }	
	    		}
    			if(!existClass) {
    				tmp +="<div class='td2' style='text-align:center;width:60px;background-color:#eeffee;vertical-align:middle;font-size:x-small'>"+register_statusStr+"未訂</div>";	    			
    			}
    		}		    		
    		recordList.add(tmp);  		
    	}
    	
    		recordListStr +=
    				"<div class='css-table' style='border-spacing:1px'>"+
    						"<div class='tr' style='background-color:white;width:400px;border-radius:10px'>&nbsp;<img src='/images/GreenSquare.png' height='9px'/>到課&nbsp;<img src='/images/RedSquare.png' height='9px'/>缺課&nbsp;&#10008;不可進班</div>"+
    				"</div>"+
    				"<div class='css-table' style='border-spacing:1px'>"+
    						"<div class='tr' style='background-color:steelblue;color:white;font-size:small'>"+recordTitle+"</div>";
    		for(int i=0;i<recordList.size();i++) {
    		    recordListStr += "<div class='tr' style='font-size:small;background-color:white'>"+recordList.get(i)+"</div>";
    		}
    		recordListStr += "</div>";
    		
    		return recordListStr;
	}
	
	public List<Books> getBooks(String category_id,String books_seq,String active){ 
		String sql = "select a.*,b.name as category_name from books a, category b where a.category_id=b.category_seq";
		if(category_id!=null && !category_id.isEmpty()) {sql+=" and a.category_id = "+category_id;}
		if(books_seq!=null && !books_seq.isEmpty()) {sql+=" and a.books_seq = "+books_seq;}
		if(active!=null && !active.isEmpty()) {sql+=" and a.active = "+active;}
		sql +=  " and a.del_item !=1";
		List<Books> LBooks = jdbcTemplate.query(sql,(result,rowNum)->new Books(
	    		result.getString("books_seq"),
	    		result.getString("category_id"),
	    		result.getString("category_name"),
	    		result.getString("bookName"),
	    		result.getString("originPrice"),
	    		result.getString("sellPrice"),
	    		result.getString("isbn"),
	    		result.getString("publisher"),
	    		result.getString("active"),
	    		result.getString("updater"),
	    		result.getString("update_time"),
	    		"1"
        ));
		return LBooks;
	}

	public List<StudentOrderStatus> studentOrderStatus(String student_seq,String grade_id){ 
		String sql = "select d.name as orderStatus,c.allow_attend from eip.Register a,eip.Register_comboSale b,eip.Register_comboSale_grade c,eip.OrderStatus d"+
				     " where d.code = a.orderStatus and a.student_seq="+student_seq+" and a.Register_seq=b.Register_id and b.Register_comboSale_seq = c.Register_comboSale_id and c.grade_id="+grade_id+" limit 1";
	
		List<StudentOrderStatus> LStudentOrderStatus = jdbcTemplate.query(sql,(result,rowNum)->new StudentOrderStatus(
	    		result.getString("orderStatus"),
	    		result.getString("allow_attend")
            ));
		
		return LStudentOrderStatus;
	}	

	public Boolean bookSettingEditSave(Books book,String updater) {
		if(book.getBooks_seq()!=null && !book.getBooks_seq().isEmpty()) {
	    	String sql = "UPDATE eip.books set category_id=?,bookName=?,originPrice=?,sellPrice=?,isbn=?,publisher=?,active=?,updater=?,update_time=NOW(),reason=? where books_seq=?";
				jdbcTemplate.update(sql,
						book.getCategory_id(),
						book.getBookName(),
						book.getOriginPrice(),
						book.getSellPrice(),
						book.getIsbn(),
						book.getPublisher(),
						book.getActive(),
						updater,
						book.getBooks_seq()
				);			
		}else{
			jdbcTemplate.update("INSERT INTO eip.books VALUES (default,?,?,?,?,?,?,?,?,?,NOW(),payOffRelease);",
					book.getCategory_id(),
					book.getBookName(),
					book.getOriginPrice(),
					book.getSellPrice(),
					book.getIsbn(),
					book.getPublisher(),
					book.getActive(),
					0,
					updater,
					1
			);
		}	
		return true;		
	}

	public List<OtherSell> getOtherSell(String cat1){ 
		List<OtherSell> LOtherSell = null;
		if(cat1.equals("1")) { //書籍
			String sql = "select a.*,b.bookName as sell_name from eip.otherSell a, eip.books b where a.sell_id=b.books_seq and cat1='1'";
		    LOtherSell = jdbcTemplate.query(sql,(result,rowNum)->new OtherSell(
	    		result.getString("otherSell_seq"),
	    		result.getString("cat1"),
	    		result.getString("sell_id"),
	    		result.getString("sell_name"),
	    		result.getString("amount"),
	    		result.getString("sellPrice"),
	    		result.getString("studentNO"),
	    		result.getString("studentName"),
	    		result.getString("creater"),
	    		result.getString("createTime"),
	    		result.getString("comment")
            ));
		}else if(cat1.equals("2")) { //Video點數
			String sql = "select * from eip.otherSell where cat1='2'";
		    LOtherSell = jdbcTemplate.query(sql,(result,rowNum)->new OtherSell(
	    		result.getString("otherSell_seq"),
	    		result.getString("cat1"),
	    		result.getString("sell_id"),
	    		"Video點數",
	    		result.getString("amount"),
	    		result.getString("sellPrice"),
	    		result.getString("studentNO"),
	    		result.getString("studentName"),
	    		result.getString("creater"),
	    		result.getString("createTime"),
	    		result.getString("comment")
            ));
		}    
		return LOtherSell;
	}


	public Boolean bookSellEditSave(OtherSell otherSell) {
		jdbcTemplate.update("INSERT INTO eip.otherSell VALUES (default,?,?,?,?,?,?,?,NOW(),?);",
				otherSell.getCat1(),
				otherSell.getSell_id(),
				otherSell.getAmount(),
				otherSell.getSellPrice(),
				otherSell.getStudentNo(),
				otherSell.getStudentName(),
				otherSell.getCreater(),
				otherSell.getComment()
		);		
		return true;		
	}
	
	public Boolean manualSignRecord(String signRecordHistory_seq,String attend_ori,String allowAttend,String attend,String[] A_classesMaterial_seq,String updater,String classroom,String seat,String comment){
		    String sql = "";
		    if(allowAttend!=null && !allowAttend.isEmpty()) {
		    	//sal,sal_mgr
		    	sql = "UPDATE eip.signRecordHistory set allowAttend=? where signRecordHistory_seq=?";
					jdbcTemplate.update(sql,
							  allowAttend,
							  signRecordHistory_seq			  
						);
		    }
		    if(attend!=null && !attend.isEmpty()) {
		    	//adm,adm_mgr
		    	sql = "UPDATE eip.signRecordHistory set attend=?,classroom=?,seat=?,comment=? where signRecordHistory_seq=?";		    
					jdbcTemplate.update(sql,
						  attend,
						  classroom,
						  seat,
						  comment,
						  signRecordHistory_seq			  
					);
		    }		
	  
		  if(A_classesMaterial_seq!=null) {
			  for(int i=0;i<A_classesMaterial_seq.length;i++) {
				    jdbcTemplate.update("DELETE from eip.signRecordMaterialHistory where signRecordHistory_id=?", signRecordHistory_seq);
					if(!A_classesMaterial_seq[i].equals("-1")) {
					    jdbcTemplate.update("INSERT INTO eip.signRecordMaterialHistory VALUES (default,?,?,NOW(),?);",
								signRecordHistory_seq,
								A_classesMaterial_seq[i],
								updater
						);
					}    
			  }
		  }	  
	  
	  return true;
	}  
	
	public Boolean addVideoRecord(SignRecordHistory signRecordHistory,String school_code) {
		  jdbcTemplate.update("INSERT INTO eip.signRecordHistory VALUES (default,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?,?,?,?,?);",
				signRecordHistory.getRegister_comboSale_grade_id(),
				1, //register_status
				signRecordHistory.getStudent_id(),
				2, //class_style
				school_code,
				signRecordHistory.getGrade_id(),
				signRecordHistory.getClass_th(),
				0,//attend
				1,//active
				"",//attend_date
				-99,//slot
				"System",//updater
				1,//makeUpNo
				"",//class_th_ex
				"",
				"",
				"",
				1,
				signRecordHistory.getClass_th()
		  );		

		  jdbcTemplate.update("UPDATE eip.signRecordHistory set active=? where signRecordHistory_seq=?",
				  0,
				  signRecordHistory.getSignRecordHistory_seq()			  
		  );		
	  return true;
	}
	
	public List<AdmRegularJob> AdmRegularJob(String school_code){ 
		String sql = "select * from eip.admRegularJob where school_code='"+school_code+"'";	
		List<AdmRegularJob> LAdmRegularJob = jdbcTemplate.query(sql,(result,rowNum)->new AdmRegularJob(
	    		result.getString("admRegularJob_seq"),
	    		result.getString("school_code"),
	    		result.getString("jobContent"),
	    		result.getString("editerId"),
	    		result.getString("editerTime")
        ));
		
		return LAdmRegularJob;
	}
	
	public Boolean admJobUpdateEdit(String yearMD,String school_code,String assigner,String[] jobContent1,String[] jobContent2,String[] worker1,String[] worker2,ArrayList<String> LjobCode,ArrayList<String> LworkerId,List<AdmGradeJob> LAdmGradeJob,String[] grade_id,String[] class_th) {
		    jdbcTemplate.update("delete from eip.admJob where school_code=? and yearMD=?",school_code,yearMD);
			jdbcTemplate.update("INSERT INTO eip.admJob VALUES (default,?,?,?,NOW());",
					school_code,
					yearMD,
					assigner
			);
			
			jdbcTemplate.update("delete from eip.jobContent where school_code=? and yearMD=? and category=1",school_code,yearMD);

			if(jobContent1!=null) {
				for(int i=0;i<jobContent1.length;i++) {
					jdbcTemplate.update("INSERT INTO eip.jobContent VALUES (default,?,?,?,?,?,?,?);",
							school_code,
							yearMD,
							jobContent1[i],
							worker1[i],
							"",
							"", //完成人
							"1"
					);
				}
			}	
				
			jdbcTemplate.update("delete from eip.jobContent where school_code=? and yearMD=? and category=2",school_code,yearMD);
			if(jobContent2!=null) {
				for(int i=0;i<jobContent2.length;i++) {					
					jdbcTemplate.update("INSERT INTO eip.jobContent VALUES (default,?,?,?,?,?,?,?);",
							school_code,
							yearMD,
							jobContent2[i],
							worker2[i],
							"",
							"", //完成人
							"2"
					);
				}
			}
			
			if(LjobCode.size()>0) {
				jdbcTemplate.update("delete from eip.jobContentCode where school_code=? and yearMD=?",school_code,yearMD);
				for(int i=0;i<LjobCode.size();i++) {
					jdbcTemplate.update("INSERT INTO eip.jobContentCode VALUES (default,?,?,?,?,?,?);",
							school_code,
							yearMD,
							LjobCode.get(i),
							LworkerId.get(i),
							"",
							0
					);
				}				
			}
			
		
			for(int i=0;i<grade_id.length;i++) {
				jdbcTemplate.update("delete from eip.admGradeJob where grade_id='"+grade_id[i]+"' and class_th='"+class_th[i]+"'");
			}
			
			for(int i=0;i<LAdmGradeJob.size();i++) {
				jdbcTemplate.update("INSERT INTO eip.admGradeJob VALUES (default,?,?,?,?,?,?,?,NOW(),?);",
						LAdmGradeJob.get(i).getGrade_id(),
						LAdmGradeJob.get(i).getClass_th(),
						LAdmGradeJob.get(i).getB1_jobContent(),
						LAdmGradeJob.get(i).getB2_jobContent(),
						LAdmGradeJob.get(i).getB3_jobContent(),
						LAdmGradeJob.get(i).getB4_jobContent(),
						LAdmGradeJob.get(i).getEditerId(),
						""
				);
			}			
					
		return true;
	}

	public Boolean admJobUpdate(String editor,String[] checkFinish1,String[] checkFinish2) {
		if(checkFinish1!=null) {
			for(int i=0;i<checkFinish1.length;i++) {
				jdbcTemplate.update("update eip.jobContent set finishName=? where jobContent_seq=?;",
						editor,
						checkFinish1[i]
				);			
			}
		}
		
		if(checkFinish2!=null) {
			for(int i=0;i<checkFinish2.length;i++) {
				jdbcTemplate.update("update eip.jobContent set finishName=? where jobContent_seq=?;",
						editor,
						checkFinish2[i]
				);			
			}
		}	
		
		return true;
	}
	
	public Boolean admGradeJobUpdate(String editor,String[] checkjob) {
		if(checkjob!=null) {
			for(int i=0;i<checkjob.length;i++) {
				jdbcTemplate.update("update eip.admGradeJob set finishName=? where admGradeJob_seq=?;",
						editor,
						checkjob[i]
				);			
			}
		}
		return true;
	}
	
	public List<JobContentCode> GetJobContentCode(String school_code,String yearMD){ 
		String sql = "select * from eip.jobContentCode where school_code='"+school_code+"' and yearMD='"+yearMD+"' order by jobContentCode";	
		List<JobContentCode> LJobContentCode = jdbcTemplate.query(sql,(result,rowNum)->new JobContentCode(
	    		result.getString("jobContentCode_seq"),
	    		result.getString("school_code"),
	    		result.getString("yearMD"),
	    		result.getString("jobContentCode"),
	    		result.getString("workerId"),
	    		result.getString("workTime"),
	    		result.getString("finished")
        ));		
		return LJobContentCode;
	}
	
	public List<JobContent> getJobContent(String school_code,String yearMD){ 
		String sql = "select * from eip.jobContent where school_code='"+school_code+"' and yearMD='"+yearMD+"'";	
		List<JobContent> LJobContent = jdbcTemplate.query(sql,(result,rowNum)->new JobContent(
	    		result.getString("jobContent_seq"),
	    		result.getString("school_code"),
	    		result.getString("yearMD"),
	    		result.getString("jobContent"),
	    		result.getString("workerId"),
	    		result.getString("workTime"),
	    		result.getString("finishName"),
	    		result.getString("category")
        ));
		
		return LJobContent;
	}
	
	public Boolean admRegularJobUpdate(String school_code,String[] jobContent,String editerId) {
		if(school_code!=null && !school_code.isEmpty()) {
		    jdbcTemplate.update("delete from eip.admRegularJob where school_code=?",school_code);
		    for(int i=0;i<jobContent.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.admRegularJob VALUES (default,?,?,?,NOW());",
						school_code,
						jobContent[i],
						editerId
				);
		    }	
		}	
		return true;
	}
	
	public List<AdmGradeJob> getAdmGradeJob(String grade_id,String class_th){ 
		String sql = "select * from eip.admGradeJob where grade_id='"+grade_id+"' and class_th='"+class_th+"'";	
		List<AdmGradeJob> LAdmGradeJob = jdbcTemplate.query(sql,(result,rowNum)->new AdmGradeJob(
	    		result.getString("admGradeJob_seq"),
	    		result.getString("grade_id"),
	    		result.getString("class_th"),
	    		result.getString("b1_jobContent"),
	    		result.getString("b2_jobContent"),
	    		result.getString("b3_jobContent"),
	    		result.getString("b4_jobContent"),
	    		result.getString("editerName"),
	    		result.getString("editerTime"),
	    		result.getString("finishName")
        ));
	
		return LAdmGradeJob;
	}
	
	public Boolean ClassRoomBookUpdate(String school_code,String bookDate,String slot,String bookClassRoom,String bookContent,String assigner) {
		jdbcTemplate.update("delete from eip.classRoomBook where school_code=? and bookDate=? and slot=? and bookClassRoom=?",school_code,bookDate,slot,bookClassRoom);
		jdbcTemplate.update("INSERT INTO eip.classRoomBook VALUES (default,?,?,?,?,?,?,NOW());",
				school_code,
				bookDate,
				slot,
				bookClassRoom,
				bookContent,
				assigner
		);
		return true;
	}
	
	public List<ClassRoomBook> getClassRoomBook(String school_code){ 
		String sql = "select * from eip.classRoomBook where school_code='"+school_code+"'";	
		List<ClassRoomBook> LClassRoomBook = jdbcTemplate.query(sql,(result,rowNum)->new ClassRoomBook(
	    		result.getString("classRoomBook_seq"),
	    		result.getString("school_code"),
	    		result.getString("bookDate"),
	    		result.getString("slot"),
	    		result.getString("bookClassRoom"),
	    		result.getString("bookContent"),
	    		result.getString("assigner"),
	    		result.getString("assignerTime")
        ));
		
		return LClassRoomBook;
	}
	
	public Boolean VideoBaseSave(String school_code,String year,String month,String[] A_flag,String[] A_slot,String updater) {
        //先砍掉舊的
        if(year!=null && !year.isEmpty() && month!=null && !month.isEmpty()) {
        	jdbcTemplate.update("delete from eip.videoBase where setDate like '"+year+"-"+month+"%'");
        }
        
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR,Integer.valueOf(year)); 
		c.set(Calendar.MONTH,Integer.valueOf(month)-1);
		
		int monthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);		
		for(int i=1;i<=c.getActualMaximum(Calendar.DAY_OF_MONTH);i++) {
			c.set(Calendar.DAY_OF_MONTH,i);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String setDate = sdf.format(c.getTime());
			String dayOfWeek = "";
	        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
	        	dayOfWeek = "7";
	        } else if ((c.get(Calendar.DAY_OF_WEEK)) == 2) {
	        	dayOfWeek = "1";
	        } else if (c.get(Calendar.DAY_OF_WEEK) == 3) {
	        	dayOfWeek = "2";
	        } else if (c.get(Calendar.DAY_OF_WEEK) == 4) {
	        	dayOfWeek = "3";
	        } else if (c.get(Calendar.DAY_OF_WEEK) == 5) {
	        	dayOfWeek = "4";
	        } else if (c.get(Calendar.DAY_OF_WEEK) == 6) {
	        	dayOfWeek = "5";
	        } else if (c.get(Calendar.DAY_OF_WEEK) == 7) {
	        	dayOfWeek = "6";
	        }
	
	        for(int x=0;x<A_flag.length;x++) {
	        	if(A_flag[x].substring(0,1).equals(dayOfWeek)) {
					jdbcTemplate.update("INSERT INTO eip.videoBase VALUES (default,?,?,?,?,NOW(),?);",
							school_code,
							setDate,							
							A_slot[x],
							updater,
							dayOfWeek//weekDay
					);		        		
	        	}
	        }		
		}	
		return true;
	}

/**	
	public List<MockBase> getSuitMockBase(String school_code,String someDay,int weekDay){ 
		String sql = "select * from eip.mockBase where startDate=(select max(startDate) from eip.mockBase where '"+someDay+"' >= startDate and school_code='"+school_code+"' and weekDay="+weekDay+")";
		List<MockBase> LMockBase = jdbcTemplate.query(sql,(result,rowNum)->new MockBase(
	    		result.getString("mockBase_seq"),
	    		result.getString("school_code"),
	    		result.getString("startDate"),
	    		result.getString("weekDay"),
	    		result.getString("slot"),
	    		result.getString("timeFrom"),
	    		result.getString("timeTo"),
	    		result.getString("updater"),
	    		result.getString("update_time")
        ));		
		return LMockBase;
	}
**/	


	public List<VideoBase> getVideoBaseFirstDay(String school_code){ 
		String sql = "select distinct(setDate) from eip.videoBase where school_code='"+school_code+"' and setDate like '%-01'";
		
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM");
		String like1 = ft.format(date);
		cal.add(Calendar.MONTH,1);
		Date date2 = cal.getTime();
		String like2 = ft.format(date2);
		cal.add(Calendar.MONTH,1);
		date = cal.getTime();
		String like3 = ft.format(date);
		sql += " and (setDate like '"+like1+"%' or setDate like '"+like2+"%' or setDate like '"+like3+"%')";
		
		List<VideoBase> LVideoBase = jdbcTemplate.query(sql,(result,rowNum)->new VideoBase(
	    		"",
	    		"",
	    		result.getString("setDate"),
	    		"",
	    		"",
	    		"",
	    		"",
	    		""
        ));	
		return LVideoBase;
	}

	public List<VideoBase> getVideoBase(String setDate,String slot,String school_code,String weekDay){ 
		String sql = "select * from eip.videoBase where setDate='"+setDate+"'";
		if(slot!=null && !slot.isEmpty()) {sql+=" and slot = "+slot;}
		if(school_code!=null && !school_code.isEmpty()) {sql+=" and school_code = '"+school_code+"'";}
		if(weekDay!=null && !weekDay.isEmpty()) {sql+=" and weekDay = '"+weekDay+"'";}

		List<VideoBase> LVideoBase = jdbcTemplate.query(sql,(result,rowNum)->new VideoBase(
	    		result.getString("videoBase_seq"),
	    		result.getString("school_code"),
	    		result.getString("setDate"),
	    		result.getString("slot"),
	    		"",
	    		result.getString("updater"),
	    		result.getString("update_time"),
	    		result.getString("weekDay")
        ));
		for(int i=0;i<LVideoBase.size();i++) {
			if(LVideoBase.get(i).getSlot().equals("1")) {
				LVideoBase.get(i).setSlotName("早");
			}else if(LVideoBase.get(i).getSlot().equals("2")) {
				LVideoBase.get(i).setSlotName("午");
			}else if(LVideoBase.get(i).getSlot().equals("3")) {
				LVideoBase.get(i).setSlotName("晚");
			}
		}		
		return LVideoBase;
	}

	public List<VideoBase> getVideoBaseDistinctDay(String school_code){
		String sql = "select distinct(setDate) from eip.videoBase where school_code='"+school_code+"'";
		List<VideoBase> LVideoBase = jdbcTemplate.query(sql,(result,rowNum)->new VideoBase(
	    		"",
	    		"",
	    		result.getString("setDate"),
	    		"",
	    		"",
	    		"",
	    		"",
	    		""
        ));		
		return LVideoBase;		
	}
	
	public List<MockBaseTitle> getMockBaseTitle(String school_code,String mockBaseTitle_seq,String category_id,String attend,String testWay){
		String sql = "select a.*,c.name as categoryName,d.name as schoolName from eip.mockBaseTitle a,eip.category c,eip.school d where d.code=a.school_code and c.category_seq=a.category_id"; 
		if(school_code!=null && !school_code.isEmpty()) {sql+=" and a.school_code='"+school_code+"'";}
		if(category_id!=null && !category_id.isEmpty()) {sql+=" and a.category_id='"+category_id+"'";}
		if(attend!=null && !attend.isEmpty()) {sql+=" and a.attend='"+attend+"'";}
		if(testWay!=null && !testWay.isEmpty()) {sql+=" and a.testWay='"+testWay+"'";}
		if(mockBaseTitle_seq!=null && !mockBaseTitle_seq.isEmpty()) {sql+=" and a.mockBaseTitle_seq="+mockBaseTitle_seq;}
		
		List<MockBaseTitle> LMockBaseTitle = jdbcTemplate.query(sql,(result,rowNum)->new MockBaseTitle(
				result.getString("mockBaseTitle_seq"),
				result.getString("school_code"),
	    		result.getString("schoolName"),
	    		result.getString("category_id"),
	    		result.getString("categoryName"),
	    		result.getString("attend"),
	    		"",
	    		result.getString("testWay"),	    		
	    		"",
	    		result.getString("slot_1_from"),
	    		result.getString("slot_1_to"),
	    		result.getString("slot_2_from"),
	    		result.getString("slot_2_to"),
	    		result.getString("slot_3_from"),
	    		result.getString("slot_3_to"),
	    		result.getString("slot_1_from2"),
	    		result.getString("slot_1_to2"),
	    		result.getString("slot_2_from2"),
	    		result.getString("slot_2_to2"),
	    		result.getString("slot_3_from2"),
	    		result.getString("slot_3_to2"),
	    		"",
	    		""
        ));

		for(int i=0;i<LMockBaseTitle.size();i++) {
			if(LMockBaseTitle.get(i).getAttend().equals("1")) {
				LMockBaseTitle.get(i).setAttendName("個別");
			}else if(LMockBaseTitle.get(i).getAttend().equals("2")) {
				LMockBaseTitle.get(i).setAttendName("視訊");
			}else if(LMockBaseTitle.get(i).getAttend().equals("3")) {
				LMockBaseTitle.get(i).setAttendName("團體");
			}else{
				LMockBaseTitle.get(i).setAttendName("");
			}
			
			if(LMockBaseTitle.get(i).getTestWay().equals("1")) {
				LMockBaseTitle.get(i).setTestWayName("電腦");
			}else if(LMockBaseTitle.get(i).getTestWay().equals("2")) {
				LMockBaseTitle.get(i).setTestWayName("口試");
			}else if(LMockBaseTitle.get(i).getTestWay().equals("3")) {
				LMockBaseTitle.get(i).setTestWayName("紙筆");
			}else if(LMockBaseTitle.get(i).getTestWay().equals("4")) {
				LMockBaseTitle.get(i).setTestWayName("真人評測");				
			}else{
				LMockBaseTitle.get(i).setTestWayName("");
			}
			
			String panelStr0="",panelStr = "";
			List<MockPanel> LMockPanel = getMockPanel(LMockBaseTitle.get(i).getMockBaseTitle_seq(),"");
			for(int a=0;a<LMockPanel.size();a++) {
				panelStr0 += LMockPanel.get(a).getPanelName()+"</span>&nbsp;&nbsp;";
				panelStr += "<span><input type='checkbox' name='panelName' value='"+LMockPanel.get(a).getPanelName()+"' checked>"+LMockPanel.get(a).getPanelName()+"</span>&nbsp;&nbsp;";
			}
			LMockBaseTitle.get(i).setPanelStr0(panelStr0);
			LMockBaseTitle.get(i).setPanelStr(panelStr);
		}
		return LMockBaseTitle;
	}
	
	public List<CounselingBaseTitle> getCounselingBaseTitle(String school_code,String counselingBaseTitle_seq,String category_id){
		String sql = "select a.*,c.name as categoryName,d.name as schoolName from eip.counselingBaseTitle a,eip.category c,eip.school d where d.code=a.school_code and c.category_seq=a.category_id"; 
		if(school_code!=null && !school_code.isEmpty()) {sql+=" and a.school_code='"+school_code+"'";}
		if(counselingBaseTitle_seq!=null && !counselingBaseTitle_seq.isEmpty()) {sql+=" and a.counselingBaseTitle_seq="+counselingBaseTitle_seq;}
		if(category_id!=null && !category_id.isEmpty()) {sql+=" and a.category_id='"+category_id+"'";}

		List<CounselingBaseTitle> LCounselingBaseTitle = jdbcTemplate.query(sql,(result,rowNum)->new CounselingBaseTitle(
				result.getString("counselingBaseTitle_seq"),
				result.getString("school_code"),
	    		result.getString("schoolName"),
	    		result.getString("category_id"),
	    		result.getString("categoryName"),
	    		result.getString("slot_1_from"),
	    		result.getString("slot_1_to"),
	    		result.getString("slot_2_from"),
	    		result.getString("slot_2_to"),
	    		result.getString("slot_3_from"),
	    		result.getString("slot_3_to"),
	    		result.getString("slot_1_from2"),
	    		result.getString("slot_1_to2"),
	    		result.getString("slot_2_from2"),
	    		result.getString("slot_2_to2"),
	    		result.getString("slot_3_from2"),
	    		result.getString("slot_3_to2")
        ));
		return LCounselingBaseTitle;
	}	
	
	public List<MockPanel> getMockPanel(String mockBaseTitle_id,String mockPanel_seq){ 
		String sql = "select * from eip.mockPanel where 1=1";
		if(mockBaseTitle_id!=null && !mockBaseTitle_id.isEmpty()) {sql+=" and mockBaseTitle_id='"+mockBaseTitle_id+"'";}
		if(mockPanel_seq!=null && !mockPanel_seq.isEmpty()) {sql+=" and mockPanel_seq="+mockPanel_seq;}

		List<MockPanel> LMockPanel = jdbcTemplate.query(sql,(result,rowNum)->new MockPanel(
	    		result.getString("mockPanel_seq"),
	    		result.getString("category_id"),
	    		result.getString("mockBaseTitle_id"),
	    		result.getString("panelName")
        ));		
		return LMockPanel;
	}
	
	public Boolean mockBaseSave(String mockBaseTitle_seq,String setDate,String[] A_slot,String round,String updater,String witLimit) {
		jdbcTemplate.update("delete from eip.mockBase where mockBaseTitle_seq=? and setDate=?",mockBaseTitle_seq,setDate);
		String slotStr="";
		if(A_slot!=null) {
			for(int i=0;i<A_slot.length;i++) {
				if(A_slot[i]!=null && !A_slot[i].isEmpty()) {
					slotStr +=A_slot[i]+"#";
				}
			}
		}	
		jdbcTemplate.update("INSERT INTO eip.mockBase VALUES (default,?,?,?,?,?,NOW(),?);",
				mockBaseTitle_seq,
				setDate,
				slotStr,
				round,
				updater,
				witLimit
		);
		
		return true;
	}
	
	public Boolean counselingBaseSave(String counselingBaseTitle_seq,String setDate,String[] A_slot,String updater,String witLimit) {
		jdbcTemplate.update("delete from eip.counselingBase where counselingBaseTitle_seq=? and setDate=?",counselingBaseTitle_seq,setDate);
		String slotStr="";
		if(A_slot!=null) {
			for(int i=0;i<A_slot.length;i++) {
				if(A_slot[i]!=null && !A_slot[i].isEmpty()) {
					slotStr +=A_slot[i]+"#";
				}
			}
		}	
		jdbcTemplate.update("INSERT INTO eip.counselingBase VALUES (default,?,?,?,?,NOW(),?);",
				counselingBaseTitle_seq,
				setDate,
				slotStr,
				updater,
				witLimit
		);
		
		return true;
	}	

	
	public Boolean mockBaseTitleSave(
			String mockBaseTitle_seq,
			String school_code,
			String category_id,
			String attend,
			String testWay,
			String slot_1_from,
			String slot_1_to,
			String slot_2_from,
			String slot_2_to,
			String slot_3_from,
			String slot_3_to,
			String slot_1_from2,
			String slot_1_to2,
			String slot_2_from2,
			String slot_2_to2,
			String slot_3_from2,
			String slot_3_to2,
			String teacher_id1,
			String[] from1,
			String[] to1,
			String[] limit1,
			String teacher_id2,
			String[] from2,
			String[] to2,
			String[] limit2,
			String teacher_id3,
			String[] from3,
			String[] to3,
			String[] limit3			
		){
		if(mockBaseTitle_seq!=null && !mockBaseTitle_seq.isEmpty()) {
			jdbcTemplate.update("Update eip.mockBaseTitle set school_code=?,category_id=?,attend=?,testWay=?,slot_1_from=?,slot_1_to=?,slot_2_from=?,slot_2_to=?,slot_3_from=?,slot_3_to=?,slot_1_from2=?,slot_1_to2=?,slot_2_from2=?,slot_2_to2=?,slot_3_from2=?,slot_3_to2=? where mockBaseTitle_seq=?",
					school_code,
					category_id,
					attend,
					testWay,
					slot_1_from,
					slot_1_to,
					slot_2_from,
					slot_2_to,
					slot_3_from,
					slot_3_to,
					slot_1_from2,
					slot_1_to2,
					slot_2_from2,
					slot_2_to2,
					slot_3_from2,
					slot_3_to2,					
					mockBaseTitle_seq
				);			
		}else {
			jdbcTemplate.update("INSERT INTO eip.mockBaseTitle VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
				school_code,
				category_id,
				attend,
				testWay,
				slot_1_from,
				slot_1_to,
				slot_2_from,
				slot_2_to,
				slot_3_from,
				slot_3_to,
				slot_1_from2,
				slot_1_to2,
				slot_2_from2,
				slot_2_to2,
				slot_3_from2,
				slot_3_to2				
			);
			mockBaseTitle_seq = String.valueOf(jdbcTemplate.queryForObject("select last_insert_id()", Integer.class));
		}
		
		//人數限制
		jdbcTemplate.update("delete from eip.testSubjectSelection2 where mockBaseTitle_id=?",mockBaseTitle_seq);
		
		//早-真人評測
		if(teacher_id1!=null && !teacher_id1.isEmpty()) {
			jdbcTemplate.update("INSERT INTO eip.testSubjectSelection2 VALUES (default,?,?,?);",
				mockBaseTitle_seq,
				"1",
				teacher_id1
			);
			int testSubjectSelection2_seq = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
			if(from1!=null && from1.length>0) {
				for(int i=0;i<from1.length;i++) {
					jdbcTemplate.update("INSERT INTO eip.mockLimit VALUES (default,?,?,?,?);",
						testSubjectSelection2_seq,
						from1[i],
						to1[i],
						limit1[i]
					);	
				}		
			}			
		}		
		//午-真人評測
		if(teacher_id2!=null && !teacher_id2.isEmpty()) {
			jdbcTemplate.update("INSERT INTO eip.testSubjectSelection2 VALUES (default,?,?,?);",
				mockBaseTitle_seq,
				"2",
				teacher_id2
			);
			int testSubjectSelection2_seq = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
			if(from2!=null && from2.length>0) {
				for(int i=0;i<from2.length;i++) {
					jdbcTemplate.update("INSERT INTO eip.mockLimit VALUES (default,?,?,?,?);",
						testSubjectSelection2_seq,
						from2[i],
						to2[i],
						limit2[i]
					);	
				}		
			}			
		}		
		//晚-真人評測
		if(teacher_id3!=null && !teacher_id3.isEmpty()) {
			jdbcTemplate.update("INSERT INTO eip.testSubjectSelection2 VALUES (default,?,?,?);",
				mockBaseTitle_seq,
				"3",
				teacher_id3
			);
			int testSubjectSelection2_seq = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
			if(from3!=null && from3.length>0) {
				for(int i=0;i<from3.length;i++) {
					jdbcTemplate.update("INSERT INTO eip.mockLimit VALUES (default,?,?,?,?);",
						testSubjectSelection2_seq,
						from3[i],
						to3[i],
						limit3[i]
					);	
				}		
			}			
		}
					
		return true;
	}
	
	
	public Boolean counselingBaseTitleSave(
			String counselingBaseTitle_seq,
			String school_code,
			String category_id,
			String slot_1_from,
			String slot_1_to,
			String slot_2_from,
			String slot_2_to,
			String slot_3_from,
			String slot_3_to,
			String slot_1_from2,
			String slot_1_to2,
			String slot_2_from2,
			String slot_2_to2,
			String slot_3_from2,
			String slot_3_to2,
			String teacher_id1,
			String[] from1,
			String[] to1,
			String[] limit1,
			String teacher_id2,
			String[] from2,
			String[] to2,
			String[] limit2,
			String teacher_id3,
			String[] from3,
			String[] to3,
			String[] limit3	
		){
		if(counselingBaseTitle_seq!=null && !counselingBaseTitle_seq.isEmpty()) {
			jdbcTemplate.update("Update eip.counselingBaseTitle set school_code=?,category_id=?,slot_1_from=?,slot_1_to=?,slot_2_from=?,slot_2_to=?,slot_3_from=?,slot_3_to=?,slot_1_from2=?,slot_1_to2=?,slot_2_from2=?,slot_2_to2=?,slot_3_from2=?,slot_3_to2=? where counselingBaseTitle_seq=?",
					school_code,
					category_id,
					slot_1_from,
					slot_1_to,
					slot_2_from,
					slot_2_to,
					slot_3_from,
					slot_3_to,
					slot_1_from2,
					slot_1_to2,
					slot_2_from2,
					slot_2_to2,
					slot_3_from2,
					slot_3_to2,					
					counselingBaseTitle_seq
				);			
		}else {
			jdbcTemplate.update("INSERT INTO eip.counselingBaseTitle VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
				school_code,
				category_id,
				slot_1_from,
				slot_1_to,
				slot_2_from,
				slot_2_to,
				slot_3_from,
				slot_3_to,
				slot_1_from2,
				slot_1_to2,
				slot_2_from2,
				slot_2_to2,
				slot_3_from2,
				slot_3_to2				
			);
			counselingBaseTitle_seq = String.valueOf(jdbcTemplate.queryForObject("select last_insert_id()", Integer.class));
		}
		
		//人數限制
		jdbcTemplate.update("delete from eip.counselingLimit1 where counselingBaseTitle_id=?",counselingBaseTitle_seq);
		jdbcTemplate.update("delete from eip.counselingLimit2 where counselingLimit1_id in (select counselingLimit1_seq from eip.counselingLimit1 where counselingBaseTitle_id=?)",counselingBaseTitle_seq);
		//早
		if(teacher_id1!=null && !teacher_id1.isEmpty()) {
			jdbcTemplate.update("INSERT INTO eip.counselingLimit1 VALUES (default,?,?,?);",
				counselingBaseTitle_seq,
				"1",
				teacher_id1
			);
			int counselingLimit1_id = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
			if(from1!=null && from1.length>0) {
				for(int i=0;i<from1.length;i++) {
					jdbcTemplate.update("INSERT INTO eip.counselingLimit2 VALUES (default,?,?,?,?);",
							counselingLimit1_id,
							from1[i],
							to1[i],
							limit1[i]
					);
				}		
			}			
		}
		
		//午
		if(teacher_id2!=null && !teacher_id2.isEmpty()) {
			jdbcTemplate.update("INSERT INTO eip.counselingLimit1 VALUES (default,?,?,?);",
				counselingBaseTitle_seq,
				"2",
				teacher_id2
			);
			int counselingLimit1_id = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
			if(from2!=null && from2.length>0) {
				for(int i=0;i<from2.length;i++) {
					jdbcTemplate.update("INSERT INTO eip.counselingLimit2 VALUES (default,?,?,?,?);",
							counselingLimit1_id,
							from2[i],
							to2[i],
							limit2[i]
					);
				}		
			}			
		}	
		
		//晚
		if(teacher_id2!=null && !teacher_id2.isEmpty()) {
			jdbcTemplate.update("INSERT INTO eip.counselingLimit1 VALUES (default,?,?,?);",
				counselingBaseTitle_seq,
				"3",
				teacher_id3
			);
			int counselingLimit1_id = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
			if(from3!=null && from3.length>0) {
				for(int i=0;i<from3.length;i++) {
					jdbcTemplate.update("INSERT INTO eip.counselingLimit2 VALUES (default,?,?,?,?);",
							counselingLimit1_id,
							from3[i],
							to3[i],
							limit3[i]
					);
				}		
			}			
		}		
					
		return true;
	}	
	
	public List<MockBase> getMockBase(String mockBaseTitle_seq,String setDate,String mockBase_seq,String witLimit){
			String sql = "select * from eip.mockBase where 1=1";
			if(mockBaseTitle_seq!=null && !mockBaseTitle_seq.isEmpty()) {sql+=" and mockBaseTitle_seq = '"+mockBaseTitle_seq+"'";}
			if(setDate!=null && !setDate.isEmpty()) {sql+=" and setDate = '"+setDate+"'";}
			if(mockBase_seq!=null && !mockBase_seq.isEmpty()) {sql+=" and mockBase_seq = "+mockBase_seq;}
			if(witLimit!=null && !witLimit.isEmpty()) {sql+=" and witLimit = '"+witLimit+"'";}

			List<MockBase> LMockBase = jdbcTemplate.query(sql,(result,rowNum)->new MockBase(
		    		result.getString("mockBase_seq"),
		    		result.getString("mockBaseTitle_seq"),
		    		result.getString("setDate"),
		    		result.getString("slot"),
		    		"",
		    		result.getString("round"),
		    		result.getString("updater"),
		    		result.getString("update_time"),
		    		"",
		    		result.getString("witLimit")
	        ));
			
			for(int i=0;i<LMockBase.size();i++) {
				if(LMockBase.get(i).getSlot().equals("1")) {
					LMockBase.get(i).setSlotName("早");
				}else if(LMockBase.get(i).getSlot().equals("2")) {
					LMockBase.get(i).setSlotName("午");
				}else if(LMockBase.get(i).getSlot().equals("3")) {
					LMockBase.get(i).setSlotName("晚");
				}
				
				List<MockBaseTitle> LMockBaseTitle = getMockBaseTitle("",LMockBase.get(i).getMockBaseTitle_seq(),"","","");
				if(LMockBaseTitle.size()>0) {
					LMockBase.get(i).setCategoryName(LMockBaseTitle.get(0).getCategoryName());
				}

			}			
			return LMockBase;
	}
	
	public List<CounselingBase> getCounselingBase(String counselingBaseTitle_seq,String setDate,String counselingBase_seq){
		String sql = "select * from eip.counselingBase where 1=1";
		if(counselingBaseTitle_seq!=null && !counselingBaseTitle_seq.isEmpty()) {sql+=" and counselingBaseTitle_seq = '"+counselingBaseTitle_seq+"'";}
		if(setDate!=null && !setDate.isEmpty()) {sql+=" and setDate = '"+setDate+"'";}
		if(counselingBase_seq!=null && !counselingBase_seq.isEmpty()) {sql+=" and counselingBase_seq = "+counselingBase_seq;}

		List<CounselingBase> LCounselingBase = jdbcTemplate.query(sql,(result,rowNum)->new CounselingBase(
	    		result.getString("counselingBase_seq"),
	    		result.getString("counselingBaseTitle_seq"),
	    		result.getString("setDate"),
	    		result.getString("slotStr"),
	    		result.getString("updater"),
	    		result.getString("update_time"),
	    		result.getString("witLimit"),
	    		""
        ));

		for(int i=0;i<LCounselingBase.size();i++) {
		/**	
			if(LCounselingBase.get(i).getSlot().equals("1")) {
				LCounselingBase.get(i).setSlotName("早");
			}else if(LCounselingBase.get(i).getSlot().equals("2")) {
				LCounselingBase.get(i).setSlotName("午");
			}else if(LCounselingBase.get(i).getSlot().equals("3")) {
				LCounselingBase.get(i).setSlotName("晚");
			}
		**/	
			List<CounselingBaseTitle> LCounselingBaseTitle = getCounselingBaseTitle("",LCounselingBase.get(i).getCounselingBaseTitle_seq(),"");
			if(LCounselingBaseTitle.size()>0) {
				LCounselingBase.get(i).setCategoryName(LCounselingBaseTitle.get(0).getCategoryName());
			}

		}			
		return LCounselingBase;
}	
	
	
	public List<VideoSlotTitle> getVideoSlotTitle(String school_code){
		String sql = "select * from eip.videoSlotTitle where school_code='"+school_code+"'";
		List<VideoSlotTitle> LVideoSlotTitle = jdbcTemplate.query(sql,(result,rowNum)->new VideoSlotTitle(
				result.getString("videoSlotTitle_seq"),
				result.getString("school_code"),
	    		result.getString("slot_1_from"),
	    		result.getString("slot_1_to"),
	    		result.getString("slot_2_from"),
	    		result.getString("slot_2_to"),
	    		result.getString("slot_3_from"),
	    		result.getString("slot_3_to"),
	    		result.getString("slot_1_from2"),
	    		result.getString("slot_1_to2"),
	    		result.getString("slot_2_from2"),
	    		result.getString("slot_2_to2"),
	    		result.getString("slot_3_from2"),
	    		result.getString("slot_3_to2")	    		
        ));	
		return LVideoSlotTitle;
	}

	public Boolean videoSlotTitleSave(VideoSlotTitle videoSlotTitle) {
       if(videoSlotTitle.getSchool_code()!=null && !videoSlotTitle.getSchool_code().isEmpty()) {
			jdbcTemplate.update("delete from eip.videoSlotTitle where school_code=?",videoSlotTitle.getSchool_code());
			jdbcTemplate.update("INSERT INTO eip.videoSlotTitle VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?);",
					videoSlotTitle.getSchool_code(),
					videoSlotTitle.getSlot_1_from(),
					videoSlotTitle.getSlot_1_to(),
					videoSlotTitle.getSlot_2_from(),
					videoSlotTitle.getSlot_2_to(),
					videoSlotTitle.getSlot_3_from(), 
					videoSlotTitle.getSlot_3_to(),
					videoSlotTitle.getSlot_1_from2(),
					videoSlotTitle.getSlot_1_to2(),
					videoSlotTitle.getSlot_2_from2(),
					videoSlotTitle.getSlot_2_to2(),
					videoSlotTitle.getSlot_3_from2(), 
					videoSlotTitle.getSlot_3_to2()
			);
       }	
	   return true;
	}
	
	public Boolean updateSeatDate(String school_code,String classRoomName,String seatNo,String dateFrom,String dateTo,String updater,String comment) {
		jdbcTemplate.update("Delete from eip.classRoomUnavail where school_code=? and classRoomName=? and seatNo=?",
				school_code,
				classRoomName,
				seatNo		
		);
		

		jdbcTemplate.update("INSERT INTO eip.classRoomUnavail VALUES (default,?,?,?,?,?,?,NOW(),?);",
				school_code,
				classRoomName,
				seatNo,
				dateFrom,
				dateTo,
				updater,
				comment
		);
		return true;
	}
	
	public List<ClassRoomUnavail> getClassRoomUnavail(String school_code,String classRoomName,String nowDate){		
		String sql = "select * from eip.classRoomUnavail"+
	                 " where school_code='"+school_code+"' and classRoomName='"+classRoomName+"'";
		if(nowDate!=null && !nowDate.isEmpty()) {
					 sql+=
					 " and dateFrom!=''"+		 
				     " and "+nowDate+">=dateFrom"+
	                 " and ("+nowDate+"<=dateTo or dateTo='')";
		}
		List<ClassRoomUnavail> LClassRoomUnavail = jdbcTemplate.query(sql,(result,rowNum)->new ClassRoomUnavail(
				result.getString("classRoomUnavail_seq"),
				result.getString("school_code"),
	    		result.getString("classRoomName"),
	    		result.getString("seatNo"),
	    		result.getString("dateFrom"),
	    		result.getString("dateTo"),
	    		result.getString("updater"),
	    		result.getString("updateTime"),
	    		result.getString("comment")
        ));	
		
		return LClassRoomUnavail;
	}
	
	public Boolean insertSeatOccupy(String school_code,String setDate,String slot,String classroom,String seat,String student_no){
		jdbcTemplate.update("INSERT INTO eip.seatOccupy VALUES (default,?,?,?,?,?,?);",
				school_code,
				setDate,
				slot,
				classroom,
				seat,
				student_no
		);			
		return true;
	}
	
	public Boolean deleteSeatOccupy(String school_code,String setDate,String slot,String classroom,String seat,String student_no){
		jdbcTemplate.update("Delete from eip.seatOccupy where school_code=? and setDate=? and slot=? and classroom=? and seat=? and student_no=?",
				school_code,
				setDate,
				slot,
				classroom,
				seat,
				student_no
		);			
		return true;
	}	

	
	public Boolean UpdateMockVideoHistory(String mockVideoHistory_seq,String updater,String comment,String seat,String classRoom){
		if(mockVideoHistory_seq!=null && !mockVideoHistory_seq.isEmpty()) {
			String comment_value = "";
			if(comment!=null && !comment.isEmpty()) {
				comment_value = comment+"<br>";
			}			
			jdbcTemplate.update("Update eip.mockVideoHistory set attend=?,classRoom=?,seat=?,comment=CONCAT(comment,'"+comment_value+"'),updater=?,update_time=NOW() where mockVideoHistory_seq=?",
					1,
					classRoom,
					seat,
					updater,
					mockVideoHistory_seq
			);
		}
		return true;
	}
	
	public Boolean UpdateMockBaseBook(String mockBaseBook_seq,String updater,String comment,String seat,String classRoom){
		if(mockBaseBook_seq!=null && !mockBaseBook_seq.isEmpty()) {
			String comment_value = "";
			if(comment!=null && !comment.isEmpty()) {
				comment_value = comment+"<br>";
			}			
			jdbcTemplate.update("Update eip.mockBaseBook set attend=?,classRoom=?,seat=?,comment=CONCAT(comment,'"+comment_value+"'),updater=?,update_time=NOW() where mockBaseBook_seq=?",
					2,
					classRoom,
					seat,
					updater,
					mockBaseBook_seq
			);
		}
		return true;
	}
	
	public Boolean UpdateCounselingBaseBook(String counselingBaseBook_seq,String updater,String comment,String seat,String classRoom){
		if(counselingBaseBook_seq!=null && !counselingBaseBook_seq.isEmpty()) {
			String comment_value = "";
			if(comment!=null && !comment.isEmpty()) {
				comment_value = comment+"<br>";
			}			
			jdbcTemplate.update("Update eip.counselingBaseBook set attend=?,classRoom=?,seat=?,comment=CONCAT(comment,'"+comment_value+"'),updater=?,update_time=NOW() where counselingBaseBook_seq=?",
					2,
					classRoom,
					seat,
					updater,
					counselingBaseBook_seq
			);
		}
		return true;
	}	
	
	public List<SeatOccupy> getSeatOccupy(String school_code,String setDate,String slot,String classroom,String seat,String student_no){
		String sql = "select * from eip.seatOccupy where school_code='"+school_code+"' and setDate='"+setDate+"' and slot='"+slot+"' and classroom='"+classroom+"' and seat='"+seat+"'";
		if(student_no!=null && !student_no.isEmpty()) {sql+=" and student_no = '"+student_no+"'";}

		List<SeatOccupy> LSeatOccupy = jdbcTemplate.query(sql,(result,rowNum)->new SeatOccupy(
				result.getString("seatLeave_seq"),
				result.getString("school_code"),
	    		result.getString("setDate"),
	    		result.getString("slot"),
	    		result.getString("classroom"),
	    		result.getString("seat"),
	    		result.getString("student_no")
        ));		
		return LSeatOccupy;		
	}
	
	public Boolean studentSeatUpdate(String signRecordHistory_seq,String mockVideoHistory_seq,String mockBaseBook_seq,String counselingBaseBook_seq,String student_no,String setDate,String slot,String school_code,String classroom,String seat) {
		jdbcTemplate.update("Update eip.seatOccupy set classroom=?,seat=? where student_no=? and school_code=? and setDate=? and slot=?",
				classroom,
				seat,
				student_no,
				school_code,
				setDate,
				slot
		);
		
		if(signRecordHistory_seq!=null && !signRecordHistory_seq.isEmpty()) {
			jdbcTemplate.update("Update eip.signRecordHistory set classroom=?,seat=? where signRecordHistory_seq=?",
					classroom,
					seat,
					signRecordHistory_seq
			);			
		}else if(mockVideoHistory_seq!=null && !mockVideoHistory_seq.isEmpty()) {
			jdbcTemplate.update("Update eip.mockVideoHistory set classroom=?,seat=? where mockVideoHistory_seq=?",
					classroom,
					seat,
					mockVideoHistory_seq
			);				
		}else if(mockBaseBook_seq!=null && !mockBaseBook_seq.isEmpty()) {
			jdbcTemplate.update("Update eip.mockBaseBook set classroom=?,seat=? where mockBaseBook_seq=?",
					classroom,
					seat,
					mockBaseBook_seq
			);				
		}else if(counselingBaseBook_seq!=null && !counselingBaseBook_seq.isEmpty()) {
			jdbcTemplate.update("Update eip.counselingBaseBook set classroom=?,seat=? where counselingBaseBook_seq=?",
					classroom,
					seat,
					counselingBaseBook_seq
			);				
		}
		return true;
	}
	
	
	
	public Boolean commentUpdate(String signRecordHistory_seq,String mockVideoHistory_seq,String mockBaseBook_seq,String counselingBaseBook_seq,String comment) {

		String comment_value = "";
		if(comment!=null && !comment.isEmpty()) {
			comment_value = comment+"<br>";
		}
		if(signRecordHistory_seq!=null && !signRecordHistory_seq.isEmpty()) {
			jdbcTemplate.update("Update eip.signRecordHistory set comment=CONCAT(comment,'"+comment_value+"') where signRecordHistory_seq=?",
					signRecordHistory_seq
			);			
		}else if(mockVideoHistory_seq!=null && !mockVideoHistory_seq.isEmpty()) {
			jdbcTemplate.update("Update eip.mockVideoHistory set comment=CONCAT(comment,'"+comment_value+"') where mockVideoHistory_seq=?",
					mockVideoHistory_seq
			);				
		}else if(mockBaseBook_seq!=null && !mockBaseBook_seq.isEmpty()) {
			jdbcTemplate.update("Update eip.mockBaseBook set comment=CONCAT(comment,'"+comment_value+"') where mockBaseBook_seq=?",
					mockBaseBook_seq
			);				
		}else if(counselingBaseBook_seq!=null && !counselingBaseBook_seq.isEmpty()) {
			jdbcTemplate.update("Update eip.counselingBaseBook set comment=CONCAT(comment,'"+comment_value+"') where counselingBaseBook_seq=?",
					counselingBaseBook_seq
			);				
		}
		return true;
	}
	
	
	public List<TestSubjectSelection2> getTestSubjectSelection2(String mockBaseTitle_seq,String testSubjectSelection2_seq,String teacher_id){
		
		String sql = "select * from eip.testSubjectSelection2 where 1=1";
		if(mockBaseTitle_seq!=null && !mockBaseTitle_seq.isEmpty()) {sql+=" and mockBaseTitle_id = '"+mockBaseTitle_seq+"'";}
		if(testSubjectSelection2_seq!=null && !testSubjectSelection2_seq.isEmpty()) {sql+=" and testSubjectSelection2_seq = "+testSubjectSelection2_seq;}
		if(teacher_id!=null && !teacher_id.isEmpty()) {sql+=" and teacher_id = '"+teacher_id+"'";}

		List<TestSubjectSelection2> LTestSubjectSelection2 = jdbcTemplate.query(sql,(result,rowNum)->new TestSubjectSelection2(
				result.getString("testSubjectSelection2_seq"),
				result.getString("mockBaseTitle_id"),
	    		result.getString("slot"),
	    		result.getString("teacher_id")
        ));	
		
		return LTestSubjectSelection2;		
	}
	
	
	public List<CounselingLimit2> getCounselingLimit2(String counselingLimit1_id,String counselingLimit2_seq){
		String sql = "select * from eip.counselingLimit2 where 1=1";
		if(counselingLimit1_id!=null && !counselingLimit1_id.isEmpty()) {sql+=" and counselingLimit1_id = '"+counselingLimit1_id+"'";}
		if(counselingLimit2_seq!=null && !counselingLimit2_seq.isEmpty()) {sql+=" and counselingLimit2_seq = "+counselingLimit2_seq;}

		List<CounselingLimit2> LCounselingLimit2 = jdbcTemplate.query(sql,(result,rowNum)->new CounselingLimit2(
				result.getString("counselingLimit2_seq"),
				result.getString("counselingLimit1_id"),
	    		result.getString("from1"),
	    		result.getString("to1"),
	    		result.getString("noLimit")
        ));		
		return LCounselingLimit2;		
	}	
	
	public Boolean addMockPanel(String category_id,String mockBaseTitle_seq,String[] panelName) {
		if(mockBaseTitle_seq!=null) {
			jdbcTemplate.update("DELETE from eip.mockPanel where mockBaseTitle_id=?",mockBaseTitle_seq);
		}
		
		if(panelName!=null) {
			for(int i=0;i<panelName.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.mockPanel VALUES (default,?,?,?);",
						category_id,
						mockBaseTitle_seq,
						panelName[i]
				);
			}	
		}
				
		return true;
	}
	
	public List<MockLimit> getMockLimit(String testSubjectSelection2_id,String mockLimit_seq){
		String sql = "select a.*,b.slot from eip.mockLimit a,eip.testSubjectSelection2 b where a.testSubjectSelection2_id=b.testSubjectSelection2_seq";
		if(testSubjectSelection2_id!=null && !testSubjectSelection2_id.isEmpty()) {sql+=" and testSubjectSelection2_id = '"+testSubjectSelection2_id+"'";}
		if(mockLimit_seq!=null && !mockLimit_seq.isEmpty()) {sql+=" and mockLimit_seq = "+mockLimit_seq;}

		List<MockLimit> LMockLimit = jdbcTemplate.query(sql,(result,rowNum)->new MockLimit(
				result.getString("mockLimit_seq"),
				result.getString("testSubjectSelection2_id"),
	    		result.getString("from1"),
	    		result.getString("to1"),
	    		result.getString("noLimit"),
	    		result.getString("slot")
        ));		
		return LMockLimit;		
	}
	
	public List<OnlineClass> getOnlineClass(String class_id){
		String sql = "select * from eip.onlineClasses where class_id='"+class_id+"'";
	
		List<OnlineClass> LOnlineClass = jdbcTemplate.query(sql,(result,rowNum)->new OnlineClass(
				result.getString("onlineClasses_seq"),
				result.getString("class_id"),
	    		result.getString("link")
        ));		
		return LOnlineClass;		
	}
	
	public List<CounselingLimit1> getCounselingLimit1(String counselingBaseTitle_id,String slot,String teacher_id,String slot_in){
		String sql = "select a.*,b.name as teacher_name from eip.counselingLimit1 a,teacher b where a.teacher_id=b.teacher_seq";
		if(counselingBaseTitle_id!=null && !counselingBaseTitle_id.isEmpty()) {sql+=" and counselingBaseTitle_id = '"+counselingBaseTitle_id+"'";}
		if(slot!=null && !slot.isEmpty()) {sql+=" and slot = '"+slot+"'";}
		if(slot_in!=null && !slot_in.isEmpty()) {sql+=" and slot in "+slot_in;}
		if(teacher_id!=null && !teacher_id.isEmpty()) {sql+=" and teacher_id = '"+teacher_id+"'";}

		List<CounselingLimit1> LCounselingLimit1 = jdbcTemplate.query(sql,(result,rowNum)->new CounselingLimit1(
				result.getString("counselingLimit1_seq"),
	    		result.getString("counselingBaseTitle_id"),
	    		result.getString("slot"),
	    		result.getString("teacher_id"),
	    		result.getString("teacher_name")
        ));		
		return LCounselingLimit1;				
	}

}
