package runner;

import java.util.ArrayList;

public class TileList {
	ArrayList<Tile> tiles=new ArrayList<Tile>();
	
	public void add(Tile tile){
		if(!tiles.contains(tile)){
			tiles.add(tile);
		}
	}
	
	public Tile get(int i){
		return tiles.get(i);
	}
	
	public void addAll(ArrayList<Tile> tiles){
		for(Tile tile:tiles){
			add(tile);
		}
	}
	
	public int size(){
		return tiles.size();
	}
}