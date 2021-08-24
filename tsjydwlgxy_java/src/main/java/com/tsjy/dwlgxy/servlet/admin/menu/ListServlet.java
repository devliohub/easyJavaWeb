package com.tsjy.dwlgxy.servlet.admin.menu;

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
import com.tsjy.dwlgxy.service.*;



@WebServlet(urlPatterns = "/a/menu/list")
public class ListServlet extends AdminBaseServlet 
{
   
	@Override
	protected String process(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException
	{
		System.out.println("4");
		
		  try
		  {
	        
	        //#
			if ( this.userInfo == null  ) 
	        {
				// TODO  Log
	        	return jsonReturn(
	                ErrConfig.getErr(ErrConfig.NOT_LOGIN, "")
	            );
	        	
	        }
	        
			
			
			//#
	        List<Menu> list = MenuService.getRows("is_delete=0", "pid asc, sortnum asc");
	        
	        
	        
	        
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
