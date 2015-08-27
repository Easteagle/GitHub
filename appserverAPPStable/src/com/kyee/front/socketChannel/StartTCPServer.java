package com.kyee.front.socketChannel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.kyee.front.socketChannel.server.MultiThreadServer;

/** 
 * <pre>
 * 描述：前置机socket服务监听启动
 * 作者：fengqianning 
 * 时间：2015年5月6日上午10:29:02
 * 类名: StartTCPServer 
 * </pre>
*/
public class StartTCPServer extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		new Thread(new MultiThreadServer()).start();
	}

}
