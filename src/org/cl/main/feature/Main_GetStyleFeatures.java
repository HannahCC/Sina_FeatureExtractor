package org.cl.main.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.cl.configuration.Config;
import org.cl.run.GetStyleFeatures;
import org.cl.service.GetInfo;
import org.cl.service.MyRejectHandler;
import org.cl.service.RWUid;
import org.cl.service.SaveInfo;
/**
 * 获取用户的风格特征
 * 表情符号、
 * 流行用语、
 * 缩写词、
 * 标点符号、
 * 语气词、
 * @author Administrator
 * params :文件名
 */
public class Main_GetStyleFeatures {
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Config.corePoolSize,Config.maximumPoolSize,Config.keepAliveTime,
			Config.unit,new LinkedBlockingQueue<Runnable>(),new MyRejectHandler());
	
	private static void initEnvironment() {
		SaveInfo.mkdir("Feature_Style");
		SaveInfo.mkdir("Feature_Style\\Acronym");
		SaveInfo.mkdir("Feature_Style\\Buzz");
		SaveInfo.mkdir("Feature_Style\\Emotion");
		SaveInfo.mkdir("Feature_Style\\Emotion2");
		SaveInfo.mkdir("Feature_Style\\Modal");
		SaveInfo.mkdir("Feature_Style\\Punt");
	}
	public static void idFilter(RWUid y_ids) throws IOException{
		GetInfo.idfilter_dirId(y_ids, "Feature_Style\\Modal");
	}
	public static void main(String args[]) throws IOException, InterruptedException
	{	
		initEnvironment();
		RWUid y_ids = GetInfo.getUIDinDir("WeibosCon");
		idFilter(y_ids);
		//"Config\\Dict_Emotion.txt"中包含所有表情符号。所以Emotion包含了所有的表情特征，而Emotion2只是方便查看，并不被后续程序使用
		Map<String,Integer> emotion_list = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Emotion.txt",emotion_list);
		Map<String,Integer> emotion_list2 = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Emotion2.txt",emotion_list2);
		//Merge.mergeDict(emotion_list, emotion_list2);
		Map<String,Integer> buzz_list = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Buzz.txt",buzz_list);
		Map<String,Integer> modal_list = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Modal.txt",modal_list);
		Map<String,Integer> punt_dict = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Punt.txt",punt_dict);
		Map<String,Integer> acronym_dict = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Acronym.txt",acronym_dict);
		
		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			GetStyleFeatures getStyleFeatures=new GetStyleFeatures(uid,emotion_list,emotion_list2,buzz_list,modal_list,punt_dict,acronym_dict);
			threadPool.execute(getStyleFeatures);
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
