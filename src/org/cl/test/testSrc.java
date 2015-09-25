package org.cl.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cl.configuration.Config;
import org.cl.run.GetSrcFeature;
import org.cl.service.GetInfo;

public class testSrc {
	public static void main(String args[]) throws IOException{
		String uid = "2263761851";
		Map<String, Set<String>> app_type_keywords = new HashMap<String, Set<String>>();
		Map<String, Set<String>> mobile_type_keywords = new HashMap<String, Set<String>>();
		GetInfo.getSetMap(app_type_keywords,Config.SAVE_PATH+"\\Config\\Dict_AppType.txt","\t","##",1,0);
		GetInfo.getSetMap(mobile_type_keywords,Config.SAVE_PATH+"\\Config\\Dict_MobileType.txt","\t","##",1,0);
		Set<String> mobile_dict = new HashSet<String>();
		GetInfo.getSet("Config\\manual\\Dict_Mobile.txt", mobile_dict,"\t",0);
		GetSrcFeature src_feature=new GetSrcFeature(uid,app_type_keywords,mobile_type_keywords,mobile_dict);
		src_feature.run();
	}
}
