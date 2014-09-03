package com.taobao.guangjie.service;

import java.net.UnknownHostException;

import com.taobao.guangjie.dataobject.ItemListDO;

public interface ItemService {
	
	public ItemListDO getPolularItems(int num, int page) throws UnknownHostException;
	public ItemListDO getPolularItemsByShopId(String shopId, int num, int page) throws UnknownHostException;
}
