package com.wordgod.eip.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wordgod.eip.Model.*;

@Service
public class SystemService_or {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private SalesService salesService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private SystemService systemService;	
	@Autowired
	private CourseService courseService;		
	@Autowired
	private CourseSaleService courseSaleService;
	@Autowired
	private AdmService admService;		
	
	public boolean categoryUpdate(Category category) {
		jdbcTemplate.update("update eip.category set bgcolor=? where category_seq=?",
					category.getBgColor(),
				    category.getCategory_seq()
				    );
		return true;
	}
	
	public List<JLM_gradeAll> getJL_gradeList(String category_id,String gradeName,String gradeId) {
		String sql = "SELECT a.*,c.eip_grade_seq as eip_grade_seq2,b.name as category_name"+
					" from eip.JLM_gradeAll a left join eip.JL_EIP_grade c on a.gradeId=c.JL_gradeId"+
					" ,category b"+
					" where a.category_id=b.category_seq";
		if (category_id != null && !category_id.isEmpty()) {
			sql += " and category_id = "+category_id;
		}
		if (gradeName != null && !gradeName.isEmpty()) {
			sql += " and gradeName like '%"+gradeName+"%'";
		}		
		if (gradeId != null && !gradeId.isEmpty()) {
			sql += " and gradeId = "+gradeId;
		}
		sql +=" order by gradeId desc limit 1000";
	
		List<JLM_gradeAll> LJLM_gradeAll = jdbcTemplate.query(sql,
				(result, rowNum) -> new JLM_gradeAll(
						result.getString("gradeAll_seq"),
						result.getString("category_id"), 
						result.getString("category_name"), 
						result.getString("gradeId"),
						result.getString("gradeName"), 
						result.getString("dateFrom")==null?"":result.getString("dateFrom").substring(0,10), 
						result.getString("dateTo")==null?"":result.getString("dateTo").substring(0,10),
						result.getString("eip_grade_seq2")==null?"?":result.getString("eip_grade_seq2")	
				));
		return LJLM_gradeAll;
	}
	
	
	public List<JL_EIP_grade> getJL_EIP_grade(String eip_grade_seq,String JL_gradeId) {
		String sql = "SELECT * from eip.JL_EIP_grade where 1=1";
		if (eip_grade_seq != null && !eip_grade_seq.isEmpty()) {
			sql += " and eip_grade_seq = "+eip_grade_seq;
		}
		if (JL_gradeId != null && !JL_gradeId.isEmpty()) {
			sql += " and JL_gradeId = "+JL_gradeId;
		}		
		List<JL_EIP_grade> LJL_EIP_grade = jdbcTemplate.query(sql,
				(result, rowNum) -> new JL_EIP_grade(
						result.getString("JL_EIP_grade_seq"),
						result.getString("eip_grade_seq"), 
						result.getString("JL_gradeId"), 
						result.getString("class_style"),
						result.getString("subject_id"),
						result.getString("schoolAbbr")
				));
		return LJL_EIP_grade;
	}
	
