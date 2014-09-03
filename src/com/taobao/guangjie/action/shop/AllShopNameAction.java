package com.taobao.guangjie.action.shop;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.guangjie.action.BaseAction;
import com.taobao.guangjie.dataobject.BaseResult;
import com.taobao.guangjie.dataobject.Constants;
import com.taobao.guangjie.dataobject.ShopNameDO;
import com.taobao.guangjie.service.ShopService;
import com.taobao.guangjie.service.impl.ShopServiceImpl;

/**
 * 获取全部的店铺列表信息
 * 
 * 用于主界面搜索框的输入自动补全功能
 */
public class AllShopNameAction extends BaseAction {

	private static final long serialVersionUID = -6642668101343655261L;
	public static final String api = "com.taobao.guangjie.shopapi.getAllShopNames";
	private ShopService shopService = new ShopServiceImpl();

	@Override
	public String execute() throws IOException {

		Logger logger = LoggerFactory.getLogger(AllShopNameAction.class);
		BaseResult<List<ShopNameDO>> result = new BaseResult<List<ShopNameDO>>(
				api);
		
		List<ShopNameDO> shopList = shopService.getAllShopNames();
		result.setData(shopList);

		if (shopList.size() == 0) {
			result.getRet().remove(0);
			result.getRet().add(Constants.API_INFO_END);
			logger.info("no more data");
			sendJson(result);
			return null;
		}

		sendJson(result);
		return null;
	}

}
