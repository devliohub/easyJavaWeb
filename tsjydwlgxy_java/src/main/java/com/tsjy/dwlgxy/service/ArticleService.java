package com.tsjy.dwlgxy.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tsjy.dwlgxy.bean.*;
import com.tsjy.dwlgxy.service.*;
import com.tsjy.dwlgxy.common.conf.DbConfig;
import com.tsjy.dwlgxy.common.utils.StringUtil;


public class ArticleService 
{
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	static {
        //序列化的时候序列对象的所有属性
        mapper.setSerializationInclusion(Include.ALWAYS);

        //反序列化的时候如果多了其他属性,不抛出异常
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //如果是空对象的时候,不抛异常
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
        //mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }
	
	//
	public static Article getRow(int id) throws SQLException, JsonMappingException, JsonProcessingException {
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
	public static List<Article> getLimitRows(String where, String order, int limit) throws SQLException, JsonMappingException, JsonProcessingException 
	{
		//
		List<Article> list = new ArrayList<>();
		
		
		//TODO repalce getOne list for cache arr  
		try (Connection conn = DbConfig.getPool().getConnection()) { 
			String sql = String.format("SELECT * FROM article WHERE %s  order by %s  limit %s",  where, order, limit);
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
	public static List<Article> getRows(String where, String order, int pageNo, int pageSize) throws SQLException, JsonMappingException, JsonProcessingException 
	{
		//
		List<Article> list = new ArrayList<>();
		
		
		//TODO replace getOne list for cache arr  &  option:（pageKey pageValue pageSize+1翻页）
		int    offset = pageSize * (pageNo - 1);
		String limit  = offset + ", " + pageSize;
		try (Connection conn = DbConfig.getPool().getConnection()) { 
			String sql = String.format("SELECT * FROM article WHERE %s  order by %s  limit %s",  where, order, limit);
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
	public static Article extractRow(ResultSet rs) throws SQLException, JsonMappingException, JsonProcessingException {
		Article obj = new Article();
		obj.id  = rs.getLong("id");
		obj.title  = rs.getString("title");
		obj.content  = rs.getString("content");
		obj.attachment  = rs.getString("attachment");
		obj.menu_pid  = rs.getInt("menu_pid") ;
		obj.menu_id  = rs.getInt("menu_id") ;
		obj.is_top  = (rs.getInt("is_top") == 1 ? true : false) ;
		obj.cover  = rs.getString("cover");
		obj.is_delete  = rs.getInt("is_delete");
		obj.create_uid  = rs.getLong("create_uid");
		obj.create_uname  =  (rs.getInt("create_uis_delete") == 1 ? "已删除" : rs.getString("create_uname"));
		obj.create_time  = rs.getLong("create_time");
		obj.update_time  = rs.getLong("update_time");
		obj.publish_time  = rs.getLong("publish_time");
		
		Menu menuObj = MenuService.getRow(obj.menu_id);
		obj.menu_name =  menuObj != null ? menuObj.name : "";
    	
    	Menu menuPObj = MenuService.getRow(obj.menu_pid);
    	obj.menu_pname =  menuPObj != null ? menuPObj.name : "";
    	
    	if( StringUtil.valid(obj.attachment) )
    	{
            obj.attachmentArr = mapper.readValue(obj.attachment, new TypeReference<List<ArticleAttachmentData>>(){});
            for (ArticleAttachmentData pre : obj.attachmentArr) 
            {
            	pre.sizeFormat = StringUtil.getPrintSize(pre.size);
            }
            
            /*
            List<ArticleAttachmentData> attachmentArr = mapper.readValue(obj.attachment, List.class);
            List<ArticleAttachmentData> attachmentFormatArr = new ArrayList<>();
            for (int i=0; i < attachmentArr.size(); i++) 
            {
            	ArticleAttachmentData pre = mapper.readValue( mapper.writeValueAsString( attachmentArr.get(i) ), ArticleAttachmentData.class );
            	pre.sizeFormat = StringUtil.getPrintSize(pre.size);
            	
            	attachmentFormatArr.add(pre);
            }
            obj.attachmentArr = attachmentFormatArr;
            */
    	}
    	else 
    	{
    		obj.attachmentArr = new ArrayList<>();
    	}
    	
		 
		return obj;
	}
	
	
	
	//
	public static long add(Article obj) throws SQLException {
		long id = 0;
		try (Connection conn = DbConfig.getPool().getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO Article (title, content, attachment, menu_pid, menu_id, is_top, cover, create_uid, create_uname, create_time, publish_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS 
			)) {
				ps.setString(1, obj.title);
				ps.setString(2, obj.content);
				ps.setString(3, obj.attachment);
				ps.setInt(4, obj.menu_pid);
				ps.setInt(5, obj.menu_id);
				ps.setInt(6, (obj.is_top ? 1 : 0));
				ps.setString(7, obj.cover);
				ps.setLong(8, obj.create_uid);
				ps.setString(9, obj.create_uname);
				ps.setLong(10, obj.create_time);
				ps.setLong(11, obj.publish_time);
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
			try (PreparedStatement ps = conn.prepareStatement("UPDATE article SET title=?, content=?, attachment=?, menu_pid=?, menu_id=?, is_top=?, cover=?, update_time=?, publish_time=? where id=?")) {
				ps.setString(1, obj.title);
				ps.setString(2, obj.content);
				ps.setString(3, obj.attachment);
				ps.setInt(4, obj.menu_pid);
				ps.setInt(5, obj.menu_id);
				ps.setInt(6, (obj.is_top ? 1 : 0));
				ps.setString(7, obj.cover);
				ps.setLong(8, obj.update_time);
				ps.setLong(9, obj.publish_time);
				ps.setLong(10, obj.id);
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
	
	
	//
	public static int updateCUName(long create_uid, String create_uname) throws SQLException {
		int n =  0;
		try (Connection conn = DbConfig.getPool().getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement("UPDATE article SET create_uname=? where create_uid=?")) {
				ps.setString(1, create_uname);
				ps.setLong(2, create_uid);
				n = ps.executeUpdate();
			}
		}
		
		// TODO _del cache
		return n;
	}
	
	
	//
	public static int updateCUISDelete(long create_uid) throws SQLException {
		int n =  0;
		try (Connection conn = DbConfig.getPool().getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement("UPDATE article SET create_uis_delete=? where create_uid=?")) {
				ps.setInt(1, 1);
				ps.setLong(2, create_uid);
				n = ps.executeUpdate();
			}
		}
		
		// TODO _del cache
		return n;
	}

}
