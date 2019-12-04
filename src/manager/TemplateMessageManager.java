package manager;

import org.junit.Test;

import service.WxService;
import util.Util;

public class TemplateMessageManager {

	/**
	 * 设置行业
	 */
	@Test
	public void set(){
		String at = WxService.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=" + at;
		String data = "{\r\n" + 
				"    \"industry_id1\":\"1\",\r\n" + 
				"    \"industry_id2\":\"2\"\r\n" + 
				"}";
		String result = Util.post(url, data);
		System.out.println(result);
	}
	
	/**
	 * 获取行业
	 */
	@Test
	public void get(){
		String at = WxService.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=" + at;
		String result = Util.get(url);
		System.out.println(result);
	}
	
	/**
	 * 发送模板消息
	 */
	@Test
	public void sendTemplateMessage() {
		String at = WxService.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + at;
		String data = "      {\r\n" + 
				"           \"touser\":\"otqCE0sFYYALyGujfH5HsGuinINY\",\r\n" + 
				"           \"template_id\":\"DPX82KEfH7Aip4XWECepBvNPgtcx_05-dnmdPWeFpqg\",\r\n" + 
				"           \"data\":{\r\n" + 
				"                   \"first\": {\r\n" + 
				"                       \"value\":\"恭喜你被录取！\",\r\n" + 
				"                       \"color\":\"#173177\"\r\n" + 
				"                   },\r\n" + 
				"                   \"company\":{\r\n" + 
				"                       \"value\":\"巧克力\",\r\n" + 
				"                       \"color\":\"#173177\"\r\n" + 
				"                   },\r\n" + 
				"                   \"time\": {\r\n" + 
				"                       \"value\":\"2019年11月12日\",\r\n" + 
				"                       \"color\":\"#173177\"\r\n" + 
				"                   },\r\n" + 
				"                   \"result\": {\r\n" + 
				"                       \"value\":\"面试通过！\",\r\n" + 
				"                       \"color\":\"#173177\"\r\n" + 
				"                   },\r\n" + 
				"                   \"remark\":{\r\n" + 
				"                       \"value\":\"请和本公司HR联系\",\r\n" + 
				"                       \"color\":\"#173177\"\r\n" + 
				"                   }\r\n" + 
				"           }\r\n" + 
				"       }";
		String result = Util.post(url, data);
		System.out.println(result);
	}
	
	
	
}
