package org.cl.main.feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.cl.configuration.Config;
import org.cl.service.GetInfo;
import org.cl.service.SaveInfo;
/**
 * -NeibourRate_feature.txt
		1812871717	1:1	2:1	3:0	4:1	5:1	6:0	7:0	8:0	9:1	10:1
		1.用户的关注数是否>平均数
		2.用户的粉丝数是否>平均数
		3.用户关注中达人用户数是否>平均数
		4.用户关注中黄V用户数是否>平均数
		5.用户关注中蓝V用户数是否>平均数
		6.用户关注中已不存在用户数是否<平均数
		7.用户粉丝中达人用户数是否>平均数
		8.用户粉丝中黄V用户数是否>平均数
		9.用户粉丝中蓝V用户数是否>平均数
		10.用户粉丝中已不存在用户数是否<平均数
		
		Sina_NLPIRandTHUext1000_Mute_GenderPre:
		Fri:DaRenCountAvg = 0.17303404,YellowVCountAvg = 0.21267685, BlueVCountAvg = 0.13318641, NotExsitCountAvg = 0.033675738
		Fol:DaRenCountAvg = 0.10465403,YellowVCountAvg = 0.018508391, BlueVCountAvg = 0.0072480114, NotExsitCountAvg = 0.39210814
 * @author Hannah
 *
 */
public class Main_GetUserInfoFeature4 {
	public static void main(String args[]) throws IOException, InterruptedException
	{	
		Map<String,Integer[]> uid_profile_map = new HashMap<String,Integer[]>();
		Map<String,Integer> uid_verifyType_map = new HashMap<String,Integer>();
		Set<String> uid_notExist = new HashSet<String>();
		GetInfo.getSet("Config\\UserNotExist.txt", uid_notExist);
		getProfileFeature(uid_profile_map,"UserInfo0.txt"/*,"UserInfoOfEnterprise0.txt"*/);
		getVerifyType(uid_verifyType_map,"UserInfo0.txt"/*,"UserInfoOfEnterprise0.txt"*/,"UserInfo1.txt","UserInfoOfEnterprise1.txt");
		getFriNumFeature(2,uid_profile_map,uid_verifyType_map,uid_notExist,"UidInfo_friends0.txt");
		getFriNumFeature(6,uid_profile_map,uid_verifyType_map,uid_notExist,"UidInfo_follows0.txt");

		SaveInfo.mkdir("Feature_UserInfo");
		SaveInfo.saveArrMap("Feature_UserInfo\\NeibourRate_feature.txt", uid_profile_map, false);
	}

