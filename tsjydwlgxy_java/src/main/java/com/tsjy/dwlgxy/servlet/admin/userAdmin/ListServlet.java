package com.tsjy.dwlgxy.servlet.admin.userAdmin;

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
import com.tsjy.dwlgxy.service.ArticleService;
import com.tsjy.dwlgxy.service.UserAdminService;



@WebServlet(urlPatterns = "/a/user/list")
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
		    if( StringUtil.valid(name) ) 
		    {
		    	sb.append("name like '%").append(name).append("%'").append(" and is_delete=0");
		    }
		    else
		    {
		    	sb.append("is_delete=0");
		    }
	    	String where = sb.toString();
	        
			
			
			//#
	        List<UserAdmin> list = UserAdminService.getRows(where, "id desc", pageNo, pageSize);
	        long total = UserAdminService.getCount(where);
	        
	        
	        
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
