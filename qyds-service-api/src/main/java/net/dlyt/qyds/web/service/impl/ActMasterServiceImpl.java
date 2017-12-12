package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.CouponMasterExt;
import net.dlyt.qyds.common.dto.ext.MmbGroupMemberExt;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.common.dto.ext.MmbShoppingBagExt;
import net.dlyt.qyds.common.form.*;
import net.dlyt.qyds.dao.*;
import net.dlyt.qyds.dao.ext.*;
import net.dlyt.qyds.web.service.ActMasterService;
import net.dlyt.qyds.web.service.CatcheRemoveService;
import net.dlyt.qyds.web.service.common.ComCode;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by cjk on 16/8/1.
 */
@Service("actMasterService")
public class ActMasterServiceImpl implements ActMasterService {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    ActMasterMapper actMasterMapper;
    @Autowired
    ActMasterMapperExt actMasterMapperExt;
    @Autowired
    ActGoodsMapperExt actGoodsMapperExt;
    @Autowired
    ActMemberMapperExt actMemberMapperExt;
    @Autowired
    ActSubMapperExt actSubMapperExt;
    @Autowired
    ActSubMapper actSubMapper;
    @Autowired
    ActGoodsMapper actGoodsMapper;
    @Autowired
    ActMemberMapper actMemberMapper;
    @Autowired
    MmbGroupMemberMapperExt mmbGroupMemberMapperExt;
    @Autowired
    MmbMasterMapperExt mmbMasterMapperExt;
    @Autowired
    SkuMapperExt skuMapperExt;
    @Autowired
    ActTemplateMapperExt actTemplateMapperExt;
    @Autowired
    ActTempParamMapperExt actTempParamMapperExt;
    @Autowired
    GdsMasterMapper gdsMasterMapper;
    @Autowired
    GdsMasterMapperExt gdsMasterMapperExt;
    @Autowired
    ComSettingMapper comSettingMapper;
    @Autowired
    OrdMasterMapperExt ordMasterMapperExt;
    @Autowired
    OrdSubListMapperExt ordSubListMapperExt;
    @Autowired
    private CatcheRemoveService catcheRemoveService;

    /**
     * 将订单里的商品按照单品活动分组 key是activityId,value是goodsList
     *
     * @param goodsList
     * @return
     */
    public static Map<String, List<OrdConfirmGoodsExt>> getGoodsGroupMap(List<OrdConfirmGoodsExt> goodsList) {
        // 将商品按照活动排序
        Collections.sort(goodsList, new Comparator<OrdConfirmGoodsExt>() {
            public int compare(OrdConfirmGoodsExt o1, OrdConfirmGoodsExt o2) {
                // 其余的按照价格从小到大排序
                if (o1.getActGoodsId() == null && o2.getActGoodsId() == null) {
                    return 0;
                } else if (o1.getActGoodsId() == null) {
                    return 1;
                } else if (o2.getActGoodsId() == null) {
                    return -1;
                } else {
                    return o2.getActGoodsId().compareTo(o1.getActGoodsId());
                }
            }
        });
        Map<String, List<OrdConfirmGoodsExt>> groupMap = new HashMap<String, List<OrdConfirmGoodsExt>>();
        String currentId = goodsList.get(0).getActGoodsId();
        List<OrdConfirmGoodsExt> currentGroup = new ArrayList<OrdConfirmGoodsExt>();
        currentGroup.add(goodsList.get(0));

        for (int i = 1; i < goodsList.size(); i++) {
            if (currentId == null) {
                if (goodsList.get(i).getActGoodsId() == currentId) {
                    currentGroup.add(goodsList.get(i));
                } else {
                    List<OrdConfirmGoodsExt> savedGroup = new ArrayList<OrdConfirmGoodsExt>();
                    savedGroup.addAll(currentGroup);
                    groupMap.put(currentId, savedGroup);

                    currentId = goodsList.get(i).getActGoodsId();
                    currentGroup.clear();
                    currentGroup.add(goodsList.get(i));
                }
            } else {
                if (currentId.equals(goodsList.get(i).getActGoodsId())) {
                    currentGroup.add(goodsList.get(i));
                } else {
                    List<OrdConfirmGoodsExt> savedGroup = new ArrayList<OrdConfirmGoodsExt>();
                    savedGroup.addAll(currentGroup);
                    groupMap.put(currentId, savedGroup);

                    currentId = goodsList.get(i).getActGoodsId();
                    currentGroup.clear();
                    currentGroup.add(goodsList.get(i));
                }
            }
        }
        // 最后一组
        List<OrdConfirmGoodsExt> savedGroup = new ArrayList<OrdConfirmGoodsExt>();
        savedGroup.addAll(currentGroup);
        groupMap.put(currentId, savedGroup);
        return groupMap;
    }

    public static void main(String[] aaa) {
        OrdConfirmGoodsExt goods1 = new OrdConfirmGoodsExt();
        OrdConfirmGoodsExt goods2 = new OrdConfirmGoodsExt();
        OrdConfirmGoodsExt goods3 = new OrdConfirmGoodsExt();
        OrdConfirmGoodsExt goods4 = new OrdConfirmGoodsExt();
        OrdConfirmGoodsExt goods5 = new OrdConfirmGoodsExt();
        OrdConfirmGoodsExt goods6 = new OrdConfirmGoodsExt();
        OrdConfirmGoodsExt goods7 = new OrdConfirmGoodsExt();
        goods1.setActGoodsId(null);
        goods2.setActGoodsId("1");
        goods3.setActGoodsId("2");
        goods4.setActGoodsId(null);
        goods5.setActGoodsId("1");
        goods6.setActGoodsId("3");
        goods7.setActGoodsId("2");
        List<OrdConfirmGoodsExt> goodsList = new ArrayList<OrdConfirmGoodsExt>();
        goodsList.add(goods1);
        goodsList.add(goods2);
        goodsList.add(goods3);
        goodsList.add(goods4);
        goodsList.add(goods5);
        goodsList.add(goods6);
        goodsList.add(goods7);
        Map<String, List<OrdConfirmGoodsExt>> groupMap = getGoodsGroupMap(goodsList);
        System.out.println(groupMap);
    }

