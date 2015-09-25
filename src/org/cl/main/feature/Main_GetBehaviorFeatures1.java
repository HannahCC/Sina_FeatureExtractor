package org.cl.main.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.cl.configuration.Config;
import org.cl.run.GetBehaviorFeatures1;
import org.cl.service.GetInfo;
import org.cl.service.MyRejectHandler;
import org.cl.service.RWUid;
import org.cl.service.SaveInfo;
/**
 * 获取用户的行为特征
 * 		1. 微博发布的时间，以小时为单位。所有的微博发布时间（小时分布）构成一个向量，例如假设用户共发表3条微博，时间分别为8:28, 10:20, 22:05， 则构成一个3维的向量  8   10   22；
		2. 微博发布的时间，以周为单位。所有的微博发布时间（周分布）构成一个向量，这个可能需要调用一个日历函数，例如假设用户共发表3条微博，时间分别为周1， 周5， 周7， 则构成一个3维的向量  1   5   7；
		3. 微博发布的地点经度
		4. 微博发布的地点纬度
		5. 微博发布时所用的设备， 所有微博使用的设备存入一个向量
		6. 微博的回复数，所有微博中的回复数存入一个向量
		7. 微博的被转发数，所有微博的被转发数存入一个向量		
		8. 微博中包含的链接数，所有微博包含的连接数存入一个向量
		9. 微博中包含的#...#（强调主题用）数，，所有微博包含的#...#数存入一个向量
		10.微博中包含的@ 数
		11.微博是否搭配图片
		12.微博是否原创 		
		13.话题url并存数
		14.图片url并存数
 * @author Administrator
 * params :文件名
 */
public class Main_GetBehaviorFeatures1 {
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Config.corePoolSize,Config.maximumPoolSize,Config.keepAliveTime,
			Config.unit,new LinkedBlockingQueue<Runnable>(),new MyRejectHandler());
	
	private static void idFilter(RWUid y_ids){
		GetInfo.idfilter_dirId(y_ids, "//Feature_Behaviour/");
		System.out.println("y_ids.getNum()="+y_ids.getNum());
	}
	public static void main(String args[]) throws IOException, InterruptedException
	{	
		SaveInfo.mkdir("/Feature_Behaviour/");
		RWUid y_ids = GetInfo.getUIDinDir("Weibos");
		idFilter(y_ids);
		Map<String, Integer> src_map = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Src.txt",src_map);
		
		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			GetBehaviorFeatures1 getBehaviorFeatures1=new GetBehaviorFeatures1(uid,src_map);
			threadPool.execute(getBehaviorFeatures1);
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
