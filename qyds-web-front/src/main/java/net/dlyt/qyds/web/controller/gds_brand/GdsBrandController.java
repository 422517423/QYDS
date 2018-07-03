package net.dlyt.qyds.web.controller.gds_brand;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsBrand;
import net.dlyt.qyds.common.dto.GdsBrandExt;
import net.dlyt.qyds.common.form.GdsBrandForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.DataUtils;
import net.dlyt.qyds.web.service.GdsBrandService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Created by cky on 2016/7/18.
 */
@Controller
@RequestMapping("/gds_brand")
public class GdsBrandController {


    @Autowired
    private GdsBrandService gdsBrandService;


    /**
     * 根据店铺ID获取商品品牌列表(没有分页,没有检索条件)
     * @return
     */
    @RequestMapping("getAllList")
    public @ResponseBody
    JSONObject getAllList(String type){
        //数据库检索
        return gdsBrandService.getAllList(type);
    }

    /**
     * 根据店铺ID获取商品品牌列表
     * @param form
     * @return
     */
    @RequestMapping("getList")
    public @ResponseBody
    JSONObject getList(GdsBrandForm form){
        //数据库检索
        return gdsBrandService.selectAll(form);

    }

    /**
     * 保存数据到商品品牌表中
     * @param data
     * @return
     */
    @RequestMapping("save")
    public @ResponseBody
    JSONObject save(String data){

        GdsBrand gdsBrand = JSON.parseObject(data, GdsBrand.class);
        return gdsBrandService.save(gdsBrand);

    }

    /**
     * 通过ID获取数据
     * @param brandId
     * @return
     */
    @RequestMapping("edit")
    public @ResponseBody JSONObject edit(@RequestParam(required = false)String brandId){
        return gdsBrandService.selectByPrimaryKey(brandId);
    }

    /**
     * 通过ID删除数据
     * @param brandId
     * @return
     */
    @RequestMapping("delete")
    public @ResponseBody JSONObject delete(@RequestParam(required = false)String brandId){
        return gdsBrandService.delete(brandId);
    }


}
