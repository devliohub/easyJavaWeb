package com.tsjy.dwlgxy.bean;


public class ArticleAttachmentData {

	public String fileName;
	public long   size;
	public String sizeFormat;
	public String fileUrl;
	
	
	public ArticleAttachmentData() {  }
	
	public ArticleAttachmentData(String fileName, long size, String fileUrl) {
		this.fileName = fileName;
		this.size     = size;
		this.fileUrl  = fileUrl;
	}

	
}	