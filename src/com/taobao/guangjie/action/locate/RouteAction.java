package com.taobao.guangjie.action.locate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.guangjie.action.BaseAction;
import com.taobao.guangjie.dataobject.BaseResult;
import com.taobao.guangjie.dataobject.Constants;
import com.taobao.guangjie.dataobject.RouteDO;
import com.taobao.guangjie.dataobject.RoutePoint;
import com.taobao.guangjie.route.JSONLoader;
import com.taobao.guangjie.route.JSONLoader.MapName;
import com.taobao.guangjie.route.JSONPoint;
import com.taobao.guangjie.util.ShopUtil;

/**
 * 
 * 
 */
public class RouteAction extends BaseAction {

	private static final long serialVersionUID = -3907665667300492359L;
	public static final String api = "com.taobao.guangjie.locateapi.getRoute";

	private String from;
	private String to;
	private Logger logger;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	private boolean isSameLevel(RoutePoint p1, RoutePoint p2) {
		if (p1.getLevel() == p2.getLevel()) {
			return true;
		}
		return false;
	}

	private String toLevelString(int level) {
		switch (level) {
		case 1:
			return "L1";
		case 2:
			return "L2";
		case 3:
			return "L3";
		case 4:
			return "L4";
		case 5:
			return "B1";
		case 6:
			return "BM";
		default:
			return null;
		}
	}

	private String buildMsg(List<RoutePoint> routeList,
			List<String> passedPoints) throws Exception {

		List<String> levels = new ArrayList<String>();
		levels.add(routeList.get(0).getShopPos().substring(0, 2));
		List<Integer> sizes = new ArrayList<Integer>();

		// 过滤掉不需要的点
		List<String> shopsInRouteList = new ArrayList<String>();
		for (int i = 0; i < routeList.size(); i++) {
			RoutePoint point = routeList.get(i);
			if (point.getShopPos().length() > 1) {
				shopsInRouteList.add(ShopUtil.getShortShopName(ShopUtil
						.getShopNameByPos(point.getShopPos())));
			} else if (point.getShopPos().equals("+")) {
				if (routeList.get(i + 1).getShopPos().equals("+")) {
					if (!isSameLevel(routeList.get(i + 1), point)) {
						if (toLevelString(routeList.get(i + 1).getLevel()) == null) {
							return null;
						}
						levels.add(toLevelString(routeList.get(i + 1)
								.getLevel()));
						if (sizes.size() == 0) {
							sizes.add(shopsInRouteList.size());
						} else {
							int tmp = shopsInRouteList.size();
							for (int item : sizes) {
								tmp -= item;
							}
							sizes.add(tmp);
						}
					}
				}
			}
		}

		if (sizes.size() == 0) {
			sizes.add(shopsInRouteList.size());
		} else {
			int tmp = shopsInRouteList.size();
			for (int item : sizes) {
				tmp -= item;
			}
			sizes.add(tmp);
		}

		System.out.println("sizes:");
		for (int item : sizes) {
			System.out.print(item + "\t");
		}
		System.out.println();

		System.out.println("levels:");
		for (String item : levels) {
			System.out.print(item + "\t");
		}
		System.out.println();

		System.out.println("shops:");
		for (String item : shopsInRouteList) {
			System.out.print(item + "  ");
		}
		System.out.println();

		// 路线只经过一个店
		if (shopsInRouteList.size() == 1) {
			passedPoints.add(shopsInRouteList.get(0));
			return "您已经在 " + shopsInRouteList.get(0);
		}

		// 构造路径规划提示
		StringBuilder msgSb = new StringBuilder();

		// 单层
		if (levels.size() == 1) {
			msgSb.append("从 ").append(shopsInRouteList.get(0)).append(" 出发，")
					.append("向 ").append(shopsInRouteList.get(1))
					.append(" 方向走，到达 ")
					.append(shopsInRouteList.get(shopsInRouteList.size() - 1));
			passedPoints.add(shopsInRouteList.get(0));
			passedPoints.add(shopsInRouteList.get(1));
			passedPoints.add(shopsInRouteList.get(shopsInRouteList.size() - 1));
			return msgSb.toString();
		}

		List<String> subList = null;
		// 多层
		msgSb.append("从 ").append(shopsInRouteList.get(0)).append(" 出发，");
		passedPoints.add(shopsInRouteList.get(0));
		for (int sizeIndex = 0; sizeIndex < sizes.size(); sizeIndex++) {
			int start = 0;
			for (int t = 0; t < sizeIndex; t++) {
				start += sizes.get(t);
			}
			if (sizes.get(sizeIndex) != 0) {
				subList = shopsInRouteList.subList(start,
						start + sizes.get(sizeIndex));
				logger.info("start: " + start);
				logger.info("end: " + (start + sizes.get(sizeIndex)));
			} else {
				subList = null;
			}

			// 该层没有经过店
			if (subList == null || subList.size() == 0) {
				logger.info("一层没有经过店");
				msgSb.append("乘电梯到 ").append(levels.get(sizeIndex + 1))
						.append(" ，");
				passedPoints.add(levels.get(sizeIndex + 1));
			}
			// 该层是终点层
			else if (sizeIndex == sizes.size() - 1) {
				logger.info("最终层" + subList.size());
				if (subList.size() == 1) {
					msgSb.append("前进到达 ").append(subList.get(0));
					passedPoints.add(subList.get(0));
				} else if (subList.size() > 1) {
					msgSb.append("向 ").append(subList.get(0))
							.append(" 方向走，到达 ")
							.append(subList.get(subList.size() - 1));
					passedPoints.add(subList.get(0));
					passedPoints.add(subList.get(subList.size() - 1));
				}
			}
			// 该层不是最终层，并且经过至少一个店
			else {
				logger.info("中间层");
				if (subList.size() == 1) {
					if (sizeIndex == 0) {
						msgSb.append("乘电梯到 ")
								.append(levels.get(sizeIndex + 1)).append(" ，");
						passedPoints.add(levels.get(sizeIndex + 1));
					} else {
						msgSb.append("前进到达 ").append(subList.get(0))
								.append(" ，乘电梯到 ")
								.append(levels.get(sizeIndex + 1)).append(" ，");
						passedPoints.add(subList.get(0));
						passedPoints.add(levels.get(sizeIndex + 1));
					}
				} else if (subList.size() > 1) {
					if (sizeIndex == 0) {
						if (sizes.size() == 2) {
							msgSb.append("前进到 ").append(subList.get(1))
									.append(" ， 乘电梯到 ")
									.append(levels.get(sizeIndex + 1))
									.append(" ，");
							passedPoints.add(subList.get(1));
							passedPoints.add(levels.get(sizeIndex + 1));
						} else {
							msgSb.append("向 ").append(subList.get(1))
									.append(" 方向走，到达 ")
									.append(subList.get(subList.size() - 1))
									.append(" ， 乘电梯到 ")
									.append(levels.get(sizeIndex + 1))
									.append(" ，");
							passedPoints.add(subList.get(1));
							passedPoints.add(subList.get(subList.size() - 1));
							passedPoints.add(levels.get(sizeIndex + 1));
						}
					} else {
						msgSb.append("向 ").append(subList.get(0))
								.append(" 方向走，到达 ")
								.append(subList.get(subList.size() - 1))
								.append(" ， 乘电梯到 ")
								.append(levels.get(sizeIndex + 1)).append(" ，");
						passedPoints.add(subList.get(0));
						passedPoints.add(subList.get(subList.size() - 1));
						passedPoints.add(levels.get(sizeIndex + 1));
					}
				}
			}
		}

		return msgSb.toString();

	}

