package com.tsjy.dwlgxy.bean;

import java.util.List;

public class Menu {

	public int id;
	public int pid;
	public String name;
	public int type;
	public int layout;
	public String url;
	public long sortnum;
	public int is_delete;
	public long create_time;
	public long update_time;
	public List<Menu> sub_menus;
	public boolean is_course_menu;
}