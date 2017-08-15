package seo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.net.URL;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler extends WebCrawler
{
	@SuppressWarnings("unused")
	private final static Pattern FILTERS = Pattern
			.compile(".*\\.(bmp|gif|png|tiff?|ico|xaml|pict|rif|pptx?|ps"
					+ "|mid|mp2|mp4|wav|wma|au|aiff|flac|ogg|3gp|aac|amr|au|vox"
					+ "|avi|mov|mpe?g|ra?m|m4v|smil|wm?v|swf|aaf|asf|flv|mkv"
					+ "|zip|rar|gz|7z|aac|ace|alz|apk|arc|arj|dmg|jar|lzip|lha)"
					+ "(\\?.*)?$");

	private static int depth;
	private static Set<String> domains = new HashSet<String>();

	private final static Storage storage = new Storage();
	private static HashMap<String, UUID> urlMapper = new HashMap<String, UUID>();

	public boolean shouldVisit(Page referringPage, WebURL url) 
	{
		String href = url.getURL().toLowerCase();

		try
		{
			@SuppressWarnings("unused")
			URL objurl = new URL(href);
			if (domains.size() < depth)
				domains.add(url.getDomain());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		if (domains.contains(url.getDomain())) 
		{
			// System.out.println("Accepted URL : " + href + "  Domain : " +
			// url.getDomain());
			return true;
		}
		else
		{
			// System.out.println("Rejected URL : " + href + "  Domain : " +
			// url.getDomain());
			return false;
		}

	}

	@SuppressWarnings("unused")
	public void visit(Page page) 
	{
		String url = page.getWebURL().getURL();

		@SuppressWarnings("static-access")
		UUID filename = storage.saveTika(url);
		if (filename != null)
			urlMapper.put(url, filename);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			@SuppressWarnings("unchecked")
			Set<WebURL> links = (Set<WebURL>) htmlParseData.getOutgoingUrls();

			// System.out.println("Text length: " + text.length());
			// System.out.println("Html length: " + html.length());
			// System.out.println("Number of outgoing links: " + links.size());
		}
	}

	@SuppressWarnings("unchecked")
	public static void writeMap()
	{
		try
		{
			FileWriter file = new FileWriter("D:\\Winter 2016\\Search Engines - CS454\\CrawlerStorage\\map.json");
			JSONObject json;
			@SuppressWarnings("unused")
			Storage storage = new Storage();
			JSONArray jsonArr = new JSONArray();
			String newUrl;

			for (String name : urlMapper.keySet()) {
				json = new JSONObject();
				newUrl = StringEscapeUtils.unescapeEcmaScript(name);
				json.put("url", newUrl);
				json.put("filename", urlMapper.get(name) + "");
				jsonArr.add(json);

			}
			file.write(jsonArr.toJSONString());
			file.write("\r\n");

			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setDepth(int d) {
		depth = d + 1;
	}
}
