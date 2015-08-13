package com.kyee.keywordFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class KeywordTest {


	public static void main(String[] args) {
		KeyWordFilterByDFA keyWordFilterByDFA = KeyWordFilterByDFA.getInstance();
		keyWordFilterByDFA.setMaxOrMinType(true);
		keyWordFilterByDFA.setIgnoreCaseType(true);
		keyWordFilterByDFA.setReplaceChar('^');
		List<String> keywords = new ArrayList<String>();
		keywords.add("|+");
		keywords.add("+");
		keywords.add("?");
		keywords.add("中国|男人");
		keywords.add("法轮");
		keywords.add("中");
		keywords.add("fuck");
		keywords.add("S+B");
		keywords.add("APP");
		keywords.add("抢占");
		keywords.add("报告单");
		keyWordFilterByDFA.parseKeyWordsMap(keywords);
		String testString = "+中|国|男人中国|男人法法轮中国+++||??(){}[]|||||\\\\\\人哈哈，中法男人反倒|是 法!@#$%*(+)(*&^%$#@轮热舞功，中国人, fuck!!! s+b fUCking function";
		System.out.println(testString);
		String filterString = keyWordFilterByDFA.keywordsFilter(testString);
		System.out.println(filterString);
	}

	
	/*public static void main(String[] args) {
		List<String> keywords = new ArrayList<String>();
		keywords.add("中国人");
		keywords.add("中国男人");
		keywords.add("法轮");
		keywords.add("中");
		keywords.add("fuck");
		keywords.add("SB");
		keywords.add("APP");
		keywords.add("抢占");
		keywords.add("报告单");
		String regexString="中国人|中国男人|法轮|中|fuck|SB";
		Pattern pattern = Pattern.compile(regexString);
		String testString = "法法轮中国人哈哈，中法男人反倒是 法轮热舞功，中国人, fuck!!! sb fUCking function";
		System.out.println(testString);
		String filterString= pattern.matcher(testString).replaceAll("*");
		System.out.println(filterString);
	}*/
	
	
	
	
	
}
