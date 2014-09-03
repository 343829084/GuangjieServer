package com.taobao.guangjie.service;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import com.taobao.guangjie.dataobject.BandCategoryDO;
import com.taobao.guangjie.dataobject.ShopListByCategoryDO;
import com.taobao.guangjie.dataobject.ShopListDO;
import com.taobao.guangjie.dataobject.ShopNameDO;

public interface ShopService {
	
	public List<ShopNameDO> getAllShopNames() throws UnknownHostException;
	public BandCategoryDO getBandCategories() throws UnknownHostException;
	public ShopListDO getCouponList(int num, int page) throws UnknownHostException;
	public ShopListDO getFunShops(int num, int page) throws UnknownHostException;
	
	public ShopListByCategoryDO getShopsByCategory(String category, int num, int page) throws UnknownHostException;
	public Map<String, String> getShopInfo(String shopid) throws UnknownHostException;
	public ShopListDO getSpecialShopList(int num, int page) throws UnknownHostException;
}
