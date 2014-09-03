package com.taobao.guangjie.action.search;

import java.io.IOException;

import com.taobao.guangjie.action.BaseAction;
import com.taobao.guangjie.dataobject.BaseResult;
import com.taobao.guangjie.dataobject.Constants;
import com.taobao.guangjie.util.SearchEngineInterface;

/**
 * 主界面搜索入口
 * 
 * 转发用户搜索请求到搜索引擎，并返回数据json 给客户端
 */
public class SearchTagAction extends BaseAction {

	private static final long serialVersionUID = 180518969991710655L;
	public static final String api = "com.taobao.guangjie.searchapi.searchTag";

	private String key;

	@Override
	public String execute() throws IOException {

		key = new String(key.getBytes("ISO-8859-1"), "UTF-8");
		String searchResultJson = null;

		if (key != null && key.length() != 0) {
			searchResultJson = SearchEngineInterface.query(key);
			sendJson(searchResultJson);
		} else {
			BaseResult<String> result = new BaseResult<String>(api, "1.0",
					Constants.API_ERROR);
			sendJson(result);
		}

		return null;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
