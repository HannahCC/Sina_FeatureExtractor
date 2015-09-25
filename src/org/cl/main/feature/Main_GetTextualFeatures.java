package org.cl.main.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.cl.configuration.Config;
import org.cl.run.GetTextualFeatures;
import org.cl.service.GetInfo;
import org.cl.service.MyRejectHandler;
import org.cl.service.RWUid;
import org.cl.service.SaveInfo;
/**
 * 获取用户的文字特征
 * @author Administrator
 * params :文件名
 */
public class Main_GetTextualFeatures {
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Config.corePoolSize,Config.maximumPoolSize,Config.keepAliveTime,
			Config.unit,new LinkedBlockingQueue<Runnable>(),new MyRejectHandler());
	
	private static void initEnvironment() {
		SaveInfo.mkdir("Feature_Textual");
		SaveInfo.mkdir("Feature_Textual\\Text");
		SaveInfo.mkdir("Feature_Textual\\POS");
	}
	public static void idFilter(RWUid y_ids) throws IOException{
		GetInfo.idfilter_dirId(y_ids, "Feature_Textual\\Text");
		GetInfo.idfilter_dirId(y_ids, "Feature_Textual\\POS");
	}
	public static void main(String args[]) throws IOException, InterruptedException
	{	
		initEnvironment();
		RWUid y_ids = GetInfo.getUIDinDir("WeibosCon");
		idFilter(y_ids);
		Map<String,Integer> word_dict = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Text.txt",word_dict);
		Map<String,Integer> word_type_dict = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_POS.txt",word_type_dict);
		
		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			GetTextualFeatures getTextualFeatures=new GetTextualFeatures(uid,word_dict,word_type_dict);
			threadPool.execute(getTextualFeatures);
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
