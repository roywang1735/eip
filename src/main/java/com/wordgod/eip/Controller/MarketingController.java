package com.wordgod.eip.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wordgod.eip.Model.*;
import com.wordgod.eip.Service.AccountService;
import com.wordgod.eip.Service.CourseService;
import com.wordgod.eip.Service.MarketingService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.ui.Model;
import java.io.FilenameFilter;
@Controller
public class MarketingController {
    @Autowired
    AccountService accountService;
    @Autowired    
    CourseService courseService;
    @Autowired    
    MarketingService marketingService;    
	
    
    @RequestMapping("/Marketing/Promo")
    public String Promo(Model model,HttpServletRequest request,HttpSession session) {
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}    	
		List<School> schoolGroup = accountService.getSchool("","");
		model.addAttribute("schoolGroup", schoolGroup);		
		
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		
		model.addAttribute("promoType",request.getParameter("promoType"));
		
        return "/Marketing/Promo";
    } 

    @RequestMapping("/Marketing/getClassPromotion")
    @ResponseBody
    public List<ClassPromotion> getClassPromotion(HttpServletRequest request){
        return marketingService.getClassPromotion(
        		request.getParameter("classPromotion_seq"),
        		request.getParameter("school_code"),
        		request.getParameter("enabled"),
        		request.getParameter("promoType"),
        		request.getParameter("active"),
        		request.getParameter("approve"),
        		""
        );
    }

	/*
	 * @RequestMapping("/Marketing/PromoRegularComp")
	 * 
	 * @ResponseBody public String PromoRegularComp(HttpServletRequest request){
	 * String [] id = request.getParameter("id").split(":"); for(int
	 * i=0;i<id.length;i++) { System.out.println("xxxxxx"+id[i]); }
	 * 
	 * return "eeee"; }
	 */ 
    
    @RequestMapping("/Marketing/PromoEdit")
    public String PromoEdit(HttpServletRequest request,Model model,@Value("${UploadPath}") String UploadPath) {
    	
    	ClassPromotion classPromotion = new ClassPromotion();
    	List<PromoApply> LPromoApply = new ArrayList<PromoApply>();
    	String student_seq = request.getParameter("student_seq");

    	String classPromotion_id = request.getParameter("classPromotion_id");
    	String promoType = request.getParameter("promoType");
    	
    	//企劃-現存優惠
    	if(classPromotion_id!=null && !classPromotion_id.isEmpty()) {
    		classPromotion = marketingService.getClassPromotion(classPromotion_id,"","","","","","").get(0);
    		model.addAttribute("classPromotion_id",classPromotion_id);
    		LPromoApply = marketingService.getPromoApply(classPromotion_id);
            //學生報名優惠
    		if(student_seq!=null && !student_seq.isEmpty()) {
    			model.addAttribute("student_seq",student_seq);
    			File dir = new File(UploadPath+promoType+"\\");    		
    	        FilenameFilter filter = new FilenameFilter() {
    	           public boolean accept (File dir, String name) { 
    	               return name.startsWith(student_seq+"_"+classPromotion_id+".");
    	           } 
    	        }; 
    	        String[] children = dir.list(filter);
    	    	if(children!=null && children.length>0) {
    	    		  //學員具有劃位單
    	    	      model.addAttribute("uploadFile","<img style='width:800px' src='/images/"+promoType+"/"+children[0]+"?random="+new Random().nextInt(10000)+"'/>");
    	    	}else {
    	    		  //學員無劃位單
    	    		  model.addAttribute("uploadFile","");
    	    	} 
    		}else{
    			model.addAttribute("student_seq","");
    			model.addAttribute("uploadFile","");
    		}
    	//企劃-新增優惠	
    	}else{
    		model.addAttribute("student_seq","");
    		model.addAttribute("uploadFile","");
    	}
    	
    	List<School> LSchool = accountService.getSchool("","");
    	
    	ArrayList<String> listSchool = new ArrayList<String>();
    	String tmp = null;
    	for(int i=0;i<LSchool.size();i++) {
    		if(classPromotion_id==null && !promoType.equals("lecture")) {
    			tmp="checked";
    		}else {
    			tmp = "";   		
    		  for(int j=0;j<LPromoApply.size();j++) {
    			if(LPromoApply.get(j).getSchool_code().equals(LSchool.get(i).getCode())) {tmp="checked";} 
    		  }
    		}
    		listSchool.add("<input type='checkbox' name='school_code' value='"+LSchool.get(i).getCode()+"' "+tmp+">"+LSchool.get(i).getName()+"&nbsp;&nbsp;");
    	}
    	model.addAttribute("listSchool",listSchool);   	
		model.addAttribute("classPromotion", classPromotion);
		model.addAttribute("promoType", promoType);
		
    	if(promoType.equals("lecture")) {
    		model.addAttribute("promoCategory","講座優惠活動");
    	}else if(promoType.equals("monthly")) {
    		model.addAttribute("promoCategory","月活動");		
    	}else if(promoType.equals("regular")) {
    		model.addAttribute("promoCategory","常態優惠");		
    	}else if(promoType.equals("combine")) {
    		model.addAttribute("promoCategory","合報優惠");
    	}else if(promoType.equals("old")) {
    		model.addAttribute("promoCategory","高中部優惠");
    	}else if(promoType.equals("other")) {
    		model.addAttribute("promoCategory","其他");
    	}
    	
		
    	//劃位單範本
    	List<String> Lfile = new ArrayList<String>();
		File[] files = new File(UploadPath+promoType+"\\").listFiles();
		for (File file : files) {
		    if (file.isFile()) {
		    	if(file.getName().startsWith("template"+classPromotion_id+".")) {
		    		model.addAttribute("fileStr","<A href='javascript:void(0)' style='vertical-align:middle;padding:1px' title='刪除此筆'  onclick='Removefile()'><img src='/images/delete2.png' height='11px'/></A>&nbsp;<A href='#' onclick='openPromoFile()' style='font-weight:bold;color:blue;text-decoration:underline'>"+file.getName()+"</A>");
		    	}
		    }
		}    	
    	
		//講座優惠由講座而來
		if(promoType.equals("lecture")) {
			List<Lecture> LLecture;
			//舊的優惠用到active=0的講座
			if(classPromotion_id!=null && !classPromotion_id.isEmpty()) {
				LLecture = marketingService.getLecture("","","","","","");
			//新增優惠只用到active=1的講座	
			}else {
				LLecture = marketingService.getLecture("","","","1","","");
			}
			String LectureStr = "<select id='promoName' name='promoName' style='padding:5px'><option></option>";
			String tmpStr = "";
			String selStr = "";
			for(int i=0;i<LLecture.size();i++) {
				tmpStr = LLecture.get(i).getLectureDate()+" "+LLecture.get(i).getLectureName()+" ("+LLecture.get(i).getTeacher_name()+"老師)";
				if(tmpStr.equals(classPromotion.getPromoName())) {
					selStr = "selected";
				}else {
					selStr = "";
				}
				LectureStr += "<option value='"+tmpStr+"' "+selStr+">"+tmpStr+"</option>";
			}
			LectureStr += "</select>";
			model.addAttribute("LectureStr",LectureStr);
		}
		
        return "/Marketing/PromoEdit";
    } 
 
    @RequestMapping("/Marketing/PromoSave")
    public String PromoSave(@RequestParam("file") MultipartFile file,HttpServletRequest request,@ModelAttribute ClassPromotion classPromotion,Principal principal,RedirectAttributes redirectAttributes,@Value("${UploadPath}") String UploadPath) {
    	String approve = request.getParameter("approve");
    	String DeleteUploadFile = request.getParameter("DeleteUploadFile");
    	String classPromotion_id = request.getParameter("classPromotion_id");
    	//送出審核 or 審核修改通過
    	if(approve.equals("0") || approve.equals("1")) {
		    	String promoType = request.getParameter("promoType");    			    	
		        if ((!file.isEmpty() || DeleteUploadFile.equals("1")) && classPromotion_id!=null && !classPromotion_id.isEmpty()) {
		  	    	 //先砍掉正檔名相同的
		  	          File dir = new File(UploadPath+promoType+"\\");    		
		  		      FilenameFilter filter = new FilenameFilter() {
		  		         public boolean accept (File dir, String name) { 
		  		            return name.startsWith("template"+classPromotion_id+".");
		  		         } 
		  		      }; 
		  		      String[] children = dir.list(filter);
		  		      for(int i=0;i<children.length;i++) {
		  		    	  File f = new File(dir+"\\"+children[i]);
		  		    	  f.delete();
		  		      }
		        }
		        
		    	String promoName = request.getParameter("promoName");
		    	if(promoName!=null && !promoName.isEmpty()) {
		    		classPromotion.setPromoName(promoName);
		    	}
		    	
		    	//儲存優惠
		    	classPromotion.setUpdater(principal.getName());
		    	String classPromotion_id_new = classPromotion_id;
		    	String[] A_school_code = request.getParameterValues("school_code");
		    	
		    	String classPromotion_id_tmp = marketingService.PromoSave(classPromotion,classPromotion_id,promoType,A_school_code,approve);    	
		    	if(classPromotion_id_tmp!=null && !classPromotion_id_tmp.isEmpty()) {
		    		classPromotion_id_new = classPromotion_id_tmp; //新增優惠的新ID
		    	}
		    	
		    	
		    	//檔案上傳
		        if (!file.isEmpty()) {
		   			  try {
		   				  byte[] bytes = file.getBytes();
		   				  String fileName = file.getOriginalFilename();
		   				  fileName = "template"+classPromotion_id_new+fileName.substring(fileName.indexOf('.'));
		   				  Path path = Paths.get(UploadPath +promoType+"/"+fileName);
		   				  Files.write(path, bytes);
		   			  }catch(Exception e) {
		   			    e.printStackTrace();
		   			}
		        }     		    	
		        return "/common/closeAndReload";
		//簽核        
    	}else if(approve.equals("1") || approve.equals("-1")) {
    		    marketingService.PromoApproveSave(classPromotion_id,approve);    	
    			return "/common/closeAndReload";
    	}else {
    		return "/Marketing/Promo?menu=3&promoType=lecture";
    	}
    } 
    
    @RequestMapping("/Marketing/openPromoFile")
    public String openPromoFile(HttpServletRequest request,Model model,@Value("${UploadPath}") String UploadPath) {
    	String promoType = request.getParameter("promoType");
    	String classPromotion_id = request.getParameter("classPromotion_id");
	          File dir = new File(UploadPath+promoType+"\\");    		
		      FilenameFilter filter = new FilenameFilter() {
		         public boolean accept (File dir, String name) { 
		            return name.startsWith("template"+classPromotion_id+".");
		         } 
		      };
		      String[] children = dir.list(filter);
    	model.addAttribute("promoFile","<img style='width:800px' src='/images/"+promoType+"/"+children[0]+"?"+new Random().nextInt(10000)+"'/>");
    	return "/Marketing/openPromoFile";
    }
    
    @RequestMapping("/Marketing/PromoFileSave")
    public String PromoFileSave(@RequestParam("file") MultipartFile file,HttpServletRequest request,@ModelAttribute ClassPromotion classPromotion,Principal principal,RedirectAttributes redirectAttributes,@Value("${UploadPath}") String UploadPath) { 
    	//檔案上傳
    	String promoType = request.getParameter("promoType");
    	String classPromotion_id = request.getParameter("classPromotion_id");
    	String student_seq = request.getParameter("student_seq");
        if (!file.isEmpty()) {
   	    	 //先砍掉正檔名相同的
   	          File dir = new File(UploadPath+promoType+"\\");    		
   		      FilenameFilter filter = new FilenameFilter() {
   		         public boolean accept (File dir, String name) { 
   		            return name.startsWith(student_seq+"_"+classPromotion_id+".");
   		         } 
   		      }; 
   		      String[] children = dir.list(filter);
   		      for(int i=0;i<children.length;i++) {
   		    	  File f = new File(dir+"\\"+children[i]);
   		    	  
   		    	  f.delete();
   		      }
   	          
   		      //塞入新的
   			  try {
   				  byte[] bytes = file.getBytes();
   				  String fileName = file.getOriginalFilename();
   				  fileName = student_seq+"_"+classPromotion_id+fileName.substring(fileName.indexOf('.'));
   				  Path path = Paths.get(UploadPath +promoType+"/"+fileName);
   				  Files.write(path, bytes);
   			  }catch(Exception e) {
   			    e.printStackTrace();
   			}
        } 
        return "/common/successful_noMenu";
    }
    @RequestMapping("/Marketing/openUpload")
    public String openUpload(Model model,HttpServletRequest request) {
    	model.addAttribute("classPromotion_id",request.getParameter("classPromotion_id"));
        return "/Marketing/openUpload";
    } 
 
