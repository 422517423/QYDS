package net.dlyt.qyds.web.service.common.YtUtil;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 通用工具类 （用于请求接口）
 * @author Administrator
 *
 */
public class XmlUtil{
	/**
	 * 获取支付随机码
	 * @return
	 */
	public static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }
	
	/**
	 * 获取订单查询随机码
	 * @return
	 */
	public static String create_nonce_check_str() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
	
   /**
    * 获取微信支付时间戳
    * @return
    */
    public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    /**
     * 获取预支付ID时  获取随机码
     * @param length
     * @return
     */
    public static String CreateNoncestr(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < length; i++) {
            Random rd = new Random();
            res += chars.indexOf(rd.nextInt(chars.length() - 1));
        }
        return res;
    }
    /**
     * 获取预支付ID时  获取随机码
     * @return
     */
    public static String CreateNoncestr() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < 16; i++) {
            Random rd = new Random();
            res += chars.charAt(rd.nextInt(chars.length() - 1));
        }
        return res;
    }
    
    /**
     * @author Mark
     * @Description：sign签名
     * @param parameters 请求参数
     * @return
     */
    public static String createSign(SortedMap<Object,Object> parameters){
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        //sb.append("key=" + ConfigUtil.API_KEY);
        String sign = MD5Util.MD5Encode(sb.toString(),"UTF-8").toUpperCase();
        return sign;
    }

    /**
     * @author Mark
     * @Description：将请求参数转换为xml格式的string
     * @param parameters  请求参数
     * @return
     */
    public static String getRequestXml(SortedMap<Object,Object> parameters){
        StringBuffer sb = new StringBuffer();
        sb.append("<RequestOrder>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
           // if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)||"sign".equalsIgnoreCase(k)) {
                /*sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">")*/;
          /*  }else {
                sb.append("<"+k+">"+v+"</"+k+">");
            }*/
            sb.append("<"+k+">"+v+"</"+k+">");
        }

        // ---------------------------- 测试 ------------------------------------

        Map<String, String> map = new HashMap<String, String>();
        // 用户姓名
        map.put("senderName", "张三");
        map.put("senderPhone", "231234134");
        map.put("senderMobile", "13575745195");
        // 用户邮编（如果没有可以填默认的0）
        map.put("senderPostCode", "310013");
        // 用户所在省
        map.put("senderProv","上海");
        // 用户所在市、县（区），市和区中间用“,”分隔；注意有些市下面是没有区
        map.put("senderCity", "上海,浦东区");
        // 用户详细地址
        map.put("senderAddress", "新龙科技大厦9层");

        map.put("receiverName", "李四");
        // 用户邮编（如果没有可以填默认的0）
        map.put("receiverPostCode", "100000");
        map.put("receiverPhone", "231234134");
        map.put("receiverMobile", "");
        // 用户所在省
        map.put("receiverProv","北京");
        // 用户所在市、县（区），市和区中间用“,”分隔；注意有些市下面是没有区
        map.put("receiverCity", "北京市,朝阳区");
        // 用户详细地址
        map.put("receiverAddress", "新龙科技大厦9层");

        sb.append(generateUserXml(map));

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        map.clear();
        // 商品名称（可填默认的0）
        map.put("name", "Nokia N73");
        // 商品数量（可填默认的0）
        map.put("number", "2");
        map.put("price", "2");

        data.add(map);
        sb.append(generateItemXml(data));

        // ---------------------------- 测试 ------------------------------------


        sb.append("</RequestOrder>");
        return sb.toString();
    }
    /**
     * @author Mark
     * @Description：返回给微信的参数
     * @param return_code 返回编码
     * @param return_msg  返回信息
     * @return
     */
    public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code
                + "]]></return_code><return_msg><![CDATA[" + return_msg
                + "]]></return_msg></xml>";
    }
    
    
    /**
	 * 锟斤拷取锟斤拷前时锟斤拷 yyyyMMddHHmmss
	 * @return String
	 */ 
	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}
	
	/**
	 * 取锟斤拷一锟斤拷指锟斤拷锟斤拷锟饺达拷小锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟?
	 * 
	 * @param length
	 *            int 锟借定锟斤拷取锟斤拷锟斤拷锟斤拷锟侥筹拷锟饺★拷length小锟斤拷11
	 * @return int 锟斤拷锟斤拷锟斤拷傻锟斤拷锟斤拷锟斤拷
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}

    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     * @param strxml
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public static Map doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if(null == strxml || "".equals(strxml)) {
            return null;
        }

        Map m = new HashMap();

        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if(children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }

        //关闭流
        in.close();

        return m;
    }

    /**
     * 获取子结点的xml
     * @param children
     * @return String
     */
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }

    /**
     * description: 解析微信通知xml
     *
     * @param xml
     * @return
     * @author ex_yangxiaoyi
     * @see
     */
    @SuppressWarnings({ "unused", "rawtypes", "unchecked" })
    public static Map parseXmlToList(String xml) {
        Map retMap = new HashMap();
        try {
            StringReader read = new StringReader(xml);
            // 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
            InputSource source = new InputSource(read);
            // 创建一个新的SAXBuilder
            SAXBuilder sb = new SAXBuilder();
            // 通过输入源构造一个Document
            Document doc = (Document) sb.build(source);
            Element root = doc.getRootElement();// 指向根节点
            List<Element> es = root.getChildren();
            if (es != null && es.size() != 0) {
                for (Element element : es) {
                    retMap.put(element.getName(), element.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retMap;
    }

    /**
     * 生存创建订单接口参数中的商品信息XML格式内容
     * @param goodItems 商品信息集合
     *                  Mao中包括：name、number、price(可为空)
     * @return
     */
    public static String generateItemXml(List<Map<String, String>> goodItems) {
        StringBuilder builder = new StringBuilder();
        builder.append("<items>");
        for(Map<String, String> items : goodItems) {
            builder.append("<item>");
            builder.append("<itemName>" + items.get("name") + "</itemName>");
            builder.append("<number>" + items.get("number") + "</number>");
            builder.append("<itemValue>" + (items.get("price") == null ? "" : items.get("price")) + "</itemValue>");
            builder.append("</item>");
        }
        builder.append("</items>");
        return builder.toString();
    }

    /**
     * 生存创建订单接口参数中的发送/接收人相关信息XML格式内容
     * @param userData 数据Map, 包括：
     *                 sender：name、postCode、phone(可为空)、mobile(可为空)、prov、city、address
     *                 receiver：name、postCode、phone(可为空)、prov、city、address
     * @return
     */
    public static String generateUserXml(Map<String, String> userData) {
        StringBuilder builder = new StringBuilder();
        builder.append("<sender>");
        builder.append("<name>" + (userData.get("senderName") == null ? "" : userData.get("senderName")) + "</name>");
        builder.append("<postCode>" + (userData.get("senderPostCode") == null ? "" : userData.get("senderPostCode")) + "</postCode>");
        builder.append("<phone>" + (userData.get("senderPhone") == null ? "" : userData.get("senderPhone")) + "</phone>");
        builder.append("<mobile>" + (userData.get("senderMobile") == null ? "" : userData.get("senderMobile")) + "</mobile>");
        builder.append("<prov>" + (userData.get("senderProv") == null ? "" : userData.get("senderProv")) + "</prov>");
        builder.append("<city>" + (userData.get("senderCity") == null ? "" : userData.get("senderCity")) + "</city>");
        builder.append("<address>" + (userData.get("senderAddress") == null ? "" : userData.get("senderAddress")) + "</address>");
        builder.append("</sender>");
        builder.append("<receiver>");
        builder.append("<name>" + (userData.get("receiverName") == null ? "" : userData.get("receiverName")) + "</name>");
        builder.append("<postCode>" + (userData.get("receiverPostCode") == null ? "" : userData.get("receiverPostCode")) + "</postCode>");
        builder.append("<phone>" + (userData.get("receiverPhone") == null ? "" : userData.get("receiverPhone")) + "</phone>");
        builder.append("<mobile>" + (userData.get("receiverMobile") == null ? "" : userData.get("receiverMobile")) + "</mobile>");
        builder.append("<prov>" + (userData.get("receiverProv") == null ? "" : userData.get("receiverProv")) + "</prov>");
        builder.append("<city>" + (userData.get("receiverCity") == null ? "" : userData.get("receiverCity")) + "</city>");
        builder.append("<address>" + (userData.get("receiverAddress") == null ? "" : userData.get("receiverAddress")) + "</address>");
        builder.append("</receiver>");
        return builder.toString();
    }

    
}