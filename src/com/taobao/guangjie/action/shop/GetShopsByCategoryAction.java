package com.taobao.guangjie.action.shop;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.guangjie.action.BaseAction;
import com.taobao.guangjie.dataobject.BaseResult;
import com.taobao.guangjie.dataobject.Constants;
import com.taobao.guangjie.dataobject.ShopListByCategoryDO;
import com.taobao.guangjie.service.ShopService;
import com.taobao.guangjie.service.impl.ShopServiceImpl;

/**
 * 获取某一类目的所有店铺信息
 * 
 * 在客户端“品牌类目”模块加载某一类目所对应的的商铺列表
 */
public class GetShopsByCategoryAction extends BaseAction {

	private static final long serialVersionUID = 2021400900885234891L;

	public static final String api = "com.taobao.guangjie.shopapi.getShopsByCategory";

	private String categoryId;
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

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	private ShopService shopService = new ShopServiceImpl();

	@Override
	public String execute() throws IOException {
		

		Logger logger = LoggerFactory.getLogger(GetShopsByCategoryAction.class);
		BaseResult<ShopListByCategoryDO> result = new BaseResult<ShopListByCategoryDO>(
				api);
		if (num <= 0 || page <= 0 || categoryId == null
				|| categoryId.length() == 0) {
			result.getRet().remove(0);
			result.getRet().add(Constants.API_ERROR_WRONG_PARAM);
			logger.error("error: page or num is empty");
			sendJson(result);
			return null;
		}
		ShopListByCategoryDO shopListByCategoryDO = shopService.getShopsByCategory(categoryId, num, page);
		result.setData(shopListByCategoryDO);
		
		if (shopListByCategoryDO == null) {
			result.getRet().remove(0);
			result.getRet().add(Constants.API_ERROR_WRONG_PARAM);
			logger.error("page or num is empty 2");
			sendJson(result);
		}

		if (shopListByCategoryDO.getShopList().size() == 0) {
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