	public List<JLM_gradeRegister> getJLM_gradeRegister(String student_no,String nolimit){

		String sql = "SELECT * from eip.JLM_gradeRegister where 1=1";
		if (student_no != null && !student_no.isEmpty()) {
			sql += " and student_no = '"+student_no+"'";
		}
		if (nolimit == null || nolimit.isEmpty()) {
			sql +=" order by registerDate desc limit 1000";
		}else {
			sql +=" order by gradeId desc"; //為使先處理到已有期別的, 再處理政龍未定
		}

		List<JLM_gradeRegister> LJLM_gradeRegister = jdbcTemplate.query(sql,
				(result, rowNum) -> new JLM_gradeRegister(
						result.getString("gradeRegister_seq"),
						result.getString("student_no"), 
						result.getString("gradeId"), 
						result.getString("gradeName"),
						result.getString("saleId"),
						result.getString("salePerson"),
						result.getString("registerDate"),
						result.getString("sitNo"),
						""
				));
		return LJLM_gradeRegister;
	}

	
	public List<ScheduleTask> getScheduleTask(String FlowStatus){
		String sql = "SELECT * from eip.scheduleTask limit 1000";
		if (FlowStatus != null && !FlowStatus.isEmpty()) {
			sql += " and FlowStatus = '"+FlowStatus+"'";
		}		

		List<ScheduleTask> LScheduleTask = jdbcTemplate.query(sql,
				(result, rowNum) -> new ScheduleTask(
						result.getString("scheduleTask_seq"),
						result.getString("recordsID"), 
						result.getString("scheduleName"), 
						result.getString("scheduleTime"),
						result.getString("FlowStatus")
				));
		return LScheduleTask;
	}
	
	
	public boolean Migration1() {
    	System.out.println("-----------------------------------------");
    	System.out.println("JLM1[基本資料] [報名資料] [繳費] [繳費記錄] 儲存 ");      	
    	System.out.println("DELETE eip.student,eip.JLM_gradeRegister,eip.JLM_studentPay,eip.JLM_studentPayRecord");
		jdbcTemplate.update("delete from eip.student;");
		jdbcTemplate.update("ALTER TABLE eip.student AUTO_INCREMENT = 1;");		
		jdbcTemplate.update("delete from eip.JLM_gradeRegister;");
		jdbcTemplate.update("ALTER TABLE eip.JLM_gradeRegister AUTO_INCREMENT = 1;");
		jdbcTemplate.update("delete from eip.JLM_studentPay;");
		jdbcTemplate.update("ALTER TABLE eip.JLM_studentPay AUTO_INCREMENT = 1;");
		jdbcTemplate.update("delete from eip.JLM_studentPayRecord;");
		jdbcTemplate.update("ALTER TABLE eip.JLM_studentPayRecord AUTO_INCREMENT = 1;");
		
    	String sql = "select * from eip.JLM_StudentInfo";

		List<Student> LStudent = jdbcTemplate.query(sql,(result,rowNum)->new Student(
	    		"", //student_seq
	    		result.getString("學號"), //student_no
	    		result.getString("姓名"), //ch_name
	    		result.getString("英文姓名"), //en_name
	    		result.getString("性別"),//sex
	    		result.getString("自訂欄位1"), //category
	    		result.getString("自訂欄位2"), //derived
	    		result.getString("身分證"), //idn
	    		result.getString("生日"), //birthday
	    		result.getString("身分"), //identity
	    		result.getString("電話1"), //tel
	    		result.getString("學生手機"), //mobile_1
	    		"", //mobile_2
	    		result.getString("Email"), //email_1
	    		result.getString("Email2"), //email_2	    		
	    		result.getString("地址"), //address_1
	    		result.getString("地址2"), //address_2
	    		"", //fb
	    		"", //line	    		
	    		result.getString("家長"), //parent_1_name
	    		result.getString("爸爸手機"), //parent_1_mobile
	    		"", //parent_1_email
	    		"", //parent_1_line
	    		result.getString("媽媽姓名"), //parent_2_name
	    		result.getString("媽媽手機"), //parent_2_mobile
	    		"", //parent_2_email
	    		"", //parent_2_line
	    		result.getString("建檔人員代碼"),//creater
	    		result.getString("建檔日期").substring(0,10), //create_time	    		 
	    		"", //editor
	    		result.getString("備註"), //remark
	    		result.getString("備註2"), //remark
	    		result.getString("特殊身分"), //special_idn
	    		result.getString("自訂欄位3"), //exam
	    		result.getString("自訂欄位4"), //abroad_date
	    		"0",
	    		"0",
	    		"",
	    		"",
	    		"",
	    		result.getString("更新人員"),//updater
	    		result.getString("更新時間"), //update_time     		 		
	    		result.getString("自訂欄位5"), //col_5
	    		"",
	    		"",
	    		"",
	    		result.getString("電話2"), //tel2
	    		result.getString("公司電話"), //company_tel
	    		result.getString("介紹人學號"), //agent_studentNo
	    		result.getString("畢業國中"), //grade_highSchool
	    		result.getString("郵遞區號"), //postCode
	    		result.getString("介紹人姓名"), //agent_studentName
	    		"888888", //passwd
	    		result.getString("自訂欄位6"), //col_6
	    		result.getString("自訂欄位7"), //col_7
	    		result.getString("自訂欄位8"), //col_8
	    		result.getString("自訂欄位9"), //col_9
	    		"1", //degree
	    		"", //cowork
	    		"",
	    		""
               )); 
		
		for(int i=0;i<LStudent.size();i++) {
			salesService.StudentSave("1",LStudent.get(i),"System","",""); //基本資料,備註
			salesService.StudentRegisterSave(LStudent.get(i).getStudent_no()); //報名資料
			salesService.StudentPaySave(LStudent.get(i).getStudent_no()); //繳費
			salesService.StudentPayRecordSave(LStudent.get(i).getStudent_no()); //繳費記錄
		}
		
	    Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		System.out.println("----------migration1結束-------------------------------------------"+ft.format(dNow)); 		
		return true;
	}
	@Value("${UploadPath}")
	private String UploadPath;
    public boolean Migration2(){
      try {	
		        Connection conn = DriverManager.getConnection("jdbc:sqlserver://192.168.0.240:1433;databaseName=JLImage","sa","0080");       
				String sql = "SELECT  照片,學號 FROM [JLImage].[dbo].[學生照片]";
		        Statement state = conn.createStatement();
		        ResultSet rs = state.executeQuery(sql);
		        byte[] fileBytes;
		        String studentNo="";
		        File folder = new File(UploadPath+"studentPhoto");
		        if (folder.isDirectory()) {
		            File[] files = folder.listFiles();
		            if (files != null && files.length > 0) {
		                for (File aFile : files) {
		                    aFile.delete();
		                }
		            }
		        }      
		        while(rs.next()){
		        	     studentNo = rs.getString(2);
		        	     fileBytes = rs.getBytes(1);
		                 OutputStream targetFile=  new FileOutputStream(UploadPath+"studentPhoto//"+studentNo+".bmp");
		                 targetFile.write(fileBytes);
		                 targetFile.close();
		        }
      }catch(Exception e) {
      	e.printStackTrace();
      }   
	    Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		System.out.println("----------migration2結束-------------------------------------------"+ft.format(dNow)); 		
		return true;        

	}  	

    
    public Boolean Migration3() {
    	
	    	jdbcTemplate.update("delete from eip.videoOpen;");
	    	
	        DriverManagerDataSource dataSource=new DriverManagerDataSource();
	        dataSource.setUrl("jdbc:sqlserver://192.168.0.240:1433;databaseName=JLM1");
	        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        dataSource.setUsername("sa");
	        dataSource.setPassword("0080"); 
	        JdbcTemplate jdbcTemplate_JLM=new JdbcTemplate(dataSource);
	
	        /**托福**/        
			String sql = "select 代碼,班別,修業期限起,修業期限訖 from JLM1.dbo.班別 where 班別 like '%托福%'"; 
			
			List<JLM_gradeAll> LJLM_gradeAll = jdbcTemplate_JLM.query(sql,(result,rowNum)->new JLM_gradeAll(
		    		"", //gradeAll_seq
		    		"2", //category_id 托福
		    		"",
		    		result.getString("代碼").trim(), //gradeId
		    		result.getString("班別"), //gradeName
		    		result.getString("修業期限起"),//dateFrom
		    		result.getString("修業期限訖"), //dateTo
		    		""
	        )); 
			
			for(int i=0;i<LJLM_gradeAll.size();i++) {
				salesService.JLM_gradeAllSave(LJLM_gradeAll.get(i));
			}
			/**多益**/ 
			String sql2 = "select 代碼,班別,修業期限起,修業期限訖 from JLM1.dbo.班別 where 班別 like '%多益%'";
	
			List<JLM_gradeAll> LJLM_gradeAll2 = jdbcTemplate_JLM.query(sql2,(result,rowNum)->new JLM_gradeAll(
		    		"", //gradeAll_seq
		    		"6", //category_id 多益
		    		"",
		    		result.getString("代碼").trim(), //gradeId
		    		result.getString("班別"), //gradeName
		    		result.getString("修業期限起"),//dateFrom
		    		result.getString("修業期限訖"), //dateTo
		    		""
	        )); 
			
			for(int i=0;i<LJLM_gradeAll2.size();i++) {
				salesService.JLM_gradeAllSave(LJLM_gradeAll2.get(i));
			}
			
			/**GRE**/ 
			String sql3 = "select 代碼,班別,修業期限起,修業期限訖 from JLM1.dbo.班別 where 班別 like '%GRE%'";
	
			List<JLM_gradeAll> LJLM_gradeAll3 = jdbcTemplate_JLM.query(sql3,(result,rowNum)->new JLM_gradeAll(
		    		"", //gradeAll_seq
		    		"4", //category_id GRE
		    		"",
		    		result.getString("代碼").trim(), //gradeId
		    		result.getString("班別"), //gradeName
		    		result.getString("修業期限起"),//dateFrom
		    		result.getString("修業期限訖"), //dateTo
		    		""
	        )); 
			
			for(int i=0;i<LJLM_gradeAll3.size();i++) {
				salesService.JLM_gradeAllSave(LJLM_gradeAll3.get(i));
			}
			
			/**雅思**/ 
			String sql4 = "select 代碼,班別,修業期限起,修業期限訖 from JLM1.dbo.班別 where 班別 like '%雅思%'";
	
			List<JLM_gradeAll> LJLM_gradeAll4 = jdbcTemplate_JLM.query(sql4,(result,rowNum)->new JLM_gradeAll(
		    		"", //gradeAll_seq
		    		"5", //category_id 雅思
		    		"",
		    		result.getString("代碼").trim(), //gradeId
		    		result.getString("班別"), //gradeName
		    		result.getString("修業期限起"),//dateFrom
		    		result.getString("修業期限訖"), //dateTo
		    		""
	        )); 
			
			for(int i=0;i<LJLM_gradeAll4.size();i++) {
				salesService.JLM_gradeAllSave(LJLM_gradeAll4.get(i));
			}
			
			/**GMAT**/ 
			String sql5 = "select 代碼,班別,修業期限起,修業期限訖 from JLM1.dbo.班別 where 班別 like '%GMAT%'";
	
			List<JLM_gradeAll> LJLM_gradeAll5 = jdbcTemplate_JLM.query(sql5,(result,rowNum)->new JLM_gradeAll(
		    		"", //gradeAll_seq
		    		"3", //category_id GMAT
		    		"",
		    		result.getString("代碼").trim(), //gradeId
		    		result.getString("班別"), //gradeName
		    		result.getString("修業期限起"),//dateFrom
		    		result.getString("修業期限訖"), //dateTo
		    		""
	        )); 
			
			for(int i=0;i<LJLM_gradeAll5.size();i++) {
				salesService.JLM_gradeAllSave(LJLM_gradeAll5.get(i));
			}		
		
			/**好神**/ 
			String sql6 = "select 代碼,班別,修業期限起,修業期限訖 from JLM1.dbo.班別 where 班別 like '%字神%'"+
			" or 班別 like '%好神%'"+
			" or 班別 like '%英語學術寫作%'"+
			" or 班別 like '%思維式文法%'"+
			" or 班別 like '%活用文法%'"+
			" or 班別 like '%閱讀突破%'"+
			" or 班別 like '%英語寫作%'"+
			" or 班別 like '%黃金單字活用%'"+
			" or 班別 like '%翻譯寫作%'"+
			" or 班別 like '%英語口說活用%'"+
			" or 班別 like '%英語口說卓越%'"+
			" or 班別 like '%聽力正音%'"+
			" or 班別 like '%聽說卓越%'"+
			" or 班別 like '%職場英文%'"+
			" or 班別 like '%背景%'";
	
			List<JLM_gradeAll> LJLM_gradeAll6 = jdbcTemplate_JLM.query(sql6,(result,rowNum)->new JLM_gradeAll(
		    		"", //gradeAll_seq
		    		"1", //category_id 好神
		    		"",
		    		result.getString("代碼").trim(), //gradeId
		    		result.getString("班別"), //gradeName
		    		result.getString("修業期限起"),//dateFrom
		    		result.getString("修業期限訖"), //dateTo
		    		""
	        )); 
			
			for(int i=0;i<LJLM_gradeAll6.size();i++) {
				salesService.JLM_gradeAllSave(LJLM_gradeAll6.get(i));
			}
			
			/**CFA**/ 
			String sql7 = "select 代碼,班別,修業期限起,修業期限訖 from JLM1.dbo.班別 where 班別 like '%CFA%'";
	
			List<JLM_gradeAll> LJLM_gradeAll7 = jdbcTemplate_JLM.query(sql7,(result,rowNum)->new JLM_gradeAll(
		    		"", //gradeAll_seq
		    		"7", //category_id CFA
		    		"",
		    		result.getString("代碼").trim(), //gradeId
		    		result.getString("班別"), //gradeName
		    		result.getString("修業期限起"),//dateFrom
		    		result.getString("修業期限訖"), //dateTo
		    		""
	        )); 
			
			for(int i=0;i<LJLM_gradeAll7.size();i++) {
				salesService.JLM_gradeAllSave(LJLM_gradeAll7.get(i));
			}		
		
	
	        return true;
    }    
    
