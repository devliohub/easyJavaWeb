package com.tsjy.dwlgxy.bean;

import java.util.List;
import java.util.Map;

public class HomeArticleData {

	public int menuPId;
	public int menuId;
	public String menuName;
	public List<Article>  list;
	
	
	public HomeArticleData(int menuPId, int menuId, String menuName, List<Article>  list) {
		this.menuPId = menuPId;
		this.menuId = menuId;
		this.menuName = menuName;
		this.list = list;
	}

}	