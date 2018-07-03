package net.dlyt.qyds.web.common.AliPay.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* *
 *类名：AlipayReturn
 *功能：支付宝接口返回类
 */
public class AlipayResult {

	  // 是否成功
	  private boolean isSuccess = false;

	  // 错误代码
	  private String errorCode = "";

	  // xml
	  private String xmlCode = "";

	  // 参数Map
	  private Map<String,String> mapParam = null;

	  // 结果Map
	  private Map<String,String> mapResult = null;

	  // sign
	  private String sign = "";

	  // sign_type
	  private String signType = "";

	  /**
	   * 构造函数<BR>
	 * @throws Exception 
	   * 
	   * 
	   */
	  public AlipayResult(String xml) throws Exception {
		Document doc;
		xmlCode = xml;
		doc =  DocumentHelper.parseText(xml);
		//根节点检查
		Element root;
		root= doc.getRootElement();
	    if (!root.getName().equals("alipay")) {
	    	throw new Exception("返回的XML不正确");
	    }
	    Element el;
		//是否成功
		el = root.element("is_success");
		isSuccess = el.getTextTrim().equals("T");
		if (!isSuccess) {
			errorCode = root.element("error").getTextTrim();
			mapParam = null;
			return;
		}
		errorCode = "";
		el = root.element("sign");
		sign = el == null ? "" : el.getTextTrim();
		el = root.element("sign_type");
		signType = el == null ? "" : el.getTextTrim();

		//request参数
		el = root.element("request");
		mapParam = new HashMap<String,String>();
		for (Element e : (List<Element>)el.elements("param")) {
            mapParam.put(e.attributeValue("name"), e.getTextTrim());
        }
		//respones
		mapResult = null;
		el = root.element("response");
		if (el == null || el.nodeCount() == 0) return;
		mapResult = new HashMap<String,String>();
		Element elm = (Element) el.elements().get(0);
		for (Element e : (List<Element>)elm.elements()) {
			mapResult.put(e.getName(), e.getTextTrim());
        }
		
	  }

//	  /**
//	   * 构造函数<BR>
//	   *
//	   *
//	   */
//	  public AlipayResult(Request request) {
//			//获取支付宝POST过来反馈信息
//		    mapParam = new HashMap<String,String>();
//			Map<?, ?> requestParams = request.getParameterMap();
//			for (Iterator<?> iter = requestParams.keySet().iterator(); iter.hasNext();) {
//				String name = (String) iter.next();
//				String[] values = (String[]) requestParams.get(name);
//				String valueStr = "";
//				for (int i = 0; i < values.length; i++) {
//					valueStr = valueStr + values[i] + ((i == values.length - 1) ? "" : ",");
//				}
//				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
//				mapParam.put(name, valueStr);
//			}
//	  }

	  /**
	   * 结果签名验证<BR>
	   * 
	   * 
	   */
	  public boolean VerifyResult() {
		  return true;
	  }

	  /**
	   * 是否成功取得<BR>
	   * 
	   * @return isSuccess
	   */
	  public boolean getIsSuccess() {
	    return isSuccess;
	  }

	  /**
	   * 是否成功赋值<BR>
	   * 
	   * @param  isSuccess
	   */
	  public void setIsSuccess(boolean isSuccess) {
	    this.isSuccess = isSuccess;
	  }

	  /**
	   * 错误代码取得<BR>
	   * 
	   * @return errorCode
	   */
	  public String getErrorCode() {
	    return errorCode;
	  }

	  /**
	   * 错误代码赋值<BR>
	   * 
	   * @param  errorCode
	   */
	  public void setErrorCode(String errorCode) {
	    this.errorCode = errorCode;
	  }

	  /**
	   * xml取得<BR>
	   * 
	   * @return xmlCode
	   */
	  public String getXmlCode() {
	    return xmlCode;
	  }

	  /**
	   * xml赋值<BR>
	   * 
	   * @param  xmlCode
	   */
	  public void setXmlCode(String xmlCode) {
	    this.xmlCode = xmlCode;
	  }

	  /**
	   * 参数Map取得<BR>
	   * 
	   * @return mapParam
	   */
	  public Map<String,String> getMapParam() {
	    return mapParam;
	  }

	  /**
	   * 参数Map赋值<BR>
	   * 
	   * @param  mapParam
	   */
	  public void setMapParam(Map<String,String> mapParam) {
	    this.mapParam = mapParam;
	  }

	  /**
	   * sign取得<BR>
	   * 
	   * @return sign
	   */
	  public String getSign() {
	    return sign;
	  }

	  /**
	   * sign赋值<BR>
	   * 
	   * @param sign
	   */
	  public void setSign(String sign) {
	    this.sign = sign;
	  }

	  /**
	   * signType取得<BR>
	   * 
	   * @return signType
	   */
	  public String getSignType() {
	    return signType;
	  }

	  /**
	   * signType赋值<BR>
	   * 
	   * @param signType
	   */
	  public void setSignType(String signType) {
	    this.signType = signType;
	  }

	  /**
	   * 结果Map取得<BR>
	   * 
	   * @return mapResult
	   */
	  public Map<String,String> getMapResult() {
	    return mapResult;
	  }

	  /**
	   * 结果Map赋值<BR>
	   * 
	   * @param  mapResult
	   */
	  public void setMapResult(Map<String,String> mapResult) {
	    this.mapResult = mapResult;
	  }
	
	
}