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
		Map<String, Integer> src_dict = new HashMap<String, Integer>();
		GetInfo.getDict("Config\\Dict_Src.txt", src_dict);
		
		String result_filename = "Feature_SRC\\Src_feature.txt";
		RWUid y_ids = GetInfo.getUID("ExpandID0.txt");
		
		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			GetSrcFeature getSrcFeature = new GetSrcFeature(uid,src_dict,result_filename);
			threadPool.execute(getSrcFeature);
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