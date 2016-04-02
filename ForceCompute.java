package com.routesearch.route;

import java.util.Stack;

import com.filetool.util.Destination;
import com.filetool.util.MatrixDG;

public class ForceCompute {
	MatrixDG mdg;
	Destination testD;
	public ForceCompute(String graphContent,String condition){
		this.init(graphContent, condition);
	}
public void init(String graphContent,String condition){
	  mdg=new MatrixDG(graphContent);
      testD =new Destination(condition);
}
public void forceToRoad(){
	Stack<Integer> in=new Stack<Integer>();
	in.add(testD.start);	
}
private void forward(int point,Stack<Integer> in,int restart ){
	for(int i=restart;i<=mdg.max-1;i++){
		//TODO 成环或到终点
		if(mdg.matrix[point][i]!=0){//深一层
			in.push(i);
			forward(i,in,0);
		}
		else{//回溯
			
		}
	}
}
private void back(){
	
}
}
