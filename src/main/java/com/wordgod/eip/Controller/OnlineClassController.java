package com.wordgod.eip.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wordgod.eip.Model.Grade;
import com.wordgod.eip.Service.CourseService;



@RestController
public class OnlineClassController {
	@Autowired
	CourseService courseService;
	
	
    @RequestMapping(value = "/OnlineClass/listClass/",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)  
    
    public ResponseEntity<List<Grade>> listClass() { 
    	String subject_id = "";
    	String school_code = "";
    	String class_start_date_0 = "";
    	String teacher_id = "";
    	String category_id = "";
    	List<Grade> LGrade = courseService.getGrade("","",subject_id,school_code,class_start_date_0,category_id,teacher_id,"","","","","0","1");  
        if(LGrade.isEmpty()){  
            return new ResponseEntity<List<Grade>>(new ArrayList<Grade>(),HttpStatus.OK); 
        }  
        return new ResponseEntity<List<Grade>>(LGrade, HttpStatus.OK);  
    }   
 
}
