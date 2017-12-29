package net.dlyt.qyds.web.controller.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbMaster;
import net.dlyt.qyds.common.dto.MmbSaler;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.common.dto.ext.MmbSalerExt;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.EncryptUtil;
import net.dlyt.qyds.web.service.MmbMasterService;
import net.dlyt.qyds.web.service.MmbSalerService;
import net.dlyt.qyds.web.service.SysSmsCaptchaService;
import net.dlyt.qyds.web.service.common.DataUtils;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by C_Nagai on 2016/6/25.
 */
@Controller
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private MmbMasterService mmbMasterService;

    @Autowired
    private MmbSalerService mmbSalerService;

    @Resource
    private SysSmsCaptchaService sysSmsCaptchaService;

    @RequestMapping("/getProvinceList")
    public JSONObject getProvinceList(){
        JSONObject jsonObject = new JSONObject();
        return jsonObject;
    }

    @RequestMapping("register")
    public
    @ResponseBody
    JSONObject signup(String data, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            JSONObject inJson = JSONObject.parseObject(data);
            String telephone = inJson.getString("telephone");
            String password = inJson.getString("password");
            String captcha = inJson.getString("captcha");
            String memberName = inJson.getString("memberName");
            String sex = inJson.getString("sex");
            String birthdate = inJson.getString("birthdate");
            // TODO: 2017/12/26 加上省份城市的key

            if (StringUtils.isEmpty(telephone)
                    || StringUtils.isEmpty(password)
                    || StringUtils.isEmpty(captcha)
                    || StringUtils.isEmpty(memberName)
                    || StringUtils.isEmpty(sex)
                    || StringUtils.isEmpty(birthdate)) {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "提交的注册信息不足");
                return json;
            }

            if (!StringUtil.isTelephone(telephone)) {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "手机号格式不正确");
                return json;
            }

            // 验证码效验  使用orgId来区分不同的操作动作 10:注册 20:绑定 30:优惠券使用 40:修改密码 90:其他
            JSONObject jsonCaptcha = sysSmsCaptchaService.isValidedCaptcha(telephone, captcha, "10");
            if (jsonCaptcha == null || Constants.FAIL.equals(jsonCaptcha.getString("resultCode"))) {
                return jsonCaptcha;
            }

            MmbMaster sysUser = new MmbMaster();
            sysUser.setTelephone(telephone);
            sysUser.setMemberName(memberName);
            sysUser.setSex("0");
            sysUser.setRegistStyle("10");//10	网站		注册方式-网站
            sysUser.setType("10");//10	普通		会员类型-普通
            sysUser.setMemberLevelId("10");
            sysUser.setPassword(EncryptUtil.encodeMD5(password.toLowerCase()));
            sysUser.setSex(sex);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sysUser.setBirthdate(sdf.parse(birthdate));

            json = mmbMasterService.registerUser(sysUser);
            if (json != null && Constants.NORMAL.equals(json.getString("resultCode"))) {
                MmbMaster user = (MmbMaster) json.get("data");
                user.setPassword("");

                // 设置cookie信息
                String token = EncryptUtil.encrypt(JSON.toJSONString(user));
                Cookie cookieToken = new Cookie("qyds_user_token", token);
                cookieToken.setPath("/");
                cookieToken.setMaxAge(30 * 60 );
                response.addCookie(cookieToken);

            } else {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "注册失败");
            }
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", "注册失败");
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
                cookieToken.setMaxAge(30 * 60);
                response.addCookie(cookieToken);

            }
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", "积分获取失败");
        }
        return json;
    }


    @RequestMapping("login")
    public
    @ResponseBody
    JSONObject login(String data, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            MmbMaster sysUser = (MmbMaster) JSONObject.parseObject(data, MmbMaster.class);

            String telephone = sysUser.getTelephone();
            String password = sysUser.getPassword();
            if (StringUtils.isEmpty(telephone)
                    || StringUtils.isEmpty(password)) {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "请输入正确的登录信息");
                return json;
            }

            sysUser.setPassword(EncryptUtil.encodeMD5(password.toLowerCase()));
            if (!StringUtil.isTelephone(telephone)) {
                MmbSaler mmbSaler = new MmbSaler();
                BeanUtils.copyProperties(sysUser,mmbSaler);
                json = mmbSalerService.loginSelective(mmbSaler);
            }else{
                json = mmbMasterService.loginSelective(sysUser);
            }

            if (json != null && Constants.NORMAL.equals(json.getString("resultCode"))) {
                MmbMasterExt user = (MmbMasterExt) json.get("data");
                user.setPassword("");

                // 设置cookie信息
                String token = EncryptUtil.encrypt(JSON.toJSONString(user));
                Cookie cookieToken = new Cookie("qyds_user_token", token);
                cookieToken.setPath("/");
                cookieToken.setMaxAge(30 * 60);
                response.addCookie(cookieToken);

            }
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", "登录失败");
        }
        return json;
    }

    @RequestMapping("loginSaler")
    public
    @ResponseBody
    JSONObject loginSaler(String data, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            MmbSaler sysUser = (MmbSaler) JSONObject.parseObject(data, MmbSaler.class);

            String telephone = sysUser.getTelephone();
            String password = sysUser.getPassword();
            if (StringUtils.isEmpty(telephone)
                    || StringUtils.isEmpty(password)) {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "请输入正确的登录信息");
                return json;
            }

