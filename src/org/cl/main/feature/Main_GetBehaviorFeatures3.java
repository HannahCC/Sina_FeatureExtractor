package org.cl.main.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.cl.configuration.Config;
import org.cl.run.GetBehaviorFeatures3;
import org.cl.service.GetInfo;
import org.cl.service.MyRejectHandler;
import org.cl.service.RWUid;
import org.cl.service.SaveInfo;
/**
 * 获取用户的行为特征
 * （微博数，粉丝数，发文数）；
 * （发文率，转发率，回复率）
 * @author Administrator
 * params :文件名
 */
public class Main_GetBehaviorFeatures3 {
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Config.corePoolSize,Config.maximumPoolSize,Config.keepAliveTime,
			Config.unit,new LinkedBlockingQueue<Runnable>(),new MyRejectHandler());
	
	private static void init() {
		SaveInfo.mkdir("Feature_Behaviour");
	}
	public static void main(String args[]) throws IOException, InterruptedException
	{	
		init();
		RWUid y_ids = GetInfo.getUIDinDir("Weibos");
		Map<String,Integer> followers_map = new HashMap<String,Integer>();
		GetInfo.getMap("UserInfo0.txt",followers_map,"id","followersCount");
		
		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			GetBehaviorFeatures3 getBehaviorFeatures3=new GetBehaviorFeatures3(uid,followers_map);
			threadPool.execute(getBehaviorFeatures3);
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
