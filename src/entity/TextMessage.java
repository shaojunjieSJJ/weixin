package entity;

import java.util.Map;

public class TextMessage extends BaseMessage{

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public TextMessage(Map<String, String> requestMap, String content) {
		super(requestMap);
		//设置文本消息的msgType为text
		this.setMsgType("text");
		this.content = content;
	}

	public TextMessage(Map<String, String> requestMap) {
		super(requestMap);
	}
	
	
}
