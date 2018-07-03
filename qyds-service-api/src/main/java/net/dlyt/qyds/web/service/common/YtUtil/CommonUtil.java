package net.dlyt.qyds.web.service.common.YtUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 通用工具类 （用于请求接口）
 * @author Administrator
 *
 */
public class CommonUtil{
	private static Logger log = LoggerFactory.getLogger(CommonUtil.class); 
	
	/** 
	* 发送https请求 
	* @param requestUrl 请求地址 
	* @param requestMethod 请求方式（GET、POST） 
	* @param outputStr 提交的数据 
	* @return 返回微信服务器响应的信息 
	*/ 
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) { 
		try {
			URL url = new URL(requestUrl); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true); 
			conn.setDoInput(true); 
			conn.setUseCaches(false); 
			// 设置请求方式（GET/POST） 
			conn.setRequestMethod(requestMethod); 
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
			// 当outputStr不为null时向输出流写数据 
				if (null != outputStr) { 
					OutputStream outputStream = conn.getOutputStream(); 
					// 注意编码格式 
					outputStream.write(outputStr.getBytes("UTF-8")); 
					outputStream.close(); 
				} 
			// 从输入流读取返回内容 
			InputStream inputStream = conn.getInputStream(); 
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8"); 
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader); 
			String str = null; 
			StringBuffer buffer = new StringBuffer(); 
			while ((str = bufferedReader.readLine()) != null) { 
			buffer.append(str); 
			} 
			// 释放资源 
			bufferedReader.close(); 
			inputStreamReader.close(); 
			inputStream.close(); 
			inputStream = null; 
			conn.disconnect(); 
			return buffer.toString(); 
		} catch (ConnectException ce) { 
			log.error("连接超时：{}", ce); 
		} catch (Exception e) { 
			log.error("https请求异常：{}", e); 
		} 
	return null; 
	}

	public static String urlEncodeUTF8(String source){
	    String result = source;
	    try {
	        result = java.net.URLEncoder.encode(source,"utf-8");
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	    return result;
	}
}