package org.cl.run;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cl.configuration.EncodeDict;
import org.cl.configuration.RegexString;
import org.cl.service.GetInfo;
import org.cl.service.SaveInfo;
import org.cl.service.Utils;

public class GetEmoticonStastic implements Runnable {
	private int index = EncodeDict.EmoticonStas;//统计特征编码从0开始
	private int segmentation = 0;//字符表情编码从0开始
	private int weiboSum = 0;//微博总数
	private int emoSum = 0;//表情总数(强度)
	private int officialEmoSum = 0;//官方表情总数
	private int characterEmoSum = 0;//字符表情总数
	private int emoTypeSum = 0;//表情类别总数(丰富度)
	private int emoWeiboSum=0;//含表情的微博数
	private int emoOnlyWeibosum=0;//只含表情的微博数(表情依赖度)
	private int successiveEmoMax = 0;//连续表情数最大值
	private int successiveSameEmoMax = 0;//连续同一表情数最大值
	private double successiveEmoAvg = 0;//连续表情数均值(>1)
	private double successiveSameEmoAvg = 0;//连续同一表情数均值(>1)
	private double ratio_EWS2WS = 0;//含表情的微博数：微博总数(<1)(频繁程度)
	private double ratio_ES2EWS = 0;//表情总数：含表情的微博数(>1)(强度)
	private double ratio_OES2ES = 0;//官方表情总数：表情总数(官方表情强度)
	private double ratio_CES2ES = 0;//字符表情总数：表情总数(字符表情强度)
	private String uid=null;
	private Map<String,Integer> emotion_dict = null;
	public GetEmoticonStastic(String uid, Map<String, Integer> emotion_list, int middle) {
		this.uid=uid;
		this.emotion_dict = emotion_list;
		this.segmentation = middle;
	}
	public String toString(){
		DecimalFormat df = new DecimalFormat("0.000");
		StringBuffer sb = new StringBuffer();
		sb.append(uid).append("\t").append(index).append(":").append(weiboSum).append("\t").append((index+1)).append(":").append(emoSum).append("\t").
				append((index+2)).append(":").append(officialEmoSum).append("\t").append((index+3)).append(":").append(characterEmoSum).append("\t").
				append((index+4)).append(":").append(emoTypeSum).append("\t").append((index+5)).append(":").append(emoWeiboSum).append("\t").
				append((index+6)).append(":").append(emoOnlyWeibosum).append("\t").
				append((index+7)).append(":").append(successiveEmoMax).append("\t").append((index+8)).append(":").append(successiveSameEmoMax).append("\t").
				append((index+9)).append(":").append(df.format(successiveEmoAvg)).append("\t").append((index+10)).append(":").append(df.format(successiveSameEmoAvg)).append("\t").
				append((index+11)).append(":").append(df.format(ratio_EWS2WS)).append("\t").append((index+12)).append(":").append(df.format(ratio_ES2EWS)).append("\t").
				append((index+13)).append(":").append(df.format(ratio_OES2ES)).append("\t").append((index+14)).append(":").append(df.format(ratio_CES2ES)).append("\r\n");
		return sb.toString();
	};
	
	
	public void run() {
		System.out.println("Getting emoticon statistical magnitudes of "+uid);
		List<Map<Integer, Integer>> emotion_map_list = new ArrayList<Map<Integer, Integer>>();//得到按特征序号从小到大排序的特征：特征出现次数的map
		List<Integer> successiveSameEmoNum = new ArrayList<Integer>();//用于存放各微博连续表情数量
		List<Integer> successiveEmoNum = new ArrayList<Integer>();//用于存放各微博连续表情数量
		List<String> weibocon_list = new ArrayList<String>();//获取用户的微博列表
		try {
			GetInfo.getMapList("Feature_Style\\Emotion\\"+uid+".txt",emotion_map_list);
			GetInfo.getList("WeibosCon\\"+uid+".txt",weibocon_list,false,"weibo","text");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		weiboSum = weibocon_list.size();
		int i = 0;
		Set<Integer> emoType = new HashSet<Integer>();
		for(Map<Integer, Integer> emotion_map : emotion_map_list){
			if(emotion_map.size()>0){//如果该微博含有表情
				int[] emoNum = getEmoStas(emoType,emotion_map);
				officialEmoSum += emoNum[0];//官方表情总数
				characterEmoSum += emoNum[1];//字符表情总数
				String weibocon = Utils.clearWeibo(weibocon_list.get(i));//获得干净的微博Text
				if(emoOnlyWeibosum(weibocon)){emoOnlyWeibosum++;}
				successiveEmoNum.add(getsuccessiveEmoNum(weibocon,RegexString.Regex_emoticon_official));
				successiveSameEmoNum.add(getsuccessiveSameEmoNum(weibocon,RegexString.Regex_emoticon_official));
			}
			i++;
		}
		emoSum=officialEmoSum+characterEmoSum;
		emoWeiboSum = successiveEmoNum.size();
		if(emoSum==0){}
		emoTypeSum = emoType.size();
		ratio_OES2ES = officialEmoSum/(double)emoSum;
		ratio_CES2ES = characterEmoSum/(double)emoSum;
		ratio_ES2EWS = emoSum/(double)emoWeiboSum;
		ratio_EWS2WS = emoWeiboSum/(double)weiboSum;
		Collections.sort(successiveEmoNum);
		successiveEmoMax = successiveEmoNum.get(successiveEmoNum.size()-1);
		successiveEmoAvg = getAvgOfList(successiveEmoNum);
		Collections.sort(successiveSameEmoNum);
		successiveSameEmoMax = successiveSameEmoNum.get(successiveSameEmoNum.size()-1);
		successiveSameEmoAvg = getAvgOfList(successiveSameEmoNum);
		try {
			SaveInfo.saveString("\\Feature_Style\\EmotionStastic_feature.txt",toString(),true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private Integer getsuccessiveSameEmoNum(String weibocon,String regexEmoticon) {
		Pattern p = Pattern.compile(RegexString.Regex_emoticon_official);
		Matcher m = p.matcher(weibocon);
		int last_end = -1,last_index = -1;
		int max = 0,num = 0;
		while(m.find()){
			String emo = m.group();
			if(!emotion_dict.containsKey(emo)){continue;}
			int index = emotion_dict.get(emo);
			int start = m.start();
			int end = m.end();
			if(start == last_end&&last_index == index){num++;}
			else{max = max>num?max:num;num=1;}
			last_end=end;
			last_index=index;
		}
		max = max>num?max:num;
		return max;
	}
	//只返回连续的官方表情数最大值
	private Integer getsuccessiveEmoNum(String weibocon, String regexEmoticon) {
		Pattern p = Pattern.compile(RegexString.Regex_emoticon_official);
		Matcher m = p.matcher(weibocon);
		int last_end = -1;
		int max = 0,num = 0;
		while(m.find()){
			String emo = m.group();
			if(!emotion_dict.containsKey(emo)){continue;}
			int start = m.start();
			int end = m.end();
			if(start == last_end){num++;}
			else{max = max>num?max:num;num=1;}
			last_end=end;
		}
		max = max>num?max:num;
		return max;
	}
	private boolean emoOnlyWeibosum(String weibocon) {
		Pattern p = Pattern.compile(RegexString.Regex_emoticon_official);//去掉官方表情
		Matcher m = p.matcher(weibocon);
		while(m.find()){
			String emo = m.group();
			if(emotion_dict.containsKey(emo)){weibocon = weibocon.replaceAll(emo, "");}
		}
		Pattern p2 = Pattern.compile(RegexString.Regex_char);//如果还存在文字（汉子、英文、数字）则认为不只有表情
		Matcher m2 = p2.matcher(weibocon);
		if(m2.find()){return false;}
		return true;
	}
	private double getAvgOfList(List<Integer> successiveEmoNum) {
		int sum = 0;
		for(Integer num : successiveEmoNum){
			sum += num;
		}
		return sum/(double)successiveEmoNum.size();
	}
	private int[] getEmoStas(Set<Integer> type,Map<Integer, Integer> emotion_map) {
		int[] num = {0,0};
		Iterator<Entry<Integer, Integer>> it = emotion_map.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer, Integer> entry = it.next();
			type.add(entry.getKey());
			if(entry.getKey()<segmentation)//官方表情
				num[0] += entry.getValue();
			else//字符表情
				num[1] += entry.getValue();
		}
		return num;
	}
}
