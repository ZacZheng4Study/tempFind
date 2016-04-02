package com.filetool.util;

import java.util.Arrays;

public class Destination {//存储目标的数据结构
public int start;//起点
public int end;//终点
public int[] middle;//经过的点
public Destination(String demand){
	String[] demandTemp=demand.split(",");
	start=Integer.valueOf(demandTemp[0]);
	end=Integer.valueOf(demandTemp[1]);
	String[] oneLine=demandTemp[2].split("\n");
	String[] passTemp=oneLine[0].split("\\|");
	middle=new int[passTemp.length];
	for(int k=0;k<passTemp.length;k++){
		middle[k]=Integer.valueOf(passTemp[k]);
	}
	Arrays.sort(middle);
}
}
