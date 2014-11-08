package runner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import config.Config;

public class Main {

	static TileList tileList=new TileList();
	static Tile curTile;
	public static void main(String[] args) {
		String end;
		if(Config.DEBUG){
			tileList.add(new Tile(Config.DEBUG_START_URL,null,0));
			end=Config.DEBUG_END_URL;

		}else{
			Scanner scan = new Scanner(System.in);		
			System.out.println("Start Link: ");
			String start = scan.nextLine();
			tileList.add(new Tile(start,null,0));
			System.out.println("End Link: ");
			end = scan.nextLine();
		}
		setupFirstPage();
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for(int i=0;i<10;i++)
			executorService.execute(new Runnable() {
				public void run() {
					curTile=startSearch(end);
				}
			});

		executorService.shutdown();
		System.out.println("FOUND!!!");
		while(curTile!=null)
		{
			System.out.println(curTile.getUrl());
			curTile=curTile.getParent();
		}
	}

	private static void setupFirstPage() {
		Tile tile=tileList.next();
		System.out.println(Thread.currentThread().getId()+": "+tile.getUrl());
		Document doc = getDocument(tile.getUrl());
		ArrayList<String> tempURLs=getURLsFromDocument(doc);
		sanitizeLinks(tempURLs);
		tileList.addAll(generateTiles(tile,tempURLs));		
	}

	public static Tile startSearch(String end)
	{	
		Tile tile;
		while((tile=tileList.next())!=null){
			System.out.println(Thread.currentThread().getId()+": "+tile.getUrl());
			if(tile.getUrl().equals(end)){
				System.out.println(Thread.currentThread().getId()+" found it!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				return tile;
			}else{
				Document doc = getDocument(tile.getUrl());
				if(doc==null)
					continue;
				ArrayList<String> tempURLs=getURLsFromDocument(doc);
				sanitizeLinks(tempURLs);
				tileList.addAll(generateTiles(tile,tempURLs));
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
