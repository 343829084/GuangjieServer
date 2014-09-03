package com.taobao.guangjie.dataobject;

import java.util.ArrayList;
import java.util.List;

/**
 * 商铺列表
 */
public class ShopListDO {

	private int num;
	private int page;
	private List<Coupon> couponList;

	public ShopListDO() {
		couponList = new ArrayList<Coupon>();
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

	public List<Coupon> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<Coupon> couponList) {
		this.couponList = couponList;
	}

}
