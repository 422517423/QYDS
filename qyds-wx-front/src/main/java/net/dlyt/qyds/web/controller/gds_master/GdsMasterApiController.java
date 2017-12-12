package net.dlyt.qyds.web.controller.gds_master;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsMasterExt;
import net.dlyt.qyds.web.service.GdsMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by congkeyan on 2016/7/27.
 */
@Controller
@RequestMapping("/gds_master_api")
public class GdsMasterApiController {

    @Autowired
    private GdsMasterService gdsMasterService;

    /**
     * 商品列表的获取方法
     *
     * @param data
     * 包含两个参数 1通过主key获得商品列表
     *            2通过分类获得商品列表
     *            3通过品牌获得商品列表
     *
     *            4必须传递的参数 updateTime 分页用 第一次不传递
     *
     * @return
     */
    @RequestMapping("getProductList")
    @ResponseBody
    public JSONObject getProductList(String data) {

        //转换数据格式
        GdsMasterExt gdsMasterExt = (GdsMasterExt) JSON.parseObject(data, GdsMasterExt.class);
        String memberId = gdsMasterExt.getMemberId();
        gdsMasterExt.setMemberId(null);
        data = JSON.toJSON(gdsMasterExt).toString();
        JSONObject jsonObject =  gdsMasterService.getProductList(data);

        List<GdsMasterExt> list = (ArrayList<GdsMasterExt>)jsonObject.get("results");
        list = gdsMasterService.resetGoodsList(list,memberId);
        jsonObject.put("results",list);
        return jsonObject;
    }


    /**
     * 商品详细的获取方法
     *
     * @param data
     * 包含两个参数 商品ID
     *
     * @return
     */
    @RequestMapping("getProductDetail")
    @ResponseBody
    public JSONObject getProductDetail(String data) {
        return gdsMasterService.getProductDetail(data);
    }


    /**
     * 商品列表的获取方法
     *
     * @param data
     * 包含参数 逗号分割的字符串
     *
     * @return
     */
    @RequestMapping("getProductListBySequre")
    @ResponseBody
    public JSONObject getProductListBySequre(String data) {
        return gdsMasterService.getProductListBySequre(data);
    }

    /**
     * 通过活动ID获取商品列表
     *
     * @param data
     * 参数 活动ID
     *
     * @return
     */
    @RequestMapping("getProductListByActivityId")
    @ResponseBody
    public JSONObject getProductListByActivityId(String data) {


        //转换数据格式
        GdsMasterExt gdsMasterExt = (GdsMasterExt) JSON.parseObject(data, GdsMasterExt.class);
        String memberId = gdsMasterExt.getMemberId();
        gdsMasterExt.setMemberId(null);
        data = JSON.toJSON(gdsMasterExt).toString();
        JSONObject jsonObject =  gdsMasterService.getProductListByActivityId(data);

        List<GdsMasterExt> list = (ArrayList<GdsMasterExt>)jsonObject.get("results");
        list = gdsMasterService.resetGoodsList(list,memberId);
        jsonObject.put("results",list);
        return jsonObject;

//        return gdsMasterService.getProductListByActivityId(data);
    }

    @RequestMapping("getGiftDetailByCode")
    @ResponseBody
    public JSONObject getGiftDetailByCode(String data) {
        return gdsMasterService.getGiftDetailByCode(data);
    }

    /**
     * 获取秒杀活动时间段
     *
     * @param data
     *
     * @return
     */
    @RequestMapping("getSecKillActivityTimes")
    @ResponseBody
    public JSONObject getSecKillActivityTimes(String data) {
        return gdsMasterService.getSecKillActivityTimes(data);
    }

    /**
     * 获取秒杀活动商品列表
     *
     * @param data memberId
     *
     * @return
     */
    @RequestMapping("getSecKillProductList")
    @ResponseBody
    public JSONObject getSecKillProductList(String data) {
        return gdsMasterService.getSecKillProductList(data);
    }

    /**
     * 活动种类列表获取(打折和满减两种)
     *
     * @return
     */
    @RequestMapping("activityTypeListService")
    @ResponseBody
    public JSONObject activityTypeListService(String data) {
        return gdsMasterService.activityTypeListService(data);
    }

}