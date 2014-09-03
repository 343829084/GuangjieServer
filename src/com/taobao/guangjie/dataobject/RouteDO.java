package com.taobao.guangjie.dataobject;

import java.util.ArrayList;
import java.util.List;

public class RouteDO {
	
	private List<RoutePoint> routeList;
	private String msg;
	private List<String> passedPoints;
	
	public RouteDO() {
		routeList = new ArrayList<RoutePoint>();
		passedPoints = new ArrayList<String>();
	}
	
	public List<String> getPassedPoints() {
		return passedPoints;
	}

	public void setPassedPoints(List<String> passedPoints) {
		this.passedPoints = passedPoints;
	}

	public List<RoutePoint> getRouteList() {
		return routeList;
	}
	public void setRouteList(List<RoutePoint> routeList) {
		this.routeList = routeList;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
