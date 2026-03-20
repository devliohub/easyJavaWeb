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
		
		
		  try
		  {
			  
			//#
//			return jsonReturn(new HashMap<Integer, String>() {
//			   {
//					put(1, "道德规范力");
//				    put(2, "协助领导力");
//					put(3, "认识理解力");
//					put(4, "应用行动力");
//					put(5, "高阶创新力");
//					put(6, "沟通表达力");
//					put(7, "审美鉴赏力");
//					
//			   }
//			});
			
			long total1 = CourseService.getCount(String.format("module_id=%s and is_delete=0 ", 1));  
			long total2 = CourseService.getCount(String.format("module_id=%s and is_delete=0 ", 2));  
			long total3 = CourseService.getCount(String.format("module_id=%s and is_delete=0 ", 3));  
			long total4 = CourseService.getCount(String.format("module_id=%s and is_delete=0 ", 4));  
			long total5 = CourseService.getCount(String.format("module_id=%s and is_delete=0 ", 5));  
			long total6 = CourseService.getCount(String.format("module_id=%s and is_delete=0 ", 6));  
			long total7 = CourseService.getCount(String.format("module_id=%s and is_delete=0 ", 7));  
			 
			  
			return jsonReturn(new DictData[] {
					new DictData(1, "道德规范力", total1, "daodeguifanli"),
					new DictData(7, "审美鉴赏力", total7, "shenmeijianshangli"),
					new DictData(2, "协作领导力", total2, "xiezuolingdaoli"),
					new DictData(3, "认识理解力", total3, "renshilijie"),
					new DictData(4, "应用行动力", total4, "yingyonghangdongli"),
					new DictData(5, "高阶创新力", total5, "gaojiechuangxinli"),
					new DictData(6, "沟通表达力", total6, "goutongbiaodali")
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
