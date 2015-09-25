package org.cl.run;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.cl.configuration.Config;
import org.cl.model.AtUser;
import org.cl.service.GetInfo;
/**
 * 只针对原始ID列表中每组之间用空格隔开的情况，且atData数据已准备好
 * 将所有id1和id2组合读入内存
 * 将第n组的id1和id2的atData读入内存，
 * 判断id1是否在id2的前3个
 * 判断id2是否在id1的前3个
 * 判断id1和id2的atId中是否有共同的id（用循环判断。有则break）
 * @author Administrator
 *
 */
public class GetRelationFeature1 {
	public static void func_GetRelationFeature1() throws IOException, InterruptedException
	{	
		File fw1 = new File(Config.SAVE_PATH+"/Feature_Relation/Mutual_feature_1.txt");
		BufferedWriter w_feature1 = new BufferedWriter(new FileWriter(fw1));
		ArrayList<String> uid1_list = new ArrayList<String>();
		ArrayList<String> uid2_list = new ArrayList<String>();
		//ArrayList<RelationFeature> Feature_list = new ArrayList<RelationFeature>();
		GetInfo.getUid1_2(uid1_list, uid2_list);
		int size1 = uid1_list.size();
		for(int m=0;m<size1;m++){
			String uid1 = uid1_list.get(m);
			String uid2 = uid2_list.get(m);
			ArrayList<AtUser> u1_list = new ArrayList<AtUser>();
			ArrayList<AtUser> u2_list = new ArrayList<AtUser>();
			int feature1_1 = 0;//判断id1是否在于id2的atId前1个   1,0分别代表true,false
			int feature1_2 = 0;//判断id1是否在于id2的atId前3个
			int feature1_3 = 0;//判断id1是否在于id2的atId前10个
			int feature2_1 = 0;//判断id2是否在于id1的atId前1个
			int feature2_2 = 0;//判断id2是否在于id1的atId前2个
			int feature2_3 = 0;//判断id2是否在于id1的atId前10个
			int feature3 = 0;//判断id1和id2的atId中是否有共同的id（用循环判断。有则break）
			//获取id1和id2的atData读入内存，
			try {
				File f1 = new File(Config.SAVE_PATH+"/AtData/"+uid1+".txt");
				if(f1.exists()){
					BufferedReader r1 = new BufferedReader(new FileReader(f1));
					String tmps = " "; 
					while((tmps = r1.readLine())!=null){
						String[] tmp = tmps.split("\\t");
						AtUser atdata = new AtUser();
						atdata.setUid(tmp[0]);
						atdata.setAtNum(Integer.parseInt(tmp[1]));
						atdata.setReNum(Integer.parseInt(tmp[2]));
						atdata.setUname(tmp[3]);
						u1_list.add(atdata);
					}
					r1.close();
					for(int i=0;i<u1_list.size();i++){
						if(u1_list.get(i).getUid().equals(uid2)){
							if(i==0||u1_list.get(i).getAtNum()>=u1_list.get(0).getAtNum()){feature2_1=1;feature2_2=1;feature2_3 = 1;}
							else if(i<=2||u1_list.get(i).getAtNum()>=u1_list.get(2).getAtNum()){feature2_2=1;feature2_3 = 1;}
							else if(i<=9||u1_list.get(i).getAtNum()>=u1_list.get(9).getAtNum()){feature2_3=1;}
							break;
						}
					}
				}
				File f2 = new File(Config.SAVE_PATH+"/AtData/"+uid2+".txt");
				if(f2.exists()){
					BufferedReader r2 = new BufferedReader(new FileReader(f2));
					String tmps = ""; 
					while((tmps = r2.readLine())!=null){
						String[] tmp = tmps.split("\\t");
						AtUser atdata = new AtUser();
						atdata.setUid(tmp[0]);
						atdata.setAtNum(Integer.parseInt(tmp[1]));
						atdata.setReNum(Integer.parseInt(tmp[2]));
						atdata.setUname(tmp[3]);
						u2_list.add(atdata);
					}
					r2.close();
					for(int i=0;i<u2_list.size();i++){
						if(u2_list.get(i).getUid().equals(uid1)){
							if(i==0||u2_list.get(i).getAtNum()>=u2_list.get(0).getAtNum()){feature1_1=1;feature1_2=1;feature1_3 =1;}
							else if(i<=2||u2_list.get(i).getAtNum()>=u2_list.get(2).getAtNum()){feature1_2=1;feature1_3 =1;}
							else if(i<=9||u2_list.get(i).getAtNum()>=u2_list.get(9).getAtNum()){feature1_3 =1;}
							break;
						}
					}
				}

				if(f1.exists()&&f2.exists()){
					for(AtUser user1:u1_list){
						for(AtUser user2:u2_list){
							if(user1.getUid().equals(user2.getUid())&&!user1.getUid().equals("failtoget")){
								feature3 = 1;
								break;
							}
						}
						if(feature3==1)break;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String con = uid1+"\t"+uid2+"\t0:"+feature1_1+"\t1:"+feature1_2+"\t2:"+feature1_3+"\t3:"+feature2_1+"\t4:"+feature2_2+"\t5:"+feature2_3+"\t6:"+feature3+"\r\n";
			w_feature1.write(con);
			System.out.println("GetRelationFeature1:"+uid1+"\t"+uid2);
		}
		w_feature1.flush();
		w_feature1.close();
	}
}
