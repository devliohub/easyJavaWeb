package com.tsjy.dwlgxy.bean;


public class DictData {

	public int    code;
	public String name;
	public long    count;
	
	
	public DictData(int code, String name) {
		this.code = code;
		this.name = name;
		this.count = 0;
	}
	
	public DictData(int code, String name, long count) {
		this.code = code;
		this.name = name;
		this.count = count;
	}

}	