package org.cl.main.feature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.cl.configuration.Config;
import org.cl.service.SaveInfo;
/**
 * -Profile_feature.txt
		1812871717	1:1	2:0	3:1	4:0	5:1	6:0	7:1	8:4	9:1	10:0
		-内容说明：
		1.用户是否填写个人简介
		2.用户是否有标签
		3.用户是否填写个性域名
		4.用户是否达人或认证
		5.用户个人简介长度是否>平均数
		6.用户标签个数是否>平均数
		7.用户昵称长度是否>平均数
		8.用户微博数是否>平均数
		9.用户收藏数是否>平均数
		10.用户使用年限>平均数
 * @author Hannah
 */
public class Main_GetUserInfoFeature2 {
	public static void main(String args[]) throws IOException, InterruptedException
	{	
		SaveInfo.mkdir("Feature_UserInfo");
		getProfileFeature("Feature_UserInfo\\Profile_feature.txt","UserInfo0.txt"/*,"UserInfoOfEnterprise0.txt","UserInfo1.txt","UserInfoOfEnterprise1.txt"*/);
		//Sina_NLPIRandTHUext1000_Mute_GenderPre 0  :DescAvg = 15.016951,TagAvg = 4.241426, nameAvg = 7.19, weiboAvg = 1.5521432, favoAvg = 201.49991, ageAvg = 4.1321445
		//Sina_NLPIRandTHUext1000_Mute_GenderPre 0+1:DescAvg = 27.608086,TagAvg = 4.742606, nameAvg = 7.3502035, weiboAvg = 2228.9568, favoAvg = 145.62927, ageAvg = 3.3635209
	}

	private static void getProfileFeature(String resfile, String ... srcfiles) throws IOException {
		Map<String,Integer[]> uid_profile_map = new HashMap<String,Integer[]>();
		int number = 0;
		float DescAvg = 0,TagAvg = 0, nameAvg = 0, weiboAvg = 0, favoAvg = 0, ageAvg = 0;

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
					String desc = json.getString("description");
					if(!"".equals(desc)){
						profile_feature[0] = 1;
						profile_feature[4] = desc.length();
						DescAvg = (float) ((DescAvg*number + desc.length())/(number+1.0));
					}
					JSONObject tagJson = json.getJSONObject("tags");
					@SuppressWarnings("unchecked")
					List<String> tags = (List<String>) tagJson.get("tags");
					if(tags.size()>0){
						profile_feature[1] = 1;
						profile_feature[5] = tags.size();
						TagAvg = (float) ((TagAvg*number + tags.size())/(number+1.0));
					}
					String domain = json.getString("userDomain");
					if(!"".equals(domain)){
						profile_feature[2] = 1;
					}
					int verifyType = json.getInt("verifiedType");
					if(verifyType==0||verifyType>=200){
						profile_feature[3] = 1;
					}
					String screenName = json.getString("screenName");
					nameAvg = (float) ((nameAvg*number + screenName.length())/(number+1.0));
					profile_feature[6] = screenName.length();

					int weibonum = json.getInt("statusesCount");
					weiboAvg = (float) ((weiboAvg*number + weibonum)/(number+1.0));
					profile_feature[7] = weibonum;

					int favonum = json.getInt("favouritesCount");
					favoAvg = (float) ((favoAvg*number + favonum)/(number+1.0));
					profile_feature[8] = favonum;

					String createdAt = json.getString("createdAt");
					int age = 2015 - Integer.parseInt(createdAt.split("-")[0]);
					ageAvg = (float) ((ageAvg*number + age)/(number+1.0));
					profile_feature[9] = age;

					number++;
					uid_profile_map.put(uid, profile_feature);

				}

			}
			br.close();
		}
		System.out.println("DescAvg = "+DescAvg+",TagAvg = "+TagAvg+", nameAvg = "+nameAvg+", weiboAvg = "+weiboAvg+", favoAvg = "+favoAvg+", ageAvg = "+ageAvg);
		File f = new File(Config.SAVE_PATH+resfile);
		BufferedWriter w = new BufferedWriter(new FileWriter(f,false));
		for(Entry<String, Integer[]> entry : uid_profile_map.entrySet()){
			String uid = entry.getKey();
			Integer[] profile_dict = entry.getValue();
			profile_dict[4] = profile_dict[4]>DescAvg?1:0;
			profile_dict[5] = profile_dict[5]>TagAvg?1:0;
			profile_dict[6] = profile_dict[6]>nameAvg?1:0;
			profile_dict[7] = profile_dict[7]>weiboAvg?1:0;
			profile_dict[8] = profile_dict[8]>favoAvg?1:0;
			profile_dict[9] = profile_dict[9]>ageAvg?1:0;
			w.write(uid+"\t");
			for(int i=0;i<10;i++){
				w.write((i+1)+":"+profile_dict[i]+"\t");
			}
			w.write("\r\n");
		}
		w.flush();
		w.close();
	}
}
