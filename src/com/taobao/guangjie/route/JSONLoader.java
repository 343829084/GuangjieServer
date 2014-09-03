package com.taobao.guangjie.route;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.taobao.guangjie.dataobject.Constants;

public class JSONLoader {
	private JSONObject getJSONFromFile(String filename)
			throws FileNotFoundException {
		// JSONTokener tokener = new JSONTokener(new BufferedReader(
		// new InputStreamReader(new FileInputStream(filename))));
		JSONTokener tokener = new JSONTokener(new BufferedReader(
				new InputStreamReader(JSONLoader.class.getClassLoader()
						.getResourceAsStream(filename))));
		JSONObject obj = new JSONObject(tokener);
		return obj;
	}

	private double getDistance(int start, int end, List<JSONPoint> normalPoints) {
		JSONPoint startPnt = null, endPnt = null;
		for (JSONPoint pnt : normalPoints) {
			if (pnt.id == start)
				startPnt = pnt;
			if (pnt.id == end)
				endPnt = pnt;
		}
		if (startPnt == null || endPnt == null) {
			System.out.println("ERROR");
		}
		int xdiff = Math.abs(startPnt.x - endPnt.x);
		int ydiff = Math.abs(startPnt.y - endPnt.y);
		return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
	}

	private int[] buildPoints(JSONObject pointsObj, List<JSONPoint> normalPoints) {
		JSONArray points = pointsObj.getJSONArray("points");
		int len = points.length();
		int maxnode = -1;
		for (int i = 0; i < len; i++) {
			JSONObject pntObj = points.getJSONObject(i);
			JSONPoint pnt = new JSONPoint(pntObj.getInt("id"),
					pntObj.getString("shopPos"), pntObj.getInt("level"),
					pntObj.getInt("x"), pntObj.getInt("y"));
			normalPoints.add(pnt);
			if (maxnode < pnt.id)
				maxnode = pnt.id;
		}
		// Mark unaccessible points.
		int[] passable = new int[maxnode + 1];
		for (int i = 0; i < len; i++) {
			JSONPoint pnt = normalPoints.get(i);
			if (pnt.shopPos.equals("+"))
				passable[pnt.id] = 0;
			else
				passable[pnt.id] = 1;
		}
		return passable;
	}

	private void buildEdges(JSONObject edgesObj, List<Edge> edges,
			List<JSONPoint> normalPoints, int[] passable) {
		String[] edgeNames = JSONObject.getNames(edgesObj);
		for (String name : edgeNames) {
			JSONArray connections = edgesObj.getJSONArray(name);
			int connLen = connections.length();
			for (int i = 0; i < connLen; i++) {
				JSONObject conn = connections.getJSONObject(i);
				int start = Integer.parseInt(name);
				int end = conn.getInt("id");
				double dist = getDistance(start, end, normalPoints);
				if (passable[start] == 0 && passable[end] == 0)
					dist += 100;
				Edge edge = new Edge(start, end, dist);
				edges.add(edge);
			}
		}
	}

	private void buildPointMap(List<JSONPoint> list) {
		for (JSONPoint point : list) {
			normalPointMap.put(point.id, point);
		}
	}

	private Map<Integer, JSONPoint> normalPointMap = new HashMap<Integer, JSONPoint>();
	private List<JSONPoint> normalPoints;
	private List<Edge> edges;
	private DijkstraFinder finder;

	public List<JSONPoint> getNormalPoints() {
		return normalPoints;
	}

	private static Map<MapName, JSONLoader> jsonLoaderCache = new HashMap<JSONLoader.MapName, JSONLoader>();

	public enum MapName {
		InCity
	};

	static {
		try {
			jsonLoaderCache.put(MapName.InCity, new JSONLoader(
					Constants.INCITY_POINTS, Constants.INCITY_EDGE));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static JSONLoader getInstance(MapName mapName)
			throws FileNotFoundException {
		JSONLoader loader = null;
		if (jsonLoaderCache.containsKey(mapName)) {
			loader = jsonLoaderCache.get(mapName);
			if (loader != null) {
				return loader;
			} else {
				loader = new JSONLoader(Constants.INCITY_POINTS,
						Constants.INCITY_EDGE);
				jsonLoaderCache.put(mapName, loader);
				return loader;
			}
		}
		loader = new JSONLoader(Constants.INCITY_POINTS, Constants.INCITY_EDGE);
		jsonLoaderCache.put(MapName.InCity, loader);
		return loader;
	}

	private JSONLoader(String pointFile, String edgeFile)
			throws FileNotFoundException {
		normalPoints = new ArrayList<JSONPoint>();
		edges = new ArrayList<Edge>();

		JSONObject pointsObj = getJSONFromFile(pointFile);
		JSONObject edgesObj = getJSONFromFile(edgeFile);

		int[] passable = buildPoints(pointsObj, normalPoints);

		buildEdges(edgesObj, edges, normalPoints, passable);

		finder = new DijkstraFinder(edges, passable);

		buildPointMap(normalPoints);
	}

	public Map<Integer, JSONPoint> getNormalPointMap() {
		return normalPointMap;
	}

	private int getPointIDFromShop(String shopPos, List<JSONPoint> normalPoints) {
		for (JSONPoint p : normalPoints)
			if (p.shopPos.equals(shopPos))
				return p.id;
		return -1;
	}

	public int translateShopPos(String shopPos) {
		return getPointIDFromShop(shopPos, normalPoints);
	}

	public DijkstraFinder getFinder() {
		return finder;
	}
}
