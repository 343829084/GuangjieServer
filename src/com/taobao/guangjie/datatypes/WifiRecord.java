package com.taobao.guangjie.datatypes;

/**
 * 客户端扫描到的wifi信息
 */
public class WifiRecord {

	public String bssid;
	public int level;

	public WifiRecord(String bssid, int level) {
		this.bssid = bssid;
		this.level = level;
	}

	public WifiRecord() {
		this.bssid = null;
		this.level = 0;
	}
}
