package org.cl.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cl.configuration.Config;
import org.cl.configuration.RegexString;

public class Utils {

	public static Date getDateFromString(String dtext,Date fileCreateDate) {
		Date date=null;
		int y,mm,se;  
		Calendar c = Calendar.getInstance();  
		c.setTime(fileCreateDate);
		y = c.get(Calendar.YEAR); //年 
		//d = c.get(Calendar.DAY_OF_MONTH); //日  
		mm = c.get(Calendar.MINUTE); //分
		se = c.get(Calendar.SECOND);//秒
		if(dtext.contains("秒前")){
			int end=0;
			for(int i=0;i<dtext.length();i++){
				if(dtext.charAt(i)>='0' && dtext.charAt(i)<='9'){
					end++;
				}else{
					break;
				}
			}
			dtext=dtext.substring(0,end);
			int second=Integer.parseInt(dtext);
			c.set(Calendar.SECOND, se-second);
			date=c.getTime();
		}
		else if(dtext.contains("分钟前")){
			int end=0;
			for(int i=0;i<dtext.length();i++){
				if(dtext.charAt(i)>='0' && dtext.charAt(i)<='9'){
					end++;
				}else{
					break;
				}
			}
			dtext=dtext.substring(0,end);
			int minute=Integer.parseInt(dtext);
			c.set(Calendar.MINUTE, mm-minute);
			date=c.getTime();
		}else if(dtext.contains("今天")){
			dtext=dtext.replace("今天 ", "").trim();
			String ss[]=dtext.split(":");
			if(ss!=null && ss.length==2){
				c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ss[0]));
				c.set(Calendar.MINUTE, Integer.parseInt(ss[1]));
				date=c.getTime();
			}
		}else if(dtext.contains("月")){
			dtext=y+"年".concat(dtext);
			SimpleDateFormat sf=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
			try {
				date=sf.parse(dtext);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else if(dtext.contains("-")){
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				date=sf.parse(dtext);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}

	/**
	 * 从用户微博内容数据中遍历，根据regex正则表达式得到词典的词项集合
	 * 超过threshold数目个用户都使用该词项，记录该词项到词典集合中
	 * @param regex
	 * @param threshold
	 * @return
	 * @throws IOException 
	 */
	public static void getItemSet(String regex,Set<String> item_set,int threshold) throws IOException {
		Map<String,Integer> map = new HashMap<String,Integer>();//<词项，出现该词项的用户数>
		Pattern p = Pattern.compile(regex);
		File dir=new File(Config.SAVE_PATH+"//WeibosCon/");
		if(!dir.exists())return;
		File file[] = dir.listFiles(); 
		for(File f : file){
			List<String> weibos =  new ArrayList<String>();
			GetInfo.getList(f,weibos,true);//按行读取f文件中微博内容
			Set<String> set_i = new HashSet<String>();//f文件中出现的词项集合
			for(String weibo:weibos){
				Matcher m = p.matcher(weibo);
				while(m.find()){
					set_i.add(m.group());
				}
			}
			//为什么不直接将词项加入map？因为一个文件中词项出现多次只能按一次计算
			for(String item:set_i){//将f文件中出现的词项集合加入到map中
				if(map.containsKey(item)){
					map.put(item, map.get(item)+1);
				}else{
					map.put(item, 1);
				}
			}

		}
		//Set<String> item_set = new HashSet<String>();
		Iterator<Entry<String, Integer>> map_it = map.entrySet().iterator();
		while(map_it.hasNext()){
			Entry<String, Integer> entry = map_it.next();
			if(entry.getValue()>threshold){//超过threshold数目个用户都使用该词项，记录该词项到词典集合中
				item_set.add(entry.getKey());
			}
		}
		//return item_set;
	}
	/**
	 * 利用子串匹配检测item是否包含word_set里面的词项
	 * @param word_set
	 * @param item
	 * @return
	 */
	public static Set<String> checkwords(Set<String> word_set, String item) {
		Set<String> word_subset = new HashSet<String>();
		for(CharSequence word : word_set){
			if(item.contains(word)){
				word_subset.add(word.toString());
				System.out.print(word+",");
			}
		}
		System.out.println();
		return word_subset;
	}

	/**
	 * 对map根据value排序
	 * @param word_map
	 * @return
	 */
	public static List<Entry<String, Integer>> orderMapByValue(Map<String, Integer> word_map) {
		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(word_map.entrySet());
		Collections.sort(list,new Comparator<Entry<String, Integer>>(){
			public int compare(Entry<String, Integer> arg0,Entry<String, Integer> arg1) {
				return arg1.getValue()-arg0.getValue();
			}
		});
		return list;
	}
	public static boolean is_same_day(String date_time, String weibo_item){
		String last_date = date_time.split(" ")[0];
		String now_date = weibo_item.split(" ")[0];
		return(last_date.equals(now_date));
	}
	public static boolean is_same_week(String week_time, String weibo_item) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date last_date = null,now_date = null;
		try {
			last_date = sdf.parse(week_time);
			now_date = sdf.parse(weibo_item);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long time = Math.abs(last_date.getTime()-now_date.getTime());
		long week = 7*24*60*60*1000;
		return (time<=week);
	}
	/**
	 * 判断一段文字中是否有手机号码
	 * @param str
	 * @return
	 */
	public static boolean HasPhone(String str){
		boolean result = false;
		String regex = "1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}";
		Pattern p=Pattern.compile(regex);        
		Matcher m=p.matcher(str);
		boolean rs = m.find();
		if(rs == true){
			if((str.contains("Q")||str.contains("群")||str.contains("q"))!=true){
				result = true;
			}
		}
		return result;  
	}
	/**
	 * 判断一段文字中是否有URL
	 * @param str
	 * @return
	 */
	public static boolean HasUrl(String urlstr){
		String regex = "(http://|ftp://|https://|www){1}";
		Pattern p=Pattern.compile(regex);        
		Matcher m=p.matcher(urlstr);
		boolean result = m.find();
		return result;
	}
	/**
	 * 判断一段文字中是否有EMAIL
	 * @param str
	 * @return
	 */
	public static boolean HasEmail(String emailstr){
		String regex = "[a-zA-Z0-9][a-zA-Z0-9._-]{2,16}[a-zA-Z0-9]@[a-zA-Z0-9]+.[a-zA-Z0-9]+";
		Pattern p=Pattern.compile(regex);        
		Matcher m=p.matcher(emailstr);
		boolean result = m.find();
		return result;
	}
	/**
	 * 判断一段文字中是否有QQ
	 * @param str
	 * @return
	 */
	public static boolean HasQQ(String qqstr){
		String regex = null;
		regex = "(Q|q|群){1}[^Z0-9]{0,8}[1-9][0-9]{5,9}$";
		Pattern p=Pattern.compile(regex);        
		Matcher m=p.matcher(qqstr);
		boolean result = m.find();
		return result;
	}

	/**
	 * 清除原始微博中包含的转发前内容、以及链接
	 * @param weibo_item
	 * @return
	 */
	public static String clearWeibo(String weibo_item) {
		String[] originalTexts = weibo_item.split("//@");
		if(originalTexts.length==0){return "";}
		String originalText = originalTexts[0].replaceAll("转发微博", "").trim();//得到原创内容
		if(originalText.equals("")){return "";}
		//去掉链接的微博内容
		String regex_url = RegexString.Regex_url;
		Pattern p_link = Pattern.compile(regex_url);
		Matcher m_link = p_link.matcher(originalText);
		String originalText_nolink = m_link.replaceAll("");
		return originalText_nolink;
	}
	/**
	 * 在只包含原创文本的情况，继续去除@xxx字段
	 * @param weibo_item
	 * @return
	 */
	public static String clearWeiboAt(String weiboCon) {
		String regex_at = RegexString.Regex_at;//"[^//]?@(\\w|[\\x{4e00}-\\x{9fa5}]|[-]|[_])*(\\s|[:]|$)";
		return weiboCon.replaceAll(regex_at, "");
	}
	/**
	 * 清除原始微博来源中的杂乱信息
	 * @param weibo_item
	 * @return
	 */
	public static String clearSource(String src) {
		if(src.equals("null")){return null;}
		if(src.equals("")){return src;}
		return src.substring(src.indexOf(">")+1,src.lastIndexOf("<"));
	}
	/**
	 * 获取原始微博来源中来源的url
	 * @param src
	 * @return
	 */
	public static String getSource(String src) {
		if(src.equals("null")){return null;}
		if(src.equals("")){return src;}
		String[] items = src.split("\\\"");
		return items[1];
	}
	public static void mergeDict(Map<String, Integer> dict,Map<String, Integer> dict2) {
		Iterator<Entry<String, Integer>> it = dict2.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Integer> entry = it.next();
			dict.put(entry.getKey(),entry.getValue());
		}
	}
	public static void mergeFeature(Map<Integer, Integer> feature1,Map<Integer, Integer> feature2) {
		Iterator<Entry<Integer, Integer>> it = feature2.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer, Integer> entry = it.next();
			putInMap(feature1, entry.getKey(),entry.getValue());
		}
	}
	//将feature_list中编码相同的term合并起来
	public static Map<Integer, Integer> mergeFeature(List<String> feature_list) {
		Map<Integer,Integer> feature_map = new TreeMap<Integer,Integer>();
		for(String term : feature_list){//term : 675:1	804:1	849:1	
			String[] vertor = term.split("\t");
			for(String v : vertor){
				int index = Integer.parseInt(v.split(":")[0]);
				int count = Integer.parseInt(v.split(":")[1]);
				putInMap(feature_map,index,count);
			}
		}
		return feature_map;
	}

	public static void putInMap( Map<Integer, Float> map,int index,float value) {
		if(map.containsKey(index)){
			map.put(index,map.get(index)+value);
		}else{
			map.put(index,value);
		}
	}

	public static void putInMap( Map<Integer, Integer> map,int index,int value) {
		if(map.containsKey(index)){
			map.put(index,map.get(index)+value);
		}else{
			map.put(index,value);
		}
	}
	public static void putInMap( Map<String, Integer> map,String index,int value) {
		if(map.containsKey(index)){
			map.put(index,map.get(index)+value);
		}else{
			map.put(index,value);
		}
	}
	public static Set<String> MapToSet(Map<String, Integer> map,int threshold) {
		Set<String> item_set = new HashSet<String>();
		Iterator<Entry<String, Integer>> map_it = map.entrySet().iterator();
		while(map_it.hasNext()){
			Entry<String, Integer> entry = map_it.next();
			if(entry.getValue()>threshold){//超过threshold数目，才将该记录添加到Set中
				item_set.add(entry.getKey());
			}
		}
		return item_set;
	}
	public static void SetToMap(Map<String, Integer> map,Set<String> set) {
		for(String item:set){//将f文件中出现的词项集合加入到map中
			if(map.containsKey(item)){
				map.put(item, map.get(item)+1);
			}else{
				map.put(item, 1);
			}
		}
	}
	public static List<String> toNgram(String str, int gram) {
		List<String> str_ngram = new ArrayList<String>();
		for (int i = 0; i < str.length()-gram+1; i++) {
			String ngram = str.substring(i, i+gram);
			str_ngram.add(ngram);
		}
		return str_ngram;
	}

	public static void main(String args[]) {
		toNgram("八月份的前奏~",2);

	}

	public static String listToString(List<String> list, String seperator) {
		StringBuffer sbuffer = new StringBuffer();
		for(String str : list){
			sbuffer.append(str+seperator);
		}
		return sbuffer.toString().trim();
	}

	public static String setToString(Set<String> list, String seperator) {
		StringBuffer sbuffer = new StringBuffer();
		for(String str : list){
			sbuffer.append(str+seperator);
		}
		return sbuffer.toString().trim();
	}

	public static Set<String> getIntersection(HashSet<String> ... sets) {
		int num = sets.length;
		for(int i=0,j=1;i<num-1;i++,j++){
			HashSet<String> new_set = new HashSet<String>();
			for(String item : sets[i]){
				if(sets[j].contains(item)){
					new_set.add(item);
				}
			}
			sets[j] = new_set;
		}
		return sets[num-1];
	}
	public static void fileCopy(File s,String desDir) throws IOException{
		FileInputStream fi = new FileInputStream(s);
		FileChannel in = fi.getChannel();
		File t = new File(desDir+s.getName());
		FileOutputStream fo = new FileOutputStream(t);
		FileChannel out = fo.getChannel();
		in.transferTo(0, in.size(), out);
		fi.close();
		in.close();
		fo.close();
		out.close();
	}

	public static void mapSortByValueInteger(List<String> list,Map<String, Integer> map) {
		List<Entry<String,Integer>> list_tmp = new ArrayList<Entry<String,Integer>>(map.entrySet());
		Collections.sort(list_tmp,new Comparator<Entry<String,Integer>>(){
			public int compare(Entry<String,Integer> arg0,Entry<String,Integer> arg1) {
				double r = arg0.getValue()-arg1.getValue();
				if(r>0)return 1;
				else if(r<0)return -1;
				else return 0;
			}
		});
		for(Entry<String,Integer> entry : list_tmp){
			String item = entry.getKey()+"\t"+entry.getValue();
			list.add(item);
		}
	}
}
