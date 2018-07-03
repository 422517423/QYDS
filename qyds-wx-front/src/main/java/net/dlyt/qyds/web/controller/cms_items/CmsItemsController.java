package net.dlyt.qyds.web.controller.cms_items;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.CmsItems;
import net.dlyt.qyds.common.dto.ext.CmsItemsExt;
import net.dlyt.qyds.web.service.CmsItemsService;
import net.dlyt.qyds.web.service.GdsMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by C_Nagai on 2016/8/2.
 */
@Controller
@RequestMapping("/cms_items_api")
public class CmsItemsController {

    @Autowired
    private CmsItemsService cmsItemsService;

    @Autowired
    private GdsMasterService gdsMasterService;


    /**
     * cms管理维护数据取得
     * @param data
     * @return
     */
    @RequestMapping("getMasterByItemList")
    public @ResponseBody
    JSONObject getMasterByItemList(String data){
        return cmsItemsService.getMasterByItemList(data);
    }

    /**
     * cms管理维护数据取得
     * 已栏目为元素的列表(footer取得)
     * @param data
     * @return
     */
    @RequestMapping("getListByItem")
    public @ResponseBody
    JSONObject getListByItem(String data){
        return cmsItemsService.getListByItem(data);
    }

    /**
     * 根据商品分类获取新品商品
     *
     * @return
     */
    @RequestMapping("getNewGoods")
    @ResponseBody
    public JSONObject getNewGoods(String data) {
        return cmsItemsService.getNewGoods(data);
    }

    @RequestMapping("getContentHtml")
    public @ResponseBody
    JSONObject getContentHtml(String data){
        return cmsItemsService.getContentHtml(data);
    }

}
