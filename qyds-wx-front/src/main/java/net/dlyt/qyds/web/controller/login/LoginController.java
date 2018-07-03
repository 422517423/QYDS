package net.dlyt.qyds.web.controller.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.EncryptUtil;
import net.dlyt.qyds.web.service.MmbMasterService;
import net.dlyt.qyds.web.service.SysSmsCaptchaService;
import net.dlyt.qyds.web.service.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;

/**
 * Created by C_Nagai on 2016/6/25.
 */
@Controller
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private MmbMasterService mmbMasterService;

    @Resource
    private SysSmsCaptchaService sysSmsCaptchaService;

    @RequestMapping("register")
    public @ResponseBody JSONObject signup(String data, HttpServletResponse response){
        JSONObject json = null;
        try{
            JSONObject inJson = JSONObject.parseObject(data);
            String openId = inJson.getString("openId");
            String telephone = inJson.getString("telephone");
            String password = inJson.getString("password");
            String captcha = inJson.getString("captcha");
            String memberName = inJson.getString("memberName");
            String memberPic = inJson.getString("memberPic");
            String sex = inJson.getString("sex"); // 0=不明;1=男;2=女
            String provinceCode=inJson.getString("provinceCode");
            String provinceName=inJson.getString("provinceName");
            String cityCode=inJson.getString("cityCode");
            String cityName=inJson.getString("cityName");
            String districtCode=inJson.getString("districtCode");
            String districtName=inJson.getString("districtName");
            String birthdate = inJson.getString("birthdate");
            if(StringUtils.isEmpty(openId)
                    ||StringUtils.isEmpty(telephone)
                    ||StringUtils.isEmpty(password)
                    ||StringUtils.isEmpty(captcha)
                    ||StringUtils.isEmpty(sex)
                    ||StringUtils.isEmpty(birthdate))
                     {
                json = new JSONObject();
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "提交的注册信息不足");
                return json;
            }

            if (!StringUtil.isTelephone(telephone)) {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "手机号格式不正确");
                return json;
            }

            // 验证码效验  使用orgId来区分不同的操作动作 10:注册 20:绑定 30:优惠券使用 90:其他
            JSONObject jsonCaptcha = sysSmsCaptchaService.isValidedCaptcha(telephone, captcha, "10");
            if(jsonCaptcha == null || Constants.FAIL.equals(jsonCaptcha.getString("resultCode"))){
                return jsonCaptcha;
            }

            MmbMaster sysUser = new MmbMaster();
            sysUser.setOpenId(openId);
            sysUser.setTelephone(telephone);
            sysUser.setMemberName(memberName);
            sysUser.setMemberPic(memberPic);
            sysUser.setSex(sex);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sysUser.setBirthdate(sdf.parse(birthdate));
            sysUser.setRegistStyle("20");//20	微信		注册方式-微信
            sysUser.setType("10");//10	普通		会员类型-普通
            sysUser.setMemberLevelId("10");
            sysUser.setProvinceCode(provinceCode);
            sysUser.setProvinceName(provinceName);
            sysUser.setCityCode(cityCode);
            sysUser.setCityName(cityName);
            sysUser.setDistrictCode(districtCode);
            sysUser.setDistrictName(districtName);
            sysUser.setPassword(EncryptUtil.encodeMD5(password.toLowerCase()));
            json = mmbMasterService.registerUser(sysUser);
            if (json != null && Constants.NORMAL.equals(json.getString("resultCode"))) {
                MmbMaster user = (MmbMaster)json.get("data");
                user.setPassword("");

                // 设置cookie信息
                String token = EncryptUtil.encrypt(JSON.toJSONString(user));
                Cookie cookieToken = new Cookie("qyds_user_token", token);
                cookieToken.setPath("/");
                cookieToken.setMaxAge(30 * 60 * 1000);
                response.addCookie(cookieToken);

            } else {
                json = new JSONObject();
                json.put("resultCode", Constants.FAIL);
            }
        }catch(Exception e){
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }


    @RequestMapping("bingingWeiXin")
    public @ResponseBody JSONObject bingingWeiXin(String data, HttpServletResponse response){
        JSONObject json = new JSONObject();
        try{
            JSONObject inJson = JSONObject.parseObject(data);
            String openId = inJson.getString("openId");
            String telephone = inJson.getString("telephone");
            String captcha = inJson.getString("captcha");
            if(StringUtils.isEmpty(openId)
                    ||StringUtils.isEmpty(telephone)
                    ||StringUtils.isEmpty(captcha) ) {
                json = new JSONObject();
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "提交的绑定信息不足");
                return json;
            }

            if (!StringUtil.isTelephone(telephone)) {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "手机号格式不正确");
                return json;
            }

            // 验证码效验
            JSONObject jsonCaptcha = sysSmsCaptchaService.isValidedCaptcha(telephone, captcha, "20");
            if(jsonCaptcha == null || Constants.FAIL.equals(jsonCaptcha.getString("resultCode"))){
                return jsonCaptcha;
            }

            MmbMaster sysUser = new MmbMaster();
            sysUser.setOpenId(openId);
            sysUser.setTelephone(telephone);
            sysUser.setDeleted("0");

            json = mmbMasterService.bindingSelective(sysUser);
            if (json != null && Constants.NORMAL.equals(json.getString("resultCode"))) {
                MmbMaster user = (MmbMaster)json.get("data");
                user.setPassword("");

                // 设置cookie信息
                String token = EncryptUtil.encrypt(JSON.toJSONString(user));
                Cookie cookieToken = new Cookie("qyds_user_token", token);
                cookieToken.setPath("/");
                cookieToken.setMaxAge(30 * 60 * 1000);
                response.addCookie(cookieToken);
            }
        }catch(Exception e){
            json.put("resultMessage",e.getMessage());
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }


    @RequestMapping("getCurrentPoints")
    public
    @ResponseBody
    JSONObject getCurrentPoints(String data, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            MmbMaster sysUser = (MmbMaster) JSONObject.parseObject(data, MmbMaster.class);
            json = mmbMasterService.getCurrentPoints(sysUser);

            if (json != null && Constants.NORMAL.equals(json.getString("resultCode"))) {
                MmbMasterExt user = (MmbMasterExt) json.get("data");
                user.setPassword("");

                // 设置cookie信息
                String token = EncryptUtil.encrypt(JSON.toJSONString(user));
                Cookie cookieToken = new Cookie("qyds_user_token", token);
                cookieToken.setPath("/");
                cookieToken.setMaxAge(30 * 60 * 1000);
                response.addCookie(cookieToken);

            }
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", "积分获取失败");
        }
        return json;
    }


    @RequestMapping("login")
    public @ResponseBody JSONObject login(String data, HttpServletResponse response){
        JSONObject json = null;
        try{
            MmbMaster sysUser = (MmbMaster) JSONObject.parseObject(data, MmbMaster.class);
//            sysUser.setPassword(EncryptUtil.encodeMD5(sysUser.getPassword().toLowerCase()));
            json = mmbMasterService.loginSelective(sysUser);
            if (json != null && Constants.NORMAL.equals(json.getString("resultCode"))) {
                MmbMasterExt user = (MmbMasterExt)json.get("data");
                user.setPassword("");

                // 设置cookie信息
                String token = EncryptUtil.encrypt(JSON.toJSONString(user));
                Cookie cookieToken = new Cookie("qyds_user_token", token);
                cookieToken.setPath("/");
                cookieToken.setMaxAge(30 * 60 * 1000);
                response.addCookie(cookieToken);

            }
        }catch(Exception e){
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    @RequestMapping("401")
    public @ResponseBody
    JSONObject unauthorized(){
        JSONObject json = new JSONObject();
        json.put("resultCode", Constants.UNAUTHORIZED);
        json.put("operation", Constants.FAIL);
        json.put("message","unauthorized");
        return json;
    }

    @RequestMapping("403")
    public @ResponseBody
    JSONObject forbidden(){
        JSONObject json = new JSONObject();
        json.put("resultCode", Constants.FORBIDDEN);
        json.put("operation", Constants.FAIL);
        json.put("message","forbidden");
        return json;
    }

}