    public boolean excel_gradeMap(){
    	System.out.println("------------------------");
    	System.out.println("(Excel)政龍期別VS.EIP期別 ");    	
    	System.out.println("DELETE eip.videoOpen,eip.classes,eip.grade,eip.JL_EIP_grade");
		jdbcTemplate.update("delete from eip.videoOpen;");
		jdbcTemplate.update("ALTER TABLE eip.videoOpen AUTO_INCREMENT = 1;");
		jdbcTemplate.update("delete from eip.classes;");
		jdbcTemplate.update("ALTER TABLE eip.classes AUTO_INCREMENT = 1;");
		jdbcTemplate.update("delete from eip.grade;");
		jdbcTemplate.update("ALTER TABLE eip.grade AUTO_INCREMENT = 1;");
		jdbcTemplate.update("delete from eip.JL_EIP_grade;");
		jdbcTemplate.update("ALTER TABLE eip.JL_EIP_grade AUTO_INCREMENT = 1;");
		
    	File folder = new File("D:\\importFrom\\gradeMap\\");
    	File[] listOfFiles = folder.listFiles();
    	DataFormatter formatter = new DataFormatter();
    	try {
    	  for (File file : listOfFiles) { //每個File
    	    if (file.isFile()) { 
    			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
    			String categoryName="";
    			String category_id = "";    			
    			String JL_gradeId = "";
    			String grade_seq = "";
    			String schoolAbbr = "";
    			String subjectAbbr = "";
    			String subject_id = "";
    			String gradeName = ""; //班別
    			String old_gradeName = ""; //舊期別名稱
    			String class_start_date_0 = "";
    			String video_date = "";
    			String class_no = "";
    			String teacher_id = "";
    			String class_style = "";
    			String status_code = "-1";
    			String grade_date = "";
    			
    			

    	        for (int x=0;x<wb.getNumberOfSheets();x++) { //每個頁籤
    	        	  	        	
    	        	categoryName = wb.getSheetName(x);
    	        	if(categoryName.contains("好神")) {
    	        		System.out.println("********好神");
    	        		category_id = "1";
    	        	}else if(categoryName.contains("托福")) {
    	        		System.out.println("********托福");
    	        		category_id = "2";
    	        	}else if(categoryName.contains("GMAT")) {
    	        		System.out.println("********GMAT");
    	        		category_id = "3";
    	        	}else if(categoryName.contains("GRE")) {
    	        		System.out.println("********GRE");
    	        		category_id = "4";
    	        	}else if(categoryName.contains("雅思")) {
    	        		System.out.println("********雅思");
    	        		category_id = "5";
    	        	}else if(categoryName.contains("多益")) {
    	        		System.out.println("********多益");
    	        		category_id = "6";
    	        	}else if(categoryName.contains("CFA L1")) {
    	        		System.out.println("********CFA L1");
    	        		category_id = "7";
    	        	}else if(categoryName.contains("CFA L2")) {
    	        		System.out.println("********CFA L2");
    	        		category_id = "8";
    	        	}else {
    	        		break;
    	        	}

    				XSSFSheet sheet=wb.getSheetAt(x);
    				XSSFRow row;
    				int rowNoFlag = 0;
					List<String> EIPCode = new ArrayList<>();
    				try {
    					//暫存實體EIP代碼
    					for(int a=1;a<sheet.getPhysicalNumberOfRows();a++) {//每列
    						rowNoFlag = a;
    						row=sheet.getRow(a);
    						if(row==null || formatter.formatCellValue(row.getCell(2))=="") {break;}	
    						class_style = row.getCell(10)==null?"":formatter.formatCellValue(row.getCell(10)).trim();
    						if(class_style.equals("1")) {
    							EIPCode.add(formatter.formatCellValue(row.getCell(3)).trim());
    						}
    					}
    					
	    				for(int k=1;k<sheet.getPhysicalNumberOfRows();k++) {//每列
	    					rowNoFlag = k;
	    					row=sheet.getRow(k);
	    					if(row==null || formatter.formatCellValue(row.getCell(2))=="") {break;}
	    					JL_gradeId = formatter.formatCellValue(row.getCell(1)).trim();    					
	    					grade_seq  = row.getCell(3)==null?"":formatter.formatCellValue(row.getCell(3)).trim();
	    					schoolAbbr = row.getCell(4)==null?"":formatter.formatCellValue(row.getCell(4)).trim();
	    					if(!schoolAbbr.equals("")) {
						    	if(schoolAbbr.equals("P")) {
						    		schoolAbbr = "TPXC";
						    	}else if(schoolAbbr.equals("K")) {
						    		schoolAbbr = "KSZS";
						    	}else if(schoolAbbr.equals("C")) {
						    		schoolAbbr = "TCFX";
						    	}else if(schoolAbbr.equals("L")) {
						    		schoolAbbr = "TPSL";
						    	}	    					
	    					}
	    					gradeName = row.getCell(6)==null?"":formatter.formatCellValue(row.getCell(6)).trim();
	    					//期別
	    					class_start_date_0 = row.getCell(7)==null?"":formatter.formatCellValue(row.getCell(7)).trim();
	    					if(!class_start_date_0.equals("") && class_start_date_0.length()>=6) {
	    						if(class_start_date_0.contains("&")) { //用於字神開營/A班/...原有期別, 放在期別別名
	    							String[] tokens = class_start_date_0.split("&");
	    							video_date = tokens[1];
	    						}else {
	    							video_date = "";
	    						}
	    						grade_date = class_start_date_0;
	    						class_start_date_0 = class_start_date_0.substring(2,4)+"/"+class_start_date_0.substring(4,6)+"/20"+class_start_date_0.substring(0,2);	
	    					}else {
	    						video_date = class_start_date_0;
	    						class_start_date_0 = "";
	    					}
	    					class_no = row.getCell(8)==null?"":formatter.formatCellValue(row.getCell(8)).trim();
	    					teacher_id = row.getCell(9)==null?"":formatter.formatCellValue(row.getCell(9)).trim();
	    					class_style = row.getCell(10)==null?"":formatter.formatCellValue(row.getCell(10)).trim();
	    					
	    					if(class_style.equals("2")) {
	    						List<JL_EIP_grade> LJL_EIP_grade = systemService.getJL_EIP_grade(grade_seq,"");
	    						if(LJL_EIP_grade.size()>0) {
	    							subject_id = LJL_EIP_grade.get(0).getSubject_id();
	    						}
	    					}	    					
   					
	    					subjectAbbr = row.getCell(5)==null?"":formatter.formatCellValue(row.getCell(5)).trim();

	    					
	    					if(!subjectAbbr.equals("")) {
	    						subject_id = "";
						    	List<Subject> LSubject = courseService.getSubject("","",subjectAbbr,"","","0");
						    	if(LSubject.size()>0) {
						    		subject_id = LSubject.get(0).getSubject_seq();
						    	}else if(LSubject.size()==0) {
						    		System.out.println("*********不存在subjectAbbr**********"+subjectAbbr);
						    	}
	    					}
	    					
	    						

	    					//*****eip.JL_EIP_grade*****//政龍EIP期別對應
	    					if(!subject_id.equals("")){
	    						jdbcTemplate.update("INSERT INTO eip.JL_EIP_grade VALUES (default,?,?,?,?,?);",
    									grade_seq,
    									JL_gradeId,
    									class_style,
    									subject_id,
    									schoolAbbr
	    						);
	    					}
	    					
			//*****eip.grade*****//學員已訂期別
			//正常實體期別+未存在EIPcode之Video課程
	    		        if(row.getCell(11)==null || formatter.formatCellValue(row.getCell(11))=="") { //是否存在堂數		
	    		        	status_code = "5"; //目前全預設為下架
	    		        }else {
	    		        	status_code = "4";
	    		        }	
						String VideoExistEIPCode = "Y";
						if(class_style.equals("2")) {
							for(int i=0;i<EIPCode.size();i++) {
								if(grade_seq.equals(EIPCode.get(i))) {
									VideoExistEIPCode = "Y";
								}
							}
						}
						if(VideoExistEIPCode.equals("N")) {EIPCode.add(grade_seq);} //加入已存在EIPCode
						if(class_style.equals("1") || VideoExistEIPCode.equals("N")){
							String onSell = "";
							String offSell = "";
    						if(!subject_id.equals("") && !JL_gradeId.equals("") && !grade_seq.equals("") && !schoolAbbr.equals("") && !subjectAbbr.equals("")  && !class_no.equals("") && !teacher_id.equals("") && !class_style.equals("")) {	
    							if(row.getCell(11)!=null && !formatter.formatCellValue(row.getCell(11)).trim().equals("video")) {
    								
								    String[] tokens = formatter.formatCellValue(row.getCell(11)).trim().split("#");
								    if(tokens.length==3) {
									    String[] tokenDate = tokens[0].split("/");
									    String s1 = tokenDate[1].length()==1? "0"+tokenDate[1] : tokenDate[1];
									    String s2 = tokenDate[2].length()==1? "0"+tokenDate[2] : tokenDate[2];
									    String oldDate = tokenDate[0]+"/"+s1+"/"+s2;
							    	 
	    								SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	    								Calendar c = Calendar.getInstance();
	    								c.setTime(sdf.parse(oldDate));
	    								
	    								SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
	    								c.add(Calendar.DAY_OF_MONTH,-60);  
	    						        onSell = sdf2.format(c.getTime());
    								}    
    							}
    							if(row.getCell(13)!=null && !formatter.formatCellValue(row.getCell(13)).trim().equals("video")) {
								    String[] tokenx = formatter.formatCellValue(row.getCell(13)).trim().split("#");
								    if(tokenx.length==3) {
									    String[] tokenDate = tokenx[0].split("/");
									    String s1 = tokenDate[1].length()==1? "0"+tokenDate[1] : tokenDate[1];
									    String s2 = tokenDate[2].length()==1? "0"+tokenDate[2] : tokenDate[2];
									    offSell = s1+"/"+s2+"/"+tokenDate[0];
								    }    
    							}    							

    							List<Grade> LGrade = courseService.getGrade(grade_seq,"","","","","","","200","","","","","");
    							if(LGrade.size()==0) {    							
    								jdbcTemplate.update("INSERT INTO eip.grade VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURDATE(),?,?,?);",
	    									grade_seq,
	    									schoolAbbr,
	    									category_id,
	    									subject_id,
	    									status_code,
	    								    gradeName,
	    								    class_no,
	    								    "",
	    								    class_style,
	    								    "",
	    								    "",
	    								    teacher_id,
	    								    class_start_date_0,
	    								    video_date,
	    								    "1",//早
	    								    "", //TimeFrom
	    								    "", //TimeTo
	    								    "System",
	    								    onSell,
	    								    offSell,
	    								    grade_date
	    						    );
    							}	
	    						    
				//*****eip.classes*****//學員已訂課堂
							    String class_date_1 = "";
							    String timeFrom = "";
							    String timeTo = "";    							
								for(int xth=0;xth<Integer.valueOf(class_no);xth++) { //每堂課
								    int class_trial = 0; //試聽
								    if(xth==0) {class_trial=1;}
								    String class_style_c = "1"; //face

								    String[] tokens = formatter.formatCellValue(row.getCell(11+xth)).trim().split("#");
								    if(tokens.length==3) {
									    String[] tokenDate = tokens[0].split("/");
									    String s1 = tokenDate[1].length()==1? "0"+tokenDate[1] : tokenDate[1];
									    String s2 = tokenDate[2].length()==1? "0"+tokenDate[2] : tokenDate[2];
									    class_date_1 = s1+"/"+s2+"/"+tokenDate[0];
									    if(class_date_1.length()!=10) {System.out.println("日期格式有誤"+class_date_1);}
									    timeFrom = tokens[1];
									    timeTo = tokens[2];								    	
								    }
								    
								    if(row.getCell(11+xth)!=null && formatter.formatCellValue(row.getCell(11+xth)).trim().equals("video")) {
								    	class_style_c = "2"; //video
								    }
									jdbcTemplate.update("INSERT INTO eip.classes VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?);",
										grade_seq,
										xth+1,
										class_date_1,
										timeFrom,
										timeTo,
										teacher_id,
										class_style_c,
										"",
										"",
										"(政龍)",
										1,
										class_trial,
										""
								    );
									//更新主檔上課時間
									if(xth==0 && !timeFrom.isEmpty()) {
							    	    jdbcTemplate.update("Update eip.grade set timeFrom=?,timeTo=? where grade_seq=?;",
							    	    		timeFrom,
							    	    		timeTo,
							    	    		grade_seq
							    		);						
									}									
								}
								
								//*****eip.videoOpen*****//
								List<School> LSchool = accountService.getSchool("","");
								for(int a=0;a<LSchool.size();a++) {
	    							jdbcTemplate.update("INSERT INTO eip.videoOpen VALUES (default,?,?);",
	    									grade_seq,
	    							        LSchool.get(a).getCode()
	    							);
								}
    						}
						}						
    				  }//每列
    		        }catch(Exception e1) {
    		        	System.out.println("row----------"+rowNoFlag);
    		        	e1.printStackTrace();
    		        }	    				
    				 wb.close();
    				 System.out.println("#######期別對應完成######");
    			  }//每個頁籤		
    	        }	  	            	        
    	    }
        }catch(Exception e) {
        	e.printStackTrace();
        }
	    Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		System.out.println("----------結束結束結束結束-------------------------------------------"+ft.format(dNow));     	
    	return true;
    }	

