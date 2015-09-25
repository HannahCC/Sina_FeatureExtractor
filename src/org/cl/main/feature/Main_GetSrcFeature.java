package org.cl.main.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.cl.configuration.Config;
import org.cl.run.GetSrcFeature;
import org.cl.service.GetInfo;
import org.cl.service.MyRejectHandler;
import org.cl.service.RWUid;
import org.cl.service.SaveInfo;
import org.cl.service.Utils;

public class Main_GetSrcFeature {
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Config.corePoolSize,Config.maximumPoolSize,Config.keepAliveTime,
			Config.unit,new LinkedBlockingQueue<Runnable>(),new MyRejectHandler());
	
	public static void main(String args[]) throws InterruptedException, IOException
	{	
		SaveInfo.mkdir("\\Feature_SRC\\MobileType");
		SaveInfo.mkdir("\\Feature_SRC\\AppType");
		
		Map<String, Set<String>> app_type_keywords = new HashMap<String, Set<String>>();
		Map<String, Set<String>> mobile_type_keywords = new HashMap<String, Set<String>>();
		GetInfo.getSetMap(app_type_keywords,Config.SAVE_PATH+"\\Config\\Dict_AppType.txt","\t","##",1,0);
		GetInfo.getSetMap(mobile_type_keywords,Config.SAVE_PATH+"\\Config\\Dict_MobileType.txt","\t","##",1,0);
		Set<String> mobile_dict = new HashSet<String>();
		GetInfo.getSet("Config\\manual\\Dict_Mobile.txt", mobile_dict,"\t",0);
		
		RWUid y_ids = GetInfo.getUID("ExpandID0.txt");
		

		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			GetSrcFeature getSrcFeature = new GetSrcFeature(uid,app_type_keywords,mobile_type_keywords,mobile_dict);
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
		
		SortUndefinedSrc("App");
		SortUndefinedSrc("Mobile");
	}

	private static void SortUndefinedSrc(String src_type) throws IOException {
		Map<String,Integer> src_type_map = new HashMap<String,Integer>();
		GetInfo.getMap("\\Feature_SRC\\"+src_type+"_undefined.txt", src_type_map, "\t",0, 1,false);
		List<Entry<String, Integer>> src_type_list = Utils.orderMapByValue(src_type_map);
		SaveInfo.saveEntryList("\\Feature_SRC\\"+src_type+"_undefined.txt", src_type_list, "\t", false);
	}

}