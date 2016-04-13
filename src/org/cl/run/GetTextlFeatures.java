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
public class GetTextlFeatures  implements Runnable
{
	private String uid=null;
	private Map<String,Integer> Text_dict = null;
	public GetTextlFeatures(String uid, Map<String, Integer> text_dict)
	{
		this.uid=uid;
		this.Text_dict = text_dict;
	}
	public void run()
	{
		System.out.println("Getting textual features of "+uid);
		List<Map<Integer, Integer>> text_list = new ArrayList<Map<Integer, Integer>>();
		try {
			File weibo_file = new File(Config.SAVE_PATH+"/Weibos_Text/"+uid+".txt");
			BufferedReader br = new BufferedReader(new FileReader(weibo_file));
			String weibocon ="";
			boolean isHasWord = false;
			while((weibocon=br.readLine())!=null)
			{
				if(weibocon.equals(""))continue;
				Map<Integer, Integer> text_map = new TreeMap<Integer, Integer>();
				get_feature_map(weibocon, text_map,Text_dict);//得到按特征序号从小到大排序的特征：特征出现次数的map
				text_list.add(text_map);if(!isHasWord&&text_map.size()>0){isHasWord=true;}
			}
			br.close();
			if(isHasWord){
				SaveInfo.saveMapList("Feature_Textual\\Text\\"+uid+".txt",text_list,":",false);
			}else {
				System.out.println(uid+"has no weibo text");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 遍历context
	 * 将文本包含的词加入text_map，key为词，value为包含次数
	 * @param context
	 * @return
	 */
	private void get_feature_map(String context,
			Map<Integer, Integer> text_map,
			Map<String, Integer> text_dict) {
		String[] terms = context.split("\\s+");
		for(String term : terms){
			if(text_dict.containsKey(term)){
				int text_text_index = text_dict.get(term);
				Utils.putInMap(text_map,text_text_index,1);
			}
		}
	}

}
