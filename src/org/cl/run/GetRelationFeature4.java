package org.cl.run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.cl.configuration.Config;

public class GetRelationFeature4 {

	public static void func_GetRelationFeature4() throws IOException {
		File f1 = new File(Config.SAVE_PATH+"/Feature_Relation/Mutual_feature_1.txt");
		BufferedReader r1 = new BufferedReader(new FileReader(f1));
		File f2 = new File(Config.SAVE_PATH+"/Feature_Relation/Mutual_feature_2.txt");
		BufferedReader r2 = new BufferedReader(new FileReader(f2));
		File f3 = new File(Config.SAVE_PATH+"/Feature_Relation/Mutual_feature_3.txt");
		BufferedReader r3 = new BufferedReader(new FileReader(f3));
		File fw = new File(Config.SAVE_PATH+"/Feature_Relation/Mutual_feature.txt");
		OutputStreamWriter w_feature = new OutputStreamWriter(new FileOutputStream(fw),"utf-8");
		//BufferedWriter w_feature = new BufferedWriter(new FileWriter(fw));
		String tmp = "";
		while((tmp = r1.readLine())!=null){
			
			tmp += "\t" + r2.readLine() 
				+ "\t" +r3.readLine()+"\r\n";
			w_feature.write(tmp);
		}
		r1.close();r2.close();r3.close();
		w_feature.flush();
		w_feature.close();
	}

}
