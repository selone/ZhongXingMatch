package com.competition.algorithm;

import java.util.Stack;

import com.competition.graph.Graph;

public class BFSManager {
	private BFS[] bfss;
	private Graph G;
	
	public BFSManager(Graph G){
		bfss = new BFS[G.V()];
		this.G = G;
	}
	
	public int dist(int v ,int w){ //v  w的最短顶点数
		if(bfss[v] == null){
			bfss[v] = new BFS(G, v);
		}
		return bfss[v].distTo(w);
	}
	
	public Stack<Integer> path(int v , int w){ //vw的路径
		if(bfss[v] == null){
			bfss[v] = new BFS(G, v);
		}
		return bfss[v].pathTo(w);
	}
	
	public BFS getBFS(int v){
		return bfss[v];
	}
}
