package org.cl.main.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
		/*********************************************使用大V用户类别*****************************************************************/
		/*String result_filename = "Feature_Relation\\VFriType_feature.txt";
		int rel_type = 1;
		//获取所有大V用户的信息
		Map<String, Set<String>> user_type_info = new HashMap<String,Set<String>>();
		GetInfo.getFriTypeInfo(user_type_info,"UserInfoTMP\\UserInfo0.txt.vtype","UserInfoTMP\\UserInfo1.txt.vtype");*/
		/*********************************************使用用户类别*****************************************************************/
		/*String result_filename = "Feature_Relation\\FriType_feature.txt";
		int rel_type = 1;*/
		/*String result_filename = "Feature_Relation\\FolType_feature.txt";
		int rel_type = 2;*/
		String result_filename = "Feature_Relation\\FriFolType_feature.txt";
		int rel_type = 3;
		//获取所有用户的信息
		Map<String, Set<String>> user_type_info = new HashMap<String,Set<String>>();
		GetInfo.getFriTypeInfo(user_type_info,"UserInfoTMP\\UserInfo0.txt.type","UserInfoTMP\\UserInfo1.txt.type");
		
		
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