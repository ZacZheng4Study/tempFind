package com.filetool.util;

import java.util.Arrays;

public class MyArray {
int[] data;
public MyArray(int[] myData){
	data=myData;
}
@Override
public int hashCode() {
	//System.out.println(data[0]+","+data[1]+"hashcodeis"+Arrays.hashCode(data));
	return Arrays.hashCode(data);
}
@Override
public boolean equals(Object obj) {
	if(obj instanceof MyArray){
		int[] compareData=((MyArray)obj).data;
		for(int i=0;i<compareData.length;i++){
			if(compareData[i]!=this.data[i]){
				return false;
			}
		}
		return true;
	}
	return false;
}

}

