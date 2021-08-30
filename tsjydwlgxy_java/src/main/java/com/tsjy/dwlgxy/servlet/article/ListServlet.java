package com.tsjy.dwlgxy.servlet.article;

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



@WebServlet(urlPatterns = "/article/list")
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
//	        if( menu_pid == 0 ) 
//	        {
//	        	// TODO  Log
//	        	return jsonReturn(
//	                ErrConfig.getErr(ErrConfig.BAD_REQUEST, "")
//	            );
//	        	
//	        }
	        
	        
	        // #
		    StringBuilder sb = new StringBuilder();
	    	sb.append("1=1");
		    if( menu_pid != 0 ) sb.append(" and menu_pid=").append(menu_pid);
		    if( menu_id != 0 ) sb.append(" and menu_id=").append(menu_id);
		    if( StringUtil.valid(title) ) 
		    {
		    	sb.append(" and title like '%").append(title).append("%'");
		    }
	    	sb.append(" and is_delete=0");
	    	String where = sb.toString();
	        
			
			
			//#
	        List<Article>  list  = ArticleService.getRows(
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
