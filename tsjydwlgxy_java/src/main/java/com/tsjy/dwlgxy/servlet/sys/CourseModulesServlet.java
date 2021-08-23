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
import com.tsjy.dwlgxy.service.*;



@WebServlet(urlPatterns = "/sys/coursemodules")
public class CourseModulesServlet extends BaseServlet 
{
   
	@Override
	protected String process(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException
	{
		System.out.println("4");
		
		  try
		  {
			  
			//#
			return jsonReturn(new HashMap<Integer, String>() {
			   {
					put(1, "道德规范力");
					put(2, "协助领导力");
					put(3, "认识理解力");
					put(4, "应用行动力");
					put(5, "高阶创新力");
					put(6, "沟通表达力");
					put(7, "审美鉴赏力");
			   }
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
