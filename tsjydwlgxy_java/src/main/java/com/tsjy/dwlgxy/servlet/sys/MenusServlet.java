package com.tsjy.dwlgxy.servlet.sys;

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



@WebServlet(urlPatterns = "/sys/menus")
public class MenusServlet extends BaseServlet 
{
   
	@Override
	protected String process(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException
	{
		System.out.println("4");
		
		  try
		  {
			  
			//#
			int pid   = StringUtil.getInt(request.getParameter("pid"), 0)  ;
			boolean is_article_type   = StringUtil.getBoolean(request.getParameter("is_article_type"))  ;
			
			
			  
			//#
			String whereType =  is_article_type ?  " and type=1 " : "";
	        List<Menu> list = MenuService.getRows( String.format("pid=%s %s and is_delete=0 ", pid, whereType), "sortnum asc");
		        
	        
	        //#outList for dynamic column
	        //List<Menu> outList = new ArrayList<>();
	        for (Menu pre : list) {
	        	if(pre.pid == 0) {
	        		pre.sub_menus = MenuService.getRows( String.format("pid=%s and is_delete=0 ", pre.id), "sortnum asc");
	        	}
	        	
        		pre.is_course_menu = pre.type == 3;  //pre.name.equals("通识课程");
	        }
	        
		        
		    
	        //#
			return jsonReturn(list);

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
