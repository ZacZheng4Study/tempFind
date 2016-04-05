package com.routesearch.route;

import java.util.ArrayList;
import java.util.HashMap;

import com.filetool.util.DJOut;
import com.filetool.util.Destination;
import com.filetool.util.MatrixDG;
import com.filetool.util.MyArray;

public class DJ {
	int[][] matrix;
	int[][] matrixNoEnd;
	int max;
	ArrayList<Integer> passPoint;
	int start;
	int[][] answer;
	ArrayList<Integer> neededPoint = new ArrayList<Integer>();// 需要求得点
	public HashMap<MyArray, Integer> pathCode;// 旧的路径
	public HashMap<MyArray, ArrayList<Integer>> newPathCode;// 新的路径
	public HashMap<Integer, Integer> convertPointMap;

	public DJ(MatrixDG mdg, Destination dt) {
		matrix = mdg.matrix.clone();
		matrixNoEnd = mdg.matrix.clone();
		max = mdg.max;
		pathCode = mdg.pathCode;
		answer = new int[dt.middle.length + 1][dt.middle.length + 1];
		newPathCode = new HashMap<MyArray, ArrayList<Integer>>();
		for (int i = 0; i < dt.middle.length; i++) {
			neededPoint.add(dt.middle[i]);// middle已被排序
		}
		start = dt.start;
		for (int i = 0; i < max; i++) {
			matrixNoEnd[dt.end][i] = 0;
			matrixNoEnd[i][dt.end] = 0;
		}
		convertPointMap = new HashMap<Integer, Integer>();
		convertPointMap.put(dt.start, 1);
		for (int i = 2; i < dt.middle.length + 2; i++) {
			convertPointMap.put(dt.middle[i - 2], i);
		}
	}

	private int[] sortMiddlePoint() {
		int[][] sortData = new int[neededPoint.size()][2];
		int s = 0;
		for (int i : neededPoint) {
			int t = 0;
			for (int k = 0; k < matrixNoEnd.length; k++) {
				if (matrixNoEnd[i][k] != 0) {
					t++;
				}
			}
			sortData[s][0] = t;
			sortData[s][1] = i;
			s = s + 1;
		}
		quickSort(sortData, 0, neededPoint.size() - 1);
		int[] sortPoint = new int[neededPoint.size()];
		for (int i = 0; i < neededPoint.size(); i++) {
			sortPoint[i] = sortData[i][1];
		}
		return sortPoint;
	}

//	public DJOut findDJ(int rand) {
//		int[] temp;
//		int[] sortPoint = sortMiddlePoint();
//		int randSeed=3;
//		while(randSeed+rand<sortPoint.length){
//			int tempRand=sortPoint[randSeed];
//			sortPoint[randSeed]=sortPoint[randSeed+rand];
//			sortPoint[randSeed+rand]=tempRand;
//			randSeed=randSeed+2;
//		}
//		for (int i : sortPoint) {
//			temp = DJEdition(matrixNoEnd, i);
//			for (int k = 1; k < answer.length; k++) {
//				answer[convertPointMap.get(i) - 1][k] = temp[k - 1];
//			}
//			deleteDuplicate();
//		}
//		temp = DJEdition(matrixNoEnd, start);
//		for (int k = 1; k < answer.length; k++) {
//			answer[0][k] = temp[k - 1];
//		}
//		deleteDuplicate();
//		int[][] trans = new int[answer.length][answer.length];
//		for (int i = 0; i < answer.length; i++) {
//			for (int j = 0; j < answer.length; j++) {
//				trans[i][j] = answer[j][i];
//			}
//		}
//		DJOut djout = new DJOut();
//		djout.answer = trans;
//		djout.newPathCode = newPathCode;
//		return djout;
//	}

	public DJOut findDJ() {
		int[] temp = DJEdition(matrixNoEnd, start);
		for (int k = 1; k < answer.length; k++) {
			answer[0][k] = temp[k - 1];
		}
	//	deleteDuplicate();

		int m = 1;
		int[] sortPoint = sortMiddlePoint();
		for (int i : neededPoint) {
			// for (int i : sortPoint) {
			temp = DJEdition(matrixNoEnd, i);

			for (int k = 1; k < answer.length; k++) {
				// answer[convertPointMap.get(i)-1][k] = temp[k - 1];
				answer[m][k] = temp[k - 1];
			}
		//	deleteDuplicate();
			m = m + 1;
		}
		int[][] trans = new int[answer.length][answer.length];
		for (int i = 0; i < answer.length; i++) {
			for (int j = 0; j < answer.length; j++) {
				trans[i][j] = answer[j][i];
			}
		}
		DJOut djout = new DJOut();
		djout.answer = trans;
		djout.newPathCode = newPathCode;
		return djout;
	}

