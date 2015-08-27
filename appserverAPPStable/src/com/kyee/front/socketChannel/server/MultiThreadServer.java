package com.kyee.front.socketChannel.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.kyee.front.socketChannel.PropertiesManager;

/**
 * <pre>
 * 描述：启动前置机服务监听
 * 作者：fengqianning 
 * 时间：2015年5月6日上午10:29:46
 * 类名: MultiThreadServer
 * </pre>
 */
public class MultiThreadServer implements Runnable {

	public void run() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(PropertiesManager.getPORT());
			// 启动前置服务监听
			while (true) {
				socket = serverSocket.accept();
				new Thread(new ClientSocketHandler(socket)).start();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally{
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
