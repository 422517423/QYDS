package net.dlyt.qyds.web.controller.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.SysUser;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.EncryptUtil;
import net.dlyt.qyds.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.Enumeration;

/**
 * Created by C_Nagai on 2016/6/25.
 */
@Controller
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("checkloginId")
    public @ResponseBody void checkloginId(HttpServletResponse response, String loginId, String userId){
        try{
            JSONObject json = userService.selectByLoginId(loginId);
            if (json != null && Constants.NORMAL.equals(json.getString("resultCode"))) {
                SysUser user = (SysUser)json.get("data");
                if (userId != null && userId.equals(user.getUserId().toString())) {
                    response.getWriter().print(true);
                } else {
                    response.getWriter().print(false);
                }
            } else {
                response.getWriter().print(true);
            }
        }catch(Exception e){
//            response.getWriter().print(false);
        }
    }

    @RequestMapping("checkUsrPsw")
    public @ResponseBody void checkUsrPsw(HttpServletResponse response, String loginId, String password){
        try{
            SysUser sysUser = new SysUser();
            sysUser.setLoginId(loginId);
            sysUser.setPassword(EncryptUtil.encodeMD5(password.toLowerCase()));
            JSONObject json = userService.selectByLoginIdAndPassword(sysUser);
            if (json != null && Constants.NORMAL.equals(json.getString("resultCode"))) {
                SysUser user = (SysUser)json.get("data");
                if (user != null) {
                    response.getWriter().print(true);

                } else {
                    response.getWriter().print(false);
                }
            }else {
                response.getWriter().print(false);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("login")
        public @ResponseBody JSONObject login(String data, HttpServletResponse response, HttpServletRequest request){
        JSONObject json = null;
        try{
            if("login".equals(request.getSession().getAttribute("loginTag"))){
                json = new JSONObject();
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "当前浏览器已经登录其他账号，请先退出或彻底重启浏览器。");
            }else{
                SysUser sysUser = (SysUser) JSONObject.parseObject(data, SysUser.class);

                String sameValue = "0";
                if(sysUser.getPassword().equals(sysUser.getLoginIdMd())){
                    sameValue = "1";
                }

                sysUser.setPassword(EncryptUtil.encodeMD5(sysUser.getPassword().toLowerCase()));

                json = userService.selectByLoginIdAndPassword(sysUser);
                if (json != null && Constants.NORMAL.equals(json.getString("resultCode"))) {
                    SysUser user = (SysUser)json.get("data");
                    user.setPassword("");
                    // 设置cookie信息
                    user.setUserName(user.getUserName());

                    Cookie cookieDomain = new Cookie("the_user_info", null);
                    cookieDomain.setMaxAge(0);

                    cookieDomain = new Cookie("the_user_info", JSON.toJSONString(user));
//                    cookieDomain.setSecure(true);
                    cookieDomain.setPath("/");
                    cookieDomain.setMaxAge(30 * 60);
                    response.addCookie(cookieDomain);

                    String token = EncryptUtil.encrypt(JSON.toJSONString(user));

                    Cookie cookieToken = new Cookie("qyds_admin_token", null);
                    cookieToken.setMaxAge(0);
                    cookieToken = new Cookie("qyds_admin_token", token);
//                    cookieDomain.setSecure(true);
                    cookieToken.setPath("/");
                    cookieToken.setMaxAge(30 * 60);
                    response.addCookie(cookieToken);

                    json.put("sameValue",sameValue);

                    request.getSession().setAttribute("loginTag","login");
                } else {
                    json = new JSONObject();
                    json.put("resultCode", Constants.FAIL);
                    json.put("resultMessage", "用户名或密码错误。");
                }
            }

        }catch(Exception e){
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", "用户名或密码错误。");
        }
        return json;
    }

    @RequestMapping("logout")
    public @ResponseBody JSONObject logout(String data, HttpServletResponse response, HttpServletRequest request){
        JSONObject json = null;
        try{
            // 清除session
            Enumeration<String> em = request.getSession().getAttributeNames();
            while (em.hasMoreElements()) {
                request.getSession().removeAttribute(em.nextElement().toString());
            }
            request.getSession().invalidate();

            Cookie[] cookies = request.getCookies();
            for(Cookie cookie : cookies ){
                cookie.setMaxAge(0);
            }

            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", "用户名或密码错误。");
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
