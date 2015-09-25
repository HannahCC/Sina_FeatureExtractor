package org.cl.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.cl.run.GetBehaviorFeatures3;
import org.cl.service.GetInfo;

public class testBF3 {

	public static void main(String args[]) throws IOException{
		String uid = "80983960";
		Map<String,Integer> followers_map = new HashMap<String,Integer>();
		GetInfo.getMap("UserInfo0.txt",followers_map,"id","followersCount");
		GetBehaviorFeatures3 getBehaviorFeatures = new GetBehaviorFeatures3(uid,followers_map);
		getBehaviorFeatures.run();
	}
}
