package org.cl.run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.Map;

import net.sf.json.JSONObject;

import org.cl.configuration.Config;
/**
 * 		
 * （微博数，粉丝数，发文数）；
 * （发文率，转发率，回复率）
 * @author Chenli
 *
 */
public class GetBehaviorFeatures3  implements Runnable
{
	/**用户ID*/
	private String uid=null;
	private Map<String, Integer> followers_map = null;
	int weibo_num = 0,original_num = 0,reply_num = 0,follower_num = 0;

	public GetBehaviorFeatures3(String uid, Map<String, Integer> followers_map)
	{
		this.uid = uid;
		this.followers_map = followers_map;
	}
	public void run()
	{
		System.out.println("Getting BehaviorFeatures3 of "+uid);
		if(followers_map.containsKey(uid)){
			follower_num = followers_map.get(uid);
		}
		String weibo = "";
		File file_or = new File(Config.SAVE_PATH+"Weibos\\"+uid+".txt");
		try {
			BufferedReader br = new BufferedReader(new FileReader(file_or));
			while((weibo=br.readLine())!=null)
			{
				if(weibo.equals(""))continue;
				JSONObject weibo_json = JSONObject.fromObject(weibo).getJSONObject("weibo");
				weibo_num++;
				String retweetedStatus = weibo_json.getString("retweetedStatus");
				if(retweetedStatus.equals("null")){original_num++;}//获取是否原创特征
				if(weibo_json.getInt("commentsCount")>0){reply_num++;}//获取回复数特征
			}
			br.close();
			saveResult();
		} catch (Exception e) {
			System.out.println(uid);
			System.out.println(weibo);
			e.printStackTrace();
		}
	}

	private void saveResult() throws IOException {
		/*
		 * bf31_stastic.txt（微博数，粉丝数，发文数）；
		 * bf32_stastic.txt（发文率，转发率，回复率）
		 */
		File file = new File(Config.SAVE_PATH+"\\Feature_Behaviour\\Behaviour31_feature.txt");
		OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(file,true));
		w.write(uid+"\t0:"+weibo_num+"\t1:"+follower_num+"\t2:"+original_num+"\r\n");
		w.flush();w.close();

		double original_ratio = original_num/(double)weibo_num;
		double repost_ratio = 1 - original_ratio;
		double reply_ratio = reply_num/(double)weibo_num;
		DecimalFormat df = new DecimalFormat("0.0000");
		File file2 = new File(Config.SAVE_PATH+"\\Feature_Behaviour\\Behaviour32_feature.txt");
		OutputStreamWriter w2 = new OutputStreamWriter(new FileOutputStream(file2,true));
		w2.write(uid+"\t0:"+df.format(original_ratio)+"\t1:"+df.format(repost_ratio)+"\t2:"+df.format(reply_ratio)+"\r\n");
		w2.flush();w2.close();
	}
}
