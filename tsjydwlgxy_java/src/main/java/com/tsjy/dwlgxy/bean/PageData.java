package com.tsjy.dwlgxy.bean;

import java.util.List;

public class PageData<T> {

	public long total;
	public List<T> list;
	
	 public PageData(long total, List<T> list) {
		this.total = total;
		this.list = list;
	}
}	