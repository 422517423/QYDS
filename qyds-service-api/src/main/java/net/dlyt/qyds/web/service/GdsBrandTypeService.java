package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsBrandType;
import net.dlyt.qyds.common.dto.GdsBrandTypeDetail;

/**
 * Created by congkeyan on 16/7/22.
 */
public interface GdsBrandTypeService {

    //获取分类列表
    JSONObject getTreeList();

    // 保存商品分类主表 分类详细
    JSONObject save(GdsBrandType gdsType, GdsBrandTypeDetail gdsTypeDetail);

    // 保存商品分类主表 分类详细
    JSONObject edit(String goodsTypeId);

    //删除分类节点
    JSONObject delete(String goodsTypeId);

    //上移下移
    JSONObject updateSort(String data);


    /***********************API**************************************/
//
//    //获取分类的第一层节点
//    JSONObject getGdsTypeFirstFloor(String data);
//
//    //获取分类的第一层和第二层节点
//    JSONObject getGdsTypeFirstAndSecondFloor(String data);
//
//    //获取第三层数据的数据和图片
//    JSONObject getGdsTypeSubFloor(String data);
//
//
//    //获取所有CMS配置商品分类
//    JSONObject getGdsTypeFloor(String data);
}
