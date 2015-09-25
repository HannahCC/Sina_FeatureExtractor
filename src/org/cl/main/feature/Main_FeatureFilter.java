package org.cl.main.feature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.cl.configuration.Config;
import org.cl.service.GetInfo;


public class Main_FeatureFilter {

	public static void main(String args[]) throws IOException {
		Set<String> id_set = new HashSet<String>();
		GetInfo.getSet("ExpandID0.txt", id_set);
/*
		featureFilter(id_set,"Feature_UserInfo\\Tag_AvgVecIn18w.txt","Feature_UserInfo\\Tag_AvgVecIn18w_feature.txt");
		featureFilter(id_set,"Feature_UserInfo\\Tag_ConcVecIn18w.txt","Feature_UserInfo\\Tag_ConcVecIn18w_feature.txt");
		featureFilter(id_set,"Feature_UserInfo\\Description_AvgVecIn18w.txt","Feature_UserInfo\\Description_AvgVecIn18w_feature.txt");
		featureFilter(id_set,"Feature_UserInfo\\Description_ConcVecIn18w.txt","Feature_UserInfo\\Description_ConcVecIn18w_feature.txt");
		*/
		/*
		featureFilter(id_set,"Feature_Relation\\Line_sample10000_d128\\vec_1st.txt","Feature_Relation\\Line_sample10000_d128\\vec_1st_feature.txt");
		featureFilter(id_set,"Feature_Relation\\Line_sample10000_d128\\vec_2nd.txt","Feature_Relation\\Line_sample10000_d128\\vec_2nd_feature.txt");
		featureFilter(id_set,"Feature_Relation\\Line_sample10000_d128\\vec_all.txt","Feature_Relation\\line_vec_all_feature.txt");
		*/
		//mute_mute_fake
		/*
		featureFilter(id_set,"Feature_Relation\\Line8_Desc_Tag_Conc\\vec_1st.txt","Feature_Relation\\line8_Desc_Tag_Conc\\vec_1st_feature.txt");
		featureFilter(id_set,"Feature_Relation\\Line8_Desc_Tag_Conc\\vec_2nd.txt","Feature_Relation\\line8_Desc_Tag_Conc\\vec_2nd_feature.txt");
		featureFilter(id_set,"Feature_Relation\\Line8_Desc_Tag_Conc\\vec_all.txt","Feature_Relation\\line8_Desc_Tag_Conc_18w_vec_all_feature.txt");
		*/
		/*featureFilter(id_set,"Feature_Relation\\Line1_Desc_Tag_Avg_18w_sample10000_d128\\vec_1st.txt","Feature_Relation\\Line1_Desc_Tag_Avg_18w_sample10000_d128\\vec_1st_feature.txt");
		featureFilter(id_set,"Feature_Relation\\Line1_Desc_Tag_Avg_18w_sample10000_d128\\vec_2nd.txt","Feature_Relation\\Line1_Desc_Tag_Avg_18w_sample10000_d128\\vec_2nd_feature.txt");
		featureFilter(id_set,"Feature_Relation\\Line1_Desc_Tag_Avg_18w_sample10000_d128\\vec_all.txt","Feature_Relation\\Line1_Desc_Tag_Avg_18w_vec_all_feature.txt");
		
		featureFilter(id_set,"Feature_Relation\\Line2_Desc_Tag_Avg_18w_sample10000_d128\\vec_1st.txt","Feature_Relation\\Line2_Desc_Tag_Avg_18w_sample10000_d128\\vec_1st_feature.txt");
		featureFilter(id_set,"Feature_Relation\\Line2_Desc_Tag_Avg_18w_sample10000_d128\\vec_2nd.txt","Feature_Relation\\Line2_Desc_Tag_Avg_18w_sample10000_d128\\vec_2nd_feature.txt");
		featureFilter(id_set,"Feature_Relation\\Line2_Desc_Tag_Avg_18w_sample10000_d128\\vec_all.txt","Feature_Relation\\Line2_Desc_Tag_Avg_18w_vec_all_feature.txt");
		
		featureFilter(id_set,"Feature_Relation\\Line3_Desc_Tag_Avg_18w_sample10000_d128\\vec_1st.txt","Feature_Relation\\Line3_Desc_Tag_Avg_18w_sample10000_d128\\vec_1st_feature.txt");
		featureFilter(id_set,"Feature_Relation\\Line3_Desc_Tag_Avg_18w_sample10000_d128\\vec_2nd.txt","Feature_Relation\\Line3_Desc_Tag_Avg_18w_sample10000_d128\\vec_2nd_feature.txt");
		featureFilter(id_set,"Feature_Relation\\Line3_Desc_Tag_Avg_18w_sample10000_d128\\vec_all.txt","Feature_Relation\\Line3_Desc_Tag_Avg_18w_vec_all_feature.txt");
		
		featureFilter(id_set,"Feature_Relation\\Line7_Desc_Tag_Avg_18w_sample10000_d128\\vec_1st.txt","Feature_Relation\\line7_Desc_Tag_Avg_18w_sample10000_d128\\vec_1st_feature.txt");
		featureFilter(id_set,"Feature_Relation\\Line7_Desc_Tag_Avg_18w_sample10000_d128\\vec_2nd.txt","Feature_Relation\\line7_Desc_Tag_Avg_18w_sample10000_d128\\vec_2nd_feature.txt");
		featureFilter(id_set,"Feature_Relation\\Line7_Desc_Tag_Avg_18w_sample10000_d128\\vec_all.txt","Feature_Relation\\line7_Desc_Tag_Avg_18w_vec_all_feature.txt");
		
		featureFilter(id_set,"Feature_Relation\\Line8_Desc_Tag_Avg_18w_sample10000_d128\\vec_1st.txt","Feature_Relation\\line8_Desc_Tag_Avg_18w_sample10000_d128\\vec_1st_feature.txt");
		featureFilter(id_set,"Feature_Relation\\Line8_Desc_Tag_Avg_18w_sample10000_d128\\vec_2nd.txt","Feature_Relation\\line8_Desc_Tag_Avg_18w_sample10000_d128\\vec_2nd_feature.txt");
		featureFilter(id_set,"Feature_Relation\\Line8_Desc_Tag_Avg_18w_sample10000_d128\\vec_all.txt","Feature_Relation\\line8_Desc_Tag_Avg_18w_vec_all_feature.txt");
		
		*/

		featureFilter(id_set,"Feature_Relation\\Line9_Desc_Tag_Avg_18w_sample10000_d128\\vec_1st.txt","Feature_Relation\\Line9_Desc_Tag_Avg_18w_sample10000_d128\\vec_1st_feature.txt");
		featureFilter(id_set,"Feature_Relation\\Line9_Desc_Tag_Avg_18w_sample10000_d128\\vec_2nd.txt","Feature_Relation\\Line9_Desc_Tag_Avg_18w_sample10000_d128\\vec_2nd_feature.txt");
		featureFilter(id_set,"Feature_Relation\\Line9_Desc_Tag_Avg_18w_sample10000_d128\\vec_all.txt","Feature_Relation\\line9_Desc_Tag_Avg_18w_vec_all_feature.txt");
		
	}

	private static void featureFilter(Set<String> id_set, String srcfile, String resfile) throws IOException {
		File f1 = new File(Config.SAVE_PATH+srcfile);
		BufferedReader br = new BufferedReader(new FileReader(f1));
		File f2 = new File(Config.SAVE_PATH+resfile);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f2));
		String line = null;
		while((line = br.readLine())!=null){
			String id = line.split("\\s")[0];
			if(id_set.contains(id)){
				bw.write(line+"\r\n");
			}
		}
		br.close();
		bw.flush();
		bw.close();
	}
}
