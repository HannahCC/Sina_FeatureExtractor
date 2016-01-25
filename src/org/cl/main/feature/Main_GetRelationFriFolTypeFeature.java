package org.cl.main.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.cl.configuration.Config;
import org.cl.run.GetFriTypeFeature;
import org.cl.service.GetInfo;
import org.cl.service.MyRejectHandler;
import org.cl.service.RWUid;
import org.cl.service.SaveInfo;

public class Main_GetRelationFriFolTypeFeature {
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Config.corePoolSize,Config.maximumPoolSize,Config.keepAliveTime,
			Config.unit,new LinkedBlockingQueue<Runnable>(),new MyRejectHandler());

	public static void main(String args[]) throws InterruptedException, IOException
	{	
		SaveInfo.mkdir("Feature_Relation");
		/*********************************************使用用户类别*****************************************************************/
		/*int topic_number = 100;//与topic type数量一致
		String result_filename = "Feature_Relation\\LDAVFriTopic2000_100_feature.txt";
		int rel_type = 1;
		String result_filename = "Feature_Relation\\LFDMMVFolTopic2000_100_300_0.6_feature.txt";
		int rel_type = 2;
		String result_filename = "Feature_Relation\\LFDMMVFriFolTopic2000_100_300_0.6_feature.txt";
		int rel_type = 3;
		Map<String, double[]> user_type_info = new HashMap<String,double[]>();//获得每个src属于各个topic的可能性
		GetInfo.getArrayMap(user_type_info,topic_number,"Feature_Relation\\VUser_topic_2000_100_LDA.txt","\t"," ",0,1);
		*/
		
		int vector_length = 100; //newtype 33 , type 36
		String result_filename = "Feature_Relation\\VFriTypeVector_feature.txt";
		int rel_type = 1;
		/*String result_filename = "Feature_Relation\\HLVFolNewType_Expand25_feature.txt";
		int rel_type = 2;*/
		/*String result_filename = "Feature_Relation\\HLVFriFolNewType_Expand25_feature.txt";
		int rel_type = 3;*/
		//获取所有用户的信息
		Map<String, double[]> user_type_info = new HashMap<String, double[]>();
		GetInfo.getArrayMap(user_type_info, vector_length, "Feature_Relation\\VUser_type_vector.txt","\t"," ",0,1);
		//Map<String, Set<String>> user_type_info = new HashMap<String,Set<String>>();
		//GetInfo.getFriTypeInfo(user_type_info,"Feature_Relation\\VUser_newtype.txt");


		RWUid y_ids = GetInfo.getUID("ExpandID0.txt");

		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			GetFriTypeFeature getFriTypeFeature = new GetFriTypeFeature(uid,user_type_info,result_filename,rel_type);
			threadPool.execute(getFriTypeFeature);
		}

		threadPool.shutdown();
		while (threadPool.isTerminated()) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}