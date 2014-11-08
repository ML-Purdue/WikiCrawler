package runner;

public class Tile {
	Tile parent;
	String url;
	int cost;
	public Tile(String url, Tile parent, int cost) {
		this.url = url;
		this.parent = parent;
		this.cost=cost;
	}

	public String getUrl() {
		return url;
	}

	public Tile getParent() {
		return parent;
	}

	public int getCost(){
		return cost;
	}
	
	public boolean equals(Object arg0) {	
		if(arg0 instanceof Tile){
			return url.equals(((Tile) arg0).getUrl());
		}
		return false;
	}	
}
