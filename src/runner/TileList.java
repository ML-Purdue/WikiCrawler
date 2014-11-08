package runner;

import java.util.ArrayList;

public class TileList {

	ArrayList<Tile> tiles=new ArrayList<Tile>();
	int counter=0;
	public void add(Tile tile){
		if(!tiles.contains(tile)){
			tiles.add(tile);
		}
	}

	public Tile next(){
		Tile tile = null;
		synchronized(this){
			for(;counter<tiles.size();counter++)
			{
				if(!tiles.get(counter).isVisited())
				{
					tile=tiles.get(counter);
					tile.setVisited();
					break;
				}
			}
		}
		return tile;
	}

	public void addAll(ArrayList<Tile> tiles){
		synchronized(this){
			for(Tile tile:tiles){
				add(tile);
			}
		}
	}

	public int size(){
		synchronized(this){
			return tiles.size();}
	}
}