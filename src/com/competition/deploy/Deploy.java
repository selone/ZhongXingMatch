package com.competition.deploy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.competition.algorithm.BFSManager;
import com.competition.algorithm.DijkstraManager;
import com.competition.algorithm.NOSPManager;
import com.competition.graph.Edge;
import com.competition.graph.Graph;
import com.competition.util.Combine;
import com.competition.util.Rest;

public class Deploy
{
	private Graph G;//图
	private BFSManager bfsManager;
	private DijkstraManager dijManager;
	private NOSPManager nospManager;
	private int[][] array;//所有必经点路径
	private int[][] arrayEdge;//所有必经点路径上的边
	private Rest rest;//余量
	private int[] appointPoint;//必经点
	private List<int[]> appointEdge = new ArrayList<int[]>();//必经边
	private int[] appointAll;//正向索引
	private int[] appointAllR;//反向索引
	private int[][] virticesMin;//各必经点间的最少顶点数
	private int[][] virticesMax;//各必经点间的最大顶点数
	private int[] arrayBFScost;//各路径下的最短顶点数，数组的最小值为最短顶点数
	private int[] arrayDijcost;//各路径的最少花费，最小值为最少花费
	private int numLimit;//用户输入的最大节点限制数
	private int sureNumLimit = Integer.MAX_VALUE;//迪杰斯特拉算出来的最少花费的节点数
	private int leastNumLimit = Integer.MAX_VALUE;//BFS算出来的最少需要的节点数
	private static List<int[]> result = new LinkedList<int[]>();//存放结果
	private int start;//起点
	private int aim;//终点

    public static String[] deployServer(String[] graphContent)
    {
    	Deploy deploy = new Deploy();
    	deploy.assemb(graphContent);
    	if(result.isEmpty()){
    		return new String[]{"无解"};
    	}
    	String[] path = new String[result.size()];
    	int mark = 0;
    	for(int[] i: result){
    		String s = "";
    		for(int j: i){
    			s = s + j + " ";
    		}
    		path[mark++] = s;
    	}
    	
    	return path;
    }
    
