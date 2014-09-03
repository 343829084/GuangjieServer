package com.taobao.guangjie.dataobject;

public class RoutePoint {
	private int x;
	private int y;
	private int level;
	private String shopPos;
	
	public RoutePoint(int x, int y, int level, String shopPos) {
		this.x = x;
		this.y = y;
		this.level = level;
		this.shopPos = shopPos;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getShopPos() {
		return shopPos;
	}
	public void setShopPos(String shopPos) {
		this.shopPos = shopPos;
	}

}
