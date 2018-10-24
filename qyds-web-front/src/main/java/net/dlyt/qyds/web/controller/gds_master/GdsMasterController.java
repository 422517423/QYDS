package net.dlyt.qyds.web.controller.gds_master;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ErpGoods;
import net.dlyt.qyds.common.form.GdsMasterForm;
import net.dlyt.qyds.common.form.SkuForm;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.ErpGoodsService;
import net.dlyt.qyds.web.service.GdsMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by cky on 2016/7/18.
 */
@Controller
@RequestMapping("/gds_master")
public class GdsMasterController {


    @Autowired
    private GdsMasterService gdsMasterService;

    @Autowired
    private ErpGoodsService erpGoodsService;

    /**
     * 根据店铺ID获取商品品牌列表
     * @return
     */
    @RequestMapping("getAllList")
    public @ResponseBody
    JSONObject getAllList(){
        return gdsMasterService.getAllList();
    }

    /**
     * 根据店铺ID获取商品品牌列表
     * @param form
     * @return
     */
    @RequestMapping("getList")
    public @ResponseBody
    JSONObject getList(GdsMasterForm form){
        return gdsMasterService.selectAll(form);
    }

    @RequestMapping("getOnsellSkuList")
    public @ResponseBody
    JSONObject getOnsellSkuList(SkuForm form){
        return gdsMasterService.selectOnsellSku(form);
    }

    @RequestMapping("getOnsellSkuListForAll")
    public @ResponseBody
    JSONObject getOnsellSkuListForAll(SkuForm form){
        form.setsEcho(null);
        form.setiDisplayLength(1000);
        form.setiDisplayStart(0);
        return gdsMasterService.selectOnsellSku(form);
    }

    @RequestMapping("getSkuColorList")
    public @ResponseBody
    JSONObject getSkuColorList(SkuForm form){
        return gdsMasterService.getSkuColorList(form);
    }


    /**
     * 保存排序数据
     * @param data
     * @return
     */
    @RequestMapping("sortSave")
    public @ResponseBody
    JSONObject sortSave(String data){
        return gdsMasterService.sortSave(data);
    }

    /**
     * 保存安全库存数据
     * @param data
     * @return
     */
    @RequestMapping("storeSave")
    public @ResponseBody
    JSONObject storeSave(String data){
        return gdsMasterService.storeSave(data);
    }


    /**
     * 保存数据到商品表中
     * @param data
     * @return
     */
    @RequestMapping("save")
    public @ResponseBody
    JSONObject save(String data){
        return gdsMasterService.save(data, PamsDataContext.get());
    }

    /**
     * 通过ID获取数据
     * @param goodsId
     * @return
     */
    @RequestMapping("edit")
    public @ResponseBody JSONObject edit(@RequestParam(required = false)String goodsId){
        return gdsMasterService.edit(goodsId);
    }

    /**
     * 通过商品代码获取ERP商品SKU数据列表
     * @param goodCode
     * @return
     */
    @RequestMapping("erpSkuList")
    public @ResponseBody JSONObject erpSkuList(@RequestParam(required = false)String goodCode){
        return erpGoodsService.selectByCode(goodCode);
    }

    /**
     * 通过ID删除数据
     * @param goodsId
     * @return
     */
    @RequestMapping("delete")
    public @ResponseBody JSONObject delete(@RequestParam(required = false)String goodsId){
        return gdsMasterService.delete(goodsId);
    }



    /**
     * 通过ID发布商品
     * @param goodsId
     * @return
     */
    @RequestMapping("publicGoods")
    public @ResponseBody JSONObject publicGoods(@RequestParam(required = false)String goodsId){
        return gdsMasterService.publicGoods(goodsId, PamsDataContext.get());
    }

    /**
     * 通过ID获取sku数据
     * @param goodsId
     * @return
     */
    @RequestMapping("skuEdit")
    public @ResponseBody JSONObject skuEdit(@RequestParam(required = false)String goodsId){
        return gdsMasterService.skuEdit(goodsId);
    }

    /**
     * 获取活动商品列表
     * @param form 活动ID
     * @return
     */
    @RequestMapping("selectAllByActId")
    public @ResponseBody
    JSONObject selectAllByActId(GdsMasterForm form){
        return gdsMasterService.selectAllByActId(form);

    }

    /**
     * 通过ID下架商品
     * @param goodsId
     * @return
     */
    @RequestMapping("master_unsell")
    public @ResponseBody JSONObject unsellGoods(@RequestParam(required = false)String goodsId){
        return gdsMasterService.unsellGoods(goodsId, PamsDataContext.get());
    }

    /**
     * 获取选中商品信息
     * @param goodsIds
     * @return
     */
    @RequestMapping("getGoodsByGoodsId")
    public @ResponseBody JSONObject getGoodsByGoodsId(@RequestParam(required = false)String goodsIds){
        return gdsMasterService.getGoodsByGoodsId(goodsIds);
    }

    /**
     * 通过ID删除数据
     * @param data
     * @return
     */
    @RequestMapping("checkSku")
    public @ResponseBody JSONObject checkSku(String data){
        return gdsMasterService.checkSku(data);
    }

    /**
     * 通过分类获取商品
     * @param form
     * @return
     */
    @RequestMapping("selectAllByGoodsTypeId")
    public @ResponseBody JSONObject selectAllByGoodsTypeId(GdsMasterForm form){
        return gdsMasterService.selectAllByGoodsTypeId(form);
    }

    /**
     * 商品预约
     * @param form
     * @return
     */
    @RequestMapping("getGoodsOrder.json")
    @ResponseBody
    public JSONObject getGoodsOrder(GdsMasterForm form){
        return gdsMasterService.getGoodsOrderList(form);
    }

}
