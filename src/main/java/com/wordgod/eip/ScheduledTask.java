package com.wordgod.eip;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wordgod.eip.Model.Classes;
import com.wordgod.eip.Model.ComboSale;
import com.wordgod.eip.Model.Grade;
import com.wordgod.eip.Model.OBJ_A;
import com.wordgod.eip.Model.ScheduleActive;
import com.wordgod.eip.Model.SignRecordHistory;
import com.wordgod.eip.Model.Student;
import com.wordgod.eip.Model.Subject_tmp;
import com.wordgod.eip.Model.VirtualSubject_tmp;
import com.wordgod.eip.Service.AdmService;
import com.wordgod.eip.Service.CourseSaleService;
import com.wordgod.eip.Service.CourseService;
import com.wordgod.eip.Service.ManagerService;
import com.wordgod.eip.Service.SalesService;
import com.wordgod.eip.Service.SystemService;

@Component
public class ScheduledTask {

	@Autowired
	CourseService courseService;
	@Autowired
	SalesService salesService;
	@Autowired
	private JdbcTemplate jdbcTemplate;	
    @Autowired
    SystemService systemService;
    @Autowired
    CourseSaleService courseSaleService;
    @Autowired
    AdmService admService;
    @Autowired
    ManagerService managerService;    
    
    
    //*****課程上下架 #3:待上架,4:上架,5:下架***********//
	//*****預定時間為凌晨1點1分(cron = "0 1 1 * * *")****//
	@Scheduled(cron = "0 1 8 * * *")
    public void schedule_1(){
			systemService.schedule_1();
    }
	
    //*****Video下架***********//
	//*****預定時間為凌晨1點1分(cron = "0 1 6 * * *")****//
	@Scheduled(cron = "0 1 6 * * *")
    public void schedule_1a(){
		ScheduleActive scheduleActive= systemService.getScheduleActive("2").get(0);
		if(scheduleActive.getActive().equals("1")) { //啟用
			systemService.schedule_1a();
		}	
    }	
    
    
    //*****科目內容更改****//
    //*****預定時間為凌晨兩點1分(cron = "0 21 1 * * *")****//
	@Scheduled(cron = "0 21 8 * * *")
    public void schedule_0(){
    	try {
    		DateFormat df = new SimpleDateFormat("yyyyMMdd");
    		String today = df.format(new Date());
    		//subject
    		List<Subject_tmp> LSubject_tmp = courseService.getSubject_tmp("","",today);
    		for(int i=0;i<LSubject_tmp.size();i++) {
    			courseService.migrateSubject_tmp(LSubject_tmp.get(i));
    			System.out.println("科目內容更改 : "+LSubject_tmp.get(i).getName());
    		}
    		
    		//virtualSubject自由選
    		List<VirtualSubject_tmp> LVirtualSubject_tmp = courseService.getVirtualSubject_tmp("",today);
    		for(int i=0;i<LVirtualSubject_tmp.size();i++) {
    			jdbcTemplate.update("DELETE from eip.virtualSubject where subject_id=?",
    					LVirtualSubject_tmp.get(i).getSubject_id()
    			);
    		}
    		for(int i=0;i<LVirtualSubject_tmp.size();i++) {
				jdbcTemplate.update("INSERT INTO eip.virtualSubject VALUES (default,?,?)",
						LVirtualSubject_tmp.get(i).getSubject_id(),
						LVirtualSubject_tmp.get(i).getChild_subject_id()
				);
    		}     		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}    		
    }
	
    //*****新套裝上架 status_code 3-->4****//
    //*****預定時間為凌晨兩點1分(cron = "0 31 1 * * *")****//
	@Scheduled(cron = "0 31 8 * * *")
    public void schedule_0a(){
    	try {
    		DateFormat df = new SimpleDateFormat("yyyyMMdd");
    		String today = df.format(new Date());

    		List<ComboSale> LComboSale = courseSaleService.getComboSale("","","","","1","","","0");
    		for(int i=0;i<LComboSale.size();i++) {
    			if(LComboSale.get(i).getSchedule_time().equals(today) && LComboSale.get(i).getFlowStatus_code().equals("3")) {
    				courseSaleService.UpdatecomboSaleStatus(LComboSale.get(i).getComboSale_seq(),"4");
    			}
    			System.out.println("新套裝上架 : "+LComboSale.get(i).getName());
    		} 		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}    		
    }	
    
