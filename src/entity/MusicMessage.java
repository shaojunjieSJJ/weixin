package entity;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("xml")
public class MusicMessage extends BaseMessage{
	
	@XStreamAlias("Music")
	private Music music;

	public Music getMusic() {
		return music;
	}

	public void setMusic(Music music) {
		this.music = music;
	}

	public MusicMessage(Map<String, String> requestMap, Music music) {
		super(requestMap);
		this.music = music;
		this.setMsgType("music");
	}
	
	
	
	
}
