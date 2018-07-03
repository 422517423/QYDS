package net.dlyt.qyds.web.controller.gds_sku;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.GdsSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by cky on 2016/7/18.
 */
@Controller
@RequestMapping("/gds_sku")
public class GdsSkuController {

    @Autowired
    private GdsSkuService gdsSkuService;


    /**
     * 保存数据到商品SKU表中
     * @param data
     * @return
     */
    @RequestMapping("save")
    public @ResponseBody
    JSONObject save(String data){

        return gdsSkuService.save(data, PamsDataContext.get());
    }

    /**
     * 通过ID获取数据
     * @param goodsSkuId
     * @return
     */
    @RequestMapping("edit")
    public @ResponseBody
    JSONObject edit(@RequestParam(required = false)String goodsSkuId,@RequestParam(required = false)String skuKey,@RequestParam(required = false)String goodsId){
        return gdsSkuService.edit(goodsSkuId,skuKey,goodsId);
    }



}
