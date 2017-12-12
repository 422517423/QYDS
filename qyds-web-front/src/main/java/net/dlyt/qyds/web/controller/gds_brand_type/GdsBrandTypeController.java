package net.dlyt.qyds.web.controller.gds_brand_type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsBrandTypeDetail;
import net.dlyt.qyds.common.dto.GdsBrandType;
import net.dlyt.qyds.web.service.GdsBrandTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by congkeyan on 16/7/23.
 */
@Controller
@RequestMapping("/gds_brand_type")
public class GdsBrandTypeController {

    //service 注入
    @Autowired
    private GdsBrandTypeService gdsBrandTypeService;

    /**
     * 获取分类列表
     * @return
     */
    @RequestMapping("getTreeList.json")
    @ResponseBody
    public JSONObject getTreeList(){
        return gdsBrandTypeService.getTreeList();
    }


    @RequestMapping("save.json")
    @ResponseBody
    public JSONObject save(@RequestParam("data") String data){
        //获取前台传递的数据
        GdsBrandType gdsType = JSON.parseObject(data, GdsBrandType.class);
        GdsBrandTypeDetail gdsTypeDetail = JSON.parseObject(data, GdsBrandTypeDetail.class);
        //店铺ID
        return gdsBrandTypeService.save(gdsType, gdsTypeDetail);
    }
    /**
     * 通过ID获取数据
     * @param goodsTypeId
     * @return
     */
    @RequestMapping("edit")
    public @ResponseBody JSONObject edit(@RequestParam(required = false)String goodsTypeId){
        return gdsBrandTypeService.edit(goodsTypeId);
    }

    /**
     * 删除分类方法
     * @param goodsTypeId
     * @return
     */
    @RequestMapping("delete.json")
    @ResponseBody
    public JSONObject delete(@RequestParam(required = false)String goodsTypeId){
        return gdsBrandTypeService.delete(goodsTypeId);
    }
//
//    /**
//     * 删除分类方法
//     * @param goodsType
//     * @return
//     */
//    @RequestMapping("getGdsTypeFirstFloorList.json")
//    @ResponseBody
//    public JSONObject getGdsTypeFirstFloorList(String goodsType){
//        return gdsBrandTypeService.getGdsTypeFirstFloor(goodsType);
//    }
//
    @RequestMapping("resort.json")
    @ResponseBody
    public JSONObject resort(@RequestParam("data") String data){

        return gdsBrandTypeService.updateSort(data);
    }

}
