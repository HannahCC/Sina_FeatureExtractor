package org.cl.run;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.cl.configuration.Config;
import org.cl.service.GetInfo;
/**
 * 将所有id1和id2组合读入内存
	逐个读取atData的文件，
	判断是否同时存在id1和id2（先逐个判断当前id是否存在与id1list<id1list可能重复>,若当前id存在，则判断当前id列表是否存在对应的id2,如果存在将该组合剔除，加入另一队列

 * @author Administrator
 *
 */
public class GetRelationFeature2 {
	public static void func_GetRelationFeature2() throws IOException, InterruptedException
	{	
		File fw2 = new File(Config.SAVE_PATH+"/Feature_Relation/Mutual_feature_2.txt");
		BufferedWriter w_feature2 = new BufferedWriter(new FileWriter(fw2));
		ArrayList<String> uid1_list = new ArrayList<String>();
		ArrayList<String> uid2_list = new ArrayList<String>();
		ArrayList<Integer> feature_list = new ArrayList<Integer>();
		//ArrayList<RelationFeature> Feature_list = new ArrayList<RelationFeature>();
		GetInfo.getUid1_2(uid1_list, uid2_list);
		for(int i=0;i<uid1_list.size();i++){
			feature_list.add(0);
		}
		File flist = new File(Config.SAVE_PATH+"/AtData");
		String[] atdata_list = flist.list();
		for(String uid :atdata_list){
			File fr = new File(Config.SAVE_PATH+"/AtData/"+uid);
			BufferedReader r1 = new BufferedReader(new FileReader(fr));
			ArrayList<String> uid_list = new ArrayList<String>();
			String tmp ="";
			while((tmp = r1.readLine())!=null){
				String[] tmps = tmp.split("\\t");
				uid_list.add(tmps[0]);
			}
			r1.close();
			int[] index = null;
			for(String u_id:uid_list){
				if((index = IndexOf(uid1_list,u_id))!=null){//如果uid1_list中有该u_id,则检验与只对应的uid2是否也存在于当前uid_list列表
					for(int index1:index){
						if(uid.equals(u_id+".txt")){continue;}
						if(uid_list.contains(uid2_list.get(index1))){
							if(uid.equals(uid2_list.get(index1)+".txt")){continue;}
							feature_list.set(index1,1);
						}
					}

				}
			}
			System.out.println("GetRelationFeature2:"+uid);
		}
		for(int feature:feature_list){
			String info = "7:"+feature + "\r\n";
			w_feature2.write(info);
		}
		w_feature2.flush();
		w_feature2.close();
	}
	private static int[] IndexOf(ArrayList<String> list,String con){
		int i = 0;
		ArrayList<Integer> tmp_list = new ArrayList<Integer>();
		for(String tmp : list){
			if(tmp.equals(con)){tmp_list.add(i);}
			i++;
		}
		int size = tmp_list.size();
		if(size>0){
			int[] rs = new int[size];
			for(int j=0;j<size;j++){
				rs[j] = tmp_list.get(j);
			}
			return rs;
		}else{
			return null;
		}

	}
}
