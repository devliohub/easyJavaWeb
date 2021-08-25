package com.tsjy.dwlgxy.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.tsjy.dwlgxy.bean.*;
import com.tsjy.dwlgxy.common.conf.DbConfig;
import com.tsjy.dwlgxy.common.utils.StringUtil;

public class CourseService 
{
	
	//
	public static Course getRow(int id) throws SQLException {
		Course obj = null;
		try (Connection conn = DbConfig.getPool().getConnection()) { 
			try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM course WHERE id = ?")) {
				ps.setInt(1, id); 
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
	public static List<Course> getRows(String where, String order, int pageNo, int pageSize) throws SQLException 
	{
		//
		List<Course> list = new ArrayList<>();
		
		
		//TODO replace getOne list for cache arr  &  option:（pageKey pageValue pageSize+1翻页）
		int    offset = pageSize * (pageNo - 1);
		String limit  = offset + ", " + pageSize;
		try (Connection conn = DbConfig.getPool().getConnection()) { 
			String sql = String.format("SELECT * FROM course WHERE %s  order by %s  limit %s",  where, order, limit);
			try (PreparedStatement ps = conn.prepareStatement(sql)) { 
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
			String sql = String.format("SELECT count(id) as count FROM course WHERE %s", where);
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
	public static Course extractRow(ResultSet rs) throws SQLException {
		Course obj = new Course();
		obj.id  = rs.getLong("id");
		obj.name  = rs.getString("name");
		obj.url  = rs.getString("url");
		obj.module_id  = rs.getInt("module_id");
		obj.type_id  = rs.getInt("type_id") ;
		obj.cover  = rs.getString("cover");
		obj.is_delete  = rs.getInt("is_delete");
		obj.create_uid  = rs.getLong("create_uid");
		obj.create_uname  = rs.getString("create_uname");
		obj.create_time  = rs.getLong("create_time");
		obj.update_time  = rs.getLong("update_time");
		 
		return obj;
	}
	
	
	
	//
	public static long add(Course obj) throws SQLException {
		long id = 0;
		try (Connection conn = DbConfig.getPool().getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO course (name, url, module_id, type_id, cover, create_uid, create_uname, create_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS 
			)) {
				ps.setString(1, obj.name);
				ps.setString(2, obj.url);
				ps.setInt(3, obj.module_id);
				ps.setInt(4, obj.type_id);
				ps.setString(5, obj.cover);
				ps.setLong(6, obj.create_uid);
				ps.setString(7, obj.create_uname);
				ps.setLong(8, obj.create_time);
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
	public static int update(Course obj) throws SQLException {
		int n =  0;
		try (Connection conn = DbConfig.getPool().getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement("UPDATE course SET name=?, url=?, module_id=?, type_id=?, cover=?, update_time=? where id=?")) {
				ps.setString(1, obj.name);
				ps.setString(2, obj.url);
				ps.setInt(3, obj.module_id);
				ps.setInt(4, obj.type_id);
				ps.setString(5, obj.cover);
				ps.setLong(6, obj.update_time);
				ps.setLong(7, obj.id);
				n = ps.executeUpdate();
			}
		}
		
		// TODO _del cache
		return n;
	}
	
	
	//
	public static int delete(long id) throws SQLException {
		int n =  0;
		try (Connection conn = DbConfig.getPool().getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement("UPDATE course SET is_delete=?, update_time=? where id=?")) {
				ps.setInt(1, 1);
				ps.setLong(2, new Date().getTime() / 1000);
				ps.setLong(3, id);
				n = ps.executeUpdate();
			}
		}
		
		// TODO _del cache
		return n;
	}
	

}
