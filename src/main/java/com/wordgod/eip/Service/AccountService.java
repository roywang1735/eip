package com.wordgod.eip.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wordgod.eip.Model.*;



@Service
public class AccountService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private PasswordEncoder passwordEncoder;	
	
	public List<Employee> getEmployee(String ch_name, String en_name, String school, String tel, String email, String department,String account0,String enabled){ 
		String sql = 
				 "select a.fb,a.line,a.employee_seq,a.account0,a.account,a.ch_name,a.en_name, b.name as department, a.tel, a.email,a.school,c.code as school_code,c.name as schoolName,a.enabled "+
				 " from eip.employee a, eip.department b, eip.school c"+
				 " where a.department = b.id and a.school = c.code";
		if(ch_name!=null && !ch_name.isEmpty()) {sql+=" and a.ch_name like '%"+ch_name+"%'";}
		if(en_name!=null && !en_name.isEmpty()) {sql+=" and a.en_name like '%"+en_name+"%'";}
		if(school!=null && !school.isEmpty()) {sql+=" and a.school = '"+school+"'";}
		if(tel!=null && !tel.isEmpty()) {sql+=" and a.tel = '"+tel+"'";}
		if(email!=null && !email.isEmpty()) {sql+=" and a.email = '"+email+"'";}
		if(department!=null && !department.isEmpty()) {sql+=" and a.department = '"+department+"'";}
		if(account0!=null && !account0.isEmpty()) {sql+=" and a.account0 = '"+account0+"'";}
		if(enabled!=null && !enabled.isEmpty()) {sql+=" and a.enabled = "+enabled;}

		List<Employee> items = jdbcTemplate.query(sql,(result,rowNum)->new Employee(
	    		result.getString("employee_seq"),
	    		result.getString("account0"),
	    		result.getString("account"),
                "",
	    		result.getString("ch_name"),
                result.getString("en_name"),
                null,
                "",
                result.getString("department"),
                result.getString("tel"),
                result.getString("email"),
                result.getString("school"),
                result.getString("school_code"),
                result.getString("schoolName"),
                "",
                result.getString("enabled"),
                result.getString("fb"),
                result.getString("line")
            ));
	    return items;
	}
	
	public Employee getAccountByID(String employee_seq,String account0) {	
			String sql = "select a.fb,a.line,a.employee_seq,a.account0,a.account,a.drowssap,a.ch_name,a.en_name, b.id as department, a.tel, a.email,c.code as school_code,c.name as schoolName,a.creater,a.enabled "+
					 " from eip.employee a, eip.department b, eip.school c"+
					 " where a.department = b.id and a.school = c.code";
			if(employee_seq!=null && !employee_seq.isEmpty()) {sql+=" and a.employee_seq = "+employee_seq;}
			if(account0!=null && !account0.isEmpty()) {sql+=" and a.account0 = '"+account0+"'";}
		
		    List<Employee> items = jdbcTemplate.query(sql,(result,rowNum)->new Employee(
		    		result.getString("employee_seq"),
		    		result.getString("account0"),
		    		result.getString("account"),
		    		result.getString("drowssap"),
		    		result.getString("ch_name"),
	                result.getString("en_name"),
	                null,
	                "",
	                result.getString("department"),
	                result.getString("tel"),
	                result.getString("email"),
	                "",
	                result.getString("school_code"),
	                result.getString("schoolName"),
	                result.getString("creater"),
	                result.getString("enabled"),
	                result.getString("fb"),
	                result.getString("line")
	              ));
		    return items.get(0);	 
	}
	
	public boolean saveAccount(Employee employee) {
		
		jdbcTemplate.update("INSERT INTO eip.Employee VALUES (default,?,?,?,?,?,?,?,?,?,?,?,CURDATE(),true,?,?,?)",
				    "", 
				    employee.getAccount0(),
				    "",
				    //employee.getDrowssap(),
				    passwordEncoder.encode(employee.getDrowssap()),
				    employee.getCh_name(),
				    employee.getEn_name(),
				    employee.getDepartment(),
				    employee.getTel(),
				    employee.getEmail(),
				    employee.getSchool(),
				    employee.getCreater(),
				    "",
				    "",
				    ""
				    );
		
		List<Account_authority> LAccount_authority = employee.getLAccount_authority();
		for(int i=0;i<LAccount_authority.size();i++) {
			jdbcTemplate.update("INSERT INTO eip.account_authority VALUES (?,?)",
					LAccount_authority.get(i).getAccount0(), 
					LAccount_authority.get(i).getAuthority()
			);		
		}
		return true;
	}
	
	public boolean updateAccount(Employee employee) {
		if(employee.getDrowssap().equals("**************")) {
			jdbcTemplate.update("update eip.Employee set account0=?,ch_name=?,en_name=?,department=?,tel=?,email=?,school=?,creater=?,fb=?,line=?,submit_date=CURDATE() where employee_seq="+employee.getEmployee_seq(),
				    employee.getAccount0(),
				    employee.getCh_name(),
				    employee.getEn_name(),
				    employee.getDepartment(),
				    employee.getTel(),
				    employee.getEmail(),
				    employee.getSchool_code(),
				    employee.getCreater(),
				    employee.getFb(),
				    employee.getLine()
			);
		}else {
			jdbcTemplate.update("update eip.Employee set account0=?,drowssap=?,ch_name=?,en_name=?,department=?,tel=?,email=?,school=?,creater=?,fb=?,line=?,submit_date=CURDATE() where employee_seq="+employee.getEmployee_seq(),
				    employee.getAccount0(),
				    passwordEncoder.encode(employee.getDrowssap()),
				    employee.getCh_name(),
				    employee.getEn_name(),
				    employee.getDepartment(),
				    employee.getTel(),
				    employee.getEmail(),
				    employee.getSchool_code(),
				    employee.getCreater(),
				    employee.getFb(),
				    employee.getLine()
			);			
		}
		
			
		List<Account_authority> LAccount_authority = employee.getLAccount_authority();
		if(LAccount_authority!=null) {
			jdbcTemplate.update("delete from eip.account_authority where account0='"+employee.getAccount0()+"'");
			for(int i=0;i<LAccount_authority.size();i++) {
				jdbcTemplate.update("INSERT INTO eip.account_authority VALUES (?,?)",
						LAccount_authority.get(i).getAccount0(), 
						LAccount_authority.get(i).getAuthority()
				);		
			}
		}	
		return true;
	}
	
	public boolean updateAccount2(Employee employee) {
		jdbcTemplate.update("update eip.Employee set drowssap=?,en_name=?,tel=?,email=?,creater=?,fb=?,line=?,submit_date=CURDATE() where employee_seq="+employee.getEmployee_seq(),
				    //employee.getDrowssap(),
				    passwordEncoder.encode(employee.getDrowssap()),
				    employee.getEn_name(),
				    employee.getTel(),
				    employee.getEmail(),
				    employee.getCreater(),
				    employee.getFb(),
				    employee.getLine()
		);

		return true;
	}	
	
	public boolean disableAccount(String employee_seq,String creater) {
		jdbcTemplate.update("update eip.Employee set enabled=0,creater='"+creater+"',submit_date=CURDATE() where employee_seq="+employee_seq);
		return true;
	}	
	
	public List<Department> getDepartment(String id){
		String sql = "select * from eip.department where 1=1";
		if(id!=null && !id.isEmpty()) {sql+=" and id='"+id+"'";}
	    List<Department> items = jdbcTemplate.query(sql,(result,rowNum)->new Department(
	    		result.getString("id"),
                result.getString("name")
                ));
	    return items;
	}
	
	public List<School> getSchool(String code,String school_seq){
		String sql = "select * from eip.school where 1=1";
		if(code!=null && !code.isEmpty()) {sql+=" and code='"+code+"'";}
		if(school_seq!=null && !school_seq.isEmpty()) {sql+=" and school_seq="+school_seq;}
		
	    List<School> items = jdbcTemplate.query(sql,(result,rowNum)->new School(
	    		result.getString("school_seq"),
	    		result.getString("code"),
                result.getString("name")
                ));
	    return items;
	}	
	
	public List<Authority> getAuthority(){
		String sql = "select * from eip.authority order by name";
	    List<Authority> items = jdbcTemplate.query(sql,(result,rowNum)->new Authority(
	    		result.getString("code"),
                result.getString("name")
        ));
	    
	    return items;
	}
	
	public List<Account_authority> getAccount_authority(String account0){
		String sql = "select a.*,b.name as authority_name from eip.account_authority a,eip.authority b where a.authority=b.code and a.account0='"+account0+"'";
		List<Account_authority> items = jdbcTemplate.query(sql,(result,rowNum)->new Account_authority(
	    		result.getString("account0"),
                result.getString("authority"),
                result.getString("authority_name")
        ));
	    
	    return items;		
	}
}
