package org.cl.run;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cl.service.GetInfo;
import org.cl.service.SaveInfo;


public class GetFriTypeFeature implements Runnable
{
	/**用户ID*/
	private String uid=null;
	private Map<String, double[]> user_type_info = null;
	private String result = null;
	private int rel_type = 0;

	public GetFriTypeFeature(String uid,Map<String, double[]> user_type_info,String result,int rel_type)
	{
		this.uid=uid;
		this.user_type_info = user_type_info;
		this.result = result;
		this.rel_type = rel_type;
	}

	public void run()
	{
		//System.out.println("Getting UserType Feature of "+uid);
		Set<String> fri_id_set = new HashSet<String>();
		try {
			if((rel_type&1)>0)GetInfo.getRelUidSet(uid,fri_id_set,"UidInfo_friends0.txt");
			if((rel_type&2)>0)GetInfo.getRelUidSet(uid,fri_id_set,"UidInfo_follows0.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(fri_id_set.size()==0)return;

		double[] user_type = null;
		int count = 0;
		for(String fri_id : fri_id_set){
			if(user_type_info.containsKey(fri_id)){
				double[] fri_type = user_type_info.get(fri_id);
				if(user_type==null){
					user_type = Arrays.copyOf(fri_type, fri_type.length);//topic_possibility;
				}else{
					for(int i=0;i<fri_type.length;i++){
						user_type[i] = (user_type[i]*count+fri_type[i])/(count+1);
					}
				}
				count++;
			}
		}
		//VGroup.put(0, V_FRI_NUM);
		//SaveInfo.saveMap(result,uid,user_type,true);
		
		if(user_type==null){
			System.out.println(uid+"has no V fri !");
			return;
		}
		String user_type_feature = arraysToString(user_type);
		//Arrays.toString(topic_possibility_avg);
		try {
			SaveInfo.saveString(result,uid+"\t"+user_type_feature,true);
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
