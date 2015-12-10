package org.cl.main.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.cl.configuration.Config;
import org.cl.run.GetSrcTypeFeature2;
import org.cl.service.GetInfo;
import org.cl.service.MyRejectHandler;
import org.cl.service.RWUid;

public class Main_GetSrcTypeFeature2 {
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Config.corePoolSize,Config.maximumPoolSize,Config.keepAliveTime,
			Config.unit,new LinkedBlockingQueue<Runnable>(),new MyRejectHandler());
	
	/**
	 * 将用户的src分到对应的类别上，并形成用户向量
	 * 第一步，人工划分src类别
	 * 再要获取用户src的描述（SinaApp_Crawler）得到一部分，百度搜索得到一部分
	 * 然后对src的描述进行分词
	 * 将分词结果筛选出频率大于1的词作为src的关键词（DataProcessor.GetKeywords得到Src_Keywords.txt
	 * 根据src关键词落在哪一个类别上，即属于哪一类
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String args[]) throws InterruptedException, IOException
	{	
		Map<String, Set<String>> src_type = new HashMap<String,Set<String>>();
		GetInfo.getFriTypeInfo(src_type,"Feature_SRC\\Src_type1.txt");
		String result_filename = "Feature_SRC\\SrcType1_feature.txt";
		RWUid y_ids = GetInfo.getUID("ExpandID0.txt");
		
		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			GetSrcTypeFeature2 getSrcFeature = new GetSrcTypeFeature2(uid,src_type,result_filename);
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