package com.wordgod.eip.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.wordgod.eip.Model.*;
import com.wordgod.eip.Service.AccountService;
import com.wordgod.eip.Service.AdmService;
import com.wordgod.eip.Service.CourseSaleService;
import com.wordgod.eip.Service.CourseService;
import com.wordgod.eip.Service.MarketingService;
import com.wordgod.eip.Service.SalesService;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class OtherController {

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
	private JdbcTemplate jdbcTemplate;
	 
		 @RequestMapping("/Other/bookSetting")
		 public String bookSetting(Model model,HttpServletRequest request,HttpSession session) {
				String menu = request.getParameter("menu");
				if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
				
				List<Category> categoryGroup = courseService.getCategory("","");
				model.addAttribute("categoryGroup", categoryGroup);
		     return "/Other/bookSetting";
		 }
	
		 @RequestMapping("/Other/bookSettingEdit")
		 public String bookSettingEdit(Model model,HttpServletRequest request) {
				List<Category> categoryGroup = courseService.getCategory("","");
				model.addAttribute("categoryGroup",categoryGroup);	
				String books_seq = request.getParameter("books_seq");
				Books book = new Books();
				if(books_seq!=null && !books_seq.isEmpty()) {
					book = admService.getBooks("",books_seq,"").get(0);
					model.addAttribute("pop","1");
				}
				model.addAttribute("book",book);
		     return "/Other/bookSettingEdit";
		 }	 
	 

		@RequestMapping("/Other/otherLagnappeEdit")
		public String otherLagnappeEdit(HttpServletRequest request,Model model) {
			 String lagnappe_seq = request.getParameter("lagnappe_seq");
			 Lagnappe lagnappe = new Lagnappe();
			 if(lagnappe_seq!=null && !lagnappe_seq.isEmpty()) {
				 List<Lagnappe> Llagnappe = courseSaleService.getLagnappe(lagnappe_seq,"");
				 if(Llagnappe.size()>0) {
					 lagnappe = Llagnappe.get(0);
				 }
			 }
			 model.addAttribute("lagnappe", lagnappe);
		     return "/Other/otherLagnappeEdit";
		}		 
	 
		@RequestMapping("/Other/getBooks")
		@ResponseBody
		public List<Books> getBooks(Model model,HttpServletRequest request){
			 String category_id = request.getParameter("category_id");
			 String active = request.getParameter("active");
			 List<Books> LBooks = admService.getBooks(category_id,"",active);
			 return LBooks;
		}
		
		@RequestMapping("/Other/getBooksOption")
		@ResponseBody
		public String getBooksOption(HttpServletRequest request){
			String returnStr = ""; 
			String category_id = request.getParameter("category_id");
			 List<Books> LBooks = admService.getBooks(category_id,"","");
			 for(int i=0;i<LBooks.size();i++) {
				 returnStr +="<option value='"+LBooks.get(i).getBooks_seq()+"'>"+LBooks.get(i).getBookName()+"&nbsp;(售價"+LBooks.get(i).getSellPrice()+"元)</option>";
			 }			 
			 return returnStr;
		}		
		
		
		 @RequestMapping("/Other/bookSell")
		 public String bookSell(Model model,HttpServletRequest request,HttpSession session) {
				String menu = request.getParameter("menu");
				if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
				
				List<Category> categoryGroup = courseService.getCategory("","");
				model.addAttribute("categoryGroup", categoryGroup);
		     return "/Other/bookSell";
		 }
		 
		 @RequestMapping("/Other/bookSellEdit")
		 public String bookSellEdit(Model model,HttpServletRequest request) {
				List<Category> categoryGroup = courseService.getCategory("","");
				model.addAttribute("categoryGroup", categoryGroup);			 
		     return "/Other/bookSellEdit";
		 }		 
		 
		 @RequestMapping("/Other/reviewStampSell")
		 public String reviewStampSell(Model model,HttpServletRequest request) {
		     return "/Other/reviewStampSell";
		 }
		 
		 @RequestMapping("/Other/reviewStampSellEdit")
		 public String reviewStampSellEdit(Model model,HttpServletRequest request) {	
		     return "/Other/reviewStampSellEdit";
		 }			 
		 
		 @RequestMapping("/Other/dmSetting")
		 public String dmSetting(Model model,HttpServletRequest request) {
		     return "/Other/dmSetting";
		 }
		 
		 @RequestMapping("/Other/otherLagnappe")
		 public String otherLagnappe(Model model,HttpServletRequest request) {
		     return "/Other/otherLagnappe";
		 }		 
		 
		 @RequestMapping("/Other/reviewStampSetting")
		 public String reviewStampSetting(Model model,HttpServletRequest request) {
		     return "/Other/reviewStampSetting";
		 }	
		 
		 @RequestMapping("/Other/getStudentName")
		 @ResponseBody
		 public String getStudentName(Model model,HttpServletRequest request) {
			 String returnStr = "";
			 List<Student> LStudent = salesService.getStudent("","","",request.getParameter("student_no"),"","","","","","");
			 if(LStudent.size()==0) {
				 returnStr = "無此學員";
			 }else {
				 returnStr = LStudent.get(0).getCh_name();
			 }
		     return returnStr;
		 }			 
	 
	    @RequestMapping("/Other/bookSettingEditSave")
	    public String bookSettingEditSave(HttpServletRequest request,@ModelAttribute Books book,Principal principal) {
	    	admService.bookSettingEditSave(book,principal.getName());
	    		return "/common/closeAndReload"; 
	    }	

		 @RequestMapping("/Other/getOtherSell")
		 @ResponseBody
		 public List<OtherSell> getOtherSell(HttpServletRequest request) {
			 List<OtherSell> LOtherSell = admService.getOtherSell(request.getParameter("cat1"));	
		     return LOtherSell;
		 }
		 
	    @RequestMapping("/Other/bookSellEditSave")
	    public String bookSellEditSave(HttpServletRequest request,Principal principal) {
	    	String cat1 = request.getParameter("cat1");
	    	String returnStr ="";
	    	OtherSell otherSell = new OtherSell(
            		"",
            		cat1,
            		request.getParameter("sell_id"),
            		"",
            		request.getParameter("amount"),
            		request.getParameter("sellPrice"),
            		request.getParameter("studentNo"),
            		request.getParameter("studentName"),
            		principal.getName(),
            		"",
            		request.getParameter("comment")
            );
	    	admService.bookSellEditSave(otherSell);
	    	
	    	
	    	if(cat1.equals("2")) { //Video點數,需更改student.[makeUpTotal]
	    		Student student = salesService.getStudent("","","",request.getParameter("studentNo"),"","","","","","").get(0);
	    		salesService.updateMakeUpTotal(
	    				request.getParameter("studentNo"),
	    				Integer.valueOf(student.getMakeUpTotal())+Integer.valueOf(request.getParameter("amount")),
	    				request.getParameter("amount"),
	    				student.getStudent_seq(),
	    				principal.getName(),
	    				"2", //2:購買新增 
	    				request.getParameter("comment"), //remark
	    				"付款 : "+request.getParameter("sellPrice"), //content
	    				""
	    		);
	    		
	    		returnStr = "redirect:/Other/reviewStampSell";
	    	}else if(cat1.equals("1")) {
	    		returnStr = "redirect:/Other/bookSell";
	    	}
	    	return returnStr;
	    }
	    
		@RequestMapping("/Other/getOtherLagnappe")
		@ResponseBody
		public List<Lagnappe> getOtherLagnappe(Model model,HttpServletRequest request) {
			String active = request.getParameter("active");
			List<Lagnappe> LLagnappe = courseSaleService.getLagnappe("",active);			
			return LLagnappe;
		}
		
	    @RequestMapping("/Other/otherLagnappeEditSave")
	    public String otherLagnappeEditSave(HttpServletRequest request,@ModelAttribute Lagnappe lagnappe,Principal principal) {
	    	courseSaleService.otherLagnappeEditSave(lagnappe,principal.getName());
	    	return "/common/closeAndReload"; 
	    }
	   
	    @RequestMapping("/Other/LagnappeDelete")
	    public String LagnappeDelete(HttpServletRequest request,Principal principal) {
	    	String lagnappe_seq_del = request.getParameter("lagnappe_seq_del");
	    	if(lagnappe_seq_del!=null && !lagnappe_seq_del.isEmpty()) {
				jdbcTemplate.update("update eip.lagnappe set del_item=?,updater=?,update_time=NOW() where lagnappe_seq=?;",
						1,
						principal.getName(),
						lagnappe_seq_del
				);		
	    	}

	    	return "/common/closeAndReload"; 
	    }
	    
	    @RequestMapping("/Other/BookDelete")
	    public String BookDelete(HttpServletRequest request,Principal principal) {
	    	String book_seq_del = request.getParameter("book_seq_del");
	    	if(book_seq_del!=null && !book_seq_del.isEmpty()) {
				jdbcTemplate.update("update eip.books set del_item=?,updater=?,update_time=NOW() where books_seq=?;",
						1,
						principal.getName(),
						book_seq_del
				);		
	    	}

	    	return "/common/closeAndReload"; 
	    }	    
	    
}