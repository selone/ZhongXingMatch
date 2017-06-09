package com.competition.algorithm;

import java.util.Stack;

import com.competition.graph.Edge;
import com.competition.graph.Graph;

public class DijkstraManager {
	private Dijkstra[] dijkstras; //存放所有计算过的源点
	private Graph G;
	
	public DijkstraManager(Graph G){
		this.G = G;
		dijkstras = new Dijkstra[G.V()];
	}

	public int dist(int v ,int w) //v  w的最短距离
	{
		if(dijkstras[v] == null){
			dijkstras[v] = new Dijkstra(G, v);
		}
		return dijkstras[v].distTo(w);
	}
	public Stack<Edge> path(int v , int w) //vw的路径
	{
		if(dijkstras[v] == null){
			dijkstras[v] = new Dijkstra(G, v);
		}
		return dijkstras[v].pathTo(w);
	}

}
