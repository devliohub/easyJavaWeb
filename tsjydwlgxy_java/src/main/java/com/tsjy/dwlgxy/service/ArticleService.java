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
import com.tsjy.dwlgxy.service.*;
import com.tsjy.dwlgxy.common.conf.DbConfig;
import com.tsjy.dwlgxy.common.utils.StringUtil;


public class ArticleService 
{
	
	//
	public static Article getRow(int id) throws SQLException {
		Article obj = null;
		try (Connection conn = DbConfig.getPool().getConnection()) { 
			try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Article WHERE id = ?")) {
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
	public static List<Article> getRows(String where, String order, int pageNo, int pageSize) throws SQLException 
	{
		//
		List<Article> list = new ArrayList<>();
		
		
		//TODO replace getOne list for cache arr  &  option:（pageKey pageValue pageSize+1翻页）
		int    offset = pageSize * (pageNo - 1);
		String limit  = offset + ", " + pageSize;
		try (Connection conn = DbConfig.getPool().getConnection()) { 
			String sql = String.format("SELECT * FROM article WHERE %s  order by %s  limit %s",  where, order, limit);
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						list.add(extractRow(rs));
					}
				}
			}
		}
		
		//#outList for dynamic column
        List<Article> outList = new ArrayList<>();
        for (Article pre : list) {
        	Menu menuObj = MenuService.getRow(pre.menu_id);
        	pre.menu_name =  menuObj != null ? menuObj.name : "";
        	
        	Menu menuPObj = MenuService.getRow(pre.menu_pid);
        	pre.menu_pname =  menuPObj != null ? menuPObj.name : "";
        }
		
		return list;
	}
	
	
	//
	public static long getCount(String where) throws SQLException {
		//
		long count = 0;
		
		//TODO replace exec 'show table status' get rows || set max const
		try (Connection conn = DbConfig.getPool().getConnection()) { 
			String sql = String.format("SELECT count(id) as count FROM article WHERE %s", where);
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
	public static Article extractRow(ResultSet rs) throws SQLException {
		Article obj = new Article();
		obj.id  = rs.getLong("id");
		obj.title  = rs.getString("title");
		obj.content  = rs.getString("content");
		obj.attachment  = rs.getString("attachment");
		obj.menu_pid  = rs.getInt("menu_pid") ;
		obj.menu_id  = rs.getInt("menu_id") ;
		obj.is_top  = rs.getInt("is_top") ;
		obj.cover  = rs.getString("cover");
		obj.is_delete  = rs.getInt("is_delete");
		obj.create_uid  = rs.getLong("create_uid");
		obj.create_uname  = rs.getString("create_uname");
		obj.create_time  = rs.getLong("create_time");
		obj.update_time  = rs.getLong("update_time");
		 
		return obj;
	}
	
	
	
	//
	public static long add(Article obj) throws SQLException {
		long id = 0;
		try (Connection conn = DbConfig.getPool().getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO Article (title, content, attachment, menu_pid, menu_id, is_top, cover, create_uid, create_uname, create_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS 
			)) {
				ps.setString(1, obj.title);
				ps.setString(2, obj.content);
				ps.setString(3, obj.attachment);
				ps.setInt(4, obj.menu_pid);
				ps.setInt(5, obj.menu_id);
				ps.setInt(6, obj.is_top);
				ps.setString(7, obj.cover);
				ps.setLong(8, obj.create_uid);
				ps.setString(9, obj.create_uname);
				ps.setLong(10, obj.create_time);
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
	public static int update(Article obj) throws SQLException {
		int n =  0;
		try (Connection conn = DbConfig.getPool().getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement("UPDATE article SET title=?, content=?, attachment=?, menu_pid=?, menu_id=?, is_top=?, cover=?, update_time=? where id=?")) {
				ps.setString(1, obj.title);
				ps.setString(2, obj.content);
				ps.setString(3, obj.attachment);
				ps.setInt(4, obj.menu_pid);
				ps.setInt(5, obj.menu_id);
				ps.setInt(6, obj.is_top);
				ps.setString(7, obj.cover);
				ps.setLong(8, obj.update_time);
				ps.setLong(9, obj.id);
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
			try (PreparedStatement ps = conn.prepareStatement("UPDATE article SET is_delete=?, update_time=? where id=?")) {
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
