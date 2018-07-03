package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsType;
import net.dlyt.qyds.common.dto.GdsTypeDetail;
import net.dlyt.qyds.common.dto.SysMenu;

import java.util.List;

/**
 * Created by congkeyan on 16/7/22.
 */
public interface GdsTypeService {

    //获取分类列表
    JSONObject getTreeList(String type);

    // 保存商品分类主表 分类详细
    JSONObject save(GdsType gdsType, GdsTypeDetail gdsTypeDetail);

    // 保存商品分类主表 分类详细
    JSONObject edit(String goodsTypeId);

    //删除分类节点
    JSONObject delete(String goodsTypeId);

    //上移下移
    JSONObject updateSort(String data);


    /***********************API**************************************/

    //获取分类的第一层节点
    JSONObject getGdsTypeFirstFloor(String data);

    //获取分类的第一层和第二层节点
    JSONObject getGdsTypeFirstAndSecondFloor(String data);

    //获取第三层数据的数据和图片
    JSONObject getGdsTypeSubFloor(String data);


    //获取所有CMS配置商品分类
    JSONObject getGdsTypeFloor(String data);

    //品牌系列的第一层数据获取接口
    JSONObject getGdsBrandType();

    //品牌系列的第一层数据获取接口
    JSONObject getGdsTypeFloorByPhone(String data);

    //获取分类的第一层和第二层节点
    JSONObject getGdsBrandTypeFirstAndSecondFloor();

    //获取第三层数据的数据和图片
    JSONObject getGdsBrandTypeSubFloor(String data);

    //获取品牌系列的一级二级三级节点
    JSONObject getGdsBrandTypeFloor();

    //获取分类的一级二级三级节点
    JSONObject getAllTypes();


}
