package com.kyee.front.socketChannel.server;

import java.util.Map;

/**
 * <pre>
 * 描述：前置服务接收消息后的处理接口
 * 作者：fengqianning 
 * 时间：2015年5月7日上午9:43:51
 * 类名: BusinessService
 * </pre>
 */
public interface BusinessService {

	/**
	 * <pre>
	 * 描述：前置服务接收消息后的处理方法 
	 * 作者：fengqianning
	 * 作者：fengqianning 
	 * 时间：2015年5月22日上午10:27:43
	 * @param paramsMap
	 * @return
	 * @throws Exception
	 * returnType：String
	 * </pre>
	 */
	public String service(Map<String, String> paramsMap) throws Exception;

}
