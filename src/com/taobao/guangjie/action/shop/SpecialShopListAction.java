package com.taobao.guangjie.action.shop;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.guangjie.action.BaseAction;
import com.taobao.guangjie.dataobject.BaseResult;
import com.taobao.guangjie.dataobject.Constants;
import com.taobao.guangjie.dataobject.ShopListDO;
import com.taobao.guangjie.service.ShopService;
import com.taobao.guangjie.service.impl.ShopServiceImpl;

/**
 * 客户端首页“特色店铺”模块显示的内容
 * 
 * 分页加载，注意图片加载
 */
public class SpecialShopListAction extends BaseAction {

	private static final long serialVersionUID = -1169568977030949955L;

	public static final String api = "com.taobao.guangjie.shopapi.getSpecialShopList";

	private int num = -1;
	private int page = -1;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	private ShopService shopService = new ShopServiceImpl();

	@Override
	public String execute() throws IOException {
		Logger logger = LoggerFactory.getLogger(SpecialShopListAction.class);
		BaseResult<ShopListDO> result = new BaseResult<ShopListDO>(api);
		if (num <= 0 || page <= 0) {
			result.getRet().remove(0);
			result.getRet().add(Constants.API_ERROR_WRONG_PARAM);
			logger.error("page or num is empty");
			sendJson(result);
			return null;
		}

		ShopListDO shopListDO = shopService.getSpecialShopList(num, page);
		result.setData(shopListDO);
		
		if (shopListDO.getCouponList().size() == 0) {
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
