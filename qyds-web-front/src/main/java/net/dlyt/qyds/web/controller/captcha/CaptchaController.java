package net.dlyt.qyds.web.controller.captcha;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import net.dlyt.qyds.common.dto.SysSmsCaptcha;
import net.dlyt.qyds.config.SmsCaptchaConfig;
import net.dlyt.qyds.web.common.SmsStringUtil;
import net.dlyt.qyds.web.service.SysSmsCaptchaService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by C_Nagai on 2016/6/25.
 */
@Controller
@RequestMapping("/captcha")
public class CaptchaController {

    @Resource
    private SysSmsCaptchaService sysSmsCaptchaService;

    @Autowired
    private SmsCaptchaConfig smsCaptchaConfig;


    @RequestMapping("send")
    public @ResponseBody JSONObject saveCaptcha(
            @RequestParam(required = true)String mobile){

        JSONObject result = null;

        String captcha = SmsStringUtil.getCode(6, SmsStringUtil.CAPTCHA_TYPE_NUMBER);
        // TODO SMS
        try {

            TaobaoClient client = new DefaultTaobaoClient(
                    smsCaptchaConfig.getUrl(),
                    smsCaptchaConfig.getAppkey(),
                    smsCaptchaConfig.getSecret());
            AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
            req.setExtend(mobile);
            req.setSmsType("normal");
            req.setSmsFreeSignName(smsCaptchaConfig.getSign());
            req.setSmsParamString("{\"code\":\"" + captcha + "\",\"product\":\"" + smsCaptchaConfig.getSign() + "\"}");
            req.setRecNum(mobile);
            req.setSmsTemplateCode(smsCaptchaConfig.getTemplate());

            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);

            if(rsp.isSuccess()){
                // 保存验证码
                SysSmsCaptcha record = new SysSmsCaptcha();
                record.setMobile(mobile);
                record.setCaptcha(captcha);
                result = sysSmsCaptchaService.insertCaptchaRecord(record);
            }else {
                result = new JSONObject();
                result.put("resultCode", Constants.FAIL);
                result.put("resultMessage", "验证码获取失败");
            }

        } catch (ExceptionBusiness e){

            result = new JSONObject();
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());

        } catch (Exception e) {
            result = new JSONObject();
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", "验证码获取失败");
        }
        return result;
    }


    @RequestMapping("check")
    public @ResponseBody JSONObject getCaptcha(
            @RequestParam String mobile,
            @RequestParam String captcha,
            @RequestParam String orgId){
        // 验证
        return sysSmsCaptchaService.isValidedCaptcha(mobile, captcha, orgId);
    }





}
