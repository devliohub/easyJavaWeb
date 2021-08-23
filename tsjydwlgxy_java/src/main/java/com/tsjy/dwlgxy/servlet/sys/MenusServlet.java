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
			
			
			  
			//#
	        var list = MenuService.getRows( String.format("pid=%s and is_delete=0 ", pid), "sortnum asc");
		        
		        
		    
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
