package com.tsjy.dwlgxy.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.tsjy.dwlgxy.bean.*;
import com.tsjy.dwlgxy.common.conf.DbConfig;
import com.tsjy.dwlgxy.common.utils.StringUtil;

public class BannerService 
{
	
	//
	public static Banner getRow(int id) throws SQLException {
		Banner obj = null;
		try (Connection conn = DbConfig.getPool().getConnection()) { 
			try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM banner WHERE id = ?")) {
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
	public static List<Banner> getLimitRows(String where, String order, int limit) throws SQLException 
	{
		//
		List<Banner> list = new ArrayList<>();
		
		
		//TODO repalce getOne list for cache arr  
		try (Connection conn = DbConfig.getPool().getConnection()) { 
			String sql = String.format("SELECT * FROM banner WHERE %s  order by %s  limit %s",  where, order, limit);
			System.out.println(sql);
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
	public static List<Banner> getRows(String where, String order) throws SQLException 
	{
		//
		List<Banner> list = new ArrayList<>();
		
		
		//TODO replace getOne list for cache arr  
		try (Connection conn = DbConfig.getPool().getConnection()) { 
			String sql = String.format("SELECT * FROM banner WHERE %s  order by %s",  where, order);
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
	public static long getCount() throws SQLException {
		//
		long count = 0;
		
		//TODO replace exec 'show table status' get rows || set max cons
		try (Connection conn = DbConfig.getPool().getConnection()) { 
			try (PreparedStatement ps = conn.prepareStatement("SELECT count(id) as count FROM banner")) {
				 
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
	public static Banner extractRow(ResultSet rs) throws SQLException {
		Banner obj = new Banner();
		obj.id  = rs.getInt("id");
		obj.img  = rs.getString("img");
		obj.url  = rs.getString("url");
		obj.sortnum  = rs.getLong("sortnum");
		obj.is_delete  = rs.getInt("is_delete");
		obj.create_time  = rs.getLong("create_time");
		obj.update_time  = rs.getLong("update_time");
		 
		return obj;
	}
	
	
	
	//
	public static long add(Banner obj) throws SQLException {
		long id = 0;
		try (Connection conn = DbConfig.getPool().getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO banner (img, url, sortnum, create_time) VALUES (?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS 
			)) {
				ps.setString(1, obj.img);
				ps.setString(2, obj.url);
				ps.setLong(3, obj.sortnum);
				ps.setLong(4, obj.create_time);
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
	public static int update(Banner obj) throws SQLException {
		int n =  0;
		try (Connection conn = DbConfig.getPool().getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement("UPDATE banner SET img=?, url=?, update_time=? where id=?")) {
				ps.setString(1, obj.img);
				ps.setString(2, obj.url);
				ps.setLong(5, obj.update_time);
				ps.setInt(6, obj.id);
				n = ps.executeUpdate();
			}
		}
		
		// TODO _del cache
		return n;
	}
	
	
	
	//
	public static int updateSortNum(Banner obj) throws SQLException {
		int n =  0;
		try (Connection conn = DbConfig.getPool().getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement("UPDATE banner SET sortnum=?, update_time=? where id=?")) {
				ps.setLong(1, obj.sortnum);
				ps.setLong(2, obj.update_time);
				ps.setInt(3, obj.id);
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
			try (PreparedStatement ps = conn.prepareStatement("UPDATE banner SET is_delete=?, update_time=? where id=?")) {
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
