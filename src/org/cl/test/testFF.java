package org.cl.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.cl.run.GetFriFeature;
import org.cl.service.GetInfo;

public class testFF {

	public static void main(String args[]) throws IOException{
		String uid = "1299453733";

		String INFO = "ScreenName3";
		//获取所有用户的信息
		String result_filename = "Feature_Relation\\"+INFO+"_feature.txt";
		
		Map<String, Map<Integer,Integer>> fri_info = new HashMap<String, Map<Integer,Integer>>();
		GetInfo.getFriInfo(fri_info,"UserInfoTMP\\UserInfo0.txt."+INFO,"UserInfoTMP\\UserInfo1.txt."+INFO);//\\UserInfo0.txt.screenName2");//\\UserInfo0.txt.verifiedReason2");

		Map<String, Set<String>> fri_id_map = new HashMap<String, Set<String>>();
		GetInfo.getRelUidMap(fri_id_map,"UidInfo_friends0.txt");
		
		GetFriFeature getFriFeature = new GetFriFeature(uid,result_filename,fri_info,fri_id_map,false);
		getFriFeature.run();
	}

}
