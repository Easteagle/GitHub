package com.kyee.front.socketChannel.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/** 
 * <pre>
 * 描述：工具类
 * 作者：fengqianning 
 * 时间：2015年5月6日下午3:44:33
 * 类名: Util 
 * </pre>
*/
public class Util {

	/**
	 * <pre>
	 * 描述：解析参数XML的方法
	 * 作者：fengqianning 
	 * 时间：2015年5月6日下午3:44:45
	 * @param xmlString
	 * @return
	 * returnType：Map<String,String>
	 * </pre>
	*/
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseParm(String xmlString) {
		Document document = null;
		Map<String, String> paraMap = new HashMap<String, String>();
		try {
			document = DocumentHelper.parseText(xmlString);
			Element root = document.getRootElement();
			Iterator<Element> iter = root.elementIterator();
			while (iter.hasNext()) {
				Element element = iter.next();
				paraMap.put(element.getName(), element.getText());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return paraMap;
	}
	
	
	/**
	 * <pre>
	 * 描述：解析XML配置文件的方法
	 * 作者：fengqianning 
	 * 时间：2015年7月28日下午8:44:06
	 * @param xmlString
	 * @return
	 * returnType：Map<String,String>
	 * </pre>
	*/
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseConfingXML(String xmlString) {
		Document document = null;
		Map<String, String> paraMap = new HashMap<String, String>();
		try {
			document = DocumentHelper.parseText(xmlString);
			Element root = document.getRootElement();
			Iterator<Element> iter = root.elementIterator();
			while (iter.hasNext()) {
				Element element = iter.next();
				paraMap.put(element.elementText("key"),element.elementText("value"));
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return paraMap;
	}

}
