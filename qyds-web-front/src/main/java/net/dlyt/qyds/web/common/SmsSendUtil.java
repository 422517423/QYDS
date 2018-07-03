package net.dlyt.qyds.web.common;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import net.dlyt.qyds.common.dto.ext.MmbPointRecordExt;
import net.dlyt.qyds.config.SmsCaptchaConfig;
import net.dlyt.qyds.web.service.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ZLH on 2016/11/17.
 */
@Component("smsSendUtil")
public class SmsSendUtil {
    static private TaobaoClient client;
    static private AlibabaAliqinFcSmsNumSendRequest req;
    static private AlibabaAliqinFcSmsNumSendResponse rsp;

    @Autowired
    private static SmsCaptchaConfig smsCaptchaConfig;

    static private SmsSendUtil instance = null;

    public static SmsSendUtil getInstance() throws Exception {
        if(instance == null){
            instance = new SmsSendUtil();
            init();
        }
        return instance;
    }

    static private void init() throws Exception {
        if(client==null) {
            if (smsCaptchaConfig==null) {
                client = new DefaultTaobaoClient(
                        "http://gw.api.taobao.com/router/rest",
                        "23320071",
                        "23243410e09e3f0ad0f433a58a79297a");
            } else {
                client = new DefaultTaobaoClient(
                        smsCaptchaConfig.getUrl(),
                        smsCaptchaConfig.getAppkey(),
                        smsCaptchaConfig.getSecret());
            }
        }
        if(req==null) {
            req = new AlibabaAliqinFcSmsNumSendRequest();
            req.setSmsFreeSignName(smsCaptchaConfig==null?"迪洛纳官方":smsCaptchaConfig.getSign());
            req.setSmsType("normal");
        }
    }
    static private void init(SmsCaptchaConfig config) throws Exception {
        if(smsCaptchaConfig==null) {
            smsCaptchaConfig = config;
        }
        init();
    }

    static public JSONObject sendNotifyPoint(MmbPointRecordExt item,SmsCaptchaConfig config) {
        // 初期化
        JSONObject result = new JSONObject();
        try {
            init(config);
            result = sendNotifyPoint(item);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", item.getTelephone()+"清除积分短信发送失败");
        }
        return result;
    }

    static public JSONObject sendNotifyPoint(MmbPointRecordExt item) {
        JSONObject result = new JSONObject();
        try {
            // 初期化
            init();
            // 设置参数
            req.setExtend(item.getTelephone());
            req.setRecNum(item.getTelephone());
            // 模板编号
            req.setSmsTemplateCode("SMS_34685003");
            req.setSmsParamString("{\"point\":\"" + item.getPointSurplus() + "\"}");
            //短信发送
            rsp = client.execute(req);
            if(rsp.isSuccess()){
                // 发送成功
                result.put("resultCode", Constants.NORMAL);
                result.put("resultMessage", item.getTelephone()+"清除积分短信发送成功");
            }else {
                // 发送失败
                throw new Exception();
            }
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", item.getTelephone()+"清除积分短信发送失败");
        }
        return result;
    }

    static public JSONObject test(SmsCaptchaConfig config) {
        // 初期化
        JSONObject result = new JSONObject();
        try {
            init(config);
            result = test();
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", "发送失败");
        }
        return result;
    }

    static public JSONObject test() {
        JSONObject result = new JSONObject();
        try {
            // 初期化
            init();
            // 设置参数
            req.setExtend("15904261987");
            req.setRecNum("15904261987");
            // 模板编号
            req.setSmsTemplateCode("SMS_15410101");
            req.setSmsParamString("{\"captcha\":\"123456\"}");
            //短信发送
            rsp = client.execute(req);
            if(rsp.isSuccess()){
                // 发送成功
                result.put("resultCode", Constants.NORMAL);
                result.put("resultMessage", "测试短信发送成功");
            }else {
                // 发送失败
                throw new Exception();
            }
            req.setSmsTemplateCode("SMS_34685003");
            req.setSmsParamString("{\"point\":\"9999\"}");
            //短信发送
            rsp = client.execute(req);
            if(rsp.isSuccess()){
                // 发送成功
                result.put("resultCode", Constants.NORMAL);
                result.put("resultMessage", "测试短信发送成功");
            }else {
                // 发送失败
                throw new Exception();
            }
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", "测试短信发送失败");
        }
        return result;
    }
}
