package org.cl.test;

import java.io.IOException;

import org.cl.run.GetBehaviorFeatures2;

public class testBF2 {

	public static void main(String args[]) throws IOException{
		String uid = "1653775045";
		GetBehaviorFeatures2 getBehaviorFeatures = new GetBehaviorFeatures2(uid);
		getBehaviorFeatures.run();
	}
}
