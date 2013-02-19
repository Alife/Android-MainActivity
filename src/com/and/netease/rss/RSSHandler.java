package com.and.netease.rss;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.text.Html;

public class RSSHandler extends DefaultHandler {

	private List<RSSItem> list;
	private RSSItem item;
	private String tag = "";
	
	private StringBuffer buffer;
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		if(item!=null){
			String data = new String(ch,start,length);
			if(tag.equals("title")){
				item.setTitle(data);
			}else if(tag.equals("link")){
				item.setLink(data);
			}else if(tag.equals("description")){
//				item.setDescription(data);
				buffer.append(Html.fromHtml(data));
			}else if(tag.equals("pubDate")){
				item.setPubDate(data);
			}
		}
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if(localName.equals("item")){
			item.setDescription(buffer.toString());
			list.add(item);
			item = null;
			buffer = null;
		}
		tag = "";
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		list = new ArrayList<RSSItem>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals("item")){
			item = new RSSItem();
			buffer = new StringBuffer();
		}
		tag = localName;
	}
	
	public List<RSSItem> getData(){
		return list;
	}

}
