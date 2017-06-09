package com.competition.util;

import java.util.LinkedList;
import java.util.List;

public class Rest {
	private static RestData[][] data = new RestData[10][10];
	private static int width = 10;
	private static int length = 10;
	
	public static void main(String[] args) {
		int[][] test = Rest.rest(0, 3);
		for (int i = 0; i < test.length; i++) {
			for (int j = 0; j < test[0].length; j++) {
				System.out.print(test[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	private static int[][] rest(int rest, int num){
		int sum = 0;
		int dx = 0;
		List<int[]> list = new LinkedList<int[]>();
		int[] temp = new int[num + 1];
		list.add(temp.clone());
		if(rest == 0)
			return new int[1][num];
		while(temp[num] != 1){
			dx = rest - sum;
			temp[0] = temp[0] + dx;
			list.add(temp.clone());
			for(int i = 0; i < temp.length; i++){
				if(temp[i] != 0){
					sum = rest - temp[i] + 1;
					temp[i] = 0;
					temp[i + 1] = temp[i + 1] + 1;
					break;
				}
			}
		}
		int[][] result = new int[list.size()][num];
		int mark = 0;
		for(int[] i: list){
			for(int j = 0; j < num; j++){
				result[mark][j] = i[j];
			}
			mark++;
		}
		return result;
	}
	
	public static int[][] getRest(int rest, int num){
		if(rest < width && num < length){
			if(data[rest][num] == null)
				data[rest][num] = new RestData(num, rest(rest, num));
			return data[rest][num].result;
		}
		return rest(rest, num);
	}
	
}
class RestData{
	int num = 0;
	int[][] result;
	
	RestData(int num, int[][] result){
		this.num = num;
		this.result = result;
	}
}
