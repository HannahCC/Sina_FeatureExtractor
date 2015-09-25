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
public class GetTextualFeatures  implements Runnable
{
	private String uid=null;
	private Map<String,Integer> Text_dict = null;
	private Map<String,Integer> POS_dict = null;
	public GetTextualFeatures(String uid, Map<String, Integer> text_dict,Map<String, Integer> POS_dict)
	{
		this.uid=uid;
		this.Text_dict = text_dict;
		this.POS_dict = POS_dict;
	}
	public void run()
	{
		System.out.println("Getting textual features of "+uid);
		List<Map<Integer, Integer>> text_list = new ArrayList<Map<Integer, Integer>>();
		List<Map<Integer, Integer>> POS_list = new ArrayList<Map<Integer, Integer>>();
		try {
			File weibo_file = new File(Config.SAVE_PATH+"/Weibos_Participle/"+uid+".txt");
			BufferedReader br = new BufferedReader(new FileReader(weibo_file));
			String weibocon ="";
			boolean isHasWord = false,isHasWordType = false;
			while((weibocon=br.readLine())!=null)
			{
				if(weibocon.equals(""))continue;
				Map<Integer, Integer> text_map = new TreeMap<Integer, Integer>();
				Map<Integer, Integer> POS_map = new TreeMap<Integer, Integer>();
				get_feature_map(weibocon, text_map,POS_map,Text_dict,POS_dict);//得到按特征序号从小到大排序的特征：特征出现次数的map
				text_list.add(text_map);if(!isHasWord&&text_map.size()>0){isHasWord=true;}
				POS_list.add(POS_map);if(!isHasWordType&&POS_map.size()>0){isHasWordType=true;}
			}
			br.close();
			if(isHasWord){
				SaveInfo.saveMapList("Feature_Textual\\Text\\"+uid+".txt",text_list,":",false);
			}else {
				System.out.println(uid+"has no weibo text");
			}
			if(isHasWordType){
				SaveInfo.saveMapList("Feature_Textual\\POS\\"+uid+".txt",POS_list,":",false);
			}else {
				System.out.println(uid+"has no weibo POS TAG");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 遍历context
	 * 将文本包含的词加入text_map，key为词，value为包含次数
	 * 将文本包含的词的类别加入POS_map，key为词的类别，value为包含次数
	 * context:随着/o 乔布斯/nrf 的/u 远/a 去/vd 和/cc iOS/n 的/u 大/a 变样/vg ，/wb 现在/s 热衷/uzhi 讨论/vl 体验/vl 和/cc 设计/vl 的/u 声音/n 小/a 了/x 。/wf 关注/uzhi 的/u 焦点/n 开始/uzhi 又/d 回到/uzhi 项目/n 的/u 策划/vl 、/wm 布局/n 和/cc 前景/n 上/vd 了/x 。/wf 懂/uzhi 策划/uzhi 的/u 人/n 总/d 觉得/uzhi 设计/uzhi 是/vn 影响/uzhi 质量/n 的/u 短/a 板/n ；/wd 懂/uzhi 设计/vl 的/u 人/n 总/d 觉得/uzhi 技术/n 细节/n 是/vn 短/a 板/n ；/wd 懂/uzhi 技术/n 的/u 人/n 总/d 觉得/uzhi 我/Rg 花/uzhi 这么/ryv 大/a 代价/n 开发/vl 和/cc 完善/uzhi 的/u 细节/n ，/wb 你/Rg 到底/d 能/uzhi 不能/uzhi 给/o 我/Rg 赚/uzhi 到/uzhi 钱/n ？/wt 
	 * @param context
	 * @return
	 */
	private void get_feature_map(String context,
			Map<Integer, Integer> text_map,Map<Integer, Integer> POS_map,
			Map<String, Integer> text_dict,Map<String, Integer> POS_dict) {
		String[] terms = context.split("\\s+");
		for(String term : terms){
			String[] POS_list = term.split("/");
			if(POS_list.length<2){continue;}
			String POS = POS_list[POS_list.length-1];
			String[] text_list = term.split("/[0-9a-z]{1,5}");
			if(text_list.length<1){continue;}
			String text_text = text_list[0];
			if(POS_dict.containsKey(POS)){//当这个词只出现在少于threshold个用户微博中时，词典中不会出现该词
				int POS_index = POS_dict.get(POS);
				Utils.putInMap(POS_map,POS_index,1);
			}
			if(text_dict.containsKey(text_text)){
				int text_text_index = text_dict.get(text_text);
				Utils.putInMap(text_map,text_text_index,1);
			}
		}
	}

}
