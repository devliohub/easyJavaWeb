package com.tsjy.dwlgxy.servlet.admin.course;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import com.tsjy.dwlgxy.bean.*;
import com.tsjy.dwlgxy.common.utils.*;
import com.tsjy.dwlgxy.common.conf.*;
import com.tsjy.dwlgxy.common.exception.*;
import com.tsjy.dwlgxy.common.AdminBaseServlet;
import com.tsjy.dwlgxy.service.CourseService;
import com.tsjy.dwlgxy.service.UserAdminService;



@WebServlet(urlPatterns = "/a/course/list")
public class ListServlet extends AdminBaseServlet 
{
   
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
	        int  pageNo   = StringUtil.getInt(request.getParameter("pageNo"), 1);
	        int  pageSize = StringUtil.getInt(request.getParameter("pageSize"), 10);
	        
	        
	        //#
			if ( this.userInfo == null  ) 
	        {
				// TODO  Log
	        	return jsonReturn(
	                ErrConfig.getErr(ErrConfig.NOT_LOGIN, "")
	            );
	        	
	        }
			
			
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
	        List<Course> list = CourseService.getRows(where, "id desc", pageNo, pageSize);
	        long total = CourseService.getCount(where);
	        System.out.print(list);
	        
	        
	        
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
