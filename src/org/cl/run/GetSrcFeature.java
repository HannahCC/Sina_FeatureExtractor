package org.cl.run;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.cl.service.GetInfo;
import org.cl.service.SaveInfo;
import org.cl.service.Utils;


public class GetSrcFeature implements Runnable
{
	/**用户ID*/
	private String uid=null;
	private Map<String, Integer> src_dict = null;
	private String result_file = null;
	public GetSrcFeature(String uid,Map<String, Integer> src_dict,String result_file)
	{
		this.uid=uid;
		this.src_dict = src_dict;
		this.result_file = result_file;
	}

	public void run()
	{

		Map<String,Integer> src_map = getSrcMap();//获取用户使用的src,并记录使用次数，如：QQ音乐,4 ——————使用QQ音乐发微博4次
		if(src_map.size()==0){
			System.out.println(uid+"has no src!");
			return;
		}
		Map<Integer,Integer> src_feature_type = new TreeMap<Integer,Integer>();
		for(Entry<String, Integer> src_entry : src_map.entrySet()){
			String src_name = src_entry.getKey();
			int src_id = src_dict.get(src_name);
			Utils.putInMap(src_feature_type, src_id, src_entry.getValue());
		}
		//VGroup.put(0, V_FRI_NUM);
		SaveInfo.saveMap(result_file,uid,src_feature_type,true);
	}

	private Map<String, Integer> getSrcMap(){  
		Map<String,Integer> src_map = new HashMap<String,Integer>();
		try {
			List<String> src_list = new ArrayList<String>();
			GetInfo.getList("\\Weibos\\"+uid+".txt", src_list, false, "weibo","source");
			for(String src : src_list){
				src = Utils.clearSource(src);
				if(src.equals(""))continue;
				Utils.putInMap(src_map, src, 1);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return src_map;
	}

}
