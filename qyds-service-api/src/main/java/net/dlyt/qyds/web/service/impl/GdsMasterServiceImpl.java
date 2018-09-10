package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.*;
import net.dlyt.qyds.common.form.*;
import net.dlyt.qyds.dao.*;
import net.dlyt.qyds.dao.ext.*;
import net.dlyt.qyds.web.service.ActMasterService;
import net.dlyt.qyds.web.service.CatcheRemoveService;
import net.dlyt.qyds.web.service.GdsMasterService;
import net.dlyt.qyds.web.service.common.*;
import net.dlyt.qyds.web.service.common.ComCode;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by congkeyan on 2016/7/19.
 */
@Service("gdsMasterService")
@Transactional(readOnly = true)
public class GdsMasterServiceImpl implements GdsMasterService {

//    net.dlyt.qyds.web.service.erp.Service service;// = new net.dlyt.qyds.web.service.erp.Service();
//    ServiceSoap soap;// = service.getServiceSoap();
    protected final Logger log = LoggerFactory.getLogger(GdsMasterServiceImpl.class);
    @Autowired
    ActGoodsMapperExt actGoodsMapperExt;
    @Autowired
    GdsScheduleMapper gdsScheduleMapper;
    @Autowired
    GdsScheduleMapperExt gdsScheduleMapperExt;
    @Autowired
    MmbGroupMemberMapperExt mmbGroupMemberMapperExt;
    @Autowired
    ActMemberMapperExt actMemberMapperExt;
    @Autowired
    MmbMasterMapperExt mmbMasterMapperExt;
    @Autowired
    ErpGoodsSizeMapper erpGoodsSizeMapper;
    @Autowired
    private GdsMasterMapperExt gdsMasterMapperExt;
    @Autowired
    private GdsMasterMapper gdsMasterMapper;
    @Autowired
    private GdsDetailMapper gdsDetailMapper;
    @Autowired
    private GdsSkuMapperExt gdsSkuMapperExt;
    @Autowired
    private GdsSuitlistMapper gdsSuitlistMapper;
    @Autowired
    private GdsSuitlistMapperExt gdsSuitlistMapperExt;
    @Autowired
    private GdsColoreimageMapper gdsColoreimageMapper;
    @Autowired
    private GdsBrandMapper gdsBrandMapper;
    @Autowired
    private GdsColorMapperExt gdsColorMapperExt;
    @Autowired
    private GdsSellMapper gdsSellMapper;
    @Autowired
    private SkuMapperExt skuMapperExt;
    @Autowired
    private ActMasterService actMasterService;
    @Autowired
    private GdsTypeMapper gdsTypeMapper;
    @Autowired
    private MmbCollectionMapperExt mmbCollectionMapperExt;
    @Autowired
    private CmsMasterMapperExt cmsMasterMapperExt;
    @Autowired
    private ActMasterMapperExt actMasterMapperExt;

    @Autowired
    private CatcheRemoveService catcheRemoveService;

