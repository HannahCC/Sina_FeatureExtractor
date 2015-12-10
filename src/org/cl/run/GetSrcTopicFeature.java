package org.cl.run;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cl.service.GetInfo;
import org.cl.service.SaveInfo;
import org.cl.service.Utils;

public class GetSrcTopicFeature implements Runnable
{
	/**用户ID*/
	private String uid=null;
	private Map<String, float[]> src_topic_info = null;
	private String result_file = null;
	public GetSrcTopicFeature(String uid,Map<String, float[]> src_type_info,String result_file)
	{
		this.uid=uid;
		this.src_topic_info = src_type_info;
		this.result_file = result_file;
	}

	public void run()
	{
		Map<String,Integer> src_map = getSrcMap();//获取用户使用的src,并记录使用次数，如：QQ音乐,4 ——————使用QQ音乐发微博4次
		if(src_map.size()==0){
			System.out.println(uid+"has no src!");
			return;
		}
		
		int src_number_sum = 0;
		float[] topic_possibility_avg = null;
		for(Entry<String, Integer> src_entry : src_map.entrySet()){
			String src_name = src_entry.getKey();
			int src_number = src_entry.getValue();
			if(src_topic_info.containsKey(src_name)){
				float[] topic_possibility = src_topic_info.get(src_name);
				if(topic_possibility_avg==null){
					topic_possibility_avg = Arrays.copyOf(topic_possibility, topic_possibility.length);//topic_possibility;//记得看src_topic_info会不会被改变
				}else{
					for(int i=0;i<topic_possibility.length;i++){
						topic_possibility_avg[i] = (topic_possibility_avg[i]*src_number_sum+topic_possibility[i]*src_number)/(src_number_sum+src_number);
					}
				}
			}
			src_number_sum += src_entry.getValue();
		}
		String topic_feature = arraysToString(topic_possibility_avg);
		Arrays.toString(topic_possibility_avg);
		try {
			SaveInfo.saveString(result_file,uid+"\t"+topic_feature,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String arraysToString(float[] topic_possibility_avg) {
		StringBuffer sBuffer = new StringBuffer();
		for(int i=1;i<=topic_possibility_avg.length;i++){
			sBuffer.append(i+":"+topic_possibility_avg[i-1]+"\t");
		}
		return sBuffer.toString();
	}

	private Map<String, Integer> getSrcMap(){  
		Map<String,Integer> src_map = new HashMap<String,Integer>();
		try {
			List<String> src_list = new ArrayList<String>();
			GetInfo.getList("\\Weibos\\"+uid+".txt", src_list, false, "weibo","source");
			for(String src : src_list){
				src = Utils.clearSource(src);
				if(src.equals(""))continue;
				Utils.putInMap(src_map, src, 1);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return src_map;
	}

}
