package com.taobao.guangjie.dataobject;

import java.util.ArrayList;
import java.util.List;

/**
 * 品牌类目DO
 */
public class BandCategoryDO {

	private List<OneBandCategory> categoryList;

	public BandCategoryDO() {
		categoryList = new ArrayList<OneBandCategory>();
	}

	public List<OneBandCategory> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<OneBandCategory> categoryList) {
		this.categoryList = categoryList;
	}
}
