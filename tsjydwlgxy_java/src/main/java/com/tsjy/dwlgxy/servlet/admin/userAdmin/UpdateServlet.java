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
	        UserAdmin obj = UserAdminService.getRow(id);
	        if(obj == null)
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.WRITING_ERROR, "不存在")
                );
	        }
	        
	        
	        
	        //#
	        if( StringUtil.invalid(name) )
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "请输入姓名")
                );
	        }
	        if( name.length() > 100 )
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "姓名不能超过100字符")
                );
	        }
	        if( StringUtil.invalid(account) )
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "请输入账号")
                );
	        }
	        if( account.length() > 100 )
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "账号不能超过100字符")
                );
	        }
	        if( ! StringUtil.checkLoginAccountInput(account) )
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "账号不能包含特殊字符")
                );
	        }
	        System.out.println(account);
	        System.out.println(obj.account);
	        System.out.println(! obj.account.equals(account));
	        if( ! obj.account.equals(account) )
	        {
	        	UserAdmin existObj = UserAdminService.getRow(account);
		        if( existObj != null )
		        {
		        	// TODO  Log
	                return jsonReturn(
	                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "账号已存在")
	                );
		        }
	        }
	        if( StringUtil.invalid(password) )
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "请输入密码")
                );
	        }
	        if( ! StringUtil.isMatchesLoginPassword(password) )
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "密码应为6-18位字母或数字")
                );
	        }
	        if( StringUtil.invalid(rolemenuids) )
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "至少勾选一项权限")
                );
	        }
	        
	        
			
			
			
	        
	        
	        
	        //#
	        obj.id = id;
	        obj.name = name;
	        obj.account = account;
	        obj.password = password;
	        obj.rolemenuids =  StringUtil.splitString(rolemenuids, ",") ;
	        obj.update_time = new Date().getTime() / 1000;	
	        int ret = UserAdminService.update(obj);
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
