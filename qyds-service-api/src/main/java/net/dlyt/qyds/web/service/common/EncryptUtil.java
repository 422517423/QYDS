package net.dlyt.qyds.web.service.common;

import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 
* @ClassName: EncryptUtil
* @Description: 异或进行加密解密算法 以及md5签名
* @date 2015年7月3日 下午3:38:20
 */
public class EncryptUtil {
	private static final Logger logger = Logger.getLogger(EncryptUtil.class);
	private static final int RADIX = 16;
	private static final String SEED = "0933910847463829232312312";

	
	/**
	* @Title: encrypt 
	* @Description: 对称加密 
	* @param @param password
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static final String encrypt(String password) {
		
		if(isEmpty(password)){
			return "";
		}

		BigInteger bi_passwd = new BigInteger(password.getBytes());

		BigInteger bi_r0 = new BigInteger(SEED);
		BigInteger bi_r1 = bi_r0.xor(bi_passwd);

		System.out.print(bi_r1.toString(RADIX));
		return bi_r1.toString(RADIX);
	}

	/**
	* @Title: decrypt 
	* @Description: 对称解密
	* @param @param encrypted
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static final String decrypt(String encrypted) {
		
		if(isEmpty(encrypted)){
			return "";
		}
		
		BigInteger bi_confuse = new BigInteger(SEED);

		try {
			BigInteger bi_r1 = new BigInteger(encrypted, RADIX);
			BigInteger bi_r0 = bi_r1.xor(bi_confuse);
			return new String(bi_r0.toByteArray());
		} catch (Exception e) {
			logger.error(e);
			return "";
		}
		
	}

	/**
	* @Title: encodeMessage 
	* @Description: md5签名
	* @param @param data
	* @param @return
	* @param @throws Exception    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String encodeMD5(String data) throws Exception {
		
		if(isEmpty(data)){
			return "";
		}
		
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(data.getBytes());
		return toHex(md5.digest());
		
	}

	private static String toHex(byte[] buffer) {
		byte[] result = new byte[buffer.length * 2];

		for (int i = 0; i < buffer.length; i++) {
			byte[] temp = getHexValue(buffer[i]);
			result[(i * 2)] = temp[0];
			result[(i * 2 + 1)] = temp[1];
		}
		return new String(result).toUpperCase();
	}

	private static byte[] getHexValue(byte b) {
		int value = b;
		if (value < 0) {
			value = 256 + b;
		}
		String s = Integer.toHexString(value);
		if (s.length() == 1) {
			return new byte[] { 48, (byte) s.charAt(0) };
		}
		return new byte[] { (byte) s.charAt(0), (byte) s.charAt(1) };
	}

	/**
	* @Title: isEmpty 
	* @Description: 判断是否为空
	* @param @param str
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	private static boolean isEmpty(String str){
		return str == null || str.length() == 0;
	}
	
	public static void main(String[] args) throws Exception{

		String contents = "orderNo=10250008765&cashierNo=00000000000000000000000046269027&payMoney=20000&payMode=020&payBank=CMBCHINA&payTime=20160312190111";
		/*1.对称加解密*/
		System.out.println("加密前：" + contents);

		String securitySign = EncryptUtil.encodeMD5(contents + "&key=9CBF8A4DCB8E3068");
		System.out.println("MD5：" + securitySign);

		String reqInfo = EncryptUtil.encrypt(contents + "&securitySign=" + securitySign);
		System.out.println("加密后：" + reqInfo);

		String params = EncryptUtil.decrypt(reqInfo);
		System.out.println("解密前：" + params);

		String contents2 = params.substring(0, params.indexOf("&securitySign="));
		System.out.println("解密后：" + contents2);
		String signInput = params.split("&securitySign=")[1];
		System.out.println("MD5：" + signInput);

		String orderStr = params.split("&")[0].split("=")[1];
//		String orderNo = orderStr.split("=")[1];
		System.out.println("orderStr：" + orderStr);
//		System.out.println("orderNo：" + orderNo);

	}



	
}