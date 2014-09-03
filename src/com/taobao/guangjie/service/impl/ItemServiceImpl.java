package com.taobao.guangjie.service.impl;

import java.net.UnknownHostException;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.taobao.guangjie.dataobject.Constants;
import com.taobao.guangjie.dataobject.Item;
import com.taobao.guangjie.dataobject.ItemListDO;
import com.taobao.guangjie.dl.MongoDL;
import com.taobao.guangjie.service.ItemService;

public class ItemServiceImpl implements ItemService {

	@Override
	public ItemListDO getPolularItems(int num, int page) throws UnknownHostException {
		ItemListDO itemListDO = new ItemListDO();
		
		itemListDO.setNum(num);
		itemListDO.setPage(page);

		MongoDL mongoDL = MongoDL.getInstance();
		DB db = mongoDL.getMdb();
		DBCollection itemCollection = db.getCollection("item");
		DBCollection shopCollection = db.getCollection("shop");

		BasicDBObject query = new BasicDBObject();

		DBCursor cursor = (DBCursor) itemCollection.find(query)
				.skip(num * (page - 1)).limit(num);

		while (cursor.hasNext()) {
			DBObject entry = cursor.next();
			Item item = new Item();

			if (entry.containsField("itemName")) {
				item.setItemName(entry.get("itemName").toString());
			}
			if (entry.containsField("itemPicUrl")) {
				item.setItemPic(Constants.ITEMPIC_BASE_URL
						+ entry.get("itemPicUrl").toString());
			}
			if (entry.containsField("itemBigPicUrl")) {
				item.setItemBigPic(Constants.BIGPIC_BASE_URL
						+ entry.get("itemBigPicUrl").toString());
			}
			if (entry.containsField("itemPrice")) {
				item.setItemPrice(entry.get("itemPrice").toString());
			}
			if (entry.containsField("idShop")) {
				item.setShopid(entry.get("idShop").toString());
				BasicDBObject subquery = new BasicDBObject("_id", new ObjectId(
						item.getShopid().toString()));
				DBCursor subcursor = (DBCursor) shopCollection.find(subquery);
				if (subcursor.hasNext()) {
					DBObject subentry = subcursor.next();
					if (subentry.containsField("shopName")) {
						item.setShopName(subentry.get("shopName").toString());
					}
				}
			}

			itemListDO.getItemList().add(item);
		}
		return itemListDO;
	}

	@Override
	public ItemListDO getPolularItemsByShopId(String shopId, int num, int page)
			throws UnknownHostException {
		ItemListDO itemListDO = new ItemListDO();
		
		itemListDO.setNum(num);
		itemListDO.setPage(page);

		MongoDL mongoDL = MongoDL.getInstance();
		DB db = mongoDL.getMdb();
		DBCollection itemCollection = db.getCollection("item");
		DBCollection shopCollection = db.getCollection("shop");

		BasicDBObject query = new BasicDBObject("idShop", new ObjectId(shopId));

		DBCursor cursor = (DBCursor) itemCollection.find(query)
				.skip(num * (page - 1)).limit(num);
		

		while (cursor.hasNext()) {
			DBObject entry = cursor.next();
			Item item = new Item();

			if (entry.containsField("itemName")) {
				item.setItemName(entry.get("itemName").toString());
			}
			if (entry.containsField("itemPicUrl")) {
				item.setItemPic(Constants.ITEMPIC_BASE_URL
						+ entry.get("itemPicUrl").toString());
			}
			if (entry.containsField("itemBigPicUrl")) {
				item.setItemBigPic(Constants.BIGPIC_BASE_URL
						+ entry.get("itemBigPicUrl").toString());
			}
			if (entry.containsField("itemPrice")) {
				item.setItemPrice(entry.get("itemPrice").toString());
			}
			if (entry.containsField("idShop")) {
				item.setShopid(entry.get("idShop").toString());
				BasicDBObject subquery = new BasicDBObject("_id", new ObjectId(
						item.getShopid().toString()));
				DBCursor subcursor = (DBCursor) shopCollection.find(subquery);
				if (subcursor.hasNext()) {
					DBObject subentry = subcursor.next();
					if (subentry.containsField("shopName")) {
						item.setShopName(subentry.get("shopName").toString());
					}
				}
			}

			itemListDO.getItemList().add(item);
		}
		return itemListDO;
	}
	

}