    //装配
    public void assemb(String[] graphContent){
    	int flag = init(graphContent);
    	
    	if(flag == 1){
    		return;
    	}else if(flag == 2){
    		result.clear();
    	}else if(flag == 0){
    		System.out.println("无解");
    		result.clear();
    		return;
    	}
    	
    	nospManager = new NOSPManager(G, virticesMax, appointAll, appointAllR, bfsManager);
    	int costTemp = Integer.MAX_VALUE;//花费
    	Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
    	for(int i = 0; i < array.length; i++){
    		if(arrayBFScost[i] > numLimit)
    			continue;
    		int[] pathTemp = array[i];
    		int[][] distribution = rest.getRest(numLimit - arrayBFScost[i], appointAll.length - 1);
    		for(int x = 0; x < distribution.length; x++){
    			int sum = 0;
    			for(int y = 0; y < distribution[0].length; y++){
    				int before = pathTemp[y];
    				int after = pathTemp[y + 1];
    				if(arrayEdge[i][y] == 1){
    					sum += G.getEdge(before, after).getWeight();//此处若利用矩阵可以更快，但是需要更大的存储空间
    					continue;
    				}
    				sum += nospManager.dist(before, after, distribution[x][y] + virticesMin[appointAllR[before]][appointAllR[after]]);
    			}
    			if(costTemp > sum){
    				costTemp = sum;
    				map.clear();
    				List<Integer> list = new ArrayList<Integer>();
    				list.add(x);
    				map.put(i, list);
    			}else if(costTemp == sum){
    				List<Integer> list = map.get(i);
    				if(list == null){
    					list = new ArrayList<Integer>();
    				}
    				list.add(x);
    				map.put(i, list);
    			}
    		}
    	}
    	
    	//还原路径
    	List<int[]> resultCode = new LinkedList<int[]>();
    	Set<Integer> set = map.keySet();
    	for(int i: set){
    		int[] pathTemp = array[i];
    		int[][] distribution = rest.getRest(numLimit - arrayBFScost[i], appointAll.length - 1);
    		for(int x: map.get(i)){
    			List<Integer> listTemp = new ArrayList<Integer>();//路径
    			for(int y = 0; y < distribution[0].length; y++){
    				int before = pathTemp[y];
    				int after = pathTemp[y + 1];
    				if(arrayEdge[i][y] == 1){
    					listTemp.add(before);
    					listTemp.add(after);
    					listTemp.add(-1);
    					listTemp.add(-2);
    					continue;
    				}
    				Stack<Integer>[] resultTemp = nospManager.path(before, after, distribution[x][y] + virticesMin[appointAllR[before]][appointAllR[after]]);
    				for(Stack<Integer> z:resultTemp){
    					while(!z.isEmpty()){
    						listTemp.add(z.pop());
    					}
    					listTemp.add(-1);
    				}
    				listTemp.add(-2);
    			}
    			int[] resultTemp = new int[listTemp.size()];
    			for(int z = 0; z < listTemp.size(); z++){
    				resultTemp[z] = listTemp.get(z);
    			}
    			resultCode.add(resultTemp);
    		}
    		
    	}
    	
    	//解码
    	result = decodeAll(resultCode);
    	
    	//输出
    	System.out.println("节点数为: " + numLimit);
    	System.out.println("总花费是: " + costTemp);
    	for(int[] i: result){
    		for(int j: i){
    			System.out.print(j + " ");
    		}
    		System.out.println();
    	}
    	
    }
    //初始化
    public int init(String[] graphContent){
    	saxContent(graphContent);
    	bfsManager = new BFSManager(G);
    	dijManager = new DijkstraManager(G);
    	for(int i:appointAll){
    		int check = bfsManager.dist(start, i);
    		if(check == Integer.MAX_VALUE)
    			return 0;
    	}
    	for(int[] i: appointEdge){
    		int check = bfsManager.dist(i[0], i[1]);
    		if(check > 1)
    			return 0;
    	}
    	virticesMin = new int[appointAll.length][appointAll.length];
    	for(int i = 0; i < appointAll.length; i++){
    		for (int j = 0; j < appointAll.length; j++) {
    			virticesMin[i][j] = bfsManager.dist(appointAll[i], appointAll[j]) + 1;
			}
    	}
    	virticesMax = new int[appointAll.length][appointAll.length];
    	int[][][] arrayTemp = Combine.combine(appointEdge, appointAll, start, aim);
    	array = arrayTemp[0];
    	arrayEdge = arrayTemp[1];
    	rest = new Rest();
    	arrayBFScost = new int[array.length];
    	arrayDijcost = new int[array.length];
    	//计算arrayBFScost和arrayDijcost
    	int dijCostMin = Integer.MAX_VALUE;//最小花费
    	List<Integer> dijCostMinIndex = new ArrayList<Integer>();//最小花费索引
    	for(int i = 0; i < array.length; i++){
    		int bfsTemp = 0;
    		int dijTemp = 0;
    		for(int j = 0; j < array[0].length - 1; j++){
    			int v = array[i][j];
    			int w = array[i][j + 1];
    			bfsTemp = bfsTemp + bfsManager.dist(v, w);
    			if(arrayEdge[i][j] == 1){
    				dijTemp = dijTemp + G.getEdge(v, w).getWeight();//此处要不要改成利用矩阵
    				continue;
    			}
    			dijTemp = dijTemp + dijManager.dist(v, w);
    		}
    		arrayBFScost[i] = bfsTemp + 1;
    		if(arrayBFScost[i] < leastNumLimit)
    			leastNumLimit = arrayBFScost[i];
			arrayDijcost[i] = dijTemp;
			if(dijCostMin > arrayDijcost[i]){
				dijCostMin = arrayDijcost[i];
				dijCostMinIndex.clear();
				dijCostMinIndex.add(i);
			}else if(dijCostMin == arrayDijcost[i]){
				dijCostMinIndex.add(i);
			}
    	}
    	//算出sureNumLimit
    	for(int dijCostMinIndexTemp:dijCostMinIndex)
    	{
    		List<Integer> pathTemp = new LinkedList<Integer>();
	    	int[] i = array[dijCostMinIndexTemp];
			int num = 0;
			for(int j = 0; j < i.length - 1; j++){
				int v = i[j];
				int w = i[j + 1];
				if(arrayEdge[dijCostMinIndexTemp][j] == 1){
					num++;
					pathTemp.add(v);
					continue;
				}
				Stack<Edge> resultTemp = dijManager.path(v, w);
				num += resultTemp.size();
				while(!resultTemp.isEmpty()){
					pathTemp.add(resultTemp.pop().getV());
				}
			}
			pathTemp.add(aim);
			int[] temp = new int[pathTemp.size()];
			int mark = 0;
			for(int z:pathTemp){
				temp[mark++] = z;
			}
			result.add(temp);
			if(sureNumLimit > num + 1)
				sureNumLimit = num + 1;
    	}
    	    	
    	//合理化numLimit
    	if(numLimit > sureNumLimit){
    		numLimit = sureNumLimit;
    	}else if(numLimit < leastNumLimit){
    		System.out.println("最少需要节点数位：" + leastNumLimit);
    		System.out.println("若没有节点数限制，最小花费为" + dijCostMin);
    		for(int[] i: result){
    			for(int j:i){
    				System.out.print(j + " ");
    			}
    			System.out.println();
    		}
    		return 1;//无最优解
    	}
    	
    	//计算virticeMax
    	for(int i = 0; i < array.length; i++){
    		if(arrayBFScost[i] > numLimit)
    			continue;
    		for(int j = 0; j < array[i].length - 1; j++){
    			int before = array[i][j];
    			int after = array[i][j + 1];
    			int temp = numLimit - arrayBFScost[i] + virticesMin[appointAllR[before]][appointAllR[after]];
    			if(virticesMax[appointAllR[before]][appointAllR[after]] < temp || virticesMax[appointAllR[after]][appointAllR[before]] < temp){
    				virticesMax[appointAllR[before]][appointAllR[after]] = temp;
    				virticesMax[appointAllR[after]][appointAllR[before]] = temp;
    			}
    		}
    	}
    	
    	return 2;//有最优解
    }
    //此方法用来预处理输入的文件信息
    public void saxContent(String[] graphContent){
    	int mark = 0;
    	Set<Integer> setTemp = new HashSet<Integer>();//用来存放必经点和必经边所有的点，利用set不能重复的特点，过滤重复点
		for(String temp:graphContent){
			if(temp.equals("")){
				mark++;
				continue;
			}
			String[] line=temp.split(" ");
			switch (mark){
			case 0:
				start = Integer.parseInt(line[0]);
				aim = Integer.parseInt(line[1]);
				G = new Graph(Integer.parseInt(line[2]));
				numLimit = Integer.parseInt(line[3]);
				setTemp.add(start);
				setTemp.add(aim);
				break;
			case 1://添加边
				Edge e = new Edge(Integer.parseInt(line[0]) , Integer.parseInt(line[1]) , Integer.parseInt(line[2]));
				G.addEdge(e);
				e = new Edge(Integer.parseInt(line[1]) , Integer.parseInt(line[0]) , Integer.parseInt(line[2]));
				G.addEdge(e);
				break;
			case 2://移除不可过边
				int v = Integer.parseInt(line[0]);
				int w = Integer.parseInt(line[1]);
				G.removeEdge(v , w);
				e = G.getEdge(Integer.parseInt(line[1]), Integer.parseInt(line[0]));
				G.removeEdge(w, v);
				break;
			case 3://必经点
				appointPoint = new int[line.length];
				for(int i = 0 ; i < line.length ; i++){
					int s = Integer.parseInt(line[i]);
					appointPoint[i] = s;
					setTemp.add(s);
				}
				Arrays.sort(appointPoint);
				break;
			case 4://必经边
				int[] edgetemp = new int[2];
				edgetemp[0] = Integer.parseInt(line[0]);
				edgetemp[1] = Integer.parseInt(line[1]);
				setTemp.add(edgetemp[0]);
				setTemp.add(edgetemp[1]);
				Arrays.sort(edgetemp);
				appointEdge.add(edgetemp);
				break;
			default:
			}
		}
		appointAll = new int[setTemp.size()];
		int k = 0;
		for(int i:setTemp){
			appointAll[k++] = i;
		}
		Arrays.sort(appointAll);
		appointAllR = new int[G.V()];
		for(int i = 0;i<appointAllR.length ; i++){
			appointAllR[i] = Integer.MAX_VALUE;
		}
		for(int i = 0;i<appointAll.length ; i++){
			appointAllR[appointAll[i]] = i;
		}
    }
    
