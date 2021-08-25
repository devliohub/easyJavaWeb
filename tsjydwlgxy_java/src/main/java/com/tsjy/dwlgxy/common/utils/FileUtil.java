package com.tsjy.dwlgxy.common.utils;

import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import com.tsjy.dwlgxy.common.conf.AppConfig;


public class FileUtil {
	
	

	public static String upload(HttpServletRequest request, String realPath)
	throws IOException, ServletException, FileUploadException, Exception
	{
		String fileUrl = "";

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List items = upload.parseRequest(new ServletRequestContext(request));
			Iterator itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (item.isFormField()) {
					System.out.println("表单参数名:" + item.getFieldName() + "，表单参数值:" + item.getString("UTF-8"));
				} else {
					if (item.getName() != null && !item.getName().equals("")) {
						System.out.println("上传文件的大小:" + item.getSize());
						System.out.println("上传文件的类型:" + item.getContentType());
						// item.getName()返回上传文件在客户端的完整路径名称
						System.out.println("上传文件的名称:" + item.getName());

						//File tempFile = new File(item.getName());
						//fileUrl       = AppConfig.STATIC_URL_PREFIX + tempFile.getName();
						
						String fileName = item.getName();
						String[] fileNameArr = fileName.split("\\.");
						SimpleDateFormat fmdate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
						String newFileName = fmdate.format(new Date()) + "." + fileNameArr[1];
						fileUrl  = AppConfig.STATIC_URL_PREFIX + newFileName;
						

						// 上传文件的保存路径
						//String realPath = this.getServletContext().getRealPath("/upload");// /WEB-INF/files
						//File file = new File(realPath, tempFile.getName());
						File file = new File(realPath, newFileName);
						item.write(file);
						System.out.println("上传文件成功！");
					} else {
						System.out.println("没有选择上传文件！");
					}
				}
			}
		} catch (FileUploadException e) {
			//e.printStackTrace();
			throw e;
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println( "上传文件失败！");
			throw e;
		}
		System.out.println("文件上传成功");
		return fileUrl;
	}
}
