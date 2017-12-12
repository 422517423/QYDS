package net.dlyt.qyds.web.controller.shp_master;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ShpMaster;
import net.dlyt.qyds.common.dto.ShpMasterExt;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.DataUtils;
import net.dlyt.qyds.web.service.ShpMasterService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by wy on 2016/7/18.
 */
@Controller
@RequestMapping("/shp_master")
public class ShpMasterController {
    @Resource
    private ShpMasterService shpMasterService;

    /*获取店铺列表*/
    @RequestMapping("getList")
    public
    @ResponseBody
    JSONObject getList(String data) {
        return shpMasterService.selectAll(data);
    }

    /* 根据店铺ID获取列表*/
    @RequestMapping("edit")
    public
    @ResponseBody
    JSONObject edit(@RequestParam(required = false) String shopId) {
        return shpMasterService.edit(shopId);
    }


    /*根据ID删除店铺信息*/
    @RequestMapping("delete")
    public
    @ResponseBody
    JSONObject delete(@RequestParam(required = false) String shopId) {
     return shpMasterService.delete(shopId);
    }

    /*新建和编辑完毕点击保存*/
    @RequestMapping("save")
    public
    @ResponseBody
    JSONObject save(String data) {
        return shpMasterService.save(data);

    }
}