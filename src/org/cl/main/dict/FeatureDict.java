package org.cl.main.dict;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.cl.configuration.Config;
import org.cl.configuration.EncodeDict;
import org.cl.configuration.RegexString;
import org.cl.service.GetInfo;
import org.cl.service.SaveInfo;
import org.cl.service.Utils;

public class FeatureDict {

	public static int threshold0 = 0;
	public static int threshold1 = 0;
	public static void main(String args[]) throws IOException{
		//getEmoticonDict();//manual+traverse(weibos)
		//getSrcDict();//traverse(weiboCon)
		//getDictFromManualDict("Dict_Mobile.txt",0,"Dict_Mobile.txt");//根据dict_src.txt中src的名称，得到Dict_mobile词典（珍妮做的），将src中的mobile分离出来重新编码
		//getDict("Dict_Mobile.txt",0,"Dict_Src.txt","Dict_App.txt");//将src中mobile分离出来后剩余的src作为APP，重新编码
		getFriDict("UserInfo1.txt");
		//getVFriDict("UserInfo1.txt");
		//getTagDict("UserInfo0.txt"/*,"UserInfoOfEnterprise0.txt","UserInfo1.txt","UserInfoOfEnterprise1.txt"*/);//traverse(UserInfo.txt)
		//getDescDict("UserInfo0.txt.description.parsed","UserInfo1.txt.description.parsed",/*"UserInfoOfEnterprise0.txt.description.parsed",*/"UserInfoOfEnterprise1.txt.description.parsed");//traverse
		//getNgramDict("description","UserInfo0.txt","UserInfo1.txt");//traverse
		//getNgramDict("screenName","UserInfo0.txt","UserInfo1.txt");//traverse
		//getNgramDict("verifiedReason","UserInfo0.txt","UserInfo1.txt");//traverse
		//getSrcTypeDescDict("App","\\Feature_SRC\\ParseHTML_App.txt.parsed");//得到App描述文本的词典
		//getSrcTypeDescDict("Mobile","\\Feature_SRC\\ParseHTML_Mobile.txt.parsed");//得到Mobile描述文本的词典

		/*getDictByTravse("Dict_Acronym.txt",EncodeDict.Acronym,RegexString.Regex_acronym,threshold2);
		getDictByTravse("Dict_Punt.txt",EncodeDict.Punt,RegexString.Regex_punt,threshold0);

		getDictFromManualDict("Dict_Buzz.txt",EncodeDict.Buzz,"Dict_Buzz.txt");//manual
		getDictFromManualDict("Dict_Modal.txt",EncodeDict.Modal,"Dict_Modal.txt","Dict_Modal_auto.txt");//manual
		getDictFromManualDict("Dict_UserType.txt",EncodeDict.UserType,"Dict_FriType.txt");//manual
		getDictFromManualDict("Dict_UserType.txt",EncodeDict.UserType,"Dict_FolType.txt");//manual
		getDictFromManualDict("Dict_UserType.txt",EncodeDict.UserType,"Dict_FriFolType.txt");//manual
		getDictFromManualDict("Dict_UserType.txt",EncodeDict.UserType,"Dict_VFriFolType.txt");//manual
		getDictFromManualDict("Dict_AppType.txt",EncodeDict.SrcType,"Dict_AppType.txt");//manual
		getDictFromManualDict("Dict_MobileType.txt",EncodeDict.SrcType,"Dict_MobileType.txt");//manual

		getDictFromManualDict("Dict_EmotionStastic.txt",0,"Dict_EmotionStastic.txt");//manual
		getDictFromManualDict("Dict_Behaviour32.txt",0,"Dict_Behaviour32.txt");//manual
		getDictFromManualDict("Dict_Behaviour31.txt",0,"Dict_Behaviour31.txt");//manual
		getDictFromManualDict("Dict_Behaviour2.txt",0,"Dict_Behaviour2.txt");//manual*/	
	}
	private static void getFriDict(String ... filenames) throws IOException {
		Set<String> vfri_set = new HashSet<String>();
		for(String filename : filenames){
			File fr1 = new File(Config.SAVE_PATH+filename);
			BufferedReader r1 = new BufferedReader(new FileReader(fr1));
			String line = "";
			while((line = r1.readLine())!=null){
				JSONObject user = JSONObject.fromObject(line);
				String uid = user.getString("id");
				vfri_set.add(uid);
			}
			r1.close();
		}
		SaveInfo.saveDict("Config\\Dict_Fri.txt",vfri_set,false,0);
	}
	private static void getVFriDict(String ... filenames) throws IOException {
		Set<String> vfri_set = new HashSet<String>();
		for(String filename : filenames){
			File fr1 = new File(Config.SAVE_PATH+filename);
			BufferedReader r1 = new BufferedReader(new FileReader(fr1));
			String line = "";
			while((line = r1.readLine())!=null){
				JSONObject user = JSONObject.fromObject(line);
				String uid = user.getString("id");
				int type = user.getInt("verifiedType");
				if(type<0||type>7){continue;}
				vfri_set.add(uid);
			}
			r1.close();
		}
		SaveInfo.saveDict("Config\\Dict_VFri.txt",vfri_set,false,0);
	}
	private static void getDict(String newdict, int start_index, String srcdict, String ... exceptdicts) throws IOException {
		Set<String> except_set = new HashSet<String>(); 
		for(String exceptdict : exceptdicts){
			GetInfo.getSet("Config\\"+exceptdict,except_set,"\t",0);
		}
		File r=new File(Config.SAVE_PATH+"Config\\"+srcdict);
		BufferedReader br=new BufferedReader(new FileReader(r));
		File f = new File(Config.SAVE_PATH+"Config\\"+newdict);
		BufferedWriter w = new BufferedWriter(new FileWriter(f));
		int i = start_index;
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				String item = line.split("\t")[0];
				if(except_set.contains(item))continue;
				w.write(item+"\t"+(i++)+"\r\n");
			}
		}
		br.close();
		w.flush();
		w.close();
	}
	//扫描文件内容获取词典
	private static void getDictByTravse(String newdict,int start_index,String regex,int threshold) throws IOException {
		Set<String> set = new HashSet<String>();
		Utils.getItemSet(regex,set,threshold);
		SaveInfo.saveDict("Config\\"+newdict,set,false,start_index);
	}
	//扫描人工语料文档获取词典
	private static void getDictFromManualDict(String newdict, int start_index,String ... manual_dicts) throws IOException {
		List<String> list = new ArrayList<String>(); 
		for(String manual_dict : manual_dicts){
			GetInfo.getList("Config\\manual\\"+manual_dict,list,"\t",0,true);
		}
		SaveInfo.saveDict("Config\\"+newdict,list,false,start_index);
	}

	private static void getSrcTypeDescDict(String type, String filename) throws IOException {
		Map<String,Integer> word_text_map = new HashMap<String,Integer>();
		File f=new File(Config.SAVE_PATH+filename);
		if(!f.exists())return;
		List<String> lines = new ArrayList<String>();
		GetInfo.getList(f,lines,true);
		for(String line:lines){
			String[] words = line.split("\\s+");
			for(String word : words){
				String[] word_type_list = word.split("/");
				if(word_type_list.length<2){continue;}
				String word_type = word_type_list[word_type_list.length-1];
				if(word_type.equals("nr")||word_type.contains("x")){continue;}//如果词项是 用户名、Email、分隔符、网址URL，则不加入词典
				if(word_type.equals("xm")){continue;}//表情符号
				if(word_type.contains("m")){continue;}//数字
				if(word_type.contains("w")){continue;}//标点符号
				String word_text = word.split("/[0-9a-z]{1,5}")[0];
				Utils.putInMap(word_text_map,word_text,1);
			}
		}
		Set<String> word_set = Utils.MapToSet(word_text_map,threshold0);
		SaveInfo.saveDict("Config\\"+type+".dict",word_set,false);
		SaveInfo.saveDict("Config\\Dict_"+type+"_Desc.txt",word_set,false,EncodeDict.Text);
	}
	private static void getDescDict(String ... filenames) throws IOException {
		//扫描微博内容文件获取
		Map<String,Integer> desc_map = new HashMap<String,Integer>();
		for(String filename : filenames){
			List<String> desc_list = new ArrayList<String>();
			GetInfo.getList("UserInfoTMP\\"+filename,desc_list,false);
			for(String desc:desc_list){
				String[] items = desc.split("\t")[1].split("\\s");
				for(String item : items){
					Utils.putInMap(desc_map, item, 1);
				}
			}
		}
		Set<String> desc_set = Utils.MapToSet(desc_map, threshold1);
		SaveInfo.saveDict("Config\\Dict_Description.txt",desc_set,false,EncodeDict.Descri);
	}
	private static void getTagDict(String ... filenames) throws IOException {
		//扫描微博内容文件获取
		Map<String,Integer> tag_map = new HashMap<String,Integer>();
		for(String filename : filenames){
			List<List<String>> tags_list = new ArrayList<List<String>>();
			GetInfo.getListList(filename,tags_list,"tags","tags");
			for(List<String> tags:tags_list){
				for(String tag : tags){
					Utils.putInMap(tag_map, tag, 1);
				}
			}
		}
		Set<String> tag_set = Utils.MapToSet(tag_map, threshold1);
		SaveInfo.saveDict("Config\\Dict_Tag.txt",tag_set,false,EncodeDict.Tag);
	}
	private static void getNgramDict(String attr,String ... filenames) throws IOException {
		Map<String,Integer> attr_map2 = new HashMap<String,Integer>();
		Map<String,Integer> attr_map3 = new HashMap<String,Integer>();
		for(String filename : filenames){
			File f = new File(Config.SAVE_PATH+filename);
			BufferedReader r = new BufferedReader(new FileReader(f));
			String line = null;
			while(null!=(line = r.readLine())){
				JSONObject user = JSONObject.fromObject(line);

				String user_attr = user.getString(attr);
				List<String> user_attr_ngram = Utils.toNgram(user_attr, 2);
				for(String ngram : user_attr_ngram){
					Utils.putInMap(attr_map2, ngram, 1);
				}
				user_attr_ngram = Utils.toNgram(user_attr, 3);
				for(String ngram : user_attr_ngram){
					Utils.putInMap(attr_map3, ngram, 1);
				}
			}
			r.close();
		}
		Set<String> attr_set2 = Utils.MapToSet(attr_map2, threshold1);
		Set<String> attr_set3 = Utils.MapToSet(attr_map3, threshold1);
		SaveInfo.saveDict("Config\\Dict_"+attr+"2.txt",attr_set2,false,EncodeDict.Descri);
		SaveInfo.saveDict("Config\\Dict_"+attr+"3.txt",attr_set3,false,EncodeDict.Descri);
	}

	private static void getSrcDict() throws IOException {
		Map<String,String> src_map = new HashMap<String,String>();
		Map<String,Integer> src_dict = new HashMap<String,Integer>();
		//扫描微博内容文件获取词典
		File f1=new File(Config.SAVE_PATH+"Weibos");
		if(!f1.exists())return;
		File file[] = f1.listFiles(); 
		for(File f : file){
			List<String> src_list = new ArrayList<String>();
			GetInfo.getList(f,src_list,true,"weibo","source");
			for(String src:src_list){
				String src_clean = Utils.clearSource(src);
				String src_href = Utils.getSource(src);
				src_map.put(src_clean, src_href);
				Utils.putInMap(src_dict, src_clean, 1);
			}
		}
		Set<String> src_set = Utils.MapToSet(src_dict, threshold1);
		SaveInfo.saveMap("Config\\Src_Url.txt", src_map, src_set, false);
		SaveInfo.saveDict("Config\\Dict_Src.txt",src_set,false,EncodeDict.Src);
	}


	private static void getEmoticonDict() throws IOException {
		//扫描微博内容文件获取词典
		Set<String> emotion_set1 = new HashSet<String>();
		Utils.getItemSet(RegexString.Regex_emoticon_official,emotion_set1,threshold1);//从用户微博内容数据中遍历，根据regex正则表达式得到词典的词项集合
		GetInfo.getSet("Config\\manual\\Dict_Emotion_auto.txt",emotion_set1);//根据分词结果标签获取到的表情
		int size = SaveInfo.saveDict("Config\\Dict_Emotion1.txt",emotion_set1,false,EncodeDict.Emoticon);//存储官方表情
		//扫描人工语料文档获取词典
		List<String> emotion_list2 =  new ArrayList<String>(); 
		GetInfo.getList("Config\\manual\\Dict_Emotion2.txt",emotion_list2,"\\t+",0,true);
		SaveInfo.saveDict("Config\\Dict_Emotion2.txt",emotion_list2,false,EncodeDict.Emoticon+size);//存储符号表情
		//存储官方和符号表情
		SaveInfo.saveDict("Config\\Dict_Emotion.txt",emotion_set1,false,EncodeDict.Emoticon);
		SaveInfo.saveDict("Config\\Dict_Emotion.txt",emotion_list2,true,EncodeDict.Emoticon+size);
	}


}
