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
import com.tsjy.dwlgxy.service.*;



@WebServlet(urlPatterns = "/a/article/add")
public class AddServlet extends AdminBaseServlet 
{
	
	@Override
	protected String process(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException 
	{
		System.out.println("4");
		
		  try
		  {
			  
			//#
			String title   = StringUtil.getString(request.getParameter("title"), "")  ;
	        String content   = StringUtil.getString(request.getParameter("content"), "")  ;
	        String attachment   = StringUtil.getString(request.getParameter("attachment"), "")  ;
	        int menu_pid   = StringUtil.getInt(request.getParameter("menu_pid"), 0)  ;
	        int menu_id    = StringUtil.getInt(request.getParameter("menu_id"), 0)  ;
	        int is_top   =   StringUtil.getBoolean(request.getParameter("is_top")) ? 1 :0  ;
	        String cover   = StringUtil.getString(request.getParameter("cover"), "")  ;
	        
	        
	        
	        
	        
	        //# || StringUtil.invalid(attachment) || menu_id == 0 || StringUtil.invalid(cover)
	        if( StringUtil.invalid(title) || StringUtil.invalid(content)   || menu_pid == 0  ) 
	        {
	        	// TODO  Log
	        	return jsonReturn(
	                ErrConfig.getErr(ErrConfig.BAD_REQUEST, "")
	            );
	        	
	        }
	        
	        
	        //#
			if ( this.userInfo == null  ) 
	        {
				// TODO  Log
	        	return jsonReturn(
	                ErrConfig.getErr(ErrConfig.NOT_LOGIN, "")
	            );
	        	
	        }
	        
	        
	        
			
			
			//#
			Article obj = new Article();
			obj.title = title;
	        obj.content = content;
	        obj.attachment = attachment;
	        obj.menu_pid =  menu_pid ;
	        obj.menu_id =  menu_id ;
	        obj.is_top =  is_top ;
	        obj.cover =  cover ;
	        obj.create_uid =  this.userInfo.id ;
	        obj.create_uname =  this.userInfo.name ;
	        obj.create_time = new Date().getTime() / 1000;	
	        long ret = ArticleService.add(obj);
	        if(ret == 0)
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "提交失败")
                );
	        }
	        
	        
	        
	        
	        //#
			return jsonReturn(
                ErrConfig.getErr(ErrConfig.STATUS_OK, "")
            );
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
