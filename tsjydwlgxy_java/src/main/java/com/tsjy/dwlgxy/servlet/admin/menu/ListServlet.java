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
	        List<Menu> list = MenuService.getRows( "pid=0 and is_delete=0 ", "sortnum asc");
	        
	        
	        
	        //#outList for dynamic column
	        List<Menu> outList = new ArrayList<>();
	        for (Menu pre : list) {
	        	if(pre.pid == 0) {
	        		pre.sub_menus = MenuService.getRows( String.format("pid=%s and is_delete=0 ", pre.id), "sortnum asc");
	        	}
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
