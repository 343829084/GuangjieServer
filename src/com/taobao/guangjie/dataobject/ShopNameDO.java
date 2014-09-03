package com.taobao.guangjie.dataobject;
/**
 * 商铺自动匹配数据
 */
public class ShopNameDO {
	
	
	private String shopName;
	private String shopId;
	
	public ShopNameDO(String shopName, String shopId) {
		this.shopName = shopName;
		this.shopId = shopId;
	}
	
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	
	

}
