package org.cl.run;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.cl.configuration.Config;
import org.cl.service.GetInfo;
import org.cl.service.SaveInfo;
import org.cl.service.Utils;


public class GetSrcFeature implements Runnable
{
	/**用户ID*/
	private String uid=null;
	private Map<String, Set<String>> app_type_keywords = null;
	private Map<String, Set<String>> mobile_type_keywords = null;
	private Set<String> mobile_dict = null;
	private int NEW_TYPE = 0;
	private int OTHER_TYPE = 1;
	public GetSrcFeature(String uid,Map<String, Set<String>> app_type_keywords,Map<String, Set<String>> mobile_type_keywords,Set<String> mobile_dict)
	{
		this.uid=uid;
		this.app_type_keywords = app_type_keywords;
		this.mobile_type_keywords = mobile_type_keywords;
		this.mobile_dict = mobile_dict;
	}

	public void run()
	{
		System.out.println("Getting Src Feature of "+uid);
		try {
			Map<String,Integer> src_map = getSrcMap();//获取用户使用的src,并记录使用次数，如：QQ音乐,4 ——————使用QQ音乐发微博4次
			if(src_map==null||src_map.size()==0)return;
			Map<String,Integer> app_map = new HashMap<String,Integer>();
			Map<String,Integer> mobile_map = new HashMap<String,Integer>();
			spilt(src_map,app_map,mobile_map);//根据mobile_dict将src分成app/mobile两类
			Map<Integer,Integer> app_type_map = new TreeMap<Integer,Integer>();
			Map<Integer,Integer> mobile_type_map = new TreeMap<Integer,Integer>();
			classiferSrc("AppType",app_map,app_type_map,app_type_keywords);//根据app_type_keywords对每个app类的src分类，如无法分类，则为默认类别OTHER_TYPE
			classiferSrc("MobileType",mobile_map,mobile_type_map,mobile_type_keywords);//根据mobile_type_keywords对每个mobile类的src分类，如无法分类，则为默认类别OTHER_TYPE
			SaveInfo.saveMap("Feature_SRC\\AppType_feature.txt",uid,app_type_map,true);
			SaveInfo.saveMap("Feature_SRC\\MobileType_feature.txt",uid,mobile_type_map,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * 根据src_type_keywords，为某一用户对应的src_map中的每个src进行分类，一个src可能属于多个类别
	 * @param src_map 
	 * @param src_type_keywords
	 * @return
	 * @throws IOException 
	 */
	private Map<Integer, Integer> classiferSrc(String src_type,Map<String, Integer> src_map,Map<Integer,Integer> src_type_map,Map<String, Set<String>> src_type_keywords) throws IOException {
		if(src_map.size()==0)return null;
		File f = new File(Config.SAVE_PATH+"\\Feature_SRC\\"+src_type+"\\"+uid+".txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		File f2 = new File(Config.SAVE_PATH+"\\Feature_SRC\\"+src_type+"_undefined.txt");
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(f2,true));
		
		bw.write("SRC种类\t"+src_map.size()+"\t"+NEW_TYPE+"##\r\n");//src的类别总数作为第一个特征
		src_type_map.put(NEW_TYPE, src_map.size());
		for(Entry<String, Integer> src_entry : src_map.entrySet()){
			String src = src_entry.getKey();
			int value = src_entry.getValue();
			StringBuffer newline = new StringBuffer();
			newline.append(src+"\t"+value+"\t");
			boolean flag = false;
			for(Entry<String,Set<String>> keywords_set_entry : src_type_keywords.entrySet()){
				int type = Integer.parseInt(keywords_set_entry.getKey());
				Set<String> keywords_set = keywords_set_entry.getValue();
				Set<String> keywords = Utils.checkwords(keywords_set, src);
				if(keywords.size()>0){
					flag = true;
					Utils.putInMap(src_type_map, type, value);//将类别添加到map中
					newline.append(type);//将类别添加到BufferString中，作为中间结果输出
					for(String keyword : keywords){newline.append("##"+keyword);}
					newline.append("\t");
				}	
			}
			if(!flag){
				Utils.putInMap(src_type_map, OTHER_TYPE, value);//未分类成功的src作为其他类src
				newline.append(OTHER_TYPE+"##\t");
				bw2.write(newline.toString()+"\r\n");
			}
			bw.write(newline.toString()+"\r\n");
		}
		bw.flush();
		bw.close();
		bw2.flush();
		bw2.close();
		return src_type_map;
	}

	private void spilt(Map<String, Integer> src_map,Map<String, Integer> app_map, Map<String, Integer> mobile_map) {
		for(Entry<String, Integer> src_entry: src_map.entrySet()){
			String src = src_entry.getKey();
			int value = src_entry.getValue();
			if(mobile_dict.contains(src)){
				mobile_map.put(src, value);
			}else{
				app_map.put(src, value);
			}
		}
	}

	private Map<String, Integer> getSrcMap() throws IOException {  
		Map<String,Integer> src_map = new HashMap<String,Integer>();
		List<String> src_list = new ArrayList<String>();
		GetInfo.getList("\\Weibos\\"+uid+".txt", src_list, false, "weibo","source");
		for(String src : src_list){
			src = Utils.clearSource(src);
			if(src.equals(""))continue;
			Utils.putInMap(src_map, src, 1);
		}
		return src_map;
	}

}
