package org.cl.main.feature;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.cl.configuration.Config;
import org.cl.run.GetBehaviorFeatures2;
import org.cl.service.GetInfo;
import org.cl.service.MyRejectHandler;
import org.cl.service.RWUid;
import org.cl.service.SaveInfo;
/**
 * 获取用户的行为特征（对于每个用户）
 
 * 获取用户的行为特征
 *结果示例：
		2263761851	1:34	2:3	3:1	4:1	5:1.1333333	6:6	7:1	8:1	9:1.8888888	10:238	11:0	12:33	13:48.52941	14:4	15:10
		-内容说明：
		1. 用户发布的微博数
		2. 一天中发布微博数最大值
		3. 一天中发布微博数最小值（不计算没发微博的日子）
		4. 一天中发布微博数中值
		5. 一天中发布微博数平均数（分母为发了微博的天数）
		6. 一周中发布微博数最大值（这里的一周是以用户第一条微博发布的日期作为星期的开始，例如用户发布了三条微博，分别是星期五、第二个星期的星期二，以及第二个星期的星期六，那么前两条算在同一个星期发布，而第三条与前两条是不同一个星期）
		7. 一周中发布微博数最小值（不计算没发微博的星期）
		8. 一周中发布微博数中值
		9. 一周中发布微博数平均数（分母为发了微博的周数）
		10.微博长度最大值
		11.微博长度最小值
		12.微博长度中值	
		13.微博长度平均值
		14.微博来源数
		15.微博来源改变次数（如来源分别是 iphone,sumsung,iphone，则改变次数为2）
 * @author Chenli
 * params :文件名
 */
public class Main_GetBehaviorFeatures2 {
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Config.corePoolSize,Config.maximumPoolSize,Config.keepAliveTime,
			Config.unit,new LinkedBlockingQueue<Runnable>(),new MyRejectHandler());
	
	public static void main(String args[]) throws IOException, InterruptedException
	{	
		SaveInfo.mkdir("\\Feature_Behaviour");
		RWUid y_ids = GetInfo.getUIDinDir("Weibos");
		
		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			GetBehaviorFeatures2 getBehaviorFeatures2=new GetBehaviorFeatures2(uid);
			threadPool.execute(getBehaviorFeatures2);
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