    //*****學員課程結束未到填入缺席****//
    //*****預定時間為每日0955-2255 每小時觸發****//
	@Scheduled(cron = "0 50 9-23 * * ?")
    public void schedule_attend(){
		ScheduleActive scheduleActive= systemService.getScheduleActive("1").get(0);
		if(scheduleActive.getActive().equals("1")) { //啟用
			DateFormat df = new SimpleDateFormat("MMdd:HH:mm");
			String today = df.format(new Date());
			System.out.println("現在時間---->"+today);		
	    	try {
	        	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	        	String class_date = dateFormat.format(new Date());
	        	List<Classes> LClasses= courseService.getClasses("","",class_date,"","","","","","","");
	        	
	        	DateFormat dateFormat2 = new SimpleDateFormat("HHmm");
	        	String now_time = dateFormat2.format(new Date());
	        	
	        	//***A.正班點名***//
	        		String class_style = "1";
	        	for(int i=0;i<LClasses.size();i++) {
	        		//該堂課已結束
	        		if(LClasses.get(i).getTime_to()!=null && !LClasses.get(i).getTime_to().isEmpty() && Integer.valueOf(now_time) >= Integer.valueOf(LClasses.get(i).getTime_to())) {     		
	        			Grade grade = courseService.getGradeList("","",LClasses.get(i).getGrade_id(),"","","","","","","","","","","","","","","1","").get(0);
		        		LClasses.get(i).setSubject_name(grade.getClass_start_date()+" "+LClasses.get(i).getSubject_name()+" "+(grade.getGradeName()==null?"":grade.getGradeName()));
		        		System.out.println("AAAA-正班課堂:"+LClasses.get(i).getSubject_name()+" 堂數"+LClasses.get(i).getClass_th());
		        		//attend=0預定;active=1最新紀錄
		        		List<SignRecordHistory> LSignRecordHistory =  admService.getSignRecordHistory("","",LClasses.get(i).getGrade_id(),"1","","","","(0)",class_style,"",LClasses.get(i).getClass_th());
		        		for(int x=0;x<LSignRecordHistory.size();x++) {
		        			jdbcTemplate.update("Update eip.signRecordHistory set attend=?,updater=?,update_time=CURDATE() where signRecordHistory_seq=?;",
		        					-1, //缺席
		        					"system",
		        					LSignRecordHistory.get(x).getSignRecordHistory_seq()
		        			);
		        			System.out.println("學號:"+LSignRecordHistory.get(x).getStudent_id());
		        		}
	        		}	
	        	}
	    		 
	        	//***B.Video點名***//
	        	    String bookSlot = "";
	        	    if(Integer.valueOf(now_time)>1300 && Integer.valueOf(now_time)<1400) {
	        	    	bookSlot = "1";
	        	    }else if(Integer.valueOf(now_time)>1800 && Integer.valueOf(now_time)<1900) {
	        	    	bookSlot = "2";
	        	    }else if(Integer.valueOf(now_time)>2200 && Integer.valueOf(now_time)<2300) {
	        	    	bookSlot = "3";
	        	    }
	        	    
	        	    if(!bookSlot.equals("")) {
		        		class_style = "2";        	
		    			//attend=0預定;active=1最新紀錄
		    			List<SignRecordHistory> LSignRecordHistory =  admService.getSignRecordHistory("","","","1","",class_date,"","(0)",class_style,"","");        	
		        		for(int x=0;x<LSignRecordHistory.size();x++) {
		        			//區分slot
		        			if( (bookSlot.equals("1") && LSignRecordHistory.get(x).getSlot().equals("1")) ||
		        				(bookSlot.equals("2") && LSignRecordHistory.get(x).getSlot().equals("2")) ||
		        				(bookSlot.equals("3") && LSignRecordHistory.get(x).getSlot().equals("3"))
		        			){
			        			  jdbcTemplate.update("Update eip.signRecordHistory set attend=?,updater=?,update_time=CURDATE() where signRecordHistory_seq=?;",
			        					-1, //缺席
			        					"system",
			        					LSignRecordHistory.get(x).getSignRecordHistory_seq()
			        			  );
			        			  System.out.println("BBBB-Video-科目"+LSignRecordHistory.get(x).getSubject_name()+" 學號:"+LSignRecordHistory.get(x).getStudent_id());
		        			 }	
		        		} 
	        	    }
	        	
	        	    managerService.applicationLogSave("system","7","-1","-1","點名缺席","","","","","");   
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    	}
		}	
	}
	
	
	
	
	
//**********************************以下未來刪除***********************************************************************************/    
    
    
    
//*****政龍 [學生資料]***<48357>********//
//*****(cron = "0 1 2 * * *")***********//	
@Scheduled(cron = "0 1 2 * * *")
  public void schedule_a(){
	  //刪除測試資料
	  jdbcTemplate.update("delete from eip.makeUpRecord"); 
	  jdbcTemplate.update("delete from eip.studentPayRecord"); 
	  jdbcTemplate.update("delete from eip.studentPayRecordDetail");
	  jdbcTemplate.update("delete from eip.Register_lagnappe");
	  jdbcTemplate.update("delete from eip.Register_outPublisher");
	  jdbcTemplate.update("delete from eip.Register_mock");
	  jdbcTemplate.update("delete from eip.Register_counseling");
	  jdbcTemplate.update("delete from eip.suspension");
	  jdbcTemplate.update("delete from eip.Register_log");
	  jdbcTemplate.update("delete from eip.registerPromo");
	  jdbcTemplate.update("delete from eip.orderChange");
	  jdbcTemplate.update("delete from eip.balanceRecord");
	  jdbcTemplate.update("delete from eip.otherSell");
	  jdbcTemplate.update("delete from eip.studentExperience");
	  jdbcTemplate.update("delete from eip.signRecordMaterialHistory");
	  jdbcTemplate.update("delete from eip.applicationLog");
	  
	  
    DriverManagerDataSource dataSource=new DriverManagerDataSource();
    dataSource.setUrl("jdbc:sqlserver://192.168.0.240:1433;databaseName=JLM1");
    dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    dataSource.setUsername("sa");
    dataSource.setPassword("0080"); 
    JdbcTemplate jdbcTemplate_JLM=new JdbcTemplate(dataSource);
    
	String sql = "SELECT  * FROM [JLM1].[dbo].[學生資料] ";

	List<OBJ_A> LOBJ_A = jdbcTemplate_JLM.query(sql,(result,rowNum)->new OBJ_A(
    		result.getString("學號"), 
    		result.getString("姓名"),
    		result.getString("英文姓名"), 
    		result.getString("性別"),
    		result.getString("自訂欄位1"), 
    		result.getString("自訂欄位2"), 
    		result.getString("身分證"), 
    		result.getString("生日"), 
    		result.getString("身分"), 
    		result.getString("電話1"),
    		result.getString("學生手機"), 
    		result.getString("Email"), 
    		result.getString("Email2"), 
    		"", //fb
    		"", //line
    		result.getString("地址"), 
    		result.getString("地址2"),
    		result.getString("家長"), 
    		result.getString("爸爸手機"),
    		"", //parent_1_email
    		"", //parent_1_line
    		result.getString("媽媽姓名"), 
    		result.getString("媽媽手機"),
    		"", //parent_2_email
    		"", //parent_2_line    		
    		result.getString("建檔人員代碼"),
    		result.getString("建檔日期"), 
    		"", //editor
    		result.getString("備註"), 
    		result.getString("備註2"),
    		result.getString("特殊身分"), 
    		result.getString("自訂欄位3"), 
    		result.getString("自訂欄位4"),
    		"", //balanceTotal
    		"", //makeUpTotal
    		"", //photo
    		"", //school_code
    		"", //school_code2
    		result.getString("更新人員")==null?"":result.getString("更新人員"),
    		result.getString("更新時間"),       		
    		result.getString("自訂欄位5"), 
    		"",//remarkTotal 
    		"",//handover
    		result.getString("電話2"), 
    		result.getString("公司電話"), 
    		result.getString("介紹人學號"), 
    		result.getString("畢業國中"), 
    		result.getString("郵遞區號"), 
    		result.getString("介紹人姓名"), 
    		result.getString("密碼"), 
    		result.getString("自訂欄位6"), 
    		result.getString("自訂欄位7"), 
    		result.getString("自訂欄位8"), 
    		result.getString("自訂欄位9") 
           ));
		    Date dNow = new Date( );
		    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			System.out.println("schedule_a----------政隆[學生資料] Query-------------------------------------------"+ft.format(dNow)); 
			jdbcTemplate.update("delete from eip.JLM_StudentInfo"); 
			for(int i=0;i<LOBJ_A.size();i++) {

				jdbcTemplate.update("INSERT INTO eip.JLM_StudentInfo VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
						LOBJ_A.get(i).getCol_1(), 
						LOBJ_A.get(i).getCol_2(), 
						LOBJ_A.get(i).getCol_3(), 
						LOBJ_A.get(i).getCol_4(), 
						LOBJ_A.get(i).getCol_5(), 
						LOBJ_A.get(i).getCol_6(), 
						LOBJ_A.get(i).getCol_7(), 
						LOBJ_A.get(i).getCol_8(), 
						LOBJ_A.get(i).getCol_9(), 
						LOBJ_A.get(i).getCol_10(), 
						LOBJ_A.get(i).getCol_11(), 
						LOBJ_A.get(i).getCol_12(), 
						LOBJ_A.get(i).getCol_13(), 
						LOBJ_A.get(i).getCol_14(), 
						LOBJ_A.get(i).getCol_15(), 
						LOBJ_A.get(i).getCol_16(), 
						LOBJ_A.get(i).getCol_17(), 
						LOBJ_A.get(i).getCol_18(), 
						LOBJ_A.get(i).getCol_19(), 
						LOBJ_A.get(i).getCol_20(), 	
						LOBJ_A.get(i).getCol_21(),
						LOBJ_A.get(i).getCol_22(),	
						LOBJ_A.get(i).getCol_23(),	
						LOBJ_A.get(i).getCol_24(),
						LOBJ_A.get(i).getCol_25().length()>49?LOBJ_A.get(i).getCol_25().substring(0,49):LOBJ_A.get(i).getCol_25(),
						LOBJ_A.get(i).getCol_26(),
						LOBJ_A.get(i).getCol_27(),
						LOBJ_A.get(i).getCol_28(),
						LOBJ_A.get(i).getCol_29(),
						LOBJ_A.get(i).getCol_30(),
						LOBJ_A.get(i).getCol_31(),
						LOBJ_A.get(i).getCol_32(),
						LOBJ_A.get(i).getCol_33(),
						LOBJ_A.get(i).getCol_34(),
						LOBJ_A.get(i).getCol_35(),
						LOBJ_A.get(i).getCol_36(),
						LOBJ_A.get(i).getCol_37(),
						LOBJ_A.get(i).getCol_38(),
						LOBJ_A.get(i).getCol_39(),
						LOBJ_A.get(i).getCol_40(),
						LOBJ_A.get(i).getCol_41(),
						LOBJ_A.get(i).getCol_42(),
						LOBJ_A.get(i).getCol_43(),
						LOBJ_A.get(i).getCol_44(),
						LOBJ_A.get(i).getCol_45(),
						LOBJ_A.get(i).getCol_46(),
						LOBJ_A.get(i).getCol_47(),
						LOBJ_A.get(i).getCol_48(),
						LOBJ_A.get(i).getCol_49(),
						LOBJ_A.get(i).getCol_50(),
						LOBJ_A.get(i).getCol_51(),
						LOBJ_A.get(i).getCol_52(),
						LOBJ_A.get(i).getCol_53(),
						LOBJ_A.get(i).getCol_54()
				);
			}
			
		    Date dNow2 = new Date( );
		    SimpleDateFormat ft2 = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			System.out.println("schedule_a-------EIP[學生資料] Insert----------------------------------------------"+ft2.format(dNow2));    

			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@schedule_b");
			schedule_b();
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@schedule_c");
			schedule_c();
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@schedule_d");
			schedule_d();
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@schedule_e");
			schedule_e();	
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@schedule_f");
			schedule_f();	
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@schedule_g");
			schedule_g();
		
										
			systemService.Migration1();
			
			//systemService.Migration2(); //Danger@@@學生相片 eip site:no suitable driver found for.....
			systemService.Migration3();
			//systemService.excel_gradeMap(); //Danger@@@會改到手動新增的期別
			//systemService.ComboBuild(); //Danger@@@科目建立單科
			systemService.insertSubjectName(); //單科: 補eip.comboSale之name及originSubjectName,若兩者相同, 前端顯示類別名稱
			systemService.eip_register2();
			systemService.subject_price(); //給定同學已報名之科目當時原價
			systemService.updateSlot(); //更新原政龍課堂由時間轉為時段
		
			try {
				systemService.upload2("3"); //實體點名
			}catch (IOException e) {
				e.printStackTrace();
			}
			
			
			try {
				systemService.uploadVideo2("2"); //Video點名
			}catch (IOException e) {
				e.printStackTrace();
			}	
					
	}
	
	
//*****政龍 [學生班別資料] <386506>***********//
//*****(cron = "0 35 12 * * *")*********//	
	//@Scheduled(cron = "never")
  public void schedule_b(){	
    DriverManagerDataSource dataSource=new DriverManagerDataSource();
    dataSource.setUrl("jdbc:sqlserver://192.168.0.240:1433;databaseName=JLM1");
    dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    dataSource.setUsername("sa");
    dataSource.setPassword("0080"); 
    JdbcTemplate jdbcTemplate_JLM=new JdbcTemplate(dataSource);
    
	String sql = "SELECT * FROM [JLM1].[dbo].[學生班別資料] ";

	List<OBJ_A> LOBJ_A = jdbcTemplate_JLM.query(sql,(result,rowNum)->new OBJ_A(
    		result.getString("班別代碼"),
    		result.getString("報名日期"),
    		result.getString("座號"),
    		result.getString("業務"),
    		result.getString("學號"),
    		"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
           ));
		    Date dNow = new Date( );
		    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			System.out.println("schedule_b----------政隆 [學生班別資料]Query-------------------------------------------"+ft.format(dNow)); 
			jdbcTemplate.update("delete from eip.JLM_StudentGradeInfo");
			for(int i=0;i<LOBJ_A.size();i++) {
				jdbcTemplate.update(
						"INSERT INTO eip.JLM_StudentGradeInfo VALUES (?,?,?,?,?);",
						LOBJ_A.get(i).getCol_1(), 
						LOBJ_A.get(i).getCol_2(), 
						LOBJ_A.get(i).getCol_3(), 
						LOBJ_A.get(i).getCol_4(), 
						LOBJ_A.get(i).getCol_5()		
				);
			}
			
		    Date dNow2 = new Date( );
		    SimpleDateFormat ft2 = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			System.out.println("schedule_b-------EIP [學生班別資料]Insert----------------------------------------------"+ft2.format(dNow2));    
			}

	
//*****政龍 [班別]***<13473>********//
//*****(cron = "0 30 11 * * *")*********//		
	//@Scheduled(cron = "never")
  public void schedule_c(){	
    DriverManagerDataSource dataSource=new DriverManagerDataSource();
    dataSource.setUrl("jdbc:sqlserver://192.168.0.240:1433;databaseName=JLM1");
    dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    dataSource.setUsername("sa");
    dataSource.setPassword("0080"); 
    JdbcTemplate jdbcTemplate_JLM=new JdbcTemplate(dataSource);
    
	String sql = "SELECT  * FROM [JLM1].[dbo].[班別] ";

	List<OBJ_A> LOBJ_A = jdbcTemplate_JLM.query(sql,(result,rowNum)->new OBJ_A(
    		result.getString("班別"),
    		result.getString("班級代碼"),
    		result.getString("代碼"),
    		"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
           ));
		    Date dNow = new Date( );
		    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			System.out.println("schedule_c----------政隆 [班別]Query-------------------------------------------"+ft.format(dNow)); 
			jdbcTemplate.update("delete from eip.JLM_StudentGrade");
			for(int i=0;i<LOBJ_A.size();i++) {
				jdbcTemplate.update(
						"INSERT INTO eip.JLM_StudentGrade VALUES (?,?,?);",
						LOBJ_A.get(i).getCol_1(), 
						LOBJ_A.get(i).getCol_2(), 
						LOBJ_A.get(i).getCol_3()		
				);
			}
			
		    Date dNow2 = new Date( );
		    SimpleDateFormat ft2 = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			System.out.println("schedule_c-------EIP[班別] Insert----------------------------------------------"+ft2.format(dNow2));    
			}
	
	
//*****政龍 [使用者]***<102>********//
//*****(cron = "0 35 12 * * *")*********//		
	//@Scheduled(cron = "never")
  public void schedule_d(){	
    DriverManagerDataSource dataSource=new DriverManagerDataSource();
    dataSource.setUrl("jdbc:sqlserver://192.168.0.240:1433;databaseName=JLM1");
    dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    dataSource.setUsername("sa");
    dataSource.setPassword("0080"); 
    JdbcTemplate jdbcTemplate_JLM=new JdbcTemplate(dataSource);
    
	String sql = "SELECT  * FROM [JLM1].[dbo].[使用者] ";

	List<OBJ_A> LOBJ_A = jdbcTemplate_JLM.query(sql,(result,rowNum)->new OBJ_A(
    		result.getString("學號"),
    		result.getString("姓名"),
    		"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
           ));
		    Date dNow = new Date( );
		    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			System.out.println("schedule_d----------政隆 [使用者]Query-------------------------------------------"+ft.format(dNow)); 
			jdbcTemplate.update("delete from eip.JLM_employee");
			for(int i=0;i<LOBJ_A.size();i++) {
				jdbcTemplate.update(
						"INSERT INTO eip.JLM_employee VALUES (?,?);",
						LOBJ_A.get(i).getCol_1().trim(), 
						LOBJ_A.get(i).getCol_2().trim()		
				);
			}
			
		    Date dNow2 = new Date( );
		    SimpleDateFormat ft2 = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			System.out.println("schedule_d-------EIP[eip.JLM_employee]Insert----------------------------------------------"+ft2.format(dNow2));    
			}
	
	
//*****政龍 [學費優待]***<360939>********//
//*****(cron = "0 35 12 * * *")*********//	
	//@Scheduled(cron = "never")
  public void schedule_e(){	
    DriverManagerDataSource dataSource=new DriverManagerDataSource();
    dataSource.setUrl("jdbc:sqlserver://192.168.0.240:1433;databaseName=JLM1");
    dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    dataSource.setUsername("sa");
    dataSource.setPassword("0080"); 
    JdbcTemplate jdbcTemplate_JLM=new JdbcTemplate(dataSource);
    
	String sql = "SELECT  * FROM [JLM1].[dbo].[學費優待] ";

	List<OBJ_A> LOBJ_A = jdbcTemplate_JLM.query(sql,(result,rowNum)->new OBJ_A(
    		result.getString("學號"),
    		result.getString("班別代碼"),
    		result.getString("優待"),
    		"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
           ));
		    Date dNow = new Date( );
		    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			System.out.println("schedule_e----------政隆[學費優待] Query-------------------------------------------"+ft.format(dNow)); 
			jdbcTemplate.update("delete from eip.JLM_StudentDiscount");
			for(int i=0;i<LOBJ_A.size();i++) {
				jdbcTemplate.update(
						"INSERT INTO eip.JLM_StudentDiscount VALUES (?,?,?);",
						LOBJ_A.get(i).getCol_1(), 
						LOBJ_A.get(i).getCol_2(),
						LOBJ_A.get(i).getCol_3()
				);
			}
			
		    Date dNow2 = new Date( );
		    SimpleDateFormat ft2 = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			System.out.println("schedule_e-------EIP [學費優待]Insert----------------------------------------------"+ft2.format(dNow2));    
			}
	
//*****政龍 [班級]***<12357>********//
//*****(cron = "0 35 12 * * *")*********//	
	//@Scheduled(cron = "never")
  public void schedule_f(){	
    DriverManagerDataSource dataSource=new DriverManagerDataSource();
    dataSource.setUrl("jdbc:sqlserver://192.168.0.240:1433;databaseName=JLM1");
    dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    dataSource.setUsername("sa");
    dataSource.setPassword("0080"); 
    JdbcTemplate jdbcTemplate_JLM=new JdbcTemplate(dataSource);
    
	String sql = "SELECT * FROM [JLM1].[dbo].[班級]";

	List<OBJ_A> LOBJ_A = jdbcTemplate_JLM.query(sql,(result,rowNum)->new OBJ_A(
    		result.getString("班級代碼"),
    		result.getString("應繳學費"),
    		result.getString("修業期限起"),
    		result.getString("修業期限訖"),
    		"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
           ));
		    Date dNow = new Date( );
		    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			System.out.println("schedule_f----------政隆 [班級]Query-------------------------------------------"+ft.format(dNow)); 
			jdbcTemplate.update("delete from eip.JLM_StudentClass");
			for(int i=0;i<LOBJ_A.size();i++) {
				jdbcTemplate.update(
						"INSERT INTO eip.JLM_StudentClass VALUES (?,?,?,?);",
						LOBJ_A.get(i).getCol_1(), 
						LOBJ_A.get(i).getCol_2(),
						LOBJ_A.get(i).getCol_3(),
						LOBJ_A.get(i).getCol_4()
				);
			}
			
		    Date dNow2 = new Date( );
		    SimpleDateFormat ft2 = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			System.out.println("schedule_f-------EIP [班級]Insert----------------------------------------------"+ft2.format(dNow2));    
			}
	

//*****政龍 [繳費記錄]***<453876>********//
//*****(cron = "0 40 17 * * *")*********//	
	//@Scheduled(cron = "never")
  public void schedule_g(){	
    DriverManagerDataSource dataSource=new DriverManagerDataSource();
    dataSource.setUrl("jdbc:sqlserver://192.168.0.240:1433;databaseName=JLM1");
    dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    dataSource.setUsername("sa");
    dataSource.setPassword("0080"); 
    JdbcTemplate jdbcTemplate_JLM=new JdbcTemplate(dataSource);
    
	String sql = "SELECT * FROM [JLM1].[dbo].[繳費記錄] ";

	List<OBJ_A> LOBJ_A = jdbcTemplate_JLM.query(sql,(result,rowNum)->new OBJ_A(
    		result.getString("班別代碼"),
    		result.getString("繳費金額"),
    		result.getString("承辦人"),
    		result.getString("收據編號"),
    		result.getString("繳費日期"),
    		result.getString("繳費時間"),
    		result.getString("繳費方式"),
    		result.getString("學號"),
    		"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
           ));
		    Date dNow = new Date( );
		    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			System.out.println("schedule_g----------政隆[繳費記錄] Query-------------------------------------------"+ft.format(dNow)); 
			jdbcTemplate.update("delete from eip.JLM_StudentFee");
			for(int i=0;i<LOBJ_A.size();i++){
				jdbcTemplate.update(
						"INSERT INTO eip.JLM_StudentFee VALUES (?,?,?,?,?,?,?,?);",
						LOBJ_A.get(i).getCol_1(), 
						LOBJ_A.get(i).getCol_2(),
						LOBJ_A.get(i).getCol_3(),
						LOBJ_A.get(i).getCol_4(),
						LOBJ_A.get(i).getCol_5(),
						LOBJ_A.get(i).getCol_6(),
						LOBJ_A.get(i).getCol_7(),
						LOBJ_A.get(i).getCol_8()
				);
			}
			
		    Date dNow2 = new Date( );
		    SimpleDateFormat ft2 = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			System.out.println("schedule_g-------EIP [繳費記錄]Insert----------------------------------------------"+ft2.format(dNow2));    
	}	
}