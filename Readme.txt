======================================================词典程序=====================================================================================================
部分词典依靠手工文件生成，运行前需准备如下文件：
	=Config\manual
		-Dict_Buzz.txt		流行语词典。格式：软妹纸 nb（加了nb是为了制作用户词典，用于给NLPIR分词系统识别流行语）
		-Dict_Emotion1.txt	官方表情词典。格式：[乐乐] xm （加xm理由同上）
		-Dict_Emotion2.txt	符号表情词典。格式：<(￣ ﹌ ￣)@m
		-Dict_Modal.txt		语气词词典。格式：欧耶
		-Dict_AppType.txt	APP类别词典。格式：设计##创意##原创（同一类别不同关键字用##隔开，每行一个类别）
		-Dict_MobileType.txt	Moible类别词典。格式：iPhone##IPHONE##IPhone（同上）
		-Dict_UserType.txt	用户类别词典。格式：演员##女星##男星（同上）
		
※WordDict：获取文本词典和POS词典（进行分词后才可执行）
	=Cofig
		-Dict_Text.txt		文本词典
		-Dict_POS.txt		文本词性词典
	=Config\manual
		-Dict_Emotion_auto.txt	遍历分词后的微博文本，根据词性为xm得到的官方表情词典
		-Dict_Modal_auto.txt	遍历分词后的微博文本，根据词性为e得到的语气词词典

※FeatureDict：获取文本词典和POS词典（进行分词后才可执行）
	=Config
		-Dict_Acronym.txt	缩写词词典
		-Dict_Buzz.txt		流行语词典
		-Dict_Emotion.txt	表情词典
		-Dict_Emotion1.txt	官方表情词典
		-Dict_Emotion2.txt	符号表情词典
		-Dict_Modal.txt		语气词词典
		-Dict_Punt.txt		标点符号词典
		-Dict_Tag.txt		用户标签词典
		-Dict_FriType.txt	用户类别词典
		-Dict_FriFolType.txt	用户类别词典
		-Dict_VFriType.txt	用户类别词典
		-Dict_AppType.txt	APP类别词典
		-Dict_MobileType.txt	Mobile类别词典


======================================================特征程序===========================================================================================
※Main_GetBehaviorFeatures1：获取用户的行为特征模板一【微博级别】
	=Feature_Behaviour：用户的行为特征
		-uid.txt ：
		0:9	1:2	2:-1	3:-1	4:3404	5:0	6:0	7:1	8:0	9:0	10:1	11:1	12:-1	13:1
		-内容说明：
		0. 微博发布的时间，以小时为单位。所有的微博发布时间（小时分布）构成一个向量，例如假设用户共发表3条微博，时间分别为8:28, 10:20, 22:05， 则构成一个3维的向量  8   10   22；
		1. 微博发布的时间，以周为单位。所有的微博发布时间（周分布）构成一个向量，这个可能需要调用一个日历函数，例如假设用户共发表3条微博，时间分别为周1， 周5， 周7， 则构成一个3维的向量  1   5   7；
		2. 微博发布的地点经度【0表示无数据】
		3. 微博发布的地点纬度【0表示无数据】
		4. 微博发布时所用的设备对应的编号（映射见"Config\\Dict_Src.txt"）
		5. 微博的回复数
		6. 微博的被转发数		
		7. 微博中包含的链接数
		8. 微博中包含的#...#（强调主题用）数
		9.微博中包含的@ 数 
		10.微博是否搭配图片【0表示不是，1表示是】
		11.微博是否原创	   【0表示不是，1表示是】		
		12.话题url是否并存 【0表示不是，1表示是】
		13.图片url是否并存 【0表示不是，1表示是】	

