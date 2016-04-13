package org.cl.main.dict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cl.configuration.Config;
import org.cl.configuration.EncodeDict;
import org.cl.service.SaveInfo;
import org.cl.service.Utils;

//通过遍历分词后的微博内容，以获取text、pos词典
public class TextDict {
	public static int threshold = 1;
	public static void main(String args[]) throws IOException{
		Map<String,Integer> word_map = new HashMap<String,Integer>();
		Set<String> word_set = new HashSet<String>();
		File f1=new File(Config.SAVE_PATH+"\\Weibos_Text\\");
		if(!f1.exists())return;
		File file[] = f1.listFiles(); 
		for(File f : file){
			BufferedReader br=new BufferedReader(new FileReader(f));
			String line="";
			while((line=br.readLine())!=null)
			{
				if(!(line.equals(""))){
					String[] word_list = line.split("\\s+");
					for(String word : word_list){
						word_set.add(word);
					}
				}
			}
			br.close();
			//为什么不直接将词项加入map？因为要让  出现在超过threshold个用户的微博中的词才能加入词典
			Utils.SetToMap(word_map,word_set);
			word_set.clear();
		}
		Set<String> final_word_set = Utils.MapToSet(word_map,threshold);
		SaveInfo.saveDict("Config\\Dict_Text.txt",final_word_set,false,EncodeDict.Text);
	}


}
