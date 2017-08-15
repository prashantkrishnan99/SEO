package seo;


import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;

public class File
{
	String url;
	String date;
	String filename;
	String localFilename;
	String type;
	
	Element elements;
	
	JSONObject json;

	public File() {
		super();
		// TODO Auto-generated constructor stub
	}

	public File(String url, String date, String filename,
			String localFilename, String type, Element elements, JSONObject json)
	{
		super();
		this.url = url;
		this.date = date;
		this.filename = filename;
		this.localFilename = localFilename;
		this.type = type;
		this.elements = elements;
		this.json = json;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}

	
	public String getLocalFilename() {
		return localFilename;
	}
	public void setLocalFilename(String localFilename) {
		this.localFilename = localFilename;
	}

	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	
	public Element getElements() {
		return elements;
	}
	public void setElements(Element elements) {
		this.elements = elements;
	}

	
	public JSONObject getJson() {
		return json;
	}
	public void setJson(JSONObject json) {
		this.json = json;
	}

	@SuppressWarnings("unchecked")
	public void createJSON()
	{
		this.json.put("url", this.url);
		this.json.put("date", this.date);
		this.json.put("filename", this.filename);
		this.json.put("localFilename", this.localFilename);
		this.json.put("type", this.type);
		this.json.put("elements", this.elements);
	}
}