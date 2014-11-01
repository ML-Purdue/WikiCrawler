package runner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import config.Config;

public class Main {

	public static void main(String[] args) {
		Tile goal;
		if(Config.DEBUG){
			goal=startSearch(Config.DEBUG_START_URL, Config.DEBUG_END_URL);
		}else{
			Scanner scan = new Scanner(System.in);		
			System.out.println("Start Link: ");
			String start = scan.nextLine();
			System.out.println("End Link: ");
			String end = scan.nextLine();
			goal=startSearch(start, end);
		}
		Tile curTile=goal;
		System.out.println("FOUND!!!");
		while(curTile!=null)
		{
			System.out.println(curTile.getUrl());
			curTile=curTile.getParent();
		}
	}

	public static Tile startSearch(String start, String end)
	{	
		TileList tiles=new TileList();
		tiles.add(new Tile(start,null,0));
		for(int i=0;i<tiles.size();i++){
			Tile tile=tiles.get(i);
			if(tile.getUrl().equals(end)){
				return tile;
			}else{
				Document doc = getDocument(tile.getUrl());
				if(doc==null)
					continue;
				ArrayList<String> tempURLs=getURLsFromDocument(doc);
				sanitizeLinks(tempURLs);
				tiles.addAll(generateTiles(tile,tempURLs));
			}
		}
		return null;
	}
	private static ArrayList<Tile> generateTiles(Tile parent,ArrayList<String> tempURLs) {
		ArrayList<Tile> tiles=new ArrayList<Tile>();
		for(String url:tempURLs){
			tiles.add(new Tile(url,parent,parent.getCost()+1));
		}
		return tiles;
	}

	public static Document getDocument(String url){
		try {
			return  Jsoup.connect(url).get();
		} catch (IOException e) {
			System.err.println("Error on "+url);
		}
		return null;
	}

	public static ArrayList<String> getURLsFromDocument(Document doc){
		ArrayList<String> URLs = new ArrayList<String>();
		Elements links = doc.select("a");
		for (Element link : links)
		{
			URLs.add(link.absUrl("href"));
		}
		return URLs;
	}
	public static void sanitizeLinks(ArrayList<String> links){
		for (int i = 0; i < links.size(); i++){
			if (!links.get(i).startsWith(Config.BASE_URL)){
				links.remove(i);
				i--;
			}
		}		
	}
}
