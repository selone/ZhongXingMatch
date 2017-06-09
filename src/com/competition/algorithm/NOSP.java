package com.competition.algorithm;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import com.competition.graph.Edge;
import com.competition.graph.Graph;

public class NOSP {
	private int[][] d; //源点到v点在k个顶点数限制下的最短距离
	private int[][] ssp;//源点到v点在K个顶点数限制下的数量
	private int s;
	
	public NOSP(Graph G, int s, List<Integer>[] distNum , int numLimit){
		this.s = s;
		int numLimitT = numLimit;
		d = new int[G.V()][numLimitT];
		ssp = new int[G.V()][numLimitT];
		for(int i = 0; i < G.V(); i++){
			for(int j = 0; j < numLimitT; j++){
				d[i][j] = Integer.MAX_VALUE;
			}
		}
		for(int i = 0; i < numLimitT; i++){
			d[s][i] = 0;
			ssp[s][i] = 1;
		}
		for(int k = 1; k < numLimitT; k++ ){//此处k成了度数
			for(int i = 1; i <= k; i++ ){//此处和往下一行，共同构成需要被更新的节点
				for(int j:distNum[i]){//j就是需要被更新的节点
					for(Edge e:G.adj(j)){
						if(d[e.getW()][k - 1] + e.getWeight() > 0 && d[j][k] > d[e.getW()][k - 1] + e.getWeight()){
							d[j][k] = d[e.getW()][k - 1] + e.getWeight();
							ssp[j][k] = ssp[e.getW()][k - 1];
						}else if(d[j][k] == d[e.getW()][k - 1] + e.getWeight()){
							ssp[j][k] = ssp[j][k] + ssp[e.getW()][k - 1];
						}
					}
				}
			}
		}
	}
	
	
	public int distTo(int v , int k) //源点到v点,在k个顶点限制数的最短距离
	{
		return d[v][k - 1];
	}
	public boolean hasPathTo(int v,int k) //源点到v点,在k个顶点限制数的情况下，是否有路径
	{
		if(d[v][k - 1] < Integer.MAX_VALUE)
			return true;
		return false;
	}
	public Stack<Integer>[] pathTo(int v , int k, Graph G) //源点到v点,在k个顶点限制数的情况下路径
	{
		if(!hasPathTo(v, k))
			return null;
		//初始化stack
		k = k - 1;
		Stack<Integer>[] paths = new Stack[ssp[v][k]];
		for(int i = 0; i < paths.length; i++){
			paths[i] = new Stack<Integer>();
		}
		List<List<Integer>> pathTemp = next(new ArrayList<Integer>(), v, k, G);
		for(int i = 0; i < paths.length; i++){
			for(int j: pathTemp.get(i)){
				paths[i].push(j);
			}
		}
		return paths;
	}
	
	public List<List<Integer>> next(List<Integer> list, int v , int k, Graph G){
		List<List<Integer>> listAll = new ArrayList<List<Integer>>();
		list.add(v);
		List<Integer> listTemp = new ArrayList<Integer>(list);
		listAll.add(list);
		if(v == s){
			return listAll;
		}else{
			int mark = 0;
			for(Edge e:G.adj(v)){
				if(d[v][k] == d[e.getW()][k - 1] + e.getWeight()){
					List<List<Integer>> temp = null;
					if(mark++ == 0){
						temp = next(list, e.getW(), k - 1, G);
					}else{
						temp = next(new ArrayList<Integer>(listTemp), e.getW(), k - 1, G);
					}
					for(List<Integer> i:temp){
						listAll.add(i);
					}
				}
			}
		}
		return listAll;
	}

}
