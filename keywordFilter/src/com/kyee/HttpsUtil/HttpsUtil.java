package com.kyee.HttpsUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;



/** 
 * <pre>
 * 描述：提供HTTPS的访问和下载文件的工具类
 * 作者：fengqianning 
 * 时间：2015年8月26日下午10:29:33
 * 类名: HttpsDownloadUtil 
 * </pre>
*/
public class HttpsUtil {  
	
	/*public static void main(String[] args) {
		String requestUrl1 = "https://115.28.217.94:8443/QuyiPay/deal?reconDate=20150524&merchantId=777290058110356&signValue=a14f4e36f048bc9e35eaad73181abf7b&transCode=PAY0005&version=1.0.0";
		boolean flag = HttpsDownloadUtil.getHttpsDownloadFile(requestUrl1,"D:/text3.csv");
	}*/
  
	/**
	 * <pre>
	 * 描述：使用https下载文件
	 * 作者：fengqianning 
	 * 时间：2015年8月26日下午10:36:26
	 * @param requestUrl 请求地址 
	 * @param filePath 请求方式（GET、POST）
	 * returnType：void
	 * </pre>
	*/
	public static boolean getHttpsDownloadFile(String requestUrl, String filePath) {
		return HttpsUtil.httpsRequest(requestUrl, "POST", null,
				filePath);
	}	  

	/**
	 * <pre>
	 * 描述：使用https下载文件
	 * 作者：fengqianning
	 * 时间：2015年8月26日下午10:41:41
	 * @param requestUrl 请求地址 
	 * @param outputStr
	 * @param filePath 请求方式（GET、POST）
	 * @return
	 * returnType：boolean
	 * </pre>
	*/
	public static boolean getHttpsDownloadFileByParameters(String requestUrl,
			String outputStr, String filePath) {
		return HttpsUtil.httpsRequest(requestUrl, "POST", outputStr,
				filePath);
	}
	  
    /**
     * <pre>
     * 描述：发起https请求并获取结果
     * 作者：fengqianning 
     * 时间：2015年8月26日下午10:30:27
     * @param requestUrl 请求地址 
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据 
     * @param filePath  文件路
     * @return
     * returnType：String
     * </pre>
    */
    public static boolean httpsRequest(String requestUrl, String requestMethod, String outputStr, String filePath) {
		boolean flag = false;
    	// ======添加以下代码，避免证书名localhost错误的问题   start=========
    	System.setProperty("java.protocol.handler.pkgs", "javax.net.ssl");//设置系统参数
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				return urlHostName.equals(session.getPeerHost());
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
		StringBuffer buffer = new StringBuffer();
		HttpsURLConnection httpUrlConn = null;
		BufferedReader bufferedReader = null;
		FileWriter fileWriter = null;
        try {  
        	// 创建SSLContext对象，并使用我们指定的信任管理器初始化 
            TrustManager[] tm = { new MyX509TrustManager() };  
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
//          SSLContext sslContext = SSLContext.getInstance("TLS");  
            sslContext.init(null, tm, new java.security.SecureRandom());  
            // 从上述SSLContext对象中得到SSLSocketFactory对象 
            SSLSocketFactory ssf = sslContext.getSocketFactory();  
  
            URL url = new URL(requestUrl);  
            httpUrlConn = (HttpsURLConnection) url.openConnection();  
            httpUrlConn.setSSLSocketFactory(ssf); 
//          Accept: application/json,text/javascript,*/*;q=0.01
//          Content-Type: application/x-www-form-urlencoded
//          key: api_qyy_key_y4AkglccDq
            httpUrlConn.setRequestProperty("Accept", "application/json,text/javascript,*/*;q=0.01");
            httpUrlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//          httpUrlConn.setRequestProperty("key", "api_qyy_key_y4AkglccDq");
            httpUrlConn.setDoOutput(true);  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setUseCaches(false);  
            // 设置请求方式（GET/POST） 
            httpUrlConn.setRequestMethod(requestMethod);  
  
            if ("GET".equalsIgnoreCase(requestMethod))  
                httpUrlConn.connect();  
  
            // 当有数据需要提交时 
            if (null != outputStr) {  
                OutputStream outputStream = httpUrlConn.getOutputStream();  
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));  
                outputStream.close();  
            }  
  
