package com.tsjy.dwlgxy.common.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DbConfig 
{
	
	
//	public static final String jdbcUrl = "jdbc:mysql://localhost:3306/tsjydwlgxy?useSSL=false&characterEncoding=utf8";
//	public static final String jdbcUsername = "root";
//	public static final String jdbcPassword = "root";
	
	
//	public static final String jdbcUrl = "jdbc:mysql://119.3.253.124:3306/dglg?autoReconnect=true&allowMultiQueries=true&useSSL=true&useUnicode=true&characterEncoding=utf8";
//	public static final String jdbcUsername = "ulearningdb";
//	public static final String jdbcPassword = "ulearning_2015";
	
	
	
//	public static final String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/dglg?autoReconnect=true&allowMultiQueries=true&useSSL=true&useUnicode=true&characterEncoding=utf8";
//	public static final String jdbcUsername = "wenhua";
//	public static final String jdbcPassword = "wenhua_20210915";
	
	
	
	public static  String jdbcUrl;
	public static  String jdbcUsername;
	public static  String jdbcPassword;
	
	
	static {
		try {
			Properties properties = new Properties();
			// 使用ClassLoader加载properties配置文件生成对应的输入流
			InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("application.properties");
			// 使用properties对象加载输入流
			properties.load(in);
			//获取key对应的value值
			jdbcUrl = properties.getProperty("jdbcUrl");
			jdbcUsername = properties.getProperty("jdbcUsername");
			jdbcPassword = properties.getProperty("jdbcPassword");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static final DataSource ds = _initPool();
	private static DataSource _initPool() {
		HikariConfig config = new HikariConfig();
		
		config.setJdbcUrl(jdbcUrl);
		config.setUsername(jdbcUsername);
		config.setPassword(jdbcPassword);
		// Weird thing is my code works fine in eclipse, 
		// but fails as a tomcat webapp. Calling HikariConfig.setDriverClassName() made it work in all situations 
		config.setDriverClassName("org.gjt.mm.mysql.Driver"); //alternative is Class.forName("org.gjt.mm.mysql.Driver")
		
		config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        
        //config.addDataSourceProperty("useUnicode", "true");
        //config.addDataSourceProperty("characterEncoding", "utf8");
	        
		DataSource ds = new HikariDataSource(config);
		return ds;
	}
	
	
	public static DataSource getPool() {
		return ds;
	}
	
	
}
