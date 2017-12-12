package net.dlyt.qyds.web.controller.shp_org;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.common.dto.ShpOrg;

import net.dlyt.qyds.common.dto.ErpStore;
import net.dlyt.qyds.web.common.Constants;

import net.dlyt.qyds.web.service.ErpStoreService;
import net.dlyt.qyds.web.service.ShpOrgService;
import org.springframework.stereotype.Controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.List;


/**
 * Created by wanglijie on 2016/7/29.
 */
@Controller
@RequestMapping("/shp_org")
public class ShpOrgController {
    @Resource
    private ShpOrgService shpOrgService;

    @Resource
    private ErpStoreService erpStoreService;

    @RequestMapping("list")
    public String list(){
        return "page/shp_org/list.html";
    }

    @RequestMapping("getOrg.json")
    @ResponseBody
    public JSONObject getOrg(){
        JSONObject map = new JSONObject();
        return   shpOrgService.getOrg();
    }
    @RequestMapping("save.json")
    @ResponseBody
    public JSONObject save(ShpOrg org){
        JSONObject map = new JSONObject();
        JSONObject json = new JSONObject();

        if(org == null){
            map.put("resultCode", Constants.FAIL);
            return map;
        }

        if(!StringUtils.isEmpty(org.getErpStoreId())){
            //modify by zlh 2016/8/9
//            ErpStore erpStore = erpStoreService.getById(org.getErpStoreId());
            JSONObject data = erpStoreService.getById(org.getErpStoreId());
            ErpStore erpStore = (ErpStore) data.get("data");
            if(erpStore == null){
                map.put("resultCode", Constants.FAIL);
                map.put("message","ERP店铺ID不存在");
                return map;
            }
        }

        String userId = (String)PamsDataContext.getLoginId();
        if(StringUtils.isEmpty(org.getOrgId())){

            if("20".equals(org.getOrgType())){
                org.setErpStoreId(null);
            }

            //新建
            try{
                org.setOrgId(UUID.randomUUID().toString());
                org.setShopId("00000000");
                org.setInsertUserId(userId);
                org.setUpdateUserId(userId);

                shpOrgService.addOrg(org);

                map.put("resultCode",Constants.NORMAL);
                json.put("flag",Constants.INSERT);

                json.put("id", org.getOrgId());
                map.put("data",json);
            }catch(Exception e){
                map.put("resultCode", Constants.FAIL);
            }
        }else{
            //编辑
            try{
                org.setUpdateUserId(userId);
                shpOrgService.editOrg(org);
                json.put("flag",Constants.UPDATE);
                map.put("data",json);
                map.put("resultCode",Constants.NORMAL);
            }catch(Exception e){
                map.put("resultCode", Constants.FAIL);
            }
        }
        map.put("message","");
        return map;

    }

    @RequestMapping("delete.json")
    @ResponseBody
    public JSONObject delete(ShpOrg org){
        JSONObject map = new JSONObject();
        if(org.getOrgId() != null){
            org.setUpdateUserId((String)PamsDataContext.getLoginId());
            shpOrgService.deleteOrg(org);
            map.put("resultCode",Constants.NORMAL);
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
            List<ShpOrg> list = JSONArray.parseArray(data,ShpOrg.class);
            shpOrgService.updateSort(list);
            map.put("resultCode",Constants.NORMAL);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }
}