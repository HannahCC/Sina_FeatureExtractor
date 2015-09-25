package org.cl.main.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.cl.configuration.Config;
import org.cl.run.GetFriFeature;
import org.cl.service.GetInfo;
import org.cl.service.MyRejectHandler;
import org.cl.service.RWUid;
import org.cl.service.SaveInfo;

public class Main_GetRelationFirFolFeature {
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Config.corePoolSize,Config.maximumPoolSize,Config.keepAliveTime,
			Config.unit,new LinkedBlockingQueue<Runnable>(),new MyRejectHandler());
	
	public static void main(String args[]) throws InterruptedException, IOException
	{	
		SaveInfo.mkdir("Feature_Relation");
		String feature = "Description_feature";
		
		
		
		//获取所有用户特征向量
		Map<String, Map<Integer,Integer>> feature_map = new HashMap<String, Map<Integer,Integer>>();
		GetInfo.getFriInfo(feature_map,"Feature_UserInfo\\"+feature+".txt");
		//获取用户的朋友关系
		Map<String, Set<String>> fri_id_map = new HashMap<String, Set<String>>();
		GetInfo.getRelUidMap(fri_id_map,"UidInfo_follows0.txt");
		GetInfo.getRelUidMap(fri_id_map,"UidInfo_friends0.txt");
		
		//获取原始用户ID
		RWUid y_ids = GetInfo.getUID("ExpandID0.txt");
		
		//合成向量中是否包含自己
		boolean isWithSelf = false;
		
		//将原始用户的朋友特征向量合并作为自己的特征向量
		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			GetFriFeature getFriFeature = new GetFriFeature(uid,feature,feature_map,fri_id_map,isWithSelf);
			threadPool.execute(getFriFeature);
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