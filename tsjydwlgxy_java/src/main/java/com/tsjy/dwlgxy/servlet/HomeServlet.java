package com.tsjy.dwlgxy.servlet;

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



@WebServlet(urlPatterns = "/home")
public class HomeServlet extends BaseServlet 
{
   
	@SuppressWarnings("unchecked")
	@Override
	protected String process(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException
	{
		
		
		  try
		  {
			  
			
			  
			
			//#
			List<Banner> list0 = BannerService.getRows("is_delete=0", "sortnum desc");
			
			
			//#
	    	Menu obj11 = MenuService.getRowByName("通识新闻");
	        List<Article>  list11  = obj11 == null ? (new ArrayList<>()) : ArticleService.getLimitRows(
	        		String.format("menu_pid=%s %s and is_delete=0 ", (obj11.pid==0 ? obj11.id : obj11.pid), (obj11.pid==0 ? "" : "and menu_id=" + obj11.id)), 
	        		"is_top desc, publish_time desc", 
	        		5
    		);
	    	Menu obj12 = MenuService.getRowByName("通识活动");
	        List<Article>  list12  = obj12 == null ? (new ArrayList<>()) : ArticleService.getLimitRows(
	        		String.format("menu_pid=%s %s and is_delete=0 ", (obj12.pid==0 ? obj12.id : obj12.pid), (obj12.pid==0 ? "" : "and menu_id=" + obj12.id)), 
	        		"is_top desc, publish_time desc", 
	        		5
    		);
	        Menu obj13 = MenuService.getRowByName("通识讲堂");
	        List<Article>  list13  = obj13 == null ? (new ArrayList<>()) : ArticleService.getLimitRows(
	        		String.format("menu_pid=%s %s and is_delete=0 ", (obj13.pid==0 ? obj13.id : obj13.pid), (obj13.pid==0 ? "" : "and menu_id=" + obj13.id)), 
	        		"is_top desc, publish_time desc", 
	        		5
    		);
	        Menu obj14 = MenuService.getRowByName("求是读书计划");
	        List<Article>  list14  = obj14 == null ? (new ArrayList<>()) : ArticleService.getLimitRows(
	        		String.format("menu_pid=%s %s and is_delete=0 ", (obj14.pid==0 ? obj14.id : obj14.pid), (obj14.pid==0 ? "" : "and menu_id=" + obj14.id)), 
	        		"is_top desc, publish_time desc", 
	        		5
    		);
	        HomeArticleData[] list1 = { 
	           new HomeArticleData(  (obj11 == null ? 0 : obj11.pid), (obj11 == null ? 0 : obj11.id), "通识新闻",  list11),
	           new HomeArticleData(  (obj12 == null ? 0 : obj12.pid), (obj12 == null ? 0 : obj12.id), "通识活动",  list12),
	           new HomeArticleData(  (obj13 == null ? 0 : obj13.pid), (obj13 == null ? 0 : obj13.id), "通识教育大讲堂",  list13),
	           new HomeArticleData(  (obj14 == null ? 0 : obj14.pid), (obj14 == null ? 0 : obj14.id), "求是读书计划",  list14),
	        };
	        
	        
	        
	        
	        
	        //#
	        //Menu obj21 = MenuService.getRowByName("知行讲坛");
	        //Menu obj22 = MenuService.getRowByName("青年大讲坛");
	        Menu obj21 = MenuService.getRowByName("活动报名");
	        Menu obj22 = MenuService.getRowByName("活动回顾");
	        Menu obj23 = MenuService.getRowByName("书目阅读");
	        Menu obj24 = MenuService.getRowByName("读书沙龙");
	        List<Menu>  list2 =  /*List.of(
        		obj21,
        		obj22,
        		obj23,
        		obj24
    		);*/ new ArrayList<>();
	        list2.add(obj21);
	        list2.add(obj22);
	        list2.add(obj23);
	        list2.add(obj24);
	        
	        
	        
	        //#
	        Menu obj31 = MenuService.getRowByName("学生风采");
	        List<Article>  list31  = obj31 == null ? (new ArrayList<>()) : ArticleService.getLimitRows(
	        		String.format("menu_pid=%s %s and is_delete=0 ", (obj31.pid==0 ? obj31.id : obj31.pid), (obj31.pid==0 ? "" : "and menu_id=" + obj31.id)), 
	        		"is_top desc, publish_time desc", 
	        		4
    		);
	        HomeArticleData  list3 =  new HomeArticleData( (obj31 == null ? 0 : obj31.pid), (obj31 == null ? 0 : obj31.id), "学生风采",  list31);
	        		
	        		
	        		
	        
	        
	        //#
	        Menu obj41 = MenuService.getRowByName("课程成果展示");
	        List<Article>  list41  = obj41 == null ? (new ArrayList<>()) : ArticleService.getLimitRows(
	        		String.format("menu_pid=%s %s and is_delete=0 ", (obj41.pid==0 ? obj41.id : obj41.pid), (obj41.pid==0 ? "" : "and menu_id=" + obj41.id)), 
	        		"is_top desc, publish_time desc", 
	        		5
    		);
	        HomeArticleData  list4 =  new HomeArticleData( (obj41 == null ? 0 : obj41.pid), (obj41 == null ? 0 : obj41.id), "课程成果展示",  list41);
	        
	      
	        
	        
	        
	        //#
			return jsonReturn(new HomeData(
					list0,
					list1, 
					list2,
					list3,
					list4
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
