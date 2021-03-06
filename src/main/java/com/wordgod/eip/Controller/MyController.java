package com.wordgod.eip.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.wordgod.eip.Model.*;
import com.wordgod.eip.Service.AccountService;
import com.wordgod.eip.Service.AdmService;
import com.wordgod.eip.Service.CourseSaleService;
import com.wordgod.eip.Service.CourseService;
import com.wordgod.eip.Service.MarketingService;
import com.wordgod.eip.Service.SalesService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class MyController {

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

	 
		@RequestMapping("/My/MyAccount")
		public String getBooks(Model model,HttpServletRequest request,HttpSession session,Principal principal,@Value("${UploadPath}") String UploadPath){
				String menu = request.getParameter("menu");
				if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
				
			if(principal!=null) {
			Employee employee = accountService.getAccountByID("",principal.getName());
			if(employee!=null) {				
		    	model.addAttribute("employee",employee);
		        List<Department> departmentGroup = accountService.getDepartment("");
		        model.addAttribute("departmentGroup",departmentGroup);
		        
		    	List<School> schoolGroup = accountService.getSchool("","");
		    	model.addAttribute("schoolGroup",schoolGroup); 
		    	
		    	List<Authority> authorityGroup = accountService.getAuthority();
		    	model.addAttribute("authorityGroup", authorityGroup); 
		    	
		    	List<Account_authority> LAccount_authority = accountService.getAccount_authority(employee.getAccount0());
	
		    	ArrayList<String> listAuthority = new ArrayList<String>();
		    	String tmp = null;
		    	for(int i=0;i<authorityGroup.size();i++){
		    		tmp = "";
		    		for(int j=0;j<LAccount_authority.size();j++) {
		    			if(authorityGroup.get(i).getCode().equals(LAccount_authority.get(j).getAuthority())) {tmp="checked";} 
		    		}
		    		listAuthority.add("<input type='checkbox' name='authorityCode' value='"+authorityGroup.get(i).getCode()+"' "+tmp+" disabled> "+authorityGroup.get(i).getName()+"<br>");
		    	}
		    	model.addAttribute("listAuthority", listAuthority);
		    	
	    		String employee_seq = employee.getEmployee_seq();
	    		File dir = new File(UploadPath+"employeePhoto");
	    		File[] files = dir.listFiles((d, name) -> name.startsWith(employee_seq));	    		
	    		if(files.length==0) {
	    			model.addAttribute("fileName","nobody.png");
	    		}else {
	    			model.addAttribute("fileName","employeePhoto/"+files[0].getName());
	    		}  		    	
			}
			 return "/My/MyAccount";
		 }else {
			 return "/System/login";
		 }
		}
		
	    @RequestMapping(value="/My/AccountUpdate2")
	    public String AccountUpdate2(@Valid Employee employee,@RequestParam("file") MultipartFile file,@Value("${UploadPath}") String UploadPath,HttpServletRequest request) {
	    	if(request.getParameter("drowssap")!=null) {
	    		employee.setDrowssap(request.getParameter("drowssap"));
	    	}
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
}