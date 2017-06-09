package com.competition.util;

import java.util.ArrayList;
import java.util.List;

public class Combine {
	private static int size = 0;
	private static int[][] path;
	private static int[][] pathEdge;
	private static List<int[]> appointEdge;
	/**
	 * @author pengx
	 * @param appointPoint 必经点
	 * @param appointEdge 必经边，用list装int数组
	 * @param appointAll 所有必经点
	 * @param start 起点
	 * @param aim 终点
	 * @return 返回的三维数组中，result[0]是全排列，result[1]是排列中必经边的分布
	 * 返回选中数的全排列
	 */
	public static int[][][] combine(List<int[]> appointEdge, int[] appointAllIn, int start, int aim){
		int[] appointAll = new int[appointAllIn.length - 2];
		int mark = 0;
		for(int i:appointAllIn){
			if(i != start && i != aim){
				appointAll[mark++] = i;
			}
		}
		int num = factorial(appointAll.length - appointEdge.size()) * (int)Math.pow(2, appointEdge.size());
		path = new int[num][appointAll.length + 2];
		pathEdge = new int[num][appointAll.length + 2];//用来记录必经边在路径中的位置
		Combine.appointEdge = appointEdge;
		int[] vec = new int[appointAll.length + 2];
		vec[0] = start;
		vec[vec.length - 1] = aim;
		for(int i = 1; i <= appointAll.length; i++){
			vec[i] = appointAll[i - 1];
		}
		size = 0;
		arrage(vec, 1, vec.length - 2);
		int[][][] result = new int[2][size][appointAll.length + 2];
		for(int i = 0; i < size; i++){
			result[0][i] = path[i];
			result[1][i] = pathEdge[i];
		}
		return result;
	}
	
	private static void swap(int[] data, int a, int b){
		int temp = 0;
		temp = data[a];
		data[a] = data[b];
		data[b] = temp;
	}
	
	private static void arrage(int[] vec, int head, int tail){//此处的head和tail均为索引
		if(head > tail || size >= path.length)
			return;
		if(tail - head == 0){
			int mark = 0;
			for(int i = 1; i < vec.length - 1; i++){
				pathEdge[size][i] = 0;
			}
			for(int i = 1; i < vec.length - 1; i++){
				for(int[] temp: appointEdge){
					if(temp[0] == vec[i] && temp[1] == vec[i + 1] || temp[1] == vec[i] && temp[0] == vec[i + 1]){
						mark++;
						pathEdge[size][i] = 1;
						pathEdge[size][i + 1] = 0;
					}
				}
			}
			if(mark == appointEdge.size()){
				path[size++] = vec;
			}
		}else{
			for(int i = tail; i > head; i--){
				int[] vecTemp = vec.clone();
				swap(vecTemp, head, i);
				arrage(vecTemp, head + 1, tail);
			}
			swap(vec, head, head);
			arrage(vec, head + 1, tail);
		}
	}
	
	private static int factorial(int num){
		int result = 1;
		if(num != 1){
			result = num * factorial(num - 1);
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		int[] appointPoint = new int[2];
		for(int i = 0; i < appointPoint.length; i++){
			appointPoint[i] = i + 1;
		}
		List<int[]> appointEdge= new ArrayList<int[]>();
		int[] temp = new int[2];
		temp[0] = 3;
		temp[1] = 4;
		appointEdge.add(temp);
		temp = new int[2];
		temp[0] = 4;
		temp[1] = 5;
		appointEdge.add(temp);
		int[] appointAll = new int[5];
		for(int i = 0; i < appointAll.length; i++){
			appointAll[i] = i + 1;
		}
		int[][] path = combine(appointEdge, appointAll, 0, 17)[0];
		for(int i = 0; i < path.length; i++){
			for(int j = 0; j < path[i].length; j++){
				System.out.print(path[i][j] + " ");
			}
			System.out.println();
		}
	}
}
