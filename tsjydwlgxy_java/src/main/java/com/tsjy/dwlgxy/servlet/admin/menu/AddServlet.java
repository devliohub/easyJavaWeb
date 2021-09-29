package com.tsjy.dwlgxy.servlet.admin.menu;

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



@WebServlet(urlPatterns = "/a/menu/add")
public class AddServlet extends AdminBaseServlet 
{
	
	@Override
	protected String process(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException 
	{
		
		
		  try
		  {
			  
			//#
			int pid   = StringUtil.getInt(request.getParameter("pid"), 0)  ;
			String name   = StringUtil.getString(request.getParameter("name"), "")  ;
			int type   = StringUtil.getInt(request.getParameter("type"), 0)  ;
			int layout   = StringUtil.getInt(request.getParameter("layout"), 0)  ;
	        String url   = StringUtil.getString(request.getParameter("url"), "")  ;
	        
	        
	        
	        //#
	        if( StringUtil.invalid(name) ) 
	        {
	        	// TODO  Log
	        	return jsonReturn(
	                ErrConfig.getErr(ErrConfig.WRITING_ERROR, "请输入菜单名称")
	            );
	        	
	        }
	        if( name.length() > 100 )
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "菜单名称不能超过100字符")
                );
	        }
	        if( (type == 1 && layout == 0) ) 
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "请选择样式")
                );
	        }
	        if( (type == 2 && StringUtil.invalid(url)) ) 
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "请输入链接")
                );
	        }
	        if( (type == 2 && ! StringUtil.ValidURL(url)) ) 
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "请输入有效链接")
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
			Menu obj = new Menu();
			obj.pid = pid;
			obj.name = name;
			obj.type = type ;
			obj.layout = layout ;
	        obj.url = url;
	        obj.sortnum =  new Date().getTime() / 1000;	
	        obj.create_time = new Date().getTime() / 1000;	
	        long ret = MenuService.add(obj);
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
