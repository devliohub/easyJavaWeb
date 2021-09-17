package com.tsjy.dwlgxy.bean;


public class DictData {

	public int     code;
	public String  name;
	public long    count;
	public String  iconName;
	
	
	public DictData(int code, String name) {
		this.code = code;
		this.name = name;
		this.count = 0;
		this.iconName = "";
	}
	
	public DictData(int code, String name, long count) {
		this.code = code;
		this.name = name;
		this.count = count;
		this.iconName = "";
	}
	
	
	public DictData(int code, String name, long count, String iconName) {
		this.code = code;
		this.name = name;
		this.count = count;
		this.iconName = iconName;
	}

}	