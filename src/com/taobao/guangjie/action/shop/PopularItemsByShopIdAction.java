package com.taobao.guangjie.action.shop;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.guangjie.action.BaseAction;
import com.taobao.guangjie.dataobject.BaseResult;
import com.taobao.guangjie.dataobject.Constants;
import com.taobao.guangjie.dataobject.ItemListDO;
import com.taobao.guangjie.service.ItemService;
import com.taobao.guangjie.service.impl.ItemServiceImpl;

/**
 * 获取某一店铺的热销商品列表
 * 
 * 用于客户端“商铺详情”页面的下方
 */
public class PopularItemsByShopIdAction extends BaseAction {

	private static final long serialVersionUID = -6238203418880461586L;

	public static final String api = "com.taobao.guangjie.shopapi.getPopularItemsByShopId";

	private int num = -1;
	private int page = -1;
	private String shopId = null;

	public int getNum() {
		return num;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
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
	
	private ItemService itemService = new ItemServiceImpl();

	@Override
	public String execute() throws IOException {
		Logger logger = LoggerFactory
				.getLogger(PopularItemsByShopIdAction.class);
		BaseResult<ItemListDO> result = new BaseResult<ItemListDO>(api);
		if (num <= 0 || page <= 0 || shopId == null || shopId.length() == 0) {
			result.getRet().remove(0);
			result.getRet().add(Constants.API_ERROR_WRONG_PARAM);
			logger.error("wrong params");
			sendJson(result);
			return null;
		}
		
		ItemListDO itemListDO = itemService.getPolularItemsByShopId(shopId, num, page);

		result.setData(itemListDO);
		if (itemListDO.getItemList().size() == 0) {
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
