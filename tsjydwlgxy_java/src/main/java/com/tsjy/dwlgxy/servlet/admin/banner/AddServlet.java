package com.tsjy.dwlgxy.servlet.admin.banner;

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



@WebServlet(urlPatterns = "/a/banner/add")
public class AddServlet extends AdminBaseServlet 
{
	
	@Override
	protected String process(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException 
	{
		System.out.println("4");
		
		  try
		  {
			  
			//#
			String img   = StringUtil.getString(request.getParameter("img"), "")  ;
	        String url   = StringUtil.getString(request.getParameter("url"), "")  ;
	        
	        
	        
	        //#
	        if( StringUtil.invalid(img)  ) 
	        {
	        	// TODO  Log
	        	return jsonReturn(
	        			ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "图片不能为空")
	            );
	        	
	        }
//	        if( ! StringUtil.invalid(url) && ! StringUtil.ValidURL(url) ) 
//	        {
//	        	// TODO  Log
//                return jsonReturn(
//                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "请输入有效链接地址")
//                );
//	        }
	        
	        
	        //#
			if ( this.userInfo == null  ) 
	        {
				// TODO  Log
	        	return jsonReturn(
	                ErrConfig.getErr(ErrConfig.NOT_LOGIN, "")
	            );
	        	
	        }
	        
	        
	        
			
			
			//#
			Banner obj = new Banner();
			obj.img = img;
			obj.url = url ;
	        obj.sortnum =  new Date().getTime() / 1000;	
	        obj.create_time = new Date().getTime() / 1000;	
	        long ret = BannerService.add(obj);
	        if(ret == 0)
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "提交失败")
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
