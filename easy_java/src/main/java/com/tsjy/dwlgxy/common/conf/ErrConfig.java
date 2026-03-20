package com.tsjy.dwlgxy.common.conf;

import java.util.*;
import com.tsjy.dwlgxy.bean.*;
import com.tsjy.dwlgxy.common.utils.*;


public class ErrConfig {
	public final static int STATUS_OK = 200;
	public final static int  BAD_REQUEST = 400;          // req param illeage
	public final static int  UNAUTHORIZED = 401;         // non right
	public final static int  ACTSCTRL = 402;             // actsctrl
	public final static int  FORBIDDEN = 403;            // forbidden by antispam
	public final static int  SIGNATURE_INVALID = 405;    // 签名验证失败
	public final static int  LOGIN_INVALID = 406;        // 登录验证失败
//    public final static int  NOT_COMPATIBLE = 410;     // 版本不兼容
//    public final static int  VERSION_TOO_LOW = 420;    // 版本过低
	public final static int UID_INVALID = 430;           // uid invalid need re-login
	public final static int NOT_LOGIN = 431;         	 // 未登录
	public final static int WECHAT_NOT_LOGIN = 432;      // 微信未登录
	public final static int MOBILE_NOT_LOGIN = 433;       // 手机未登录
	public final static int INTERNAL_SERVER_ERROR = 500;  // server error
	public final static int SERVICE_UNAVAILABLE = 503;    // 主动拒绝服务
	public final static int WRITING_ERROR = 1000;         // 提示性错误


	
	private static Map<Integer, ResponseData> errConf = new HashMap<Integer, ResponseData>() {
		{
			put(STATUS_OK, new ResponseData(STATUS_OK, "SUCC", ""));
			put(BAD_REQUEST, new ResponseData(BAD_REQUEST, "param illege", "参数错误"));
			put(UNAUTHORIZED, new ResponseData(UNAUTHORIZED, "no right", ""));
			put(ACTSCTRL, new ResponseData(ACTSCTRL, "forbidden by actsctrl", "操作太频繁了, 请稍后再试"));
			put(FORBIDDEN, new ResponseData(FORBIDDEN, "forbidden by antispam", "含有不合适的词，改一下吧"));
			put(SIGNATURE_INVALID, new ResponseData(SIGNATURE_INVALID, "signature invalid", "签名验证失败"));
			put(LOGIN_INVALID, new ResponseData(LOGIN_INVALID, "login invalid", "用户名或密码不正确"));
			put(UID_INVALID, new ResponseData(UID_INVALID, "uid invalid ,need call /sys/login", ""));
			put(NOT_LOGIN, new ResponseData(NOT_LOGIN, "not login", "请重新登录"));
			put(WECHAT_NOT_LOGIN, new ResponseData(WECHAT_NOT_LOGIN, "wechat logon", "微信未认证"));
			put(MOBILE_NOT_LOGIN, new ResponseData(MOBILE_NOT_LOGIN, "mobile logon", "手机未认证"));
			put(INTERNAL_SERVER_ERROR, new ResponseData(INTERNAL_SERVER_ERROR, "", "服务错误"));
			put(SERVICE_UNAVAILABLE, new ResponseData(SERVICE_UNAVAILABLE, "service unavailable, reject", "抱歉，服务器累趴了"));
			put(WRITING_ERROR, new ResponseData(WRITING_ERROR, "writing error", "网络错误"));
		}
	};
	 

    public static Map<Integer, ResponseData>  getAllErr() {
        return errConf;
    }

    public static  ResponseData getErr(int errno, String usermsg) {
    	ResponseData arrErr = errConf.containsKey(errno)  ?  errConf.get(errno) : errConf.get(SERVICE_UNAVAILABLE);

        if( StringUtil.checkStringNotNull(usermsg) ) {
        	arrErr.usermsg = usermsg;
        }
       
        return arrErr;
    }
}
