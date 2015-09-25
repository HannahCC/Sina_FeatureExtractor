package org.cl.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;
import org.cl.run.GetFriUserInfo;
import org.cl.service.GetInfo;
import org.cl.service.RWUid;

public class testFFU {

	public static void main(String args[]) throws IOException{
		Map<String, JSONObject> fri_info = new HashMap<String,JSONObject>();
		GetInfo.getFriUserInfo(fri_info,"UserInfo0.txt");//\\UserInfo0.txt.screenName2");//\\UserInfo0.txt.verifiedReason2");

		Map<String, Set<String>> fri_id_map = new HashMap<String, Set<String>>();
		GetInfo.getRelUidMap(fri_id_map,"UidInfo_friends0.txt");
		
		RWUid y_ids = GetInfo.getUID("ExpandID0.txt");
		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			GetFriUserInfo getFriUserInfo = new GetFriUserInfo(uid,fri_info,fri_id_map);
			getFriUserInfo.run();
		}
	}

}
