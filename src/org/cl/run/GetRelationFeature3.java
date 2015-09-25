package org.cl.run;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cl.configuration.Config;
import org.cl.service.GetInfo;
/**
 * 将所有id1和id2组合读入内存
 * 将id1和id2的粉丝、关注存入内存
 * 判断是否有共同好友
 * 判断id1和id2共同的粉丝数  fensiNum
 * 逐个判断id1和id2的共同粉丝是否为大V fensiNumV
 * 判断id1和id2共同的关注数  friNum
 * 逐个判断id1和id2的共同关注是否为大V friNumV
 * 用户1和用户2是原始用户，共同@或者转发等等的人是拓展用户
 * @author Administrator
 *
 */
public class GetRelationFeature3 {
	public static void func_GetRelationFeature3() throws IOException, InterruptedException
	{	
		File fw3 = new File(Config.SAVE_PATH+"/Feature_Relation/Mutual_feature_3.txt");
		BufferedWriter w_feature3 = new BufferedWriter(new FileWriter(fw3));
		ArrayList<String> uid1_list = new ArrayList<String>();
		ArrayList<String> uid2_list = new ArrayList<String>();
		//ArrayList<RelationFeature> Feature_list = new ArrayList<RelationFeature>();
		GetInfo.getUid1_2(uid1_list, uid2_list);
		int size = uid1_list.size();
		
		for(int m=0;m<size;m++){
			String uid1 = uid1_list.get(m);
			String uid2 = uid2_list.get(m);
			int fensiNum = 0,fensiNumV=0,friNum=0,friNumV=0;
			int noCommonFri = 0;
			Set<String> uid1_fensi = new HashSet<String>();
			Set<String> uid1_fri = new HashSet<String>();
			Set<String> uid2_fensi = new HashSet<String>();
			Set<String> uid2_fri = new HashSet<String>();
			GetInfo.getRelUidSet(uid1,uid1_fensi,"UidInfo_follows0.txt");
			GetInfo.getRelUidSet(uid1,uid1_fri,"UidInfo_friends0.txt");
			GetInfo.getRelUidSet(uid2,uid2_fensi,"UidInfo_follows0.txt");
			GetInfo.getRelUidSet(uid2,uid2_fri,"UidInfo_friends0.txt");
			
			//获取所有爬取到的大V的Id
			List<String> v_uid = new ArrayList<String>();
			GetInfo.getList("/Config/YellowV_ID.txt",v_uid,true);
			
			//判断是否有共同粉丝,以及其中大V的数量
			if(uid1_fensi.size()>0&&uid2_fensi.size()>0){
				for(String uid:uid1_fensi){
					if(uid2_fensi.contains(uid)){
						if(v_uid.contains(uid)){fensiNumV++;}
						else{fensiNum++;}
					}
				}
			}
			//判断是否有共同关注,以及其中大V的数量
			if(uid1_fri.size()>0&&uid2_fri.size()>0){
				for(String uid:uid1_fri){
					if(uid2_fri.contains(uid)){
						if(v_uid.contains(uid)){friNumV++;}
						else{friNum++;}
					}
				}
			}
			if(fensiNum>0||friNum>0||fensiNumV>0||friNumV>0){noCommonFri = 1;}
			String con = "8:"+noCommonFri+"\t9:"+fensiNum+"\t10:"+fensiNumV+"\t11:"+friNum+"\t12:"+friNumV+"\r\n";
			w_feature3.write(con);
			System.out.println("GetRelationFeature3:"+uid1+"\t"+uid2);
		}
		w_feature3.flush();
		w_feature3.close();
	}

	

}
