package net.dlyt.qyds.web.controller.menu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.SysMenu;
import net.dlyt.qyds.common.dto.SysUserExt;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.MenuService;
import net.dlyt.qyds.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by panda on 16/6/23.
 */
@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;


    @RequestMapping("list")
    public String list(){
        return "page/menu/list.html";
    }

    @RequestMapping("getMenu.json")
    @ResponseBody
    public JSONObject getMenu(){
        return menuService.getMenu();
    }

    @RequestMapping("getValidMenu.json")
    @ResponseBody
    public JSONObject getValidMenu(@RequestParam("userId") Integer userId){
        JSONObject userJson = userService.selectByPrimaryKey(Integer.toString(userId));
        if(userJson != null && Constants.NORMAL.equals(userJson.getString("resultCode"))){
            SysUserExt user = (SysUserExt)userJson.get("data");
            return menuService.getRoleMenu(user.getRoleId());
        }else {
            return menuService.getValidMenu(userId);
        }
    }

    @RequestMapping("save.json")
    @ResponseBody
    public JSONObject save(SysMenu menu){
        JSONObject map = new JSONObject();
        if(menu == null){
            map.put("resultCode", Constants.FAIL);
            return map;
        }

        if(StringUtils.isEmpty(menu.getMenuId())){
            menu.setIsValid(Constants.VALID);
            map = menuService.addMenu(menu);
        }else{
            map = menuService.editMenu(menu);
        }
        map.put("message","");
        return map;
    }

    @RequestMapping("delete.json")
    @ResponseBody
    public JSONObject delete(SysMenu menu){
        JSONObject map = new JSONObject();
        if(menu.getMenuId() != null){
            map = menuService.deleteMenu(menu);
        }else{
            map.put("resultCode",Constants.FAIL);
        }
        map.put("message","");
        return map;
    }


    @RequestMapping("resort.json")
    @ResponseBody
    public JSONObject resort(@RequestParam("data") String data){
        JSONObject map = new JSONObject();

        try{
            List<SysMenu> list = JSONArray.parseArray(data,SysMenu.class);
            map = menuService.updateSort(list);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }

}
