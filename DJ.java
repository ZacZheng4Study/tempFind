package com.routesearch.route;

import java.util.ArrayList;
import java.util.HashMap;

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
	ArrayList<Integer> neededPoint=new ArrayList<Integer>();//需要求得点
	public HashMap<MyArray,Integer> pathCode;//旧的路径
	public HashMap<MyArray,ArrayList<Integer>> newPathCode;//新的路径
	public DJ(MatrixDG mdg,Destination dt){
		matrix=mdg.matrix;
		matrixNoEnd=mdg.matrix;
		max=mdg.max;
		pathCode=mdg.pathCode;
		answer=new int[dt.middle.length+1][dt.middle.length+1];
		newPathCode=new HashMap<MyArray,ArrayList<Integer>>();
		for(int i=0;i<dt.middle.length;i++){
			neededPoint.add(dt.middle[i]);//middle已被排序
			}
		start=dt.start;
		for(int i=0;i<max;i++){
			matrixNoEnd[dt.end][i]=0;
			matrixNoEnd[i][dt.end]=0;
		}
		}
	public int[][] findDJ(){
		int[] temp=DJEdition(matrixNoEnd,start);
		for(int k=1;k<answer.length;k++){
			answer[0][k]=temp[k-1];
		}
		deleteDuplicate();
		int m=1;
		for(int i:neededPoint){
			temp=DJEdition(matrixNoEnd,i);
			for(int k=1;k<answer.length;k++){
			answer[m][k]=temp[k-1];
			}
			deleteDuplicate();
			m=m+1;
		}
		return answer;
	}
	private int[] DJEdition(int[][] weight, int start){
		 int n = weight.length;      //顶点个数
		 HashMap<MyArray,ArrayList<Integer>> pass=new HashMap<MyArray,ArrayList<Integer>>();
		passPoint=new ArrayList<Integer>();//保存到达目的地时路上的点
		 int[] shortPath = new int[n];  //保存start到其他各点的最短路径
		 int[] visited = new int[n];   //标记当前该顶点的最短路径是否已经求出,1表示已求出   
		 //初始化，第一个顶点已经求出
		 shortPath[start] = 0;
		 visited[start] = 1;  
		 for(int count = 1; count < n; count++) {   //要加入n-1个顶点
		  int k = -1;        //选出一个距离初始顶点start最近的未标记顶点 
		  int dmin = Integer.MAX_VALUE;
		  for(int i = 0; i < n; i++) {
				if (visited[i] == 0 && weight[start][i]!=0&&weight[start][i] < dmin) {
		   dmin = weight[start][i];
		   k = i;
		  }
		  }
		  //将新选出的顶点标记为已求出最短路径，且到start的最短路径就是dmin 
		  if(k==-1){
			  break;
		  }
		  shortPath[k] = dmin;
		  visited[k] = 1;  
		  if(neededPoint.contains(k)){
			int[] keyInt={start,k};
			MyArray key=new MyArray(keyInt);
			ArrayList<Integer> tempPathCode=new ArrayList<Integer>();
			if(pass.get(key)!=null){
				ArrayList<Integer> temp=pass.get(key);
				if(temp.size()==1){
					int[] key1Int={start,temp.get(0)};
					MyArray key1=new MyArray(key1Int);
					int[] key2Int={temp.get(0),k};
					MyArray key2=new MyArray(key2Int);
					tempPathCode.add(pathCode.get(key1));
					tempPathCode.add(pathCode.get(key2));
				}
				else{
					int[] key1Int={start,temp.get(0)};
					MyArray key1=new MyArray(key1Int);
					tempPathCode.add(pathCode.get(key1));
					for(int s=1;s<temp.size();s++){
						key1Int[0]=temp.get(s-1);
						key1Int[1]=temp.get(s);
						tempPathCode.add(pathCode.get(key1));
					}
					key1Int[0]=temp.get(temp.size()-1);
					key1Int[1]=k;
					key1=new MyArray(key1Int);
					tempPathCode.add(pathCode.get(key1));
				}
			for(int i:temp){
				passPoint.add(i);//判断到达所需顶点后，将路过点加入经过点中
			}}
			else{
				tempPathCode.add(pathCode.get(key));
			}
			newPathCode.put(key, tempPathCode);
		  }
		  else{
		  //以k为中间点，修正从start到未访问各点的距离 
		  for(int i = 0; i < n; i++) {
		  if(visited[i] == 0 &&weight[k][i]!=0&& (weight[start][i]==0||weight[start][k] + weight[k][i] < weight[start][i])) {
		   weight[start][i] = weight[start][k] + weight[k][i];
		   
		   int[] key1Int={start,i};
		   int[] key2Int={start,k};
		   MyArray key1=new MyArray(key1Int);
		   MyArray key2=new MyArray(key2Int);
//		   if(!pass.containsKey(key2)){
//			   System.out.println(start+","+k+"isEmptyiskey2");
//		   }
//		   else{
//			   System.out.println(start+","+k+"isNotEmptyiskey2");
//		   }
		   ArrayList<Integer> middlePass=(pass.get(key2)==null?new ArrayList<Integer>():pass.get(key2));
		   middlePass.add(k);
		   pass.put(key1, middlePass);
//		   int[] key3Int={start,i};
//		   MyArray key3=new MyArray(key3Int);
//		   if(!pass.containsKey(key1)){
//				 
//			   System.out.println(start+","+i+"isEmptykey1");
//		   }
//		   else{
//			   System.out.println(start+","+i+"isNotEmptykey1");
//		   }
//		   if(!pass.containsKey(key3)){
//				 
//			   System.out.println(start+","+i+"isEmptykey3");
//		   }
//		   else{
//			   System.out.println(start+","+i+"isNotEmptykey3");
//		   }
//		   int ss=0;
		//   pass.replace(key1,middlePass);//更新到某一点最短路径时路过的点
		 //  ArrayList<Integer> temp=newPathCode.get(key2)==null?new ArrayList<Integer>():newPathCode.get(key2);
		  }
		  }}
	}
		 int[] answer=new int[neededPoint.size()];
		 int k=0;
		 for(int i=0;i<n;i++){
			 if(neededPoint.contains(i)){
				 answer[k]=shortPath[i];
				 k=k+1;
			 }
		 }
		 return answer;
}
	private void deleteDuplicate(){
		for(int i:passPoint){
			for(int k=0;k<max;k++){
				matrixNoEnd[i][k]=0;
				matrixNoEnd[k][i]=0;
			}
		}
	}
	public void savePath(){
		
	}
	public static int[] dijkstra(int[][] weight, int start) {
		 //接受一个有向图的权重矩阵，和一个起点编号start（从0编号，顶点存在数组中） 
		    //返回一个int[] 数组，表示从start到它的最短路径长度 
		 int n = weight.length;      //顶点个数
		 int[] shortPath = new int[n];  //保存start到其他各点的最短路径
		 String[] path = new String[n];  //保存start到其他各点最短路径的字符串表示
		 for(int i=0;i<n;i++) 
		  path[i]=new String(start+"-->"+i); 
		 int[] visited = new int[n];   //标记当前该顶点的最短路径是否已经求出,1表示已求出 
		  
		 //初始化，第一个顶点已经求出
		 shortPath[start] = 0;
		 visited[start] = 1;
		  
		 for(int count = 1; count < n; count++) {   //要加入n-1个顶点
		  int k = -1;        //选出一个距离初始顶点start最近的未标记顶点 
		  int dmin = Integer.MAX_VALUE;
		  for(int i = 0; i < n; i++) {
		  if(visited[i] == 0 && weight[start][i] < dmin) {
		   dmin = weight[start][i];
		   k = i;
		  }
		  }
		   
		  //将新选出的顶点标记为已求出最短路径，且到start的最短路径就是dmin 
		  shortPath[k] = dmin;
		  visited[k] = 1;
		   
		  //以k为中间点，修正从start到未访问各点的距离 
		  for(int i = 0; i < n; i++) {
		  if(visited[i] == 0 && weight[start][k] + weight[k][i] < weight[start][i]) {
		   weight[start][i] = weight[start][k] + weight[k][i];
		   path[i] = path[k] + "-->" + i; 
		  }
		  }
		 }
		 for(int i = 0; i < n; i++) {
		  System.out.println("从"+start+"出发到"+i+"的最短路径为："+path[i]);
		 }
		 System.out.println("====================================="); 
		 return shortPath;
		 }
	
	}
	
