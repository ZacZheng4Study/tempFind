package com.filetool.util;

import java.util.HashMap;

public class MatrixDG {
public int[] v;
public int[][]matrix;
public int max;
public HashMap<MyArray,Integer> pathCode;
private int[] tempPath;
public MatrixDG(String topo){
	pathCode=new HashMap<MyArray,Integer>();
	tempPath=new int[4];
	String[] topoOneLine=topo.split("\n");
	for(int k=0;k<topoOneLine.length;k++){
		String[] temp=topoOneLine[k].split(",");
		for(int q=0;q<4;q++){
			tempPath[q]=Integer.valueOf(temp[q]);
		}
		if(tempPath[1]>max){
			max=tempPath[1];
		}
		if(tempPath[2]>max){
			max=tempPath[2];
		}
	}
	matrix=new int[max+1][max+1];
	for(int k=0;k<topoOneLine.length;k++){
		String[] temp=topoOneLine[k].split(",");
		for(int q=0;q<4;q++){
			tempPath[q]=Integer.valueOf(temp[q]);
		}
		matrix[tempPath[1]][tempPath[2]]=tempPath[3];
		int[] tempCode=new int[]{tempPath[1],tempPath[2]};
		MyArray mA=new MyArray(tempCode);
		pathCode.put(mA, tempPath[0]);
	}
}
}
