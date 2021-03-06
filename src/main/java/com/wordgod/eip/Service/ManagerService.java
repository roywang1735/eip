package com.wordgod.eip.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.wordgod.eip.Model.*;



@Service
public class ManagerService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public boolean applicationLogSave(String updater,String fun1,String fun2,String fun3,String col1,String col2,String col3,String col4,String col5,String col6) {
		jdbcTemplate.update("INSERT INTO eip.applicationLog VALUES (default,NOW(),?,?,?,?,?,?,?,?,?,?)",
				updater,
				fun1, 
				fun2,
				fun3,
				col1,
				col2,
				col3,
				col4,
				col5,
				col6
	     );
		return true;
	}
	
	public List<ApplicationLog> getApplicationLog(String fun1,String fun2,String fun3){
		String sql = "select * from eip.applicationLog where 1=1";
		if(fun1!=null && !fun1.isEmpty()) {sql+=" and fun1="+fun1;}
		if(fun2!=null && !fun2.isEmpty()) {sql+=" and fun2="+fun2;}
		if(fun3!=null && !fun3.isEmpty()) {sql+=" and fun3="+fun3;}
		sql+=" order by update_time desc  limit 150";

	    List<ApplicationLog> LApplicationLog = jdbcTemplate.query(sql,(result,rowNum)->new ApplicationLog(
	    		result.getString("applicationLog_seq"),
                result.getString("update_time"),
                result.getString("updater"),
                result.getString("fun1"),
                result.getString("fun2"),
                result.getString("fun3"),
                result.getString("col1"),
                result.getString("col2"),
                result.getString("col3"),
                result.getString("col4"),
                result.getString("col5"),
                result.getString("col6")
        ));
	    
	    return LApplicationLog;
	}
}
