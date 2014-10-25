package runner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import config.Config;

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);		
		System.out.println("Start Link: ");
		String start = scan.nextLine();
		System.out.println("End Link: ");
		String end = scan.nextLine();
		startSearch(start, end);
	}
	
	public static void startSearch(String start, String end)
	{
		Document doc = null;
		try {
			doc = Jsoup.connect(start).get();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Elements links = doc.select("a");
		ArrayList<String> URL = new ArrayList<String>();
		
		for (Element link : links)
		{
			URL.add(link.absUrl("href"));
		}
		sanitizeLinks(URL);
	}
	
	public static void sanitizeLinks(ArrayList<String> links){
		for (int i = 0; i < links.size(); i++){
			if (!links.get(i).startsWith(Config.BASE_URL)){
				links.remove(i);
				i--;
			}
			if(i>=0)
				System.out.println(links.get(i));
		}		
	}
}
