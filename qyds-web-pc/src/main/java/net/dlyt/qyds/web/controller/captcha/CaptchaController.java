package net.dlyt.qyds.web.controller.captcha;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import net.dlyt.qyds.common.dto.MmbMaster;
import net.dlyt.qyds.common.dto.SysSmsCaptcha;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.config.SmsCaptchaConfig;
import net.dlyt.qyds.web.common.SmsStringUtil;
import net.dlyt.qyds.web.service.MmbMasterService;
import net.dlyt.qyds.web.service.SysSmsCaptchaService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
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

    @Autowired
    private MmbMasterService mmbMasterService;


    @RequestMapping("send")
    public
    @ResponseBody
    JSONObject saveCaptcha(
            String data) {

        JSONObject result = null;
        // TODO SMS
        try {

            SysSmsCaptcha form = (SysSmsCaptcha) JSON.parseObject(data, SysSmsCaptcha.class);

            String mobile = form.getMobile();
            // 使用orgId来区分不同的操作动作 10:注册 20:绑定 30:优惠券使用 40:修改密码 90:其他
            String orgId = form.getOrgId();

            if (StringUtil.isEmpty(mobile)) {
                throw new ExceptionErrorParam("请输入手机号");
            }

            if (!StringUtil.isTelephone(mobile)) {
                throw new ExceptionErrorParam("手机号格式不正确");
            }

            if (StringUtil.isEmpty(orgId)) {
                throw new ExceptionErrorParam("验证码请求不合理");
            }

            MmbMaster user = new MmbMaster();
            user.setTelephone(mobile);
            user.setDeleted("0");
            user.setIsValid("0");
            JSONObject valid = mmbMasterService.selectBySelective(user);

            // 注册时需要验证mobile是否已经存在,如果已经存在,则注册验证码获取失败
            if ("10".equals(orgId)) {

                if (valid != null && Constants.NORMAL.equals(valid.getString("resultCode"))) {
                    throw new ExceptionErrorParam("该手机号码已经被注册");
                }
            }

            if ("20".equals(orgId) || "30".equals(orgId)) {
                if (valid == null || !Constants.NORMAL.equals(valid.getString("resultCode"))) {
                    throw new ExceptionErrorParam("该账号不存在,请输入正确的手机号码");
                } else if ("20".equals(orgId)) {
                    MmbMasterExt rMmbMaster = valid.getObject("data", MmbMasterExt.class);
                    if (null != rMmbMaster && !StringUtil.isEmpty(rMmbMaster.getOpenId())) {
                        throw new ExceptionErrorParam("该手机号码已经被绑定");
                    }
                }
            }

            if ("40".equals(orgId)) {
                if (valid == null || !Constants.NORMAL.equals(valid.getString("resultCode"))) {
                    throw new ExceptionErrorParam("该账号不存在,请输入正确的手机号码");
                }
            }

            String captcha = SmsStringUtil.getCode(6, SmsStringUtil.CAPTCHA_TYPE_NUMBER);

            TaobaoClient client = new DefaultTaobaoClient(
                    smsCaptchaConfig.getUrl(),
                    smsCaptchaConfig.getAppkey(),
                    smsCaptchaConfig.getSecret());
            AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
            req.setExtend(mobile);
            req.setSmsType("normal");
            req.setSmsFreeSignName(smsCaptchaConfig.getSign());
            req.setSmsParamString("{\"captcha\":\"" + captcha + "\"}");
            req.setRecNum(mobile);
            req.setSmsTemplateCode(smsCaptchaConfig.getTemplate());

            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);

            if (rsp.isSuccess()) {
                // 保存验证码
                SysSmsCaptcha record = new SysSmsCaptcha();
                record.setOrgId(orgId);
                record.setMobile(mobile);
                record.setCaptcha(captcha);
                result = sysSmsCaptchaService.insertCaptchaRecord(record);
            } else {
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
    public
    @ResponseBody
    JSONObject getCaptcha(
            @RequestParam String mobile,
            @RequestParam String captcha,
            @RequestParam String orgId) {

        // 使用orgId来区分不同的操作动作 10:注册 20:绑定 30:优惠券使用 40:修改密码 90:其他
        // 验证
        return sysSmsCaptchaService.isValidedCaptcha(mobile, captcha, orgId);
    }


}