※Main_GetBehaviorFeatures2：获取用户的行为特征模板二【用户级别】
	=Feature_Behaviour
		-Behaviour2_feature.txt"：用户的行为特征		来源数	来源改变次数
		1730857704	0:534	1:48	2:1	3:1	4:2.592233	5:61	6:1	7:2	8:5.3939395	9:134	10:0	11:41	12:56.024345	13:36	14:212
		-内容说明：
		0. 用户发布的微博数
		1.一天中发布微博数最大值
		2.一天中发布微博数最小值（不计算没发微博的日子）
		3.一天中发布微博数中值
		4.一天中发布微博数平均数（分母为发了微博的天数）
		5.一周中发布微博数最大值（这里的一周是以用户第一条微博发布的日期作为星期的开始，例如用户发布了三条微博，分别是星期五、第二个星期的星期二，以及第二个星期的星期六，那么前两条算在同一个星期发布，而第三条与前两条是不同一个星期）
		6.一周中发布微博数最小值（不计算没发微博的星期）
		7.一周中发布微博数中值
		8.一周中发布微博数平均数（分母为发了微博的周数）
		9.微博长度最大值
		10.微博长度最小值
		11.微博长度中值	
		12.微博长度平均值
		13.微博来源数
		14.微博来源改变次数（如来源分别是 iphone,sumsung,iphone，则改变次数为2）

※Main_GetBehaviorFeatures3：获取用户的行为特征模板三【用户级别】
	=Feature_Behaviour
		-Behaviour31_feature.txt"：用户的行为特征
		1730857704	0:534	1:48	2:1
		-内容说明：
		0.微博数
		1.粉丝数（实际数）
		2.原创数
	=Feature_Behaviour
		-Behaviour32_feature.txt"：用户的行为特征
		1730857704	0:0.001	1:0.999	2:0.61
		-内容说明：
		0.原创率
		1.转发率
		2.回复率(有评论的微博占微博数比例)

※Main_GetEmoticonStastic:获取表情统计特征（在表情符号特征<Main_GetStyleFeatures>已经获取完成情况才能统计）【用户级别】
	=Feature_Style
		-EmotionStastic_feature.txt
		1874991105	0:978	1:508	2:225	3:283	4:74	5:385	6:26	7:6	8:3	9:0.553	10:0.532	11:0.394	12:1.319	13:0.443	14:0.557
		-内容说明：
		0.微博总数
		1.表情总数(强度)
		2.官方表情总数
		3.字符表情总数
		4.表情类别总数(丰富度)
		5.含表情的微博数
		6.只含表情的微博数(表情依赖度)
		7.连续表情数最大值
		8.连续同一表情数最大值
		9.连续表情数均值(>1)
		10.连续同一表情数均值(>1)
		11.含表情的微博数：微博总数(<1)(频繁程度)
		12.表情总数：含表情的微博数(>1)(强度)
		13.官方表情总数：表情总数(官方表情强度)
		14.字符表情总数：表情总数(字符表情强度)

※Main_GetRelationFeature：获取用户的关系特征 【用户级别】（备注：首先执行“其他程序中的”GetYellowV_UserId得到"/Config/YellowV_ID.txt"）
	=Feature_Relation：用户的关系特征,侧重两人交互情况
		-Mutual_feature.txt
		1609949805	2720981765	0:0	1:0	2:1	3:0	4:0	5:1	6:1	7:0	8:1	9:0	10:0	11:1	12:0
		-内容说明：
		0.用户1是否用户2经常@的人前1（是为1，不是为0）
		1.用户1是否用户2经常@的人前3（是为1，不是为0）
		2.用户1是否用户2经常@的人前10（是为1，不是为0）
		3.用户2是否用户1经常@的人前1（是为1，不是为0）
		4.用户2是否用户1经常@的人前3（是为1，不是为0）
		5.用户2是否用户1经常@的人前10（是为1，不是为0）
		6.是否有共同@的第3个人（是为1，不是为0）
		7.是否共同出现在第3个人的@对象中（是为1，不是为0）
		8.两人没有共同好友（有为1，没有为0）
		9.粉丝的共同普通好友数
		10.粉丝的共同大V好友数
		11.关注的共同普通好友数
		12.关注的共同大V好友数

