package com.tsjy.dwlgxy.bean;

import java.util.List;

public class PageData<T> {

	public long total;
	public List<T> list;
	public int layout;
	
	
	 public PageData(long total, List<T> list) {
		this.total = total;
		this.list = list;
		this.layout = 0;
	}
	 
	 
	 public PageData(long total, List<T> list, int layout) {
		this.total = total;
		this.list = list;
		this.layout = layout;
	 }
}	