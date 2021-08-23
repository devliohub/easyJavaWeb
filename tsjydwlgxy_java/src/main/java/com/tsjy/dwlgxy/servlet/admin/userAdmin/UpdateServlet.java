package com.tsjy.dwlgxy.servlet.admin.userAdmin;

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
import com.tsjy.dwlgxy.service.UserAdminService;



@WebServlet(urlPatterns = "/a/user/update")
public class UpdateServlet extends AdminBaseServlet 
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
			String name   = StringUtil.getString(request.getParameter("name"), "")  ;
	        String account   = StringUtil.getString(request.getParameter("account"), "")  ;
	        String password   = StringUtil.getString(request.getParameter("password"), "")  ;
	        String rolemenuids   = StringUtil.getString(request.getParameter("rolemenuids"), "")  ;
	        if( rolemenuids.endsWith(",") ) {
	        	rolemenuids = rolemenuids.substring(-1);
	        }
	        if( rolemenuids.startsWith(",") ) {
	        	rolemenuids = rolemenuids.substring(0, 1);
	        }
	        
	        
	        
	        //#
	        if( id == 0 || StringUtil.invalid(name) || StringUtil.invalid(account) || StringUtil.invalid(password) || StringUtil.invalid(rolemenuids) ) 
	        {
	        	// TODO  Log
	        	return jsonReturn(
	                ErrConfig.getErr(ErrConfig.BAD_REQUEST, "")
	            );
	        	
	        }
	        
	        
			
			
			//#
	        UserAdmin obj = UserAdminService.getRow(id);
	        if(obj == null)
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.WRITING_ERROR, "不存在")
                );
	        }
	        
	        
	        
	        //#
	        obj.id = id;
	        obj.name = name;
	        obj.account = account;
	        obj.password = password;
	        obj.rolemenuids =  StringUtil.splitString(rolemenuids, ",") ;
	        obj.update_time = new Date().getTime() / 1000;	
	        var ret = UserAdminService.update(obj);
	        if(ret == 0)
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "更新失败")
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
