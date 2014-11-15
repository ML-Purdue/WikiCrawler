package runner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;


public class Main {

	public static void main(String[] args) {
		if(config.Config.DEBUG){
			startSearch(config.Config.DEBUG_START_URL, config.Config.DEBUG_END_URL);
		}else{
			Scanner scan = new Scanner(System.in);		
			System.out.println("Start Link: ");
			String start = scan.nextLine();
			System.out.println("End Link: ");
			String end = scan.nextLine();
			startSearch(start, end);
		}
	}

	public static void startSearch(String start, String end)
	{
//		Document doc = null;
//		try {
//			doc = Jsoup.connect(start).get();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return;
//		}
//		
//		Elements links = doc.select("a");
//		ArrayList<String> URL = new ArrayList<String>();
//		
//		for (Element link : links)
//		{
//			URL.add(link.absUrl("href"));
//		}
//		sanitizeLinks(URL);
		
		// HazelCast stuff
		Config cfg = new Config();
		HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
		
		
		long startTime = System.currentTimeMillis();
		
		Set<String> visitedLinks = instance.getSet("visitedLinks");
		
		Page endPage = null;
		
		Queue<Page> pages = instance.getQueue("pages");
		Page startPage = new Page(start);
		visitedLinks.add(start);
		pages.add(startPage);
		while (!pages.isEmpty()) {
			Page page = pages.remove();
			System.out.println(page.getUrl());
			if (page.getUrl().equals(end)) {
				endPage = page;
				break;
			}
			
			Document doc = null;
			try {
				doc = Jsoup.connect(page.getUrl()).get();
			} catch (org.jsoup.HttpStatusException e) {
				// We got a 404
				continue;
			} catch (IOException e) {
				e.printStackTrace();
			}
			Elements links = doc.select("a");
			HashSet<String> urls = new HashSet<String>();
			
			for (Element link : links) {
				String url = link.absUrl("href");
				if (url.startsWith(config.Config.BASE_URL) && !url.contains("User") && !url.contains("#cite_")) {
					urls.add(url);
				}
			}
			boolean found = false;
			for (String url : urls) {
				if (!visitedLinks.contains(url)) {
					//System.out.println(url);
					visitedLinks.add(url);
					Page nextPage = new Page(url);
					nextPage.setParent(page);
					pages.add(nextPage);
					if (nextPage.getUrl().equals(end)) {
						endPage = nextPage;
						found = true;
						break;
					}
				}
			}
			if (found) {
				break;
			}
			//System.out.println(page.getUrl() + ": " + urls.size());
			
		}
		
		System.out.println("DONE!!!!!!!!!!!!");
		
		Page curPage = endPage;
		while (curPage != null) {
			System.out.println(curPage.getUrl());
			curPage = curPage.getParent();
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("Time: " + (endTime-startTime)/1000.0);
	}

//	public static void sanitizeLinks(ArrayList<String> links){
//		for (int i = 0; i < links.size(); i++){
//			if (!links.get(i).startsWith(Config.BASE_URL)) { //|| links.get(i).contains("User") || links.get(i).contains("Help") || links.get(i).contains("EContact") || links.get(i).contains("ERecentChanges") || links.get(i).contains("ECommunity") || links.get(i).contains("ECurrent") || links.get(i).contains("EAbout") || links.get(i).contains("EFeatured") || links.get(i).contains("EContents")){
//				links.remove(i);
//				i--;
//			}
//			if(i>=0)
//				System.out.println(links.get(i));
//		}
//		
//	}
}
