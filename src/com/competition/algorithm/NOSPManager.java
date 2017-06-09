package com.competition.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.competition.graph.Graph;

public class NOSPManager {
	private NOSP[] NOSPs; //存放所有计算过的源点
	private Graph G;
	
	public NOSPManager(Graph G, int[][] virticesMax, int[] appointAll, int[] appointAllR, BFSManager bfsManager){
		this.G = G;
		NOSPs = new NOSP[G.V()];
		
		int max = 0;
		int[] maxI = new int[virticesMax.length];
		for(int i = 0; i < virticesMax.length; i++){
			for(int j = 0; j < virticesMax[0].length; j++){
				if(virticesMax[i][j] > maxI[i])
					maxI[i] = virticesMax[i][j];
			}
			if(maxI[i] > max)
				max = maxI[i];
		}
		//用distNum来装个数的归类
		List<Integer>[] distNum = new ArrayList[max];//应不超过sureNumLimit
		for(int i = 0; i < max; i++){
			distNum[i] = new ArrayList<Integer>();
		}
		//遍历所有必经点
		for(int i: appointAll){
			int[] dist = bfsManager.getBFS(i).getDistTo();
			for(int j = 0; j < G.V(); j++){
				if(dist[j] > max - 1)
					continue;
				distNum[dist[j]].add(j);
			}
			NOSPs[i] = new NOSP(G, i, distNum, maxI[appointAllR[i]]);
			for(int j = 0; j < max; j++){
				distNum[j].clear();
			}
		}
	}
	
	public int dist(int v ,int w , int k) //v  w在k个顶点数限制下的最短距离
	{
		return NOSPs[v].distTo(w, k);
	}
	public Stack<Integer>[] path(int v , int w ,int k) //vw在k个顶点数限制下的路径（不止一条）
	{
		return NOSPs[v].pathTo(w, k, G);
	}

}
