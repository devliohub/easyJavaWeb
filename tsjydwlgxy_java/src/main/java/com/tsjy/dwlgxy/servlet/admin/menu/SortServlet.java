package com.tsjy.dwlgxy.servlet.admin.menu;

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
import com.tsjy.dwlgxy.service.*;



@WebServlet(urlPatterns = "/a/menu/sort")
public class SortServlet extends AdminBaseServlet 
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
			String sorttype = StringUtil.getString(request.getParameter("sorttype"), "");
	        
	        
	        
	        //#
	        if( id == 0 || StringUtil.invalid(sorttype) ) 
	        {
	        	// TODO  Log
	        	return jsonReturn(
	                ErrConfig.getErr(ErrConfig.BAD_REQUEST, "")
	            );
	        	
	        }
	        
	        
			
			
			//#
	        Menu obj = MenuService.getRow(id);
	        if(obj == null)
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.WRITING_ERROR, "不存在")
                );
	        }
	        
	        
	        
	        //#   
	        if( sorttype.equals("prev") )
        	// prev
	        {
	        	List<Menu> objPrevList = MenuService.getLimitRows( String.format("pid=%s and sortnum <= %s and is_delete=0 and id <> %s", obj.pid, obj.sortnum, obj.id), "pid asc, sortnum desc", 1);
	        	if( objPrevList != null )
	        	{
	        		Menu objPrev = objPrevList.get(0);
	        		//
	    	        obj.sortnum = objPrev.sortnum - 1;
	    	        obj.update_time = new Date().getTime() / 1000;
	    	        int ret =  MenuService.updateSortNum(obj);
	    	        if(ret == 0)
	    	        {
	    	        	// TODO  Log
	                    return jsonReturn(
	                        ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "更新失败")
	                    );
	    	        }
	        	}
	        	
	        }
	        if( sorttype.equals("next") )
        	// next
	        {
	        	
	        	List<Menu> objNextList = MenuService.getLimitRows( String.format("pid=%s and sortnum >= %s and is_delete=0 and id <> %s", obj.pid, obj.sortnum, obj.id), "pid asc, sortnum asc", 1);
	        	if( objNextList != null )
	        	{
	        		Menu objNext = objNextList.get(0);
	        		//
	    	        obj.sortnum = objNext.sortnum + 1;
	    	        obj.update_time = new Date().getTime() / 1000;	
	    	        int ret =  MenuService.updateSortNum(obj);
	    	        if(ret == 0)
	    	        {
	    	        	// TODO  Log
	                    return jsonReturn(
	                        ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "更新失败")
	                    );
	    	        }
	        	}
	        	
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