	private int[] DJEdition(int[][] weight, int start) {
		int n = weight.length; // 顶点个数
		int[] answer = new int[neededPoint.size()];
		HashMap<MyArray, ArrayList<Integer>> pass = new HashMap<MyArray, ArrayList<Integer>>();
		passPoint = new ArrayList<Integer>();// 保存到达目的地时路上的点
		int[] shortPath = new int[n]; // 保存start到其他各点的最短路径
		int[] visited = new int[n]; // 标记当前该顶点的最短路径是否已经求出,1表示已求出
		// 初始化，第一个顶点已经求出
		shortPath[start] = 0;
		visited[start] = 1;
		for (int count = 1; count < n; count++) { // 要加入n-1个顶点
			int k = -1; // 选出一个距离初始顶点start最近的未标记顶点
			int dmin = Integer.MAX_VALUE;
			for (int i = 0; i < n; i++) {
				if (visited[i] == 0 && weight[start][i] != 0
						&& weight[start][i] < dmin) {
					dmin = weight[start][i];
					k = i;
				}
			}
			// 将新选出的顶点标记为已求出最短路径，且到start的最短路径就是dmin
			if (k == -1) {
				break;
			}
			shortPath[k] = dmin;
			visited[k] = 1;
			if (neededPoint.contains(k)) {
				int[] keyInt = { start, k };
				MyArray key = new MyArray(keyInt);
				ArrayList<Integer> tempPathCode = new ArrayList<Integer>();
				if (pass.get(key) != null) {
						ArrayList<Integer> temp = pass.get(key);
						if (temp.size() == 1) {
							int[] key1Int = { start, temp.get(0) };
							MyArray key1 = new MyArray(key1Int);
							int[] key2Int = { temp.get(0), k };
							MyArray key2 = new MyArray(key2Int);
							tempPathCode.add(pathCode.get(key1));
							tempPathCode.add(pathCode.get(key2));
						} else {
							int[] key1Int = { start, temp.get(0) };
							MyArray key1 = new MyArray(key1Int);
							tempPathCode.add(pathCode.get(key1));
							for (int s = 1; s < temp.size(); s++) {
								key1Int[0] = temp.get(s - 1);
								key1Int[1] = temp.get(s);
								tempPathCode.add(pathCode.get(key1));
							}
							key1Int[0] = temp.get(temp.size() - 1);
							key1Int[1] = k;
							key1 = new MyArray(key1Int);
							tempPathCode.add(pathCode.get(key1));
						}
						for (int i : temp) {
							passPoint.add(i);// 判断到达所需顶点后，将路过点加入经过点中
						}
					
				} else {
					tempPathCode.add(pathCode.get(key));
				}
				if (tempPathCode != null) {
					newPathCode.put(key, tempPathCode);
				}

			} else {
				// 以k为中间点，修正从start到未访问各点的距离
				for (int i = 0; i < n; i++) {
					if (visited[i] == 0
							&& weight[k][i] != 0
							&& (weight[start][i] == 0 || weight[start][k]
									+ weight[k][i] < weight[start][i])) {
						weight[start][i] = weight[start][k] + weight[k][i];

						int[] key1Int = { start, i };
						int[] key2Int = { start, k };
						MyArray key1 = new MyArray(key1Int);
						MyArray key2 = new MyArray(key2Int);

						ArrayList<Integer> middlePass = (pass.get(key2) == null ? new ArrayList<Integer>()
								: pass.get(key2));
						middlePass.add(k);
						pass.put(key1, middlePass);
					}
				}
			}
		}

		int k = 0;
		for (int i = 0; i < n; i++) {
			if (neededPoint.contains(i)) {
				answer[k] = shortPath[i];
				k = k + 1;
			}
		}
		return answer;
	}

	private void deleteDuplicate() {
		for (int i : passPoint) {
			for (int k = 0; k < max; k++) {
				matrixNoEnd[i][k] = 0;
				matrixNoEnd[k][i] = 0;
			}
		}
	}

	public static int[] dijkstra(int[][] weight, int start) {
		// 接受一个有向图的权重矩阵，和一个起点编号start（从0编号，顶点存在数组中）
		// 返回一个int[] 数组，表示从start到它的最短路径长度
		int n = weight.length; // 顶点个数
		int[] shortPath = new int[n]; // 保存start到其他各点的最短路径
		String[] path = new String[n]; // 保存start到其他各点最短路径的字符串表示
		for (int i = 0; i < n; i++)
			path[i] = new String(start + "-->" + i);
		int[] visited = new int[n]; // 标记当前该顶点的最短路径是否已经求出,1表示已求出

		// 初始化，第一个顶点已经求出
		shortPath[start] = 0;
		visited[start] = 1;

		for (int count = 1; count < n; count++) { // 要加入n-1个顶点
			int k = -1; // 选出一个距离初始顶点start最近的未标记顶点
			int dmin = Integer.MAX_VALUE;
			for (int i = 0; i < n; i++) {
				if (visited[i] == 0 && weight[start][i] < dmin) {
					dmin = weight[start][i];
					k = i;
				}
			}

			// 将新选出的顶点标记为已求出最短路径，且到start的最短路径就是dmin
			shortPath[k] = dmin;
			visited[k] = 1;

			// 以k为中间点，修正从start到未访问各点的距离
			for (int i = 0; i < n; i++) {
				if (visited[i] == 0
						&& weight[start][k] + weight[k][i] < weight[start][i]) {
					weight[start][i] = weight[start][k] + weight[k][i];
					path[i] = path[k] + "-->" + i;
				}
			}
		}
		for (int i = 0; i < n; i++) {
			System.out.println("从" + start + "出发到" + i + "的最短路径为：" + path[i]);
		}
		System.out.println("=====================================");
		return shortPath;
	}

	private int getMiddle(int[][] list, int low, int high) {
		int tmp = list[low][0]; // 数组的第一个作为中轴
		while (low < high) {
			while (low < high && list[high][0] >= tmp) {
				high--;
			}
			list[low][0] = list[high][0]; // 比中轴小的记录移到低端
			list[low][1] = list[high][1];
			while (low < high && list[low][0] <= tmp) {
				low++;
			}
			list[high][0] = list[low][0]; // 比中轴大的记录移到高端
			list[high][1] = list[low][1];
		}
		list[low][0] = tmp; // 中轴记录到尾
		return low; // 返回中轴的位置
	}

	private void quickSort(int[][] list, int low, int high) {
		if (low < high) {
			int middle = getMiddle(list, low, high); // 将list数组进行一分为二
			quickSort(list, low, middle - 1); // 对低字表进行递归排序
			quickSort(list, middle + 1, high); // 对高字表进行递归排序
		}
	}
}