    private List<Integer>[] decode(int[] in){
    	int area = 0;//有几个-2
    	List<Integer> sta = new ArrayList<Integer>();//有几个-1
    	int pathNum = 1;//有多少条路径
    	int staNum = 0;
    	for(int i:in){
    		if(i == -1){
    			staNum++;
    		}
    		if(i == -2){
    			pathNum *= staNum;
    			sta.add(staNum);
    			staNum = 0;
    			area++;
    		}
    	}
    	List<Integer>[] path = new List[pathNum];
    	for (int i = 0; i < path.length; i++) {
			path[i] = new ArrayList<Integer>();
		}
    	int base = 0;
    	int baseBack = 0;
    	int mark = 0;
    	
    	for(int i = 0; i < area; i++){//区
    		for(int j = 0; j < sta.get(i); j ++){//段
    			List<Integer> temp = new ArrayList<Integer>();//存放取到的数据
    			for(int k = base; k < in.length; k++){//填充temp
    				base++;
    				if(in[k] == -1){
    					break;
    				}
    				if(in[k] == -2){
    					continue;
    				}
    				temp.add(in[k]);
    			}
    			int fillNum = 1;//填充次数
    			for(int k = i + 1; k < area; k++){
    				fillNum *= sta.get(k);
    			}
    			//循环填充
    			for(int k = mark; k < mark + fillNum; k++){
    				path[k].addAll(temp.subList(0, temp.size() - 1));
    			}
    			mark += fillNum;
    		}
	    	if(mark < pathNum){
				i--;
				base = baseBack;
			}else{
				baseBack = base;
				mark = 0;
			}
    	}
    	for(int i = 0 ; i < pathNum; i++){
    		path[i].add(in[in.length - 3]);
    	}
    	return path;
    }
    
    private List<int[]> decodeAll(List<int[]> in){
    	List<int[]> result = new ArrayList<int[]>();
    	for(int i = 0; i < in.size(); i++){
    		List<Integer>[] paths = decode(in.get(i));
    		int[] resultTemp = null;
    		for(List<Integer> j: paths){
    			resultTemp = new int[j.size()];
    			for (int j2 = 0; j2 < resultTemp.length; j2++) {
					resultTemp[j2] = j.get(j2);
				}
    		}
    		result.add(resultTemp);
    	}
    	return result;
    	
    }
}
