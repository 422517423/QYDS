package net.dlyt.qyds.web.controller.gds_type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsType;
import net.dlyt.qyds.common.dto.GdsTypeDetail;
import net.dlyt.qyds.web.service.GdsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by congkeyan on 16/7/23.
 */
@Controller
@RequestMapping("/gds_type")
public class GdsTypeController {

    //service 注入
    @Autowired
    private GdsTypeService gdsTypeService;

    /**
     * 获取分类列表
     * @return
     */
    @RequestMapping("getTreeList.json")
    @ResponseBody
    public JSONObject getTreeList(@RequestParam("type") String type){
        return gdsTypeService.getTreeList(type);
    }


    @RequestMapping("save.json")
    @ResponseBody
    public JSONObject save(@RequestParam("data") String data){
        //获取前台传递的数据
        GdsType gdsType = JSON.parseObject(data, GdsType.class);
        GdsTypeDetail gdsTypeDetail = JSON.parseObject(data, GdsTypeDetail.class);
        //店铺ID
        return gdsTypeService.save(gdsType, gdsTypeDetail);
    }
    /**
     * 通过ID获取数据
     * @param goodsTypeId
     * @return
     */
    @RequestMapping("edit")
    public @ResponseBody JSONObject edit(@RequestParam(required = false)String goodsTypeId){
        return gdsTypeService.edit(goodsTypeId);
    }

    /**
     * 删除分类方法
     * @param goodsTypeId
     * @return
     */
    @RequestMapping("delete.json")
    @ResponseBody
    public JSONObject delete(@RequestParam(required = false)String goodsTypeId){
        return gdsTypeService.delete(goodsTypeId);
    }

    /**
     * 删除分类方法
     * @param goodsType
     * @return
     */
    @RequestMapping("getGdsTypeFirstFloorList.json")
    @ResponseBody
    public JSONObject getGdsTypeFirstFloorList(String goodsType){
        return gdsTypeService.getGdsTypeFirstFloor(goodsType);
    }

    @RequestMapping("resort.json")
    @ResponseBody
    public JSONObject resort(@RequestParam("data") String data){

        return gdsTypeService.updateSort(data);
    }

}
