import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.dlyt.qyds.common.dto.OrdMaster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class mainTest {
    public static void main(String[] args) {
//        JSONObject json = new JSONObject();
//        json.put("a",1);
//        json.put("c",1);
//        json.put("b",1);
//        json.put("d",1);
//        json.put("g",1);
//        json.put("e",1);
//        System.out.println(json.toString());
//        String s = json.toJSONString();
//        LinkedHashMap<String, String> jsonMap = JSON.parseObject(s, new TypeReference<LinkedHashMap<String, String>>() {
//        });
//        StringBuilder param = new StringBuilder();
//        for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
//            if(param.length()>0){
//                param.append("&");
//            }
//            param.append(entry.getKey());
//            param.append("=");
//            param.append(entry.getValue());
//        }
//        System.out.println(param);
        send();
    }

    public static String send(){
        JSONObject json = new JSONObject();
        try{
            if(true){


                OutputStreamWriter out = null;
                BufferedReader in = null;
                StringBuilder resultB = new StringBuilder();
                try {
                    //旧版本的url
//                    URL realUrl = new URL("http://merchant.vikpay.com:9050/ali-pay/payinterface");
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
                    //Map<String, Object> params = new HashMap<>();
                    String total_fee = "5.00";
                    int total_amount = 500;
//                    params.put("merchantId",storeid);
//                    params.put("storeId",storesubid);
//                    params.put("userId",operate);
//                    params.put("totalAmount",total_fee);
//                    params.put("otherOrderNumber",result);
//                    params.put("authCode",signcode);
//                    params.put("md5", this.string2MD5(result+"huifu2017"));
                    StringBuffer flag = new StringBuffer();
                    for (int j = 0; j < 16; j++)
                    {
                        flag.append((char)('a'+Math.random()*('z'-'a'+1)));
                    }
                    String nonce = flag.toString();
                    //测试用mch_id
                    json.put("mch_id","m11015");
                    //正式用mch_id
//                    params.put("mch_id",storeid);
                    json.put("nonce",nonce);
                    json.put("total_amount",total_amount);
                    json.put("trade_no","20180499955959841578987987855");
                    json.put("auth_code","284861058918988495");
                    if (json!=null){
                        String jsonString = json.toJSONString();
//                        LinkedHashMap<String, Object> jsonMap = JSON.parseObject(s, new TypeReference<LinkedHashMap<String, Object>>() {});
                        jsonString=jsonString.replace(':','=');
                        jsonString=jsonString.replace(',','&');
                        jsonString=jsonString.replace("\"","");
                        jsonString=jsonString.replace("{","");
                        jsonString=jsonString.replace("}","");
//                        StringBuilder param = new StringBuilder();
//                        for (Map.Entry<String, Object> entry :jsonMap.entrySet()) {
//                            if(param.length()>0){
//                                param.append("&");
//                            }
//                            param.append(entry.getKey());
//                            param.append("=");
//                            param.append(entry.getValue());
//                        }
//                        param.append("&");
//                        param.append("key=");
//                        param.append("5GbN6Sw2gV");
                        jsonString = jsonString +"&key=5GbN6Sw2gV";
                        String sign = MD5(jsonString).toUpperCase();
                        json.put("sign",sign);
                        String data = json.toString();
                        String dataOut = "data="+data;
                        out.write(dataOut);
                    }
                    //旧版本代码
//                    json.put("md5", this.string2MD5(result+"huifu2017"));
//                    if (params != null) {
//                        StringBuilder param = new StringBuilder();
//                        for (Map.Entry<String, Object> entry : params.entrySet()) {
//                            if(param.length()>0){
//                                param.append("&");
//                            }
//                            param.append(entry.getKey());
//                            param.append("=");
//                            param.append(entry.getValue());
//                        }
//                        out.write(json);
//                    }

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
                            mParam.put("orderCode","的示范法");
                            mParam.put("total_fee", String.valueOf(total_fee));//单位‘元’ 0.00
                            String orderNumber = (String)jsonObject.get("transaction_id");
                            //旧版本的ordernumber
//                            JSONObject data = (JSONObject)jsonObject.get("data");
//                            String orderNumber = (String)data.get("orderNumber");
                            mParam.put("orderNumber", orderNumber);
                            //成功回调
                            System.out.println("成功了");
                            System.out.println(mParam);
//                            ordAliPayService.paySuccess(mParam);
                            return "0";
                        }else{
                            //支付失败
                            return "1";
                        }
                    }
                    //旧版本代码
//                    while ((line = in.readLine()) != null) {
//                        JSONObject jsonObject = (JSONObject)JSONObject.parse(line);
//                        Boolean resultCode = (Boolean)jsonObject.get("success");
//                        String message = (String)jsonObject.get("err_msg");
//                        if(resultCode){
//                            Map<String, String> mParam = new HashMap<String, String>();
//                            mParam.put("orderCode", result);
//                            mParam.put("total_fee", String.valueOf(total_fee));//单位‘元’ 0.00
//
//                            JSONObject data = (JSONObject)jsonObject.get("data");
//                            String orderNumber = (String)data.get("orderNumber");
//                            mParam.put("orderNumber", orderNumber);
//                            //成功回调
//                            ordAliPayService.paySuccess(mParam);
//                            return "0";
//                        }else{
//                            //支付失败
//                            return "1";
//                        }
//                    }
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
