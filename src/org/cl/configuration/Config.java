package org.cl.configuration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Config
{

	
	/** 所有信息保存地址*/
	public static String SAVE_PATH="D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\"; 
	//public static String SAVE_PATH="D:\\Project_DataMinning\\Data\\Sina_res\\Sina_GenderPre\\"; 

	/** 程序最大连续请求次数（建议10000-20000）*/
	public static int REQUEST_MAX=10000;

	/** 达到最大的请求次数或请求被拒绝时程序的休眠时间,单位为毫秒（建议3600000-7200000）*/
	public static long SLEEP_TIME=4800000;

	/** 每爬取一个ID的信息后，线程休眠时间，以此降低爬取速度*/
	public static long UNIT_SLEEP_TIME=8000;

	/** 请求重试次数*/
	public static int RETRY = 3;
	
	/** 多线程设置 */
	public static int corePoolSize = 8;
	public static int maximumPoolSize = 8;
	public static int keepAliveTime = 10;
	public static TimeUnit unit = TimeUnit.MINUTES;
	

	public static void initial(String Path)
	{
		SAVE_PATH = Path;
		//获取配置
		File f = new File(SAVE_PATH+"Config\\Config.txt");
		Map<String,String> confmap = new HashMap<String,String>();
		try {
			BufferedReader r = new BufferedReader(new FileReader(f));
			String conf = "";
			while((conf = r.readLine())!= null){
				if("".equals(conf))continue;
				String conf_name = conf.split("\\s")[0];
				String conf_value =  conf.split("\\s")[1];
				confmap.put(conf_name, conf_value);
			}
			if(confmap.containsKey("REQUEST_MAX")){REQUEST_MAX = Integer.parseInt(confmap.get("REQUEST_MAX"));}
			if(confmap.containsKey("SLEEP_TIME")){SLEEP_TIME = Integer.parseInt(confmap.get("SLEEP_TIME"));}
			if(confmap.containsKey("UNIT_SLEEP_TIME")){UNIT_SLEEP_TIME = Integer.parseInt(confmap.get("UNIT_SLEEP_TIME"));}
			if(confmap.containsKey("RETRY")){RETRY = Integer.parseInt(confmap.get("RETRY"));}
			if(confmap.containsKey("corePoolSize")){corePoolSize = Integer.parseInt(confmap.get("corePoolSize"));}
			if(confmap.containsKey("maximumPoolSize")){maximumPoolSize = Integer.parseInt(confmap.get("maximumPoolSize"));}
			if(confmap.containsKey("keepAliveTime")){keepAliveTime = Integer.parseInt(confmap.get("keepAliveTime"));}
			r.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
