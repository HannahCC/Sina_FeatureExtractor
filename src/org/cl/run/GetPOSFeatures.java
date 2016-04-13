package org.cl.run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.cl.configuration.Config;
import org.cl.service.SaveInfo;
import org.cl.service.Utils;
/**
 * 获取用户的文本特征
 * @author lichen
 * params :文件名
 */
public class GetPOSFeatures  implements Runnable
{
	private String uid=null;
	private Map<String,Integer> POS_dict = null;
	public GetPOSFeatures(String uid, Map<String, Integer> POS_dict)
	{
		this.uid=uid;
		this.POS_dict = POS_dict;
	}
	public void run()
	{
		System.out.println("Getting pos features of "+uid);
		List<Map<Integer, Integer>> POS_list = new ArrayList<Map<Integer, Integer>>();
		try {
			File weibo_file = new File(Config.SAVE_PATH+"/Weibos_POS/"+uid+".txt");
			BufferedReader br = new BufferedReader(new FileReader(weibo_file));
			String weibocon ="";
			boolean isHasWordType = false;
			while((weibocon=br.readLine())!=null)
			{
				if(weibocon.equals(""))continue;
				Map<Integer, Integer> POS_map = new TreeMap<Integer, Integer>();
				get_feature_map(weibocon,POS_map,POS_dict);//得到按特征序号从小到大排序的特征：特征出现次数的map
				POS_list.add(POS_map);if(!isHasWordType&&POS_map.size()>0){isHasWordType=true;}
			}
			br.close();
			if(isHasWordType){
				SaveInfo.saveMapList("Feature_textual\\POS\\"+uid+".txt",POS_list,":",false);
			}else {
				System.out.println(uid+"has no weibo POS TAG");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * 将文本包含的词加入pos_map，key为词，value为包含次数
	 * @param
	 * @return
	 */
	private void get_feature_map(String conpos,
			Map<Integer, Integer> pos_map,
			Map<String, Integer> pos_dict) {
		String[] terms = conpos.split("\\s+");
		for(String term : terms){
			if(pos_dict.containsKey(term)){
				int pos_index = pos_dict.get(term);
				Utils.putInMap(pos_map,pos_index,1);
			}
		}
	}
}
