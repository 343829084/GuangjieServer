package com.taobao.guangjie.util;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.taobao.guangjie.dl.MongoDL;

public class ShopUtil {
	public static String getShopNameByPos(String shopPos) {
		MongoDL mongoDL = null;
		try {
			mongoDL = MongoDL.getInstance();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
		DB db = mongoDL.getMdb();
		DBCollection shopCollection = db.getCollection("shop");
		BasicDBObject query = new BasicDBObject("shopPos", shopPos);
		DBCursor cursor = (DBCursor) shopCollection.find(query);

		if (cursor.hasNext()) {
			DBObject entry = cursor.next();
			return entry.get("shopName").toString();
		}
		return "";

	}

	public static String getShortShopName(String shopName) {
		if (shopName == null || shopName.length() == 0) {
			return "";
		}
		shopName = shopName.trim();
		if ((shopName.charAt(0) >= 'a' && shopName.charAt(0) <= 'z')
				|| (shopName.charAt(0) >= 'A' && shopName.charAt(0) <= 'Z')) {
			return shopName;
		}
//		int start = -1;
//		for (int i = 0; i < shopName.length(); i++) {
//			if ((shopName.charAt(i) >= 'a' && shopName.charAt(i) <= 'z')
//				|| (shopName.charAt(i) >= 'A' && shopName.charAt(i) <= 'Z')) {
//				start = i;
//				break;
//			}
//		}
//		if (start == -1) {
//			return shopName;
//		}
//		return shopName.substring(0, start).trim();
		String[] split = shopName.split(" ");
		return split[0].trim();
	}

	public static void main(String[] args) {
		System.out.println(getShortShopName("肯德基KFC"));
	}

}
