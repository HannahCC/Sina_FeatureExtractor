package org.cl.run;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.cl.service.SaveInfo;

public class GetSrcTypeFeature implements Runnable
{
	/**用户ID*/
	private String uid=null;
	private Map<String, double[]> src_type_info = null;
	private Map<String,Integer> src_map;//存放用户使用的src,并记录使用次数，如：QQ音乐,4 ——————使用QQ音乐发微博4次
	private String result_file = null;
	public GetSrcTypeFeature(String uid,Map<String, double[]> src_type_info,Map<String,Integer> src_map,String result_file)
	{
		this.uid=uid;
		this.src_type_info = src_type_info;
		this.src_map = src_map;
		this.result_file = result_file;
	}

	public void run()
	{
		int src_number_sum = 0;
		double[] user_type = null;
		for(Entry<String, Integer> src_entry : src_map.entrySet()){
			String src_name = src_entry.getKey();
			//int src_number = src_entry.getValue();
			if(src_type_info.containsKey(src_name)){
				double[] src_type = src_type_info.get(src_name);
				if(user_type==null){
					user_type = Arrays.copyOf(src_type, src_type.length);
				}else{
					for(int i=0;i<src_type.length;i++){
						user_type[i] = (user_type[i]*src_number_sum+src_type[i])/(src_number_sum+1);
					}
				}
			}
			src_number_sum ++;
		}
		
		if(user_type==null){
			System.out.println(uid+"has no src !");
			return;
		}
		
		String user_type_feature = arraysToString(user_type);
		//Arrays.toString(topic_possibility_avg);
		try {
			SaveInfo.saveString(result_file,uid+"\t"+user_type_feature,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//所有可能性统一乘以100
	private String arraysToString(double[] topic_possibility_avg) {
		StringBuffer sBuffer = new StringBuffer();
		for(int i=1;i<=topic_possibility_avg.length;i++){
			sBuffer.append(i+":"+topic_possibility_avg[i-1]+"\t");
		}
		return sBuffer.toString();
	}

}
