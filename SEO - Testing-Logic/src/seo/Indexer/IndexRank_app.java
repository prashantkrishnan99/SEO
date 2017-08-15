package seo;

import java.io.IOException;
import java.util.Scanner;

import org.json.simple.parser.ParseException;

public class IndexRank_app 
{
     public static void main(String args[])throws IOException, ParseException
     {
    	 Scanner sc = new Scanner(System.in);
    	 
    	 System.out.println("Enter the path :");
    	 String startPath = sc.nextLine();
    	 System.out.println("Enter the stopword :");
    	 String stopPath = sc.nextLine();
    	 
    	 Indexer index = new Indexer();
    	 index.getStopWords(stopPath);
    	 System.out.println("------------Indexing started-----------------");
    	 index.controlIndex(startPath);
    	 System.out.println("------------Indexing completed---------------");
    	 
    	 Ranker rank = new Ranker();
    	 System.out.println("------------Ranking started------------------");
    	 rank.start(startPath);
    	 System.out.println("------------Ranking Completed----------------");
     
     }
}
