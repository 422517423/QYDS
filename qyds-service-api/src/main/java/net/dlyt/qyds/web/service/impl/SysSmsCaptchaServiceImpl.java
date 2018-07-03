package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.SysSmsCaptcha;
import net.dlyt.qyds.dao.SysSmsCaptchaMapper;
import net.dlyt.qyds.dao.ext.SysSmsCaptchaMapperExt;
import net.dlyt.qyds.web.service.SysSmsCaptchaService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by dkzhang on 16/8/4.
 */
@Service("sysSmsCaptchaService")
public class SysSmsCaptchaServiceImpl implements SysSmsCaptchaService{

    @Autowired
    private SysSmsCaptchaMapperExt sysSmsCaptchaMapperExt;

    @Autowired
    private SysSmsCaptchaMapper sysSmsCaptchaMapper;

    public JSONObject insertCaptchaRecord(SysSmsCaptcha record) {
        JSONObject json = new JSONObject();
        try{
            // 使用orgId来区分不同的操作动作 10:注册 20:绑定 30:优惠券使用 40:修改密码 90:其他
            record.setOrgId(record.getOrgId());
            record.setIsValid("0");
            record.setCreateUser(Constants.ORGID);
            record.setUpdateUser(Constants.ORGID);
            int ret = sysSmsCaptchaMapper.insertSelective(record);
            if(ret == 1){
                json.put("resultCode", Constants.NORMAL);
            }else {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "验证码获取失败");
            }

        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", "验证码获取失败");
        }
        return json;
    }

    // 使用orgId来区分不同的操作动作 10:注册 20:绑定 30:优惠券使用 40:修改密码 90:其他
    public JSONObject isValidedCaptcha(String mobile, String captcha, String orgId) {
        JSONObject json = new JSONObject();
        try{
            if(StringUtil.isEmpty(captcha)){
                throw new ExceptionBusiness("请输入验证码");
            }
            if(StringUtil.isEmpty(mobile)){
                throw new ExceptionBusiness("请输入手机号");
            }
            SysSmsCaptcha conds = new SysSmsCaptcha();
            conds.setOrgId(orgId);
            conds.setMobile(mobile);
            conds.setCaptcha(captcha);
            SysSmsCaptcha record = sysSmsCaptchaMapperExt.selectBySelective(conds);
            if(record == null){
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "验证码无效");
            }else{
                Date updateTime = record.getUpdateTime();
                Date now = new Date();
                if(now.getTime() < (updateTime.getTime() + Constants.VALIDED_MILLISECONDS)){
                    json.put("resultCode", Constants.NORMAL);
                }else{
                    json.put("resultCode", Constants.FAIL);
                    json.put("resultMessage", "验证码超过10分钟有效时间");
                }
            }
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", "验证码无效");
        }
        return json;
    }
}
