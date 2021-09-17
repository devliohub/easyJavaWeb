package com.tsjy.dwlgxy.common.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig 
{
	
	//public final static String STATIC_UPLOAD_PATH = "C:\\Users\\87537\\eclipse-workspace\\z-tsjy-dwlgxy\\upload"; 
	//public final static String STATIC_UPLOAD_PATH = "/app/tomcat8/tomcat-dglg-9098/webapps/upload"; 
	public static String STATIC_UPLOAD_PATH;
	
	//public final static String STATIC_URL_PREFIX = "http://localhost/upload/"; 
	//public final static String STATIC_URL_PREFIX = "http://121.36.22.149:9098/upload/"; 
	//public final static String STATIC_URL_PREFIX = "https://dglg.tongshike.cn/upload/"; 
	public static String STATIC_URL_PREFIX;
	
	
	static {
		try {
			Properties properties = new Properties();
			// 使用ClassLoader加载properties配置文件生成对应的输入流
			InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("application.properties");
			// 使用properties对象加载输入流
			properties.load(in);
			//获取key对应的value值
			STATIC_UPLOAD_PATH = properties.getProperty("staticUploadPath");
			STATIC_URL_PREFIX = properties.getProperty("staticUrlPrefix");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
