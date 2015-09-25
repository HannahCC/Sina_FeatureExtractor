package org.cl.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.cl.run.GetTextualFeatures;
import org.cl.service.GetInfo;

public class testTF {
	public static void main(String args[]) throws IOException{
		String uid = "2009192895";
		Map<String,Integer> word_dict = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_Text.txt",word_dict);
		Map<String,Integer> word_type_dict = new HashMap<String,Integer>();
		GetInfo.getDict("Config\\Dict_POS.txt",word_type_dict);
		GetTextualFeatures getTextualFeatures=new GetTextualFeatures(uid,word_dict,word_type_dict);
		getTextualFeatures.run();
	}
}
