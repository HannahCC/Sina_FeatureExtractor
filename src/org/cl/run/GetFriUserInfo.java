package org.cl.run;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.cl.service.SaveInfo;
import org.cl.service.Utils;


public class GetFriUserInfo implements Runnable
{
	/**用户ID*/
	private String uid=null;
	private Map<String, JSONObject> fri_info_map = null;
	private Map<String, Set<String>> fri_id_map = null;

	public GetFriUserInfo(String uid,
			Map<String, JSONObject> fri_info_map,
			Map<String, Set<String>> fri_id_map) {
		super();
		this.uid = uid;
		this.fri_info_map = fri_info_map;
		this.fri_id_map = fri_id_map;
	}

	public void run()
	{
		//System.out.println("Getting Fri UserInfo of "+uid);
		if(!fri_id_map.containsKey(uid)){
			System.out.println("**********************Fri_id_map has no "+uid);
			return;
		}
		Set<String> fri_id_set = fri_id_map.get(uid);
		if(fri_id_set.size()==0){
			System.out.println("======================User has no Fri==="+uid);
			return;
		}
		
		List<String> fri_info_list = new ArrayList<String>();
		for(String fri_id : fri_id_set){
			if(fri_info_map.containsKey(fri_id)){
				JSONObject fri_info = fri_info_map.get(fri_id);
//用户id   微博数  关注数  粉丝数  完整的用户名(用##开头和结尾，包含呢称、性别和地址以/分隔， 如#和珅/男/河北承德#)   用户类别  认证原因(用##开头和结尾，如果没有认证就是##）  用户描述(用##开头和结尾，如果没有描述就是##）  用户tag标签(用##开头和结尾，tag之间用空格分隔）。
				StringBuffer fri_info_buffer = new StringBuffer();
				fri_info_buffer.append(fri_info.getString("id")+"\t");
				fri_info_buffer.append(fri_info.getString("statusesCount")+"\t");
				fri_info_buffer.append(fri_info.getString("friendsCount")+"\t");
				fri_info_buffer.append(fri_info.getString("followersCount")+"\t");
				fri_info_buffer.append("#"+fri_info.getString("screenName")+"/");
				String gender = fri_info.getString("gender");
				if("m".equals(gender)){gender = "男";}
				else if("f".equals(gender)){gender = "女";}
				fri_info_buffer.append(gender+"/");
				fri_info_buffer.append(fri_info.getString("location")+"#\t");
				fri_info_buffer.append(fri_info.getString("verifiedType")+"\t");
				fri_info_buffer.append("#"+fri_info.getString("verifiedReason")+"#\t");
				fri_info_buffer.append("#"+fri_info.getString("description")+"#\t");
				@SuppressWarnings("unchecked")
				List<String> tags = (List<String>) fri_info.getJSONObject("tags").get("tags");
				fri_info_buffer.append("#"+Utils.listToString(tags," ")+"#");
				fri_info_list.add(fri_info_buffer.toString());
			}else{
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~Fri_info_map has no userinfo of "+fri_id);
			}
		}
		try {
			SaveInfo.saveList("ToQian\\FollowsInfo\\"+uid+".txt", fri_info_list, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
