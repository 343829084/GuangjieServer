package com.taobao.guangjie.service.impl;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.taobao.guangjie.dataobject.BandCategoryDO;
import com.taobao.guangjie.dataobject.Constants;
import com.taobao.guangjie.dataobject.Coupon;
import com.taobao.guangjie.dataobject.OneBandCategory;
import com.taobao.guangjie.dataobject.ShopDO;
import com.taobao.guangjie.dataobject.ShopListByCategoryDO;
import com.taobao.guangjie.dataobject.ShopListDO;
import com.taobao.guangjie.dataobject.ShopNameDO;
import com.taobao.guangjie.dl.MongoDL;
import com.taobao.guangjie.service.ShopService;

public class ShopServiceImpl implements ShopService {
	
	private MongoDL mongoDL;
	private DB db;
	
	public ShopServiceImpl() {
		try {
			mongoDL = MongoDL.getInstance();
			db = mongoDL.getMdb();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<ShopNameDO> getAllShopNames() throws UnknownHostException {
		List<ShopNameDO> shopList = new ArrayList<ShopNameDO>();
		if (mongoDL == null || db == null ) {
			mongoDL = MongoDL.getInstance();
			db = mongoDL.getMdb();
		}
		DBCollection shopCollection = db.getCollection("shop");

		DBCursor cursor = (DBCursor) shopCollection.find();
		while (cursor.hasNext()) {
			DBObject entry = cursor.next();
			ShopNameDO shopNameDO = new ShopNameDO(entry.get("shopName")
					.toString(), null);
			shopList.add(shopNameDO);
//			shopNameDO = new ShopNameDO(entry.get("shopPos")
//					.toString(), null);
//			shopList.add(shopNameDO);
		}
		
		return shopList;
	}

	@Override
	public BandCategoryDO getBandCategories() throws UnknownHostException {
		BandCategoryDO bandCategoryDO = new BandCategoryDO();

		if (mongoDL == null || db == null ) {
			mongoDL = MongoDL.getInstance();
			db = mongoDL.getMdb();
		}
		DBCollection tag = db.getCollection("tag");

		BasicDBObject query = new BasicDBObject();
		DBCursor cursor = (DBCursor) tag.find(query).sort(new BasicDBObject("rank", 1));
		String tag1;
		String tagId;
		while (cursor.hasNext()) {
			DBObject entry = cursor.next();
			tag1 = entry.get("tag").toString();
			tagId = entry.get("_id").toString();
			OneBandCategory oneBandCategory = new OneBandCategory(tag1, tagId);
			bandCategoryDO.getCategoryList().add(oneBandCategory);
		}
		return bandCategoryDO;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ShopListDO getCouponList(int num, int page) throws UnknownHostException {
		if (mongoDL == null || db == null ) {
			mongoDL = MongoDL.getInstance();
			db = mongoDL.getMdb();
		}
		
		DBCollection couponCollection = db.getCollection("coupon");
		DBCollection shopCollection = db.getCollection("shop");

		BasicDBObject query = new BasicDBObject();
		ShopListDO shopListDO = new ShopListDO();
		shopListDO.setNum(num);
		shopListDO.setPage(page);

		DBCursor cursor = (DBCursor) couponCollection.find(query)
				.sort(new BasicDBObject("addTime", -1)).skip(num * (page - 1))
				.limit(num);
		cursor.skip(num * (page - 1));
		while (cursor.hasNext()) {
			DBObject entry = cursor.next();
			Coupon coupon = new Coupon();

			Date date = null;
			if (entry.containsField("couponInfo")) {
				coupon.setCoupondesc(entry.get("couponInfo").toString());
			}
			if (entry.containsField("startTime")
					&& entry.containsField("endTime")
					&& entry.containsField("addTime")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				String time = entry.get("startTime").toString();
				date = new Date(time);
				coupon.setStartTime(sdf.format(date));

				time = entry.get("endTime").toString();
				date = new Date(time);
				coupon.setEndTime(sdf.format(date));

				time = entry.get("addTime").toString();
				date = new Date(time);
				coupon.setAddTime(sdf.format(date));
			}

			String idShop = entry.get("idShop").toString();

			BasicDBObject subquery = new BasicDBObject("_id", new ObjectId(
					idShop));

			DBCursor subcursor = (DBCursor) shopCollection.find(subquery);
			if (subcursor.hasNext()) {
				DBObject subentry = subcursor.next();
				if (subentry.containsField("shopName")) {
					coupon.setName(subentry.get("shopName").toString());
				}
				if (subentry.containsField("shopPos")) {
					coupon.setLocation(subentry.get("shopPos").toString());
				}
				if (entry.containsField("imgPic")
						&& entry.get("imgPic").toString().trim().length() > 0) {
					coupon.setPic(Constants.NODE_JS_BASE_URL
							+ entry.get("imgPic").toString());
				} else if (subentry.containsField("shopPos")) {
					coupon.setPic(Constants.LOGO_BASE_URL
							+ subentry.get("shopPos").toString() + ".png");
				}

				if (subentry.containsField("shopDesc")) {
					coupon.setDesc(subentry.get("shopDesc").toString());
				}
				if (subentry.containsField("_id")) {
					coupon.setShopid(subentry.get("_id").toString());
				}
			}

			shopListDO.getCouponList().add(coupon);
		}
		return shopListDO;
	}

	@Override
	public ShopListDO getFunShops(int num, int page)
			throws UnknownHostException {
		ShopListDO shopListDO = new ShopListDO();
		shopListDO.setNum(num);
		shopListDO.setPage(page);
		
		String tag = "美食佳肴";
		
		if (mongoDL == null || db == null ) {
			mongoDL = MongoDL.getInstance();
			db = mongoDL.getMdb();
		}
		DBCollection shopcollection = db.getCollection("shop");
		DBCollection couponCollection = db.getCollection("coupon");

		BasicDBObject query = new BasicDBObject("shopTags", tag);

		DBCursor cursor = (DBCursor) shopcollection.find(query)
				.skip(num * (page - 1)).limit(num);
		while (cursor.hasNext()) {
			DBObject entry = cursor.next();

			Coupon coupon = new Coupon();

			if (entry.containsField("shopName")) {
				coupon.setName(entry.get("shopName").toString());
			}
			if (entry.containsField("shopPos")) {
				coupon.setLocation(entry.get("shopPos").toString());
				coupon.setPic(Constants.LOGO_BASE_URL
						+ entry.get("shopPos").toString() + ".png");
			}
			if (entry.containsField("_id")) {
				coupon.setShopid(entry.get("_id").toString());
			}
			if (entry.containsField("couponList")) {
				DBObject couponList = (DBObject) (entry.get("couponList"));
				String couponId = couponList.get("0").toString();
				BasicDBObject subquery = new BasicDBObject("_id", new ObjectId(
						couponId));
				DBCursor subcursor = (DBCursor) couponCollection.find(subquery);
				if (subcursor.hasNext()) {
					DBObject subentry = subcursor.next();
					coupon.setCoupondesc(subentry.get("couponInfo").toString());
				}
			}
			if (entry.containsField("shopDesc")) {
				coupon.setDesc(entry.get("shopDesc").toString());
			}

			shopListDO.getCouponList().add(coupon);
		}
		return shopListDO;
	}

	@Override
	public ShopListByCategoryDO getShopsByCategory(String categoryId, int num, int page)
			throws UnknownHostException {
		ShopListByCategoryDO shopListByCategoryDO = new ShopListByCategoryDO();
		String tag = null;
		if (mongoDL == null || db == null ) {
			mongoDL = MongoDL.getInstance();
			db = mongoDL.getMdb();
		}
		DBCollection shopcollection = db.getCollection("shop");
		DBCollection tagCollection = db.getCollection("tag");
		BasicDBObject tagQuery = new BasicDBObject("_id", new ObjectId(
				categoryId));
		DBCursor tagCursor = tagCollection.find(tagQuery);
		if (tagCursor.hasNext()) {
			DBObject tagEntry = tagCursor.next();
			tag = tagEntry.get("tag").toString();
		} else {
			return null;
		}

		shopListByCategoryDO.setNum(num);
		shopListByCategoryDO.setPage(page);
		shopListByCategoryDO.setCategory(tag);

		BasicDBObject query = new BasicDBObject("shopTags", tag);

		DBCursor cursor = (DBCursor) shopcollection.find(query)
				.skip(num * (page - 1)).limit(num);
		while (cursor.hasNext()) {
			DBObject entry = cursor.next();

			ShopDO shopDO = new ShopDO();

			if (entry.containsField("shopName")) {
				shopDO.setName(entry.get("shopName").toString());
			}
			if (entry.containsField("shopPos")) {
				shopDO.setPic(Constants.LOGO_BASE_URL
						+ entry.get("shopPos").toString() + ".png");
			}
			if (entry.containsField("_id")) {
				shopDO.setShopid(entry.get("_id").toString());
			}

			shopListByCategoryDO.getShopList().add(shopDO);
		}

		return shopListByCategoryDO;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Map<String, String> getShopInfo(String shopid)
			throws UnknownHostException {

		if (mongoDL == null || db == null ) {
			mongoDL = MongoDL.getInstance();
			db = mongoDL.getMdb();
		}
		DBCollection shop = db.getCollection("shop");
		DBCollection couponCollection = db.getCollection("coupon");
		DBCursor cursor = (DBCursor) shop.find(new BasicDBObject("_id",
				new ObjectId(shopid)));
		if (cursor.hasNext()) {
			DBObject entry = cursor.next();
			entry.get("_id").toString();

			Map<String, String> data = new HashMap<String, String>();

			if (entry.containsField("shopName")) {
				data.put("name", entry.get("shopName").toString());
			}

			if (entry.containsField("shopPos")) {
				data.put("pic", Constants.LOGO_BASE_URL
						+ entry.get("shopPos").toString() + ".png");
			}

			if (entry.containsField("couponList")) {
				DBObject couponList = (DBObject) (entry.get("couponList"));
				String couponId = couponList.get("0").toString();
				BasicDBObject subquery = new BasicDBObject("_id", new ObjectId(
						couponId));
				DBCursor subcursor = (DBCursor) couponCollection.find(subquery);
				if (subcursor.hasNext()) {
					DBObject subentry = subcursor.next();
					data.put("coupondesc", subentry.get("couponInfo")
							.toString());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

					String time = subentry.get("startTime").toString();
					Date date = new Date(time);
					data.put("startTime", sdf.format(date));

					time = subentry.get("endTime").toString();
					date = new Date(time);
					data.put("endTime", sdf.format(date));

					time = subentry.get("addTime").toString();
					date = new Date(time);
					data.put("addTime", sdf.format(date));
				}
			}
			if (entry.containsField("shopPos")) {
				data.put("location", entry.get("shopPos").toString());
			}

			if (entry.containsField("shopDesc")) {
				data.put("desc", entry.get("shopDesc").toString());
			}

			if (entry.containsField("_id")) {
				data.put("shopid", entry.get("_id").toString());
			}

			return data;
		} else {
			return null;
		}
	}

	@Override
	public ShopListDO getSpecialShopList(int num, int page)
			throws UnknownHostException {
		ShopListDO shopListDO = new ShopListDO();
		shopListDO.setNum(num);
		shopListDO.setPage(page);

		if (mongoDL == null || db == null ) {
			mongoDL = MongoDL.getInstance();
			db = mongoDL.getMdb();
		}
		DBCollection shop = db.getCollection("shop");
		DBCollection couponCollection = db.getCollection("coupon");

		BasicDBObject query = new BasicDBObject("isSpecial", true);

		DBCursor cursor = (DBCursor) shop.find(query).skip(num * (page - 1))
				.limit(num);
		while (cursor.hasNext()) {
			DBObject entry = cursor.next();
			Coupon coupon = new Coupon();

			if (entry.containsField("shopName")) {
				coupon.setName(entry.get("shopName").toString());
			}
			if (entry.containsField("shopPos")) {
				coupon.setLocation(entry.get("shopPos").toString());
				coupon.setPic(Constants.LOGO_BASE_URL
						+ entry.get("shopPos").toString() + ".png");
			}
			if (entry.containsField("couponList")) {
				DBObject couponList = (DBObject) (entry.get("couponList"));
				String couponId = couponList.get("0").toString();
				BasicDBObject subquery = new BasicDBObject("_id", new ObjectId(
						couponId));
				DBCursor subcursor = (DBCursor) couponCollection.find(subquery);
				if (subcursor.hasNext()) {
					DBObject subentry = subcursor.next();
					coupon.setCoupondesc(subentry.get("couponInfo").toString());
				}
			}
			if (entry.containsField("shopDesc")) {
				coupon.setDesc(entry.get("shopDesc").toString());
			}
			if (entry.containsField("_id")) {
				coupon.setShopid(entry.get("_id").toString());
			}

			shopListDO.getCouponList().add(coupon);
		}
		return shopListDO;
	}

}
