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
public class SystemService {
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
		jdbcTemplate.update("update eip.category set bgcolor=?,ranks=? where category_seq=?",
					category.getBgColor(),
					category.getRanks(),
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
			sql +=" order by gradeId desc"; //?????????????????????????????????, ?????????????????????
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
		String sql = "SELECT * from eip.scheduleTask where 1=1";
		if (FlowStatus != null && !FlowStatus.isEmpty()) {
			sql += " and FlowStatus in "+FlowStatus;
		}
		
		sql += " limit 1000";

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
    	System.out.println("JLM1[????????????] [????????????] [??????] [????????????] ?????? ");      	
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
	    		result.getString("??????"), //student_no
	    		result.getString("??????"), //ch_name
	    		result.getString("????????????"), //en_name
	    		result.getString("??????"),//sex
	    		result.getString("????????????1"), //category
	    		result.getString("????????????2"), //derived
	    		result.getString("?????????"), //idn
	    		result.getString("??????"), //birthday
	    		result.getString("??????"), //identity
	    		result.getString("??????1"), //tel
	    		result.getString("????????????"), //mobile_1
	    		"", //mobile_2
	    		result.getString("Email"), //email_1
	    		result.getString("Email2"), //email_2	    		
	    		result.getString("??????"), //address_1
	    		result.getString("??????2"), //address_2
	    		"", //fb
	    		"", //line	    		
	    		result.getString("??????"), //parent_1_name
	    		result.getString("????????????"), //parent_1_mobile
	    		"", //parent_1_email
	    		"", //parent_1_line
	    		result.getString("????????????"), //parent_2_name
	    		result.getString("????????????"), //parent_2_mobile
	    		"", //parent_2_email
	    		"", //parent_2_line
	    		result.getString("??????????????????"),//creater
	    		result.getString("????????????").substring(0,10), //create_time	    		 
	    		"", //editor
	    		result.getString("??????"), //remark
	    		result.getString("??????2"), //remark
	    		result.getString("????????????"), //special_idn
	    		result.getString("????????????3"), //exam
	    		result.getString("????????????4"), //abroad_date
	    		"0",
	    		"0",
	    		"",
	    		"",
	    		"",
	    		result.getString("????????????"),//updater
	    		result.getString("????????????"), //update_time     		 		
	    		result.getString("????????????5"), //col_5
	    		"",
	    		"",
	    		"",
	    		result.getString("??????2"), //tel2
	    		result.getString("????????????"), //company_tel
	    		result.getString("???????????????"), //agent_studentNo
	    		result.getString("????????????"), //grade_highSchool
	    		result.getString("????????????"), //postCode
	    		result.getString("???????????????"), //agent_studentName
	    		"", //passwd
	    		result.getString("????????????6"), //col_6
	    		result.getString("????????????7"), //col_7
	    		result.getString("????????????8"), //col_8
	    		result.getString("????????????9"), //col_9
	    		"1", //degree
	    		"", //cowork
	    		"",
	    		""
               )); 
		
		for(int i=0;i<LStudent.size();i++) {
			salesService.StudentSave("1",LStudent.get(i),"System","",""); //????????????,??????
			salesService.StudentRegisterSave(LStudent.get(i).getStudent_no()); //????????????
			salesService.StudentPaySave(LStudent.get(i).getStudent_no()); //??????
			salesService.StudentPayRecordSave(LStudent.get(i).getStudent_no()); //????????????
		}
		
	    Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		System.out.println("----------migration1??????-------------------------------------------"+ft.format(dNow)); 		
		return true;
	}
	@Value("${UploadPath}")
	private String UploadPath;
    public boolean Migration2(){
    	System.out.println("????????????");
      try {	
		        Connection conn = DriverManager.getConnection("jdbc:sqlserver://192.168.0.240:1433;databaseName=JLImage","sa","0080");       
				String sql = "SELECT  ??????,?????? FROM [JLImage].[dbo].[????????????]";
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
		System.out.println("----------migration2??????-------------------------------------------"+ft.format(dNow)); 		
		return true;        

	}  	

    
    public Boolean Migration3() {
    	
	    	jdbcTemplate.update("delete from eip.JLM_gradeAll;");
	    	
	        DriverManagerDataSource dataSource=new DriverManagerDataSource();
	        dataSource.setUrl("jdbc:sqlserver://192.168.0.240:1433;databaseName=JLM1");
	        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        dataSource.setUsername("sa");
	        dataSource.setPassword("0080"); 
	        JdbcTemplate jdbcTemplate_JLM=new JdbcTemplate(dataSource);
	
	        /**??????**/        
			String sql = "select ??????,??????,???????????????,??????????????? from JLM1.dbo.?????? where ?????? like '%??????%'"; 
			
			List<JLM_gradeAll> LJLM_gradeAll = jdbcTemplate_JLM.query(sql,(result,rowNum)->new JLM_gradeAll(
		    		"", //gradeAll_seq
		    		"2", //category_id ??????
		    		"",
		    		result.getString("??????").trim(), //gradeId
		    		result.getString("??????"), //gradeName
		    		result.getString("???????????????"),//dateFrom
		    		result.getString("???????????????"), //dateTo
		    		""
	        )); 
			
			for(int i=0;i<LJLM_gradeAll.size();i++) {
				salesService.JLM_gradeAllSave(LJLM_gradeAll.get(i));
			}
			/**??????**/ 
			String sql2 = "select ??????,??????,???????????????,??????????????? from JLM1.dbo.?????? where ?????? like '%??????%'";
	
			List<JLM_gradeAll> LJLM_gradeAll2 = jdbcTemplate_JLM.query(sql2,(result,rowNum)->new JLM_gradeAll(
		    		"", //gradeAll_seq
		    		"6", //category_id ??????
		    		"",
		    		result.getString("??????").trim(), //gradeId
		    		result.getString("??????"), //gradeName
		    		result.getString("???????????????"),//dateFrom
		    		result.getString("???????????????"), //dateTo
		    		""
	        )); 
			
			for(int i=0;i<LJLM_gradeAll2.size();i++) {
				salesService.JLM_gradeAllSave(LJLM_gradeAll2.get(i));
			}
			
			/**GRE**/ 
			String sql3 = "select ??????,??????,???????????????,??????????????? from JLM1.dbo.?????? where ?????? like '%GRE%'";
	
			List<JLM_gradeAll> LJLM_gradeAll3 = jdbcTemplate_JLM.query(sql3,(result,rowNum)->new JLM_gradeAll(
		    		"", //gradeAll_seq
		    		"4", //category_id GRE
		    		"",
		    		result.getString("??????").trim(), //gradeId
		    		result.getString("??????"), //gradeName
		    		result.getString("???????????????"),//dateFrom
		    		result.getString("???????????????"), //dateTo
		    		""
	        )); 
			
			for(int i=0;i<LJLM_gradeAll3.size();i++) {
				salesService.JLM_gradeAllSave(LJLM_gradeAll3.get(i));
			}
			
			/**??????**/ 
			String sql4 = "select ??????,??????,???????????????,??????????????? from JLM1.dbo.?????? where ?????? like '%??????%'";
	
			List<JLM_gradeAll> LJLM_gradeAll4 = jdbcTemplate_JLM.query(sql4,(result,rowNum)->new JLM_gradeAll(
		    		"", //gradeAll_seq
		    		"5", //category_id ??????
		    		"",
		    		result.getString("??????").trim(), //gradeId
		    		result.getString("??????"), //gradeName
		    		result.getString("???????????????"),//dateFrom
		    		result.getString("???????????????"), //dateTo
		    		""
	        )); 
			
			for(int i=0;i<LJLM_gradeAll4.size();i++) {
				salesService.JLM_gradeAllSave(LJLM_gradeAll4.get(i));
			}
			
			/**GMAT**/ 
			String sql5 = "select ??????,??????,???????????????,??????????????? from JLM1.dbo.?????? where ?????? like '%GMAT%'";
	
			List<JLM_gradeAll> LJLM_gradeAll5 = jdbcTemplate_JLM.query(sql5,(result,rowNum)->new JLM_gradeAll(
		    		"", //gradeAll_seq
		    		"3", //category_id GMAT
		    		"",
		    		result.getString("??????").trim(), //gradeId
		    		result.getString("??????"), //gradeName
		    		result.getString("???????????????"),//dateFrom
		    		result.getString("???????????????"), //dateTo
		    		""
	        )); 
			
			for(int i=0;i<LJLM_gradeAll5.size();i++) {
				salesService.JLM_gradeAllSave(LJLM_gradeAll5.get(i));
			}		
		
			/**??????**/ 
			String sql6 = "select ??????,??????,???????????????,??????????????? from JLM1.dbo.?????? where ?????? like '%??????%'"+
			" or ?????? like '%??????%'"+
			" or ?????? like '%??????????????????%'"+
			" or ?????? like '%???????????????%'"+
			" or ?????? like '%????????????%'"+
			" or ?????? like '%????????????%'"+
			" or ?????? like '%????????????%'"+
			" or ?????? like '%??????????????????%'"+
			" or ?????? like '%????????????%'"+
			" or ?????? like '%??????????????????%'"+
			" or ?????? like '%??????????????????%'"+
			" or ?????? like '%????????????%'"+
			" or ?????? like '%????????????%'"+
			" or ?????? like '%????????????%'"+
			" or ?????? like '%??????%'";
	
			List<JLM_gradeAll> LJLM_gradeAll6 = jdbcTemplate_JLM.query(sql6,(result,rowNum)->new JLM_gradeAll(
		    		"", //gradeAll_seq
		    		"1", //category_id ??????
		    		"",
		    		result.getString("??????").trim(), //gradeId
		    		result.getString("??????"), //gradeName
		    		result.getString("???????????????"),//dateFrom
		    		result.getString("???????????????"), //dateTo
		    		""
	        )); 
			
			for(int i=0;i<LJLM_gradeAll6.size();i++) {
				salesService.JLM_gradeAllSave(LJLM_gradeAll6.get(i));
			}
			
			/**CFA**/ 
			String sql7 = "select ??????,??????,???????????????,??????????????? from JLM1.dbo.?????? where ?????? like '%CFA%'";
	
			List<JLM_gradeAll> LJLM_gradeAll7 = jdbcTemplate_JLM.query(sql7,(result,rowNum)->new JLM_gradeAll(
		    		"", //gradeAll_seq
		    		"7", //category_id CFA
		    		"",
		    		result.getString("??????").trim(), //gradeId
		    		result.getString("??????"), //gradeName
		    		result.getString("???????????????"),//dateFrom
		    		result.getString("???????????????"), //dateTo
		    		""
	        )); 
			
			for(int i=0;i<LJLM_gradeAll7.size();i++) {
				salesService.JLM_gradeAllSave(LJLM_gradeAll7.get(i));
			}		
		
	
	        return true;
    }    
    
    public boolean excel_gradeMap(){
    	System.out.println("------------------------");
    	System.out.println("(Excel)????????????VS.EIP?????? ");    	
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
    	  for (File file : listOfFiles) { //??????File
    	    if (file.isFile()) { 
    			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
    			String categoryName="";
    			String category_id = "";    			
    			String JL_gradeId = "";
    			String grade_seq = "";
    			String schoolAbbr = "";
    			String subjectAbbr = "";
    			String subject_id = "";
    			String gradeName = ""; //??????
    			String old_gradeName = ""; //???????????????
    			String class_start_date_0 = "";
    			String video_date = "";
    			String class_no = "";
    			String teacher_id = "";
    			String class_style = "";
    			String status_code = "-1";
    			String grade_date = "";
    			
    			

    	        for (int x=0;x<wb.getNumberOfSheets();x++) { //????????????
    	        	  	        	
    	        	categoryName = wb.getSheetName(x);
    	        	if(categoryName.contains("??????")) {
    	        		System.out.println("********??????");
    	        		category_id = "1";
    	        	}else if(categoryName.contains("??????")) {
    	        		System.out.println("********??????");
    	        		category_id = "2";
    	        	}else if(categoryName.contains("GMAT")) {
    	        		System.out.println("********GMAT");
    	        		category_id = "3";
    	        	}else if(categoryName.contains("GRE")) {
    	        		System.out.println("********GRE");
    	        		category_id = "4";
    	        	}else if(categoryName.contains("??????")) {
    	        		System.out.println("********??????");
    	        		category_id = "5";
    	        	}else if(categoryName.contains("??????")) {
    	        		System.out.println("********??????");
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
    					//????????????EIP??????
    					for(int a=1;a<sheet.getPhysicalNumberOfRows();a++) {//??????
    						rowNoFlag = a;
    						row=sheet.getRow(a);
    						if(row==null || formatter.formatCellValue(row.getCell(2))=="") {break;}	
    						class_style = row.getCell(10)==null?"":formatter.formatCellValue(row.getCell(10)).trim();
    						if(class_style.equals("1")) {
    							EIPCode.add(formatter.formatCellValue(row.getCell(3)).trim());
    						}
    					}
    					
	    				for(int k=1;k<sheet.getPhysicalNumberOfRows();k++) {//??????
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
	    					//??????
	    					class_start_date_0 = row.getCell(7)==null?"":formatter.formatCellValue(row.getCell(7)).trim();
	    					if(!class_start_date_0.equals("") && class_start_date_0.length()>=6) {
	    						if(class_start_date_0.contains("&")) { //??????????????????/A???/...????????????, ??????????????????
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
						    		System.out.println("*********?????????subjectAbbr**********"+subjectAbbr);
						    	}
	    					}
	    					
	    						

	    					//*****eip.JL_EIP_grade*****//??????EIP????????????
	    					if(!subject_id.equals("")){
	    						jdbcTemplate.update("INSERT INTO eip.JL_EIP_grade VALUES (default,?,?,?,?,?);",
    									grade_seq,
    									JL_gradeId,
    									class_style,
    									subject_id,
    									schoolAbbr
	    						);
	    					}
	    					
			//*****eip.grade*****//??????????????????
			//??????????????????+?????????EIPcode???Video??????
	    		        if(row.getCell(11)==null || formatter.formatCellValue(row.getCell(11))=="") { //??????????????????		
	    		        	status_code = "5"; //????????????????????????
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
						if(VideoExistEIPCode.equals("N")) {EIPCode.add(grade_seq);} //???????????????EIPCode
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
    								jdbcTemplate.update("INSERT INTO eip.grade VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURDATE(),?,?,?,?,?,?);",
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
	    								    "1",//???
	    								    "", //TimeFrom
	    								    "", //TimeTo
	    								    "System",
	    								    onSell,
	    								    offSell,
	    								    grade_date,
	    								    "1",
	    								    "0",
	    								    "0"
	    						    );
    							}	
	    						    
				//*****eip.classes*****//??????????????????
							    String class_date_1 = "";
							    String timeFrom = "";
							    String timeTo = "";    							
								for(int xth=0;xth<Integer.valueOf(class_no);xth++) { //?????????
								    int class_trial = 0; //??????
								    if(xth==0) {class_trial=1;}
								    String class_style_c = "1"; //face

								    String[] tokens = formatter.formatCellValue(row.getCell(11+xth)).trim().split("#");
								    if(tokens.length==3) {
									    String[] tokenDate = tokens[0].split("/");
									    String s1 = tokenDate[1].length()==1? "0"+tokenDate[1] : tokenDate[1];
									    String s2 = tokenDate[2].length()==1? "0"+tokenDate[2] : tokenDate[2];
									    class_date_1 = s1+"/"+s2+"/"+tokenDate[0];
									    if(class_date_1.length()!=10) {System.out.println("??????????????????"+class_date_1);}
									    timeFrom = tokens[1];
									    timeTo = tokens[2];								    	
								    }
								    
								    if(row.getCell(11+xth)!=null && formatter.formatCellValue(row.getCell(11+xth)).trim().equals("video")) {
								    	class_style_c = "2"; //video
								    }
									jdbcTemplate.update("INSERT INTO eip.classes VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
										grade_seq,
										xth+1,
										class_date_1,
										timeFrom,
										timeTo,
										teacher_id,
										class_style_c,
										"",
										"",
										"(??????)",
										1,
										class_trial,
										"",
										xth+1
								    );
									//????????????????????????
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
    				  }//??????
    		        }catch(Exception e1) {
    		        	System.out.println("row----------"+rowNoFlag);
    		        	e1.printStackTrace();
    		        }	    				
    				 wb.close();
    				 System.out.println("#######??????????????????######");
    			  }//????????????		
    	        }	  	            	        
    	    }
        }catch(Exception e) {
        	e.printStackTrace();
        }
	    Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		System.out.println("----------????????????????????????-------------------------------------------"+ft.format(dNow));     	
    	return true;
    }	

    public Boolean ComboBuild() {
    	List<ComboSale> LComboSale = courseSaleService.getComboSaleByCombo("0","");
    	for(int i=0;i<LComboSale.size();i++) {
    		jdbcTemplate.update("delete from eip.comboSale where comboSale_seq="+LComboSale.get(i).getComboSale_seq());
    		jdbcTemplate.update("delete from eip.comboSale_subject where comboSale_id="+LComboSale.get(i).getComboSale_seq());
    	}
    	List<Subject> LSubject = courseService.getSubject("","","","","","0"); //active 0,1 ?????????
    	int FlowStatus_code = -1;
    	for(int i=0;i<LSubject.size();i++) {
    		//*****eip.comboSale*****//
    		FlowStatus_code = 4;
    		if(LSubject.get(i).getActive().equals("0")) {
    			FlowStatus_code = 5;
    		}
			jdbcTemplate.update("INSERT INTO eip.comboSale VALUES (default,?,?,?,?,?,?,?,?,?,CURDATE(),?,?,?,?,?,?);",
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
					"", //originSubjectName
					"", //favor_id
					"1", //haveRead
					"0" //disable
			);
			int x  = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
			//*****eip.comboSale_subject*****//
			jdbcTemplate.update("INSERT INTO eip.comboSale_subject VALUES (default,?,?);",
					x, //comboSale_id
					LSubject.get(i).getSubject_seq() //subject_id
			);			
    	}
    	System.out.println("---------- EIP ?????????????????????-----??????");
        return true;
    }
    
    public Boolean insertSubjectName() { 
    	List<ComboSale> LComboSale = courseSaleService.getComboSale("","","0","","","","","0");
    	for(int i=0;i<LComboSale.size();i++) {
    		List<ComboSale_subject> LComboSale_subject = courseSaleService.getComboSale_subject(LComboSale.get(i).getComboSale_seq(),"");
	    	if(LComboSale_subject.size()>0) {
    			String subjectName = LComboSale_subject.get(0).getSubject_name();
    			//?????????
	    		jdbcTemplate.update("Update eip.comboSale set name =? where isCombo=0 and comboSale_seq=? and (name is null or name='');",
	    				subjectName,
	    				LComboSale.get(i).getComboSale_seq()		
	    		);
	    		//????????????
	    		jdbcTemplate.update("Update eip.comboSale set originSubjectName =? where isCombo=0 and comboSale_seq=?;",
	    				subjectName,
	    				LComboSale.get(i).getComboSale_seq()		
	    		);	    		
	    	}	
    	}    	
    	System.out.println("---------- ???eip.comboSale???name???originSubjectName-----??????");
        return true;
    }
    
    public boolean eip_register() {
    	System.out.println("------------------------");
    	System.out.println("EIP ??????+?????????+?????????");
    	System.out.println("DELETE eip.Register,eip.Register_comboSale,eip.Register_comboSale_grade,eip.signRecordHistory");
		jdbcTemplate.update("delete from eip.Register;");
		jdbcTemplate.update("ALTER TABLE eip.Register AUTO_INCREMENT = 1;");
		jdbcTemplate.update("delete from eip.Register_comboSale;");
		jdbcTemplate.update("ALTER TABLE eip.Register_comboSale AUTO_INCREMENT = 1;");
		jdbcTemplate.update("delete from eip.Register_comboSale_grade;");
		jdbcTemplate.update("ALTER TABLE eip.Register_comboSale_grade AUTO_INCREMENT = 1;");
		jdbcTemplate.update("delete from eip.signRecordHistory;");
		jdbcTemplate.update("ALTER TABLE eip.signRecordHistory AUTO_INCREMENT = 1;");    	

		List<JLM_gradeRegister> LJLM_gradeRegister = systemService.getJLM_gradeRegister("","1"); //??????????????????,????????????

			for(int j=0;j<LJLM_gradeRegister.size();j++) {			  	
			  List<JL_EIP_grade> LJL_EIP_grade = systemService.getJL_EIP_grade("",LJLM_gradeRegister.get(j).getGradeId()); //????????????		  
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
						orderStatus = "2";//?????????
					}else {
						orderStatus = "1";
					}
				  if(jL_EIP_grade.getClass_style().equals("1") || jL_EIP_grade.getClass_style().equals("2")) {	//?????????Video	
							//----eip.Register----//
							jdbcTemplate.update("INSERT INTO eip.Register VALUES (default,?,?,?,?,?,?,?,'',0);",
								student_seq,
								originPrice,
								actualPrice,
								paid,
								"??????", //comment
								jLM_studentPay.getSalePerson(), //creater
								orderStatus 
							);
							int x = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
							//----eip.Register_comboSalee----//
							jdbcTemplate.update("INSERT INTO eip.Register_comboSale VALUES (default,?,?,?,?,?,?,?,?,?,?);",
								x, //Register_id
								comboSale_seq, //comboSale_id
								subject_id, //subject_id
								0, //gradeNoLeft
								"", //subject_id_virtual
								"", //replaceFrom
								"", //from_Register_comboSale_id
								1, //active
								actualPrice, //costShare
								""
							);					
							int y = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
							//----eip.Register_comboSale_grade----//
				    		jdbcTemplate.update("INSERT INTO eip.Register_comboSale_grade VALUES (default,?,?,?,?,?,?,?,?,?);",
			   					y, //Register_comboSale_id
			   					jL_EIP_grade.getEip_grade_seq(), //grade_id
			   					1, //register_status
			   					jL_EIP_grade.getClass_style(), //class_style
			   					jL_EIP_grade.getSchoolAbbr(),//school_code
			   					jLM_studentPay.getSitNo(), //sitNo
			   					1,
			   					"",
			   					""
				   			);
				    		int z = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
		                    //----eip.signRecordHistory----//
					    		List<Grade> LGrade = courseService.getGradeList("","",jL_EIP_grade.getEip_grade_seq(),"","","","","","","1","","","","","","","","1","");
					    		if(LGrade.size()>0) { //???????????????????????????
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
				   }else if(jL_EIP_grade.getClass_style().equals("0")) {	//??????
							//----eip.Register----//
							jdbcTemplate.update("INSERT INTO eip.Register VALUES (default,?,?,?,?,?,?,?,'',0);",
								student_seq,
								originPrice, //originPrice
								actualPrice, //actualPrice
								paid, //paid
								"??????", //comment
								jLM_studentPay.getSalePerson(), //creater
								-1 //orderStatus 
							);
							int x = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
							//----eip.Register_comboSalee----//
							Subject subject = courseService.getSubject("",subject_id,"","","","0").get(0);
							jdbcTemplate.update("INSERT INTO eip.Register_comboSale VALUES (default,?,?,?,?,?,?,?,?,?,?);",
								x, //Register_id
								comboSale_seq, //comboSale_id
								subject_id, //subject_id
								"-99", //gradeNoLeft
								"", //subject_id_virtual
								"", //replaceFrom
								"", //from_Register_comboSale_id
								1, //active 
								actualPrice, //costShare
								""
							);					   
				   }
				}					
			}	  
	    Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		System.out.println("---------- EIP ??????+?????????+?????????-----??????-------------------------------------------"+ft.format(dNow));		
    	return true;	
    }
    
    
    public boolean eip_register2() {
    	System.out.println("------------------------");
    	System.out.println("EIP2 ??????+?????????+?????????");
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

		List<JLM_gradeRegister> LJLM_gradeRegister = systemService.getJLM_gradeRegister("","1"); //??????????????????,????????????
		  String salePerson="";
		  for(int j=0;j<LJLM_gradeRegister.size();j++) {
			if(!LJLM_gradeRegister.get(j).getSitNo().contains("?????????")) {	
			  List<JL_EIP_grade> LJL_EIP_grade = systemService.getJL_EIP_grade("",LJLM_gradeRegister.get(j).getGradeId()); //????????????		  
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
				    //????????????
				    List<JLM_studentPay> LJLM_studentPay0 = salesService.getJLM_studentPay(LJLM_gradeRegister.get(j).getStudent_no(),"","",LJLM_gradeRegister.get(j).getGradeId());
				    if(LJLM_studentPay0.size()>0) {
				    	jLM_studentPay = LJLM_studentPay0.get(0);
						originPrice = jLM_studentPay.getOriginPrice();
						actualPrice = Integer.valueOf(jLM_studentPay.getNeedPay());
						paid =  Integer.valueOf(jLM_studentPay.getPaidMoney().equals("")?"0":jLM_studentPay.getPaidMoney());				    	
				    }
				    student_seq = salesService.getStudent("","","",LJLM_gradeRegister.get(j).getStudent_no(),"","","","","","").get(0).getStudent_seq(); 					
					if(courseSaleService.getComboSaleBySubject("0",subject_id).size()==0) {
						//????????????????????????
						comboSale_seq = "-1";
						System.out.println("@@@@@@@@@@@@@@@????????????????????????subject_id@@@@@@@@@@@@@@@@"+subject_id);
					}else {
						comboSale_seq = courseSaleService.getComboSaleBySubject("0",subject_id).get(0).getComboSale_seq();
					}	

			  		if(actualPrice==0 && paid==0) {
			  			orderStatus = "-1";//?????????
			  		}else if(paid>=actualPrice) {
						orderStatus = "2";//?????????
					}else {
						orderStatus = "1";//?????????
					}
					
				  if(jL_EIP_grade.getClass_style().equals("1") || jL_EIP_grade.getClass_style().equals("2")) {	//?????????Video	
							//----eip.Register----//
							jdbcTemplate.update("INSERT INTO eip.Register VALUES (default,?,?,?,?,?,?,?,?,0);",
									student_seq,
									originPrice,
									actualPrice,
									paid,
									"", //comment
									salePerson, //creater
									orderStatus,
									LJLM_gradeRegister.get(j).getRegisterDate()
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
							jdbcTemplate.update("INSERT INTO eip.Register_comboSale VALUES (default,?,?,?,?,?,?,?,?,?,?);",
								x, //Register_id
								comboSale_seq, //comboSale_id
								subject_id, //subject_id
								0, //gradeNoLeft
								"", //subject_id_virtual
								"", //replaceFrom
								"", //from_Register_comboSale_id
								1, //active
								actualPrice, //costShare
								""
							);					
							int y = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
							//----eip.Register_comboSale_grade----//
				    		jdbcTemplate.update("INSERT INTO eip.Register_comboSale_grade VALUES (default,?,?,?,?,?,?,?,?,?);",
			   					y, //Register_comboSale_id
			   					jL_EIP_grade.getEip_grade_seq(), //grade_id
			   					1, //register_status
			   					jL_EIP_grade.getClass_style(), //class_style
			   					jL_EIP_grade.getSchoolAbbr(),//school_code
			   					jLM_studentPay.getSitNo(), //sitNo
			   					1,
			   					"",
			   					""
				   			);
				    		int z = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
		                    //----eip.signRecordHistory----//
					    		List<Grade> LGrade = courseService.getGradeList("","",jL_EIP_grade.getEip_grade_seq(),"","","","","","","1","","","","","","","","1","");
					    		if(LGrade.size()>0) { //???????????????????????????
						    		for(int k=0;k<Integer.valueOf(LGrade.get(0).getClass_no());k++) {
							    		jdbcTemplate.update("INSERT INTO eip.signRecordHistory VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
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
											1,
											k+1
									    );
						    		}
					    		}	
				   }else if(jL_EIP_grade.getClass_style().equals("0")) {	//??????
							//----eip.Register----//
							jdbcTemplate.update("INSERT INTO eip.Register VALUES (default,?,?,?,?,?,?,?,?,0);",
									student_seq,
									originPrice, //originPrice
									actualPrice, //actualPrice
									paid, //paid
									"????????????", //comment
									salePerson, //creater
									orderStatus, //orderStatus 
									LJLM_gradeRegister.get(j).getRegisterDate()
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
							//??????????????????????????????????????????
							String sql = "select count(*) from eip.register a, eip.Register_comboSale b where a.Register_seq=b.Register_id and a.student_seq='"+student_seq+"' and b.subject_id="+subject_id;							
							int y = jdbcTemplate.queryForObject(sql, Integer.class);
							String gradeNoLeft = "";
							if(y>0) {
								gradeNoLeft = "-1";
							}else {
								Subject subject = courseService.getSubject("",subject_id,"","","","0").get(0);	
								gradeNoLeft = "-99";
							}

							jdbcTemplate.update("INSERT INTO eip.Register_comboSale VALUES (default,?,?,?,?,?,?,?,?,?,?);",
									x, //Register_id
									comboSale_seq, //comboSale_id
									subject_id, //subject_id
									gradeNoLeft, 
									"", //subject_id_virtual
									"", //replaceFrom
									"", //from_Register_comboSale_id
									1, //active 
									actualPrice, //costShare
									""
							);								
				   }
				}					
			}	  
		  }
	    Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		System.out.println("---------- EIP ??????2+?????????+?????????-----??????-------------------------------------------"+ft.format(dNow));	
		System.out.println("---------- EIP ??????2+?????????+?????????-----??????-------------------------------------------"+ft.format(dNow));		
		System.out.println("---------- EIP ??????2+?????????+?????????-----??????-------------------------------------------"+ft.format(dNow));		
    	return true;	
    } 

    
    @Value("${ExcelUploadPath}") String ExcelUploadPath;
    public void upload2(String passArea) throws IOException {	   
    	List<String> LExcelUploadPath = new ArrayList<>();
    	
    if(passArea.equals("0")) {
    	   LExcelUploadPath.add(ExcelUploadPath+"/");
    }
    
    if(passArea.equals("1") || passArea.equals("999")) {   
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/CFA/L1/");		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/TOEFL/TFD/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/TOEFL/TFJJ/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/TOEFL/TFL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/TOEFL/TFR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/TOEFL/TFS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/TOEFL/TFSD/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/TOEFL/TFW/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/GMAT/ATAWA/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/GMAT/ATCR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/GMAT/ATD/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/GMAT/ATDSC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/GMAT/ATIR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/GMAT/ATQ/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/GMAT/ATRC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/GMAT/ATSC/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/GRE/REAW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/GRE/RECR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/GRE/RED/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/GRE/REQ/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/GRE/RERC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/GRE/RETC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/GRE/REV/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/IELTS/ISL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/IELTS/ISR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/IELTS/ISS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/IELTS/ISW/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/TOEIC/TC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/TOEIC/TC/?????????/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/TOEIC/TCL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/TOEIC/TCL/?????????/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/TOEIC/TCR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/TOEIC/TCR/?????????/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/TOEIC/TCSW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/TOEIC/TCSW/?????????/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/Wordgod/ADS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/Wordgod/PAPER/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/Wordgod/PREG/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/Wordgod/PREG(K)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/Wordgod/PREG(P)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/Wordgod/PREL(M)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/Wordgod/PREL(O)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/Wordgod/PRER(P)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/Wordgod/PRES/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/Wordgod/PREW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/Wordgod/TW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2018???????????????/Wordgod/V/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/??????2018 ???????????????/IELTS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????2018 ???????????????/TOEFL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????2018 ???????????????/TOEIC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????2018 ???????????????/WORDGOD/");		   
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/??????2018?????????/IELTS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????2018?????????/TOEFL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????2018?????????/TOEIC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????2018?????????/Wordgod/");		   
    }
    
    if(passArea.equals("2") || passArea.equals("999")) {    		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEFL/TFJJ/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEFL/TFJJ/error/");    	
 		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEFL/TFL/");
 		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEFL/TFL/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEFL/TFR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEFL/TFR/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEFL/TFS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEFL/TFS/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEFL/TFSD/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEFL/TFSD/error/");	
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEFL/TFW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEFL/TFW/error/");		   
   
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEIC/TC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEIC/TC/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEIC/TCL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEIC/TCL/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEIC/TCR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEIC/TCR/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEIC/TCSW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/TOEIC/TCSW/error/");	
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GMAT/ATAWA/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GMAT/ATAWA/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GMAT/ATCR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GMAT/ATCR/error/");		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GMAT/ATD/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GMAT/ATD/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GMAT/ATIR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GMAT/ATIR/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GMAT/ATQ/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GMAT/ATQ/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GMAT/ATRC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GMAT/ATRC/error/");	
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GMAT/ATSC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GMAT/ATSC/error/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GRE/REAW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GRE/REAW/error/");		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GRE/RECR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GRE/RECR/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GRE/RED/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GRE/RED/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GRE/REQ/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GRE/REQ/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GRE/RERC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GRE/RERC/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GRE/RETC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GRE/RETC/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GRE/REV/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/GRE/REV/error/");	
		   		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/IELTS/ISL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/IELTS/ISL/error/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/IELTS/ISR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/IELTS/ISR/error/");	
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/IELTS/ISS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/IELTS/ISS/error/");	
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/IELTS/ISW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/IELTS/ISW/error/");	
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/Wordgod/ADS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/Wordgod/BULS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/Wordgod/BURW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/Wordgod/OOOB/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/Wordgod/OOON/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/Wordgod/PAPER/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/Wordgod/PREG/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/Wordgod/PREG(K)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/Wordgod/PREL(M)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/Wordgod/PREL(O)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/Wordgod/PRER(P)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/Wordgod/PRES/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/Wordgod/PREV/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/Wordgod/PREW(P)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2019???????????????/Wordgod/V/");
		   		   
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/???????????????/IELTS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/???????????????/TOEFL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/???????????????/TOEIC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/???????????????/??????/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/??????/GRE/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/??????/IS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/??????/TC/TC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/??????/TC/TCSW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/??????/TF/TFL(A)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/??????/TFR(A)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/??????/TFR(S)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/??????/TFS(M)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/??????/TFW(W)/");		   
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/??????/WG/PRES/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/??????/WG/PREW/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/2019??????????????????/ISL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/2019??????????????????/ISR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/2019??????????????????/ISS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/2019??????????????????/ISW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/2019??????????????????/PREG/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/2019??????????????????/PREL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/2019??????????????????/PRES/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/2019??????????????????/PREW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/2019??????????????????/TCL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/2019??????????????????/TCR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/2019??????????????????/TFL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/2019??????????????????/TFR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/2019??????????????????/TFS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/??????/2019??????????????????/TFW/");
    }  
    
    if(passArea.equals("3") || passArea.equals("999")) { 
    	   //LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/????????????/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/CFA/L1/");		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/TOEFL/TFJJ/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/TOEFL/TFL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/TOEFL/TFR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/TOEFL/TFS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/TOEFL/TFSD/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/TOEFL/TFW/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GMAT/ATAWA/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GMAT/ATCR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GMAT/ATD/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GMAT/ATDSC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GMAT/ATIR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GMAT/ATQ/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GMAT/ATRC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GMAT/ATSC/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GRE/REAW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GRE/RECR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GRE/RED/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GRE/REQ/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GRE/RERC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GRE/RETC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GRE/REV/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/IELTS/ISL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/IELTS/ISR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/IELTS/ISS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/IELTS/ISW/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/TOEIC/TC/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/TOEIC/TCL/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/TOEIC/TCR/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/TOEIC/TCSW/");
		   
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/ADS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/BULS/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/BURW/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/OOOB/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/OOON/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/PAPER/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/PREG/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/PREG(K)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/PREL(M)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/PREL(O)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/PRER(P)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/PRES/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/PREV/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/PREW(P)/");
		   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/V/");
		   
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/CFA/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GMAT/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GRE/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/IELTS/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/TOEFL/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/TOEIC/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/");
    	   
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/CFA/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GMAT/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GRE/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/IELTS/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/TOEFL/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/TOEIC/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/"); 
    	   
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/CFA/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GMAT/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/GRE/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/IELTS/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/TOEFL/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/TOEIC/");
    	   LExcelUploadPath.add(ExcelUploadPath+"/2020???????????????/??????/Wordgod/");      	   
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
 		    		//Step1.????????????????????????
 		    		fileName = Lfile[i].getName();  
 		    		System.out.println("[??????]"+fileName);
 			        String[] fn = fileName.substring(0,fileName.length()-5).split("-");
 			        String school_code       = fn[0];
 			        String subject_abbr      = fn[1];       
 			        String class_start_date  = fn[2];
 			        
 			        String video_date = "";
					if(!class_start_date.equals("") && class_start_date.length()==6) {
						class_start_date = class_start_date.substring(2,4)+"/"+class_start_date.substring(4,6)+"/20"+class_start_date.substring(0,2);
						video_date = "";
					}else {
						video_date = class_start_date; //????????????
						class_start_date = "";
					} 			        

 			        String teacher_name      = fn[3];
 			        String class_th = "";
 			        String student_no = "";
 			        String attend = "";
 			        String TakeHandout = "";

 			        String grade_id = "";
 			        
 			        String gradeName = "";
 			        if(fn.length==5) {
 			        	gradeName = fn[4];
 			        }
 					List<Grade> LGrade = courseService.getGradeList("","","",school_code,"",subject_abbr,class_start_date,"","","1",video_date,teacher_name,"","","",gradeName,"","1","");
 /**
 					for(int x=0;x<LGrade.size();x++) {
 						System.out.println(LGrade.get(x).getGrade_seq());
 					}
 **/					
 					if(LGrade.size()==0) {	
 						System.out.println("??? ??????????????????: "+school_code+"/"+subject_abbr+"/"+fn[2]+"/"+teacher_name);
 						continue;
 					}        
 		    	
 		    		//Step2.??????Excel 
 					InputStream ExcelFileToRead = new FileInputStream(LExcelUploadPath.get(z) + fileName);
 					String updater = "system";
 					XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
 					String student_id = ""; 
 					ArrayList<String> A_attend = new ArrayList<String>();
 					ArrayList<String> A_TakeHandout = new ArrayList<String>();
 					ArrayList<String> A_student_id = new ArrayList<String>();
 					ArrayList<String> A_student_no = new ArrayList<String>();
 					ArrayList<String> A_student_name = new ArrayList<String>();
 					ArrayList<String> A_class_th = new ArrayList<String>();
 					DataFormatter formatter = new DataFormatter();
 	

 					//????????????
 			        for (int x=0;x<wb.getNumberOfSheets();x++) {
 			        	
 			        	class_th = wb.getSheetName(x);
 						//??????????????????: Ex.?????????: 1-V-??????-?????? 			        	
 			        	if(class_th.contains("-")) {
 			        		String[] class_th_x = class_th.split("-");
 			        		class_th     = class_th_x[0];
 			        		String className    = class_th_x[1];
 			        		String class_style  = class_th_x[2];
 			        		if(class_style.equals("??????")) {
 			        			class_style = "1";
 			        		}else if(class_style.equals("VIDEO")) {
 			        			class_style = "2";
 			        		}
 			        		String teacher_name_2 = class_th_x[3];
 			        		String teacher_id = courseService.getTeacher("","","","","","",teacher_name_2).get(0).getTeacher_seq();
 			        		Boolean returnStr = courseService.UpdateClasses(class_th,className,teacher_id,grade_id,class_style);
 			        		if(!returnStr) {						
 								System.out.println("??????????????????: ??????"+student_no+" ??????"+class_th);
 								wb.close(); 							} 	        		 			        		
 			        	}
 			        	
 			        	
 						XSSFSheet sheet=wb.getSheetAt(x);
 						XSSFRow row;
 						//??????
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
 								for(int m=0;m<LGrade.size();m++) {
 									List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory("",student_id,LGrade.get(m).getGrade_seq(),"1","(1)","","","","1",school_code,class_th);
 									
	 								//?????????????????? 
	 								if(LSignRecordHistory.size()>0) {
	 									A_attend.add(attend);
	 									A_TakeHandout.add(TakeHandout);
	 									A_student_id.add(student_id);
	 									A_student_no.add(student_no);
	 									A_student_name.add(LStudent.get(0).getCh_name());
	 									A_class_th.add(class_th);
	 									grade_id = LGrade.get(m).getGrade_seq(); //(TFJJ??????????????????grade_id)
	 									break;
	 								}
 								}	
	 							if(grade_id.isEmpty()) {
	 								System.out.println("----------------------------------------------------------------------------------------??????????????????????????????: "+fileName+"??????"+student_no+"??????"+class_th);
	 							}
 							}else {	
 								System.out.println("??? ??????????????????: file"+fileName+"??????"+student_no+"??????"+class_th);

 							} 							
 						}	 								
 			        }	
 			        wb.close();
 			        
 			        for(int j=0;j<A_student_id.size();j++) {
 			        	//???????????????
 						Boolean returnStr = admService.UpdateSignRecord(subject_abbr,school_code,A_class_th.get(j),A_student_id.get(j),A_attend.get(j),A_TakeHandout.get(j),updater,grade_id,"1","",A_student_no.get(j),"",A_student_name.get(j));
 						if(!returnStr) {					
 							System.out.println("????????????: ??????"+A_student_id.get(j)+" ??????"+class_th);
 						}        	
 			        }
 		    	}
 		      }	
 		    }catch(Exception e) {
 		            e.printStackTrace();
 		            System.out.println("????????????"+e);
 		    } 		  
 	     } 
 	     System.out.println("---------------------------??????????????????-----------------------------------------");
   } 
    

    public void uploadVideo2(String test) throws IOException {	
 	   List<String> LExcelUploadPath = new ArrayList<>();
 	   if(test.equals("1")) { //?????????
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/");
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/??????/");
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/??????/");
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/??????/");
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/??????/");
 	   }else if(test.equals("0")) {  //???????????????
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/2018/??????/");
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/2018/??????/");
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/2018/??????/");
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/2018/??????/"); 
	 	   
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/2019/??????/");
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/2019/??????/");
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/2019/??????/");
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/2019/??????/");
	 	   
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/2020/??????/");
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/2020/??????/");
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/2020/??????/");
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/2020/??????/"); 
	 	   
 	   }else if(test.equals("2")) {
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/2020/??????/");
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/2020/??????/");
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/2020/??????/");
	 	   LExcelUploadPath.add(ExcelUploadPath+"video/2020/??????/"); 		 	   
 	   }
 	   //LExcelUploadPath.add(ExcelUploadPath+"video/2019VIDEO??????/VIDEO?????????/1???/");
 	   
 	   for(int z=0;z<LExcelUploadPath.size();z++) { 
 		    try {
 		    	File folder = new File(LExcelUploadPath.get(z));
 		    	File[] Lfile = folder.listFiles();
 		    	String fileName = "";
 //?????????		    	
 		    	for (int i=0;i<Lfile.length;i++) {
 		    		if(Lfile[i].isDirectory()) {
 		    			continue;
 		    		}
 		    		//Step1.????????????????????????
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

 //?????????
 					for (int x=0;x<wb.getNumberOfSheets();x++) {
 				      if(!wb.getSheetName(x).contains("??????")) {		
 						XSSFSheet sheet=wb.getSheetAt(x);
 						XSSFRow row;
 //??????						
 						for(int a=1;a<sheet.getPhysicalNumberOfRows();a++) {
 							grade_id = "";
 							row=sheet.getRow(a);
 					
 							if(row==null || row.getCell(0)==null || formatter.formatCellValue(row.getCell(0)).isEmpty()) {
 								break; 
 							}
 							student_no = formatter.formatCellValue(row.getCell(0)).trim();						
 							subject_abbr = formatter.formatCellValue(row.getCell(4)).trim(); 
 							class_start_date0 = formatter.formatCellValue(row.getCell(3)).trim(); 
							class_th    = formatter.formatCellValue(row.getCell(5)).trim();
							TakeHandout = formatter.formatCellValue(row.getCell(8)).trim();
							attend      = formatter.formatCellValue(row.getCell(9)).trim(); 							
 							List<Teacher> LTeacher = courseService.getTeacher("","","","","","",formatter.formatCellValue(row.getCell(6)));
							List<Student> LStudent = salesService.getStudent("","","",student_no,"","","","","","");

 							if(LTeacher.size()==0) {
 								System.out.println(a+1+"~> ?????????????????? : "+formatter.formatCellValue(row.getCell(6)));
 							}else if(LStudent.size()==0){
 								System.out.println(a+1+"~> ?????????????????? : "+student_no);
 							}else if(class_th.isEmpty() || class_th.trim().replaceAll("\\s+","").equals("0")) {
 								System.out.println(a+1+"~> ???????????? : "+student_no+" ??????:"+class_start_date+" "+subject_abbr+" ??????"+class_th); 									
 							}else {	
 								teacher_id = LTeacher.get(0).getTeacher_seq();
 								student_id = LStudent.get(0).getStudent_seq();
 								List<Grade> LGrade = new ArrayList<Grade>();
 								if(class_start_date0.length()==6) {
 									class_start_date = class_start_date0.substring(2,4)+"/"+class_start_date0.substring(4,6)+"/"+"20"+class_start_date0.substring(0,2);
 									//LGrade = courseService.getGradeList(teacher_id,"","",school_code,"",subject_abbr,class_start_date,"","","1","","","");
 									LGrade = courseService.getGradeList(teacher_id,"","","","",subject_abbr,class_start_date,"","","1","","","","","","","","1","");
 								}else {
 									if(class_start_date0.length()>1 && class_start_date0.length()<6) { //????????????
 										video_date = class_start_date0;
 										//LGrade = courseService.getGradeList(teacher_id,"","",school_code,"",subject_abbr,"","","","1",video_date,"","");
 										LGrade = courseService.getGradeList(teacher_id,"","","","",subject_abbr,"","","","1",video_date,"","","","","","","1","");
 									}	
 								}
 								
 								if(LGrade.size()==0) {
 										System.out.println(a+1+"~> ?????????????????? : "+subject_abbr+" "+class_start_date0+" "+formatter.formatCellValue(row.getCell(6)));
 								}else{										
 									for(int m=0;m<LGrade.size();m++) {
	 									List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory("",LStudent.get(0).getStudent_seq(),LGrade.get(m).getGrade_seq(),"1","(1)","","","","","",class_th);
	 								    //????????????(????????????)
	 									if(LSignRecordHistory.size()==1) {								
	 										if(LSignRecordHistory.get(0).getClass_style().equals("2")) {
	 											grade_id = LGrade.get(m).getGrade_seq();
	 										}else{
												//System.out.println(a+1+"~> ?????????????????????,????????????????????????Video");
	 											if(admService.addVideoRecord(LSignRecordHistory.get(0),school_code));
	 											grade_id = LGrade.get(m).getGrade_seq();
												break;
	 										}	 										
	 									//???????????????,???????????????
	 									}else if(LSignRecordHistory.size()>1){
	 										//1. ????????????
	 										LGrade = courseService.getGradeList(teacher_id,"","",school_code,"",subject_abbr,"","","","1",video_date,"","","","","","","1","");
	 										if(LGrade.size()>0){
	 											grade_id = LGrade.get(0).getGrade_seq();
	 										//2. ???????????????,??????grade_id	
	 										}else{
	 											for(int p=0;p<LSignRecordHistory.size();p++) {
	 												if(LSignRecordHistory.get(p).getAttend().equals("0")) {
	 													grade_id = LSignRecordHistory.get(p).getGrade_id();
	 												}
	 											}
	 											LGrade = courseService.getGradeList(teacher_id,"",grade_id,"","",subject_abbr,"","","","1",video_date,"","","","","","","1","");
		 										if(LGrade.size()>0) {
		 											grade_id = LGrade.get(0).getGrade_seq();
		 										}else {	
		 											System.out.println(a+1+"~> ??????????????? ? : ??????:"+student_no+" ??????:"+class_start_date+" "+subject_abbr);
		 										}	
	 										}
	 									}	
	 								}
	 								
	 								if(!grade_id.isEmpty()) {
	 									//System.out.println(a+1+">>>>"+subject_abbr+"--"+school_code+"--"+class_th+"--"+student_id+"--"+attend+"--"+student_no+"--"+grade_id);
	 									admService.UpdateSignRecord(subject_abbr,school_code,class_th,student_id,attend,TakeHandout,updater,grade_id,"2",attend_date,student_no,String.valueOf(a+1),"");  
	 								}else{
 										System.out.println(a+1+"~> ?????????? : ??????:"+student_no+" ??????:"+class_start_date+" "+subject_abbr);												
 									}								
 							    }
 						     }	
 			              } //??????
 				       }
 					}//?????????
 				    wb.close();
 		    	} //?????????  		    	
 		    }catch(Exception e) {
 		    	e.printStackTrace();
 		    }		    
 	   }
 	  System.out.println("---------------------------Video????????????-----------------------------------------");
    }
    
    public boolean subject_price() {
    	List<Register_comboSale> LRegister_comboSale = salesService.getComboSaleByRegister("","","","","","",false,false,"");
    	for(int i=0;i<LRegister_comboSale.size();i++) {
    		if(LRegister_comboSale.get(i).getSubject_id()!=null) {
    			List<Subject> LSubject = courseService.getSubject("",LRegister_comboSale.get(i).getSubject_id(),"","","","0");  		
	    		if(LSubject.size()>0) {
		    		jdbcTemplate.update("Update eip.Register_comboSale set subject_price =? where Register_comboSale_seq=?;",
		    				LSubject.get(0).getPrice(),
		    				LRegister_comboSale.get(i).getRegister_comboSale_seq()	
		    		);    			
	    		}
    		}	
    	}
    	return true;
    }
    
	public List<classRoom> getclassRoom(String school_code,String grade_id,String order_by,String withSeatNo,String classRoomName){
		String sql = "";
		if (school_code != null && !school_code.isEmpty()) {
			sql = " SELECT * from eip.classRoom where school_code = '"+school_code+"'";
		}
		if (grade_id != null && !grade_id.isEmpty()) {
			sql = "SELECT a.* from eip.classRoom a,eip.grade b where b.grade_seq="+grade_id+" and b.school_code=a.school_code";
		}
		if (classRoomName != null && !classRoomName.isEmpty()) {
			sql += "  and name='"+classRoomName+"'";
		}		
		if (withSeatNo != null && withSeatNo.equals("1")) {
			sql += "  and seatFrom is not null and seatFrom !=''";
		}			
		if (order_by != null && !order_by.isEmpty()) {
			sql += "  order by "+order_by;
		}	
		List<classRoom> LclassRoom = jdbcTemplate.query(sql,
				(result, rowNum) -> new classRoom(
						result.getString("classRoom_seq"),
						result.getString("school_code"), 
						result.getString("name"), 
						result.getString("isVideo"),
						"",
						result.getString("capacity"),
						result.getString("seatFrom"),
						result.getString("seatTo")
				));
		return LclassRoom;
	} 
	
	public boolean updateSlot() {
		jdbcTemplate.update("update eip.classes set slot_id=2 where time_from >= 1200 and time_from <1730 and time_from !=''");
		jdbcTemplate.update("update eip.classes set slot_id=3 where time_from >= 1730 and time_from !=''");
		return true;
	}
	
	public boolean schedule_1() {
		try {
			String nowDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
			//3:?????????-->4:??????(=??????????????????)
	    	List<Grade> LGrade = courseService.getGradeList("","","","","","","","","3","1","","","","","","","","1","");
	    	for(int i=0;i<LGrade.size();i++) {
	    		String onSell = LGrade.get(i).getOnSell();
	    		if(onSell.length()>9) {
	    			onSell = onSell.substring(6,10)+onSell.substring(0,2)+onSell.substring(3,5);
	    			if(Integer.valueOf(nowDate)>=Integer.valueOf(onSell)){
	    				courseService.UpdateGradeStatus(LGrade.get(i).getGrade_seq(),"4");
	    				courseService.scheduleTask(LGrade.get(i).getGrade_seq(),"(?????????-->??????) "+LGrade.get(i).getClass_start_date()+" "+LGrade.get(i).getSubject_name(),"4");
	    				System.out.println("??????Grade_seq"+LGrade.get(i).getGrade_seq());
	    			}
	    		}
	    	}
	    	
			//4:??????-->5:??????
	    	List<Grade> LGrade2 = courseService.getGradeList("","","","","","","","","4","1","","","","","","","","1","");
	    	String class_xth_date = "";
	    	boolean needToOff;
	    	for(int i=0;i<LGrade2.size();i++) {
	    		needToOff = true;
	    		List<Classes> LClasses = courseService.getClasses(LGrade2.get(i).getGrade_seq(),"","","","","","","","","");
	    		for(int x=0;x<LClasses.size();x++) {
	    			class_xth_date = LClasses.get(x).getClass_date();
	    			if(class_xth_date.length()>9) {
		    			class_xth_date = class_xth_date.substring(6,10)+class_xth_date.substring(0,2)+class_xth_date.substring(3,5);
		    			if(Integer.valueOf(class_xth_date)>=Integer.valueOf(nowDate)){
		    				needToOff = false;
		    			}
	    			}	
	    		}
	    		if(needToOff) {
    				courseService.UpdateGradeStatus(LGrade2.get(i).getGrade_seq(),"5");
    				courseService.scheduleTask(LGrade2.get(i).getGrade_seq(),"(??????-->??????) "+LGrade2.get(i).getClass_start_date()+" "+LGrade2.get(i).getSubject_name(),"5");
    				System.out.println("??????Grade_seq"+LGrade2.get(i).getGrade_seq());	    			
	    		}
	    		
	    		
	    		/**
	    		String class_no = LGrade2.get(i).getClass_no();
	    		List<Grade> LGrade3 = courseService.getGradeList("","",LGrade2.get(i).getGrade_seq(),"","","","","","",class_no,"","","","");
	    		String classEnddate = LGrade3.get(0).getClass_xth_date();
	    		String styleEnddate = LGrade3.get(0).getClass_style();
	    		
	    		if(classEnddate.length()>9) {
	    			classEnddate = classEnddate.substring(6,10)+classEnddate.substring(0,2)+classEnddate.substring(3,5);
	    			if(Integer.valueOf(nowDate)>=Integer.valueOf(classEnddate)){
	    				courseService.UpdateGradeStatus(LGrade2.get(i).getGrade_seq(),"5");
	    				courseService.scheduleTask(LGrade2.get(i).getGrade_seq(),"(??????-->??????) "+LGrade2.get(i).getClass_start_date()+" "+LGrade2.get(i).getSubject_name());
	    				System.out.println("??????Grade_seq"+LGrade2.get(i).getGrade_seq());
	    			}
	    		
	    		}else if(classEnddate.length()==0 && styleEnddate.equals("1")){
					courseService.UpdateGradeStatus(LGrade2.get(i).getGrade_seq(),"5");
					courseService.scheduleTask(LGrade2.get(i).getGrade_seq(),"(??????-->??????) "+LGrade2.get(i).getClass_start_date()+" "+LGrade2.get(i).getSubject_name());
					System.out.println("?????????????????????Grade_seq"+LGrade2.get(i).getGrade_seq());
	    		}
	    		**/
	    	}	
		}catch(Exception e) {
			e.printStackTrace();
		}	
		return true;
	}
	
	public boolean schedule_1a() {
		try {
			String nowDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
			//5:????????????-->8:Video??????
	    	List<Grade> LGrade = courseService.getGradeList("","","","","","","","","5","1","","","","","","","","1","0");
	    	for(int i=0;i<LGrade.size();i++) {
	    			String class_start_date = LGrade.get(i).getClass_start_date();
	    			//????????????????????????????????????
	    			if(class_start_date!=null && class_start_date.length()==8) {
		    			if(Integer.valueOf(nowDate)>=Integer.valueOf(class_start_date)+20000){
		    				courseService.UpdateGradeStatus(LGrade.get(i).getGrade_seq(),"8");
		    				courseService.scheduleTask(LGrade.get(i).getGrade_seq(),"(????????????-->Video??????) "+LGrade.get(i).getClass_start_date()+" "+LGrade.get(i).getSubject_name(),"8");
		    				System.out.println("Video??????Grade_seq"+LGrade.get(i).getGrade_seq());
		    			}
	    			}	
	    	}				
		}catch(Exception e) {
			e.printStackTrace();
		}		
		return true;
	}	
	
	public List<ScheduleActive> getScheduleActive(String schedule_id) {
		String sql = "SELECT * from eip.scheduleActive where schedule_id="+schedule_id;

		List<ScheduleActive> LScheduleActive = jdbcTemplate.query(sql,(result, rowNum) -> new ScheduleActive(
				result.getString("schedule_id"),
				result.getString("active")
		));
		return LScheduleActive;
	}	
}
