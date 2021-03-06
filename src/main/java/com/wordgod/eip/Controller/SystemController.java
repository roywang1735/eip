package com.wordgod.eip.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.wordgod.eip.Model.*;
import com.wordgod.eip.Service.AccountService;
import com.wordgod.eip.Service.AdmService;
import com.wordgod.eip.Service.CourseSaleService;
import com.wordgod.eip.Service.CourseService;
import com.wordgod.eip.Service.ManagerService;
import com.wordgod.eip.Service.SalesService;
import com.wordgod.eip.Service.SystemService;

import org.springframework.security.core.context.SecurityContextHolder;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class SystemController {
    @Autowired
    AccountService accountService;
    @Autowired
    ManagerService managerService;    
    @Autowired
    CourseService courseService; 
    @Autowired
    AdmService admService;
    @Autowired
    SalesService salesService; 
    @Autowired
    CourseSaleService courseSaleService; 
    @Autowired
    SystemService systemService;
	@Autowired
	private JdbcTemplate jdbcTemplate;    
    
    @RequestMapping(value="/")
    public String index(Model model,HttpServletRequest request,HttpSession session,Principal principal) {
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}    	
//log in
      if(principal!=null) {
    	String ip = request.getHeader("x-forwarded-for");
    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	ip = request.getHeader("Proxy-Client-IP");
    	}
    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	ip = request.getHeader("WL-Proxy-Client-IP");
    	}
    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    	ip = request.getRemoteAddr();
    	}
    	session.setAttribute("fromIp",ip); 
    	SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    	Date date = new Date(System.currentTimeMillis());
    	managerService.applicationLogSave(principal.getName(),"4","-1","-1",principal.getName(),ip,formatter.format(date),"","","");   	
         
    	//我的工作    	
    	List<String> LmyJob = new ArrayList<String>();
    	if(request.isUserInRole("cou_mgr")) {
    		List<Grade> LGrade= courseService.getGradeList("","","","","","","","","2","1","","","","","","","","1","");
    		for(int i=0;i<LGrade.size();i++) {
    			LmyJob.add("<span style='font-size:x-small'>"+LGrade.get(i).getUpdate_time()+"</span><A href='/Manager/CourseApproveFlow' style='text-decoration:underline;'>[ 課程簽核 ]</A> "+LGrade.get(i).getClass_start_date()+" "+LGrade.get(i).getSubject_name()+" <span style='font-size:small'>("+LGrade.get(i).getCreater()+")</span>");
    		}
    		List<ComboSale> LComboSale = courseSaleService.getComboSale("","","","2","","","","0");
    		for(int i=0;i<LComboSale.size();i++) {
    			LmyJob.add("<span style='font-size:x-small'>"+LComboSale.get(i).getUpdate_time()+"</span><A href='/Manager/ComboSaleFlow' style='text-decoration:underline;'>[ 課務簽核 ]</A> "+LComboSale.get(i).getCategory_name()+"-"+LComboSale.get(i).getName()+" <span style='font-size:small'>("+LComboSale.get(i).getCreater()+")</span>");
    		}    		
    	}
    	if(request.isUserInRole("cou")) {
    		List<Grade> LGrade= courseService.getGradeList("","","","","","","","","3","1","","","","","","","","1","");
    		for(int i=0;i<LGrade.size();i++) {
    			//String subject_name = courseService.getSubject("",LGrade.get(i).getSubject_id(),"","").get(0).getName();
    			LmyJob.add("<span style='font-size:x-small'>"+LGrade.get(i).getUpdate_time()+"</span><A href='/Course/CourseSetting' style='text-decoration:underline;'>[ 課程待上架 ]</A> "+LGrade.get(i).getClass_start_date()+" "+LGrade.get(i).getSubject_name()+" <span style='font-size:small'>("+LGrade.get(i).getCreater()+")</span>");
    		}
    		
    		List<ComboSale> LComboSale = courseSaleService.getComboSale("","","","3","","","","0");
    		for(int i=0;i<LComboSale.size();i++) {
    			LmyJob.add("<span style='font-size:x-small'>"+LComboSale.get(i).getUpdate_time()+"</span><A href='/CourseSale/ComboSaleSetting' style='text-decoration:underline;'>[ 課務待上架 ]</A> "+LComboSale.get(i).getCategory_name()+"-"+LComboSale.get(i).getName()+" <span style='font-size:small'>("+LComboSale.get(i).getCreater()+")</span>");
    		}      		
    		
    		List<Grade> LGrade2= courseService.getGradeList("","","","","","","","","6","1","","","","","","","","1","");
    		for(int i=0;i<LGrade2.size();i++) {
    			//String subject_name = courseService.getSubject("",LGrade2.get(i).getSubject_id(),"","").get(0).getName();
    			LmyJob.add("<span style='font-size:x-small'>"+LGrade2.get(i).getUpdate_time()+"</span><A href='/Course/CourseSetting' style='text-decoration:underline;'>[ 課程退件 ]</A> "+LGrade2.get(i).getClass_start_date()+" "+LGrade2.get(i).getSubject_name()+" <span style='font-size:small'>("+LGrade2.get(i).getCreater()+")</span>");
    		}
    		
    		List<ComboSale> LComboSale2 = courseSaleService.getComboSale("","","","6","","","","0");
    		for(int i=0;i<LComboSale2.size();i++) {
    			LmyJob.add("<span style='font-size:x-small'>"+LComboSale2.get(i).getUpdate_time()+"</span><A href='/CourseSale/ComboSaleSetting' style='text-decoration:underline;'>[ 課務退件 ]</A> "+LComboSale2.get(i).getCategory_name()+"-"+LComboSale2.get(i).getName()+" <span style='font-size:small'>("+LComboSale2.get(i).getCreater()+")</span>");
    		} 		
    	} 
		
		//下架通知
		if(request.isUserInRole("adm") || request.isUserInRole("adm_mgr")) {
			List<Grade> LGrade3= courseService.getGradeList("","","","","","","","","5","","","","","","","","","1",""); //已下架之每堂課
			Calendar now = Calendar.getInstance();
			now.add(Calendar.DATE,-2); //僅顯示更新後三天資料
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			//int nowTime = Integer.valueOf(format.format(now.getTime()));
			int updateTime = -1;
			int classTime = -1;
			String tmp = "";
			ArrayList<String> Lgrade_seq = new ArrayList<String>();
			for(int i=0;i<LGrade3.size();i++) {
				updateTime = Integer.valueOf(LGrade3.get(i).getUpdate_time().replace("-",""));
				//if(updateTime>=nowTime) {
					//大於更新日的所有課程(人工下架)
					tmp=LGrade3.get(i).getClass_xth_date();
					if(tmp.length()>9) {
						classTime= Integer.valueOf(tmp.substring(6,10)+tmp.substring(0,2)+tmp.substring(3,5)); //ex.20191102
						if(classTime>updateTime) {
					            if (!Lgrade_seq.contains(LGrade3.get(i).getGrade_seq())) { 				  
					            	Lgrade_seq.add(LGrade3.get(i).getGrade_seq()); 
					            } 
					    }
					}
				//}
			}			
			for(int i=0;i<Lgrade_seq.size();i++) {
				List<Grade> LGrade4= courseService.getGradeList("","",Lgrade_seq.get(i),"","","","","","","1","","","","","","","","1","");
				LmyJob.add("<span style='font-size:x-small'>"+LGrade4.get(0).getUpdate_time()+"</span>[ <A href='/Adm/classRemark' style='text-decoration:underline;'><span style='background-color:#FDF6F6'>下架通知</span></A> ]"+LGrade4.get(0).getClass_start_date()+" "+LGrade4.get(0).getSubject_name()+" <span style='font-size:x-small'>("+LGrade4.get(0).getCreater()+")</span>");
			}				
		}
		//上架變更通知
		if(request.isUserInRole("adm") || request.isUserInRole("adm_mgr")) {
			    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
			    String today = sdFormat.format(new Date());	
				List<ApplicationLog> LApplicationLog = managerService.getApplicationLog("2","","");
				for(int i=0;i<LApplicationLog.size();i++) {
					String imgAlert = "";
					if(LApplicationLog.get(i).getCol1().equals("上架修改")) {
					   if(LApplicationLog.get(i).getUpdate_time().equals(today))	{
						   imgAlert = "<img src='/images/new.png' style='width:25px'>";
					   }
					   LmyJob.add("<span style='font-size:x-small'>"+LApplicationLog.get(i).getUpdate_time()+"</span>[ <A href='/Course/newCourse?action=view&grade_seq="+LApplicationLog.get(i).getCol6()+"' style='text-decoration:underline;'><span style='background-color:#FDF6F6'>上架變更通知</span></A> ]"+LApplicationLog.get(i).getCol5()+" "+LApplicationLog.get(i).getCol4()+" <span style='font-size:x-small'>("+LApplicationLog.get(i).getUpdater()+") "+imgAlert+"</span>");				
					}
				}
		}
		
		  Collections.sort(LmyJob, Collections.reverseOrder());
		  
	        	
    	model.addAttribute("LmyJob",LmyJob);
    	
    	//公佈欄    
    	List<String> LmyBillBoard = new ArrayList<String>();
    	List<Suspension> LSuspension = admService.getSuspension("");
    	for(int i=0;i<LSuspension.size();i++) {
    		LmyBillBoard.add("[ 休假 ] : "+LSuspension.get(i).getReason()+"-"+LSuspension.get(i).getSuspension_date());
    	}
    	model.addAttribute("LmyBillBoard",LmyBillBoard);
      }	else {
  		 return "/System/login";
  	  }
        return "/common/entry";
    }
    
    @RequestMapping(value="/noAuthority")
    public String noAuthority() {
        return "/common/noAuthority";
    }    
    
    
    @RequestMapping("/login")
    public String login(HttpSession session) {
		session.setAttribute("menu","1");  	
     return "/System/login";
    }
    
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
            org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null){   
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
     return "redirect:/login";
     //return "/Account/logout";
    }    

    @RequestMapping("/login-error")
    public String loginError(Model model) {
     model.addAttribute("loginError", true);
     return "/System/login";
    }    
    
    @RequestMapping("/System/AccountSearch")
    public String AccountSearch(Model model,HttpSession session,HttpServletRequest request) {
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
		
    	List<Department> departmentGroup = accountService.getDepartment("");
    	model.addAttribute("departmentGroup", departmentGroup);
    	
    	List<School> schoolGroup = accountService.getSchool("","");
    	model.addAttribute("schoolGroup", schoolGroup); 
  	
        return "/System/AccountSearch";
    }
 
    @RequestMapping("/System/AccountView")
    public String AccountView(HttpServletRequest request,Model model) {
    	
    	
    	Employee employee = accountService.getAccountByID(request.getParameter("employee_seq"),"");
    	model.addAttribute("employee", employee);
    	
        List<Department> departmentGroup = accountService.getDepartment("");
        model.addAttribute("departmentGroup", departmentGroup);
        
    	List<School> schoolGroup = accountService.getSchool("","");
    	model.addAttribute("schoolGroup", schoolGroup); 
    	
    	List<Authority> authorityGroup = accountService.getAuthority();
    	model.addAttribute("authorityGroup", authorityGroup); 
    	
    	List<Account_authority> LAccount_authority = accountService.getAccount_authority(employee.getAccount0());

    	ArrayList<String> listAuthority = new ArrayList<String>();
    	String tmp = null;
    	for(int i=0;i<authorityGroup.size();i++) {
    		tmp = "";
    		for(int j=0;j<LAccount_authority.size();j++) {
    			if(authorityGroup.get(i).getCode().equals(LAccount_authority.get(j).getAuthority())) {tmp="checked";} 
    		}
    		listAuthority.add("<input type='checkbox' name='authorityCode' value='"+authorityGroup.get(i).getCode()+"' "+tmp+"> "+authorityGroup.get(i).getName()+"<br>");
    	}
    	model.addAttribute("listAuthority", listAuthority); 
    	
        return "/System/AccountView";
    } 
    
   
    @RequestMapping("/System/AccountCreate")
    
    public String AccountCreate(Model model,Principal principal) {

        List<Department> departmentGroup = accountService.getDepartment("");
        model.addAttribute("departmentGroup", departmentGroup);
        
    	List<School> schoolGroup = accountService.getSchool("","");
    	model.addAttribute("schoolGroup", schoolGroup);    
    	
    	List<Authority> authorityGroup = accountService.getAuthority();
    	model.addAttribute("authorityGroup", authorityGroup); 
    	
    	Employee employee = new Employee();
    	employee.setCreater(principal.getName());
    	model.addAttribute("employee", employee);
        
    	return "/System/AccountCreate";        
    }    
    
 
    @RequestMapping("/System/getEmployee")
    @ResponseBody
    public List<Employee> getEmployee(HttpServletRequest request){
    	List<Employee> LEmployee = accountService.getEmployee(
        		request.getParameter("ch_name"),
        		request.getParameter("en_name"),
        		request.getParameter("school"),
        		request.getParameter("tel"),
        		request.getParameter("email"),
        		request.getParameter("department"),
        		"",
        		request.getParameter("enabled")
        );
    	
    	String tmp = null;
    	for(int i=0;i<LEmployee.size();i++) {
    		List<Account_authority> LAccount_authority = accountService.getAccount_authority(LEmployee.get(i).getAccount0());
    		tmp = "";
    		for(int j=0;j<LAccount_authority.size();j++) {
    			tmp += LAccount_authority.get(j).getAuthority_name()+"<br>";
    		}
    		LEmployee.get(i).setAuthority_name(tmp);
    	}  	
    	return LEmployee;
    } 
    
    @RequestMapping(value="/System/AccountSave", method=RequestMethod.POST)
    public String AccountSave(@Valid Employee employee,BindingResult bindingResult,Model model,HttpServletRequest request,Principal principal) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("departmentGroup", accountService.getDepartment(""));
        	model.addAttribute("schoolGroup", accountService.getSchool("","")); 
        	model.addAttribute("authorityGroup", accountService.getAuthority());         	
            return "redirect:/System/AccountSearch";
            
        }
        
    	String authorityCode[] = request.getParameterValues("authorityCode");
    	
    	List<Account_authority> LAccount_authority = new ArrayList<Account_authority>();   	
    	if(authorityCode.length>0) {
    	  for(int i=0;i<authorityCode.length;i++) {
    		Account_authority account_authority = new Account_authority();
    		account_authority.setAccount0(employee.getAccount0());
    		account_authority.setAuthority(authorityCode[i]);
    		LAccount_authority.add(account_authority);
    	  }
    	}  
    	employee.setLAccount_authority(LAccount_authority);
    	
    	accountService.saveAccount(employee);
    	String schoolName = accountService.getSchool(employee.getSchool(),"").get(0).getName();
    	String depName = accountService.getDepartment(employee.getDepartment()).get(0).getName();
    	managerService.applicationLogSave(principal.getName(),"1","-1","-1","新增帳號",schoolName,depName,employee.getCh_name(),"","");
        return "/System/AccountSearch";
    } 
    
    @RequestMapping(value="/System/AccountUpdate", method=RequestMethod.POST)
    public String AccountUpdate(@Valid Employee employee,BindingResult bindingResult,Model model,HttpServletRequest request,Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departmentGroup", accountService.getDepartment(""));
        	model.addAttribute("schoolGroup", accountService.getSchool("","")); 
        	model.addAttribute("authorityGroup", accountService.getAuthority());         	
            return "/System/AccountCreate";
            
        }
        
    	String authorityCode[] = request.getParameterValues("authorityCode");
    	
    	List<Account_authority> LAccount_authority = new ArrayList<Account_authority>();   	
    	if(authorityCode.length>0) {
    	  for(int i=0;i<authorityCode.length;i++) {
    		Account_authority account_authority = new Account_authority();
    		account_authority.setAccount0(employee.getAccount0());
    		account_authority.setAuthority(authorityCode[i]);
    		LAccount_authority.add(account_authority);
    	  }
    	}  
    	employee.setLAccount_authority(LAccount_authority);
    	if(request.getParameter("drowssap")!=null) {
    		employee.setDrowssap(request.getParameter("drowssap"));
    	}    	
    	accountService.updateAccount(employee);
    	String schoolName = accountService.getSchool(employee.getSchool(),"").get(0).getName();
    	String depName = accountService.getDepartment(employee.getDepartment()).get(0).getName();
    	managerService.applicationLogSave(principal.getName(),"1","-1","-1","修改帳號",schoolName,depName,employee.getCh_name(),"","");
        return "redirect:/System/AccountSearch";
    } 
    
    @RequestMapping(value="/System/AccountUpdate2")
    public String AccountUpdate2(@Valid Employee employee,@RequestParam("file") MultipartFile file,@Value("${UploadPath}") String UploadPath) {
    	accountService.updateAccount2(employee);
    	//檔案上傳
        if (!file.isEmpty()) {
           try {
           	byte[] bytes = file.getBytes();
           	String fileName = file.getOriginalFilename();
           	fileName = employee.getEmployee_seq()+fileName.substring(fileName.indexOf('.'));
    		//刪除同名照片
    		File dir = new File(UploadPath+"studentPhoto");
    		File[] files = dir.listFiles((d, name) -> name.startsWith(employee.getEmployee_seq()));
    		for(int i=0;i<files.length;i++) {
    			files[i].delete();
    		}
            //新增
           	Path path = Paths.get(UploadPath+"employeePhoto/" + fileName);
           	Files.write(path, bytes);
           }catch(Exception e) {
               e.printStackTrace();
           }
        } 
        return "/common/successful";
    }     

    @RequestMapping("/System/Migration")
    public String Migration() {
    	return "/System/Migration";
    }
    
