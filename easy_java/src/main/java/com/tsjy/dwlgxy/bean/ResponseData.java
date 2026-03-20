package com.tsjy.dwlgxy.bean;

public class ResponseData {

	public int errno;
	public String errmsg;
	public String usermsg;
	public Object result;
	
	public ResponseData(int errno, String errmsg, String usermsg) {
		this.errno = errno;
		this.errmsg = errmsg;
		this.usermsg = usermsg;
	}
}	