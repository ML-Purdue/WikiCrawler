package runner;

public class Page {
	private Page parent;
	private String url;
	
	public Page(String url) {
		this.url = url;
	}

	public Page getParent() {
		return parent;
	}

	public void setParent(Page parent) {
		this.parent = parent;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
