package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.WxService;

/**
 * Servlet implementation class WxServlet
 */
@WebServlet("/wx")
public class WxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WxServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 *	参数 描述 
		 * signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
		 * timestamp 时间戳 
		 * nonce 随机数 
		 * echostr 随机字符串
		 */
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		
		//校验证签名
		if (WxService.check(timestamp,nonce,signature)) {
			PrintWriter out = response.getWriter();
			//原样返回echostr参数
			out.print(echostr);
			out.flush();
			out.close();
			System.out.println("接入成功");
		} else {
			System.out.println("接入失败");
		}
		
	}

	/**
	 * 接收消息和事件驱动
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//接收xml数据包
//		ServletInputStream is = request.getInputStream();
//		byte[] b = new byte[1024];
//		int len;
//		StringBuilder sb = new StringBuilder();
//		while ((len=is.read(b)) != -1) {
//			sb.append(new String(b,0,len));
//		}
//		System.out.println(sb.toString());
		
		request.setCharacterEncoding("utf8");
		response.setCharacterEncoding("utf8");
		//处理消息和事件推送
		Map<String, String> requestMap = WxService.parseRequest(request.getInputStream());
		//准备回复的数据包
//		String respxml = "<xml><ToUserName><![CDATA["+requestMap.get("FromUserName")+"]]></ToUserName><FromUserName><![CDATA["+requestMap.get("ToUserName")+"]]></FromUserName><CreateTime>"+System.currentTimeMillis()/1000+"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[why?]]></Content></xml>";
		String respxml = WxService.getRespose(requestMap);
		
		System.out.println(respxml);
		PrintWriter out = response.getWriter();
		out.print(respxml);
		out.flush();
		out.close();
		System.out.println(requestMap);
	}

}
