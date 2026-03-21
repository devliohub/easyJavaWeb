package com.tsjy.dwlgxy;

import java.io.File;
import java.net.URL;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

public class Main {

	public static void main(String[] args) throws Exception {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(Integer.getInteger("port", 8080));
		tomcat.getConnector();

		// 过时的Webapp部署方式
		// Context ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
		
		// 获取当前类的路径，用于动态定位资源目录
		URL classUrl = Main.class.getProtectionDomain().getCodeSource().getLocation();
		File classesDir = new File(classUrl.toURI());
		System.out.println("Classes directory: " + classesDir.getAbsolutePath());
		
		// 项目根目录 = classes目录的父目录的父目录
		File projectDir = classesDir.getParentFile().getParentFile();
		System.out.println("Project directory: " + projectDir.getAbsolutePath());
		
		// Webapp目录
		File webappDir = new File(projectDir, "src/main/webapp");
		System.out.println("Webapp directory: " + webappDir.getAbsolutePath());
		
		Context ctx = tomcat.addWebapp("", webappDir.getAbsolutePath());
		WebResourceRoot resources = new StandardRoot(ctx);

		// 过时的使用target/classes目录
		// resources.addPreResources(
		// 		new DirResourceSet(resources, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(), "/"));
		
		// 使用动态获取的classes目录
		resources.addPreResources(
				new DirResourceSet(resources, "/WEB-INF/classes", classesDir.getAbsolutePath(), "/"));
		ctx.setResources(resources);
		
		tomcat.start();
		tomcat.getServer().await();
	}
}
