package com.taobao.guangjie.dataobject;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品列表
 */
public class ItemListDO {

	private int num;
	private int page;
	private List<Item> itemList;

	public ItemListDO() {
		itemList = new ArrayList<Item>();
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

	public List<Item> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}

}
