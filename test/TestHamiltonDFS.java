package test;

import arg.HamiltonDFS;

public class TestHamiltonDFS {
	public static void main(String[] args) {
		HamiltonDFS obj = new HamiltonDFS();
		for (int i = 10; i < 900; i++) {
			int[][] G = HamiltonDFS.EdgeWeightedDigraph(18, i);
			long begin = System.currentTimeMillis();
			for(int j = 1; j < 10; j++) {
				obj.HamiltonPath(G, j, 50); // list all Hamiltonian paths start at
											// point
			}
			long end = System.currentTimeMillis();
			System.out.printf("%d %d %d\n", i, end - begin, obj.getPathArray().size());
//			for (HamiltonDFS.PathInfo pathInfo : obj.getPathArray()) {
//				if (testPathConnectInGraph(pathInfo.getPath(), G)) {
//					System.out.printf("%s\n", true);
//				} else {
//					System.out.printf("%s\n", false);
//				}
//			}
		}
	}

	public static boolean testPathConnectInGraph(int[] path, int[][] G) {
		int len = path.length;
		for (int i = 0; i < len - 1; i++) {
			// System.out.printf("%d \n", G[path[i+1]-1][path[i]-1]);
			if (G[path[i + 1] - 1][path[i] - 1] == 0) {
				return false;
			}
		}
		return true;

	}
}