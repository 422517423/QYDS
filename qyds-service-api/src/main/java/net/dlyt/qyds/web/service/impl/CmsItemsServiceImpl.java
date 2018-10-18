package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.CmsItemsExt;
import net.dlyt.qyds.common.dto.ext.CmsMasterExt;
import net.dlyt.qyds.common.form.ActMasterForm;
import net.dlyt.qyds.dao.*;
import net.dlyt.qyds.dao.ext.*;
import net.dlyt.qyds.web.service.ActMasterService;
import net.dlyt.qyds.web.service.CmsItemsService;
import net.dlyt.qyds.web.service.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by C_Nagai on 2016/7/28.
 */
@Service("cmsItemsService")
@Transactional(readOnly = true)
public class CmsItemsServiceImpl implements CmsItemsService {

    @Autowired
    private CmsItemsMapperExt cmsItemsMapperExt;

    @Autowired
    private CmsItemsMapper cmsItemsMapper;

    @Autowired
    private CmsMasterMapperExt cmsMasterMapperExt;

    @Autowired
    private GdsMasterMapper gdsMasterMapper;

    @Autowired
    private GdsMasterMapperExt gdsMasterMapperExt;

    @Autowired
    private CmsMasterGoodsMapperExt cmsMasterGoodsMapperExt;

    @Autowired
    private GdsTypeMapperExt gdsTypeMapperExt;

    @Autowired
    private ActMasterMapperExt actMasterMapperExt;

    @Autowired
    private CmsMasterMapper cmsMasterMapper;

    @Autowired
    private ActMasterService actMasterService;

    @Autowired
    private GdsColorMapperExt gdsColorMapperExt;

