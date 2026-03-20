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



@WebServlet(urlPatterns = "/a/user/password")
public class PassWordServlet extends AdminBaseServlet 
{
	
	

	@Override
	protected String process(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException 
	{
		
		
		  try
		  {
			  
			//#
	        String password      = StringUtil.getString(request.getParameter("password"), "")  ;
	        String newpassword   = StringUtil.getString(request.getParameter("newpassword"), "")  ;
	        
	        
	        
	        
	        
	        //#
	        if( StringUtil.invalid(password) || StringUtil.invalid(newpassword) ) 
	        {
	        	// TODO  Log
	        	return jsonReturn(
	        			ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "请输入密码")
	            );
	        	
	        }
			if( ! this.userInfo.password.equals(password) )
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.WRITING_ERROR, "密码错误")
                );
	        }
			if( ! StringUtil.isMatchesLoginPassword(newpassword) )
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "密码应为6-16位字母或数字")
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
	         
			this.userInfo.password = newpassword;
			this.userInfo.update_time = new Date().getTime() / 1000;	
	        int ret = UserAdminService.updatePassWord(this.userInfo);
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
