package com.tsjy.dwlgxy.common;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tsjy.dwlgxy.bean.*;
import com.tsjy.dwlgxy.common.utils.*;
import com.tsjy.dwlgxy.common.conf.ErrConfig;



public class AdminBaseServlet extends HttpServlet
{
	public  boolean ignoreVerify = false;
    public  UserAdmin userInfo;
    private String _ret;
    protected  final Logger logger = LoggerFactory.getLogger(getClass());
    
    
	private void initialize(HttpServletRequest request)  
	{
		System.out.println("1");
	}
	
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        doGet(request, response);
    }
	
	public  void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException 
	{
		initialize(request);
		System.out.println("2");
		System.out.println(this.ignoreVerify);
		
		HttpSession session = request.getSession(true);
		System.out.println("ID " + session.getId());
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");  
		response.setContentType("application/json");
		//String origin = request.getHeader("Origin");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "*");
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("P3P", "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
		PrintWriter pw = response.getWriter();
		
		
		if( ! this.ignoreVerify )
		{
			if ( checkLogin(session) )
			{
				_ret = process(request, response);
			}
			else 
			{
				_ret = jsonReturn(
		                 ErrConfig.getErr(ErrConfig.NOT_LOGIN, "")
		        );
			}
		}
		else
		{
			_ret = process(request, response);
		}
        
        
        pw.write(_ret);
		pw.flush();
	}
	
	
	protected String process(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException 
	{
		System.out.println("3");
		return "";
	}
	
	
	protected String jsonReturn(ResponseData ret) throws JsonProcessingException
    {
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        String json = mapper.writeValueAsString(ret);
        
        return json;
    }
	
	
	protected String jsonReturn(Object result) throws JsonProcessingException
    {
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        ResponseData ret = new ResponseData(ErrConfig.STATUS_OK, "SUCC", "");
        ret.result = result;
        String json = mapper.writeValueAsString(ret);
        
        return json;
    }
		
	
	public boolean checkLogin(HttpSession session)
    {
		userInfo = (UserAdmin) session.getAttribute("user");
        if (userInfo == null) {
        	return false;
		}
		
        return true;
    }
    

}