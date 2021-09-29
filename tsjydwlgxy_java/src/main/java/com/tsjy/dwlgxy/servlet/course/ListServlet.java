package com.tsjy.dwlgxy.servlet.course;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import com.tsjy.dwlgxy.bean.*;
import com.tsjy.dwlgxy.common.utils.*;
import com.tsjy.dwlgxy.common.conf.*;
import com.tsjy.dwlgxy.common.exception.*;
import com.tsjy.dwlgxy.common.BaseServlet;
import com.tsjy.dwlgxy.service.*;



@WebServlet(urlPatterns = "/course/list")
public class ListServlet extends BaseServlet 
{
   
	@SuppressWarnings("unchecked")
	@Override
	protected String process(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException
	{
		
		
		  try
		  {
			  
			//#
			int module_id   = StringUtil.getInt(request.getParameter("module_id"), 0)  ;
		    int type_id   = StringUtil.getInt(request.getParameter("type_id"), 0)  ;
		    String name   = StringUtil.getString(request.getParameter("name"), "")  ;
	        int pageNo   = StringUtil.getInt(request.getParameter("pageNo"), 1);
	        int pageSize = StringUtil.getInt(request.getParameter("pageSize"), 10);
	        
	        
	        // #
		    StringBuilder sb = new StringBuilder();
		    sb.append("1=1");
		    if( module_id != 0 ) sb.append(" and module_id=").append(module_id);
		    if( type_id != 0 ) sb.append(" and type_id=").append(type_id);
		    if( StringUtil.valid(name) ) 
		    {
		    	sb.append(" and name like '%").append(name).append("%'");
		    }
	    	sb.append(" and is_delete=0");
	    	String where = sb.toString();
	       
			
			
			//#
	        List<Course>  list  = CourseService.getRows(
	        		where, 
	        		"create_time desc", 
	        		pageNo, 
	        		pageSize
    		);
	        long total = CourseService.getCount(where);
	        
	        
	        
	        
	      //#outList for dynamic column
	      for (Course pre : list) {
	        	if(pre.type_id == 1) {
	        		pre.typeName = "公共必修课";
	        	}
	        	else if(pre.type_id == 2) {
	        		pre.typeName = "公共选修课";
	        	}
	        	else if(pre.type_id == 3) {
	        		pre.typeName = "核心限选课";
	        	} else {
	        		pre.typeName = "";
	        	} 
	      }
	        
	        
	        
	        
	        //#
			return jsonReturn(new PageData(
					total, 
					list
			));
		  }
		  catch (UserException ex){
			    logger.error("exception:", ex);
	            return jsonReturn(
	                ErrConfig.getErr(ErrConfig.WRITING_ERROR, ex.getMessage())
	            );
	      }
		  catch (Exception ex){
			    logger.error("exception:", ex);
	            return jsonReturn(
	                ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, ex.getMessage())
	            );
	      }
	}
	
}
