package com.tsjy.dwlgxy.servlet.admin.article;

import java.io.*;
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
import com.tsjy.dwlgxy.service.CourseService;



@WebServlet(urlPatterns = "/a/article/info")
public class InfoServlet extends AdminBaseServlet 
{
	
	

	@Override
	protected String process(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException 
	{
		System.out.println("4");
		
		  try
		  {
			  
			//#
			int id = StringUtil.getInt(request.getParameter("id"), 0);
	        
	        
	        
	        //#
	        if( id == 0 ) 
	        {
	        	// TODO  Log
	        	return jsonReturn(
	                ErrConfig.getErr(ErrConfig.BAD_REQUEST, "")
	            );
	        	
	        }
	        
	        
			
			
			//#
	        Article obj = ArticleService.getRow(id);
	        if(obj == null)
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.WRITING_ERROR, "不存在")
                );
	        }
	        
	        
	        
	        
	        //#
			return jsonReturn(obj);
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
