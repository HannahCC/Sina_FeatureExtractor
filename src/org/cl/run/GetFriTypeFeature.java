package org.cl.run;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.cl.service.GetInfo;
import org.cl.service.SaveInfo;
import org.cl.service.Utils;


public class GetFriTypeFeature implements Runnable
{
	/**用户ID*/
	private String uid=null;
	private Map<String, Set<String>> user_type_info = null;
	private String result = null;
	private int rel_type = 0;

	public GetFriTypeFeature(String uid,Map<String, Set<String>> user_type_info,String result,int rel_type)
	{
		this.uid=uid;
		this.user_type_info = user_type_info;
		this.result = result;
		this.rel_type = rel_type;
	}

	public void run()
	{
		System.out.println("Getting UserType Feature of "+uid);
		Set<String> fri_id_set = new HashSet<String>();
		try {
			if((rel_type&1)>0)GetInfo.getRelUidSet(uid,fri_id_set,"UidInfo_friends0.txt");
			if((rel_type&2)>0)GetInfo.getRelUidSet(uid,fri_id_set,"UidInfo_follows0.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(fri_id_set.size()==0)return;
		
		//int V_FRI_NUM = 0;
		Map<Integer,Integer> user_type = new TreeMap<Integer,Integer>();
		for(String fri_id : fri_id_set){
			if(user_type_info.containsKey(fri_id)){
				//V_FRI_NUM++;
				Set<String>  fri_type = user_type_info.get(fri_id);
				for(String type_str : fri_type){
					int type = Integer.parseInt(type_str);
					Utils.putInMap(user_type, type, 1);
				}
			}
		}
		//VGroup.put(0, V_FRI_NUM);
		SaveInfo.saveMap(result,uid,user_type,true);
		
	}
}
