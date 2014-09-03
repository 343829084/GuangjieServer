package com.taobao.guangjie.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionSupport;
import com.taobao.guangjie.dataobject.BaseResult;

/**
 * Action基础类
 * 
 * 其他用于客户端访问的API类Action需要继承自这个类 web端的action不需要继承这个类
 */
public class BaseAction extends ActionSupport {

	private static final long serialVersionUID = -6954125644160091831L;

	@SuppressWarnings("rawtypes")
	public void sendJson(BaseResult result) throws IOException {

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/javascript;charset=utf-8");
		PrintWriter out = response.getWriter();
		String jsonString = JSON.toJSONString(result);
		out.println(jsonString);
		out.flush();
		out.close();

	}

	public void sendJson(String json) throws IOException {

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/javascript;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(json);
		out.flush();
		out.close();

	}

}
