package com.kyee.keywordFilter;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * <pre>
 * 描述：关键字过滤DFA算法工具类
 * 作者：fengqianning 
 * 时间：2015-7-13下午5:33:53
 * 类名: KeyWordFilterByDFA 
 * </pre>
*/
public class KeyWordFilterByDFA {

	/**
	 * 单例设计模式实例
	 */
	private static KeyWordFilterByDFA keyWordFilterByDFA;
	/**
	 * 最大或最小模式标识；默认为true(true：最大模式；false：最小模式)
	 */
	private static boolean MaxOrMinType = true;
	/**
	 * 大小写匹配模式标识；默认为true(true：忽略大小写匹配；false：考虑大小写匹配)
	 */
	private static boolean IgnoreCaseType = true;
	/**
	 * 用来替换关键字的特殊字符；默认为*
	 */
	private static char ReplaceChar = '*';
	/**
	 * keyWordsMap用来存放关键字构建的过滤集合(关键字Map)
	 */
	private static final HashMap<String, Object> keyWordsMap = new HashMap<String, Object>();

	
	/**
	 * 构造方法设置为私有，防止在外部新new对象
	 */
	private KeyWordFilterByDFA() {
	}
	
	/**
	 * <pre>
	 * 描述：获取一个DFA工具类实例
	 * 作者：fengqianning 
	 * 时间：2015-7-13下午5:35:51
	 * @return
	 * returnType：KeyWordFilterByDFA
	 * </pre>
	*/
	public static KeyWordFilterByDFA getInstance() {
		if (keyWordFilterByDFA == null) {
			syncInitInstance();
		}
		return keyWordFilterByDFA;
	}
	
	/**
	 * <pre>
	 * 描述：单例设计模式，保证在多线程调用中不影响性能的只创建一次
	 * 作者：fengqianning 
	 * 时间：2015-7-13下午6:04:38
	 * returnType：void
	 * </pre>
	*/
	private static synchronized void syncInitInstance() {
		// 此判断不可删除，否则会因为首次的多线程同时调用而被初始化多次
		if (keyWordFilterByDFA == null) {
			keyWordFilterByDFA = new KeyWordFilterByDFA();
		}
	}

	/**
	 * <pre>
	 * 描述：根据所给的关键字集合构建DFA所需的关键字Map
	 * 作者：fengqianning 
	 * 时间：2015-7-13下午6:25:35
	 * @param keywords
	 * returnType：void
	 * </pre>
	*/
	public void parseKeyWordsMap(List<String> keywords) {
		for (int i = 0; i < keywords.size(); i++) {
			String keyword = keywords.get(i);
			if (IgnoreCaseType) {
				// 忽略大小写模式下，所有的匹配都转为小写
				keyword = keyword.toLowerCase();
			}
			// 每个关键字初始都要从最外层开始遍历
			HashMap<String, Object> nowKeywordMap = keyWordsMap;
			for (int j = 0; j < keyword.length(); j++) {
				String word = String.valueOf(keyword.charAt(j));
				Object wordMap = nowKeywordMap.get(word);
				if (null != wordMap) {
					nowKeywordMap = (HashMap<String, Object>) wordMap;
				} else {
					HashMap<String, Object> newKeywordMap = new HashMap<String, Object>();
					// 默认不是最后一个字，如果是在此关键字循环完会修改为1
					newKeywordMap.put("isEnd", "0");
					nowKeywordMap.put(word, newKeywordMap);
					nowKeywordMap = newKeywordMap;// 此句可以和下面代码互换
				 // nowKeywordMap =(HashMap<String, Object>)nowKeywordMap.get(word);
				}
			}
			nowKeywordMap.put("isEnd", "1");
		}
	}
	
	/**
	 * <pre>
	 * 描述：从传入的位置匹配关键字，如果没有匹配到则返回0，否则返回匹配到的关键字长度
	 * 作者：fengqianning 
	 * 时间：2015-7-13下午7:07:28
	 * @param txt:源字符串
	 * @param beginIndex:字符串检测的起始位置
	 * @return
	 * returnType：int
	 * </pre>
	*/
	private int checkKeywords(String txt, int beginIndex) {
		int length = 0;
		int maxTypeLength = 0;
		HashMap<String, Object> nowKeywordMap = keyWordsMap;
		for (int i = beginIndex; i < txt.length(); i++) {
			String word = String.valueOf(txt.charAt(i));
			if (IgnoreCaseType) {
				// 忽略大小写模式下，所有的匹配都转为小写
				word = word.toLowerCase();
			}
			Object newKeywordMap = nowKeywordMap.get(word);
			if (newKeywordMap == null) {
				return maxTypeLength;
			} else {
				length++;
				nowKeywordMap = (HashMap<String, Object>) newKeywordMap;
				if ("1".equals((String) nowKeywordMap.get("isEnd"))) {
					if (MaxOrMinType) {
						// 如果是最大模式，则需要继续匹配下去，所以把当前暂且匹配到的关键字length赋值给maxTypeLength
						// 只有isEnd==1时，记录下的length才是应该返回的
						maxTypeLength = length;
					} else {
						// 最小模式在第一次匹配到isEnd==1时，则返回
						return length;
					}
				}
			}
		}
		return maxTypeLength;
	}
	
