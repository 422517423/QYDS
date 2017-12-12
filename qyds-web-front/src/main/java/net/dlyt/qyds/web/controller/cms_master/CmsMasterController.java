package net.dlyt.qyds.web.controller.cms_master;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.CmsItems;
import net.dlyt.qyds.common.dto.CmsMaster;
import net.dlyt.qyds.common.dto.CmsMasterGoods;
import net.dlyt.qyds.common.dto.SysUser;
import net.dlyt.qyds.common.dto.ext.CmsItemsExt;
import net.dlyt.qyds.common.dto.ext.CmsMasterExt;
import net.dlyt.qyds.common.form.CmsMsterForm;
import net.dlyt.qyds.common.form.MmbGroupForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.EncryptUtil;
import net.dlyt.qyds.web.service.CmsItemsService;
import net.dlyt.qyds.web.service.CmsMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/**
 * Created by C_Nagai on 2016/7/30.
 */
@Controller
@RequestMapping("/cms_master")
public class CmsMasterController {

    @Autowired
    private CmsMasterService cmsMasterService;

    @RequestMapping("getList")
    @ResponseBody
    public JSONObject getList(CmsMsterForm form) {
        return cmsMasterService.getList(form);
    }

    @RequestMapping("edit")
    @ResponseBody
    public JSONObject edit(String data) {
        CmsMaster form = JSON.parseObject(data, CmsMaster.class);
        return cmsMasterService.selectCmsMasterByCmsId(form);
    }

    @RequestMapping("save")
    @ResponseBody
    public JSONObject save(String data) {
        CmsMasterExt form = JSON.parseObject(data, CmsMasterExt.class);
        return cmsMasterService.save(form);
    }

    @RequestMapping("orderList")
    @ResponseBody
    public JSONObject orderList() {
        return cmsMasterService.orderList();
    }

    @RequestMapping("resort")
    @ResponseBody
    public JSONObject resort(@RequestParam("data") String data){
        return cmsMasterService.updateSort(data);

    }

    @RequestMapping("delete")
    @ResponseBody
    public JSONObject delete(@RequestParam("cmsId") String cmsId){
        return cmsMasterService.delete(cmsId);

    }

    @RequestMapping("checkGdsTypeId")
    @ResponseBody
    public void checkGdsTypeId(HttpServletResponse response, String gdsTypeId, String itemId, String cmsId){
        try{
            boolean result = cmsMasterService.checkGdsTypeId(gdsTypeId, itemId, cmsId);
            response.getWriter().print(result);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 通过cmsId获取商品列表
     * @param cmsId
     * @return
     */
    @RequestMapping("getMasterGoodsByCmsId")
    public @ResponseBody JSONObject getMasterGoodsByCmsId(@RequestParam(required = false)String cmsId){
        return cmsMasterService.getMasterGoodsByCmsId(cmsId);
    }

    @RequestMapping("resortCmsGdsIds")
    @ResponseBody
    public JSONObject resortCmsGdsIds(@RequestParam("data") String data){
        return cmsMasterService.resortCmsGdsIds(data);

    }

    @RequestMapping("resetCmsGdsIds")
    @ResponseBody
    public JSONObject resetCmsGdsIds(@RequestParam("data") String data){
        return cmsMasterService.resetCmsGdsIds(data);

    }

}
