package entity;

public class ClickButton extends AbstractButon{

	private String type="click";
	private String key;
	public String getType() {
		return type;
	}
	public String getKey() {
		return key;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public ClickButton(String name, String key) {
		super(name);
		this.key = key;
	}
	
}