	/**
	 * <pre>
	 * 描述：按设置生成过滤后的字符串
	 * 作者：fengqianning 
	 * 时间：2015-7-13下午7:08:02
	 * @param txt
	 * @return
	 * returnType：String
	 * </pre>
	*/
	@Deprecated
	public String keywordsFilterDeprecated(String txt) {
		for (int i = 0; i < txt.length();) {
			int keywordIndex = checkKeywords(txt, i);
			if (keywordIndex == 0) {
				i++;
			} else {
				//此种替换方式不建议使用，虽然目前还没有发现这种方法有什么问题
				txt = txt.replaceFirst(
						getHandledString(txt.substring(i, i + keywordIndex)),
						getHandledString(getReplaceString(keywordIndex, ReplaceChar)));
				i = i + keywordIndex;
			}
		}
		return txt;
	}
	
	/**
	 * <pre>
	 * 描述：按设置生成过滤后的字符串
	 * 作者：fengqianning 
	 * 时间：2015年7月17日下午4:01:24
	 * @param txt
	 * @return
	 * returnType：String
	 * </pre>
	*/
	public String keywordsFilter(String txt) {
		for (int i = 0; i < txt.length();) {
			int keywordLength = checkKeywords(txt, i);
			if (keywordLength == 0) {
				i++;
			} else {
				//此为JAVA中String里面Replace的源码写法，建议使用此种方法
				txt = Pattern.compile(txt.substring(i, i + keywordLength),Pattern.LITERAL)
						.matcher(txt)
						.replaceFirst(Matcher.quoteReplacement(getReplaceString(keywordLength, ReplaceChar)));
				i = i + keywordLength;
			}
		}
		return txt;
	}
	
	/**
	 * <pre>
	 * 描述：对字符串进行特殊处理，防止含有正则表达式中的特殊字符而使替换失败或报错
	 * 作者：fengqianning 
	 * 时间：2015年7月15日下午3:38:57
	 * @param sourceString
	 * @return
	 * returnType：String
	 * </pre>
	*/
	private String getHandledString(String sourceString) {
		StringBuilder resultString = new StringBuilder();
		for (int i = 0; i < sourceString.length(); i++) {
			String sourceChar = String.valueOf(sourceString.charAt(i));
			switch (sourceChar) {
			case "*":
			case ".":
			case "?":
			case "+":
			case "$":
			case "^":
			case "[":
			case "]":
			case "(":
			case ")":
			case "{":
			case "}":
			case "|":
			case "/":
			case "\\":
				//如果是这些字符* . ? + $ ^ [ ] ( ) { } | \ / 则进行特殊处理
				resultString.append("\\");
			default:
				resultString.append(sourceChar);
			}
		}
		return resultString.toString();
	}
	
	/**
	 * <pre>
	 * 描述：获取关键字替换的替换字符串
	 * 作者：fengqianning 
	 * 时间：2015-7-13下午6:39:05
	 * @param number
	 * @param repalceChar
	 * @return
	 * returnType：String
	 * </pre>
	*/
	private String getReplaceString(int number, char repalceChar) {
		StringBuilder replaceString = new StringBuilder();
		for (int i = 0; i < number; i++) {
			replaceString.append(repalceChar);
		}
		return replaceString.toString();
	}

	/**
	 * <pre>
	 * 描述：获取关键字匹配模式(true:最大模式；false:最小模式)
	 * 作者：fengqianning 
	 * 时间：2015-7-13下午6:18:50
	 * @return
	 * returnType：boolean
	 * </pre>
	*/
	public boolean getMaxOrMinType() {
		return MaxOrMinType;
	}

	/**
	 * <pre>
	 * 描述：设置关键字匹配模式(true:最大模式；false:最小模式)
	 * 作者：fengqianning 
	 * 时间：2015-7-13下午6:18:44
	 * @param maxOrMinType
	 * returnType：void
	 * </pre>
	*/
	public void setMaxOrMinType(boolean maxOrMinType) {
		MaxOrMinType = maxOrMinType;
	}
	
	/**
	 * <pre>
	 * 描述：获取大小写匹配模式(true：忽略大小写匹配；false：考虑大小写匹配)
	 * 作者：fengqianning 
	 * 时间：2015-7-13下午6:29:39
	 * @return
	 * returnType：boolean
	 * </pre>
	*/
	public boolean getIgnoreCaseType() {
		return IgnoreCaseType;
	}

	/**
	 * <pre>
	 * 描述：设置大小写匹配模式(true：忽略大小写匹配；false：考虑大小写匹配)
	 * 作者：fengqianning 
	 * 时间：2015-7-13下午6:29:44
	 * @param ignoreCaseType
	 * returnType：void
	 * </pre>
	*/
	public void setIgnoreCaseType(boolean ignoreCaseType) {
		IgnoreCaseType = ignoreCaseType;
	}
	
	/**
	 * <pre>
	 * 描述：获取替换关键字的特殊字符
	 * 作者：fengqianning 
	 * 时间：2015-7-13下午6:46:35
	 * @return
	 * returnType：char
	 * </pre>
	*/
	public char getReplaceChar() {
		return ReplaceChar;
	}

	/**
	 * <pre>
	 * 描述：设置替换关键字的特殊字符
	 * 作者：fengqianning 
	 * 时间：2015-7-13下午6:46:39
	 * @param replaceChar
	 * returnType：void
	 * </pre>
	*/
	public void setReplaceChar(char replaceChar) {
		ReplaceChar = replaceChar;
	}
	
}
