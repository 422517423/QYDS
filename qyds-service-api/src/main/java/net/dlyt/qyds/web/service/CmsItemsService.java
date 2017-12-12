package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.CmsItems;
import net.dlyt.qyds.common.dto.GdsMasterExt;
import net.dlyt.qyds.common.dto.ext.CmsItemsExt;

import java.util.List;

/**
 * Created by C_Nagai on 2016/7/28.
 */
public interface CmsItemsService {

    List<CmsItemsExt> selectAll();

    int insertSelective(CmsItems record);

    int updateByPrimaryKeySelective(CmsItems record);

    CmsItems selectByPrimaryKey(String itemId);

    void updateSort(List<CmsItems> list);

    int delete(CmsItems record);

    JSONObject selectByItemCode(String itemCode);

    JSONObject getMasterByItemList(String data);

    JSONObject getListByItem(String data);

    JSONObject getMasterByItemArray(String data);

    JSONObject getContentHtml(String data);

    JSONObject getContentHtmlByCmsId(String data);

    JSONObject getContentHtmlByItemCode(String data);

    //获取品牌系列的一级二级三级节点
    JSONObject getGoodsTypeIndex(String data);

    List<GdsMasterExt> resetGoodsList(List<GdsMasterExt> list, String memberId);

    //获取杂志背景故事
    JSONObject getTheStory(String data);

    // 根据商品分类获取新品商品
    JSONObject getNewGoods(String data);

    JSONObject getMetaData(String data);

    JSONObject getDeliverData(String data);
}