	private static void getFriNumFeature(int s, Map<String, Integer[]> uid_profile_map, Map<String,Integer> uid_verifyType_map, 
			Set<String> uid_notExist, String ... srcfiles) throws IOException {
		int number = 0;
		float DaRenCountAvg = 0, YellowVCountAvg = 0, BlueVCountAvg = 0, NotExsitCountAvg = 0;
		Map<String,Float[]> uid_fri_rate_map = new HashMap<String,Float[]>();
		for(String srcfile : srcfiles){
			File r=new File(Config.SAVE_PATH+srcfile);
			BufferedReader br=new BufferedReader(new FileReader(r));
			String line="";
			while((line=br.readLine())!=null)
			{
				JSONObject json = JSONObject.fromObject(line);
				String id = json.getString("id");
				Integer[] profile_feature = uid_profile_map.get(id);
				Float[] uid_fri_rate = {0f,0f,0f,0f};
				@SuppressWarnings("unchecked")
				List<String> uids = (List<String>) json.get("uids");
				if(uids.size()>0){
					for(String uid : uids){
						if(uid_verifyType_map.containsKey(uid)){
							int verifyType = uid_verifyType_map.get(uid);
							if(verifyType>=220){profile_feature[s]+=1;}
							else if(verifyType==0){profile_feature[s+1]+=1;}
							else if(verifyType>0&&verifyType<9){profile_feature[s+2]+=1;}
						}
						if(uid_notExist.contains(uid)){profile_feature[s+3]+=1;}
					}
					float size = uids.size();
					uid_fri_rate[0] = profile_feature[s]/size;
					uid_fri_rate[1] = profile_feature[s+1]/size;
					uid_fri_rate[2] = profile_feature[s+2]/size;
					uid_fri_rate[3] = profile_feature[s+3]/size;
				}
				uid_fri_rate_map.put(id, uid_fri_rate);
				DaRenCountAvg = (DaRenCountAvg*number + uid_fri_rate[0])/(number+1f);
				YellowVCountAvg =(YellowVCountAvg*number + uid_fri_rate[1])/(number+1f);
				BlueVCountAvg = (BlueVCountAvg*number + uid_fri_rate[2])/(number+1f);
				NotExsitCountAvg = (NotExsitCountAvg*number + uid_fri_rate[3])/(number+1f);
				number++;
			}
			br.close();
		}
		System.out.println("DaRenCountAvg = "+DaRenCountAvg+",YellowVCountAvg = "+YellowVCountAvg+", BlueVCountAvg = "+BlueVCountAvg+", NotExsitCountAvg = "+NotExsitCountAvg);
		for(Entry<String, Integer[]> entry : uid_profile_map.entrySet()){
			Float[] uid_fri_rate = uid_fri_rate_map.get(entry.getKey());
			Integer[] profile_dict = entry.getValue();
			profile_dict[s] = uid_fri_rate[0]>DaRenCountAvg?1:0;
			profile_dict[s+1] = uid_fri_rate[1]>YellowVCountAvg?1:0;
			profile_dict[s+2] = uid_fri_rate[2]>BlueVCountAvg?1:0;
			profile_dict[s+3] = uid_fri_rate[3]<NotExsitCountAvg?1:0;
		}

	}

	private static void getProfileFeature(Map<String,Integer[]> uid_profile_map, String ... srcfiles) throws IOException {
		int number = 0;
		float friendsCountAvg = 0,followersCountAvg = 0;
		for(String srcfile : srcfiles){
			File r=new File(Config.SAVE_PATH+srcfile);
			BufferedReader br=new BufferedReader(new FileReader(r));
			String line="";
			while((line=br.readLine())!=null)
			{
				if(!(line.equals(""))){
					Integer[] profile_feature = {0,0,0,0,0,0,0,0,0,0};
					JSONObject json = JSONObject.fromObject(line);
					String uid = json.getString("id");
					int friendsCount = json.getInt("friendsCount");
					profile_feature[0] = friendsCount;
					friendsCountAvg = (friendsCountAvg*number + friendsCount)/(number+1f);

					int followersCount = json.getInt("followersCount");
					profile_feature[1] = followersCount;
					followersCountAvg = (followersCountAvg*number + followersCount)/(number+1f);

					number++;
					uid_profile_map.put(uid, profile_feature);
				}
			}
			br.close();
		}
		for(Entry<String, Integer[]> entry : uid_profile_map.entrySet()){
			Integer[] profile_dict = entry.getValue();
			profile_dict[0] = profile_dict[0]>friendsCountAvg?1:0;
			profile_dict[1] = profile_dict[1]>followersCountAvg?1:0;
		}
	}

	private static void getVerifyType(Map<String,Integer> uid_verifyType_map, String ... srcfiles) throws IOException {
		for(String srcfile : srcfiles){
			File r=new File(Config.SAVE_PATH+srcfile);
			BufferedReader br=new BufferedReader(new FileReader(r));
			String line="";
			while((line=br.readLine())!=null)
			{
				if(!(line.equals(""))){
					JSONObject json = JSONObject.fromObject(line);
					String uid = json.getString("id");
					int verifiedType = json.getInt("verifiedType");
					uid_verifyType_map.put(uid, verifiedType);
				}
			}
			br.close();
		}
	}
}