    /**
     * 获取所有商品
     *
     * @return
     */
    public JSONObject getAllList() {
        JSONObject json = new JSONObject();
        try {
            GdsMasterExt ext = new GdsMasterExt();
            ext.setShopId(Constants.ORGID);

            //数据库检索 -- 过滤数据
            List<GdsMasterExt> list = gdsMasterMapperExt.getAllList(ext);
            json.put("data", list);
            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 获取商品品牌列表
     *
     * @return
     */
    public JSONObject selectAll(GdsMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            GdsMasterExt ext = new GdsMasterExt();
            ext.setShopId(Constants.ORGID);
            ext.setGoodsName(form.getGoodsName());
            ext.setGoodsCode(form.getGoodsCode());
            ext.setType(form.getType());
            ext.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
            ext.setStartPoint(Integer.parseInt(form.getiDisplayStart()));
            ext.setIsOnsell(form.getIsOnsell());
            ext.setMaintainStatus(form.getMaintainStatus());

            //数据库检索 -- 过滤数据
            List<GdsMasterExt> list = gdsMasterMapperExt.selectAll(ext);
            //获取商品的总数
            int allCount = gdsMasterMapperExt.getAllDataCount(ext);
            json.put("aaData", list);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);


            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;

    }

    /**
     * 保存排序数据
     *
     * @param data
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject sortSave(String data) {
        JSONObject map = new JSONObject();
        try {
            GdsMaster gdsMaster = (GdsMaster) JSON.parseObject(data, GdsMaster.class);
            gdsMasterMapper.updateByPrimaryKeySelective(gdsMaster);
            map.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }

    /**
     * 保存安全库存数据
     *
     * @param data
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject storeSave(String data) {
        JSONObject map = new JSONObject();
        try {
            ErpGoodsSize erpGoodsSize = (ErpGoodsSize) JSON.parseObject(data, ErpGoodsSize.class);
            erpGoodsSizeMapper.updateByPrimaryKeySelective(erpGoodsSize);
            ErpGoodsSizeKey key = new ErpGoodsSizeKey();
            key.setSizeCode(erpGoodsSize.getSizeCode());
            key.setSizeTypeCode(erpGoodsSize.getSizeTypeCode());
            map.put("data", erpGoodsSizeMapper.selectByPrimaryKey(key));
            map.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }


    /**
     * 保存数据到商品表中
     *
     * @param data
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Caching(evict = { @CacheEvict(value="${goods_list_catch_name}",allEntries=true), @CacheEvict(value="${new_goods_list_catch_name}",allEntries=true)
            , @CacheEvict(value="${activity_goods_list_catch_name}",allEntries=true) , @CacheEvict(value="${goods_second_main_catch_name}",allEntries=true)
            , @CacheEvict(value="${cms_items_catch_name}",allEntries=true)})
    public JSONObject save(String data, Map<String, Object> userMap) {
        JSONObject map = new JSONObject();
        try {
            //获取前台传递的数据
            GdsMaster gdsMaster = (GdsMaster) JSON.parseObject(data, GdsMaster.class);
            GdsDetail gdsMasterDetail = (GdsDetail) JSON.parseObject(data, GdsDetail.class);
            //店铺ID
            gdsMaster.setShopId(Constants.ORGID);
            if (gdsMaster == null) {
                map.put("resultCode", Constants.FAIL);
                return map;
            }

            //ERP商品首次编辑
            if ("10".equals(gdsMaster.getType())
                    && gdsMaster.getGoodsId().length() < 36) {
                gdsMaster.setGoodsId(null);
                //维护状态设置成已完成
                gdsMaster.setMaintainStatus("20");
            }

//            BaseDate baseDate = new BaseDate();
            // 在这里添加ERP调用
//            if ("10".equals(gdsMaster.getType())) {
//
//                //商品Code
//                String goodsCode = gdsMaster.getGoodsCode();
//                //商品分类名称
//                String typeNamePath = gdsMaster.getGoodsTypeNamePath();
//                String typeName1 = typeNamePath.split("_")[0];
//                String typeName2 = typeNamePath.split("_")[1];
//                String typeName3 = typeNamePath.split("_")[2];
//
//                Goods goods = new Goods();
//                goods.setGoodsCode(goodsCode);
//                goods.setGoodstype1(typeName1);
//                goods.setGoodstype2(typeName2);
//                goods.setGoodstype3(typeName3);
//                baseDate.setGoods(goods);

//                if (StringUtils.isEmpty(gdsMaster.getGoodsId())) {
//                    //新建 直接调用ERP
//                    erpGoodsUpdate(date);
//                }else {
//                    //数据库中的数据再反查
//                    GdsMaster gdsMaster1 = gdsMasterMapper.selectByPrimaryKey(gdsMaster.getGoodsId());
//                    //数据库中的分类ID
//                    String goodsTypeId = gdsMaster1.getGoodsTypeId();
//                    //有变化通知ERP
//                    if(!goodsTypeId.equals(gdsMaster.getGoodsTypeId())){
//                        // 调用ERP
//                        erpGoodsUpdate(baseDate);
//                    }
//                }
//            }

            if (StringUtils.isEmpty(gdsMaster.getGoodsId())) {
                //新建
                //主表
                gdsMaster.setDeleted(Constants.DELETED_NO);
                String randomUUID = UUID.randomUUID().toString();
                gdsMaster.setGoodsId(randomUUID);
                gdsMaster.setInsertTime(new Date());
                gdsMaster.setUpdateTime(new Date());

                //详细表
                gdsMasterDetail.setGoodsId(randomUUID);
                gdsMasterDetail.setInsertTime(new Date());
                gdsMasterDetail.setUpdateTime(new Date());
                gdsMasterDetail.setDeleted(Constants.DELETED_NO);

                //如果有套装要添加到套装详细表里边
                String goodsIds = "";
                if ("30".equals(gdsMaster.getType())) {
                    JSONObject object = JSON.parseObject(data);
                    goodsIds = (String) object.get("goodsIds");
                } else if ("10".equals(gdsMaster.getType())) {
                    //ERP商品要把颜色属性插入到颜色表里边
                    String erpGoodsCode = gdsMaster.getErpGoodsCode();
                    String goodsId = gdsMaster.getGoodsId();
                    //通过ERP商品code获取颜色code和颜色名称
                    List<GdsColoreimage> colorList = gdsMasterMapperExt.selectColorCodeAndName(erpGoodsCode);
                    for (GdsColoreimage coloreimage : colorList) {
                        GdsColoreimage item = new GdsColoreimage();
                        item.setGoodsColoreId(UUID.randomUUID().toString());
                        item.setGoodsId(goodsId);
                        item.setColoreCode(coloreimage.getColoreCode());
                        item.setColoreName(coloreimage.getColoreName());
                        item.setDeleted("0");
                        Date date = new Date();
                        item.setUpdateTime(date);
                        item.setInsertTime(date);
                        item.setInsertUserId((String) userMap.get("loginId"));
                        item.setUpdateUserId((String) userMap.get("loginId"));
                        insertColorData(item);
                    }
                }

                //插入主表和详细表
                doInsert(gdsMaster, gdsMasterDetail, goodsIds);
                //新建 直接调用ERP
                if ("10".equals(gdsMaster.getType())) {
//                    erpGoodsUpdate(goodsIds, baseDate);
//                    ErpSendUtil.getInstance().GoodsUpdate(gdsMaster);
                    ErpSendUtil.GoodsUpdate(gdsMaster,gdsMasterMapper);
                }

            } else {
                //编辑
                //主表
                gdsMaster.setUpdateTime(new Date());
                gdsMaster.setInsertUserId(null);

                //详细表
                gdsMasterDetail.setUpdateTime(new Date());
                gdsMasterDetail.setInsertUserId(null);

                //如果有套装要添加到套装详细表里边
                String goodsIds = "";
                if ("30".equals(gdsMaster.getType())) {
                    JSONObject object = JSON.parseObject(data);
                    goodsIds = (String) object.get("goodsIds");
                }

                //数据库中的数据再反查
                GdsMaster gdsMaster1 = gdsMasterMapper.selectByPrimaryKey(gdsMaster.getGoodsId());
                //更新主表和详细表
                doUpdate(gdsMaster, gdsMasterDetail, goodsIds);

                if ("10".equals(gdsMaster.getType())) {
                    //数据库中的分类ID
                    String goodsTypeId = gdsMaster1.getGoodsTypeId();
                    //有变化通知ERP
                    if (!goodsTypeId.equals(gdsMaster.getGoodsTypeId())) {
                        // 调用ERP
//                        erpGoodsUpdate(gdsMaster.getGoodsId(), baseDate);
//                        ErpSendUtil.getInstance().GoodsUpdate(gdsMaster);
                        ErpSendUtil.GoodsUpdate(gdsMaster,gdsMasterMapper);
                    }
                }
            }

            //调用清理缓存 全部
            catcheRemoveService.sendPost(Constants.PC_SOAP_URL+"removeAll.json",null);
            catcheRemoveService.sendPost(Constants.WX_SOAP_URL+"removeAll.json",null);

            map.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }

    /**
     * ERP商品分类接口调用
     *
     * @param data
     * @return
     */
//    private void erpGoodsUpdate(String id, BaseDate data) {
//        GdsMaster master = new GdsMaster();
//        master.setGoodsId(id);
//        master.setErpSendTypeStatus("10");
//        try {
//            String result = soap.goodsUpdate(getKeyGoodsUpdate(data), data);
//            log.debug("ERP goodsUpdate result code:"+result+",param:"+ JSON.toJSONString(data));
//            if (result.equals("00")) {
//            } else if (result.equals("11")) {
//                throw new Exception("ERP数据库连接失败");
//            } else if (result.equals("12")) {
//                throw new Exception("ERP数据库更新失败");
//            } else if (result.equals("21")) {
//                throw new Exception("ERP验证失败");
//            } else {
//                throw new Exception("ERP未知错误");
//            }
//        } catch (Exception e) {
//            // 若通讯失败,记录失败原因及通讯信息
//            master.setErpSendTypeStatus("20");
//        } finally {
//            master.setUpdateTime(new Date());
//            gdsMasterMapper.updateByPrimaryKeySelective(master);
//        }
//
//    }

    /**
     * 插入商品主表 从表
     *
     * @param gdsMaster
     * @param gdsMasterDetail
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    private void doInsert(GdsMaster gdsMaster, GdsDetail gdsMasterDetail, String goodsIds) {

        //有0的情况进入到数据库中
        if(StringUtil.isEmpty(gdsMaster.getType())
                || "0".equals(gdsMaster.getType())){
            gdsMaster.setType("10");
        }

        gdsMasterMapper.insertSelective(gdsMaster);
        gdsDetailMapper.insertSelective(gdsMasterDetail);
        if (goodsIds.trim().length() > 0) {
            //要清除一下组成套装的商品
            gdsSuitlistMapperExt.delete(gdsMaster.getGoodsId());
            String[] ids = goodsIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                String goods_id_suit = ids[i];
                GdsSuitlist record = new GdsSuitlist();
                String uuId = UUID.randomUUID().toString();
                Date date = new Date();
                record.setSuitId(uuId);
                record.setGoodsId(gdsMaster.getGoodsId());
                record.setGoodsIdSuit(goods_id_suit);
                record.setDeleted("0");
                record.setInsertTime(date);
                record.setUpdateTime(date);
                record.setInsertUserId(gdsMaster.getInsertUserId());
                record.setUpdateUserId(gdsMaster.getUpdateUserId());
                gdsSuitlistMapper.insertSelective(record);
            }
        }
    }


    /**
     * 更新商品主表 从表
     *
     * @param gdsMaster
     * @param gdsMasterDetail
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    private void doUpdate(GdsMaster gdsMaster, GdsDetail gdsMasterDetail, String goodsIds) {
        //有0的情况进入到数据库中
        if(StringUtil.isEmpty(gdsMaster.getType())
                || "0".equals(gdsMaster.getType())){
            gdsMaster.setType("10");
        }
        gdsMasterMapper.updateByPrimaryKeySelective(gdsMaster);
        gdsDetailMapper.updateByPrimaryKeySelective(gdsMasterDetail);
        if (goodsIds.trim().length() > 0) {
            gdsSuitlistMapperExt.delete(gdsMaster.getGoodsId());
            String[] ids = goodsIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                String goods_id_suit = ids[i];
                GdsSuitlist record = new GdsSuitlist();
                String uuId = UUID.randomUUID().toString();
                Date date = new Date();
                record.setSuitId(uuId);
                record.setGoodsId(gdsMaster.getGoodsId());
                record.setGoodsIdSuit(goods_id_suit);
                record.setDeleted("0");
                record.setInsertTime(date);
                record.setUpdateTime(date);
                record.setInsertUserId(gdsMaster.getInsertUserId());
                record.setUpdateUserId(gdsMaster.getUpdateUserId());

                GdsSuitlist gdsSuitlist = gdsSuitlistMapperExt.selectByGoodsIdAndSuitId(record);
                if (gdsSuitlist == null) {
                    gdsSuitlistMapper.insertSelective(record);
                }

            }
        }
    }

    /**
     * 通过ID获取数据
     *
     * @param goodsId
     * @return
     */
    public JSONObject edit(String goodsId) {
        JSONObject json = new JSONObject();
        try {
            if (!StringUtils.isEmpty(goodsId)) {
                //品牌名称
                String brandName = "";
                JSONObject jsonObject = new JSONObject();
                //ERP商品获取视图中的数据
                GdsMasterExt gdsMasterExt = gdsMasterMapperExt.selectViewByPrimaryKey(goodsId);
                if (gdsMasterExt != null && "10".equals(gdsMasterExt.getType()) && goodsId.length() < 36) {
                    jsonObject.put("goods_id", gdsMasterExt.getGoodsId());
                    jsonObject.put("shop_id", gdsMasterExt.getShopId());
                    jsonObject.put("brand_id", gdsMasterExt.getBrandId());
                    jsonObject.put("type", gdsMasterExt.getType());
                    jsonObject.put("goods_code", gdsMasterExt.getErpGoodsCode());
                    jsonObject.put("goods_name", gdsMasterExt.getGoodsName());
                    jsonObject.put("erp_goods_code", gdsMasterExt.getErpGoodsCode());
                    jsonObject.put("erp_goods_name", gdsMasterExt.getErpGoodsName());
                    jsonObject.put("maintain_status", gdsMasterExt.getMaintainStatus());
                    jsonObject.put("is_onsell", gdsMasterExt.getIsOnsell());
                    jsonObject.put("is_waste", gdsMasterExt.getIsWaste());
                    jsonObject.put("erp_style_no", gdsMasterExt.getErpBrandName());
                    jsonObject.put("brandName", gdsMasterExt.getBrandName());

                } else {
                    //商品主表数据
                    GdsMaster gdsMaster = gdsMasterMapper.selectByPrimaryKey(goodsId);
                    //商品详细的数据取得
                    GdsDetail gdsDetail = gdsDetailMapper.selectByPrimaryKey(goodsId);
                    //利用band_id获取品牌的名称
                    GdsBrand gdsBrand = gdsBrandMapper.selectByPrimaryKey(gdsMaster.getBrandId());
                    if (gdsBrand != null) {
                        brandName = gdsBrand.getBrandName();
                    }


                    //商品主表
                    if (gdsMaster != null) {
                        jsonObject.put("goods_id", gdsMaster.getGoodsId());
                        jsonObject.put("shop_id", gdsMaster.getShopId());
                        jsonObject.put("type", gdsMaster.getType());
                        jsonObject.put("brand_id", gdsMaster.getBrandId());
                        jsonObject.put("goods_type_id", gdsMaster.getGoodsTypeId());
                        jsonObject.put("goods_type_id_path", gdsMaster.getGoodsTypeIdPath());
                        jsonObject.put("goods_type_code", gdsMaster.getGoodsTypeCode());
                        jsonObject.put("goods_type_code_path", gdsMaster.getGoodsTypeCodePath());
                        jsonObject.put("goods_type_name_path", gdsMaster.getGoodsTypeNamePath());
                        jsonObject.put("goods_brand_type_id", gdsMaster.getGoodsBrandTypeId());
                        jsonObject.put("goods_brand_type_id_path", gdsMaster.getGoodsBrandTypeIdPath());
                        jsonObject.put("goods_brand_type_code", gdsMaster.getGoodsBrandTypeCode());
                        jsonObject.put("goods_brand_type_code_path", gdsMaster.getGoodsBrandTypeCodePath());
                        jsonObject.put("goods_brand_type_name_path", gdsMaster.getGoodsBrandTypeNamePath());
                        jsonObject.put("erp_style_no", gdsMaster.getErpStyleNo());
                        jsonObject.put("goods_code", gdsMaster.getGoodsCode());
                        jsonObject.put("goods_name", gdsMaster.getGoodsName());
                        jsonObject.put("erp_goods_code", gdsMaster.getErpGoodsCode());
                        jsonObject.put("erp_goods_name", gdsMaster.getErpGoodsName());
                        jsonObject.put("maintain_status", gdsMaster.getMaintainStatus());
                        jsonObject.put("is_onsell", gdsMaster.getIsOnsell());
                        jsonObject.put("is_waste", gdsMaster.getIsWaste());
                    }
                    //详细
                    if (gdsDetail != null) {
                        jsonObject.put("search_key", gdsDetail.getSearchKey());
                        jsonObject.put("imageUrlJson", gdsDetail.getImageUrlJson());
                        jsonObject.put("property_json", gdsDetail.getPropertyJson());
                        jsonObject.put("introduce_html", gdsDetail.getIntroduceHtml());
                        jsonObject.put("description", gdsDetail.getDescription());
                        jsonObject.put("size_description", gdsDetail.getSizeDescription());
                    }
                    //商品品牌
                    jsonObject.put("brandName", brandName);
                }

                json.put("data", jsonObject);
            }
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 删除商品信息
     *
     * @param goodsId
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject delete(String goodsId) {
        JSONObject json = new JSONObject();
        try {
            if (!StringUtils.isEmpty(goodsId)) {
                //主表
                GdsMaster gdsMaster = new GdsMaster();
                gdsMaster.setGoodsId(goodsId);
                gdsMaster.setDeleted("1");
                gdsMasterMapper.updateByPrimaryKeySelective(gdsMaster);
                //详细表
                GdsDetail gdsDetail = new GdsDetail();
                gdsDetail.setGoodsId(goodsId);
                gdsDetail.setDeleted("1");
                gdsDetailMapper.updateByPrimaryKeySelective(gdsDetail);
                //sku表
                GdsSku gdsSku = new GdsSku();
                gdsSku.setGoodsId(goodsId);
                gdsSku.setDeleted("1");
                gdsSkuMapperExt.updateByGoodsId(gdsSku);

                //套装部分
                GdsSuitlist gdsSuitlist = new GdsSuitlist();
                gdsSuitlist.setGoodsId(goodsId);
                gdsSuitlist.setDeleted("1");
                gdsSuitlistMapperExt.updateByGoodsId(gdsSuitlist);
            }
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;

    }


    /**
     * 发布商品信息
     *
     * @param goodsId
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject publicGoods(String goodsId, Map<String, Object> userMap) {
        JSONObject json = new JSONObject();
        try {
            if (!StringUtils.isEmpty(goodsId)) {
                //主表
                GdsMaster gdsMaster = new GdsMaster();
                gdsMaster.setGoodsId(goodsId);
                gdsMaster.setUpdateTime(new Date());
                gdsMaster.setUpdateUserId((String) userMap.get("loginId"));
                gdsMaster.setMaintainStatus("30");
                gdsMasterMapper.updateByPrimaryKeySelective(gdsMaster);
            }
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;

    }

    /**
     * 通过ID获取sku数据
     *
     * @param goodsId
     * @return
     */
    public JSONObject skuEdit(String goodsId) {
        JSONObject json = new JSONObject();
        try {
            if (!StringUtils.isEmpty(goodsId)) {
                GdsMasterExt gdsMasterExt = gdsMasterMapperExt.selectItemByKey(goodsId);
                json.put("data", gdsMasterExt);

                //ERP单品
                if ("10".equals(gdsMasterExt.getType())) {
                    //获取颜色列表
                    List<GdsColoreimage> colorList = gdsColorMapperExt.selectColorList(goodsId);
                    json.put("colorList", colorList);
                } else {
                    //根据商品ID获取sku列表
                    List<GdsSku> gdsSkuList = gdsSkuMapperExt.selectByGoodsId(goodsId);
                    json.put("gdsSkuList", gdsSkuList);
                }
            }
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }


    /**
     * 插入到颜色数据表中
     *
     * @param coloreimage
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    private void insertColorData(GdsColoreimage coloreimage) {
        gdsColoreimageMapper.insertSelective(coloreimage);
    }


    /**
     * 通过ID下架商品
     *
     * @param goodsId
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Caching(evict = { @CacheEvict(value="${goods_list_catch_name}",allEntries=true), @CacheEvict(value="${new_goods_list_catch_name}",allEntries=true)
            , @CacheEvict(value="${activity_goods_list_catch_name}",allEntries=true) , @CacheEvict(value="${goods_second_main_catch_name}",allEntries=true)
            , @CacheEvict(value="${cms_items_catch_name}",allEntries=true)})
    public JSONObject unsellGoods(String goodsId, Map<String, Object> userMap) {
        JSONObject json = new JSONObject();
        try {
            if (!StringUtils.isEmpty(goodsId)) {
                GdsMaster gdsMaster = gdsMasterMapper.selectByPrimaryKey(goodsId);
                gdsMaster.setIsOnsell("1");
                gdsMaster.setUpdateTime(new Date());
                gdsMaster.setUpdateUserId((String) userMap.get("loginId"));
                gdsMasterMapper.updateByPrimaryKeySelective(gdsMaster);

                GdsSell record = new GdsSell();
                record.setGoodsId(gdsMaster.getGoodsId());
                record.setOffsellInfactTime(DataUtils.formatTimeStampToYMD(new Date()));
                gdsSellMapper.updateByPrimaryKeySelective(record);

                //如果该商品存在于套装中下架套装商品
                List<GdsSuitlist> list = gdsSuitlistMapperExt.selectByGoodsIdSuit(goodsId);
                for (GdsSuitlist item : list) {
                    String suitId = item.getGoodsId();
                    GdsMaster gdsMastersuit = new GdsMaster();
                    //下架套装ID
                    gdsMastersuit.setIsOnsell("1");
                    gdsMastersuit.setGoodsId(suitId);
                    gdsMastersuit.setUpdateTime(new Date());
                    gdsMasterMapper.updateByPrimaryKeySelective(gdsMastersuit);

                    GdsSell recordTz = new GdsSell();
                    recordTz.setGoodsId(suitId);
                    recordTz.setOffsellInfactTime(DataUtils.formatTimeStampToYMD(new Date()));
                    gdsSellMapper.updateByPrimaryKeySelective(recordTz);
                }

                //调用清理缓存 全部
                catcheRemoveService.sendPost(Constants.PC_SOAP_URL+"removeAll.json",null);
                catcheRemoveService.sendPost(Constants.WX_SOAP_URL+"removeAll.json",null);
            }
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 获取已经上架的sku
     *
     * @param form
     * @return
     */
    public JSONObject selectOnsellSku(SkuForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getGoodsCode()) && StringUtil.isEmpty(form.getColorName())) {
                List<SkuForm> list = new ArrayList<SkuForm>();
                int allCount = 0;
                json.put("aaData", list);
                json.put("sEcho", form.getsEcho());
                json.put("iTotalRecords", allCount);
                json.put("iTotalDisplayRecords", allCount);
                json.put("resultCode", Constants.NORMAL);
            } else {
                //数据库检索 -- 过滤数据
                List<SkuForm> list = skuMapperExt.select(form);
                int allCount = skuMapperExt.selectCount(form);
                json.put("aaData", list);
                json.put("sEcho", form.getsEcho());
                json.put("iTotalRecords", allCount);
                json.put("iTotalDisplayRecords", allCount);
                json.put("resultCode", Constants.NORMAL);
            }
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    /**
     * 获取已经上架的sku
     *
     * @param form
     * @return
     */
    public JSONObject getSkuColorList(SkuForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getGoodsCode()) && StringUtil.isEmpty(form.getColorName())) {
                List<SkuForm> list = new ArrayList<SkuForm>();
                int allCount = 0;
                json.put("aaData", list);
                json.put("sEcho", form.getsEcho());
                json.put("iTotalRecords", allCount);
                json.put("iTotalDisplayRecords", allCount);
                json.put("resultCode", Constants.NORMAL);
            } else {
                //数据库检索 -- 过滤数据
                List<SkuForm> list = skuMapperExt.selectSkuColor(form);
                int allCount = skuMapperExt.selectSkuColorCount(form);
                json.put("aaData", list);
                json.put("sEcho", form.getsEcho());
                json.put("iTotalRecords", allCount);
                json.put("iTotalDisplayRecords", allCount);
                json.put("resultCode", Constants.NORMAL);
            }
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    //*******************************API接口部分************************************//

    /**
     * 商品列表的获取方法
     *
     * @param data 包含两个参数 1通过主key获得商品列表
     *             2通过分类获得商品列表
     *             3通过品牌获得商品列表
     *             <p>
     *             4必须传递的参数 updateTime 分页用 第一次不传递
     * @return
     */
    @Cacheable(value="${goods_list_catch_name}", key="#data")
    public JSONObject getProductList(String data) {
        JSONObject json = new JSONObject();
        try {
            //转换数据格式
            GdsMasterExt gdsMasterExt = (GdsMasterExt) JSON.parseObject(data, GdsMasterExt.class);
            //尺码
            if (!StringUtil.isEmpty(gdsMasterExt.getSizeCode())) {
                gdsMasterExt.setSizeTypeCode(gdsMasterExt.getSizeCode().split("_")[0]);
                gdsMasterExt.setSizeCode(gdsMasterExt.getSizeCode().split("_")[1]);
            }

            List<GdsMasterExt> sizeCodeAndNameList = null;
            //在这块判断传过来的分类条件是否在分类表里边存在呢
            //如果存在说明是按照分类查询
            //如果不存在说明是按照品牌系列查询
            if (!StringUtil.isEmpty(gdsMasterExt.getFirstGoodsTypeId())) {

                String[] array = gdsMasterExt.getFirstGoodsTypeId().split("_");
                String goodsTypeId = gdsMasterExt.getFirstGoodsTypeId();
                if (array.length > 1) {
                    goodsTypeId = array[0];
                }
                GdsType gdsType = gdsTypeMapper.selectByPrimaryKey(goodsTypeId);

                if (gdsType == null) {
                    gdsMasterExt.setFirstGoodsBrandTypeId(gdsMasterExt.getFirstGoodsTypeId());
                    gdsMasterExt.setFirstGoodsTypeId(null);
                    sizeCodeAndNameList = gdsMasterMapperExt.getSizeCodeListByfirstBrandErp(gdsMasterExt.getFirstGoodsBrandTypeId());
                } else {
                    sizeCodeAndNameList = gdsMasterMapperExt.getSizeCodeListByfirstErp(gdsMasterExt.getFirstGoodsTypeId());
                }
            }

            if(sizeCodeAndNameList == null
                    || sizeCodeAndNameList.size() == 0){
                GdsMasterExt ext = new GdsMasterExt();
                ext.setGoodsIds(gdsMasterExt.getGoodsIds());
                sizeCodeAndNameList = gdsMasterMapperExt.getSizeCodeListByGoodsIdsErp(ext);
            }

            //返回前台的尺码列表
            List<GdsMasterExt> sizeList = new ArrayList<GdsMasterExt>();
            if (sizeCodeAndNameList != null) {
                for (GdsMasterExt ext : sizeCodeAndNameList) {
                    GdsMasterExt gdsMasterExt1 = new GdsMasterExt();
                    gdsMasterExt1.setSizeCode(ext.getSizeTypeCode() + "_" + ext.getSizeCode());
                    gdsMasterExt1.setSizeName(ext.getSizeName());
                    sizeList.add(gdsMasterExt1);
                }
            }


            if (!StringUtil.isEmpty(gdsMasterExt.getGoodsTypeId())) {
                GdsType gdsType = gdsTypeMapper.selectByPrimaryKey(gdsMasterExt.getGoodsTypeId());
                if (gdsType == null) {
                    gdsMasterExt.setGoodsBrandTypeId(gdsMasterExt.getGoodsTypeId());
                    gdsMasterExt.setGoodsTypeId(null);
                }
            }

            if (gdsMasterExt.getCurrentPage() != 0 && gdsMasterExt.getPageSize() != 0) {
                //获取总页数
                int allCount = gdsMasterMapperExt.selectProductsByParameterCountUp(gdsMasterExt);
                int devi = allCount / gdsMasterExt.getPageSize();
                int count = 0;
                if (allCount % gdsMasterExt.getPageSize() == 0) {
                    count = devi;
                } else {
                    count = devi + 1;
                }
                json.put("allCount", count);
                gdsMasterExt.setDisplayItemCount(gdsMasterExt.getPageSize() * (gdsMasterExt.getCurrentPage() - 1));
            }

            if(!StringUtil.isEmpty(gdsMasterExt.getSearchKey())){
                gdsMasterExt.setSearchKey(gdsMasterExt.getSearchKey().toLowerCase());
            }
            List<GdsMasterExt> pList = gdsMasterMapperExt.selectProductsByParameterUp(gdsMasterExt);
            for (GdsMasterExt gdsMasterExt1: pList) {
                List<GdsColoreimage> gdsColoreimages = gdsColorMapperExt.selectColorList(gdsMasterExt1.getGoodsId());
                gdsMasterExt1.setGdsColoreimageList(gdsColoreimages);
            }
            json.put("sizeList", sizeList);
            json.put("results", pList);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }


    public List<GdsMasterExt> resetGoodsList(List<GdsMasterExt> list,String memberId){

        //遍历获取价格信息,活动标签
        for (GdsMasterExt ext : list) {
            List<HashMap> activityIdsList = null;
            List<String> activity_type = new ArrayList<String>();
            if (Constants.GDS_SUIT.equals(ext.getType())) {
                activityIdsList = gdsMasterMapperExt.selectActivityIdsForSuit(ext.getGoodsId());
                //套装的场合要把价格合计
                float minPriceTotal = 0;
                float maxPriceTotal = 0;
                List<GdsSuitlist> suitlists = gdsSuitlistMapperExt.selectByGoodsId(ext.getGoodsId());
                for (GdsSuitlist gdsSuitlist : suitlists) {
                    String goodsId = gdsSuitlist.getGoodsIdSuit();
                    GdsMasterExt gdsMasExt = new GdsMasterExt();
                    gdsMasExt.setGoodsId(goodsId);
                    List<GdsMasterExt> suitList = gdsMasterMapperExt.selectProductsByParameterUp(gdsMasExt);//参数验证
                    if (suitList.size() != 1) {
                        throw new ExceptionBusiness("参数错误!");
                    }
                    minPriceTotal = minPriceTotal + Float.parseFloat(suitList.get(0).getMinPrice());
                    maxPriceTotal = maxPriceTotal + Float.parseFloat(suitList.get(0).getMaxPrice());
                }
                ext.setMaxPrice(String.valueOf(maxPriceTotal));
                ext.setMinPrice(String.valueOf(minPriceTotal));
            } else {
                activityIdsList = gdsMasterMapperExt.selectActivityIds(ext.getGoodsId());
            }

            //组成获取优惠价格的form
            List<ActMasterForm> activityFormList = new ArrayList<ActMasterForm>();

            //获取最小优惠
            for (int i = 0; i < activityIdsList.size(); i++) {
                ActMasterForm form = new ActMasterForm();
                form.setActivityId((String) activityIdsList.get(i).get("activity_id"));
                form.setOriginPrice(Float.parseFloat(ext.getMinPrice()));
                activityFormList.add(form);
            }
            JSONObject jsonObjectMin = actMasterService.getNewPricesByActivity(activityFormList, memberId, ext.getGoodsId());
            //最小的优惠价钱
            float minNewPrice = 0;
            List<ActMasterForm> minList = (ArrayList<ActMasterForm>) jsonObjectMin.get("data");
            if (minList != null && minList.size() != 0) {
                minNewPrice = minList.get(0).getNewPrice();
                ext.setActivityName(minList.get(0).getActivityName());
            }

            //获取最大优惠
            activityFormList.clear();
            for (int i = 0; i < activityIdsList.size(); i++) {
                ActMasterForm form = new ActMasterForm();
                form.setActivityId((String) activityIdsList.get(i).get("activity_id"));
                form.setOriginPrice(Float.parseFloat(ext.getMaxPrice()));
                activityFormList.add(form);
            }
            JSONObject jsonObjectMax = actMasterService.getNewPricesByActivity(activityFormList, memberId, ext.getGoodsId());
            //最大的优惠价钱
            float maxNewPrice = 0;
            List<ActMasterForm> maxList = (ArrayList<ActMasterForm>) jsonObjectMax.get("data");
            if (maxList != null && maxList.size() != 0) {
                maxNewPrice = maxList.get(0).getNewPrice();
                ext.setActivityName(maxList.get(0).getActivityName());
            }
            //返回给前台的优惠信息情况
            if (minNewPrice == 0 || maxNewPrice == 0) {
                ext.setMinAndMaxPriceActivity(null);
            } else if (minNewPrice == maxNewPrice) {
                ext.setMinAndMaxPriceActivity("¥" + minNewPrice);
            } else {
                ext.setMinAndMaxPriceActivity("¥" + minNewPrice + "~" + "¥" + maxNewPrice);
            }
            //返回给前台的非优惠信息情况
            if (ext.getMinPrice() != null && ext.getMaxPrice() != null && ext.getMinPrice().equals(ext.getMaxPrice())) {
                ext.setMinAndMaxPrice("¥" + ext.getMinPrice());
            }
            if (ext.getMinPrice() == null) {
                ext.setMinAndMaxPrice("¥0.00");
            }


            //获取商品图片的第一张
            if (ext.getImageUrlJson().startsWith(",")) {
                ext.setImageUrlJson(ext.getImageUrlJson().substring(1, ext.getImageUrlJson().length()));
            }
            if (ext.getImageUrlJson().endsWith(",")) {
                ext.setImageUrlJson(ext.getImageUrlJson().substring(0, ext.getImageUrlJson().length() - 1));
            }
            if (!StringUtils.isEmpty(ext.getImageUrlJson())) {
                String imageUrlJson = ext.getImageUrlJson().split(",")[0];
                ext.setImageUrlJson(imageUrlJson);
            }

            //返回给前台的非优惠信息情况
            if (ext.getMinPrice() != null && ext.getMaxPrice() != null && ext.getMinPrice().equals(ext.getMaxPrice())) {
                ext.setMinAndMaxPrice("¥" + ext.getMinPrice());
            }

            if (ext.getMinPrice() == null) {
                ext.setMinAndMaxPrice("¥0.00");
            }

        }
        return list;
    }

    /**
     * 商品详细的获取方法
     *
     * @param data 包含两个参数 商品ID
     * @return
     */
    public JSONObject getProductDetail(String data) {
        JSONObject json = new JSONObject();
        try {
            //转换数据格式
            GdsMasterExt gdsMasterExt = (GdsMasterExt) JSON.parseObject(data, GdsMasterExt.class);
            List<GdsMasterExt> pList = gdsMasterMapperExt.selectProductsByParameter(gdsMasterExt);

            //参数验证
            if (pList.size() != 1) {
                throw new ExceptionBusiness("参数错误!");
            }

            // 商品的详细信息
            GdsMasterExt result = pList.get(0);
            result.setMemberId(gdsMasterExt.getMemberId());
            //List<String> activity_type = new ArrayList<String>();
            //活动信息
            //List<HashMap>  activityList = null;
            List<HashMap> activityIdsList = null;
            //套装的场合
            if (Constants.GDS_SUIT.equals(result.getType())) {
                activityIdsList = gdsMasterMapperExt.selectActivityIdsForSuit(result.getGoodsId());
                //activityList = gdsMasterMapperExt.selectActivityItemsForSuit(result.getGoodsId());
                //套装的场合要把价格合计
                float minPriceTotal = 0;
                float maxPriceTotal = 0;
                List<GdsSuitlist> suitlists = gdsSuitlistMapperExt.selectByGoodsId(result.getGoodsId());
                for (GdsSuitlist gdsSuitlist : suitlists) {
                    String goodsId = gdsSuitlist.getGoodsIdSuit();
                    GdsMasterExt gdsMasExt = new GdsMasterExt();
                    gdsMasExt.setGoodsId(goodsId);
                    List<GdsMasterExt> suitList = gdsMasterMapperExt.selectProductsByParameter(gdsMasExt);//参数验证
                    if (suitList.size() != 1) {
                        throw new ExceptionBusiness("参数错误!");
                    }
                    maxPriceTotal = maxPriceTotal + Float.parseFloat(suitList.get(0).getMaxPrice());
                    minPriceTotal = minPriceTotal + Float.parseFloat(suitList.get(0).getMinPrice());
                }
                result.setMinPrice(String.valueOf(minPriceTotal));
                result.setMaxPrice(String.valueOf(maxPriceTotal));

            } else {
                activityIdsList = gdsMasterMapperExt.selectActivityIds(result.getGoodsId());
                //activityList = gdsMasterMapperExt.selectActivityItems(result.getGoodsId());
            }
//            //标签
//            for(int i = 0; i<activityList.size(); i++){
//                activity_type.add((String)activityList.get(i).get("actition_type"));
//            }

            //组成获取优惠价格的form
            List<ActMasterForm> activityFormList = new ArrayList<ActMasterForm>();

            //获取最小优惠
            for (int i = 0; i < activityIdsList.size(); i++) {
                ActMasterForm form = new ActMasterForm();
                form.setActivityId((String) activityIdsList.get(i).get("activity_id"));
                form.setOriginPrice(Float.parseFloat(result.getMinPrice()));
                activityFormList.add(form);
            }
            JSONObject jsonObjectMin = actMasterService.getNewPricesByActivity(activityFormList, result.getMemberId(), result.getGoodsId());
            //最小的优惠价钱
            float minNewPrice = 0;
            //活动信息
            ActMasterForm activityInfo = null;
            List<ActMasterForm> minList = (ArrayList<ActMasterForm>) jsonObjectMin.get("data");
            if (minList != null && minList.size() != 0) {
                minNewPrice = minList.get(0).getNewPrice();
                activityInfo = minList.get(0);
                result.setActivityName(minList.get(0).getActivityName());
            }
//            for(ActMasterForm form : minList){
//                if(form.getNewPrice() > minNewPrice){
//                    minNewPrice = form.getNewPrice();
//                }
//            }

            //获取最大优惠
            activityFormList.clear();
            for (int i = 0; i < activityIdsList.size(); i++) {
                ActMasterForm form = new ActMasterForm();
                form.setActivityId((String) activityIdsList.get(i).get("activity_id"));
                form.setOriginPrice(Float.parseFloat(result.getMaxPrice()));
                activityFormList.add(form);
            }
            JSONObject jsonObjectMax = actMasterService.getNewPricesByActivity(activityFormList, result.getMemberId(), result.getGoodsId());
            //最大的优惠价钱
            float maxNewPrice = 0;
            List<ActMasterForm> maxList = (ArrayList<ActMasterForm>) jsonObjectMax.get("data");
            if (maxList != null && maxList.size() != 0) {
                maxNewPrice = maxList.get(0).getNewPrice();
                result.setActivityName(maxList.get(0).getActivityName());
            }
//            for(ActMasterForm form : maxList){
//                if(form.getNewPrice() > maxNewPrice){
//                    maxNewPrice = form.getNewPrice();
//                }
//            }
            //返回给前台的优惠信息情况
            if (minNewPrice == 0 || maxNewPrice == 0) {
                result.setMinAndMaxPriceActivity(null);
            } else if (minNewPrice == maxNewPrice) {
                result.setMinAndMaxPriceActivity("¥" + minNewPrice);
            } else {
                result.setMinAndMaxPriceActivity("¥" + minNewPrice + "~" + "¥" + maxNewPrice);
            }
            //返回给前台的非优惠信息情况
            if (result.getMinPrice() != null && result.getMaxPrice() != null && result.getMinPrice().equals(result.getMaxPrice())) {
                result.setMinAndMaxPrice("¥" + result.getMinPrice());
            }
            if (result.getMinPrice() == null) {
                result.setMinAndMaxPrice("¥0.00");
            }

//            //活动标签获取
//            result.setActivityTags(activity_type);
            //获取商品图片的第一张
            if (!StringUtils.isEmpty(result.getImageUrlJson())) {
                result.setImageUrlJsonPc(result.getImageUrlJson().split(","));
                String imageUrlJson = result.getImageUrlJson().split(",")[0];
                result.setImageUrlJson(imageUrlJson);
            }

            List<ViewOnsellSku> viewOnsellSkuList = new ArrayList<ViewOnsellSku>();
            //套装的场合 SKU信息获取
            if (Constants.GDS_SUIT.equals(result.getType())) {
                //根据商品ID获取组成该套餐的商品ID列表
                List<GdsSuitlist> suitlists = gdsSuitlistMapperExt.selectByGoodsId(result.getGoodsId());
                List<GdsMasterExt> regoodsList = new ArrayList<GdsMasterExt>();
                for (GdsSuitlist suitlist : suitlists) {
                    String goodsId = suitlist.getGoodsIdSuit();
                    GdsMasterExt ext = new GdsMasterExt();
                    ext.setGoodsId(goodsId);
                    List<GdsMasterExt> goodsList = gdsMasterMapperExt.selectProductsByParameter(ext);
                    //组成套装的商品信息
                    ext = goodsList.get(0);
                    ext = this.getColorAndSizeList(ext);
                    viewOnsellSkuList = new ArrayList<ViewOnsellSku>();
                    ext.setSkulist(this.getSkuInfo(viewOnsellSkuList, ext, Constants.GDS_SUIT));
                    regoodsList.add(ext);
                }
                result.setGoodsList(regoodsList);
            } else {
                result = this.getColorAndSizeList(result);
                result.setSkulist(this.getSkuInfo(viewOnsellSkuList, result, null));

            }
            //获取是否加入心愿单的标记
            MmbCollectionForm form = new MmbCollectionForm();
            form.setMemberId(result.getMemberId());
            form.setObjectId(result.getGoodsId());
            MmbCollectionExt mmbCollectionExt = mmbCollectionMapperExt.selectSameCollection(form);
            result.setIsInWishlist("0");
            if (mmbCollectionExt != null) {
                result.setCollectNo(mmbCollectionExt.getCollectNo());
                result.setIsInWishlist("1");
            }
            // 获取售后服务html
            List<CmsMasterExt> cmList = cmsMasterMapperExt.selectCmsMasterByItemCode(Constants.GOODS_SERVE);
            CmsMasterExt cmsMasterExt = null;
            if (cmList != null && cmList.size() > 0) {
                cmsMasterExt = cmList.get(0);
            }
            if (cmsMasterExt != null) {
                result.setGoodsServeHtml(cmsMasterExt.getContentHtml());
            }

//            GdsSell gdsSell = gdsSellMapper.selectByPrimaryKey(result.getGoodsId());
//            //获取搭配款式
//            String mating_json = gdsSell.getMatingJson();
//            List<GdsMasterExt> mating_list = new ArrayList<GdsMasterExt>();
//            if (!StringUtil.isEmpty(mating_json)) {
//                String[] mattingArray = mating_json.split(",");
//                for (int i = 0; i < mattingArray.length; i++) {
//                    String goodsId = mattingArray[i];
//                    //通过goodsId检索商品信息
//                    gdsMasterExt.setGoodsId(goodsId);
//                    List<GdsMasterExt> list = gdsMasterMapperExt.selectProductsByParameter(gdsMasterExt);
//
//                    //参数验证
//                    if (list.size() != 1) {
//                        //throw new ExceptionBusiness("参数错误!");
//                        continue;
//                    }
//                    GdsMasterExt ext = list.get(0);
//
//                    //搭配商品还要向前台传递sku信息
//                    //ext = this.getColorAndSizeList(ext);
//                    //ext.setSkulist(this.getSkuInfo(viewOnsellSkuList, ext, null));
//
//                    //返回给前台的非优惠信息情况
//                    if (ext.getMinPrice() != null && ext.getMaxPrice() != null && ext.getMinPrice().equals(ext.getMaxPrice())) {
//                        ext.setMinAndMaxPrice("¥" + ext.getMinPrice());
//                    }
//                    if (ext.getMinPrice() == null) {
//                        ext.setMinAndMaxPrice("¥0.00");
//                    }
//                    mating_list.add(ext);
//                }
//                //设定到前台去
//                result.setMating_list(mating_list);
//            }
//
//            //获取猜你喜欢
//            String recommend_json = gdsSell.getRecommendJson();
//            List<GdsMasterExt> recommend_list = new ArrayList<GdsMasterExt>();
//            if (!StringUtil.isEmpty(recommend_json)) {
//                String[] recommendArray = recommend_json.split(",");
//                for (int i = 0; i < recommendArray.length; i++) {
//                    String goodsId = recommendArray[i];
//                    gdsMasterExt.setGoodsId(goodsId);
//                    //通过goodsId检索商品信息
//                    List<GdsMasterExt> list = gdsMasterMapperExt.selectProductsByParameter(gdsMasterExt);
//
//                    //参数验证
//                    if (list.size() != 1) {
//                        //throw new ExceptionBusiness("参数错误!");
//                        continue;
//                    }
//
//                    GdsMasterExt ext = list.get(0);
//                    //返回给前台的非优惠信息情况
//                    if (ext.getMinPrice() != null && ext.getMaxPrice() != null && ext.getMinPrice().equals(ext.getMaxPrice())) {
//                        ext.setMinAndMaxPrice("¥" + ext.getMinPrice());
//                    }
//                    if (ext.getMinPrice() == null) {
//                        ext.setMinAndMaxPrice("¥0.00");
//                    }
//                    recommend_list.add(list.get(0));
//                }
//                //设定到前台去
//                result.setRecommend_list(recommend_list);
//            }

            //如果是秒杀的时候要特殊处理一下
            if(activityInfo != null && ComCode.ActivityType.SECOND_KILL.equals(activityInfo.getActivityType())){
                //秒杀
                Date current = new Date();
                try {
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat ydf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String currentDateStr = sdf.format(current);

                    String startTime = currentDateStr + " " + df.format(activityInfo.getStartTime());
                    String endTime = currentDateStr + " " + df.format(activityInfo.getEndTime());
                    activityInfo.setStartTime(ydf.parse(startTime));
                    activityInfo.setEndTime(ydf.parse(endTime));
                } catch (Exception e) {
                }
                result.setActivityInfo(activityInfo);
                result.setNowTime(current);
            }


            json.put("results", result);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    public JSONObject getMatingListAndRecommendList(String data){
        JSONObject json = new JSONObject();
        try {
            //转换数据格式
            GdsMasterExt gdsMasterExt = (GdsMasterExt) JSON.parseObject(data, GdsMasterExt.class);
        GdsSell gdsSell = gdsSellMapper.selectByPrimaryKey(gdsMasterExt.getGoodsId());
        //获取搭配款式
        String mating_json = gdsSell.getMatingJson();
        List<GdsMasterExt> mating_list = new ArrayList<GdsMasterExt>();
        if (!StringUtil.isEmpty(mating_json)) {
            String[] mattingArray = mating_json.split(",");
            for (int i = 0; i < mattingArray.length; i++) {
                String goodsId = mattingArray[i];
                //通过goodsId检索商品信息
                gdsMasterExt.setGoodsId(goodsId);
                List<GdsMasterExt> list = gdsMasterMapperExt.selectProductsByParameter(gdsMasterExt);

                //参数验证
                if (list.size() != 1) {
                    //throw new ExceptionBusiness("参数错误!");
                    continue;
                }
                GdsMasterExt ext = list.get(0);

                //搭配商品还要向前台传递sku信息
                //ext = this.getColorAndSizeList(ext);
                //ext.setSkulist(this.getSkuInfo(viewOnsellSkuList, ext, null));

                //返回给前台的非优惠信息情况
                if (ext.getMinPrice() != null && ext.getMaxPrice() != null && ext.getMinPrice().equals(ext.getMaxPrice())) {
                    ext.setMinAndMaxPrice("¥" + ext.getMinPrice());
                }
                if (ext.getMinPrice() == null) {
                    ext.setMinAndMaxPrice("¥0.00");
                }
                mating_list.add(ext);
            }
            //设定到前台去
            gdsMasterExt.setMating_list(mating_list);
        }

        //获取猜你喜欢
        String recommend_json = gdsSell.getRecommendJson();
        List<GdsMasterExt> recommend_list = new ArrayList<GdsMasterExt>();
        if (!StringUtil.isEmpty(recommend_json)) {
            String[] recommendArray = recommend_json.split(",");
            for (int i = 0; i < recommendArray.length; i++) {
                String goodsId = recommendArray[i];
                gdsMasterExt.setGoodsId(goodsId);
                //通过goodsId检索商品信息
                List<GdsMasterExt> list = gdsMasterMapperExt.selectProductsByParameter(gdsMasterExt);

                //参数验证
                if (list.size() != 1) {
                    //throw new ExceptionBusiness("参数错误!");
                    continue;
                }

                GdsMasterExt ext = list.get(0);
                //返回给前台的非优惠信息情况
                if (ext.getMinPrice() != null && ext.getMaxPrice() != null && ext.getMinPrice().equals(ext.getMaxPrice())) {
                    ext.setMinAndMaxPrice("¥" + ext.getMinPrice());
                }
                if (ext.getMinPrice() == null) {
                    ext.setMinAndMaxPrice("¥0.00");
                }
                recommend_list.add(list.get(0));
            }
            //设定到前台去
            gdsMasterExt.setRecommend_list(recommend_list);
        }
            json.put("results", gdsMasterExt);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    private List<ViewOnsellSku> getSkuInfo(List<ViewOnsellSku> viewOnsellSkuList, GdsMasterExt result, String type) {

        List<HashMap> list = gdsMasterMapperExt.selectSkuItems(result.getGoodsId());

        Map<String,SkuForm> skuFormMap =new ConcurrentHashMap<String,SkuForm>();
        //遍历SKU根据不同情况设定返回给画面的值
        for (int i = 0; i < list.size(); i++) {

            ViewOnsellSku viewOnsellSku = new ViewOnsellSku();

            String skuId = (String) list.get(i).get("skuid");
            viewOnsellSku.setSkuid(skuId);

            //ERP商品
            List<SkuNameKeyValue> nameKeyValueList = new ArrayList<SkuNameKeyValue>();
            if (Constants.GDS_ERP.equals(result.getType())) {
                String color_code = (String) list.get(i).get("color_code");
                String color_name = (String) list.get(i).get("color_name");
                String size_code = (String) list.get(i).get("size_code");
                String size_name = (String) list.get(i).get("size_name");
                String bnk_no_limit = String.valueOf(list.get(i).get("bnk_no_limit")==null?0:list.get(i).get("bnk_no_limit"));
                String bnk_less_limit = String.valueOf(list.get(i).get("bnk_less_limit")==null?0:list.get(i).get("bnk_less_limit"));

                SkuNameKeyValue nameKeyValue = new SkuNameKeyValue();
                nameKeyValue.setName("颜色");
                nameKeyValue.setKey(color_code);
                nameKeyValue.setValue(color_name);

                SkuNameKeyValue nameKeyValue1 = new SkuNameKeyValue();
                nameKeyValue1.setName("尺寸");
                nameKeyValue1.setKey(size_code);
                nameKeyValue1.setValue(size_name);
                nameKeyValue1.setBnk_no_limit(bnk_no_limit);
                nameKeyValue1.setBnk_less_limit(bnk_less_limit);

                nameKeyValueList.add(nameKeyValue);
                nameKeyValueList.add(nameKeyValue1);

                //图片信息
                String erpimg = (String) list.get(i).get("erpimg");
                List<String> imgList = new ArrayList<String>();
                if (!StringUtil.isEmpty(erpimg)) {
                    String[] strs = erpimg.split(",");
                    for (int j = 0; j < strs.length; j++) {
                        imgList.add(strs[j]);
                    }
                }
                viewOnsellSku.setImgs(imgList);

            } else {
                //电商商品
                String skucontent = (String) list.get(i).get("skucontent");
                JSONObject jsonObject = (JSONObject) JSON.parse(skucontent);
                JSONArray imageArray = (JSONArray) jsonObject.get("sku_img");
                String sku_display = (String) jsonObject.get("sku_display");
                String sku_key = (String) jsonObject.get("sku_key");
                String sku_value = (String) jsonObject.get("sku_value");

                String[] skuDs = sku_display.split("_");
                String[] skuKs = sku_key.split("_");
                String[] skuVs = sku_value.split("_");

                for (int j = 0; j < skuDs.length; j++) {
                    SkuNameKeyValue nameKeyValue = new SkuNameKeyValue();
                    nameKeyValue.setName(skuDs[j]);
                    nameKeyValue.setKey(skuKs[j]);
                    nameKeyValue.setValue(skuVs[j]);
                    nameKeyValueList.add(nameKeyValue);
                }

                List<String> imgList = new ArrayList<String>();
                for (int j = 0; j < imageArray.size(); j++) {
                    imgList.add(String.valueOf(imageArray.get(j)));
                }
                viewOnsellSku.setImgs(imgList);
            }

            viewOnsellSku.setNameKeyValues(nameKeyValueList);

            String new_count = String.valueOf(list.get(i).get("new_count") == null ? "0" : list.get(i).get("new_count"));
            viewOnsellSku.setNewCount(new_count);

            String price = String.valueOf(list.get(i).get("price"));
            viewOnsellSku.setPrice(price);

            //sku的优惠价格
            SkuForm form = new SkuForm();
            form.setGoodsId(result.getGoodsId());
            form.setGoodsTypeId(result.getGoodsTypeId());
            form.setSkuid(skuId);
            form.setBrandId(result.getBrandId());
            form.setPrice(Float.parseFloat(price));
            form.setMemberId(result.getMemberId());
            skuFormMap.put(form.getSkuid(),form);
//            if (type == null) {
//                JSONObject object = actMasterService.getNewPricesBySku(form);
//                if (object.get("data") != null) {
//                    viewOnsellSku.setActivityPrice(String.valueOf(object.get("data")));
//                }
//                if (object.get("activityName") != null) {
//                    viewOnsellSku.setActivityName(String.valueOf(object.get("activityName")));
//                }
//
//                if (object.get("activityType") != null) {
//                    viewOnsellSku.setActivityType(String.valueOf(object.get("activityType")));
//                }
//            }
            viewOnsellSkuList.add(viewOnsellSku);
        }
        List<ViewOnsellSku> viewOnsellSkus=viewOnsellSkuList;
        if (type == null) {
            viewOnsellSkus =actMasterService.getNewPricesBySku(skuFormMap,viewOnsellSkuList);

        }
        return viewOnsellSkus;
//        return viewOnsellSkuList;
    }

    //获取颜色列表和尺码列表
    private GdsMasterExt getColorAndSizeList(GdsMasterExt result) {
        if (Constants.GDS_GOODS.equals(result.getType())) {
            //电商单品的场合
            List<HashMap> list = gdsMasterMapperExt.selectSkuItems(result.getGoodsId());
            List<SkuNameKeyValue> listc = new ArrayList<SkuNameKeyValue>();
            List<SkuNameKeyValue> lists = new ArrayList<SkuNameKeyValue>();
            for (int i = 0; i < list.size(); i++) {
                //电商商品
                String skucontent = (String) list.get(i).get("skucontent");
                JSONObject jsonObject = (JSONObject) JSON.parse(skucontent);
                String sku_display = (String) jsonObject.get("sku_display");
                String sku_key = (String) jsonObject.get("sku_key");
                String sku_value = (String) jsonObject.get("sku_value");

                String[] skuDs = sku_display.split("_");
                String[] skuKs = sku_key.split("_");
                String[] skuVs = sku_value.split("_");

                for (int j = 0; j < skuDs.length; j++) {

                    if (j == 0) {
                        boolean isExist = false;
                        //判断是否已经被加入 如果加入将不再add
                        for (SkuNameKeyValue nameKeyValue : listc) {
                            if (skuKs[j].equals(nameKeyValue.getKey())) {
                                isExist = true;
                                break;
                            }
                        }
                        if (isExist) {
                            continue;
                        }
                        SkuNameKeyValue nameKeyValue = new SkuNameKeyValue();
                        nameKeyValue.setName(skuDs[j]);
                        nameKeyValue.setKey(skuKs[j]);
                        nameKeyValue.setValue(skuVs[j]);
                        listc.add(nameKeyValue);
                    } else if (j == 1) {
                        boolean isExist = false;
                        //判断是否已经被加入 如果加入将不再add
                        for (SkuNameKeyValue nameKeyValue : lists) {
                            if (skuKs[j].equals(nameKeyValue.getKey())) {
                                isExist = true;
                                break;
                            }
                        }
                        if (isExist) {
                            continue;
                        }
                        SkuNameKeyValue nameKeyValue = new SkuNameKeyValue();
                        nameKeyValue.setName(skuDs[j]);
                        nameKeyValue.setKey(skuKs[j]);
                        nameKeyValue.setValue(skuVs[j]);
                        lists.add(nameKeyValue);
                    } else {
                        continue;
                    }
                }

            }
            result.setColorList(listc);
            result.setSizeList(lists);
        } else {
            //ERP的场合
            List<HashMap> colorList = gdsMasterMapperExt.getColorList(result.getGoodsId());
            List<SkuNameKeyValue> listc = new ArrayList<SkuNameKeyValue>();
            for (int j = 0; j < colorList.size(); j++) {
                String color_code = (String) colorList.get(j).get("color_code");
                String color_name = (String) colorList.get(j).get("color_name");
                SkuNameKeyValue nameKeyValue = new SkuNameKeyValue();
                nameKeyValue.setName("颜色");
                nameKeyValue.setKey(color_code);
                nameKeyValue.setValue(color_name);
                listc.add(nameKeyValue);
            }
            result.setColorList(listc);
            List<HashMap> sizeList = gdsMasterMapperExt.getSizeList(result.getGoodsId());
            List<SkuNameKeyValue> list = new ArrayList<SkuNameKeyValue>();
            for (int j = 0; j < sizeList.size(); j++) {
                String size_code = (String) sizeList.get(j).get("size_code");
                String size_name = (String) sizeList.get(j).get("size_name");

                if(StringUtil.isEmpty(size_code)){
                    continue;
                }

                SkuNameKeyValue nameKeyValue = new SkuNameKeyValue();
                nameKeyValue.setName("尺寸");
                nameKeyValue.setKey(size_code);
                nameKeyValue.setValue(size_name);
                list.add(nameKeyValue);
            }
            result.setSizeList(list);
        }
        return result;
    }

    public JSONObject selectAllByActId(GdsMasterForm form) {
        JSONObject json = new JSONObject();

        try {
//            List<GdsMasterExt> list = gdsMasterMapperExt.selectAllByActId(form);
            GdsMasterExt gdsMasterExt = new GdsMasterExt();
            gdsMasterExt.setNeedColumns(Integer.valueOf(form.getiDisplayLength()));
            gdsMasterExt.setStartPoint(Integer.valueOf(form.getiDisplayStart()));
            gdsMasterExt.setActivityId(form.getActivityId());
            gdsMasterExt.setGoodsName(form.getGoodsName());
            gdsMasterExt.setType(form.getType());

//            gdsMasterExt.set
            List<GdsMasterExt> list = gdsMasterMapperExt.selectAllByActId(gdsMasterExt);


            int allCount = gdsMasterMapperExt.getAllDataCountByActId(gdsMasterExt);

            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);
            json.put("aaData", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;

    }

    public JSONObject getGoodsByGoodsId(String goodsId) {
        JSONObject json = new JSONObject();
        List<GdsMasterExt> list = new ArrayList<GdsMasterExt>();

        try {
            String ids = "";
            String[] goodsIdArray = goodsId.split(",");
            for (int i = 0; i < goodsIdArray.length; i++) {
                if (!StringUtils.isEmpty(goodsIdArray[i])) {
                    List<GdsMasterExt> gdsMasterExtList = gdsMasterMapperExt.getGoodsByGoodsId(goodsIdArray[i]);
                    if (gdsMasterExtList != null && gdsMasterExtList.size() > 0) {
                        list.addAll(gdsMasterExtList);
                    }
                }
            }

            json.put("data", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 通过ID删除数据
     *
     * @param data
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject checkSku(String data) {
        JSONObject json = new JSONObject();
        try {
            GdsMaster obj = JSON.parseObject(data, GdsMaster.class);
            GdsMaster gdsMaster = gdsMasterMapper.selectByPrimaryKey(obj.getGoodsId());
            int skuNo = 0;
            if (Constants.GDS_GOODS.equals(gdsMaster.getType())) {
                skuNo = gdsMasterMapperExt.checkSkuByGoods(gdsMaster);
            } else if (Constants.GDS_ERP.equals(gdsMaster.getType())) {
                skuNo = gdsMasterMapperExt.checkSkuByErp(gdsMaster);
            }
            int goodsNo = gdsMasterMapperExt.checkSkuByGoods(gdsMaster);
            json.put("skuNo", skuNo);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }


    /**
     * 根据,分割的字符串查询商品列表
     *
     * @param data
     * @return
     */
    public JSONObject getProductListBySequre(String data) {
        JSONObject json = new JSONObject();
        try {

            String[] array = data.split("/")[0].split(",");
            String memberId = "";
            if (data.split("/").length > 1) {
                memberId = data.split("/")[1];
            }
            List<GdsMasterExt> results = new ArrayList<GdsMasterExt>();
            if (array != null && array.length > 0) {
                for (int i = 0; i < array.length; i++) {
                    String productId = array[i];
                    GdsMasterExt gdsMasterExt = new GdsMasterExt();
                    gdsMasterExt.setGoodsId(productId);
                    List<GdsMasterExt> pList = gdsMasterMapperExt.selectProductsByParameterUp(gdsMasterExt);
                    //参数验证
                    if (pList.size() != 1) {
                        throw new ExceptionBusiness("参数错误!");
                    }
                    GdsMasterExt ext = pList.get(0);
//                    List<HashMap>  activityList = null;
                    List<HashMap> activityIdsList = null;
                    List<String> activity_type = new ArrayList<String>();
                    if (Constants.GDS_SUIT.equals(ext.getType())) {
//                    price  = gdsMasterMapperExt.selectSuitMinAndMaxPrice(ext);
//                        activityList = gdsMasterMapperExt.selectActivityItemsForSuit(ext.getGoodsId());
                        activityIdsList = gdsMasterMapperExt.selectActivityIdsForSuit(ext.getGoodsId());
                        //套装的场合要把价格合计
                        float minPriceTotal = 0;
                        float maxPriceTotal = 0;
                        List<GdsSuitlist> suitlists = gdsSuitlistMapperExt.selectByGoodsId(ext.getGoodsId());
                        for (GdsSuitlist gdsSuitlist : suitlists) {
                            String goodsId = gdsSuitlist.getGoodsIdSuit();
                            GdsMasterExt gdsMasExt = new GdsMasterExt();
                            gdsMasExt.setGoodsId(goodsId);
                            List<GdsMasterExt> suitList = gdsMasterMapperExt.selectProductsByParameterUp(gdsMasExt);//参数验证
                            if (suitList.size() != 1) {
                                throw new ExceptionBusiness("参数错误!");
                            }
                            minPriceTotal = minPriceTotal + Float.parseFloat(suitList.get(0).getMinPrice());
                            maxPriceTotal = maxPriceTotal + Float.parseFloat(suitList.get(0).getMaxPrice());
                        }
                        ext.setMaxPrice(String.valueOf(maxPriceTotal));
                        ext.setMinPrice(String.valueOf(minPriceTotal));
                    } else {
//                    price = gdsMasterMapperExt.selectGoodsMinAndMaxPrice(ext);
//                        activityList = gdsMasterMapperExt.selectActivityItems(ext.getGoodsId());
                        activityIdsList = gdsMasterMapperExt.selectActivityIds(ext.getGoodsId());
                    }
//                    for(int j = 0; j<activityList.size(); j++){
//                        activity_type.add((String)activityList.get(j).get("actition_type"));
//                    }
//                    //活动标签获取
//                    ext.setActivityTags(activity_type);

                    //组成获取优惠价格的form
                    List<ActMasterForm> activityFormList = new ArrayList<ActMasterForm>();
                    //获取最小优惠
                    for (int j = 0; j < activityIdsList.size(); j++) {
                        ActMasterForm form = new ActMasterForm();
                        form.setActivityId((String) activityIdsList.get(j).get("activity_id"));
                        form.setOriginPrice(Float.parseFloat(ext.getMinPrice()));
                        activityFormList.add(form);
                    }
                    JSONObject activityTypeList = actMasterService.filterActivityByMember(activityFormList, memberId);

                    List<ActMasterForm> minList = (ArrayList<ActMasterForm>) activityTypeList.get("data");
                    if (minList != null && minList.size() != 0) {
                        for (ActMasterForm form : minList) {
                            activity_type.add(form.getActivityType());
                        }
                    }
                    //活动标签获取
                    ext.setActivityTags(activity_type);

                    //获取商品图片的第一张
                    if (!StringUtils.isEmpty(ext.getImageUrlJson())) {
                        String imageUrlJson = ext.getImageUrlJson().split(",")[0];
                        ext.setImageUrlJson(imageUrlJson);
                    }

                    //返回给前台的非优惠信息情况
                    if (ext.getMinPrice() != null && ext.getMaxPrice() != null && ext.getMinPrice().equals(ext.getMaxPrice())) {
                        ext.setMinAndMaxPrice("¥" + ext.getMinPrice());
                    }

                    if (ext.getMinPrice() == null) {
                        ext.setMinAndMaxPrice("¥0.00");
                    }

                    results.add(ext);

                }
            }

            json.put("results", results);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }


    /**
     * 根据,分割的字符串查询商品列表
     *
     * @param data
     * @return
     */
    @Cacheable(value="${activity_goods_list_catch_name}", key="#data")
    public JSONObject getProductListByActivityId(String data) {
        JSONObject json = new JSONObject();
        try {
            //转换数据格式
            GdsMasterExt gdsMasterExt = (GdsMasterExt) JSON.parseObject(data, GdsMasterExt.class);
            if (StringUtil.isEmpty(gdsMasterExt.getActivityId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            ActMasterForm form = new ActMasterForm();
            form.setActivityId(gdsMasterExt.getActivityId());
            ActMasterForm activity = actMasterMapperExt.selectById(form);
            if (activity == null) {
                throw new ExceptionErrorData("活动不存在");
            }
            List<ActGoodsForm> goodsList = null;
            ActGoods goods = new ActGoods();
            goods.setActivityId(form.getActivityId());
            // "10";"全部"
            // "20";"按分类"
            // "30";"按品牌"
            // "40";"商品"
            // "50";"SKU"
//            public static final String ALL = "10";
//            public static final String TYPE = "20";
//            public static final String BRAND = "30";
//            public static final String GOODS = "40";
//            public static final String SKU = "50";
            if ("10".equals(activity.getGoodsType())) {
                //全部商品
            } else if ("20".equals(activity.getGoodsType())) {
                // 按分类
                goodsList = actGoodsMapperExt.selectGoodsTypeByActivityId(goods);
            } else if ("30".equals(activity.getGoodsType())) {
                // 按品牌
                goodsList = actGoodsMapperExt.selectGoodsBrandByActivityId(goods);
            } else if ("40".equals(activity.getGoodsType())) {
                // 按商品
                goodsList = actGoodsMapperExt.selectGoodsByActivityId(goods);
            } else if ("50".equals(activity.getGoodsType())) {
                // 按SKU
                goodsList = actGoodsMapperExt.selectSkuByActivityIdUp(goods);
            }

            //in 条件做成
            String searchKey = "";
            if (!"10".equals(activity.getGoodsType())) {
                for (ActGoodsForm actGoodsForm : goodsList) {
                    searchKey = searchKey + "," + actGoodsForm.getGoodsId();
                }
                if (searchKey.startsWith(",")) {
                    searchKey = searchKey.substring(1, searchKey.length());
                }
                if (searchKey.endsWith(",")) {
                    searchKey = searchKey.substring(0, searchKey.length() - 1);
                }

                if (!StringUtil.isEmpty(searchKey)) {
                    gdsMasterExt.setActivitySearchKey(searchKey);
                    gdsMasterExt.setActivityType(activity.getGoodsType());
                }
            }

            if (!StringUtil.isEmpty(gdsMasterExt.getSizeCode())) {
                gdsMasterExt.setSizeTypeCode(gdsMasterExt.getSizeCode().split("_")[0]);
                gdsMasterExt.setSizeCode(gdsMasterExt.getSizeCode().split("_")[1]);
            }

            List<GdsMasterExt> sizeCodeAndNameList = null;
            //在这块判断传过来的分类条件是否在分类表里边存在呢
            //如果存在说明是按照分类查询
            //如果不存在说明是按照品牌系列查询
            if (!StringUtil.isEmpty(gdsMasterExt.getFirstGoodsTypeId())) {

                String[] array = gdsMasterExt.getFirstGoodsTypeId().split("_");
                String goodsTypeId = gdsMasterExt.getFirstGoodsTypeId();
                if (array.length > 1) {
                    goodsTypeId = array[0];
                }
                GdsType gdsType = gdsTypeMapper.selectByPrimaryKey(goodsTypeId);

                if (gdsType == null) {
                    gdsMasterExt.setFirstGoodsBrandTypeId(gdsMasterExt.getFirstGoodsTypeId());
                    gdsMasterExt.setFirstGoodsTypeId(null);
                    sizeCodeAndNameList = gdsMasterMapperExt.getSizeCodeListByfirstBrandErp(gdsMasterExt.getFirstGoodsBrandTypeId());
                } else {
                    sizeCodeAndNameList = gdsMasterMapperExt.getSizeCodeListByfirstErp(gdsMasterExt.getFirstGoodsTypeId());
                }
            }

            if(sizeCodeAndNameList == null
                    || sizeCodeAndNameList.size() == 0){
                GdsMasterExt ext = new GdsMasterExt();
                ext.setGoodsIds(searchKey);
                sizeCodeAndNameList = gdsMasterMapperExt.getSizeCodeListByGoodsIdsErp(ext);
            }

            //返回前台的尺码列表
            List<GdsMasterExt> sizeList = new ArrayList<GdsMasterExt>();
            if (sizeCodeAndNameList != null) {
                for (GdsMasterExt ext : sizeCodeAndNameList) {
                    GdsMasterExt gdsMasterExt1 = new GdsMasterExt();
                    gdsMasterExt1.setSizeCode(ext.getSizeTypeCode() + "_" + ext.getSizeCode());
                    gdsMasterExt1.setSizeName(ext.getSizeName());
                    sizeList.add(gdsMasterExt1);
                }
            }


            if (!StringUtil.isEmpty(gdsMasterExt.getGoodsTypeId())) {
                GdsType gdsType = gdsTypeMapper.selectByPrimaryKey(gdsMasterExt.getGoodsTypeId());
                if (gdsType == null) {
                    gdsMasterExt.setGoodsBrandTypeId(gdsMasterExt.getGoodsTypeId());
                    gdsMasterExt.setGoodsTypeId(null);
                }
            }
            //转换数据格式
            if (gdsMasterExt.getCurrentPage() != 0 && gdsMasterExt.getPageSize() != 0) {
                //获取总页数
                int allCount = gdsMasterMapperExt.selectProductsByParameterCountUp(gdsMasterExt);
                int devi = allCount / gdsMasterExt.getPageSize();
                int count = 0;
                if (allCount % gdsMasterExt.getPageSize() == 0) {
                    count = devi;
                } else {
                    count = devi + 1;
                }
                json.put("allCount", count);
                gdsMasterExt.setDisplayItemCount(gdsMasterExt.getPageSize() * (gdsMasterExt.getCurrentPage() - 1));
            }

            List<GdsMasterExt> pList = gdsMasterMapperExt.selectProductsByParameterUp(gdsMasterExt);
            //遍历获取价格信息,活动标签
//            for (GdsMasterExt ext : pList) {
//                //价格信息
////                GdsMasterExt price = null;
//                //活动信息
////                List<HashMap>  activityList = null;
//                List<HashMap> activityIdsList = null;
//                List<String> activity_type = new ArrayList<String>();
//                if (Constants.GDS_SUIT.equals(ext.getType())) {
////                    price  = gdsMasterMapperExt.selectSuitMinAndMaxPrice(ext);
////                    activityList = gdsMasterMapperExt.selectActivityItemsForSuit(ext.getGoodsId());
//                    activityIdsList = gdsMasterMapperExt.selectActivityIdsForSuit(ext.getGoodsId());
//                    //套装的场合要把价格合计
//                    float minPriceTotal = 0;
//                    float maxPriceTotal = 0;
//                    List<GdsSuitlist> suitlists = gdsSuitlistMapperExt.selectByGoodsId(ext.getGoodsId());
//                    for (GdsSuitlist gdsSuitlist : suitlists) {
//                        String goodsId = gdsSuitlist.getGoodsIdSuit();
//                        GdsMasterExt gdsMasExt = new GdsMasterExt();
//                        gdsMasExt.setGoodsId(goodsId);
//                        List<GdsMasterExt> suitList = gdsMasterMapperExt.selectProductsByParameterUp(gdsMasExt);//参数验证
//                        if (suitList.size() != 1) {
//                            throw new ExceptionBusiness("参数错误!");
//                        }
//                        minPriceTotal = minPriceTotal + Float.parseFloat(suitList.get(0).getMinPrice());
//                        maxPriceTotal = maxPriceTotal + Float.parseFloat(suitList.get(0).getMaxPrice());
//                    }
//                    ext.setMaxPrice(String.valueOf(maxPriceTotal));
//                    ext.setMinPrice(String.valueOf(minPriceTotal));
//                } else {
////                    price = gdsMasterMapperExt.selectGoodsMinAndMaxPrice(ext);
////                    activityList = gdsMasterMapperExt.selectActivityItems(ext.getGoodsId());
//                    activityIdsList = gdsMasterMapperExt.selectActivityIds(ext.getGoodsId());
//                }
////                for(int i = 0; i<activityList.size(); i++){
////                    activity_type.add((String)activityList.get(i).get("actition_type"));
////                }
////                //活动标签获取
////                ext.setActivityTags(activity_type);
//
////                //组成获取优惠价格的form
////                List<ActMasterForm> activityFormList = new ArrayList<ActMasterForm>();
////                //获取最小优惠
////                for (int j = 0; j < activityIdsList.size(); j++) {
////                    ActMasterForm actMasterForm = new ActMasterForm();
////                    actMasterForm.setActivityId((String) activityIdsList.get(j).get("activity_id"));
////                    actMasterForm.setOriginPrice(Float.parseFloat(ext.getMinPrice()));
////                    activityFormList.add(actMasterForm);
////                }
////                JSONObject activityTypeList = actMasterService.filterActivityByMember(activityFormList, gdsMasterExt.getMemberId());
////
////                List<ActMasterForm> minList = (ArrayList<ActMasterForm>) activityTypeList.get("data");
////                if (minList != null && minList.size() != 0) {
////                    for (ActMasterForm actMasterForm : minList) {
////                        activity_type.add(actMasterForm.getActivityType());
////                    }
////                }
////                //活动标签获取
////                ext.setActivityTags(activity_type);
//
//
//                //组成获取优惠价格的form
//                List<ActMasterForm> activityFormList = new ArrayList<ActMasterForm>();
//
//                //获取最小优惠
//                for (int i = 0; i < activityIdsList.size(); i++) {
//                    ActMasterForm form1 = new ActMasterForm();
//                    form1.setActivityId((String) activityIdsList.get(i).get("activity_id"));
//                    form1.setOriginPrice(Float.parseFloat(ext.getMinPrice()));
//                    activityFormList.add(form1);
//                }
//                JSONObject jsonObjectMin = actMasterService.getNewPricesByActivity(activityFormList, gdsMasterExt.getMemberId(), ext.getGoodsId());
//                //最小的优惠价钱
//                float minNewPrice = 0;
//                List<ActMasterForm> minList = (ArrayList<ActMasterForm>) jsonObjectMin.get("data");
//                if (minList != null && minList.size() != 0) {
//                    minNewPrice = minList.get(0).getNewPrice();
//                    ext.setActivityName(minList.get(0).getActivityName());
//                }
//
//                //获取最大优惠
//                activityFormList.clear();
//                for (int i = 0; i < activityIdsList.size(); i++) {
//                    ActMasterForm form1 = new ActMasterForm();
//                    form1.setActivityId((String) activityIdsList.get(i).get("activity_id"));
//                    form1.setOriginPrice(Float.parseFloat(ext.getMaxPrice()));
//                    activityFormList.add(form1);
//                }
//                JSONObject jsonObjectMax = actMasterService.getNewPricesByActivity(activityFormList, gdsMasterExt.getMemberId(), ext.getGoodsId());
//                //最大的优惠价钱
//                float maxNewPrice = 0;
//                List<ActMasterForm> maxList = (ArrayList<ActMasterForm>) jsonObjectMax.get("data");
//                if (maxList != null && maxList.size() != 0) {
//                    maxNewPrice = maxList.get(0).getNewPrice();
//                    ext.setActivityName(maxList.get(0).getActivityName());
//                }
//                //返回给前台的优惠信息情况
//                if (minNewPrice == 0 || maxNewPrice == 0) {
//                    ext.setMinAndMaxPriceActivity(null);
//                } else if (minNewPrice == maxNewPrice) {
//                    ext.setMinAndMaxPriceActivity("¥" + minNewPrice);
//                } else {
//                    ext.setMinAndMaxPriceActivity("¥" + minNewPrice + "~" + "¥" + maxNewPrice);
//                }
//
//                //获取商品图片的第一张
//                if (ext.getImageUrlJson().startsWith(",")) {
//                    ext.setImageUrlJson(ext.getImageUrlJson().substring(1, ext.getImageUrlJson().length()));
//                }
//                if (ext.getImageUrlJson().endsWith(",")) {
//                    ext.setImageUrlJson(ext.getImageUrlJson().substring(0, ext.getImageUrlJson().length() - 1));
//                }
//                if (!StringUtils.isEmpty(ext.getImageUrlJson())) {
//                    String imageUrlJson = ext.getImageUrlJson().split(",")[0];
//                    ext.setImageUrlJson(imageUrlJson);
//                }
//
//                //返回给前台的非优惠信息情况
//                if (ext.getMinPrice() != null && ext.getMaxPrice() != null && ext.getMinPrice().equals(ext.getMaxPrice())) {
//                    ext.setMinAndMaxPrice("¥" + ext.getMinPrice());
//                }
//
//                if (ext.getMinPrice() == null) {
//                    ext.setMinAndMaxPrice("¥0.00");
//                }
//            }

            json.put("sizeList", sizeList);
            json.put("results", pList);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    /**
     * 获取商品品牌列表
     *
     * @return
     */
    public JSONObject selectAllByGoodsTypeId(GdsMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            GdsMasterExt ext = new GdsMasterExt();
            ext.setShopId(Constants.ORGID);
            ext.setGoodsName(form.getGoodsName());
            ext.setGoodsCode(form.getGoodsCode());
            ext.setType(form.getType());
            ext.setCmsGoodsTypeId(form.getCmsGoodsTypeId());
            ext.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
            ext.setStartPoint(Integer.parseInt(form.getiDisplayStart()));
            ext.setIsOnsell(form.getIsOnsell());
            ext.setMaintainStatus(form.getMaintainStatus());

            //数据库检索 -- 过滤数据
            List<GdsMasterExt> list = gdsMasterMapperExt.selectAll(ext);
            //获取商品的总数
            int allCount = gdsMasterMapperExt.getAllDataCount(ext);
            json.put("aaData", list);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);


            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;

    }


    //活动种类列表获取
    @Override
    @Transactional(readOnly = false)
    public JSONObject activityTypeListService(String date){

        JSONObject json = new JSONObject();

        try{
            JSONObject jsonObject = JSON.parseObject(date);
            String firstGoodsTypeId = (String)jsonObject.get("firstGoodsTypeId");
            String memberId = (String) jsonObject.get("memberId");
            ActMasterForm actMasterForm = new ActMasterForm();
            actMasterForm.setShopId(Constants.ORGID);
            List<ActMasterForm> list = actMasterMapperExt.selectAllActList(actMasterForm);

            List<ActMasterForm> activities = new ArrayList<ActMasterForm>();
            for (int i = 0; i < list.size(); i++) {
                String activityId = list.get(i).getActivityId();
                GdsMasterExt gdsMasterExt = new GdsMasterExt();
                gdsMasterExt.setActivityId(activityId);
                gdsMasterExt.setFirstGoodsTypeId(firstGoodsTypeId);
                ActMasterForm form = new ActMasterForm();
                form.setActivityId(gdsMasterExt.getActivityId());
                ActMasterForm activity = actMasterMapperExt.selectById(form);
                if (activity == null) {
                    throw new ExceptionErrorData("活动不存在");
                }
                List<ActGoodsForm> goodsList = null;
                ActGoods goods = new ActGoods();
                goods.setActivityId(form.getActivityId());
                if ("10".equals(activity.getGoodsType())) {
                    //全部商品
                } else if ("20".equals(activity.getGoodsType())) {
                    // 按分类
                    goodsList = actGoodsMapperExt.selectGoodsTypeByActivityId(goods);
                } else if ("30".equals(activity.getGoodsType())) {
                    // 按品牌
                    goodsList = actGoodsMapperExt.selectGoodsBrandByActivityId(goods);
                } else if ("40".equals(activity.getGoodsType())) {
                    // 按商品
                    goodsList = actGoodsMapperExt.selectGoodsByActivityId(goods);
                } else if ("50".equals(activity.getGoodsType())) {
                    // 按SKU
                    goodsList = actGoodsMapperExt.selectSkuByActivityIdUp(goods);
                }

                //in 条件做成
                String searchKey = "";
                if (!"10".equals(activity.getGoodsType())) {
                    for (ActGoodsForm actGoodsForm : goodsList) {
                        searchKey = searchKey + "," + actGoodsForm.getGoodsId();
                    }
                    if (searchKey.startsWith(",")) {
                        searchKey = searchKey.substring(1, searchKey.length());
                    }
                    if (searchKey.endsWith(",")) {
                        searchKey = searchKey.substring(0, searchKey.length() - 1);
                    }

                    if (!StringUtil.isEmpty(searchKey)) {
                        gdsMasterExt.setActivitySearchKey(searchKey);
                        gdsMasterExt.setActivityType(activity.getGoodsType());
                    }
                }

                if (!StringUtil.isEmpty(gdsMasterExt.getSizeCode())) {
                    gdsMasterExt.setSizeTypeCode(gdsMasterExt.getSizeCode().split("_")[0]);
                    gdsMasterExt.setSizeCode(gdsMasterExt.getSizeCode().split("_")[1]);
                }
                List<GdsMasterExt> pList = gdsMasterMapperExt.selectProductsByParameterUp(gdsMasterExt);
                if (pList!=null && pList.size()!=0){
                    ActMasterForm activity1 = list.get(i);
                    if (activity1 != null && isContainsMember(activity1, memberId)) {
                        activities.add(activity1);
                    }
                }
            }
            json.put("data", activities);
            json.put("resultCode", Constants.NORMAL);
        }catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    //活动种类列表获取(用户下所有)
    @Override
    @Transactional(readOnly = false)
    public JSONObject activitysListService(String memberId){

        JSONObject json = new JSONObject();

        try{
            JSONObject jsonObject = JSON.parseObject(memberId);
            memberId = (String) jsonObject.get("memberId");
            ActMasterForm form = new ActMasterForm();
            form.setShopId(Constants.ORGID);
            List<ActMasterForm> list = actMasterMapperExt.selectAllActListByMember(form);

            List<ActMasterForm> activities = new ArrayList<ActMasterForm>();
            for (int i = 0; i < list.size(); i++) {
                ActMasterForm activity = list.get(i);
                if (activity != null && isContainsMember(activity, memberId)) {
                    activities.add(activity);
                }
            }
            json.put("data", activities);
            json.put("resultCode", Constants.NORMAL);
        }catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }




    /**
     * 获取秒杀活动时间段
     *
     * @param data
     * @return
     */
    @Override
    public JSONObject getSecKillActivityTimes(String data) {
        JSONObject json = new JSONObject();

        try{
            String memberId = null;
            if(!StringUtil.isEmpty(data)){
                JSONObject jsonObject = JSON.parseObject(data);
                memberId = (String) jsonObject.get("memberId");
            }

            Date current = new Date();

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(current);
            calendar.add(calendar.DATE,1);
            Date tomorrow = calendar.getTime();

            ActMasterForm form = new ActMasterForm();
            form.setShopId(Constants.ORGID);
            form.setEndTime(current);// 当前时间前截止
            form.setStartTime(tomorrow);// 24小时内即将开始
            // 根据当前时间获取进行中的活动及即将开始的活动
            List<ActMasterForm> list = actMasterMapperExt.selectSecKillActivityList(form);

            List<ActMasterForm> activities = new ArrayList<ActMasterForm>();
            for (ActMasterForm activity : list) {
                if (activity != null && isContainsMember(activity, memberId)) {
                    activities.add(activity);
                }
            }

            List<String> times = new ArrayList<>();
            List<Map<String, String>> allTimes = new ArrayList<>();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat ydf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateStr = sdf.format(current);
            String tomorrowDateStr = sdf.format(tomorrow);

            String lastTime = currentDateStr + " 23:59:59";
            Date lastDate = formater.parse(lastTime);

            List<ActMasterForm> temps = new ArrayList<>();
            for(ActMasterForm activity : activities){
                if(activity.getStartTime().after(current) && lastDate.before(activity.getStartTime())){
                    // 24小时内即将开始的秒杀活动
                    temps.add(activity);
                } else {

                    String startTimeShow = df.format(activity.getStartTime());
                    String startTime = currentDateStr + " " +  startTimeShow;
                    String endTime = currentDateStr + " " +  df.format(activity.getEndTime());

                    boolean startedFlag = false;
                    if(ydf.parse(endTime).before(current)){
                        // 已结束的活动计入后一天
                        temps.add(activity);
                        continue;
                    } else if(ydf.parse(startTime).before(current)){
                        // 进行中
                        startedFlag = true;
                    }

                    if(!times.contains(startTime)){
                        Map<String, String> resultItem = new HashMap<>();
                        resultItem.put("started", startedFlag ? "0" : "1");
                        resultItem.put("startTime", startTime);
                        resultItem.put("startTimeSec", ydf.parse(startTime).getTime() + "");
                        resultItem.put("endTime", endTime);
                        resultItem.put("endTimeSec", ydf.parse(endTime).getTime() + "");
                        resultItem.put("currentTime", current.getTime() + "");
                        resultItem.put("startTimeShow", startTimeShow);
                        allTimes.add(resultItem);
                        times.add(startTime);
                    }
                }
            }

            for(ActMasterForm activity : temps){
                String startTimeShow = df.format(activity.getStartTime());
                String time = tomorrowDateStr + " " + startTimeShow;
                String endTime = currentDateStr + " " +  df.format(activity.getEndTime());
                if(!times.contains(time)){
                    Map<String, String> resultItem = new HashMap<>();
                    resultItem.put("started", "1");
                    resultItem.put("startTime", time);
                    resultItem.put("startTimeSec", ydf.parse(time).getTime() + "");
                    resultItem.put("endTime", endTime);
                    resultItem.put("endTimeSec", ydf.parse(endTime).getTime() + "");
                    resultItem.put("currentTime", current.getTime() + "");
                    resultItem.put("startTimeShow", "明天"+ startTimeShow);
                    allTimes.add(resultItem);
                    times.add(time);
                }
            }

            Collections.sort(allTimes, new Comparator<Map<String, String>>() {
                final String cKey = "startTime";
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                @Override
                public int compare(Map<String, String> o1, Map<String, String> o2) {
                    try {
                        return (format.parse(o1.get(cKey))).compareTo(format.parse(o2.get(cKey)));
                    } catch (ParseException e) {
                        return 0;
                    }
                }
            });

            if(allTimes !=null && allTimes.size() > 4){
                for(int i=allTimes.size()-1; i>=4; i--){
                    allTimes.remove(i);
                }
            }

            json.put("data", allTimes);
            json.put("resultCode", Constants.NORMAL);
        }catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 获取秒杀活动商品列表
     * @param data
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public JSONObject getSecKillProductList(String data){
        JSONObject json = new JSONObject();

        try{
            JSONObject jsonObject = JSON.parseObject(data);
            String memberId = (String) jsonObject.get("memberId");
            String activityTime = (String) jsonObject.get("activityTime");
            activityTime = activityTime + ":00";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            int iDisplayStart = jsonObject.getIntValue("page");
            int iDisplayLength = jsonObject.getIntValue("displayLength");
            ActMasterForm form = new ActMasterForm();
            form.setShopId(Constants.ORGID);
            Date startTime = sdf.parse(activityTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            form.setStartTime(startTime);
            form.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            form.setMinute(calendar.get(Calendar.MINUTE));
            List<ActMasterForm> list = actMasterMapperExt.selectSecKillActivityByTime(form);

            List<ActMasterForm> activities = new ArrayList<ActMasterForm>();
            for (int i = 0; i < list.size(); i++) {
                ActMasterForm activity = list.get(i);
                if (activity != null && isContainsMember(activity, memberId)) {
                    activities.add(activity);
                }
            }

            if(activities == null || activities.size() == 0){
                json.put("data", null);
                json.put("resultCode", Constants.NORMAL);
                return json;
            }

            String activityIds = "";
            for (int i = 0; i < activities.size() - 1; i++) {
                activityIds += activities.get(i).getActivityId() + ",";
            }
            activityIds += activities.get(list.size() - 1).getActivityId() ;
            ActMasterForm actForm = new ActMasterForm();
            actForm.setSearchIds(activityIds);

            if(iDisplayStart > 0){
                iDisplayStart = iDisplayStart * iDisplayLength;
            }
            actForm.setiDisplayStart(iDisplayStart);
            actForm.setiDisplayLength(iDisplayLength);
            form.setShopId(Constants.ORGID);
            List<ActGoods> actGoodsList = actGoodsMapperExt.selectSecGoodsByActivity(actForm);
            List<GdsMasterExt> goodsList = new ArrayList<>();
            int allCount = actGoodsMapperExt.countSecGoodsByActivity(actForm);
            int devi = allCount / iDisplayLength;
            int count = 0;
            if (allCount % iDisplayLength == 0) {
                count = devi;
            } else {
                count = devi + 1;
            }
            for(ActGoods actGoods : actGoodsList){
                GdsMaster gdsMaster = new GdsMaster();
                gdsMaster.setGoodsId(actGoods.getGoodsId());
                GdsMasterExt item = gdsMasterMapperExt.selectGoodsMinAndMaxPrice(gdsMaster);
                if(null == item){
                    json.put("resultCode", Constants.FAIL);
                }

                ActGoods searchInfo = new ActGoods();
                searchInfo.setGoodsId(actGoods.getGoodsId());
                searchInfo.setActivityId(activityIds);
                ActGoods actInfo = actGoodsMapperExt.selectSecGoodsInfo(searchInfo);
                item.setActInfo(actInfo);

                //获取商品图片的第一张
                if (item.getImageUrlJson().startsWith(",")) {
                    item.setImageUrlJson(item.getImageUrlJson().substring(1, item.getImageUrlJson().length()));
                }
                if (item.getImageUrlJson().endsWith(",")) {
                    item.setImageUrlJson(item.getImageUrlJson().substring(0, item.getImageUrlJson().length() - 1));
                }
                if (!StringUtils.isEmpty(item.getImageUrlJson())) {
                    String imageUrlJson = item.getImageUrlJson().split(",")[0];
                    item.setImageUrlJson(imageUrlJson);
                }

                goodsList.add(item);
            }

            json.put("data", goodsList);
            json.put("iTotalRecords", count);
            json.put("resultCode", Constants.NORMAL);
        }catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    private boolean isContainsMember(ActMasterForm activity, String memberId) {
        boolean isContainsMember = false;

        if (memberId == null || memberId.trim().length() == 0) {
            if (net.dlyt.qyds.web.service.common.ComCode.ActivityMemberType.ALL.equals(activity.getMemberType())) {
                //全部会员
                return true;
            } else {
                return false;
            }
        }

        if (net.dlyt.qyds.web.service.common.ComCode.ActivityMemberType.ALL.equals(activity.getMemberType())) {
            // 判断活动用户是否包含该用户
            //全部会员
            isContainsMember = true;
        } else if (net.dlyt.qyds.web.service.common.ComCode.ActivityMemberType.MEMBER_GROUP.equals(activity.getMemberType())) {
            ActMember member = new ActMember();
            member.setActivityId(activity.getActivityId());
            // 按分类
            List<ActMemberForm> memberGroupList = actMemberMapperExt.selectMemberGroupByActivityId(member);
            for (ActMemberForm form : memberGroupList) {
                // 判断memberId是否存在于某个组中
                MmbGroupMember record = new MmbGroupMember();
                record.setGroupId(form.getMemberId());
                record.setMemberId(memberId);
                List<MmbGroupMemberExt> memberList = mmbGroupMemberMapperExt.select(record);
                if (memberList != null && memberList.size() > 0) {
                    isContainsMember = true;
                    break;
                }
            }
        } else if (net.dlyt.qyds.web.service.common.ComCode.ActivityMemberType.MEMBER_LEVEL.equals(activity.getMemberType())) {
            ActMember member = new ActMember();
            member.setActivityId(activity.getActivityId());
            // 按分类
            List<ActMemberForm> memberLevelList = actMemberMapperExt.selectMemberLevelByActivityId(member);
            for (ActMemberForm form : memberLevelList) {
                // 判断memberId是否存在于某个级别中
                MmbMasterExt record = new MmbMasterExt();
                record.setMemberId(memberId);
                record.setMemberLevelId(form.getMemberId());
                record.setDeleted(Constants.DELETED_NO);
                MmbMasterExt memberList = mmbMasterMapperExt.selectBySelective(record);
                if (memberList != null) {
                    isContainsMember = true;
                    break;
                }
            }
        }
        return isContainsMember;
    }

    @Override
    @Transactional(readOnly = false)
    public JSONObject goodsOrder(String data) {
        JSONObject json = new JSONObject();

        try{
            JSONObject dataJson = JSON.parseObject(data);
            GdsSchedule form = JSON.toJavaObject(dataJson,GdsSchedule.class);
            form.setDeleted(Constants.DELETED_NO);
            String randomUUID = UUID.randomUUID().toString();
            form.setScheduleId(randomUUID);
            form.setInsertTime(new Date());
            form.setUpdateTime(new Date());
            gdsScheduleMapper.insertSelective(form);

            json.put("resultCode", Constants.NORMAL);
        }catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;

    }

    @Override
    public JSONObject getGoodsOrderList(GdsMasterForm form) {
        JSONObject json = new JSONObject();

        try{
            GdsMasterExt ext = new GdsMasterExt();
            ext.setGoodsName(form.getGoodsName());
            ext.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
            ext.setStartPoint(Integer.parseInt(form.getiDisplayStart()));
            ext.setMaintainStatus(form.getMaintainStatus());
            ext.setUserName(form.getUserName());
            ext.setTelephone(form.getTelephone());

            List<GdsScheduleExt> result = gdsScheduleMapperExt.getGoodsOrderList(ext);
            //获取商品的总数
            int allCount = gdsScheduleMapperExt.getAllDataCount(ext);
            json.put("aaData", result);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);


            json.put("resultCode", Constants.NORMAL);
        }catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;

    }

    @Override
    public JSONObject getGiftDetailByCode(String data) {
        JSONObject json = new JSONObject();
        try {
            //转换数据格式
            GdsMasterExt gdsMasterExt = (GdsMasterExt) JSON.parseObject(data, GdsMasterExt.class);
            List<GdsMasterExt> pList = gdsMasterMapperExt.selectProductsByParameterForSelectGift(gdsMasterExt);

            //参数验证
            if (pList.size() != 1) {
                throw new ExceptionBusiness("参数错误!");
            }

            // 商品的详细信息
            GdsMasterExt result = pList.get(0);
            result.setMemberId(gdsMasterExt.getMemberId());
            //返回给前台的非优惠信息情况
            //获取商品图片的第一张
            if (!StringUtils.isEmpty(result.getImageUrlJson())) {
                result.setImageUrlJsonPc(result.getImageUrlJson().split(","));
                String imageUrlJson = result.getImageUrlJson().split(",")[0];
                result.setImageUrlJson(imageUrlJson);
            }

            List<ViewOnsellSku> viewOnsellSkuList = new ArrayList<ViewOnsellSku>();
            //套装的场合 SKU信息获取
            if (Constants.GDS_SUIT.equals(result.getType())) {
                //根据商品ID获取组成该套餐的商品ID列表
                List<GdsSuitlist> suitlists = gdsSuitlistMapperExt.selectByGoodsId(result.getGoodsId());
                List<GdsMasterExt> regoodsList = new ArrayList<GdsMasterExt>();
                for (GdsSuitlist suitlist : suitlists) {
                    String goodsId = suitlist.getGoodsIdSuit();
                    GdsMasterExt ext = new GdsMasterExt();
                    ext.setGoodsId(goodsId);
                    List<GdsMasterExt> goodsList = gdsMasterMapperExt.selectProductsByParameter(ext);
                    //组成套装的商品信息
                    ext = goodsList.get(0);
                    ext = this.getColorAndSizeList(ext);
                    viewOnsellSkuList = new ArrayList<ViewOnsellSku>();
                    ext.setSkulist(this.getSkuInfo(viewOnsellSkuList, ext, Constants.GDS_SUIT));
                    regoodsList.add(ext);
                }
                result.setGoodsList(regoodsList);
            } else {
                result = this.getColorAndSizeList(result);
                result.setSkulist(this.getSkuInfo(viewOnsellSkuList, result, null));

            }
            json.put("results", result);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }


    /**
     * 根据,分割的字符串查询商品列表
     *
     * @param data
     * @return
     */
    @Cacheable(value="${activity_goods_list_catch_name}", key="#data")
    public JSONObject getProductListByActivityIds(String data) {
        JSONObject json = new JSONObject();
        try {
            //转换数据格式
            GdsMasterExt gdsMasterExt = (GdsMasterExt) JSON.parseObject(data, GdsMasterExt.class);
            if (StringUtil.isEmpty(gdsMasterExt.getActivityIds())) {
                throw new ExceptionErrorParam("缺少参数");
            }

            String activityIds = gdsMasterExt.getActivityIds();
            String[] ids = activityIds.split(",");
            List<ActGoodsForm> goodsListAll = new ArrayList<ActGoodsForm>();
            for(int i = 0; i < ids.length; i++){
                String activityId = ids[i];
                ActMasterForm form = new ActMasterForm();
                form.setActivityId(activityId);
                ActMasterForm activity = actMasterMapperExt.selectById(form);
                if (activity == null) {
                    throw new ExceptionErrorData("活动不存在");
                }

                List<ActGoodsForm> goodsList = null;
                ActGoods goods = new ActGoods();
                goods.setActivityId(form.getActivityId());

                if ("10".equals(activity.getGoodsType())) {
                    //全部商品
                } else if ("20".equals(activity.getGoodsType())) {
                    // 按分类
                    goodsList = actGoodsMapperExt.selectGoodsTypeByActivityId(goods);
                } else if ("30".equals(activity.getGoodsType())) {
                    // 按品牌
                    goodsList = actGoodsMapperExt.selectGoodsBrandByActivityId(goods);
                } else if ("40".equals(activity.getGoodsType())) {
                    // 按商品
                    goodsList = actGoodsMapperExt.selectGoodsByActivityId(goods);
                } else if ("50".equals(activity.getGoodsType())) {
                    // 按SKU
                    goodsList = actGoodsMapperExt.selectSkuByActivityIdUp(goods);
                }
                goodsListAll.addAll(goodsList);
            }



            // "10";"全部"
            // "20";"按分类"
            // "30";"按品牌"
            // "40";"商品"
            // "50";"SKU"
//            public static final String ALL = "10";
//            public static final String TYPE = "20";
//            public static final String BRAND = "30";
//            public static final String GOODS = "40";
//            public static final String SKU = "50";


            //in 条件做成
            String searchKey = "";
            for (ActGoodsForm actGoodsForm : goodsListAll) {
                searchKey = searchKey + "," + actGoodsForm.getGoodsId();
            }
            if (searchKey.startsWith(",")) {
                searchKey = searchKey.substring(1, searchKey.length());
            }
            if (searchKey.endsWith(",")) {
                searchKey = searchKey.substring(0, searchKey.length() - 1);
            }

            if (!StringUtil.isEmpty(searchKey)) {
                gdsMasterExt.setActivitySearchKey(searchKey);
                gdsMasterExt.setActivityType("60");
            }

            if (!StringUtil.isEmpty(gdsMasterExt.getSizeCode())) {
                gdsMasterExt.setSizeTypeCode(gdsMasterExt.getSizeCode().split("_")[0]);
                gdsMasterExt.setSizeCode(gdsMasterExt.getSizeCode().split("_")[1]);
            }

            List<GdsMasterExt> sizeCodeAndNameList = null;
            //在这块判断传过来的分类条件是否在分类表里边存在呢
            //如果存在说明是按照分类查询
            //如果不存在说明是按照品牌系列查询
            if (!StringUtil.isEmpty(gdsMasterExt.getFirstGoodsTypeId())) {

                String[] array = gdsMasterExt.getFirstGoodsTypeId().split("_");
                String goodsTypeId = gdsMasterExt.getFirstGoodsTypeId();
                if (array.length > 1) {
                    goodsTypeId = array[0];
                }
                GdsType gdsType = gdsTypeMapper.selectByPrimaryKey(goodsTypeId);

                if (gdsType == null) {
                    gdsMasterExt.setFirstGoodsBrandTypeId(gdsMasterExt.getFirstGoodsTypeId());
                    gdsMasterExt.setFirstGoodsTypeId(null);
                    sizeCodeAndNameList = gdsMasterMapperExt.getSizeCodeListByfirstBrandErp(gdsMasterExt.getFirstGoodsBrandTypeId());
                } else {
                    sizeCodeAndNameList = gdsMasterMapperExt.getSizeCodeListByfirstErp(gdsMasterExt.getFirstGoodsTypeId());
                }
            }

            if(sizeCodeAndNameList == null
                    || sizeCodeAndNameList.size() == 0){
                GdsMasterExt ext = new GdsMasterExt();
                ext.setGoodsIds(searchKey);
                sizeCodeAndNameList = gdsMasterMapperExt.getSizeCodeListByGoodsIdsErp(ext);
            }

            //返回前台的尺码列表
            List<GdsMasterExt> sizeList = new ArrayList<GdsMasterExt>();
            if (sizeCodeAndNameList != null) {
                for (GdsMasterExt ext : sizeCodeAndNameList) {
                    GdsMasterExt gdsMasterExt1 = new GdsMasterExt();
                    gdsMasterExt1.setSizeCode(ext.getSizeTypeCode() + "_" + ext.getSizeCode());
                    gdsMasterExt1.setSizeName(ext.getSizeName());
                    sizeList.add(gdsMasterExt1);
                }
            }


            if (!StringUtil.isEmpty(gdsMasterExt.getGoodsTypeId())) {
                GdsType gdsType = gdsTypeMapper.selectByPrimaryKey(gdsMasterExt.getGoodsTypeId());
                if (gdsType == null) {
                    gdsMasterExt.setGoodsBrandTypeId(gdsMasterExt.getGoodsTypeId());
                    gdsMasterExt.setGoodsTypeId(null);
                }
            }
            //转换数据格式
            if (gdsMasterExt.getCurrentPage() != 0 && gdsMasterExt.getPageSize() != 0) {
                //获取总页数
                int allCount = gdsMasterMapperExt.selectProductsByParameterCountUp(gdsMasterExt);
                int devi = allCount / gdsMasterExt.getPageSize();
                int count = 0;
                if (allCount % gdsMasterExt.getPageSize() == 0) {
                    count = devi;
                } else {
                    count = devi + 1;
                }
                json.put("allCount", count);
                gdsMasterExt.setDisplayItemCount(gdsMasterExt.getPageSize() * (gdsMasterExt.getCurrentPage() - 1));
            }

            List<GdsMasterExt> pList = gdsMasterMapperExt.selectProductsByParameterUp(gdsMasterExt);


            json.put("sizeList", sizeList);
            json.put("results", pList);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }
}
