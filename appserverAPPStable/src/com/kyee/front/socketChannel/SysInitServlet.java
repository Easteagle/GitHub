package com.kyee.front.socketChannel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/** 
 * <pre>
 * 描述：socketChannel通讯的初始化
 * 作者：fengqianning 
 * 时间：2015年5月6日上午10:22:49
 * 类名: SysInitServlet 
 * </pre>
*/
public class SysInitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		new PropertiesManager().init();
	}

}
