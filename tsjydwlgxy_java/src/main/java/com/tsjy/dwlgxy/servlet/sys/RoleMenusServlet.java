package com.tsjy.dwlgxy.servlet.sys;

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
import com.tsjy.dwlgxy.common.BaseServlet;
import com.tsjy.dwlgxy.service.UserAdminService;



@WebServlet(urlPatterns = "/sys/rolemenus")
public class RoleMenusServlet extends BaseServlet 
{
   
	@Override
	protected String process(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException
	{
		System.out.println("4");
		
		  try
		  {
			  
	        //#
//			return jsonReturn(new HashMap<Integer, String>() {
//			   {
//					put(1, "文章管理");
//					put(2, "轮播图管理");
//					put(3, "菜单管理");
//					put(4, "课程管理");
//					put(5, "用户管理");
//			   }
//			});
			  
		  return jsonReturn(new DictData[] {
					
				new DictData(1, "文章管理"),
				new DictData(2, "轮播图管理"),
				new DictData(3, "菜单管理"),
				new DictData(4, "课程管理"),
				new DictData(5, "用户管理"),
			   
			});
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
