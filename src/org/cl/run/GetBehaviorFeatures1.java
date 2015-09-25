package org.cl.run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.cl.configuration.Config;
import org.cl.configuration.RegexString;
import org.cl.service.Utils;
/**
 * 		1. 微博发布的时间，以小时为单位。所有的微博发布时间（小时分布）构成一个向量，例如假设用户共发表3条微博，时间分别为8:28, 10:20, 22:05， 则构成一个3维的向量  8   10   22；
		2. 微博发布的时间，以周为单位。所有的微博发布时间（周分布）构成一个向量，这个可能需要调用一个日历函数，例如假设用户共发表3条微博，时间分别为周1， 周5， 周7， 则构成一个3维的向量  1   5   7；
		3. 微博发布的地点经度
		4. 微博发布的地点纬度
		5. 微博发布时所用的设备， 所有微博使用的设备存入一个向量
		6. 微博的回复数，所有微博中的回复数存入一个向量
		7. 微博的被转发数，所有微博的被转发数存入一个向量		
		8. 微博中包含的url数，所有微博包含的连接数存入一个向量
		9. 微博中包含的#...#（强调主题用）数，，所有微博包含的#...#数存入一个向量
		10.微博中包含的@ 数
		11.微博是否搭配图片
		12.微博是否原创 		
		13.话题url是否并存
		14.图片url是否并存
		//0表示不是，1表示是
 * @author Chenli
 *
 */