※Main_GetRelationFirFolFeature：获取用户关注的用户特征【用户级别】（备注：首先执行Main_GetUserInfoFeature得到每个用户的特征Feature_UserInfo\UserInfoX.txt.description.parsed等文件）
	=Feature_Relation：用户关注的用户类别特征(Y表示使用Ygram对用户的属性进行分词)
		-Tag_feature.txt
		-ScreenNameY_feature.txt
		-VerifiedReasonY_feature.txt
		-DescriptionY_feature.txt
		1812871717	1:1	2:6	4:1	5:1	7:1	17:1	18:6	19:1	20:1	25:1	28:1	34:2	35:3	36:1	40:4	
		-内容说明：
		x:y  x表示对应的tag或screenname等，y为对应用户使用该tag或screenname的次数。x与tag映射文件为Config\Dict_Tag.txt 。。。。。


※Main_GetRelationFirFolTypeFeature：获取用户关注的（大V）用户、关注用户的（大V）用户的用户类别特征【用户级别】（备注：首先执行ClassifierUserByDict得到Feature_Relation\\VUser_type.txt文件）
	=Feature_Relation：用户关注的用户以及关注用户的用户类别特征
		-FriType_feature.txt  : 只使用朋友的数据
		-FriFolType_feature.txt   ：使用朋友和粉丝的数据
		1812871717	1:1	2:6	4:1	5:1	7:1	17:1	18:6	19:1	20:1	25:1	28:1	34:2	35:3	36:1	40:4	
		1299453733	1:8	2:14	4:15	5:6	7:1	17:4	18:14	19:4	20:1	26:1	27:1	28:2	29:2	34:4	35:1	36:1	38:4	40:2
		-内容说明：
		x:y  x表示对应的用户关注的（大V）用户类别，y为对应用户使用该类别好友个数。x与用户类别映射文件为Config\Dict_FriFolType.txt

※Main_GetRepostFeature：获取用户的转发特征【微博级别】【待改进】
	=Feature_RePost
		-uid.txt：
		1234565212:4
		-内容说明：
		被转发用户id:转发了该用户微博的次数

※Main_GetSrcFeature:获取用户的微博来源特征【用户级别】
	=Feature_SRC
		-Src_feature.txt【最终结果】
		1715196817	0:15	1:3	13:4	14:1	28:1	35:1	42:3	43:1	44:32
		-内容说明：x：y x表示Config\Dict_Src.txt中对应的src，y表示该src在用户微博中出现的次数
		
※Main_GetSrcTopicFeature:获取用户的微博来源特征【用户级别】（首先要得到Feature_SRC\\src_topic.txt）
	**c0.人工准备Config\\Dict_SrcType.txt
	**c1.由本工程的main.dict.FeatureDict中的getSrcDict()遍历微博，获得Config\\Dict_Src.txt、Config\\Src_Url.txt两个文件
	**c2.由SinaApp_Crawler的爬虫根据Config\\Src_Url.txt访问对应的网页获取src的描述信息，以及网页已不存在的src，获得Feature_SRC\\Src_Description.txt、Feature_SRC\\Src_NotExist.txt两个文件
	**c3.由王飞的百度爬虫根据Feature_SRC\\Src_NotExist.txt，获得各应用的搜索结果，将前10条结果的标题补充到Src_Description.txt中，另外手动对之前版本备份为Src_Description_OnlySinaWebSource.txt
	//利用LDA得到Src属于各个Topic的概率分布，从而得到各用户
	**c4.由JnaTest_NLPIR将Feature_SRC\\Src_Description.txt进行分词，得到Feature_SRC\\Src_Description.txt.parsed
	**c5.由DataProcessor的RemoveWords将描述的分词文件进行清洗，得到Feature_SRC\\Src_Description.txt.parsed.clr
	**6.创建Src_DescriptionLDA文件夹，对Feature_SRC\\Src_Description.txt.parsed.clr第一行添加文件的行数
	**7.由JGibbLDA-v1.0对Feature_SRC\\Src_DescriptionLDA\\Src_Description.txt.parsed.clr进行训练，得到各文档属于各主题的可能性，即model-final.theta
	**8.由DataProcessor的MergeDocument将Feature_SRC\\Src_Description.txt.parsed.clr与Feature_SRC\\Src_DescriptionLDA\\model-final.theta整合起来，将文档对应上概率分布，得到Feature_SRC\\src_topic.txt
	=Feature_SRC
		-SrcTopic_feature.txt【最终结果】
		1596784950	0:0.8655	1:0.5656	...
		1808041887	0:0.56554	1:0.5784	...
		-内容说明：
		x:y  x表示对应的srcTopic（未知，由LDA训练得到），y为对应用户属于topicN的可能性。

