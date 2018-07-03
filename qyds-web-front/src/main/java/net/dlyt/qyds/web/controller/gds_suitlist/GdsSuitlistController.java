package net.dlyt.qyds.web.controller.gds_suitlist;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.GdsMasterForm;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.GdsMasterService;
import net.dlyt.qyds.web.service.GdsSuitlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by cky on 2016/7/18.
 */
@Controller
@RequestMapping("/gds_suitlist")
public class GdsSuitlistController {

    @Autowired
    private GdsSuitlistService gdsSuitlistService;

    /**
     * 根据店铺ID获取商品品牌列表
     * @param form
     * @return
     */
    @RequestMapping("getList")
    public @ResponseBody
    JSONObject getList(GdsMasterForm form){
        return gdsSuitlistService.getList(form);
    }

    /**
     * 保存数据到商品SKU表中
     * @param data
     * @return
     */
    @RequestMapping("save")
    public @ResponseBody
    JSONObject save(String data){
        return gdsSuitlistService.save(data, PamsDataContext.get());
    }

    /**
     * 获取已经选择的商品信息
     * @param form
     * @return
     */
    @RequestMapping("getSelectList")
    public @ResponseBody
    JSONObject getSelectList(GdsMasterForm form){
        return gdsSuitlistService.getSelectList(form);
    }

    /**
     * 通过ID删除数据
     * @param suitId
     * @return
     */
    @RequestMapping("delete")
    public @ResponseBody JSONObject delete(@RequestParam(required = false)String suitId){
       return gdsSuitlistService.delete(suitId);
    }

}
