package com.taobao.guangjie.route;

public class Edge {
	int s, t;
	double dist;

	public Edge() {
		s = t = -1;
		dist = Double.POSITIVE_INFINITY;
	}

	public Edge(int s, int t, double dist) {
		this.s = s;
		this.t = t;
		this.dist = dist;
	}

	public Edge getReverse() {
		return new Edge(t, s, dist);
	}
}
