package net.dlyt.qyds.web.controller.scanpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.OrdMaster;
import net.dlyt.qyds.common.dto.ShpOrg;
import net.dlyt.qyds.web.common.StringUtil;
import net.dlyt.qyds.web.common.unionpay.DemoBase;
import net.dlyt.qyds.web.common.unionpay.sdk.AcpService;
import net.dlyt.qyds.web.common.unionpay.sdk.LogUtil;
import net.dlyt.qyds.web.common.unionpay.sdk.SDKConfig;
import net.dlyt.qyds.web.service.ErpStoreService;
import net.dlyt.qyds.web.service.OrdAliPayService;
import net.dlyt.qyds.web.service.OrdMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by congkeyan on 17/3/17.
 */
@Controller
@RequestMapping("/scanpay")
public class scanPayController {


    @Autowired
    private OrdAliPayService ordAliPayService;

    @Autowired
    private OrdMasterService ordMasterService;

    @Autowired
    private ErpStoreService erpStoreService;

    /***
     * MD5加码 生成32位md5码
     */
    public String string2MD5(String inStr){
        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    /**
     * 扫描到账
     * @param orderId
     * @param signcode
     * @param request
     * @param response
     * @throws Exception
     *
     */
    @RequestMapping("scanpay")
    @ResponseBody
    public String payment(@RequestParam(required = true)String orderId,@RequestParam(required = true)String signcode,
                          @RequestParam(required = true)String operate,@RequestParam(required = true)String storeid,@RequestParam(required = true)String storesubid,
                          HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        JSONObject json = new JSONObject();
        try{

            String result = ordAliPayService.checkOrderInfo(orderId);
            OrdMaster ordMaster = ordAliPayService.getOrderInfo(orderId);
            ordMasterService.checkSecActivityOrderInfo(orderId, null);

            if(!"NODATA".equals(result) && !"ZEROMONEY".equals(result)){


                OutputStreamWriter out = null;
                BufferedReader in = null;
                StringBuilder resultB = new StringBuilder();
                try {
                    //新版本url
                    URL realUrl = new URL("http://micropay.vikpay.com/api/pay.do");
                    HttpURLConnection conn =(HttpURLConnection) realUrl.openConnection();
                    // 发送POST请求必须设置如下两行
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    // POST方法
                    conn.setRequestMethod("POST");
                    // 设置通用的请求属性
                    conn.setRequestProperty("accept", "*/*");
                    conn.setRequestProperty("connection", "Keep-Alive");
                    conn.setRequestProperty("user-agent",
                            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("kdniao-nocache", "true");
                    conn.connect();
                    // 获取URLConnection对象对应的输出流
                    out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                    // 发送请求参数
                    String total_fee = ordMaster.getPayInfact() != null ? String.valueOf(ordMaster.getPayInfact()) : "0.00";
                    int total_amount = ordMaster.getPayInfact().multiply(new BigDecimal(100)).intValue();
                    StringBuffer nonce = new StringBuffer();
                    for (int j = 0; j < 16; j++)
                    {
                        nonce.append((char)('a'+Math.random()*('z'-'a'+1)));
                    }
                    //测试用mch_id
//                    json.put("mch_id","m11015");
                    //正式用mch_id
                    ShpOrg shpOrg = erpStoreService.selectByStoresubid(storesubid);
                    String mchid = shpOrg.getMchid();
                    json.put("mch_id",mchid);
                    json.put("nonce",nonce.toString());
                    json.put("total_amount",total_amount);
//                    json.put("total_amount",1);
                    json.put("trade_no",result);
                    json.put("auth_code",signcode);
                    if (json!=null){
                        String jsonString = JSON.toJSONString(json);
                        jsonString=jsonString.replace(':','=');
                        jsonString=jsonString.replace(',','&');
                        jsonString=jsonString.replace("\"","");
                        jsonString=jsonString.replace("{","");
                        jsonString=jsonString.replace("}","");
                        jsonString = jsonString +"&key=5GbN6Sw2gV";
                        String sign = MD5(jsonString.toString()).toUpperCase();
                        json.put("sign",sign);
                        String data = json.toString();
                        String dataOut = "data="+data;
                        out.write(dataOut);
                    }
                    // flush输出流的缓冲
                    out.flush();
                    // 定义BufferedReader输入流来读取URL的响应
                    in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    String line;
                    while ((line = in.readLine()) != null) {
                        JSONObject jsonObject = (JSONObject)JSONObject.parse(line);
                        String code = (String)jsonObject.get("code");
                        String message = (String)jsonObject.get("msg");
                        if(code!=null&&code.equalsIgnoreCase("success")){
                            Map<String, String> mParam = new HashMap<String, String>();
                            mParam.put("orderCode", result);
                            mParam.put("total_fee", String.valueOf(total_fee));//单位‘元’ 0.00
//                            String orderNumber = (String)jsonObject.get("trade_no");
                            mParam.put("orderNumber",result);
                            //成功回调
                            ordAliPayService.paySuccess(mParam);
                            return "0";
                        }else{
                            //支付失败
                            return "1";
                        }
                    }
                } catch (Exception e) {
                    //支付失败
                    return "1";
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
                        //支付失败
                        return "1";
                    }
                }

            }else{
                //支付失败
                return "1";
            }
        }catch(Exception e){
            //支付失败
            return "1";
        }
        return "1";
    }


    public static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
