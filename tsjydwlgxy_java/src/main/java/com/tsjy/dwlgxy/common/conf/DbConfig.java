package com.tsjy.dwlgxy.common.conf;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DbConfig 
{
	
	//public static final String jdbcUrl = "jdbc:mysql://localhost:3306/tsjydwlgxy?useSSL=false&characterEncoding=utf8";
	//public static final String jdbcUsername = "root";
	//public static final String jdbcPassword = "root";
	public static final String jdbcUrl = "jdbc:mysql://119.3.253.124:3306/dglg?autoReconnect=true&allowMultiQueries=true&useSSL=true&characterEncoding=utf8";
	public static final String jdbcUsername = "ulearningdb";
	public static final String jdbcPassword = "ulearning_2015";
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
	        
		DataSource ds = new HikariDataSource(config);
		return ds;
	}
	
	
	public static DataSource getPool() {
		return ds;
	}
	
	
}