//政龍學生    
    @RequestMapping("/System/Migration1")
    public String Migration1() {
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
    	//String sql = "select * from eip.JLM_StudentInfo order by 建檔日期 desc limit 2000";
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
			    result.getString("更新人員"),
			    result.getString("更新時間"),
			    result.getString("自訂欄位5"),
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
        
     return "/System/Migration";
    }  
    
//政龍學生相片
    @RequestMapping("/System/Migration2")
    public String Migration2(@Value("${UploadPath}") String UploadPath) throws SQLException, IOException {

        Connection conn = DriverManager.getConnection("jdbc:sqlserver://192.168.0.240:1433;databaseName=JLImage","sa","0080");       
		String sql = "SELECT 照片,學號 FROM [JLImage].[dbo].[學生照片]";
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(sql);
        byte[] fileBytes;
        String studentNo="";
        while(rs.next())
       {
        	System.out.print(".");
        	     studentNo = rs.getString(2);
        	     fileBytes = rs.getBytes(1);
                 OutputStream targetFile=  new FileOutputStream(UploadPath+"studentPhoto//"+studentNo+".bmp");
                 targetFile.write(fileBytes);
                 targetFile.close();
       } 
        System.out.println("學生照片完畢");
	     return "redirect:/System/Migration";
	    }  

