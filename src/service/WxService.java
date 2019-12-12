package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.baidu.aip.ocr.AipOcr;
import com.thoughtworks.xstream.XStream;

import entity.AccessToken;
import entity.Article;
import entity.BaseMessage;
import entity.ImageMessage;
import entity.MusicMessage;
import entity.NewsMessage;
import entity.TextMessage;
import entity.VideoMessage;
import entity.VoiceMessage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.Util;

public class WxService {

	//wx配置
	private static final String TOKEN = "sjjzs";
	public static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	public static final String APPID = "wxc32c5d964a697764";
	public static final String APPSECRET = "e9cd4a7c101061f94080eacd507de478";
	//用于存储token
	private static AccessToken at;
	
	//聚合数据（聊天机器人）的APPKEY
	private static final String APPKEY = "3af7f1e99504ba6ada122b818d15ae92";
	
    //百度通用文字识别设置APPID/AK/SK
    public static final String APP_ID = "17920778";
    public static final String API_KEY = "EE3tcXv5cxT5nUs017lLI3MG";
    public static final String SECRET_KEY = "Wtr4ZreFbnOtCu0kOhUGsfwPTr23dnaG";
	
	/**
	 * 获取token
	 */
	private static void getToken() {
		String url = GET_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		String tokenStr = Util.get(url);
		JSONObject jsonObject = JSONObject.fromObject(tokenStr);
		String token = jsonObject.getString("access_token");
		String expireIn = jsonObject.getString("expires_in");
		//创建token对象
		at = new AccessToken(token, expireIn);
	}
	
	/**
	 * 向外暴露的获取token的方法
	 */
	public static String getAccessToken() {
		if (at == null || at.isExpired()) {
			getToken();
		}
		return at.getAccessToken();
	}
	
	
	/**
	 * 验证签名
	 */
	public static boolean check(String timestamp, String nonce, String signature) {
		
		// 1）将token、timestamp、nonce三个参数进行字典序排序
		String[] strs = new String[] { TOKEN, timestamp, nonce };
		Arrays.sort(strs);
		// 2）将三个参数字符串拼接成一个字符串进行sha1加密
		String str = strs[0] + strs[1] + strs[2];
		String mysig = sha1(str);
		// 3）开发者获得加密后的字符串可与signature对比
		return mysig.equalsIgnoreCase(signature);
	}

