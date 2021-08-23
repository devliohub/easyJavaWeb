package com.tsjy.dwlgxy.servlet.admin;

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



@WebServlet(urlPatterns = "/a/login")
public class LoginServlet extends AdminBaseServlet 
{
	
	
	
	public LoginServlet() {
		System.out.println("0");
		super.ignoreVerify = true;
	}

	@Override
	protected String process(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException 
	{
		System.out.println("4");
		
		  try
		  {
			  
			//#
		    String account = request.getParameter("account").trim();
	        String password = request.getParameter("password").trim();
	        
	        
	        //#
	        if( StringUtil.invalid(account) || StringUtil.invalid(password) ) 
	        {
	        	// TODO  Log
	        	return jsonReturn(
	                ErrConfig.getErr(ErrConfig.BAD_REQUEST, "")
	            );
	        	
	        }
	        UserAdmin user = UserAdminService.getRow(account);
			if (user == null) 
	        {
				// TODO  Log
				return jsonReturn(
	                ErrConfig.getErr(ErrConfig.WRITING_ERROR, "用户不存在")
	            );
	        }
			if ( ! password.equals(user.password)  ) 
	        {
				// TODO  Log
	        	return jsonReturn(
	                ErrConfig.getErr(ErrConfig.LOGIN_INVALID, "")
	            );
	        	
	        }
	        
			
			
			//#
	        HttpSession session = request.getSession(true);
			System.out.println("ID " + session.getId());
	        session.setAttribute("user", user);
	        
	        
	        
	        
	        //#
			return jsonReturn(
					
				/*
				 * List.of( new UserAdmin(), new UserAdmin(), new UserAdmin() )
				 */
					
				/* UserAdminService.getRow(1) */
					
				user
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
