package com.taobao.guangjie.dataobject;

import com.taobao.guangjie.dl.MongoDL;

/**
 * 保存系统需要的主要常量
 * 
 * 主要包括API返回信息，静态文件url路径
 */
public class Constants {

	public static final String API_SUCCESS = "SUCCESS::调用成功";
	public static final String API_ERROR = "ERROR::系统错误";
	public static final String API_ERROR_NO_DATA = "ERROR::数据不存在或没有查询到";
	public static final String API_ERROR_WRONG_PARAM = "ERROR::请求参数错误";
	public static final String API_INFO_END = "INFO::没有更多数据了";
	public static final String LOGO_BASE_URL = "http://" + MongoDL.IP
			+ "/static/logo/";
	public static final String BIGPIC_BASE_URL = "http://" + MongoDL.IP
			+ "/static/bigpic/";
	public static final String ITEMPIC_BASE_URL = "http://" + MongoDL.IP
			+ "/static/itempic/";
	public static final String NODE_JS_BASE_URL = "http://" + MongoDL.IP
			+ ":3000";
	public static final String INCITY_POINTS = "point.json";
	public static final String INCITY_EDGE = "edge.json";	
	// public static final String LOGO_BASE_URL =
	// "http://dogeguangjie.qiniudn.com/";
	// public static final String BIGPIC_BASE_URL =
	// "http://dogeguangjie.qiniudn.com/";
	// public static final String ITEMPIC_BASE_URL =
	// "http://dogeguangjie.qiniudn.com/";
}
