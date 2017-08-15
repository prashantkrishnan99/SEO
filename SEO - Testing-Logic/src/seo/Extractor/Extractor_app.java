package seo;

import java.util.Scanner;

public class Extractor_app 
{
	public static void main(String[] args)
		{
			try
			{
				/*Scanner sc = new Scanner(System.in);
				System.out.println("Enter start dir: ");
				String startDir = sc.nextLine();*/
				//String startDir = args[0];
				//System.out.println(args[0]);
				System.out.println("Extraction Started...");
				new Extractor().walk("D:\\Winter 2016\\Search Engines - CS454\\CrawlerStorage\\map.json");
				System.out.println("Extraction completed and written to Control.json");
			} 
			catch (Exception ex) {
			
			}
		}
}
