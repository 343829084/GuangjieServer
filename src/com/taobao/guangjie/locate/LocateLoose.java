package com.taobao.guangjie.locate;

import java.net.UnknownHostException;
import java.util.*;

import com.taobao.guangjie.datatypes.*;
import com.mongodb.*;
import com.taobao.guangjie.dl.*;

public class LocateLoose implements Locate {

	MongoDL dl;
	int range;

	public LocateLoose() throws UnknownHostException {
		dl = MongoDL.getInstance();
		range = 10;
	}

	@Override
	public void setThreshold(int t) {
		this.range = t;
	}

	@Override
	public Set<String> locate(List<WifiRecord> records) {
		Collections.sort(records, new Comparator<WifiRecord>() {
			@Override
			public int compare(WifiRecord arg0, WifiRecord arg1) {
				return arg1.level - arg0.level;
			}
		});
		DBCollection coll = dl.getMdb().getCollection("qs");
		Set<String> shops = null;
		for (WifiRecord rec : records) {
			DBCursor cursor = coll.find(new BasicDBObject("BSSID", rec.bssid)
					.append("LEVEL", new BasicDBObject("$gte", rec.level
							- this.range)));
			if (!cursor.hasNext()) // bssid cleaning
				continue;
			Set<String> curShops = new HashSet<String>();
			while (cursor.hasNext()) {
				String pos = cursor.next().get("Store").toString();
				if (shops == null || shops.contains(pos))
					curShops.add(pos);
			}
			if (curShops.size() == 0)
				break;
			shops = curShops;
		}
		return shops;
	}

	@Override
	public String locateOne(List<WifiRecord> records) {
		Set<String> shops = locate(records);
		if (shops == null) {
			return "null";
		}
		/*
		 * System.out.println(shops.size()); for(String str : shops) {
		 * System.out.println(str); }
		 */
		int mindiff = Integer.MAX_VALUE;
		String minShop = null;
		int maxlevel = Integer.MIN_VALUE;
		String maxbssid = null;
		for (WifiRecord rec : records) {
			if (rec.level > maxlevel) {
				maxlevel = rec.level;
				maxbssid = rec.bssid;
			}
		}
		for (String pos : shops) {
			int diff = getMinDiff(pos, maxbssid, maxlevel);
			if (diff < mindiff) {
				mindiff = diff;
				minShop = pos;
			}
		}
		return minShop;
	}

	private String getMaxBSSID(String shopPos) {
		DBCollection coll = dl.getMdb().getCollection("qs");
		DBCursor cursor = coll.find(new BasicDBObject("Store", shopPos));
		double maxlevel = Double.NEGATIVE_INFINITY;
		String bssid = null;
		while (cursor.hasNext()) {
			DBObject row = cursor.next();
			if ((Double) row.get("LEVEL") > maxlevel) {
				maxlevel = (Double) row.get("LEVEL");
				bssid = row.get("BSSID").toString();
			}
		}
		return bssid;
	}

	private int getMinDiff(String shopPos, String bssid, int userlevel) {
		DBCollection coll = dl.getMdb().getCollection("qs");
		int boost = 0;
		if (bssid.equals(getMaxBSSID(shopPos))) {
			boost = -1000;
		}
		DBObject below = coll.findOne(new BasicDBObject("LEVEL",
				new BasicDBObject("$lte", userlevel)).append("BSSID", bssid)
				.append("Store", shopPos));
		DBObject above = coll.findOne(new BasicDBObject("LEVEL",
				new BasicDBObject("$gte", userlevel)).append("BSSID", bssid)
				.append("Store", shopPos));
		if (above == null && below == null) {
			return Integer.MAX_VALUE;
		}
		if (above == null)
			return (int) (userlevel - (Double) below.get("LEVEL")) + boost;
		if (below == null)
			return (int) ((Double) above.get("LEVEL") - userlevel) + boost;
		return (int) Math.min(userlevel - (Double) below.get("LEVEL"),
				(Double) above.get("LEVEL") - userlevel) + boost;
	}

}
