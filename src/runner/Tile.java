package runner;

public class Tile {
	Tile parent;
	String url;
	int cost;
	boolean visited;
	
	public Tile(String url, Tile parent, int cost) {
		this.url = url;
		this.parent = parent;
		this.cost=cost;
		visited=false;
	}

	public String getUrl() {
		return url;
	}

	public Tile getParent() {
		return parent;
	}
	
	public boolean isVisited(){
		synchronized(this){
			return visited;
		}
	}
	
	public void setVisited(){
		synchronized(this){
			visited=true;
		}
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
