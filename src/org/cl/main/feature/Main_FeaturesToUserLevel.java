package org.cl.main.feature;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.cl.configuration.Config;
import org.cl.run.FeaturesToUserLevel;
import org.cl.service.GetInfo;
import org.cl.service.MyRejectHandler;
import org.cl.service.RWUid;
/**
 * 将微博级别的特征转换为用户级别的特征
 * @author Administrator
 * params :文件名
 */
public class Main_FeaturesToUserLevel {
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Config.corePoolSize,Config.maximumPoolSize,Config.keepAliveTime,
			Config.unit,new LinkedBlockingQueue<Runnable>(),new MyRejectHandler());

	/*private static String feature = "Feature_Style";
	private static String[] attrs = new String[]{"Emotion","Buzz","Modal","Punt","Acronym"};*/
	private static String feature = "Feature_Textual";
	private static String[] attrs = new String[]{"Text","POS"};

	public static void main(String args[]) throws IOException, InterruptedException
	{	
		for(String attr:attrs){
			RWUid y_ids = GetInfo.getUIDinDir(feature+"/"+attr);
			String uid = null;
			while (null!=(uid = y_ids.getUid())) {
				FeaturesToUserLevel featuresToUserLevel = new FeaturesToUserLevel(uid,feature,attr);
				threadPool.execute(featuresToUserLevel);
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
