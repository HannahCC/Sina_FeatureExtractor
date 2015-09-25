package org.cl.run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.TreeMap;

import org.cl.configuration.Config;
import org.cl.configuration.RegexString;
import org.cl.service.SaveInfo;
import org.cl.service.Utils;
/**
 * 
 * @author lichen
 */
public class GetStyleFeatures  implements Runnable
{
	private String uid=null;
	private Map<String,Integer> emotion_dict = null;
	private Map<String,Integer> emotion_dict2 = null;
	private Map<String,Integer> buzz_dict = null;
	private Map<String,Integer> modal_dict = null;
	private Map<String,Integer> punt_dict = null;
	private Map<String,Integer> acronym_dict = null;
	public GetStyleFeatures(String uid, Map<String, Integer> emotion_list,Map<String, Integer> emotion_list2,Map<String, Integer> buzz_list, 
			Map<String, Integer> modal_list,Map<String, Integer> punt_dict,Map<String, Integer> acronym_dict)
	{
		this.uid=uid;
		this.emotion_dict = emotion_list;
		this.emotion_dict2 = emotion_list2;
		this.buzz_dict = buzz_list;
		this.modal_dict = modal_list;
		this.punt_dict = punt_dict;
		this.acronym_dict = acronym_dict;
	}
	public void run()
	{
		System.out.println("Getting style features of "+uid);
		List<Map<Integer, Integer>> emotion_list = new ArrayList<Map<Integer, Integer>>();
		List<Map<Integer, Integer>> emotion_list2 = new ArrayList<Map<Integer, Integer>>();
		List<Map<Integer, Integer>> buzz_list = new ArrayList<Map<Integer, Integer>>();
		List<Map<Integer, Integer>> acronym_list = new ArrayList<Map<Integer, Integer>>();
		List<Map<Integer, Integer>> punt_list = new ArrayList<Map<Integer, Integer>>();
		List<Map<Integer, Integer>> modal_list = new ArrayList<Map<Integer, Integer>>();
		try {
			File weibo_file = new File(Config.SAVE_PATH+"/WeibosCon/"+uid+".txt");
			BufferedReader br = new BufferedReader(new FileReader(weibo_file));
			String weibocon ="";
			boolean isEmotion = false,isEmotion2 = false,isBuzz = false,isModal = false,isAcronym =false,isPunt = false;
			while((weibocon=br.readLine())!=null)
			{
				if(weibocon.equals(""))continue;
				weibocon = Utils.clearWeiboAt(weibocon);//将@用户名从微薄内容中去掉
				
				Map<Integer, Integer> emotion_map = get_feature_map(weibocon, emotion_dict);//得到按特征序号从小到大排序的特征：特征出现次数的map
				emotion_list.add(emotion_map);if(!isEmotion&&emotion_map.size()>0){isEmotion=true;}
				
				Map<Integer, Integer> emotion_map2 = get_feature_map(weibocon, emotion_dict2);
				emotion_list2.add(emotion_map2);if(!isEmotion2&&emotion_map2.size()>0){isEmotion2=true;}
				
				Map<Integer, Integer> buzz_map = get_feature_map(weibocon, buzz_dict);
				buzz_list.add(buzz_map);if(!isBuzz&&buzz_map.size()>0){isBuzz=true;}
				
				Map<Integer, Integer> modal_map = get_feature_map(weibocon, modal_dict);
				modal_list.add(modal_map);if(!isModal&&modal_map.size()>0){isModal=true;}
				
				Map<Integer, Integer> acronym_map = get_feature_map(weibocon, acronym_dict,RegexString.Regex_acronym);
				acronym_list.add(acronym_map);if(!isAcronym&&acronym_map.size()>0){isAcronym=true;}
				
				Map<Integer, Integer> punt_map = get_feature_map(weibocon, punt_dict,RegexString.Regex_punt);
				punt_list.add(punt_map);if(!isPunt&&punt_map.size()>0){isPunt=true;}
			}
			br.close();
			if(isEmotion)SaveInfo.saveMapList("Feature_Style\\Emotion\\"+uid+".txt",emotion_list,":",false);
			if(isEmotion2)SaveInfo.saveMapList("Feature_Style\\Emotion2\\"+uid+".txt",emotion_list2,":",false);
			if(isBuzz)SaveInfo.saveMapList("Feature_Style\\Buzz\\"+uid+".txt",buzz_list,":",false);
			if(isAcronym)SaveInfo.saveMapList("Feature_Style\\Acronym\\"+uid+".txt",acronym_list,":",false);
			if(isPunt)SaveInfo.saveMapList("Feature_Style\\Punt\\"+uid+".txt",punt_list,":",false);
			if(isModal)SaveInfo.saveMapList("Feature_Style\\Modal\\"+uid+".txt",modal_list,":",false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private Map<Integer, Integer> get_feature_map(String weibocon,Map<String, Integer> dict, String regex) {
		Map<Integer,Integer> map = new TreeMap<Integer, Integer>();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(weibocon);
		while(m.find()){
			String term = m.group();
			if(!dict.containsKey(term)){continue;}
			int trem_num = dict.get(term);
			if(map.containsKey(trem_num)){
				map.put(trem_num,map.get(trem_num)+1);
			}else{
				map.put(trem_num, 1);
			}
		}
		return map;
	}

	/**
	 * 遍历dict，将文本包含列在其中的词加入map，key为词，value为包含次数
	 * ps：注意这种情况
	 * 内容：这是 WIN7 和 WIN8 的优化版本，因为你不是平板设备，所以它显示的是为键鼠设备准备的界面。如果是平板设备，在和键鼠模式切换的时候，它也会根据需求而变化。）
		坏结果：10497:1	5081:2	2147:2	10502:1	1891:2	【分别匹配了WIN7、WIN8 、WIN、IN、WI】
		理想结果：10497:1	10502:1						【只匹配了WIN7、WIN8】		 
	 * @param context
	 * @return
	 */
	public Map<Integer, Integer> get_feature_map(String context,Map<String,Integer> dict) {
		Map<String,Integer> term_map = new HashMap<String,Integer>();//<词项，词项编码>
		Map<Integer,Integer> feature_map = new TreeMap<Integer, Integer>();//<词项编码，词项出现次数>
		Iterator<Entry<String, Integer>> it = dict.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Integer> term_item = it.next();
			String term = term_item.getKey();
			int term_id = term_item.getValue();
			int num = 0;
			String context_cp = context;
			while(!context_cp.equals("")){//一条微博可能匹配某词项多次
				int index = context_cp.indexOf(term);
				boolean flag = true;
				if(index==-1){//如果微博中不存在该词项，直接尝试下一个词项
					break;
				}else if(flag&&contains(term_map,feature_map,term)){//如果匹配到该词项，则检测已经匹配到的词项中是否存在该词项的真子集或真超集
					//如有超集，则放弃匹配该词项，返回true；若有子集，则将子集从term_map,feature_map中去掉，返回false;
					break;
				}else{//如果匹配到该词项，并且已匹配到的词项中不存在该词项，或该词项的超集，则为该词项计数,并且下次不用再判断该词项是否出现在已经匹配到的词项集合中
					num++;
					flag = false;
					context_cp = context_cp.substring(index+term.length());//不考虑词项重叠的情况，如词项为ww，www只会匹配ww一次
				}
			}
			if(num>0){			
				feature_map.put(term_id, num);
				term_map.put(term,term_id);
			}
		}
		return feature_map;
	}

	/**
	 * 检查term_map中是否包含term的真子集或真超集
	 * @param term_map
	 * @param term
	 * @param map
	 * @return
	 */
	private boolean contains(Map<String,Integer> term_map, Map<Integer, Integer> feature_map, String term) {
		boolean flag = false;
		Set<String> term_set = new HashSet<String>();
		Iterator<Entry<String, Integer>> it = term_map.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Integer> w = it.next();
			if(w.getKey().contains(term)){flag = true;break;}//如有超集，则放弃匹配该词项，返回true，如WIN7存在于term_map中，再匹配WIN、WI、IN时应该舍弃
			if(term.contains(w.getKey())){feature_map.remove(w.getValue());term_set.add(w.getKey());}//若有子集，则将子集从feature_map中去掉，返回false，如WI、IN存在于term_map中，再匹配WIN7时应该丢弃原来匹配的WI、IN

		}
		for(String tr : term_set){//将term包含的子集，从term_map中去掉（不能在while循环内删除，因为正在使用term_map的迭代器）
			term_map.remove(tr);
		}
		return flag;
	}

}
