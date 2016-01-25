package org.cl.main.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.cl.configuration.Config;
import org.cl.run.GetSrcTypeFeature;
import org.cl.service.GetInfo;
import org.cl.service.MyRejectHandler;
import org.cl.service.RWUid;

public class Main_GetSrcTypeFeature {
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
		Map<String, Map<String,Integer>> uid_src_map = new HashMap<String, Map<String,Integer>>();
		GetInfo.getMapMap("WeibosSrc\\Src_map.txt",uid_src_map,"\t",":",0);

		/*Map<String, double[]> src_topic = new HashMap<String,double[]>();//获得每个src属于各个topic的可能性
		int topic_number = 100;//与topic type数量一致
		GetInfo.getArrayMap(src_topic,topic_number,"Feature_SRC\\Src_topic_2000_100_LDA.txt","\t"," ",0,1);
		String result_filename = "Feature_SRC\\LDASrcTopic2000_100_feature.txt";*/
		
		int src_type_size =100; // 52;//srcNEWtype  61;//srctype      41;//apptype   38;//appnewtype    
		Map<String, double[]> src_type = new HashMap<String,double[]>();
		GetInfo.getArrayMap(src_type,src_type_size,"Feature_SRC\\Src_type1_vector.txt","\t"," ",0,1);
		String result_filename = "Feature_SRC\\SrcType1Vector_feature.txt";
		
		
		RWUid y_ids = GetInfo.getUID("ExpandID0.txt");


		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			if(uid_src_map.containsKey(uid)){
				Map<String,Integer> src_map = uid_src_map.get(uid);
				GetSrcTypeFeature getSrcFeature = new GetSrcTypeFeature(uid,src_type,src_map,result_filename);
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