package org.cl.main.feature;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.cl.configuration.Config;
import org.cl.run.GetRepost;
import org.cl.service.GetInfo;
import org.cl.service.MyRejectHandler;
import org.cl.service.RWUid;
import org.cl.service.SaveInfo;
/**
 * 待完善
 * 目前获取了对应用户 <转发了的用户id:转发该用户次数>的map,并按转发次数降序排列，存放在Feature_RePost
 * @author Administrator
 * params:文件名
 */
public class Main_GetRepostFeature {
	
	public static String path = "";
	public static int deep = 1;
	
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Config.corePoolSize,Config.maximumPoolSize,Config.keepAliveTime,
			Config.unit,new LinkedBlockingQueue<Runnable>(),new MyRejectHandler());
	
	public static GetRepost user_repost=null;
	public static int run_num=12;//线程数
	/**
	 * 初始化程序环境
	 */
	public static void init(){
		SaveInfo.mkdir("Feature_RePost");
	}
	
	public static void main(String args[])
	{	
		init();
		RWUid y_ids = GetInfo.getUIDinDir("Weibos");
		idFiters(y_ids);
		
		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			GetRepost getRepost=new GetRepost(uid);
			threadPool.execute(getRepost);
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
	
	private static void idFiters(RWUid y_ids) {
		GetInfo.idfilter_dirId(y_ids, "Feature_RePost");
	}
}