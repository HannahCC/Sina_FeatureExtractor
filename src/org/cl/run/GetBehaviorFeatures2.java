package org.cl.run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

import org.cl.configuration.Config;
import org.cl.service.SaveInfo;
import org.cl.service.Utils;
/**
 * 获取用户的行为特征
 *结果示例：
uid			微博数	每日最大	最小	中值	平均数		每周最大	最小	中值	平均数		最大长度	最小长度	长度中值	平均长度		来源数	来源改变次数
2094390181	49		5		1	1	1.4411764	12		1	4	4.4545455	66		0		13		17.387754	8		21
 * @author Chenli
 *
 */
public class GetBehaviorFeatures2  implements Runnable
{
	private String uid=null;
	int w_num = 0;//微博数量
	int dw_max = 0,dw_min = 100000,dw_median = 0;//每天微博最大、最小、中值、平均数
	int ww_max = 0,ww_min = 100000,ww_median = 0;//每周微博最大、最小、中值、平均数
	int wl_max = 0,wl_min = 100000,wl_median = 0;//微博长度最大、最小、中值、平均值
	float dw_aver = 0,ww_aver = 0,wl_aver = 0;//
	String date_time = "1900-01-01 00:00:00",week_time ="1900-01-01 00:00:00",last_src = "";
	public GetBehaviorFeatures2(String uid)
	{
		this.uid=uid;
	}

	public void run()
	{
		System.out.println("Getting BehaviorFeatures2 of "+uid);
		int src_num = 0,src_change_num = 0;//微博来源数、来源改变次数
		Set<String> src_set = new HashSet<String>();
		List<Integer> dw_list = new ArrayList<Integer>();
		List<Integer> ww_list = new ArrayList<Integer>();
		List<Integer> wl_list = new ArrayList<Integer>();
		String weibo = "";
		try {
			File weibo_file = new File(Config.SAVE_PATH+"\\Weibos\\"+uid+".txt");
			BufferedReader br = new BufferedReader(new FileReader(weibo_file));
			
			int dw = 0,ww = 0;
			long wl_sum=0;
			
			while((weibo=br.readLine())!=null)
			{
				if(weibo.equals(""))continue;
				JSONObject weibo_json = JSONObject.fromObject(weibo).getJSONObject("weibo");
				w_num++;
				wl_sum+=get_wl(wl_list,weibo_json.getString("text"));
				dw = get_dw(dw_list,weibo_json.getString("createdAt"),dw);
				ww = get_ww(ww_list,weibo_json.getString("createdAt"),ww);
				String src = Utils.clearSource(weibo_json.getString("source"));
				if(!is_same_src(last_src,src,src_set)){src_change_num++;last_src = src;}
			}
			dw_list.add(dw);
			ww_list.add(ww);
			get_statistics_dw(dw_list);
			get_statistics_ww(ww_list);
			get_statistics_wl(wl_list,wl_sum);
			src_num = src_set.size();
			br.close();
		} catch (Exception e) {
			System.out.println(uid);
			System.out.println(weibo);
			e.printStackTrace();
		}
		int s = -1;
		String bf = uid + "\t" +(s+1)+":"+ w_num + "\t" +(s+2)+":" + dw_max + "\t" +(s+3)+":" + dw_min + "\t" +(s+4)+":" + dw_median + "\t" +(s+5)+":" + dw_aver
				+ "\t" +(s+6)+":" + ww_max + "\t" +(s+7)+":" +ww_min+ "\t" +(s+8)+":" +ww_median + "\t" +(s+9)+":" + ww_aver
				+ "\t" +(s+10)+":" + wl_max + "\t" +(s+11)+":" +wl_min+ "\t" +(s+12)+":" +wl_median + "\t" +(s+13)+":" + wl_aver
				+ "\t" +(s+14)+":" + src_num + "\t" +(s+15)+":" + src_change_num;
		try {
			SaveInfo.saveString("\\Feature_Behaviour\\Behaviour2_feature.txt", bf,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void get_statistics_wl(List<Integer> list, long sum) {
		Collections.sort(list);
		int num = list.size();
		wl_max = list.get(num-1);
		wl_min = list.get(0);
		wl_median = list.get(num/2);
		wl_aver = sum/(float)num;
	}
	private void get_statistics_dw(List<Integer> dw_list) {
		Collections.sort(dw_list);
		int dw_num = dw_list.size();
		dw_max = dw_list.get(dw_num-1);
		dw_min = dw_list.get(0);
		dw_median = dw_list.get(dw_num/2);
		dw_aver = w_num/(float)dw_num;
	}
	private void get_statistics_ww(List<Integer> ww_list) {
		Collections.sort(ww_list);
		int ww_num = ww_list.size();
		ww_max = ww_list.get(ww_num-1);
		ww_min = ww_list.get(0);
		ww_median = ww_list.get(ww_num/2);
		ww_aver = w_num/(float)ww_num;
	}
	private int get_ww(List<Integer> ww_list, String time, int ww) {
		if(Utils.is_same_week(week_time,time)){ww++;}
		else{//如果是不同一星期
			if(ww>0){ww_list.add(ww);}
			week_time = time;
			ww = 1;
		}
		return ww;
	}
	private int get_dw(List<Integer> dw_list, String time, int dw) {
		if(Utils.is_same_day(date_time,time)){dw++;}
		else{//如果是不同一天
			if(dw>0){dw_list.add(dw);}
			date_time = time;
			dw = 1;
		}
		return dw;
	}
	private int get_wl(List<Integer> wl_list,String weibo) {
		String cleared_weibo = Utils.clearWeibo(weibo);
		int wl = cleared_weibo.length();
		wl_list.add(wl);
		return wl;
	}
	private boolean is_same_src(String last_src, String src, Set<String> src_set) {
		src_set.add(src);
		return last_src.equals(src);
	}


}
