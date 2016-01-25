package org.cl.main.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.cl.configuration.Config;
import org.cl.run.GetFriFolFeature;
import org.cl.service.GetInfo;
import org.cl.service.MyRejectHandler;
import org.cl.service.RWUid;

public class Main_GetFriFolFeature {
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Config.corePoolSize,Config.maximumPoolSize,Config.keepAliveTime,
			Config.unit,new LinkedBlockingQueue<Runnable>(),new MyRejectHandler());

	public static void main(String args[]) throws InterruptedException, IOException
	{	
		Map<String, Set<String>> uid_fri_map = new HashMap<String,Set<String>>();
		GetInfo.getSetMap("UidInfo_friends0.txt", uid_fri_map, "id", "uids");

		Map<String, Integer> src_dict = new HashMap<String, Integer>();
		GetInfo.getDict("Config\\Dict_VFri.txt", src_dict);
		String result_filename = "Feature_Relation\\VFri_feature.txt";
		
		RWUid y_ids = GetInfo.getUID("ExpandID0.txt");

		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			if(uid_fri_map.containsKey(uid)){
				Set<String> fri_set = uid_fri_map.get(uid);
				GetFriFolFeature getSrcFeature = new GetFriFolFeature(uid,src_dict,fri_set,result_filename);
				threadPool.execute(getSrcFeature);
			}else{
				System.out.println(uid+"has no fri!");
			}
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