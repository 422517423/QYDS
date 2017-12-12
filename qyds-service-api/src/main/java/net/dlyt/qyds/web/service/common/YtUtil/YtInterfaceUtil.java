package net.dlyt.qyds.web.service.common.YtUtil;

import org.apache.commons.codec.binary.Base64;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public final class YtInterfaceUtil {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(YtInterfaceUtil.class);

    // class path
    private static String classPath = "";

    // loader
    private static ClassLoader loader = Thread.currentThread().getContextClassLoader();

    // PartnerID
    private static final String PARTNER_ID = "123456";

    private YtInterfaceUtil() {
    }

    public static void main(String[] args) {
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        // 客户编码（电商标识，由圆通人员给出）
        parameters.put("clientID", "TEST");
        // 物流公司ID（YTO）
        parameters.put("logisticProviderID", "YTO");
        parameters.put("customerId", "");

        // 物流号
        // 当前时间 yyyyMMddHHmmss
        //String currTime = XmlUtil.getCurrTime();
        // 8位日期
        //String strTime = currTime.substring(8, currTime.length());
        // 四位随机数
        //String strRandom = XmlUtil.buildRandom(4) + "";
        // 10位序列号,可以自行调整。
        //String txLogisticID = "LP"+strTime + strRandom;
        parameters.put("txLogisticID", "LP07082300225709");
        parameters.put("tradeNo", "2007082300225709");
        parameters.put("mailNo", "");
        parameters.put("totalServiceFee", "0.0");
        parameters.put("codSplitFee", "0.0");
        // 订单类型(0-COD,1-普通订单,3-退货单)
        parameters.put("orderType", "1");
        // 服务类型(1-上门揽收, 2-次日达 4-次晨达 8-当日达,0-自己联系)。（数据库未使用）（目前暂未使用默认为0）
        parameters.put("serviceType", "0");
        parameters.put("flag", "0");


       parameters.put("sendStartTime", "Sun Dec 03 2017 19:33:56 GMT 0800 (中国标准时间)");
       parameters.put("sendEndTime", "Sun Dec 03 2017 19:33:56 GMT 0800 (中国标准时间)");
        parameters.put("goodsValue", "1900");
        parameters.put("itemsValue", "2000");
        parameters.put("insuranceValue", "0.0");
        // 商品类型（保留字段，暂时不用，默认填0）
        parameters.put("special", "0");

        parameters.put("remark", "易碎品");


        String requestXML = XmlUtil.getRequestXml(parameters);

        // 对xml内容<order></order>进行URL编码（字符集UTF-8）为%3Corder%3E%3C%2Forder%3E
        // 将普通字符串转换成application/x-www-form-urlencoded字符串
        String logistics_interface = null;
        try {
            logistics_interface = java.net.URLEncoder.encode(requestXML,"UTF-8");
            System.out.println(logistics_interface);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // md5 加密 base64编码
        String data_digest="";
        byte[] b=MD5Util.getDigest((requestXML + PARTNER_ID).getBytes());
        Base64 b64= new Base64();
        data_digest=new String(b64.encode(b));
        System.out.println("md5加密后再base64:"+data_digest);

        String requestDate = "logistics_interface="+logistics_interface+"&data_digest="+data_digest+"&type=online&clientId=TEST";

        String result =CommonUtil.httpsRequest("http://jingangtest.yto56.com.cn/ordws/Vip16Servlet", "POST", requestDate);
        System.out.println(result.toString());
        Map<String, String> map=new HashMap<String, String>();
        try {
            map = XmlUtil.doXMLParse(result);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //解析微信返回的信息，以Map形式存储便于取值
        String txLogisticID1 = map.get("txLogisticID").toString();
        String success = map.get("success").toString();
        System.out.println(txLogisticID1);
        System.out.println(success);
    }

    /**
     *  创建圆通订单
     * @return
     */
    public static  String createYtOrder() {
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        // 客户编码（电商标识，由圆通人员给出）
        parameters.put("clientID", "TEST");
        // 物流公司ID（YTO）
        parameters.put("logisticProviderID", "YTO");
        // 物流号
        // 当前时间 yyyyMMddHHmmss
        String currTime = XmlUtil.getCurrTime();
        // 8位日期
        String strTime = currTime.substring(8, currTime.length());
        // 四位随机数
        String strRandom = XmlUtil.buildRandom(4) + "";
        // 10位序列号,可以自行调整。
        String txLogisticID = strTime + strRandom;
        parameters.put("txLogisticID", txLogisticID);
        // 订单类型(0-COD,1-普通订单,3-退货单)
        parameters.put("orderType", "1");
        // 服务类型(1-上门揽收, 2-次日达 4-次晨达 8-当日达,0-自己联系)。（数据库未使用）（目前暂未使用默认为0）
        parameters.put("serviceType", "0");
        // 用户姓名
        parameters.put("name", "zhag");
        // 用户邮编（如果没有可以填默认的0）
        parameters.put("postCode", "0");
        // 用户所在省
        parameters.put("prov","辽宁省");
        // 用户所在市、县（区），市和区中间用“,”分隔；注意有些市下面是没有区
        parameters.put("city", "大连市,高新园区");
        // 用户详细地址
        parameters.put("address", "普罗旺斯二期");
        // 商品名称（可填默认的0）
        parameters.put("itemName", "0");
        // 商品数量（可填默认的0）
        parameters.put("number", "0");
        // 商品类型（保留字段，暂时不用，默认填0）
        parameters.put("special", "0");
        String requestXML = XmlUtil.getRequestXml(parameters);
        System.out.println(requestXML.toString());

        // 对xml内容<order></order>进行URL编码（字符集UTF-8）为%3Corder%3E%3C%2Forder%3E
        // 将普通字符串转换成application/x-www-form-urlencoded字符串
        String logistics_interface = null;
        try {
            logistics_interface = java.net.URLEncoder.encode(requestXML,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 签名
        String data_digest = MD5Util.MD5Encode(requestXML + PARTNER_ID, "UTF-8");
        // 进行Base64转码
        byte[] baseString = Base64.encodeBase64(data_digest.getBytes());
        try {
            data_digest = java.net.URLEncoder.encode(new String(baseString),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String requestDate = "logistics_interface="+logistics_interface+"&data_digest="+data_digest+"&type=online&clientId=TEST";

        String result =CommonUtil.httpsRequest("http://jingangtest.yto56.com.cn/ordws/Vip16Servlet", "POST", requestDate);
        System.out.println(result.toString());
        Map<String, String> map=new HashMap<String, String>();
        try {
            map = XmlUtil.doXMLParse(result);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //解析微信返回的信息，以Map形式存储便于取值
        String txLogisticID1 = map.get("txLogisticID").toString();
        String success = map.get("success").toString();
        System.out.println(txLogisticID1);
        System.out.println(success);
        return "";
    }

    /**
     *  取消圆通订单
     * @param length
     * @return
     */
    public static final String delYtOrder(int length) {

        return "";
    }

    public static String getClassPath() {
        return classPath;
    }

    public static ClassLoader getLoader() {
        return loader;
    }
}
