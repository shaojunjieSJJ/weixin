package service;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class WxService {

	private static final String TOKEN = "sjjzs";
	
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
		String msgType = requestMap.get("MsgType");
		//news回复图文消息，voice回复语音消息
		switch (msgType) {
		case "text":
			
			break;
		case "image":
			
			break;
		case "voice":
			
			break;
		case "video":
			
			break;
		case "music":
			
			break;
		case "news":
			
			break;
		default:
			break;
		}
		
		return null;
	}

}
