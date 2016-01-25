package org.cl.run;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.cl.service.SaveInfo;
import org.cl.service.Utils;


public class GetTagTypeFeature implements Runnable
{
	/**用户ID*/
	private String uid=null;
	private Map<String, Set<String>> tag_type_info = null;
	private Set<String> tag_set;//存放用户使用的tag,并记录使用次数，如：QQ音乐,4 ——————使用QQ音乐发微博4次
	private String result_file = null;
	public GetTagTypeFeature(String uid,Map<String, Set<String>> tag_type_info,Set<String> tag_set,String result_file)
	{
		this.uid=uid;
		this.tag_type_info = tag_type_info;
		this.tag_set = tag_set;
		this.result_file = result_file;
	}

	public void run()
	{
		Map<Integer,Integer> tag_type = new TreeMap<Integer,Integer>();
		for(String tag : tag_set){
			if(tag_type_info.containsKey(tag)){
				Set<String>  tag_type_set = tag_type_info.get(tag);
				for(String type_str : tag_type_set){
					int type = Integer.parseInt(type_str);
					Utils.putInMap(tag_type, type, 1);
				}
			}
		}
		//VGroup.put(0, V_FRI_NUM);
		SaveInfo.saveMap(result_file,uid,tag_type,true);
	}

}
