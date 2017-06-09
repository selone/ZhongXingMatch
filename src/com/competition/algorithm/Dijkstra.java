package com.competition.algorithm;

import java.util.Stack;

import com.competition.graph.Edge;
import com.competition.graph.Graph;
import com.competition.util.IndexMinPQ;

public class Dijkstra {
	private Edge[] edgeTo;
	private int[] distTo;
	private IndexMinPQ<Integer> pq;
	
	public Dijkstra(Graph G, int s){
		edgeTo = new Edge[G.V()];
		distTo = new int[G.V()];
		pq = new IndexMinPQ<Integer>(G.V());
		for(int v = 0; v < G.V(); v++){
			distTo[v] = Integer.MAX_VALUE;
		}
		distTo[s] = 0;
		pq.insert(s, 0);
		while(!pq.isEmpty())
			relax(G , pq.delMin());
	}
	
	private void relax(Graph G, int v){
		for(Edge e:G.adj(v)){
			int w = e.getW();
			if(distTo[w] > distTo[v] + e.getWeight()){
				distTo[w] = distTo[v] + e.getWeight();
				edgeTo[w] = e;
				if(pq.contains(w)) 
					pq.changeKey(w, distTo[w]);
				else
					pq.insert(w, distTo[w]);
			}
			
		}
	}
	
	public int distTo(int v)
	{
		return distTo[v];
	}
	
	public boolean hasPathTo(int v)
	{
		return distTo[v] < Integer.MAX_VALUE;
	}
	
	public Stack<Edge> pathTo(int v)
	{
		if(!hasPathTo(v)) return null;
		Stack<Edge> path = new Stack<Edge>();
		for(Edge e = edgeTo[v]; e != null; e = edgeTo[e.getV()])
			path.push(e);
		return path;
	}
	
}
