package org.cl.run;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cl.service.GetInfo;
import org.cl.service.SaveInfo;
import org.cl.service.Utils;

public class FeaturesToUserLevel implements Runnable {
	private String uid=null;
	private String feature=null;
	private String attr=null;
	public FeaturesToUserLevel(String uid, String feature, String attr) {
		this.uid = uid ;
		this.feature = feature;
		this.attr = attr;
	}

	public void run() {
		System.out.println("Making "+attr+" Features To UserLevel --- "+uid);
		try {
			List<String> feature_list = new ArrayList<String>();
			GetInfo.getList(feature+"\\"+attr+"\\"+uid+".txt",feature_list,false);
			Map<Integer,Integer> feature_map = Utils.mergeFeature(feature_list);//将同一编号的值相加,每个元素为：编号:次数+"\t",从0开始编码,返回总次数
			SaveInfo.saveMap(feature+"\\"+attr+"_feature.txt",uid,feature_map,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
