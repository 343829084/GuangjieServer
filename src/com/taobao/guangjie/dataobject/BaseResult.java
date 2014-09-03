package com.taobao.guangjie.dataobject;

import java.util.ArrayList;

/**
 * 返回JSON数据基础类
 * 
 * 所有返回给客户端的数据都使用这个类，通过fastjson转换为 json字符串传递给客户端，由客户端进行解析。data域作为数据实际存储的字段。
 */

public class BaseResult<T> {

	private String api;
	private String v;
	private ArrayList<String> ret;
	private T data;

	public BaseResult(String api, String v, String msg) {
		ret = new ArrayList<String>();
		this.api = api;
		this.v = v;
		this.ret.add(msg);
	}

	public BaseResult(String api, String v) {
		this(api, v, Constants.API_SUCCESS);
	}

	public BaseResult(String api) {
		this(api, "1.0");
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public ArrayList<String> getRet() {
		return ret;
	}

	public void setRet(ArrayList<String> ret) {
		this.ret = ret;
	}

	public Object getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