    public Boolean ComboBuild() {
    	jdbcTemplate.update("delete from eip.comboSale;");
    	jdbcTemplate.update("delete from eip.comboSale_subject;");
    	List<Subject> LSubject = courseService.getSubject("","","","","","0"); //active 0,1 全抓取
    	int FlowStatus_code = -1;
    	for(int i=0;i<LSubject.size();i++) {
    		//*****eip.comboSale*****//
    		FlowStatus_code = 4;
    		if(LSubject.get(i).getActive().equals("0")) {
    			FlowStatus_code = 5;
    		}
			jdbcTemplate.update("INSERT INTO eip.comboSale VALUES (default,?,?,?,?,?,?,?,?,?,CURDATE(),?,?,?);",
					0, //isCombo
					LSubject.get(i).getCategory_id(), //category_id
					FlowStatus_code,
					"",//name
					LSubject.get(i).getPrice(),//origin_price
					LSubject.get(i).getPrice(),//sale_price
					0, //class_makeup
					"",//remark
					"System",
					"1", //class_style
					"", //schedule_time
					"" //originSubjectName
			);
			int x  = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
			//*****eip.comboSale_subject*****//
			jdbcTemplate.update("INSERT INTO eip.comboSale_subject VALUES (default,?,?);",
					x, //comboSale_id
					LSubject.get(i).getSubject_seq() //subject_id
			);			
    	}
    	System.out.println("---------- EIP 由科目建立單科-----結束");
        return true;
    }
    