	@Override
	public String execute() throws IOException {
		logger = LoggerFactory.getLogger(RouteAction.class);

		if (from == null || from.length() == 0 || to == null
				|| to.length() == 0) {
			BaseResult<List<RoutePoint>> result = new BaseResult<List<RoutePoint>>(
					api, "1.0", Constants.API_ERROR_WRONG_PARAM);
			sendJson(result);
			return null;
		}

		RouteDO routeDO = new RouteDO();

		List<RoutePoint> routeList = routeDO.getRouteList();

		JSONLoader loader = JSONLoader.getInstance(MapName.InCity);
		List<Integer> path = loader.getFinder().find(
				loader.translateShopPos(from), loader.translateShopPos(to));

		if (path == null || path.size() == 0) {
			logger.error("path is NULL");
			BaseResult<List<RoutePoint>> result = new BaseResult<List<RoutePoint>>(
					api, "1.0", Constants.API_ERROR);
			sendJson(result);
			return null;
		}

		Map<Integer, JSONPoint> map = loader.getNormalPointMap();
		for (int i : path) {
			if (map.containsKey(i)) {
				JSONPoint point = map.get(i);
				routeList.add(new RoutePoint(point.x, point.y, point.level,
						point.shopPos));
			} else {
				logger.error("point is NULL");
				BaseResult<List<RoutePoint>> result = new BaseResult<List<RoutePoint>>(
						api, "1.0", Constants.API_ERROR);
				sendJson(result);
				return null;
			}
		}
		try {
			routeDO.setMsg(buildMsg(routeList, routeDO.getPassedPoints()));
		} catch (Exception e) {
			logger.error("构造字符串发生异常，放弃生成字符串");
			routeDO.setMsg(null);
		}
		BaseResult<RouteDO> result = new BaseResult<RouteDO>(api);
		result.setData(routeDO);
		sendJson(result);

		return null;
	}
}
