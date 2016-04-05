package com.routesearch.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.filetool.util.DJOneAnswer;
import com.filetool.util.DJOut;
import com.filetool.util.Destination;
import com.filetool.util.MatrixDG;
import com.filetool.util.MyArray;
import com.routesearch.route.HamiltonDFS.PathInfo;

public class LastConnect {
	public int[][] initialMap;
	public DJOut djOut;
	public HashMap<Integer, Integer> pointMap;
	public HashMap<Integer, int[]> pathCodeToPoint;
	private int end;
	public HashMap<MyArray, Integer> pathCode;

	public LastConnect(MatrixDG mdg, DJOut djOut, Destination testD) {
		this.initialMap = mdg.matrix.clone();
		pathCodeToPoint = mdg.pathCodeToPoint;
		pathCode = mdg.pathCode;
		this.djOut = djOut;
		pointMap = new HashMap<Integer, Integer>();
		pointMap.put(1, testD.start);
		for (int i = 2; i < testD.middle.length + 2; i++) {
			pointMap.put(i, testD.middle[i - 2]);
		}
		this.end = testD.end;
	}

	public DJOneAnswer chooseNearst(HamiltonDFS obj) {
		DJOneAnswer answer = new DJOneAnswer();
		List<PathInfo> mPath = obj.getPathArray();
		int min = Integer.MAX_VALUE;
		ArrayList<Integer> pathCodeFinal = new ArrayList<Integer>();
		for (PathInfo i : mPath) {
			int[] path = i.getPath();
			int[][] removedMap = this.produceMap(initialMap, path);
			DJOneAnswer djo = this.dJOneRoad(pathCode, removedMap,
					pointMap.get(path[path.length - 1]), end);
			if (djo.weight == 0) {
				continue;
			}
			if (djo.weight + i.getWeight() < min) {
				min = djo.weight + i.getWeight();
				pathCodeFinal = new ArrayList<Integer>();
				int[] tempPath = i.getPath();
				for (int k = 0; k < tempPath.length - 1; k++) {
					int k1 = pointMap.get(tempPath[k]);
					int k2 = pointMap.get(tempPath[k + 1]);
					ArrayList<Integer> temp = this.djOut.newPathCode
							.get(new MyArray(new int[] { k1, k2 }));
					pathCodeFinal.addAll(temp);
				}
				pathCodeFinal.addAll(djo.pathCode);
			}
		}
		answer.weight = min;
		answer.pathCode = pathCodeFinal;
		return answer;
	}

	private int[][] produceMap(int[][] initialMap, int[] path) {
		int[][] removedMap = initialMap.clone();
		for (int i = 0; i < path.length - 1; i++) {
			int k1 = pointMap.get(path[i]);
			int k2 = pointMap.get(path[i + 1]);
			ArrayList<Integer> temp = this.djOut.newPathCode.get(new MyArray(
					new int[] { k1, k2 }));
			if (temp != null && temp.size() != 0 && !temp.isEmpty()) {
				for (int m = 0; m < temp.size(); m++) {
					if (temp.get(m) != null) {
						int k = temp.get(m);

						if (pathCodeToPoint.containsKey(k)) {
							int[] twoPoint = pathCodeToPoint.get(k);
							for (int j = 0; j < removedMap.length; j++) {
								removedMap[j][twoPoint[0]] = 0;
							//	removedMap[j][twoPoint[1]] = 0;
								removedMap[twoPoint[0]][j] = 0;
							//	removedMap[twoPoint[1]][j] = 0;
							}
						}
					}
				}
			}
		}
		return removedMap;
	}

	private DJOneAnswer dJOneRoad(HashMap<MyArray, Integer> pathCode,
			int[][] weight, int start, int end) {
		int n = weight.length; // 顶点个数
		DJOneAnswer dja = new DJOneAnswer();
		HashMap<MyArray, ArrayList<Integer>> pass = new HashMap<MyArray, ArrayList<Integer>>();
		ArrayList<Integer> passPoint = new ArrayList<Integer>();// 保存到达目的地时路上的点
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
			if (end == k) {
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
				dja.pathCode = tempPathCode;
				dja.weight = dmin;
				return dja;
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
		return dja;
	}
}
