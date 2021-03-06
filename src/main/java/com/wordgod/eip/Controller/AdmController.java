package com.wordgod.eip.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordgod.eip.Model.*;
import com.wordgod.eip.Service.AccountService;
import com.wordgod.eip.Service.AdmService;
import com.wordgod.eip.Service.CourseSaleService;
import com.wordgod.eip.Service.CourseService;
import com.wordgod.eip.Service.MarketingService;
import com.wordgod.eip.Service.SalesService;
import com.wordgod.eip.Service.SystemService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

	
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Iterator;
import org.springframework.ui.Model;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
@Controller
public class AdmController {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	CourseService courseService;
	@Autowired
	AccountService accountService;	
	@Autowired
	SalesService salesService;
	@Autowired
	CourseSaleService courseSaleService;	
    @Autowired    
    MarketingService marketingService;
    @Autowired
    AdmService admService;
    @Autowired
    SystemService systemService;

 
    @RequestMapping("/Adm/StudentSign")
    public String StudentSign(Model model,Principal principal) {
		
		List<School> schoolGroup = accountService.getSchool("","");
		model.addAttribute("schoolGroup", schoolGroup);	
		
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);

		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		String SchoolCode = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		model.addAttribute("SchoolCode",SchoolCode);		
		
        return "/Adm/StudentSign";
    }
 
    @RequestMapping("/Adm/openUpload")
    public String openUpload() {				
        return "/Adm/openUpload";
    } 
    
    @RequestMapping("/Adm/openUploadVideo")
    public String openUploadVideo() {				
        return "/Adm/openUploadVideo";
    }     
        
   @RequestMapping("/Adm/upload") 
   public String upload(HttpServletRequest request,@RequestParam("file") MultipartFile file,RedirectAttributes redirectAttributes,Principal principal,@Value("${ExcelUploadPath}") String ExcelUploadPath) throws IOException {
       //Step0.單筆上傳或多筆資料夾處理
	   if(request.getParameter("batch")==null) {
		   if (file.isEmpty()) {
	            redirectAttributes.addFlashAttribute("message", "請選擇檔案");
	            return "redirect:/Adm/openUpload";
	       }
		   try {
			   //刪除目前存在的
			   File folder = new File(ExcelUploadPath);      
			   String[] myFiles;    
			   if (folder.isDirectory()) {
			       myFiles = folder.list();
			       for (int i = 0; i < myFiles.length; i++) {
			           File myFile = new File(folder, myFiles[i]); 
			           myFile.delete();
			       }
			   }
			   
		        byte[] bytes = file.getBytes();
		        String fileName = file.getOriginalFilename();
		        Path path = Paths.get(ExcelUploadPath + fileName);
		        Files.write(path, bytes);
		        redirectAttributes.addFlashAttribute("message","已成功上傳 [ " + fileName + " ]");
		   }catch(Exception e) {
	            e.printStackTrace();
	            redirectAttributes.addFlashAttribute("message", "上傳失敗"+e);
	            return "redirect:/Adm/openUpload";
	       }
	   }
	   
	   List<String> LExcelUploadPath = new ArrayList<>();
	   if(request.getParameter("batch")==null) {
		   LExcelUploadPath.add(ExcelUploadPath);		   
	   }   
  

	   
	     for(int z=0;z<LExcelUploadPath.size();z++) { 
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
		    		System.out.println("---------檔案--------"+fileName+"<br>");
			        String[] fn = fileName.substring(0,fileName.length()-5).split("-");
			        String school_code       = fn[0];
			        String subject_abbr      = fn[1];       
			        String class_start_date  = fn[2];
			        class_start_date = fn[2].substring(2,4)+"/"+fn[2].substring(4,6)+"/"+"20"+fn[2].substring(0,2);
			        String teacher_name      = fn[3];
			        String class_th = "";
			        String student_no = "";
			        String attend = "";
			        String TakeHandout = "";
			        String returnMessage = "---------檔案--------"+fileName+"<br>";
			        String grade_id = "";
			        
					List<Grade> LGrade = courseService.getGradeList("","","",school_code,"",subject_abbr,class_start_date,"","","1","",teacher_name,"","","","","","1","");
					if(LGrade.size()>0) {
						grade_id = LGrade.get(0).getGrade_seq();
					}else {
						returnMessage +="不存在的期別或老師: "+school_code+"/"+subject_abbr+"/"+class_start_date+"/"+teacher_name+"<br>";
						redirectAttributes.addFlashAttribute("message",returnMessage);
						continue;
						//return "redirect:/Adm/openUpload";
					}        
		    	
		    		//Step2.處理Excel 
					InputStream ExcelFileToRead = new FileInputStream(LExcelUploadPath.get(z) + fileName);
					String updater = principal.getName();
					XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
					String student_id = ""; 
					ArrayList<String> A_attend = new ArrayList<String>();
					ArrayList<String> A_TakeHandout = new ArrayList<String>();
					ArrayList<String> A_student_id = new ArrayList<String>();
					ArrayList<String> A_class_th = new ArrayList<String>();
					DataFormatter formatter = new DataFormatter();
		
			        for (int x=0;x<wb.getNumberOfSheets();x++) {
			          if(!wb.getSheetName(x).contains("不吃")) {	
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
								returnMessage +="課堂更改有誤: 學生"+student_no+" 課堂"+class_th+"<br>";						
								redirectAttributes.addFlashAttribute("message",returnMessage);
								wb.close();
								return "redirect:/Adm/openUpload";	
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
								List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory("",student_id,grade_id,"1","(1)","","","","",school_code,class_th);
								//儲存點名紀錄 
								if(LSignRecordHistory.size()>0) {
									A_attend.add(attend);
									A_TakeHandout.add(TakeHandout);
									A_student_id.add(student_id);
									A_class_th.add(class_th);	
								}else {	
									returnMessage +="此學生不存在點名課堂清單: "+fileName+"學生"+student_no+"堂數"+class_th+"<br>";						
									redirectAttributes.addFlashAttribute("message",returnMessage);
									//wb.close();
									//return "redirect:/Adm/openUpload";								
								} 
							}else {	
								returnMessage +="不存在的學生: "+fileName+"學生"+student_no+"堂數"+class_th+"<br>";
								redirectAttributes.addFlashAttribute("message",returnMessage);
								//wb.close();
								//return "redirect:/Adm/openUpload";
							}
							
						}	
			
			        }	
			        wb.close();
			        for(int j=0;j<A_student_id.size();j++) {
						Boolean returnStr = admService.UpdateSignRecord(subject_abbr,school_code,A_class_th.get(j),A_student_id.get(j),A_attend.get(j),A_TakeHandout.get(j),updater,grade_id,"1","",student_no,"","");
						if(!returnStr) {
							returnMessage +="資料有誤: 學生"+student_no+" 課堂"+class_th+"<br>";						
							redirectAttributes.addFlashAttribute("message",returnMessage);
							return "redirect:/Adm/openUpload";	
						}        	
			        }
			       }  
		    	}
		      }	
		    }catch(Exception e) {
		            e.printStackTrace();
		            redirectAttributes.addFlashAttribute("message", "上傳失敗"+e);
		            return "redirect:/Adm/openUpload";
		    }
	     }  
	    
    return "redirect:/Adm/openUpload";
  }
   
   @RequestMapping("/Adm/uploadVideo") 
   public String uploadVideo(HttpServletRequest request,@RequestParam("file") MultipartFile file,RedirectAttributes redirectAttributes,Principal principal,@Value("${ExcelUploadPath}") String ExcelUploadPath) throws IOException {
       //Step0.單筆上傳或多筆資料夾處理
	   if(request.getParameter("batch")==null) { 
		   if (file.isEmpty()) {
	            redirectAttributes.addFlashAttribute("message", "請選擇檔案");
	            return "redirect:/Adm/openUploadVideo";
	       }
		   try {
			   //刪除目前存在的
			   File folder = new File(ExcelUploadPath);      
			   String[] myFiles;    
			   if (folder.isDirectory()) {
			       myFiles = folder.list();
			       for (int i = 0; i < myFiles.length; i++) {
			           File myFile = new File(folder, myFiles[i]); 
			           myFile.delete();
			       }
			   }
			   
		        byte[] bytes = file.getBytes();
		        String fileName = file.getOriginalFilename();
		        Path path = Paths.get(ExcelUploadPath+fileName);
		        Files.write(path, bytes);
		        redirectAttributes.addFlashAttribute("message","已成功上傳 [ " + fileName + " ]");
		   }catch(Exception e) {
	            e.printStackTrace();
	            redirectAttributes.addFlashAttribute("message", "上傳失敗"+e);
	            return "redirect:/Adm/openUpload";
	       }		   
	   } 
	   
	   
	   List<String> LExcelUploadPath = new ArrayList<>();
	   if(request.getParameter("batch")==null) {
		   LExcelUploadPath.add(ExcelUploadPath);
	   }else {
		   LExcelUploadPath.add(ExcelUploadPath+"video/2019VIDEO名單/VIDEO點名條/1月/");
	   }
	   
	   for(int z=0;z<LExcelUploadPath.size();z++) { 
		    try {
		    	File folder = new File(LExcelUploadPath.get(z));
		    	File[] Lfile = folder.listFiles();
		    	String fileName = "";
		    	for (int i=0;i<Lfile.length;i++) {
		    		if(Lfile[i].isDirectory()) {
		    			continue;
		    		}
		    		//Step1.判斷期別是否存在
		    		fileName = Lfile[i].getName();  
		    		System.out.println("-------Video檔案----------"+fileName);
		        
			        String[] fn = fileName.substring(0,fileName.length()-5).split("-");
			        String school_code   = fn[0];
			        String attend_date   = fn[1];
			        attend_date = attend_date.substring(4,6)+"/"+attend_date.substring(6,8)+"/"+attend_date.substring(0,4);
			        String class_th = "";
			        String student_no = "";
			        String attend = "";
			        String TakeHandout = "";
			        String returnMessage = "";
			        String grade_id = "";
			        String subject_abbr = "";
			        String class_start_date = "";
			        String teacher_id = "-1";
			        String video_date = "";
			        
			             
					InputStream ExcelFileToRead = new FileInputStream(LExcelUploadPath.get(z)+fileName);
					String updater = principal.getName();
					XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
					String student_id = ""; 
					ArrayList<String> A_attend = new ArrayList<String>();
					ArrayList<String> A_student_id = new ArrayList<String>();
					ArrayList<String> A_class_th = new ArrayList<String>();
					ArrayList<String> A_TakeHandout = new ArrayList<String>();
					ArrayList<String> A_grade_id = new ArrayList<String>();
					DataFormatter formatter = new DataFormatter();
					int errorData=0;
					int TFJJerror = 0;
			        for (int x=0;x<wb.getNumberOfSheets();x++) {
				      if(!wb.getSheetName(x).contains("不吃")) {		
						XSSFSheet sheet=wb.getSheetAt(x);
						XSSFRow row;
						for(int a=1;a<sheet.getPhysicalNumberOfRows();a++) {
							TFJJerror = 0;
							row=sheet.getRow(a);
					
							if(row==null || row.getCell(0)==null || formatter.formatCellValue(row.getCell(0)).isEmpty()) {
								break; 
							}
							student_no = formatter.formatCellValue(row.getCell(0));						
							subject_abbr = formatter.formatCellValue(row.getCell(4)); 
							class_start_date = formatter.formatCellValue(row.getCell(3)); 
							List<Teacher> LTeacher = courseService.getTeacher("","","","","","",formatter.formatCellValue(row.getCell(6)));
							if(LTeacher.size()>0) {teacher_id = LTeacher.get(0).getTeacher_seq();}
							
							List<Grade> LGrade = new ArrayList<Grade>();
							if(class_start_date.length()==6) {
								class_start_date = class_start_date.substring(2,4)+"/"+class_start_date.substring(4,6)+"/"+"20"+class_start_date.substring(0,2);
								LGrade = courseService.getGradeList(teacher_id,"","","","",subject_abbr,class_start_date,"","","1","","","","","","","","1","");				
							}else {
								if(class_start_date.length()>1 && class_start_date.length()<6) { //期別別名
									video_date = class_start_date;
									LGrade = courseService.getGradeList(teacher_id,"","","","",subject_abbr,"","","","1",video_date,"","","","","","","1","");
								}	
							}
												
							if(LGrade.size()>0) {
								//判定是否此期別存在於學生已訂期別中
								List<Student> LStudent = salesService.getStudent("","","",student_no,"","","","","","");
								if(LStudent.size()>0) {
								      student_id = LStudent.get(0).getStudent_seq();
								}else {	
									returnMessage +=a+"-->不存在的學生: "+student_no+"<hr>";
									redirectAttributes.addFlashAttribute("message",returnMessage);
									errorData=1;
									//wb.close();
									//return "redirect:/Adm/openUploadVideo";
								}      
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
							}else {
								returnMessage +=a+"-->不存在的期別或老師: "+subject_abbr+" / "+class_start_date+" / "+formatter.formatCellValue(row.getCell(6))+"<hr>";
								redirectAttributes.addFlashAttribute("message",returnMessage);
								errorData=1;
								//wb.close();
								//return "redirect:/Adm/openUploadVideo";
							}  
							
							class_th    = formatter.formatCellValue(row.getCell(5));
							TakeHandout = formatter.formatCellValue(row.getCell(8));
							attend      = formatter.formatCellValue(row.getCell(9)); 
							
					        //不用管到原來實體的school_code
							List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory("",student_id,grade_id,"1","(1)","","","","","",class_th);
							//TFJJ
							if(subject_abbr.contains("TFJJ")) {
								if(LSignRecordHistory.size()>0) {
									if(!LSignRecordHistory.get(0).getTeacher_name().equals(formatter.formatCellValue(row.getCell(6)))) {
										returnMessage +=a+"-->TFJJ課堂老師比對有誤(口說,寫作,字彙...): 學號:"+student_no+" 科目:"+class_start_date+" "+subject_abbr+" 課堂"+class_th+"<hr>";						
										redirectAttributes.addFlashAttribute("message",returnMessage);
										errorData=1;
										TFJJerror = 1;
									}
								}
							}
							
							
							//儲存點名紀錄 
							if(LSignRecordHistory.size()>0 && TFJJerror ==0 ) {
								//檢查點名紀錄(此堂課class_th)是實體,則新增一筆Video (補課?)
								if(LSignRecordHistory.get(0).getClass_style().equals("1")) {
									admService.addVideoRecord(LSignRecordHistory.get(0),school_code);
								}
								A_attend.add(attend);
								A_student_id.add(student_id);
								A_class_th.add(class_th);
								A_TakeHandout.add(TakeHandout);
								A_grade_id.add(grade_id);
							}else {	
								returnMessage +=a+"-->此學生不存在於點名課堂清單: 學號:"+student_no+" 科目:"+class_start_date+" "+subject_abbr+"<hr>";						
								redirectAttributes.addFlashAttribute("message",returnMessage);
								errorData=1;
								//wb.close();
								//return "redirect:/Adm/openUploadVideo";								
							} 				
						}	
			        }	
			        if(errorData==1) {
			        	wb.close();
			        	return "redirect:/Adm/openUploadVideo";	
			        }
			        
			        for(int j=0;j<A_student_id.size();j++) {
						Boolean returnStr = admService.UpdateSignRecord(subject_abbr,school_code,A_class_th.get(j),A_student_id.get(j),A_attend.get(j),A_TakeHandout.get(j),updater,A_grade_id.get(j),"2",attend_date,student_no,"","");
						if(!returnStr) {
							returnMessage +="資料有誤: 學生"+student_no+" 課堂"+class_th+"<hr>";						
							redirectAttributes.addFlashAttribute("message",returnMessage);
							wb.close();
							return "redirect:/Adm/openUploadVideo";	
						}        	
			        }
			       }  
		    	} 
		    }catch(Exception e) {
		            e.printStackTrace();
		            redirectAttributes.addFlashAttribute("message", "上傳失敗"+e);
		            return "redirect:/Adm/openUploadVideo";
		    }		
	   }       
    return "redirect:/Adm/openUploadVideo";
  }  
   
   @RequestMapping("/Adm/uploadVideo2") 
   public String uploadVideo2(HttpServletRequest request) throws IOException {
	   try {
		   String test = request.getParameter("test");
		   systemService.uploadVideo2(test);
       }catch (IOException e) {
	   		e.printStackTrace();
       }  
	   return "redirect:/System/ExcelRoleCall";
   }      
    
    @RequestMapping("/Adm/signRecord")
    public String signRecord(Model model,HttpServletRequest request) {
/**    	
    	String grade_id = request.getParameter("grade_id");
    	String student_seq = request.getParameter("student_seq");
    	String recordListStr = admService.getRecordListStr(grade_id,student_seq,"","");
    	model.addAttribute("recordListStr", recordListStr);
**/    	
		model.addAttribute("Register_comboSale_grade_seq",request.getParameter("Register_comboSale_grade_seq"));
		//顯示取消訂班
		if(request.getParameter("cancelRegister")!=null){model.addAttribute("cancelRegister", "1");}
        return "/Adm/signRecord";
    } 
    
    @RequestMapping("/Adm/signRecord2")
    public String signRecord2(Model model,HttpServletRequest request) {
    	String grade_id = request.getParameter("grade_id");   	
    	model.addAttribute("grade_id", grade_id);
    	String student_seq = request.getParameter("student_seq");
    	String class_style = request.getParameter("class_style");
    	String school_code = request.getParameter("school_code");
    	String recordListStr = admService.getRecordListStr(grade_id,student_seq,class_style,school_code);
    	model.addAttribute("recordListStr", recordListStr);
        return "/Adm/signRecord2";
    }
    
    @RequestMapping("/Adm/signRecord3")
    public String signRecord3(Model model,HttpServletRequest request) {
    	String register_comboSale_grade_id = request.getParameter("register_comboSale_grade_id");
    	String class_th = request.getParameter("class_th"); 
    	List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory("","","","1","","",register_comboSale_grade_id,"","","","");
    	String recordListStr = "";
    	String classThTitle = "";
    	String recordTitle = "";
    	String recordGrade = "";
    	String recordAttend = "";
		//點名紀錄
		for(int j=0;j<LSignRecordHistory.size();j++) {
			    String classTh = ""; 
    			String attend_date = "";
    			String checkStr = "";
    			
    			if(LSignRecordHistory.get(j).getClass_th().equals(class_th)) {
    				classTh ="<img src='/images/down.png' height='15px'/>";
    			}	
    			classThTitle +="<div class='td2' style='width:65px;text-align:center;vertical-align:bottom;'>"+classTh+"</div>";
    				
    			if(LSignRecordHistory.get(j).getAttend_date()!=null && LSignRecordHistory.get(j).getAttend_date().length()>5) {
    				attend_date = LSignRecordHistory.get(j).getAttend_date().substring(0,5);
    			}   			
    			recordTitle +="<div class='td2' style='width:65px;text-align:center;vertical-align:bottom;font-size:small'>"+attend_date+"<br>第 "+LSignRecordHistory.get(j).getClass_th()+" 堂</div>";
    			
    			Grade grade =  courseService.getGrade(LSignRecordHistory.get(j).getGrade_id(),"","","","","","","200","","","","","").get(0);
    			recordGrade +="<div class='td2' style='width:65px;text-align:center;vertical-align:bottom;font-size:x-small'>"+grade.getClass_start_date()+"<br>"+grade.getSubject_name()+"</div>";
    			
            		if(LSignRecordHistory.get(j).getAttend().equals("1")) { //出席
            			if(LSignRecordHistory.get(j).getClass_style().equals("2")) { //video
            				checkStr = "Green_repeat.png";
            			}else {
            				checkStr = "GreenSquare.png";
            			}
            		}else if(LSignRecordHistory.get(j).getAttend().equals("-1")) { //缺席
            			if(LSignRecordHistory.get(j).getClass_style().equals("2")) { //video
            				checkStr = "Red_repeat.png";
            			}else {
            				checkStr = "RedSquare.png";
            			}
            		}else if(LSignRecordHistory.get(j).getAttend().equals("0")) { //預定
            			if(LSignRecordHistory.get(j).getClass_style().equals("2")) { //video
            				checkStr = "White_repeat.png";
            			}else {
            				checkStr = "WhiteSquare.png";
            			}
            		}
            	if(checkStr.equals("")) { //ex. attend=2 中途插班待選
            		recordAttend +="<div class='td2' style='width:65px;text-align:center;font-size:small;border:1px #eeeeee solid'>&nbsp;<img src='/images/question.png' height='12px'/></div>";
            	}else {
            		recordAttend +="<div class='td2' style='width:65px;text-align:center;font-size:small;border:1px #eeeeee solid'><A href='javascript:changeAttend("+LSignRecordHistory.get(j).getSignRecordHistory_seq()+","+LSignRecordHistory.get(j).getAttend()+")' style='text-decoration:underline;color:blue'>&nbsp;<img src='/images/"+checkStr+"' height='12px'/>&nbsp;</A></div>";
            	}	
   
		}
			
		recordListStr +=
				"<div class='css-table' style='border-spacing:1px'>"+
						"<div class='tr' style='background-color:white;width:500px;border-radius:10px'>&nbsp;<img src='/images/GreenSquare.png' height='9px'/>到課&nbsp;<img src='/images/RedSquare.png' height='9px'/>缺課&nbsp;</div>"+
				"</div>"+
				"<div>&nbsp;</div>"+		
				"<div class='css-table' style='border-spacing:1px'>"+
						"<div class='tr' style=''>"+classThTitle+"</div>"+
						"<div class='tr' style='background-color:steelblue;color:white;font-size:small'>"+recordTitle+"</div>"+
						"<div class='tr' style='background-color:#ffeeff;font-size:x-small'>"+recordGrade+"</div>"+
						"<div class='tr' style='font-size:small'>"+recordAttend+"</div>"+
				"</div>";					
		
    	model.addAttribute("recordListStr", recordListStr);
        return "/Adm/signRecord3";
    }     
   
    @RequestMapping("/Adm/StudentVideoSign")
    public String StudentVideoSign(Model model,Principal principal){   	
		List<School> LSchool = accountService.getSchool("",""); 
		model.addAttribute("LSchool",LSchool); 

		List<Category> LCategory = courseService.getCategory("","");
		model.addAttribute("LCategory",LCategory);
		
		String SchoolCode = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		model.addAttribute("SchoolCode",SchoolCode);			
		
        return "/Adm/StudentVideoSign";
    } 
    
    @RequestMapping("/Adm/getSignRecordHistory")
    @ResponseBody
    public List<SignRecordHistory> getSignRecordHistory(HttpServletRequest request){
        return admService.getSignRecordHistory("","","","","","","","(0,1,-1)","","","");
    } 
   
    @RequestMapping("/Adm/RegisterStatusUpdate")
    @ResponseBody
    public Boolean RegisterStatusUpdate(HttpServletRequest request,Principal principal){
    	String register_status = request.getParameter("register_status");
    	String Register_comboSale_grade_seq = request.getParameter("Register_comboSale_grade_seq");
    	String updater = principal.getName();
    	String student_seq = request.getParameter("student_seq");
    	String CancelComment = request.getParameter("CancelComment");
    	String reason_option = request.getParameter("reason_option");
    	String classCharge = request.getParameter("classCharge");
    	String operationCharge = request.getParameter("operationCharge"); 
    	String order_grade = request.getParameter("order_grade"); //期別名稱
    	String register_id = request.getParameter("register_id"); 
    	String register_log_seq = request.getParameter("register_log_seq");
    	String payStyle_2_money = request.getParameter("payStyle_2_money");
    	String payStyle_5_periods = request.getParameter("payStyle_5_periods");
    	String payStyle_5_code = request.getParameter("payStyle_5_code");
    	String payStyle_5_money = request.getParameter("payStyle_5_money"); 
    	String payStyle_7_code = request.getParameter("payStyle_7_code");
    	String payStyle_7_money = request.getParameter("payStyle_7_money");    	
    	
    	if(request.getParameter("register_status")!=null && request.getParameter("Register_comboSale_grade_seq")!=null && updater!=null) {
    		String Register_comboSale_seq = courseService.getRegister_comboSale_grade("",Register_comboSale_grade_seq,"","").get(0).getRegister_comboSale_id();
    		admService.UpdateRegisterStatus(Register_comboSale_grade_seq,Register_comboSale_seq,register_status);
    		if(register_log_seq!=null && !register_log_seq.equals("-1")) {	
    			//被更新
    			admService.updateRegisterLog(register_log_seq,"1");
    		    //更新後的一筆
    			CancelComment = "<span style='font-weight:bold;color=red'>(修改Seq."+register_log_seq+")</span><br>"+CancelComment;
    		}	
	    		admService.SaveRegisterLog(Register_comboSale_seq,Register_comboSale_grade_seq,register_status,CancelComment,reason_option,updater,classCharge,operationCharge,payStyle_2_money,payStyle_5_periods,payStyle_5_code,payStyle_5_money,payStyle_7_code,payStyle_7_money,"0");
	             
		    	//繳費主檔+繳費明細
/**	    		
	    		StudentPayRecord studentPayRecord = new StudentPayRecord();
	    		studentPayRecord.setRegister_id(register_id);
		    	studentPayRecord.setTakePerson(accountService.getAccountByID("",principal.getName()).getCh_name());
		    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		    	studentPayRecord.setPayDate(formatter.format(new Date()));
		    	studentPayRecord.setSchool_code(accountService.getAccountByID("",principal.getName()).getSchool()); 

	    	
	    	    salesService.StudentPaySaveRecord(
	    			studentPayRecord,
	    			"",
	    			payStyle_2_money,
	    			null,
	    			null,
	    			"",
	    			"",
	    			payStyle_5_periods,
	    			payStyle_5_code,
	    			payStyle_5_money,
	    			"",
	    			"",
	    			payStyle_7_code,
	    			payStyle_7_money,
	    			student_seq,
	    			principal.getName(),
	    			"", //改報轉入
	    			"1", //1:繳費,-1:退費
	    			"",
	    			"", //應繳
	    			"", //應退
	    			""
	    	    );
**/ 

	 	    
	 	    //學生歷程		
	 	    String experience_id = "13";
	 	    salesService.insertStudentExperience(student_seq,"",experience_id,"",order_grade,"",updater,register_id); 
	 	    
    		   		
	    		//主管費用紀錄 : 6.課程異動繳費
		    	int payMoney_to = 0;
	    		if(classCharge!=null && !classCharge.isEmpty()) {
	    			payMoney_to += Integer.valueOf(classCharge);
	    		}
	    		if(operationCharge!=null && !operationCharge.isEmpty()) {
	    			payMoney_to += Integer.valueOf(operationCharge);
	    		}
	    		
	    		String changeType = "6";
	    		salesService.orderChangeSave(register_id,student_seq,changeType,"","","",order_grade,"",String.valueOf(payMoney_to),"","",updater,"");
	    		
	    		//修改原來的為line-through
	    		if(register_log_seq!=null && !register_log_seq.equals("-1")) {
	    			salesService.orderChangeUpdate(register_id,student_seq,changeType,order_grade,"1");
	    		}	
   		
    		
    		//先上課復原
    		//salesService.pay_statusSave("",Register_comboSale_grade_seq,"0","");

    		//所有備註 eip.register[comment] 
    		
    		/**
    		String remarkTotal = null;
    		String remarkTotalOri = salesService.getStudent(student_seq,"","","","","","","","","").get(0).getRemarkTotal();
    		if(remarkTotalOri==null) {remarkTotalOri="";}
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String nowTime = dateFormat.format(date);
    		if(CancelComment!=null && !CancelComment.isEmpty()) {
    			remarkTotal = "------------  "+updater+" "+nowTime+"  ------------\n"+CancelComment+"\n"+remarkTotalOri;
    		}
    		jdbcTemplate.update("Update eip.student set remarkTotal=?,update_time=NOW() where student_seq=?;",
    				remarkTotal, 
    				student_seq
    		);
    		**/    		
    	}
        return true;
    } 
    
	@RequestMapping("/Adm/GradeMessage")
	public String GradeMessage(Model model) {
		
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
				
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		List<FlowStatus> flowStatusGroup = courseService.getFlowStatus();
		model.addAttribute("flowStatusGroup", flowStatusGroup);		
		
		return "/Adm/GradeMessage";
	} 
	 
    @RequestMapping("/Adm/MockSetting")
    public String MockSetting() {				
        return "/Adm/MockSetting";
    } 
    
    @RequestMapping("/Adm/GmatMockSet1")
    public String GmatMockSet1(Model model,HttpServletRequest request,Principal principal) {

    	//上課校區
    	String SchoolCode = accountService.getAccountByID("",principal.getName()).getSchool();
        model.addAttribute("SchoolCode",SchoolCode);
  	
		List<School> LSchool = accountService.getSchool("",""); 
		model.addAttribute("LSchool", LSchool);
		List<Category> LCategory = courseService.getCategory("","GMAT");
		model.addAttribute("category_id", LCategory.get(0).getCategory_seq());
        return "/Adm/GmatMockSet1";
    } 
    
    @RequestMapping("/Adm/GmatMockSet2")
    public String GmatMockSet2(Model model,HttpServletRequest request) {
    	if(request.getParameter("school_id")!=null) {
    		model.addAttribute("school_id", request.getParameter("school_id"));
    	}else {
    		model.addAttribute("school_id","0");
    	}
    	
		List<School> LSchool = accountService.getSchool("",""); 
		model.addAttribute("LSchool", LSchool);
		List<Category> LCategory = courseService.getCategory("","GMAT");
		model.addAttribute("category_id", LCategory.get(0).getCategory_seq());
        return "/Adm/GmatMockSet2";
    }     
		
    @RequestMapping("/Adm/openMockSet1")
    public String openMockSet1(Model model,HttpServletRequest request) {
    	
    	String category_id = request.getParameter("category_id");
    	String school_code   = request.getParameter("school_code");
    	List<School> schoolGroup = accountService.getSchool(school_code,"");
    	String school_id = schoolGroup.get(0).getSchool_seq();
    	String school_name = schoolGroup.get(0).getName();
    			
    	String dateSel     = request.getParameter("dateSel");
    	model.addAttribute("category_id",category_id);
    	model.addAttribute("school_id",school_id);
    	model.addAttribute("school_name",school_name);
    	model.addAttribute("dateSel",dateSel);

        return "/Adm/openMockSet1";
    }
      
    
    @RequestMapping("/Adm/getmockSetEvent")
    @ResponseBody
    public String getmockSetEvent(Model model,HttpServletRequest request) { 
    	String category_id = request.getParameter("category_id");
    	String school_id   = request.getParameter("school_id");
    	String testMethod  = request.getParameter("testMethod");
    	
    	String jsonMsg = null;
        List<Event> events = new ArrayList<Event>();    	
        
        try {   	
    		List<mockSet> LMockSet = admService.getMockSet(category_id,testMethod,school_id);   	      
    		for(int j=0;j<LMockSet.size();j++) { 	    
    			Event event = new Event();  
    			event.setTitle("");
    			event.setDescription0("<span style='border-radius:10px;padding:5px;background-color:darkorange;color:white;font-weight:bold'>"+LMockSet.get(j).getSlotName()+"</span>");
    			event.setStart(LMockSet.get(j).getDate_testMethod());
    			events.add(event);   	                	    	
            }
    
    		//假日停課 apply to every students
    		List<Suspension> LSuspension = admService.getSuspension("");   	      
    		for(int i=0;i<LSuspension.size();i++) { 	    
    			Event event = new Event();  
    			event.setTitle("");
    			event.setDescription0("<b><span style='border-radius:10px;padding:5px;background-color:#FFFF99;'>&nbsp;&nbsp;&#10022; "+LSuspension.get(i).getReason()+"&nbsp;&nbsp;</span></b>");
    			event.setStart(LSuspension.get(i).getSuspension_date());
    			//event.setBackgroundColor("");
    			events.add(event);   	                	    	
    		} 
    
    		ObjectMapper mapper = new ObjectMapper();
    		jsonMsg =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);

         } catch (IOException ioex) {
             System.out.println(ioex.getMessage());
         }

         return jsonMsg; 
     }  
    
    @RequestMapping("/Adm/SMS")
    public String SMS(Model model) {
		
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
				
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);    	
        return "/Adm/SMS";
    } 

    @RequestMapping("/Adm/openSMSList")
    public String openSMSList(Model model,HttpServletRequest request) {
    	List<Student> LStudent = salesService.getGradeStudent(
        		request.getParameter("ch_name"),
        		request.getParameter("en_name"),
        		request.getParameter("student_no"),
        		request.getParameter("idn"),
        		request.getParameter("mobile"),
        		request.getParameter("email"),
        		"",
        		request.getParameter("grade"),
        		"",
        		"",
        		"1" //訂班狀態 1:訂班,2:取消,3:保留
        );
    	model.addAttribute("LStudent",LStudent);
    	model.addAttribute("amount",LStudent.size());
  	
        return "/Adm/openSMSList";
    }
    
 @RequestMapping("/Adm/SMSSend")
 public String SMSSend(HttpServletRequest request,Principal principal) {
    	String[] A_mobile = request.getParameterValues("mobile");
    	String SmsContent = request.getParameter("SmsContent");
    try {
    	for(int i=0;i<A_mobile.length;i++) {
    		StringBuffer reqUrl = new StringBuffer();
    		reqUrl.append("https://sms.mitake.com.tw/b2c/mtk/SmSend?");
    		reqUrl.append("username=28851608");
    		reqUrl.append("&password=QWE0831class");
    		reqUrl.append("&dstadd=0939104534");
    		//reqUrl.append("&dstadd="+A_mobile[i]);
    		reqUrl.append("&smbody=" + URLEncoder.encode(SmsContent,"UTF-8"));		
    		reqUrl.append("&CharsetURL=UTF-8");
    	
    		URL url = new URL(reqUrl.toString());
    		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    		urlConnection.setRequestMethod("GET");
    		int code = urlConnection.getResponseCode();
    	}	
    } catch (Exception e) {
			e.printStackTrace();
	}    	
        return "/Adm/SMS";
    }
 
 @RequestMapping("/Adm/Email")
 public String Email(Model model,HttpSession session,HttpServletRequest request) {
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
		
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
				
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);    	
     return "/Adm/Email";
 }
 
 @RequestMapping("/Adm/openEmailList")
 public String openEmailList(Model model,HttpServletRequest request) {
 	List<Student> LStudent = salesService.getGradeStudent(
    		request.getParameter("ch_name"),
    		request.getParameter("en_name"),
    		request.getParameter("student_no"),
    		request.getParameter("idn"),
    		request.getParameter("mobile"),
    		request.getParameter("email"),
    		"",
    		request.getParameter("grade"),
    		"",
    		"",
    		"1" //訂班狀態 1:訂班,2:取消,3:保留
     );
 	model.addAttribute("LStudent",LStudent);
 	model.addAttribute("amount",LStudent.size());
	
     return "/Adm/openEmailList";
 } 
 
	/*
	 * @RequestMapping("/Adm/getStudentInfo")
	 * 
	 * @ResponseBody public String getStudentInfo(HttpServletRequest request){
	 * List<Student> LStudent =
	 * salesService.getStudent("","","",request.getParameter("student_no"),"","","",
	 * ""); if(LStudent.size()>0) { return "課程明稱"; }else { return "今日無此學員課程!"; } }
	 */
 
 
 @RequestMapping("/Adm/getStudentTodayGrade")
 @ResponseBody
 public String getStudentTodayGrade(HttpServletRequest request,Principal principal,@Value("${UploadPath}") String UploadPath){
	 String returnStr="";
	 String speechText="";
	 String student_no = request.getParameter("student_no");
	 String studentName = "";
	 String studentPhoto= "";
	 String outPublisher = "";
	 String lagnappe = "";
	 String makeUpTotal = "";
	 
	 if(student_no.equals("") || salesService.getStudent("","","",student_no,"","","","","","").size()==0) {
		 returnStr = "?"; 
	 }else {
		    //照片
	 		File dir = new File(UploadPath+"studentPhoto");
	 		File[] files = dir.listFiles((d, name) -> name.startsWith(student_no));
	 		if(files.length==0) {
	 			studentPhoto = "<img src='/images/nobody.png' height='80px'/>";
	 		}else {
	 			studentPhoto = "<img src='/images/studentPhoto/"+files[0].getName()+"' height='80px'/>";
	 		} 		 
		 
			String school_code = request.getParameter("school_code");
			String class_today = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
			String setDate = new SimpleDateFormat("yyyyMMdd").format(new Date()); 
			String student_id = salesService.getStudent("","","",student_no,"","","","","","").get(0).getStudent_seq();
			//學號及姓名
			studentName = "<div style=\"letter-spacing:1px;font-size:small\">"+student_no+"</div>"+salesService.getStudent("","","",student_no,"","","","","","").get(0).getCh_name();
			String makeUpTotal_no = salesService.getStudent("","","",student_no,"","","","","","").get(0).getMakeUpTotal();
			
			//報名領取		
	    		List<Register_outPublisher> LRegister_outPublisher = salesService.getRegister_outPublisher(student_id,"","","0");
	    		outPublisher +="<div style='color:white;background-color:#266489;text-align:center;padding:0px;border-radius:5px'>報名尚未領取 <A href='/Sales/LagnappeRecord?student_seq="+student_id+"' style='color:white;text-decoration:underline;font-size:large' target='_blank'>&hellip;</A></div>";
	    		for(int i=0;i<LRegister_outPublisher.size();i++) {
	    			outPublisher +="<div style='color:gold;font-size:small'>&bull; "+LRegister_outPublisher.get(i).getBook_name()+"</div>";
	    		}
	    		
	    		List<Register_lagnappe> LRegister_lagnappe = salesService.getRegister_lagnappe(student_id,"","","","0","");
	    		for(int i=0;i<LRegister_lagnappe.size();i++) {
	    			lagnappe +="<div style='color:gold;font-size:small'>&bull; "+LRegister_lagnappe.get(i).getLagnappe_name()+"</div>";
	    		}	    		
			//補課點數		
	    		makeUpTotal +="<div style='color:white;background-color:#266489;text-align:center;padding:3px;border-radius:5px'>Video 點數 : "+makeUpTotal_no+"</div>";
	    		if(makeUpTotal_no!=null && !makeUpTotal_no.isEmpty()) {
	    			if(Integer.valueOf(makeUpTotal_no)<0) {
	    				makeUpTotal +="<div Style='color:red'>( 請即刻補繳費用 )</div>";
	    			}
	    		}
	    	//教室
	    	String classRoomStr = "<select id='classRoom' onchange=getSeats('"+school_code+"',this.value,this) style='border:0px;width:75px;background-color:#3e7e99'><option value=''></option>";	
	    	List<classRoom> LclassRoom = systemService.getclassRoom(school_code,"","name","1","");
	    	for(int i=0;i<LclassRoom.size();i++) {
	    		classRoomStr+="<option value='"+LclassRoom.get(i).getName()+"'>"+LclassRoom.get(i).getName()+"</option>";
	    	}
	    	classRoomStr+="</select>";
	    		
			//學生所有課程 attend:0預定 
			List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory2("","","","","","1","(1)","(0)","",student_id,"","","","","","1","","");
			 for(int j=0;j<LSignRecordHistory.size();j++) {
				 
				//1.學生原定實體課程 或Video課程但尚未自訂日期
				if( (LSignRecordHistory.get(j).getAttend_date()==null || LSignRecordHistory.get(j).getAttend_date().isEmpty())){ 				 					 
			    	 //今日,本校,實體課程
					 List<Classes> LClasses= courseService.getClasses("","",class_today,school_code,"","","","","","");
				     for(int i=0;i<LClasses.size();i++) {
				    	  Grade grade = courseService.getGradeList("","",LClasses.get(i).getGrade_id(),"","","","","","","","","","","","","","1","1","").get(0);
					      //學生原定,今日,本校,實體課程
				    	  if(LSignRecordHistory.get(j).getGrade_id().equals(grade.getGrade_seq()) && LSignRecordHistory.get(j).getClass_th().equals(LClasses.get(i).getClass_th())) {
								 LClasses.get(i).setSubject_name(grade.getClass_start_date()+" "+LClasses.get(i).getSubject_name()+" "+(grade.getGradeName()==null?"":grade.getGradeName()));
								 //領取
								 String materialStr = "";
								 List<ClassesMaterial> LClassesMaterial = courseService.getClassesMaterial(LClasses.get(i).getClass_seq());

								 for(int a=0;a<LClassesMaterial.size();a++) {
									 if(LClassesMaterial.get(a).getMaterial_id().equals("1")) {
										 List<SignRecordMaterialHistory> LSignRecordMaterialHistory = courseService.getSignRecordMaterialHistory(LSignRecordHistory.get(j).getSignRecordHistory_seq());
										 if(LSignRecordMaterialHistory.size()>0) {
											 materialStr +="<div>(已領取)&nbsp;"+LClassesMaterial.get(a).getBookName()+"</div>";
										 }else {
											 materialStr +="<div style='color:gold'><input type='checkbox' checked='checked' style='width:15px;height:15px' name='classesMaterial_id' value='"+LClassesMaterial.get(a).getClassesMaterial_seq()+"'>&nbsp;"+LClassesMaterial.get(a).getBookName()+"</div>";
										 }	 
									 }else if(LClassesMaterial.get(a).getMaterial_id().equals("2")) {
										 List<SignRecordMaterialHistory> LSignRecordMaterialHistory = courseService.getSignRecordMaterialHistory(LSignRecordHistory.get(j).getSignRecordHistory_seq());
										 if(LSignRecordMaterialHistory.size()>0) {
											 materialStr +="<div>(已領取)&nbsp;"+LClassesMaterial.get(a).getInputTex()+"</div>";
										 }else {
											 materialStr +="<div style='color:gold'><input type='checkbox' checked='checked' style='width:15px;height:15px' name='classesMaterial_id' value='"+LClassesMaterial.get(a).getClassesMaterial_seq()+"'>&nbsp;"+LClassesMaterial.get(a).getInputTex()+"</div>";
										 }	 
									 }
								 }
								 String buttonOk = "OK";
								 String changeToReal = "0";
								 if(LSignRecordHistory.get(j).getClass_style().equals("2")) {
									 buttonOk="Video改正班";
									 changeToReal = "1";
								 }
								 returnStr +=
										   "<div class='tr' style='border-radius:10px'>"+
												 "<div class='td2' style='border-bottom:1px #999999 solid;width:220px;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+LClasses.get(i).getSubject_name()+"</div>"+
												 "<div class='td2' style='border-bottom:1px #999999 solid;width:80px;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+(LSignRecordHistory.get(j).getClass_style().equals("1")? "正班" : "Video")+"</div>"+
												 "<div class='td2' style='border-bottom:1px #999999 solid;width:130px;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+LClasses.get(i).getTime_from()+"~"+LClasses.get(i).getTime_to()+"</div>"+
												 "<div class='td2' style='border-bottom:1px #999999 solid;width:120px;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+LClasses.get(i).getTeacher_name()+"</div>"+
												 "<div class='td2' style='border-bottom:1px #999999 solid;width:60px;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+LClasses.get(i).getClass_th()+"</div>"+
												 "<div class='td2' style='border-bottom:1px #999999 solid;width:200px;vertical-align:middle;text-align:center;vertical-align:middle;font-weight:bold;font-size:medium;background-color:#3e7e99;color:white'>"+materialStr+"</div>"+
												 "<div class='td2' style='border-bottom:1px #999999 solid;width:80px;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+
												 		LClasses.get(i).getClassRoom()+
												 "</div>"+
												 "<div class='td2' style='border-bottom:1px #999999 solid;width:100px;font-weight:bold;text-align:center;font-size:medium;background-color:#3e7e99'>"+
											     "</div>"+													 
												 "<div class='td2' style='border-bottom:1px #999999 solid;width:300px;border-bottom:1px #999999 solid;text-align:left;vertical-align:middle;font-size:small;background-color:#3e7e99;padding:2px'>"+
												    LSignRecordHistory.get(j).getComment()+
												 	"<input type='text' id='comment' style='color:black;width:280px;height:25px;border:1px solid #eeeeee;border-radius:3px;background-color:#ffeeff'>"+
												 "</div>"+													 
												 "<div class='td2' style='border-bottom:1px #999999 solid;width:150px;text-align:center;vertical-align:middle;padding:5px;color:darkblue;font-weight:bold;font-size:large'><button type='button'  style='background-color:#266489;border:3px #555555 solid;padding:2px' class='btn btn-primary' onclick=this.disabled=true;signOk(this,'"+LSignRecordHistory.get(j).getSignRecordHistory_seq()+"','"+student_id+"','"+grade.getGrade_seq()+"','"+changeToReal+"','','','','0')>"+buttonOk+"</button></div>"+
										   "</div>";   
									 //speechText +="點名完成 請至 - 教室 ";
									 break; //跳出重複grade的課堂    
					       }
				     }		
			     
				 } 
				
				 //2. 學生自訂Video課程
				 String slot_name="";
				 if(LSignRecordHistory.get(j).getAttend_date().equals(class_today) && LSignRecordHistory.get(j).getSchool_code().equals(school_code)) {
						 //領取
						 
						 String materialStr2 = "";
						 List<Classes> LClasses= courseService.getClasses(LSignRecordHistory.get(j).getGrade_id(),"","","","","",LSignRecordHistory.get(j).getClass_th_ex(),"","","");
						 List<ClassesMaterial> LClassesMaterial = courseService.getClassesMaterial(LClasses.get(0).getClass_seq());
	 
						 for(int a=0;a<LClassesMaterial.size();a++) {
							 if(LClassesMaterial.get(a).getMaterial_id().equals("1")) {
								 List<SignRecordMaterialHistory> LSignRecordMaterialHistory = courseService.getSignRecordMaterialHistory(LSignRecordHistory.get(j).getSignRecordHistory_seq());
								 if(LSignRecordMaterialHistory.size()>0) {
									 materialStr2 +="<div>(已領取)&nbsp;"+LClassesMaterial.get(a).getBookName()+"</div>";
								 }else {
									 materialStr2 +="<div style='color:gold'><input type='checkbox' checked='checked' style='width:15px;height:15px' name='classesMaterial_id' value='"+LClassesMaterial.get(a).getClassesMaterial_seq()+"'>&nbsp;"+LClassesMaterial.get(a).getBookName()+"y</div>";
								 }	 
							 }else if(LClassesMaterial.get(a).getMaterial_id().equals("2")) {
								 List<SignRecordMaterialHistory> LSignRecordMaterialHistory = courseService.getSignRecordMaterialHistory(LSignRecordHistory.get(j).getSignRecordHistory_seq());
								 if(LSignRecordMaterialHistory.size()>0) {
									 materialStr2 +="<div>(已領取)&nbsp;"+LClassesMaterial.get(a).getInputTex()+"</div>";
								 }else {
									 materialStr2 +="<div style='color:gold'><input type='checkbox' checked='checked' style='width:15px;height:15px' name='classesMaterial_id' value='"+LClassesMaterial.get(a).getClassesMaterial_seq()+"'>&nbsp;"+LClassesMaterial.get(a).getInputTex()+"x</div>";
								 }	 
							 }
						 }
						 
						 
						 
						 if(LSignRecordHistory.get(j).getSlot().equals("1")) {
							 slot_name = "早";
						 }else if(LSignRecordHistory.get(j).getSlot().equals("2")) {
							 slot_name = "午";
						 }else if(LSignRecordHistory.get(j).getSlot().equals("3")) {
							 slot_name = "晚";
						 } 
				 			returnStr +=
							"<div class='tr' style='border-radius:10px'>"+
								 "<div class='td2' style='width:220px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+LSignRecordHistory.get(j).getClass_start_date()+" "+LSignRecordHistory.get(j).getSubject_name()+"</div>"+
								 "<div class='td2' style='width:80px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+(LSignRecordHistory.get(j).getClass_style().equals("1")? "正班" : "Video")+"</div>"+
								 "<div class='td2' style='width:130px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+slot_name+"</div>"+
								 "<div class='td2' style='width:120px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+LSignRecordHistory.get(j).getTeacher_name()+"</div>"+
								 "<div class='td2' style='width:60px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+LSignRecordHistory.get(j).getClass_th_ex()+"</div>"+
								 "<div class='td2' style='width:200px;border-bottom:1px #999999 solid;vertical-align:top;text-align:center;vertical-align:middle;font-weight:bold;font-size:medium;background-color:#3e7e99;color:white'>&nbsp;"+materialStr2+"</div>"+										 
								 "<div class='td2' style='width:80px;border-bottom:1px #999999 solid;font-weight:bold;vertical-align:middle;font-size:medium;background-color:#3e7e99' align='center'>"+
								    "<input type='hidden' class='slot' value='"+LSignRecordHistory.get(j).getSlot()+"'>"+
								 	classRoomStr+
								 "</div>"+
								 "<div class='td2' id='seatSel' style='width:100px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+
								 "</div>"+									 
								 "<div class='td2' style='width:300px;border-bottom:1px #999999 solid;text-align:left;vertical-align:middle;font-size:small;background-color:#3e7e99;padding:2px'>"+
								    LSignRecordHistory.get(j).getComment()+
								 	"<input type='text' id='comment' style='color:black;width:280px;height:25px;border:1px solid #eeeeee;border-radius:3px;background-color:#ffeeff;vertical-align:middle'>"+
								 "</div>"+
								 "<div class='td2' style='width:150px;border-bottom:1px #999999 solid;text-align:center;vertical-align:middle;padding:5px;color:darkblue;font-weight:bold;font-size:large'><button type='button' class='btn btn-primary' style='background-color:#266489;border:3px #555555 solid;padding:2px' onclick=this.disabled=true;signOk(this,'"+LSignRecordHistory.get(j).getSignRecordHistory_seq()+"','"+student_id+"','"+LSignRecordHistory.get(j).getGrade_id()+"','','"+LSignRecordHistory.get(j).getSlot()+"','"+LSignRecordHistory.get(j).getStudent_no()+"','"+LSignRecordHistory.get(j).getSchool_code()+"','1')>OK</button></div>"+
							"</div>";	   
							 //speechText +="點名完成 請至 - 教室 ";						 	 
				}
			}	 
				 
			//3.模考講解
				List<MockVideoHistory> LMockVideoHistory = courseService.getMockVideoHistory("","1","","(1)",student_id,setDate,"","","0","");
				for(int c=0;c<LMockVideoHistory.size();c++) {
		 			returnStr +=
					"<div class='tr' style='border-radius:10px'>"+
						 "<div class='td2' style='width:220px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+LMockVideoHistory.get(c).getMockVideo_name()+"</div>"+
						 "<div class='td2' style='width:80px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>模考講解</div>"+
						 "<div class='td2' style='width:130px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+LMockVideoHistory.get(c).getSlotName()+"</div>"+
						 "<div class='td2' style='width:120px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'></div>"+
						 "<div class='td2' style='width:60px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'></div>"+
						 "<div class='td2' style='width:200px;border-bottom:1px #999999 solid;vertical-align:top;text-align:center;vertical-align:middle;font-weight:bold;font-size:medium;background-color:#3e7e99;color:white'></div>"+										 
						 "<div class='td2' style='width:80px;border-bottom:1px #999999 solid;font-weight:bold;vertical-align:middle;font-size:medium;background-color:#3e7e99' align='center'>"+
						    "<input type='hidden' class='slot' value='"+LMockVideoHistory.get(c).getSlot()+"'>"+
						 	classRoomStr+
						 "</div>"+
						 "<div class='td2' id='seatSel' style='width:100px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+
						 "</div>"+								 
						 "<div class='td2' style='width:300px;border-bottom:1px #999999 solid;text-align:left;vertical-align:middle;font-size:small;background-color:#3e7e99;padding:2px'>"+
						 	LMockVideoHistory.get(c).getComment()+
						 	"<input type='text' id='comment' style='color:black;width:280px;height:25px;border:1px solid #eeeeee;border-radius:3px;background-color:#ffeeff;vertical-align:middle'>"+
						 "</div>"+
						 "<div class='td2' style='width:150px;border-bottom:1px #999999 solid;text-align:center;vertical-align:middle;padding:5px;color:darkblue;font-weight:bold;font-size:large'><button type='button'  class='btn btn-primary' style='background-color:#266489;border:3px #555555 solid;padding:2px' onclick=this.disabled=true;signOk_c(this,'"+LMockVideoHistory.get(c).getMockVideoHistory_seq()+"','"+student_id+"','"+LMockVideoHistory.get(c).getSlot()+"','"+LMockVideoHistory.get(c).getStudent_no()+"','"+LMockVideoHistory.get(c).getSchool_code()+"','"+LMockVideoHistory.get(c).getSlot()+"','"+LMockVideoHistory.get(c).getStudent_no()+"','"+LMockVideoHistory.get(c).getSchool_code()+"')>OK</button></div>"+
					"</div>";				
				}			 
			//4.模考
			List<MockBaseBook2> LMockBaseBook2 = salesService.getMockBaseBook(student_id,"1","",setDate,"","","","","","");
			for(int d=0;d<LMockBaseBook2.size();d++) {
	 			returnStr +=
				"<div class='tr' style='border-radius:10px'>"+
					 "<div class='td2' style='width:220px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+LMockBaseBook2.get(d).getMockSubjectName()+"</div>"+
					 "<div class='td2' style='width:80px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>模考</div>"+
					 "<div class='td2' style='width:130px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+LMockBaseBook2.get(d).getMockParaName()+"</div>"+
					 "<div class='td2' style='width:120px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'></div>"+
					 "<div class='td2' style='width:60px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'></div>"+
					 "<div class='td2' style='width:200px;border-bottom:1px #999999 solid;vertical-align:top;text-align:center;vertical-align:middle;font-weight:bold;font-size:medium;background-color:#3e7e99;color:white'></div>"+										 
					 "<div class='td2' style='width:80px;border-bottom:1px #999999 solid;font-weight:bold;vertical-align:middle;font-size:medium;background-color:#3e7e99' align='center'>"+
					    "<input type='hidden' class='slot' value='"+LMockBaseBook2.get(d).getSlot()+"'>"+
					 	classRoomStr+
					 "</div>"+
					 "<div class='td2' id='seatSel' style='width:100px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+
					 "</div>"+									 
					 "<div class='td2' style='width:300px;border-bottom:1px #999999 solid;text-align:left;vertical-align:middle;font-size:small;background-color:#3e7e99;padding:2px'>"+
					 	LMockBaseBook2.get(d).getComment()+
					 	"<input type='text' id='comment' style='color:black;width:280px;height:25px;border:1px solid #eeeeee;border-radius:3px;background-color:#ffeeff;vertical-align:middle'>"+
					 "</div>"+
					 "<div class='td2' style='width:150px;border-bottom:1px #999999 solid;text-align:center;vertical-align:middle;padding:5px;color:darkblue;font-weight:bold;font-size:large'><button type='button'  class='btn btn-primary' style='background-color:#266489;border:3px #555555 solid;padding:2px' onclick=this.disabled=true;signOk_d(this,'"+LMockBaseBook2.get(d).getMockBaseBook_seq()+"','"+student_id+"','"+LMockBaseBook2.get(d).getSlot()+"','"+LMockBaseBook2.get(d).getStudent_no()+"','"+LMockBaseBook2.get(d).getSchool_code()+"')>OK</button></div>"+
				"</div>";				
			}
			
			//5.充電站
			List<CounselingBaseBook> LCounselingBaseBook = salesService.getCounselingBaseBook("","1",setDate,"","","","","","","1");
			for(int d=0;d<LCounselingBaseBook.size();d++) {			
				returnStr+=
				"<div class='tr' style='border-radius:10px'>"+
						 "<div class='td2' style='width:220px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+LCounselingBaseBook.get(d).getCounseling_name()+"</div>"+
						 "<div class='td2' style='width:80px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>充電站</div>"+
						 "<div class='td2' style='width:130px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+LCounselingBaseBook.get(d).getLimitName()+"</div>"+
						 "<div class='td2' style='width:120px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'></div>"+
						 "<div class='td2' style='width:60px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'></div>"+
						 "<div class='td2' style='width:200px;border-bottom:1px #999999 solid;vertical-align:top;text-align:center;vertical-align:middle;font-weight:bold;font-size:medium;background-color:#3e7e99;color:white'></div>"+										 
						 "<div class='td2' style='width:80px;border-bottom:1px #999999 solid;font-weight:bold;vertical-align:middle;font-size:medium;background-color:#3e7e99' align='center'>"+
						    "<input type='hidden' class='slot' value='"+LCounselingBaseBook.get(d).getSlot()+"'>"+
						 	classRoomStr+
						 "</div>"+
						 "<div class='td2' id='seatSel' style='width:100px;border-bottom:1px #999999 solid;font-weight:bold;text-align:center;vertical-align:middle;font-size:medium;background-color:#3e7e99'>"+
						 "</div>"+									 
						 "<div class='td2' style='width:300px;border-bottom:1px #999999 solid;text-align:left;vertical-align:middle;font-size:small;background-color:#3e7e99;padding:2px'>"+
						 	LCounselingBaseBook.get(d).getComment()+
						 	"<input type='text' id='comment' style='color:black;width:280px;height:25px;border:1px solid #eeeeee;border-radius:3px;background-color:#ffeeff;vertical-align:middle'>"+
						 "</div>"+
						 "<div class='td2' style='width:150px;border-bottom:1px #999999 solid;text-align:center;vertical-align:middle;padding:5px;color:darkblue;font-weight:bold;font-size:large'><button type='button'  class='btn btn-primary' style='background-color:#266489;border:3px #555555 solid;padding:2px' onclick=this.disabled=true;signOk_e(this,'"+LCounselingBaseBook.get(d).getCounselingBaseBook_seq()+"','"+student_id+"','"+LCounselingBaseBook.get(d).getSlot()+"','"+LCounselingBaseBook.get(d).getStudent_no()+"','"+LCounselingBaseBook.get(d).getSchool_code()+"')>OK</button></div>"+
			    "</div>";						
			}				
		   
	}	 
		  
	if(returnStr.equals("?")) {
		return "?";
	}else if(returnStr.equals("")) {
		return "<input type='hidden' id='makeUpTotal' value=\""+makeUpTotal+"\"><input type='hidden' id='outPublisher' value=\""+outPublisher+"\"><input type='hidden' id='lagnappe' value=\""+lagnappe+"\"><input type='hidden' id='studentPhoto' value=\""+studentPhoto+"\"><input type='hidden' id='studentName' value='"+studentName+"'><input type='hidden' id='speechText' value='今日無此學員課程'><div style='font-weight:bold;padding:10px'>今日無此學員課程 / 無法進班 / 已點名完畢 !</div>";
	}else {
		return "<input type='hidden' id='makeUpTotal' value=\""+makeUpTotal+"\"><input type='hidden' id='outPublisher' value=\""+outPublisher+"\"><input type='hidden' id='lagnappe' value=\""+lagnappe+"\"><input type='hidden' id='studentPhoto' value=\""+studentPhoto+"\"><input type='hidden' id='studentName' value='"+studentName+"'><input type='hidden' id='speechText' value='"+speechText+"'>"+returnStr;
	}	
 }
 
 
 @RequestMapping("/Adm/signRecordSave")
 @ResponseBody
 public String signRecordSave(HttpServletRequest request,Principal principal){
		String student_no 	= request.getParameter("student_no");
		String class_th 	= request.getParameter("class_th");
		String grade_id 	= request.getParameter("grade_id");	 
		String updater      = principal.getName();

		String returnStr = admService.signRecordSave(grade_id,class_th,student_no,updater);

 		return returnStr;
 }  


 @RequestMapping("/Adm/todaySign")
 public String todaySign(Model model,Principal principal,HttpSession session,HttpServletRequest request){ 
		List<School> LSchool = accountService.getSchool("","");
		model.addAttribute("LSchool", LSchool);	
		String SchoolCode = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		model.addAttribute("SchoolCode",SchoolCode);			

		return "/Adm/todaySign";
 } 
 
 @RequestMapping("/Adm/lectureSign")
 public String lectureSign(Model model,Principal principal){
		return "/Adm/lectureSign";
} 
 
 @RequestMapping("/Adm/speech")
 @ResponseBody
 public String speech(HttpServletRequest request){
		String speechText= request.getParameter("speechText");
		jacobtest.textToSpeech(speechText);
		
		return "true";
 }
 
 @RequestMapping("/Adm/classRemark")
 public String classRemark(HttpServletRequest request,Model model,Principal principal,HttpSession session){
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
		
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
				
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		List<FlowStatus> flowStatusGroup = courseService.getFlowStatus();
		model.addAttribute("flowStatusGroup", flowStatusGroup);
		
		String SchoolCode = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		model.addAttribute("SchoolCode",SchoolCode);			

		return "/Adm/classRemark";
 } 
 
 @RequestMapping("/Adm/openTodaySign")
 public String openTodaySign(Model model,HttpServletRequest request,Principal principal) {
		String SchoolCode = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		model.addAttribute("SchoolCode",SchoolCode);
     return "/Adm/openTodaySign";
 }
 
 @RequestMapping("/Adm/signOk")
 @ResponseBody
 public String signOk(Model model,HttpServletRequest request,Principal principal) {
	 String signRecordHistory_seq = request.getParameter("signRecordHistory_seq");	
	 String student_id = request.getParameter("student_id");
	 String grade_id = request.getParameter("grade_id");
	 String updater = principal.getName();
	 String[] A_classesMaterial_id = request.getParameterValues("classesMaterial_id");
	 String changeToReal = request.getParameter("changeToReal");
	 String comment = request.getParameter("comment");
	 String seat = request.getParameter("seat");
	 String classRoom = request.getParameter("classRoom");
	 
	 String slot = request.getParameter("slot");
	 String student_no = request.getParameter("student_no");
	 String school_code = request.getParameter("school_code");

	//更新點名紀錄
	admService.UpdateSignRecord2(signRecordHistory_seq,student_id,A_classesMaterial_id,updater,changeToReal,comment,seat,classRoom);
	//非正班之座位使用
	if(seat!=null && !seat.isEmpty()) {
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
		String setDate = sdFormat.format(new Date());		
		admService.insertSeatOccupy(school_code,setDate,slot,classRoom,seat,student_no);
	}	
 	//取出點名紀錄
	String recordListStr = admService.getRecordListStr(grade_id,student_id,"","");	
	String returnStr ="<div class='css-table' style='border-spacing:1px'>"+recordListStr+"</div>";
    return returnStr;
 }
 
 @RequestMapping("/Adm/signOk_c")
 @ResponseBody
 public String signOk_c(Model model,HttpServletRequest request,Principal principal) {
	 String mockVideoHistory_seq = request.getParameter("mockVideoHistory_seq");	
	 String updater = principal.getName();
	 String comment = request.getParameter("comment");
	 String seat = request.getParameter("seat");
	 String classRoom = request.getParameter("classRoom");
	 
	 String slot = request.getParameter("slot");
	 String student_no = request.getParameter("student_no");
	 String school_code = request.getParameter("school_code");	 

	admService.UpdateMockVideoHistory(mockVideoHistory_seq,updater,comment,seat,classRoom);
	//非正班之座位使用
	if(seat!=null && !seat.isEmpty()) {
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
		String setDate = sdFormat.format(new Date());		
		admService.insertSeatOccupy(school_code,setDate,slot,classRoom,seat,student_no);
	}
    return "<div style='font-weight:bold;color:gold;letter-spacing:5px'>模考講解點名成功!</div>";
 } 
 
 @RequestMapping("/Adm/signOk_d")
 @ResponseBody
 public String signOk_d(Model model,HttpServletRequest request,Principal principal) {
	 String mockBaseBook_seq = request.getParameter("mockBaseBook_seq");	
	 String updater = principal.getName();
	 String comment = request.getParameter("comment");
	 String seat = request.getParameter("seat");
	 String classRoom = request.getParameter("classRoom");
	 
	 String slot = request.getParameter("slot");
	 String student_no = request.getParameter("student_no");
	 String school_code = request.getParameter("school_code");	 

	admService.UpdateMockBaseBook(mockBaseBook_seq,updater,comment,seat,classRoom);
	
	//非正班之座位使用
	if(seat!=null && !seat.isEmpty()) {
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
		String setDate = sdFormat.format(new Date());		
		admService.insertSeatOccupy(school_code,setDate,slot,classRoom,seat,student_no);
	}	

    return "<div style='font-weight:bold;color:gold;letter-spacing:5px'>模考點名成功!</div>";
 }
 
 @RequestMapping("/Adm/signOk_e")
 @ResponseBody
 public String signOk_e(Model model,HttpServletRequest request,Principal principal) {
	 String CounselingBaseBook_seq = request.getParameter("CounselingBaseBook_seq");	
	 String updater = principal.getName();
	 String comment = request.getParameter("comment");
	 String seat = request.getParameter("seat");
	 String classRoom = request.getParameter("classRoom");
	 
	 String slot = request.getParameter("slot");
	 String student_no = request.getParameter("student_no");
	 String school_code = request.getParameter("school_code");	 

	admService.UpdateCounselingBaseBook(CounselingBaseBook_seq,updater,comment,seat,classRoom);
	
	//非正班之座位使用
	if(seat!=null && !seat.isEmpty()) {
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
		String setDate = sdFormat.format(new Date());		
		admService.insertSeatOccupy(school_code,setDate,slot,classRoom,seat,student_no);
	}	

    return "<div style='font-weight:bold;color:gold;letter-spacing:5px'>充電站點名成功!</div>";
 }  
 
	@RequestMapping("/Adm/getTodayVideoStudentList")
	@ResponseBody
	public List<SignRecordHistory> getTodayVideoStudentList(Model model,HttpServletRequest request){
		 String school_code = request.getParameter("school_code");
		 String student_no = request.getParameter("student_no");
		 String subject_id = request.getParameter("subject_id");
		 String category_id = request.getParameter("category_id");
		 String attend_date = request.getParameter("attend_date");
		 String video_date = request.getParameter("video_date");
		 String ch_name = request.getParameter("ch_name");
		 
		 String class_start_date_0 = "";
		 String class_start_date = request.getParameter("class_start_date");
		 if(class_start_date!=null && class_start_date.length()==6) {
			 class_start_date_0 = class_start_date.substring(2,4)+"/"+class_start_date.substring(4,6)+"/"+"20"+class_start_date.substring(0,2);
		 }			 
		
		  DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); 
		  String isToday = request.getParameter("isToday"); 
		  if(isToday!=null && isToday.equals("1")) {
			  attend_date = dateFormat.format(new Date());
		  }
		 
		 List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory2(school_code,student_no,"","","","1","(1)","(0,1,-1)","2","",subject_id,class_start_date_0,attend_date,"",video_date,"",ch_name,"");
		 return LSignRecordHistory;
	}
	
	
	@RequestMapping("/Adm/StudentVideoSignRecord")
	public String StudentVideoSignRecord(Model model,HttpServletRequest request,Principal principal){
		List<School> schoolGroup = accountService.getSchool("","");
		model.addAttribute("schoolGroup", schoolGroup);	
		
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);

		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		String SchoolCode = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		model.addAttribute("SchoolCode",SchoolCode);
		
		return "/Adm/StudentVideoSignRecord";
	}
	
	@RequestMapping("/Adm/editSignRecord")
	public String editSignRecord(Model model,HttpServletRequest request,Principal principal){
		String signRecordHistory_seq = request.getParameter("signRecordHistory_seq");
		model.addAttribute("signRecordHistory_seq",signRecordHistory_seq);
		model.addAttribute("attend",request.getParameter("attend"));
    	model.addAttribute("creater",principal.getName());
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
    	model.addAttribute("createTime",formatter.format(new Date()));		
    	
    	
    	SignRecordHistory signRecordHistory = admService.getSignRecordHistory(signRecordHistory_seq,"","","","","","","","","","").get(0);
    	String checked_x="",checked_y="";
    	if(signRecordHistory.getAllowAttend().equals("0")) {
    		checked_x = "checked";
    	}else if(signRecordHistory.getAllowAttend().equals("1")) {
    		checked_y = "checked";
    	}
    	String allowAttendStr = 
    			"<div><input type='radio' name='allowAttend' value='0' "+checked_x+">&nbsp;不可</div>"+	
    			"<div><input type='radio' name='allowAttend' value='1' "+checked_y+">&nbsp;可</div>";
    	model.addAttribute("allowAttendStr",allowAttendStr);
    	
    	String checked_a="",checked_b="",checked_c="";
    	if(signRecordHistory.getAttend().equals("0")) {
    		checked_a = "checked";
    	}else if(signRecordHistory.getAttend().equals("1")) {
    		checked_b = "checked";
    	}else if(signRecordHistory.getAttend().equals("-1")) {
    		checked_c = "checked";
    	} 
    	String attendStr = 
    			"<div><input type='radio' name='signRecord' value='0' "+checked_a+">&nbsp;未 點&nbsp;&nbsp;&nbsp;<img src='/images/WhiteSquare.png' height='10px'/></div>"+	
    			"<div><input type='radio' name='signRecord' value='1' "+checked_b+">&nbsp;出 席&nbsp;&nbsp;&nbsp;<img src='/images/GreenSquare.png' height='10px'/></div>"+	
    			"<div><input type='radio' name='signRecord' value='-1' "+checked_c+">&nbsp;缺 席&nbsp;&nbsp;&nbsp;<img src='/images/RedSquare.png' height='10px'/></div>";
    	model.addAttribute("attendStr",attendStr);
    	
    	String otherStr =
    			"<div>"+
    			   "<span>教室 <input type='text' id='classroom' value='"+(signRecordHistory.getClassroom()==null?"":signRecordHistory.getClassroom())+"' style='color:black;width:35px;height:27px;border:1px solid #eeeeee;border-radius:5px;background-color:white'></span>"+
    			   "&nbsp;&nbsp;<span>座位 <input type='text' id='seat' value='"+(signRecordHistory.getSeat()==null?"":signRecordHistory.getSeat())+"' style='color:black;width:35px;height:25px;border:1px solid #eeeeee;border-radius:5px;background-color:white'></span>"+
    			"</div>"+
    			"<div style='margin-top:5px'>"+
    			   "<span>備註 <input type='text' id='comment' value='"+signRecordHistory.getComment()+"' style='color:black;width:300px;height:27px;border:1px solid #eeeeee;border-radius:5px;background-color:white'></span>"+
    			"</div>";
    	
    	model.addAttribute("otherStr",otherStr);
    	
    	String materialStr = "&nbsp;";
    	//Excel移轉進來的東西
    	List<SignRecordMaterialHistory> LSignRecordMaterialHistory = courseService.getSignRecordMaterialHistory_0(signRecordHistory_seq);
    	for(int j=0;j<LSignRecordMaterialHistory.size();j++) {
	    	if(LSignRecordMaterialHistory.get(j).getClassesMateria_id().equals("-1")) {
				materialStr += "[講義]";
			}
    	}
    	//本課程原來應改領的東西
    	List<ClassesMaterial> LClassesMaterial = courseService.getClassesMaterial(signRecordHistory.getClass_id());
    	for(int i=0;i<LClassesMaterial.size();i++) {
    		String checked = "";			
			for(int j=0;j<LSignRecordMaterialHistory.size();j++) {
				if(LSignRecordMaterialHistory.get(j).getClassesMateria_id().equals(LClassesMaterial.get(i).getClassesMaterial_seq())) {
					checked = "checked";
					if(LClassesMaterial.get(i).getMaterial_id().equals("1")) {
						materialStr +=
								"<input type='checkbox' name='classesMaterial_seq' value='"+LClassesMaterial.get(i).getClassesMaterial_seq()+"' "+checked+">"+
						        " [書籍]"+LClassesMaterial.get(i).getBookName();
					}else if(LClassesMaterial.get(i).getMaterial_id().equals("2")) {
						materialStr +=
								"<input type='checkbox' name='classesMaterial_seq' value='"+LClassesMaterial.get(i).getClassesMaterial_seq()+"' "+checked+">"+
						        " [講義]"+LClassesMaterial.get(i).getInputTex();
					}
				}
			}
		}
    	
		model.addAttribute("materialStr",materialStr);
    	return "/Adm/editSignRecord";
	}

	@RequestMapping("/Adm/manualSignRecord")
	@ResponseBody
	public String manualSignRecord(Model model,HttpServletRequest request,Principal principal){	
		String signRecordHistory_seq = request.getParameter("signRecordHistory_seq");
		String attend_ori = request.getParameter("allowAttend");
		String allowAttend = request.getParameter("allowAttend");
		String attend = request.getParameter("attend");
		String[] A_classesMaterial_seq = request.getParameterValues("classesMaterial_seq");
		String classroom = request.getParameter("classroom");
		String seat = request.getParameter("seat");
		String comment = request.getParameter("comment");
	    admService.manualSignRecord(signRecordHistory_seq,attend_ori,allowAttend,attend,A_classesMaterial_seq,principal.getName(),classroom,seat,comment);
	    
	    return "success";
	}
	
	@RequestMapping("/Adm/TodayBook")
	public String TodayBook(Model model,HttpServletRequest request){
		return "/Adm/TodayBook";
	}
	
   @RequestMapping("/Adm/upload2") 
   public String upload2(HttpServletRequest request) {
	   String passArea = request.getParameter("passArea");
	   try {
		   	systemService.upload2(passArea);
	   }catch (IOException e) {
		   		e.printStackTrace();
	   }
	   return "redirect:/Adm/openUpload";
   } 
   
   @RequestMapping("/Adm/programSetting2")
   public String programSetting2(Model model,HttpServletRequest request,HttpSession session) {
		
		List<School> schoolGroup = accountService.getSchool("","");
		model.addAttribute("schoolGroup", schoolGroup);	
		
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int range = 2; //2個月間隔
		String beginYear = String.valueOf(year);
		String beginMonth = String.valueOf(month+1);
		String endMonth = String.valueOf(month+range>12?month+range-12:month+range);
		String endYear = "";
		if(month+range>12) {
			endYear = String.valueOf(Integer.valueOf(beginYear)+1);
		}else{
			endYear=beginYear;
		}
				
		model.addAttribute("beginYear",beginYear);
		model.addAttribute("endYear",endYear);
		model.addAttribute("beginMonth",beginMonth);
		model.addAttribute("endMonth",endMonth);
		
       return "/Adm/programSetting2";
   }
   
   @RequestMapping("/Adm/programSettingWait")
   public String programSettingWait(Model model,HttpServletRequest request,HttpSession session) {
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
		
		List<School> schoolGroup = accountService.getSchool("","");
		model.addAttribute("schoolGroup", schoolGroup);	
		
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int range = 2; //2個月間隔
		String beginYear = String.valueOf(year);
		String beginMonth = String.valueOf(month+1);
		String endMonth = String.valueOf(month+range>12?month+range-12:month+range);
		String endYear = "";
		if(month+range>12) {
			endYear = String.valueOf(Integer.valueOf(beginYear)+1);
		}else{
			endYear=beginYear;
		}
				
		model.addAttribute("beginYear",beginYear);
		model.addAttribute("endYear",endYear);
		model.addAttribute("beginMonth",beginMonth);
		model.addAttribute("endMonth",endMonth);
		
       return "/Adm/programSettingWait";
   }   

   @RequestMapping("/Adm/admCalendar")
   public String admCalendar(Model model,HttpServletRequest request,HttpSession session,Principal principal) {
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
		
		String saveMessage = request.getParameter("saveMessage");
		model.addAttribute("saveMessage", saveMessage);	
		
		List<School> schoolGroup = accountService.getSchool("","");
		model.addAttribute("schoolGroup", schoolGroup);	
		
		String school_code_self = "";
		if(principal!=null && principal.getName()!=null) {	
			school_code_self = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		}
		model.addAttribute("school_code_self", school_code_self);
		
		Calendar c = Calendar.getInstance();
		String beginYear = String.valueOf(c.get(Calendar.YEAR));
		String beginMonth = String.valueOf(c.get(Calendar.MONTH)+1);
		if(beginMonth.length()==1) {beginMonth="0"+beginMonth;}
		String beginDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		if(beginDay.length()==1) {beginDay="0"+beginDay;}
		String weekName = "";
		switch(c.get(Calendar.DAY_OF_WEEK)){
			case 1:weekName="星期日";break;
			case 2:weekName="星期一";break;
			case 3:weekName="星期二";break;
			case 4:weekName="星期三";break;
			case 5:weekName="星期四";break;
			case 6:weekName="星期五";break;
			case 7:weekName="星期六";break;
		}	

		model.addAttribute("beginYear",beginYear);
		model.addAttribute("beginMonth",beginMonth);
		model.addAttribute("beginDay",beginDay);
		model.addAttribute("weekName",weekName);		
		
       return "/Adm/admCalendar";
   } 

   @RequestMapping("/Adm/admCalendarEdit")
   public String admCalendarEdit(Model model,HttpServletRequest request,HttpSession session,Principal principal) {
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
		
		String saveMessage = request.getParameter("saveMessage");
		model.addAttribute("saveMessage", saveMessage);	
		
		List<School> schoolGroup = accountService.getSchool("","");
		model.addAttribute("schoolGroup", schoolGroup);	
		
		String school_code_self = "";
		if(principal!=null && principal.getName()!=null) {	
			school_code_self = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		}
		model.addAttribute("school_code_self", school_code_self);
		
		String beginYear = "";
		String beginMonth= "";
		String beginDay  = "";
		Calendar c = Calendar.getInstance();
		if(request.getParameter("noSession")!=null && request.getParameter("noSession").equals("0")) {
			if(session.getAttribute("beginYear")!=null) {
				beginYear = (String)session.getAttribute("beginYear");
				beginMonth = (String)session.getAttribute("beginMonth");
				beginDay = (String)session.getAttribute("beginDay");
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Date date;
				try{
					date = sdf.parse(beginYear+beginMonth+beginDay);
					c.setTime(date);
				}catch (ParseException e) {e.printStackTrace();}								
			}			
		}else{
			beginYear = String.valueOf(c.get(Calendar.YEAR));
			beginMonth = String.valueOf(c.get(Calendar.MONTH)+1);
			if(beginMonth.length()==1) {beginMonth="0"+beginMonth;}
			beginDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
			if(beginDay.length()==1) {beginDay="0"+beginDay;}
		}
		
		
		String weekName = "";
		switch(c.get(Calendar.DAY_OF_WEEK)){
			case 1:weekName="星期日";break;
			case 2:weekName="星期一";break;
			case 3:weekName="星期二";break;
			case 4:weekName="星期三";break;
			case 5:weekName="星期四";break;
			case 6:weekName="星期五";break;
			case 7:weekName="星期六";break;
		}	

		model.addAttribute("beginYear",beginYear);
		model.addAttribute("beginMonth",beginMonth);
		model.addAttribute("beginDay",beginDay);
		model.addAttribute("weekName",weekName);		
		
       return "/Adm/admCalendarEdit";
   }   
   
   @RequestMapping("/Adm/GetAdmCalendarJob")
   @ResponseBody
   public String GetAdmCalendarJob(Model model,HttpServletRequest request,Principal principal,HttpSession session) {	   
  	 String beginYear  = request.getParameter("beginYear");
  	 session.setAttribute("beginYear",beginYear);
  	 String beginMonth = request.getParameter("beginMonth");
  	 session.setAttribute("beginMonth",beginMonth);
  	 String beginDay   = request.getParameter("beginDay");
  	 session.setAttribute("beginDay",beginDay);
  	 String school_code= request.getParameter("school_code");
  	 String editMode   = request.getParameter("editMode");
	 String selectOption = "";
	 String textAreaEdit = "";
		if(editMode.equals("0") || !(request.isUserInRole("adm_mgr") || request.isUserInRole("approve_mgr"))){
			selectOption = "disabled";
			if(editMode.equals("0")) {
				textAreaEdit = "readonly";
			}			
		}else{
			selectOption = "";
			textAreaEdit = "";
		}
  	 
  	 List<Classes> Lclasses = courseService.getClasses("","",beginMonth+"/"+beginDay+"/"+beginYear,school_code,"","(4,5)","","","","");
  	 List<Classes> Lclasses_slot1 = new ArrayList<Classes>();
  	 List<AdmGradeJob> LAdmGradeJob_slot1 = new ArrayList<AdmGradeJob>();
  	 List<Classes> Lclasses_slot2 = new ArrayList<Classes>();
  	 List<AdmGradeJob> LAdmGradeJob_slot2 = new ArrayList<AdmGradeJob>();
  	 List<Classes> Lclasses_slot3 = new ArrayList<Classes>();
  	 List<AdmGradeJob> LAdmGradeJob_slot3 = new ArrayList<AdmGradeJob>();
  	 
  	 for(int i=0;i<Lclasses.size();i++) {

  		 if(Lclasses.get(i).getSlot_id().equals("1")) {
  			Lclasses_slot1.add(Lclasses.get(i));
  			
  	  		List<AdmGradeJob> LAdmGradeJob = admService.getAdmGradeJob(Lclasses.get(i).getGrade_id(),Lclasses.get(i).getClass_th());
  	  		for(int x=0;x<LAdmGradeJob.size();x++) {
  	  			LAdmGradeJob_slot1.add(LAdmGradeJob.get(x));
  	  		}
  	  			
  		 }else if(Lclasses.get(i).getSlot_id().equals("2")) {
   			Lclasses_slot2.add(Lclasses.get(i));
   			
  	  		List<AdmGradeJob> LAdmGradeJob = admService.getAdmGradeJob(Lclasses.get(i).getGrade_id(),Lclasses.get(i).getClass_th());
  	  		for(int x=0;x<LAdmGradeJob.size();x++) {
  	  			LAdmGradeJob_slot2.add(LAdmGradeJob.get(x));
  	  		} 
  	  	    
   		 }else if(Lclasses.get(i).getSlot_id().equals("3")) {
    		Lclasses_slot3.add(Lclasses.get(i));
    		
  	  		List<AdmGradeJob> LAdmGradeJob = admService.getAdmGradeJob(Lclasses.get(i).getGrade_id(),Lclasses.get(i).getClass_th());
  	  		for(int x=0;x<LAdmGradeJob.size();x++) {
  	  			LAdmGradeJob_slot3.add(LAdmGradeJob.get(x));
  	  		}     		
   		 }		
  	 }
  	 
  	 List<JobContent> LJobContent = admService.getJobContent(school_code,beginYear+beginMonth+beginDay);  	
  	 
 	 List<Employee> LEmployee = accountService.getEmployee("","",school_code,"","","1","","");
 	 String AdmEmployee = "<option value=''></option>";
 	 for(int i=0;i<LEmployee.size();i++) {
 		AdmEmployee += "<option value='"+LEmployee.get(i).getEmployee_seq()+"'>"+LEmployee.get(i).getCh_name()+"</option>";
 	 }
	 String jobChecked = "";
  	 String returnStr =
  	 "<div class='css-table' style='width:1000px;text-align:left;margin-top:10px'>";
  	  	 if(editMode.equals("1")) {		    
  	  	 	 returnStr+=   	 
  			 "<div class='tr'><div class='td' style='font-weight:bold'><span style='border:1px #dddddd solid;background-color:#FEFFEF'>&nbsp;&nbsp;&#9788; 例 行 工 作&nbsp;&nbsp;</span>&nbsp";
  			   if(request.isUserInRole("adm_mgr") || request.isUserInRole("approve_mgr")) {
  					 returnStr +=
  					 "<A href='javascript:void(0)' onclick='addAdmJob(1)' title='新增行政例行工作'><img src='/images/edit.png' height='12px' class='expired' /></A>";
  			   }
  			 returnStr+= "</div></div>";   
  	  	 }else {
  	  	 	 returnStr+=   	 
  			 "<div class='tr'><div class='td' style='font-weight:bold'><span style='border:1px #dddddd solid;background-color:#FEFFEF'>&nbsp;&nbsp;&#9788; 例 行 工 作&nbsp;&nbsp;</span>&nbsp;</div></div>";  	  		 
  	  	 }
//*****************************1.例行工作**********************************//   	  	 
  	         returnStr += 
  			 "<div class='tr'>"+
  			 	"<div class='td' style='padding:5px'>"+
  			 		"<div id='area1' style='font-size:small;border:1px #dddddd solid;padding:10px;width:990px;height:auto;background-color:white'>";
  	 					for(int i=0;i<LJobContent.size();i++) {
  	 						if(LJobContent.get(i).getCategory().equals("1")) {
  	 							if(editMode.equals("0")) {
									if(LJobContent.get(i).getFinishName()!=null && !LJobContent.get(i).getFinishName().isEmpty()) {
										jobChecked = "checked disabled";
										
									}else {
										jobChecked = "";
									}
  	 							}else {
  	 								jobChecked = "disabled";
  	 							}
  	 							
  	 							
  	 							if(editMode.equals("1")) {
	  	 						        returnStr +=
	  	 								"<div style='padding:2px'><A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>";
  	 							}else {
  	 									returnStr +=    
  	 									"<div style='padding:2px'>";
  	 							}
  	 										returnStr +=		
  	 										"<select name='worker1' "+selectOption+" style='padding:0px;display:inline-block;height:25px;width:80px;vertical-align:bottom;border:1px #dddddd solid'>";
	  	 									String checked = "";
	  	 									returnStr += "<option value=''></option>";
	  	 									for(int x=0;x<LEmployee.size();x++) {
	  	 										checked="";
	  	 										if(LJobContent.get(i).getWorkerId().equals(LEmployee.get(x).getEmployee_seq())){checked=" selected";}
	  	 										returnStr +=
	  	 										"<option value='"+LEmployee.get(x).getEmployee_seq()+"'"+checked+">"+LEmployee.get(x).getCh_name()+"</option>";		
	  	 									}
	  	 								returnStr +=	
	  	 								"</select>"+
	  	 								"<input type='hidden' id='previousStatus' value='"+jobChecked+"'>"+
	  	 								"<input type='checkbox'  "+jobChecked+" name='checkFinish1' value='"+LJobContent.get(i).getJobContent_seq()+"' style='width:18px;height:18px;display:inline-block;vertical-align:bottom;margin-left:5px;margin-right:5px'>";
	  	 								if(editMode.equals("1")) {
	  	 									returnStr +=
	  	 									"<div style='display:inline-block;vertical-align:bottom'><input type='text' name='jobContent1' value='"+LJobContent.get(i).getJobContent()+"' style='width:790px;border:0px'></div>";
	  	 								}else {
	  	 									returnStr +=
	  	 									"<div style='display:inline-block;vertical-align:bottom'><font color='blue'>"+LJobContent.get(i).getFinishName()+"</font> <b>"+LJobContent.get(i).getJobContent()+"</b></div>";
	  	 								}
	  	 						returnStr +=		
	  	 						"</div>";
  	 						}			
  	 					}
  	 			    returnStr +=
  			 	    "</div>"+
  			 	"</div>"+
  			 "</div>"+

  	 "</div>"+
  	 "<div class='css-table' style='width:1000px;text-align:left;margin-top:10px'>";
  	 	if(editMode.equals("1")) {		    
  	 		returnStr += 
  	 		"<div class='tr'><div class='td' style='font-weight:bold'><span style='border:1px #dddddd solid;background-color:#FEFFEF'>&nbsp;&nbsp;&#9788; 新 增 事 項&nbsp;&nbsp;</span>&nbsp;<A href='javascript:void(0)' onclick='addAdmJob(2)' title='新增行政工作事項'><img src='/images/edit.png' height='12px' class='expired' /></A></div></div>";
  	 	}else {
  	 		returnStr += 
  	 		"<div class='tr'><div class='td' style='font-weight:bold'><span style='border:1px #dddddd solid;background-color:#FEFFEF'>&nbsp;&nbsp;&#9788; 新 增 事 項&nbsp;&nbsp;</span>&nbsp;</div></div>";  	 		
  	 	}
  	 	
//*****************************2.新增事項**********************************//  	 	
  	 	    returnStr +=
  	 		"<div class='tr'>"+
			 	"<div class='td' style='padding:5px'>"+
			 		"<div id='area2' style='font-size:small;border:1px #dddddd solid;padding:10px;width:990px;height:auto;background-color:white'>";
	 					for(int i=0;i<LJobContent.size();i++) {
  	 						if(LJobContent.get(i).getCategory().equals("2")) {
  	 							if(editMode.equals("0")) {
									if(LJobContent.get(i).getFinishName()!=null && !LJobContent.get(i).getFinishName().isEmpty()) {
										jobChecked = "checked disabled";
										
									}else {
										jobChecked = "";
									}
  	 							}else {
  	 								jobChecked = "disabled";
  	 							}								
								if(editMode.equals("1")) {
								    returnStr +=
									"<div style='padding:2px'><A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>";
								}else {
									returnStr +=
									"<div style='padding:2px'>";
								}
										returnStr +=
										"<select name='worker2' "+selectOption+" style='padding:0px;display:inline-block;height:25px;width:80px;vertical-align:bottom;border:1px #dddddd solid'>";
	  	 									String checked = "";
	  	 									returnStr += "<option value=''></option>";
	  	 									for(int x=0;x<LEmployee.size();x++) {
	  	 										checked="";
	  	 										if(LJobContent.get(i).getWorkerId().equals(LEmployee.get(x).getEmployee_seq())){checked=" selected";}
	  	 										returnStr +=
	  	 										"<option value='"+LEmployee.get(x).getEmployee_seq()+"'"+checked+">"+LEmployee.get(x).getCh_name()+"</option>";		
	  	 									}
	  	 								returnStr +=	
	  	 								"</select>";
	  	 								if(editMode.equals("1")) {
	  	 								      returnStr +=
	  	 								      "<input type='checkbox' "+jobChecked+" name='checkFinish2' value='"+LJobContent.get(i).getJobContent_seq()+"' style='width:18px;height:18px;display:inline-block;vertical-align:bottom;margin-left:5px;margin-right:5px'>"+
	  	 								      "<div style='display:inline-block;vertical-align:bottom'><input type='text' name='jobContent2' value='"+LJobContent.get(i).getJobContent()+"' style='width:790px;border:0px'></div>";
	  	 								}else {
		  	 								  returnStr +=
		  		  	 						  "<input type='checkbox' "+jobChecked+" name='checkFinish2' value='"+LJobContent.get(i).getJobContent_seq()+"' style='width:18px;height:18px;display:inline-block;vertical-align:bottom;margin-left:5px;margin-right:5px'>"+
		  		  	 						  "<div style='display:inline-block;vertical-align:bottom'><font color='blue'>"+LJobContent.get(i).getFinishName()+"</font> <b>"+LJobContent.get(i).getJobContent()+"</b></div>";
	  	 								}
	  	 								  
	  	 						returnStr +=
	  	 						"</div>";
  	 						}			
  	 					}
  	 			    returnStr +=			 	
			 	    "</div>"+
			 	"</div>"+
			 "</div>"+
     "</div>"+
  	 "<div class='css-table' style='width:1000px;text-align:left;margin-top:10px'>"+
		     "<div class='tr'><div class='td' style='font-weight:bold'><span style='border:1px #dddddd solid;background-color:#FEFFEF'>&nbsp;&nbsp;&#9788; 輪 值&nbsp;&nbsp;</span></div></div>"+
			 "<div class='tr'>"+
			 	"<div class='td' style='width:950px'>"+
			 		"<div class='css-table' style='border:1px #dddddd solid;margin:5px;font-weight:bold;background-color:white'>"+
		 				"<div class='tr'>"+
	 						"<div class='td2' style='width:80px;height:30px'>&nbsp;</div>"+
	 						"<div class='td2' style='width:150px;text-align:center;vertical-align:middle'>早</div>"+
	 						"<div class='td2' style='width:150px;text-align:center;vertical-align:middle'>午</div>"+
	 						"<div class='td2' style='width:150px;text-align:center;vertical-align:middle'>晚</div>"+
	 					"</div>"+			 	
			 			"<div class='tr'>"+
			 				"<div class='td2' style='text-align:right;vertical-align:middle'>一桌</div>"+
			 				"<div class='td2' style='' align='center'><select id='a11' "+selectOption+" name='a11' style='margin:3px;width:100px' class='form-control'>"+AdmEmployee+"</select></div>"+
			 				"<div class='td2' style='' align='center'><select id='a12' "+selectOption+" name='a12' style='margin:3px;width:100px' class='form-control'>"+AdmEmployee+"</select></div>"+
			 				"<div class='td2' style='' align='center'><select id='a13' "+selectOption+" name='a13' style='margin:3px;width:100px' class='form-control'>"+AdmEmployee+"</select></div>"+
			 		    "</div>"+
			 			"<div class='tr'>"+
		 				    "<div class='td2' style='text-align:right;vertical-align:middle'>值日生</div>"+
			 				"<div class='td2' style='' align='center'><select id='a21' "+selectOption+" name='a21' style='margin:3px;width:100px' class='form-control'>"+AdmEmployee+"</select></div>"+
			 				"<div class='td2' style='' align='center'><select id='a22' "+selectOption+" name='a22' style='margin:3px;width:100px' class='form-control'>"+AdmEmployee+"</select></div>"+
			 				"<div class='td2' style='' align='center'><select id='a23' "+selectOption+" name='a23' style='margin:3px;width:100px' class='form-control'>"+AdmEmployee+"</select></div>"+
		 				"</div>"+	
			 			"<div class='tr'>"+
		 				    "<div class='td2' style='text-align:right;vertical-align:middle'>作業</div>"+
			 				"<div class='td2' style='' align='center'><select id='a31' "+selectOption+" name='a31' style='margin:3px;width:100px;text-align:center' class='form-control'>"+AdmEmployee+"</select></div>"+
			 				"<div class='td2' style='' align='center'><select id='a32' "+selectOption+" name='a32' style='margin:3px;width:100px;text-align:center' class='form-control'>"+AdmEmployee+"</select></div>"+
			 				"<div class='td2' style='' align='center'><select id='a33' "+selectOption+" name='a33' style='margin:3px;width:100px;text-align:center' class='form-control'>"+AdmEmployee+"</select></div>"+
	 				    "</div>"+
			 			"<div class='tr'>"+
	 				    	"<div class='td2' style='text-align:right;vertical-align:middle'>SET教室</div>"+
	 				    	"<div class='td2' style='' align='center'><select id='a41' "+selectOption+" name='a41' style='margin:3px;width:100px;text-align:center' class='form-control'>"+AdmEmployee+"</select></div>"+
	 				    	"<div class='td2' style='' align='center'><select id='a42' "+selectOption+" name='a42' style='margin:3px;width:100px;text-align:center' class='form-control'>"+AdmEmployee+"</select></div>"+
	 				    	"<div class='td2' style='' align='center'><select id='a43' "+selectOption+" name='a43' style='margin:3px;width:100px;text-align:center' class='form-control'>"+AdmEmployee+"</select></div>"+
		 				"</div>"+
			 			"<div class='tr'>"+
	 				    	"<div class='td2' style='text-align:right;vertical-align:middle'>檢查</div>"+
	 				    	"<div class='td2' style='' align='center'><select id='a51' "+selectOption+" name='a51' style='margin:3px;width:100px;text-align:center' class='form-control'>"+AdmEmployee+"</select></div>"+
	 				    	"<div class='td2' style='' align='center'><select id='a52' "+selectOption+" name='a52' style='margin:3px;width:100px;text-align:center' class='form-control'>"+AdmEmployee+"</select></div>"+
	 				    	"<div class='td2' style='' align='center'><select id='a53' "+selectOption+" name='a53' style='margin:3px;width:100px;text-align:center' class='form-control'>"+AdmEmployee+"</select></div>"+
	 				    "</div>"+	
			 			"<div class='tr'>"+
	 				    	"<div class='td2' style='text-align:right;vertical-align:middle'>點名</div>"+
	 				    	"<div class='td2' style='' align='center'><select id='a61' "+selectOption+" name='a61' style='margin:3px;width:100px;text-align:center' class='form-control'>"+AdmEmployee+"</select></div>"+
	 				    	"<div class='td2' style='' align='center'><select id='a62' "+selectOption+" name='a62' style='margin:3px;width:100px;text-align:center' class='form-control'>"+AdmEmployee+"</select></div>"+
	 				    	"<div class='td2' style='' align='center'><select id='a63' "+selectOption+" name='a63' style='margin:3px;width:100px;text-align:center' class='form-control'>"+AdmEmployee+"</select></div>"+
	 				    "</div>"+
	 				   "<div class='tr' style='height:10px'></div>"+	
			 	    "</div>"+
			 	"</div>"+
			 "</div>"+  	 
     "</div>";

  	 			    
//****早****//  	 			    
returnStr +=
"<div style='text-align:left'>";
		returnStr +=
	 	"<div style='margin-top:5px;font-size:large;background-color:#7ba6b6;color:white;text-align:center;font-weight:bold'>早</div>";
	
  	    for(int i=0;i<Lclasses_slot1.size();i++) {
  	    	    int gradeNo = salesService.getRegisterGradeNo(Lclasses_slot1.get(i).getGrade_id());
  	    	    String textareaName = Lclasses_slot1.get(i).getGrade_id()+Lclasses_slot1.get(i).getClass_th();
	  	    	returnStr +=
	  	    	"<input type='hidden' name='grade_id' value='"+Lclasses_slot1.get(i).getGrade_id()+"'>"+
	  	    	"<input type='hidden' name='class_th' value='"+Lclasses_slot1.get(i).getClass_th()+"'>"+
		        "<div style='width:990px;font-size:large;text-align:center;font-weight:bold;padding:5px;border:1px #dddddd dotted;background-color:#e4edf0;color:darkblue'>"+
		        	"<span style='font-size:small;color:black'>"+Lclasses_slot1.get(i).getSchool_name()+"&nbsp;&nbsp;&nbsp;&nbsp;</span>"+"&#10096; "+Lclasses_slot1.get(i).getClassRoom()+" &#10097;&nbsp;&nbsp;&nbsp;&nbsp;"+Lclasses_slot1.get(i).getTime_from()+"~"+Lclasses_slot1.get(i).getTime_to()+"&nbsp;&nbsp;"+"&#10100; <a href='javascript:void(0)'  onclick='courseEdit("+Lclasses_slot1.get(i).getGrade_id()+","+Lclasses_slot1.get(i).getClass_th()+")' style='text-decoration:underline;color:blue'>"+Lclasses_slot1.get(i).getClass_start_date()+"&nbsp;"+Lclasses_slot1.get(i).getSubject_name()+(Lclasses_slot1.get(i).getGradeName()==null?"":Lclasses_slot1.get(i).getGradeName())+Lclasses_slot1.get(i).getClass_name()+"_"+Lclasses_slot1.get(i).getClass_th()+" </A>&#10101;&nbsp;&nbsp;"+Lclasses_slot1.get(i).getTeacher_name()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+gradeNo+"人"+    
		        "</div>"+		
				"<div class='tr'>"+
					"<div class='td2' style='width:180px;border:1px #dddddd dotted;text-align:center;vertical-align:middle;background-color:white;font-weight:bold'>講 義";
				    	if(editMode.equals("1")) {
				    		returnStr += "&nbsp;&nbsp;<A href='javascript:void(0)' onclick=editClassJob('"+Lclasses_slot1.get(i).getGrade_id()+"','"+Lclasses_slot1.get(i).getClass_th()+"','b1','"+textareaName+"b1') title='新增[講義]事項'><img src='/images/edit.png' height='12px' class='expired' /></A>";
				    	}
				    returnStr +=	
				    "</div>"+
					"<div class='td2' style='width:200px;border:1px #dddddd dotted;text-align:center;vertical-align:middle;background-color:white;font-weight:bold'>前 置 作 業";
				    	if(editMode.equals("1")) {
				    		returnStr += "&nbsp;&nbsp;<A href='javascript:void(0)' onclick=editClassJob('"+Lclasses_slot1.get(i).getGrade_id()+"','"+Lclasses_slot1.get(i).getClass_th()+"','b2','"+textareaName+"b2') title='新增[前置作業]事項'><img src='/images/edit.png' height='12px' class='expired' /></A>";
				    	}
				    returnStr +=	
				    "</div>"+				    
					"<div class='td2' style='width:285px;border:1px #dddddd dotted;text-align:center;vertical-align:middle;background-color:white;font-weight:bold'>SET 教 室";
				    	if(editMode.equals("1")) {
				    		returnStr += "&nbsp;&nbsp;<A href='javascript:void(0)' onclick=editClassJob('"+Lclasses_slot1.get(i).getGrade_id()+"','"+Lclasses_slot1.get(i).getClass_th()+"','b3','"+textareaName+"b3') title='新增[SET教室]事項'><img src='/images/edit.png' height='12px' class='expired' /></A>";
				    	}
				    returnStr +=	
				    "</div>"+				    
					"<div class='td2' style='width:325px;border:1px #dddddd dotted;text-align:center;vertical-align:middle;background-color:white;font-weight:bold'>點 名";
				    	if(editMode.equals("1")) {
				    		returnStr += "&nbsp;&nbsp;<A href='javascript:void(0)' onclick=editClassJob('"+Lclasses_slot1.get(i).getGrade_id()+"','"+Lclasses_slot1.get(i).getClass_th()+"','b4','"+textareaName+"b4') title='新增[點名]事項'><img src='/images/edit.png' height='12px' class='expired' /></A>";
				    	}
				    returnStr +=	
				    "</div>"+				    
				"</div>"+
				"<div class='tr'>";
					returnStr +=
					"<div class='td2' id='"+Lclasses_slot1.get(i).getGrade_id()+Lclasses_slot1.get(i).getClass_th()+"b1' style='background-color:white;font-size:small;border:1px #dddddd solid;height:180px'>";
				        for(int a=0;a<LAdmGradeJob_slot1.size();a++) {
				        	if(LAdmGradeJob_slot1.get(a).getGrade_id().equals(Lclasses_slot1.get(i).getGrade_id()) && !LAdmGradeJob_slot1.get(a).getB1_jobContent().isEmpty()) {
					        	returnStr+="<div>";
					        	if(editMode.equals("1")) {
					        		returnStr+="<A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>";
					        		returnStr+="<textarea name='"+LAdmGradeJob_slot1.get(a).getGrade_id()+LAdmGradeJob_slot1.get(a).getClass_th()+"b1' style='border:1px #eeeeee solid;padding:0px;overflow:hidden;height:20px;width:100%'>";
					        		returnStr+= LAdmGradeJob_slot1.get(a).getB1_jobContent();
					        		returnStr+="</textarea>";	
					        	}else {	
									if(LAdmGradeJob_slot1.get(a).getFinishName()!=null && !LAdmGradeJob_slot1.get(a).getFinishName().isEmpty()) {
										jobChecked = "checked disabled";						
									}else {jobChecked = "";}					        		
					        		returnStr+=
				 						"<input type='checkbox' "+jobChecked+" name='checkjob' value='"+LAdmGradeJob_slot1.get(a).getAdmGradeJob_seq()+"' style='width:13px;height:13px;display:inline-block;vertical-align:bottom;margin-left:1px;margin-right:1px'>"+
				 						"<div style='display:inline-block;vertical-align:bottom'><font color='blue'>"+LAdmGradeJob_slot1.get(a).getFinishName()+"</font>"+LAdmGradeJob_slot1.get(a).getB1_jobContent()+"</div>";				        		
					        	}	
					        	returnStr+="</div>";
				        	}
				        }					
						returnStr+= 
					"</div>"+
					"<div class='td2' id='"+Lclasses_slot1.get(i).getGrade_id()+Lclasses_slot1.get(i).getClass_th()+"b2' style='background-color:white;font-size:small;border:1px #dddddd solid;height:180px'>";
				        for(int a=0;a<LAdmGradeJob_slot1.size();a++) {
				        	if(LAdmGradeJob_slot1.get(a).getGrade_id().equals(Lclasses_slot1.get(i).getGrade_id()) && !LAdmGradeJob_slot1.get(a).getB2_jobContent().isEmpty()) {
					        	returnStr+="<div>";
					        	if(editMode.equals("1")) {
					        		returnStr+="<A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>";
					        		returnStr+="<textarea name='"+LAdmGradeJob_slot1.get(a).getGrade_id()+LAdmGradeJob_slot1.get(a).getClass_th()+"b2' style='border:1px #eeeeee solid;padding:0px;overflow:hidden;height:20px;width:100%'>";
				        			returnStr+= LAdmGradeJob_slot1.get(a).getB2_jobContent();
				        		    returnStr+="</textarea>";
					        	}else {
											if(LAdmGradeJob_slot1.get(a).getFinishName()!=null && !LAdmGradeJob_slot1.get(a).getFinishName().isEmpty()) {
												jobChecked = "checked disabled";						
											}else {jobChecked = "";}					        		
							        		returnStr+=
						 						"<input type='checkbox' "+jobChecked+" name='checkjob' value='"+LAdmGradeJob_slot1.get(a).getAdmGradeJob_seq()+"' style='width:13px;height:13px;display:inline-block;vertical-align:bottom;margin-left:1px;margin-right:1px'>"+
						 						"<div style='display:inline-block;vertical-align:bottom'><font color='blue'>"+LAdmGradeJob_slot1.get(a).getFinishName()+"</font>"+LAdmGradeJob_slot1.get(a).getB2_jobContent()+"</div>";			        		
					        	}	
					        	returnStr+="</div>";
				        	}	
				        }						
						returnStr+=
				    "</div>"+
				    "<div class='td2' id='"+Lclasses_slot1.get(i).getGrade_id()+Lclasses_slot1.get(i).getClass_th()+"b3' style='background-color:white;font-size:small;border:1px #dddddd solid;height:180px'>";
				        for(int a=0;a<LAdmGradeJob_slot1.size();a++) {
				        	if(LAdmGradeJob_slot1.get(a).getGrade_id().equals(Lclasses_slot1.get(i).getGrade_id()) && !LAdmGradeJob_slot1.get(a).getB3_jobContent().isEmpty()) {
					        	returnStr+="<div>";
					        	if(editMode.equals("1")) {
					        		returnStr+="<A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>";
					        		returnStr+="<textarea name='"+LAdmGradeJob_slot1.get(a).getGrade_id()+LAdmGradeJob_slot1.get(a).getClass_th()+"b3' style='border:1px #eeeeee solid;padding:0px;overflow:hidden;height:20px;width:100%'>";
				        			returnStr+= LAdmGradeJob_slot1.get(a).getB3_jobContent();
				        		    returnStr+="</textarea>";
					        	}else {
									if(LAdmGradeJob_slot1.get(a).getFinishName()!=null && !LAdmGradeJob_slot1.get(a).getFinishName().isEmpty()) {
										jobChecked = "checked disabled";						
									}else {jobChecked = "";}					        		
					        		returnStr+=
				 						"<input type='checkbox' "+jobChecked+" name='checkjob' value='"+LAdmGradeJob_slot1.get(a).getAdmGradeJob_seq()+"' style='width:13px;height:13px;display:inline-block;vertical-align:bottom;margin-left:1px;margin-right:1px'>"+
				 						"<div style='display:inline-block;vertical-align:bottom'><font color='blue'>"+LAdmGradeJob_slot1.get(a).getFinishName()+"</font>"+LAdmGradeJob_slot1.get(a).getB3_jobContent()+"</div>";				        		
					        	}	
					        	returnStr+="</div>";
				        	}	
				        }						
					    returnStr+= 
			        "</div>"+
			        "<div class='td2' id='"+Lclasses_slot1.get(i).getGrade_id()+Lclasses_slot1.get(i).getClass_th()+"b4' style='background-color:white;font-size:small;border:1px #dddddd solid;height:180px'>";
				        for(int a=0;a<LAdmGradeJob_slot1.size();a++) {
				        	if(LAdmGradeJob_slot1.get(a).getGrade_id().equals(Lclasses_slot1.get(i).getGrade_id()) && !LAdmGradeJob_slot1.get(a).getB4_jobContent().isEmpty()) {
					        	returnStr+="<div>";
					        	if(editMode.equals("1")) {
					        		returnStr+="<A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>";
					        		returnStr+="<textarea name='"+LAdmGradeJob_slot1.get(a).getGrade_id()+LAdmGradeJob_slot1.get(a).getClass_th()+"b4' style='border:1px #eeeeee solid;padding:0px;overflow:hidden;height:20px;width:100%'>";
				        			returnStr+= LAdmGradeJob_slot1.get(a).getB4_jobContent();
				        		    returnStr+="</textarea>";
					        	}else {
									if(LAdmGradeJob_slot1.get(a).getFinishName()!=null && !LAdmGradeJob_slot1.get(a).getFinishName().isEmpty()) {
										jobChecked = "checked disabled";						
									}else {jobChecked = "";}					        		
					        		returnStr+=
				 						"<input type='checkbox' "+jobChecked+" name='checkjob' value='"+LAdmGradeJob_slot1.get(a).getAdmGradeJob_seq()+"' style='width:13px;height:13px;display:inline-block;vertical-align:bottom;margin-left:1px;margin-right:1px'>"+
				 						"<div style='display:inline-block;vertical-align:bottom'><font color='blue'>"+LAdmGradeJob_slot1.get(a).getFinishName()+"</font>"+LAdmGradeJob_slot1.get(a).getB4_jobContent()+"</div>";			        		
					        	}	
					        	returnStr+="</div>";
				        	}	
				        }	    
				        returnStr+= 
		            "</div>"+							
			    "</div>";
  	    } 
returnStr +=
 "</div>";



//****午****///
returnStr +=
"<div style='text-align:left'>";
  	    returnStr +=
		"<div style='margin-top:10px;font-size:large;background-color:#7ba6b6;color:white;text-align:center;font-weight:bold'>午</div>";  
		for(int i=0;i<Lclasses_slot2.size();i++) {
			  int gradeNo = salesService.getRegisterGradeNo(Lclasses_slot2.get(i).getGrade_id());
			  String textareaName = Lclasses_slot2.get(i).getGrade_id()+Lclasses_slot2.get(i).getClass_th();
		      returnStr +=
		      "<input type='hidden' name='grade_id' value='"+Lclasses_slot2.get(i).getGrade_id()+"'>"+
			  "<input type='hidden' name='class_th' value='"+Lclasses_slot2.get(i).getClass_th()+"'>"+		      
		      "<div style='width:990px;font-size:large;text-align:center;font-weight:bold;padding:5px;border:1px #dddddd dotted;background-color:#e4edf0;color:darkblue'>"+
		      		"<span style='font-size:small;color:black'>"+Lclasses_slot2.get(i).getSchool_name()+"&nbsp;&nbsp;&nbsp;&nbsp;</span>"+"&#10096; "+Lclasses_slot2.get(i).getClassRoom()+" &#10097;&nbsp;&nbsp;&nbsp;&nbsp;"+Lclasses_slot2.get(i).getTime_from()+"~"+Lclasses_slot2.get(i).getTime_to()+"&nbsp;&nbsp;"+"&#10100; <a href='javascript:void(0)'  onclick='courseEdit("+Lclasses_slot2.get(i).getGrade_id()+","+Lclasses_slot2.get(i).getClass_th()+")' style='text-decoration:underline;color:blue'>"+Lclasses_slot2.get(i).getClass_start_date()+"&nbsp;"+Lclasses_slot2.get(i).getSubject_name()+(Lclasses_slot2.get(i).getGradeName()==null?"":Lclasses_slot2.get(i).getGradeName())+Lclasses_slot2.get(i).getClass_name()+"_"+Lclasses_slot2.get(i).getClass_th()+"</A> &#10101;&nbsp;&nbsp;&nbsp;&nbsp;"+Lclasses_slot2.get(i).getTeacher_name()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+gradeNo+"人"+  
		      "</div>"+		
				"<div class='tr'>"+
					"<div class='td2' style='width:180px;border:1px #dddddd dotted;text-align:center;vertical-align:middle;background-color:white;font-weight:bold'>講 義";
				    	if(editMode.equals("1")) {
				    		returnStr += "&nbsp;&nbsp;<A href='javascript:void(0)' onclick=editClassJob('"+Lclasses_slot2.get(i).getGrade_id()+"','"+Lclasses_slot2.get(i).getClass_th()+"','b1','"+textareaName+"b1') title='新增[講義]事項'><img src='/images/edit.png' height='12px' class='expired' /></A>";
				    	}
				    returnStr +=	
				    "</div>"+
					"<div class='td2' style='width:200px;border:1px #dddddd dotted;text-align:center;vertical-align:middle;background-color:white;font-weight:bold'>前 置 作 業";
				    	if(editMode.equals("1")) {
				    		returnStr += "&nbsp;&nbsp;<A href='javascript:void(0)' onclick=editClassJob('"+Lclasses_slot2.get(i).getGrade_id()+"','"+Lclasses_slot2.get(i).getClass_th()+"','b2','"+textareaName+"b2') title='新增[前置作業]事項'><img src='/images/edit.png' height='12px' class='expired' /></A>";
				    	}
				    returnStr +=	
				    "</div>"+				    
					"<div class='td2' style='width:285px;border:1px #dddddd dotted;text-align:center;vertical-align:middle;background-color:white;font-weight:bold'>SET 教 室";
				    	if(editMode.equals("1")) {
				    		returnStr += "&nbsp;&nbsp;<A href='javascript:void(0)' onclick=editClassJob('"+Lclasses_slot2.get(i).getGrade_id()+"','"+Lclasses_slot2.get(i).getClass_th()+"','b3','"+textareaName+"b3') title='新增[SET教室]事項'><img src='/images/edit.png' height='12px' class='expired' /></A>";
				    	}
				    returnStr +=	
				    "</div>"+				    
					"<div class='td2' style='width:325px;border:1px #dddddd dotted;text-align:center;vertical-align:middle;background-color:white;font-weight:bold'>點 名";
				    	if(editMode.equals("1")) {
				    		returnStr += "&nbsp;&nbsp;<A href='javascript:void(0)' onclick=editClassJob('"+Lclasses_slot2.get(i).getGrade_id()+"','"+Lclasses_slot2.get(i).getClass_th()+"','b4','"+textareaName+"b4') title='新增[點名]事項'><img src='/images/edit.png' height='12px' class='expired' /></A>";
				    	}
			    returnStr +=	
			    "</div>"+
			  "</div>"+
				"<div class='tr'>";
				returnStr +=
				"<div class='td2' id='"+Lclasses_slot2.get(i).getGrade_id()+Lclasses_slot2.get(i).getClass_th()+"b1' style='background-color:white;font-size:small;border:1px #dddddd solid;height:180px'>";
				        for(int a=0;a<LAdmGradeJob_slot2.size();a++) {
				        	if(LAdmGradeJob_slot2.get(a).getGrade_id().equals(Lclasses_slot2.get(i).getGrade_id()) && !LAdmGradeJob_slot2.get(a).getB1_jobContent().isEmpty()) {
					        	returnStr+="<div>";
					        	if(editMode.equals("1")) {
					        		returnStr+="<A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>";
					        		returnStr+="<textarea name='"+LAdmGradeJob_slot2.get(a).getGrade_id()+LAdmGradeJob_slot2.get(a).getClass_th()+"b1' style='border:1px #eeeeee solid;padding:0px;overflow:hidden;height:20px;width:100%'>";
				        			returnStr+= LAdmGradeJob_slot2.get(a).getB1_jobContent();
				        		    returnStr+="</textarea>";
					        	}else {
									if(LAdmGradeJob_slot2.get(a).getFinishName()!=null && !LAdmGradeJob_slot2.get(a).getFinishName().isEmpty()) {
										jobChecked = "checked disabled";						
									}else {jobChecked = "";}					        		
					        		returnStr+=
				 						"<input type='checkbox' "+jobChecked+" name='checkjob' value='"+LAdmGradeJob_slot2.get(a).getAdmGradeJob_seq()+"' style='width:13px;height:13px;display:inline-block;vertical-align:bottom;margin-left:1px;margin-right:1px'>"+
				 						"<div style='display:inline-block;vertical-align:bottom'><font color='blue'>"+LAdmGradeJob_slot2.get(a).getFinishName()+"</font>"+LAdmGradeJob_slot2.get(a).getB1_jobContent()+"</div>";			        		
					        	}	
					        	returnStr+="</div>";
				        	}	
				        }
				    returnStr+=    
				"</div>"+
				"<div class='td2' id='"+Lclasses_slot2.get(i).getGrade_id()+Lclasses_slot2.get(i).getClass_th()+"b2' style='background-color:white;font-size:small;border:1px #dddddd solid;height:180px'>";
				        for(int a=0;a<LAdmGradeJob_slot2.size();a++) {
				        	if(LAdmGradeJob_slot2.get(a).getGrade_id().equals(Lclasses_slot2.get(i).getGrade_id()) && !LAdmGradeJob_slot2.get(a).getB2_jobContent().isEmpty()) {
					        	returnStr+="<div>";
					        	if(editMode.equals("1")) {
					        		returnStr+="<A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>";
					        		returnStr+="<textarea name='"+LAdmGradeJob_slot2.get(a).getGrade_id()+LAdmGradeJob_slot2.get(a).getClass_th()+"b2' style='border:1px #eeeeee solid;padding:0px;overflow:hidden;height:20px;width:100%'>";
				        			returnStr+= LAdmGradeJob_slot2.get(a).getB2_jobContent();
				        		    returnStr+="</textarea>";
					        	}else {
									if(LAdmGradeJob_slot2.get(a).getFinishName()!=null && !LAdmGradeJob_slot2.get(a).getFinishName().isEmpty()) {
										jobChecked = "checked disabled";						
									}else {jobChecked = "";}					        		
					        		returnStr+=
				 						"<input type='checkbox' "+jobChecked+" name='checkjob' value='"+LAdmGradeJob_slot2.get(a).getAdmGradeJob_seq()+"' style='width:13px;height:13px;display:inline-block;vertical-align:bottom;margin-left:1px;margin-right:1px'>"+
				 						"<div style='display:inline-block;vertical-align:bottom'><font color='blue'>"+LAdmGradeJob_slot2.get(a).getFinishName()+"</font>"+LAdmGradeJob_slot2.get(a).getB2_jobContent()+"</div>";			        		
					        	}	
					        	returnStr+="</div>";
				        	}	
				        }
					returnStr+= 
			    "</div>"+
			    "<div class='td2' id='"+Lclasses_slot2.get(i).getGrade_id()+Lclasses_slot2.get(i).getClass_th()+"b3' style='background-color:white;font-size:small;border:1px #dddddd solid;height:180px'>";
			        for(int a=0;a<LAdmGradeJob_slot2.size();a++) {
			        	if(LAdmGradeJob_slot2.get(a).getGrade_id().equals(Lclasses_slot2.get(i).getGrade_id()) && !LAdmGradeJob_slot2.get(a).getB3_jobContent().isEmpty()) {
				        	returnStr+="<div>";
				        	if(editMode.equals("1")) {
				        		returnStr+="<A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>";
				        		returnStr+="<textarea name='"+LAdmGradeJob_slot2.get(a).getGrade_id()+LAdmGradeJob_slot2.get(a).getClass_th()+"b3' style='border:1px #eeeeee solid;padding:0px;overflow:hidden;height:20px;width:100%'>";
			        			returnStr+= LAdmGradeJob_slot2.get(a).getB3_jobContent();
			        		    returnStr+="</textarea>";
				        	}else {
								if(LAdmGradeJob_slot2.get(a).getFinishName()!=null && !LAdmGradeJob_slot2.get(a).getFinishName().isEmpty()) {
									jobChecked = "checked disabled";						
								}else {jobChecked = "";}					        		
				        		returnStr+=
			 						"<input type='checkbox' "+jobChecked+" name='checkjob' value='"+LAdmGradeJob_slot2.get(a).getAdmGradeJob_seq()+"' style='width:13px;height:13px;display:inline-block;vertical-align:bottom;margin-left:1px;margin-right:1px'>"+
			 						"<div style='display:inline-block;vertical-align:bottom'><font color='blue'>"+LAdmGradeJob_slot2.get(a).getFinishName()+"</font>"+LAdmGradeJob_slot2.get(a).getB3_jobContent()+"</div>";			        		
				        	}	
				        	returnStr+="</div>";
			        	}	
			        }					
				    returnStr+= 
		        "</div>"+
		        "<div class='td2' id='"+Lclasses_slot2.get(i).getGrade_id()+Lclasses_slot2.get(i).getClass_th()+"b4' style='background-color:white;font-size:small;border:1px #dddddd solid;height:180px'>";
			        for(int a=0;a<LAdmGradeJob_slot2.size();a++) {
			        	if(LAdmGradeJob_slot2.get(a).getGrade_id().equals(Lclasses_slot2.get(i).getGrade_id()) && !LAdmGradeJob_slot2.get(a).getB4_jobContent().isEmpty()) {
				        	returnStr+="<div>";
				        	if(editMode.equals("1")) {
				        		returnStr+="<A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>";
				        		returnStr+="<textarea name='"+LAdmGradeJob_slot2.get(a).getGrade_id()+LAdmGradeJob_slot2.get(a).getClass_th()+"b4' style='border:1px #eeeeee solid;padding:0px;overflow:hidden;height:20px;width:100%'>";
			        			returnStr+= LAdmGradeJob_slot2.get(a).getB4_jobContent();
			        		    returnStr+="</textarea>";
				        	}else {
								if(LAdmGradeJob_slot2.get(a).getFinishName()!=null && !LAdmGradeJob_slot2.get(a).getFinishName().isEmpty()) {
									jobChecked = "checked disabled";						
								}else {jobChecked = "";}					        		
				        		returnStr+=
			 						"<input type='checkbox' "+jobChecked+" name='checkjob' value='"+LAdmGradeJob_slot2.get(a).getAdmGradeJob_seq()+"' style='width:13px;height:13px;display:inline-block;vertical-align:bottom;margin-left:1px;margin-right:1px'>"+
			 						"<div style='display:inline-block;vertical-align:bottom'><font color='blue'>"+LAdmGradeJob_slot2.get(a).getFinishName()+"</font>"+LAdmGradeJob_slot2.get(a).getB4_jobContent()+"</div>";			        		
				        	}	
				        	returnStr+="</div>";
			        	}	
			        }				    
			        returnStr+=
	            "</div>"+							
		    "</div>";
		}
returnStr +=
"</div>";


//****晚****//
returnStr +=
"<div style='text-align:left'>";
        returnStr +=
        "<div style='margin-top:10px;font-size:large;background-color:#7ba6b6;color:white;text-align:center;font-weight:bold'>晚</div>";

			for(int i=0;i<Lclasses_slot3.size();i++) {
				int gradeNo = salesService.getRegisterGradeNo(Lclasses_slot3.get(i).getGrade_id());
				String textareaName = Lclasses_slot3.get(i).getGrade_id()+Lclasses_slot3.get(i).getClass_th();
			    returnStr +=
			    "<input type='hidden' name='grade_id' value='"+Lclasses_slot3.get(i).getGrade_id()+"'>"+
				"<input type='hidden' name='class_th' value='"+Lclasses_slot3.get(i).getClass_th()+"'>"+				    
			    "<div style='width:990px;font-size:large;text-align:center;font-weight:bold;padding:5px;border:1px #dddddd dotted;background-color:#e4edf0;color:darkblue'>"+
			    	"<span style='font-size:small;color:black'>"+Lclasses_slot3.get(i).getSchool_name()+"&nbsp;&nbsp;&nbsp;&nbsp;</span>"+"&#10096; "+Lclasses_slot3.get(i).getClassRoom()+" &#10097;&nbsp;&nbsp;&nbsp;&nbsp;"+Lclasses_slot3.get(i).getTime_from()+"~"+Lclasses_slot3.get(i).getTime_to()+"&nbsp;&nbsp;"+"&#10100; <a href='javascript:void(0)'  onclick='courseEdit("+Lclasses_slot3.get(i).getGrade_id()+","+Lclasses_slot3.get(i).getClass_th()+")' style='text-decoration:underline;color:blue'>"+Lclasses_slot3.get(i).getClass_start_date()+"&nbsp;"+Lclasses_slot3.get(i).getSubject_name()+(Lclasses_slot3.get(i).getGradeName()==null?"":Lclasses_slot3.get(i).getGradeName())+Lclasses_slot3.get(i).getClass_name()+"_"+Lclasses_slot3.get(i).getClass_th()+"</A> &#10101;&nbsp;&nbsp;&nbsp;&nbsp;"+Lclasses_slot3.get(i).getTeacher_name()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+gradeNo+"人"+  
			    "</div>"+		
				  "<div class='tr'>"+
						"<div class='td2' style='width:180px;border:1px #dddddd dotted;text-align:center;vertical-align:middle;background-color:white;font-weight:bold'>講 義";
					    	if(editMode.equals("1")) {
					    		returnStr += "&nbsp;&nbsp;<A href='javascript:void(0)' onclick=editClassJob('"+Lclasses_slot3.get(i).getGrade_id()+"','"+Lclasses_slot3.get(i).getClass_th()+"','b1','"+textareaName+"b1') title='新增[講義]事項'><img src='/images/edit.png' height='12px' class='expired' /></A>";
					    	}
					    returnStr +=	
					    "</div>"+
						"<div class='td2' style='width:200px;border:1px #dddddd dotted;text-align:center;vertical-align:middle;background-color:white;font-weight:bold'>前 置 作 業";
					    	if(editMode.equals("1")) {
					    		returnStr += "&nbsp;&nbsp;<A href='javascript:void(0)' onclick=editClassJob('"+Lclasses_slot3.get(i).getGrade_id()+"','"+Lclasses_slot3.get(i).getClass_th()+"','b2','"+textareaName+"b2') title='新增[前置作業]事項'><img src='/images/edit.png' height='12px' class='expired' /></A>";
					    	}
					    returnStr +=	
					    "</div>"+				    
						"<div class='td2' style='width:285px;border:1px #dddddd dotted;text-align:center;vertical-align:middle;background-color:white;font-weight:bold'>SET 教 室";
					    	if(editMode.equals("1")) {
					    		returnStr += "&nbsp;&nbsp;<A href='javascript:void(0)' onclick=editClassJob('"+Lclasses_slot3.get(i).getGrade_id()+"','"+Lclasses_slot3.get(i).getClass_th()+"','b3','"+textareaName+"b3') title='新增[SET教室]事項'><img src='/images/edit.png' height='12px' class='expired' /></A>";
					    	}
					    returnStr +=	
					    "</div>"+				    
						"<div class='td2' style='width:325px;border:1px #dddddd dotted;text-align:center;vertical-align:middle;background-color:white;font-weight:bold'>點 名";
					    	if(editMode.equals("1")) {
					    		returnStr += "&nbsp;&nbsp;<A href='javascript:void(0)' onclick=editClassJob('"+Lclasses_slot3.get(i).getGrade_id()+"','"+Lclasses_slot3.get(i).getClass_th()+"','b4','"+textareaName+"b4') title='新增[點名]事項'><img src='/images/edit.png' height='12px' class='expired' /></A>";
					    	}
					    returnStr +=	
					    "</div>"+
			    "</div>"+
				"<div class='tr'>";
						returnStr +=
						"<div class='td2' id='"+Lclasses_slot3.get(i).getGrade_id()+Lclasses_slot3.get(i).getClass_th()+"b1' style='background-color:white;font-size:small;border:1px #dddddd solid;height:180px'>";
				        for(int a=0;a<LAdmGradeJob_slot3.size();a++) {
				        	if(LAdmGradeJob_slot3.get(a).getGrade_id().equals(Lclasses_slot3.get(i).getGrade_id()) && !LAdmGradeJob_slot3.get(a).getB1_jobContent().isEmpty()) {
					        	returnStr+="<div>";
					        	if(editMode.equals("1")) {
					        		returnStr+="<A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>";
					        		returnStr+="<textarea name='"+LAdmGradeJob_slot3.get(a).getGrade_id()+LAdmGradeJob_slot3.get(a).getClass_th()+"b1' style='border:1px #eeeeee solid;padding:0px;overflow:hidden;height:20px;width:100%'>";
				        			returnStr+= LAdmGradeJob_slot3.get(a).getB1_jobContent();
				        		    returnStr+="</textarea>";
					        	}else {
									if(LAdmGradeJob_slot3.get(a).getFinishName()!=null && !LAdmGradeJob_slot3.get(a).getFinishName().isEmpty()) {
										jobChecked = "checked disabled";						
									}else {jobChecked = "";}					        		
					        		returnStr+=
				 						"<input type='checkbox' "+jobChecked+" name='checkjob' value='"+LAdmGradeJob_slot3.get(a).getAdmGradeJob_seq()+"' style='width:13px;height:13px;display:inline-block;vertical-align:bottom;margin-left:1px;margin-right:1px'>"+
				 						"<div style='display:inline-block;vertical-align:bottom'><font color='blue'>"+LAdmGradeJob_slot3.get(a).getFinishName()+"</font>"+LAdmGradeJob_slot3.get(a).getB1_jobContent()+"</div>";			        		
					        	}	
					        	returnStr+="</div>";
				        	}	
				        }					
							returnStr+= 
						"</div>"+
						"<div class='td2' id='"+Lclasses_slot3.get(i).getGrade_id()+Lclasses_slot3.get(i).getClass_th()+"b2' style='background-color:white;font-size:small;border:1px #dddddd solid;height:180px'>";
					        for(int a=0;a<LAdmGradeJob_slot3.size();a++) {
					        	if(LAdmGradeJob_slot3.get(a).getGrade_id().equals(Lclasses_slot3.get(i).getGrade_id()) && !LAdmGradeJob_slot3.get(a).getB2_jobContent().isEmpty()) {
						        	returnStr+="<div>";
						        	if(editMode.equals("1")) {
						        		returnStr+="<A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>";
						        		returnStr+="<textarea name='"+LAdmGradeJob_slot3.get(a).getGrade_id()+LAdmGradeJob_slot3.get(a).getClass_th()+"b2' style='border:1px #eeeeee solid;padding:0px;overflow:hidden;height:20px;width:100%'>";
					        			returnStr+= LAdmGradeJob_slot3.get(a).getB2_jobContent();
					        		    returnStr+="</textarea>";
						        	}else {
										if(LAdmGradeJob_slot3.get(a).getFinishName()!=null && !LAdmGradeJob_slot3.get(a).getFinishName().isEmpty()) {
											jobChecked = "checked disabled";						
										}else {jobChecked = "";}					        		
						        		returnStr+=
					 						"<input type='checkbox' "+jobChecked+" name='checkjob' value='"+LAdmGradeJob_slot3.get(a).getAdmGradeJob_seq()+"' style='width:13px;height:13px;display:inline-block;vertical-align:bottom;margin-left:1px;margin-right:1px'>"+
					 						"<div style='display:inline-block;vertical-align:bottom'><font color='blue'>"+LAdmGradeJob_slot3.get(a).getFinishName()+"</font>"+LAdmGradeJob_slot3.get(a).getB2_jobContent()+"</div>";				        		
						        	}	
						        	returnStr+="</div>";
					        	}	
					        }							
					        returnStr+= 
					    "</div>"+
					    "<div class='td2' id='"+Lclasses_slot3.get(i).getGrade_id()+Lclasses_slot3.get(i).getClass_th()+"b3' style='background-color:white;font-size:small;border:1px #dddddd solid;height:180px'>";
					        for(int a=0;a<LAdmGradeJob_slot3.size();a++) {
					        	if(LAdmGradeJob_slot3.get(a).getGrade_id().equals(Lclasses_slot3.get(i).getGrade_id()) && !LAdmGradeJob_slot3.get(a).getB3_jobContent().isEmpty()) {
						        	returnStr+="<div>";
						        	if(editMode.equals("1")) {
						        		returnStr+="<A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>";
						        		returnStr+="<textarea name='"+LAdmGradeJob_slot3.get(a).getGrade_id()+LAdmGradeJob_slot3.get(a).getClass_th()+"b3' style='border:1px #eeeeee solid;padding:0px;overflow:hidden;height:20px;width:100%'>";
					        			returnStr+= LAdmGradeJob_slot3.get(a).getB3_jobContent();
					        		    returnStr+="</textarea>";
						        	}else {
										if(LAdmGradeJob_slot3.get(a).getFinishName()!=null && !LAdmGradeJob_slot3.get(a).getFinishName().isEmpty()) {
											jobChecked = "checked disabled";						
										}else {jobChecked = "";}					        		
						        		returnStr+=
					 						"<input type='checkbox' "+jobChecked+" name='checkjob' value='"+LAdmGradeJob_slot3.get(a).getAdmGradeJob_seq()+"' style='width:13px;height:13px;display:inline-block;vertical-align:bottom;margin-left:1px;margin-right:1px'>"+
					 						"<div style='display:inline-block;vertical-align:bottom'><font color='blue'>"+LAdmGradeJob_slot3.get(a).getFinishName()+"</font>"+LAdmGradeJob_slot3.get(a).getB3_jobContent()+"</div>";			        		
						        	}	
						        	returnStr+="</div>";
					        	}	
					        }	        
						    returnStr+=
				        "</div>"+
				        "<div class='td2' id='"+Lclasses_slot3.get(i).getGrade_id()+Lclasses_slot3.get(i).getClass_th()+"b4' style='background-color:white;font-size:small;border:1px #dddddd solid;height:180px'>";
					        for(int a=0;a<LAdmGradeJob_slot3.size();a++) {
					        	if(LAdmGradeJob_slot3.get(a).getGrade_id().equals(Lclasses_slot3.get(i).getGrade_id()) && !LAdmGradeJob_slot3.get(a).getB4_jobContent().isEmpty()) {
						        	returnStr+="<div>";
						        	if(editMode.equals("1")) {
						        		returnStr+="<A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>";
						        		returnStr+="<textarea name='"+LAdmGradeJob_slot3.get(a).getGrade_id()+LAdmGradeJob_slot3.get(a).getClass_th()+"b4' style='border:1px #eeeeee solid;padding:0px;overflow:hidden;height:20px;width:100%'>";
					        			returnStr+= LAdmGradeJob_slot3.get(a).getB4_jobContent();
					        		    returnStr+="</textarea>";
						        	}else {
										if(LAdmGradeJob_slot3.get(a).getFinishName()!=null && !LAdmGradeJob_slot3.get(a).getFinishName().isEmpty()) {
											jobChecked = "checked disabled";						
										}else {jobChecked = "";}					        		
						        		returnStr+=
					 						"<input type='checkbox' "+jobChecked+" name='checkjob' value='"+LAdmGradeJob_slot3.get(a).getAdmGradeJob_seq()+"' style='width:13px;height:13px;display:inline-block;vertical-align:bottom;margin-left:1px;margin-right:1px'>"+
					 						"<div style='display:inline-block;vertical-align:bottom'><font color='blue'>"+LAdmGradeJob_slot3.get(a).getFinishName()+"</font>"+LAdmGradeJob_slot3.get(a).getB4_jobContent()+"</div>";				        		
						        	}	
						        	returnStr+="</div>";
					        	}	
					        }						    
				        returnStr+= 
				        "</div>"+		
	            "</div>";							
			}
returnStr +=
"</div>";			
//**********//
 	 
      return returnStr;
   }
   
   
   @RequestMapping("/Adm/getDate")
   @ResponseBody
   public String getDate(Model model,HttpServletRequest request,Principal principal) {
	  	 String NowDay  = request.getParameter("NowDay");
	  	 String DayAdd  = request.getParameter("DayAdd");	
	  	 String weekName = "";
	  	 Calendar cal = Calendar.getInstance();
	  	 
	  	 try {
	  		 DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			 Date date = dateFormat.parse(NowDay);		  	 
		  	 cal.setTime(date);
		     cal.add(Calendar.DATE,Integer.valueOf(DayAdd));
			 switch(cal.get(Calendar.DAY_OF_WEEK)){
				case 1:weekName="星期日";break;
				case 2:weekName="星期一";break;
				case 3:weekName="星期二";break;
				case 4:weekName="星期三";break;
				case 5:weekName="星期四";break;
				case 6:weekName="星期五";break;
				case 7:weekName="星期六";break;
		     }		     		     
		}catch(ParseException e) {
			 e.printStackTrace();
		}
	  	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	  	return sdf.format(cal.getTime())+weekName;	  	 
   }
   
   @RequestMapping("/Adm/addAdmJob")
   public String addAdmJob(Model model,HttpServletRequest request,Principal principal) {
	    String school_code = request.getParameter("school_code"); 
	    List<Employee> LEmployee = accountService.getEmployee("","",school_code,"","","1","","");
	 	String AdmEmployee = "<option value=''></option>";
	 	for(int i=0;i<LEmployee.size();i++) {
	 		AdmEmployee += "<option value='"+LEmployee.get(i).getEmployee_seq()+"'>"+LEmployee.get(i).getCh_name()+"</option>";
	 	}
	 	model.addAttribute("AdmEmployee",AdmEmployee); 
	 	 
	    String category = request.getParameter("category");
	    if(category.equals("1")) {
	    	model.addAttribute("category","新增行政例行工作");
			String school_code_self = "";
			String school_name_self = "";
			if(principal!=null && principal.getName()!=null) {
				Employee employee= accountService.getEmployee("","","","","","",principal.getName(),"").get(0);
				school_code_self = employee.getSchool();
				school_name_self = employee.getSchoolName();
			}
			model.addAttribute("school_name_self",school_name_self);
		    List<AdmRegularJob> LadmRegularJob = admService.AdmRegularJob(school_code_self);		    
		    model.addAttribute("LadmRegularJob",LadmRegularJob);
		    return "/Adm/addAdmJob1";
		    
	    }else if(category.equals("2")) {
	    	model.addAttribute("category","新增行政事項"); 
	    	model.addAttribute("jobContent","<textarea id='jobContent2' style='font-size:small;border:1px #dddddd solid;width:700px;height:180px;background-color:white'  placeholder='<<< 每個工作事項請在文字尾端以斷行(enter鍵)來區分 >>>'></textarea>");
	    	return "/Adm/addAdmJob2";
	    	
	    }else{	    	
	    	model.addAttribute("textareaName",request.getParameter("textareaName"));
	    	model.addAttribute("id",request.getParameter("id"));
	    	model.addAttribute("category","新增課程事項"); 
	    	model.addAttribute("jobContent","<textarea id='jobContent2' style='font-size:small;border:1px #dddddd solid;width:400px;height:100px;background-color:white'  placeholder='<<< 每個工作事項請在文字尾端以斷行(enter鍵)來區分 >>>'></textarea>");
	    	return "/Adm/editClassJob";
	    }
        
   }
 
   @RequestMapping("/Adm/admRegularJob")
   public String admCalendarSet(Model model,HttpServletRequest request,Principal principal) {
		String saveMessage = request.getParameter("saveMessage");
		model.addAttribute("saveMessage", saveMessage);
		
		String school_code_self = "";
		String school_name_self = "";
		if(principal!=null && principal.getName()!=null) {	
			Employee employee= accountService.getEmployee("","","","","","",principal.getName(),"").get(0);
			school_code_self = employee.getSchool();
			school_name_self = employee.getSchoolName();
		}	   
	    List<AdmRegularJob> LadmRegularJob = admService.AdmRegularJob(school_code_self);
	    model.addAttribute("LadmRegularJob",LadmRegularJob);
	    model.addAttribute("school_name_self",school_name_self);
	    model.addAttribute("school_code_self",school_code_self);
        return "/Adm/admRegularJob";
   } 

   @RequestMapping("/Adm/admCalendarUpdate") 
   public String admCalendarUpdate(HttpServletRequest request,Principal principal){
	       String editMode = request.getParameter("editMode");   
	   	   String yearMD = request.getParameter("yearMD");
	   	   String school_code = request.getParameter("school_code");
	   	   String editor = principal.getName();
	   	   
	   	   if(editMode.equals("1")){
			   String jobContent1[] = request.getParameterValues("jobContent1");
			   String jobContent2[] = request.getParameterValues("jobContent2");
			   String worker1[] = request.getParameterValues("worker1");
			   String worker2[] = request.getParameterValues("worker2");

			   ArrayList<String> LjobCode = new ArrayList<String>();
			   ArrayList<String> LworkerId = new ArrayList<String>();
			   LjobCode.add("a11"); LworkerId.add(request.getParameter("a11"));
			   LjobCode.add("a12"); LworkerId.add(request.getParameter("a12"));
			   LjobCode.add("a13"); LworkerId.add(request.getParameter("a13"));
			   LjobCode.add("a21"); LworkerId.add(request.getParameter("a21"));
			   LjobCode.add("a22"); LworkerId.add(request.getParameter("a22"));
			   LjobCode.add("a23"); LworkerId.add(request.getParameter("a23"));
			   LjobCode.add("a31"); LworkerId.add(request.getParameter("a31"));
			   LjobCode.add("a32"); LworkerId.add(request.getParameter("a32"));
			   LjobCode.add("a33"); LworkerId.add(request.getParameter("a33"));
			   LjobCode.add("a41"); LworkerId.add(request.getParameter("a41"));
			   LjobCode.add("a42"); LworkerId.add(request.getParameter("a42"));
			   LjobCode.add("a43"); LworkerId.add(request.getParameter("a43"));
			   LjobCode.add("a51"); LworkerId.add(request.getParameter("a51"));
			   LjobCode.add("a52"); LworkerId.add(request.getParameter("a52"));
			   LjobCode.add("a53"); LworkerId.add(request.getParameter("a53"));
			   LjobCode.add("a61"); LworkerId.add(request.getParameter("a61"));
			   LjobCode.add("a62"); LworkerId.add(request.getParameter("a62"));
			   LjobCode.add("a63"); LworkerId.add(request.getParameter("a63"));
			   
			   //講義,前置作業,SET教室,點名
			   String grade_id[] = request.getParameterValues("grade_id");
			   String class_th[] = request.getParameterValues("class_th");

			   
			   List<AdmGradeJob> LAdmGradeJob = new ArrayList<AdmGradeJob>();
			   if(grade_id!=null) {
				   for(int i=0;i<grade_id.length;i++) {
					   String b1[] = request.getParameterValues(grade_id[i]+class_th[i]+"b1");
					   String b2[] = request.getParameterValues(grade_id[i]+class_th[i]+"b2");
					   String b3[] = request.getParameterValues(grade_id[i]+class_th[i]+"b3");
					   String b4[] = request.getParameterValues(grade_id[i]+class_th[i]+"b4");
					   if(b1!=null) {
						   for(int x=0;x<b1.length;x++) {
							   LAdmGradeJob.add(new AdmGradeJob("",grade_id[i],class_th[i],b1[x],"","","",principal.getName(),"",""));
						   }
					   }
					   if(b2!=null) {
						   for(int x=0;x<b2.length;x++) {
							   LAdmGradeJob.add(new AdmGradeJob("",grade_id[i],class_th[i],"",b2[x],"","",principal.getName(),"",""));
						   }
					   }
					   if(b3!=null) {
						   for(int x=0;x<b3.length;x++) {
							   LAdmGradeJob.add(new AdmGradeJob("",grade_id[i],class_th[i],"","",b3[x],"",principal.getName(),"",""));
						   }
					   }
					   if(b4!=null) {
						   for(int x=0;x<b4.length;x++) {
							   LAdmGradeJob.add(new AdmGradeJob("",grade_id[i],class_th[i],"","","",b4[x],principal.getName(),"",""));
						   }
					   }	   
				   }			   
			   }	   
			   admService.admJobUpdateEdit(yearMD,school_code,editor,jobContent1,jobContent2,worker1,worker2,LjobCode,LworkerId,LAdmGradeJob,grade_id,class_th);
			   return "redirect:/Adm/admCalendarEdit?noSession=0&saveMessage=儲存成功!";
		   
		   //完成工作	   
	   	   }else {
			   String checkFinish1[] = request.getParameterValues("checkFinish1");
			   String checkFinish2[] = request.getParameterValues("checkFinish2");			   
			   admService.admJobUpdate(editor,checkFinish1,checkFinish2);
			   
			   String checkjob[] = request.getParameterValues("checkjob");
			   admService.admGradeJobUpdate(editor,checkjob);
			   return "redirect:/Adm/admCalendar?saveMessage=儲存成功!";
	   	   }
   }    
   
   
   @RequestMapping("/Adm/GetJobContentCode")
   @ResponseBody
   public String GetJobContentCode(Model model,HttpServletRequest request,Principal principal) {
	    String school_code = request.getParameter("school_code");
	    String yearMD = request.getParameter("yearMD");
	    List<JobContentCode> LJobContentCode = admService.GetJobContentCode(school_code,yearMD);
	    String returnStr = "";
	    for(int i=0;i<LJobContentCode.size();i++) {
	    	returnStr += LJobContentCode.get(i).getWorkerId()+"@";
	    }
	    return returnStr;
   }
   
   @RequestMapping("/Adm/admRegularJobUpdate")
   public String admRegularJobUpdate(HttpServletRequest request,Principal principal){
   	   String[] jobContent = request.getParameterValues("jobContent");
   	   String school_code = request.getParameter("school_code");
   	   admService.admRegularJobUpdate(school_code,jobContent,principal.getName());
	   return "redirect:/Adm/admRegularJob?saveMessage=Save successfully!";
   }
      
   @RequestMapping("/Adm/coursePreviousAttend")
   public String coursePreviousAttend(HttpServletRequest request,Model model){
		String grade_id = request.getParameter("grade_id");
		String school_code = request.getParameter("school_code");
	   	Grade grade = courseService.getGrade(grade_id,"","",school_code,"","","","","","","","","").get(0);
	   	List<Grade> LGrade = courseService.getGrade("","",grade.getSubject_id(),school_code,"","",grade.getTeacher_id(),"","","","","","");
	   	for(int i=0;i<LGrade.size();i++) {
	   		int gradeNo = salesService.getRegisterGradeNo(LGrade.get(i).getGrade_seq());
	   		LGrade.get(i).setGradeNo(String.valueOf(gradeNo));	
	   	}
	   	
	   	String returnStr = 
	   	"<div class='css-table' style='width:400px;text-align:center'>"+
	   		 "<div class='tr'><div class='th' style='width:100px;border:1px #ffffff solid;background-color:#FFFFCC'>分校</div><div class='th' style='width:200px;border:1px #ffffff solid;background-color:#FFFFCC'>期別</div><div class='th'  style='width:100px;border:1px #ffffff solid;background-color:#FFFFCC'>正班人數</div></div>";
	   	for(int i=0;i<LGrade.size();i++) {
	   		returnStr+= 
	   		"<div class='tr'><div class='td2' style='border-bottom:1px #eeeeee solid'>"+LGrade.get(i).getSchool_name()+"</div><div class='td2' style='border-bottom:1px #eeeeee solid'>"+LGrade.get(i).getClass_start_date()+LGrade.get(i).getSubject_name()+" "+LGrade.get(i).getGradeName()+"</div><div class='td2' style='font-weight:bold;border-bottom:1px #eeeeee solid'>"+LGrade.get(i).getGradeNo()+"</div></div>"; 		
	   	}
	   	
	   	returnStr+= 
	   	"</div>";
	   	model.addAttribute("returnStr",returnStr);
   	    return "/Adm/coursePreviousAttend";
   }
   
	@RequestMapping("/Adm/addClassRoomBook")
	public String addClassRoomBook(Model model,HttpServletRequest request){
		String bookDate = request.getParameter("bookDate");
		model.addAttribute("bookDate",bookDate);
		String bookClassRoom = request.getParameter("bookClassRoom");
		model.addAttribute("bookClassRoom",bookClassRoom);
		String school_code = request.getParameter("school_code");
		model.addAttribute("school_code",school_code);
		String school_name = accountService.getSchool(school_code,"").get(0).getName();
		model.addAttribute("school_name",school_name);
		String slot = request.getParameter("slot");
		model.addAttribute("slot",slot);
		if(slot.equals("1")) {
			model.addAttribute("slot_name","早");
		}else if(slot.equals("2")) {
			model.addAttribute("slot_name","午");
		}else if(slot.equals("3")) {
			model.addAttribute("slot_name","晚");
		}
		return "/Adm/addClassRoomBook";
	} 
	
	@RequestMapping("/Adm/ClassRoomBookUpdate")
	public String ClassRoomBookUpdate(Model model,HttpServletRequest request,Principal principal){
		admService.ClassRoomBookUpdate(request.getParameter("school_code"),request.getParameter("bookDate"),request.getParameter("slot"),request.getParameter("bookClassRoom"),request.getParameter("bookContent"),principal.getName());
		return "/common/closeAndReload";
	} 

    @RequestMapping("/Adm/MockBase")
    public String MockBase(Model model) {
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup",schoolGroup); 
			
    	return "/Adm/MockBase";
    }
    
    @RequestMapping("/Adm/getMockMonth")
    @ResponseBody
    public String getMockMonth(HttpServletRequest request) {
    	String mockBaseTitle_seq = request.getParameter("mockBaseTitle_seq");
    	String student_seq = request.getParameter("student_seq");
    	String school_code = request.getParameter("school_code");
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMdd");
		
    	String returnStr="";
    	String roundName = "";
    	String slotName = "";
    	String slotName2 = "";
    	for(int i=0;i<3;i++) {
    		if(i!=0) {c.add(Calendar.MONTH,1);}   		   
			returnStr+= 
			"<div style='width:840px;text-align:left;font-weight:bold;padding:3px;letter-spacing:2px'>[ "+sdf.format(c.getTime())+" ]</div>";
			returnStr+=
			"<div class='css-table'>"+
					"<div class='tr' style='background-color:#77739C;font-weight:bold;color:white'>"+
					    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周一</div>"+
					    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周二</div>"+
					    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周三</div>"+
					    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周四</div>"+
					    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周五</div>"+
					    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周六</div>"+
					    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周日</div>"+		    		    
					"</div>";
			
			int monthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH); //該月總日數
			//取得該校每日模考預定資料,如該月共30日
			for(int x=1;x<=monthDay;x++) {
				c.set(Calendar.DAY_OF_MONTH,x);
				int rank=-1;
				switch(c.get(Calendar.DAY_OF_WEEK)){
					case 1:rank=7;break;
					case 2:rank=1;break;
					case 3:rank=2;break;
					case 4:rank=3;break;
					case 5:rank=4;break;
					case 6:rank=5;break;
					case 7:rank=6;break;
				}	
				//該行頭空格
				if(x==1 && rank!=1) {
					returnStr+= 
					"<div class='tr' style='background-color:#ffffff'>";
					for(int y=1;y<rank;y++) {
						returnStr+= 
						"<div class='td2' style='text-align:center;border:1px #eeeeee solid'>&nbsp;</div>";
					}
				}
				
				if(rank==1) {
					returnStr+= 
					"<div class='tr' style='background-color:#ffffff'>";
				}								
/*****************************內容***************************/
				   List<MockBase> LMockBase = admService.getMockBase(mockBaseTitle_seq,sdf3.format(c.getTime()),"","");
				        roundName = "";
				        slotName = "";
				        slotName2 = "";
				        if(LMockBase.size()>0) {
					        if(LMockBase.get(0).getRound()!=null && !LMockBase.get(0).getRound().isEmpty()) {
					        	roundName = "<b>第 "+LMockBase.get(0).getRound()+" 回</b>";
					        }					        
					        String[] slotNameArr = LMockBase.get(0).getSlot().split("#");
					        for(int a=0;a<slotNameArr.length;a++) {
					        	if(!slotNameArr[a].isEmpty()) {
					        		if(student_seq!=null && !student_seq.isEmpty()) {
						        		if(slotNameArr[a].equals("1")) {
						        			slotName += "<span style='padding:2px;background-color:#FAFDDD;border:1px #aaaaaa solid' onclick=MockBookDate('"+LMockBase.get(0).getSetDate()+"','1','"+LMockBase.get(0).getRound()+"','"+LMockBase.get(0).getMockBase_seq()+"','')><a style='color:blue' onMouseOver=this.style.cursor='pointer'>早</a></span>&nbsp;&nbsp;";
						        		}else if(slotNameArr[a].equals("2")) {
						        			slotName += "<span style='padding:2px;background-color:#FAFDDD;border:1px #aaaaaa solid' onclick=MockBookDate('"+LMockBase.get(0).getSetDate()+"','2','"+LMockBase.get(0).getRound()+"','"+LMockBase.get(0).getMockBase_seq()+"','')><a style='color:blue' onMouseOver=this.style.cursor='pointer'>午</a></span>&nbsp;&nbsp;";
						        		}else if(slotNameArr[a].equals("3")) {
						        			slotName += "<span style='padding:2px;background-color:#FAFDDD;border:1px #aaaaaa solid' onclick=MockBookDate('"+LMockBase.get(0).getSetDate()+"','3','"+LMockBase.get(0).getRound()+"','"+LMockBase.get(0).getMockBase_seq()+"','')><a style='color:blue' onMouseOver=this.style.cursor='pointer'>晚</a></span>";
						        		}
					        		}else {
						        		if(slotNameArr[a].equals("1")) {
						        			slotName2 += "<span style='padding:2px;background-color:#FAFDDD;border:1px #aaaaaa solid'>早</span>&nbsp;&nbsp;";
						        		}else if(slotNameArr[a].equals("2")) {
						        			slotName2 += "<span style='padding:2px;background-color:#FAFDDD;border:1px #aaaaaa solid'>午</span>&nbsp;&nbsp;";
						        		}else if(slotNameArr[a].equals("3")) {
						        			slotName2 += "<span style='padding:2px;background-color:#FAFDDD;border:1px #aaaaaa solid'>晚</span>";
						        		}					        			
					        		}
					        	}	
					        }
				        }
                        //學生選時段
				        if(student_seq!=null && !student_seq.isEmpty()) {
							returnStr+= 
							"<div class='td2' style='text-align:center;border:1px #eeeeee solid;padding:3px'>"+
									"<div style='text-align:left;font-size:xx-small;letter-spacing:0px'><span style='color:#999999'>"+sdf2.format(c.getTime())+"</span>&nbsp;&nbsp;"+roundName+"</div>"+
									"<div style='text-align:center;font-size:small;letter-spacing:0px;font-weight:bold'>";
									if(!slotName.isEmpty()) {
										returnStr+= slotName;
									}
									returnStr+="</div>";

							//限制人數
					        if(LMockBase.size()>0) {
						        if(LMockBase.get(0).getWitLimit()!=null && LMockBase.get(0).getWitLimit().equals("1")) {
						    		List<TestSubjectSelection2> LTestSubjectSelection = admService.getTestSubjectSelection2(LMockBase.get(0).getMockBaseTitle_seq(),"","");
						    		for(int a=0;a<LTestSubjectSelection.size();a++) {
						    			List<MockLimit> LMockLimit = admService.getMockLimit(LTestSubjectSelection.get(a).getTestSubjectSelection2_seq(),"");
						    			//人數額滿
						    			for(int b=0;b<LMockLimit.size();b++) {
						    				List<MockBaseBook2> LMockBaseBook2 = salesService.getMockBaseBook("","1","",sdf3.format(c.getTime()),school_code,"",LMockLimit.get(b).getMockLimit_seq(),"","","");
							    			if(LMockBaseBook2!=null && String.valueOf(LMockBaseBook2.size()).equals(LMockLimit.get(b).getNoLimit())) {
							    				returnStr += "<div style='font-weight:normal;letter-spacing:0px;font-size:small;margin:2px;color:#aaaaaa'><span style='background-color:#FFFFFF;border:1px #aaaaaa solid'>"+LMockLimit.get(b).getFromx()+"~"+LMockLimit.get(b).getTox()+"</span>&nbsp;:&nbsp;0人</div>";
							    			}else {	    			
							    				returnStr += "<div style='font-weight:normal;letter-spacing:0px;font-size:small;margin:2px'><span style='background-color:#FAFDDD;border:1px #aaaaaa solid' onclick=MockBookDate('"+LMockBase.get(0).getSetDate()+"','"+LMockLimit.get(b).getSlot()+"','"+LMockBase.get(0).getRound()+"','"+LMockBase.get(0).getMockBase_seq()+"','"+LMockLimit.get(b).getMockLimit_seq()+"')><a style='color:blue' onMouseOver=this.style.cursor='pointer'>"+LMockLimit.get(b).getFromx()+"~"+LMockLimit.get(b).getTox()+"</a></span>&nbsp;:&nbsp;"+LMockLimit.get(b).getNoLimit()+"人</div>";
							    			}
						    			}	
						    		}						        	
						        }
				            }
					        returnStr+=
							"</div>";
						//行政設置	
				        }else {
							returnStr+= 
							"<div class='td2' style='text-align:center;border:1px #eeeeee solid;padding:3px'>"+
									"<div style='text-align:left;font-size:xx-small;letter-spacing:0px'><A href='javascript:javascript:void(0)' style='text-decoration:underline;color:blue' onclick='openMockBase("+sdf3.format(c.getTime())+")'>"+sdf2.format(c.getTime())+"</A>&nbsp;&nbsp;"+roundName+"</div>"+
									"<div style='text-align:center;font-size:small;letter-spacing:0px;font-weight:bold'>"+
										slotName2+
									"</div>";
							
							//限制人數
					        if(LMockBase.size()>0) {
						        if(LMockBase.get(0).getWitLimit()!=null && LMockBase.get(0).getWitLimit().equals("1")) {
						    		List<TestSubjectSelection2> LTestSubjectSelection = admService.getTestSubjectSelection2(LMockBase.get(0).getMockBaseTitle_seq(),"","");
						    		for(int a=0;a<LTestSubjectSelection.size();a++) {
						    			List<MockLimit> LMockLimit = admService.getMockLimit(LTestSubjectSelection.get(a).getTestSubjectSelection2_seq(),"");
						    			for(int b=0;b<LMockLimit.size();b++) {
						    				returnStr += "<div style='font-weight:normal;letter-spacing:0px;font-size:small'>&bull;"+LMockLimit.get(b).getFromx()+"~"+LMockLimit.get(b).getTox()+"&nbsp;:&nbsp;"+LMockLimit.get(b).getNoLimit()+"人</div>";
						    			}	
						    		}						        	
						        }
				            }
					        returnStr+= 
							"</div>";
				        }    
/********************************************************/
				if(rank==7) {
					returnStr+= "</div>";
				}

				//該行尾空格
				if(x==monthDay && rank!=7) {
					for(int y=1;y<=7-rank;y++) {
						returnStr+= 
						"<div class='td2' style='text-align:center;border:1px #eeeeee solid'>&nbsp;</div>";
					}
					returnStr+= "</div>";
				}									
			}			
			returnStr+=
			"</div><div>&nbsp;</div>";
    	}    	
    	return returnStr;
    }
    
    @RequestMapping("/Adm/getCounselingMonth")
    @ResponseBody
    public String getCounselingMonth(HttpServletRequest request) {
    	
    	String counselingBaseTitle_seq = request.getParameter("counselingBaseTitle_seq");
    	String student_seq = request.getParameter("student_seq");
    	String school_code = request.getParameter("school_code");
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMdd");
		
    	String returnStr="";
    	String roundName = "";
    	String slotName = "";
    	for(int i=0;i<3;i++) {
    		if(i!=0) {c.add(Calendar.MONTH,1);}   		   
			returnStr+= 
			"<div style='width:840px;text-align:left;font-weight:bold;padding:3px;letter-spacing:2px'>[ "+sdf.format(c.getTime())+" ]</div>";
			returnStr+=
			"<div class='css-table'>"+
					"<div class='tr' style='background-color:#77739C;font-weight:bold;color:white'>"+
					    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周一</div>"+
					    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周二</div>"+
					    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周三</div>"+
					    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周四</div>"+
					    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周五</div>"+
					    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周六</div>"+
					    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周日</div>"+		    		    
					"</div>";
			
			int monthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH); //該月總日數
			//取得該校每日模考預定資料,如該月共30日
			for(int x=1;x<=monthDay;x++) {
				c.set(Calendar.DAY_OF_MONTH,x);
				int rank=-1;
				switch(c.get(Calendar.DAY_OF_WEEK)){
					case 1:rank=7;break;
					case 2:rank=1;break;
					case 3:rank=2;break;
					case 4:rank=3;break;
					case 5:rank=4;break;
					case 6:rank=5;break;
					case 7:rank=6;break;
				}	
				//該行頭空格
				if(x==1 && rank!=1) {
					returnStr+= 
					"<div class='tr' style='background-color:#ffffff'>";
					for(int y=1;y<rank;y++) {
						returnStr+= 
						"<div class='td2' style='text-align:center;border:1px #eeeeee solid'>&nbsp;</div>";
					}
				}
				
				if(rank==1) {
					returnStr+= 
					"<div class='tr' style='background-color:#ffffff'>";
				}								
/*****************************內容***************************/
				List<CounselingBase> LCounselingBase = admService.getCounselingBase(counselingBaseTitle_seq,sdf3.format(c.getTime()),"");				
				        roundName = "";
				        slotName = "";
                        //學生選時段
				        if(student_seq!=null && !student_seq.isEmpty()) {
							returnStr+= 
							"<div class='td2' style='text-align:center;border:1px #eeeeee solid;padding:3px'>"+
									"<div style='text-align:left;font-size:xx-small;letter-spacing:0px'><span style='color:#999999'>"+sdf2.format(c.getTime())+"</span>&nbsp;&nbsp;"+roundName+"</div>"+
									"<div style='text-align:center;font-size:small;letter-spacing:0px;font-weight:bold'>";
									if(!slotName.isEmpty()) {
										returnStr+= slotName;
									}
									returnStr+="</div>";

							//限制人數
							        if(LCounselingBase.size()>0) {
							        	String slotStr = LCounselingBase.get(0).getSlotStr();
							        	String[] slotArr = slotStr.split("#");
							        	for(int a=0;a<slotArr.length;a++) {
							        		List<CounselingLimit1> LCounselingLimit1 = admService.getCounselingLimit1(LCounselingBase.get(0).getCounselingBaseTitle_seq(),slotArr[a],"","");
							        		for(int m=0;m<LCounselingLimit1.size();m++) {
							        			List<CounselingLimit2> LCounselingLimit2 = admService.getCounselingLimit2(LCounselingLimit1.get(m).getCounselingLimit1_seq(),"");
									    		for(int y=0;y<LCounselingLimit2.size();y++) {
											    			List<CounselingBaseBook> LCounselingBaseBook = salesService.getCounselingBaseBook("","1",sdf3.format(c.getTime()),school_code,"",LCounselingLimit2.get(y).getCounselingLimit2_seq(),"","","",""); 
											    			//人數額滿
											    			if(LCounselingBaseBook!=null && String.valueOf(LCounselingBaseBook.size()).equals(LCounselingLimit2.get(y).getNoLimit())) {
											    				returnStr += "<div style='font-weight:normal;letter-spacing:0px;font-size:small;margin:2px;color:#aaaaaa'><span style='background-color:#FFFFFF;border:1px #aaaaaa solid'>"+LCounselingLimit2.get(y).getFrom1()+"~"+LCounselingLimit2.get(y).getTo1()+"</span>&nbsp;:&nbsp;0人</div>";
											    			}else {	    			
											    				returnStr += "<div style='font-weight:normal;letter-spacing:0px;font-size:small;margin:2px'><span style='background-color:#ccffcc;border:1px #aaaaaa solid' onclick=CounselingBookDate('"+LCounselingBase.get(0).getSetDate()+"','','','"+LCounselingBase.get(0).getCounselingBase_seq()+"','"+LCounselingLimit2.get(y).getCounselingLimit2_seq()+"')><a style='color:blue' onMouseOver=this.style.cursor='pointer'>"+LCounselingLimit2.get(y).getFrom1()+"~"+LCounselingLimit2.get(y).getTo1()+"</a></span>&nbsp;:&nbsp;"+LCounselingLimit2.get(y).getNoLimit()+"人</div>";
											    			}
									        	}
							        		}	
								        /**	
								        	List<CounselingLimit2> LCounselingLimit2 = admService.getCounselingLimit2(counselingBaseTitle_seq,"");
								    		for(int a=0;a<LCounselingLimit2.size();a++) {
								    			List<CounselingBaseBook> LCounselingBaseBook = salesService.getCounselingBaseBook("","1",sdf3.format(c.getTime()),school_code,"",LCounselingLimit2.get(a).getCounselingLimit_seq(),"","","",""); 
								    			//人數額滿
								    			if(LCounselingBaseBook!=null && String.valueOf(LCounselingBaseBook.size()).equals(LCounselingLimit2.get(a).getNoLimit())) {
								    				returnStr += "<div style='font-weight:normal;letter-spacing:0px;font-size:small;margin:2px;color:#aaaaaa'><span style='background-color:#FFFFFF;border:1px #aaaaaa solid'>"+LCounselingLimit2.get(a).getFrom1()+"~"+LCounselingLimit2.get(a).getTo1()+"</span>&nbsp;:&nbsp;0人</div>";
								    			}else {	    			
								    				returnStr += "<div style='font-weight:normal;letter-spacing:0px;font-size:small;margin:2px'><span style='background-color:#ccffcc;border:1px #aaaaaa solid' onclick=CounselingBookDate('"+LCounselingBase.get(0).getSetDate()+"','','','"+LCounselingBase.get(0).getCounselingBase_seq()+"','"+LCounselingLimit2.get(a).getCounselingLimit_seq()+"')><a style='color:blue' onMouseOver=this.style.cursor='pointer'>"+LCounselingLimit2.get(a).getFrom1()+"~"+LCounselingLimit2.get(a).getTo1()+"</a></span>&nbsp;:&nbsp;"+LCounselingLimit2.get(a).getNoLimit()+"人</div>";
								    			}	
								    		}
								    	**/							        	
								        }
						            }									
						    								        	
					        returnStr+=
							"</div>";
						//行政設置	
				        }else {
							returnStr+= 
							"<div class='td2' style='text-align:center;border:1px #eeeeee solid;padding:3px'>"+
									"<div style='text-align:left;font-size:xx-small;letter-spacing:0px'><A href='javascript:javascript:void(0)' style='text-decoration:underline;color:blue' onclick='openCounselingBase("+sdf3.format(c.getTime())+")'>"+sdf2.format(c.getTime())+"</A>&nbsp;&nbsp;"+roundName+"</div>";
							//限制人數
					        if(LCounselingBase.size()>0) {
						        	String slotStr = LCounselingBase.get(0).getSlotStr();
						        	String[] slotArr = slotStr.split("#");
						        	for(int a=0;a<slotArr.length;a++) {
						        		List<CounselingLimit1> LCounselingLimit1 = admService.getCounselingLimit1(LCounselingBase.get(0).getCounselingBaseTitle_seq(),slotArr[a],"","");
						        		returnStr += "<div style='font-size:x-small;text-align:left'>"+LCounselingLimit1.get(0).getTeacher_name()+"</div>";
							        	for(int m=0;m<LCounselingLimit1.size();m++) {
								        		List<CounselingLimit2> LCounselingLimit2 = admService.getCounselingLimit2(LCounselingLimit1.get(m).getCounselingLimit1_seq(),"");
									    		for(int y=0;y<LCounselingLimit2.size();y++) {
									    			returnStr += "<div style='font-weight:normal;letter-spacing:0px;font-size:x-small'>&bull;"+LCounselingLimit2.get(y).getFrom1()+"~"+LCounselingLimit2.get(y).getTo1()+"&nbsp;:&nbsp;"+LCounselingLimit2.get(y).getNoLimit()+"人</div>";
									    		}
							        	}
						        	}	
				            }
					        returnStr+= 
							"</div>";
				        }    
/********************************************************/
				if(rank==7) {
					returnStr+= "</div>";
				}

				//該行尾空格
				if(x==monthDay && rank!=7) {
					for(int y=1;y<=7-rank;y++) {
						returnStr+= 
						"<div class='td2' style='text-align:center;border:1px #eeeeee solid'>&nbsp;</div>";
					}
					returnStr+= "</div>";
				}									
			}			
			returnStr+=
			"</div><div>&nbsp;</div>";
    	}    	
    	return returnStr;
    }    
 
    @RequestMapping("/Adm/getCounselingMonth2")
    @ResponseBody
    public String getCounselingMonth2(HttpServletRequest request) {
    	String school_code = request.getParameter("school_code");
    	String category_id = request.getParameter("category_id");
    	List<CounselingBaseTitle> LCounselingBaseTitle = admService.getCounselingBaseTitle(school_code,"",category_id);
    	if(LCounselingBaseTitle.size()>0) {
    		return LCounselingBaseTitle.get(0).getCounselingBaseTitle_seq();
    	}else {
    		return "";
    	}
    }
    @RequestMapping("/Adm/getVideoBase")
    @ResponseBody
    public String getVideoBase(HttpServletRequest request,Model model) {  	
    	String school_code = request.getParameter("school_code");
    	String student_seq = request.getParameter("student_seq");
		List<VideoBase> LVideoBase_First = admService.getVideoBaseFirstDay(school_code);
		String returnStr = "";
		String withBackground = "";
		String tmpStr = "";
		for(int i=0;i<LVideoBase_First.size();i++) { //該月第一天
			returnStr+= "<span style='font-weight:bold;padding:3px'>[ "+LVideoBase_First.get(i).getSetDate().substring(0,4)+"年 "+LVideoBase_First.get(i).getSetDate().substring(5,7)+"月 ]</span>";
			returnStr+=
					"<div class='css-table'>"+
							"<div class='tr' style='background-color:#7ba6b6;font-weight:bold;color:white'>"+
							    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周一</div>"+
							    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周二</div>"+
							    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周三</div>"+
							    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周四</div>"+
							    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px'>周五</div>"+
							    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px;background-color:#3e7e99'>周六</div>"+
							    "<div class='td2' style='text-align:center;padding:0px;border:1px #ffffff solid;width:120px;letter-spacing:15px;background-color:#3e7e99'>周日</div>"+		    		    
							"</div>";
							/*************************************************/
							Calendar c = Calendar.getInstance();
							c.set(Calendar.YEAR,Integer.valueOf(LVideoBase_First.get(i).getSetDate().substring(0,4))); 
							c.set(Calendar.MONTH,Integer.valueOf(LVideoBase_First.get(i).getSetDate().substring(5,7))-1);
							int monthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH); //該月總日數
							
							//取得該校每日模考預定資料,如該月共30日
							for(int x=1;x<=monthDay;x++) {
								String setDate = LVideoBase_First.get(i).getSetDate().substring(0,7)+"-";
								if(x<10) {setDate +="0"+x;}else {setDate +=x;}					
								List<VideoBase> LVideoBase = admService.getVideoBase(setDate,"",school_code,"");
								if(LVideoBase.size()>0) {
									//該行頭空格
									if(x==1 && !LVideoBase.get(0).getWeekDay().equals("1")) {
										returnStr+= "<div class='tr' style='background-color:#ffffff'>";
										for(int y=1;y<Integer.valueOf(LVideoBase.get(0).getWeekDay());y++) {
											returnStr+= "<div class='td2' style='text-align:center;border:1px #ffffff solid'><div style='text-align:left;font-size:xx-small'>&nbsp;</div></div>";
										}
									}
									if(LVideoBase.get(0).getWeekDay().equals("1")) {
										returnStr+= "<div class='tr' style='background-color:#ffffff'>";
									}								
										//該日日期
								    	withBackground = "";
								    	tmpStr = "";									
									    if(student_seq!=null && !student_seq.isEmpty()) {
									    	//學員選video時段
									    	SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
									    	String nowDate = sdFormat.format(new Date());									    	
									    	if(Integer.valueOf(setDate.replaceAll("-",""))>=Integer.valueOf(nowDate)) {
												for(int a=0;a<LVideoBase.size();a++) {
													if(LVideoBase.get(a).getSlotName()!=null && !LVideoBase.get(a).getSlotName().isEmpty()) {
														withBackground = "background-color:#ffffe6";										
														tmpStr+= "<A href='javascript:void(0)' style='text-decoration:underline;color:blue' onclick=BookClass('"+setDate+"','"+LVideoBase.get(a).getSlot()+"');>"+LVideoBase.get(a).getSlotName()+"</A>&nbsp;&nbsp;";
													}	
												}
									        }
									    	returnStr+= "<div class='td2' style='text-align:center;border:1px #ffffff solid; "+withBackground+"'><div style='text-align:left;font-size:xx-small;letter-spacing:0px'>"+setDate.substring(5,7)+"/"+setDate.substring(8,10)+"</div>";
									    }else {
									    	//行政設置video時段
											for(int a=0;a<LVideoBase.size();a++) {
												if(LVideoBase.get(a).getSlotName()!=null && !LVideoBase.get(a).getSlotName().isEmpty()) {									
													tmpStr+= LVideoBase.get(a).getSlotName()+"&nbsp;&nbsp;";
												}	
											}									    	
									    	returnStr+= "<div class='td2' style='text-align:center;border:1px #ffffff solid'><div style='text-align:left;font-size:xx-small;letter-spacing:0px'><A href='javascript:void(0)' style='text-decoration:underline;color:blue' onclick=openDate('"+setDate+"')>"+setDate.substring(5,7)+"/"+setDate.substring(8,10)+"</A></div>";
									    }	
									    //該日早午晚
										returnStr+= "<div style='text-align:center;font-weight:bold'>"+tmpStr+"</div>";
										returnStr+= "</div>";
									//該行尾空格
									if(x==monthDay && !LVideoBase.get(0).getWeekDay().equals("7")) {
										for(int y=1;y<=7-Integer.valueOf(LVideoBase.get(0).getWeekDay());y++) {
											returnStr+= "<div class='td2' style='text-align:center;border:1px #ffffff solid'><div style='text-align:left;font-size:xx-small'>&nbsp;<br>&nbsp;</div></div>";
										}
									}
									if(LVideoBase.get(0).getWeekDay().equals("7")) {
										returnStr+= "</div>";
									}
								}	
							}
							/*************************************************/
			returnStr+=	
					"</div></div><div>&nbsp;</div>";			
		}
		
		returnStr+=	"@";
		List<VideoSlotTitle> LVideoSlotTitle = admService.getVideoSlotTitle(school_code);
		if(LVideoSlotTitle.size()==0) {returnStr +="@@@@@";}
		for(int i=0;i<LVideoSlotTitle.size();i++) {
			returnStr += LVideoSlotTitle.get(i).getSlot_1_from()+" ~ "+LVideoSlotTitle.get(i).getSlot_1_to()+"@";
			returnStr += LVideoSlotTitle.get(i).getSlot_2_from()+" ~ "+LVideoSlotTitle.get(i).getSlot_2_to()+"@";
			returnStr += LVideoSlotTitle.get(i).getSlot_3_from()+" ~ "+LVideoSlotTitle.get(i).getSlot_3_to()+"@";
			returnStr += LVideoSlotTitle.get(i).getSlot_1_from2()+" ~ "+LVideoSlotTitle.get(i).getSlot_1_to2()+"@";
			returnStr += LVideoSlotTitle.get(i).getSlot_2_from2()+" ~ "+LVideoSlotTitle.get(i).getSlot_2_to2()+"@";
			returnStr += LVideoSlotTitle.get(i).getSlot_3_from2()+" ~ "+LVideoSlotTitle.get(i).getSlot_3_to2();			
		} 
		
    	return returnStr;
    }    
    
    
    @RequestMapping("/Adm/getMockBaseTitle")
	@ResponseBody     
    public List<MockBaseTitle> getMockBaseTitle(HttpServletRequest request) {
		String school_code = request.getParameter("school_code");    	
    	List<MockBaseTitle> LMockBaseTitle = admService.getMockBaseTitle(school_code,"","","",""); 
    	return LMockBaseTitle;
    }
    
    @RequestMapping("/Adm/getCounselingBaseTitle")
	@ResponseBody     
    public List<CounselingBaseTitle> getCounselingBaseTitle(HttpServletRequest request) {
		String school_code = request.getParameter("school_code");    	
    	List<CounselingBaseTitle> LCounselingBaseTitle = admService.getCounselingBaseTitle(school_code,"",""); 
    	return LCounselingBaseTitle;
    }    
    
    @RequestMapping("/Adm/getMockBaseTitleStr")
	@ResponseBody     
    public String getMockBaseTitleStr(HttpServletRequest request) {
		String school_code = request.getParameter("school_code");    	
    	List<MockBaseTitle> LMockBaseTitle = admService.getMockBaseTitle(school_code,"","","","");
    	String returnStr = "<option value=''></option>";
    	for(int i=0;i<LMockBaseTitle.size();i++) {
    		returnStr += "<option value="+LMockBaseTitle.get(i).getMockBaseTitle_seq()+">"+LMockBaseTitle.get(i).getCategoryName()+"&nbsp;"+LMockBaseTitle.get(i).getAttendName()+"&nbsp;"+LMockBaseTitle.get(i).getTestWayName()+"</option>";
    	}    		
    	return returnStr;
    }  
    
    @RequestMapping("/Adm/VideoBaseSave")
    public String VideoBaseSave(Model model,HttpServletRequest request,Principal principal) {
    	String school_code = request.getParameter("school_code");
    	String year = request.getParameter("year");
    	String month = request.getParameter("month");
    	String[] A_slot = request.getParameterValues("slot");
    	String[] A_flag = request.getParameterValues("flag");
    	admService.VideoBaseSave(school_code,year,month,A_flag,A_slot,principal.getName());    	
        return "redirect:/Adm/VideoBase";	
    }
    @RequestMapping("/Adm/VideoBase")
    public String VideoBase(Model model,Principal principal,HttpSession session,HttpServletRequest request){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH,1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String year = sdf.format(c.getTime()).substring(0,4);
		String month = sdf.format(c.getTime()).substring(4,6);
		model.addAttribute("year",year);
		model.addAttribute("month",month); 
		
   		String menu = request.getParameter("menu");
   		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}	 
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup",schoolGroup);
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);		
   		return "/Adm/VideoBase";
    } 
    

    @RequestMapping("/Adm/openTitleLink")
    public String openTitleLink(Model model,HttpServletRequest request,Principal principal,HttpSession session){
    	String backURL = request.getRequestURL().append('?').append(request.getQueryString()).toString();
    	session.setAttribute("backURL",backURL);
    	model.addAttribute("backURL",backURL);
    	
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup",schoolGroup);
		String school_code = request.getParameter("school_code");
		String category_id ="";
		String attend = "";
		String testWay = "";
			if(request.getParameter("student_seq")==null) {
				//行政模考設置
				String mockBaseTitle_seq = request.getParameter("mockBaseTitle_seq");
				model.addAttribute("mockBaseTitle_seq",mockBaseTitle_seq); //考科-名稱
				MockBaseTitle mockBaseTitle = admService.getMockBaseTitle("",mockBaseTitle_seq,"","","").get(0);
				model.addAttribute("slot_1",mockBaseTitle.getSlot_1_from()+" ~ "+mockBaseTitle.getSlot_1_to());
				model.addAttribute("slot_2",mockBaseTitle.getSlot_2_from()+" ~ "+mockBaseTitle.getSlot_2_to());
				model.addAttribute("slot_3",mockBaseTitle.getSlot_3_from()+" ~ "+mockBaseTitle.getSlot_3_to());

				school_code = mockBaseTitle.getSchool_code();
			}else {
		   		//學生模考預約				
				String register_mock_seq = request.getParameter("register_mock_seq");
				model.addAttribute("register_mock_seq",register_mock_seq); 		
		   		model.addAttribute("Register_seq",request.getParameter("Register_seq"));
		   		model.addAttribute("student_seq",request.getParameter("student_seq"));
		   		model.addAttribute("mockDetail_seq",request.getParameter("mockDetail_seq"));

		   		
		   		List<MockDetail> LMockDetail= courseService.getMockDetail("",request.getParameter("mockDetail_seq"));
		   		if(LMockDetail.size()>0) {
		   			category_id = LMockDetail.get(0).getCategory_id();//學生報名-->考科-名稱
		   			attend = LMockDetail.get(0).getTestStyle();
		   			testWay = LMockDetail.get(0).getTestMethod();
		   			
		   		}	
		   		
		    	Student student = salesService.getStudent(request.getParameter("student_seq"),"","","","","","","","","").get(0);
		    	
		    	
		    	if(student.getSchool_code()!=null && !student.getSchool_code().isEmpty()) {
		    		school_code = student.getSchool_code();
		    	}else {
		    		school_code = accountService.getAccountByID("",principal.getName()).getSchool_code();
		    	}
			}
					
        //可選行政設置之-->考科-名稱
   		List<MockBaseTitle> LMockBaseTitle;
   		LMockBaseTitle = admService.getMockBaseTitle(school_code,"",category_id,attend,testWay);
   		if(LMockBaseTitle.size()==0) { //選不到時顯示該分校所有-->考科-名稱
   			LMockBaseTitle = admService.getMockBaseTitle(school_code,"","","","");
   		}
   		model.addAttribute("LMockBaseTitle",LMockBaseTitle);
   		model.addAttribute("school_code",school_code);
   		return "/Adm/openTitleLink";
    }
  
    @RequestMapping("/Adm/openCounselingBaseTitle") 
    public String openCounselingBaseTitle(Model model,HttpServletRequest request,Principal principal,HttpSession session){
    	String backURL = request.getRequestURL().append('?').append(request.getQueryString()).toString();
    	session.setAttribute("backURL",backURL);
    	model.addAttribute("backURL",backURL);
    	   	
		String counselingBaseTitle_seq = request.getParameter("counselingBaseTitle_seq");
		String category_id = request.getParameter("category_id");
		String school_code = request.getParameter("school_code");
		
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup); 		
		
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup",schoolGroup);
		
		
			if(request.getParameter("student_seq")==null) {
				//行政模考設置			
				model.addAttribute("counselingBaseTitle_seq",counselingBaseTitle_seq);
				CounselingBaseTitle counselingBaseTitle = admService.getCounselingBaseTitle("",counselingBaseTitle_seq,"").get(0);
				model.addAttribute("slot_1",counselingBaseTitle.getSlot_1_from()+" ~ "+counselingBaseTitle.getSlot_1_to());
				model.addAttribute("slot_2",counselingBaseTitle.getSlot_2_from()+" ~ "+counselingBaseTitle.getSlot_2_to());
				model.addAttribute("slot_3",counselingBaseTitle.getSlot_3_from()+" ~ "+counselingBaseTitle.getSlot_3_to());

				school_code = counselingBaseTitle.getSchool_code();
				category_id = counselingBaseTitle.getCategory_id();
				
			}else {
				
		   		//學生充電站預約				
				String register_counseling_seq = request.getParameter("register_counseling_seq");
				model.addAttribute("register_counseling_seq",register_counseling_seq); 		
		   		model.addAttribute("Register_seq",request.getParameter("Register_seq"));
		   		model.addAttribute("student_seq",request.getParameter("student_seq"));

		   		
		    	Student student = salesService.getStudent(request.getParameter("student_seq"),"","","","","","","","","").get(0);
		    	
		    /**	
		    	if(student.getSchool_code()!=null && !student.getSchool_code().isEmpty()) {
		    		school_code = student.getSchool_code();
		    	}else {
		    		school_code = accountService.getAccountByID("",principal.getName()).getSchool_code();
		    	}
		    **/	
		    	
			}
					
        //可選行政設置之-->考科-名稱
   		List<CounselingBaseTitle> LCounselingBaseTitle;
   		LCounselingBaseTitle = admService.getCounselingBaseTitle(school_code,"",category_id);
   		if(LCounselingBaseTitle.size()==0) { //選不到時顯示該分校所有-->考科-名稱
   			LCounselingBaseTitle = admService.getCounselingBaseTitle(school_code,"","");
   		}else {
   			model.addAttribute("counselingBaseTitle_seq",LCounselingBaseTitle.get(0).getCounselingBaseTitle_seq());
   		}	
   		model.addAttribute("school_code",school_code);
   		model.addAttribute("category_id",category_id);
   		return "/Adm/openCounselingBaseTitle";
    }    

    @RequestMapping("/Adm/getPanel")
    @ResponseBody
    public String getPanel(Model model,HttpServletRequest request){ 
    	String student_seq = request.getParameter("student_seq");
		String panelStr = "";
		List<MockPanel> LMockPanel = admService.getMockPanel(request.getParameter("mockBaseTitle_seq"),"");
		for(int a=0;a<LMockPanel.size();a++) {
			if(student_seq!=null && !student_seq.isEmpty()) {
				panelStr += "<div><input type='radio' name='mockPanel_id' value='"+LMockPanel.get(a).getMockPanel_seq()+"'>&nbsp;"+LMockPanel.get(a).getPanelName()+"</div>";
			}else {
				panelStr += "<div>"+LMockPanel.get(a).getPanelName()+"</div>";
			}	
		}
		return panelStr;
    }	
 
    @RequestMapping("/Adm/openMockBase")
    public String openMockBase(Model model,HttpServletRequest request){
   		String setDate = request.getParameter("setDate");
   		model.addAttribute("setDate",setDate);
   		if(setDate.length()==8) {
   			model.addAttribute("settingDate","日期 : "+setDate.substring(4,6)+"/"+setDate.substring(6,8)+"/"+setDate.substring(0,4));
   		}
   		String mockBaseTitle_seq = request.getParameter("mockBaseTitle_seq");
   		model.addAttribute("mockBaseTitle_seq",mockBaseTitle_seq);

   		//人數限制   		
   		List<TestSubjectSelection2> LTestSubjectSelection = admService.getTestSubjectSelection2(mockBaseTitle_seq,"","");
   		if(LTestSubjectSelection.size()>0) {
	   		String tmp = "<input type='hidden' name='witLimit' value='1'>";
	   		for(int i=0;i<LTestSubjectSelection.size();i++) {
	   			List<MockLimit> LMockLimit = admService.getMockLimit(LTestSubjectSelection.get(i).getTestSubjectSelection2_seq(),"");
	   			for(int b=0;b<LMockLimit.size();b++) {
	   				tmp += "<div style='font-weight:normal;margin-top:3px;font-size:small;letter-spacing:0px'>"+LMockLimit.get(b).getFromx()+"~"+LMockLimit.get(b).getTox()+"&nbsp;:&nbsp;"+LMockLimit.get(b).getNoLimit()+"人</div>";
	   			}	
	   		}
	   		model.addAttribute("Limit",tmp);
   		}	
   		
   		return "/Adm/openMockBase";
    }
  
  
    @RequestMapping("/Adm/openCounselingBase")
    public String openCounselingBase(Model model,HttpServletRequest request){
   		String setDate = request.getParameter("setDate");
   		model.addAttribute("setDate",setDate);
   		if(setDate.length()==8) {
   			model.addAttribute("settingDate","日期 : "+setDate.substring(4,6)+"/"+setDate.substring(6,8)+"/"+setDate.substring(0,4));
   		}
   		String counselingBaseTitle_seq = request.getParameter("counselingBaseTitle_seq");
   		model.addAttribute("counselingBaseTitle_seq",counselingBaseTitle_seq);

   		//人數限制  
   		String tmp1="",tmp2="",tmp3="";
		if(counselingBaseTitle_seq!=null && !counselingBaseTitle_seq.isEmpty()) {
		    List<CounselingLimit1> LCounselingLimit1 = admService.getCounselingLimit1(counselingBaseTitle_seq,"","","");				
			for(int i=0;i<LCounselingLimit1.size();i++) {
				List<CounselingLimit2> LCounselingLimit2 = admService.getCounselingLimit2(LCounselingLimit1.get(i).getCounselingLimit1_seq(),"");
				if(LCounselingLimit1.get(i).getSlot().equals("1")) {
					tmp1 ="早 : "+LCounselingLimit1.get(i).getTeacher_name();
					for(int a=0;a<LCounselingLimit2.size();a++) {					
						tmp1 += "<div style='text-align:center;font-weight:normal;margin-top:3px;font-size:small;letter-spacing:0px'>"+LCounselingLimit2.get(a).getFrom1()+"~"+LCounselingLimit2.get(a).getTo1()+"&nbsp;:&nbsp;"+LCounselingLimit2.get(a).getNoLimit()+"人</div>";
					} 
					model.addAttribute("Limit1",tmp1);
				}else if(LCounselingLimit1.get(i).getSlot().equals("2")) {
					tmp2 ="午 : "+LCounselingLimit1.get(i).getTeacher_name();
					for(int a=0;a<LCounselingLimit2.size();a++) {					
						tmp2 += "<div style='text-align:center;font-weight:normal;margin-top:3px;font-size:small;letter-spacing:0px'>"+LCounselingLimit2.get(a).getFrom1()+"~"+LCounselingLimit2.get(a).getTo1()+"&nbsp;:&nbsp;"+LCounselingLimit2.get(a).getNoLimit()+"人</div>";
					} 
					model.addAttribute("Limit2",tmp2);
				}else if(LCounselingLimit1.get(i).getSlot().equals("3")) {
					tmp3 ="晚 : "+LCounselingLimit1.get(i).getTeacher_name();
					for(int a=0;a<LCounselingLimit2.size();a++) {					
						tmp3 += "<div style='text-align:center;font-weight:normal;margin-top:3px;font-size:small;letter-spacing:0px'>"+LCounselingLimit2.get(a).getFrom1()+"~"+LCounselingLimit2.get(a).getTo1()+"&nbsp;:&nbsp;"+LCounselingLimit2.get(a).getNoLimit()+"人</div>";
					} 
					model.addAttribute("Limit3",tmp3);
				}
			}
		}	
   		
   		return "/Adm/openCounselingBase";
    }     
    
    @RequestMapping("/Adm/mockBaseSave")
    public String mockBaseSave(Model model,HttpServletRequest request,Principal principal){
    	String mockBaseTitle_seq  = request.getParameter("mockBaseTitle_seq");
    	String setDate  = request.getParameter("setDate");
    	String round  = request.getParameter("round");
    	String A_slot[] = request.getParameterValues("slot");
    	String witLimit = request.getParameter("witLimit");
    	
    	admService.mockBaseSave(mockBaseTitle_seq,setDate,A_slot,round,principal.getName(),witLimit);   
      	
    	return "/common/closeAndReload";
    }
    
    @RequestMapping("/Adm/counselingBaseSave")
    public String counselingBaseSave(Model model,HttpServletRequest request,Principal principal){
    	String counselingBaseTitle_seq  = request.getParameter("counselingBaseTitle_seq");
    	String setDate  = request.getParameter("setDate");
    	String A_slot[] = request.getParameterValues("slot");
    	String witLimit = request.getParameter("witLimit");
    	
    	admService.counselingBaseSave(counselingBaseTitle_seq,setDate,A_slot,principal.getName(),witLimit);   
      	
    	return "/common/closeAndReload";
    }    
    
    @RequestMapping("/Adm/openDate")
    public String openDate(Model model,HttpServletRequest request){
   		return "/Adm/openDate";
    }
    
    @RequestMapping("/Adm/addMockBaseTitle")
    public String addMockBaseTitle(Model model,HttpServletRequest request){
    	String mockBaseTitle_seq = request.getParameter("mockBaseTitle_seq");
		
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup",schoolGroup); 
		
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		
		if(mockBaseTitle_seq!=null && !mockBaseTitle_seq.isEmpty()) {
			model.addAttribute("mockBaseTitle_seq",mockBaseTitle_seq);		
			List<MockBaseTitle> LMockBaseTitle = admService.getMockBaseTitle("",mockBaseTitle_seq,"","","");		
			model.addAttribute("mockBaseTitle",LMockBaseTitle.get(0));
		}else {
			model.addAttribute("mockBaseTitle_seq","");
			model.addAttribute("mockBaseTitle",new MockBaseTitle());
		}
		
		String tmp1="",tmp2="",tmp3="";
		if(mockBaseTitle_seq!=null && !mockBaseTitle_seq.isEmpty()) {			
			List<TestSubjectSelection2> LTestSubjectSelection = admService.getTestSubjectSelection2(mockBaseTitle_seq,"","");
			tmp1="";tmp2="";tmp3="";
			for(int i=0;i<LTestSubjectSelection.size();i++) {
				List<MockLimit> LMockLimit = admService.getMockLimit(LTestSubjectSelection.get(i).getTestSubjectSelection2_seq(),"");
				for(int b=0;b<LMockLimit.size();b++) {
					if(LTestSubjectSelection.get(i).getSlot().equals("1")) {
						model.addAttribute("teacher_id1",LTestSubjectSelection.get(i).getTeacher_id());
						tmp1 += "<div style='margin-top:3px;font-weight:normal;letter-spacing:0px;font-size:small'><A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>"+"<input type='text' style='height:21px;width:50px' name='from1' value='"+LMockLimit.get(b).getFromx()+"'>~<input type='text' style='height:21px;width:50px' name='to1' value='"+LMockLimit.get(b).getTox()+"'>&nbsp;:&nbsp;<input type='text' style='height:21px;width:30px' name='limit1' value='"+LMockLimit.get(b).getNoLimit()+"'>人</div>";
					}else if(LTestSubjectSelection.get(i).getSlot().equals("2")) {
						model.addAttribute("teacher_id2",LTestSubjectSelection.get(i).getTeacher_id());
						tmp2 += "<div style='margin-top:3px;font-weight:normal;letter-spacing:0px;font-size:small'><A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>"+"<input type='text' style='height:21px;width:50px' name='from2' value='"+LMockLimit.get(b).getFromx()+"'>~<input type='text' style='height:21px;width:50px' name='to2' value='"+LMockLimit.get(b).getTox()+"'>&nbsp;:&nbsp;<input type='text' style='height:21px;width:30px' name='limit2' value='"+LMockLimit.get(b).getNoLimit()+"'>人</div>";
					}else if(LTestSubjectSelection.get(i).getSlot().equals("3")) {
						model.addAttribute("teacher_id3",LTestSubjectSelection.get(i).getTeacher_id());
						tmp3 += "<div style='margin-top:3px;font-weight:normal;letter-spacing:0px;font-size:small'><A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>"+"<input type='text' style='height:21px;width:50px' name='from3' value='"+LMockLimit.get(b).getFromx()+"'>~<input type='text' style='height:21px;width:50px' name='to3' value='"+LMockLimit.get(b).getTox()+"'>&nbsp;:&nbsp;<input type='text' style='height:21px;width:30px' name='limit3' value='"+LMockLimit.get(b).getNoLimit()+"'>人</div>";
					}	
				}	
			}
			model.addAttribute("limitList1",tmp1);
			model.addAttribute("limitList2",tmp2);
			model.addAttribute("limitList3",tmp3);
		}
		
		List<Teacher> LTeacher = courseService.getTeacher("","","","","1","","");
		model.addAttribute("LTeacher",LTeacher);		
		
   		return "/Adm/addMockBaseTitle";
    } 
    
    @RequestMapping("/Adm/addCounselingBaseTitle")
    public String addCounselingBaseTitle(Model model,HttpServletRequest request){
    	String counselingBaseTitle_seq = request.getParameter("counselingBaseTitle_seq");
		
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup",schoolGroup); 
		
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		
		if(counselingBaseTitle_seq!=null && !counselingBaseTitle_seq.isEmpty()) {
			model.addAttribute("counselingBaseTitle_seq",counselingBaseTitle_seq);		
			List<CounselingBaseTitle> LCounselingBaseTitle = admService.getCounselingBaseTitle("",counselingBaseTitle_seq,"");		
			model.addAttribute("counselingBaseTitle",LCounselingBaseTitle.get(0));
		}else {
			model.addAttribute("counselingBaseTitle_seq","");
			model.addAttribute("counselingBaseTitle",new CounselingBaseTitle());
		}
		
		String tmp1="",tmp2="",tmp3="";
		if(counselingBaseTitle_seq!=null && !counselingBaseTitle_seq.isEmpty()) {
		    List<CounselingLimit1> LCounselingLimit1 = admService.getCounselingLimit1(counselingBaseTitle_seq,"","","");	
			
			for(int i=0;i<LCounselingLimit1.size();i++) {
				List<CounselingLimit2> LCounselingLimit2 = admService.getCounselingLimit2(LCounselingLimit1.get(i).getCounselingLimit1_seq(),"");
				if(LCounselingLimit1.get(i).getSlot().equals("1")) {
					model.addAttribute("teacher_id1",LCounselingLimit1.get(i).getTeacher_id());
					for(int a=0;a<LCounselingLimit2.size();a++) {					
						tmp1 += "<div style='margin-top:3px;font-weight:normal;letter-spacing:0px;font-size:small'><A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>"+"<input type='text' style='height:21px;width:50px' name='from1' value='"+LCounselingLimit2.get(a).getFrom1()+"'>~<input type='text' style='height:21px;width:50px' name='to1' value='"+LCounselingLimit2.get(a).getTo1()+"'>&nbsp;&nbsp;&nbsp;<input type='text' style='height:21px;width:30px' name='limit1' value='"+LCounselingLimit2.get(a).getNoLimit()+"'>人</div>";
					}
			    }else if(LCounselingLimit1.get(i).getSlot().equals("2")) {
			    	model.addAttribute("teacher_id2",LCounselingLimit1.get(i).getTeacher_id());
					for(int a=0;a<LCounselingLimit2.size();a++) {					
						tmp2 += "<div style='margin-top:3px;font-weight:normal;letter-spacing:0px;font-size:small'><A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>"+"<input type='text' style='height:21px;width:50px' name='from2' value='"+LCounselingLimit2.get(a).getFrom1()+"'>~<input type='text' style='height:21px;width:50px' name='to2' value='"+LCounselingLimit2.get(a).getTo1()+"'>&nbsp;&nbsp;&nbsp;<input type='text' style='height:21px;width:30px' name='limit2' value='"+LCounselingLimit2.get(a).getNoLimit()+"'>人</div>";
					}
			    }else if(LCounselingLimit1.get(i).getSlot().equals("3")) {
			    	model.addAttribute("teacher_id3",LCounselingLimit1.get(i).getTeacher_id());
					for(int a=0;a<LCounselingLimit2.size();a++) {					
						tmp3 += "<div style='margin-top:3px;font-weight:normal;letter-spacing:0px;font-size:small'><A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete2.png' height='11px'/></A>"+"<input type='text' style='height:21px;width:50px' name='from3' value='"+LCounselingLimit2.get(a).getFrom1()+"'>~<input type='text' style='height:21px;width:50px' name='to3' value='"+LCounselingLimit2.get(a).getTo1()+"'>&nbsp;&nbsp;&nbsp;<input type='text' style='height:21px;width:30px' name='limit3' value='"+LCounselingLimit2.get(a).getNoLimit()+"'>人</div>";
					}
			    }	
			}

			model.addAttribute("limitList1",tmp1);
			model.addAttribute("limitList2",tmp2);
			model.addAttribute("limitList3",tmp3);
		}	
		
		List<Teacher> LTeacher = courseService.getTeacher("","","","","1","","");
		model.addAttribute("LTeacher",LTeacher);
		
   		return "/Adm/addCounselingBaseTitle";
    }     

    @RequestMapping("/Adm/getSlot")
    @ResponseBody
    public String getSlot(Model model,HttpServletRequest request){
   		String mockBaseTitle_seq = request.getParameter("mockBaseTitle_seq");
   		List<MockBaseTitle> LMockBaseTitle = admService.getMockBaseTitle("",mockBaseTitle_seq,"","","");
   		String returnStr = "";
   		if(LMockBaseTitle.size()>0) {
   			returnStr=
   			"<div class='tr'>"+		
	   			"<div class='td' style='text-align:center;width:40px'>平日 :</div>"+			
				"<div class='td' style='text-align:center;width:100px'>早 "+LMockBaseTitle.get(0).getSlot_1_from()+" ~ "+LMockBaseTitle.get(0).getSlot_1_to()+"</div>"+
				"<div class='td' style='text-align:center;width:100px'>午 "+LMockBaseTitle.get(0).getSlot_2_from()+" ~ "+LMockBaseTitle.get(0).getSlot_2_to()+"</div>"+
				"<div class='td' style='text-align:center;width:100px'>晚 "+LMockBaseTitle.get(0).getSlot_3_from()+" ~ "+LMockBaseTitle.get(0).getSlot_3_to()+"</div>"+ 
   		    "</div>"+
   			"<div class='tr'>"+		
   			"<div class='td' style='text-align:center;width:40px'>假日 :</div>"+			
				"<div class='td' style='text-align:center;width:100px'>早 "+LMockBaseTitle.get(0).getSlot_1_from2()+" ~ "+LMockBaseTitle.get(0).getSlot_1_to2()+"</div>"+
				"<div class='td' style='text-align:center;width:100px'>午 "+LMockBaseTitle.get(0).getSlot_2_from2()+" ~ "+LMockBaseTitle.get(0).getSlot_2_to2()+"</div>"+
				"<div class='td' style='text-align:center;width:100px'>晚 "+LMockBaseTitle.get(0).getSlot_3_from2()+" ~ "+LMockBaseTitle.get(0).getSlot_3_to2()+"</div>"+ 
		    "</div>";				
   		}
   		return returnStr;
    }
    
    
    @RequestMapping("/Adm/getCounselingSlot")
    @ResponseBody
    public String getCounselingSlot(Model model,HttpServletRequest request){
   		String counselingBaseTitle_seq = request.getParameter("counselingBaseTitle_seq");
   		List<CounselingBaseTitle> LCounselingBaseTitle = admService.getCounselingBaseTitle("",counselingBaseTitle_seq,"");
   		String returnStr = "";
   		if(LCounselingBaseTitle.size()>0) {
   			returnStr=
   			"<div class='tr'>"+		
	   			"<div class='td' style='text-align:center;width:40px'>平日 :</div>"+			
				"<div class='td' style='text-align:center;width:100px'>早 "+LCounselingBaseTitle.get(0).getSlot_1_from()+" ~ "+LCounselingBaseTitle.get(0).getSlot_1_to()+"</div>"+
				"<div class='td' style='text-align:center;width:100px'>午 "+LCounselingBaseTitle.get(0).getSlot_2_from()+" ~ "+LCounselingBaseTitle.get(0).getSlot_2_to()+"</div>"+
				"<div class='td' style='text-align:center;width:100px'>晚 "+LCounselingBaseTitle.get(0).getSlot_3_from()+" ~ "+LCounselingBaseTitle.get(0).getSlot_3_to()+"</div>"+ 
   		    "</div>"+
   			"<div class='tr'>"+		
   			"<div class='td' style='text-align:center;width:40px'>假日 :</div>"+			
				"<div class='td' style='text-align:center;width:100px'>早 "+LCounselingBaseTitle.get(0).getSlot_1_from2()+" ~ "+LCounselingBaseTitle.get(0).getSlot_1_to2()+"</div>"+
				"<div class='td' style='text-align:center;width:100px'>午 "+LCounselingBaseTitle.get(0).getSlot_2_from2()+" ~ "+LCounselingBaseTitle.get(0).getSlot_2_to2()+"</div>"+
				"<div class='td' style='text-align:center;width:100px'>晚 "+LCounselingBaseTitle.get(0).getSlot_3_from2()+" ~ "+LCounselingBaseTitle.get(0).getSlot_3_to2()+"</div>"+ 
		    "</div>";				
   		}
   		return returnStr;
    }    
 
