package com.taobao.guangjie.action.shop;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.guangjie.action.BaseAction;
import com.taobao.guangjie.dataobject.BaseResult;
import com.taobao.guangjie.dataobject.Constants;
import com.taobao.guangjie.service.ShopService;
import com.taobao.guangjie.service.impl.ShopServiceImpl;

/**
 * 获取“商铺详情”页面的信息
 */
public class ShopInfoAction extends BaseAction {

	private static final long serialVersionUID = -6264053133748277020L;

	public static final String api = "com.taobao.guangjie.shopapi.getShopInfo";

	private String shopid;

	public String getShopid() {
		return shopid;
	}

	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	
	private ShopService shopService = new ShopServiceImpl();

	@Override
	public String execute() throws Exception {
		Logger logger = LoggerFactory.getLogger(ShopInfoAction.class);
		BaseResult<Map<String, String>> result = new BaseResult<Map<String, String>>(
				api);
		if (shopid == null || shopid.length() == 0) {
			result.getRet().remove(0);
			result.getRet().add(Constants.API_ERROR_WRONG_PARAM);
			logger.error("shopid is empty");
			sendJson(result);
			return null;
		}
		
		Map<String, String> data = shopService.getShopInfo(shopid);
		
		if (data == null) {
			result.getRet().remove(0);
			result.getRet().add(Constants.API_ERROR_NO_DATA);
			logger.error("no data");
		} else {
			result.setData(data);
		}

		sendJson(result);
		return null;
	}

}
