package test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import com.baidu.aip.ocr.AipOcr;
import com.thoughtworks.xstream.XStream;

import entity.Button;
import entity.ClickButton;
import entity.PhotoOrAlbumButton;
import entity.SubButton;
import entity.TextMessage;
import entity.ViewButton;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import service.WxService;

public class TestWx {
	
    //百度通用文字识别设置APPID/AK/SK
    public static final String APP_ID = "17920778";
    public static final String API_KEY = "EE3tcXv5cxT5nUs017lLI3MG";
    public static final String SECRET_KEY = "Wtr4ZreFbnOtCu0kOhUGsfwPTr23dnaG";

    
    /**
     * 上传素材
     */
    @Test
    public void testUpload() {
    	String file = "‪C:\\Download\\1.jpg";
    	file = file.replace("\\\\", "/");
    	System.out.println(file);
    	String result = WxService.upload(file, "image");
    	System.out.println(result);
    }
    
	@Test
	public void testPic() {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        //System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        String path = "‪C:\\Download\\1.jpg";
        path = path.replace("\\\\", "/");
        org.json.JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
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
        
	}
	
	
	
	//自定义菜单测试
	@Test
	public void testButton() {
		
		//菜单对象
		Button button = new Button();
		//第一个一级菜单
		button.getButton().add(new ClickButton("一级点击", "1"));
		//第二个一级菜单
		button.getButton().add(new ViewButton("一级跳转", "http://www.baidu.com"));
		//第三个一级菜单
		SubButton subButton = new SubButton("有子菜单");
		//为第三个一级菜单增加子菜单
		subButton.getSub_button().add(new PhotoOrAlbumButton("传图", "31"));
		subButton.getSub_button().add(new ClickButton("点击", "32"));
		subButton.getSub_button().add(new ViewButton("网易新闻", "http://news.163.com"));
		//加入第三个一级菜单
		button.getButton().add(subButton);
		
		//转为json
		JSONObject jsonObject = JSONObject.fromObject(button);
		System.out.println(jsonObject.toString());
		
	}
	
	@Test
	public void testToken() {
		System.out.println(WxService.getAccessToken());
		System.out.println(WxService.getAccessToken());
	}
	
	@Test
	public void testMsg() {
		Map<String, String> map = new HashMap<>();
		map.put("ToUserName", "to");
		map.put("FromUserName", "from");
		map.put("MsgType", "type");
		TextMessage tm = new TextMessage(map, "你好");
		XStream xStream = new XStream();
		xStream.processAnnotations(TextMessage.class);
		String xml = xStream.toXML(tm);
		System.out.println(xml);
	}
	
}
