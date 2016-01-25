package org.cl.run;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.cl.service.SaveInfo;
import org.cl.service.Utils;


public class GetFriFolFeature implements Runnable
{
	/**用户ID*/
	private String uid;
	private Set<String> fri_set;//存放用户所有关注的人
	private Map<String, Integer> src_dict;
	private String result_file;
	public GetFriFolFeature(String uid,Map<String, Integer> src_dict,Set<String> fri_set,String result_file)
	{
		this.uid=uid;
		this.src_dict = src_dict;
		this.fri_set = fri_set;
		this.result_file = result_file;
	}

	public void run()
	{
		Map<Integer,Float> fri_feature_type = new TreeMap<Integer,Float>();
		int fri_size = 0;
		for(String fri_uid : fri_set){
			if(src_dict.containsKey(fri_uid)){//dict可能不包含该fri  （dict是V用户组成的）
				int fri_id = src_dict.get(fri_uid);
				fri_size+=1;
				Utils.putInMap(fri_feature_type, fri_id, 1);
			}
		}
		if(fri_size==0){
			System.out.println(uid+"has no fri of this type!");
			return;
		}
		//将频次改为  （频率*1000）
		/*for(Entry<Integer, Float> src_entry : src_feature_type.entrySet()){
			float src_fre = src_entry.getValue()/(float)src_size*1000;
			src_feature_type.put(src_entry.getKey(), src_fre);
		}*/
		//VGroup.put(0, V_FRI_NUM);
		SaveInfo.saveMap(result_file,uid,fri_feature_type,true);
	}


}
