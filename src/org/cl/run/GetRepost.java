package org.cl.run;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.cl.configuration.Config;
import org.cl.service.GetInfo;
import org.cl.service.SaveInfo;
import org.cl.service.Utils;

/**
 * 待完善
 * 目前获取了对应用户 <转发了的用户id:转发该用户次数>的map,并按转发次数降序排列，存放在Feature_RePost
 * @author Hannah
 *
 */
public class GetRepost implements Runnable {
	/**用户ID*/
	private String uid=null;
	Map<String,Integer> user = new HashMap<String,Integer>();
	public GetRepost(String uid)
	{
		this.uid=uid;
	}

	public void run() {
		System.out.println("Getting Repost Feature of "+uid);
		File f = new File(Config.SAVE_PATH+"\\Weibos\\"+uid+".txt");
		List<String> weibo_list = new ArrayList<String>();
		try {
			GetInfo.getList(f,weibo_list,false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for(String weibo : weibo_list){
			JSONObject weibo_json = JSONObject.fromObject(weibo);
			JSONObject retweet = weibo_json.getJSONObject("weibo").getJSONObject("retweetedStatus");
			if(retweet==null||retweet.equals(""))continue;
			String id = retweet.getString("userId");
			if(id.equals("null"))continue;//被转发的微博已经被删除
			if(user.containsKey(id)){user.put(id, user.get(id)+1);}
			else{user.put(id, 1);}
		}
		List<Entry<String,Integer>> list = Utils.orderMapByValue(user);//按转发次数从大到小排序
		try {
			SaveInfo.saveEntryList("\\Feature_RePost\\"+uid+".txt", list,":",false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
