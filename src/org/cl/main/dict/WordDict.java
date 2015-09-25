package org.cl.main.dict;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cl.configuration.Config;
import org.cl.configuration.EncodeDict;
import org.cl.service.GetInfo;
import org.cl.service.SaveInfo;
import org.cl.service.Utils;

//通过遍历分词后的微博内容，以获取text、pos词典，同时获取manual\\Dict_Emotion_auto.txt和manual\\Dict_Modal_auto.txt
public class WordDict {
	public static int threshold = 1;
	public static void main(String args[]) throws IOException{
		Map<String,Integer> word_map = new HashMap<String,Integer>();
		Map<String,Integer> word_type_map = new HashMap<String,Integer>();
		Map<String,Integer> emotion_map = new HashMap<String,Integer>();
		Map<String,Integer> modal_map = new HashMap<String,Integer>();
		//Map<String,Integer> punt_map = new HashMap<String,Integer>();
		File f1=new File(Config.SAVE_PATH+"\\Weibos_Participle\\");
		if(!f1.exists())return;
		File file[] = f1.listFiles(); 
		for(File f : file){
			Set<String> word_type_set = new HashSet<String>();//1839013465
			Set<String> word_set = new HashSet<String>();
			Set<String> emotion_set = new HashSet<String>();
			Set<String> modal_set = new HashSet<String>();
			//Set<String> punt_set = new HashSet<String>();
			List<String> weibo_list = new ArrayList<String>();
			GetInfo.getList(f,weibo_list,true);
			for(String weibo:weibo_list){
				String[] word_list = weibo.split("\\s+");
				for(String word : word_list){
					//一个word可能是：饺子/n、///xs、  /n
					String[] word_item = word.split("/");
					if(word_item.length<2){continue;}
					String word_type = word_item[word_item.length-1];
					if(word_type.equals("nr")){continue;}//如果词项是 用户名则不加入词典
					word_type_set.add(word_type);
					String word_text = word_item[0];
					if(word_type.equals("xm")){emotion_set.add(word_text);continue;}//表情符号 加入到表情词典中
					if(word_type.equals("e")){modal_set.add(word_text);}//叹词语气词 加入到语气词词典中
					//if(word_type.contains("w")){punt_set.add(word_text);}//标点符号加入到标点符号词典中
					word_set.add(word_text);
				}
			}
			//为什么不直接将词项加入map？因为一个文件中词项出现多次只能按一次计算
			Utils.SetToMap(word_type_map,word_type_set);
			Utils.SetToMap(word_map,word_set);
			Utils.SetToMap(emotion_map,emotion_set);
			Utils.SetToMap(modal_map,modal_set);
			//Utils.SetToMap(punt_map,punt_set);
		}
		Set<String> word_type_set = Utils.MapToSet(word_type_map,threshold);
		Set<String> word_set = Utils.MapToSet(word_map,threshold);
		Set<String> emotion_set = Utils.MapToSet(emotion_map,threshold);
		Set<String> modal_set = Utils.MapToSet(modal_map,threshold);
		//Set<String> punt_set = Utils.MapToSet(punt_map,threshold);

		SaveInfo.saveDict("Config\\Dict_Text.txt",word_set,false,EncodeDict.Text);
		SaveInfo.saveDict("Config\\Dict_POS.txt",word_type_set,false,EncodeDict.POS);
		SaveInfo.saveDict("Config\\manual\\Dict_Emotion_auto.txt",emotion_set,"");
		SaveInfo.saveDict("Config\\manual\\Dict_Modal_auto.txt",modal_set,"");
		//SaveInfo.saveDict("Config\\manual\\Dict_Punt_auto.txt",punt_set,"");
	}


}