            // 将返回的输入流转换成字符串   
			bufferedReader = new BufferedReader(new InputStreamReader(
					httpUrlConn.getInputStream(), "UTF-8"));
			char[] charArray = new char[8192];//临时缓冲区
			buffer.append((char) bufferedReader.read());// 阻塞读取
			int len = 0;
			while ((len = bufferedReader.read(charArray)) != -1) {
				buffer.append(charArray, 0, len);
			}
            File file=new File(filePath);
            if(!file.exists()){
				file.createNewFile();
				fileWriter = new FileWriter(file);
				fileWriter.write(buffer.toString());
				fileWriter.flush();
            }
			flag = true;
        } catch (Exception e) { 
//        	HLogger.error(e);
        } finally {
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (httpUrlConn != null) {
					httpUrlConn.disconnect();
				}
			} catch (IOException e) {
				// HLogger.error(e);
			}
        }
        return flag;
    }
    
    /**
     * <pre>
     * 描述：发起https请求并获取结果
     * 作者：fengqianning 
     * 时间：2015年8月27日下午2:26:42
     * @param requestUrl 请求地址 
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return
     * returnType：String :读取的返回结果
     * </pre>
    */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
    	// ======添加以下代码，避免证书名localhost错误的问题   start=========
    	System.setProperty("java.protocol.handler.pkgs", "javax.net.ssl");//设置系统参数
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				return urlHostName.equals(session.getPeerHost());
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
		StringBuffer buffer = new StringBuffer();
		HttpsURLConnection httpUrlConn = null;
		BufferedReader bufferedReader = null;
        try {  
        	// 创建SSLContext对象，并使用我们指定的信任管理器初始化 
            TrustManager[] tm = { new MyX509TrustManager() };  
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
//          SSLContext sslContext = SSLContext.getInstance("TLS");  
            sslContext.init(null, tm, new java.security.SecureRandom());  
            // 从上述SSLContext对象中得到SSLSocketFactory对象 
            SSLSocketFactory ssf = sslContext.getSocketFactory();  
  
            URL url = new URL(requestUrl);  
            httpUrlConn = (HttpsURLConnection) url.openConnection();  
            httpUrlConn.setSSLSocketFactory(ssf); 
//          Accept: application/json,text/javascript,*/*;q=0.01
//          Content-Type: application/x-www-form-urlencoded
//          key: api_qyy_key_y4AkglccDq
            httpUrlConn.setRequestProperty("Accept", "application/json,text/javascript,*/*;q=0.01");
            httpUrlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//          httpUrlConn.setRequestProperty("key", "api_qyy_key_y4AkglccDq");
            httpUrlConn.setDoOutput(true);  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setUseCaches(false);  
            // 设置请求方式（GET/POST） 
            httpUrlConn.setRequestMethod(requestMethod);  
  
            if ("GET".equalsIgnoreCase(requestMethod))  
                httpUrlConn.connect();  
  
            // 当有数据需要提交时 
            if (null != outputStr) {  
                OutputStream outputStream = httpUrlConn.getOutputStream();  
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));  
                outputStream.close();  
            }  
  
            // 将返回的输入流转换成字符串   
			bufferedReader = new BufferedReader(new InputStreamReader(
					httpUrlConn.getInputStream(), "UTF-8"));
			char[] charArray = new char[8192];//临时缓冲区
			buffer.append((char) bufferedReader.read());// 阻塞读取
			int len = 0;
			while ((len = bufferedReader.read(charArray)) != -1) {
				buffer.append(charArray, 0, len);
			}           
        } catch (Exception e) { 
//        	HLogger.error(e);
        } finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (httpUrlConn != null) {
					httpUrlConn.disconnect();
				}
			} catch (IOException e) {
				// HLogger.error(e);
			}
        }
        return buffer.toString();
    }
    
	public static class MyX509TrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
    
}  