※Main_GetSrcTypeFeature1:获取用户的微博来源特征【用户级别】
	=Feature_SRC\Mobile【中间结果】
		-uid.txt（SRC种类总是作为第一个特征）
		src描述		src使用次数	src类别##判断src类别的依据	
		SRC种类		1		0##
		华为Ascend手机	8		3##华为
	=Feature_SRC\App【中间结果】
		SRC种类		27		0##
		百度分享	2		42##分享
		益动GPS		1		1##
		......
	=Feature_SRC
		-Mobile_undefined.txt(未区分成功的mobile)【中间结果】
		美图手机2	2	1##	
	=Feature_SRC
		-App_undefined.txt(未区分成功的app)【中间结果】
		返还网	1	1##
	=Feature_SRC
		-Mobile_feature.txt【最终结果】
		1596784950	0:1	6:1	
		1808041887	0:2	1:1	14:1
		-内容说明：
		x:y  x表示对应的src类别，y为对应用户使用该类别src的次数。x与src类别映射文件为Config\Dict_MobileType.txt	
	=Feature_SRC
		-App_feature.txt【最终结果】
		1715196817	0:15	1:3	13:4	14:1	28:1	35:1	42:3	43:1	44:911
		1677398887	0:13	1:3	2:1	3:7	14:1	28:1	33:1	42:1	43:1	44:863	
		-内容说明：类似-Feature_SRC\\mobile_feature的内容说明。x与src类别映射文件为Config\Dict_AppType.txt

※Main_GetSrcTypeFeature2:获取用户的微博来源特征【用户级别】（首先要得到Feature_SRC\\Src_type.txt）
	**0.人工准备Config\\Dict_SrcType.txt
	**1.由本工程的main.dict.FeatureDict中的getSrcDict()遍历微博，获得Config\\Dict_Src.txt、Config\\Src_Url.txt两个文件
	**2.由SinaApp_Crawler的爬虫根据Config\\Src_Url.txt访问对应的网页获取src的描述信息，以及网页已不存在的src，获得Feature_SRC\\Src_Description.txt、Feature_SRC\\Src_NotExist.txt两个文件
	**3.由王飞的百度爬虫根据Feature_SRC\\Src_NotExist.txt，获得各应用的搜索结果，将前10条结果的标题补充到Src_Description.txt中，另外手动对之前版本备份为Src_Description_OnlySinaWebSource.txt
	**2-3.或者DataProcessor的ExtractInfo从已得到Feature_SRC\\Src_Description.txt中筛选出需要的src的描述
	//利用描述信息对src分类
	**4.由DataProcessor的ClassifierSrcByDict利用classiferSrc()方法,得到Src分类结果Feature_SRC\\Src_type1.txt、Feature_SRC\\Src_typeUndefined1.txt
	//利用描述信息的关键字对src分类
	**4.由JnaTest_NLPIR将Feature_SRC\\Src_Description.txt进行分词，得到Feature_SRC\\Src_Description.txt.parsed
	**5.由DataProcessor的RemoveWords将描述的分词文件进行清洗，得到Feature_SRC\\Src_Description.txt.parsed.clr
	**6.由DataProcessor的GetKeywords从Feature_SRC\\Src_Description.txt.parsed.clr挑选出出现频率超过N的词，并去重，得到Feature_SRC\\Src_Keywords.txt
	**7.由DataProcessor的ClassifierSrcByDict利用classiferSrcWithKeyword()方法,得到Src分类结果Feature_SRC\\Src_type.txt、Feature_SRC\\Src_typeUndefined.txt
	=Feature_SRC
		-SrcType_feature.txt【最终结果】
		1596784950	0:1	6:1	
		1808041887	0:2	1:1	14:1
		-内容说明：
		x:y  x表示对应的src类别，y为对应用户使用该类别src的次数。x与src类别映射文件为Config\Dict_SrcType.txt

