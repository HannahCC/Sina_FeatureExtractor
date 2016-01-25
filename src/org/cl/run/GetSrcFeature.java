package org.cl.run;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.cl.service.SaveInfo;
import org.cl.service.Utils;


public class GetSrcFeature implements Runnable
{
	/**用户ID*/
	private String uid;
	private Map<String,Integer> src_map;//存放用户使用的src,并记录使用次数，如：QQ音乐,4 ——————使用QQ音乐发微博4次
	private Map<String, Integer> src_dict;
	private String result_file;
	public GetSrcFeature(String uid,Map<String, Integer> src_dict,Map<String,Integer> src_map,String result_file)
	{
		this.uid=uid;
		this.src_dict = src_dict;
		this.src_map = src_map;
		this.result_file = result_file;
	}

	public void run()
	{
		Map<Integer,Float> src_feature_type = new TreeMap<Integer,Float>();
		int src_size = 0;
		for(Entry<String, Integer> src_entry : src_map.entrySet()){
			String src_name = src_entry.getKey();
			if(src_dict.containsKey(src_name)){//如果只使用app或者mobile时，dict可能不包含该src
				int src_id = src_dict.get(src_name);
				int src_count = src_entry.getValue();
				src_size+=src_count;
				Utils.putInMap(src_feature_type, src_id, /*src_count*/1);
			}
		}
		if(src_size==0){
			System.out.println(uid+"has no src of this type!");
			return;
		}
		//将频次改为  （频率*1000）
		/*for(Entry<Integer, Float> src_entry : src_feature_type.entrySet()){
			float src_fre = src_entry.getValue()/(float)src_size*1000;
			src_feature_type.put(src_entry.getKey(), src_fre);
		}*/
		//VGroup.put(0, V_FRI_NUM);
		SaveInfo.saveMap(result_file,uid,src_feature_type,true);
	}


}
