package org.cl.main.feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.cl.configuration.Config;
import org.cl.service.GetInfo;
import org.cl.service.SaveInfo;
import org.cl.service.Utils;

public class Main_GetUserInfoFeature {
	public static void main(String args[]) throws IOException, InterruptedException
	{	
		SaveInfo.mkdir("Feature_UserInfo");
		//使用nlpir分词，运行前首先使用nlpir分词工具对用户描述进行分词，得到"Feature_UserInfo\\UserInfo0.txt.description.parsed"文件
		/*Map<String,Integer> Descri_dict = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Description.txt",Descri_dict);
		getDescriFeature("UserInfoTMP\\UserInfo0.txt.description.parsed",Descri_dict,"");
		//getDescriFeature("UserInfoTMP\\UserInfoOfEnterprise0.txt.description.parsed",Descri_dict,"");
		getDescriFeature("UserInfoTMP\\UserInfo1.txt.description.parsed",Descri_dict,"");
		getDescriFeature("UserInfoTMP\\UserInfoOfEnterprise1.txt.description.parsed",Descri_dict,"");
		*/
		/*//使用2gram分词，运行前首先使用other.GetUserInfoNgram对用户描述进行NGRAM分词，得到"Feature_UserInfo\\UserInfo0.txt.description.ngram"文件
		Descri_dict = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Description2.txt",Descri_dict);
		getDescriFeature("UserInfoTMP\\UserInfo0.txt.description.2gram",Descri_dict,"2");
		
		//使用3gram分词，运行前首先使用other.GetUserInfoNgram对用户描述进行NGRAM分词，得到"Feature_UserInfo\\UserInfo0.txt.description.ngram"文件
		Descri_dict = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Description3.txt",Descri_dict);
		getDescriFeature("UserInfoTMP\\UserInfo0.txt.description.3gram",Descri_dict,"3");*/
		
		//Tag特征
		Map<String,Integer> Tag_dict = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Tag.txt",Tag_dict);
		getTagFeature("UserInfo0.txt",Tag_dict);
		//getTagFeature("UserInfoOfEnterprise0.txt",Tag_dict);
		getTagFeature("UserInfo1.txt",Tag_dict);
		getTagFeature("UserInfoOfEnterprise1.txt",Tag_dict);
	}
	
	private static void getDescriFeature(String filename,Map<String, Integer> descri_dict,String gram) throws IOException {
		File r=new File(Config.SAVE_PATH+filename);
		BufferedReader br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				String[] items = line.split("\t");
				String id = items[0];
				String[] features = items[1].split("\\s");
				Map<Integer,Integer> map = new TreeMap<Integer, Integer>();//存放该用户的descri标签特征列表  112:1,113:1
				for(String feature : features){
					if(descri_dict.containsKey(feature)){//不存在于字典中的是被过滤掉的词组
						int index = descri_dict.get(feature);
						Utils.putInMap(map, index, 1);
					}
				}
				SaveInfo.saveMap("Feature_UserInfo\\Description"+gram+"_feature.txt",id,map,true);
			}
		}
		br.close();
	}
	
	private static void getTagFeature(String filename,Map<String,Integer> tag_dict) throws IOException {
		Map<String,List<String>> uid_tag_map = new HashMap<String,List<String>>();
		GetInfo.getMap(filename,uid_tag_map,new String[]{"id"},new String[]{"tags","tags"});
		Iterator<Entry<String,List<String>>> it = uid_tag_map.entrySet().iterator();
		while(it.hasNext()){//对每一个用户
			Entry<String,List<String>> entry = it.next();
			String id = entry.getKey();
			List<String> tags = entry.getValue();
			if(null==tags||tags.size()==0)continue;
			Map<Integer,Integer> map = new TreeMap<Integer, Integer>();//存放该用户的tag标签特征列表  112:1,113:1
			for(String t :tags){
				int index = tag_dict.get(t);
				Utils.putInMap(map, index, 1);
			}
			SaveInfo.saveMap("Feature_UserInfo\\Tag_feature.txt",id,map,true);
		}
	}
}
