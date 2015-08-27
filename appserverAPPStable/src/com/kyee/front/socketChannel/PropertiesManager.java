package com.kyee.front.socketChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import com.kyee.front.socketChannel.util.Util;

/**
 * <pre>
 * 描述：加载配置文件中的参数
 * 作者：fengqianning 
 * 时间：2015年5月7日上午9:29:25
 * 类名: PropertiesManager
 * </pre>
 */
public class PropertiesManager {

	/**
	 * 前置机IP地址
	 */
	private static String IP;

	/**
	 * 前置机监听端口
	 */
	private static String PORT;

	/**
	 * 前置机的服务实现类
	 */
	private static String SERVICECLASS;

	/**
	 * <pre>
	 * 描述：配置文件初始化加载
	 * 作者：fengqianning 
	 * 时间：2015年5月7日上午9:29:30
	 * returnType：void
	 * 修改描述：增加了从项目配置文件中读取配置的过程，
	 * 主要是为了防止现场升级覆盖掉tcp-config-manager.properties中的配置导致连接失败
	 * 修改人：fengqianning
	 * 修改时间：2015年7月29日 13:27:35
	 * </pre>
	 */
	public void init() {
		Properties Properties = new Properties();
		InputStream inputStream = null;
		FileReader projectConfigFileReader = null;
		try {
			inputStream = PropertiesManager.class
					.getClassLoader().getResourceAsStream(
							"tcp-config-manager.properties");
			if (inputStream != null) {
				// 读取到配置文件时，直接使用配置文件赋值
				Properties.load(inputStream);
				getPropertiesValue(Properties);
			} else {
				// 否则赋值为默认值
				IP = "127.0.0.1";
				PORT = "9999";
				SERVICECLASS = "com.kyee.front.socketChannel.BusinessServiceController";
			}
			URL url = PropertiesManager.class.getClassLoader()
					.getResource("project-config.xml");
			if (url != null) {
				//如果读到project-config.xml的配置文件，说明是端服务器，以sysConfig.xml中的配置文件为准
				File configFile = new File(url.getFile());
				projectConfigFileReader = new FileReader(configFile);
				StringBuilder xmlContent = new StringBuilder();
				char[] charArray = new char[8192];
				int len = 0;
				while ((len = projectConfigFileReader.read(charArray)) != -1) {
					xmlContent.append(charArray, 0, len);
				}
				Map<String, String> projectConfigMap = Util
						.parseConfingXML(new String(xmlContent.toString().getBytes(),"UTF-8"));
				//获取projectHome的配置路径(d:/web-config/app)
				String projectHomePath = projectConfigMap.get("projectHome");
				if (projectHomePath != null) {
					//生成系统配置文件路径(d:/web-config/app/config/tcp-config-manager.properties)
					File sysFile = new File(projectHomePath + File.separator
							+ "config" + File.separator	+ "tcp-config-manager.properties");
					//系统配置文件是否存在
					if (sysFile.exists()) {
						inputStream = new FileInputStream(sysFile);
						Properties.load(inputStream);
						String socketHost = Properties.getProperty("SOCKET_HOST");
						String socketPort = Properties.getProperty("SOCKET_PORT");
						if (socketHost != null && !socketHost.trim().isEmpty()) {
							IP = socketHost;
						}
						if (socketPort != null && !socketPort.trim().isEmpty()) {
							PORT = socketPort;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (projectConfigFileReader != null) {
					projectConfigFileReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * <pre>
	 * 描述： 获取配置文件中的配置参数，如果获取不到配置文件中的参数，将会使用默认值
	 * 作者：fengqianning 
	 * 时间：2015年5月7日上午9:32:17
	 * @param Properties
	 * returnType：void
	 * </pre>
	 */
	private void getPropertiesValue(Properties Properties) {
		String ip = Properties.getProperty("IP");
		if (null == ip || ip.trim().isEmpty()) {
			IP = "127.0.0.1";
		} else {
			IP = ip.trim();
		}
		String port = Properties.getProperty("PORT");
		if (null == port || port.trim().isEmpty()) {
			PORT = "9999";
		} else {
			PORT = port.trim();
		}
		String serviceclass = Properties.getProperty("SERVICECLASS");
		if (null == serviceclass || serviceclass.trim().isEmpty()) {
			SERVICECLASS = "com.kyee.front.socketChannel.BusinessServiceController";
		} else {
			SERVICECLASS = serviceclass.trim();
		}
	}

	public static String getIP() {
		return IP;
	}

	public static void setIP(String iP) {
		IP = iP;
	}

	public static int getPORT() {
		return Integer.parseInt(PORT);
	}

	public static void setPORT(String pORT) {
		PORT = pORT;
	}

	public static String getSERVICECLASS() {
		return SERVICECLASS;
	}

	public static void setSERVICECLASS(String sERVICECLASS) {
		SERVICECLASS = sERVICECLASS;
	}

}