/**    
    @RequestMapping("/Adm/getConditations")
    @ResponseBody
    public String getConditations(Model model,HttpServletRequest request){
   		String mockBaseTitle_seq = request.getParameter("mockBaseTitle_seq");
   		List<MockBaseTitle> LMockBaseTitle = admService.getMockBaseTitle("",mockBaseTitle_seq,"");
   		String returnStr = "";
   		if(LMockBaseTitle.size()>0) {
   			returnStr=	
   			"<div class='tr'>"+		
	   			"<div class='td' style='text-align:center;width:40px'>平日 :</div>"+			
				"<div class='td' style='text-align:center;width:120px'>早 "+LMockBaseTitle.get(0).getSlot_1_from()+" ~ "+LMockBaseTitle.get(0).getSlot_1_to()+"</div>"+
				"<div class='td' style='text-align:center;width:120px'>午 "+LMockBaseTitle.get(0).getSlot_2_from()+" ~ "+LMockBaseTitle.get(0).getSlot_2_to()+"</div>"+
				"<div class='td' style='text-align:center;width:120px'>晚 "+LMockBaseTitle.get(0).getSlot_3_from()+" ~ "+LMockBaseTitle.get(0).getSlot_3_to()+"</div>"+ 
   		    "</div>"+
   			"<div class='tr'>"+		
   			"<div class='td' style='text-align:center;width:40px'>假日 :</div>"+			
				"<div class='td' style='text-align:center;width:120px'>早 "+LMockBaseTitle.get(0).getSlot_1_from2()+" ~ "+LMockBaseTitle.get(0).getSlot_1_to2()+"</div>"+
				"<div class='td' style='text-align:center;width:120px'>午 "+LMockBaseTitle.get(0).getSlot_2_from2()+" ~ "+LMockBaseTitle.get(0).getSlot_2_to2()+"</div>"+
				"<div class='td' style='text-align:center;width:120px'>晚 "+LMockBaseTitle.get(0).getSlot_3_from2()+" ~ "+LMockBaseTitle.get(0).getSlot_3_to2()+"</div>"+ 
		    "</div>";				
   		}
   		return returnStr;
    }    
**/
    
    @RequestMapping("/Adm/mockBaseTitleSave")
    public String mockBaseTitleSave(Model model,HttpServletRequest request){
    	String mockBaseTitle_seq = request.getParameter("mockBaseTitle_seq");
    	String school_code = request.getParameter("school_code");
    	String category_id = request.getParameter("category_id");
    	String attend = request.getParameter("attend");
    	String testWay = request.getParameter("testWay");
    	String slot_1_from = request.getParameter("slot_1_from");
    	String slot_1_to = request.getParameter("slot_1_to"); 
    	String slot_2_from = request.getParameter("slot_2_from");
    	String slot_2_to = request.getParameter("slot_2_to");
    	String slot_3_from = request.getParameter("slot_3_from");
    	String slot_3_to = request.getParameter("slot_3_to"); 
    	String slot_1_from2 = request.getParameter("slot_1_from2");
    	String slot_1_to2 = request.getParameter("slot_1_to2"); 
    	String slot_2_from2 = request.getParameter("slot_2_from2");
    	String slot_2_to2 = request.getParameter("slot_2_to2");
    	String slot_3_from2 = request.getParameter("slot_3_from2");
    	String slot_3_to2 = request.getParameter("slot_3_to2"); 
    	String panelName[] = request.getParameterValues("panelName");
    	
    	//人數限制
    	String teacher_id1 = request.getParameter("teacher_id1");
    	String from1[] = request.getParameterValues("from1");
    	String to1[] = request.getParameterValues("to1");
    	String limit1[] = request.getParameterValues("limit1");
    	String teacher_id2 = request.getParameter("teacher_id2");
    	String from2[] = request.getParameterValues("from2");
    	String to2[] = request.getParameterValues("to2");
    	String limit2[] = request.getParameterValues("limit2");
    	String teacher_id3 = request.getParameter("teacher_id3");
    	String from3[] = request.getParameterValues("from3");
    	String to3 [] = request.getParameterValues("to3");
    	String limit3[] = request.getParameterValues("limit3");    	
    	  	
    	admService.mockBaseTitleSave(
    			mockBaseTitle_seq,
    			school_code,category_id,
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
    			teacher_id1,
    			from1,
    			to1,
    			limit1,
    			teacher_id2,
    			from2,
    			to2,
    			limit2, 
    			teacher_id3,
    			from3,
    			to3,
    			limit3    			
    	);
    	
    		admService.addMockPanel("5",mockBaseTitle_seq,panelName);

   		return "/common/closeAndReload";
    }
    
    @RequestMapping("/Adm/counselingBaseTitleSave")
    public String counselingBaseTitleSave(Model model,HttpServletRequest request){
    	String counselingBaseTitle_seq = request.getParameter("counselingBaseTitle_seq");
    	String school_code = request.getParameter("school_code");
    	String category_id = request.getParameter("category_id");
    	String slot_1_from = request.getParameter("slot_1_from");
    	String slot_1_to = request.getParameter("slot_1_to"); 
    	String slot_2_from = request.getParameter("slot_2_from");
    	String slot_2_to = request.getParameter("slot_2_to");
    	String slot_3_from = request.getParameter("slot_3_from");
    	String slot_3_to = request.getParameter("slot_3_to"); 
    	String slot_1_from2 = request.getParameter("slot_1_from2");
    	String slot_1_to2 = request.getParameter("slot_1_to2"); 
    	String slot_2_from2 = request.getParameter("slot_2_from2");
    	String slot_2_to2 = request.getParameter("slot_2_to2");
    	String slot_3_from2 = request.getParameter("slot_3_from2");
    	String slot_3_to2 = request.getParameter("slot_3_to2"); 
    	
    	
    	//人數限制
    	String teacher_id1 = request.getParameter("teacher_id1");
    	String from1[] = request.getParameterValues("from1");
    	String to1[] = request.getParameterValues("to1");
    	String limit1[] = request.getParameterValues("limit1");
    	String teacher_id2 = request.getParameter("teacher_id2");
    	String from2[] = request.getParameterValues("from2");
    	String to2[] = request.getParameterValues("to2");
    	String limit2[] = request.getParameterValues("limit2");
    	String teacher_id3 = request.getParameter("teacher_id3");
    	String from3[] = request.getParameterValues("from3");
    	String to3 [] = request.getParameterValues("to3");
    	String limit3[] = request.getParameterValues("limit3");     	
   	   	  	
    	admService.counselingBaseTitleSave(
    			counselingBaseTitle_seq,
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
    			teacher_id1,
    			from1,
    			to1,
    			limit1,
    			teacher_id2,
    			from2,
    			to2,
    			limit2, 
    			teacher_id3,
    			from3,
    			to3,
    			limit3  	
    	);

   		return "/common/closeAndReload";
    }     
    
    @RequestMapping("/Adm/editSlot")
    public String editSlot(Model model,HttpServletRequest request){
    	String school_code = request.getParameter("school_code");
    	model.addAttribute("school_code",school_code);
    	List<School> schoolGroup = accountService.getSchool(school_code,"");
    	model.addAttribute("school_name",schoolGroup.get(0).getName());
    	List<VideoSlotTitle> LVideoSlotTitle = admService.getVideoSlotTitle(school_code);
    	if(LVideoSlotTitle.size()>0) {
    		model.addAttribute("videoSlotTitle",LVideoSlotTitle.get(0));
    	}else {
    		model.addAttribute("videoSlotTitle",new VideoSlotTitle());
    	}
   		return "/Adm/editSlot";
    } 
    
    @RequestMapping("/Adm/CounselingBase")
    public String CounselingBase(Model model,HttpServletRequest request){
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup",schoolGroup);     	
   		return "/Adm/CounselingBase";
    }
    
    @RequestMapping("/Adm/MockSign")
    public String MockSign(Model model,HttpServletRequest request){
   		return "/Adm/MockSign";
    }
    
    @RequestMapping("/Adm/BookDiagram")
    public String BookDiagram(Model model,HttpServletRequest request,Principal principal,HttpSession session){
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}	
		
		List<School> schoolGroup = accountService.getSchool("","");
		model.addAttribute("schoolGroup", schoolGroup);	
		
		String school_code_self = "";
		if(principal!=null && principal.getName()!=null) {	
			school_code_self = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		}
		model.addAttribute("school_code_self", school_code_self);
		
		Calendar c = Calendar.getInstance();
		String beginYear = String.valueOf(c.get(Calendar.YEAR));
		String beginMonth = String.valueOf(c.get(Calendar.MONTH)+1);
		if(beginMonth.length()==1) {beginMonth="0"+beginMonth;}
		String beginDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		if(beginDay.length()==1) {beginDay="0"+beginDay;}
		String weekName = "";
		switch(c.get(Calendar.DAY_OF_WEEK)){
			case 1:weekName="星期日";break;
			case 2:weekName="星期一";break;
			case 3:weekName="星期二";break;
			case 4:weekName="星期三";break;
			case 5:weekName="星期四";break;
			case 6:weekName="星期五";break;
			case 7:weekName="星期六";break;
		}	

		model.addAttribute("beginYear",beginYear);
		model.addAttribute("beginMonth",beginMonth);
		model.addAttribute("beginDay",beginDay);
		model.addAttribute("weekName",weekName);	    	
   		return "/Adm/BookDiagram";
    }
    
    @RequestMapping("/Adm/classroomSetting")
    public String classroomSetting(Model model,HttpServletRequest request) {
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup",schoolGroup);
		if(request.getParameter("saveMessage")!=null) {
			model.addAttribute("saveMessage",request.getParameter("saveMessage"));
		}
    	return "/Adm/classroomSetting";
    }
    
    
    @RequestMapping("/Adm/classRoomSel")
    @ResponseBody
    public String classRoomSel(HttpServletRequest request){
		List<classRoom> LclassRoom = systemService.getclassRoom(request.getParameter("school_code"),"","school_code,name","","");
		String videoSel = "";
		String returnStr = "";
		String withSeat = "";
		for(int i=0;i<LclassRoom.size();i++) {
			withSeat = "";
			if(LclassRoom.get(i).getSeatFrom()!=null && !LclassRoom.get(i).getSeatFrom().isEmpty()) {
				withSeat = "&hellip;";
			}
			if(LclassRoom.get(i).getIsVideo().equals("0")) {
				videoSel = 
		        "<select name='isVideo' style='width:120px'>"+
		        		"<option value=''></option>"+		        		
		        		"<option value='0' selected>有錄影</option>"+
		        		"<option value='1'>無錄影</option>"+
		        		"<option value='2'>Video教室</option>"+
		        "</select>";						
			}else if(LclassRoom.get(i).getIsVideo().equals("1")) {
				videoSel = 
		        "<select name='isVideo' style='width:120px'>"+
		        		"<option value=''></option>"+		        		
		        		"<option value='0'>有錄影</option>"+
		        		"<option value='1' selected>無錄影</option>"+
		        		"<option value='2'>Video教室</option>"+
		        "</select>";						
			}else if(LclassRoom.get(i).getIsVideo().equals("2")) {
				videoSel = 
		        "<select name='isVideo' style='width:120px'>"+
		        		"<option value=''></option>"+		        		
		        		"<option value='0'>有錄影</option>"+
		        		"<option value='1'>無錄影</option>"+
		        		"<option value='2' selected>Video教室</option>"+
		        "</select>";						
			}else {
				videoSel = 
		        "<select name='isVideo' style='width:120px'>"+
		        		"<option value='' selected></option>"+		        		
		        		"<option value='0'>有錄影</option>"+
		        		"<option value='1'>無錄影</option>"+
		        		"<option value='2'>Video教室</option>"+
		        "</select>";
			}			
			LclassRoom.get(i).setVideoSel(videoSel);
			
			returnStr +=
	        "<div class='tr'>"+
	        	"<div class='td2' style='width:70px;  padding:2px;text-align:center'><A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='$(this).parent().parent().remove();'><img src='/images/delete2.png' height='11px'/></A></div>"+
	            "<div class='td2' style='width:130px; padding:2px;text-align:center'><input type='text' style='width:60px' name='classRoom_name' value="+LclassRoom.get(i).getName()+"></div>"+
	            "<div class='td2' style='width:180px; padding:2px;text-align:center'>"+LclassRoom.get(i).getVideoSel()+"</div>"+  
	            "<div class='td2' style='width:120px; padding:2px;text-align:center'><input type='text' style='width:60px' name='capacity' value="+LclassRoom.get(i).getCapacity()+"></div>"+
	            "<div class='td2' style='width:200px; padding:2px;text-align:center'><input type='text' style='width:60px' name='seatFrom' value="+LclassRoom.get(i).getSeatFrom()+"> ~ <input type='text' style='width:60px' name='seatTo'  value="+LclassRoom.get(i).getSeatTo()+"></div>"+ 
	            "<div class='td2' style='width:100px; padding:2px;text-align:center'><A href='javascript:void(0)' style='color:blue;text-decoration:underline;font-size:large' onclick=editClassroomStyle('"+LclassRoom.get(i).getSchool_code()+"','"+LclassRoom.get(i).getName()+"','"+LclassRoom.get(i).getSeatFrom()+"','"+LclassRoom.get(i).getSeatTo()+"')>"+withSeat+"</A></div>"+
            "</div>";			
		}
        return returnStr;
    } 
    
    
    @RequestMapping("/Adm/videoSlotTitleSave")
    public String videoSlotTitleSave (@Valid VideoSlotTitle videoSlotTitle,Model model,HttpServletRequest request){
    	admService.videoSlotTitleSave(videoSlotTitle);
   		return "/common/closeAndReload";
    }
    
    @RequestMapping("/Adm/classroomUpdate")
    public String classroomUpdate(Model model,HttpServletRequest request) {
    	String isVideo[] = request.getParameterValues("isVideo");
    	String classRoom_name[] = request.getParameterValues("classRoom_name");
    	String capacity[] = request.getParameterValues("capacity");
    	String school_code = request.getParameter("school_code");
    	String seatFrom[] = request.getParameterValues("seatFrom");
    	String seatTo[] = request.getParameterValues("seatTo");
    	
    	if(request.getParameter("update")!=null && school_code!=null && !school_code.isEmpty()) {
    		jdbcTemplate.update("delete from eip.classRoom where school_code='"+school_code+"'");
    		for(int i=0;i<classRoom_name.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.classRoom VALUES (default,?,?,?,?,?,?);",
						school_code,
						classRoom_name[i],
						isVideo[i],
						capacity[i],
						seatFrom[i],
						seatTo[i]
				);    			
    		}
    		return "redirect:/Adm/classroomSetting?saveMessage=Save Successfully!";
    	}else{   		
    	    if(request.getParameter("insert")!=null) {
				jdbcTemplate.update("INSERT INTO eip.classRoom VALUES (default,?,?,?,?);",
						school_code,
						classRoom_name[0],
						isVideo[0],
						capacity[0]
				);
    	    }	
			return "/common/closeAndReload";
    	}    	
    }
    
     
    @RequestMapping("/Adm/GetBookDiagram")
    @ResponseBody
    public String GetBookDiagram(Model model,HttpServletRequest request){
    	String beginYear = request.getParameter("beginYear");
    	String beginMonth = request.getParameter("beginMonth");
    	String beginDay = request.getParameter("beginDay");
    	String school_code = request.getParameter("school_code");
    	String slot = request.getParameter("slot");
    	String attend_date = beginMonth+"/"+beginDay+"/"+beginYear;
    	String attend_date2 = beginYear+beginMonth+beginDay;
        String returnStr = "";
        String setDate = beginYear+beginMonth+beginDay;
        
        //1.Video
		List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory2(school_code,"","","","","1","(1)","(0,1,-1)","2","","","",attend_date,"","","","",slot);
		String feePay1 = "";
		for(int i=0;i<LSignRecordHistory.size();i++) {
			if(LSignRecordHistory.get(i).getAllowAttend().equals("1")) {
				feePay1 = "&#10004";
			}else {feePay1 = "&#10006";}
			
			returnStr+=
			"<div class='tr' style=''>"+
			    "<div class='td2' style='width:50px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid;border-left:1px #aaaaaa solid;padding:3px'>"+(i+1)+"</div>"+
			    "<div class='td2' style='width:100px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+LSignRecordHistory.get(i).getUpdater()+"</div>"+
			    "<div class='td2' style='width:80px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+feePay1+"</div>"+			    
			    "<div class='td2' style='width:120px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+LSignRecordHistory.get(i).getClassroom()+"-"+LSignRecordHistory.get(i).getSeat()+"&nbsp;&nbsp;&nbsp;<A href='javascript:void(0)' onclick=studentSeat1('"+LSignRecordHistory.get(i).getSignRecordHistory_seq()+"','"+LSignRecordHistory.get(i).getStudent_no()+"','"+attend_date2+"','"+slot+"','"+school_code+"','"+LSignRecordHistory.get(i).getClassroom()+"','"+LSignRecordHistory.get(i).getSeat()+"')><img src='/images/edit.png' height='12px'></A></div>"+
			    "<div class='td2' style='width:80px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+LSignRecordHistory.get(i).getCh_name()+"</div>"+
			    "<div class='td2' style='width:80px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+LSignRecordHistory.get(i).getStudent_no()+"</div>"+
			    "<div class='td2' style='width:100px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+LSignRecordHistory.get(i).getClass_start_date()+"</div>"+
			    "<div class='td2' style='width:200px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid;text-align:left;padding:3px'>"+LSignRecordHistory.get(i).getSubject_name()+"</div>"+
			    "<div class='td2' style='width:50px;letter-spacing:5px;border-bottom:1px #aaaaaa solid;border-right:1px #aaaaaa solid'>"+LSignRecordHistory.get(i).getClass_th()+"</div>"+
			    "<div class='td2' style='width:250px;border-bottom:1px #aaaaaa solid;text-align:left;padding:3px'>"+LSignRecordHistory.get(i).getComment()+"</div>"+		
			    "<div class='td2' style='width:20px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid;text-align:left;padding:3px;text-align:center'><A href='javascript:void(0)' onclick=studentRemark('"+LSignRecordHistory.get(i).getSignRecordHistory_seq()+"','','','')><img src='/images/edit.png' height='12px'></A></div>"+	
		    "</div>";					
		}
		
		
		//2.模考講解
		List<MockVideoHistory> LMockVideoHistory = courseService.getMockVideoHistory("","1","","(1)","",attend_date2,school_code,slot,"","");
		for(int i=0;i<LMockVideoHistory.size();i++) {			
			returnStr+=
			"<div class='tr' style=''>"+
			    "<div class='td2' style='width:50px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid;border-left:1px #aaaaaa solid;padding:3px'>"+(LSignRecordHistory.size()+Integer.valueOf(i)+1)+"</div>"+
			    "<div class='td2' style='width:100px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+LMockVideoHistory.get(i).getUpdater()+"</div>"+
			    "<div class='td2' style='width:80px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>N/A</div>"+			    
			    "<div class='td2' style='width:120px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+LMockVideoHistory.get(i).getClassroom()+"-"+LMockVideoHistory.get(i).getSeat()+"&nbsp;&nbsp;&nbsp;<A href='javascript:void(0)' onclick=studentSeat2('"+LMockVideoHistory.get(i).getMockVideoHistory_seq()+"','"+LMockVideoHistory.get(i).getStudent_no()+"','"+attend_date2+"','"+slot+"','"+school_code+"','"+LMockVideoHistory.get(i).getClassroom()+"','"+LMockVideoHistory.get(i).getSeat()+"')><img src='/images/edit.png' height='12px'></A></div>"+
			    "<div class='td2' style='width:80px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+LMockVideoHistory.get(i).getCh_name()+"</div>"+
			    "<div class='td2' style='width:80px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+LMockVideoHistory.get(i).getStudent_no()+"</div>"+
			    "<div class='td2' style='width:100px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>(模考講解)</div>"+
			    "<div class='td2' style='width:200px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid;text-align:left;padding:3px'>"+LMockVideoHistory.get(i).getMockVideo_name()+"</div>"+
			    "<div class='td2' style='width:50px;border-bottom:1px #aaaaaa solid;border-right:1px #aaaaaa solid'>N/A</div>"+
			    "<div class='td2' style='width:250px;border-bottom:1px #aaaaaa solid;text-align:left;padding:3px'></div>"+			    
			    "<div class='td2' style='width:20px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid;text-align:left;padding:3px;text-align:center'><A href='javascript:void(0)' onclick=studentRemark('','"+LMockVideoHistory.get(i).getMockVideoHistory_seq()+"','','')><img src='/images/edit.png' height='12px'></A></div>"+	
		    "</div>";					
		}
		returnStr+="@";
		
		
		//3.模考
		List<MockBaseBook2> LMockBaseBook2 = salesService.getMockBaseBook("","1","",setDate,"",slot,"","","","");
		for(int i=0;i<LMockBaseBook2.size();i++) {			
			returnStr+=
			"<div class='tr' style=''>"+
			    "<div class='td2' style='width:50px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid;border-left:1px #aaaaaa solid;padding:3px'>"+(i+1)+"</div>"+
			    "<div class='td2' style='width:100px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+LMockBaseBook2.get(i).getUpdater()+"</div>"+
			    "<div class='td2' style='width:80px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>N/A</div>"+			    
			    "<div class='td2' style='width:120px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+LMockBaseBook2.get(i).getClassroom()+"-"+LMockBaseBook2.get(i).getSeat()+"&nbsp;&nbsp;&nbsp;<A href='javascript:void(0)' onclick=studentSeat3('"+LMockBaseBook2.get(i).getMockBaseBook_seq()+"','"+LMockBaseBook2.get(i).getStudent_no()+"','"+attend_date2+"','"+slot+"','"+school_code+"','"+LMockBaseBook2.get(i).getClassroom()+"','"+LMockBaseBook2.get(i).getSeat()+"')><img src='/images/edit.png' height='12px'></A></div>"+
			    "<div class='td2' style='width:80px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+LMockBaseBook2.get(i).getCh_name()+"</div>"+
			    "<div class='td2' style='width:80px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+LMockBaseBook2.get(i).getStudent_no()+"</div>"+
			    "<div class='td2' style='width:100px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>(模考)</div>"+
			    "<div class='td2' style='width:200px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid;text-align:left;padding:3px'>"+LMockBaseBook2.get(i).getMockSubjectName()+" "+LMockBaseBook2.get(i).getMockParaName()+" "+LMockBaseBook2.get(i).getPanelName()+"</div>"+
			    "<div class='td2' style='width:50px;border-bottom:1px #aaaaaa solid;border-right:1px #aaaaaa solid'>N/A</div>"+
			    "<div class='td2' style='width:250px;border-bottom:1px #aaaaaa solid;text-align:left;padding:3px'>"+LMockBaseBook2.get(i).getComment()+"</div>"+			    
			    "<div class='td2' style='width:20px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid;text-align:left;padding:3px;text-align:center'><A href='javascript:void(0)' onclick=studentRemark('','','"+LMockBaseBook2.get(i).getMockBaseBook_seq()+"','')><img src='/images/edit.png' height='12px'></A></div>"+	
		    "</div>";					
		}
		
		returnStr+="@";
		
		
		//4.充電站
		List<CounselingBaseBook> LCounselingBaseBook = salesService.getCounselingBaseBook("","1",setDate,"",slot,"","","","","");
		for(int i=0;i<LCounselingBaseBook.size();i++) {			
			returnStr+=
			"<div class='tr' style=''>"+
			    "<div class='td2' style='width:50px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid;border-left:1px #aaaaaa solid;padding:3px'>"+(i+1)+"</div>"+
			    "<div class='td2' style='width:100px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+LCounselingBaseBook.get(i).getUpdater()+"</div>"+
			    "<div class='td2' style='width:80px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>N/A</div>"+			    
			    "<div class='td2' style='width:120px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+LCounselingBaseBook.get(i).getClassroom()+"-"+LCounselingBaseBook.get(i).getSeat()+"&nbsp;&nbsp;&nbsp;<A href='javascript:void(0)' onclick=studentSeat4('"+LCounselingBaseBook.get(i).getCounselingBaseBook_seq()+"','"+LCounselingBaseBook.get(i).getStudent_no()+"','"+attend_date2+"','"+slot+"','"+school_code+"','"+LCounselingBaseBook.get(i).getClassroom()+"','"+LCounselingBaseBook.get(i).getSeat()+"')><img src='/images/edit.png' height='12px'></A></div>"+
			    "<div class='td2' style='width:80px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+LCounselingBaseBook.get(i).getCh_name()+"</div>"+
			    "<div class='td2' style='width:80px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>"+LCounselingBaseBook.get(i).getStudent_no()+"</div>"+
			    "<div class='td2' style='width:100px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid'>(充電站)</div>"+
			    "<div class='td2' style='width:200px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid;text-align:left;padding:3px'>"+LCounselingBaseBook.get(i).getCounseling_name()+"<br>"+LCounselingBaseBook.get(i).getLimitName()+"</div>"+
			    "<div class='td2' style='width:50px;border-bottom:1px #aaaaaa solid;border-right:1px #aaaaaa solid'>N/A</div>"+
			    "<div class='td2' style='width:250px;border-bottom:1px #aaaaaa solid;text-align:left;padding:3px'>"+LCounselingBaseBook.get(i).getComment()+"</div>"+			    
			    "<div class='td2' style='width:20px;border-right:1px #aaaaaa solid;border-bottom:1px #aaaaaa solid;text-align:left;padding:3px;text-align:center'><A href='javascript:void(0)' onclick=studentRemark('','','','"+LCounselingBaseBook.get(i).getCounselingBaseBook_seq()+"')><img src='/images/edit.png' height='12px'></A></div>"+	
		    "</div>";					
		}		
		
   		return returnStr;
    } 

    @RequestMapping("/Adm/getClassRoomName")
    @ResponseBody
    public String getClassRoomName(Model model,HttpServletRequest request){
    	String beginYear = request.getParameter("beginYear");
    	String beginMonth = request.getParameter("beginMonth");
    	String beginDay = request.getParameter("beginDay");
    	String school_code = request.getParameter("school_code");
    	String slot = request.getParameter("slot");
    	String attend_date = beginMonth+"/"+beginDay+"/"+beginYear;
    	
    	List<classRoom> LclassRoom = systemService.getclassRoom(request.getParameter("school_code"),"","name","1","");
		String returnStr =
		"<div class='css-table' style='margin-top:10px'>"+
    		"<div class='tr'>";
    		for(int i=0;i<LclassRoom.size();i++) {
    			if(i==0) {
    				returnStr+=
    				"<input type='hidden' id='classRoomName' value='"+LclassRoom.get(0).getName()+"'>";		
    			}
    			returnStr+=		
    			"<div class='td2 classRoomSel' style='font-size:x-small;width:40px;text-align:center;border:1px #dddddd solid;border-radius:5px' id='"+LclassRoom.get(i).getName()+"'><A href='javascript:void(0)' onclick=getSeatNo('"+LclassRoom.get(i).getName()+"') style='font-weight:bold;color:steelblue'>"+LclassRoom.get(i).getName()+"</div><div class='td2'>&nbsp;</div>";   				
    	    }
    		    returnStr+=
    		    "</div>"+
    		"</div>"+
        "</div>";		    		
    	       
        return returnStr;
    } 
    @RequestMapping("/Adm/getSeats")
    @ResponseBody
    public String getSeats(Model model,HttpServletRequest request){
    	String school_code = request.getParameter("school_code");
    	String slot = request.getParameter("slot");
    	String classRoom = request.getParameter("classRoom");
    	SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
    	String nowDate = sdFormat.format(new Date());	
    	List<ClassRoomUnavail> LClassRoomUnavail = admService.getClassRoomUnavail(school_code,classRoom,nowDate);
    	List<classRoom> LclassRoom = systemService.getclassRoom(school_code,"","","1",classRoom);
    	String returnStr = "<select id='seat' style='border:0px;width:95px;background-color:#3e7e99'><option value=''></option>";
    	if(LclassRoom.size()>0) {
	    	int seatFrom = Integer.valueOf(LclassRoom.get(0).getSeatFrom());
	    	int seatTo = Integer.valueOf(LclassRoom.get(0).getSeatTo());

    		for(int a=seatFrom;a<=seatTo;a++) {
    			int avail = 1;
		    	for(int i=0;i<LClassRoomUnavail.size();i++) {
        			//今日時段佔有座位(已點名未離開) 	
        			List<SeatOccupy> LSeatOccupy = admService.getSeatOccupy(school_code,nowDate,slot,classRoom,String.valueOf(a),"");				    		
		    		if(LClassRoomUnavail.get(i).getSeatNo().equals(String.valueOf(a)) || LSeatOccupy.size()>0) {
		    			avail = 0;
		    		}
		    	}
    			if(avail==1) {
		    		returnStr +="<option value='"+a+"'>"+a+"</option>";
		    	}
	    	}
    	}	
    	return returnStr;
    }	
    @RequestMapping("/Adm/getSeatNo")
    @ResponseBody
    public String getSeatNo(Model model,HttpServletRequest request){
    	String beginYear = request.getParameter("beginYear");
    	String beginMonth = request.getParameter("beginMonth");
    	String beginDay = request.getParameter("beginDay");
    	String school_code = request.getParameter("school_code");
    	String slot = request.getParameter("slot");
    	String attend_date = beginMonth+"/"+beginDay+"/"+beginYear;
    	String attend_date2 = beginYear+beginMonth+beginDay;
    	String classRoomName = request.getParameter("classRoomName");
    	String nowDate = beginYear+beginMonth+beginDay;
    	   	
    	List<HistoryMatrix> LHistoryMatrix = new ArrayList<>(); 
        //1.Video
		List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory2(school_code,"","","","","1","(1)","(0,1,-1)","2","","","",attend_date,"","","","","");
		for(int a=0;a<LSignRecordHistory.size();a++) {
			LHistoryMatrix.add(new HistoryMatrix(
					LSignRecordHistory.get(a).getSignRecordHistory_seq(),
					"",
					"",
					LSignRecordHistory.get(a).getStudent_id(),
					LSignRecordHistory.get(a).getCh_name(),
					LSignRecordHistory.get(a).getAttend(),
					LSignRecordHistory.get(a).getSlot(),
					LSignRecordHistory.get(a).getClassroom(),
					LSignRecordHistory.get(a).getSeat(),
					LSignRecordHistory.get(a).getStudent_no()
			));
		}
    	//2. 模考講解
		List<MockVideoHistory> LMockVideoHistory = courseService.getMockVideoHistory("","1","","(1)","",attend_date2,school_code,"","","");
		for(int b=0;b<LMockVideoHistory.size();b++) {
			LHistoryMatrix.add(new HistoryMatrix(
					"",
					LMockVideoHistory.get(b).getMockVideoHistory_seq(),
					"",
					LMockVideoHistory.get(b).getStudent_id(),
					LMockVideoHistory.get(b).getCh_name(),
					LMockVideoHistory.get(b).getAttend(),
					LMockVideoHistory.get(b).getSlot(),
					LMockVideoHistory.get(b).getClassroom(),
					LMockVideoHistory.get(b).getSeat(),
					LMockVideoHistory.get(b).getStudent_no()
			));
		}		
		//3.模考
		List<MockBaseBook2> LMockBaseBook2 = salesService.getMockBaseBook("","1","",attend_date2,school_code,"","","","","");
		for(int c=0;c<LMockBaseBook2.size();c++) {
			LHistoryMatrix.add(new HistoryMatrix(
					"",
					"",
					LMockBaseBook2.get(c).getMockBaseBook_seq(),
					LMockBaseBook2.get(c).getStudent_id(),
					LMockBaseBook2.get(c).getCh_name(),
					LMockBaseBook2.get(c).getAttend(),
					LMockBaseBook2.get(c).getSlot(),
					LMockBaseBook2.get(c).getClassroom(),
					LMockBaseBook2.get(c).getSeat(),
					LMockBaseBook2.get(c).getStudent_no()
			));
		}
		//4.充電站
		List<CounselingBaseBook> LCounselingBaseBook = salesService.getCounselingBaseBook("","1",attend_date2,school_code,"","","","","","2");
		for(int c=0;c<LCounselingBaseBook.size();c++) {
			LHistoryMatrix.add(new HistoryMatrix(
					"",
					"",
					LCounselingBaseBook.get(c).getCounselingBaseBook_seq(),
					LCounselingBaseBook.get(c).getStudent_id(),
					LCounselingBaseBook.get(c).getCh_name(),
					LCounselingBaseBook.get(c).getAttend(),
					LCounselingBaseBook.get(c).getSlot(),
					LCounselingBaseBook.get(c).getClassroom(),
					LCounselingBaseBook.get(c).getSeat(),
					LCounselingBaseBook.get(c).getStudent_no()
			));
		}		
		
		//教室
		List<classRoom> LclassRoom = systemService.getclassRoom(request.getParameter("school_code"),"","","",classRoomName);
    	String returnStr ="";
    	if(LclassRoom.size()>0 && LclassRoom.get(0).getSeatFrom()!=null && !LclassRoom.get(0).getSeatFrom().isEmpty() && LclassRoom.get(0).getSeatTo()!=null && !LclassRoom.get(0).getSeatTo().isEmpty()) {
    		int seatFrom = Integer.valueOf(LclassRoom.get(0).getSeatFrom());
    		int seatTo= Integer.valueOf(LclassRoom.get(0).getSeatTo());
    		int no = 0;
    		String slotStr="";
    		String attend_student = "";
    		String studentStr="";
            String student_no = "";
            String student_name = "";
            String unavail = "";
            
    		List<ClassRoomUnavail> LClassRoomUnavail = admService.getClassRoomUnavail(school_code,classRoomName,nowDate);
    		
    		returnStr+=    		
    		"<div class='css-table' style='margin-top:10px'>";
    		    for(int i=seatFrom;i<=seatTo;i++) {
    		    	//位置不可用
    		    	unavail = "avail";
    		    	student_no = "";
    		    	student_name = "";
    		    	for(int x=0;x<LClassRoomUnavail.size();x++) {
	        			if(LClassRoomUnavail.get(x).getSeatNo().equals(String.valueOf(i))) {
	        				unavail = LClassRoomUnavail.get(x).getComment()==null?"":LClassRoomUnavail.get(x).getComment();
	        			}  
    		    	}	
    		    	studentStr="";
    		    	no++;
    		    	
    		    	if(no%10==1) {
    		    		returnStr+= "<div class='tr'>";
    		    	}
    		    	returnStr+=
    		    			slotStr="";
    		    			attend_student="";
    		    			for(int a=0;a<LHistoryMatrix.size();a++) {
    		    	        	//此座位
    		    	        	if(LHistoryMatrix.get(a).getClassroom().equals(classRoomName) && LHistoryMatrix.get(a).getSeat().equals(String.valueOf(i))) {
		    	        			//今日時段佔有座位(已點名未離開) 	
		    	        			List<SeatOccupy> LSeatOccupy = admService.getSeatOccupy(school_code,attend_date2,slot,classRoomName,String.valueOf(i),LHistoryMatrix.get(a).getStudent_no());			
		    	        			if(LSeatOccupy.size()>0) {    		    	        		
	    		    	        		//本時段學生
	    		    	        		if(LHistoryMatrix.get(a).getSlot().equals(slot)) {
	    		    	        			studentStr = "<b>"+LHistoryMatrix.get(a).getStudent_no()+" "+"<A href='javascript:void(0)' onclick='openStudent("+LHistoryMatrix.get(a).getStudent_id()+")' style='color:blue'>"+LHistoryMatrix.get(a).getCh_name()+"</A></b>";
	    		    	        			attend_student = LHistoryMatrix.get(a).getStudent_id();
	 		    	        			    student_no = LHistoryMatrix.get(a).getStudent_no();
	 		    	        			    student_name = LHistoryMatrix.get(a).getCh_name();	    		    	        			   
	    		    	        		//別時段	
	    		    	        		}else {
	    		    	        			if(LHistoryMatrix.get(a).getStudent_id().equals(attend_student) && LHistoryMatrix.get(a).getSlot().equals("2") && Integer.valueOf(slot)<=1) {slotStr +="(+午)";}
	    		    	        			if(LHistoryMatrix.get(a).getStudent_id().equals(attend_student) && LHistoryMatrix.get(a).getSlot().equals("3") && Integer.valueOf(slot)<=2) {slotStr +="(+晚)";}
	    		    	        		}
		    	        			}
    		    	        	}
    		    	        }
    		    			
    		    			/*********************座位td*****************************/
		    				returnStr+=
	    		    	    "<div class='td2' style='width:115px;height:70px;border:1px #dddddd solid;font-size:xx-small;letter-spacing:0px;border-radius:3px'>";
	    		    	    
		    				if(unavail.equals("avail")){		    
			    				returnStr+=
	    		    			"<span style='font-size:xx-small;border:1px #dddddd solid;font-weight:bold;color:#266489;background-color:#ffefff'>"+i+"</span>";  
			    				if(!studentStr.isEmpty()) {	
			    					/**
			    					returnStr+=
	                                "&nbsp;&nbsp;&nbsp;<span id='attend' style='border-radius:5px;font-size:xx-small;border:1px #dddddd solid;"+bgColor+"'>&nbsp;點 名&nbsp;</span>&nbsp;&nbsp;&nbsp;&nbsp;";
			    					List<SeatLeave> LSeatLeave= admService.getSeatLeave(school_code,attend_date2,slot,classRoomName,String.valueOf(i));
			    					if(LSeatLeave.size()==0) {
			    						returnStr+="<span><A href='javascript:void(0)' onclick=seatEmpty(\'"+school_code+"\',\'"+attend_date2+"\',\'"+slot+"\',\'"+classRoomName+"\',\'"+i+"\') style='color:blue;text-decoration:underline'>離開</A></span>";
			    					}else {
			    						returnStr+="<span style='border-radius:5px;border:1px #dddddd solid;background-color:#FEFF8C'><A href='javascript:void(0)' onclick=seatEmpty(\'"+school_code+"\',\'"+attend_date2+"\',\'"+slot+"\',\'"+classRoomName+"\',\'"+i+"\') style='color:blue'>&nbsp;離 開&nbsp;</A></span>";
			    					}**/
			    					returnStr+="<span style='font-size:xx-small'>&nbsp;<A href='javascript:void(0)' onclick=seatEmpty(\'"+school_code+"\',\'"+attend_date2+"\',\'"+slot+"\',\'"+classRoomName+"\',\'"+i+"\',\'"+student_no+"\',\'"+student_name+"\') style='color:#266489;text-decoration:underline'>離開</A></span>";
	    		    				returnStr+=
			    					"<div style='font-size:small;padding:2px;margin-top:6px'>"+studentStr+"<br>"+slotStr+"</div>";
	    		    			}
		    				}else {
		    					returnStr+=
		    					"<span style='font-size:xx-small;border:1px #dddddd solid;font-weight:bold;color:#bbbbbb;background-color:#ffefff'>"+i+"</span>"+  
		    					"<div style='text-align:center;font-size:20px'>&#9888;</div>"+
		    					"<div style='text-align:center;color:red'>"+unavail+"</div>";		
		    							
		    				}
		    		    	returnStr+=		
		    		    	"</div>"+
		    		    	/**************************************************/
    		    	"<div style='width:4px'></div>";
    		    	if(no%10==0) {
    		    		returnStr+= "</div><div style='height:4px'></div>";
    		    	}    		    	
    		    }
    		returnStr+= 
    		"</div>";        		
    	}
    	
    	return returnStr;
    }
    
    @RequestMapping("/Adm/seatOccupy")
    @ResponseBody
    public Boolean seatOccupy(Model model,HttpServletRequest request){
    	String school_code = request.getParameter("school_code");
    	String setDate = request.getParameter("setDate");
    	String slot = request.getParameter("slot");
    	String classroom = request.getParameter("classroom");
    	String seat = request.getParameter("seat");
    	String student_no = request.getParameter("student_no");
    	
    	List<SeatOccupy> LSeatOccupy = admService.getSeatOccupy(school_code,setDate,slot,classroom,seat,student_no);
    	if(LSeatOccupy.size()==0) {
    		admService.insertSeatOccupy(school_code,setDate,slot,classroom,seat,student_no);
    	}else {
    		admService.deleteSeatOccupy(school_code,setDate,slot,classroom,seat,student_no);
    	}
   		return true;
    } 
    

    @RequestMapping("/Adm/getStudentList")
    @ResponseBody
    public String getStudentList(Model model,HttpServletRequest request){
    	String grade_id = request.getParameter("grade_id");
    	String class_th = request.getParameter("class_th");
    	
    	//顯示正班學生
    	String returnStr = "";
    	List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory("","",grade_id,"1","(1)","","","","1","",class_th);
    	for(int i=0;i<LSignRecordHistory.size();i++) {
    		List<Student> LStudent = salesService.getStudent(LSignRecordHistory.get(i).getStudent_id(),"","","","","","","","","");
    		if(LStudent.size()>0) {
    			returnStr +=
    			"<tr style='background-color:white'>"+
    				"<td style='padding:2px;width:30px;border:1px #999999 solid;text-align:center;height:30px;background-color:#D4DBD3'>"+(i+1)+"</td>"+
    				"<td style='padding:2px;width:100px;border:1px #999999 solid;text-align:center;height:30px'>"+LStudent.get(0).getStudent_no()+"</td>"+
    				"<td style='padding:2px;width:100px;border:1px #999999 solid;text-align:center'>"+LStudent.get(0).getCh_name()+"</td>"+
    				"<td style='padding:2px;width:100px;border:1px #999999 solid;text-align:left'>"+(LStudent.get(0).getMobile_1()==null?"":LStudent.get(0).getMobile_1())+"</td>"+
    				"<td style='padding:2px;width:300px;border:1px #999999 solid;text-align:left'>"+(LStudent.get(0).getEmail_1()==null?"":LStudent.get(0).getEmail_1())+"</td>"+
    				"<td style='padding:2px;width:100px;border:1px #999999 solid;text-align:center'>"+LStudent.get(0).getFb()+"</td>"+
    				"<td style='padding:2px;width:100px;border:1px #999999 solid;text-align:center'>"+LStudent.get(0).getLine()+"</td>"+    				
    				"<td style='padding:2px;width:300px;border:1px #999999 solid;text-align:left'>"+LStudent.get(0).getCategory()+"</td>"+
    				"<td style='padding:2px;width:100px;border:1px #999999 solid;text-align:center'>"+LStudent.get(0).getCreater()+"</td>"+
    			"</tr>";	
    		}
    	}
    	return returnStr;
    }	
 
    @RequestMapping("/Adm/addClassRoom")
    public String addClassRoom(Model model,HttpServletRequest request) {
    	model.addAttribute("school_code",request.getParameter("school_code"));
    	return "/Adm/addClassRoom";
    } 
    
    @RequestMapping("/Adm/editClassroomStyle")
    public String editClassroomStyle(Model model,HttpServletRequest request) {
    	String school_code = request.getParameter("school_code");
    	String classRoomName = request.getParameter("classRoomName");
    	String seatFrom = request.getParameter("seatFrom");
    	String seatTo = request.getParameter("seatTo");
    	String saveSuccessful = request.getParameter("saveSuccessful");
    	if(saveSuccessful!=null && saveSuccessful.equals("1")) {
    		model.addAttribute("saveSuccessful","儲存成功!");
    	}
    	
    	model.addAttribute("school_code",school_code);
    	model.addAttribute("classRoomName",classRoomName);
    	int seatFromInt = Integer.valueOf(request.getParameter("seatFrom"));
    	int seatToInt = Integer.valueOf(request.getParameter("seatTo"));
    	String returnStr =
    	"<div class='css-table'>"+
    			"<div class='tr'>"+
    			    "<div class='td2' style='width:100px;height:25px;text-align:center;font-weight:bold;background-color:#FFFFCC;vertical-align:bottom;border:1px #cccccc solid'>座位編號</div>"+
    			    "<div class='td2' style='width:170px;text-align:center;font-weight:bold;background-color:#FFFFCC;vertical-align:bottom;border:1px #cccccc solid'>不使用_開始日期</div>"+
    			    "<div class='td2' style='width:170px;text-align:center;font-weight:bold;background-color:#FFFFCC;vertical-align:bottom;border:1px #cccccc solid'>不使用_結束日期</div>"+
    			    "<div class='td2' style='width:170px;text-align:center;font-weight:bold;background-color:#FFFFCC;vertical-align:bottom;border:1px #cccccc solid'>原因</div>"+
    			    "<div class='td2' style='width:100px;text-align:center;font-weight:bold;background-color:#FFFFCC;vertical-align:bottom;border:1px #cccccc solid'></div>"+
    			"</div>"+
    	"</div>";		    
    	
    	List<ClassRoomUnavail> LClassRoomUnavail = admService.getClassRoomUnavail(school_code,classRoomName,"");
    	String dateFrom = "";
    	String dateTo = "";
    	String comment = "";
    	for(int i=seatFromInt;i<=seatToInt;i++) {
			dateFrom = "";
			dateTo = ""; 
			comment = "";
    		for(int x=0;x<LClassRoomUnavail.size();x++) {
    			if(LClassRoomUnavail.get(x).getSeatNo().equals(String.valueOf(i))) {
    				dateFrom = LClassRoomUnavail.get(x).getDateFrom();
    				dateTo = LClassRoomUnavail.get(x).getDateTo();
    				comment = LClassRoomUnavail.get(x).getComment()==null?"":LClassRoomUnavail.get(x).getComment(); 
    			}
    		}
		    returnStr +=
		    "<form action='/Adm/updateSeatDate'>"+
		    "<input type='hidden' name='school_code' value='"+school_code+"'>"+
		    "<input type='hidden' name='classRoomName' value='"+classRoomName+"'>"+
		    "<input type='hidden' name='seatFrom' value='"+seatFrom+"'>"+
		    "<input type='hidden' name='seatTo' value='"+seatTo+"'>"+
		    "<input type='hidden' name='seatNo' value='"+i+"'>"+  
    		 "<div class='css-table'>"+	    		
	    		    "<div class='tr'>"+		
		    		    "<div class='td2' style='width:100px;text-align:center;border:1px #eeeeee solid;font-weight:bold'>"+i+"</div>"+
		    	        "<div class='td2' align='center' style='width:170px;padding:3px;border-bottom:1px #eeeeee solid'><input type='text' name='dateFrom' value='"+dateFrom+"' placeholder='&#128197;' autocomplete='off'  style='width:140px;background-color:white' class='form-control title' onclick='$(this).datepicker({dateFormat:\"yymmdd\",});$(this).datepicker(\"show\")'></div>"+
		    	        "<div class='td2' align='center' style='width:170px;padding:3px;border-bottom:1px #eeeeee solid'><input type='text' name='dateTo' value='"+dateTo+"' placeholder='&#128197;' autocomplete='off'  style='width:140px' class='form-control title' onclick='$(this).datepicker({dateFormat:\"yymmdd\",});$(this).datepicker(\"show\")'></div>"+
		    	        "<div class='td2' align='center' style='width:170px;padding:3px;border-bottom:1px #eeeeee solid'><input type='text' name='comment' value='"+comment+"'  style='width:150px;background-color:white' class='form-control title'></div>"+
		    	        "<div class='td2' style='width:100px;border:1px #eeeeee solid'><input type='submit' value='儲存'></div>"+
		    	    "</div>"+
		     "</div>"+ 	        
	    	 "</form>";    
    	}
    	
    	model.addAttribute("returnStr",returnStr);
    	return "/Adm/editClassroomStyle";
    } 
    

    @RequestMapping("/Adm/updateSeatDate")
    public String updateSeatDate(Model model,HttpServletRequest request,Principal principal){
    	String seatNo = request.getParameter("seatNo");
    	String dateFrom = request.getParameter("dateFrom");
    	String dateTo = request.getParameter("dateTo");
    	String seatFrom = request.getParameter("seatFrom");
    	String seatTo = request.getParameter("seatTo");
    	String school_code = request.getParameter("school_code");
    	String classRoomName = request.getParameter("classRoomName");
    	String comment = request.getParameter("comment");
    	
    	if(school_code!=null && !school_code.isEmpty() && classRoomName!=null && !classRoomName.isEmpty() &&seatNo!=null && !seatNo.isEmpty()) {
    		admService.updateSeatDate(school_code,classRoomName,seatNo,dateFrom,dateTo,principal.getName(),comment);
    	}
    	return "redirect:/Adm/editClassroomStyle?saveSuccessful=1&school_code="+school_code+"&classRoomName="+classRoomName+"&seatFrom="+seatFrom+"&seatTo="+seatTo;
    } 
    

    @RequestMapping("/Adm/studentSeat")
    public String studentSeat(Model model,HttpServletRequest request) {
    	if(request.getParameter("signRecordHistory_seq")!=null) {
    		model.addAttribute("signRecordHistory_seq",request.getParameter("signRecordHistory_seq"));
    	}else if(request.getParameter("mockVideoHistory_seq")!=null) {
    		model.addAttribute("mockVideoHistory_seq",request.getParameter("mockVideoHistory_seq"));
    	}else if(request.getParameter("mockBaseBook_seq")!=null) {
    		model.addAttribute("mockBaseBook_seq",request.getParameter("mockBaseBook_seq"));
    	}else if(request.getParameter("counselingBaseBook_seq")!=null) {
    		model.addAttribute("counselingBaseBook_seq",request.getParameter("counselingBaseBook_seq"));
    	}
    	
    	model.addAttribute("student_no",request.getParameter("student_no"));
    	model.addAttribute("setDate",request.getParameter("setDate"));
    	model.addAttribute("slot",request.getParameter("slot"));
    	model.addAttribute("school_code",request.getParameter("school_code"));
    	model.addAttribute("classRoom",request.getParameter("classRoom"));
    	model.addAttribute("seat",request.getParameter("seat"));
    	
    	//教室
    	String classRoomStr = "<select id='classroom' onchange=getSeats('"+request.getParameter("school_code")+"',this.value,'"+request.getParameter("slot")+"') style='border:0px;width:100px;background-color:#3e7e99'><option value=''></option>";	
    	List<classRoom> LclassRoom = systemService.getclassRoom(request.getParameter("school_code"),"","name","1","");
    	for(int i=0;i<LclassRoom.size();i++) {
    		classRoomStr+="<option value='"+LclassRoom.get(i).getName()+"'>"+LclassRoom.get(i).getName()+"</option>";
    	}
    	classRoomStr+="</select>";
    	model.addAttribute("classRoomStr",classRoomStr);
    	
    	return "/Adm/studentSeat";
    } 
       
    @RequestMapping("/Adm/studentSeatUpdate")
    @ResponseBody
    public Boolean studentSeatUpate(Model model,HttpServletRequest request){
    	String signRecordHistory_seq = request.getParameter("signRecordHistory_seq");
    	String mockVideoHistory_seq = request.getParameter("mockVideoHistory_seq");
    	String mockBaseBook_seq = request.getParameter("mockBaseBook_seq");
    	String counselingBaseBook_seq = request.getParameter("counselingBaseBook_seq");
    	String student_no = request.getParameter("student_no");
    	String setDate = request.getParameter("setDate");
    	String slot = request.getParameter("slot");
    	String school_code = request.getParameter("school_code");
    	String classroom = request.getParameter("classroom");
    	String seat = request.getParameter("seat");

    	admService.studentSeatUpdate(signRecordHistory_seq,mockVideoHistory_seq,mockBaseBook_seq,counselingBaseBook_seq,student_no,setDate,slot,school_code,classroom,seat);  	    
    	return true;	
    }
    
    
    @RequestMapping("/Adm/studentRemark")
    public String studentRemark(Model model,HttpServletRequest request) {
    	if(request.getParameter("signRecordHistory_seq")!=null && !request.getParameter("signRecordHistory_seq").isEmpty()) {
    		model.addAttribute("signRecordHistory_seq",request.getParameter("signRecordHistory_seq"));
    	}else if(request.getParameter("mockVideoHistory_seq")!=null && !request.getParameter("mockVideoHistory_seq").isEmpty()) {
    		model.addAttribute("mockVideoHistory_seq",request.getParameter("mockVideoHistory_seq"));
    	}else if(request.getParameter("mockBaseBook_seq")!=null && !request.getParameter("mockBaseBook_seq").isEmpty()) {
    		model.addAttribute("mockBaseBook_seq",request.getParameter("mockBaseBook_seq"));
    	}else if(request.getParameter("counselingBaseBook_seq")!=null && !request.getParameter("counselingBaseBook_seq").isEmpty()) {
    		model.addAttribute("counselingBaseBook_seq",request.getParameter("counselingBaseBook_seq"));
    	}
    	return "/Adm/studentRemark";
    }
    
       
    @RequestMapping("/Adm/commentUpdate")
    @ResponseBody
    public Boolean commentUpdate(Model model,HttpServletRequest request){
    	String signRecordHistory_seq = request.getParameter("signRecordHistory_seq");
    	String mockVideoHistory_seq = request.getParameter("mockVideoHistory_seq");
    	String mockBaseBook_seq = request.getParameter("mockBaseBook_seq");
    	String counselingBaseBook_seq = request.getParameter("counselingBaseBook_seq");
    	String comment = request.getParameter("comment");

    	admService.commentUpdate(signRecordHistory_seq,mockVideoHistory_seq,mockBaseBook_seq,counselingBaseBook_seq,comment);  	    
    	return true;	
    }  
    
    @RequestMapping("/Adm/onlineSetting")
    public String onlineSetting (Model model,HttpServletRequest request){
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
				
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);    	
   		return "/Adm/onlineSetting";
    } 
    
    
    @RequestMapping("/Adm/editOnlineClass")
    public String editOnlineClass(Model model,HttpServletRequest request) { 
    	String grade_seq = request.getParameter("grade_seq");
		 List<Classes> LClasses= courseService.getClasses(grade_seq,"","","","","","","","","");
		 String returnStr = "";
	     for(int i=0;i<LClasses.size();i++) {
	    	 if(LClasses.get(i).getClass_style().equals("3")) {
	    		 String link="";
	    		 List<OnlineClass> LOnlineClass = admService.getOnlineClass(LClasses.get(i).getClass_seq());
	    		 if(LOnlineClass.size()>0) {link=LOnlineClass.get(0).getLink();}
	    		 returnStr += "<div style='font-weight:bold;margin:10px'>"+
	    	                       "<input type='hidden' name='class_id' value='"+LClasses.get(i).getClass_seq()+"'>"+
	    		 		           "<span style='width:50px;background-color:#ccffff;border:1px #eeeeee solid'>&nbsp;第"+LClasses.get(i).getClass_th()+"堂&nbsp;</span>&nbsp;:&nbsp;<span>URL&nbsp;<input type='text' name='link' value='"+link+"' style='color:blue;border-radius:5px;height:30px;width:700px;border:1px #dddddd solid'></span>"+
	    				     "</div>";
	    	 }	 
	     }
	    	 
	    model.addAttribute("returnStr", returnStr);
    	return "/Adm/editOnlineClass";
    } 
    
    
    @RequestMapping("/Adm/onlineClassUpdate")
    public String onlineClassUpdate(Model model,HttpServletRequest request) { 
    	String[] A_class_id = request.getParameterValues("class_id");
    	String[] A_link = request.getParameterValues("link");
    	
    	for(int i=0;i<A_class_id.length;i++) {
			jdbcTemplate.update("delete from eip.onlineClasses where class_id=?",
					A_class_id[i]
			);		
             
			if(!A_link[i].equals("")) {
				jdbcTemplate.update("INSERT INTO eip.onlineClasses VALUES (default,?,?);",
						A_class_id[i],
						A_link[i]
				);    			
			}
    	}
    	return "/common/closeAndReload";
    }	
}

