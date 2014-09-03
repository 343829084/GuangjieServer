package com.taobao.guangjie.dataobject;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类商铺列表
 */
public class ShopListByCategoryDO {

	private int num;
	private int page;
	private String category;
	private List<ShopDO> shopList;

	public ShopListByCategoryDO() {
		shopList = new ArrayList<ShopDO>();
	}

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<ShopDO> getShopList() {
		return shopList;
	}

	public void setShopList(List<ShopDO> shopList) {
		this.shopList = shopList;
	}

}
