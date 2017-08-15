package seo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypesFactory;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.Link;
import org.apache.tika.sax.LinkContentHandler;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.codehaus.jackson.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.sleepycat.je.sync.impl.SyncCleanerBarrier.SyncTrigger;

@SuppressWarnings("unused")
public class Storage
{
	private final static Pattern FILTERS = Pattern
			.compile(".*\\.(jpg|xls|xlsx|doc|docx|ppt|pptx|pdf|mp3|jpeg)"
					+ "(\\?.*)?$");

	public static UUID saveTika(String url)
	{
		UUID uuid = null;
		try {
			boolean htmlFlag = true;
			File dir2 = new File("D:\\Winter 2016\\Search Engines - CS454\\CrawlerStorage");
			if (dir2.mkdir())
			{
			}
			if (!url.toLowerCase().contains("https"))
			{
				uuid = UUID.randomUUID();
				File directory = new File("D:\\Winter 2016\\Search Engines - CS454CrawlerStorage\\"
						+ uuid.toString());
				if (directory.mkdir()) {
					//System.out.println("Directory is created!");
				} else {
					//System.out.println("Failed to create directory!");
				}

				Tika tika = new Tika();
				tika.setMaxStringLength(10 * 1024 * 1024);
				Metadata met = new Metadata();
				URL objurl = new URL(url.toString());

				ToHTMLContentHandler toHTMLHandler = new ToHTMLContentHandler();
				ParseContext parseContext = new ParseContext();
				LinkContentHandler linkHandler = new LinkContentHandler();
				ContentHandler textHandler = new BodyContentHandler(
						10 * 1024 * 1024);
				TeeContentHandler teeHandler = new TeeContentHandler(
						linkHandler, textHandler, toHTMLHandler);

				AutoDetectParser parser = new AutoDetectParser();
				parser.parse(objurl.openStream(), teeHandler, met, parseContext);
				String type = met.get(Metadata.CONTENT_TYPE);
	
				if (url.toLowerCase().contains(".pdf")) {
					htmlFlag = false;
					URL website = new URL(url);
					ReadableByteChannel rbc = Channels.newChannel(website
							.openStream());
					@SuppressWarnings("resource")
					FileOutputStream fos = new FileOutputStream(
							".\\CrawlerStorage\\" + uuid.toString() + "\\"
									+ uuid.toString() + ".pdf");
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				}
				if (url.toLowerCase().contains(".css")) {
					htmlFlag = false;
				}
				if (type.equals("application/vnd.ms-powerpoint")) {
					htmlFlag = false;
					URL website = new URL(url);
					ReadableByteChannel rbc = Channels.newChannel(website
							.openStream());
					@SuppressWarnings("resource")
					FileOutputStream fos = new FileOutputStream(
							".\\CrawlerStorage\\" + uuid.toString() + "\\"
									+ uuid.toString() + ".ppt");
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				}
				if (type.equals("image/png")) {
					htmlFlag = false;
					URL website = new URL(url);
					ReadableByteChannel rbc = Channels.newChannel(website
							.openStream());
					@SuppressWarnings("resource")
					FileOutputStream fos = new FileOutputStream(
							".\\CrawlerStorage\\" + uuid.toString() + "\\"
									+ uuid.toString() + ".png");
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				}
				if (type.equals("image/jpeg")) {
					htmlFlag = false;
					URL website = new URL(url);
					ReadableByteChannel rbc = Channels.newChannel(website
							.openStream());
					@SuppressWarnings("resource")
					FileOutputStream fos = new FileOutputStream(
							".\\CrawlerStorage\\" + uuid.toString() + "\\"
									+ uuid.toString() + ".jpg");
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				}
				if (type.equals("image/gif")) {
					htmlFlag = false;
					URL website = new URL(url);
					ReadableByteChannel rbc = Channels.newChannel(website
							.openStream());
					@SuppressWarnings("resource")
					FileOutputStream fos = new FileOutputStream(
							".\\CrawlerStorage\\" + uuid.toString() + "\\"
									+ uuid.toString() + ".gif");
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				}
				if (type.equals("application/xml")) {
					htmlFlag = false;
					URL website = new URL(url);
					ReadableByteChannel rbc = Channels.newChannel(website
							.openStream());
					@SuppressWarnings("resource")
					FileOutputStream fos = new FileOutputStream(
							".\\CrawlerStorage\\" + uuid.toString() + "\\"
									+ uuid.toString() + ".xml");
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				}
				if (type.equals("image/vnd.microsoft.icon")) {
					htmlFlag = false;
					URL website = new URL(url);
					ReadableByteChannel rbc = Channels.newChannel(website
							.openStream());
					@SuppressWarnings("resource")
					FileOutputStream fos = new FileOutputStream(
							".\\CrawlerStorage\\" + uuid.toString() + "\\"
									+ uuid.toString() + ".icon");
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				}
				if (htmlFlag) {
					URL website = new URL(url);
					ReadableByteChannel rbc = Channels.newChannel(website
							.openStream());
					@SuppressWarnings("resource")
					FileOutputStream fos = new FileOutputStream(
							".\\CrawlerStorage\\" + uuid.toString() + "\\"
									+ uuid.toString() + ".html");
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return uuid;
	}

	@SuppressWarnings("unchecked")
	public void extractMetaData(String fileName, String url) {
		try {
			FileWriter metadataFile = new FileWriter(
					"D:/Winter 2016/Search Engines - CS454/CrawlerStorage/metadata.json", true);
			JSONObject json = new JSONObject();

			File dirLoc = new File("D:/Winter 2016/Search Engines - CS454/CrawlerStorage");
			for (File file : dirLoc.listFiles()) {
				if (file.isDirectory() && file.getName().equals(fileName)) {
					for (File jsonFile : file.listFiles()) {
						if (jsonFile.getName().equals(fileName + ".json")) {
							JSONParser jsonParser = new JSONParser();
							JSONObject jsonObject = (JSONObject) jsonParser
									.parse(new FileReader(jsonFile));

							json.put("URL", url);
							json.put("File Name", fileName);
							json.put("MetaData", jsonObject.get("Metadata"));

							metadataFile.write(json.toJSONString());
							metadataFile.write("\r\n");

						}
					}
				}
			}

			metadataFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    private static JSONArray jsonArr = new JSONArray();
	
	public static void saveArray() throws IOException{
		FileWriter file = new FileWriter("D:\\Winter 2016\\Search Engines - CS454\\CrawlerStorage\\Control.json",false);
		file.write(jsonArr.toJSONString());
		file.write("\r\n");
		file.flush();
		file.close();
		
	}

	@SuppressWarnings("unchecked")
	public void saveMetadata(String filePath, String url) throws SAXException, TikaException {
		try {
			Document doc = Jsoup.parse(new File(filePath), "utf-8");
			Web data = new Web();
			data.setUrl(url);
			data.setPath(filePath);
			Elements urlLinks = doc.select("a[href]");
			HashMap<String, String> linkMap = new HashMap<String, String>();
			for (Element ele : urlLinks) {
				if(!ele.attr("abs:href").equals(""))
					linkMap.put(ele.text(), ele.attr("abs:href"));
			}
			data.setLinks(linkMap);
			
			
			File f = new File(filePath);
			AutoDetectParser parser = new AutoDetectParser();
		      BodyContentHandler handler = new BodyContentHandler(10*1024*1024);
		      Metadata metadata = new Metadata();
		      try
		      {
		      FileInputStream inputstream = new FileInputStream(f);
		      ParseContext context = new ParseContext();
		      
		      parser.parse(inputstream, handler, metadata, context);
		      }
		      catch(Exception e)
		      {
		    	  e.printStackTrace();
		      }
		    
		      HashMap<String, String> metaDataMap = new HashMap<String, String>();
		      for(String key : metadata.names())
		      {
		    	  metaDataMap.put(key, metadata.get(key).toString());
		      }
		      data.setFileMetaData(metaDataMap);
			
			
			  data.createJSON();
			
    		  jsonArr.add(data.getJson());

		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}

	}
}
