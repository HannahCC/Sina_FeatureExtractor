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

import net.sf.json.JSONObject;

import org.cl.configuration.Config;
import org.cl.service.GetInfo;
import org.cl.service.SaveInfo;
/**
 * -NeibourNum_feature.txt
		1812871717	1:324	2:432	3:123	4:32	5:34	6:34	7:43	8:45	9:76	
		1.用户的关注数
		2.用户的粉丝数
		3.用户关注中达人用户数
		4.用户关注中黄V用户数
		5.用户关注中蓝V用户数
		6.用户关注中已不存在用户数
		7.用户粉丝中达人用户数
		8.用户粉丝中黄V用户数
		9.用户粉丝中蓝V用户数
		10.用户粉丝中已不存在用户数
 * @author Hannah
 *
 */
public class Main_GetUserInfoFeature3 {
	public static void main(String args[]) throws IOException, InterruptedException
	{	
		Map<String,Integer[]> uid_profile_map = new HashMap<String,Integer[]>();
		Map<String,Integer> uid_verifyType_map = new HashMap<String,Integer>();
		Set<String> uid_notExist = new HashSet<String>();
		GetInfo.getSet("Config\\UserNotExist.txt", uid_notExist);
		SaveInfo.mkdir("Feature_UserInfo");
		getProfileFeature(uid_profile_map,"UserInfo0.txt"/*,"UserInfoOfEnterprise0.txt"*/);
		getVerifyType(uid_verifyType_map,"UserInfo0.txt"/*,"UserInfoOfEnterprise0.txt"*/,"UserInfo1.txt","UserInfoOfEnterprise1.txt");
		getFriNumFeature(2,uid_profile_map,uid_verifyType_map,uid_notExist,"UidInfo_friends0.txt");
		getFriNumFeature(6,uid_profile_map,uid_verifyType_map,uid_notExist,"UidInfo_follows0.txt");
		SaveInfo.saveArrMap("Feature_UserInfo\\NeibourNum_feature.txt", uid_profile_map, false);
	}

	private static void getFriNumFeature(int s, Map<String, Integer[]> uid_profile_map, Map<String,Integer> uid_verifyType_map, 
			Set<String> uid_notExist, String ... srcfiles) throws IOException {
		for(String srcfile : srcfiles){
			File r=new File(Config.SAVE_PATH+srcfile);
			BufferedReader br=new BufferedReader(new FileReader(r));
			String line="";
			while((line=br.readLine())!=null)
			{
				JSONObject json = JSONObject.fromObject(line);
				String id = json.getString("id");
				Integer[] profile_feature = uid_profile_map.get(id);
				@SuppressWarnings("unchecked")
				List<String> uids = (List<String>) json.get("uids");
				for(String uid : uids){
					if(uid_verifyType_map.containsKey(uid)){
						int verifyType = uid_verifyType_map.get(uid);
						if(verifyType>=220){profile_feature[s]+=1;}
						else if(verifyType==0){profile_feature[s+1]+=1;}
						else if(verifyType>0&&verifyType<9){profile_feature[s+2]+=1;}
					}
					if(uid_notExist.contains(uid)){profile_feature[s+3]+=1;}
				}
			}
			br.close();
		}
	}

	private static void getProfileFeature(Map<String,Integer[]> uid_profile_map, String ... srcfiles) throws IOException {
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
					int followersCount = json.getInt("followersCount");
					profile_feature[1] = followersCount;
					uid_profile_map.put(uid, profile_feature);
				}
			}
			br.close();
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
