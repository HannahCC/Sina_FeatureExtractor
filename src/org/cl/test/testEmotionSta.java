package org.cl.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.cl.run.GetEmoticonStastic;
import org.cl.service.GetInfo;
import org.cl.service.Utils;

public class testEmotionSta {

	public static void main(String[] args) throws IOException{
		Map<String,Integer> emotion_list = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Emotion1.txt",emotion_list);
		int middle = emotion_list.size();
		Map<String,Integer> emotion_list2 = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Emotion2.txt",emotion_list2);
		Utils.mergeDict(emotion_list,emotion_list2);
		GetEmoticonStastic getEmoticonStastic=new GetEmoticonStastic("1874991105",emotion_list,middle);
		getEmoticonStastic.run();
	}
}