/**    
    @RequestMapping("/Marketing/upload") 
    public String upload(@RequestParam("file") MultipartFile file,RedirectAttributes redirectAttributes,Principal principal,HttpServletRequest request) throws IOException {
         if (file.isEmpty()) {
             redirectAttributes.addFlashAttribute("message", "請選擇檔案");
             return "redirect:/Marketing/openUpload";
         }

     try {
    	 String classPromotion_id = request.getParameter("classPromotion_id");
         byte[] bytes = file.getBytes();
         String fileName = file.getOriginalFilename();
         fileName = classPromotion_id+fileName.substring(fileName.indexOf('.'));
         String pathStr = Paths.get("").toAbsolutePath().toString()+"\\src\\main\\resources\\static\\images\\lecture\\";
         Path path = Paths.get(pathStr + fileName);
         Files.write(path, bytes);
         redirectAttributes.addFlashAttribute("message","成功上傳 [" + fileName + "]");

     }catch(Exception e) {
             e.printStackTrace();
             redirectAttributes.addFlashAttribute("message", "上傳失敗"+e);
             return "redirect:/Marketing/openUpload";
     }		
 	    
         return "redirect:/Marketing/openUpload";
   }
**/ 
    
    @RequestMapping("/Marketing/LectureSetting")
    public String LectureSetting(Model model,HttpServletRequest request,HttpSession session) {
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}    	
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup); 
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);		
        return "/Marketing/LectureSetting";
    } 
    
    @RequestMapping("/Marketing/LectureSetting2")
    public String LectureSetting2(Model model,HttpServletRequest request) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int range = 3; //3個月間隔
		String beginYear = String.valueOf(year);
		String beginMonth = String.valueOf(month+1);				
		model.addAttribute("beginYear",beginYear);
		model.addAttribute("beginMonth",beginMonth);
		
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
		
        return "/Marketing/LectureSetting2";
    }  
    
    
    @RequestMapping("/Marketing/ClassTrial")
    public String ClassTrial(Model model,HttpServletRequest request,HttpSession session) {
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}    	
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int range = 3; //3個月間隔
		String beginYear = String.valueOf(year);
		String beginMonth = String.valueOf(month+1);				
		model.addAttribute("beginYear",beginYear);
		model.addAttribute("beginMonth",beginMonth);
		
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
			
        return "/Marketing/ClassTrial";
    }
    
    @RequestMapping("/Marketing/getClassTrial")
    @ResponseBody
    public String getClassTrial(Model model,HttpServletRequest request) {
    	String school_code = request.getParameter("school_code");
    	String beginYear  = request.getParameter("beginYear");
    	String beginMonth = request.getParameter("beginMonth");
    	if(Integer.valueOf(beginMonth)<10) {beginMonth = "0"+beginMonth;}
    	String class_date_like = beginMonth +"%"+ beginYear;
    	List<Classes> Lclasses = courseService.getClasses("","","",school_code,"","","1","",class_date_like,"");
    	String classStr = "";
    	String wk = ""; //星期
    	for(int i=0;i<Lclasses.size();i++) {
    		if(Lclasses.get(i).getClass_date().length()==10) {
    			wk = DateToWeek.getDateToWeek(Lclasses.get(i).getClass_date());
    		}
    		List<classTrialFlow> LclassTrialFlow = marketingService.getClassTrialFlow(Lclasses.get(i).getGrade_id());
    		String beclass = "&hellip;";
    		String art = "&hellip;";
    		String market = "&hellip;";
    		String website = "&hellip;";
    		String comment = "&hellip;";
    		if(LclassTrialFlow.size()>0) {
    			if(LclassTrialFlow.get(0).getBeclass()!=null && !LclassTrialFlow.get(0).getBeclass().isEmpty()) {
    				beclass = LclassTrialFlow.get(0).getBeclass();
    			}
    			if(LclassTrialFlow.get(0).getArt()!=null && !LclassTrialFlow.get(0).getArt().isEmpty()) {
    				art = LclassTrialFlow.get(0).getArt();
    				if(art.equals("1")) {
    					art = "<div style='font-size:medium;color:black'>&#9745;</div>";
    				}else {
    					art = "<div style='font-size:medium;color:red'>&#9746;</div>";
    				}
    			}
    			if(LclassTrialFlow.get(0).getMarket()!=null && !LclassTrialFlow.get(0).getMarket().isEmpty()) {
    				market = LclassTrialFlow.get(0).getMarket();
    				if(market.equals("1")) {
    					market = "<div style='font-size:medium;color:black'>&#9745;</div>";
    				}else {
    					market = "<div style='font-size:medium;color:red'>&#9746;</div>";
    				}
    			}
    			if(LclassTrialFlow.get(0).getWebsite()!=null && !LclassTrialFlow.get(0).getWebsite().isEmpty()) {
    				website = LclassTrialFlow.get(0).getWebsite();
    				if(website.equals("1")) {
    					website = "<div style='font-size:medium;color:black'>&#9745;</div>";
    				}else {
    					website = "<div style='font-size:medium;color:red'>&#9746;</div>";
    				}
    			}
    			if(LclassTrialFlow.get(0).getComment()!=null && !LclassTrialFlow.get(0).getComment().isEmpty()) {
    				comment = LclassTrialFlow.get(0).getComment();
    			}    			
    		}
    		classStr += "<div class='tr' style='font-size:small'>";
    			            classStr += 
    						"<div class='td' style='width:100px;border-bottom:1px #dddddd solid;border-radius:0px;text-align:center;padding:5px'>"+Lclasses.get(i).getClass_date().substring(0,5)+wk+"</div>"+
    						"<div class='td' style='width:100px;border-bottom:1px #dddddd solid;border-radius:0px;text-align:center'>"+Lclasses.get(i).getTime_from()+"-"+Lclasses.get(i).getTime_to()+"</div>"+
    						"<div class='td' style='width:200px;border-bottom:1px #dddddd solid;border-radius:0px;text-align:left;font-weight:bold'>"+Lclasses.get(i).getSubject_name()+"</div>"+
    						"<div class='td' style='width:130px;border-bottom:1px #dddddd solid;border-radius:0px;text-align:center'>"+Lclasses.get(i).getTeacher_name()+"</div>"+
    						"<div class='td' style='width:170px;border-bottom:1px #dddddd solid;border-radius:0px;text-align:left'><A href='javascript:void(0)' onclick=ClassTrialEdit("+Lclasses.get(i).getGrade_id()+",\'beclass\') style='text-decoration:underline;color:blue'>"+beclass+"</A></div>"+
    						"<div class='td' style='width:150px;border-bottom:1px #dddddd solid;border-radius:0px;text-align:center'><A href='javascript:void(0)' onclick=ClassTrialEdit("+Lclasses.get(i).getGrade_id()+",\'art\') style='text-decoration:underline;color:blue'>"+art+"</A></div>"+
    						"<div class='td' style='width:150px;border-bottom:1px #dddddd solid;border-radius:0px;text-align:center'><A href='javascript:void(0)' onclick=ClassTrialEdit("+Lclasses.get(i).getGrade_id()+",\'market\') style='text-decoration:underline;color:blue'>"+market+"</A></div>"+
    						"<div class='td' style='width:150px;border-bottom:1px #dddddd solid;border-radius:0px;text-align:center'><A href='javascript:void(0)' onclick=ClassTrialEdit("+Lclasses.get(i).getGrade_id()+",\'website\') style='text-decoration:underline;color:blue'>"+website+"</A></div>"+
    						"<div class='td' style='width:150px;border-bottom:1px #dddddd solid;border-radius:0px;text-align:left'><A href='javascript:void(0)' onclick=ClassTrialEdit("+Lclasses.get(i).getGrade_id()+",\'comment\') style='text-decoration:underline;color:blue'>"+comment+"</A></div>";
    		classStr += "</div>";
    	}
    	
        return classStr;
    }    
    
    @RequestMapping("/Marketing/LectureCalendar")
    public String LectureCalendar(Model model,HttpServletRequest request) {
		model.addAttribute("page",request.getParameter("page"));
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
				
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);

		List<Teacher> teacherGroup = courseService.getTeacher("","","","","1","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int range = 2; //3個月間隔
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
        return "/Marketing/LectureCalendar";
    }
    
    @RequestMapping("/Marketing/LectureEdit")
    public String LectureEdit(Model model,HttpServletRequest request) {
    	
		List<School> LSchool = accountService.getSchool("","");
		model.addAttribute("LSchool", LSchool);	

		List<Category> LCategory = courseService.getCategory("","");
		model.addAttribute("LCategory", LCategory);
		
		List<Teacher> LTeacher = courseService.getTeacher("","","","","1","","");
		model.addAttribute("LTeacher", LTeacher);
		
		Lecture lecture = new Lecture();
		String lecture_id = request.getParameter("lecture_id");
		if(lecture_id!=null) {
			lecture = marketingService.getLecture(lecture_id,"","","","","").get(0);			
		}
		model.addAttribute("lecture", lecture);
        return "/Marketing/LectureEdit";
    }
    
    @RequestMapping("/Marketing/getLectureList")
    @ResponseBody
    public List<Lecture> getLectureList(Model model,HttpServletRequest request) {
        String category_id = request.getParameter("category_id");
        String teacher_id  = request.getParameter("teacher_id");
        String active  = request.getParameter("active");
    	List<Lecture> LLecture = marketingService.getLecture("",category_id,teacher_id,active,"","");

        return LLecture;
    }      
    
    @RequestMapping("/Marketing/LectureEditSave")
    public String LectureEditSave(HttpServletRequest request,@ModelAttribute Lecture lecture,Principal principal,RedirectAttributes redirectAttributes) {

    	lecture.setCreater(principal.getName());
    	marketingService.LectureEditSave(lecture,request.getParameter("lecture_seq"));
    	return "redirect:/Marketing/LectureSetting";
    }	
 
    
    @RequestMapping("/Marketing/TeacherSchedule")
    public String TeacherSchedule(Model model,HttpServletRequest request,HttpSession session) {
		List<Teacher> LTeacher = courseService.getTeacher("","","","","1","","");
		model.addAttribute("LTeacher", LTeacher); 
		String teacher_seq = "-1";
		if(request.getParameter("teacher_seq")!=null) {
			teacher_seq = request.getParameter("teacher_seq");
			model.addAttribute("teacher_id", teacher_seq); 
		}	
        return "/Marketing/TeacherSchedule";
    } 
        
    @RequestMapping("/Marketing/DeleteLecture")
    public String DeleteLecture(Model model,HttpServletRequest request,Principal principal) {
    	String lecture_seq = request.getParameter("lecture_seq");
    	courseService.DeleteLecture(lecture_seq,principal.getName());   	
    	return "redirect:/Marketing/LectureSetting";
    }
    
    @RequestMapping("/Marketing/DeletePromo")
    public String DeletePromo(Model model,HttpServletRequest request,Principal principal) {
    	String classPromotion_id = request.getParameter("classPromotion_id");
    	String promoType = request.getParameter("promoType");
    	courseService.DeletePromo(classPromotion_id,principal.getName());   	
    	return "redirect:/Marketing/Promo?promoType="+promoType;
    } 
    
    @RequestMapping("/Marketing/getLectureGrade")
    @ResponseBody  
    public String getLectureGrade(HttpServletRequest request,Model model){
    	String school_id = request.getParameter("school_id");
    	String beginYear  = request.getParameter("beginYear");
    	String beginMonth = request.getParameter("beginMonth");
    	if(Integer.valueOf(beginMonth)<10) {beginMonth = "0"+beginMonth;}
    	String lectureDate_like = beginMonth +"%"+ beginYear;    	
    	List<Lecture> LLecture = marketingService.getLecture("","","","",lectureDate_like,school_id); 
    	
    	String lectureStr = "";
    	String wk = ""; //星期
    	for(int i=0;i<LLecture.size();i++) {
    		if(LLecture.get(i).getLectureDate().length()==10) {
    			wk = DateToWeek.getDateToWeek(LLecture.get(i).getLectureDate());
    		}
    		String beclass = "&hellip;";
    		String comment = "&hellip;";
    		String website = "&hellip;";
    		String art = "&hellip;";
   		
    		List<LectureFlow> LLectureFlow = marketingService.getLectureFlow(LLecture.get(i).getLecture_seq());
    		if(LLectureFlow.size()>0) {
    			if(LLectureFlow.get(0).getBeclass()!=null && !LLectureFlow.get(0).getBeclass().isEmpty()) {
    				beclass = LLectureFlow.get(0).getBeclass();
    			}
    			if(LLectureFlow.get(0).getArt()!=null && !LLectureFlow.get(0).getArt().isEmpty()) {
    				art = LLectureFlow.get(0).getArt();
    				if(art.equals("1")) {
    					art = "<div style='font-size:medium;color:black'>&#9745;</div>";
    				}else {
    					art = "<div style='font-size:medium;color:red'>&#9746;</div>";
    				}
    			}
    			if(LLectureFlow.get(0).getWebsite()!=null && !LLectureFlow.get(0).getWebsite().isEmpty()) {
    				website = LLectureFlow.get(0).getWebsite();
    				if(website.equals("1")) {
    					website = "<div style='font-size:medium;color:black'>&#9745;</div>";
    				}else {
    					website = "<div style='font-size:medium;color:red'>&#9746;</div>";
    				}
    			}
    			if(LLectureFlow.get(0).getComment()!=null && !LLectureFlow.get(0).getComment().isEmpty()) {
    				comment = LLectureFlow.get(0).getComment();
    			}    			
    		}
    		lectureStr += "<div class='tr' style='font-size:small'>";
    						lectureStr += 
    						"<div class='td' style='width:80px;border-bottom:1px #dddddd solid;border-radius:0px;text-align:center;padding:5px'>"+LLecture.get(i).getLectureDate().substring(0,5)+wk+"</div>"+
    						"<div class='td' style='width:100px;border-bottom:1px #dddddd solid;border-radius:0px;text-align:center'>"+LLecture.get(i).getLectureTimeFrom()+"-"+LLecture.get(i).getLectureTimeTo()+"</div>"+
    						"<div class='td' style='width:200px;border-bottom:1px #dddddd solid;border-radius:0px;text-align:left;font-weight:bold'>"+LLecture.get(i).getLectureName()+"</div>"+
    						"<div class='td' style='width:130px;border-bottom:1px #dddddd solid;border-radius:0px;text-align:center'>"+LLecture.get(i).getTeacher_name()+"</div>"+
    						"<div class='td' style='width:150px;border-bottom:1px #dddddd solid;border-radius:0px;text-align:center'><A href='javascript:void(0)' onclick=LectureFlowEdit("+LLecture.get(i).getLecture_seq()+",\'art\') style='text-decoration:underline;color:blue'>"+art+"</A></div>"+    						
    						"<div class='td' style='width:170px;border-bottom:1px #dddddd solid;border-radius:0px;text-align:left'><A href='javascript:void(0)' onclick=LectureFlowEdit("+LLecture.get(i).getLecture_seq()+",\'beclass\') style='text-decoration:underline;color:blue'>"+beclass+"</A></div>"+
    						"<div class='td' style='width:150px;border-bottom:1px #dddddd solid;border-radius:0px;text-align:center'><A href='javascript:void(0)' onclick=LectureFlowEdit("+LLecture.get(i).getLecture_seq()+",\'website\') style='text-decoration:underline;color:blue'>"+website+"</A></div>"+
    						"<div class='td' style='width:150px;border-bottom:1px #dddddd solid;border-radius:0px;text-align:left'><A href='javascript:void(0)' onclick=LectureFlowEdit("+LLecture.get(i).getLecture_seq()+",\'comment\') style='text-decoration:underline;color:blue'>"+comment+"</A></div>";
    			            lectureStr += "</div>";
    	}
    	
        return lectureStr;    	
    } 
    
    @RequestMapping("/Marketing/ClassTrialEdit")
    public String ClassTrialEdit(Model model,HttpServletRequest request) {
    	String grade_id = request.getParameter("grade_id"); 
    	model.addAttribute("grade_id",grade_id);
    	
    	String functions = request.getParameter("functions"); 
    	model.addAttribute("functions",functions);
    	
    	classTrialFlow cTF = new classTrialFlow();
    	List<classTrialFlow> LclassTrialFlow = marketingService.getClassTrialFlow(grade_id);
    	if(LclassTrialFlow.size()>0) {
    		cTF = LclassTrialFlow.get(0);
    	}
    	
    	model.addAttribute("classTrialFlow",cTF);
    	
    	return "/Marketing/ClassTrialEdit";
    }
    
    @RequestMapping("/Marketing/LectureFlowEdit")
    public String LectureFlowEdit(Model model,HttpServletRequest request) {
    	String lecture_id = request.getParameter("lecture_id"); 
    	model.addAttribute("lecture_id",lecture_id);
    	
    	String functions = request.getParameter("functions"); 
    	model.addAttribute("functions",functions);
    	
    	LectureFlow LF = new LectureFlow();
    	List<LectureFlow> LLectureFlow = marketingService.getLectureFlow(lecture_id);
    	if(LLectureFlow.size()>0) {
    		LF = LLectureFlow.get(0);
    	}
    	
    	model.addAttribute("LectureFlow",LF);
    	
    	return "/Marketing/LectureFlowEdit";
    }    
    
    @RequestMapping("/Marketing/ClassTrialEditSave")
    public String ClassTrialEditSave(Model model,HttpServletRequest request,@ModelAttribute classTrialFlow cTF,Principal principal) {
    	String grade_id = request.getParameter("grade_id"); 
    	String functions = request.getParameter("functions");

    	String classTrialFlow_seq = cTF.getClassTrialFlow_seq();
        if(classTrialFlow_seq!=null && !classTrialFlow_seq.isEmpty()) {
        	marketingService.updateClassTrial(cTF,functions,principal.getName());
        }else {       	
        	marketingService.insertClassTrial(cTF,grade_id,functions,principal.getName());
        }

    	return "/common/closeAndReload";
    } 
    
       
    @RequestMapping("/Marketing/LectureFlowEditSave")
    public String LectureFlowEditSave(Model model,HttpServletRequest request,@ModelAttribute LectureFlow LF,Principal principal) {
    	String lecture_id = request.getParameter("lecture_id"); 
    	String functions = request.getParameter("functions");

    	String lectureFlow_seq = LF.getLectureFlow_seq();
        if(lectureFlow_seq!=null && !lectureFlow_seq.isEmpty()) {
        	marketingService.updateLectureFlow(LF,functions,principal.getName());
        }else {       	
        	marketingService.insertLectureFlow(LF,lecture_id,functions,principal.getName());
        }

    	return "/common/closeAndReload";
    } 
    
	@RequestMapping("/Marketing/Course")
    public String Course(Model model,Principal principal) {
		
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
			
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		List<FlowStatus> flowStatusGroup = courseService.getFlowStatus();
		model.addAttribute("flowStatusGroup", flowStatusGroup);	
		
		String school_code = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		model.addAttribute("school_code",school_code);			
	    	
        return "/Marketing/Course";
    } 
	
	@RequestMapping("/Marketing/SetRanking")
	@ResponseBody 
    public String SetRanking(Model model,HttpServletRequest request) {
		String classPromotion_seq = request.getParameter("classPromotion_seq");
		String ranking = request.getParameter("ranking");
		if(classPromotion_seq!=null && !classPromotion_seq.isEmpty() && ranking!=null && !ranking.isEmpty()) {
			marketingService.SetRanking(classPromotion_seq,ranking);
		}	
		return "true";
	}
    
}
