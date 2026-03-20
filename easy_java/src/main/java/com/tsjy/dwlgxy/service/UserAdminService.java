package com.tsjy.dwlgxy.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.tsjy.dwlgxy.bean.UserAdmin;
import com.tsjy.dwlgxy.common.conf.DbConfig;
import com.tsjy.dwlgxy.common.utils.StringUtil;

public class UserAdminService 
{
	
	//
	public static UserAdmin getRow(long id) throws SQLException {
		UserAdmin obj = null;
		try (Connection conn = DbConfig.getPool().getConnection()) { 
			try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM user_admin WHERE id = ?")) {
				ps.setLong(1, id); 
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						obj = extractRow(rs);
					}
				}
			}
		}
		
		// TODO add cache
		return obj;
	}
	
	
	//
	public static UserAdmin getRow(String account) throws SQLException {
		UserAdmin obj = null;
		try (Connection conn = DbConfig.getPool().getConnection()) { 
			try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM user_admin WHERE account = ? and is_delete = 0")) {
				ps.setString(1, account); 
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						obj = extractRow(rs);
					}
				}
			}
		}
		
	   // TODO add cache
		return obj;
	}
	
	
	//
	public static List<UserAdmin> getRows(String where, String order, int pageNo, int pageSize) throws SQLException 
	{
		//
		List<UserAdmin> list = new ArrayList<>();
		
		
		//TODO repalce getOne list for cache arr  &  option:（pageKey pageValue pageSize+1翻页）
		int    offset = pageSize * (pageNo - 1);
		String limit  = offset + ", " + pageSize;
		try (Connection conn = DbConfig.getPool().getConnection()) { 
			String sql = String.format("SELECT * FROM user_admin WHERE %s  order by %s  limit %s",  where, order, limit);
			System.out.println(sql);
			// "SELECT * FROM user_admin WHERE ?  order by ?  limit ?"
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
//				ps.setString(1, where); 
//				ps.setString(2, order);
//				ps.setString(3, limit); 
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						list.add(extractRow(rs));
					}
				}
			}
		}
		
		return list;
	}
	
	
	//
	public static long getCount(String where) throws SQLException {
		//
		long count = 0;
		
		//TODO repalce exec 'show table status' get rows || set max const
		try (Connection conn = DbConfig.getPool().getConnection()) { 
			String sql = String.format("SELECT count(id) as count FROM user_admin WHERE %s", where);
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				 
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						count = rs.getLong("count");
					}
				}
			}
		}
		
		return count;
	}

	
	//
	public static UserAdmin extractRow(ResultSet rs) throws SQLException {
		UserAdmin obj = new UserAdmin();
		obj.id  = rs.getLong("id");
		obj.name  = rs.getString("name");
		obj.account  = rs.getString("account");
		obj.password  = rs.getString("password");
		String[] rolemenuids = StringUtil.splitString(rs.getString("rolemenuids"), ",") ;
		Arrays.sort(rolemenuids);
		obj.rolemenuids  = rolemenuids;
		obj.is_delete  = rs.getInt("is_delete");
		obj.create_time  = rs.getLong("create_time");
		obj.update_time  = rs.getLong("update_time");
		 
		return obj;
	}
	
	
	
	//
	public static long add(UserAdmin obj) throws SQLException {
		long id = 0;
		try (Connection conn = DbConfig.getPool().getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO user_admin (name, account, password, rolemenuids, is_delete, create_time) VALUES (?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS 
			)) {
				ps.setString(1, obj.name);
				ps.setString(2, obj.account);
				ps.setString(3, obj.password);
				ps.setString(4, String.join(",", obj.rolemenuids));
				ps.setInt(5, obj.is_delete);
				ps.setLong(6, obj.create_time);
				int n = ps.executeUpdate();
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						id = rs.getLong(1);
					}
				}
			}
		}
		
		// TODO _del cache
		return id;
	}
	
	
	//
	public static int update(UserAdmin obj) throws SQLException {
		int n =  0;
		try (Connection conn = DbConfig.getPool().getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement("UPDATE user_admin SET name=?, account=?, password=?, rolemenuids=?, update_time=? where id=?")) {
				ps.setString(1, obj.name);
				ps.setString(2, obj.account);
				ps.setString(3, obj.password);
				ps.setString(4, String.join(",", obj.rolemenuids));
				ps.setLong(5, obj.update_time);
				ps.setLong(6, obj.id);
				n = ps.executeUpdate();
			}
		}
		
		// TODO _del cache
		ArticleService.updateCUName(obj.id, obj.name);
		CourseService.updateCUName(obj.id, obj.name);
		return n;
	}
	
	
	
	//
	public static int updatePassWord(UserAdmin obj) throws SQLException {
		int n =  0;
		try (Connection conn = DbConfig.getPool().getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement("UPDATE user_admin SET password=?, update_time=? where id=?")) {
				ps.setString(1, obj.password);
				ps.setLong(2, obj.update_time);
				ps.setLong(3, obj.id);
				n = ps.executeUpdate();
			}
		}
		
		// TODO _del cache
		return n;
	}
	
	
	//
	public static int delete(long id, String account) throws SQLException {
		int n =  0;
		try (Connection conn = DbConfig.getPool().getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement("UPDATE user_admin SET  account=?, is_delete=?, update_time=? where id=?")) {
				ps.setString(1, account);
				ps.setInt(2, 1);
				ps.setLong(3, new Date().getTime() / 1000);
				ps.setLong(4, id);
				n = ps.executeUpdate();
			}
		}
		
		// TODO _del cache
		ArticleService.updateCUISDelete(id);
		CourseService.updateCUISDelete(id);
		return n;
	}
	

}
