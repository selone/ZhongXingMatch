package com.competition.graph;

public class Edge {
	private int v;
	private int w;
	private int weight;
	
	public Edge(int v,int w,int weight){
		this.v = v;
		this.w = w;
		this.weight = weight;
	}
	
	public int getV() {
		return v;
	}

	public int getW() {
		return w;
	}

	public int getWeight() {
		return weight;
	}
	
	@Override
	public int hashCode() {
		return v * 7 + w * 17 + weight * 53;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Edge){
			Edge e = (Edge)obj;
			if(e.getV() == v && e.getW() == w && e.getWeight() == weight)
				return true;
		}
		return false;
	}

	public String toString(){
		return v + "-->" + w;
	}
}
