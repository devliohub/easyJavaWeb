package com.tsjy.dwlgxy.servlet.sys;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import org.apache.tomcat.util.http.fileupload.FileUploadException;

import java.nio.charset.StandardCharsets;

import com.tsjy.dwlgxy.bean.*;
import com.tsjy.dwlgxy.common.utils.*;
import com.tsjy.dwlgxy.common.conf.*;
import com.tsjy.dwlgxy.common.exception.*;
import com.tsjy.dwlgxy.common.BaseServlet;
import com.tsjy.dwlgxy.service.*;

@WebServlet(urlPatterns = "/sys/upload")
public class UploadServlet extends BaseServlet {

	@Override
	protected String process(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException 
	{
		try {
			
			// 读取Request Body: -> 写入文件:
			//String realPath = this.getServletContext().getRealPath("/upload");// /WEB-INF/files
			String realPath = AppConfig.STATIC_UPLOAD_PATH;
			String fileUrl  = FileUtil.upload(request, realPath);

			// 显示上传结果:
			return jsonReturn(fileUrl);
		} catch (UserException ex) {
			logger.error("exception:", ex);
			return jsonReturn(ErrConfig.getErr(ErrConfig.WRITING_ERROR, ex.getMessage()));
		} catch (FileUploadException ex) {
			logger.error("exception:", ex);
			return jsonReturn(ErrConfig.getErr(ErrConfig.WRITING_ERROR, ex.getMessage()));
		} catch (Exception ex) {
			logger.error("exception:", ex);
			return jsonReturn(ErrConfig.getErr(ErrConfig.INTERNAL_SERVER_ERROR, ex.getMessage()));
		}
	}

}
