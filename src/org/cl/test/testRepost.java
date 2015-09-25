package org.cl.test;

import org.cl.configuration.Config;
import org.cl.run.GetRepost;

public class testRepost {

	public static void main(String args[]){
		initEnvironment();
		String uid = "1759003503";
		GetRepost user_repost=new GetRepost(uid);
		user_repost.run();
	}

	private static void initEnvironment() {
		String path = System.getProperty("user.dir");
		path = path.replace("Sina_AgePred", "");
		path += "/Sina_res/Data_AgePred/";
		System.out.println(path);
		Config.SAVE_PATH = path;
	}
}
