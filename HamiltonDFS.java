/**
 * 实现代码文件
 * 
 * @author XG
 * @since 2016-3-19
 * @version V1.0
 */
package com.routesearch.route;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HamiltonDFS {
	private int len;
	private int[] path;
	private int count = 0;
	private List<PathInfo> pathArray;

	public List<PathInfo> getPathArray() {
		return pathArray;
	}

	public void setPathArray(List<PathInfo> pathArray) {
		this.pathArray = pathArray;
	}

	public void HamiltonPath(int[][] x, int startNode, int pathNo) { // List all possible
		this.len = x.length;
		this.path = new int[len];
		this.path[0] = startNode;
		this.pathArray = new ArrayList<PathInfo>();
		this.count = 0;
		int beginNode = 0;
		int index = 0;
		int pathWeight = 0;
				
		findHamiltonpath(x, startNode - 1, beginNode, index, pathWeight, pathNo);
	}

	private void findHamiltonpath(int[][] M, int startNode, int beginNode, int index, int pathWeight, int pathNo) {

		if(this.count > pathNo){
			return;
		}
		int i;
		for (i = beginNode; i < this.len; i++) { // Go through row

			if (M[i][startNode] != 0) { // 2 point connect

				if (detect(this.path, i + 1))// if detect a point that already in the
										// path => duplicate
					continue;
				int weight = M[i][startNode];
				index++;
				pathWeight += weight; // Increase path length due to 1 new point is
								// connected
				this.path[index] = i + 1; // correspond to the array that start at 0,
									// graph that start at point 1
				if (index == this.len - 1) {// Except initial point already count
									// =>success connect all point
					this.count++;
					//if (this.count == 1)
						//System.out.println("Hamilton path of graph: ");

					//display(this.path);
					PathInfo pathInfo = new PathInfo();
					pathInfo.setWeight(pathWeight);
					pathInfo.setPath(this.path.clone());
					this.pathArray.add(pathInfo);
					pathWeight -= weight;
					index--;
					
					continue;
				}

				M[i][startNode] = 0; // remove the path that has been get and
				findHamiltonpath(M, i, beginNode, index, pathWeight, pathNo); // recursively start to find
													// new
													// path at new end point
				index--;
				pathWeight -= weight; // reduce path length due to the failure to find new path
				M[i][startNode] = weight; // and tranform back to the inital form
								// of adjacent matrix(graph)
			}
		}
		this.path[index + 1] = 0; // disconnect two point correspond the failure to find
							// the..
	} // possible hamilton path at new point(ignore newest point try another
		// one)

	public void display(int[] x) {

		System.out.print(this.count + " : ");
		for (int i : x) {
			System.out.print(i + " ");
		}
		System.out.println();
	}

	private boolean detect(int[] x, int target) { // Detect duplicate point in
													// Halmilton path
		boolean t = false;
		for (int i : x) {
			if (i == target) {
				t = true;
				break;
			}
		}
		return t;
	}
	
	public class PathInfo {
		private int[] path;
		private int weight;

		public int[] getPath() {
			return path;
		}

		public void setPath(int[] path) {
			this.path = path;
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}
	}

	public static int[][] EdgeWeightedDigraph(int V, int E) {
		if (E < 0)
			throw new IllegalArgumentException(
					"Number of edges in a Digraph must be nonnegative");
		int[][] G = new int[V][V];
		long seed = System.currentTimeMillis();
		Random random = new Random(seed);
		for (int i = 0; i < E; i++) {
			int v = random.nextInt(V);
			int w = random.nextInt(V);
			int weight = 1 * random.nextInt(100);

			G[v][w] = weight;
		}
		return G;
	}
}