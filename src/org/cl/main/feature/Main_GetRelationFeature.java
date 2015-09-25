package org.cl.main.feature;
import java.io.IOException;

import org.cl.run.GetRelationFeature1;
import org.cl.run.GetRelationFeature2;
import org.cl.run.GetRelationFeature3;
import org.cl.run.GetRelationFeature4;
import org.cl.service.SaveInfo;
/**
 * 提取关系特征（前提：只针对原始ID列表中每组之间用空格隔开的情况，且atData数据已准备好；关系数据准备好；大V用户ID数据准备好<YellowV_ID.txt>）
id1
id2
1是否2经常@的人（前3或前10），Feature1
2是否1经常@的人（前3或前10），Feature1
是否有共同@的第3个人		  Feature1

是否共同出现在第3个人的@对象中，Feature2

两人有没有共同好友		  Feature3
粉丝的共同普通好友数		  Feature3
粉丝的共同大V好友数		  Feature3
关注的共同普通好友数		  Feature3
关注的共同大V好友数		  Feature3
//解决：用3个处理程序分别获取不同的关系特征，写入三个不同的文件，最后再合并为一个
 * @author Administrator
 * params:文件名
 */
public class Main_GetRelationFeature {
	public static void main(String args[]) throws IOException, InterruptedException
	{	
		SaveInfo.mkdir("Feature_Relation");
		//读取用户ID放入ids[hashSet]
		GetRelationFeature1.func_GetRelationFeature1();
		GetRelationFeature2.func_GetRelationFeature2();
		GetRelationFeature3.func_GetRelationFeature3();
		GetRelationFeature4.func_GetRelationFeature4();
	}
}
