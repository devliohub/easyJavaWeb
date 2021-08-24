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
		System.out.println("4");
		
		  try
		  {
			  
			//#
			int module_id   = StringUtil.getInt(request.getParameter("module_id"), 0)  ;
		    int type_id   = StringUtil.getInt(request.getParameter("type_id"), 0)  ;
		    String name   = StringUtil.getString(request.getParameter("name"), "")  ;
	        int pageNo   = StringUtil.getInt(request.getParameter("pageNo"), 1);
	        int pageSize = StringUtil.getInt(request.getParameter("pageSize"), 10);
	        
	        
	        //#
//	        if(  module_id == 0 || type_id != 0  ) 
//	        {
//	        	// TODO  Log
//	        	return jsonReturn(
//	                ErrConfig.getErr(ErrConfig.BAD_REQUEST, "")
//	            );
//	        	
//	        }
	       
			
			
			//#
	        String wname  = StringUtil.valid(name) ? String.format(" and name like '%%s%'", name) : "";
	        String where  = String.format("module_id=%s and type_id=%s %s and is_delete=0", module_id, type_id, wname);
	        List<Course>  list  = CourseService.getRows(
	        		where, 
	        		"create_time desc", 
	        		pageNo, 
	        		pageSize
    		);
	        long total = CourseService.getCount(where);
	        
	        
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
