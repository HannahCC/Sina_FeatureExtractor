package org.cl.main.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.cl.configuration.Config;
import org.cl.run.GetEmoticonStastic;
import org.cl.service.GetInfo;
import org.cl.service.MyRejectHandler;
import org.cl.service.RWUid;
import org.cl.service.Utils;

public class Main_GetEmoticonStastic {
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Config.corePoolSize,Config.maximumPoolSize,Config.keepAliveTime,
			Config.unit,new LinkedBlockingQueue<Runnable>(),new MyRejectHandler());
	
	public static void main(String args[]) throws IOException, InterruptedException
	{	
		RWUid y_ids = GetInfo.getUIDinDir("Feature_Style\\Emotion");
		Map<String,Integer> emotion_list = new HashMap<String,Integer>();
		Map<String,Integer> emotion_list2 = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Emotion1.txt",emotion_list);
		GetInfo.getDict("Config\\Dict_Emotion2.txt",emotion_list2);
		int middle = emotion_list.size();
		Utils.mergeDict(emotion_list, emotion_list2);

		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			GetEmoticonStastic getEmoticonStastic=new GetEmoticonStastic(uid,emotion_list,middle);
			threadPool.execute(getEmoticonStastic);
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
