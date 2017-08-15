package seo;

import java.io.IOException;
import java.util.Scanner;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Crawler_app
{
	public static void main(String[] args)throws IOException
	{
        Scanner sc = new Scanner(System.in);
		
		/*System.out.println("Enter the depth :");
		int depth = sc.nextInt();
		System.out.println("Enter the URL string :");
		String url = sc.nextLine();*/
		int depth = Integer.parseInt(args[0]);
		String url = args[1];

		CrawlConfig crawlConfig = new CrawlConfig();
		crawlConfig.setMaxDepthOfCrawling(depth);
		// crawlConfig.setMaxDepthOfCrawling(2);
		crawlConfig.setMaxPagesToFetch(100);
		crawlConfig.setCrawlStorageFolder("C:\\storage\\demo");
		crawlConfig.setIncludeBinaryContentInCrawling(true);
		
		PageFetcher pageFetcher = new PageFetcher(crawlConfig);
		
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,pageFetcher);
		
		try 
		{
			CrawlController controller = new CrawlController(crawlConfig,pageFetcher,robotstxtServer);
			
			Crawler.setDepth(depth);
			System.out.println("Crawling Started...for URL: "+url);
			
			controller.addSeed(url);
			controller.start(Crawler.class, 10); 
			
			Crawler.writeMap();
			
			System.out.println("Crawling completed...Writing to Map.json");
			
			System.out.println("Map.json created...");
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
