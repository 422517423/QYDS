package net.dlyt.qyds.web.controller.gds_sell;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.GdsSellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by cky on 2016/7/18.
 */
@Controller
@RequestMapping("/gds_sell")
public class GdsSellController {

    @Autowired
    private GdsSellService gdsSellService;

    /**
     * 查询上架信息表获取商品的上架信息
     * 检索出已经配置的相关推荐
     * 见错处已经配置的配套商品
     *
     * @param goodsId
     * @return
     */
    @RequestMapping("edit")
    public @ResponseBody
    JSONObject edit(String goodsId){
        return gdsSellService.edit(goodsId);
    }

    /**
     * 保存数据到商品上架信息中
     * @param data
     * @return
     */
    @RequestMapping("save")
    public @ResponseBody
    JSONObject save(String data){
       return gdsSellService.save(data, PamsDataContext.get());
    }

    /**
     * 保存数据到商品上架信息中
     * @param goodsIds
     * @return
     */
    @RequestMapping("selectDatas")
    public @ResponseBody
    JSONObject selectDatas(String goodsIds){
        return gdsSellService.selectDatas(goodsIds);
    }

    /**
     * 通过ID删除数据
     * @param goodsId
     * @return
     */
    @RequestMapping("delete")
    public @ResponseBody JSONObject delete(@RequestParam(required = false)String goodsId,@RequestParam(required = false)String delGoodsId,@RequestParam(required = false)String flag){
        return gdsSellService.delete(goodsId, delGoodsId, flag, PamsDataContext.get());
    }

}
