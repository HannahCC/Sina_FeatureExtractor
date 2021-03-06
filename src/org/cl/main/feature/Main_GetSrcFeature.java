package org.cl.main.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.cl.configuration.Config;
import org.cl.run.GetSrcFeature;
import org.cl.service.GetInfo;
import org.cl.service.MyRejectHandler;
import org.cl.service.RWUid;

public class Main_GetSrcFeature {
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Config.corePoolSize,Config.maximumPoolSize,Config.keepAliveTime,
			Config.unit,new LinkedBlockingQueue<Runnable>(),new MyRejectHandler());

	public static void main(String args[]) throws InterruptedException, IOException
	{	
		Map<String, Map<String,Integer>> uid_src_map = new HashMap<String, Map<String,Integer>>();
		GetInfo.getMapMap("WeibosSrc\\Src_map.txt",uid_src_map,"\t",":",0);

		Map<String, Integer> src_dict = new HashMap<String, Integer>();
		GetInfo.getDict("Config\\Dict_Src.txt", src_dict);
		//GetInfo.getDict("Config\\Dict_App.txt", src_dict);//只使用APP作为来源

		String result_filename = "Feature_SRC\\Src_feature.txt";
		//String result_filename = "Feature_SRC\\App_feature.txt";
		RWUid y_ids = GetInfo.getUID("ExpandID0.txt");

		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			if(uid_src_map.containsKey(uid)){
				Map<String,Integer> src_map = uid_src_map.get(uid);
				GetSrcFeature getSrcFeature = new GetSrcFeature(uid,src_dict,src_map,result_filename);
				threadPool.execute(getSrcFeature);
			}else{
				System.out.println(uid+"has no src!");
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