    public boolean eip_register() {
    	System.out.println("------------------------");
    	System.out.println("EIP 報名+訂期別+點名單");
    	System.out.println("DELETE eip.Register,eip.Register_comboSale,eip.Register_comboSale_grade,eip.signRecordHistory");
		jdbcTemplate.update("delete from eip.Register;");
		jdbcTemplate.update("ALTER TABLE eip.Register AUTO_INCREMENT = 1;");
		jdbcTemplate.update("delete from eip.Register_comboSale;");
		jdbcTemplate.update("ALTER TABLE eip.Register_comboSale AUTO_INCREMENT = 1;");
		jdbcTemplate.update("delete from eip.Register_comboSale_grade;");
		jdbcTemplate.update("ALTER TABLE eip.Register_comboSale_grade AUTO_INCREMENT = 1;");
		jdbcTemplate.update("delete from eip.signRecordHistory;");
		jdbcTemplate.update("ALTER TABLE eip.signRecordHistory AUTO_INCREMENT = 1;");    	

		List<JLM_gradeRegister> LJLM_gradeRegister = systemService.getJLM_gradeRegister("","1"); //政龍報名資料,不限數量

			for(int j=0;j<LJLM_gradeRegister.size();j++) {			  	
			  List<JL_EIP_grade> LJL_EIP_grade = systemService.getJL_EIP_grade("",LJLM_gradeRegister.get(j).getGradeId()); //對應期別		  
			  if(LJL_EIP_grade.size()>0) {
				    JL_EIP_grade jL_EIP_grade = LJL_EIP_grade.get(0);
					String subject_id = jL_EIP_grade.getSubject_id();
					String student_seq = "";
					String comboSale_seq = "";
					String orderStatus = "";
					String originPrice = "0";
					int actualPrice = 0;
					int paid = 0;
					
				    JLM_studentPay jLM_studentPay = new JLM_studentPay();
				    List<JLM_studentPay> LJLM_studentPay0 = salesService.getJLM_studentPay("","","",LJLM_gradeRegister.get(j).getGradeId());
				    if(LJLM_studentPay0.size()>0) {
				    	jLM_studentPay = LJLM_studentPay0.get(0);
						originPrice = jLM_studentPay.getOriginPrice();
						actualPrice = Integer.valueOf(jLM_studentPay.getNeedPay());
						paid =  Integer.valueOf(jLM_studentPay.getPaidMoney().equals("")?"0":jLM_studentPay.getPaidMoney());				    	
				    }
				    student_seq = salesService.getStudent("","","",LJLM_gradeRegister.get(j).getStudent_no(),"","","","","","").get(0).getStudent_seq(); 					
					comboSale_seq = courseSaleService.getComboSaleBySubject("0",subject_id).get(0).getComboSale_seq();

					if(paid>=actualPrice) {
						orderStatus = "2";//已結清
					}else {
						orderStatus = "1";
					}
				  if(jL_EIP_grade.getClass_style().equals("1") || jL_EIP_grade.getClass_style().equals("2")) {	//面授及Video	
							//----eip.Register----//
							jdbcTemplate.update("INSERT INTO eip.Register VALUES (default,?,?,?,?,?,?,?,'',0);",
								student_seq,
								originPrice,
								actualPrice,
								paid,
								"政龍", //comment
								jLM_studentPay.getSalePerson(), //creater
								orderStatus 
							);
							int x = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
							//----eip.Register_comboSalee----//
							jdbcTemplate.update("INSERT INTO eip.Register_comboSale VALUES (default,?,?,?,?,?,?,?,?,?);",
								x, //Register_id
								comboSale_seq, //comboSale_id
								subject_id, //subject_id
								0, //gradeNoLeft
								"", //subject_id_virtual
								"", //replaceFrom
								"", //from_Register_comboSale_id
								1, //active
								actualPrice //costShare
							);					
							int y = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
							//----eip.Register_comboSale_grade----//
				    		jdbcTemplate.update("INSERT INTO eip.Register_comboSale_grade VALUES (default,?,?,?,?,?,?,?);",
			   					y, //Register_comboSale_id
			   					jL_EIP_grade.getEip_grade_seq(), //grade_id
			   					1, //register_status
			   					jL_EIP_grade.getClass_style(), //class_style
			   					jL_EIP_grade.getSchoolAbbr(),//school_code
			   					jLM_studentPay.getSitNo(), //sitNo
			   					1
				   			);
				    		int z = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
		                    //----eip.signRecordHistory----//
					    		List<Grade> LGrade = courseService.getGradeList("","",jL_EIP_grade.getEip_grade_seq(),"","","","","","","1","","","","","","","","1","");
					    		if(LGrade.size()>0) { //舊得無法有上課紀錄
						    		for(int k=0;k<Integer.valueOf(LGrade.get(0).getClass_no());k++) {
							    		jdbcTemplate.update("INSERT INTO eip.signRecordHistory VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
											z, //Register_comboSale_grade_id
											1, //register_status
											student_seq, //student_id
											jL_EIP_grade.getClass_style(), //class_style
											LGrade.get(0).getSchool_code(), //school_code
											LGrade.get(0).getGrade_seq(), //grade_id
											k+1, //class_th
											0, //attend
											1, //active
											"", //attend_date
											-99, //slot
											"", //update_time
											"System", //updater
											0, //makeUpNo,
											"", //class_th_ex
											"",
											"",
											"",
											1
									    );
						    		}
					    		}	
				   }else if(jL_EIP_grade.getClass_style().equals("0")) {	//未訂
							//----eip.Register----//
							jdbcTemplate.update("INSERT INTO eip.Register VALUES (default,?,?,?,?,?,?,?,'',0);",
								student_seq,
								originPrice, //originPrice
								actualPrice, //actualPrice
								paid, //paid
								"政龍", //comment
								jLM_studentPay.getSalePerson(), //creater
								-1 //orderStatus 
							);
							int x = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
							//----eip.Register_comboSalee----//
							Subject subject = courseService.getSubject("",subject_id,"","","","0").get(0);
							jdbcTemplate.update("INSERT INTO eip.Register_comboSale VALUES (default,?,?,?,?,?,?,?,?,?);",
								x, //Register_id
								comboSale_seq, //comboSale_id
								subject_id, //subject_id
								"-99", //gradeNoLeft
								"", //subject_id_virtual
								"", //replaceFrom
								"", //from_Register_comboSale_id
								1, //active 
								actualPrice //costShare
							);					   
				   }
				}					
			}	  
	    Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		System.out.println("---------- EIP 報名+訂期別+點名單-----結束-------------------------------------------"+ft.format(dNow));		
    	return true;	
    }
    
    
    public boolean eip_register2() {
    	System.out.println("------------------------");
    	System.out.println("EIP2 報名+訂期別+點名單");
    	System.out.println("DELETE eip.Register,eip.Register_comboSale,eip.Register_comboSale_grade,eip.signRecordHistory");
		jdbcTemplate.update("delete from eip.Register;");
		jdbcTemplate.update("ALTER TABLE eip.Register AUTO_INCREMENT = 1;");		
		jdbcTemplate.update("delete from eip.studentPayRecord;");
		jdbcTemplate.update("ALTER TABLE eip.studentPayRecord AUTO_INCREMENT = 1;");		
		jdbcTemplate.update("delete from eip.Register_comboSale;");
		jdbcTemplate.update("ALTER TABLE eip.Register_comboSale AUTO_INCREMENT = 1;");
		jdbcTemplate.update("delete from eip.Register_comboSale_grade;");
		jdbcTemplate.update("ALTER TABLE eip.Register_comboSale_grade AUTO_INCREMENT = 1;");
		jdbcTemplate.update("delete from eip.signRecordHistory;");
		jdbcTemplate.update("ALTER TABLE eip.signRecordHistory AUTO_INCREMENT = 1;");    	

		List<JLM_gradeRegister> LJLM_gradeRegister = systemService.getJLM_gradeRegister("","1"); //政龍報名資料,不限數量
		  String salePerson="";
		  for(int j=0;j<LJLM_gradeRegister.size();j++) {
			if(!LJLM_gradeRegister.get(j).getSitNo().contains("不上了")) {	
			  List<JL_EIP_grade> LJL_EIP_grade = systemService.getJL_EIP_grade("",LJLM_gradeRegister.get(j).getGradeId()); //對應期別		  
			  salePerson = LJLM_gradeRegister.get(j).getSalePerson();
			     if(LJL_EIP_grade.size()>0) {
				    JL_EIP_grade jL_EIP_grade = LJL_EIP_grade.get(0);
					String subject_id = jL_EIP_grade.getSubject_id();
					String student_seq = "";
					String comboSale_seq = "";
					String orderStatus = "";
					String originPrice = "0";
					int actualPrice = 0;
					int paid = 0;
					
				    JLM_studentPay jLM_studentPay = new JLM_studentPay();
				    //繳費紀錄
				    List<JLM_studentPay> LJLM_studentPay0 = salesService.getJLM_studentPay(LJLM_gradeRegister.get(j).getStudent_no(),"","",LJLM_gradeRegister.get(j).getGradeId());
				    if(LJLM_studentPay0.size()>0) {
				    	jLM_studentPay = LJLM_studentPay0.get(0);
						originPrice = jLM_studentPay.getOriginPrice();
						actualPrice = Integer.valueOf(jLM_studentPay.getNeedPay());
						paid =  Integer.valueOf(jLM_studentPay.getPaidMoney().equals("")?"0":jLM_studentPay.getPaidMoney());				    	
				    }
				    student_seq = salesService.getStudent("","","",LJLM_gradeRegister.get(j).getStudent_no(),"","","","","","").get(0).getStudent_seq(); 					
					comboSale_seq = courseSaleService.getComboSaleBySubject("0",subject_id).get(0).getComboSale_seq();

			  		if(actualPrice==0 && paid==0) {
			  			orderStatus = "-1";//不適用
			  		}else if(paid>=actualPrice) {
						orderStatus = "2";//已結清
					}else {
						orderStatus = "1";//未結清
					}
					
				  if(jL_EIP_grade.getClass_style().equals("1") || jL_EIP_grade.getClass_style().equals("2")) {	//面授及Video	
							//----eip.Register----//
							jdbcTemplate.update("INSERT INTO eip.Register VALUES (default,?,?,?,?,?,?,?,'',0);",
								student_seq,
								originPrice,
								actualPrice,
								paid,
								"", //comment
								salePerson, //creater
								orderStatus 
							);
							int x = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
							
							//----eip.studentPayRecord----//
							jdbcTemplate.update("INSERT INTO eip.studentPayRecord VALUES (default,?,?,?,?,?,?,?,?);",
									x, //register_id
									paid,
									actualPrice,
									salePerson,
									-1, //receiptNo
									"", //payDate
									"", //school_code
									1
							);								
							//----eip.Register_comboSalee----//
							jdbcTemplate.update("INSERT INTO eip.Register_comboSale VALUES (default,?,?,?,?,?,?,?,?,?);",
								x, //Register_id
								comboSale_seq, //comboSale_id
								subject_id, //subject_id
								0, //gradeNoLeft
								"", //subject_id_virtual
								"", //replaceFrom
								"", //from_Register_comboSale_id
								1, //active
								actualPrice //costShare
							);					
							int y = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
							//----eip.Register_comboSale_grade----//
				    		jdbcTemplate.update("INSERT INTO eip.Register_comboSale_grade VALUES (default,?,?,?,?,?,?,?);",
			   					y, //Register_comboSale_id
			   					jL_EIP_grade.getEip_grade_seq(), //grade_id
			   					1, //register_status
			   					jL_EIP_grade.getClass_style(), //class_style
			   					jL_EIP_grade.getSchoolAbbr(),//school_code
			   					jLM_studentPay.getSitNo(), //sitNo
			   					1
				   			);
				    		int z = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
		                    //----eip.signRecordHistory----//
					    		List<Grade> LGrade = courseService.getGradeList("","",jL_EIP_grade.getEip_grade_seq(),"","","","","","","1","","","","","","","","1","");
					    		if(LGrade.size()>0) { //舊的無法有上課紀錄
						    		for(int k=0;k<Integer.valueOf(LGrade.get(0).getClass_no());k++) {
							    		jdbcTemplate.update("INSERT INTO eip.signRecordHistory VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
											z, //Register_comboSale_grade_id
											1, //register_status
											student_seq, //student_id
											jL_EIP_grade.getClass_style(), //class_style
											LGrade.get(0).getSchool_code(), //school_code
											LGrade.get(0).getGrade_seq(), //grade_id
											k+1, //class_th
											0, //attend
											1, //active
											"", //attend_date
											-99, //slot
											"", //update_time
											"System", //updater
											0, //makeUpNo,
											"", //class_th_ex
											"",
											"",
											"",
											1
									    );
						    		}
					    		}	
				   }else if(jL_EIP_grade.getClass_style().equals("0")) {	//未訂
							//----eip.Register----//
							jdbcTemplate.update("INSERT INTO eip.Register VALUES (default,?,?,?,?,?,?,?,'',0);",
									student_seq,
									originPrice, //originPrice
									actualPrice, //actualPrice
									paid, //paid
									"政龍未定", //comment
									salePerson, //creater
									orderStatus //orderStatus 
							);
							int x = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
							
							//----eip.studentPayRecord----//
							jdbcTemplate.update("INSERT INTO eip.studentPayRecord VALUES (default,?,?,?,?,?,?,?,?);",
									x, //register_id
									paid,
									actualPrice,
									salePerson,
									-1, //receiptNo
									"", //payDate
									"", //school_code
									1
							);	
							
							//----eip.Register_comboSalee----//							
							//判斷是否政龍未定班已新增期別
							String sql = "select count(*) from eip.register a, eip.Register_comboSale b where a.Register_seq=b.Register_id and a.student_seq='"+student_seq+"' and b.subject_id="+subject_id;							
							int y = jdbcTemplate.queryForObject(sql, Integer.class);
							String gradeNoLeft = "";
							if(y>0) {
								gradeNoLeft = "-1";
							}else {
								Subject subject = courseService.getSubject("",subject_id,"","","","0").get(0);	
								gradeNoLeft = "-99";
							}

							jdbcTemplate.update("INSERT INTO eip.Register_comboSale VALUES (default,?,?,?,?,?,?,?,?,?);",
									x, //Register_id
									comboSale_seq, //comboSale_id
									subject_id, //subject_id
									gradeNoLeft, 
									"", //subject_id_virtual
									"", //replaceFrom
									"", //from_Register_comboSale_id
									1, //active 
									actualPrice //costShare
							);								
				   }
				}					
			}	  
		  }
	    Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		System.out.println("---------- EIP 報名2+訂期別+點名單-----結束-------------------------------------------"+ft.format(dNow));	
		System.out.println("---------- EIP 報名2+訂期別+點名單-----結束-------------------------------------------"+ft.format(dNow));		
		System.out.println("---------- EIP 報名2+訂期別+點名單-----結束-------------------------------------------"+ft.format(dNow));		
    	return true;	
    } 

    
    @Value("${ExcelUploadPath}") String ExcelUploadPath;
    public void upload2(String passArea) throws IOException {	   
    	List<String> LExcelUploadPath = new ArrayList<>();
    	
    if(passArea.equals("0")) {
    	   LExcelUploadPath.add(ExcelUploadPath+"/");
    }else if(passArea.equals("1") || passArea.equals("999")) {   
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/CFA/L1/");		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/TOEFL/TFD/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/TOEFL/TFJJ/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/TOEFL/TFL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/TOEFL/TFR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/TOEFL/TFS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/TOEFL/TFSD/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/TOEFL/TFW/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/GMAT/ATAWA/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/GMAT/ATCR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/GMAT/ATD/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/GMAT/ATDSC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/GMAT/ATIR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/GMAT/ATQ/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/GMAT/ATRC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/GMAT/ATSC/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/GRE/REAW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/GRE/RECR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/GRE/RED/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/GRE/REQ/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/GRE/RERC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/GRE/RETC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/GRE/REV/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/IELTS/ISL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/IELTS/ISR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/IELTS/ISS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/IELTS/ISW/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/TOEIC/TC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/TOEIC/TC/不存在/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/TOEIC/TCL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/TOEIC/TCL/不存在/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/TOEIC/TCR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/TOEIC/TCR/不存在/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/TOEIC/TCSW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/TOEIC/TCSW/不存在/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/Wordgod/ADS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/Wordgod/PAPER/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/Wordgod/PREG/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/Wordgod/PREG(K)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/Wordgod/PREG(P)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/Wordgod/PREL(M)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/Wordgod/PREL(O)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/Wordgod/PRER(P)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/Wordgod/PRES/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/Wordgod/PREW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/Wordgod/TW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018正班點名單/Wordgod/V/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/台中2018 正班點名表/IELTS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/台中2018 正班點名表/TOEFL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/台中2018 正班點名表/TOEIC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/台中2018 正班點名表/WORDGOD/");		   
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/高雄2018年正班/IELTS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/高雄2018年正班/TOEFL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/高雄2018年正班/TOEIC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/高雄2018年正班/Wordgod/");
		   	   
		   
    }else if(passArea.equals("2") || passArea.equals("999")) {    		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEFL/TFJJ/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEFL/TFJJ/error/");    	
 		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEFL/TFL/");
 		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEFL/TFL/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEFL/TFR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEFL/TFR/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEFL/TFS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEFL/TFS/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEFL/TFSD/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEFL/TFSD/error/");	
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEFL/TFW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEFL/TFW/error/");		   
   
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEIC/TC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEIC/TC/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEIC/TCL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEIC/TCL/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEIC/TCR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEIC/TCR/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEIC/TCSW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/TOEIC/TCSW/error/");	
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GMAT/ATAWA/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GMAT/ATAWA/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GMAT/ATCR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GMAT/ATCR/error/");		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GMAT/ATD/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GMAT/ATD/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GMAT/ATIR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GMAT/ATIR/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GMAT/ATQ/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GMAT/ATQ/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GMAT/ATRC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GMAT/ATRC/error/");	
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GMAT/ATSC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GMAT/ATSC/error/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GRE/REAW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GRE/REAW/error/");		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GRE/RECR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GRE/RECR/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GRE/RED/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GRE/RED/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GRE/REQ/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GRE/REQ/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GRE/RERC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GRE/RERC/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GRE/RETC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GRE/RETC/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GRE/REV/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/GRE/REV/error/");	
		   		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/IELTS/ISL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/IELTS/ISL/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/IELTS/ISR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/IELTS/ISR/error/");	
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/IELTS/ISS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/IELTS/ISS/error/");	
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/IELTS/ISW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/IELTS/ISW/error/");	
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/Wordgod/ADS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/Wordgod/BULS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/Wordgod/BURW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/Wordgod/OOOB/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/Wordgod/OOON/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/Wordgod/PAPER/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/Wordgod/PREG/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/Wordgod/PREG(K)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/Wordgod/PREL(M)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/Wordgod/PREL(O)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/Wordgod/PRER(P)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/Wordgod/PRES/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/Wordgod/PREV/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/Wordgod/PREW(P)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019正班點名單/Wordgod/V/");
		   		   
		   LExcelUploadPath.add(ExcelUploadPath+"/台中/正班點名表/IELTS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/台中/正班點名表/TOEFL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/台中/正班點名表/TOEIC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/台中/正班點名表/好神/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/高雄/正班/GRE/");
		   LExcelUploadPath.add(ExcelUploadPath+"/高雄/正班/IS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/高雄/正班/TC/TC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/高雄/正班/TC/TCSW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/高雄/正班/TF/TFL(A)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/高雄/正班/TFR(A)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/高雄/正班/TFR(S)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/高雄/正班/TFS(M)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/高雄/正班/TFW(W)/");		   
		   LExcelUploadPath.add(ExcelUploadPath+"/高雄/正班/WG/PRES/");
		   LExcelUploadPath.add(ExcelUploadPath+"/高雄/正班/WG/PREW/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/士林/2019年正班點名條/ISL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/士林/2019年正班點名條/ISR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/士林/2019年正班點名條/ISS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/士林/2019年正班點名條/ISW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/士林/2019年正班點名條/PREG/");
		   LExcelUploadPath.add(ExcelUploadPath+"/士林/2019年正班點名條/PREL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/士林/2019年正班點名條/PRES/");
		   LExcelUploadPath.add(ExcelUploadPath+"/士林/2019年正班點名條/PREW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/士林/2019年正班點名條/TCL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/士林/2019年正班點名條/TCR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/士林/2019年正班點名條/TFL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/士林/2019年正班點名條/TFR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/士林/2019年正班點名條/TFS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/士林/2019年正班點名條/TFW/");
    }   
		   


		   	   
 	     String returnMessage;
 	     for(int z=0;z<LExcelUploadPath.size();z++) {
 	       returnMessage = "";	 
 		   try {
 		    File folder = new File(LExcelUploadPath.get(z));
 		    if(folder.exists()) {
 		    	File[] Lfile = folder.listFiles();
 		    	String fileName = "";
 		    	
 		    	for (int i=0;i<Lfile.length;i++) {
 		    		if(Lfile[i].isDirectory()) {
 		    			continue;
 		    		}
 		    		//Step1.判斷期別是否存在
 		    		fileName = Lfile[i].getName();  
 		    		System.out.println("[檔案]"+fileName);
 			        String[] fn = fileName.substring(0,fileName.length()-5).split("-");
 			        String school_code       = fn[0];
 			        String subject_abbr      = fn[1];       
 			        String class_start_date  = fn[2];
 			        String video_date = "";
					if(!class_start_date.equals("") && class_start_date.length()==6) {
						class_start_date = class_start_date.substring(2,4)+"/"+class_start_date.substring(4,6)+"/20"+class_start_date.substring(0,2);
						video_date = "";
					}else {
						video_date = class_start_date; //期別別名
						class_start_date = "";
					} 			        

 			        String teacher_name      = fn[3];
 			        String class_th = "";
 			        String student_no = "";
 			        String attend = "";
 			        String TakeHandout = "";

 			        String grade_id = ""; 			        
 					List<Grade> LGrade = courseService.getGradeList("","","",school_code,"",subject_abbr,class_start_date,"","","1",video_date,teacher_name,"","","","","","1","");
 					if(LGrade.size()>0) {
 						grade_id = LGrade.get(0).getGrade_seq();
 					}else {
 						System.out.println("??? 不存在的期別: "+school_code+"/"+subject_abbr+"/"+fn[2]+"/"+teacher_name);
 						continue;
 					}        
 		    	
 		    		//Step2.處理Excel 
 					InputStream ExcelFileToRead = new FileInputStream(LExcelUploadPath.get(z) + fileName);
 					String updater = "system";
 					XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
 					String student_id = ""; 
 					ArrayList<String> A_attend = new ArrayList<String>();
 					ArrayList<String> A_TakeHandout = new ArrayList<String>();
 					ArrayList<String> A_student_id = new ArrayList<String>();
 					ArrayList<String> A_class_th = new ArrayList<String>();
 					DataFormatter formatter = new DataFormatter();
 		
 			        for (int x=0;x<wb.getNumberOfSheets();x++) {
 			        	class_th = wb.getSheetName(x);
 			        	if(class_th.contains("-")) {
 			        		String[] class_th_x = class_th.split("-");
 			        		class_th     = class_th_x[0];
 			        		String className    = class_th_x[1];
 			        		String class_style  = class_th_x[2];
 			        		if(class_style.equals("正班")) {
 			        			class_style = "1";
 			        		}else if(class_style.equals("VIDEO")) {
 			        			class_style = "2";
 			        		}
 			        		String teacher_name_2 = class_th_x[3];
 			        		String teacher_id = courseService.getTeacher("","","","","","",teacher_name_2).get(0).getTeacher_seq();
 			        		Boolean returnStr = courseService.UpdateClasses(class_th,className,teacher_id,grade_id,class_style);
 			        		if(!returnStr) {						
 								System.out.println("課堂更改有誤: 學生"+student_no+" 課堂"+class_th);
 								wb.close();
 							} 	        		
 			        		
 			        	}
 						XSSFSheet sheet=wb.getSheetAt(x);
 						XSSFRow row;
 						for(int a=1;a<sheet.getPhysicalNumberOfRows();a++) {
 							row=sheet.getRow(a);
 							
 							if(row==null || row.getCell(0)==null || formatter.formatCellValue(row.getCell(0)).isEmpty()) {
 								break; 
 							}else {
 								student_no = formatter.formatCellValue(row.getCell(0));
 							}
 							attend = formatter.formatCellValue(row.getCell(2)); 
 							TakeHandout = formatter.formatCellValue(row.getCell(6)); 					
 							List<Student> LStudent = salesService.getStudent("","","",student_no,"","","","","","");
 							if(LStudent.size()>0) {
 								student_id = LStudent.get(0).getStudent_seq();
 								List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory("",student_id,grade_id,"1","(1)","","","","1",school_code,class_th);
 								//儲存點名紀錄 
 								if(LSignRecordHistory.size()>0) {
 									A_attend.add(attend);
 									A_TakeHandout.add(TakeHandout);
 									A_student_id.add(student_id);
 									A_class_th.add(class_th);	
 								}else {	
 									//System.out.println("(student_id)"+student_id+"(grade_id)"+grade_id+"(school_code)"+school_code+"(class_th)"+class_th);
 									System.out.println("----------------------------------------------------------------------------------------不存在的點名課堂清單: "+fileName+"學生"+student_no+"堂數"+class_th);
								
 								} 
 							}else {	
 								System.out.println("??? 不存在的學生: file"+fileName+"學生"+student_no+"堂數"+class_th);

 							}
 							
 						}	
 			
 			        }	
 			        wb.close();
 			        for(int j=0;j<A_student_id.size();j++) {
 			        	//點名單重複
 						Boolean returnStr = admService.UpdateSignRecord(subject_abbr,school_code,A_class_th.get(j),A_student_id.get(j),A_attend.get(j),A_TakeHandout.get(j),updater,grade_id,"1","",student_no,"","");
 						if(!returnStr) {
 							returnMessage +="資料有誤: 學生"+student_no+" 課堂"+class_th+"<br>";						
 							System.out.println(returnMessage);
 						}        	
 			        }
 		    	}
 		      }	
 		    }catch(Exception e) {
 		            e.printStackTrace();
 		            System.out.println("上傳失敗"+e);
 		    } 		  
 	     } 
 	     System.out.println("---------------------------實體點名結束-----------------------------------------");
   } 
    

    public void uploadVideo2() throws IOException {	
 	   List<String> LExcelUploadPath = new ArrayList<>();
 	   LExcelUploadPath.add(ExcelUploadPath+"video/");
 	   //LExcelUploadPath.add(ExcelUploadPath+"video/2019VIDEO名單/VIDEO點名條/1月/");
 	   
 	   for(int z=0;z<LExcelUploadPath.size();z++) { 
 		    try {
 		    	File folder = new File(LExcelUploadPath.get(z));
 		    	File[] Lfile = folder.listFiles();
 		    	String fileName = "";
 //每檔案		    	
 		    	for (int i=0;i<Lfile.length;i++) {
 		    		if(Lfile[i].isDirectory()) {
 		    			continue;
 		    		}
 		    		//Step1.判斷期別是否存在
 		    		fileName = Lfile[i].getName();  
 		    		System.out.println("-----------------------------------------------------------------------");
 		    		System.out.println(fileName);
 		    		System.out.println("-----------------------------------------------------------------------");
 		        
 			        String[] fn = fileName.substring(0,fileName.length()-5).split("-");
 			        String school_code   = fn[0];
 			        String attend_date   = fn[1];
 			        attend_date = attend_date.substring(4,6)+"/"+attend_date.substring(6,8)+"/"+attend_date.substring(0,4);
 			        String class_th = "";
 			        String student_no = "";
 			        String attend = "";
 			        String TakeHandout = "";
 			        String grade_id = "";
 			        String subject_abbr = "";
 			        String class_start_date0 = "";
 			        String class_start_date = "";
 			        String teacher_id = "-1";
 			        String video_date = "";
 			        
 			             
 					InputStream ExcelFileToRead = new FileInputStream(LExcelUploadPath.get(z)+fileName);
 					String updater = "System";
 					XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
 					String student_id = ""; 
 					ArrayList<String> A_attend = new ArrayList<String>();
 					ArrayList<String> A_student_id = new ArrayList<String>();
 					ArrayList<String> A_student_no = new ArrayList<String>();
 					ArrayList<String> A_class_th = new ArrayList<String>();
 					ArrayList<String> A_TakeHandout = new ArrayList<String>();
 					ArrayList<String> A_grade_id = new ArrayList<String>();
 					DataFormatter formatter = new DataFormatter();
 					int SomeError = 0;
 //每頁籤
 					for (int x=0;x<wb.getNumberOfSheets();x++) {
 				      if(!wb.getSheetName(x).contains("不吃")) {		
 						XSSFSheet sheet=wb.getSheetAt(x);
 						XSSFRow row;
 //每列						
 						for(int a=1;a<sheet.getPhysicalNumberOfRows();a++) {
 							SomeError = 0;
 							row=sheet.getRow(a);
 					
 							if(row==null || row.getCell(0)==null || formatter.formatCellValue(row.getCell(0)).isEmpty()) {
 								break; 
 							}
 							student_no = formatter.formatCellValue(row.getCell(0)).trim();						
 							subject_abbr = formatter.formatCellValue(row.getCell(4)).trim(); 
 							class_start_date0 = formatter.formatCellValue(row.getCell(3)).trim(); 
 							List<Teacher> LTeacher = courseService.getTeacher("","","","","","",formatter.formatCellValue(row.getCell(6)));
 							if(LTeacher.size()==0) {
 								System.out.println(a+1+"~> 不存在的老師 : "+formatter.formatCellValue(row.getCell(6)));								
 							}else {	
 								teacher_id = LTeacher.get(0).getTeacher_seq();
 							
 								List<Grade> LGrade = new ArrayList<Grade>();
 								if(class_start_date0.length()==6) {
 									class_start_date = class_start_date0.substring(2,4)+"/"+class_start_date0.substring(4,6)+"/"+"20"+class_start_date0.substring(0,2);
 									LGrade = courseService.getGradeList(teacher_id,"","",school_code,"",subject_abbr,class_start_date,"","","1","","","","","","","","1","");
 								}else {
 									if(class_start_date0.length()>1 && class_start_date0.length()<6) { //期別別名
 										video_date = class_start_date0;
 										LGrade = courseService.getGradeList(teacher_id,"","",school_code,"",subject_abbr,"","","","1",video_date,"","","","","","","1","");
 									}	
 								}
 								if(LGrade.size()==0) {
	 									//拿掉school再判斷一次								
	 	 								if(class_start_date0.length()==6) {
	 	 									class_start_date = class_start_date0.substring(2,4)+"/"+class_start_date0.substring(4,6)+"/"+"20"+class_start_date0.substring(0,2);
	 	 									LGrade = courseService.getGradeList(teacher_id,"","","","",subject_abbr,class_start_date,"","","1","","","","","","","","1","");
	 	 								}else {
	 	 									if(class_start_date0.length()>1 && class_start_date0.length()<6) { //期別別名
	 	 										video_date = class_start_date0;
	 	 										LGrade = courseService.getGradeList(teacher_id,"","","","",subject_abbr,"","","","1",video_date,"","","","","","","1","");
	 	 									}	
	 	 								}
	 	 							
	 	 								if(LGrade.size()==0) {
	 	 										System.out.println(a+1+"~> 不存在的期別 : "+subject_abbr+" "+class_start_date0+" "+formatter.formatCellValue(row.getCell(6)));
	 	 								}		
 								}
 								
 								if(LGrade.size()==1){
 									grade_id = LGrade.get(0).getGrade_seq(); 
 									List<Student> LStudent = salesService.getStudent("","","",student_no,"","","","","","");
 									if(LStudent.size()==0) {
 										System.out.println(a+1+"~> 不存在的學生 : "+student_no);  										
 									}else {	
 										student_id = LStudent.get(0).getStudent_seq();
 										//判定是否此期別存在於學生已訂期別中
 										/**
 								    	List<Register_comboSale> LRegister_comboSale = salesService.getComboSaleByRegister("","",student_id,"","","1",false,false,"");
 									    for(int b=0;b<LRegister_comboSale.size();b++) { 
 											List<Register_comboSale_grade> LRegister_comboSale_grade = courseService.getRegister_comboSale_grade(LRegister_comboSale.get(b).getRegister_comboSale_seq(),"","(1)","");			
 											for(int j=0;j<LRegister_comboSale_grade.size();j++) {
 												for(int k=0;k<LGrade.size();k++) {
 													if(LRegister_comboSale_grade.get(j).getGrade_id().equals(LGrade.get(k).getGrade_seq())) {
 														grade_id = LGrade.get(k).getGrade_seq();
 													}
 												}		
 											}						
 									    }
 									    **/	
 							        }  
 							
 									class_th    = formatter.formatCellValue(row.getCell(5)).trim();
 									TakeHandout = formatter.formatCellValue(row.getCell(8)).trim();
 									attend      = formatter.formatCellValue(row.getCell(9)).trim();
 									
 									//第0堂
 									if(class_th.isEmpty() || class_th.trim().replaceAll("\\s+","").equals("0")) {
 										System.out.println(a+1+"~> 堂數有誤 : "+student_no+" 科目:"+class_start_date+" "+subject_abbr+" 課堂"+class_th);
 										SomeError = 1;
 									}else {																
 								        //不用管到原來實體的school_code
 										List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory("",student_id,grade_id,"1","(1)","","","","","",class_th);
						
 										//準備儲存點名紀錄 
 										if(LSignRecordHistory.size()>0 && SomeError ==0 ) {
 											//檢查點名紀錄(此堂課class_th)是實體,則新增一筆Video (補課?)
 											int existVideo = 0;
 											for(int h=0;h<LSignRecordHistory.size();h++) {
 												if(LSignRecordHistory.get(h).getClass_style().equals("2")) {
 													existVideo = 1;
 												}
 											}
 											if(existVideo==0) {
 												//System.out.println(a+1+"~> 只存在實體訂班,系統自行新增一筆Video");
 												admService.addVideoRecord(LSignRecordHistory.get(0),school_code);
 											}
 											admService.UpdateSignRecord(subject_abbr,school_code,class_th,student_id,attend,TakeHandout,updater,grade_id,"2",attend_date,student_no,String.valueOf(a+1),"");      											
 										}else {	
 											System.out.println(a+1+"~> 此學生不存在於點名課堂清單: 學號:"+student_no+" 科目:"+class_start_date+" "+subject_abbr);												
 										}
 									}	
 								}
 						     }	
 			              } //每列
 				       }
 					}//每頁籤
 				    wb.close();
 		    	} //每檔案  		    	
 		    }catch(Exception e) {
 		    	e.printStackTrace();
 		    }		    
 	   }
 	  System.out.println("---------------------------Video點名結束-----------------------------------------");
    }
}
