package com.tsjy.dwlgxy.servlet.admin.course;

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
import com.tsjy.dwlgxy.service.CourseService;



@WebServlet(urlPatterns = "/a/course/add")
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
			String name   = StringUtil.getString(request.getParameter("name"), "")  ;
	        String url   = StringUtil.getString(request.getParameter("url"), "")  ;
	        int module_id   = StringUtil.getInt(request.getParameter("module_id"), 0)  ;
	        int type_id   = StringUtil.getInt(request.getParameter("type_id"), 0)  ;
	        String cover   = StringUtil.getString(request.getParameter("cover"), "")  ;
	        
	        
	        System.out.println(name);
	        
	        
	        //#
	        if( StringUtil.invalid(name) )
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "请输入课程名称")
                );
	        }
	        if( name.length() > 200 )
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "课程名称不能超过200字符")
                );
	        }
//	        if( StringUtil.invalid(url) )
//	        {
//	        	// TODO  Log
//                return jsonReturn(
//                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "请输入链接地址")
//                );
//	        }
	        if( ! StringUtil.invalid(url) &&  url.length() > 1000 )
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "链接地址不能超过1000字符")
                );
	        }
	        if( ! StringUtil.invalid(url) && ! StringUtil.ValidURL(url) ) 
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "请输入有效链接地址")
                );
	        }
	        
	        
			
			//#
			Course obj = new Course();
			obj.name = name;
	        obj.url = url;
	        obj.module_id = module_id;
	        obj.type_id =  type_id ;
	        obj.cover =  cover ;
	        obj.create_uid =  this.userInfo.id ;
	        obj.create_uname =  this.userInfo.name ;
	        obj.create_time = new Date().getTime() / 1000;	
	        long ret = CourseService.add(obj);
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
