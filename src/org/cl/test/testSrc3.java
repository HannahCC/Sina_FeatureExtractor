package org.cl.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.cl.run.GetSrcTopicFeature;
import org.cl.service.GetInfo;

public class testSrc3 {
	public static void main(String args[]) throws IOException{
		String uid = "2263761851";
		Map<String, float[]> src_topic = new HashMap<String,float[]>();//获得每个src属于各个topic的可能性
		GetInfo.getArrayMap(src_topic,60,"Feature_SRC\\Src_topic.txt","\t"," ",0,1);//==============================
		String result_filename = "Feature_SRC\\SrcTopic_feature.txt";
		GetSrcTopicFeature getSrcFeature = new GetSrcTopicFeature(uid,src_topic,result_filename);
		getSrcFeature.run();
		
	}
}
