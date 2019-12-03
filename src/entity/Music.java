package entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Music {

	@XStreamAlias("Title")
	private String title;
	
	@XStreamAlias("Description")
	private String description;
	
	@XStreamAlias("MusicURL")
	private String musicURL;
	
	@XStreamAlias("HQMusicUrl")
	private String hQMusicUrl;
	
	@XStreamAlias("ThumbMediaId")
	private String thumbMediaId;
	
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public String getMusicURL() {
		return musicURL;
	}
	public String gethQMusicUrl() {
		return hQMusicUrl;
	}
	public String getThumbMediaId() {
		return thumbMediaId;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setMusicURL(String musicURL) {
		this.musicURL = musicURL;
	}
	public void sethQMusicUrl(String hQMusicUrl) {
		this.hQMusicUrl = hQMusicUrl;
	}
	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}
	
	public Music(String title, String description, String musicURL, String hQMusicUrl, String thumbMediaId) {
		super();
		this.title = title;
		this.description = description;
		this.musicURL = musicURL;
		this.hQMusicUrl = hQMusicUrl;
		this.thumbMediaId = thumbMediaId;
	}
	
	
}