public class GetBehaviorFeatures1  implements Runnable
{
	/**用户ID*/
	private String uid=null;
	private Map<String, Integer> src_map;
	String hours = "",weeks = "",longitudes = "",latitudes = "", equipments = "";
	String replys = "",trans = "",links = "",themes = "",ats="";
	String isImg = "", isOriginal = "",theme_links = "",img_links = "";//0表示不是，1表示是
	public GetBehaviorFeatures1(String uid, Map<String, Integer> src_map)
	{
		this.uid=uid;
		this.src_map = src_map;
	}
	public void run()
	{
		System.out.println("Getting BehaviorFeatures1 of "+uid);
		File file_or = new File(Config.SAVE_PATH+"/Weibos/"+uid+".txt");
		String line = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file_or));
			StringBuffer result = new StringBuffer();
			while((line=br.readLine())!=null)
			{
				if(line.equals(""))continue;
				JSONObject statusAndComment = JSONObject.fromObject(line);
				JSONObject weibo_json = statusAndComment.getJSONObject("weibo");

				getTime(weibo_json.getString("createdAt"));//获取小时特征、星期特征
				getLocation(weibo_json.getString("geo"));//获取地点特征
				getSrc(weibo_json.getString("source"));//获取设备特征
				replys= weibo_json.getString("commentsCount")+"\t";//获取回复数特征
				trans= weibo_json.getString("repostsCount")+"\t";//获取转发数特征 

				boolean img = getImg(weibo_json.getString("originalPic"));//获取图片特征
				getOriginal(weibo_json.getString("retweetedStatus"));//获取是否原创特征

				String[] weiboCon = weibo_json.getString("text").split("//@");//后面几个特征的获取都需要微博内容这一项
				if(weiboCon.length==0||weiboCon[0].equals("")){//如果微博内容为空则获取下一条微博
					links=0 + "\t";
					themes=0+"\t";
					ats=0+"\t";
					theme_links=0+"\t";
					img_links=0+"\t";
				}else{
					boolean links = getLinks(weiboCon[0]);//获取链接特征
					boolean themes = getThemes(weiboCon[0]);//获取主题特征
					getAtNum(weiboCon[0]);//获取@数量特征
					if(links&&themes){theme_links = 1+"\t";}//判断是否同时有主题和链接
					else{theme_links = 0+"\t";}
					if(links&&img){img_links = 1+"\t";}//判断是否同时有图片和链接
					else{img_links = 0+"\t";}
				}
				toStringBuffer(result);
			}
			br.close();
			saveResult(result);
		} catch (Exception e) {
			System.out.println(uid);
			System.out.println(line);
			e.printStackTrace();
		}
	}
	private void toStringBuffer(StringBuffer result) {
		int s = 0;
		result.append((s+0)+":"+hours+(s+1)+":"+weeks+(s+2)+":"+longitudes+(s+3)+":"+latitudes+(s+4)+":"+equipments+(s+5)+":"
				+replys+(s+6)+":"+trans+(s+7)+":"+links+(s+8)+":"+themes+(s+9)+":"+ats+(s+10)+":"
				+isImg+(s+11)+":"+isOriginal+(s+12)+":"+theme_links+(s+13)+":"+img_links+"\r\n");
	}
	
	private void saveResult(StringBuffer result) throws IOException {
		File file = new File(Config.SAVE_PATH+"/Feature_Behaviour/"+uid+".txt");
		OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(file));
		w.write(result.toString());
		w.flush();
		w.close();
	}
	private void getOriginal(String retweetedStatus_item) {
		if(retweetedStatus_item==null||retweetedStatus_item.equals("")){//原创
			isOriginal="1"+"\t";
		}else{
			isOriginal="0"+"\t";
		}
	}
	private boolean getImg(String originalPic_item) {
		if(originalPic_item==null||originalPic_item.equals("")){//没有配图
			isImg="0"+"\t";
			return false;
		}else{
			isImg="1"+"\t";
			return true;
		}
	}

	private void getAtNum(String weiboCon) {
		//String regex_at = "[^//]?@(\\w|[\\x{4e00}-\\x{9fa5}]|[-]|[_])*\\s";
		String regex_at = RegexString.Regex_at;//"[^//]?@(\\w|[\\x{4e00}-\\x{9fa5}]|[-]|[_])*(\\s|[:]|$)";
		Pattern p_at = Pattern.compile(regex_at);
		Matcher m_at = p_at.matcher(weiboCon);
		int at = 0;
		while(m_at.find()){
			at++;
		}
		ats=at+"\t";
	}
	private boolean getThemes(String weiboCon) {
		String regex_theme = RegexString.Regex_theme;//"#(\\w|[\\x{4e00}-\\x{9fa5}]|[-]|[_])*#";
		Pattern p_theme = Pattern.compile(regex_theme);
		Matcher m_theme = p_theme.matcher(weiboCon);
		int theme = 0;
		while(m_theme.find()){
			//String theme = m_theme.group();
			theme++;
		}
		themes=theme+"\t";
		return theme>0;
	}
	private boolean getLinks(String weiboCon) {
		String regex_url = RegexString.Regex_url;//"(http|https)://[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
		Pattern p_link = Pattern.compile(regex_url);
		Matcher m_link = p_link.matcher(weiboCon);
		int link = 0;
		while(m_link.find()){
			//String link = m_link.group();
			link++;
		}
		links =link + "\t";
		return link>0;
	}
	private void getSrc(String src_item) {
		src_item = Utils.clearSource(src_item);
		if(src_map.containsKey(src_item)){
			equipments = src_map.get(src_item)+"\t";
		}else{
			System.out.println(uid+":"+src_item+"词典里没有我！！不开森!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
	}
	private void getLocation(String location_item) {
		if(location_item.equals("")){longitudes = "0\t";latitudes = "0\t";return;}
		//System.out.println("uid has addr : "+uid);
		longitudes = location_item.split(",")[0]+"\t";//substring(location_item.indexOf("[")+1,location_item.indexOf(","))+"\t";
		latitudes =  location_item.split(",")[1]+"\t";//.substring(location_item.indexOf(",")+1,location_item.indexOf("]"))+"\t";

	}
	private void getTime(String time_item) {
		String[] time = time_item.split("-|\\s|:");
		Calendar c = Calendar.getInstance();
		c.set(Integer.parseInt(time[0]), Integer.parseInt(time[1])-1, Integer.parseInt(time[2]));
		int week = c.get(Calendar.DAY_OF_WEEK)-1;
		if(week==0)week=7;
		weeks=week+"\t";
		hours=time[3]+"\t";
	}

}
