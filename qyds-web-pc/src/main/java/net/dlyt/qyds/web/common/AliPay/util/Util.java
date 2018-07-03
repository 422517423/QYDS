
package net.dlyt.qyds.web.common.AliPay.util;

import net.dlyt.qyds.web.common.AliPay.httpClient.HttpProtocolHandler;
import net.dlyt.qyds.web.common.AliPay.httpClient.HttpRequest;
import net.dlyt.qyds.web.common.AliPay.httpClient.HttpResponse;
import net.dlyt.qyds.web.common.AliPay.httpClient.HttpResultType;
import net.dlyt.qyds.web.common.AlipayConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/* *
 *类名：UtilDate
 *功能：自定义订单类
 *详细：工具类，可以用作获取系统日期、订单编号等
 *版本：3.3
 *日期：2012-08-17
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */
public class Util {
	
	/**
	 * 判断空字符串
	 * @return
	 */
	public static boolean isNullString(String s) {
		if (s == null) return true;
		if (s.equals("")) return true;
		return false;
	}

    /** 
     * 除去map中的空值和签名参数
     * @param pMap 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> mapFilter(Map<String, String> pMap) {

        Map<String, String> result = new HashMap<String, String>();

        if (pMap == null || pMap.size() <= 0) {
            return result;
        }

        for (String key : pMap.keySet()) {
            String value = pMap.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }
	
    /**
     * 生成签名结果
     * @param para 要签名的数组
     * @return 签名结果字符串
     */
	public static String getSign(Map<String, String> para) {
    	// 拼接map
    	String strLink = getLinkString(para);
        return sign(strLink, AlipayConfig.key, AlipayConfig.sign_type,AlipayConfig.input_charset);
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    private static String sign(String text, String key, String sign_type, String input_charset) {
    	text = text + key;
        if (sign_type.equals("MD5")) {
        	return DigestUtils.md5Hex(getContentBytes(text, input_charset));
        } else if(sign_type.equals("SHA")) {
    		return DigestUtils.sha256Hex(getContentBytes(text, input_charset));
    	} else {
    		return "";
        }
    }
    
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verifySign(String text, String sign, String key, String sign_type,String input_charset) {
    	String mysign = sign(text, key, sign_type,input_charset);
		return mysign.equals(sign);
    }
    
    /**
     * http提交
     * @param url 提交地址
     * @param para 签名结果
     * @return 结果
     * @throws HttpException
     * @throws IOException
     */
    public static String httpSubmit(String url, Map<String, String> para) throws HttpException, IOException {

        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();
        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        //设置编码集
        request.setCharset(AlipayConfig.input_charset);
        request.setParameters(getNameValuePair(para));
        request.setUrl(url);

        HttpResponse response = httpProtocolHandler.execute(request,"","");
        return response == null ? "" : response.getStringResult();
    }
    
    /**
     * http提交
     * @param url 提交地址
     * @param para
     * @return 签名结果
     * @throws IOException 
     * @throws HttpException 
     */
    public static void httpSubmitNoReturn(String url, Map<String, String> para) throws HttpException, IOException {

        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();
        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        //设置编码集
        request.setCharset(AlipayConfig.input_charset);
        request.setParameters(getNameValuePair(para));
        request.setUrl(url);

        httpProtocolHandler.execute(request,"","");
    }

    /**
     * MAP类型数组转换成NameValuePair类型
     * @param para  MAP类型数组
     * @return NameValuePair类型数组
     */
    private static NameValuePair[] getNameValuePair(Map<String, String> para) {
        NameValuePair[] nameValuePair = new NameValuePair[para.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : para.entrySet()) {
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }

        return nameValuePair;
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("指定的编码集不对:" + charset);
        }
    }

    /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param para 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    private static String getLinkString(Map<String, String> para) {

        List<String> keys = new ArrayList<String>(para.keySet());
        Collections.sort(keys);

        StringBuffer strReturn = new StringBuffer();
        String key = "";
        String value = "";
        for (int i = 0; i < keys.size(); i++) {
            key = keys.get(i);
            value = para.get(key);
            if (isNullString(value)) continue;
            if (!isNullString(strReturn.toString())) {
            	strReturn.append("&");
            }
        	strReturn.append(key).append("=").append(value);
        }
        return strReturn.toString();
    }
	
	/**
	 * 获取系统当前日期(精确到毫秒)，格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getDateFormatter(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}
	public static String joinStringList(List<String> orders) {
		if (orders == null || orders.size() == 0) return "";
		StringBuffer stRtn = new StringBuffer();
		for (int i=0;i<orders.size();i++) {
			stRtn.append(i>0?",'":"'").append(orders.get(i)).append("'");
		}
		return stRtn.toString();
	}
	
}
