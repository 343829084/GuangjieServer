package com.taobao.guangjie.dataobject;

/**
 * 商品信息
 */
public class Item {

	private String itemName;
	private String itemPic;
	private String itemPrice;
	private String shopName;
	private String shopid;
	private String itemBigPic;

	public String getItemBigPic() {
		return itemBigPic;
	}

	public void setItemBigPic(String itemBigPic) {
		this.itemBigPic = itemBigPic;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemPic() {
		return itemPic;
	}

	public void setItemPic(String itemPic) {
		this.itemPic = itemPic;
	}

	public String getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopid() {
		return shopid;
	}

	public void setShopid(String shopid) {
		this.shopid = shopid;
	}

}
