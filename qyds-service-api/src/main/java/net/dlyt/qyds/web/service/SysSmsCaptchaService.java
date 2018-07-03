package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.SysSmsCaptcha;

/**
 * Created by dkzhang on 16/8/4.
 */
public interface SysSmsCaptchaService {

    JSONObject insertCaptchaRecord(SysSmsCaptcha record);

    JSONObject isValidedCaptcha(String mobile, String captcha, String orgId);
}