    public JSONObject getAllList() {
        JSONObject json = new JSONObject();
        try {
            ActMasterForm form = new ActMasterForm();
            form.setShopId(Constants.ORGID);
            List<ActMaster> list = actMasterMapperExt.getAllList(form);

            json.put("data", list);
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

    public JSONObject getList(ActMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            //数据库检索 -- 过滤数据
            form.setDeleted(Constants.DELETED_NO);
            List<ActMaster> list = actMasterMapperExt.select(form);
            int allCount = actMasterMapperExt.selectCount(form);
            json.put("aaData", list);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);
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

    public JSONObject getApproveList(ActMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            //数据库检索 -- 过滤数据
            form.setDeleted(Constants.DELETED_NO);
            List<ActMaster> list = actMasterMapperExt.selectApproveList(form);
            int allCount = actMasterMapperExt.selectApproveCount(form);
            json.put("aaData", list);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);
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

    public JSONObject getSellerYears(){
        JSONObject json = new JSONObject();
        try {
            //获取年份列表
            List<String> list = actMasterMapperExt.getSellerYears();
            json.put("data", list);
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

    public JSONObject getSellerSeasons(String year){
        JSONObject json = new JSONObject();
        try {
            //获取年份列表
            List<Map> list = actMasterMapperExt.getSellerSeasons(year);
            json.put("data", list);
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

    public JSONObject getErpBrands(){
        JSONObject json = new JSONObject();
        try {
            //获取年份列表
            List<Map> list = actMasterMapperExt.getErpBrands();
            json.put("data", list);
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

    public JSONObject getErpLineCode(){
        JSONObject json = new JSONObject();
        try {
            //获取年份列表
            List<Map> list = actMasterMapperExt.getErpLineCode();
            json.put("data", list);
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




    public JSONObject getDetail(ActMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getActivityId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            ActMasterForm activity = actMasterMapperExt.selectById(form);
            if (activity == null) {
                throw new ExceptionErrorData("活动不存在");
            }
            List<ActGoodsForm> goodsList = null;
            ActGoods goods = new ActGoods();
            goods.setActivityId(form.getActivityId());
            if (ComCode.ActivityGoodsType.ALL.equals(activity.getGoodsType())) {
                //全部商品

            } else if (ComCode.ActivityGoodsType.TYPE.equals(activity.getGoodsType())) {
                // 按分类
                goodsList = actGoodsMapperExt.selectGoodsTypeByActivityId(goods);
            } else if (ComCode.ActivityGoodsType.BRAND.equals(activity.getGoodsType())) {
                // 按品牌
                goodsList = actGoodsMapperExt.selectGoodsBrandByActivityId(goods);
            } else if (ComCode.ActivityGoodsType.GOODS.equals(activity.getGoodsType())) {
                // 按商品
                goodsList = actGoodsMapperExt.selectGoodsByActivityId(goods);
            } else if (ComCode.ActivityGoodsType.SKU.equals(activity.getGoodsType())) {
                // 按SKU
                goodsList = actGoodsMapperExt.selectSkuByActivityId(goods);
            }
            activity.setGoodsList(goodsList);
            //优惠会员
            List<ActMemberForm> memberList = null;
            ActMember member = new ActMember();
            member.setActivityId(form.getActivityId());
            if (ComCode.ActivityMemberType.ALL.equals(activity.getMemberType())) {
                //全部会员

            } else if (ComCode.ActivityMemberType.MEMBER_GROUP.equals(activity.getMemberType())) {
                // 按会员分组
                memberList = actMemberMapperExt.selectMemberGroupByActivityId(member);
            } else if (ComCode.ActivityMemberType.MEMBER_LEVEL.equals(activity.getMemberType())) {
                // 按会员级别
                memberList = actMemberMapperExt.selectMemberLevelByActivityId(member);
            }
            activity.setMemberList(memberList);
            // 子活动
            if ("1".equals(activity.getHasSubActivity())) {
                ActSub actSub = new ActSub();
                actSub.setActivityId(form.getActivityId());
                activity.setSubActivityList(actSubMapperExt.selectByActivityId(actSub));
            }
            json.put("data", activity);
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

    public JSONObject delete(ActMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getActivityId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            ActMaster record = new ActMaster();
            record.setActivityId(form.getActivityId());
            record.setDeleted(Constants.DELETED_YES);
            actMasterMapper.updateByPrimaryKeySelective(record);
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

    @Transactional(rollbackFor = Exception.class)
    public JSONObject add(ActMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getActivityId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getActivityName())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getTempId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getTempName())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getGoodsType())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getMemberType())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getActitionType())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (ComCode.ActivityType.SECOND_KILL.equals(form.getActitionType())) {
                if (StringUtil.isEmpty(form.getStartDateTimeStr())) {
                    throw new ExceptionErrorParam("缺少参数");
                }
                if (StringUtil.isEmpty(form.getEndDateTimeStr())) {
                    throw new ExceptionErrorParam("缺少参数");
                }

//                for (ActGoodsForm item : form.getGoodsList()) {
//
//                    if (null == item.getActPrice() || 1 != item.getActPrice().compareTo(BigDecimal.ZERO)) {
//                        throw new ExceptionErrorParam("秒杀价格不能为空或为零");
//                    }
//
//                    if (null == item.getQuantity() || 0 == item.getQuantity()) {
//                        throw new ExceptionErrorParam("活动数量不能为空或为零");
//                    }
//                }

            } else {
                if (StringUtil.isEmpty(form.getStartTimeStr())) {
                    throw new ExceptionErrorParam("缺少参数");
                }
                if (StringUtil.isEmpty(form.getEndTimeStr())) {
                    throw new ExceptionErrorParam("缺少参数");
                }
            }

            // 判断活动名称是否重复
            ActMaster form1 = new ActMaster();
            form1.setActivityName(form.getActivityName());
            int count = actMasterMapperExt.checkExistByActivityName(form1);
            if (count > 0) {
                throw new ExceptionErrorData("已经存在相同名称的活动");
            }
            // 插入活动主表
            Date date = new Date();
            String id = form.getActivityId();
            ActMaster at = new ActMaster();
            at.setActivityId(id);
            at.setActivityName(form.getActivityName());
            at.setTempId(form.getTempId());
            at.setTempName(form.getTempName());
            at.setHasSubActivity(form.getHasSubActivity());
            at.setIsMemberActivity(form.getIsMemberActivity());
            at.setNeedFee(form.getNeedFee());
            at.setNeedPoint(form.getNeedPoint());
            at.setIsOriginPrice(form.getIsOriginPrice());
            at.setApplyContent(form.getApplyContent());
            at.setAdditionalDiscount(form.getAdditionalDiscount());
            at.setCanExchange(form.getCanExchange());
            at.setCanReturn(form.getCanReturn());
            at.setGoodsType(form.getGoodsType());
            at.setMemberType(form.getMemberType());
            at.setUnit(form.getUnit());
            at.setShopId(form.getShopId());
            at.setLimitCount(form.getLimitCount());
            at.setIsValid("0");
            if (ComCode.ActivityType.SECOND_KILL.equals(form.getActitionType())) {
                at.setStartTime(sdf.parse(form.getStartDateTimeStr() + ":00"));
                at.setEndTime(sdf.parse(form.getEndDateTimeStr() + ":00"));
            } else {
                at.setStartTime(sdf.parse(form.getStartTimeStr() + " 00:00:00"));
                at.setEndTime(sdf.parse(form.getEndTimeStr() + " 23:59:59"));
            }

            at.setApproveStatus(ComCode.ApproveStatus.APPROVE_STATUS_NOT_APPLY);
            at.setComment(form.getComment());
            at.setInsertUserId(form.getInsertUserId());
            at.setInsertTime(date);
            at.setDeleted(Constants.DELETED_NO);
            if("60".equals(form.getGoodsType())){
                at.setGoodsTypeValue(form.getActivityGoodsSellYear().concat("_").concat(form.getActivityGoodsSeasonCode()));
            }else if("70".equals(form.getGoodsType())){
                at.setGoodsTypeValue(form.getActivityGoodsErpBrand());
            }else if("80".equals(form.getGoodsType())){
                at.setGoodsTypeValue(form.getActivityGoodsLineCode());
            }
            actMasterMapper.insertSelective(at);
            form.setActivityId(id);
            processSubTable(form, null);
            // 年/季节
            if(!StringUtil.isEmpty(form.getActivityGoodsSellYear())){
                //处理年份还有季节
                //按照年份查询sku
                List<Map>  list = actGoodsMapperExt.selectSKUByYearAndSeason(form);
                //添加sku信息
                for(Map map:list){
                    String sku = (String) map.get("sku");
                    String goodsId = (String) map.get("goods_id");

                    ActGoods record = new ActGoods();
                    record.setActGoodsId(UUID.randomUUID().toString());
                    record.setShopId("00000000");
                    record.setActivityId(form.getActivityId());
                    record.setGoodsType("50");
                    record.setGoodsId(goodsId);
                    record.setSkuId(sku);
                    record.setDeleted("0");
                    record.setUpdateTime(date);
                    record.setGoodsTypeValue(form.getGoodsType());
                    record.setUpdateUserId(form.getUpdateUserId());
                    record.setInsertTime(date);
                    record.setInsertUserId(form.getUpdateUserId());
                    actGoodsMapper.insertSelective(record);
                }
            }

            if(!StringUtil.isEmpty(form.getActivityGoodsErpBrand())){
                //处理ERP品牌
                List<Map>  list = actGoodsMapperExt.selectSKUByErpBrand(form);

                //添加sku信息
                for(Map map:list){
                    String sku = (String) map.get("sku");
                    String goodsId = (String) map.get("goods_id");

                    ActGoods record = new ActGoods();
                    record.setActGoodsId(UUID.randomUUID().toString());
                    record.setShopId("00000000");
                    record.setActivityId(form.getActivityId());
                    record.setGoodsType("50");
                    record.setGoodsId(goodsId);
                    record.setSkuId(sku);
                    record.setDeleted("0");
                    record.setUpdateTime(date);
                    record.setGoodsTypeValue(form.getGoodsType());
                    record.setUpdateUserId(form.getUpdateUserId());
                    record.setInsertTime(date);
                    record.setInsertUserId(form.getUpdateUserId());
                    actGoodsMapper.insertSelective(record);
                }
            }

            if(!StringUtil.isEmpty(form.getActivityGoodsLineCode())){
                //处理ERP品牌
                List<Map>  list = actGoodsMapperExt.selectSKUByLineCode(form);

                //添加sku信息
                for(Map map:list){
                    String sku = (String) map.get("sku");
                    String goodsId = (String) map.get("goods_id");

                    ActGoods record = new ActGoods();
                    record.setActGoodsId(UUID.randomUUID().toString());
                    record.setShopId("00000000");
                    record.setActivityId(form.getActivityId());
                    record.setGoodsType("50");
                    record.setGoodsId(goodsId);
                    record.setSkuId(sku);
                    record.setDeleted("0");
                    record.setUpdateTime(date);
                    record.setGoodsTypeValue(form.getGoodsType());
                    record.setUpdateUserId(form.getUpdateUserId());
                    record.setInsertTime(date);
                    record.setInsertUserId(form.getUpdateUserId());
                    actGoodsMapper.insertSelective(record);
                }
            }

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

    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {@CacheEvict(value = "${activity_goods_list_catch_name}", allEntries = true)})
    public JSONObject edit(ActMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getActivityId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getActivityName())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getTempId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getTempName())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getGoodsType())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getMemberType())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getActitionType())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (ComCode.ActivityType.SECOND_KILL.equals(form.getActitionType())) {
                if (StringUtil.isEmpty(form.getStartDateTimeStr())) {
                    throw new ExceptionErrorParam("缺少参数");
                }
                if (StringUtil.isEmpty(form.getEndDateTimeStr())) {
                    throw new ExceptionErrorParam("缺少参数");
                }

//                for (ActGoodsForm item : form.getGoodsList()) {
//
//                    if (null == item.getActPrice() || 1 != item.getActPrice().compareTo(BigDecimal.ZERO)) {
//                        throw new ExceptionErrorParam("秒杀价格不能为空或为零");
//                    }
//
//                    if (null == item.getQuantity() || 0 == item.getQuantity()) {
//                        throw new ExceptionErrorParam("活动数量不能为空");
//                    }
//                }

            } else {
                if (StringUtil.isEmpty(form.getStartTimeStr())) {
                    throw new ExceptionErrorParam("缺少参数");
                }
                if (StringUtil.isEmpty(form.getEndTimeStr())) {
                    throw new ExceptionErrorParam("缺少参数");
                }
            }
            // 检索出原来的数据
            ActMaster oldData = actMasterMapper.selectByPrimaryKey(form.getActivityId());
            if (oldData == null || Constants.DELETED_YES.equals(oldData.getDeleted())) {
                throw new ExceptionErrorData("活动不存在");
            }
            if (!oldData.getActivityName().equals(form.getActivityName())) {
                // 活动名称发生变化
                // 判断活动名称是否重复
                ActMaster form1 = new ActMaster();
                form1.setActivityName(form.getActivityName());
                int count = actMasterMapperExt.checkExistByActivityName(form1);
                if (count > 0) {
                    throw new ExceptionErrorData("已经存在相同名称的活动");
                }
            }

            if (ComCode.ActivityType.SECOND_KILL.equals(form.getActitionType())) {
                form.setStartTime(sdf.parse(form.getStartDateTimeStr() + ":00"));
                form.setEndTime(sdf.parse(form.getEndDateTimeStr() + ":00"));
            } else {
                form.setStartTime(sdf.parse(form.getStartTimeStr() + " 00:00:00"));
                form.setEndTime(sdf.parse(form.getEndTimeStr() + " 23:59:59"));
            }

            // 更新活动主表
            Date date = new Date();
            form.setUpdateUserId(form.getUpdateUserId());
            form.setUpdateTime(date);
            // 编辑之后状态变成未申请和停用
            form.setApproveStatus(ComCode.ApproveStatus.APPROVE_STATUS_NOT_APPLY);
            form.setIsValid("0");
            if("60".equals(form.getGoodsType())){
                form.setGoodsTypeValue(form.getActivityGoodsSellYear().concat("_").concat(form.getActivityGoodsSeasonCode()));
            }else if("70".equals(form.getGoodsType())){
                form.setGoodsTypeValue(form.getActivityGoodsErpBrand());
            }else if("80".equals(form.getGoodsType())){
                form.setGoodsTypeValue(form.getActivityGoodsLineCode());
            }
            actMasterMapper.updateByPrimaryKeySelective(form);
            processSubTable(form, oldData.getGoodsType());

            // 年/季节
            if(!StringUtil.isEmpty(form.getActivityGoodsSellYear())){
                //处理年份还有季节
                //按照年份查询sku
                List<Map>  list = actGoodsMapperExt.selectSKUByYearAndSeason(form);
                //删除对应的sku
                ActGoods actGoods = new ActGoods();
                actGoods.setActivityId(form.getActivityId());
                actGoodsMapperExt.deleteByActivityId(actGoods);
                //添加sku信息
                for(Map map:list){
                    String sku = (String) map.get("sku");
                    String goodsId = (String) map.get("goods_id");

                    ActGoods record = new ActGoods();
                    record.setActGoodsId(UUID.randomUUID().toString());
                    record.setShopId("00000000");
                    record.setActivityId(form.getActivityId());
                    record.setGoodsType("50");
                    record.setGoodsId(goodsId);
                    record.setSkuId(sku);
                    record.setDeleted("0");
                    record.setUpdateTime(date);
                    record.setGoodsTypeValue(form.getGoodsType());
                    record.setUpdateUserId(form.getUpdateUserId());
                    record.setInsertTime(date);
                    record.setInsertUserId(form.getUpdateUserId());
                    actGoodsMapper.insertSelective(record);
                }
            }

            if(!StringUtil.isEmpty(form.getActivityGoodsErpBrand())){
                //处理ERP品牌
                List<Map>  list = actGoodsMapperExt.selectSKUByErpBrand(form);
                //删除对应的sku
                ActGoods actGoods = new ActGoods();
                actGoods.setActivityId(form.getActivityId());
                actGoodsMapperExt.deleteByActivityId(actGoods);
                //添加sku信息
                for(Map map:list){
                    String sku = (String) map.get("sku");
                    String goodsId = (String) map.get("goods_id");

                    ActGoods record = new ActGoods();
                    record.setActGoodsId(UUID.randomUUID().toString());
                    record.setShopId("00000000");
                    record.setActivityId(form.getActivityId());
                    record.setGoodsType("50");
                    record.setGoodsId(goodsId);
                    record.setSkuId(sku);
                    record.setDeleted("0");
                    record.setUpdateTime(date);
                    record.setGoodsTypeValue(form.getGoodsType());
                    record.setUpdateUserId(form.getUpdateUserId());
                    record.setInsertTime(date);
                    record.setInsertUserId(form.getUpdateUserId());
                    actGoodsMapper.insertSelective(record);
                }
            }

            if(!StringUtil.isEmpty(form.getActivityGoodsLineCode())){
                //处理ERP品牌
                List<Map>  list = actGoodsMapperExt.selectSKUByLineCode(form);
                //删除对应的sku
                ActGoods actGoods = new ActGoods();
                actGoods.setActivityId(form.getActivityId());
                actGoodsMapperExt.deleteByActivityId(actGoods);
                //添加sku信息
                for(Map map:list){
                    String sku = (String) map.get("sku");
                    String goodsId = (String) map.get("goods_id");

                    ActGoods record = new ActGoods();
                    record.setActGoodsId(UUID.randomUUID().toString());
                    record.setShopId("00000000");
                    record.setActivityId(form.getActivityId());
                    record.setGoodsType("50");
                    record.setGoodsId(goodsId);
                    record.setSkuId(sku);
                    record.setDeleted("0");
                    record.setUpdateTime(date);
                    record.setGoodsTypeValue(form.getGoodsType());
                    record.setUpdateUserId(form.getUpdateUserId());
                    record.setInsertTime(date);
                    record.setInsertUserId(form.getUpdateUserId());
                    actGoodsMapper.insertSelective(record);
                }
            }

            //调用清理缓存 单个
            catcheRemoveService.sendPost(Constants.PC_SOAP_URL + "removeOnly.json", null);
            catcheRemoveService.sendPost(Constants.WX_SOAP_URL + "removeOnly.json", null);

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
     * 秒杀活动商品信息导入
     *
     * @param skuList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject uploadSecKillGoods(List<ActGoods> skuList) {
        JSONObject json = new JSONObject();
        try {
            if (null == skuList || skuList.size() == 0) {
                throw new ExceptionErrorParam("活动商品数据不存在");
            }

            String activityId = skuList.get(0).getActivityId();

            String loginId = skuList.get(0).getInsertUserId();

            if (StringUtil.isEmpty(activityId)) {
                throw new ExceptionErrorParam("缺少参数");
            }

            // 检索出原来的数据
            ActMaster oldData = actMasterMapper.selectByPrimaryKey(activityId);
            if (oldData == null || Constants.DELETED_YES.equals(oldData.getDeleted())) {
                throw new ExceptionErrorData("活动不存在");
            }

            if (!ComCode.ActivityGoodsType.SKU.equals(oldData.getGoodsType())) {
                throw new ExceptionErrorData("活动商品类型不正确");
            }

            if (!ComCode.ApproveStatus.APPROVE_STATUS_APPROVING.equals(oldData.getApproveStatus())
                    && !ComCode.ApproveStatus.APPROVE_STATUS_NOT_APPLY.equals(oldData.getApproveStatus())) {
                throw new ExceptionErrorData("活动状态不正确");
            }

            // 更新活动主表
            Date date = new Date();
            oldData.setUpdateUserId(loginId);
            oldData.setUpdateTime(date);

            actMasterMapper.updateByPrimaryKeySelective(oldData);

            if (skuList != null && skuList.size() > 0) {

                List<ActGoods> addGoodsList = new ArrayList<>();
                List<ActGoods> delGoodsList = new ArrayList<>();
                List<ActGoods> updateGoodsList = new ArrayList<>();

                ActGoods goods = new ActGoods();
                goods.setActivityId(activityId);
                List<ActGoods> goodsList = actGoodsMapperExt.selectByActivityId(goods);

                if (goodsList == null || goodsList.size() == 0) {
                    // 如果既存数据没有直接添加
                    addGoodsList = skuList;
                } else {

                    List<String> oldList = new ArrayList<>();
                    for (ActGoods item : goodsList) {
                        oldList.add(item.getSkuId());
                    }

                    List<String> updateList = new ArrayList<>();
                    for (ActGoods item : skuList) {
                        updateList.add(item.getSkuId());
                    }

                    for (ActGoods item : goodsList) {
                        String itemKey = item.getSkuId();

                        if (updateList.contains(itemKey)) {
                            continue;
                        } else {
                            delGoodsList.add(item);
                        }
                    }

                    for (ActGoods item : skuList) {
                        String itemKey = item.getSkuId();

                        if (oldList.contains(itemKey)) {
                            for (ActGoods goodsItem : goodsList) {
                                if (goodsItem.getSkuId().equals(itemKey)) {
                                    if (goodsItem.getQuantity() != item.getQuantity()
                                            || goodsItem.getBuyMax() != item.getBuyMax()
                                            || 0 != goodsItem.getActPrice().compareTo(item.getActPrice())) {
                                        goodsItem.setQuantity(item.getQuantity());
                                        goodsItem.setBuyMax(item.getBuyMax());
                                        goodsItem.setSurplus(item.getQuantity());
                                        goodsItem.setActPrice(item.getActPrice());
                                        goodsItem.setUpdateTime(date);
                                        goodsItem.setUpdateUserId(loginId);
                                        updateGoodsList.add(goodsItem);
                                    }
                                    break;
                                }
                            }
                        } else {
                            addGoodsList.add(item);
                        }
                    }
                }

                for (ActGoods item : updateGoodsList) {
                    actGoodsMapper.updateByPrimaryKeySelective(item);
                }

                HashMap<String, String> goodsMap = new HashMap<>();
                if (addGoodsList != null && addGoodsList.size() > 0) {
                    goodsMap = getGoodsMap();
                }

                for (ActGoods item : addGoodsList) {
                    goods = new ActGoods();
                    goods.setGoodsId(goodsMap.get(item.getSkuId()));
                    goods.setQuantity(item.getQuantity());
                    goods.setBuyMax(item.getBuyMax());
                    goods.setSurplus(item.getQuantity());
                    goods.setActPrice(item.getActPrice());
                    goods.setInsertUserId(loginId);
                    goods.setShopId(Constants.ORGID);
                    goods.setInsertTime(date);
                    goods.setActivityId(activityId);
                    goods.setGoodsType(ComCode.ActivityGoodsType.SKU);
                    goods.setDeleted(Constants.DELETED_NO);
                    goods.setSkuId(item.getSkuId());
                    goods.setActGoodsId(UUID.randomUUID().toString());
                    actGoodsMapper.insertSelective(goods);
                }

                for (ActGoods item : delGoodsList) {
                    actGoodsMapperExt.deleteByActGoodsId(item.getActGoodsId());
                }
            }

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

    private HashMap<String, String> getGoodsMap() {
        HashMap<String, String> goodsMap = new HashMap<>();
        List<HashMap<String, String>> resutsList = actGoodsMapperExt.selectAllGoodsMap();

        if (resutsList == null) {
            goodsMap = new HashMap<>();
        } else {
            for (HashMap<String, String> item : resutsList) {
                goodsMap.put(item.get("skuid"), item.get("goods_id"));
            }
        }

        return goodsMap;
    }

    /**
     * 批量更新活动SKU信息
     *
     * @param loginId
     * @param activityId
     * @param skuList
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject uploadSKUInfo(String loginId, String activityId, List<String> skuList) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(activityId)) {
                throw new ExceptionErrorParam("缺少参数");
            }

            // 检索出原来的数据
            ActMaster oldData = actMasterMapper.selectByPrimaryKey(activityId);
            if (oldData == null || Constants.DELETED_YES.equals(oldData.getDeleted())) {
                throw new ExceptionErrorData("活动不存在");
            }

            if (!ComCode.ActivityGoodsType.SKU.equals(oldData.getGoodsType())) {
                throw new ExceptionErrorData("活动商品类型不正确");
            }

            if (!ComCode.ApproveStatus.APPROVE_STATUS_APPROVING.equals(oldData.getApproveStatus())
                    && !ComCode.ApproveStatus.APPROVE_STATUS_NOT_APPLY.equals(oldData.getApproveStatus())) {
                throw new ExceptionErrorData("活动状态不正确");
            }

            // 更新活动主表
            Date date = new Date();
            oldData.setUpdateUserId(loginId);
            oldData.setUpdateTime(date);

            actMasterMapper.updateByPrimaryKeySelective(oldData);

            if (skuList != null && skuList.size() > 0) {

                List<String> addGoodsList = new ArrayList<>();
                List<ActGoods> delGoodsList = new ArrayList<>();

                ActGoods goods = new ActGoods();
                goods.setActivityId(activityId);
                List<ActGoods> goodsList = actGoodsMapperExt.selectByActivityId(goods);
                ;

                if (goodsList == null || goodsList.size() == 0) {
                    // 如果既存数据没有直接添加
                    addGoodsList = skuList;
                } else {

                    List<String> oldList = new ArrayList<>();
                    for (ActGoods item : goodsList) {
                        String itemKey = item.getSkuId();
                        oldList.add(itemKey);

                        if (skuList.contains(itemKey)) {
                            continue;
                        } else {
                            delGoodsList.add(item);
                        }
                    }

                    for (String item : skuList) {
                        if (oldList.contains(item)) {
                            continue;
                        } else {
                            addGoodsList.add(item);
                        }
                    }
                }

                HashMap<String, String> goodsMap = new HashMap<>();
                if (addGoodsList != null && addGoodsList.size() > 0) {
                    goodsMap = getGoodsMap();
                }

                for (String skuItem : addGoodsList) {
                    if (goodsMap.containsKey(skuItem)) {
                        goods = new ActGoods();
                        goods.setGoodsId(goodsMap.get(skuItem));
                        goods.setInsertUserId(loginId);
                        goods.setShopId(oldData.getShopId());
                        goods.setInsertTime(date);
                        goods.setActivityId(activityId);
                        goods.setGoodsType(ComCode.ActivityGoodsType.SKU);
                        goods.setDeleted(Constants.DELETED_NO);
                        goods.setSkuId(skuItem);
                        goods.setActGoodsId(UUID.randomUUID().toString());
                        actGoodsMapper.insertSelective(goods);
                    }
                }

                for (ActGoods item : delGoodsList) {
                    actGoodsMapperExt.deleteByActGoodsId(item.getActGoodsId());
                }
            }

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
     * 取得活动的SKU列表
     *
     * @param activityId
     * @return
     */
    @Override
    public JSONObject getActivitySKUList(String activityId) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(activityId)) {
                throw new ExceptionErrorParam("缺少参数");
            }

            // 检索出原来的数据
            ActMaster oldData = actMasterMapper.selectByPrimaryKey(activityId);
            if (oldData == null || Constants.DELETED_YES.equals(oldData.getDeleted())) {
                throw new ExceptionErrorData("活动不存在");
            }

            if (!ComCode.ActivityGoodsType.SKU.equals(oldData.getGoodsType())) {
                throw new ExceptionErrorData("活动商品类型不正确");
            }

            ActGoods goods = new ActGoods();
            goods.setActivityId(activityId);
            List<ActGoodsForm> goodsList = actGoodsMapperExt.selectSkuByActivityId(goods);

            json.put("name", oldData.getActivityName());
            json.put("resultList", goodsList);
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

    @Override
    public JSONObject setValid(ActMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getActivityId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            ActMaster record = new ActMaster();
            record.setActivityId(form.getActivityId());
            record.setIsValid("1");
            actMasterMapper.updateByPrimaryKeySelective(record);
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

    @Override
    public JSONObject setInvalid(ActMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getActivityId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            ActMaster record = new ActMaster();
            record.setActivityId(form.getActivityId());
            record.setIsValid("0");
            actMasterMapper.updateByPrimaryKeySelective(record);
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

    private void processSubTable(ActMasterForm form, String oldGoodsType) {
        Date date = new Date();
        // 先删除之前的参数,重新插入
//        ActGoods actGoods = new ActGoods();
//        actGoods.setActivityId(form.getActivityId());
//        actGoodsMapperExt.deleteByActivityId(actGoods);
        ActMember actMember = new ActMember();
        actMember.setActivityId(form.getActivityId());
        actMemberMapperExt.deleteByActivityId(actMember);
        ActSub actSub1 = new ActSub();
        actSub1.setActivityId(form.getActivityId());
        actSubMapperExt.deleteByActivityId(actSub1);

//        if (form.getGoodsList() != null && form.getGoodsList().size() > 0) {
//
//            List<ActGoodsForm> addGoodsList = new ArrayList<>();
//            List<ActGoods> delGoodsList = new ArrayList<>();
//            List<ActGoods> updateGoodsList = new ArrayList<>();
//
//            if (null == oldGoodsType) {
//                // 新增-add
//                addGoodsList = form.getGoodsList();
//            } else if (!form.getGoodsType().equals(oldGoodsType)) {
//                // 更改活动商品类型-先清空,再添加
//                ActGoods actGoods = new ActGoods();
//                actGoods.setActivityId(form.getActivityId());
//                actGoodsMapperExt.deleteByActivityId(actGoods);
//
//                addGoodsList = form.getGoodsList();
//
//            } else {
//                // 增量更新商品
//
//                ActGoods goods = new ActGoods();
//                goods.setActivityId(form.getActivityId());
//                List<ActGoods> goodsList = actGoodsMapperExt.selectByActivityId(goods);
//
//                if (goodsList == null || goodsList.size() == 0) {
//                    // 如果既存数据没有直接添加
//                    addGoodsList = form.getGoodsList();
//                } else {
//
//                    List<String> oldList = new ArrayList<>();
//                    for (ActGoods item : goodsList) {
//                        if (ComCode.ActivityGoodsType.SKU.equals(oldGoodsType)) {
//                            oldList.add(item.getSkuId());
//                        } else {
//                            oldList.add(item.getGoodsId());
//                        }
//                    }
//
//                    List<String> updateList = new ArrayList<>();
//                    for (ActGoodsForm item : form.getGoodsList()) {
//                        if (ComCode.ActivityGoodsType.SKU.equals(oldGoodsType)) {
//                            updateList.add(item.getSkuId());
//                        } else {
//                            updateList.add(item.getGoodsId());
//                        }
//                    }
//
//                    for (ActGoods item : goodsList) {
//                        String itemKey = item.getGoodsId();
//                        if (ComCode.ActivityGoodsType.SKU.equals(oldGoodsType)) {
//                            itemKey = item.getSkuId();
//                        }
//
//                        if (updateList.contains(itemKey)) {
//                            continue;
//                        } else {
//                            delGoodsList.add(item);
//                        }
//                    }
//
//                    for (ActGoodsForm item : form.getGoodsList()) {
//                        String itemKey = item.getGoodsId();
//                        if (ComCode.ActivityGoodsType.SKU.equals(oldGoodsType)) {
//                            itemKey = item.getSkuId();
//                        }
//
//                        if (oldList.contains(itemKey)) {
//                            if (ComCode.ActivityType.SECOND_KILL.equals(form.getActitionType())) {
//                                for (ActGoods goodsItem : goodsList) {
//                                    if (goodsItem.getSkuId().equals(itemKey)) {
//                                        if (goodsItem.getQuantity() != item.getQuantity()
//                                                || goodsItem.getBuyMax() != item.getBuyMax()
//                                                || 0 != goodsItem.getActPrice().compareTo(item.getActPrice())) {
//                                            goodsItem.setQuantity(item.getQuantity());
//                                            goodsItem.setBuyMax(item.getBuyMax());
//                                            goodsItem.setSurplus(item.getQuantity());
//                                            goodsItem.setActPrice(item.getActPrice());
//                                            goodsItem.setUpdateTime(date);
//                                            goodsItem.setUpdateUserId(form.getInsertUserId());
//                                            updateGoodsList.add(goodsItem);
//                                        }
//                                        break;
//                                    }
//                                }
//                            } else {
//                                continue;
//                            }
//                        } else {
//                            addGoodsList.add(item);
//                        }
//                    }
//                }
//
//            }
//
//            for (ActGoods item : updateGoodsList) {
//                actGoodsMapper.updateByPrimaryKeySelective(item);
//            }
//
//            for (ActGoodsForm item : addGoodsList) {
//                ActGoods goods = new ActGoods();
//                goods.setGoodsId(item.getGoodsId());
//                goods.setQuantity(item.getQuantity());
//                goods.setBuyMax(item.getBuyMax());
//                goods.setSurplus(item.getQuantity());
//                goods.setActPrice(item.getActPrice());
//                goods.setInsertUserId(form.getInsertUserId());
//                goods.setShopId(form.getShopId());
//                goods.setInsertTime(date);
//                goods.setActivityId(form.getActivityId());
//                goods.setGoodsType(form.getGoodsType());
//                goods.setDeleted(Constants.DELETED_NO);
//                goods.setSkuId(item.getSkuId());
//                goods.setActGoodsId(UUID.randomUUID().toString());
//                actGoodsMapper.insertSelective(goods);
//            }
//
//            for (ActGoods item : delGoodsList) {
//                actGoodsMapperExt.deleteByActGoodsId(item.getActGoodsId());
//            }
//        } else {
//
//            // 如果没有新数据则直接删除所有
//
//            ActGoods actGoods = new ActGoods();
//            actGoods.setActivityId(form.getActivityId());
//            actGoodsMapperExt.deleteByActivityId(actGoods);
//        }
        // 优惠会员
        if (form.getMemberList() != null) {
            for (int i = 0; i < form.getMemberList().size(); i++) {
                ActMember member = new ActMember();
                member.setMemberId(form.getMemberList().get(i).getMemberId());
                member.setInsertUserId(form.getInsertUserId());
                member.setShopId(form.getShopId());
                member.setInsertTime(date);
                member.setActivityId(form.getActivityId());
                member.setMemberType(form.getMemberType());
                member.setDeleted(Constants.DELETED_NO);
                member.setActMemberId(UUID.randomUUID().toString());
                actMemberMapper.insertSelective(member);
            }
        }
        // 子活动
        if (form.getSubActivityList() != null) {
            for (int i = 0; i < form.getSubActivityList().size(); i++) {
                ActSub actSub = new ActSub();
                actSub.setActivityId(form.getActivityId());
                actSub.setSubActivityId(form.getSubActivityList().get(i).getActivityId());
                actSub.setInsertTime(date);
                actSub.setDeleted(Constants.DELETED_NO);
                actSub.setShopId(form.getShopId());
                actSub.setActSubId(UUID.randomUUID().toString());
                actSubMapper.insertSelective(actSub);
            }
        }
    }

    /**
     * 申请审批
     *
     * @param form
     * @
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject apply(ActMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getActivityId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 获取当前审批状态
            ActMaster Master = actMasterMapper.selectByPrimaryKey(form.getActivityId());
            if (Master == null) {
                throw new ExceptionErrorData("活动不存在");
            }
            if (!ComCode.ApproveStatus.APPROVE_STATUS_NOT_APPLY.equals(Master.getApproveStatus())) {
                throw new ExceptionErrorData("审批状态不正确");
            }
            Master.setApplyContent(form.getApplyContent());
            Master.setApproveStatus(ComCode.ApproveStatus.APPROVE_STATUS_APPROVING);
            Master.setApplyUserId(form.getApplyUserId());
            Master.setApplyTime(new Date());
            actMasterMapper.updateByPrimaryKeySelective(Master);
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
     * 审批通过
     *
     * @param form
     * @
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject approve(ActMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getActivityId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 获取当前审批状态
            ActMaster Master = actMasterMapper.selectByPrimaryKey(form.getActivityId());
            if (Master == null) {
                throw new ExceptionErrorData("活动不存在");
            }
            if (!ComCode.ApproveStatus.APPROVE_STATUS_APPROVING.equals(Master.getApproveStatus())) {
                throw new ExceptionErrorData("审批状态不正确");
            }
            Master.setApproveContent(form.getApproveContent());
            Master.setApproveStatus(ComCode.ApproveStatus.APPROVE_STATUS_APPROVED);
            Master.setApproveUserId(form.getApproveUserId());
            Master.setApproveTime(new Date());
            actMasterMapper.updateByPrimaryKeySelective(Master);
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
     * 审批拒绝
     *
     * @param form
     * @
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject reject(ActMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getActivityId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 获取当前审批状态
            ActMaster Master = actMasterMapper.selectByPrimaryKey(form.getActivityId());
            if (Master == null) {
                throw new ExceptionErrorData("活动不存在");
            }
            if (!ComCode.ApproveStatus.APPROVE_STATUS_APPROVING.equals(Master.getApproveStatus())) {
                throw new ExceptionErrorData("审批状态不正确");
            }
            Master.setApproveContent(form.getApproveContent());
            Master.setApproveStatus(ComCode.ApproveStatus.APPROVE_STATUS_REJECT);
            Master.setApproveUserId(form.getApproveUserId());
            Master.setApproveTime(new Date());
            actMasterMapper.updateByPrimaryKeySelective(Master);
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
     * 获取有活动价
     *
     * @param activityList
     * @param memberId
     * @param goodsId
     * @return
     */
    public JSONObject getNewPricesByActivity(List<ActMasterForm> activityList, String memberId, String goodsId) {
        JSONObject json = new JSONObject();
        try {
            List<ActMasterForm> activities = new ArrayList<ActMasterForm>();
            for (int i = 0; i < activityList.size(); i++) {
                ActMasterForm activity = actMasterMapperExt.selectByPrimaryKey(activityList.get(i).getActivityId());
                if (checkSecKillActivity(activity)) {
                    activity = checkActivity(activity, memberId, activityList.get(i).getOriginPrice(), 1, goodsId, null);
                    if (activity != null) {
                        activities.add(activity);
                    }
                }
            }

            if (activities.size() > 1) {
                Collections.sort(activities, new Comparator<ActMasterForm>() {
                    public int compare(ActMasterForm o1, ActMasterForm o2) {
                        // 其余的按照价格从小到大排序
                        if (o2.getNewPrice() - o1.getNewPrice() > 0) {
                            return -1;
                        } else if (o2.getNewPrice() - o1.getNewPrice() < 0) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
            }
            json.put("data", activities);
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
     * 如果是秒杀活动，判断当前时间是否在秒杀时间段内
     *
     * @param activity
     * @return
     */
    private boolean checkSecKillActivity(ActMasterForm activity) {

        if (!ComCode.ActivityType.SECOND_KILL.equals(activity.getActivityType())) {
            return true;
        }

        Date current = new Date();
        if (current.before(activity.getStartTime())) {
            //秒杀活动还未开始
            return false;
        }

        if (current.after(activity.getEndTime())) {
            // 秒杀活动已结束
            return false;
        }

        try {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat ydf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String currentDateStr = sdf.format(current);

            String startTime = currentDateStr + " " + df.format(activity.getStartTime());
            String endTime = currentDateStr + " " + df.format(activity.getEndTime());

            if (ydf.parse(endTime).before(current)) {
                // 秒杀活动已结束
                return false;
            }

            if (ydf.parse(startTime).after(current)) {
                // 秒杀活动还未开始
                return false;
            }

        } catch (Exception e) {
            // 秒杀活动时间不正确
            return false;
        }

        return true;
    }

    /**
     * 获取有活动价
     *
     * @param activityList
     * @param memberId
     * @return
     */
    public JSONObject filterActivityByMember(List<ActMasterForm> activityList, String memberId) {
        JSONObject json = new JSONObject();
        try {
            List<ActMasterForm> activities = new ArrayList<ActMasterForm>();
            for (int i = 0; i < activityList.size(); i++) {
                if (checkSecKillActivity(activityList.get(i))) {
                    ActMasterForm activity = actMasterMapperExt.selectByPrimaryKey(activityList.get(i).getActivityId());
                    if (activity != null && isContainsMember(activity, memberId)) {
                        activities.add(activity);
                    }
                }

            }
            json.put("data", activities);
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
     * 获取订单级别的活动根据活动ID
     *
     * @param
     * @return
     */
    public ActMasterForm getOrderActivityById(String memberId, String activityId, List<OrdConfirmGoodsExt> goodsInfo) {
        if (activityId == null) {
            return null;
        }
        // 查询当前有效的订单活动
        List<ActMasterForm> activityList = actMasterMapperExt.selectOrderActivity();
        if (activityList == null || activityList.size() == 0) {
            return null;
        }
        ActMasterForm activity = null;
        for (int i = 0; i < activityList.size(); i++) {
            if (activityId.equals(activityList.get(i).getActivityId())) {
                activity = activityList.get(i);
                break;
            }
        }
        List<ActMasterForm> activityList2 = new ArrayList<ActMasterForm>();
        activityList2.add(activity);
        // 过滤掉不符合条件的活动
        List<ActMasterForm> validActivityList = filterValidActivity(activityList2, memberId, goodsInfo);
        if (validActivityList.size() > 0) {
            return validActivityList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取订单级别的活动
     *
     * @param
     * @return
     */
    public List<ActMasterForm> getOrderActivity(String memberId, List<OrdConfirmGoodsExt> goodsInfo) {
        List<ActMasterForm> activityList = actMasterMapperExt.selectOrderActivity();
        // 过滤掉不符合条件的活动
        return filterValidActivity(activityList, memberId, goodsInfo);
    }

    private List<ActMasterForm> filterValidActivity(List<ActMasterForm> activityList, String memberId, List<OrdConfirmGoodsExt> goodsInfo) {
        // 获取首次购买的活动
        String firstBuyActivityId = null;
        ComSetting comSetting = comSettingMapper.selectByPrimaryKey("QYDS");
        if (comSetting != null) {
            firstBuyActivityId = comSetting.getFirstBuyActivity();
        }

        List<ActMasterForm> validActivityList = new ArrayList<ActMasterForm>();
        for (int i = 0; i < activityList.size(); i++) {
            if (activityList.get(i).getActivityId().equals(firstBuyActivityId)) {
                // 首次购买的活动
                // 判断该用户是不是首次购买
                OrdMaster ordMaster = new OrdMaster();
                ordMaster.setPayStatus("20");
                ordMaster.setMemberId(memberId);
                int payCount = ordMasterMapperExt.getOrderCount(ordMaster);
                if (payCount > 0) {
                    // 不是一次购买,则这个订单活动是无效的，过滤掉
                    continue;
                }
            }

            // 1.判断是否满足优惠会员要求
            if (!isContainsMember(activityList.get(i), memberId)) {
                continue;
            }
            // 判断是否要求单品折扣
            if ("1".equals(activityList.get(i).getHasSubActivity())) {
                // 取得单品活动
                ActSub actSub = new ActSub();
                actSub.setActivityId(activityList.get(i).getActivityId());
                List<ActMasterForm> subActList = actSubMapperExt.selectByActivityId(actSub);
                boolean isAllInSubActivity = isAllInSubActivity(subActList, goodsInfo);
                if (!isAllInSubActivity) {
                    // 跳过不符合
                    continue;
                }
            }
            // 2.过滤无效的商品
            // 2.1 通过要求正价或折扣过滤
            List<OrdConfirmGoodsExt> validGoods1 = new ArrayList<OrdConfirmGoodsExt>();
            if (ComCode.CouponOriginPriceType.MUST_ORIGIN_PRICE.equals(activityList.get(i).getIsOriginPrice())) {
                // 要求正价参与计算
                validGoods1 = getGoodsInOriginPrice(goodsInfo);
            } else if (ComCode.CouponOriginPriceType.MUST_DISCOUNT_PRICE.equals(activityList.get(i).getIsOriginPrice())) {
                // 要求折扣价参与计算
                validGoods1 = getGoodsInDiscountPrice(goodsInfo);
            } else {
                validGoods1 = goodsInfo;
            }
            // 2.2 计算过滤不符合条件的商品
            List<OrdConfirmGoodsExt> validGoods2 = new ArrayList<OrdConfirmGoodsExt>();
            List<ActGoodsForm> goodsList = null;
            ActGoods goods = new ActGoods();
            goods.setActivityId(activityList.get(i).getActivityId());
            // 是否有指定的商品类型
            if (ComCode.ActivityGoodsType.ALL.equals(activityList.get(i).getGoodsType())) {
                //全部商品
                validGoods2 = validGoods1;
            } else if (ComCode.ActivityGoodsType.TYPE.equals(activityList.get(i).getGoodsType())) {
                // 按分类
                goodsList = actGoodsMapperExt.selectGoodsTypeByActivityId(goods);
                validGoods2 = getGoodsInType(validGoods1, goodsList);
            } else if (ComCode.ActivityGoodsType.BRAND.equals(activityList.get(i).getGoodsType())) {
                // 按品牌
                goodsList = actGoodsMapperExt.selectGoodsBrandByActivityId(goods);
                validGoods2 = getGoodsInBrand(validGoods1, goodsList);
            } else if (ComCode.ActivityGoodsType.GOODS.equals(activityList.get(i).getGoodsType())) {
                // 按商品
                goodsList = actGoodsMapperExt.selectGoodsByActivityId(goods);
                validGoods2 = getGoodsInGoods(validGoods1, goodsList);
            } else if (ComCode.ActivityGoodsType.SKU.equals(activityList.get(i).getGoodsType())) {
                // 按SKU
                goodsList = actGoodsMapperExt.selectSkuByActivityId(goods);
                validGoods2 = getGoodsInSku(validGoods1, goodsList);
            }

            // 3.判断是否满足最少订单金额条件
            boolean isTotalPriceValid = isTotalPriceValid(activityList.get(i), validGoods2);
            if (!isTotalPriceValid) {
                continue;
            }
            // 计算活动价
            ActMasterForm activity = bindActivityPrice(activityList.get(i), goodsInfo);
            // 添加到有效的活动列表中
            validActivityList.add(activity);
        }
        return validActivityList;
    }

    private boolean isContainsMember(ActMasterForm activity, String memberId) {
        boolean isContainsMember = false;

        if (memberId == null || memberId.trim().length() == 0) {
            if (ComCode.ActivityMemberType.ALL.equals(activity.getMemberType())) {
                //全部会员
                return true;
            } else {
                return false;
            }
        }

        if (ComCode.ActivityMemberType.ALL.equals(activity.getMemberType())) {
            // 判断活动用户是否包含该用户
            //全部会员
            isContainsMember = true;
        } else if (ComCode.ActivityMemberType.MEMBER_GROUP.equals(activity.getMemberType())) {
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
        } else if (ComCode.ActivityMemberType.MEMBER_LEVEL.equals(activity.getMemberType())) {
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

    private boolean isAllInSubActivity(List<ActMasterForm> subActList, List<OrdConfirmGoodsExt> goodsInfo) {
        boolean isAllInSubActivity = true;
        for (int i = 0; i < goodsInfo.size(); i++) {
            // 循环商品,对比活动
            if (goodsInfo.get(i).getActivity() == null || goodsInfo.get(i).getActivity().getActivityId() == null) {
                // 没参加活动
                isAllInSubActivity = false;
                break;
            }
            String activiyId = goodsInfo.get(i).getActivity().getActivityId();
            boolean isInSubActivity = false;
            for (int j = 0; j < subActList.size(); j++) {
                if (activiyId.equals(subActList.get(j).getActivityId())) {
                    isInSubActivity = true;
                    break;
                }
            }
            if (isInSubActivity) {
                continue;
            } else {
                isAllInSubActivity = false;
                break;
            }
        }
        return isAllInSubActivity;
    }

    private boolean isTotalPriceValid(ActMasterForm activity, List<OrdConfirmGoodsExt> goodsInfo) {
        // 获取模板参数template = actTemplateMapperExt.selectById(template);
        ActTempParam actTempParam = new ActTempParam();
        actTempParam.setTempId(activity.getTempId());
        List<ActTempParam> paramList = actTempParamMapperExt.selectByTempId(actTempParam);
        float totalPrice = getGoodsTotalPrice(goodsInfo);
        boolean isTotalPriceValid = false;
        for (int i = 0; i < paramList.size(); i++) {
            // price是否大于等于所有优惠条件中的件数
            if (totalPrice >= Float.valueOf(paramList.get(i).getParamCondition())) {
                isTotalPriceValid = true;
                break;
            }
        }
        return isTotalPriceValid;
    }

    private ActMasterForm bindActivityPrice(ActMasterForm activity, List<OrdConfirmGoodsExt> goodsInfo) {
        //获取活动优惠信息
        ActTemplateForm template = new ActTemplateForm();
        template.setTempId(activity.getTempId());

        // 获取模板参数template = actTemplateMapperExt.selectById(template);
        ActTempParam actTempParam = new ActTempParam();
        actTempParam.setTempId(template.getTempId());
        List<ActTempParam> paramList = actTempParamMapperExt.selectByTempId(actTempParam);
        template.setParamList(paramList);
        template.setActitionType(activity.getActivityType());
        // 判断是否符合调
        float totalPrice = getGoodsTotalPrice(goodsInfo);
        float newPrice = getActivityPrice(totalPrice, template, 1, null, null, null);
        activity.setNewPrice(newPrice);
        activity.setOriginPrice(totalPrice);

        // 优惠的价钱 原价-优惠价
        activity.setCutPrice(new BigDecimal(totalPrice - newPrice).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());

        if (ComCode.ActivityType.FULL_SEND.equals(template.getActitionType())) {
            // 如果是满送,则判断是否符合条件,如果符合,选出赠送的优惠券ID
            // 满减
            int j = -1;
            for (int i = 0; i < template.getParamList().size(); i++) {
                // price是否大于等于所有优惠条件中的件数
                if (totalPrice >= Float.valueOf(template.getParamList().get(i).getParamCondition())) {
                    j = i;
                } else {
                    break;
                }
            }
            if (j >= 0) {
                // 赠送的优惠券ID
                activity.setSendCouponId(template.getParamList().get(j).getParamValue());
            }
        } else if (ComCode.ActivityType.FULL_SEND_GOODS.equals(template.getActitionType())) {
            if (paramList != null && paramList.size() >= 0) {
                String skuJson = paramList.get(0).getParamValue();
                if (!StringUtil.isEmpty(skuJson)) {
                    List<SkuForm> skuIdList = (List<SkuForm>) JSONArray.parseArray(skuJson, SkuForm.class);
                    // 拼成符合in的查询语句
                    StringBuffer inIdsSb = new StringBuffer();
                    for (int i = 0; i < skuIdList.size(); i++) {
                        inIdsSb.append("'");
                        inIdsSb.append(skuIdList.get(i).getGoodsCode());
                        inIdsSb.append("'");
                        if (i != skuIdList.size() - 1) {
                            inIdsSb.append(",");
                        }
                    }
                    List<GdsMasterExt> goodsList = gdsMasterMapperExt.getGoodsListByGoodsCode(inIdsSb.toString());
                    for (int i = 0; i < goodsList.size(); i++) {
                        goodsList.get(i).setActivityColorCodeList(new ArrayList<String>());
                        for (int j = 0; j < skuIdList.size(); j++) {
                            if (goodsList.get(i).getGoodsCode().equals(skuIdList.get(j).getGoodsCode())) {
                                goodsList.get(i).getActivityColorCodeList().add(skuIdList.get(j).getColorCode());
                            }
                        }
                    }
                    activity.setSendGoodsList(goodsList);
                }
            }

        } else if (ComCode.ActivityType.FULL_SEND_POINT.equals(template.getActitionType())) {
            // 如果是满送积分,则判断是否符合条件,如果符合,选出赠送的优惠券ID
            // 满减
            int j = -1;
            for (int i = 0; i < template.getParamList().size(); i++) {
                // price是否大于等于所有优惠条件中的件数
                if (totalPrice >= Float.valueOf(template.getParamList().get(i).getParamCondition())) {
                    j = i;
                } else {
                    break;
                }
            }
            if (j >= 0) {
                // 赠送的优惠券ID
                activity.setSendPoint(Integer.valueOf(template.getParamList().get(j).getParamValue()));
            }
        }
        return activity;
    }

    private float getGoodsTotalPrice(List<OrdConfirmGoodsExt> validGoods) {
        BigDecimal goodsTotalPrice = new BigDecimal("0");
        goodsTotalPrice = goodsTotalPrice.setScale(2, RoundingMode.HALF_UP);
        for (int i = 0; i < validGoods.size(); i++) {
            BigDecimal goodsPrice = null;
            if (!StringUtil.isEmpty(validGoods.get(i).getActGoodsId())) {
                goodsPrice = new BigDecimal(String.valueOf(validGoods.get(i).getActivity().getNewPrice()));
            } else {
                goodsPrice = new BigDecimal(validGoods.get(i).getOrdConfirmOrderUnitExtList().get(0).getPrice());
            }
            goodsPrice = goodsPrice.setScale(2, RoundingMode.HALF_UP);
            BigDecimal quantity = new BigDecimal(validGoods.get(i).getQuantity());
            goodsTotalPrice = goodsTotalPrice.add(goodsPrice.multiply(quantity));
        }
        return goodsTotalPrice.floatValue();
    }

    /**
     * 获取原价的商品
     *
     * @param goodsInfo
     * @return
     */
    private List<OrdConfirmGoodsExt> getGoodsInOriginPrice(List<OrdConfirmGoodsExt> goodsInfo) {
        List<OrdConfirmGoodsExt> validGoods = new ArrayList<OrdConfirmGoodsExt>();
        for (int j = 0; j < goodsInfo.size(); j++) {

            if (goodsInfo.get(j).getActivity() != null
                    && goodsInfo.get(j).getActivity().getActivityId() != null
                    && !"1".equals(goodsInfo.get(j).getActivity().getIsMemberActivity())) {
                // 有活动,就认为不是正价,这里不包含会员的固定活动
                continue;
            } else {
                validGoods.add(goodsInfo.get(j));
            }
        }
        return validGoods;
    }

    /**
     * 获取打折的商品
     *
     * @param goodsInfo
     * @return
     */
    private List<OrdConfirmGoodsExt> getGoodsInDiscountPrice(List<OrdConfirmGoodsExt> goodsInfo) {
        List<OrdConfirmGoodsExt> validGoods = new ArrayList<OrdConfirmGoodsExt>();
        for (int j = 0; j < goodsInfo.size(); j++) {
            // 没有活动,就认为是正价,会员的固定活动也算是正价
            if (goodsInfo.get(j).getActivity() == null
                    || goodsInfo.get(j).getActivity().getActivityId() == null
                    || goodsInfo.get(j).getActGoodsId() == null) {
                continue;
            } else if ("1".equals(goodsInfo.get(j).getActivity().getIsMemberActivity())) {
                // 会员的固定活动认为是正价
                continue;
            } else {
                validGoods.add(goodsInfo.get(j));
            }
        }
        return validGoods;
    }

    /**
     * 过滤指定分类的商品
     *
     * @param goodsInfo
     * @param goodsTypeList
     * @return
     */
    private List<OrdConfirmGoodsExt> getGoodsInType(List<OrdConfirmGoodsExt> goodsInfo, List<ActGoodsForm> goodsTypeList) {
        List<OrdConfirmGoodsExt> validGoods = new ArrayList<OrdConfirmGoodsExt>();
        for (int i = 0; i < goodsInfo.size(); i++) {
            GdsMaster goodsDetail = gdsMasterMapper.selectByPrimaryKey(goodsInfo.get(i).getGoodsId());
            boolean isGoodsInType = false;
            for (int j = 0; j < goodsTypeList.size(); j++) {
                if (goodsDetail.getGoodsTypeIdPath() != null && goodsDetail.getGoodsTypeIdPath().contains(goodsTypeList.get(j).getGoodsId())) {
                    isGoodsInType = true;
                    break;
                }
            }
            if (isGoodsInType) {
                validGoods.add(goodsInfo.get(i));
            }
        }
        return validGoods;
    }

    /**
     * 过滤指定品牌的商品
     *
     * @param goodsInfo
     * @param goodsBrandList
     * @return
     */
    private List<OrdConfirmGoodsExt> getGoodsInBrand(List<OrdConfirmGoodsExt> goodsInfo, List<ActGoodsForm> goodsBrandList) {
        List<OrdConfirmGoodsExt> validGoods = new ArrayList<OrdConfirmGoodsExt>();
        for (int i = 0; i < goodsInfo.size(); i++) {
            GdsMaster goodsDetail = gdsMasterMapper.selectByPrimaryKey(goodsInfo.get(i).getGoodsId());
            boolean isGoodsInBrand = false;
            for (int j = 0; j < goodsBrandList.size(); j++) {
                if (goodsBrandList.get(j).getGoodsId().equals(goodsDetail.getBrandId())) {
                    isGoodsInBrand = true;
                    break;
                }
            }
            if (isGoodsInBrand) {
                validGoods.add(goodsInfo.get(i));
            }
        }
        return validGoods;
    }

    /**
     * 过滤指定款的商品
     *
     * @param goodsInfo
     * @param goodsList
     * @return
     */
    private List<OrdConfirmGoodsExt> getGoodsInGoods(List<OrdConfirmGoodsExt> goodsInfo, List<ActGoodsForm> goodsList) {
        List<OrdConfirmGoodsExt> validGoods = new ArrayList<OrdConfirmGoodsExt>();
        for (int i = 0; i < goodsInfo.size(); i++) {
            boolean isGoodsInGoods = false;
            for (int j = 0; j < goodsList.size(); j++) {
                if (goodsList.get(j).getGoodsId().equals(goodsInfo.get(i).getGoodsId())) {
                    isGoodsInGoods = true;
                    break;
                }
            }
            if (isGoodsInGoods) {
                validGoods.add(goodsInfo.get(i));
            }
        }
        return validGoods;
    }

    /**
     * 过滤指定SKU的商品
     *
     * @param goodsInfo
     * @param goodsList
     * @return
     */
    private List<OrdConfirmGoodsExt> getGoodsInSku(List<OrdConfirmGoodsExt> goodsInfo, List<ActGoodsForm> goodsList) {
        List<OrdConfirmGoodsExt> validGoods = new ArrayList<OrdConfirmGoodsExt>();
        for (int i = 0; i < goodsInfo.size(); i++) {
            boolean isGoodsInGoods = false;
            // 套装不计算
            if (!"30".equals(goodsInfo.get(i).getType())) {
                for (int j = 0; j < goodsList.size(); j++) {
                    if (goodsList.get(j).getGoodsId().equals(goodsInfo.get(i).getGoodsId())
                            && goodsList.get(j).getSkuId().equals(goodsInfo.get(i).getOrdConfirmOrderUnitExtList().get(0).getSkuId())) {
                        isGoodsInGoods = true;
                        break;
                    }
                }
            }
            if (isGoodsInGoods) {
                validGoods.add(goodsInfo.get(i));
            }
        }
        return validGoods;
    }

    /**
     * 获取某个商品的所有活动中最便宜的价钱
     *
     * @param skuInfo
     * @return
     */
    public JSONObject getNewPricesBySku(SkuForm skuInfo) {
        JSONObject json = new JSONObject();
        try {
            // 取出当前商品的所有活动
            List<ActMasterForm> activities = new ArrayList<ActMasterForm>();
            List<ActMasterForm> activityList = actMasterMapperExt.selectActivitiesBySkuInfo(skuInfo);
            for (int j = 0; j < activityList.size(); j++) {
                if (checkSecKillActivity(activityList.get(j))) {
                    ActMasterForm activity = checkActivity(activityList.get(j), skuInfo.getMemberId(), skuInfo.getPrice(), 1, skuInfo.getGoodsId(), skuInfo.getSkuid());
                    if (activity != null) {
                        activities.add(activity);
                    }
                }

            }
            if (activities.size() > 0) {
                //对活动按照价格排序
                Collections.sort(activities, new Comparator<ActMasterForm>() {
                    public int compare(ActMasterForm o1, ActMasterForm o2) {
                        // 其余的按照价格从小到大排序
                        if (o2.getNewPrice() - o1.getNewPrice() > 0) {
                            return -1;
                        } else if (o2.getNewPrice() - o1.getNewPrice() < 0) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
                json.put("activityType", activities.get(0).getActivityType());
                json.put("activityName", activities.get(0).getActivityName());
                json.put("data", activities.get(0).getNewPrice());
            } else {
                json.put("data", null);
            }
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
     * 购物车的活动列表，单品活动
     *
     * @param goodsList
     * @param memberId
     * @return
     */
    public JSONObject bindActivityForShopingBag(List<MmbShoppingBagExt> goodsList, String memberId) {
        JSONObject json = new JSONObject();
        try {

            for (int i = 0; i < goodsList.size(); i++) {
                // 取出当前商品的所有活动
                List<ActMasterForm> activities = new ArrayList<ActMasterForm>();
                float price = 0;
                SkuForm skuInfo = null;
                List<ActMasterForm> activityList = null;
                String goodsId = goodsList.get(i).getGoodsId();
                String skuId = null;
                if (goodsList.get(i).getSkuList().size() == 1) {
                    skuId = goodsList.get(i).getSkuList().get(0).getSkuId();
                    skuInfo = skuMapperExt.selectBySkuId(goodsList.get(i).getSkuList().get(0).getSkuId());
                    price = skuInfo.getPrice();
                    activityList = actMasterMapperExt.selectActivitiesBySkuInfo(skuInfo);
                } else {
                    StringBuffer skuBuffer = new StringBuffer();
                    for (int j = 0; j < goodsList.get(i).getSkuList().size(); j++) {
                        skuBuffer.append("'");
                        skuBuffer.append(goodsList.get(i).getSkuList().get(j).getSkuId());
                        skuBuffer.append("'");
                        if (j != goodsList.get(i).getSkuList().size() - 1) {
                            skuBuffer.append(",");
                        }
                    }
                    price = skuMapperExt.getSuitPrice(skuBuffer.toString());

                    skuInfo = new SkuForm();
                    skuInfo.setGoodsId(goodsList.get(i).getGoodsId());
                    GdsMaster suit = gdsMasterMapper.selectByPrimaryKey(goodsList.get(i).getGoodsId());
                    skuInfo.setBrandId(suit.getBrandId());
                    skuInfo.setGoodsTypeId(suit.getGoodsTypeId());
                    activityList = actMasterMapperExt.selectActivitiesBySkuInfo(skuInfo);
                }

                for (int j = 0; j < activityList.size(); j++) {
                    if (checkSecKillActivity(activityList.get(j))) {
                        ActMasterForm activity = checkActivity(activityList.get(j), memberId, price, goodsList.get(i).getQuantity(), goodsId, skuId);
                        if (activity != null) {
                            activities.add(activity);
                        }
                    }
                }
                final String selectedActId = goodsList.get(i).getActGoodsId();
                //对活动按照价格排序
                Collections.sort(activities, new Comparator<ActMasterForm>() {
                    public int compare(ActMasterForm o1, ActMasterForm o2) {
                        // 选中的activity排第一
                        if (o2.getActivityId().equals(selectedActId)) {
                            return 1;
                        } else if (o1.getActivityId().equals(selectedActId)) {
                            return -1;
                        }
                        // 其余的按照价格从小到大排序
                        if (o2.getNewPrice() - o1.getNewPrice() > 0) {
                            return -1;
                        } else if (o2.getNewPrice() - o1.getNewPrice() < 0) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
                goodsList.get(i).setActivityList(activities);

            }
            json.put("results", goodsList);
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
     * 检查秒杀活动商品
     *
     * @param activityId
     * @param goodsId
     * @param skuId
     * @return
     */
    private void checkActGoodsInfo(String activityId, String goodsId, String skuId, String memberId, Integer quantity) {
        if (StringUtil.isEmpty(activityId)
                || StringUtil.isEmpty(goodsId)
                || StringUtil.isEmpty(skuId)
                || StringUtil.isEmpty(memberId)) {
            return;
        }

        ActGoods goodsForm = new ActGoods();
        goodsForm.setActivityId(activityId);
        goodsForm.setGoodsId(goodsId);
        goodsForm.setSkuId(skuId);
        List<ActGoods> list = actGoodsMapperExt.selectActGoodsInfo(goodsForm);
        ActGoods actGoods;
        if (list != null && list.size() == 1) {
            actGoods = list.get(0);
        } else {
            throw new ExceptionBusiness("秒杀活动已经结束!");
        }

        if (actGoods.getSurplus() < quantity) {
            throw new ExceptionBusiness("秒杀商品已经被抢光!");
        }

        if (actGoods.getBuyMax() != null && actGoods.getBuyMax() > 0) {
            OrdSubList subList = new OrdSubList();
            subList.setSkuId(skuId);
            subList.setActionId(activityId);
            subList.setInsertUserId(memberId);
            int count = ordSubListMapperExt.countMemberSecKillGoods(subList);
            if (count + quantity > actGoods.getBuyMax()) {
                throw new ExceptionBusiness("秒杀商品数量超过购买限制！此次可购买" + (actGoods.getBuyMax() - count) + "件");
            }
        }
    }

    private void checkSecKillDateTime(ActMasterForm activity) {

        if (!ComCode.ActivityType.SECOND_KILL.equals(activity.getActivityType())) {
            return;
        }

        Date current = new Date();
        if (current.before(activity.getStartTime())) {
            throw new ExceptionBusiness("秒杀活动还未开始");
        }

        if (current.after(activity.getEndTime())) {
            throw new ExceptionBusiness("秒杀活动已结束");
        }

        try {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat ydf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String currentDateStr = sdf.format(current);

            String startTime = currentDateStr + " " + df.format(activity.getStartTime());
            String endTime = currentDateStr + " " + df.format(activity.getEndTime());

            if (ydf.parse(endTime).before(current)) {
                throw new ExceptionBusiness("秒杀活动已结束");
            }

            if (ydf.parse(startTime).after(current)) {
                throw new ExceptionBusiness("秒杀活动还未开始");
            }

        } catch (ExceptionBusiness e) {
            throw new ExceptionBusiness(e.getMessage());
        } catch (Exception e) {
            throw new ExceptionBusiness("秒杀活动时间不正确");
        }
    }

    @Override
    public void checkSecKillActivity(OrdConfirmGoodsExt goodsInfo, String memberId) {
        // 从立即购买进入的,则自动为用户选择最便宜的活动
        // 取出当前商品的所有活动
        List<ActMasterForm> activities = new ArrayList<ActMasterForm>();
        List<ActMasterForm> activityList = null;
        float price = 0;
        String goodsId = null;
        String skuId = null;
        // 计算商品原价
        if (goodsInfo.getOrdConfirmOrderUnitExtList().size() == 1) {
            goodsId = goodsInfo.getGoodsId();
            skuId = goodsInfo.getOrdConfirmOrderUnitExtList().get(0).getSkuId();
            // 如果是单品,直接获取sku的价格
            SkuForm skuInfo = skuMapperExt.selectBySkuId(goodsInfo.getOrdConfirmOrderUnitExtList().get(0).getSkuId());
            price = skuInfo.getPrice();
            activityList = actMasterMapperExt.selectActivitiesBySkuInfo(skuInfo);
        } else {
            goodsId = goodsInfo.getGoodsId();
            // 如果是套装,价格等于每个sku价格之和
            StringBuffer skuBuffer = new StringBuffer();
            for (int j = 0; j < goodsInfo.getOrdConfirmOrderUnitExtList().size(); j++) {
                skuBuffer.append("'");
                skuBuffer.append(goodsInfo.getOrdConfirmOrderUnitExtList().get(j).getSkuId());
                skuBuffer.append("'");
                if (j != goodsInfo.getOrdConfirmOrderUnitExtList().size() - 1) {
                    skuBuffer.append(",");
                }
            }
            price = skuMapperExt.getSuitPrice(skuBuffer.toString());
            SkuForm skuInfo = new SkuForm();
            skuInfo.setGoodsId(goodsInfo.getGoodsId());
            GdsMaster suit = gdsMasterMapper.selectByPrimaryKey(goodsInfo.getGoodsId());
            skuInfo.setBrandId(suit.getBrandId());
            skuInfo.setGoodsTypeId(suit.getGoodsTypeId());
            activityList = actMasterMapperExt.selectActivitiesBySkuInfo(skuInfo);
        }

        for (int j = 0; j < activityList.size(); j++) {
            if (checkSecKillActivity(activityList.get(j))) {// 如果是秒杀，先判断是否满足秒杀条件
                ActMasterForm activity = checkActivity(activityList.get(j), memberId, price, Integer.valueOf(goodsInfo.getQuantity()), goodsId, skuId);
                if (activity != null) {
                    activities.add(activity);
                }
            }
        }
        //对活动按照价格排序
        Collections.sort(activities, new Comparator<ActMasterForm>() {
            public int compare(ActMasterForm o1, ActMasterForm o2) {
                // 其余的按照价格从小到大排序
                if (o2.getNewPrice() - o1.getNewPrice() > 0) {
                    return -1;
                } else if (o2.getNewPrice() - o1.getNewPrice() < 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        if (activities.size() > 0) {
            ActMasterForm activity = activities.get(0);
            if (ComCode.ActivityType.SECOND_KILL.equals(activity.getActivityType())) {
                // 秒杀
                // 检查秒杀库存及秒杀订单数量
                checkActGoodsInfo(activity.getActivityId(),
                        goodsInfo.getGoodsId(),
                        goodsInfo.getOrdConfirmOrderUnitExtList().get(0).getSkuId(),
                        memberId,
                        goodsInfo.getQuantity());
            }

        }
    }

    /**
     * 订单确认的绑定活动，单品活动
     *
     * @param goodsList
     * @return
     */
    public List<OrdConfirmGoodsExt> bindActivityForOrderConfirm(List<OrdConfirmGoodsExt> goodsList, String memberId, boolean isShopFromShoppingBag) {
        // isShopFromShoppingBag:
        // true 来自购物车,可能有多种商品;
        // false:来自立即购买,只可能有一种sku
        if (isShopFromShoppingBag) {
            // 按照活动分组
            return bindActivityForGoodsList(goodsList, memberId);
        } else {
            //只可能有一种sku,并且没有选择活动,需要计算一种最便宜的活动
            for (int i = 0; i < goodsList.size(); i++) {
                // 从立即购买进入的,则自动为用户选择最便宜的活动
                // 取出当前商品的所有活动
                List<ActMasterForm> activities = new ArrayList<ActMasterForm>();
                List<ActMasterForm> activityList = null;
                float price = 0;
                String goodsId = null;
                String skuId = null;
                // 计算商品原价
                if (goodsList.get(i).getOrdConfirmOrderUnitExtList().size() == 1) {
                    goodsId = goodsList.get(i).getGoodsId();
                    skuId = goodsList.get(i).getOrdConfirmOrderUnitExtList().get(0).getSkuId();
                    // 如果是单品,直接获取sku的价格
                    SkuForm skuInfo = skuMapperExt.selectBySkuId(goodsList.get(i).getOrdConfirmOrderUnitExtList().get(0).getSkuId());
                    price = skuInfo.getPrice();
                    activityList = actMasterMapperExt.selectActivitiesBySkuInfo(skuInfo);
                } else {
                    goodsId = goodsList.get(i).getGoodsId();
                    // 如果是套装,价格等于每个sku价格之和
                    StringBuffer skuBuffer = new StringBuffer();
                    for (int j = 0; j < goodsList.get(i).getOrdConfirmOrderUnitExtList().size(); j++) {
                        skuBuffer.append("'");
                        skuBuffer.append(goodsList.get(i).getOrdConfirmOrderUnitExtList().get(j).getSkuId());
                        skuBuffer.append("'");
                        if (j != goodsList.get(i).getOrdConfirmOrderUnitExtList().size() - 1) {
                            skuBuffer.append(",");
                        }
                    }
                    price = skuMapperExt.getSuitPrice(skuBuffer.toString());
                    SkuForm skuInfo = new SkuForm();
                    skuInfo.setGoodsId(goodsList.get(i).getGoodsId());
                    GdsMaster suit = gdsMasterMapper.selectByPrimaryKey(goodsList.get(i).getGoodsId());
                    skuInfo.setBrandId(suit.getBrandId());
                    skuInfo.setGoodsTypeId(suit.getGoodsTypeId());
                    activityList = actMasterMapperExt.selectActivitiesBySkuInfo(skuInfo);
                }

                for (int j = 0; j < activityList.size(); j++) {
                    if (checkSecKillActivity(activityList.get(j))) {// 如果是秒杀，先判断是否满足秒杀条件
                        ActMasterForm activity = checkActivity(activityList.get(j), memberId, price, Integer.valueOf(goodsList.get(i).getQuantity()), goodsId, skuId);
                        if (activity != null) {
                            activities.add(activity);
                        }
                    }
                }
                //对活动按照价格排序
                Collections.sort(activities, new Comparator<ActMasterForm>() {
                    public int compare(ActMasterForm o1, ActMasterForm o2) {
                        // 其余的按照价格从小到大排序
                        if (o2.getNewPrice() - o1.getNewPrice() > 0) {
                            return -1;
                        } else if (o2.getNewPrice() - o1.getNewPrice() < 0) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
                if (activities.size() > 0) {
//                    if (ComCode.ActivityType.SECOND_KILL.equals(activities.get(0).getActivityType())
//                            && goodsList.get(i).getOrdConfirmOrderUnitExtList() != null
//                            && goodsList.get(i).getOrdConfirmOrderUnitExtList().size() > 0) {
//
//                        ActMasterForm activity = actMasterMapperExt.selectByPrimaryKey(activities.get(0).getActivityId());
//                        // 检查秒杀活动是否开始
//                        checkSecKillDateTime(activity);
//                        // 检查秒杀库存及秒杀订单数量
//                        checkActGoodsInfo(activity.getActivityId(),
//                                goodsList.get(i).getGoodsId(),
//                                goodsList.get(i).getOrdConfirmOrderUnitExtList().get(0).getSkuId(),
//                                memberId,
//                                goodsList.get(i).getQuantity());
//                    }
//                    goodsList.get(i).setActivity(activities.get(0));
                    goodsList.get(i).setActGoodsId(activities.get(0).getActivityId());
                } else {
                    goodsList.get(i).setActivity(null);
                    goodsList.get(i).setActGoodsId(null);
                }
            }
            return bindActivityForGoodsList(goodsList, memberId);
        }
    }

    @Override
    public void checkBindActivityForOrder(List<OrdConfirmGoodsExt> goodsList, String memberId) {
        Map<String, List<OrdConfirmGoodsExt>> groupMap = getGoodsGroupMap(goodsList);
        for (String key : groupMap.keySet()) {
            List<OrdConfirmGoodsExt> goods = groupMap.get(key);
            if (goods.get(0).getActGoodsId() != null) {
                // 有活动,根据Id取出活动信息
                ActMasterForm activity = actMasterMapperExt.selectByPrimaryKey(key);
                // 有活动
                if (activity != null
                        && ComCode.ActivityType.SECOND_KILL.equals(activity.getActivityType())
                        && isContainsMember(activity, memberId)) {
                    // 秒杀
                    for (int i = 0; i < goods.size(); i++) {
                        float orginPrice = getOrginPrice(goods.get(i));
                        float newPrice = getActivitySecKillPrice(activity, goods.get(i).getGoodsId(),
                                goods.get(i).getOrdConfirmOrderUnitExtList().get(0).getSkuId());
                        ActMasterForm activity1 = new ActMasterForm();
                        BeanUtils.copyProperties(activity, activity1);
                        activity1.setOriginPrice(orginPrice);
                        activity1.setNewPrice(newPrice);
                        goods.get(i).setActivity(activity1);

                        // 检查秒杀活动是否开始
                        checkSecKillDateTime(activity1);

                        if (goods.get(i).getOrdConfirmOrderUnitExtList() != null && goods.get(i).getOrdConfirmOrderUnitExtList().size() > 0) {
                            // 检查秒杀库存及秒杀订单数量
                            checkActGoodsInfo(activity1.getActivityId(),
                                    goods.get(i).getGoodsId(),
                                    goods.get(i).getOrdConfirmOrderUnitExtList().get(0).getSkuId(),
                                    memberId,
                                    goods.get(i).getQuantity());
                        }
                    }
                }
            }
        }
    }

    private List<OrdConfirmGoodsExt> bindActivityForGoodsList(List<OrdConfirmGoodsExt> goodsList, String memberId) {
        Map<String, List<OrdConfirmGoodsExt>> groupMap = getGoodsGroupMap(goodsList);
        List<OrdConfirmGoodsExt> newGoodsList = new ArrayList<OrdConfirmGoodsExt>();
        for (String key : groupMap.keySet()) {
            List<OrdConfirmGoodsExt> goods = groupMap.get(key);
            if (goods.get(0).getActGoodsId() == null) {
                // 没有活动直接循环添加到新列表中
                for (int i = 0; i < goods.size(); i++) {
                    newGoodsList.add(goods.get(i));
                }
            } else {
                // 有活动,根据Id取出活动信息
                ActMasterForm activity = actMasterMapperExt.selectByPrimaryKey(key);
                if (activity == null || !isContainsMember(activity, memberId)
                        || activity.getStartTime().compareTo(new Date()) > 0
                        || activity.getEndTime().compareTo(new Date()) < 0) {
                    // 活动为空或者该会员不符合活动条件
                    for (int i = 0; i < goods.size(); i++) {
                        goods.get(i).setActGoodsId(null);
                        goods.get(i).setActivity(null);
                        newGoodsList.add(goods.get(i));
                    }
                } else {
                    // 有活动
                    // 折扣活动
                    if (ComCode.ActivityType.DISCOUNT.equals(activity.getActivityType())) {
                        // 计算折扣的商品数量需要加起来算
                        int goodsCount = 0;
                        for (int i = 0; i < goods.size(); i++) {
                            goodsCount += goods.get(i).getQuantity();
                        }
                        // 获取最终折扣
                        float discount = getActivityDiscount(activity, goodsCount);
                        for (int i = 0; i < goods.size(); i++) {
                            // 计算每个商品的价格
                            float orginPrice = getOrginPrice(goods.get(i));
                            float newPrice = getDiscountPrice(orginPrice, discount);
                            ActMasterForm activity1 = new ActMasterForm();
                            BeanUtils.copyProperties(activity, activity1);
                            activity1.setOriginPrice(orginPrice);
                            activity1.setNewPrice(newPrice);
                            goods.get(i).setActivity(activity1);
                            newGoodsList.add(goods.get(i));
                        }
                    } else if (ComCode.ActivityType.DISCOUNT_TWO.equals(activity.getActivityType())) {
                        // 一二件折需要拆分
                        List<OrdConfirmGoodsExt> goods2 = new ArrayList<OrdConfirmGoodsExt>();
                        // 获取活动折扣
                        ActTemplateForm template = new ActTemplateForm();
                        template.setTempId(activity.getTempId());

                        // 获取模板参数template = actTemplateMapperExt.selectById(template);
                        ActTempParam actTempParam = new ActTempParam();
                        actTempParam.setTempId(template.getTempId());
                        List<ActTempParam> paramList = actTempParamMapperExt.selectByTempId(actTempParam);
                        float discount1 = 10;
                        float discount2 = 10;
                        if (paramList != null) {
                            if (paramList.size() > 0) {
                                discount1 = Float.parseFloat(paramList.get(0).getParamValue());
                            }
                            if (paramList.size() > 1) {
                                discount2 = Float.parseFloat(paramList.get(1).getParamValue());
                            }
                        }

                        for (int i = 0; i < goods.size(); i++) {
                            for (int j = 0; j < goods.get(i).getQuantity(); j++) {
                                OrdConfirmGoodsExt singleGoods = new OrdConfirmGoodsExt();
                                BeanUtils.copyProperties(goods.get(i), singleGoods);
                                singleGoods.setQuantity(1);
                                singleGoods.setPrice(String.valueOf(getOrginPrice(goods.get(i))));
                                goods2.add(singleGoods);
                            }
                        }
                        // 拆分后按照价格排序
                        Collections.sort(goods2, new Comparator<OrdConfirmGoodsExt>() {
                            public int compare(OrdConfirmGoodsExt o1, OrdConfirmGoodsExt o2) {
                                // 其余的按照价格从大到小排序
                                if (Float.parseFloat(o1.getPrice()) - Float.parseFloat(o2.getPrice()) > 0) {
                                    return -1;
                                } else if (Float.parseFloat(o1.getPrice()) - Float.parseFloat(o2.getPrice()) < 0) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                        });
                        for (int i = 0; i < goods2.size() / 2; i++) {
                            // 第一件取最大的
                            ActMasterForm activity1 = new ActMasterForm();
                            // 第二件取最小的
                            ActMasterForm activity2 = new ActMasterForm();
                            BeanUtils.copyProperties(activity, activity1);
                            BeanUtils.copyProperties(activity, activity2);
                            float orginPrice1 = Float.valueOf(goods2.get(i).getPrice());
                            float orginPrice2 = Float.valueOf(goods2.get(goods2.size() - i - 1).getPrice());
                            float newPrice1 = orginPrice1 * (discount1 * 10) / 100;
                            float newPrice2 = orginPrice2 * (discount2 * 10) / 100;
                            activity1.setOriginPrice(orginPrice1);
                            activity2.setOriginPrice(orginPrice2);
                            activity1.setNewPrice(newPrice1);
                            if(newPrice2 == 0.0){
                                newPrice2 = 1.0f;
                            }
                            activity2.setNewPrice(newPrice2);
                            goods2.get(i).setActivity(activity1);
                            goods2.get(goods2.size() - i - 1).setActivity(activity2);
                            newGoodsList.add(goods2.get(i));
                            newGoodsList.add(goods2.get(goods2.size() - i - 1));
                        }
                        if (goods2.size() % 2 != 0) {
                            int middleIndex = goods2.size() / 2;
                            ActMasterForm activity3 = new ActMasterForm();
                            BeanUtils.copyProperties(activity, activity3);
                            float orginPrice3 = Float.valueOf(goods2.get(middleIndex).getPrice());
                            float newPrice3 = orginPrice3 * (discount1 * 10) / 100;
                            activity3.setOriginPrice(orginPrice3);
                            activity3.setNewPrice(newPrice3);
                            goods2.get(middleIndex).setActivity(activity3);
                            newGoodsList.add(goods2.get(middleIndex));
                        }
                    } else if (ComCode.ActivityType.SPECIAL_PRICE.equals(activity.getActivityType())) {
                        // 特价
                        // 其他活动也是直接添加到新列表中
                        for (int i = 0; i < goods.size(); i++) {
                            float orginPrice = getOrginPrice(goods.get(i));
                            float newPrice = getActivitySpecialPrice(activity);
                            ActMasterForm activity1 = new ActMasterForm();
                            BeanUtils.copyProperties(activity, activity1);
                            activity1.setOriginPrice(orginPrice);
                            activity1.setNewPrice(newPrice);
                            goods.get(i).setActivity(activity1);
                            newGoodsList.add(goods.get(i));
                        }
                    } else if (ComCode.ActivityType.SECOND_KILL.equals(activity.getActivityType())) {
                        // 秒杀
                        // 其他活动也是直接添加到新列表中
                        for (int i = 0; i < goods.size(); i++) {
                            float orginPrice = getOrginPrice(goods.get(i));
                            float newPrice = getActivitySecKillPrice(activity, goods.get(i).getGoodsId(),
                                    goods.get(i).getOrdConfirmOrderUnitExtList().get(0).getSkuId());
                            ActMasterForm activity1 = new ActMasterForm();
                            BeanUtils.copyProperties(activity, activity1);
                            activity1.setOriginPrice(orginPrice);
                            activity1.setNewPrice(newPrice);
                            goods.get(i).setActivity(activity1);

                            // 检查秒杀活动是否开始
                            checkSecKillDateTime(activity1);

                            if (goods.get(i).getOrdConfirmOrderUnitExtList() != null && goods.get(i).getOrdConfirmOrderUnitExtList().size() > 0) {
                                // 检查秒杀库存及秒杀订单数量
                                checkActGoodsInfo(activity1.getActivityId(),
                                        goods.get(i).getGoodsId(),
                                        goods.get(i).getOrdConfirmOrderUnitExtList().get(0).getSkuId(),
                                        memberId,
                                        goods.get(i).getQuantity());
                            }

                            newGoodsList.add(goods.get(i));
                        }
                    } else if (ComCode.ActivityType.INTEGRAL_EXCHANGE.equals(activity.getActivityType())) {
                        // 积分换购
                        for (int i = 0; i < goods.size(); i++) {
                            ActMasterForm activity1 = new ActMasterForm();
                            BeanUtils.copyProperties(activity, activity1);
                            activity1 = getPointAndPrice(activity1);
                            goods.get(i).setActivity(activity1);
                            newGoodsList.add(goods.get(i));
                        }
                    } else if (ComCode.ActivityType.FULL_SEND_GOODS.equals(activity.getActivityType())) {
                        // 加钱赠送的货品
                        for (int i = 0; i < goods.size(); i++) {
                            float orginPrice = getOrginPrice(goods.get(i));
                            float newPrice = activity.getNeedFee().floatValue();
                            ActMasterForm activity1 = new ActMasterForm();
                            BeanUtils.copyProperties(activity, activity1);
                            activity1.setOriginPrice(orginPrice);
                            activity1.setNewPrice(newPrice);
                            goods.get(i).setActivity(activity1);
                            newGoodsList.add(goods.get(i));
                        }
                    }
                }
            }
        }
        return newGoodsList;
    }

    private float getDiscountPrice(float orginPrice, float discount) {
        BigDecimal oldPrice = new BigDecimal(String.valueOf(orginPrice));
        oldPrice = oldPrice.setScale(2, RoundingMode.HALF_UP);

        BigDecimal discountB = new BigDecimal(String.valueOf(discount));
        discountB = discountB.setScale(1, RoundingMode.HALF_UP);
        BigDecimal newPrice = oldPrice.multiply(discountB).divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP);
        newPrice = newPrice.setScale(2, RoundingMode.HALF_UP);
        return newPrice.floatValue();
    }

    private ActMasterForm getPointAndPrice(ActMasterForm activity) {
        ActTemplateForm template = new ActTemplateForm();
        template.setTempId(activity.getTempId());
        // 获取模板参数template = actTemplateMapperExt.selectById(template);
        ActTempParam actTempParam = new ActTempParam();
        actTempParam.setTempId(template.getTempId());
        List<ActTempParam> paramList = actTempParamMapperExt.selectByTempId(actTempParam);
        // 积分换购
        float newPrice = Float.parseFloat(paramList.get(0).getParamValue());
        int point = Integer.parseInt(paramList.get(0).getParamCondition());
        activity.setNewPrice(newPrice);
        activity.setPoint(point);
        return activity;
    }

    /**
     * 获取秒杀活动的特价
     *
     * @param activity
     * @return
     */
    private float getActivitySecKillPrice(ActMasterForm activity, String goodsId, String skuId) {

        ActGoods actGoods = new ActGoods();
        actGoods.setActivityId(activity.getActivityId());
        actGoods.setGoodsId(goodsId);
        actGoods.setSkuId(skuId);
        List<ActGoods> actGoodsList = actGoodsMapperExt.selectActGoodsInfo(actGoods);
        if (actGoodsList != null && actGoodsList.size() > 0) {
            actGoods = actGoodsList.get(0);
            BigDecimal price = actGoods.getActPrice().setScale(2, RoundingMode.HALF_UP);
            price = price.setScale(2, RoundingMode.HALF_UP);
            return price.floatValue();
        } else {
            throw new ExceptionBusiness("秒杀商品价格取得失败");
        }
    }

    /**
     * 获取特价活动的特价
     *
     * @param activity
     * @return
     */
    private float getActivitySpecialPrice(ActMasterForm activity) {
        // 获取模板参数
        ActTempParam actTempParam = new ActTempParam();
        actTempParam.setTempId(activity.getTempId());
        List<ActTempParam> paramList = actTempParamMapperExt.selectByTempId(actTempParam);
        BigDecimal price = new BigDecimal(paramList.get(0).getParamValue());
        price = price.setScale(2, RoundingMode.HALF_UP);
        return price.floatValue();
    }

    /**
     * 获取折扣活动的折扣
     *
     * @param activity
     * @param goodsCount
     * @return
     */
    private float getActivityDiscount(ActMasterForm activity, int goodsCount) {
        ActTempParam actTempParam = new ActTempParam();
        actTempParam.setTempId(activity.getTempId());
        List<ActTempParam> paramList = actTempParamMapperExt.selectByTempId(actTempParam);
        BigDecimal discount = new BigDecimal("10");
        // 打折 x件y折
        for (int i = 0; i < paramList.size(); i++) {
            // goodsCount是否大于所有优惠条件中的件数
            if (goodsCount == Integer.valueOf(paramList.get(i).getParamCondition())) {
                discount = new BigDecimal(paramList.get(i).getParamValue());
                break;
            }
            // 只要大于最后一种打折,就按照最后一种来算,对排序有要求:最后一种的件数最多
            if (i == paramList.size() - 1) {
                if (goodsCount > Integer.valueOf(paramList.get(i).getParamCondition())) {
                    discount = new BigDecimal(paramList.get(i).getParamValue());
                }
            }
        }
        discount = discount.setScale(1, RoundingMode.HALF_UP);
        return discount.floatValue();
    }

    /**
     * 判断活动是否符合条件,符合条件则算出活动价,然后加到活动列表中
     *
     * @param activity
     */
    private ActMasterForm checkActivity(ActMasterForm activity, String memberId, float originPrice, int goodsCount, String goodsId, String skuId) {
        boolean isContainsMember = false;
        if (memberId == null || memberId.trim().length() == 0) {
            //如果没有会员ID 只取针对全体会员有效的活动
            if (ComCode.ActivityMemberType.ALL.equals(activity.getMemberType())) {
                isContainsMember = true;
            } else {
                isContainsMember = false;
            }
        } else if (ComCode.ActivityMemberType.ALL.equals(activity.getMemberType())) {
            // 判断活动用户是否包含该用户
            //全部会员
            isContainsMember = true;
        } else if (ComCode.ActivityMemberType.MEMBER_GROUP.equals(activity.getMemberType())) {
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
        } else if (ComCode.ActivityMemberType.MEMBER_LEVEL.equals(activity.getMemberType())) {
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
        if (isContainsMember) {
            // 活动有效
            //获取活动优惠信息
            ActTemplateForm template = new ActTemplateForm();
            template.setTempId(activity.getTempId());

            // 获取模板参数template = actTemplateMapperExt.selectById(template);
            ActTempParam actTempParam = new ActTempParam();
            actTempParam.setTempId(template.getTempId());
            List<ActTempParam> paramList = actTempParamMapperExt.selectByTempId(actTempParam);
            template.setParamList(paramList);
            template.setActitionType(activity.getActivityType());
            activity.setOriginPrice(originPrice);
            activity.setNewPrice(getActivityPrice(originPrice, template, goodsCount, goodsId, activity.getActivityId(), skuId));
            if (ComCode.ActivityType.INTEGRAL_EXCHANGE.equals(template.getActitionType())) {
                // 积分换购 获取需要的积分
                int point = Integer.parseInt(template.getParamList().get(0).getParamCondition());
                activity.setPoint(point);
            }
            return activity;
        } else {
            return null;
        }
    }

    /**
     * 计算活动价
     *
     * @param price
     * @param template
     * @param goodsCount
     * @return
     */
    private float getActivityPrice(float price, ActTemplateForm template, int goodsCount, String goodsId, String activityId, String skuId) {
        BigDecimal oldPrice = new BigDecimal(String.valueOf(price));
        BigDecimal newPrice = new BigDecimal(String.valueOf(price));
        oldPrice = oldPrice.setScale(2, RoundingMode.HALF_UP);
        newPrice = newPrice.setScale(2, RoundingMode.HALF_UP);
        if (ComCode.ActivityType.SPECIAL_PRICE.equals(template.getActitionType())) {
            // 特价
            newPrice = new BigDecimal(template.getParamList().get(0).getParamValue());
        } else if (ComCode.ActivityType.SECOND_KILL.equals(template.getActitionType())) {
            // TODO goodsId & activityId & skuId
            // 秒杀
            if (StringUtil.isEmpty(goodsId) && StringUtil.isEmpty(skuId)) {
                newPrice = oldPrice;
            } else {
                ActGoods actGoods = new ActGoods();
                actGoods.setActivityId(activityId);
                actGoods.setGoodsId(goodsId);
                actGoods.setSkuId(skuId);
                List<ActGoods> actGoodsList = actGoodsMapperExt.selectActGoodsInfo(actGoods);
                if (actGoodsList != null && actGoodsList.size() > 0) {
                    actGoods = actGoodsList.get(0);
                    newPrice = actGoods.getActPrice();
                } else {
                    newPrice = oldPrice;
                }
            }
        } else if (ComCode.ActivityType.DISCOUNT.equals(template.getActitionType())) {
            // 打折 x件y折
            for (int i = 0; i < template.getParamList().size(); i++) {
                // goodsCount是否大于所有优惠条件中的件数
                if (goodsCount == Integer.valueOf(template.getParamList().get(i).getParamCondition())) {
                    newPrice = oldPrice.multiply(new BigDecimal(template.getParamList().get(i).getParamValue())).divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP);
                    break;
                }
                // 只要大于最后一种打折,就按照最后一种来算,对排序有要求:最后一种的件数最多
                if (i == template.getParamList().size() - 1) {
                    if (goodsCount > Integer.valueOf(template.getParamList().get(i).getParamCondition())) {

                    }
                }
            }
        } else if (ComCode.ActivityType.DISCOUNT_TWO.equals(template.getActitionType())) {
            // 打折 第一件折，第二件几折，这里只计算第一件的折扣
            newPrice = oldPrice.multiply(new BigDecimal(template.getParamList().get(0).getParamValue())).divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP);
        } else if (ComCode.ActivityType.INTEGRAL_EXCHANGE.equals(template.getActitionType())) {
            // 积分换购
            newPrice = new BigDecimal(template.getParamList().get(0).getParamValue());

        } else if (ComCode.ActivityType.FULL_CUT.equals(template.getActitionType())) {
            // 前提是要求从paramCondition小到大排序
            // 满减
            int j = -1;
            for (int i = 0; i < template.getParamList().size(); i++) {
                // price是否大于等于所有优惠条件中的件数
                if (price >= Float.valueOf(template.getParamList().get(i).getParamCondition())) {
                    j = i;
                } else {
                    break;
                }
            }
            if (j >= 0) {
                // 原价-优惠价
                newPrice = oldPrice.subtract(new BigDecimal(template.getParamList().get(j).getParamValue()));
            } else {
                newPrice = oldPrice;
            }

        } else if (ComCode.ActivityType.FULL_DISCOUNT.equals(template.getActitionType())) {
            // 满折
            // 前提是要求从paramCondition小到大排序
            // 满减
            int j = -1;
            for (int i = 0; i < template.getParamList().size(); i++) {
                // price是否大于等于所有优惠条件中的件数
                if (price >= Float.valueOf(template.getParamList().get(i).getParamCondition())) {
                    j = i;
                } else {
                    break;
                }
            }
            if (j >= 0) {
                // 原价-原件X折扣
                BigDecimal discount = new BigDecimal(template.getParamList().get(j).getParamValue());
                newPrice = oldPrice.multiply(discount).divide(new BigDecimal("10"));
            } else {
                newPrice = oldPrice;
            }
        } else if (ComCode.ActivityType.FULL_SEND.equals(template.getActitionType())) {
            // 满送优惠劵
            newPrice = oldPrice;
        } else if (ComCode.ActivityType.FULL_SEND_GOODS.equals(template.getActitionType())) {
            // 满送货品
            newPrice = oldPrice;
        } else if (ComCode.ActivityType.FULL_SEND_POINT.equals(template.getActitionType())) {
            // 满送积分
            newPrice = oldPrice;
        } else {
            newPrice = oldPrice;
        }
        newPrice = newPrice.setScale(2, RoundingMode.HALF_UP);
        return newPrice.floatValue();
    }

    private float getOrginPrice(OrdConfirmGoodsExt goods) {
        BigDecimal price = new BigDecimal(0);
        // 计算商品原价
        if (goods.getOrdConfirmOrderUnitExtList().size() == 1) {
            // 如果是单品,直接获取sku的价格
            SkuForm skuInfo = skuMapperExt.selectBySkuId(goods.getOrdConfirmOrderUnitExtList().get(0).getSkuId());
            float priceFloat = skuInfo.getPrice();
            price = new BigDecimal(String.valueOf(priceFloat));
        } else {
            // 如果是套装,价格等于每个sku价格之和
            StringBuffer skuBuffer = new StringBuffer();
            for (int j = 0; j < goods.getOrdConfirmOrderUnitExtList().size(); j++) {
                skuBuffer.append("'");
                skuBuffer.append(goods.getOrdConfirmOrderUnitExtList().get(j).getSkuId());
                skuBuffer.append("'");
                if (j != goods.getOrdConfirmOrderUnitExtList().size() - 1) {
                    skuBuffer.append(",");
                }
            }
            float priceFloat = skuMapperExt.getSuitPrice(skuBuffer.toString());
            price = new BigDecimal(String.valueOf(priceFloat));
        }
        price = price.setScale(2, RoundingMode.HALF_UP);
        return price.floatValue();
    }

    @Override
    public String getSendCouponIdByActivity(String activityId) {
        if (StringUtil.isEmpty(activityId)) {
            return null;
        }
        ActMasterForm activity = actMasterMapperExt.selectByPrimaryKey(activityId);
        if (activity == null) {
            return null;
        }
        // 判断是不是满送优惠券的类型
        if (!ComCode.ActivityType.FULL_SEND.equals(activity.getActivityType())) {
            return null;
        }
        ActTempParam actTempParam = new ActTempParam();
        actTempParam.setTempId(activity.getTempId());
        List<ActTempParam> paramList = actTempParamMapperExt.selectByTempId(actTempParam);
        if (paramList == null || paramList.size() == 0) {
            return null;
        }
        // 获取模板
        return paramList.get(0).getParamValue();
    }


    @Override
    public String getSendPointsByActivity(String activityId) {
        if (StringUtil.isEmpty(activityId)) {
            return null;
        }
        ActMasterForm activity = actMasterMapperExt.selectByPrimaryKey(activityId);
        if (activity == null) {
            return null;
        }

        // 模版
        String tempId = activity.getTempId();
        if (StringUtil.isEmpty(tempId)) {
            return null;
        }
        ActTemplateForm form = new ActTemplateForm();
        form.setTempId(tempId);
        form = actTemplateMapperExt.selectById(form);

        // 判断是不是满送优惠券的类型
        if (!ComCode.ActivityType.FULL_SEND_POINT.equals(form.getActitionType())) {
            return null;
        }

        // 模版
        ActTempParam actTempParam = new ActTempParam();
        actTempParam.setTempId(activity.getTempId());
        List<ActTempParam> paramList = actTempParamMapperExt.selectByTempId(actTempParam);
        if (paramList == null || paramList.size() == 0) {
            return null;
        }
        // 获取模板value
        return paramList.get(0).getParamValue();
    }


    public JSONObject setSort(ActMaster form) {
        JSONObject json = new JSONObject();
        try {
            //数据库检索 -- 过滤数据
            int count = actMasterMapper.updateByPrimaryKeySelective(form);
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
     * 添加活动相关商品
     *
     * @param form
     * @return
     */
    @Override
    public JSONObject addGoods(ActMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getActivityId())) {
                throw new ExceptionBusiness("缺少参数");
            }
            if (StringUtil.isEmpty(form.getGoodsType())) {
                throw new ExceptionBusiness("缺少参数");
            }

            if (form.getGoodsList() == null || form.getGoodsList().size() == 0) {
                throw new ExceptionBusiness("缺少参数");
            }
            for (int i = 0; i < form.getGoodsList().size(); i++) {
                ActGoods goods = new ActGoods();
                goods.setGoodsId(form.getGoodsList().get(i).getGoodsId());
                goods.setQuantity(form.getGoodsList().get(i).getQuantity());
                goods.setBuyMax(form.getGoodsList().get(i).getBuyMax());
                goods.setSurplus(form.getGoodsList().get(i).getQuantity());
                goods.setActPrice(form.getGoodsList().get(i).getActPrice());
                goods.setInsertUserId(form.getInsertUserId());
                goods.setShopId(Constants.ORGID);
                goods.setInsertTime(new Date());
                goods.setActivityId(form.getActivityId());
                goods.setGoodsType(form.getGoodsType());
                goods.setDeleted(Constants.DELETED_NO);
                goods.setSkuId(form.getGoodsList().get(i).getSkuId());
                goods.setActGoodsId(UUID.randomUUID().toString());
                actGoodsMapper.insertSelective(goods);
            }
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
     * 删除活动相关商品
     *
     * @param form
     * @return
     */
    @Override
    public JSONObject deleteGoods(ActMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            if (form.getGoodsList() == null || form.getGoodsList().size() == 0) {
                throw new ExceptionBusiness("缺少参数");
            }
            for (int i = 0; i < form.getGoodsList().size(); i++) {
                actGoodsMapper.deleteByPrimaryKey(form.getGoodsList().get(i).getActGoodsId());
            }
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
     * 编辑活动相关商品
     *
     * @param form
     * @return
     */
    @Override
    public JSONObject editGoods(ActMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            if (form.getGoodsList() == null || form.getGoodsList().size() == 0) {
                throw new ExceptionBusiness("缺少参数");
            }
            for (int i = 0; i < form.getGoodsList().size(); i++) {
                ActGoods goods = new ActGoods();
                goods.setQuantity(form.getGoodsList().get(i).getQuantity());
                goods.setBuyMax(form.getGoodsList().get(i).getBuyMax());
                goods.setSurplus(form.getGoodsList().get(i).getQuantity());
                goods.setActPrice(form.getGoodsList().get(i).getActPrice());
                goods.setActGoodsId(form.getGoodsList().get(i).getActGoodsId());
                actGoodsMapper.updateByPrimaryKeySelective(goods);
            }
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

    @Override
    public JSONObject getGoodsList(ActMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getActivityId())) {
                throw new ExceptionBusiness("缺少参数");
            }
            if (StringUtil.isEmpty(form.getGoodsType())) {
                throw new ExceptionBusiness("缺少参数");
            }
            List<ActGoodsForm> goodsList = null;
            ActGoods goods = new ActGoods();
            goods.setActivityId(form.getActivityId());
            if (ComCode.ActivityGoodsType.ALL.equals(form.getGoodsType())) {
                //全部商品
            } else if (ComCode.ActivityGoodsType.TYPE.equals(form.getGoodsType())) {
                // 按分类
                goodsList = actGoodsMapperExt.selectGoodsTypeByActivityId(goods);
            } else if (ComCode.ActivityGoodsType.BRAND.equals(form.getGoodsType())) {
                // 按品牌
                goodsList = actGoodsMapperExt.selectGoodsBrandByActivityId(goods);
            } else if (ComCode.ActivityGoodsType.GOODS.equals(form.getGoodsType())) {
                // 按商品
                goodsList = actGoodsMapperExt.selectGoodsByActivityId(goods);
            } else if (ComCode.ActivityGoodsType.SKU.equals(form.getGoodsType())) {
                // 按SKU
                goodsList = actGoodsMapperExt.selectSkuByActivityId(goods);
            }
            json.put("resultCode", Constants.NORMAL);
            json.put("result",goodsList);
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

