package seo;

import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.FileReader;
	import java.io.IOException;
	import java.util.HashMap;
	import java.util.Map;

	import org.apache.tika.exception.TikaException;
	import org.json.simple.JSONArray;
	import org.json.simple.JSONObject;
	import org.json.simple.parser.JSONParser;
	import org.json.simple.parser.ParseException;
	import org.xml.sax.SAXException;

public class Extractor
{
	public void walk(String jsonPath) throws FileNotFoundException, IOException, ParseException, SAXException, TikaException {
			System.out.println("hiiI"+jsonPath);
			File root1 = new File(jsonPath);
			String path = root1.getParentFile().toString();
			path = path+"\\";
			Storage objstorage = new Storage();
	        File jsonFile = new File(jsonPath);
	        JSONParser jsonParser = new JSONParser();
			JSONArray jsonArr = (JSONArray) jsonParser.parse(new FileReader(jsonFile));
			
			Map<File, String> urlMap = new HashMap<File, String>();
			
			JSONObject jsonObject;
			
			String key, value;
			key = path;
			
			for(Object obj : jsonArr)
			{
				//System.out.println("in for lop");
				jsonObject = (JSONObject) obj;
				key = key + (String) jsonObject.get("filename")+ "\\";
				value = (String) jsonObject.get("url");
				urlMap.put(new File(key),value );
				key = path;
			}
			
			if (urlMap.keySet() == null)
				return;

			for (File f : urlMap.keySet()) {
				System.out.println(f.getName());
			System.out.println("inforlopp");
				File[] list = f.listFiles();
				System.out.println(list);
				for(File f1 : list)
				{
					System.out.println("in last for loooppp");
					objstorage.saveMetadata(f1.getAbsolutePath(), urlMap.get(f));
					}
			}
			System.out.println("end");
			Storage.saveArray();
			
		}
}
