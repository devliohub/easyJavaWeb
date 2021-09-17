package com.tsjy.dwlgxy.servlet.admin.article;

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



@WebServlet(urlPatterns = "/a/article/add")
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
			String title   = StringUtil.getString(request.getParameter("title"), "")  ;
	        String content   = StringUtil.getString(request.getParameter("content"), "")  ;
	        String attachment   = StringUtil.getString(request.getParameter("attachment"), "")  ;
	        int menu_pid   = StringUtil.getInt(request.getParameter("menu_pid"), 0)  ;
	        int menu_id    = StringUtil.getInt(request.getParameter("menu_id"), 0)  ;
	        boolean is_top   =   StringUtil.getBoolean(request.getParameter("is_top"));
	        String cover   = StringUtil.getString(request.getParameter("cover"), "")  ;
	        int publish_time   = StringUtil.getInt(request.getParameter("publish_time"), 0)  ;
	        
	        
	        
	        
	        
	        
	        
	        //# || StringUtil.invalid(attachment) || menu_id == 0 || StringUtil.invalid(cover)
	        if( StringUtil.invalid(title) ) 
	        {
	        	// TODO  Log
	        	return jsonReturn(
	                ErrConfig.getErr(ErrConfig.WRITING_ERROR, "请输入标题")
	            );
	        	
	        }
	        if( title.length() > 200 )
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "标题不能超过100字符")
                );
	        }
	        if( StringUtil.invalid(content) ) 
	        {
	        	// TODO  Log
	        	return jsonReturn(
	                ErrConfig.getErr(ErrConfig.WRITING_ERROR, "请输入正文")
	            );
	        	
	        }
	        if( content.length() > 20000 )
	        {
	        	// TODO  Log
                return jsonReturn(
                    ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, "正文不能超过20000字符")
                );
	        }
	        if( menu_pid == 0 ) 
	        {
	        	// TODO  Log
	        	return jsonReturn(
	                ErrConfig.getErr(ErrConfig.WRITING_ERROR, "请选择所属栏目")
	            );
	        	
	        }
	        List<Menu> subMenuList = MenuService.getRows( String.format("pid=%s and is_delete=0 ", menu_pid), "sortnum asc");
	        if( subMenuList.size() > 0 && menu_id == 0 ) 
	        {
	        	// TODO  Log
	        	return jsonReturn(
	                ErrConfig.getErr(ErrConfig.WRITING_ERROR, "请选择所属栏目")
	            );
	        	
	        }
//	        if( publish_time == 0 ) 
//	        {
//	        	// TODO  Log
//	        	return jsonReturn(
//		                ErrConfig.getErr(ErrConfig.WRITING_ERROR, "请选择发布时间")
//		            );
//	        	
//	        }
	        if( StringUtil.invalid(cover) ) 
	        {
	        	// EGs: content = "<p>哈喽2<img src=\"http://121.36.22.149:9098/upload/20210827000900405.jpg\" contenteditable=\"false\" style=\"font-size: 14px; max-width: 100%;\"/><img src=\"http://121.36.22.149:9098/upload/20210910162511508.jpg\" style=\"max-width:100%;\" contenteditable=\"false\"/></p>";
		        List<String>  imgList =  StringUtil.getImgStr(content);
		        if( imgList.size() > 0 )
		        {
		        	cover = imgList.get(0);
		        }
	        }
	        
	        
			
			
			//#
			Article obj = new Article();
			obj.title = title;
	        obj.content = content;
	        obj.attachment = attachment;
	        obj.menu_pid =  menu_pid ;
	        obj.menu_id =  menu_id ;
	        obj.is_top =  is_top ;
	        obj.cover =  cover ;
	        obj.create_uid =  this.userInfo.id ;
	        obj.create_uname =  this.userInfo.name ;
	        obj.create_time = new Date().getTime() / 1000;	
	        obj.publish_time = (publish_time > 0 ? publish_time : new Date().getTime() / 1000);
	        long ret = ArticleService.add(obj);
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
