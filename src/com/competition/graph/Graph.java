package com.competition.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	private int V;//顶点总数
	private int E;//边的总数
	private List<Edge>[] adj;//邻接表 

	public Graph(int V){
			this.V=V;
			E=0;
			adj = (List<Edge>[])new ArrayList[V];
			for(int v = 0; v < V ; v++)
				adj[v] = new ArrayList<Edge>();
	} 

	public int V(){ //得到顶点数
		return V;
	}
	
	public int E(){ //得到边的数量，无向图既为双向图，所以边的数量是两倍
		return E;
	}
	
	public void addEdge(Edge e){ //添加边 v w
		adj[e.getV()].add(e);
		E++;
	}
	
	public void removeEdge(int v , int w ){
		Edge e = getEdge(v , w);
		if(adj[v].remove(e))
			E--;
	}
	
	public Edge getEdge(int v,int w){
		for(Edge e:adj[v]){
			if(e.getW() == w)
				return e;
		}
		return null;
	}
	
	public List<Edge> adj(int v){ //得到与v相连的所有边
		return adj[v];
	}
 
}
