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
 * 获取热销商品的API
 * 
 * 分页加载，加载时注意对图片请求不要太密集； 部分商品没有价格，客户端需要注意
 */
public class PopularItemsAction extends BaseAction {

	private static final long serialVersionUID = -6238203418880461586L;

	public static final String api = "com.taobao.guangjie.shopapi.getPopularItems";

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
	
	private ItemService itemService = new ItemServiceImpl();

	@Override
	public String execute() throws IOException {
		Logger logger = LoggerFactory.getLogger(PopularItemsAction.class);

		BaseResult<ItemListDO> result = new BaseResult<ItemListDO>(api);
		if (num <= 0 || page <= 0) {
			result.getRet().remove(0);
			result.getRet().add(Constants.API_ERROR_WRONG_PARAM);
			logger.error("page or num is empty");
			sendJson(result);
			return null;
		}
		
		ItemListDO itemListDO = itemService.getPolularItems(num, page);
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
