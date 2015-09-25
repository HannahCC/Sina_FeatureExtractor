package org.cl.test;
import org.cl.run.FeaturesToUserLevel;

public class testFeatureToUserLevel {

	public static void main(String args[]){
		String uid = "2009192895";
		FeaturesToUserLevel getFeatureTF=new FeaturesToUserLevel(uid,"Feature_Textual","Text");
		getFeatureTF.run();
	}
}
