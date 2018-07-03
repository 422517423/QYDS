package net.dlyt.qyds.web.controller.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ShpOrg;
import net.dlyt.qyds.common.dto.SysUser;
import net.dlyt.qyds.common.dto.SysUserExt;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.EncryptUtil;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.quartz.QydsSchedulerFactory;
import net.dlyt.qyds.web.service.ShpOrgService;
import net.dlyt.qyds.web.service.SysRoleService;
import net.dlyt.qyds.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by C_Nagai on 2016/6/25.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private ShpOrgService shpOrgService;

//    @Autowired
//    private PamsRoleService pamsRoleService;

    @RequestMapping("list")
    public String list(){
        return "page/role/list.html";
    }

//    @RequestMapping("getList")
//    public @ResponseBody
//    JSONObject getList(String data){
////        System.out.println(JSON.toJSONString(PamsDataContext.get("sysUser")));
////        System.out.println("userId="+PamsDataContext.get("userId"));
////        System.out.println("userName="+PamsDataContext.get("userName"));
////        System.out.println("loginId="+PamsDataContext.get("loginId"));
//
////        QydsSchedulerFactory.doThumbnail("/50/50527a8d-459c-459b-9ede-5917a427020d.jpg");
//        return userService.selectAll(Constants.ORGID);
//    }

    /**
     * 一览
     *
     * @param form
     * @return
     */
    @RequestMapping("getList")
    @ResponseBody
    public JSONObject getList(SysUserExt form) {

        return userService.selectAll(form);
    }


    @RequestMapping("getSysRole")
    public @ResponseBody
    JSONObject getSysRole(String data){
        return sysRoleService.selectAll();
    }

    @RequestMapping("getShpChildOrg")
    public @ResponseBody
    JSONObject getShpOrg(String data){
        return  shpOrgService.getChildOrg();
    }

    @RequestMapping("edit")
    public @ResponseBody JSONObject edit(@RequestParam(required = false)String id){
        return userService.selectByPrimaryKey(id);
    }

    @RequestMapping("save")
    public @ResponseBody
    JSONObject save(String data){
        JSONObject json = null;
        try{
            SysUserExt sysUserExt = (SysUserExt) JSON.parseObject(data, SysUserExt.class);
            SysUser sysUser = (SysUser) JSON.parseObject(data, SysUser.class);
            SysUserExt user = null;
            sysUser.setShpId(Constants.ORGID);
            if (StringUtils.isEmpty(sysUserExt.getUserId())) {
                sysUser.setIsValid(Constants.VALID);
                String password = sysUser.getLoginId();
                password = EncryptUtil.encodeMD5(password.toLowerCase());
                password = EncryptUtil.encodeMD5(password.toLowerCase());
                sysUser.setPassword(password);
                json = userService.insertSelective(sysUserExt, sysUser);
            } else {
                sysUser.setPassword(null);
                json = userService.updateByPrimaryKeySelective(sysUserExt, sysUser);
            }
            if (json != null) {
                return json;
            }
        }catch(Exception e){
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
        }
        return json;

    }

    @RequestMapping("changePassword")
    public @ResponseBody
    JSONObject changePassword(String data){
        JSONObject json = null;
        try{
            SysUser sysUser = (SysUser) JSON.parseObject(data, SysUser.class);
            sysUser.setPassword(EncryptUtil.encodeMD5(sysUser.getPassword().toLowerCase()));
            json = userService.changePassword(sysUser);
            json = userService.updateByPrimaryKeySelective(sysUser);
        }catch(Exception e){
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    @RequestMapping("delete")
    public @ResponseBody
    JSONObject delete(String data){
        JSONObject json = null;
        try{
            SysUser sysUser = (SysUser) JSON.parseObject(data, SysUser.class);
            json = userService.deleteByPrimaryKey(sysUser);
        }catch(Exception e){
            if(json == null){
                json = new JSONObject();
            }
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    //重置密码
    @RequestMapping("resetPassword")
    public @ResponseBody
    JSONObject resetPassword(String data){
        JSONObject json = null;
        try{
            SysUser sysUser = (SysUser) JSON.parseObject(data, SysUser.class);
            String password = sysUser.getLoginId();
            password = EncryptUtil.encodeMD5(password.toLowerCase());
            password = EncryptUtil.encodeMD5(password.toLowerCase());
            sysUser.setPassword(password);
            json = userService.resetPassword(sysUser);
        }catch(Exception e){
            if(json == null){
                json = new JSONObject();
            }
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    //修改密码的时候check填写的密码是否正确
    @RequestMapping("checkOldPassword")
    public @ResponseBody void checkloginId(HttpServletResponse response, String loginId, String password){
        try{
            JSONObject json = userService.selectByLoginId(loginId);
            if (json != null && Constants.NORMAL.equals(json.getString("resultCode"))) {
                SysUser user = (SysUser)json.get("data");
                if (EncryptUtil.encodeMD5(password.toLowerCase()).equals(user.getPassword())) {
                    response.getWriter().print(true);
                } else {
                    response.getWriter().print(false);
                }
            } else {
                response.getWriter().print(true);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
