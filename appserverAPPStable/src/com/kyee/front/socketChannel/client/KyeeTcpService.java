package com.kyee.front.socketChannel.client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.kyee.front.socketChannel.PropertiesManager;

/**
 * <pre>
 * 描述：提供端服务器连接前置机方法的类 
 * 作者：fengqianning 
 * 时间：2015年5月7日上午9:45:24
 * 类名: KyeeTcpService
 * </pre>
 */
public class KyeeTcpService {

	/**
	 * <pre>
	 * 描述：端服务器连接前置机进行socket通讯调用的方法
	 * 作者：fengqianning 
	 * 时间：2015年5月7日上午9:45:27
	 * @param param
	 * @return
	 * returnType：String
	 * </pre>
	 * 
	 * @throws IOException
	 */
	public static String tcpService(String type, String json)
			throws IOException {
		String resultJsonString = "";
		BufferedReader socketIn = null;
		BufferedOutputStream socketOut = null;
		Socket socket = null;
		try {
			socket = new Socket(PropertiesManager.getIP(),
					PropertiesManager.getPORT());
			socketOut = new BufferedOutputStream(socket.getOutputStream());
			socketIn = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String paramString = getRequestXML(type, json);
			socketOut.write(getStringLength(paramString));// 发送长度信息
			socketOut.write(paramString.getBytes());// 发送参数字符串
			socketOut.flush();
			char[] charArray = new char[8192];
			StringBuilder strBuilder = new StringBuilder();
			int len = 0;
			strBuilder.append((char) socketIn.read());// 使用阻塞方法等待数据返回
			while (socketIn.ready()) {
				while ((len = socketIn.read(charArray, 0, charArray.length)) != -1) {
					strBuilder.append(charArray, 0, len);
				}
			}
			resultJsonString = strBuilder.toString();
		} finally {
			if (null != socketIn) {
				socketIn.close();
			}
			if (null != socketOut) {
				socketOut.close();
			}
			if (null != socket) {
				socket.close();
			}
		}
		return resultJsonString;
	}

	/**
	 * <pre>
	 * 描述：生成标准格式的请求XML参数
	 * 作者：fengqianning 
	 * 时间：2015年5月22日上午11:24:23
	 * @param type
	 * @param json
	 * @return
	 * returnType：String
	 * </pre>
	 */
	private static String getRequestXML(String type, String json) {
		StringBuffer result = new StringBuffer();
		result.append("<xml><type>");
		result.append(type);
		result.append("</type><json>");
		result.append(json);
		result.append("</json></xml>");
		return result.toString();
	}

	/**
	 * <pre>
	 * 描述：用来生成4字节的byte[]，存储发送字符串的长度
	 * 作者：fengqianning 
	 * 时间：2015年7月24日下午6:31:02
	 * @param sourceString
	 * @return
	 * returnType：byte[]
	 * </pre>
	 */
	private static byte[] getStringLength(String sourceString) {
		int sourceLength = sourceString.getBytes().length;
		byte[] lengthArray = new byte[4];
		for (int i = 0; i < lengthArray.length; i++) {
			lengthArray[i] = (byte) ((sourceLength >> (lengthArray.length - 1 - i) * 8) & 0xff);
		}
		return lengthArray;
	}

}