    public List<CmsItemsExt> selectAll() {
        return cmsItemsMapperExt.selectAll();
    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public int insertSelective(CmsItems record){
        Short sort = cmsItemsMapperExt.selectMaxSrot(record);
        if (sort == null) {
            record.setSort(Short.valueOf("1"));
        } else {
            record.setSort( ++sort);
        }

        return cmsItemsMapper.insertSelective(record);
    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public int updateByPrimaryKeySelective(CmsItems record){
        return cmsItemsMapper.updateByPrimaryKeySelective(record);
    }

    public CmsItems selectByPrimaryKey(String itemId){
        return cmsItemsMapper.selectByPrimaryKey(itemId);
    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public void updateSort(List<CmsItems> list) {
        for (CmsItems cmsItems : list) {
            cmsItemsMapper.updateByPrimaryKeySelective(cmsItems);
        }
    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public int delete(CmsItems record) {
        return cmsItemsMapperExt.delete(record);
    }

    public JSONObject selectByItemCode(String itemCode){
        JSONObject json = new JSONObject();
        try{
            CmsItems cmsItems = cmsItemsMapperExt.selectByItemCode(itemCode);
            json.put("data", cmsItems);
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode",Constants.FAIL);
        }
        return json;
    }

    /**
     *
     * @param data
     * @return
     */
    public JSONObject getMasterByItemList(String data) {
        // isChild 1：获取子栏目 2：获取自己栏目
        JSONObject json = new JSONObject();
        try{
            CmsItemsExt cmsItems = JSON.parseObject(data, CmsItemsExt.class);
            List<CmsItemsExt> list = new ArrayList<CmsItemsExt>();
//            List<CmsMasterExt> cmList = new ArrayList<CmsMasterExt>();
            // 获取子栏目
            if ("1".equals(cmsItems.getIsChild())) {
                list = cmsItemsMapperExt.getChildItemList(cmsItems.getItemCode());
                for (CmsItemsExt cmsItemsExt : list) {
                    childItem(cmsItemsExt, cmsItems);
                }


                json.put("results", list);
            } else {
                List<CmsMasterExt> cmList = cmsMasterMapperExt.selectCmsMasterByItemCode(cmsItems.getItemCode());
                formatList(cmList, cmsItems);
                json.put("results", cmList);


                //广告和轮播图处理
                if("index_figure".equals(cmsItems.getItemCode())
                        || "index_new".equals(cmsItems.getItemCode()) ){
                    if(cmList != null && cmList.size() > 0){
                        for(int i=0;i<cmList.size();i++){
                            String value = "";
                            List<GdsMasterExt>  list1 = cmList.get(i).getGdsMasterExtList();
                            if(list1 != null){
                                for(int j=0;j<list1.size(); j++){
                                    value = value + ',' + list1.get(j).getGoodsId();
                                }

                                if(value.startsWith(",")){
                                    value = value.substring(1,value.length());
                                }
                                if(value.endsWith(",")){
                                    value = value.substring(0,value.length()-1);
                                }
                                cmList.get(i).setItemTypeVal(value);
                            }
                        }
                    }
                }

//                if(cmList != null && cmList.size() > 0){
//                    String value = "";
//                    for(int i=0;i<cmList.size();i++){
//                        if(i == 0){
//                            value = cmList.get(i).getItemTypeVal();
//                        }else{
//                            value = value + ',' + cmList.get(i).getItemTypeVal();
//                        }
//                        cmList.get(i).setItemTypeVal(value);
//                    }
//
//                    for(int i=0;i<cmList.size();i++){
//                       cmList.get(i).setItemTypeVal(value);
//                    }
//                }


            }

            json.put("resultCode", Constants.NORMAL);
            json.put("resultMessage", Constants.SUCCESS_MSG);
        }catch(Exception e){
            json.put("resultCode",Constants.FAIL);
        }
        return json;

    }

    /**
     * cms管理维护数据取得
     * 已栏目为元素的列表
     * @param data
     * @return
     */
    @Cacheable(value="${cms_items_catch_name}", key="#data")
    public JSONObject getListByItem(String data) {
        // isChild 1：获取子栏目 2：获取自己栏目
        JSONObject json = new JSONObject();
        try{
            CmsItemsExt cmsItems = JSON.parseObject(data, CmsItemsExt.class);
            List<CmsItemsExt> list = new ArrayList<CmsItemsExt>();
            // 获取子栏目
            if ("1".equals(cmsItems.getIsChild())) {
                list = cmsItemsMapperExt.getChildItemList(cmsItems.getItemCode());
                for (CmsItemsExt cmsItemsExt : list) {
                    childItem(cmsItemsExt, cmsItems);
                }
                json.put("results", list);
            } else {
                CmsItems cmsItemsObj = cmsItemsMapperExt.selectByItemCode(cmsItems.getItemCode());
                if (cmsItemsObj != null) {
                    CmsItemsExt cmsItemsExt = new CmsItemsExt();
                    cmsItemsExt.setItemId(cmsItemsObj.getItemId());
                    cmsItemsExt.setItemCode(cmsItemsObj.getItemCode());
                    cmsItemsExt.setItemName(cmsItemsObj.getItemName());
                    List<CmsMasterExt> cmList = cmsMasterMapperExt.selectCmsMasterByItemCode(cmsItems.getItemCode());
                    formatList(cmList, cmsItems);
                    cmsItemsExt.setCmList(cmList);
                    list.add(cmsItemsExt);
                    json.put("results", list);
                }
            }

            json.put("resultCode", Constants.NORMAL);
            json.put("resultMessage", Constants.SUCCESS_MSG);
        }catch(Exception e){
            json.put("resultCode",Constants.FAIL);
        }
        return json;

    }

    /**
     * cms管理维护数据取得，根据多code获取数据
     * @param data
     * @return
     */
    public JSONObject getMasterByItemArray(String data) {
        // isChild 1：获取子栏目 2：获取自己栏目
        JSONObject json = new JSONObject();
        try{
            CmsItemsExt cmsItems = JSON.parseObject(data, CmsItemsExt.class);
            String itemCode = cmsItems.getItemCode();
            if (itemCode != null && !"".equals(itemCode)) {
                List<CmsMasterExt> list = new ArrayList<CmsMasterExt>();
                String[] itemCodeArray = itemCode.split(",");
                if (itemCodeArray != null && itemCodeArray.length > 0) {
                    for (String code : itemCodeArray) {
                        if (code != null && !"".equals(code)) {
                            List<CmsMasterExt> cmList = cmsMasterMapperExt.selectCmsMasterByItemCode(code);
                            formatList(cmList, cmsItems);
                            list.addAll(cmList);
                        }
                    }

                    json.put("results", list);
                }
            }
            json.put("resultCode", Constants.NORMAL);
            json.put("resultMessage", Constants.SUCCESS_MSG);
        }catch(Exception e){
            json.put("resultCode",Constants.FAIL);
        }
        return json;

    }

    /**
     * 递归获取子栏目信息
     * @param cmsItemsExt
     */
    private void childItem(CmsItemsExt cmsItemsExt, CmsItemsExt cmsItems) {
        List<CmsItemsExt> list = cmsItemsMapperExt.getChildItemList(cmsItemsExt.getItemCode());
        if (list != null && list.size() > 0) {
            cmsItemsExt.setCiList(list);
            for (CmsItemsExt itemsExt : list) {
                childItem(itemsExt, cmsItems);
            }
        } else {
            List<CmsMasterExt> cmList = cmsMasterMapperExt.selectCmsMasterByItemCode(cmsItemsExt.getItemCode());
            formatList(cmList, cmsItems);
            cmsItemsExt.setCmList(cmList);
        }
    }

    /**
     * 格式互获取的cms管理数据
     * @param cmList
     */
    private void formatList(List<CmsMasterExt> cmList, CmsItemsExt cmsItems) {
        if (cmList != null && cmList.size() > 0) {
            CmsMasterExt cmsMasterExt = null;
            for (int y = 0; y < cmList.size(); y ++) {
                cmsMasterExt = cmList.get(y);
                String listJson = cmsMasterExt.getListJson();
                JSONObject jsonObj = JSONObject.parseObject(listJson);
                cmsMasterExt.setImageUrl((String)jsonObj.get("imageUrl"));
                cmsMasterExt.setImageLink((String)jsonObj.get("imageLink"));
                cmsMasterExt.setItemTypeVal((String)jsonObj.get("value"));

                // 商品分类
                if (Constants.CMS_MASTER_ADS.equals(cmsMasterExt.getItemType())) {
                    String[] gdsTypeArray = cmsMasterExt.getItemTypeVal().split("_");
                    GdsTypeExt gdsType = gdsTypeMapperExt.getGdsTypeById(gdsTypeArray[gdsTypeArray.length - 1]);
                    cmsMasterExt.setGdsType(gdsType);
                // 活动
                } else if (Constants.CMS_MASTER_GDS.equals(cmsMasterExt.getItemType()) || Constants.CMS_MASTER_ACT.equals(cmsMasterExt.getItemType())) {
                    // 获取商品列表
                    if ("index_figure".equals(cmsItems.getItemCode()) || "index_new".equals(cmsItems.getItemCode())
                            || "index_new_1".equals(cmsItems.getItemCode())) {
                        continue;
                    } else {
                        List<CmsMasterGoods> goodsList = cmsMasterGoodsMapperExt.selectGoodsByCmsId(cmsMasterExt.getCmsId());
                        if (goodsList != null && goodsList.size() > 0) {
                            cmsMasterExt.setActId(goodsList.get(0).getActId());
                            // 多商品的时候
                            List<GdsMasterExt> gdsList = new ArrayList<GdsMasterExt>();
                            for (CmsMasterGoods cmsMasterGoods : goodsList) {
                                GdsMaster gdsMaster = gdsMasterMapper.selectByPrimaryKey(cmsMasterGoods.getGoodsId());
                                List<String> activity_type = new ArrayList<String>();
                                if (Constants.GDS_SUIT.equals(gdsMaster.getType())) {
                                    GdsMasterExt gdsMasterExt = gdsMasterMapperExt.selectSuitMinAndMaxPrice(gdsMaster);
                                    if (gdsMasterExt != null) {
                                        String[] imageUrlArray = gdsMasterExt.getImageUrlJson().split(",");
                                        gdsMasterExt.setImageUrlJson(imageUrlArray[0]);
//                                        // 商品的时候
//                                        if (Constants.CMS_MASTER_GDS.equals(cmsMasterExt.getItemType())) {
//                                            List<HashMap> activityList = gdsMasterMapperExt.selectActivityItemsForSuit(gdsMasterExt.getGoodsId());
//                                            //标签
//                                            for(int i = 0; i<activityList.size(); i++){
//                                                activity_type.add((String)activityList.get(i).get("actition_type"));
//                                            }
//
//                                            // 活动的时候
//                                        } else {
//                                            ActMasterForm actMaster = actMasterMapperExt.selectByPrimaryKey(cmsMasterExt.getActId());
//                                            activity_type.add(actMaster.getActivityType());
//                                        }
//                                        gdsMasterExt.setActivityTags(activity_type);
                                        cmsMasterExt.setGdsMasterExt(gdsMasterExt);
                                        gdsList.add(gdsMasterExt);
                                    }
                                } else {
                                    GdsMasterExt gdsMasterExt = gdsMasterMapperExt.selectGoodsMinAndMaxPrice(gdsMaster);
                                    if (gdsMasterExt != null) {
                                        String[] imageUrlArray = gdsMasterExt.getImageUrlJson().split(",");
                                        gdsMasterExt.setImageUrlJson(imageUrlArray[0]);
//                                        // 商品的时候
//                                        if (Constants.CMS_MASTER_GDS.equals(cmsMasterExt.getItemType())) {
//                                            List<HashMap> activityList = gdsMasterMapperExt.selectActivityItems(gdsMasterExt.getGoodsId());
//                                            //标签
//                                            for(int i = 0; i<activityList.size(); i++){
//                                                activity_type.add((String)activityList.get(i).get("actition_type"));
//                                            }
//
//                                            // 活动的时候
//                                        } else {
//                                            ActMasterForm actMaster = actMasterMapperExt.selectByPrimaryKey(cmsMasterExt.getActId());
//                                            if (actMaster != null) {
//                                                activity_type.add(actMaster.getActivityType());
//                                            }
//                                        }
//                                        gdsMasterExt.setActivityTags(activity_type);

//                                        List<HashMap> activityIdsList = null;
//                                        if (Constants.GDS_SUIT.equals(gdsMasterExt.getType())) {
//                                            activityIdsList = gdsMasterMapperExt.selectActivityIdsForSuit(gdsMasterExt.getGoodsId());
//                                        }else{
//                                            activityIdsList = gdsMasterMapperExt.selectActivityIds(gdsMasterExt.getGoodsId());
//                                        }
//
//                                        //组成获取优惠价格的form
//                                        List<ActMasterForm> activityFormList = new ArrayList<ActMasterForm>();
//
//                                        //获取最小优惠
//                                        for (int i = 0; i < activityIdsList.size(); i++) {
//                                            ActMasterForm form = new ActMasterForm();
//                                            form.setActivityId((String) activityIdsList.get(i).get("activity_id"));
//                                            form.setOriginPrice(Float.parseFloat(gdsMasterExt.getMinPrice()));
//                                            activityFormList.add(form);
//                                        }
//                                        JSONObject jsonObjectMin = actMasterService.getNewPricesByActivity(activityFormList, gdsMasterExt.getMemberId(), gdsMasterExt.getGoodsId());
//                                        //最小的优惠价钱
//                                        float minNewPrice = 0;
//                                        List<ActMasterForm> minList = (ArrayList<ActMasterForm>) jsonObjectMin.get("data");
//                                        if (minList != null && minList.size() != 0) {
//                                            minNewPrice = minList.get(0).getNewPrice();
//                                            gdsMasterExt.setActivityName(minList.get(0).getActivityName());
//                                        }
//
//                                        //获取最大优惠
//                                        activityFormList.clear();
//                                        for (int i = 0; i < activityIdsList.size(); i++) {
//                                            ActMasterForm form = new ActMasterForm();
//                                            form.setActivityId((String) activityIdsList.get(i).get("activity_id"));
//                                            form.setOriginPrice(Float.parseFloat(gdsMasterExt.getMaxPrice()));
//                                            activityFormList.add(form);
//                                        }
//                                        JSONObject jsonObjectMax = actMasterService.getNewPricesByActivity(activityFormList, gdsMasterExt.getMemberId(), gdsMasterExt.getGoodsId());
//                                        //最大的优惠价钱
//                                        float maxNewPrice = 0;
//                                        List<ActMasterForm> maxList = (ArrayList<ActMasterForm>) jsonObjectMax.get("data");
//                                        if (maxList != null && maxList.size() != 0) {
//                                            maxNewPrice = maxList.get(0).getNewPrice();
//                                            gdsMasterExt.setActivityName(maxList.get(0).getActivityName());
//                                        }
//                                        //返回给前台的优惠信息情况
//                                        if (minNewPrice == 0 || maxNewPrice == 0) {
//                                            gdsMasterExt.setMinAndMaxPriceActivity(null);
//                                        } else if (minNewPrice == maxNewPrice) {
//                                            gdsMasterExt.setMinAndMaxPriceActivity("¥" + minNewPrice);
//                                        } else {
//                                            gdsMasterExt.setMinAndMaxPriceActivity("¥" + minNewPrice + "~" + "¥" + maxNewPrice);
//                                        }
//                                        //返回给前台的非优惠信息情况
//                                        if (gdsMasterExt.getMinPrice() != null && gdsMasterExt.getMaxPrice() != null && gdsMasterExt.getMinPrice().equals(gdsMasterExt.getMaxPrice())) {
//                                            gdsMasterExt.setMinAndMaxPrice("¥" + gdsMasterExt.getMinPrice());
//                                        }
//                                        if (gdsMasterExt.getMinPrice() == null) {
//                                            gdsMasterExt.setMinAndMaxPrice("¥0.00");
//                                        }


                                        cmsMasterExt.setGdsMasterExt(gdsMasterExt);
                                        gdsList.add(gdsMasterExt);
                                    }
                                }
                            }
                            cmsMasterExt.setGdsMasterExtList(gdsList);
                        }
//                        else {
//                            if (!"index_figure".equals(cmsItems.getItemCode())) {
//                                cmList.remove(y);
//                                y--;
//                                continue;
//                            }
//                        }
                    }

                }
            }
        }

    }

    /**
     *
     * @param data
     * @return
     */
    public JSONObject getContentHtml(String data) {
        // isChild 1：获取子栏目 2：获取自己栏目
        JSONObject json = new JSONObject();
        try{
            CmsItemsExt cmsItems = JSON.parseObject(data, CmsItemsExt.class);
            List<CmsMasterExt> cmList = cmsMasterMapperExt.selectCmsMasterByItemCode(cmsItems.getItemCode());
            CmsMasterExt cmsMasterExt = null;
            if (cmList != null && cmList.size() > 0) {
                cmsMasterExt = cmList.get(0);
            }
            json.put("results", cmsMasterExt);
            json.put("resultCode", Constants.SUCCESS);
            json.put("resultMessage", Constants.SUCCESS_MSG);
        }catch(Exception e){
            json.put("resultCode",Constants.FAIL);
        }
        return json;

    }

    /**
     *
     * @param data
     * @return
     */
    public JSONObject getContentHtmlByCmsId(String data) {
        // isChild 1：获取子栏目 2：获取自己栏目
        JSONObject json = new JSONObject();
        try{
            CmsMaster cmsMaster = JSON.parseObject(data, CmsMaster.class);
            CmsMaster cm = cmsMasterMapper.selectByPrimaryKey(cmsMaster.getCmsId());
            List<CmsMaster> list = new ArrayList<CmsMaster>();
//            list.add(cm);
//            json.put("results", list);
            json.put("results", cm);
            json.put("resultCode", Constants.NORMAL);
            json.put("resultMessage", Constants.SUCCESS_MSG);
        }catch(Exception e){
            json.put("resultCode",Constants.FAIL);
        }
        return json;

    }

    /**
     * 根据模块code获取图文区域。
     * 如果有多个取第一个
     * @param data
     * @return
     */
    public JSONObject getContentHtmlByItemCode(String data) {
        JSONObject json = new JSONObject();
        try{
            CmsItems cmsItems = JSON.parseObject(data, CmsItems.class);
//            CmsMaster cm = cmsMasterMapper.selectByPrimaryKey(cmsItems.getItemCode());
            List<CmsMasterExt> cmList = cmsMasterMapperExt.selectCmsMasterByItemCode(cmsItems.getItemCode());
            if (cmList != null && cmList.size() > 0) {
                json.put("results", cmList.get(0));
            }
//            List<CmsMaster> list = new ArrayList<CmsMaster>();
//            list.add(cm);
//            json.put("results", list);

            json.put("resultCode", Constants.NORMAL);
            json.put("resultMessage", Constants.SUCCESS_MSG);
        }catch(Exception e){
            json.put("resultCode",Constants.FAIL);
        }
        return json;

    }

    /**
     * 根据商品分类获取二级主页面
     * @param data
     * @return
     */

    @Cacheable(value="${goods_second_main_catch_name}", key="#data")
    public JSONObject getGoodsTypeIndex(String data) {
        JSONObject json = new JSONObject();
        try{
            //转换数据格式
            GdsMasterExt gdsMasterObj = JSON.parseObject(data, GdsMasterExt.class);
            List<CmsMasterExt> newList = new ArrayList<CmsMasterExt>();
            // 存放有商品分类页面显示商品
            List<GdsMasterExt> goodsList = new ArrayList<GdsMasterExt>();
            // 存放商品
            List<GdsMaster> gdsMasterList = new ArrayList<GdsMaster>();

            // 存放有商品分类返回广告
            List<CmsMasterExt> goodsTypeNewList = new ArrayList<CmsMasterExt>();
            // 存放商品
            List<GdsMaster> goodsTypegdsMasterList = new ArrayList<GdsMaster>();

            // 存放有品牌返回广告
            List<CmsMasterExt> brandNewList = new ArrayList<CmsMasterExt>();
            // 存放品牌商品
            List<GdsMaster> brandGdsMasterList = new ArrayList<GdsMaster>();
            List<CmsMasterExt> cmList = cmsMasterMapperExt.getGoodsTypeIndex("goods_type_index");
            if (cmList != null && cmList.size() > 0) {
                for (CmsMasterExt cmExt : cmList) {
                    if (cmExt == null) {
                        continue;
                    }
                    String listJson = cmExt.getListJson();
                    JSONObject jsonObj = JSONObject.parseObject(listJson);
                    cmExt.setImageUrl((String) jsonObj.get("imageUrl"));
                    cmExt.setImageLink((String) jsonObj.get("imageLink"));
                    cmExt.setItemTypeVal((String) jsonObj.get("value"));
                    String text = (String) jsonObj.get("text");
                    if (text != null && !"".equals(text)) {
                        if (cmExt.getItemTypeVal().equals(gdsMasterObj.getGoodsTypeId())) {
                            if (cmExt.getImageUrl() == null || "".equals(cmExt.getImageUrl())) {
                                List<CmsMasterGoods> cmsGoodsList = cmsMasterGoodsMapperExt.selectGoodsByCmsId(cmExt.getCmsId());
                                for (CmsMasterGoods cmsMasterGoods : cmsGoodsList) {
                                    GdsMaster gdsMaster = gdsMasterMapper.selectByPrimaryKey(cmsMasterGoods.getGoodsId());
                                    goodsTypegdsMasterList.add(gdsMaster);
//                                    List<String> activity_type = new ArrayList<String>();
                                }
                            } else {
                                goodsTypeNewList.add(cmExt);
                            }
                        }
                        // 品牌系列的时候
                    } else {
                        if (cmExt.getImageUrl() == null || "".equals(cmExt.getImageUrl())) {
                            String goodsIds = cmExt.getItemTypeVal();
                            String[] goodsIdArray = goodsIds.split(",");
                            for (String goodsId : goodsIdArray) {
                                if (goodsId != null && !"".equals(goodsId)) {
                                    GdsMaster gdsMaster = gdsMasterMapper.selectByPrimaryKey(goodsId);
                                    brandGdsMasterList.add(gdsMaster);
                                }
                            }
                        } else {
                            brandNewList.add(cmExt);
                        }
                    }
                }

                if (goodsTypeNewList.size() > 0 || gdsMasterList.size() > 0) {
                    newList.addAll(goodsTypeNewList);
                    gdsMasterList.addAll(goodsTypegdsMasterList);

                } else {
                    newList.addAll(brandNewList);
                    gdsMasterList.addAll(brandGdsMasterList);
                }
//                for (GdsMaster gdsMaster : gdsMasterList) {
//                    if (Constants.GDS_SUIT.equals(gdsMaster.getType())) {
//                        GdsMasterExt gdsMasterExt = gdsMasterMapperExt.selectSuitMinAndMaxPrice(gdsMaster);
//                        if (gdsMasterExt != null) {
//                            String[] imageUrlArray = gdsMasterExt.getImageUrlJson().split(",");
//                            gdsMasterExt.setImageUrlJson(imageUrlArray[0]);
//                            //                                    cmExt.setGdsMasterExt(gdsMasterExt);
//                            goodsList.add(gdsMasterExt);
//                        }
//                    } else {
//                        GdsMasterExt gdsMasterExt = gdsMasterMapperExt.selectGoodsMinAndMaxPrice(gdsMaster);
//                        if (gdsMasterExt != null) {
//                            String[] imageUrlArray = gdsMasterExt.getImageUrlJson().split(",");
//                            gdsMasterExt.setImageUrlJson(imageUrlArray[0]);
//                            //                                    cmExt.setGdsMasterExt(gdsMasterExt);
//
//                            List<HashMap> activityIdsList = null;
//                            if (Constants.GDS_SUIT.equals(gdsMasterExt.getType())) {
//                                activityIdsList = gdsMasterMapperExt.selectActivityIdsForSuit(gdsMasterExt.getGoodsId());
//                            }else{
//                                activityIdsList = gdsMasterMapperExt.selectActivityIds(gdsMasterExt.getGoodsId());
//                            }
//
//                            //组成获取优惠价格的form
//                            List<ActMasterForm> activityFormList = new ArrayList<ActMasterForm>();
//
//                            //获取最小优惠
//                            for (int i = 0; i < activityIdsList.size(); i++) {
//                                ActMasterForm form = new ActMasterForm();
//                                form.setActivityId((String) activityIdsList.get(i).get("activity_id"));
//                                form.setOriginPrice(Float.parseFloat(gdsMasterExt.getMinPrice()));
//                                activityFormList.add(form);
//                            }
//                            JSONObject jsonObjectMin = actMasterService.getNewPricesByActivity(activityFormList, gdsMasterExt.getMemberId(), gdsMasterExt.getGoodsId());
//                            //最小的优惠价钱
//                            float minNewPrice = 0;
//                            List<ActMasterForm> minList = (ArrayList<ActMasterForm>) jsonObjectMin.get("data");
//                            if (minList != null && minList.size() != 0) {
//                                minNewPrice = minList.get(0).getNewPrice();
//                                gdsMasterExt.setActivityName(minList.get(0).getActivityName());
//                            }
//
//                            //获取最大优惠
//                            activityFormList.clear();
//                            for (int i = 0; i < activityIdsList.size(); i++) {
//                                ActMasterForm form = new ActMasterForm();
//                                form.setActivityId((String) activityIdsList.get(i).get("activity_id"));
//                                form.setOriginPrice(Float.parseFloat(gdsMasterExt.getMaxPrice()));
//                                activityFormList.add(form);
//                            }
//                            JSONObject jsonObjectMax = actMasterService.getNewPricesByActivity(activityFormList, gdsMasterExt.getMemberId(), gdsMasterExt.getGoodsId());
//                            //最大的优惠价钱
//                            float maxNewPrice = 0;
//                            List<ActMasterForm> maxList = (ArrayList<ActMasterForm>) jsonObjectMax.get("data");
//                            if (maxList != null && maxList.size() != 0) {
//                                maxNewPrice = maxList.get(0).getNewPrice();
//                                gdsMasterExt.setActivityName(maxList.get(0).getActivityName());
//                            }
//                            //返回给前台的优惠信息情况
//                            if (minNewPrice == 0 || maxNewPrice == 0) {
//                                gdsMasterExt.setMinAndMaxPriceActivity(null);
//                            } else if (minNewPrice == maxNewPrice) {
//                                gdsMasterExt.setMinAndMaxPriceActivity("¥" + minNewPrice);
//                            } else {
//                                gdsMasterExt.setMinAndMaxPriceActivity("¥" + minNewPrice + "~" + "¥" + maxNewPrice);
//                            }
//                            //返回给前台的非优惠信息情况
//                            if (gdsMasterExt.getMinPrice() != null && gdsMasterExt.getMaxPrice() != null && gdsMasterExt.getMinPrice().equals(gdsMasterExt.getMaxPrice())) {
//                                gdsMasterExt.setMinAndMaxPrice("¥" + gdsMasterExt.getMinPrice());
//                            }
//                            if (gdsMasterExt.getMinPrice() == null) {
//                                gdsMasterExt.setMinAndMaxPrice("¥0.00");
//                            }
//
//                            goodsList.add(gdsMasterExt);
//                        }
//                    }
//                }
            }
            json.put("newList", newList);
            json.put("goodsList", gdsMasterList);
            json.put("resultCode", Constants.NORMAL);
            json.put("resultMessage", Constants.SUCCESS_MSG);
        }catch(Exception e){
            json.put("resultCode",Constants.FAIL);
        }
        return json;
    }

    public List<GdsMasterExt> resetGoodsList(List<GdsMasterExt> gdsMasterList,String memberId){

        // 存放有商品分类页面显示商品
        List<GdsMasterExt> goodsList = new ArrayList<GdsMasterExt>();

        //遍历获取价格信息,活动标签
        for (GdsMaster gdsMaster : gdsMasterList) {
            if (Constants.GDS_SUIT.equals(gdsMaster.getType())) {
                GdsMasterExt gdsMasterExt = gdsMasterMapperExt.selectSuitMinAndMaxPrice(gdsMaster);
                if (gdsMasterExt != null) {
                    String[] imageUrlArray = gdsMasterExt.getImageUrlJson().split(",");
                    gdsMasterExt.setImageUrlJson(imageUrlArray[0]);
                    //                                    cmExt.setGdsMasterExt(gdsMasterExt);
                    goodsList.add(gdsMasterExt);
                }
            } else {
                GdsMasterExt gdsMasterExt = gdsMasterMapperExt.selectGoodsMinAndMaxPrice(gdsMaster);
                if (gdsMasterExt != null) {
                    String[] imageUrlArray = gdsMasterExt.getImageUrlJson().split(",");
                    gdsMasterExt.setImageUrlJson(imageUrlArray[0]);
                    //                                    cmExt.setGdsMasterExt(gdsMasterExt);

                    List<HashMap> activityIdsList = null;
                    if (Constants.GDS_SUIT.equals(gdsMasterExt.getType())) {
                        activityIdsList = gdsMasterMapperExt.selectActivityIdsForSuit(gdsMasterExt.getGoodsId());
                    }else{
                        activityIdsList = gdsMasterMapperExt.selectActivityIds(gdsMasterExt.getGoodsId());
                    }

                    //组成获取优惠价格的form
                    List<ActMasterForm> activityFormList = new ArrayList<ActMasterForm>();

                    //获取最小优惠
                    for (int i = 0; i < activityIdsList.size(); i++) {
                        ActMasterForm form = new ActMasterForm();
                        form.setActivityId((String) activityIdsList.get(i).get("activity_id"));
                        form.setOriginPrice(Float.parseFloat(gdsMasterExt.getMinPrice()));
                        activityFormList.add(form);
                    }
                    JSONObject jsonObjectMin = actMasterService.getNewPricesByActivity(activityFormList, memberId, gdsMasterExt.getGoodsId());
                    //最小的优惠价钱
                    float minNewPrice = 0;
                    List<ActMasterForm> minList = (ArrayList<ActMasterForm>) jsonObjectMin.get("data");
                    if (minList != null && minList.size() != 0) {
                        minNewPrice = minList.get(0).getNewPrice();
                        gdsMasterExt.setActivityName(minList.get(0).getActivityName());
                    }

                    //获取最大优惠
                    activityFormList.clear();
                    for (int i = 0; i < activityIdsList.size(); i++) {
                        ActMasterForm form = new ActMasterForm();
                        form.setActivityId((String) activityIdsList.get(i).get("activity_id"));
                        form.setOriginPrice(Float.parseFloat(gdsMasterExt.getMaxPrice()));
                        activityFormList.add(form);
                    }
                    JSONObject jsonObjectMax = actMasterService.getNewPricesByActivity(activityFormList, memberId, gdsMasterExt.getGoodsId());
                    //最大的优惠价钱
                    float maxNewPrice = 0;
                    List<ActMasterForm> maxList = (ArrayList<ActMasterForm>) jsonObjectMax.get("data");
                    if (maxList != null && maxList.size() != 0) {
                        maxNewPrice = maxList.get(0).getNewPrice();
                        gdsMasterExt.setActivityName(maxList.get(0).getActivityName());
                    }
                    //返回给前台的优惠信息情况
                    if (minNewPrice == 0 || maxNewPrice == 0) {
                        gdsMasterExt.setMinAndMaxPriceActivity(null);
                    } else if (minNewPrice == maxNewPrice) {
                        gdsMasterExt.setMinAndMaxPriceActivity("¥" + minNewPrice);
                    } else {
                        gdsMasterExt.setMinAndMaxPriceActivity("¥" + minNewPrice + "~" + "¥" + maxNewPrice);
                    }
                    //返回给前台的非优惠信息情况
                    if (gdsMasterExt.getMinPrice() != null && gdsMasterExt.getMaxPrice() != null && gdsMasterExt.getMinPrice().equals(gdsMasterExt.getMaxPrice())) {
                        gdsMasterExt.setMinAndMaxPrice("¥" + gdsMasterExt.getMinPrice());
                    }
                    if (gdsMasterExt.getMinPrice() == null) {
                        gdsMasterExt.setMinAndMaxPrice("¥0.00");
                    }

                    goodsList.add(gdsMasterExt);
                }
            }
        }
        for (GdsMasterExt gdsMasterExt1: goodsList) {
            List<GdsColoreimage> gdsColoreimages = gdsColorMapperExt.selectColorList(gdsMasterExt1.getGoodsId());
            gdsMasterExt1.setGdsColoreimageList(gdsColoreimages);
        }
        return goodsList;
    }

    /**
     * 获取杂志背景故事
     * @param data
     * @return
     */
    public JSONObject getTheStory(String data) {
        JSONObject json = new JSONObject();
        try{
            //转换数据格式
            CmsMaster cmsMasterObj = JSON.parseObject(data, CmsMaster.class);
            CmsMaster cmsMaster = cmsMasterMapper.selectByPrimaryKey(cmsMasterObj.getCmsId());
            CmsMasterExt cmsMasterExt = new CmsMasterExt();
            cmsMasterExt.setTitle(cmsMaster.getTitle());
            cmsMasterExt.setTextComment(cmsMaster.getTextComment());
            cmsMasterExt.setInsertTime(cmsMaster.getInsertTime());
            String listJson = cmsMaster.getListJson();
            JSONObject jsonObj = JSON.parseObject(listJson);
            cmsMasterExt.setImageUrl((String)jsonObj.get("imageUrl"));
            String contentHtml = cmsMaster.getContentHtml();
            String[] pArray = contentHtml.split("</p>");
            String video = "";
            String html = "";
            for (String p : pArray) {
                if (p != null && p.indexOf("</video>") > 0) {
                    if ("".equals(video)) {
                        int videoClassNo = p.indexOf("class=\"");
                        videoClassNo = videoClassNo + 7;
                        int videoClasslength = p.substring(videoClassNo).indexOf("\"");
                        String videoClass = p.substring(videoClassNo, videoClassNo + videoClasslength);
                        p = p.replace(videoClass, videoClass + " col-xs-12");
                        int videoHeightNo = p.indexOf("height=\"");
                        videoHeightNo = videoHeightNo + 8;
                        int videoHeightlength = p.substring(videoHeightNo).indexOf("\"");
                        String videoHeight = p.substring(videoHeightNo, videoHeightNo + videoHeightlength);
                        p = p.replace(videoHeight, "auto");
                        video = p.replace("<p>", "<p class='col-xs-12'>") + "</p>";
                    }
                } else {
                    html += p + "</p>";
                }
            }
            cmsMasterExt.setVideo(video);
            cmsMasterExt.setContentHtml(html);
            json.put("results", cmsMasterExt);
            json.put("resultCode", Constants.NORMAL);
            json.put("resultMessage", Constants.SUCCESS_MSG);
        }catch(Exception e){
            json.put("resultCode",Constants.FAIL);
        }
        return json;
    }

    /**
     * 根据商品分类获取新品商品
     * @param data
     * @return
     */

    @Cacheable(value="${new_goods_list_catch_name}", key="#data")
    public JSONObject getNewGoods(String data) {
        JSONObject json = new JSONObject();
        try{
            //转换数据格式
            CmsItemsExt cmsItemsExt = JSON.parseObject(data, CmsItemsExt.class);
            List<CmsMasterExt> cmList = cmsMasterMapperExt.selectCmsMasterByItemCode(cmsItemsExt.getItemCode());
            if (cmList != null && cmList.size() > 0) {
//                List<GdsMasterExt> gdsList = new ArrayList<GdsMasterExt>();
                List<String> gdsList = new ArrayList<String>();
                String gdsIds = "";
                for (CmsMasterExt cmExt : cmList) {
                    List<CmsMasterGoods> goodsList = cmsMasterGoodsMapperExt.selectGoodsByCmsId(cmExt.getCmsId());
                    if (goodsList != null && goodsList.size() > 0) {
                        GdsMaster gdsParams = new GdsMaster();
                        for (CmsMasterGoods cmsMasterGoods : goodsList) {
                            GdsMaster gdsMaster = gdsMasterMapper.selectByPrimaryKey(cmsMasterGoods.getGoodsId());
                            if (gdsMaster != null) {
                                if (gdsMaster.getGoodsTypeIdPath().indexOf(cmsItemsExt.getGoodsTypeId()) != -1
                                        || gdsMaster.getGoodsBrandTypeIdPath().indexOf(cmsItemsExt.getGoodsTypeId()) != -1) {
                                    gdsIds = gdsIds.concat(gdsMaster.getGoodsId()).concat(",");
                                }
                            }
                        }
                    }
                }
                if(gdsIds.startsWith(",")){
                    gdsIds = gdsIds.substring(1,gdsIds.length());
                }
                if(gdsIds.endsWith(",")){
                    gdsIds = gdsIds.substring(0,gdsIds.length() - 1 );
                }

                json.put("results", gdsIds);
                json.put("cmsId",cmList.get(0).getCmsId());
            }
            json.put("resultCode", Constants.NORMAL);
            json.put("resultMessage", Constants.SUCCESS_MSG);
        }catch(Exception e){
            json.put("resultCode",Constants.FAIL);
        }
        return json;
    }

    @Override
    public JSONObject getMetaData(String data) {
        JSONObject json = new JSONObject();

        JSONObject pJSON = JSONObject.parseObject(data);
        try{
            List<CmsMasterExt> metaData = cmsItemsMapperExt.getMetaData(pJSON.get("itemCode").toString());
            JSONObject jsonData = new JSONObject();
            for(int i=0;i<metaData.size();i++){
                jsonData.put( metaData.get(i).getItemCode(),metaData.get(i).getTextComment());
            }
            json.put("data", jsonData);
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode",Constants.FAIL);
        }
        return json;
    }

    @Override
    public JSONObject getDeliverData(String data) {
        JSONObject json = new JSONObject();

        JSONObject pJSON = JSONObject.parseObject(data);
        try{
            CmsMasterExt metaData = cmsItemsMapperExt.getDeliverData(pJSON.get("itemCode").toString());

            json.put("data", metaData.getTextComment());
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode",Constants.FAIL);
        }
        return json;
    }
}
