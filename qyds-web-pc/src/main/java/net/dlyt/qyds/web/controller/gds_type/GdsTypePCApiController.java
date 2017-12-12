package net.dlyt.qyds.web.controller.gds_type;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.service.GdsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by C_Nagai on 2016/9/4.
 */
@Controller
@RequestMapping("/gds_type_pc_api")
public class GdsTypePCApiController {

    @Autowired
    private GdsTypeService gdsTypeService;

    /**
     * 获取分类的第一层节点和第二层节点
     * 默认ERP分类和电商分类都包含
     * 如果传递参数获取电商分类或者Erp分类的第一层节点和第二层节点
     *
     * @param data type:规则编码
     *             10.ERP分类，20.电商分类
     * @return
     */
    @RequestMapping("getGdsTypeFloor")
    @ResponseBody
    public JSONObject getGdsTypeFloor(String data) {
        return gdsTypeService.getGdsTypeFloor(data);
    }

    /**
     * 获取分类的第一层节点和第二层节点
     * 默认ERP分类和电商分类都包含
     * 如果传递参数获取电商分类或者Erp分类的第一层节点和第二层节点
     *
     * @return
     */
    @RequestMapping("getGdsTypeFloorByPhone")
    @ResponseBody
    public JSONObject getGdsTypeFloorByPhone(String data) {
        return gdsTypeService.getGdsTypeFloorByPhone(data);
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