//            if (!StringUtil.isTelephone(telephone)) {
//                json.put("resultCode", Constants.FAIL);
//                json.put("resultMessage", "手机号格式不正确");
//                return json;
//            }

            sysUser.setPassword(EncryptUtil.encodeMD5(password.toLowerCase()));
            json = mmbSalerService.loginSelective(sysUser);
            if (json != null && Constants.NORMAL.equals(json.getString("resultCode"))) {
                MmbSalerExt user = (MmbSalerExt) json.get("data");
                user.setPassword("");

                // 设置cookie信息
                String token = EncryptUtil.encrypt(JSON.toJSONString(user));
                Cookie cookieToken = new Cookie("qyds_user_token", token);
                cookieToken.setPath("/");
                cookieToken.setMaxAge(30 * 60);
                response.addCookie(cookieToken);

            }
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", "登录失败");
        }
        return json;
    }


    /**
     * 修改密码
     *
     * @param data
     * @return
     */
    @RequestMapping("changePassword")
    @ResponseBody
    public JSONObject changePassword(String data, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            JSONObject inJson = JSONObject.parseObject(data);
            String telephone = inJson.getString("telephone");
            String password = inJson.getString("password");
            String captcha = inJson.getString("captcha");
            if (StringUtils.isEmpty(telephone)
                    || StringUtils.isEmpty(password)
                    || StringUtils.isEmpty(captcha)) {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "请输入正确的修改信息");
                return json;
            }

            if (!StringUtil.isTelephone(telephone)) {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "手机号格式不正确");
                return json;
            }

            // 验证码效验  使用orgId来区分不同的操作动作 10:注册 20:绑定 30:优惠券使用 40:修改密码 90:其他
            JSONObject jsonCaptcha = sysSmsCaptchaService.isValidedCaptcha(telephone, captcha, "40");
            if (jsonCaptcha == null || Constants.FAIL.equals(jsonCaptcha.getString("resultCode"))) {
                return jsonCaptcha;
            }

            MmbMaster sysUser = new MmbMaster();
            sysUser.setTelephone(telephone);
            sysUser.setPassword(EncryptUtil.encodeMD5(password.toLowerCase()));

            json = mmbMasterService.changePassword(sysUser);
            if (json != null && Constants.NORMAL.equals(json.getString("resultCode"))) {
                MmbMaster user = (MmbMaster) json.get("data");
                user.setPassword("");

                // 设置cookie信息
                String token = EncryptUtil.encrypt(JSON.toJSONString(user));
                Cookie cookieToken = new Cookie("qyds_user_token", token);
                cookieToken.setPath("/");
                cookieToken.setMaxAge(30 * 60);
                response.addCookie(cookieToken);

            } else {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "注册失败");
            }
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;

    }

    @RequestMapping("401")
    public
    @ResponseBody
    JSONObject unauthorized() {
        JSONObject json = new JSONObject();
        json.put("resultCode", Constants.UNAUTHORIZED);
        json.put("operation", Constants.FAIL);
        json.put("message", "unauthorized");
        return json;
    }

    @RequestMapping("403")
    public
    @ResponseBody
    JSONObject forbidden() {
        JSONObject json = new JSONObject();
        json.put("resultCode", Constants.FORBIDDEN);
        json.put("operation", Constants.FAIL);
        json.put("message", "forbidden");
        return json;
    }

}
