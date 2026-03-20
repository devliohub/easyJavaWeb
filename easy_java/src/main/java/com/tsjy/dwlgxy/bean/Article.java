package com.tsjy.dwlgxy.bean;

import java.util.List;

public class Article {

	public long id;
	public String title;
	public String title_url;
	public String content;
	public String attachment;
	public List<ArticleAttachmentData> attachmentArr;
	public int menu_pid;
	public String menu_pname;
	public int menu_id;
	public String menu_name;
	public boolean is_top;
	public String cover;
	public boolean is_attachment_down;
	public int is_delete;
	public long create_uid;
	public String create_uname;
	public long create_time;
	public long update_time;
	public long publish_time;
}