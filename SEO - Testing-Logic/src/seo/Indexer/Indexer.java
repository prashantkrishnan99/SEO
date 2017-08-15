package seo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Indexer
{
     public Map<String, List<PageIndexer>> wordcount = new HashMap<String, List<PageIndexer>>();
     public static List<String> stopWords = new ArrayList<String>();
     
     //called from the application.java program,
     //with stop string path as the parameter
	 public List<String> getStopWords(String path)
	 {
		 try 
		 {
				FileReader inputFile = new FileReader(path);
				@SuppressWarnings("resource")
				BufferedReader br = new BufferedReader(inputFile);

				String line;

				while ((line = br.readLine()) != null)
				{
					StringTokenizer tokenizer = new StringTokenizer(line, ",");
					while (tokenizer.hasMoreTokens())
					{
						stopWords.add(tokenizer.nextToken().trim());
					}
				}
		 }
		 catch (Exception e)
		 {
				System.out.println("Error while reading file  by line:"
						+ e.getMessage());
		 } 
		 
		 return stopWords; 
     }
     
     
     //called from the application.java program after making a call to the getStopWords function, 
	 //having path string as a parameter.
     public void controlIndex(String path)
     {   	 
    	 JSONParser parser = new JSONParser();
    	 
    	 try
    	 {
    		 JSONArray array = (JSONArray)parser.parse(new FileReader(path));
    		 
    		 JSONObject jsonObject = new JSONObject();
    		 JSONObject metaObj;
    		 
    		 for(Object obj : array)
    		 {
    			 jsonObject = (JSONObject) obj;
    			 String p = jsonObject.get("p").toString().replaceAll("\\\\", "/");
    			 metaObj = (JSONObject) jsonObject.get("metadata");
    			 
    			 String title = "";
    			 String description = "";  
    			 if(metaObj.get("title")!=null)
    			 {
    				 title = metaObj.get("title").toString();
    			 }
    			 if(metaObj.get("description")!=null)
    			 {
    				 description = metaObj.get("description").toString();
    			 }
    			 
    			 String id = p.substring(p.lastIndexOf("/")+1,p.lastIndexOf("."));
    			 
    			 performIndex(p,id,title,description);
    			 performMetaIndex(jsonObject.get("metadata"),id);
    		 }
    		 fileWriter();
    		 }
    	 catch(Exception e)
    	 {   	
    		 e.printStackTrace();
    	 }
     }
       
	 private void performMetaIndex(Object object, String id)
	 {
		 JSONObject jsonObject = (JSONObject) object;
		 
		 if(jsonObject.get("title")!=null)
		 {
			 String title = jsonObject.get("title").toString();
			 StringTokenizer stringtokenizer = new StringTokenizer(title,"-.");
			 while(stringtokenizer.hasMoreElements())
			 {
				 String element = stringtokenizer.nextToken();
				 addIndex2Map(element,id,"",title);
				 addTitle2HashMap(element,title,id,"");
			 }
		 }
		 if(jsonObject.get("description")!=null)
		 {
			 String description = jsonObject.get("description").toString();
			 String title = "";
			 if(jsonObject.get("title")!=null)
			 {
				 title = jsonObject.get("title").toString();
			 }
				 StringTokenizer stringtokenizer = new StringTokenizer(description," .");
				 while(stringtokenizer.hasMoreElements())
				 {
					 String element = stringtokenizer.nextToken();
					 addIndex2Map(element,id,title,description);
					 addTitle2HashMap(element,"",id,description);
				 }
		 }
		  if(jsonObject.get("Author")!=null)
		  {
			  String author = jsonObject.get("Author").toString();
			  String title = "";
			  if(jsonObject.get("title")!=null)
			  {
				  title = jsonObject.get("title").toString();
			  }
			  StringTokenizer stringtokenizer = new StringTokenizer(author," ,.");
			  while(stringtokenizer.hasMoreElements())
			  {
				  String element = stringtokenizer.nextToken();
				  addIndex2Map(element,id,"",title);
			  }
		  }
	 }

	 private void addTitle2HashMap(String element, String title, String id,
			String string)
	 {
	 }

	 private void addIndex2Map(String element, String id, String description,String title)
	 { 
		 List<PageIndexer> pageWordList = new ArrayList<PageIndexer>();
		 PageIndexer pageIndex = new PageIndexer();
		 
		 if(wordcount.get("element")==null)
		 {
			 pageIndex.setuUId(id);
			 pageIndex.setWordCount(1);
			 pageIndex.setTitle(title);
			 pageIndex.setDescription(description);
			 pageWordList.add(pageIndex);
			 wordcount.put(element, pageWordList);
		 }
		 else
		 {
			 pageWordList = wordcount.get(element);
			 boolean flag = true;
			 
			 for(PageIndexer p : pageWordList)
			 {
				 if(p.getuUId().equalsIgnoreCase(id))
				 {
					int count = p.getWordCount();
					p.setWordCount(++count);
					flag = false;
				 }
			 }
			 if (flag)
			 {
					pageIndex = new PageIndexer();
					pageIndex.setuUId(id);
					pageIndex.setWordCount(1);
					pageIndex.setDescription(description);
					pageIndex.setTitle(title);
					pageWordList.add(pageIndex);
			 }
			 wordcount.put(element,pageWordList);
		 }
	 }

	 private void performIndex(String p, String id, String title,String description) 
	 {
		 try
		 {
			 Parser parser = new AutoDetectParser();
			 
			 BodyContentHandler contentHandler = new BodyContentHandler(10*1024*1024);
			 Metadata metadata = new Metadata();
			 FileInputStream inputstream = new FileInputStream(p);
			 ParseContext context = new ParseContext();
             
			 parser.parse(inputstream, contentHandler, metadata, context);	
			 
			 StringTokenizer stringtokenizer = new StringTokenizer(contentHandler.toString().replaceAll("\\s+", " "), " .,-");
			 
			 while(stringtokenizer.hasMoreElements())
			 {
				 String element = stringtokenizer.nextToken();
				 element = Element(element);
				 if(!stopWords.equals(element) && checkElements(element)==false && element.length()>2 && isNumber(element)==false)
				 {
					 addIndex2Map(element,id,"",title);
					 addTitle2HashMap(element,title,id,description);
				 }
			 }
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
	 }

	 private boolean isNumber(String element)
	 {
		    String regex = "[0-9]+";
			if (element.matches(regex))
				return true;

			return false;
	 }


	 private boolean checkElements(String element)
	 {
		 if (element.equals("#") || element.equals("&") || element.equals("+") || element.equals("-") || element.equals("")
					             || element.equals("|") || element.equals(".") || element.equals("\\\\"))
				return true;
		return false;
     }
 

	 private String Element(String element) 
	 {	
			element = element.toLowerCase();
			element = element.trim();

			if (element.endsWith(".")) 
			{
				element = element.substring(0, element.lastIndexOf("."));
			}
			else if (element.endsWith(" .")) 
			{
				element = element.substring(0, element.lastIndexOf(" ."));
			}
			else if (element.endsWith(" ,"))
			{
				element = element.substring(0, element.lastIndexOf(" ,"));
			} 
			else if (element.endsWith(","))
			{
				element = element.substring(0, element.lastIndexOf(","));
			}

			return element;
		
  	 }

	 
     @SuppressWarnings("unchecked")
	 private void fileWriter() throws IOException 
     {
    	JSONArray jsonArray;
 		List<PageIndexer> pageIndex;
 		JSONObject jsonObj;
 		JSONObject jsonPage = new JSONObject();

 		for (String word : wordcount.keySet()) {
 			jsonArray = new JSONArray();
 			pageIndex = wordcount.get(word);
 			double totalSize = pageIndex.size();
 			double termFreq = 0.0;
 			for (PageIndexer page : pageIndex) {
 				jsonObj = new JSONObject();
 				jsonObj.put("id", page.getuUId().toString());
 				jsonObj.put("Count", Integer.toString(page.getWordCount()));
 				termFreq = page.getWordCount() / totalSize;
 				termFreq = Double.parseDouble(new DecimalFormat("##.##")
 						.format(termFreq));
 				jsonObj.put("TermFrequency", Double.toString(termFreq));
 				jsonObj.put("TitleRank", Integer.toString(page.getTitleRank()));
 				jsonObj.put("Description", page.getDescription());
 				jsonObj.put("Title", page.getTitle());
 				
 				jsonArray.add(jsonObj);
 			}
 			jsonPage.put(word, jsonArray);
 		}

 		FileWriter file = new FileWriter("./indexer.json", false);
 		
 	    try 
 		{
 			file.write(jsonPage.toJSONString());
 			System.out.println("Successfully Copied JSON Object to File...");
        }
 		catch (IOException e) 
 		{
 			e.printStackTrace();
 		} 
 		finally 
 		{
 			file.flush();
 			file.close();
 		}
	 }
}
