package com.taobao.guangjie.action.shop;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.guangjie.action.BaseAction;
import com.taobao.guangjie.dataobject.BandCategoryDO;
import com.taobao.guangjie.dataobject.BaseResult;
import com.taobao.guangjie.dataobject.Constants;
import com.taobao.guangjie.service.ShopService;
import com.taobao.guangjie.service.impl.ShopServiceImpl;

/**
 * 获取当前商场的品牌类目信息
 * 
 * 用于客户端“品牌类目”界面左边的分类列表 每个类目包含的店铺列表需要调用其他API获得
 * 
 */
public class BandCategoryAction extends BaseAction {

	private static final long serialVersionUID = -4583205460370852903L;

	public static final String api = "com.taobao.guangjie.shopapi.getBandCategory";
	
	private ShopService shopService = new ShopServiceImpl();

	@Override
	public String execute() throws IOException {
		Logger logger = LoggerFactory.getLogger(BandCategoryAction.class);
		BaseResult<BandCategoryDO> result = new BaseResult<BandCategoryDO>(api);
		BandCategoryDO bandCategoryDO = shopService.getBandCategories();
		result.setData(bandCategoryDO);

		if (bandCategoryDO.getCategoryList().size() == 0) {
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
