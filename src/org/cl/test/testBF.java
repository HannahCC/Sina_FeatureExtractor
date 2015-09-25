package org.cl.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.cl.run.GetBehaviorFeatures1;
import org.cl.service.GetInfo;

public class testBF {

	public static void main(String args[]) throws IOException{
		String uid = "1299453733";
		Map<String, Integer> src_map = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Src.txt",src_map);
		GetBehaviorFeatures1 getBehaviorFeatures=new GetBehaviorFeatures1(uid,src_map);
		getBehaviorFeatures.run();
	}

}
