package org.cl.main.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.cl.configuration.Config;
import org.cl.run.GetTagTypeFeature;
import org.cl.service.GetInfo;
import org.cl.service.MyRejectHandler;
import org.cl.service.RWUid;

public class Main_GetTagTypeFeature {
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Config.corePoolSize,Config.maximumPoolSize,Config.keepAliveTime,
			Config.unit,new LinkedBlockingQueue<Runnable>(),new MyRejectHandler());

	public static void main(String args[]) throws InterruptedException, IOException
	{	
		Map<String, Set<String>> uid_Tag_map = new HashMap<String, Set<String>>();
		GetInfo.getSetMap(uid_Tag_map, "Feature_UserInfo\\UserIdTag.txt", "\t", "\\s+",0,1);

		Map<String, Set<String>> Tag_type = new HashMap<String,Set<String>>();
		GetInfo.getTagTypeInfo(Tag_type,"Feature_UserInfo\\Tag_type_Expand25.txt");
		String result_filename = "Feature_UserInfo\\TagType_Expand25_feature.txt";
		RWUid y_ids = GetInfo.getUID("ExpandID0.txt");
		
		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			if(uid_Tag_map.containsKey(uid)){
				Set<String> Tag_set = uid_Tag_map.get(uid);
				GetTagTypeFeature getTagFeature = new GetTagTypeFeature(uid,Tag_type,Tag_set,result_filename);
				threadPool.execute(getTagFeature);
			}else{
				System.out.println(uid+"has no Tag!");
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