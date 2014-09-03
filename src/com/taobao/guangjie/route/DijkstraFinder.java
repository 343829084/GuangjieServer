package com.taobao.guangjie.route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class DijkstraFinder {
	Map<Integer, List<Edge>> graph;
	int[] passable;
	int maxnode;

	public DijkstraFinder(List<Edge> edges, int[] passable) {
		buildGraph(edges);
		if (passable != null)
			this.passable = Arrays.copyOf(passable, passable.length);
		else
			this.passable = null;
	}

	private void addEdge(Edge e) {
		if (!graph.containsKey(e.s))
			graph.put(e.s, new ArrayList<Edge>());
		graph.get(e.s).add(e);
	}

	private void buildGraph(List<Edge> edges) {
		graph = new HashMap<Integer, List<Edge>>();
		maxnode = -1;
		for (Edge e : edges) {
			addEdge(e);
			addEdge(e.getReverse());
			maxnode = Math.max(maxnode, e.s);
			maxnode = Math.max(maxnode, e.t);
		}
	}

	private static class PointStatus implements Comparable<PointStatus> {
		int node;
		double dist;

		public PointStatus(int node, double dist) {
			this.node = node;
			this.dist = dist;
		}

		@Override
		public int compareTo(PointStatus arg0) {
			double diff = this.dist - arg0.dist;
			if (diff > 0)
				return 1;
			else if (diff == 0)
				return 0;
			else
				return -1;
		}
	}

	private List<Integer> tracePath(int[] from, int end) {
		List<Integer> res = new ArrayList<Integer>();
		for (int i = end; i != -1; i = from[i]) {
			res.add(i);
		}
		Collections.reverse(res);
		return res;
	}

	public void printConnectivity(int start) {
		for (Edge e : graph.get(start)) {
			System.err.println(e.t + " " + e.dist);
		}
	}

	public double getEdgeLength(int x, int y) {
		for (Edge e : graph.get(x)) {
			if (e.t == y)
				return e.dist;
		}
		return Double.POSITIVE_INFINITY;
	}

	public double getPathLength(List<Integer> path) {
		double res = 0;
		int last = -1;
		for (Integer i : path) {
			if (last == -1) {
				last = i;
				continue;
			}
			res += getEdgeLength(last, i);
			System.out.print(getEdgeLength(last, i) + "\t");
			last = i;
		}
		System.out.println();
		return res;
	}

	private void reset() {
		for (int i = 0; i < passable.length; i++)
			if (passable[i] == 2)
				passable[i] = 0;
	}

	public List<Integer> find(int start, int end) {
		PriorityQueue<PointStatus> queue = new PriorityQueue<PointStatus>();
		int[] from = new int[maxnode + 1];
		double[] dist = new double[maxnode + 1];
		for (int i = 0; i < dist.length; i++) {
			dist[i] = Double.POSITIVE_INFINITY;
			from[i] = -1;
		}
		queue.add(new PointStatus(start, 0));
		dist[start] = 0;
		while (queue.size() > 0) {
			PointStatus top = queue.poll();
			if (dist[top.node] < top.dist - 1e-8)
				continue;
			if (!graph.containsKey(top.node))
				continue;
			for (Edge edge : graph.get(top.node)) {
				if (passable[top.node] == 0 && from[top.node] != -1
						&& passable[from[top.node]] != 0
						&& passable[edge.t] != 0)
					continue;
				if (dist[top.node] + edge.dist < dist[edge.t] - 1e-8) {
					dist[edge.t] = dist[top.node] + edge.dist;
					queue.add(new PointStatus(edge.t, dist[edge.t]));
					from[edge.t] = top.node;
				}
			}
		}
		this.reset();
		if (dist[end] == Double.POSITIVE_INFINITY)
			return null;
		return tracePath(from, end);
	}
}
