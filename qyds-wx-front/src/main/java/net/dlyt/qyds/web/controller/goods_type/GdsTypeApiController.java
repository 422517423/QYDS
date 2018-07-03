package net.dlyt.qyds.web.controller.goods_type;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.service.GdsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by congkeyan on 2016/7/27.
 */
@Controller
@RequestMapping("/gds_type_api")
public class GdsTypeApiController {

    @Autowired
    private GdsTypeService gdsTypeService;

    /**
     * 获取分类的第一层节点
     * 默认ERP分类和电商分类都包含
     * 如果传递参数获取电商分类或者Erp分类的第一层节点
     *
     * @param data type:规则编码
     *             10.ERP分类，20.电商分类
     * @return
     */
    @RequestMapping("getGdsTypeFirstFloor")
    @ResponseBody
    public JSONObject getGdsTypeFirstFloor(String data) {
        return gdsTypeService.getGdsTypeFirstFloor(data);
    }

    /**
     * 获取分类的第一层节点和第二层节点
     * 默认ERP分类和电商分类都包含
     * 如果传递参数获取电商分类或者Erp分类的第一层节点和第二层节点
     *
     * @param data type:规则编码
     *             10.ERP分类，20.电商分类
     * @return
     */
    @RequestMapping("getGdsTypeFirstAndSecondFloor")
    @ResponseBody
    public JSONObject getGdsTypeFirstAndSecondFloor(String data) {
        return gdsTypeService.getGdsTypeFirstAndSecondFloor(data);
    }



    /**
     * 获取第三层数据以及第三层数据的图片
     *
     * @param data goods_type_id:商品分类ID
     * @return
     */
    @RequestMapping("getGdsTypeSubFloor")
    @ResponseBody
    public JSONObject getGdsTypeSubFloor(String data) {
        return gdsTypeService.getGdsTypeSubFloor(data);
    }



    /**
     * 品牌系列的第一级数据获取
     * @return
     */
    @RequestMapping("getGdsBrandType")
    @ResponseBody
    public JSONObject getGdsBrandType() {
        return gdsTypeService.getGdsBrandType();
    }


    /**
     * 品牌系列的第一层节点和第二层节点
     * 默认ERP分类和电商分类都包含
     * 如果传递参数获取电商分类或者Erp分类的第一层节点和第二层节点
     * @return
     */
    @RequestMapping("getGdsBrandTypeFirstAndSecondFloor")
    @ResponseBody
    public JSONObject getGdsBrandTypeFirstAndSecondFloor() {
        return gdsTypeService.getGdsBrandTypeFirstAndSecondFloor();
    }


    /**
     * 获取第三层数据以及第三层数据的图片
     *
     * @param data goods_type_id:品牌系列ID
     * @return
     */
    @RequestMapping("getGdsBrandTypeSubFloor")
    @ResponseBody
    public JSONObject getGdsBrandTypeSubFloor(String data) {
        return gdsTypeService.getGdsBrandTypeSubFloor(data);
    }


    /**
     * 品牌系列的第一层节点和第二层节点和第三层节点
     *
     * @return
     */
    @RequestMapping("getGdsBrandTypeFloor")
    @ResponseBody
    public JSONObject getGdsBrandTypeFloor() {
        return gdsTypeService.getGdsBrandTypeFloor();
    }

    /**
     * 分类的的第一层节点和第二层节点和第三层节点
     *
     * @return
     */
    @RequestMapping("getAllTypes")
    @ResponseBody
    public JSONObject getAllTypes() {
        return gdsTypeService.getAllTypes();
    }


}