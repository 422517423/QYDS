package net.dlyt.qyds.web.controller.cms_item;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsMasterExt;
import net.dlyt.qyds.web.service.CmsItemsService;
import net.dlyt.qyds.web.service.GdsMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by C_Nagai on 2016/8/29.
 */
@Controller
@RequestMapping("/cms_items_api")
public class CmsItemsApiController {

    @Autowired
    private CmsItemsService cmsItemsService;

    /**
     * cms管理维护数据取得
     * @param data
     * @return
     */
    @RequestMapping("getMetaData")
    public @ResponseBody
    JSONObject getMetaData(String data){
        return cmsItemsService.getMetaData(data);
    }

    /**
     * cms管理维护数据取得
     * @param data
     * @return
     */
    @RequestMapping("getDeliverData")
    public @ResponseBody
    JSONObject getDeliiverData(String data){
        return cmsItemsService.getDeliverData(data);
    }

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
     * cms管理维护数据取得，根据多code获取数据
     * @param data
     * @return
     */
    @RequestMapping("getMasterByItemArray")
    public @ResponseBody
    JSONObject getMasterByItemArray(String data){
        return cmsItemsService.getMasterByItemArray(data);
    }


    @RequestMapping("getContentHtml")
    public @ResponseBody
    JSONObject getContentHtml(String data){
        return cmsItemsService.getContentHtml(data);
    }

    @RequestMapping("getContentHtmlByCmsId")
    public @ResponseBody
    JSONObject getContentHtmlByCmsId(String data){
        return cmsItemsService.getContentHtmlByCmsId(data);
    }

    @RequestMapping("getContentHtmlByItemCode")
    public @ResponseBody
    JSONObject contactUs(String data){
        return cmsItemsService.getContentHtmlByItemCode(data);
    }

    /**
     * 根据商品分类获取二级主页面
     *
     * @return
     */
    @RequestMapping("getGoodsTypeIndex")
    @ResponseBody
    public JSONObject getGoodsTypeIndex(String data) {

        //转换数据格式
        GdsMasterExt gdsMasterExt = JSON.parseObject(data, GdsMasterExt.class);
        String memberId = gdsMasterExt.getMemberId();
        gdsMasterExt.setMemberId(null);
        data = JSON.toJSON(gdsMasterExt).toString();
        JSONObject jsonObject =  cmsItemsService.getGoodsTypeIndex(data);

        List<GdsMasterExt> list = (ArrayList<GdsMasterExt>)jsonObject.get("goodsList");
        list = cmsItemsService.resetGoodsList(list,memberId);
        jsonObject.put("goodsList",list);
        return jsonObject;

//        return cmsItemsService.getGoodsTypeIndex(data);
    }

    /**
     * 获取杂志背景故事
     *
     * @return
     */
    @RequestMapping("getTheStory")
    @ResponseBody
    public JSONObject getTheStory(String data) {
        return cmsItemsService.getTheStory(data);
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
}