//政龍期別
    @RequestMapping("/System/Migration3")
    public String Migration3() {
    	jdbcTemplate.update("delete from eip.JLM_gradeAll;");
    	
    	//搜尋政龍
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
	
		
		
     return "redirect:/System/Migration";
    }      
    
    @RequestMapping("/System/SchoolSetting")
    public String SchoolSetting(Model model,HttpSession session,HttpServletRequest request) {
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
		
    	List<School> LSchool = accountService.getSchool("","");
    	model.addAttribute("LSchool",LSchool); 
    	return "/System/SchoolSetting";
    }
    
    @RequestMapping(value="/System/AccountDisable", method=RequestMethod.POST)
    public String AccountDisable(HttpServletRequest request,Principal principal) {
    	String employee_seq = request.getParameter("employee_seq2");
    	accountService.disableAccount(employee_seq,principal.getName());
    	Employee employee = accountService.getAccountByID(employee_seq,"");
    	String schoolName = accountService.getSchool(employee.getSchool(),"").get(0).getName();
    	String depName = accountService.getDepartment(employee.getDepartment()).get(0).getName();    	
    	managerService.applicationLogSave(principal.getName(),"1","-1","-1","停用帳號",schoolName,depName,employee.getCh_name(),"","");
        return "/System/AccountSearch";
    }  
    
    @RequestMapping("/System/loginLog")
    public String loginLog() {
    	return "/System/loginLog";
    }
    
    @RequestMapping("/System/scheduleLog")
    public String scheduleLog(HttpSession session,HttpServletRequest request) {
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}    	
    	return "/System/scheduleLog";
    } 
    
    @RequestMapping("/System/scheduleLog2")
    public String scheduleLog2() {
    	return "/System/scheduleLog2";
    } 
    
    @RequestMapping("/System/scheduleLog2a")
    public String scheduleLog2a() {
    	return "/System/scheduleLog2a";
    }     
    
    @RequestMapping("/System/scheduleLog3")
    public String scheduleLog3() {
    	return "/System/scheduleLog3";
    }     
    
    @RequestMapping("/System/serverLog")
    public String serverLog() {
    	return "/System/serverLog";
    }
    
    @RequestMapping("/System/dbLog")
    public String dbLog() {
    	return "/System/dbLog";
    }     
    
    @RequestMapping("/System/getApplicationLog")
    @ResponseBody
    public List<ApplicationLog> getApplicationLog(HttpServletRequest request){
        return managerService.getApplicationLog(request.getParameter("fun1"),"","");
    } 
    
    @RequestMapping("/System/categorySetting")
    public String categorySetting(Model model) {
		List<Category> LCategory = courseService.getCategory("","");
		model.addAttribute("LCategory", LCategory);
    	return "/System/categorySetting";
    }  
    
    @RequestMapping(value="/System/categoryUpdate", method=RequestMethod.POST)
    public String categoryUpdate(HttpServletRequest request) {
    	String category_seq[] = request.getParameterValues("category_seq");
    	String bgColor[] = request.getParameterValues("bgColor");
    	String ranks[] = request.getParameterValues("ranks");
    	
    	for(int i=0;i<category_seq.length;i++) {
    		Category category = new Category(category_seq[i],"",true,bgColor[i],ranks[i]);
    		systemService.categoryUpdate(category);
    	}
    	return "redirect:/System/categorySetting";
    }

    @RequestMapping("/System/JL_grade")
    public String JL_grade(Model model) {
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
				
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		List<FlowStatus> flowStatusGroup = courseService.getFlowStatus();
		model.addAttribute("flowStatusGroup", flowStatusGroup);	
    	return "/System/JL_grade";
    }  
    
    @RequestMapping("/System/getJL_gradeList")
    @ResponseBody
    public List<JLM_gradeAll> getJL_gradeList(HttpServletRequest request){
    	String category_id = request.getParameter("category_id");
    	String gradeName = request.getParameter("gradeName");
    	String gradeId = request.getParameter("gradeId");
        return systemService.getJL_gradeList(category_id,gradeName,gradeId);
    }  
    
    @RequestMapping("/System/excel_gradeMap")
    public String excel_gradeMap(Model model){
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
    	        		System.out.println("********"+categoryName);
    	        		category_id = "1";
    	        	}else if(categoryName.contains("托福")) {
    	        		System.out.println("********"+categoryName);
    	        		category_id = "2";
    	        	}else if(categoryName.contains("GMAT")) {
    	        		System.out.println("********"+categoryName);
    	        		category_id = "3";
    	        	}else if(categoryName.contains("GRE")) {
    	        		System.out.println("********"+categoryName);
    	        		category_id = "4";
    	        	}else if(categoryName.contains("雅思")) {
    	        		System.out.println("********"+categoryName);
    	        		category_id = "5";
    	        	}else if(categoryName.contains("多益")) {
    	        		System.out.println("********"+categoryName);
    	        		category_id = "6";
    	        	}else if(categoryName.contains("CFA L1")) {
    	        		System.out.println("********"+categoryName);
    	        		category_id = "7";
    	        	}else if(categoryName.contains("CFA L2")) {
    	        		System.out.println("********"+categoryName);
    	        		category_id = "8";
    	        	}else {
    	        		System.out.println("********?????無法讀取標籤"+categoryName);
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
	    					grade_date = class_start_date_0;
	    					if(!class_start_date_0.equals("") && class_start_date_0.length()>=6) {
	    						if(class_start_date_0.contains("&")) { //用於字神開營/A班/...原有期別, 放在期別別名
	    							String[] tokens = class_start_date_0.split("&");
	    							video_date = tokens[1];
	    						}else {
	    							video_date = "";
	    						}
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
    								jdbcTemplate.update("Insert INTO eip.grade VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURDATE(),?,?,?,?,?,?);",
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
	    								    grade_date,
	    								    1,
	    								    "0",
	    								    "0"
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
										"(政龍)",
										1,
										class_trial,
										"",
										xth+1
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
    	return "redirect:/System/Migration";
    }
    
    @RequestMapping("/System/excel_pay")
    public String excel_pay(Model model){
    	File folder = new File("D:\\importFrom\\pay\\");
    	File[] listOfFiles = folder.listFiles();
    	String JL_gradeId = "";
    	String student_no = "";
    	String payMoney = "";
    	try {
    	  for (File file : listOfFiles) { //每個File
    	    if (file.isFile()) { 
System.out.println("@@@@@@@@@"+file.getName()+"@@@@@@@@@");
    			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
    	        for (int x=0;x<wb.getNumberOfSheets();x++) { //每個頁籤
    	        	JL_gradeId = wb.getSheetName(x);
System.out.println("########"+JL_gradeId+"########");
					XSSFSheet sheet=wb.getSheetAt(x);
					XSSFRow row; 
						for(int k=1;k<sheet.getPhysicalNumberOfRows();k++) {//每列
							row=sheet.getRow(k);
							student_no = row.getCell(0).getStringCellValue();
							payMoney = row.getCell(1).getStringCellValue(); 
						}	
    	        }
    	    }
    	  }  
        }catch(Exception e) {e.printStackTrace();}
        return "redirect:/System/Migration";
    } 
    
    @RequestMapping("/System/ComboBuild")
    public String ComboBuild() {
    	systemService.ComboBuild(); 
        return "redirect:/System/Migration";
    }
    
    @RequestMapping("/System/eip_register")
    public String eip_register(Model model) {
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
		
    	
		List<JL_EIP_grade> LJL_EIP_grade = systemService.getJL_EIP_grade("",""); //政龍EIP期別對應
		List<JLM_studentPay> LJLM_studentPay = salesService.getJLM_studentPay("","不上了","",""); //政龍學生訂期別及付款
		
		for(int i=0;i<LJL_EIP_grade.size();i++) {
			String subject_id = LJL_EIP_grade.get(i).getSubject_id();
			String student_seq = "";
			String comboSale_seq = "";
			String orderStatus = "";
			String originPrice = "";
			int actualPrice = 0;
			int paid = 0;
			for(int j=0;j<LJLM_studentPay.size();j++) {
			  if(LJLM_studentPay.get(j).getGradeId().equals(LJL_EIP_grade.get(i).getJL_gradeId())) {
				  
				    student_seq = salesService.getStudent("","","",LJLM_studentPay.get(j).getStudent_no(),"","","","","","").get(0).getStudent_seq(); 					
					comboSale_seq = courseSaleService.getComboSaleBySubject("0",subject_id).get(0).getComboSale_seq();
					originPrice = LJLM_studentPay.get(j).getOriginPrice();
					actualPrice = Integer.valueOf(LJLM_studentPay.get(j).getNeedPay());
					paid =  Integer.valueOf(LJLM_studentPay.get(j).getPaidMoney().equals("")?"0":LJLM_studentPay.get(j).getPaidMoney());
					if(paid>=actualPrice) {
						orderStatus = "2";//已結清
					}else {
						orderStatus = "1";
					}
				  if(LJL_EIP_grade.get(i).getClass_style().equals("1") || LJL_EIP_grade.get(i).getClass_style().equals("2")) {	//面授及Video	
							//----eip.Register----//
							jdbcTemplate.update("INSERT INTO eip.Register VALUES (default,?,?,?,?,?,?,?,'',0);",
								student_seq,
								originPrice,
								actualPrice,
								paid,
								"政龍", //comment
								LJLM_studentPay.get(j).getSalePerson(), //creater
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
			   					LJL_EIP_grade.get(i).getEip_grade_seq(), //grade_id
			   					1, //register_status
			   					LJL_EIP_grade.get(i).getClass_style(), //class_style
			   					LJL_EIP_grade.get(i).getSchoolAbbr(),//school_code
			   					LJLM_studentPay.get(j).getSitNo(), //sitNo
			   					1
				   			);
				    		int z = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
		                    //----eip.signRecordHistory----//
					    		List<Grade> LGrade = courseService.getGradeList("","",LJL_EIP_grade.get(i).getEip_grade_seq(),"","","","","","","1","","","","","","","","1","");
					    		if(LGrade.size()>0) { //舊得無法有上課紀錄
						    		for(int k=0;k<Integer.valueOf(LGrade.get(0).getClass_no());k++) {
							    		jdbcTemplate.update("INSERT INTO eip.signRecordHistory VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
											z, //Register_comboSale_grade_id
											1, //register_status
											student_seq, //student_id
											LJL_EIP_grade.get(i).getClass_style(), //class_style
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
				   }else if(LJL_EIP_grade.get(i).getClass_style().equals("0")) {	//未訂
							//----eip.Register----//
							jdbcTemplate.update("INSERT INTO eip.Register VALUES (default,?,?,?,?,?,?,?,'',0);",
								student_seq,
								Integer.valueOf(LJLM_studentPay.get(j).getOriginPrice()), //originPrice
								Integer.valueOf(LJLM_studentPay.get(j).getNeedPay()), //actualPrice
								Integer.valueOf(LJLM_studentPay.get(j).getPaidMoney().equals("")?"0":LJLM_studentPay.get(j).getPaidMoney()), //paid
								"政龍", //comment
								LJLM_studentPay.get(j).getSalePerson(), //creater
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
		}
    	return "redirect:/System/Migration";	
    } 
 
    
    @RequestMapping("/System/eip_register2")
    public String eip_register2(Model model) {
		systemService.eip_register2();       
    	return "redirect:/System/Migration";	
    }     
   
    @RequestMapping("/System/getScheduleTask")
    @ResponseBody
    public List<ScheduleTask> getScheduleTask(HttpServletRequest request) {
    	List<ScheduleTask> LScheduleTask = systemService.getScheduleTask(request.getParameter("FlowStatus"));
    	return LScheduleTask;
    }
    
    @RequestMapping("/System/JL_gradeRegister")
    public String JL_gradeRegister(Model model) {
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
				
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		List<FlowStatus> flowStatusGroup = courseService.getFlowStatus();
		model.addAttribute("flowStatusGroup", flowStatusGroup);	
    	return "/System/JL_gradeRegister";
    } 
    
    @RequestMapping("/System/getJLM_gradeRegister")
    @ResponseBody
    public List<JLM_gradeRegister> getJLM_gradeRegister(Model model,HttpServletRequest request) {
    	String student_no = request.getParameter("student_no");
    	List<JLM_gradeRegister> LJLM_gradeRegister = systemService.getJLM_gradeRegister(student_no,"");
    	return LJLM_gradeRegister;
    }
    
    @RequestMapping("/System/JL_studentPay")
    public String JL_studentPay(Model model) {
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
				
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		List<FlowStatus> flowStatusGroup = courseService.getFlowStatus();
		model.addAttribute("flowStatusGroup", flowStatusGroup);	
    	return "/System/JL_studentPay";
    }
    
    @RequestMapping("/System/getJLM_studentPay")
    @ResponseBody
    public List<JLM_studentPay> getJLM_studentPay(Model model,HttpServletRequest request) {
    	String student_no = request.getParameter("student_no");
    	List<JLM_studentPay> LJLM_studentPay = salesService.getJLM_studentPay(student_no,"","1000","");   
    	return LJLM_studentPay;
    }
    
    
    @RequestMapping("/System/JL_studentPayRecord")
    public String JL_studentPayRecord(Model model) {
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
				
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		List<FlowStatus> flowStatusGroup = courseService.getFlowStatus();
		model.addAttribute("flowStatusGroup", flowStatusGroup);	
    	return "/System/JL_studentPayRecord";
    }
       
    @RequestMapping("/System/getJLM_studentPayRecord")
    @ResponseBody
    public List<JLM_studentPayRecord> getJLM_studentPayRecord(Model model,HttpServletRequest request) {
    	String student_no = request.getParameter("student_no");
    	List<JLM_studentPayRecord> LJLM_studentPayRecord = salesService.getJLM_studentPayRecord(student_no,"","1000");
    	return LJLM_studentPayRecord;
    }    

    @RequestMapping("/System/getServerLog")
    @ResponseBody
    public String getServerLog(HttpServletRequest request) {   	
    	String fileName = System.getProperty("catalina.base")+"/logs/" + request.getParameter("fileName");
    	String returnStr = "";
    	try {
    		  InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
    		  BufferedReader br = new BufferedReader(isr); 	  
    	      String st;
	    	  while ((st = br.readLine()) != null) { 
	    		  returnStr += st+"<br>";
	    	  } 
    	}catch(Exception e) {e.printStackTrace();}	  
    	return returnStr;
    } 
    
    @RequestMapping("/System/ExcelRoleCall")
    public String ExcelRoleCall(Model model) {
    	return "/System/ExcelRoleCall";
    } 
    
	/*
	 * @RequestMapping("/System/ExcelEntityRoleCall") public String
	 * ExcelEntityRoleCall(Model model) {
	 * 
	 * File folder = new File("D:\\importFrom\\ExcelEntityRoleCall\\"); File[]
	 * listOfFiles = folder.listFiles();
	 * 
	 * try { for (File file : listOfFiles) { //每個File if (file.isFile()) { String
	 * fileName = file.getName(); System.out.println("@@@@@@@@@@@@@@@@@@"+fileName);
	 * String[] fn = fileName.substring(0,fileName.length()-5).split("-"); String
	 * school_code = fn[0]; String subject_abbr = fn[1]; String class_start_date =
	 * fn[2]; class_start_date =
	 * fn[2].substring(2,4)+"/"+fn[2].substring(4,6)+"/"+"20"+fn[2].substring(0,2);
	 * String teacher_code = fn[3]; String class_th = ""; String student_no = "";
	 * String attend = ""; String TakeHandout = ""; XSSFWorkbook wb = new
	 * XSSFWorkbook(new FileInputStream(file));
	 * 
	 * for (int x=0;x<wb.getNumberOfSheets();x++) { class_th = wb.getSheetName(x);
	 * XSSFSheet sheet=wb.getSheetAt(x); XSSFRow row; XSSFCell cell;
	 * 
	 * Iterator<?> rows = sheet.rowIterator(); int i=0;
	 * 
	 * while (rows.hasNext()){ i++; row=(XSSFRow) rows.next(); Iterator<?> cells =
	 * row.cellIterator(); //取消Row1 if(i!=1) { int j=0; while (cells.hasNext()){
	 * j++; cell=(XSSFCell) cells.next(); //選取 Col1,Col3,Col4 if(j==1) {student_no =
	 * cell.getStringCellValue().trim();} if(j==3) {attend =
	 * cell.getStringCellValue().trim();} if(j==4) {TakeHandout =
	 * cell.getStringCellValue().trim();}
	 * admService.UpdateSignRecord(subject_abbr,school_code,teacher_code,class_th,
	 * student_no,attend,TakeHandout,"System","",""); } } } } wb.close(); } }
	 * }catch(Exception e) { e.printStackTrace(); } return "/System/ExcelRoleCall";
	 * }
	 */
    
    
    @RequestMapping("/System/ExcelVideoRoleCall")
    public String ExcelVideoRoleCall(Model model) {
    	return "/System/ExcelRoleCall";
    }
    
    @RequestMapping("/System/groupAuthority")
    public String groupAuthority() {
    	return "/System/groupAuthority";
    } 
    
    @RequestMapping("/System/mainFlow")
    public String mainFlow() {
    	return "/System/mainFlow";
    }
    
    @RequestMapping("/System/open/systemAnalysis")
    public String systemAnalysis(HttpSession session,HttpServletRequest request) {  	
    	return "/System/open/systemAnalysis";
    }
    
    @RequestMapping("/System/open/eipStructure")
    public String eipStructure(HttpSession session,HttpServletRequest request) {
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}    	
    	return "/System/open/eipStructure";
    }    
    
    @RequestMapping("/System/open/gradeShelf")
    public String gradeShelf() {
    	return "/System/open/gradeShelf";
    } 
    
    @RequestMapping("/System/open/gradeRefund")
    public String gradeRefund() {
    	return "/System/open/gradeRefund";
    } 
    
    @RequestMapping("/System/open/admFlow")
    public String admFlow() {
    	return "/System/open/admFlow";
    }  
    @RequestMapping("/System/open/makeUpNo")
    public String makeUpNo() {
    	return "/System/open/makeUpNo";
    } 
    @RequestMapping("/System/open/projectDef")
    public String projectDef() {
    	return "/System/open/projectDef";
    }     
    
    
    @RequestMapping("/System/getSubject_tmp")
    @ResponseBody
    public List<Subject_tmp> getSubject_tmp(Model model,HttpServletRequest request) {
    	List<Subject_tmp> LSubject_tmp = courseService.getSubject_tmp("","","");
    	return LSubject_tmp;
    } 
    
    @RequestMapping("/System/preSetting1")
    public String preSetting1(Model model,HttpServletRequest request) {
		//jdbcTemplate.update("delete from eip.subjectTeacher;");
		//jdbcTemplate.update("ALTER TABLE eip.subjectTeacher AUTO_INCREMENT = 1;");	    	
    	List<Subject> LSubject = courseService.getSubject("","","","","","0");
    	for(int i=0;i<LSubject.size();i++) {
    		//在此科目有期別的老師
    		List<SubjectTeacher> LSubjectTeacher = courseService.getSubjectTeacher(LSubject.get(i).getSubject_seq());
    		//在eip.subjectTeacher存在的老師
    		List<SubjectTeacher> LSubjectTeacher2 = courseService.getSubjectTeacher2("","",LSubject.get(i).getSubject_seq(),"1","1");
    		Boolean exist = false;
    		for(int a=0;a<LSubjectTeacher.size();a++) {
    			for(int x=0;x<LSubjectTeacher2.size();x++) {
    				if(LSubjectTeacher.get(a).getTeacher_id().equals(LSubjectTeacher2.get(x).getTeacher_id())) {
    					exist = true;
    				}
    			}
    			if(!exist) {
	    			jdbcTemplate.update("INSERT INTO eip.subjectTeacher VALUES (default,?,?,?,?,?,?,?,NOW(),?)",
	    					LSubject.get(i).getSubject_seq(),
	    					LSubjectTeacher.get(a).getTeacher_id(),
	    					1,
	    					1,
	    					0,
	    					"",
	    					"system",
	    					1 //active
	    			);
    			}
    		}
    	}
    	return "/System/Migration";
    } 
  
    
    
    @RequestMapping("/System/w_report")
    public String w_report(Model model,@Value("${ExcelReportPath}") String ExcelReportPath) {    
		
//*******************資料來源******************//
		DateFormat dateFormat = new SimpleDateFormat("MM/__/yyyy");
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM");
    	String status_code = "";
    	String school_code = "";
    	String subject_id  = ""; 
    	String teacher_id  = "";
    	String category_id  = "";

    	List<Grade> LGrade = courseService.getGrade2("",status_code,subject_id,school_code,"",category_id,teacher_id," a.grade_date desc",dateFormat.format(new Date()));		
//*******************************************//
    	
    	
		    XSSFWorkbook workbook = new XSSFWorkbook(); 
		    XSSFSheet spreadsheet = workbook.createSheet("月報表");
		    spreadsheet.setColumnWidth(0,3600);
		    spreadsheet.setColumnWidth(1,3600);
		    spreadsheet.setColumnWidth(2,3600);
		    spreadsheet.setColumnWidth(3,3600);
		    spreadsheet.setColumnWidth(4,3600);
		    spreadsheet.setColumnWidth(5,3600);
		    spreadsheet.setColumnWidth(6,3600);
		    spreadsheet.setColumnWidth(7,3600);
		    spreadsheet.setColumnWidth(8,3600);
		    spreadsheet.setColumnWidth(9,3600);
		    spreadsheet.setColumnWidth(10,3600);
		    spreadsheet.setColumnWidth(11,3600);
		    spreadsheet.setColumnWidth(12,3600);
		    spreadsheet.setColumnWidth(13,3600);
		    spreadsheet.setColumnWidth(14,3600);
		    spreadsheet.setColumnWidth(15,3600);
		    
		    XSSFRow row;
		    
		    Cell cell;
			CellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
			
			XSSFFont font1 = workbook.createFont();
			font1.setColor(HSSFColor.WHITE.index); // 顏色
			font1.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗體字
			font1.setFontName("標楷體");
			font1.setFontHeightInPoints((short) 11);			  
			style.setFont(font1); // 設定字體
		    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
 
		    style.setBorderTop(CellStyle.BORDER_THIN);
		    style.setBorderRight(CellStyle.BORDER_THIN);
		    style.setBorderBottom(CellStyle.BORDER_THIN);
		    style.setBorderLeft(CellStyle.BORDER_THIN);		    
			row = spreadsheet.createRow(0);
			style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 水平置中
			
			cell = row.createCell(0);cell.setCellValue("結業日期");cell.setCellStyle(style);
			cell = row.createCell(1);cell.setCellValue("開課分校");cell.setCellStyle(style);
			cell = row.createCell(2);cell.setCellValue("科目");cell.setCellStyle(style);
			cell = row.createCell(3);cell.setCellValue("開課日期");cell.setCellStyle(style);
			cell = row.createCell(4);cell.setCellValue("期別");cell.setCellStyle(style);
			cell = row.createCell(5);cell.setCellValue("班別");cell.setCellStyle(style);
			cell = row.createCell(6);cell.setCellValue("老師");cell.setCellStyle(style);
			cell = row.createCell(7);cell.setCellValue("堂數");cell.setCellStyle(style);
			cell = row.createCell(8);cell.setCellValue("正班總人數");cell.setCellStyle(style);
			cell = row.createCell(9);cell.setCellValue("正班贈課人數");cell.setCellStyle(style);
			cell = row.createCell(10);cell.setCellValue("正班總收入");cell.setCellStyle(style);
			cell = row.createCell(11);cell.setCellValue("正班贈課費用");cell.setCellStyle(style);
			cell = row.createCell(12);cell.setCellValue("總人數");cell.setCellStyle(style);
			cell = row.createCell(13);cell.setCellValue("贈課人數");cell.setCellStyle(style);
			cell = row.createCell(14);cell.setCellValue("總收入");cell.setCellStyle(style);
			cell = row.createCell(15);cell.setCellValue("贈課原價");cell.setCellStyle(style);

			
	         for (int i=0;i<LGrade.size();i++)
	         {
	        	row = spreadsheet.createRow(i+1);
	            row.createCell(0).setCellValue(LGrade.get(i).getClass_date_final());
	            row.createCell(1).setCellValue(LGrade.get(i).getSchool_name());
	            row.createCell(2).setCellValue(LGrade.get(i).getSubject_name());
				row.createCell(3).setCellValue(LGrade.get(i).getClass_date_start());
				row.createCell(4).setCellValue(LGrade.get(i).getGrade_date());
				row.createCell(5).setCellValue(LGrade.get(i).getGradeName());
				row.createCell(6).setCellValue(LGrade.get(i).getTeacher_name());
				row.createCell(7).setCellValue(LGrade.get(i).getClass_no());
				row.createCell(8).setCellValue(LGrade.get(i).getGradeNo());
				row.createCell(9).setCellValue(LGrade.get(i).getGradeFreeNo());
				row.createCell(10).setCellValue(LGrade.get(i).getGradeFee());
				row.createCell(11).setCellValue(LGrade.get(i).getGradeFreeFee());
				row.createCell(12).setCellValue(LGrade.get(i).getGradeFreeNo_v());
				row.createCell(13).setCellValue(LGrade.get(i).getGradeFreeNo_v());
				row.createCell(14).setCellValue(LGrade.get(i).getGradeFee_v());
				row.createCell(15).setCellValue(LGrade.get(i).getGradeFreeFee_v());	            
	         }

	        
			try{				  
				FileOutputStream out;
				out = new FileOutputStream(new File(ExcelReportPath+"costReport-"+dateFormat2.format(new Date())+".xlsx"),false);
				workbook.write(out);
				out.close();			
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		 return "/System/Migration";
  } 
       
    @RequestMapping("/System/insertSubjectName")
    public String insertSubjectName() {
    	systemService.insertSubjectName();
    	return "/System/Migration";
    } 
    
       
    @RequestMapping("/System/insertTotalClassNo")
    public String insertTotalClassNo() {
    	List<Grade> LGrade = courseService.getGrade("","","","","","","","","","","","","");
    	for(int i=0;i<LGrade.size();i++) {
    		List<GradeClassNo> LGradeClassNo = courseService.getGradeClassNo("",LGrade.get(i).getSubject_id(),LGrade.get(i).getTeacher_id(),"");
    		if(LGradeClassNo.size()==0) {
	    		jdbcTemplate.update("Update eip.subjectTeacher set totalClassNo =? where subject_id=? and teacher_id=?;",
	    				LGrade.get(i).getClass_no(),
	    				LGrade.get(i).getSubject_id(),
	    				LGrade.get(i).getTeacher_id()		
	    		);    			
    		}
    	}
    	return "/System/Migration";
    }    
    
    
    @RequestMapping("/System/addCategory")
    public String addCategory() {
    	return "/System/addCategory";
    }
    
    @RequestMapping("/System/saveCategory")
    public String saveCategory(HttpServletRequest request) {
    	
    	String categoryName = request.getParameter("categoryName");
    	String bgColor = request.getParameter("bgColor");
    	if(categoryName!=null && !categoryName.isEmpty()) {
			jdbcTemplate.update("INSERT INTO eip.category VALUES (default,?,?,?);", 
					categoryName, 
					1,
					bgColor
			);
    	}	
    	return "/common/closeAndReload";
    } 
    
    @RequestMapping("/System/serviceContract")
    public String serviceContract(HttpServletRequest request,Model model,@Value("${UploadPath}") String UploadPath) {
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup",schoolGroup);	
    	return "/System/serviceContract";
    }
    
    @RequestMapping("/System/getServiceContract")
    @ResponseBody
    public String getServiceContract(HttpServletRequest request,Model model,@Value("${UploadPath}") String UploadPath) {
    	String school_code = request.getParameter("school_code");		
		List<String> Lfile = new ArrayList<String>();
		File[] files = new File(UploadPath+"/serviceContract/").listFiles();
		for (File file : files) {
		    if (file.isFile()) {
		    	if(file.getName().contains("serviceContract") && file.getName().contains(school_code)) {
		    		Lfile.add(file.getName());
		    	}
		    }
		}
		Collections.reverse(Lfile);
		String LfileStr = "";
		for(int i=0;i<Lfile.size();i++) {
			LfileStr += "<div style='padding:2px'><A href='#' onclick=openContract(\""+Lfile.get(i)+"\") style='color:blue'>"+Lfile.get(i)+"</A></div>";
		}			
    	return LfileStr;
    }    

    @RequestMapping("/System/TOEFL_guarantyContract")
    public String TOEFL_guarantyContract(HttpServletRequest request,Model model,@Value("${UploadPath}") String UploadPath) {
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup",schoolGroup);	
    	return "/System/TOEFL_guarantyContract";
    }
    
    @RequestMapping("/System/getTOEFL_guarantyContract")
    @ResponseBody
    public String getTOEFL_guarantyContract(HttpServletRequest request,Model model,@Value("${UploadPath}") String UploadPath) {
    	String school_code = request.getParameter("school_code");
	
		List<String> Lfile = new ArrayList<String>();
		File[] files = new File(UploadPath+"/serviceContract/").listFiles();
		for (File file : files) {
			if (file.isFile()) {
		    	if(file.getName().contains("TOEFL_guarantyContract") && file.getName().contains(school_code)) {
		    		Lfile.add(file.getName());
		    	}
			}	
		}
		Collections.reverse(Lfile);
		String LfileStr = "";
		for(int i=0;i<Lfile.size();i++) {
			LfileStr += "<div style='padding:2px'><A href='#' onclick=openContract(\""+Lfile.get(i)+"\") style='color:blue'>"+Lfile.get(i)+"</A></div>";
		}
		return LfileStr;
    }
 
    @RequestMapping("/System/IELTS_guarantyContract")
    public String IELTS_guarantyContract(HttpServletRequest request,Model model,@Value("${UploadPath}") String UploadPath) {
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup",schoolGroup);	
    	return "/System/IELTS_guarantyContract";
    }
    
    @RequestMapping("/System/getIELTS_guarantyContract")
    @ResponseBody
    public String getIELTS_guarantyContract(HttpServletRequest request,Model model,@Value("${UploadPath}") String UploadPath) {
    	String school_code = request.getParameter("school_code");
		
		List<String> Lfile = new ArrayList<String>();
		File[] files = new File(UploadPath+"/serviceContract/").listFiles();
		for (File file : files) {
			if (file.isFile()) {
		    	if(file.getName().contains("IELTS_guarantyContract") && file.getName().contains(school_code)) {
		    		Lfile.add(file.getName());
		    	}
			}	
		}
		Collections.reverse(Lfile);
		String LfileStr = "";
		for(int i=0;i<Lfile.size();i++) {
			LfileStr += "<div style='padding:2px'><A href='#' onclick=openContract(\""+Lfile.get(i)+"\") style='color:blue'>"+Lfile.get(i)+"</A></div>";
		}
		return LfileStr;
    } 
    
    @RequestMapping("/System/TOEIC_guarantyContract")
    public String TOEIC_guarantyContract(HttpServletRequest request,Model model,@Value("${UploadPath}") String UploadPath) {
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup",schoolGroup);	
    	return "/System/TOEIC_guarantyContract";
    }    
    
    @RequestMapping("/System/getTOEIC_guarantyContract")
    @ResponseBody
    public String getTOEIC_guarantyContract(HttpServletRequest request,Model model,@Value("${UploadPath}") String UploadPath) {
    	String school_code = request.getParameter("school_code");
		
		List<String> Lfile = new ArrayList<String>();
		File[] files = new File(UploadPath+"/serviceContract/").listFiles();
		for (File file : files) {
			if (file.isFile()) {
		    	if(file.getName().contains("TOEIC_guarantyContract") && file.getName().contains(school_code)) {
		    		Lfile.add(file.getName());
		    	}
			}
		}
		Collections.reverse(Lfile);
		String LfileStr = "";
		for(int i=0;i<Lfile.size();i++) {
			LfileStr += "<div style='padding:2px'><A href='#' onclick=openContract(\""+Lfile.get(i)+"\") style='color:blue'>"+Lfile.get(i)+"</A></div>";
		}
		return LfileStr;
    }     
      
    
    @RequestMapping(value="/System/serviceContractSave",method=RequestMethod.POST)
    public String serviceContractSave(@RequestParam("file") MultipartFile file,Model model,HttpServletRequest request,@Value("${UploadPath}") String UploadPath) { 
    	String contractName = request.getParameter("contractName");
    	String student_seq  = request.getParameter("student_seq");
    	String contractType = request.getParameter("contractType");
    	String school_code  = request.getParameter("school_code");
    	//檔案上傳
        if (!file.isEmpty()) {
            try {       	   
	           	byte[] bytes = file.getBytes();
	           	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
	           	String nowTime = dateFormat.format(new Date()); 
	           	String fileName= "";
	           	String pathStr = "";
	           	    //學生合約
		        	if(student_seq!=null && !student_seq.isEmpty()) {
		                if(contractType.equals("1")) {
		                	fileName = nowTime+"_"+student_seq+"_學生契約書.pdf";
		                }else if(contractType.equals("2")) {
		                	fileName = nowTime+"_"+student_seq+"_TOEFL保證班合約書.pdf";
		                }else if(contractType.equals("3")) {
		                	fileName = nowTime+"_"+student_seq+"_IELTS保證班合約書.pdf";
		                }else if(contractType.equals("4")) {
		                	fileName = nowTime+"_"+student_seq+"_TOEIC保證班合約書.pdf";
		                }
			           	pathStr = UploadPath+"/studentContract/"; 
		        	//範本合約
		        	}else{
			           	fileName = nowTime+"_"+school_code+"_"+contractName+".html";
			           	pathStr = UploadPath+"/serviceContract/";    		
		        	}
			           	Path path = Paths.get(pathStr + fileName);
			           	Files.write(path, bytes);		        	
            }catch(Exception e) {
               e.printStackTrace();
            }
        } 
        if(student_seq!=null) {
        	return "redirect:/Sales/studentContract?student_seq="+student_seq;
        }else {  
        	return "redirect:/System/"+contractName;
        }	
    }
    
    @RequestMapping("/System/openContract")
    public String openContract(HttpServletRequest request,Model model,@Value("${UploadPath}") String UploadPath){
    	String fileName = request.getParameter("fileName");
    	String salesFileName = request.getParameter("salesFileName");
    	String fileNameShow = "";
    	//[系統顯示]
    	if(fileName!=null && !fileName.isEmpty()) {
    		fileNameShow = fileName;
    	//[業務顯示]	
    	}else if(salesFileName!=null && !salesFileName.isEmpty()) {
    		List<String> Lfile = new ArrayList<String>();
    		File[] files = new File(UploadPath+"/serviceContract/").listFiles();
    		for (File file : files) {
    		    if (file.isFile()) {
    		    	if(file.getName().contains(salesFileName)) {
    		    		Lfile.add(file.getName());
    		    	}	
    		    }
    		}
    		Collections.reverse(Lfile);
    		if(Lfile.size()>0) {
    			fileNameShow = Lfile.get(0); 
    		}	
    	}
		String includeStr = "\\images\\serviceContract\\"+fileNameShow;
		model.addAttribute("includeStr",includeStr);	
		return "/System/fileInclude";
	}
    
    @RequestMapping("/System/fileInclude")
    public String fileInclude() {
    	return "/System/fileInclude";
    }
            
    @RequestMapping("/System/subject_price")
    public String subject_price() {
    	systemService.subject_price();
    	return "/System/Migration";
    } 
        	
    
    @RequestMapping("/System/updateSlot")
    public String updateSlot() {
    	systemService.updateSlot();
    	return "/System/Migration";
    } 
    
    @RequestMapping("/System/schedule_1")
    public String schedule_1() {
    	systemService.schedule_1();
    	return "/System/Migration";
    } 
    
    @RequestMapping("/System/JL_grade_not")
    public String JL_grade_not() {
    	//systemService.schedule_1();
    	return "/System/JL_grade_not";
    }  
    
    @RequestMapping("/System/SpecialSetting_combo")
    public String SpecialSetting_combo(Model model,Principal principal,HttpSession session,HttpServletRequest request) {
		
		List<School> schoolGroup = accountService.getSchool("","");
		model.addAttribute("schoolGroup", schoolGroup);
		
		
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 

		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		List<FlowStatus> flowStatusGroup = courseService.getFlowStatus();
		model.addAttribute("flowStatusGroup", flowStatusGroup);
		
		if(request.getParameter("sessionState")!=null) {
			model.addAttribute("sessionState","keep");
			
			String category_id = "";
			if(session.getAttribute("CourseController_category_id")!=null) {
				category_id = (String)session.getAttribute("CourseController_category_id");
			}  
			model.addAttribute("category_id",category_id);
			
			String isCombo = "";
			if(session.getAttribute("CourseController_isCombo")!=null) {
				isCombo = (String)session.getAttribute("CourseController_isCombo");
			}  
			model.addAttribute("isCombo",isCombo);
		
			String FlowStatus_code = "";
			if(session.getAttribute("CourseController_FlowStatus_code")!=null) {
				FlowStatus_code = (String)session.getAttribute("CourseController_FlowStatus_code");
			}  
			model.addAttribute("FlowStatus_code",FlowStatus_code);			
		}else {
			session.removeAttribute("CourseController_category_id");
			session.removeAttribute("CourseController_isCombo");
			session.removeAttribute("CourseController_FlowStatus_code");
		}	    	
    	return "/System/SpecialSetting_combo";
    } 
    
    @RequestMapping("/System/SpecialSetting_grade")
    public String SpecialSetting_grade(Model model,Principal principal,HttpSession session,HttpServletRequest request) {
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
				
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		List<FlowStatus> flowStatusGroup = courseService.getFlowStatus();
		model.addAttribute("flowStatusGroup", flowStatusGroup);	
		
		String school_code = "";
		if(principal!=null && principal.getName()!=null) {	
			school_code = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
			model.addAttribute("school_code",school_code);
		}
		
		if(request.getParameter("sessionState")!=null) {
			model.addAttribute("sessionState","keep");
			if(session.getAttribute("CourseController_school_code")!=null) {
				school_code = (String)session.getAttribute("CourseController_school_code");
			}  
			model.addAttribute("school_code",school_code);
			
			String category_id = "";	
		    if(session.getAttribute("CourseController_category_id")!=null) {						
				category_id = (String)session.getAttribute("CourseController_category_id");
			}
			model.addAttribute("category_id",category_id);
	
			String subject_id = "";		
			if(session.getAttribute("CourseController_subject_id")!=null) {
				subject_id = (String)session.getAttribute("CourseController_subject_id");
			}
			model.addAttribute("subject_id",subject_id);
			
			String status_code = "";		
			if(session.getAttribute("CourseController_status_code")!=null) {
				status_code = (String)session.getAttribute("CourseController_status_code");
			}		
			model.addAttribute("status_code",status_code);
			
			String teacher_id = "";
			if(session.getAttribute("CourseController_teacher_id")!=null) {
				teacher_id = (String)session.getAttribute("CourseController_teacher_id");
			}		
			model.addAttribute("teacher_id",teacher_id);			
		}else {
			session.removeAttribute("CourseController_school_code");
			session.removeAttribute("CourseController_category_id");
			session.removeAttribute("CourseController_subject_id");
			session.removeAttribute("CourseController_status_code");
			session.removeAttribute("CourseController_teacher_id");
		}    	
    	return "/System/SpecialSetting_grade";
    } 
    
    @RequestMapping("/System/SpecialSetting_subject")
    public String SpecialSetting_subject(Model model,HttpServletRequest request,HttpSession session) {
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		
		if(request.getParameter("sessionState")!=null) {
			model.addAttribute("sessionState","keep");
			String CourseController_category_id = (String)session.getAttribute("CourseController_category_id");
			if(CourseController_category_id!=null && !CourseController_category_id.isEmpty()) {
				model.addAttribute("CourseController_category_id", CourseController_category_id);
			}
		}else{
			session.removeAttribute("CourseController_category_id");
		}    	
    	return "/System/SpecialSetting_subject";
    }   
 
    @RequestMapping("/System/schedule_videoOff")
    public String schedule_videoOff() {
    	return "/System/schedule_videoOff";
    } 
    
    @RequestMapping("/System/schedule_rollCall")
    public String schedule_rollCall(Model model) {
    	ScheduleActive scheduleActive= systemService.getScheduleActive("1").get(0);
    	model.addAttribute("active",scheduleActive.getActive());
    	return "/System/schedule_rollCall";
    }
    
    @RequestMapping("/System/scheduleActive")
    @ResponseBody  
    public Boolean scheduleActive(HttpServletRequest request) {
    	String active = request.getParameter("active");
    	String schedule_id = request.getParameter("schedule_id");
        if(schedule_id!=null && !schedule_id.isEmpty()) {
			jdbcTemplate.update("UPDATE eip.scheduleActive SET active=? WHERE schedule_id=?", 
					active,
					schedule_id
			);
        }
        
        return true;
    }    
    
    
    @RequestMapping("/System/gradeDisable")
    public String gradeDisable() {
    	return "/System/schedule_rollCall";
    } 
    
    @RequestMapping("/System/GenQRcode")
    public String GenQRcode() {
    	List<Student> LStudent = salesService.getStudent("","","","","","","","","","");
    	for(int i=0;i<LStudent.size();i++) {
    		salesService.GenQRcode(LStudent.get(i).getStudent_no());
    	}
    	
    	return "/System/Migration";
    }    
          
}    


