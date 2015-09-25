package org.cl.run;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.cl.service.SaveInfo;
import org.cl.service.Utils;


public class GetFriFeature implements Runnable
{
	/**用户ID*/
	private String uid=null;
	//private boolean isUseFollower = false;
	private String feature=null;
	private Map<String, Map<Integer,Integer>> feature_map = null;
	private Map<String, Set<String>> fri_id_map = null;
	private boolean isWithSelf = false;


	public GetFriFeature(String uid, String feature,
			Map<String, Map<Integer, Integer>> feature_map,
			Map<String, Set<String>> fri_id_map,boolean isWithSelf) {
		super();
		this.uid = uid;
		this.feature = feature;
		this.feature_map = feature_map;
		this.fri_id_map = fri_id_map;
		this.isWithSelf = isWithSelf;
	}

	public void run()
	{
		//System.out.println("Getting Fri Feature of "+uid);
		if(!fri_id_map.containsKey(uid))return;
		Set<String> fri_id_set = fri_id_map.get(uid);
		if(fri_id_set.size()==0)return;

		Map<Integer,Integer> user_fri_feature = new TreeMap<Integer,Integer>();
		//将自己的合并进来

		Map<Integer, Integer> user_feature = null;
		if(isWithSelf){
			if(feature_map.containsKey(uid)){
				user_feature = feature_map.get(uid);
				Utils.mergeFeature(user_fri_feature, user_feature);
			}
		}
		//合并朋友的
		for(String fri_id : fri_id_set){
			if(feature_map.containsKey(fri_id)){
				user_feature = feature_map.get(fri_id);
				Utils.mergeFeature(user_fri_feature, user_feature);
			}
		}
		if(user_fri_feature.size()==0){
			System.out.println(uid+"has no fri feature!");
		}
		String result_filename = "Feature_Relation\\"+feature+".txt".intern();
		SaveInfo.saveMap(result_filename,uid,user_fri_feature,true);

	}

}