	/**
	 * 进行sha1加密
	 */
	private static String sha1(String src) {
		try {
			//获取一个加密对象
			MessageDigest md = MessageDigest.getInstance("sha1");
			//加密
			byte[] digest = md.digest(src.getBytes());
			char[] chars = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
			StringBuffer sb = new StringBuffer();
			//处理加密结果
			for (byte b:digest) {
				sb.append(chars[(b>>4)&15]);
				sb.append(chars[b&15]);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 处理消息和事件推送
	 */
	public static Map<String, String> parseRequest(InputStream is) {
		SAXReader saxReader = new SAXReader();
		Map<String, String> map = new HashMap<>();
		try {
			//读取输入流，获取文档对象
			Document document = saxReader.read(is);
			//根据文档对象获取根节点
			Element root = document.getRootElement();
			//获取根节点的所有子节点
			List<Element> elements = root.elements();
			for (Element e : elements) {
				map.put(e.getName(), e.getStringValue());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 用于处理所有的事件和消息的回复
	 * 返回的是xml数据包
	 */
	public static String getRespose(Map<String, String> requestMap) {
		BaseMessage msg = null;
		String msgType = requestMap.get("MsgType");
		//news回复图文消息，voice回复语音消息
		switch (msgType) {
		//处理文本消息
		case "text":
			msg = dealTextMessage(requestMap);
			break;
		case "image":
			msg = dealImage(requestMap);
			break;
		case "voice":
			
			break;
		case "video":
			
			break;
		case "music":
			
			break;
		case "news":
			
			break;
		case "event":
			msg = dealEvent(requestMap);
			break;
		default:
			break;
		}
		//把消息对象处理为xml数据包
		if (msg != null) {
			return beanToXml(msg);
		}
		return null;
	}
	
	/**
	 * 进行图片识别
	 */
	private static BaseMessage dealImage(Map<String, String> requestMap) {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 调用接口
        String path = requestMap.get("PicUrl");
        //上传本地图片
        //org.json.JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
        //上传网上图片
        org.json.JSONObject res = client.generalUrl(path, new HashMap<String, String>());
        String json = res.toString();
        //转为jsonObject
        JSONObject jsonObject = JSONObject.fromObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("words_result");
        Iterator<JSONObject> it = jsonArray.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
        	JSONObject next = it.next();
        	sb.append(next.getString("words")).append("/");
		}
		return new TextMessage(requestMap, sb.toString()); 
	}

	/**
	 * 处理事件推送
	 */
	private static BaseMessage dealEvent(Map<String, String> requestMap) {
		String event = requestMap.get("Event");
		switch (event) {
			case "CLICK":
				return dealClick(requestMap);
			case "VIEW":
				return dealView(requestMap);
			default:
				break;
		}
		return null;
	}

	/**
	 * 处理view类型的按钮的
	 */
	private static BaseMessage dealView(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 处理click类型的按钮的
	 */
	private static BaseMessage dealClick(Map<String, String> requestMap) {
		String key = requestMap.get("EventKey");
		switch (key) {
		case "1":
			//处理点击了第一个一级菜单
			return new TextMessage(requestMap, "处理点击了第一个一级菜单");
		case "31":
			//处理点击了第三个一级菜单的第二个子菜单
			
			break;

		default:
			break;
		}
		return null;
	}

	/**
	 * 把消息对象处理为xml数据包
	 */
	private static String beanToXml(BaseMessage msg) {
		XStream xStream = new XStream();
		xStream.processAnnotations(TextMessage.class);
		xStream.processAnnotations(ImageMessage.class);
		xStream.processAnnotations(MusicMessage.class);
		xStream.processAnnotations(NewsMessage.class);
		xStream.processAnnotations(VideoMessage.class);
		xStream.processAnnotations(VoiceMessage.class);
		String xml = xStream.toXML(msg);
		return xml;
	}

	private static BaseMessage dealTextMessage(Map<String, String> requestMap) {
		//用户发来的内容
		String msg = requestMap.get("Content");
		if (msg.equals("图文")) {
			List<Article> articles = new ArrayList<>();
			articles.add(new Article("这是图文消息的标题", "这是图文消息的详情", "http://mmbiz.qpic.cn/mmbiz_jpg/TIdSmCYib4Z08dACyCkdPIibjEtgXjzpCwtiaWgqqMwx5uH6E8DFbbC3Co0WkzVO5HibbLFRrfpvgoDDokYKSNS4EA/0", "www.baidu.com"));
			NewsMessage nm = new NewsMessage(requestMap, articles);
			return nm;
		}
		if (msg.equals("登录")) {
			String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE#wechat_redirect";
			url = url.replace("APPID", APPID).replace("REDIRECT_URI", "http://wx.dashaocun.com/weixin/GetUserInfo").replace("SCOPE", "snsapi_userinfo");
			TextMessage tm = new TextMessage(requestMap, "点击<a href=\""+url+"\">这里</a>登录");
			return tm;
		}
		
		//调用方法返回聊天的内容
		String resp = chat(msg);
		TextMessage tm = new TextMessage(requestMap, resp);
		return tm;
	}
	
	/**
	 * 调用图灵机器人聊天
	 */
	private static String chat(String msg) {
        String result =null;
        String url ="http://op.juhe.cn/iRobot/index";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key",APPKEY);//您申请到的本接口专用的APPKEY
        params.put("info",msg);//要发送给机器人的内容，不要超过30个字符
        params.put("dtype","");//返回的数据的格式，json或xml，默认为json
        params.put("loc","");//地点，如北京中关村
        params.put("lon","");//经度，东经116.234632（小数点后保留6位），需要写为116234632
        params.put("lat","");//纬度，北纬40.234632（小数点后保留6位），需要写为40234632
        params.put("userid","");//1~32位，此userid针对您自己的每一个用户，用于上下文的关联
        try {
            result =Util.net(url, params, "GET");
            System.out.println(result);
            //解析json
            JSONObject jsonObject = JSONObject.fromObject(result);
            //取出error_code
            int code = jsonObject.getInt("error_code");
            if(code!=0) {
            		return null;
            }
            //取出返回的消息的内容
            String resp = jsonObject.getJSONObject("result").getString("text");
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	/**
	 * 上传临时素材
	 * path路径，type类型
	 */
	public static String upload(String path, String type) {
		File file = new File(path);
		String url = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
		url = url.replace("ACCESS_TOKEN", getAccessToken()).replace("TYPE", type);
		try {
			URL urlObj = new URL(url);
			//强转为安全链接
			HttpsURLConnection conn = (HttpsURLConnection)urlObj.openConnection();
			//设置链接信息
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(true);
			//设置请求头信息
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "utf8");
			//数据的边界
			String boundary = "-----" + System.currentTimeMillis();
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			//获取输出流
			OutputStream out = conn.getOutputStream();
			//创建文件的输入流
			FileInputStream is = new FileInputStream(file);
			//第一部分：头部信息
			//准备头部信息
			StringBuilder sb = new StringBuilder();
			sb.append("--");
			sb.append(boundary);
			sb.append("\r\n"); 
			sb.append("Content-Disposition:form-data;name=\"media\";filename=\""+file.getName()+"\"\r\n"); 
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			out.write(sb.toString().getBytes());
			//第二部分：文件内容
			byte[] b = new byte[1024];
			int len;
			while ((len=is.read(b))!=-1) {
				out.write(b, 0, len);
			}
			//第三部分：
			String foot = "\r\n--"+boundary+"--\r\n";
			out.write(foot.getBytes());
			out.flush();
			out.close();
			//读取数据
			InputStream is2 = conn.getInputStream();
			StringBuilder resp = new StringBuilder();
			while ((len = is2.read(b))!=-1) {
				resp.append(new String(b,0,len));
			}
			return resp.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取带参数二维码的ticket
	 */
	public static String getOrCodeTicket() {
		String at = getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + at;
		// 生成临时字符二维码
		String data = "{\"expire_seconds\": 600, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"sjjzs\"}}}";
		String result = Util.post(url, data);
		String ticket = JSONObject.fromObject(result).getString("ticket");
		return ticket;
	}
	
	/**
	 * 获取用户信息
	 */
	public static String getUserInfo(String openid) {
		String at = getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		url = url.replace("ACCESS_TOKEN", at).replace("OPENID", openid);
		String result = Util.get(url);
		return result;
	}
	
}
