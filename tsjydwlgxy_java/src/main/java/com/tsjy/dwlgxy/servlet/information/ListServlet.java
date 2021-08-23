package com.tsjy.dwlgxy.servlet.information;

import java.io.*;
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



@WebServlet(urlPatterns = "/information/list")
public class ListServlet extends BaseServlet 
{
   
	@SuppressWarnings("unchecked")
	@Override
	protected String process(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException
	{
		System.out.println("4");
		
		  try
		  {
			  
			//#
		    int menu_pid   = StringUtil.getInt(request.getParameter("menu_pid"), 0)  ;
	        int menu_id    = StringUtil.getInt(request.getParameter("menu_id"), 0)  ;
	        String title   = StringUtil.getString(request.getParameter("title"), "")  ;
	        int pageNo   = StringUtil.getInt(request.getParameter("pageNo"), 1);
	        int pageSize = StringUtil.getInt(request.getParameter("pageSize"), 10);
	        
	        
	        //#
	        if( menu_pid == 0 ) 
	        {
	        	// TODO  Log
	        	return jsonReturn(
	                ErrConfig.getErr(ErrConfig.BAD_REQUEST, "")
	            );
	        	
	        }
	        
			
			
			//#
	        String wtitle  = StringUtil.valid(title) ? String.format(" and title like '%%s%'", title) : "";
	        String where   = String.format("menu_pid=%s and menu_id=%s %s and is_delete=0", menu_pid, menu_id, wtitle);
	        var  list  = ArticleService.getRows(
	        		where, 
	        		"is_top desc, create_time desc", 
	        		pageNo, 
	        		pageSize
    		);
	        long total = ArticleService.getCount(where);
	        
	        
	        //#outList for dynamic column
	        //List<Object> outList = new ArrayList<>();
	        //for (Article pre : list) {
	        //
	        //}
	        
	        
	        
	        
	        //#
			return jsonReturn(new PageData(
					total, 
					list
			));
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
