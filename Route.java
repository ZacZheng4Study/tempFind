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
       for(int i=0;i<testD.middle.length;i++){
       System.out.print(testD.middle[i]);}
       DJ test=new DJ(mdg,testD);
       int[][] answer=test.findDJ();
       HamiltonDFS obj = new HamiltonDFS();
       obj.HamiltonPath(answer, 1, 20);
    	return "hello world!";
    }

}