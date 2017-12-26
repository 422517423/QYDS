package net.dlyt.qyds.web.service.common.YtUtil;

import me.chanjar.weixin.common.util.StringUtils;
import net.dlyt.qyds.common.dto.OrdMasterExt;
import net.dlyt.qyds.common.dto.OrdSubListExt;
import net.dlyt.qyds.common.dto.SysUser;
import net.dlyt.qyds.common.dto.ext.OrdTransferListExt;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class YtApi {
   /* //电商加密私钥-测试
    private final static String AppKey="123456";
    //请求url-测试
    private  final static String ReqURL="http://jingangtest.yto56.com.cn/ordws/Vip16Servlet";*/

    //电商加密私钥-正式
    private final static String AppKey="1Bi478P";
    //请求url-正式
    private  final static String ReqURL="http://jingang.yto56.com.cn/ordws/Vip16Servlet";

    // 发货人信息
    private static String senderName = "9999";
    private static String senderPostCode = "0";
    private static String senderProv = "";
    private static String senderCity = "";
    private static String senderAddress = "";

    // 收货人信息
    private static String receiverName = "";
    private static String receiverPostCode = "0";
    private static String receiverPhone = "";
    private static String receiverProv = "";
    private static String receiverCity = "";
    private static String receiverAddress = "";
    // 物流号
    private static String subOrderId = "";
    // 商品名称
    private static String goodsName ="";
    // 商品数量
    private static String quantity ="";
    /**
     * 给门店发快递
     * @throws Exception
     */
    public static String getOrderTracesByXml1(OrdTransferListExt ordTransferListExt) throws Exception{
            // 收货人信息
            // 收货人姓名
            receiverName = ordTransferListExt.getApplyContactor();
            // 收货人手机号
            receiverPhone = ordTransferListExt.getApplyPhone();
            // 收货人省份
            receiverProv = ordTransferListExt.getApplyPname();
            // 收货人城市,区域
            receiverCity = ordTransferListExt.getApplyCname()+","+ordTransferListExt.getApplyDname();
            // 收货人地址
            receiverAddress = ordTransferListExt.getApplyAddress1();
            // 收件人邮编
            receiverPostCode = ordTransferListExt.getDeliveryPostcode();

            // 发货人信息
            // 发货人姓名
            senderName =ordTransferListExt.getDispatchUname();
            // 发货人省份
            senderProv = ordTransferListExt.getDispatchPname();
            // 发货人城市,区域
            if(StringUtils.isNotBlank(ordTransferListExt.getDispatchDname())){
                senderCity = ordTransferListExt.getDispatchCname()+","+ordTransferListExt.getDispatchDname();
            }else{
                senderCity = ordTransferListExt.getDispatchDname();
            }
            // 发货人地址
            senderAddress = ordTransferListExt.getDispatchAddress1();
            // 发件人邮编
            senderPostCode = "";

            // 物流号
            subOrderId = ordTransferListExt.getOrderTransferId();
            // 商品名称
            goodsName = ordTransferListExt.getGoodsName();
            return getXml();
        }

    /**
     * 给客户发货
     * @throws Exception
     */
    public static String getOrderTracesByXml(OrdMasterExt ordMaster, OrdSubListExt subOrder, SysUser sysUser, int num) throws Exception{
        // 门店给客户发货

        // 收货人信息

        // 电商发货(后台给门店派单，派送到距离客户最近的门店，由门店给直接给客户发货)
        // 收货人姓名
        receiverName = ordMaster.getDeliveryContactor();
        // 收货人手机号
        receiverPhone = ordMaster.getDeliveryPhone();
        // 收货人省份
        receiverProv = ordMaster.getDistrictidProvince();
        // 收货人城市,区域
        senderCity = ordMaster.getDistrictidCity()+","+ordMaster.getDistrictidDistrict();
        // 收货人地址
        senderAddress = ordMaster.getDeliveryAddress();

        // 发货人信息
        // 发货人姓名
        senderName = sysUser.getUserName();
        // 发货邮编
        senderPostCode = "0";
        // 发货人省份
        senderProv = subOrder.getShopProvince();
        // 发货人城市,区域
        if(StringUtils.isNotBlank(subOrder.getShopDistrict())){
            senderCity = subOrder.getShopCity()+","+subOrder.getShopDistrict();
        }else{
            senderCity = subOrder.getShopCity();
        }
        // 发货人地址
        senderAddress = subOrder.getShopAddress();

        // 物流号
        subOrderId = subOrder.getSubOrderId();
        // 商品名称
        goodsName = subOrder.getGoodsName();
        return getXml();
    }

    /**
     * 发送xml给圆通接口
     * @throws Exception
     */
    public static String getXml() throws Exception{
        String requestData= /*"<?xml version=\"1.0\" encoding=\"utf-8\" ?>"+*/
                "<RequestOrder>" +
                        // 客户编码（电商标识，由圆通人员给出）
                        "<clientID>"+AppKey+"</clientID>" +
                        // 物流公司ID（YTO）
                        "<logisticProviderID>YTO</logisticProviderID>" +
                        // 客户标识（COD业务，且有多个仓发货则不能为空，请填写分仓号）
                        "<customerId></customerId>" +
                        // 物流号(现在先默认子订单表中的子订单id
                       /* "<txLogisticID>"+subOrder.getSubOrderId()+"</txLogisticID>" +*/
                        "<txLogisticID>"+subOrderId+"</txLogisticID>" +

                        // 订单类型(0-COD,1-普通订单,3-退货单)
                        "<orderType>1</orderType>" +
                        // 服务类型(1-上门揽收, 2-次日达 4-次晨达 8-当日达,0-自己联系)。（数据库未使用）（目前暂未使用默认为0）
                        "<serviceType>0</serviceType>" +

                        // 发货人信息
                        "<sender>" +
                        // 门店发货人
                        "<name>"+senderName+"</name>" +
                        "<postCode>"+senderPostCode+"</postCode>" +
                        "<prov>"+senderProv+"</prov>" +
                        "<city>"+senderCity+"</city>" +
                        "<address>"+senderAddress+"</address>" +
                        "</sender>" +

                        // 收货人信息
                        "<receiver>" +
                        "<name>"+receiverName+"</name>" +
                        "<postCode>"+receiverPostCode+"</postCode>" +
                        "<phone>"+receiverPhone+"</phone>" +
                        "<prov>"+receiverProv+"</prov>" +
                        "<city>"+receiverCity+"</city>" +
                        "<address>"+receiverAddress+"</address>" +
                        "</receiver>" +
                        "<items>" +
                        "<item>" +
                        // 商品名称
                        "<itemName>"+goodsName+"</itemName>" +
                        // 商品数量
                        "<number>1</number>" +
                        "</item>" +
                        "</items>" +
                        "<special>0</special>" +
                        "</RequestOrder>";

        Map<String, String> params = new HashMap<String, String>();
        params.put("logistics_interface", urlEncoder(requestData, "UTF-8"));
        params.put("data_digest",urlEncoder(encrypt(requestData, AppKey, "UTF-8"),"UTF-8"));
        params.put("clientId", AppKey);
        params.put("type", "online");
        String result=sendPost(ReqURL, params);
        //根据公司业务处理返回的信息
        return result;
    }


    /**
     * @param str
     * @return md5+base64进行加密
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        //确定计算方法
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    private static String urlEncoder(String str, String charset) throws UnsupportedEncodingException{
        String result = URLEncoder.encode(str, charset);
        return result;
    }

    /**
     * 签名生成
     * @param content 内容
     * @param keyValue Appkey
     * @param charset 编码方式
     * @throws UnsupportedEncodingException ,Exception
     * @return DataSign签名
     */
    private static String encrypt (String content, String keyValue, String charset) throws UnsupportedEncodingException, Exception
    {
        return EncoderByMd5(content+keyValue);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * @param url 发送请求的 URL
     * @param params 请求的参数集合
     * @return 远程资源的响应结果
     */
    private static String sendPost(String url, Map<String, String> params) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn =(HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            if (params != null) {
                StringBuilder param = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if(param.length()>0){
                        param.append("&");
                    }
                    param.append(entry.getKey());
                    param.append("=");
                    param.append(entry.getValue());
                }
                System.out.println("param:"+param.toString());
                out.write(param.toString());
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    public static String cancelYto(String orderId) throws Exception{
        String requestData= /*"<?xml version=\"1.0\" encoding=\"utf-8\" ?>"+*/
                "<UpdateInfo>" +
                        // 物流公司ID（YTO）
                        "<logisticProviderID>YTO</logisticProviderID>" +
                        // 客户编码（电商标识，由圆通人员给出）
                        "<clientID>"+AppKey+"</clientID>" +
                        // 物流号(现在先默认子订单表中的子订单id
                       /* "<txLogisticID>"+subOrder.getSubOrderId()+"<LogisticID>" +*/
                        "<txLogisticID>"+ orderId +"<LogisticID>" +
                        "<infoType>INSTRUCTION</infoType>" +
                        "<infoContent>WITHDRAW</infoContent>" +
                        "<remark>商品没了</remark>"+
                        "</UpdateInfo>";
        Map<String, String> params = new HashMap<String, String>();
        params.put("logistics_interface", urlEncoder(requestData, "UTF-8"));
        params.put("data_digest",urlEncoder(encrypt(requestData, AppKey, "UTF-8"),"UTF-8"));
        params.put("clientId", AppKey);
        params.put("type", "online");
        String result=sendPost(ReqURL, params);
        //根据公司业务处理返回的信息
        return result;
    }


}
