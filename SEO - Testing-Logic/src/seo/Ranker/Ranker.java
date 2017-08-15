package seo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Ranker
{
      private int numOfLinks;
      
      private List<LinkInfo> allLinks = new ArrayList<LinkInfo>();
      
      private HashMap<String,Integer> incoming = new HashMap<String,Integer>();
      private HashMap<String,Integer> outgoing = new HashMap<String,Integer>();
      private HashMap<String,LinkInfo> record = new HashMap<String,LinkInfo>();
      
      private JSONArray array = new JSONArray();
      private JSONObject obj = new JSONObject();
      
      public void saveJSONArray() throws IOException
      {
    	  FileWriter file = new FileWriter(".\\rank.json",false);
    	  
    	  file.write(obj.toJSONString());
    	  file.write("\r\n");
    	  file.flush();
    	  file.close();
      }
      
      public void start(String filepath) throws FileNotFoundException, IOException, ParseException
      {
    	  File jsonFile = new File(filepath);
    	  
    	  JSONParser jsonparser = new JSONParser();
    	  JSONArray arr = (JSONArray) jsonparser.parse(new FileReader(jsonFile));
    	  numOfLinks = arr.size();
    	  
    	  JSONObject links;
  		 
  		  
  		  for(Object obj : arr)  
  		  {
  			  JSONObject jsonobject = (JSONObject) obj;
  			  List<String> outgoing = new ArrayList<String>();
  			  
  			  LinkInfo link = new LinkInfo();
  			  
  			  String path = (String) jsonobject.get("path");
  			  String temp[] = path.split("\\\\");
  			  
  			  link.setId(temp[temp.length-2]);
  			  	
  			  String url = (String) jsonobject.get("URL");
  			  links = (JSONObject) jsonobject.get("links");
  	
  			  for (Object l : links.keySet())
  			  {
  		 		  String linkHolder = (String) links.get(l);
  				  if (!linkHolder.equals(""))
  				  {
  					  outgoing.add(linkHolder);
  				  }
  			   }
  	
  			  link.setPath(path);
  			  link.setUrl(url);
  			  link.setGoingOut(outgoing);
  	
  			  allLinks.add(link);
  			  record.put(link.getUrl(), link);
  		}

  		int tempStore;

  		for (LinkInfo single : allLinks)
  		{
  			for (String eachUrl : single.getGoingOut())
  			{
  				if (incoming.containsKey(eachUrl))
  				{
  					tempStore = incoming.get(eachUrl);
  					incoming.put(eachUrl, ++tempStore);
  				} 
  				else
  				{
  					incoming.put(eachUrl, 1);
  				}
  				single.getIncoming().add(eachUrl);
  			}
  		}

  		for (String single : incoming.keySet()) {
  			if (record.containsKey(single)) {
  				record.get(single).setPointedBy(incoming.get(single));
  			}
  		}

  		begin();

  	}


  	  public void begin() throws IOException 
  	  {
  		
  		double defaultRank = 1.0 / numOfLinks;
  		
  		for (LinkInfo link : allLinks)
  		{
  			link.setRank(defaultRank);
  			link.setNewRank(defaultRank);
  		}

  		double rank;
  		double tempRank;
  		LinkInfo holder;
  		
  		for (int i = 0; i < 10; i++)
  		{
  			for (String url : record.keySet())
  			{
  				holder = record.get(url);
  				rank = 0;
  				for (String goingOut : holder.getGoingOut())
  				{
  					if (record.containsKey(goingOut))
  					{
  						tempRank = record.get(goingOut).getRank();
  						if (incoming.containsKey(goingOut))
  							if (incoming.get(goingOut) > 0)
  							{
  								tempRank = tempRank / incoming.get(goingOut);
  							}
  						rank = rank + tempRank;
  					} 
  					else 
  					{
  						rank = rank + defaultRank;
  					}
  				}
  				if (rank == 0.0)
  					rank = defaultRank;

  				holder.setNewRank(rank);
  				holder.setFinalRank1();
  			}
  			for (LinkInfo link : allLinks) 
  			{
  				link.copyRank();
  			}
  		}
  		
  		for (LinkInfo link : allLinks)
  		{
  			link.getFinalRank1();
  		}
  		
  		Ranking();

  	}
  	
  	  @SuppressWarnings("unchecked")
  	  public void Ranking() throws IOException 
  	  {	
  		double defaultRank = 1.0 / numOfLinks;
  		
  		for (LinkInfo link : allLinks)
  		{
  			link.setRank(defaultRank);
  			link.setNewRank(defaultRank);
  			outgoing.put(link.getUrl(), link.getGoingOut().size());
  		}

  		double rank;
  		double tempRank;
  		LinkInfo holder;
  		for (int i = 0; i < 10; i++) {
  		
  			for (String url : record.keySet()) 
  			{
  				holder = record.get(url);
  				rank = 0;
  				for (String incoming : holder.getIncoming())
  				{
  					if (record.containsKey(incoming))
  					{
  						tempRank = record.get(incoming).getRank();
  						if (outgoing.containsKey(incoming))
  							if (outgoing.get(incoming) > 0)
  							{
  								tempRank = tempRank / outgoing.get(incoming);
  							}
  						rank = rank + tempRank;
  					} 
  					else
  					{
  						rank = rank + defaultRank;
  					}
  				}
  				if (rank == 0.0)
  				{
  					rank = defaultRank;
  				}
  				
  				holder.setNewRank(rank);
  				holder.setFinalRank2();
  			}
  			for (LinkInfo link : allLinks)
  			{
  				link.copyRank();
  			}
  		}

  		for (LinkInfo link : allLinks) {
  			link.setFinalRank2();
  			link.round();
  			link.createJSON();
  			obj.put(link.getId(), link.getJson());
  			array.add(link.getJson());
  		}
  		saveJSONArray();
  	}
  	
  	  //code changes
  	  public void changeRanking() throws IOException {
  		System.out.println("Number of Links" + numOfLinks);
  		double defaultRank = 1.0 / numOfLinks;
  		System.out.println("Default Rank " + defaultRank);

  		for (LinkInfo link : allLinks) {
  			link.setRank(defaultRank);
  			link.setNewRank(defaultRank);
  		}

  		double rank;
  		double tempRank;
  		LinkInfo holder;
  		for (int i = 0; i < 10; i++) {
  			// System.out.println("Iteration number : "+i);
  			for (String url : record.keySet()) {
  				holder = record.get(url);
  				rank = 0;
  				for (String goingOut : holder.getGoingOut()) {
  					if (record.containsKey(goingOut)) {
  						tempRank = record.get(goingOut).getRank();
  						if (incoming.containsKey(goingOut))
  							if (incoming.get(goingOut) > 0)
  								tempRank = tempRank	/ incoming.get(goingOut);
  						rank = rank + tempRank;
  					} else {
  						rank = rank + defaultRank;
  					}
  				}
  				if (rank == 0.0)
  					rank = defaultRank;

  				// System.out.println("Rank : "+rank);

  				holder.setNewRank(rank);
  				holder.setFinalRank1();
  			}
  			for (LinkInfo link : allLinks) {
  				link.copyRank();
  			}
  		}
  		
  		for (LinkInfo link : allLinks) {
  			link.getFinalRank1();
  		}
  		
  		Ranking();
  	}
  		  
}
      

