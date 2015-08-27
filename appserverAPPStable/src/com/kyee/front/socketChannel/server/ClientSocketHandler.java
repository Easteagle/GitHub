package com.kyee.front.socketChannel.server;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;

import com.kyee.front.socketChannel.PropertiesManager;
import com.kyee.front.socketChannel.util.Util;

/**
 * <pre>
 * 描述：处理前置机接收到的socket的子线程
 * 作者：fengqianning 
 * 时间：2015年5月6日上午10:35:34
 * 类名: ClientSocketHandler
 * </pre>
 */
public class ClientSocketHandler extends Thread {

	/**
	 * 前置机接收到的端服务器socket信息
	 */
	private Socket socket;

	public ClientSocketHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		BufferedInputStream socketIn = null;
		BufferedWriter socketOut = null;
		try {
			socketIn = new BufferedInputStream(socket.getInputStream());
			socketOut = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			byte[] lengthArray = new byte[4];
			socketIn.read(lengthArray, 0, 4);// 使用阻塞方法来等待数据的返回(读取存储入参长度的4字节)
			int len = 0;// 存储接收到的参数长度
			//解析入参的长度
			for (int i = 0; i < lengthArray.length; i++) {
				len += ((int) lengthArray[i] & 0xff) << (lengthArray.length - 1 - i) * 8;
			}
			byte[] byteArray = new byte[len]; // 接收入参的byte数组
			socketIn.read(byteArray, 0, len);// 使用阻塞方法来等待数据的返回(按照长度读取入参的值)
			// 解析接收到的xml格式参数
			Map<String, String> paramMap = Util.parseParm(new String(byteArray, 0, len));
			// 加载调用的服务类
			BusinessService businessService = (BusinessService) Class.forName(
					PropertiesManager.getSERVICECLASS()).newInstance();
			// 调用方法后的返回结果
			String resultString = businessService.service(paramMap);
			socketOut.write(resultString);
			socketOut.flush();
			socket.shutdownOutput();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (socketIn != null) {
					socketIn.close();
				}
				if (socketOut != null) {
					socketOut.close();
				}
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
