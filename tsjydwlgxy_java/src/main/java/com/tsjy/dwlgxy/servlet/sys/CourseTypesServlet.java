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



@WebServlet(urlPatterns = "/sys/coursetypes")
public class CourseTypesServlet extends BaseServlet 
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
//					put(1, "公共必修课");
//					put(2, "公共选修课");
//					put(3, "核心限选课");
//			   }
//			});
			  
		    long total1 = CourseService.getCount(String.format("type_id=%s and is_delete=0 ", 1));  
			long total2 = CourseService.getCount(String.format("type_id=%s and is_delete=0 ", 2));  
			long total3 = CourseService.getCount(String.format("type_id=%s and is_delete=0 ", 3));  
			
			
			return jsonReturn(new DictData[] {
				    new DictData(1, "公共必修课", total1),
					new DictData(2, "公共选修课", total2),
					new DictData(3, "核心限选课", total3)
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
