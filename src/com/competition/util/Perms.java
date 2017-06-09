package com.competition.util;

import java.util.ArrayList;
import java.util.List;

public class Perms {
	private static int size = 0;
	
	/**
	 * @author pengx
	 * @param vec 需要被排列的数组、head 起始数的索引、 tail终止数的索引
	 * 返回选中数的全排列
	 */
	public static int[][] perms(int[] vec, int head, int tail){//此处的head和tail均为索引
		int[][] path = new int[factorial(tail - head + 1)][vec.length];
		size = 0;
		arrage(vec, head, tail, path);
		return path;
	}
	
	/**
	 * @author pengx
	 * @param appointPoint 必经点
	 * @param appointEdge 必经边，用list装int数组
	 * @param appointAll 所有必经点
	 * @param start 起点
	 * @param aim 终点
	 * @return 返回的三维数组中，result[0]是全排列，result[1]是排列中必经边的分布
	 * 缺点：如果边的点有重复，例如2-4，4-6此类情况就会出错，这个问题在combine类中得到修复，但是计算量增加
	 */
	private static int[][][] combine(int[] appointPoint, List<int[]> appointEdge, int[] appointAll, int start, int aim){
		int num = factorial(appointAll.length - appointEdge.size()) * (int)Math.pow(2, appointEdge.size());
		int[][] path = new int[num][appointAll.length + 2];
		int[][] pathEdge = new int[num][appointAll.length + 2];//用来记录必经边在路径中的位置
		int[] vec = new int[appointAll.length - appointEdge.size()];
		int vecPoint = 0;
		outer:
		for(int i: appointPoint){
			for(int j:vec){
				if(j == i)
					continue outer;
			}
			vec[vecPoint++] = i;
		}
		outer:
		for(int[] i: appointEdge){
			for(int j:vec){
				if(j == i[0])
					continue outer;
			}
			vec[vecPoint++] = i[0];
		}
		int[][] pathTemp = perms(vec, 0, vec.length - 1);
		int pathPoint = pathTemp.length;
		//赋值到path中
		for(int i = 0; i < pathTemp.length; i ++){
			//将pathTemp中的值扩充
			path[i][0] = start;
			int dy = 0;
			for(int j = 0; j < pathTemp[0].length; j++){
				path[i][j + 1 + dy] = pathTemp[i][j];
				for(int k = 0; k < appointEdge.size(); k++){
					if(pathTemp[i][j] == appointEdge.get(k)[0]){
						dy++;
						path[i][j + 1 + dy] = appointEdge.get(k)[1];
						pathEdge[i][j + 1 + dy] = 1;
						pathEdge[i][j + dy] = 1;
						break;
					}
				}
			}
			path[i][path[0].length - 1] = aim;
			
			//以下开始颠倒必经边
			for(int k = 1; k < (int)Math.pow(2, appointEdge.size()); k++){//k的二进制数，用来表示某一位是否颠倒
				int x = 1;//x代表二进制的位数
				for(int j = 0; j < path[0].length; j++){
					if(pathEdge[i][j] == 0){
						path[pathPoint][j] = path[i][j];
						continue;
					}
					int y = getB(k, x++);
					if(y == 1){
						path[pathPoint][j] = path[i][j + 1];
						path[pathPoint][j + 1] = path[i][j];
					}else{
						path[pathPoint][j] = path[i][j];
						path[pathPoint][j + 1] = path[i][j + 1];
					}
					j++;
				}
				pathPoint++;
			}
		}
		//将path和pathedge放入三维数组
		int[][][] result = new int[2][num][appointAll.length + 2];
		result[0] = path;
		result[1] = pathEdge;
		return result;
	}
	
	//得到一个数的二进制的第index位数值
	private static int getB(int num, int index)
    {
 //       return (num & (0x1 << index)) >> index;
		return (num >> (index - 1)) & 1;
    }
	
	private static void swap(int[] data, int a, int b){
		int temp = 0;
		temp = data[a];
		data[a] = data[b];
		data[b] = temp;
	}
	
	private static void arrage(int[] vec, int head, int tail, int[][] path){//此处的head和tail均为索引
		if(head > tail)
			return;
		if(tail - head == 0){
			path[size++] = vec;
		}else{
			for(int i = tail; i > head; i--){
				int[] vecTemp = vec.clone();
				swap(vecTemp, head, i);
				arrage(vecTemp, head + 1, tail, path);
			}
			swap(vec, head, head);
			arrage(vec, head + 1, tail, path);
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
		temp[0] = 5;
		temp[1] = 6;
		appointEdge.add(temp);
		int[] appointAll = new int[6];
		for(int i = 0; i < appointAll.length; i++){
			appointAll[i] = i + 1;
		}
		int[][] path = combine(appointPoint, appointEdge, appointAll, 0, 17)[0];
		for(int i = 0; i < path.length; i++){
			for(int j = 0; j < path[i].length; j++){
				System.out.print(path[i][j] + " ");
			}
			System.out.println();
		}
	}
}