※Main_GetStyleFeatures：获取用户微博的风格特征（包括表情符号、流行用语、缩写词、标点符号、语气词特征）【微博级别-可以通过Main_FeatureToUserLevel改为用户级别，改变后格式见Main_FeatureToUserLevel内容说明】
	=Feature_Style\Emotion：用户表情特征
		-uid.txt
		77:1	657:1	1308:1	
		1771:1	
		-内容说明：
		每条微博对应结果文件中的一行。每行中的x:y里面，x表示对应的Emotion符号，y为对应用户该行的微博使用该Emotion的次数。
		x与Emotion类别映射文件为Config\Dict_Emotion.txt和Config\Dict_Emotion1.txt、Config\Dict_Emotion2.txt（其中1为官方表情，2为符号表情，Dict_Emotion则包含所有表情）
	=Feature_Style\Emotion2（中间结果，方便查看用户有哪些符号表情）
		-uid.txt
		-内容说明：（类似Emotion）
	=Feature_Style\Buzz：用户微博流行用语使用特征
		-uid.txt
		-内容说明：（类似Emotion）
	=Feature_Style\Acronym：用户微博缩写词使用特征
		-uid.txt
		-内容说明：（类似Emotion）
	=Feature_Style\Modal：用户微博语气词使用特征
		-uid.txt
		-内容说明：（类似Emotion）
	=Feature_Style\Punt：用户微博标点符号使用特征
		-uid.txt
		-内容说明：（类似Emotion）
		
※Main_GetTextualFeatures:：获取用户的文本特征（text、POS）【微博级别-可以通过Main_FeatureToUserLevel改为用户级别，改变后格式见Main_FeatureToUserLevel内容说明】
	=Feature_Textual\Text
		-uid.txt
		77:1	324:1	1308:1	
		13241:1	
		-内容说明：
		每条微博对应结果文件中的一行。每行中的x:y里面，x表示对应的text符号，y为对应用户该行的微博使用该text的次数。
		x与Text类别映射文件为Config\Dict_Text.txt
	=Feature_Textual\POS
		-uid.txt
		43:1	53:1	98:1	
		-内容说明：(类似Text)

※Main_GetUserInfoFeature: 获取用户的Profile特征（描述、昵称、标签、认证原因等   作为假词）
	=Feature_UserInfo: 用户的Profile特征
		-Tag_feature.txt
		-DescriptionY_feature.txt
		1812871717	1:1	2:6	4:1	5:1	7:1	17:1	18:6	19:1	20:1	25:1	28:1	34:2	35:3	36:1	40:4	
		-内容说明：(类似Text)
		
※Main_GetUserInfoFeature2: 获取用户的Profile特征（描述、昵称、标签、认证原因等）
	=Feature_UserInfo: 用户的Profile特征
		-Profile_feature.txt
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
		
※Main_GetUserInfoFeature3: 获取用户的好友数特征
	=Feature_UserInfo: 用户的Profile特征
		-NeibourNum_feature.txt
		1812871717	1:324	2:432	3:123	4:32	5:34	6:34	7:43	8:45	9:76	10:43
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

※Main_GetUserInfoFeature4: 获取用户的好友比例特征
	=Feature_UserInfo: 用户的Profile特征
		-NeibourNum_feature.txt
		1812871717	1:1	2:0	3:1	4:0	5:1	6:0	7:1	8:4	9:1	10:0	
		1.用户的关注数是否>平均数
		2.用户的粉丝数是否>平均数
		3.用户关注中达人用户数是否>平均数
		4.用户关注中黄V用户数是否>平均数
		5.用户关注中蓝V用户数是否>平均数
		6.用户关注中已不存在用户数是否<平均数
		7.用户粉丝中达人用户数是否>平均数
		8.用户粉丝中黄V用户数是否>平均数
		9.用户粉丝中蓝V用户数是否>平均数
		10.用户粉丝中已不存在用户数是否<平均数	
