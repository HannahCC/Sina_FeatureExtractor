package org.cl.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.cl.run.GetStyleFeatures;
import org.cl.service.GetInfo;
public class testSF {

	public static void main(String args[]) throws IOException{
		String uid = "1781626377";
		Map<String,Integer> emotion_list = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Emotion.txt",emotion_list);
		Map<String,Integer> emotion_list2 = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Emotion2.txt",emotion_list2);
		Map<String,Integer> buzz_list = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Buzz.txt",buzz_list);
		Map<String,Integer> modal_list = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Modal.txt",modal_list);
		Map<String,Integer> punt_dict = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Punt.txt",punt_dict);
		Map<String,Integer> acronym_dict = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Acronym.txt",acronym_dict);
		GetStyleFeatures getStyleFeatures = new GetStyleFeatures(uid,emotion_list,emotion_list2,buzz_list,modal_list,punt_dict,acronym_dict);
		getStyleFeatures.run();
	}

}
