/**
 * 实现代码文件
 * 
 * @author XXX
 * @since 2016-3-4
 * @version V1.0
 */
package com.routesearch.route;
import com.filetool.util.*;
public final class Route
{
    /**
     * 你需要完成功能的入口
     * 
     * @author XXX
     * @since 2016-3-4
     * @version V1
     */
    public static String searchRoute(String graphContent, String condition)
    {
       MatrixDG mdg=new MatrixDG(graphContent);
       Destination testD =new Destination(condition);
//       for(int i=0;i<testD.middle.length;i++){
//       System.out.print(testD.middle[i]);}
       for(int rand=0;rand<100;rand++){
       DJ test=new DJ(mdg,testD);
       DJOut djOut=test.findDJ();
       HamiltonDFS obj = new HamiltonDFS();
       obj.HamiltonPath(djOut.answer, 1, 1);
       MatrixDG mdg1=new MatrixDG(graphContent);
       LastConnect lc=new LastConnect(mdg1,djOut,testD);
       DJOneAnswer djo=lc.chooseNearst(obj);
       String answer="";
       for(int i:djo.pathCode){
    	   answer+=i+"|";
       }
       
       if(answer.length()==0){
    	  continue;
       }
       else{
       answer=answer.substring(0, answer.length()-1);}
     //  answer+="weight="+djo.weight;
    	return answer;
    }
	return "NA";}

}