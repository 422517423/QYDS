package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ComCode;
import net.dlyt.qyds.common.dto.ext.*;
import net.dlyt.qyds.common.form.*;
import net.dlyt.qyds.dao.*;
import net.dlyt.qyds.dao.ext.*;
import net.dlyt.qyds.web.service.*;
import net.dlyt.qyds.web.service.common.*;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by wenxuechao on 16/7/23.
 */
@Service("ordMasterService")
public class OrdMasterServiceImpl implements OrdMasterService {

    protected final Logger log = LoggerFactory.getLogger(OrdMasterServiceImpl.class);
    @Autowired
    KdniaoTrackQueryAPI kdniaoTrackQueryAPI;
    @Autowired
    ActMasterService actMasterService;
    @Autowired
    SkuMapperExt skuMapperExt;
    @Autowired
    CouponMemberService couponMemberService;
    @Autowired
    CouponMemberMapper couponMemberMapper;
    @Autowired
    CouponMasterMapper couponMasterMapper;
    @Autowired
    BnkMasterService bnkMasterService;
    @Autowired
    MmbPointRecordService mmbPointRecordService;
    @Autowired
    MmbMasterMapper mmbMasterMapper;
    @Autowired
    ComDistrictMapper comDistrictMapper;
    @Autowired
    MmbMasterMapperExt mmbMasterMapperExt;
    @Autowired
    MmbSalerMapperExt mmbSalerMapperExt;
    @Autowired
    private OrdMasterMapperExt ordMasterMapperExt;
    @Autowired
    private OrdMasterMapper ordMasterMapper;
    @Autowired
    private OrdListMapperExt ordListMapperExt;
    @Autowired
    private OrdSubListMapperExt ordSubListMapperExt;
    @Autowired
    private OrdSubListMapper ordSubListMapper;
    @Autowired
    private OrdHistoryMapperExt ordHistoryMapperExt;
    @Autowired
    private OrdHistoryMapper ordHistoryMapper;
    @Autowired
    private OrdReturnExchangeMapperExt ordReturnExchangeMapperExt;
    @Autowired
    private OrdReturnExchangeMapper ordReturnExchangeMapper;
    @Autowired
    private MmbAddressMapperExt mmbAddressMapperExt;
    @Autowired
    private MmbAddressMapper mmbAddressMapper;
    @Autowired
    private GdsMasterMapperExt gdsMasterMapperExt;
    @Autowired
    private GdsMasterMapper gdsMasterMapper;
    @Autowired
    private ComCodeMapperExt comCodeMapperExt;
    @Autowired
    private MmbShoppingBagMapperExt mmbShoppingBagMapperExt;
    @Autowired
    private ActMasterMapper actMasterMapper;
    @Autowired
    private ActMasterMapperExt actMasterMapperExt;
    @Autowired
    private ActGoodsMapper actGoodsMapper;
    @Autowired
    private ActGoodsMapperExt actGoodsMapperExt;
    @Autowired
    private MmbPointRecordMapperExt mmbPointRecordMapperExt;
    @Autowired
    private BnkMasterMapperExt bnkMasterMapperExt;
    @Autowired
    private BnkMasterMapper bnkMasterMapper;
    @Autowired
    private BnkRecordsMapper bnkRecordsMapper;
    @Autowired
    private ErpStoreMapper erpStoreMapper;
    @Autowired
    private ActTempParamMapperExt actTempParamMapperExt;
    @Autowired
    private ErpOrdMasterMapperExt erpOrdMasterMapperExt;
    @Autowired
    private ErpOrderSubMapperExt erpOrderSubMapperExt;
    @Autowired
    private SysSmsCaptchaMapperExt sysSmsCaptchaMapperExt;

    public List<OrdMasterExt> getAllDatas(OrdMasterExt ordMasterExt) {

        List<OrdMasterExt> list = ordMasterMapperExt.getAllDatas(ordMasterExt);
        //为前台准备数据
        for (OrdMasterExt ome : list) {
            //传递前台显示订单状态
            String orderStatusName = orderStatusNameGet(ome.getOrderStatus(), ome.getPayStatus(), ome.getDeliverStatus());
            ome.setOrderStatusName(orderStatusName);

            // 获取活动信息
            if (!StringUtil.isEmpty(ome.getActionId())) {
                // 获取活动信息
                ActMasterForm activity = actMasterMapperExt.selectByPrimaryKey(ome.getActionId());
                //ome.setActivity(activity);
                ome.setActivityName(activity.getActivityName());
            }
            // 获取优惠券信息
            if (!StringUtil.isEmpty(ome.getCouponId())) {
                // 获取活动信息
                CouponMember couponMember = couponMemberMapper.selectByPrimaryKey(ome.getCouponId());
                if (couponMember != null) {
                    CouponMaster couponMaster = couponMasterMapper.selectByPrimaryKey(couponMember.getCouponId());
                    //ome.setCoupon(couponMaster);
                    ome.setCouponName(couponMaster.getCouponName());
                }
            }

        }
        return list;
    }

    public int getAllDatasCount(OrdMasterExt ordMasterExt) {
        int count = ordMasterMapperExt.getAllDatasCount(ordMasterExt);
        return count;
    }

    public OrdMasterExt selectByPrimaryKey(String orderId) {
        // 获取基本信息
        OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(orderId);
        OrdMasterExt result = new OrdMasterExt();
        BeanUtils.copyProperties(ordMaster, result);
        result.setOrderStatusName(orderStatusNameGet(ordMaster.getOrderStatus(), ordMaster.getPayStatus(), ordMaster.getDeliverStatus()));
        // 获取活动信息
        if (!StringUtil.isEmpty(result.getActionId())) {
            // 获取活动信息
            ActMasterForm activity = actMasterMapperExt.selectByPrimaryKey(result.getActionId());
            result.setActivity(activity);
        }
        // 获取优惠券信息
        if (!StringUtil.isEmpty(result.getCouponId())) {
            // 获取活动信息
            CouponMember couponMember = couponMemberMapper.selectByPrimaryKey(result.getCouponId());
            if (couponMember != null) {
                CouponMaster couponMaster = couponMasterMapper.selectByPrimaryKey(couponMember.getCouponId());
                result.setCoupon(couponMaster);
            }
        }
        return result;
    }

    public List<OrdList> selectOrderGoodsInfo(String orderId) {
        return ordListMapperExt.selectOrderGoodsInfo(orderId);
    }

    public List<OrdSubListExt> selectOrderSubInfo(String orderId) {
        return ordSubListMapperExt.selectOrderSubInfo(orderId);
    }

    public List<OrdMasterExt> getOrdListByUserId(OrdMasterExt ordMasterExt) {
        //获取订单列表主数据
        List<OrdMasterExt> list = ordMasterMapperExt.getAllDatas(ordMasterExt);
        if (list.size() == 0 || list == null) {
            return null;
        }
        //获取主订单订单id
        String ord_ids = "";
        for (int i = 0; i < list.size() - 1; i++) {
            ord_ids += "'" + list.get(i).getOrderId() + "',";
        }
        ord_ids += "'" + list.get(list.size() - 1).getOrderId() + "'";
        //根据订单id获取商品明细信息
        List<OrdList> ordLists = ordListMapperExt.selectOrdListByOrderId(ord_ids);
        //遍历子订单数组,整合数据
        for (int j = 0; j < list.size(); j++) {
            String order_id = list.get(j).getOrderId();
            List<OrdList> actOrdList = new ArrayList<OrdList>();
            for (int m = 0; m < ordLists.size(); m++) {
                if (order_id.equals(ordLists.get(m).getOrderId())) {
                    OrdList ordList = new OrdList();
                    ordList = ordLists.get(m);
                    actOrdList.add(ordList);
                }
            }
            list.get(j).setOrdList(actOrdList);
        }
        return list;
    }

    public List<OrdMasterExt> getOrdDetailByOrderId(OrdMasterExt ordMasterExt) {
        //获取订单列表主数据
        List<OrdMasterExt> list = ordMasterMapperExt.getAllDatas(ordMasterExt);
        if (list == null || list.size() == 0) {
            return null;
        } else {
            //根据订单id获取商品信息
            List<OrdList> ordLists = ordListMapperExt.selectOrderGoodsInfo(ordMasterExt.getOrderId());
            //根据订单id获取物流信息
            List<OrdSubListExt> subLists = ordSubListMapperExt.selectDistinctOrderSubInfo(ordMasterExt.getOrderId());
            //遍历商品数组,整合商品数据
            String order_id = list.get(0).getOrderId();
            List<OrdList> actOrdList = new ArrayList<OrdList>();
            for (int m = 0; m < ordLists.size(); m++) {
                if (order_id.equals(ordLists.get(m).getOrderId())) {
                    OrdList ordModel = new OrdList();
                    ordModel = ordLists.get(m);
                    actOrdList.add(ordModel);
                }
            }

            list.get(0).setOrdList(actOrdList);
            //遍历物流信息,整个物流数据
            List<OrdSubList> actSubList = new ArrayList<OrdSubList>();
            for (int i = 0; i < subLists.size(); i++) {
                if (order_id.equals(subLists.get(i).getOrderId())) {
                    OrdSubList subModel = new OrdSubList();
                    subModel = subLists.get(i);
                    actSubList.add(subModel);
                }
            }
            list.get(0).setSubList(actSubList);
            // coupon 优惠劵获取
            if (!StringUtil.isEmpty(list.get(0).getCouponId())) {
                CouponMember member = couponMemberMapper.selectByPrimaryKey(list.get(0).getCouponId());
                if (member != null) {
                    CouponMaster coupon = couponMasterMapper.selectByPrimaryKey(member.getCouponId());
                    list.get(0).setCoupon(coupon);
                }
            }
            // coupon 门店信息获取
            if (!StringUtil.isEmpty(list.get(0).getErpStoreId())) {
                ErpStore store = erpStoreMapper.selectByPrimaryKey(list.get(0).getErpStoreId());
                if (store != null) {
                    list.get(0).setStoreName(store.getStoreNameCn());
                    list.get(0).setStorePhone(store.getPhone());
                    list.get(0).setStoreAddress(store.getAddress());
                }
            }
        }
        return list;
    }

    public List<MmbShoppingSKuExt> getSKUListByOrderDetailId(String detailId) {

        return ordSubListMapperExt.querySKUListByOrderDetailId(detailId);
    }


    public JSONObject getActivityCouponList(String data){
        JSONObject result = new JSONObject();
        try {
            OrdMasterExt ordMasterExt = (OrdMasterExt) JSON.parseObject(data, OrdMasterExt.class);
            //获得满足条件的活动
            List<HashMap> activitylist =  actMasterMapperExt.getActiveIdAndList(ordMasterExt);
            //获得满足条件的优惠劵
            List<HashMap> coupponlist =  actMasterMapperExt.getCoupponIdAndList(ordMasterExt);
            result.put("activitylist",activitylist);
            result.put("coupponlist",coupponlist);
            //执行结束返回成功
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }


    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject confirmReceiptInMaster(OrdMasterExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getOrderId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getUpdateUserId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            //业务校验订单是否存在并属于该客户
            OrdMaster orderMaster = ordMasterMapper.selectByPrimaryKey(form.getOrderId());
            if (orderMaster == null) {
                throw new ExceptionErrorData("订单不存在");
            }
            if (!orderMaster.getMemberId().equals(form.getMemberId())) {
                throw new ExceptionErrorParam("订单信息有误");
            }
            confirmReceiveOrderGoods(orderMaster.getOrderId(), form.getUpdateUserId());
            json.put("result", Constants.NORMAL);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 物流列表处确认收货按钮
     *
     * @param form memberId:会员ID
     *             orderId:订单编号 : 收货用标识符-门店自提
     *             expressNo:邮件编号 : 收货用标识符-物流送货
     * @return
     * @功能位置 物流列表单个物流确认收货
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Deprecated
    public JSONObject confirmReceiptInSub(OrdLogisticsForm form) {

        JSONObject json = new JSONObject();

        try {

            OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(form.getOrderId());

            if (ordMaster == null || !"0".equals(ordMaster.getDeleted())) {
                throw new ExceptionErrorData("订单不存在");
            } else if (!form.getMemberId().equals(ordMaster.getMemberId())) {
                throw new ExceptionErrorData("用户订单不存在");
            } else if (!"20".equals(ordMaster.getDeliverStatus())) {
                // 10.未发货，20.已发货，21.已收货，30.退货已发货，31.退货已收货，40.换货已发退货，41.换货已收退货，43.换货已发新货，44.换货已收新货
                throw new ExceptionErrorData("订单状态已经被更新");
            }

            // 10.电商发货，20.门店自提
            if ("20".equals(ordMaster.getDeliverType())) {
                //更新主订单状态为已收货
                OrdMasterExt ordMasterExt = new OrdMasterExt();
                ordMasterExt.setMemberId(form.getMemberId());
                ordMasterExt.setOrderId(form.getOrderId());
                ordMasterMapperExt.confirmReceiptInMaster(ordMasterExt);

                //更新子订单收货状态为已收货
                ordSubListMapperExt.confirmReceipt(ordMasterExt);

                //获取订单主表信息
                ordMaster = ordMasterMapper.selectByPrimaryKey(form.getOrderId());

                //插入订单历史记录表
                OrdHistory ordHistory = this.masterToHistory(ordMaster);
                ordHistory.setSeqOrderId(UUID.randomUUID().toString());
                ordHistory.setInsertUserId(ordMasterExt.getMemberId());
                //主订单操作历史信息插入
                ordHistoryMapperExt.insertSelective(ordHistory);
                //获取子订单数据
                List<OrdSubListExt> list = ordSubListMapperExt.selectOrderSubInfo(ordMasterExt.getOrderId());
                for (OrdSubList item : list) {
                    OrdHistory ordHistorySub = this.masterToHistory(ordMaster);
                    ordHistorySub.setSeqOrderId(UUID.randomUUID().toString());
                    ordHistorySub.setInsertUserId(form.getMemberId());
                    ordHistorySub.setExpressJson(item.getExpressNo());
                    //子订单操作历史信息插入
                    ordHistoryMapperExt.insertSelective(ordHistorySub);
                }
            } else {

                OrdMasterExt ordMasterExt = new OrdMasterExt();
                ordMasterExt.setMemberId(ordMaster.getMemberId());
                ordMasterExt.setExpressNo(form.getExpressNo());
                ordSubListMapperExt.confirmReceipt(ordMasterExt);

                //子订单操作历史信息插入
                OrdHistory ordHistory = new OrdHistory();
                ordHistory = this.masterToHistory(ordMaster);
                ordHistory.setSeqOrderId(UUID.randomUUID().toString());
                ordHistory.setInsertUserId(form.getMemberId());
                ordHistory.setExpressJson(form.getExpressNo());
                ordHistoryMapperExt.insertSelective(ordHistory);

                List<OrdSubListExt> list = ordSubListMapperExt.selectOrderSubInfo(form.getOrderId());
                int receivedCount = 0;
                if (list == null || list.size() > 0) {
                    for (OrdSubList item : list) {
                        if ("21".equals(item.getDeliverStatus()) && "0".equals(item.getDeleted())) {
                            receivedCount += 1;
                        }
                    }

                    if (receivedCount == list.size()) {

                        //更新主订单状态为已收货
                        ordMasterExt = new OrdMasterExt();
                        ordMasterExt.setMemberId(form.getMemberId());
                        ordMasterExt.setExpressNo(form.getExpressNo());
                        ordMasterExt.setOrderId(form.getOrderId());
                        ordMasterMapperExt.confirmReceiptInMaster(ordMasterExt);
                        //获取订单主表信息
                        ordMaster = ordMasterMapper.selectByPrimaryKey(form.getOrderId());
                        //历史表操作
                        OrdHistory ordHistoryMaster = this.masterToHistory(ordMaster);
                        ordHistoryMaster.setSeqOrderId(UUID.randomUUID().toString());
                        ordHistoryMaster.setInsertUserId(ordMasterExt.getMemberId());
                        //主订单操作历史信息插入
                        ordHistoryMapperExt.insertSelective(ordHistoryMaster);
                        json.put("orderCode", ordMaster.getOrderCode());
                    }
                }
            }
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }

        return json;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String deleteOrder(OrdMasterExt ordMasterExt) {
        String result = "";
        //业务校验订单是否存在并属于该客户
        int orderCount = ordMasterMapperExt.getAllDatasCount(ordMasterExt);
        if (orderCount == 0) {
            return null;
        } else {
            //更新主订单删除标记为删除
            int deleteCount = ordMasterMapperExt.deleteOrder(ordMasterExt);
            //获取主订单信息
            OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(ordMasterExt.getOrderId());
            //插入订单历史记录表
            OrdHistory ordHistory = new OrdHistory();
            ordHistory = this.masterToHistory(ordMaster);
            ordHistory.setSeqOrderId(UUID.randomUUID().toString());
            ordHistory.setInsertUserId(ordMasterExt.getMemberId());
            //主订单操作历史信息插入
            int insertHistoryCount = ordHistoryMapperExt.insertSelective(ordHistory);
            //执行结束返回成功
            result = Constants.NORMAL;
        }
        return result;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject cancelOrder(OrdMasterExt ordMasterExt) {
        JSONObject result = new JSONObject();
        try {
            if (StringUtil.isEmpty(ordMasterExt.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(ordMasterExt.getOrderId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            //业务校验订单是否存在并属于该客户
            OrdMaster order = ordMasterMapper.selectByPrimaryKey(ordMasterExt.getOrderId());
            if (order == null) {
                throw new ExceptionErrorData("订单不存在");
            }
            // 判断状态
            if (!"10".equals(order.getOrderStatus()) || !"10".equals(order.getPayStatus()) || !"10".equals(order.getDeliverStatus())) {
                throw new ExceptionErrorData("订单状态已经变化，请刷新重试");
            }
            //更新主订单状态为
            int deleteCount = ordMasterMapperExt.cancelOrder(ordMasterExt);
            //获取主订单信息
            OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(ordMasterExt.getOrderId());
            //插入订单历史记录表
            OrdHistory ordHistory = new OrdHistory();
            ordHistory = this.masterToHistory(ordMaster);
            ordHistory.setSeqOrderId(UUID.randomUUID().toString());
            ordHistory.setInsertUserId(ordMasterExt.getMemberId());
            //主订单操作历史信息插入
            int insertHistoryCount = ordHistoryMapperExt.insertSelective(ordHistory);
            //若使用优惠券,返还优惠券
            if (ordMaster.getCouponId() != null && !"".equals(ordMaster.getCouponId())) {
                CouponMemberExt couponMemberExt = new CouponMemberExt();
                couponMemberExt.setCouponMemberId(ordMaster.getCouponId());
                couponMemberService.returnCoupon(couponMemberExt);
            }

            // 获取订单关联的秒杀活动商品
            List<ActGoods> actGoodsList = actGoodsMapperExt.selectSecKillGoodsByOrderId(ordMaster.getOrderId());
            for(ActGoods actItem : actGoodsList){
                ActGoods actGoods = actGoodsMapper.selectByPrimaryKey(actItem.getActGoodsId());
                if(actGoods != null){
                    // 如果是参加秒杀活动，需要恢复秒杀活动商品数量
                    actGoods.setSurplus(actGoods.getSurplus() + actItem.getQuantity());
                    actGoodsMapper.updateByPrimaryKeySelective(actGoods);
                }
            }
            //执行结束返回成功
            result.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return result;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject applyReturnGoods(OrdReturnExchangeExt ordReturnExchangeExt) {
        JSONObject result = new JSONObject();
        try {
            //获取订单id
            if (StringUtil.isEmpty(ordReturnExchangeExt.getOrderId())) {
                throw new ExceptionBusiness("缺少参数");
            }
            //获取用户id
            if (StringUtil.isEmpty(ordReturnExchangeExt.getMemberId())) {
                throw new ExceptionBusiness("缺少参数");
            }
            //退货申请理由
            if (StringUtil.isEmpty(ordReturnExchangeExt.getApplyComment())) {
                throw new ExceptionBusiness("缺少参数");
            }
            // 退换货地点(10.电商，20.门店)
            if (StringUtil.isEmpty(ordReturnExchangeExt.getRexPoint())) {
                throw new ExceptionBusiness("缺少参数");
            }
            if ("20".equals(ordReturnExchangeExt.getRexPoint())) {
                // 退换货门店ID
                if (StringUtil.isEmpty(ordReturnExchangeExt.getRexStoreId())) {
                    throw new ExceptionBusiness("缺少参数");
                }
                // 退换货门店名称
                if (StringUtil.isEmpty(ordReturnExchangeExt.getRexStoreName())) {
                    throw new ExceptionBusiness("缺少参数");
                }
            }

            //查询主订单条件
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            //买家ID
            ordMasterExt.setMemberId(ordReturnExchangeExt.getMemberId());
            //订单ID
            ordMasterExt.setOrderId(ordReturnExchangeExt.getOrderId());
            //订单状态为已完成
            ordMasterExt.setOrderStatus("10");
            //订单付款状态为支付成功
            ordMasterExt.setPayStatus("20");
            //订单发货状态为已收货
            ordMasterExt.setDeliverStatus("21");
            //订单允许退货
            ordMasterExt.setCanReturn("1");
            //业务校验订单是否存在并属于该客户
            int orderCount = ordMasterMapperExt.getAllDatasCount(ordMasterExt);
            if (orderCount == 0) {
                throw new ExceptionErrorData("订单不存在");
            }

            //更新主订单状态订单退单中
            int updateOrdMaster = ordMasterMapperExt.applyReturnGoods(ordMasterExt);
            //获取主订单信息
            OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(ordMasterExt.getOrderId());
            //插入订单历史记录表
            OrdHistory ordHistory = new OrdHistory();
            ordHistory = this.masterToHistory(ordMaster);
            ordHistory.setSeqOrderId(UUID.randomUUID().toString());
            ordHistory.setInsertUserId(ordMasterExt.getMemberId());
            //主订单操作历史信息插入
            int insertHistoryCount = ordHistoryMapperExt.insertSelective(ordHistory);
            //更新子订单发货状态为退货申请中
            int updateSubOrder = ordSubListMapperExt.applyReturnGoods(ordMasterExt);
            //获取主订单中子订单数据
            List<OrdSubListExt> ordSubLists = ordSubListMapperExt.selectOrderSubInfo(ordMasterExt.getOrderId());
            //插入退换货订单
            for (OrdSubList osl : ordSubLists) {
                ordReturnExchangeExt.setRexOrderId(UUID.randomUUID().toString());
                ordReturnExchangeExt.setSubOrderId(osl.getSubOrderId());
                //全单退货
                ordReturnExchangeExt.setRexMode("10");
                ordReturnExchangeExt.setRexType("10");
                // 快递退货，哪买哪退
                if ("10".equals(ordReturnExchangeExt.getRexPoint())) {
                    // 退换货门店ID
//                    ordReturnExchangeExt.setRexStoreId(osl.getErpStoreId());
//                    ordReturnExchangeExt.setRexStoreName(osl.getStoreName());
                    // 修改为默认总部
                    ordReturnExchangeExt.setRexStoreId("010001");
                    ordReturnExchangeExt.setRexStoreName("总部");
                }
                ordReturnExchangeExt.setOrderCode(ordMaster.getOrderCode());
                ordReturnExchangeExt.setShopId(ordMaster.getShopId());
                ordReturnExchangeExt.setRexMemberId(ordReturnExchangeExt.getMemberId());
                ordReturnExchangeExt.setUpdateUserId(ordReturnExchangeExt.getMemberId());
                ordReturnExchangeExt.setInsertUserId(ordReturnExchangeExt.getMemberId());
                int insertCount = ordReturnExchangeMapperExt.insertSelective(ordReturnExchangeExt);
            }
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject applyReturnSubGoods(OrdReturnExchangeExt ordReturnExchangeExt) {
        JSONObject result = new JSONObject();
        try {
            //获取订单id
            if (StringUtil.isEmpty(ordReturnExchangeExt.getOrderId())) {
                throw new ExceptionBusiness("缺少参数");
            }
            //获取用户id
            if (StringUtil.isEmpty(ordReturnExchangeExt.getMemberId())) {
                throw new ExceptionBusiness("缺少参数");
            }
            //退货申请理由
            if (StringUtil.isEmpty(ordReturnExchangeExt.getApplyComment())) {
                throw new ExceptionBusiness("缺少参数");
            }
            // 退换货地点(10.电商，20.门店)
            if (StringUtil.isEmpty(ordReturnExchangeExt.getRexPoint())) {
                throw new ExceptionBusiness("缺少参数");
            }
            //获取商品明细订单id
            if (StringUtil.isEmpty(ordReturnExchangeExt.getDetailId())) {
                throw new ExceptionBusiness("缺少参数");
            }
            //退单数量
            if (StringUtil.isEmpty(ordReturnExchangeExt.getReturnCount())) {
                throw new ExceptionBusiness("缺少参数");
            }
            if ("20".equals(ordReturnExchangeExt.getRexPoint())) {
                // 退换货门店ID
                if (StringUtil.isEmpty(ordReturnExchangeExt.getRexStoreId())) {
                    throw new ExceptionBusiness("缺少参数");
                }
                // 退换货门店名称
                if (StringUtil.isEmpty(ordReturnExchangeExt.getRexStoreName())) {
                    throw new ExceptionBusiness("缺少参数");
                }
            }

            //查询主订单条件
            OrdMasterExt order = new OrdMasterExt();
            //买家ID
            order.setMemberId(ordReturnExchangeExt.getMemberId());
            //订单ID
            order.setOrderId(ordReturnExchangeExt.getOrderId());
            //订单状态为已完成
            order.setOrderStatus("10");
            //订单付款状态为支付成功
            order.setPayStatus("20");
            //订单发货状态为已收货
            order.setDeliverStatus("21");
            //订单允许退货
            order.setCanReturn("1");
            //业务校验订单是否存在并属于该客户
            int orderCount = ordMasterMapperExt.getAllDatasCount(order);
            if (orderCount == 0) {
                throw new ExceptionErrorData("订单不存在");
            }
            //查询条件
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            //买家ID
            ordMasterExt.setMemberId(ordReturnExchangeExt.getMemberId());
            String[] detailIdArray = ordReturnExchangeExt.getDetailId().split(",");
            String[] returnCount = ordReturnExchangeExt.getReturnCount().split(",");
            int countZero = 0;
            for (int p = 0; p < returnCount.length; p++) {
                countZero += Integer.valueOf(returnCount[p]);
            }
            if (countZero == 0) {
                throw new ExceptionBusiness("请选择退货商品件数!");
            }
            OrdMaster ordMaster = new OrdMaster();
            for (int i = 0; i < detailIdArray.length; i++) {
                if ("0".equals(returnCount[i])) {
                    continue;
                }
                //订单商品明细ID
                ordMasterExt.setDetailId(detailIdArray[i]);
                //业务校验订单是否存在并属于该客户
                List<OrdSubList> ordSubList = ordSubListMapperExt.selectSubOrderByDetailId(ordMasterExt);
                if (ordSubList == null || ordSubList.size() == 0 || ordSubList.size() < Integer.valueOf(returnCount[i])) {
                    throw new ExceptionErrorData("子订单信息有误。");
                } else {
                    //订单ID
                    ordMasterExt.setOrderId(ordSubList.get(0).getOrderId());
                    //订单状态为已完成
                    ordMasterExt.setOrderStatus("10");
                    //订单付款状态为支付成功
                    ordMasterExt.setPayStatus("20");
                    //订单发货状态为已收货
                    ordMasterExt.setDeliverStatus("21");
                    //订单允许退货
                    ordMasterExt.setCanReturn("1");
                    //更新主订单状态订单退单中
                    int updateOrdMaster = ordMasterMapperExt.applyReturnGoods(ordMasterExt);
                    //获取主订单信息
                    ordMaster = ordMasterMapper.selectByPrimaryKey(ordMasterExt.getOrderId());
                    for (int m = 0; m < Integer.valueOf(returnCount[i]); m++) {
                        //子订单id
                        ordMasterExt.setSubOrderId(ordSubList.get(m).getSubOrderId());
                        //更新子订单发货状态为退货申请中
                        int updateSubOrder = ordSubListMapperExt.applyReturnGoods(ordMasterExt);
                        //插入退换货订单
                        ordReturnExchangeExt.setRexOrderId(UUID.randomUUID().toString());
                        ordReturnExchangeExt.setSubOrderId(ordSubList.get(m).getSubOrderId());
                        ordReturnExchangeExt.setRexType("10");
                        //拆单退货
                        ordReturnExchangeExt.setRexMode("20");
                        // 快递退货，哪买哪退
                        if ("10".equals(ordReturnExchangeExt.getRexPoint())) {
                            // 退换货门店ID
//                            ordReturnExchangeExt.setRexStoreId(ordSubList.get(m).getErpStoreId());
//                            ordReturnExchangeExt.setRexStoreName(ordSubList.get(m).getStoreName());
                            // 修改为默认总部
                            ordReturnExchangeExt.setRexStoreId("010001");
                            ordReturnExchangeExt.setRexStoreName("总部");
                        }
                        ordReturnExchangeExt.setOrderCode(ordMaster.getOrderCode());
                        ordReturnExchangeExt.setShopId(ordMaster.getShopId());
                        ordReturnExchangeExt.setRexMemberId(ordReturnExchangeExt.getMemberId());
                        ordReturnExchangeExt.setUpdateUserId(ordReturnExchangeExt.getMemberId());
                        ordReturnExchangeExt.setInsertUserId(ordReturnExchangeExt.getMemberId());
                        int insertCount = ordReturnExchangeMapperExt.insertSelective(ordReturnExchangeExt);
                    }
                }
            }
            //插入订单历史记录表
            OrdHistory ordHistory = new OrdHistory();
            ordHistory = this.masterToHistory(ordMaster);
            ordHistory.setSeqOrderId(UUID.randomUUID().toString());
            ordHistory.setInsertUserId(ordMasterExt.getMemberId());
            //主订单操作历史信息插入
            int insertHistoryCount = ordHistoryMapperExt.insertSelective(ordHistory);
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    public List<OrdReturnExchangeExt> viewApplyReturnGoods(OrdReturnExchangeExt ordReturnExchangeExt) {
        return ordReturnExchangeMapperExt.viewApplyReturnGoods(ordReturnExchangeExt);
    }

    public String reSubmitReturnGoodsByOrderId(OrdReturnExchangeExt ordReturnExchangeExt) {
        return ordReturnExchangeMapperExt.reSubmitReturnGoodsByOrderId(ordReturnExchangeExt);
    }

    @Override
    public void checkConfirmOrderFromGoods(OrdMasterExt ordMasterExt) {
        if (Integer.valueOf(ordMasterExt.getQuantity()) <= 0) {
            throw new ExceptionBusiness("购买商品数量错误!");
        }
        //商品信息参数构造
        GdsMasterExt gdsMasterExt = new GdsMasterExt();
        gdsMasterExt.setGoodsId(ordMasterExt.getGoodsId());
        gdsMasterExt.setGoodsSkuId(ordMasterExt.getGoodsSkuId());
        //根据商品ID判断商品是否满足成单条件
        Map<String, Object> goodsInfoMap = new HashMap<String, Object>();
        goodsInfoMap = gdsMasterMapperExt.getAllGoodsInfo(gdsMasterExt);
        if (goodsInfoMap == null) {
            throw new ExceptionBusiness("购买商品已下架!");
        } else {
            if (goodsInfoMap.get("new_count") == null) {
                throw new ExceptionBusiness("商品库存不足!");
            }
            if (Integer.valueOf(goodsInfoMap.get("new_count").toString()) == 0
                    || Integer.valueOf(ordMasterExt.getQuantity()) > Integer.valueOf(goodsInfoMap.get("new_count").toString())) {
                throw new ExceptionBusiness("商品库存不足");
            } else {
                //会员地址信息参数构造
                MmbAddressForm form = new MmbAddressForm();
                form.setMemberId(ordMasterExt.getMemberId());
                //获取商品详情
                List<OrdConfirmGoodsExt> goodsInfo = new ArrayList<OrdConfirmGoodsExt>();
                OrdConfirmGoodsExt ordConfirmGoodsExt = new OrdConfirmGoodsExt();
                List<OrdConfirmOrderUnitExt> ordConfirmOrderUnitExtList = new ArrayList<OrdConfirmOrderUnitExt>();
                OrdConfirmOrderUnitExt ordConfirmOrderUnitExt = new OrdConfirmOrderUnitExt();
                ordConfirmGoodsExt.setGoodsId(ordMasterExt.getGoodsId());
                ordConfirmGoodsExt.setGoodsName(goodsInfoMap.get("goods_name") == null ? "" : goodsInfoMap.get("goods_name").toString());
                ordConfirmGoodsExt.setType(ordMasterExt.getType());
                ordConfirmGoodsExt.setQuantity(Integer.parseInt(ordMasterExt.getQuantity()));
                ordConfirmGoodsExt.setImageUrlJson(goodsInfoMap.get("image_url_json") == null ? "" : goodsInfoMap.get("image_url_json").toString());
                ordConfirmOrderUnitExt.setGoodsId(ordMasterExt.getGoodsId());
                ordConfirmOrderUnitExt.setGoodsName(goodsInfoMap.get("goods_name") == null ? "" : goodsInfoMap.get("goods_name").toString());
                ordConfirmOrderUnitExt.setSkucontent(goodsInfoMap.get("skucontent") == null ? "" : goodsInfoMap.get("skucontent").toString());
                ordConfirmOrderUnitExt.setSkuId(goodsInfoMap.get("skuid") == null ? "" : goodsInfoMap.get("skuid").toString());
                ordConfirmOrderUnitExt.setPrice(goodsInfoMap.get("price") == null ? "" : goodsInfoMap.get("price").toString());
                //ERP单品
                if ("10".equals(ordMasterExt.getType())) {
                    ordConfirmOrderUnitExt.setColorName(goodsInfoMap.get("color_name") == null ? "" : goodsInfoMap.get("color_name").toString());
                    ordConfirmOrderUnitExt.setSizeName(goodsInfoMap.get("size_name") == null ? "" : goodsInfoMap.get("size_name").toString());
                }
                ordConfirmOrderUnitExt.setImageUrlJson(goodsInfoMap.get("image_url_json") == null ? "" : goodsInfoMap.get("image_url_json").toString());
                ordConfirmOrderUnitExtList.add(ordConfirmOrderUnitExt);
                ordConfirmGoodsExt.setOrdConfirmOrderUnitExtList(ordConfirmOrderUnitExtList);
                goodsInfo.add(ordConfirmGoodsExt);

                actMasterService.checkSecKillActivity(ordConfirmGoodsExt, ordMasterExt.getMemberId());
            }
        }
    }

    public OrdConfirmExt confirmOrderFromGoods(OrdMasterExt ordMasterExt) {
        if (Integer.valueOf(ordMasterExt.getQuantity()) <= 0) {
            throw new ExceptionBusiness("购买商品数量错误!");
        }
        //返回结果
        OrdConfirmExt ordConfirmExt = new OrdConfirmExt();
        //商品信息参数构造
        GdsMasterExt gdsMasterExt = new GdsMasterExt();
        gdsMasterExt.setGoodsId(ordMasterExt.getGoodsId());
        gdsMasterExt.setGoodsSkuId(ordMasterExt.getGoodsSkuId());
        //根据商品ID判断商品是否满足成单条件
        Map<String, Object> goodsInfoMap = new HashMap<String, Object>();
        goodsInfoMap = gdsMasterMapperExt.getAllGoodsInfo(gdsMasterExt);
        if (goodsInfoMap == null) {
            return null;
        } else {
            if (goodsInfoMap.get("new_count") == null) {
                throw new ExceptionBusiness("商品库存不足!");
            }
            if (Integer.valueOf(goodsInfoMap.get("new_count").toString()) == 0
//                    || Integer.valueOf(goodsInfoMap.get("safe_bank").toString())*2 < Integer.valueOf(goodsInfoMap.get("new_count").toString())
                    || Integer.valueOf(ordMasterExt.getQuantity()) > Integer.valueOf(goodsInfoMap.get("new_count").toString())) {
                throw new ExceptionBusiness("商品库存不足");
            } else {
                //会员地址信息参数构造
                MmbAddressForm form = new MmbAddressForm();
                form.setMemberId(ordMasterExt.getMemberId());
                //根据会员ID获取会员地址信息
                List<MmbAddressExt> mmbAddressExt = mmbAddressMapperExt.selectDefaultAddress(form);
                if (mmbAddressExt != null && mmbAddressExt.size() > 0) {
                    ordConfirmExt.setMmbAddressExt(mmbAddressExt.get(0));
                }
                //获取商品详情
                List<OrdConfirmGoodsExt> goodsInfo = new ArrayList<OrdConfirmGoodsExt>();
                OrdConfirmGoodsExt ordConfirmGoodsExt = new OrdConfirmGoodsExt();
                List<OrdConfirmOrderUnitExt> ordConfirmOrderUnitExtList = new ArrayList<OrdConfirmOrderUnitExt>();
                OrdConfirmOrderUnitExt ordConfirmOrderUnitExt = new OrdConfirmOrderUnitExt();
                ordConfirmGoodsExt.setGoodsId(ordMasterExt.getGoodsId());
                ordConfirmGoodsExt.setGoodsName(goodsInfoMap.get("goods_name") == null ? "" : goodsInfoMap.get("goods_name").toString());
                ordConfirmGoodsExt.setType(ordMasterExt.getType());
                ordConfirmGoodsExt.setQuantity(Integer.parseInt(ordMasterExt.getQuantity()));
                ordConfirmGoodsExt.setImageUrlJson(goodsInfoMap.get("image_url_json") == null ? "" : goodsInfoMap.get("image_url_json").toString());
                ordConfirmOrderUnitExt.setGoodsId(ordMasterExt.getGoodsId());
                ordConfirmOrderUnitExt.setGoodsName(goodsInfoMap.get("goods_name") == null ? "" : goodsInfoMap.get("goods_name").toString());
                ordConfirmOrderUnitExt.setSkucontent(goodsInfoMap.get("skucontent") == null ? "" : goodsInfoMap.get("skucontent").toString());
                ordConfirmOrderUnitExt.setSkuId(goodsInfoMap.get("skuid") == null ? "" : goodsInfoMap.get("skuid").toString());
                ordConfirmOrderUnitExt.setPrice(goodsInfoMap.get("price") == null ? "" : goodsInfoMap.get("price").toString());
                //ERP单品
                if ("10".equals(ordMasterExt.getType())) {
                    ordConfirmOrderUnitExt.setColorName(goodsInfoMap.get("color_name") == null ? "" : goodsInfoMap.get("color_name").toString());
                    ordConfirmOrderUnitExt.setSizeName(goodsInfoMap.get("size_name") == null ? "" : goodsInfoMap.get("size_name").toString());
                }
                ordConfirmOrderUnitExt.setImageUrlJson(goodsInfoMap.get("image_url_json") == null ? "" : goodsInfoMap.get("image_url_json").toString());
                ordConfirmOrderUnitExtList.add(ordConfirmOrderUnitExt);
                ordConfirmGoodsExt.setOrdConfirmOrderUnitExtList(ordConfirmOrderUnitExtList);
                goodsInfo.add(ordConfirmGoodsExt);
                // 获取商品级别优惠
                goodsInfo = actMasterService.bindActivityForOrderConfirm(goodsInfo, ordMasterExt.getMemberId(), ordMasterExt.getTelephone(),false);
                ordConfirmExt.setGoodsInfo(goodsInfo);
                ordConfirmExt.setGoodsTotalPrice(getGoodsToatalPrice(goodsInfo));
                // 调用接口获取该笔订单能用的优惠
                ordConfirmExt.setActMasterList(actMasterService.getOrderActivity(ordMasterExt.getMemberId(), goodsInfo));
                //获取配送方式码表数据
                List<ComCode> deliverType = comCodeMapperExt.selectComCodeByCategory("DELIVER_TYPE");
                ordConfirmExt.setDeliverType(deliverType);
            }
        }
        return ordConfirmExt;
    }

    public OrdConfirmExt confirmOrderFromSuitGoods(OrdMasterExt ordMasterExt) {
        //[{"goodsId":"1001","goodsSkuId":"100001"},{"goodsId":"1002","goodsSkuId":"100002"}]
        if (Integer.valueOf(ordMasterExt.getQuantity()) <= 0) {
            throw new ExceptionBusiness("购买商品数量错误!");
        }
        String gdIdAndSkuIdJson = ordMasterExt.getGdIdAndSkuIdJson();
        //返回结果
        OrdConfirmExt ordConfirmExt = new OrdConfirmExt();
        //套装商品信息
        List<Map<String, Object>> goodsInfoList = new ArrayList<Map<String, Object>>();
        JSONArray jsonArray = JSONArray.parseArray(gdIdAndSkuIdJson);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            //商品信息参数构造
            GdsMasterExt gdsMasterExt = new GdsMasterExt();
            gdsMasterExt.setGoodsId(jsonObject.getString("goodsId"));
            gdsMasterExt.setGoodsSkuId(jsonObject.getString("goodsSkuId"));
            Map<String, Object> goodsInfoMap = new HashMap<String, Object>();
            goodsInfoMap = gdsMasterMapperExt.getAllGoodsInfo(gdsMasterExt);
            if (goodsInfoMap == null) {
                return null;
            } else {
                if (goodsInfoMap.get("new_count") == null) {
                    throw new ExceptionBusiness("商品库存不足!");
                }
                if (Integer.valueOf(goodsInfoMap.get("new_count").toString()) == 0
//                        || Integer.valueOf(goodsInfoMap.get("safe_bank").toString()) * 2 < Integer.valueOf(goodsInfoMap.get("new_count").toString())
                        || Integer.valueOf(ordMasterExt.getQuantity()) > Integer.valueOf(goodsInfoMap.get("new_count").toString())) {
                    throw new ExceptionBusiness("商品库存不足!");
                } else {
                    goodsInfoList.add(goodsInfoMap);
                }
            }
        }
        //会员地址信息参数构造
        MmbAddressForm form = new MmbAddressForm();
        form.setMemberId(ordMasterExt.getMemberId());
        //根据会员ID获取会员地址信息
        List<MmbAddressExt> mmbAddressExt = mmbAddressMapperExt.selectDefaultAddress(form);
        if (mmbAddressExt != null && mmbAddressExt.size() > 0) {
            ordConfirmExt.setMmbAddressExt(mmbAddressExt.get(0));
        }
        //获取商品详情
        List<OrdConfirmGoodsExt> goodsInfo = new ArrayList<OrdConfirmGoodsExt>();
        OrdConfirmGoodsExt ordConfirmGoodsExt = new OrdConfirmGoodsExt();
        ordConfirmGoodsExt.setGoodsId(ordMasterExt.getGoodsId());
        GdsMasterExt gdsMaster = gdsMasterMapperExt.selectByPrimaryKey(ordMasterExt.getGoodsId());
        if (gdsMaster == null) {
            throw new ExceptionBusiness("商品不存在");
        }
        ordConfirmGoodsExt.setGoodsName(gdsMaster.getGoodsName());
        ordConfirmGoodsExt.setQuantity(Integer.parseInt(ordMasterExt.getQuantity()));
        ordConfirmGoodsExt.setType(gdsMaster.getType());
        ordConfirmGoodsExt.setImageUrlJson(gdsMaster.getImageUrlJson());
        List<OrdConfirmOrderUnitExt> ordConfirmOrderUnitExtList = new ArrayList<OrdConfirmOrderUnitExt>();
        for (Map<String, Object> map : goodsInfoList) {
            OrdConfirmOrderUnitExt model = new OrdConfirmOrderUnitExt();
            model.setGoodsName(map.get("goods_name") == null ? "" : map.get("goods_name").toString());
            model.setGoodsId(map.get("goods_id") == null ? "" : map.get("goods_id").toString());
            model.setSkucontent(map.get("skucontent") == null ? "" : map.get("skucontent").toString());
            model.setPrice(map.get("price") == null ? "" : map.get("price").toString());
            model.setSkuId(map.get("skuid") == null ? "" : map.get("skuid").toString());
            //ERP单品
            if ("10".equals(map.get("type").toString())) {
                model.setColorName(map.get("color_name") == null ? "" : map.get("color_name").toString());
                model.setSizeName(map.get("size_name") == null ? "" : map.get("size_name").toString());
            }
            model.setImageUrlJson(map.get("image_url_json") == null ? "" : map.get("image_url_json").toString());
            ordConfirmOrderUnitExtList.add(model);
        }
        ordConfirmGoodsExt.setOrdConfirmOrderUnitExtList(ordConfirmOrderUnitExtList);
        goodsInfo.add(ordConfirmGoodsExt);
        // 获取商品级别优惠
        goodsInfo = actMasterService.bindActivityForOrderConfirm(goodsInfo, ordMasterExt.getMemberId(),ordMasterExt.getTelephone(), false);
        ordConfirmExt.setGoodsInfo(goodsInfo);
        ordConfirmExt.setGoodsTotalPrice(getGoodsToatalPrice(goodsInfo));
        // 调用接口获取该笔订单能用的优惠
        ordConfirmExt.setActMasterList(actMasterService.getOrderActivity(ordMasterExt.getMemberId(), goodsInfo));
        //获取配送方式码表数据
        List<ComCode> deliverType = comCodeMapperExt.selectComCodeByCategory("DELIVER_TYPE");
        ordConfirmExt.setDeliverType(deliverType);
        return ordConfirmExt;
    }

    @Override
    public void checkConfirmOrderFromBag(OrdMasterExt ordMasterExt) {
        //购物车编号JSON
        String[] bagNoArray = ordMasterExt.getBagNoArray();
        if (bagNoArray == null || bagNoArray.length == 0) {
            throw new ExceptionBusiness("请选择要结算的商品");
        }
        String bagNo = "";
        for (int i = 0; i < bagNoArray.length - 1; i++) {
            bagNo += "'" + bagNoArray[i] + "',";
        }
        bagNo += "'" + bagNoArray[bagNoArray.length - 1] + "'";
        //获取选择购物车信息
        List<MmbShoppingBagExt> mmbShoppingBagList = mmbShoppingBagMapperExt.queryListByBagNo(bagNo);
        if (mmbShoppingBagList == null || mmbShoppingBagList.size() == 0) {
            throw new ExceptionBusiness("购物车数据获取失败");
        } else {
            //商品信息结果
            List<MmbShoppingBagExt> goodsInfoList = new ArrayList<MmbShoppingBagExt>();
            MmbShoppingBagExt goodsInfo = new MmbShoppingBagExt();
            goodsInfo.setBagNo(mmbShoppingBagList.get(0).getBagNo());
            goodsInfo.setMemberId(mmbShoppingBagList.get(0).getMemberId());
            goodsInfo.setGoodsId(mmbShoppingBagList.get(0).getGoodsId());
            goodsInfo.setActGoodsId(mmbShoppingBagList.get(0).getActGoodsId());
            goodsInfo.setQuantity(mmbShoppingBagList.get(0).getQuantity());
            goodsInfo.setType(mmbShoppingBagList.get(0).getType());

            String goodsId = mmbShoppingBagList.get(0).getGoodsId();
            //遍历整理SKU数据
            List<MmbShoppingSKuExt> skuList = new ArrayList<MmbShoppingSKuExt>();
            for (int i = 0; i < mmbShoppingBagList.size(); i++) {
                if ("30".equals(mmbShoppingBagList.get(i).getType())) {
                    if (goodsId.equals(mmbShoppingBagList.get(i).getGoodsId())) {
                        MmbShoppingSKuExt sku = new MmbShoppingSKuExt();
                        sku.setSkuId(mmbShoppingBagList.get(i).getSkuId());
                        skuList.add(sku);
                        if (i == mmbShoppingBagList.size() - 1) {
                            goodsInfo.setSkuList(skuList);
                            goodsInfoList.add(goodsInfo);
                        }
                    } else {
                        goodsInfo.setSkuList(skuList);
                        goodsInfoList.add(goodsInfo);
                        goodsInfo = new MmbShoppingBagExt();
                        goodsInfo.setBagNo(mmbShoppingBagList.get(i).getBagNo());
                        goodsInfo.setMemberId(mmbShoppingBagList.get(i).getMemberId());
                        goodsInfo.setGoodsId(mmbShoppingBagList.get(i).getGoodsId());
                        goodsInfo.setActGoodsId(mmbShoppingBagList.get(i).getActGoodsId());
                        goodsInfo.setQuantity(mmbShoppingBagList.get(i).getQuantity());
                        goodsInfo.setType(mmbShoppingBagList.get(i).getType());
                        goodsId = mmbShoppingBagList.get(i).getGoodsId();
                        skuList = new ArrayList<MmbShoppingSKuExt>();
                        MmbShoppingSKuExt sku = new MmbShoppingSKuExt();
                        sku.setSkuId(mmbShoppingBagList.get(i).getSkuId());
                        skuList.add(sku);
                        if (i == mmbShoppingBagList.size() - 1) {
                            goodsInfo.setSkuList(skuList);
                            goodsInfoList.add(goodsInfo);
                        }
                    }
                } else {
                    if (skuList != null && skuList.size() > 0) {
                        goodsInfo.setSkuList(skuList);
                        goodsInfoList.add(goodsInfo);
                    }
                    goodsInfo = new MmbShoppingBagExt();
                    goodsInfo.setBagNo(mmbShoppingBagList.get(i).getBagNo());
                    goodsInfo.setMemberId(mmbShoppingBagList.get(i).getMemberId());
                    goodsInfo.setGoodsId(mmbShoppingBagList.get(i).getGoodsId());
                    goodsInfo.setActGoodsId(mmbShoppingBagList.get(i).getActGoodsId());
                    goodsInfo.setQuantity(mmbShoppingBagList.get(i).getQuantity());
                    goodsInfo.setType(mmbShoppingBagList.get(i).getType());
                    skuList = new ArrayList<MmbShoppingSKuExt>();
                    MmbShoppingSKuExt sku = new MmbShoppingSKuExt();
                    sku.setSkuId(mmbShoppingBagList.get(i).getSkuId());
                    skuList.add(sku);
                    goodsInfo.setSkuList(skuList);
                    goodsInfoList.add(goodsInfo);
                    if (mmbShoppingBagList.size() > (i + 1)) {
                        goodsInfo = new MmbShoppingBagExt();
                        goodsId = mmbShoppingBagList.get(i + 1).getGoodsId();
                        goodsInfo.setBagNo(mmbShoppingBagList.get(i + 1).getBagNo());
                        goodsInfo.setMemberId(mmbShoppingBagList.get(i + 1).getMemberId());
                        goodsInfo.setGoodsId(mmbShoppingBagList.get(i + 1).getGoodsId());
                        goodsInfo.setActGoodsId(mmbShoppingBagList.get(i + 1).getActGoodsId());
                        goodsInfo.setQuantity(mmbShoppingBagList.get(i + 1).getQuantity());
                        goodsInfo.setType(mmbShoppingBagList.get(i + 1).getType());
                        skuList = new ArrayList<MmbShoppingSKuExt>();
                    }
                }
            }
            List<OrdConfirmGoodsExt> list = new ArrayList<OrdConfirmGoodsExt>();
            //获取对应商品信息
            for (MmbShoppingBagExt msbe : goodsInfoList) {
                //商品信息数据组装
                OrdConfirmGoodsExt goods = new OrdConfirmGoodsExt();
                goods.setGoodsId(msbe.getGoodsId());
                goods.setQuantity(msbe.getQuantity());
                goods.setType(msbe.getType());
                goods.setActGoodsId(msbe.getActGoodsId());
                GdsMasterExt gdsMaster = gdsMasterMapperExt.selectByPrimaryKey(msbe.getGoodsId());
                if (gdsMaster == null) {
                    throw new ExceptionBusiness("商品不存在");
                }
                goods.setGoodsName(gdsMaster.getGoodsName());
                goods.setImageUrlJson(gdsMaster.getImageUrlJson());
                List<OrdConfirmOrderUnitExt> ordConfirmOrderUnitExtList = new ArrayList<OrdConfirmOrderUnitExt>();
                for (MmbShoppingSKuExt msse : msbe.getSkuList()) {
                    //商品信息参数构造
                    GdsMasterExt gdsMasterExt = new GdsMasterExt();
                    Map<String, Object> goodsInfoMap = new HashMap<String, Object>();
                    //套装场合
                    if ("30".equals(msbe.getType())) {
                        //套装场合
                        gdsMasterExt.setGoodsSkuId(msse.getSkuId());
                        goodsInfoMap = gdsMasterMapperExt.getSuitGoodsInfo(gdsMasterExt);
                    } else {
                        //非套装场合
                        gdsMasterExt.setGoodsId(msbe.getGoodsId());
                        gdsMasterExt.setGoodsSkuId(msse.getSkuId());
                        goodsInfoMap = gdsMasterMapperExt.getAllGoodsInfo(gdsMasterExt);
                    }
                    if (goodsInfoMap == null) {
                        throw new ExceptionBusiness("商品不存在,请联系管理员");
                    }
                    if (goodsInfoMap.get("new_count") == null) {
                        throw new ExceptionBusiness(goodsInfoMap.get("goods_name").toString() + "库存不足!");
                    }
                    if (Integer.valueOf(goodsInfoMap.get("new_count").toString()) == 0
//                            || Integer.valueOf(goodsInfoMap.get("safe_bank").toString()) < Integer.valueOf(goodsInfoMap.get("new_count").toString())
                            || Integer.valueOf(msbe.getQuantity()) > Integer.valueOf(goodsInfoMap.get("new_count").toString())) {
                        throw new ExceptionBusiness(goodsInfoMap.get("goods_name").toString() + "库存不足!");
                    } else {
                        OrdConfirmOrderUnitExt model = new OrdConfirmOrderUnitExt();
                        model.setGoodsName(goodsInfoMap.get("goods_name") == null ? "" : goodsInfoMap.get("goods_name").toString());
                        model.setGoodsId(goodsInfoMap.get("goods_id") == null ? "" : goodsInfoMap.get("goods_id").toString());
                        model.setSkucontent(goodsInfoMap.get("skucontent") == null ? "" : goodsInfoMap.get("skucontent").toString());
                        model.setPrice(goodsInfoMap.get("price") == null ? "" : goodsInfoMap.get("price").toString());
                        model.setSkuId(goodsInfoMap.get("skuid") == null ? "" : goodsInfoMap.get("skuid").toString());
                        //ERP单品
                        if ("10".equals(goodsInfoMap.get("type").toString())) {
                            model.setColorName(goodsInfoMap.get("color_name") == null ? "" : goodsInfoMap.get("color_name").toString());
                            model.setSizeName(goodsInfoMap.get("size_name") == null ? "" : goodsInfoMap.get("size_name").toString());
                        }
                        model.setImageUrlJson(goodsInfoMap.get("image_url_json") == null ? "" : goodsInfoMap.get("image_url_json").toString());
                        ordConfirmOrderUnitExtList.add(model);
                    }
                }
                goods.setOrdConfirmOrderUnitExtList(ordConfirmOrderUnitExtList);
                list.add(goods);
            }

            // 获取商品级别优惠
            actMasterService.checkBindActivityForOrder(list, ordMasterExt.getMemberId());
        }
    }

    public OrdConfirmExt confirmOrderFromBag(OrdMasterExt ordMasterExt) {
        //返回结果
        OrdConfirmExt ordConfirmExt = new OrdConfirmExt();
        //购物车编号JSON
        String[] bagNoArray = ordMasterExt.getBagNoArray();
        if (bagNoArray == null || bagNoArray.length == 0) {
            return null;
        }
        String bagNo = "";
        for (int i = 0; i < bagNoArray.length - 1; i++) {
            bagNo += "'" + bagNoArray[i] + "',";
        }
        bagNo += "'" + bagNoArray[bagNoArray.length - 1] + "'";
        //获取选择购物车信息
        List<MmbShoppingBagExt> mmbShoppingBagList = mmbShoppingBagMapperExt.queryListByBagNo(bagNo);
        if (mmbShoppingBagList == null || mmbShoppingBagList.size() == 0) {
            throw new ExceptionBusiness("购物车数据获取失败");
        } else {
            //商品信息结果
            List<MmbShoppingBagExt> goodsInfoList = new ArrayList<MmbShoppingBagExt>();
            MmbShoppingBagExt goodsInfo = new MmbShoppingBagExt();
            goodsInfo.setBagNo(mmbShoppingBagList.get(0).getBagNo());
            goodsInfo.setMemberId(mmbShoppingBagList.get(0).getMemberId());
            goodsInfo.setGoodsId(mmbShoppingBagList.get(0).getGoodsId());
            goodsInfo.setActGoodsId(mmbShoppingBagList.get(0).getActGoodsId());
            goodsInfo.setQuantity(mmbShoppingBagList.get(0).getQuantity());
            goodsInfo.setType(mmbShoppingBagList.get(0).getType());

            String goodsId = mmbShoppingBagList.get(0).getGoodsId();
            //遍历整理SKU数据
            List<MmbShoppingSKuExt> skuList = new ArrayList<MmbShoppingSKuExt>();
            for (int i = 0; i < mmbShoppingBagList.size(); i++) {
                if ("30".equals(mmbShoppingBagList.get(i).getType())) {
                    if (goodsId.equals(mmbShoppingBagList.get(i).getGoodsId())) {
                        MmbShoppingSKuExt sku = new MmbShoppingSKuExt();
                        sku.setSkuId(mmbShoppingBagList.get(i).getSkuId());
                        skuList.add(sku);
                        if (i == mmbShoppingBagList.size() - 1) {
                            goodsInfo.setSkuList(skuList);
                            goodsInfoList.add(goodsInfo);
                        }
                    } else {
                        goodsInfo.setSkuList(skuList);
                        goodsInfoList.add(goodsInfo);
                        goodsInfo = new MmbShoppingBagExt();
                        goodsInfo.setBagNo(mmbShoppingBagList.get(i).getBagNo());
                        goodsInfo.setMemberId(mmbShoppingBagList.get(i).getMemberId());
                        goodsInfo.setGoodsId(mmbShoppingBagList.get(i).getGoodsId());
                        goodsInfo.setActGoodsId(mmbShoppingBagList.get(i).getActGoodsId());
                        goodsInfo.setQuantity(mmbShoppingBagList.get(i).getQuantity());
                        goodsInfo.setType(mmbShoppingBagList.get(i).getType());
                        goodsId = mmbShoppingBagList.get(i).getGoodsId();
                        skuList = new ArrayList<MmbShoppingSKuExt>();
                        MmbShoppingSKuExt sku = new MmbShoppingSKuExt();
                        sku.setSkuId(mmbShoppingBagList.get(i).getSkuId());
                        skuList.add(sku);
                        if (i == mmbShoppingBagList.size() - 1) {
                            goodsInfo.setSkuList(skuList);
                            goodsInfoList.add(goodsInfo);
                        }
                    }
                } else {
                    if (skuList != null && skuList.size() > 0) {
                        goodsInfo.setSkuList(skuList);
                        goodsInfoList.add(goodsInfo);
                    }
                    goodsInfo = new MmbShoppingBagExt();
                    goodsInfo.setBagNo(mmbShoppingBagList.get(i).getBagNo());
                    goodsInfo.setMemberId(mmbShoppingBagList.get(i).getMemberId());
                    goodsInfo.setGoodsId(mmbShoppingBagList.get(i).getGoodsId());
                    goodsInfo.setActGoodsId(mmbShoppingBagList.get(i).getActGoodsId());
                    goodsInfo.setQuantity(mmbShoppingBagList.get(i).getQuantity());
                    goodsInfo.setType(mmbShoppingBagList.get(i).getType());
                    skuList = new ArrayList<MmbShoppingSKuExt>();
                    MmbShoppingSKuExt sku = new MmbShoppingSKuExt();
                    sku.setSkuId(mmbShoppingBagList.get(i).getSkuId());
                    skuList.add(sku);
                    goodsInfo.setSkuList(skuList);
                    goodsInfoList.add(goodsInfo);
                    if (mmbShoppingBagList.size() > (i + 1)) {
                        goodsInfo = new MmbShoppingBagExt();
                        goodsId = mmbShoppingBagList.get(i + 1).getGoodsId();
                        goodsInfo.setBagNo(mmbShoppingBagList.get(i + 1).getBagNo());
                        goodsInfo.setMemberId(mmbShoppingBagList.get(i + 1).getMemberId());
                        goodsInfo.setGoodsId(mmbShoppingBagList.get(i + 1).getGoodsId());
                        goodsInfo.setActGoodsId(mmbShoppingBagList.get(i + 1).getActGoodsId());
                        goodsInfo.setQuantity(mmbShoppingBagList.get(i + 1).getQuantity());
                        goodsInfo.setType(mmbShoppingBagList.get(i + 1).getType());
                        skuList = new ArrayList<MmbShoppingSKuExt>();
                    }
                }
            }
            List<OrdConfirmGoodsExt> list = new ArrayList<OrdConfirmGoodsExt>();
            //获取对应商品信息
            for (MmbShoppingBagExt msbe : goodsInfoList) {
                //商品信息数据组装
                OrdConfirmGoodsExt goods = new OrdConfirmGoodsExt();
                goods.setGoodsId(msbe.getGoodsId());
                goods.setQuantity(msbe.getQuantity());
                goods.setType(msbe.getType());
                goods.setActGoodsId(msbe.getActGoodsId());
                GdsMasterExt gdsMaster = gdsMasterMapperExt.selectByPrimaryKey(msbe.getGoodsId());
                if (gdsMaster == null) {
                    throw new ExceptionBusiness("商品不存在");
                }
                goods.setGoodsName(gdsMaster.getGoodsName());
                goods.setImageUrlJson(gdsMaster.getImageUrlJson());
                List<OrdConfirmOrderUnitExt> ordConfirmOrderUnitExtList = new ArrayList<OrdConfirmOrderUnitExt>();
                for (MmbShoppingSKuExt msse : msbe.getSkuList()) {
                    //商品信息参数构造
                    GdsMasterExt gdsMasterExt = new GdsMasterExt();
                    Map<String, Object> goodsInfoMap = new HashMap<String, Object>();
                    //套装场合
                    if ("30".equals(msbe.getType())) {
                        //套装场合
                        gdsMasterExt.setGoodsSkuId(msse.getSkuId());
                        goodsInfoMap = gdsMasterMapperExt.getSuitGoodsInfo(gdsMasterExt);
                    } else {
                        //非套装场合
                        gdsMasterExt.setGoodsId(msbe.getGoodsId());
                        gdsMasterExt.setGoodsSkuId(msse.getSkuId());
                        goodsInfoMap = gdsMasterMapperExt.getAllGoodsInfo(gdsMasterExt);
                    }
                    if (goodsInfoMap == null) {
                        throw new ExceptionBusiness("商品不存在,请联系管理员");
                    }
                    if (goodsInfoMap.get("new_count") == null) {
                        throw new ExceptionBusiness(goodsInfoMap.get("goods_name").toString() + "库存不足!");
                    }
                    if (Integer.valueOf(goodsInfoMap.get("new_count").toString()) == 0
//                            || Integer.valueOf(goodsInfoMap.get("safe_bank").toString()) < Integer.valueOf(goodsInfoMap.get("new_count").toString())
                            || Integer.valueOf(msbe.getQuantity()) > Integer.valueOf(goodsInfoMap.get("new_count").toString())) {
                        throw new ExceptionBusiness(goodsInfoMap.get("goods_name").toString() + "库存不足!");
                    } else {
                        OrdConfirmOrderUnitExt model = new OrdConfirmOrderUnitExt();
                        model.setGoodsName(goodsInfoMap.get("goods_name") == null ? "" : goodsInfoMap.get("goods_name").toString());
                        model.setGoodsId(goodsInfoMap.get("goods_id") == null ? "" : goodsInfoMap.get("goods_id").toString());
                        model.setSkucontent(goodsInfoMap.get("skucontent") == null ? "" : goodsInfoMap.get("skucontent").toString());
                        model.setPrice(goodsInfoMap.get("price") == null ? "" : goodsInfoMap.get("price").toString());
                        model.setSkuId(goodsInfoMap.get("skuid") == null ? "" : goodsInfoMap.get("skuid").toString());
                        //ERP单品
                        if ("10".equals(goodsInfoMap.get("type").toString())) {
                            model.setColorName(goodsInfoMap.get("color_name") == null ? "" : goodsInfoMap.get("color_name").toString());
                            model.setSizeName(goodsInfoMap.get("size_name") == null ? "" : goodsInfoMap.get("size_name").toString());
                        }
                        model.setImageUrlJson(goodsInfoMap.get("image_url_json") == null ? "" : goodsInfoMap.get("image_url_json").toString());
                        ordConfirmOrderUnitExtList.add(model);
                    }
                }
                goods.setOrdConfirmOrderUnitExtList(ordConfirmOrderUnitExtList);
                list.add(goods);
            }

            // 获取商品级别优惠
            list = actMasterService.bindActivityForOrderConfirm(list, ordMasterExt.getMemberId(),ordMasterExt.getTelephone(), true);
            // 20180109返回购物车编号
            ordConfirmExt.setBagNoArray(ordMasterExt.getBagNoArray());
            ordConfirmExt.setGoodsInfo(list);
            ordConfirmExt.setGoodsTotalPrice(getGoodsToatalPrice(list));
            //会员地址信息参数构造
            MmbAddressForm form = new MmbAddressForm();
            form.setMemberId(ordMasterExt.getMemberId());
            //根据会员ID获取会员地址信息
            List<MmbAddressExt> mmbAddressExt = mmbAddressMapperExt.selectDefaultAddress(form);
            if (mmbAddressExt != null && mmbAddressExt.size() > 0) {
                ordConfirmExt.setMmbAddressExt(mmbAddressExt.get(0));
            }
            // 调用接口获取该笔订单能用的优惠
            ordConfirmExt.setActMasterList(actMasterService.getOrderActivity(ordMasterExt.getMemberId(), list));
            //获取配送方式码表数据
            List<ComCode> deliverType = comCodeMapperExt.selectComCodeByCategory("DELIVER_TYPE");
            ordConfirmExt.setDeliverType(deliverType);
        }
        return ordConfirmExt;
    }

    private float getGoodsToatalPrice(List<OrdConfirmGoodsExt> goodsList) {
        float goodsTotalPrice = 0;
        for (int i = 0; i < goodsList.size(); i++) {
            float goodsPrice = 0;
            if ("10".equals(goodsList.get(i).getType()) || "20".equals(goodsList.get(i).getType())) {
                goodsPrice = Float.parseFloat(goodsList.get(i).getOrdConfirmOrderUnitExtList().get(0).getPrice());
            } else if ("30".equals(goodsList.get(i).getType())) {
                // 套装
                for (int j = 0; j < goodsList.get(i).getOrdConfirmOrderUnitExtList().size(); j++) {
                    goodsPrice += Float.parseFloat(goodsList.get(i).getOrdConfirmOrderUnitExtList().get(j).getPrice());
                }
            }

            if (goodsList.get(i).getActivity() != null) {
                goodsPrice = goodsList.get(i).getActivity().getNewPrice();
            }
            goodsTotalPrice = goodsTotalPrice + goodsPrice * goodsList.get(i).getQuantity();
        }
        return new BigDecimal(goodsTotalPrice).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public OrdMaster submitOrder(OrdMasterExt ordMasterExt) throws Exception {
        //ordListJson
        // [
        // {"goodsId":"1001","skuInfo":[{"skuId":"101"}],"actionId":"1","actionName":"商品优惠1","priceDiscount":"40","quantity":"2","amount":"100.00","amountDiscount":"80.00"},
        // {"goodsId":"1002","skuInfo":[{"skuId":"102"}],"actionId":"2","actionName":"商品优惠1","priceDiscount":"40","quantity":"2","amount":"100.00","amountDiscount":"80.00"},
        // {"goodsId":"1003","skuInfo":[{"skuId":"103"},{"skuId":"104"}],"actionId":"","actionName":"商品优惠1","priceDiscount":"","quantity":"2","amount":"100.00","amountDiscount":""},
        // {"goodsId":"1004","skuInfo":[{"skuId":"105"}],"actionId":"","actionName":"商品优惠1","priceDiscount":"","quantity":"2","amount":"100.00","amountDiscount":""},
        // ]
        // 代买的情况，需要验证验证码
        String newMemberId = null;
        if (!StringUtil.isEmpty(ordMasterExt.getNewmemberId())) {
            if (StringUtil.isEmpty(ordMasterExt.getCaptcha())) {
                throw new ExceptionBusiness("请输入验证码");
            }
            String tel = ordMasterExt.getNewmemberId();
            // 判断会员是否存在
            MmbMasterExt ext = new MmbMasterExt();
            ext.setTelephone(tel);
            List<MmbMasterExt> list = mmbMasterMapperExt.selectAllByTel(ext);
            if (list == null || list.size() == 0 || !"0".equals(list.get(0).getIsValid())) {
                throw new ExceptionBusiness("手机号对应的会员不存在。请先注册");
            }
            newMemberId = list.get(0).getMemberId();
            SysSmsCaptcha conds = new SysSmsCaptcha();
            conds.setOrgId("50");
            conds.setMobile(tel);
            conds.setCaptcha(ordMasterExt.getCaptcha());
            SysSmsCaptcha record = sysSmsCaptchaMapperExt.selectBySelective(conds);
            if (record == null) {
                throw new ExceptionBusiness("验证码无效");
            } else {
                Date updateTime = record.getUpdateTime();
                Date now = new Date();
                if (now.getTime() > (updateTime.getTime() + Constants.VALIDED_MILLISECONDS)) {
                    throw new ExceptionBusiness("验证码超过10分钟有效时间");
                }
            }
        }

        String result = "";
        String ordListJson = ordMasterExt.getOrdListJson();
        List<OrdListExt> ordListExts = new ArrayList<OrdListExt>();
        ordListExts = JSONArray.parseArray(ordListJson, OrdListExt.class);
        ordMasterExt.setOrdListExtList(ordListExts);

        //------------业务校验 START------
        //商品级优惠价格buss
        BigDecimal goodsSum = new BigDecimal(0);
        //活动优惠金额
        BigDecimal actionDiscount = new BigDecimal(0);

        //float activityNeedFee = 0;
        //代金券优惠金额
        BigDecimal couponDiscount = new BigDecimal(0);
        //积分换购所需积分个数
        int exchangePointCount = 0;
        // 积分抵值所需积分个数
        int pointCount = 0;
        //积分优惠金额
        BigDecimal pointDiscount = new BigDecimal(0);
        //运费金额
        BigDecimal deliveryFee = new BigDecimal(0);

        int goodsCount = 0;
        //------------数据集合end------
        //商品信息
        List<OrdListExt> ordListExtList = ordMasterExt.getOrdListExtList();

        List<OrdConfirmGoodsExt> ordConfirmGoodsExtList = new ArrayList<OrdConfirmGoodsExt>();
        for (OrdListExt ole : ordListExtList) {
            OrdConfirmGoodsExt ordConfirmGoodsExt = new OrdConfirmGoodsExt();
            ordConfirmGoodsExt.setGoodsId(ole.getGoodsId());
            ordConfirmGoodsExt.setQuantity(ole.getQuantity());
            ordConfirmGoodsExt.setActGoodsId(ole.getActionId());
            ordConfirmGoodsExt.setSkuInfo(ole.getSkuInfo());
            if (ole.getPriceDiscount() != null) {
                ordConfirmGoodsExt.setPriceDiscount(ole.getPriceDiscount().setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            }
            List<OrdConfirmOrderUnitExt> ordConfirmOrderUnitExtList = new ArrayList<OrdConfirmOrderUnitExt>();
            for (int i = 0; i < ole.getSkuInfo().size(); i++) {
                OrdConfirmOrderUnitExt ordConfirmOrderUnitExt = new OrdConfirmOrderUnitExt();
                ordConfirmOrderUnitExt.setSkuId(ole.getSkuInfo().get(i).getSkuId());
                ordConfirmOrderUnitExtList.add(ordConfirmOrderUnitExt);
            }
            ordConfirmGoodsExt.setOrdConfirmOrderUnitExtList(ordConfirmOrderUnitExtList);
            ordConfirmGoodsExtList.add(ordConfirmGoodsExt);
        }
        //调用获取优惠信息
        ordConfirmGoodsExtList = actMasterService.bindActivityForOrderConfirm(ordConfirmGoodsExtList, ordMasterExt.getMemberId(),ordMasterExt.getTelephone(), true);

        //商品级优惠校验
        for (OrdConfirmGoodsExt ole : ordConfirmGoodsExtList) {
            if (ole.getActGoodsId() == null || "".equals(ole.getActGoodsId()) || ole.getActivity() == null) {
                if (ole.getSkuInfo().size() == 1) {
                    //单品
                    SkuForm skuInfo = skuMapperExt.selectBySkuId(ole.getSkuInfo().get(0).getSkuId());
                    // 获取单价
                    float priceFloat = skuInfo.getPrice();
//                    ole.getOrdConfirmOrderUnitExtList().get(0).setPrice(String.valueOf(priceFloat));
                    //027:没有活动，但是取新价格
                    ole.getOrdConfirmOrderUnitExtList().get(0).setPrice(String.valueOf(ole.getActivity().getNewPrice()));
//                    BigDecimal price = new BigDecimal(String.valueOf(priceFloat));
                    //027:没有活动，但是取新价格
                    BigDecimal price = new BigDecimal(String.valueOf(ole.getActivity().getNewPrice()));
                    // 单价四舍五入
                    price = price.setScale(2,RoundingMode.HALF_UP);
                    // 获取数量
                    BigDecimal quantity = new BigDecimal(ole.getQuantity());

                    goodsSum = goodsSum.add(price.multiply(quantity));
                    goodsCount += ole.getQuantity();
                } else {
                    // 套装
                    StringBuffer skuBuffer = new StringBuffer();
                    for (int j = 0; j < ole.getSkuInfo().size(); j++) {
                        skuBuffer.append("'");
                        skuBuffer.append(ole.getSkuInfo().get(j).getSkuId());
                        skuBuffer.append("'");
                        if (j != ole.getSkuInfo().size() - 1) {
                            skuBuffer.append(",");
                        }
                    }
                    // 获取单价
                    float suitPirceFloat = skuMapperExt.getSuitPrice(skuBuffer.toString());
//                    BigDecimal suitPrice = new BigDecimal(String.valueOf(suitPirceFloat));
                    //027：
                    BigDecimal suitPrice = new BigDecimal(String.valueOf(ole.getActivity().getNewPrice()));
                    // 单价四舍五入
                    suitPrice = suitPrice.setScale(2,RoundingMode.HALF_UP);
                    // 获取数量
                    BigDecimal quantity = new BigDecimal(ole.getQuantity());
                    goodsSum = goodsSum.add(suitPrice.multiply(quantity));
                    goodsCount += ole.getQuantity();
                }
            } else {
                // 获取单价
                BigDecimal newPrice = new BigDecimal(String.valueOf(ole.getActivity().getNewPrice()));
                // 单价四舍五入
                newPrice = newPrice.setScale(2,RoundingMode.HALF_UP);
                // 获取数量
                BigDecimal quantity = new BigDecimal(ole.getQuantity());

                goodsSum = goodsSum.add(newPrice.multiply(quantity));
                exchangePointCount += ole.getActivity().getPoint() * ole.getQuantity();
                goodsCount += ole.getQuantity();
            }
            goodsSum =  goodsSum.setScale(2,RoundingMode.HALF_UP);
        }

        if (ordMasterExt.getAmountTotle().compareTo(goodsSum) != 0) {
            throw new ExceptionBusiness("商品金额计算有误，请重试!");
        }

        //商家级优惠校验
        if (ordMasterExt.getActionId() == null || "".equals(ordMasterExt.getActionId())) {
            //shopDiscount += goodsSum;
        } else {
            ActMasterForm actMasterForm = actMasterService.getOrderActivityById(ordMasterExt.getMemberId(), ordMasterExt.getActionId(), ordConfirmGoodsExtList);
            if (actMasterForm == null) {
                throw new ExceptionBusiness("该优惠不存在,请重新选择优惠!");
            } else {
                actionDiscount = new BigDecimal(actMasterForm.getCutPrice());
                actionDiscount = actionDiscount.setScale(2,RoundingMode.HALF_UP);
                if (actMasterForm.getNeedFee() != null) {
                    //activityNeedFee = actMasterForm.getNeedFee().floatValue();
                }
                if (actMasterForm.getNeedPoint() != null) {
                    exchangePointCount += actMasterForm.getNeedPoint();
                }

            }
        }

        //积分换购的积分数量校验
        if (ordMasterExt.getExchangePointCount() != exchangePointCount) {
            throw new ExceptionBusiness("订单积分计算有误,请重新选择商品!");
        }
        // 判断用户积分是否足够
        if (exchangePointCount > 0) {
            MmbMaster rMmbMaster = mmbMasterMapper.selectByPrimaryKey(ordMasterExt.getMemberId());
            if (rMmbMaster == null) {
                throw new ExceptionBusiness("用户积分信息获取失败,请稍后重试!");
            }
            if (rMmbMaster.getPoint() < exchangePointCount) {
                throw new ExceptionBusiness("积分不足,请重新选择商品活动!");
            }
        }
        if (actionDiscount.compareTo(ordMasterExt.getAmountDiscount()) != 0) {
            throw new ExceptionBusiness("促销活动有变动,请重新选择活动!");
        }
        //代金券场合
        if (ordMasterExt.getCouponMemberId() == null || "".equals(ordMasterExt.getCouponMemberId())) {
            // couponDiscount += 0;
        } else {
            String memberId = ordMasterExt.getMemberId();
            if (!StringUtil.isEmpty(newMemberId)) {
                memberId = newMemberId;
            }
            CouponMasterExt couponMasterExt = couponMemberService.getOrderCouponById(memberId, ordMasterExt.getCouponMemberId(), goodsSum.subtract(actionDiscount).floatValue());
            if (couponMasterExt == null) {
                throw new ExceptionBusiness("所选择的优惠券已失效,请重新选择优惠券!");
            } else {
                if (net.dlyt.qyds.web.service.common.ComCode.CouponStyle.WORTH.equals(couponMasterExt.getCouponStyle())) {
                    // 抵值
                    couponDiscount = new BigDecimal(couponMasterExt.getWorth());
                } else {
                    //折扣
                    BigDecimal noCouponPrice = goodsSum.subtract(actionDiscount);
                    BigDecimal couponedPrice = noCouponPrice.multiply(couponMasterExt.getDiscount()).divide(new BigDecimal(10),2,RoundingMode.HALF_UP);

                    couponDiscount = noCouponPrice.subtract(couponedPrice);
                }
            }
            couponDiscount = couponDiscount.setScale(2,RoundingMode.HALF_UP);
            if (ordMasterExt.getAmountCoupon().compareTo(couponDiscount) != 0) {
                throw new ExceptionBusiness("所选择的优惠券价格有变动,请重新选择优惠券");
            }
        }

        //订单总价校验 商品总价-商家级优惠价格+商家及优惠所需额外加钱-代金券金额+邮费
        DecimalFormat df = new DecimalFormat("0.00");
        BigDecimal orderFinalPrice = goodsSum.subtract(actionDiscount).subtract(couponDiscount).subtract(pointDiscount);
        if (orderFinalPrice.compareTo(new BigDecimal(goodsCount)) < 0) {
            orderFinalPrice = new BigDecimal(goodsCount);
        }
        BigDecimal finalPrice = orderFinalPrice.subtract(deliveryFee);
        if (ordMasterExt.getPayInfact().compareTo(finalPrice) != 0) {
            throw new ExceptionBusiness("订单金额不正确,请联系管理员!");
        }
        // 业务校验 END
        //-------校验成功场合执行订单创建操作start

        OrdMaster ordMaster = new OrdMaster();
        //订单ID
        ordMaster.setOrderId(UUID.randomUUID().toString());
        //订单编号
        ordMaster.setOrderCode(StringUtil.getBusinessCode());
        //订单类型 10.PC电商，11.手机电商，20.微信，30.APP
        ordMaster.setOrderType(ordMasterExt.getOrderType());
        //会员ID
        String oldMemberId = ordMasterExt.getMemberId();
        ordMaster.setMemberId(oldMemberId);
        //店铺ID
        ordMaster.setShopId(ordMasterExt.getShopId());
        //总价金额
        ordMaster.setAmountTotle(ordMasterExt.getAmountTotle());
        //订单级活动ID
        ordMaster.setActionId(ordMasterExt.getActionId());
        //订单级活动名称
        ordMaster.setActionName(ordMasterExt.getActionName());
        //优惠金额
        ordMaster.setAmountDiscount(ordMasterExt.getAmountDiscount());
        //使用代金券ID
        ordMaster.setCouponId(ordMasterExt.getCouponMemberId());
        //代金券金额
        ordMaster.setAmountCoupon(ordMasterExt.getAmountCoupon());
        //使用积分数量
        ordMaster.setPointCount(ordMasterExt.getPointCount());
        //使用积分兑换金额
        ordMaster.setAmountPoint(ordMasterExt.getAmountPoint());
        //是否包邮  1:是  0:否
        ordMaster.setDeliveryFree("0");
        //运费金额  初始为0.00
        ordMaster.setDeliveryFee(BigDecimal.valueOf(0.00));
        //运费负担方 10.会员，20.店铺
        ordMaster.setPayDeliveryType("10");
        // 应付金额
        ordMaster.setPayInfact(ordMasterExt.getPayInfact());
        //店家应收金额
        ordMaster.setAmountShop(ordMasterExt.getPayInfact());
        //付款方式 10.支付宝，20.微信支付
        ordMaster.setPayType(ordMasterExt.getPayType());
        //客户留言
        ordMaster.setMessage(ordMasterExt.getMessage());
        //订单状态 10.订单未完成，11.取消订单，12.删除订单，20.订单退单中，21.订单退单完成，30.退货中，31.退货完成，40.换货中，41.换货完成，90.订单完成
        ordMaster.setOrderStatus("10");
        //付款状态 10.未付款，20.付款成功，21.付款失败，30.退款中，31.退款完成，40.补款中，41.补款完成
        ordMaster.setPayStatus("10");
        //发货状态  10.未发货，20.已发货，21.已收货，30.退货已发货，31.退货已收货，40.换货已发退货，41.换货已收退货，43.换货已发新货，44.换货已收新货
        ordMaster.setDeliverStatus("10");
        //是否开发票
        ordMaster.setWantInvoice(ordMasterExt.getWantInvoice());
        //发票抬头
        ordMaster.setInvoiceTitle(ordMasterExt.getInvoiceTitle());
        //发票地址
        ordMaster.setInvoiceAddress(ordMasterExt.getInvoiceAddress());
        //电话
        ordMaster.setInvoiceTel(ordMasterExt.getInvoiceTel());
        //税号
        ordMaster.setInvoiceTaxno(ordMasterExt.getInvoiceTaxno());
        //开户行
        ordMaster.setInvoiceBank(ordMasterExt.getInvoiceBank());
        //账号
        ordMaster.setInvoiceAccount(ordMasterExt.getInvoiceAccount());
        //发货方式 10.电商发货，20.门店自提
        ordMaster.setDeliverType(ordMasterExt.getDeliverType());
        // 需要扣除的积分
        ordMaster.setExchangePointCount(ordMasterExt.getExchangePointCount());
        //邮寄地址
        if ("10".equals(ordMasterExt.getDeliverType())) {
            MmbAddress mmbAddress = mmbAddressMapper.selectByPrimaryKey(ordMasterExt.getAddressId());
            //省
            ComDistrict commDistrictProvince = comDistrictMapper.selectByPrimaryKey(mmbAddress.getDistrictidProvince());
            String province = commDistrictProvince.getDistrictName();
            ordMaster.setDistrictidProvince(province);
            //市
            commDistrictProvince = comDistrictMapper.selectByPrimaryKey(mmbAddress.getDistrictidCity());
            String city = commDistrictProvince.getDistrictName();
            ordMaster.setDistrictidCity(city);
            //区
            commDistrictProvince = comDistrictMapper.selectByPrimaryKey(mmbAddress.getDistrictidDistrict());
            String district = commDistrictProvince.getDistrictName();
            ordMaster.setDistrictidDistrict(district);
            //地址
            ordMaster.setDeliveryAddress(mmbAddress.getAddress());
            //联系人
            ordMaster.setDeliveryContactor(mmbAddress.getContactor());
            //联系电话
            ordMaster.setDeliveryPhone(mmbAddress.getPhone());
            //邮编
            ordMaster.setDeliveryPostcode(mmbAddress.getPostcode());
            // 如果是代码的话更新为带买人的地址
            if (!StringUtil.isEmpty(newMemberId)) {
                MmbAddress record = new MmbAddress();
                record.setAddressId(ordMasterExt.getAddressId());
                record.setMemberId(newMemberId);
                mmbAddressMapper.updateByPrimaryKeySelective(record);
            }
        } else {
            //门店ID
            ordMaster.setErpStoreId(ordMasterExt.getErpStoreId());
            //门店名称
            ordMaster.setStoreName(ordMasterExt.getStoreName());
            //门店联系电话
            ordMaster.setStorePhone(ordMasterExt.getStorePhone());

            //联系人
            ordMaster.setDeliveryContactor(ordMasterExt.getCname());
            //联系电话
            ordMaster.setDeliveryPhone(ordMasterExt.getCtel());

            //到店取货的时候
            ordMaster.setDispatchStatus("1");

        }
        //是否允许退货 1:是  0:否
        ordMaster.setCanReturn("1");
        //是否允许换货 1:是  0:否
        ordMaster.setCanExchange("0");
        //是否允许拆单退换货
        if ((ordMasterExt.getActionId() != null && !"".equals(ordMasterExt.getActionId()))
                || (ordMasterExt.getCouponMemberId() != null && !"".equals(ordMasterExt.getCouponMemberId()))) {
            ordMaster.setCanDivide("0");
        } else {
            ordMaster.setCanDivide("1");
        }
        //更新人
        if (!StringUtil.isEmpty(newMemberId)) {
            ordMaster.setUpdateUserId(newMemberId);
        } else {
            ordMaster.setUpdateUserId(oldMemberId);
        }
        //创建人
        ordMaster.setInsertUserId(oldMemberId);
        //订单主表插入
        int insertMaster = ordMasterMapper.insertSelective(ordMaster);
        //插入订单历史记录表
        OrdHistory ordHistory = new OrdHistory();
        ordHistory = this.masterToHistory(ordMaster);
        ordHistory.setSeqOrderId(UUID.randomUUID().toString());
        ordHistory.setInsertUserId(oldMemberId);
        //主订单操作历史信息插入
        int insertHistoryCount = ordHistoryMapperExt.insertSelective(ordHistory);
        //订单商品明细表插入
        for (OrdListExt ole : ordListExts) {
            OrdList ordList = new OrdList();
            //订单明细ID
            ordList.setDetailId(UUID.randomUUID().toString());
            //订单ID
            ordList.setOrderId(ordMaster.getOrderId());
            //获取商品信息
            GdsMasterExt gdsMasterExt = gdsMasterMapperExt.selectByPrimaryKey(ole.getGoodsId());
            //套装场合
            if ("30".equals(gdsMasterExt.getType())) {
                //套装
                ordList.setType("30");
                //商品编码
                ordList.setGoodsCode(gdsMasterExt.getGoodsCode());
                //商品ID
                ordList.setGoodsId(gdsMasterExt.getGoodsId());
                //商品图片
                ordList.setImageUrlJson(gdsMasterExt.getImageUrlJson());
                //商品名称
                ordList.setGoodsName(gdsMasterExt.getGoodsName());
                String skuInfo = "[";
                Double price = 0.00;
                //商品SKU
                for (OrdSkuInfoExt osie : ole.getSkuInfo()) {
                    GdsMasterExt gme = new GdsMasterExt();
                    gme.setGoodsSkuId(osie.getSkuId());
                    //获取商品SKU信息
                    Map<String, Object> goodsInfoMap = gdsMasterMapperExt.getSuitGoodsInfo(gme);
                    //库存不存在场合
                    if (goodsInfoMap.get("new_count") == null) {
                        throw new ExceptionBusiness("库存不足!");
                    }

                    if (Integer.valueOf(goodsInfoMap.get("new_count").toString()) == 0) {
                        throw new ExceptionBusiness("库存不足!");
                    }
                    //商品不在销售场合
                    if (goodsInfoMap == null) {
                        throw new ExceptionBusiness("商品已下架!");
                    }
                    //单个商品的SKU信息
                    String singleSku = "";
                    if (goodsInfoMap.get("type") != null && "10".equals(goodsInfoMap.get("type").toString())) {
                        singleSku += "{\"color_code\":\"" + goodsInfoMap.get("color_code").toString() + "\",\"" +
                                "color_name\":\"" + goodsInfoMap.get("color_name").toString() + "\",\"" +
                                "size_code\":\"" + goodsInfoMap.get("size_code").toString() + "\",\"" +
                                "size_name\":\"" + goodsInfoMap.get("size_name").toString() + "\"}";
                    } else {
                        singleSku += goodsInfoMap.get("skucontent").toString();
                    }
                    skuInfo += singleSku + ",";
                    price += Double.valueOf(goodsInfoMap.get("price").toString());
                    //子订单表插入
                    for (int i = 0; i < ole.getQuantity(); i++) {
                        OrdSubList ordSubList = new OrdSubList();
                        //子订单ID
                        ordSubList.setSubOrderId(UUID.randomUUID().toString());
                        //订单ID
                        ordSubList.setOrderId(ordMaster.getOrderId());
                        //订单明细ID
                        ordSubList.setDetailId(ordList.getDetailId());
                        //商品类型
                        ordSubList.setType(goodsInfoMap.get("type").toString());
                        //商品编码
                        ordSubList.setGoodsCode(goodsInfoMap.get("goods_code").toString());
                        //商品ID
                        ordSubList.setGoodsId(goodsInfoMap.get("goods_id").toString());
                        //商品名称
                        ordSubList.setGoodsName(goodsInfoMap.get("goods_name").toString());
                        //商品SKU
                        ordSubList.setSku(singleSku);
                        //商品SKUID
                        ordSubList.setSkuId(osie.getSkuId());
                        //原价
                        ordSubList.setPrice(BigDecimal.valueOf(Double.valueOf(goodsInfoMap.get("price").toString())));
                        //件数
                        ordSubList.setQuantity(1);
                        //原价金额
                        ordSubList.setAmount(BigDecimal.valueOf(Double.valueOf(goodsInfoMap.get("price").toString())));
                        //发货状态
                        ordSubList.setDeliverStatus("10");
                        //活动ID
                        ordSubList.setActionId(ole.getActionId());
                        // 活动名称
                        ordSubList.setActionName(ole.getActionName());
                        //删除标志
                        ordSubList.setDeleted("0");
                        //修改人
                        ordSubList.setUpdateUserId(oldMemberId);
                        //创建人
                        ordSubList.setInsertUserId(oldMemberId);

                        //到店取货
                        if (!"10".equals(ordMasterExt.getDeliverType())) {
                            ordSubList.setErpStoreId(ordMasterExt.getErpStoreId());
                            ordSubList.setStoreName(ordMasterExt.getStoreName());
                            ordSubList.setStorePhone(ordMasterExt.getStorePhone());
                            ordSubList.setDispatchStatus("1");
                            ordSubList.setDispatchStore(ordMasterExt.getErpStoreId());
                        }
                        //插入子订单
                        int subCount = ordSubListMapperExt.insertSelective(ordSubList);
                    }
                }
                //订单商品明细SKU
                ordList.setSku(skuInfo.substring(0, skuInfo.length() - 1) + "]");
                //价格
                ordList.setPrice(BigDecimal.valueOf(price));
                //活动ID
                ordList.setActionId(ole.getActionId());
                //活动名称
                ordList.setActionName(ole.getActionName());
                //优惠后价格
                ordList.setPriceDiscount(ole.getPriceDiscount());
                //件数
                ordList.setQuantity(ole.getQuantity());
                //原价金额
                ordList.setAmount(ole.getAmount());
                //优惠后金额
                ordList.setAmountDiscount(ole.getAmountDiscount());
                //删除标记
                ordList.setDeleted("0");
                //修改人
                ordList.setUpdateUserId(oldMemberId);
                //创建人
                ordList.setInsertUserId(oldMemberId);
                //插入商品明细订单
                int ordCount = ordListMapperExt.insertSelective(ordList);
            } else {
                //非套装
                ordList.setType(gdsMasterExt.getType());
                //商品编码
                ordList.setGoodsCode(gdsMasterExt.getGoodsCode());
                //商品ID
                ordList.setGoodsId(gdsMasterExt.getGoodsId());
                //商品图片
                ordList.setImageUrlJson(gdsMasterExt.getImageUrlJson());
                //商品名称
                ordList.setGoodsName(gdsMasterExt.getGoodsName());
                //商品SKU
                String skuInfo = "[";
                Double price = 0.00;
                //商品SKU
                for (OrdSkuInfoExt osie : ole.getSkuInfo()) {
                    GdsMasterExt gme = new GdsMasterExt();
                    gme.setGoodsSkuId(osie.getSkuId());
                    //获取商品SKU信息
                    Map<String, Object> goodsInfoMap = gdsMasterMapperExt.getSuitGoodsInfo(gme);
                    //库存不存在场合
                    if (goodsInfoMap.get("new_count") == null) {
                        throw new ExceptionBusiness("库存不足!");
                    }
                    if (Integer.valueOf(goodsInfoMap.get("new_count").toString()) == 0) {
                        throw new ExceptionBusiness("库存不足!");
                    }
                    //商品不在销售场合
                    if (goodsInfoMap == null) {
                        throw new ExceptionBusiness("商品已下架!");
                    }
                    //单个商品的SKU信息
                    String singleSku = "";
                    if (goodsInfoMap.get("type") != null && "10".equals(goodsInfoMap.get("type").toString())) {
                        singleSku += "{\"color_code\":\"" + goodsInfoMap.get("color_code").toString() + "\",\"" +
                                "color_name\":\"" + goodsInfoMap.get("color_name").toString() + "\",\"" +
                                "size_code\":\"" + goodsInfoMap.get("size_code").toString() + "\",\"" +
                                "size_name\":\"" + goodsInfoMap.get("size_name").toString() + "\"}";
                    } else {
                        singleSku += goodsInfoMap.get("skucontent").toString();
                    }
                    skuInfo += singleSku + "]";
                    price += Double.valueOf(goodsInfoMap.get("price").toString());
                    //子订单表插入
                    for (int i = 0; i < ole.getQuantity(); i++) {
                        OrdSubList ordSubList = new OrdSubList();
                        //子订单ID
                        ordSubList.setSubOrderId(UUID.randomUUID().toString());
                        //订单ID
                        ordSubList.setOrderId(ordMaster.getOrderId());
                        //订单明细ID
                        ordSubList.setDetailId(ordList.getDetailId());
                        //商品类型
                        ordSubList.setType(goodsInfoMap.get("type").toString());
                        //商品编码
                        ordSubList.setGoodsCode(goodsInfoMap.get("goods_code").toString());
                        //商品ID
                        ordSubList.setGoodsId(goodsInfoMap.get("goods_id").toString());
                        //商品名称
                        ordSubList.setGoodsName(goodsInfoMap.get("goods_name").toString());
                        //商品SKU
                        ordSubList.setSku(singleSku);
                        //商品SKUID
                        ordSubList.setSkuId(osie.getSkuId());
                        //原价
                        ordSubList.setPrice(BigDecimal.valueOf(Double.valueOf(goodsInfoMap.get("price").toString())));
                        //优惠后金额
                        ordSubList.setPriceDiscount(ole.getPriceDiscount());
                        //件数
                        ordSubList.setQuantity(1);
                        //原价金额
                        ordSubList.setAmount(BigDecimal.valueOf(Double.valueOf(goodsInfoMap.get("price").toString())));
                        //发货状态
                        ordSubList.setDeliverStatus("10");
                        //活动ID
                        ordSubList.setActionId(ole.getActionId());
                        // 活动名称
                        ordSubList.setActionName(ole.getActionName());
                        //删除标志
                        ordSubList.setDeleted("0");
                        //修改人
                        ordSubList.setUpdateUserId(oldMemberId);
                        //创建人
                        ordSubList.setInsertUserId(oldMemberId);
                        //到店取货
                        if (!"10".equals(ordMasterExt.getDeliverType())) {
                            ordSubList.setErpStoreId(ordMasterExt.getErpStoreId());
                            ordSubList.setStoreName(ordMasterExt.getStoreName());
                            ordSubList.setStorePhone(ordMasterExt.getStorePhone());
                            ordSubList.setDispatchStatus("1");
                            ordSubList.setDispatchStore(ordMasterExt.getErpStoreId());
                        }
                        //插入子订单
                        int subCount = ordSubListMapperExt.insertSelective(ordSubList);
                    }
                }
                //订单商品明细SKU
                ordList.setSku(skuInfo.substring(0, skuInfo.length() - 1) + "]");
                //价格
                ordList.setPrice(BigDecimal.valueOf(price));
                //活动ID
                ordList.setActionId(ole.getActionId());
                //活动名称
                ordList.setActionName(ole.getActionName());
                //优惠后价格
                ordList.setPriceDiscount(ole.getPriceDiscount());
                //件数
                ordList.setQuantity(ole.getQuantity());
                //原价金额
                ordList.setAmount(ole.getAmount());
                //优惠后金额
                ordList.setAmountDiscount(ole.getAmountDiscount());
                //删除标记
                ordList.setDeleted("0");
                //修改人
                ordList.setUpdateUserId(oldMemberId);
                //创建人
                ordList.setInsertUserId(oldMemberId);
                // 是不是赠品
                ordList.setIsGift(ole.getIsGift());
                //插入商品明细订单
                int ordCount = ordListMapperExt.insertSelective(ordList);
            }
        }
        //-------校验成功场合执行订单创建操作end
        // 订单总共优惠的价格
        BigDecimal discountSharePrice = goodsSum.subtract(orderFinalPrice);
        // 订单总共优惠的价格分摊给每个子订单 START
        // 获取所有子订单
        List<OrdSubList> subLists = ordSubListMapperExt.selectSubOrderByOrderId(ordMaster.getOrderId());
        // 获取订单关联的秒杀活动商品
        List<ActGoods> actGoodsList = actGoodsMapperExt.selectSecKillGoodsByOrderId(ordMaster.getOrderId());
        for(ActGoods actItem : actGoodsList){
            ActGoods actGoods = actGoodsMapper.selectByPrimaryKey(actItem.getActGoodsId());
            if(actGoods != null){
                // 秒杀活动商品数量减去购买数量
                actGoods.setSurplus(actGoods.getSurplus() - actItem.getQuantity());
                actGoodsMapper.updateByPrimaryKeySelective(actGoods);
            }
        }
        if (discountSharePrice.compareTo(new BigDecimal(0))>0) {
            // 按照价格从高到底排序
            Collections.sort(subLists, new Comparator<OrdSubList>() {
                public int compare(OrdSubList o1, OrdSubList o2) {
                    // 其余的按照价格从大到小排序
                    BigDecimal price1 = null;
                    BigDecimal price2 = null;
                    if (o1.getPriceDiscount() != null && o1.getPriceDiscount().compareTo(new BigDecimal(0)) > 0) {
                        price1 = o1.getPriceDiscount();
                    } else {
                        price1 = o1.getPrice();
                    }
                    if (o2.getPriceDiscount() != null && o2.getPriceDiscount().compareTo(new BigDecimal(0)) > 0) {
                        price2 = o2.getPriceDiscount();
                    } else {
                        price2 = o2.getPrice();
                    }
                    if (price2.compareTo(price1) < 0) {
                        return -1;
                    } else if (price2.compareTo(price1) > 0) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
            BigDecimal sharedPrice = new BigDecimal(df.format(0));
            for (int i = 0; i < subLists.size(); i++) {
                if (i > 0) {
                    BigDecimal price = null;
                    if (subLists.get(i).getPriceDiscount() != null && subLists.get(i).getPriceDiscount().compareTo(new BigDecimal(0)) > 0) {
                        price = subLists.get(i).getPriceDiscount();
                    } else {
                        price = subLists.get(i).getPrice();
                    }
                    BigDecimal sharePrice = price.divide(goodsSum,2,RoundingMode.HALF_UP).multiply(discountSharePrice);
                    if (price.subtract(sharePrice).compareTo(new BigDecimal(1)) < 0) {
                        //如果价格减去分摊的价格小于1元,则不分摊了
                        subLists.get(i).setPriceShare(price);
                    } else {
                        subLists.get(i).setPriceShare(price.subtract(new BigDecimal(df.format(sharePrice))));
                        sharedPrice = sharedPrice.add(new BigDecimal(df.format(sharePrice)));
                    }
                    ordSubListMapper.updateByPrimaryKeySelective(subLists.get(i));
                }
            }
            // 再处理第0个
            BigDecimal leftSharePrice = new BigDecimal(df.format(discountSharePrice)).subtract(sharedPrice);
            BigDecimal price = null;
            if (subLists.get(0).getPriceDiscount() != null && subLists.get(0).getPriceDiscount().compareTo(new BigDecimal(0)) > 0) {
                price = subLists.get(0).getPriceDiscount();
            } else {
                price = subLists.get(0).getPrice();
            }
            subLists.get(0).setPriceShare(price.subtract(leftSharePrice));
            ordSubListMapper.updateByPrimaryKeySelective(subLists.get(0));
            //订单总共优惠的价格分摊给每个子订单 END
        } else {
            for (int i = 0; i < subLists.size(); i++) {
                BigDecimal price = null;
                if (subLists.get(i).getPriceDiscount() != null && subLists.get(i).getPriceDiscount().compareTo(new BigDecimal(0)) > 0) {
                    price = subLists.get(i).getPriceDiscount();
                } else {
                    price = subLists.get(i).getPrice();
                }
                subLists.get(i).setPriceShare(price);
                ordSubListMapper.updateByPrimaryKeySelective(subLists.get(i));
            }
        }

        //使用优惠券后进行优惠券状态修正
        if (ordMaster.getCouponId() != null || !"".equals(ordMaster.getCouponId())) {
            CouponMemberExt form = new CouponMemberExt();
            form.setOrderId(ordMaster.getOrderId());
            form.setCouponMemberId(ordMaster.getCouponId());
            couponMemberService.useCoupon(form);
        }
//        //更新库存信息
//        bnkMasterService.submitOrderReduceBank(ordMaster.getOrderId(),ordMasterExt.getMemberId());
        //执行结束返回成功
        //result = ordMaster.getOrderCode();

        return ordMaster;
    }

    public List<OrdReturnExchangeExt> getReturnGoodsInfo(OrdReturnExchangeExt ordReturnExchangeExt) {
        List<OrdReturnExchangeExt> ordReturnExchangeList = ordReturnExchangeMapperExt.viewApplyReturnGoods(ordReturnExchangeExt);
        return ordReturnExchangeList;
    }

    /**
     * 支付前校验秒杀活动商品信息
     * @param orderId
     * @param orderCode
     * @return
     */
    @Override
    public void checkSecActivityOrderInfo(String orderId, String orderCode){

        if(StringUtil.isEmpty(orderId) && StringUtil.isEmpty(orderCode)){
            throw new ExceptionBusiness("订单参数为空!");
        }

        OrdMaster ordMaster;

        // 订单主表信息
        if(null == orderId){
            ordMaster = ordMasterMapperExt.selectByOrderCode(orderCode);
        } else {
            ordMaster = ordMasterMapper.selectByPrimaryKey(orderId);
        }

        if(ordMaster == null){
            throw new ExceptionBusiness("订单信息已更新!");
        }

        // 查找该订单所有参加了秒杀活动的子订单信息
        List<OrdSubList> subLists = ordSubListMapperExt.selectSecKillActivityOrdSubList(ordMaster.getOrderId());

        for(OrdSubList item : subLists){

            ActGoods goodsForm = new ActGoods();
            goodsForm.setActivityId(item.getActionId());
            goodsForm.setGoodsId(item.getGoodsId());
            goodsForm.setSkuId(item.getSkuId());
            List<ActGoods> list = actGoodsMapperExt.selectActGoodsInfo(goodsForm);
            ActGoods actGoods;
            if(list != null && list.size() == 1){
                actGoods = list.get(0);
            } else {
                throw new ExceptionBusiness("秒杀活动不存在");
            }

            // 比较当前活动剩余数量是否满足购买数量
            if(actGoods.getSurplus() < item.getQuantity()) {
                throw new ExceptionBusiness("秒杀商品数量不足");
            }

            if(null != actGoods.getBuyMax() && actGoods.getBuyMax() > 0){
                OrdSubList subList = new OrdSubList();
                subList.setSkuId(item.getSkuId());
                subList.setActionId(item.getActionId());
                subList.setInsertUserId(ordMaster.getMemberId());
                subList.setOrderId(ordMaster.getOrderId());
                int count = ordSubListMapperExt.countMemberSecKillGoods(subList);

                // 比较已经购买数量加上当前订单数量是否超过购买限制
                if(count + item.getQuantity() > actGoods.getBuyMax()) {
                    throw new ExceptionBusiness("秒杀商品数量超过购买限制");
                }
            }
        }

    }

    /**
     * 类型转换
     *
     * @param ordMaster
     * @return
     * @工具类 OrdMaster转换OrdHistory
     */
    public OrdHistory masterToHistory(OrdMaster ordMaster) {
        OrdHistory ordHistory = new OrdHistory();
        BeanUtils.copyProperties(ordMaster, ordHistory);
        return ordHistory;
    }

    /**
     * 订单发货处理
     *
     * @param data
     * @return JSONObject
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject sendOrder(String data) {
        JSONObject map = new JSONObject();
        JSONObject jsonTmp = new JSONObject();
        log.debug("ERP send order input:" + JSON.toJSONString(data));
        System.out.println("ERP send order input:" + JSON.toJSONString(data));
        try {
            //取得数据
            List<OrdSubList> ll = JSON.parseArray(data, OrdSubList.class);
            if (ll.size() <= 0) {
                throw new ExceptionBusiness("没有订单数据");
            }
            String orderId = "";
            String subOrderId = "";
            OrdHistory history = new OrdHistory();
            OrdMaster master = new OrdMaster();
//            OrdSubList sub = new OrdSubList();
            for (OrdSubList record : ll) {
                //子订单ID检查
                subOrderId = record.getSubOrderId();
                if (StringUtil.isEmpty(subOrderId)) {
                    throw new ExceptionBusiness("子订单ID为空");
                }
                //主订单ID检查
                if (StringUtil.isEmpty(record.getOrderId())) {
                    throw new ExceptionBusiness("订单ID为空");
                }
                //取得主订单
                if (!orderId.equals(record.getOrderId())) {
                    orderId = record.getOrderId();
                    master = ordMasterMapper.selectByPrimaryKey(orderId);
                    if (master == null) {
                        throw new ExceptionBusiness("订单不存在");
                    }
                    //设定履历数据
                    history.setOrderId(master.getOrderId());
                    history.setOrderCode(master.getOrderCode());
                    history.setOrderType(master.getOrderType());
                    history.setMemberId(master.getMemberId());
                    history.setShopId(master.getShopId());
                    history.setAmountTotle(master.getAmountTotle());
                    history.setActionId(master.getActionId());
                    history.setActionName(master.getActionName());
                    history.setAmountDiscount(master.getAmountDiscount());
                    history.setCouponId(master.getCouponId());
                    history.setAmountCoupon(master.getAmountCoupon());
                    history.setPointCount(master.getPointCount());
                    history.setAmountPoint(master.getAmountPoint());
                    history.setDeliveryFree(master.getDeliveryFree());
                    history.setDeliveryFee(master.getDeliveryFee());
                    history.setPayDeliveryType(master.getPayDeliveryType());
                    history.setPayInfact(master.getPayInfact());
                    history.setServiceFee(master.getServiceFee());
                    history.setAmountShop(master.getAmountShop());
                    history.setPayType(master.getPayType());
                    history.setPayAccount(master.getPayAccount());
                    history.setAmount(master.getAmount());
                    history.setReceiptAccount(master.getReceiptAccount());
                    history.setMessage(master.getMessage());
                    history.setOrderStatus(master.getOrderStatus());
                    history.setOrderTime(master.getOrderTime());
                    history.setPayStatus(master.getPayStatus());
                    history.setPayTime(master.getPayTime());
                    //付款请求
//                    jsonTmp.clear();
//                history.setPayReqJson("");
                    //付款返回
//                history.setPayRespJson("");
                    history.setReceiptTime(master.getReceiptTime());
                    history.setEvaluateStatus(master.getEvaluateStatus());
                    history.setEvaluateTime(master.getEvaluateTime());
                    history.setWantInvoice(master.getWantInvoice());
                    //发票信息
                    jsonTmp.clear();
                    jsonTmp.put("invoice_type", master.getInvoiceType());
                    jsonTmp.put("invoice_title", master.getInvoiceTitle());
                    jsonTmp.put("invoice_address", master.getInvoiceAddress());
                    jsonTmp.put("invoice_tel", master.getInvoiceTel());
                    jsonTmp.put("invoice_taxno", master.getInvoiceTaxno());
                    jsonTmp.put("invoice_bank", master.getInvoiceBank());
                    jsonTmp.put("invoice_account", master.getInvoiceAccount());
                    jsonTmp.put("invoice_no", master.getInvoiceNo());
                    history.setInvoiceJson(jsonTmp.toJSONString());
                    history.setInvoiceAccount(master.getInvoiceAccount());
                    history.setErpStoreid(master.getErpStoreId());
                    history.setStorename(master.getStoreName());
                    history.setStorePhone(master.getStorePhone());
                    history.setStoreDeliveryid(master.getStoreDeliveryId());
                    history.setStoreDeliveryname(master.getStoreDeliveryName());
                    history.setCanReturn(master.getCanReturn());
                    history.setCanExchange(master.getCanExchange());
                    history.setCanDivide(master.getCanDivide());
                    history.setCancelType(master.getCancelType());
                    //发货信息
                    jsonTmp.clear();
                    jsonTmp.put("deliver_type", master.getDeliverType());
//                    jsonTmp.put("deliver_fee",master.getDeliveryFee());
                    jsonTmp.put("deliver_contactor", master.getDeliveryContactor());
                    jsonTmp.put("deliver_phone", master.getDeliveryPhone());
                    jsonTmp.put("deliver_address", master.getDeliveryAddress());
                    jsonTmp.put("deliver_postcode", master.getDeliveryPostcode());
                    history.setDeliveryJson(jsonTmp.toJSONString());
                }
                //更新子订单
                int upCount = ordSubListMapperExt.updateByErp(record);
                if (upCount == 0) {
                    throw new ExceptionBusiness(record.getSubOrderId() + "子订单不存在");
                }
                //取得未发货子订单件数
                int countNoSend = ordSubListMapperExt.getCountNoSendByOrderId(orderId);
                //取得已发货子订单件数
                int countSend = ordSubListMapperExt.getCountSendByOrderId(orderId);
                //发货时间
                master.setDeliverTime(record.getDeliverTime());
                //更新主订单发货状态和发货时间
                if (countNoSend == 0) {
                    master.setDeliverStatus("20");
                } else if (countSend == 0) {
                    master.setDeliverStatus("19");
                }
                ordMasterMapper.updateByPrimaryKeySelective(master);
                //插入履历
                history.setDeliverStatus(master.getDeliverStatus());
                history.setDeliverTime(master.getDeliverTime());
                jsonTmp.clear();
                jsonTmp.put("sub_order_id", record.getSubOrderId());
                jsonTmp.put("sku", record.getSku());
                jsonTmp.put("express_id", record.getExpressId());
                jsonTmp.put("express_name", record.getExpressName());
                jsonTmp.put("express_no", record.getExpressNo());
                history.setExpressJson(jsonTmp.toJSONString());
                history.setSeqOrderId(UUID.randomUUID().toString());
                ordHistoryMapper.insertSelective(history);
            }
            map.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            map.put("resultCode", Constants.FAIL);
            map.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            map.put("resultCode", Constants.FAIL);
            map.put("resultMessage", e.getMessage());
        }
        log.debug("ERP send order result:" + map.toString());
        System.out.println("ERP send order result:" + map.toString());
        return map;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void acceptReturnAllGoods(String orderId, Map<String, Object> userMap) {
        //参数准备
        OrdReturnExchangeExt ordReturnExchangeExt = new OrdReturnExchangeExt();
        ordReturnExchangeExt.setOrderId(orderId);
        //校验该订单是否满足退货确认收货情况
        List<OrdReturnExchangeExt> ordReturnExchangeList = ordReturnExchangeMapperExt.viewApplyReturnGoods(ordReturnExchangeExt);
        if (ordReturnExchangeList == null || ordReturnExchangeList.size() == 0) {
            throw new ExceptionBusiness("订单未退货,请联系系统管理员");
        }
        for (OrdReturnExchangeExt oree : ordReturnExchangeList) {
            //非全单退场合,退货状态非退货已发,退款状态非等待退款
            if (!"10".equals(oree.getRexModeCode()) || !"20".equals(oree.getRexStatusCode()) || !"10".equals(oree.getRefundStatus())) {
                throw new ExceptionBusiness("订单退货还未发货!");
            }
        }
        //退货确认收货
        OrdReturnExchange ordReturnExchange = new OrdReturnExchange();
        //订单ID
        ordReturnExchange.setOrderId(orderId);
        //登录人
        String loginId = (String) userMap.get("loginId");
        //退货状态 22:退货已收
        ordReturnExchange.setRexStatus("22");
        //退货确认人
        ordReturnExchange.setReturnAcceptUserId(loginId);
        //修改人
        ordReturnExchange.setUpdateUserId(loginId);
        //更新退货单
        try {
            ordReturnExchangeMapperExt.acceptReturnGoods(ordReturnExchange);
        } catch (ExceptionBusiness eb) {
            throw new ExceptionBusiness("更新失败");
        }
        //更新子订单表状态为退货已收货
        OrdSubList ordSubList = new OrdSubList();
        //发货状态  31.退货已收货
        ordSubList.setDeliverStatus("31");
        //修改人
        ordSubList.setUpdateUserId(loginId);
        //订单ID
        ordSubList.setOrderId(orderId);
        try {
            ordSubListMapperExt.acceptReturnGoods(ordSubList);
        } catch (ExceptionBusiness eb) {
            throw new ExceptionBusiness("更新失败");
        }
        //更新主订单状态为退货已收货
        OrdMaster ordMaster = new OrdMaster();
        //发货状态 31:退货已收货
        ordMaster.setDeliverStatus("31");
        //订单ID
        ordMaster.setOrderId(orderId);
        //更新人
        ordMaster.setUpdateUserId(loginId);
        try {
            ordMasterMapperExt.acceptReturnGoods(ordMaster);
        } catch (ExceptionBusiness eb) {
            throw new ExceptionBusiness("更新失败");
        }
        //更新订单历史表
        ordMaster = ordMasterMapper.selectByPrimaryKey(ordReturnExchange.getOrderId());
        //插入订单历史记录表
        OrdHistory ordHistory = new OrdHistory();
        ordHistory = this.masterToHistory(ordMaster);
        ordHistory.setSeqOrderId(UUID.randomUUID().toString());
        ordHistory.setInsertUserId(loginId);
        //主订单操作历史信息插入
        int insertHistoryCount = ordHistoryMapperExt.insertSelective(ordHistory);
        //更新库存信息
//        try {
//            bnkMasterService.returnOrderAddBank(orderId, null, loginId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void acceptReturnSubGoods(String subOrderId, Map<String, Object> userMap) {
        //参数准备
        OrdReturnExchangeExt ordReturnExchangeExt = new OrdReturnExchangeExt();
        ordReturnExchangeExt.setSubOrderId(subOrderId);
        //校验该订单是否满足退货确认收货情况
        List<OrdReturnExchangeExt> ordReturnExchangeList = ordReturnExchangeMapperExt.viewApplyReturnGoods(ordReturnExchangeExt);
        if (ordReturnExchangeList == null || ordReturnExchangeList.size() == 0) {
            throw new ExceptionBusiness("订单未退货,请联系系统管理员");
        }
        for (OrdReturnExchangeExt oree : ordReturnExchangeList) {
            //非全单退场合,退货状态非退货已发,退款状态非等待退款
            if (!"20".equals(oree.getRexStatusCode()) || !"10".equals(oree.getRefundStatus())) {
                throw new ExceptionBusiness("订单退货还未发货!");
            }
        }
        //退货确认收货
        OrdReturnExchange ordReturnExchange = new OrdReturnExchange();
        //订单ID
        ordReturnExchange.setSubOrderId(subOrderId);
        //登录人
        String loginId = (String) userMap.get("loginId");
        //退货状态 22:退货已收
        ordReturnExchange.setRexStatus("22");
        //退货确认人
        ordReturnExchange.setReturnAcceptUserId(loginId);
        //修改人
        ordReturnExchange.setUpdateUserId(loginId);
        //更新退货单
        try {
            ordReturnExchangeMapperExt.acceptReturnGoods(ordReturnExchange);
        } catch (ExceptionBusiness eb) {
            throw new ExceptionBusiness("更新失败");
        }
        //更新子订单表状态为退货已收货
        OrdSubList ordSubList = new OrdSubList();
        //发货状态  31.退货已收货
        ordSubList.setDeliverStatus("31");
        //修改人
        ordSubList.setUpdateUserId(loginId);
        //子订单ID
        ordSubList.setSubOrderId(subOrderId);
        try {
            ordSubListMapperExt.acceptReturnGoods(ordSubList);
        } catch (ExceptionBusiness eb) {
            throw new ExceptionBusiness("更新失败");
        }
        //判断是否更新主表信息
        int count = ordReturnExchangeMapperExt.getReturnExchangeCount(ordReturnExchangeList.get(0).getOrderId());
        if (count == 0) {
            //更新主订单状态为退货已收货
            OrdMaster ordMaster = new OrdMaster();
            //发货状态 31:退货已收货
            ordMaster.setDeliverStatus("31");
            //订单ID
            ordMaster.setOrderId(ordReturnExchangeList.get(0).getOrderId());
            //更新人
            ordMaster.setUpdateUserId(loginId);
            try {
                ordMasterMapperExt.acceptReturnGoods(ordMaster);
            } catch (ExceptionBusiness eb) {
                throw new ExceptionBusiness("更新失败");
            }
            //更新订单历史表
            ordMaster = ordMasterMapper.selectByPrimaryKey(ordReturnExchange.getOrderId());
            //插入订单历史记录表
            OrdHistory ordHistory = new OrdHistory();
            ordHistory = this.masterToHistory(ordMaster);
            ordHistory.setSeqOrderId(UUID.randomUUID().toString());
            ordHistory.setInsertUserId(loginId);
            //主订单操作历史信息插入
            int insertHistoryCount = ordHistoryMapperExt.insertSelective(ordHistory);
        }
//        //更新库存信息
//        try {
//            bnkMasterService.returnOrderAddBank(null, subOrderId, loginId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public void submitReturnMoney(OrdReturnExchange ordReturnExchange, Map<String, Object> userMap) {
        //参数准备
        OrdReturnExchangeExt ordReturnExchangeExt = new OrdReturnExchangeExt();
        ordReturnExchangeExt.setSubOrderId(ordReturnExchange.getSubOrderId());
        //退货状态为 21:退货已收
        ordReturnExchangeExt.setRexStatus("21");
        //退款状态为 10:等待退款
        ordReturnExchangeExt.setRefundStatus("10");
        //校验该订单是否满足退货确认收货情况
        List<OrdReturnExchangeExt> ordReturnExchangeList = ordReturnExchangeMapperExt.viewApplyReturnGoods(ordReturnExchangeExt);
        if (ordReturnExchangeList == null || ordReturnExchangeList.size() == 0) {
            throw new ExceptionBusiness("订单未到确认退单场合,请联系系统管理员");
        }
        //确认退单
        //登录人
        String loginId = (String) userMap.get("loginId");
        //退款状态 20:退款已付
        ordReturnExchange.setRefundStatus("20");
        //商品退款金额
        ordReturnExchange.setRefundInfact(ordReturnExchange.getRefundGoods());
        //退款确认人
        ordReturnExchange.setRefundMemberId(loginId);
        //修改人
        ordReturnExchange.setUpdateUserId(loginId);
        //更新退货单
        try {
            ordReturnExchangeMapperExt.submitReturnMoney(ordReturnExchange);
        } catch (ExceptionBusiness eb) {
            throw new ExceptionBusiness("更新失败");
        }
        //判断是否更新主表信息
        int count = ordReturnExchangeMapperExt.getReturnMoneyCount(ordReturnExchangeList.get(0).getOrderId());
        if (count == 0) {
            //更新主订单付款状态为 退款完成
            OrdMaster ordMaster = new OrdMaster();
            //退款完成 31
            ordMaster.setPayStatus("31");
            //订单ID
            ordMaster.setOrderId(ordReturnExchangeList.get(0).getOrderId());
            //更新人
            ordMaster.setUpdateUserId(loginId);
            try {
                ordMasterMapperExt.submitReturnMoney(ordMaster);
            } catch (ExceptionBusiness eb) {
                throw new ExceptionBusiness("更新失败");
            }
            //更新订单历史表
            ordMaster = ordMasterMapper.selectByPrimaryKey(ordReturnExchangeList.get(0).getOrderId());
            //插入订单历史记录表
            OrdHistory ordHistory = new OrdHistory();
            ordHistory = this.masterToHistory(ordMaster);
            ordHistory.setSeqOrderId(UUID.randomUUID().toString());
            ordHistory.setInsertUserId(loginId);
            //主订单操作历史信息插入
            int insertHistoryCount = ordHistoryMapperExt.insertSelective(ordHistory);
        }

    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public void cancancelOrderQuartz(String orderCode) {
        ordMasterMapperExt.cancancelOrderQuartz(orderCode);
        OrdMaster ordMaster = ordMasterMapperExt.selectByOrderCode(orderCode);
        //插入订单历史记录表
        OrdHistory ordHistory = new OrdHistory();
        ordHistory = this.masterToHistory(ordMaster);
        ordHistory.setSeqOrderId(UUID.randomUUID().toString());
        ordHistory.setInsertUserId("SYSTEM");
        //主订单操作历史信息插入
        ordHistoryMapperExt.insertSelective(ordHistory);
        //若使用优惠券,返还优惠券
        if (ordMaster.getCouponId() != null && !"".equals(ordMaster.getCouponId())) {
            CouponMemberExt couponMemberExt = new CouponMemberExt();
            couponMemberExt.setCouponMemberId(ordMaster.getCouponId());
            couponMemberService.returnCoupon(couponMemberExt);
        }

        // 获取订单关联的秒杀活动商品
        List<ActGoods> actGoodsList = actGoodsMapperExt.selectSecKillGoodsByOrderId(ordMaster.getOrderId());
        for(ActGoods actItem : actGoodsList){
            ActGoods actGoods = actGoodsMapper.selectByPrimaryKey(actItem.getActGoodsId());
            if(actGoods != null){
                actGoods.setSurplus(actGoods.getSurplus() + actItem.getQuantity());
                actGoodsMapper.updateByPrimaryKeySelective(actGoods);
            }
        }
    }

    @Override
    public void cancelReturnOrderQuartz(String orderCode) {
        //更新订单状态为最终状态 92:订单完成(该状态不再允许退货)
        ordMasterMapperExt.cancelReturnOrderQuartz(orderCode);
        OrdMaster ordMaster = ordMasterMapperExt.selectByOrderCode(orderCode);
        //插入订单历史记录表
        OrdHistory ordHistory = new OrdHistory();
        ordHistory = this.masterToHistory(ordMaster);
        ordHistory.setSeqOrderId(UUID.randomUUID().toString());
        ordHistory.setInsertUserId("SYSTEM");
        //主订单操作历史信息插入
        ordHistoryMapperExt.insertSelective(ordHistory);
    }

    @Override
    public JSONObject getReturnInfo(OrdReturnExchangeExt ordReturnExchange) {
        JSONObject json = new JSONObject();

        try {
            if (StringUtil.isEmpty(ordReturnExchange.getOrderId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            json.put("results", ordReturnExchangeMapperExt.viewApplyReturnGoods(ordReturnExchange));
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }

        return json;

    }


    /**
     * 订单物流信息查询
     *
     * @param orderId
     * @return
     */
    public JSONObject queryLogisticsInfo(String orderId) {

        JSONObject json = new JSONObject();
        try {

            List<OrdLogisticsForm> result = new ArrayList<OrdLogisticsForm>();

            OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(orderId);

            if (ordMaster == null || !"0".equals(ordMaster.getDeleted())) {
                throw new ExceptionErrorData("订单不存在");
            }

            OrdTraceForm first = new OrdTraceForm();
            first.setAcceptTime(DataUtils.formatTimeStampToYMDHM(ordMaster.getOrderTime()));
            first.setAcceptStation("您提交了订单,请等待系统确认");

            OrdTraceForm second = new OrdTraceForm();
            // 订单确认时间为6分钟
            Date afterDate = new Date(ordMaster.getOrderTime().getTime() + 6 * 60 * 1000);
            second.setAcceptTime(DataUtils.formatTimeStampToYMDHM(afterDate));
            second.setAcceptStation("您的订单准备出库");

            // 记录相同的物流快递单号
            List<String> expressNoList = new ArrayList<String>();

            Map<String, Integer> goodsMap = new HashMap<String, Integer>();

            // 记录未发货状态订单
            boolean hasUnDelivered = false;

            List<OrdTraceForm> traces;

            List<OrdSubListExt> subList = ordSubListMapperExt.selectOrderSubInfoOrderByExpressNo(orderId);
            if (subList != null && subList.size() > 0) {
                for (int i = 0; i < subList.size(); i++) {

                    OrdSubListExt item = subList.get(i);

                    // 10.电商发货，20.门店自提
                    if ("20".equals(item.getDeliverType())) {

                        OrdTraceForm order = new OrdTraceForm();
                        order.setAcceptTime(DataUtils.formatTimeStampToYMDHM(ordMaster.getOrderTime()));
                        order.setAcceptStation("您提交了订单,发货方式为门店自提.门店名称:"
                                + item.getStoreName()
                                + ",联系电话:"
                                + item.getStorePhone());

                        traces = new ArrayList<OrdTraceForm>();
                        traces.add(order);

                        OrdLogisticsForm logisticsForm = new OrdLogisticsForm();
                        logisticsForm.setTraces(traces);
                        logisticsForm.setOrdSubList(subList);

                        logisticsForm.setDeliverStatus(ordMaster.getDeliverStatus());

                        result.add(logisticsForm);

                        // 整个订单门店自提
                        break;

                    }

                    // 10.未发货，20.已发货，21.已收货，29.退货申请中，30.退货已发货，31.退货已收货，40.换货已发退货，41.换货已收退货，43.换货已发新货，44.换货已收新货
                    if (("10".equals(item.getDeliverStatus()) || StringUtil.isEmpty(item.getExpressNo()))) {

                        if (hasUnDelivered) {

                            Integer index = goodsMap.get("0000");

                            OrdLogisticsForm logisticsForm = result.get(index);

                            logisticsForm.getOrdSubList().add(item);

                            continue;
                        }

                        traces = new ArrayList<OrdTraceForm>();

                        if (null != ordMaster.getDeliverTime()) {
                            OrdTraceForm order = new OrdTraceForm();
                            order.setAcceptTime(DataUtils.formatTimeStampToYMDHM(ordMaster.getDeliverTime()));
                            order.setAcceptStation("您的订单已发货.");

                            traces.add(order);
                        }

                        traces.add(second);
                        traces.add(first);

                        OrdLogisticsForm logisticsForm = new OrdLogisticsForm();
                        logisticsForm.setTraces(traces);

                        List<OrdSubListExt> ordSubList = new ArrayList<OrdSubListExt>();
                        ordSubList.add(item);
                        logisticsForm.setOrdSubList(ordSubList);

                        logisticsForm.setDeliverStatus(item.getDeliverStatus());

                        result.add(logisticsForm);

                        // 使用0000标记所有未发货的子订单
                        goodsMap.put("0000", result.indexOf(logisticsForm));

                        hasUnDelivered = true;

                        // 未发货的子订单统一为已下单
                        continue;
                    }

                    String expressNo = item.getExpressNo();
                    if (expressNoList.contains(expressNo)) {

                        Integer index = goodsMap.get(expressNo);

                        OrdLogisticsForm logisticsForm = result.get(index);

                        logisticsForm.getOrdSubList().add(item);

                        // 相同快递单号的只显示一条数据
                        continue;
                    } else {
                        expressNoList.add(expressNo);

                        traces = new ArrayList<OrdTraceForm>();

                        String orderTraces = kdniaoTrackQueryAPI.getOrderTracesByJson(item.getExpressId(), expressNo);

                        JSONObject inJson = JSONObject.parseObject(orderTraces);
                        boolean successFlag = inJson.getBoolean("Success");
                        if (successFlag) {

                            JSONArray jsonArray = inJson.getJSONArray("Traces");
                            if (jsonArray != null && jsonArray.size() > 0) {
                                for (int k = jsonArray.size() - 1; k >= 0; k--) {
                                    JSONObject jo = (JSONObject) jsonArray.get(k);

                                    OrdTraceForm trace = new OrdTraceForm();
                                    trace.setAcceptTime(jo.getString("AcceptTime"));
                                    trace.setAcceptStation(jo.getString("AcceptStation"));
                                    trace.setRemark(jo.getString("Remark"));

                                    traces.add(trace);
                                }
                            }
                        }

                        if (null != item.getDeliverTime()) {
                            OrdTraceForm order = new OrdTraceForm();
                            order.setAcceptTime(DataUtils.formatTimeStampToYMDHM(item.getDeliverTime()));
                            order.setAcceptStation("您的订单已发货.快递公司"
                                    + item.getExpressName()
                                    + "承运,快递单号:"
                                    + expressNo);

                            traces.add(order);
                        }

                        traces.add(second);
                        traces.add(first);

                        OrdLogisticsForm logisticsForm = new OrdLogisticsForm();
                        logisticsForm.setTraces(traces);

                        List<OrdSubListExt> ordSubList = new ArrayList<OrdSubListExt>();
                        ordSubList.add(item);
                        logisticsForm.setOrdSubList(ordSubList);

                        logisticsForm.setDeliverStatus(item.getDeliverStatus());

                        result.add(logisticsForm);

                        // 使用快递单号标记所有未发货的子订单
                        goodsMap.put(expressNo, result.indexOf(logisticsForm));
                    }
                }
            } else {
                throw new ExceptionErrorData("没有查询到订单信息");
            }

            json.put("results", result);
            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }

        return json;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject applyRefund(String data) {
        JSONObject result = new JSONObject();
        try {
            OrdReturnExchange input = (OrdReturnExchange) JSON.parseObject(data, OrdReturnExchange.class);
            // 参数检查
            String orderId = input.getOrderId();
            if (StringUtil.isEmpty(orderId)) {
                throw new ExceptionErrorParam("未指定订单ID");
            }
            // 查询主订单
            OrdMaster master = ordMasterMapper.selectByPrimaryKey(orderId);
            if (master == null) {
                throw new ExceptionErrorParam("指定订单不存在");
            }
            // 检查订单支付状态是否为支付成功
            if (!master.getPayStatus().equals("20")) {
                throw new ExceptionErrorParam("订单付款未成功");
            }
            // 检查订单发货状态是否为未发货
            if (!master.getDeliverStatus().equals("10")) {
                throw new ExceptionErrorParam("订单已发货");
            }
            // 检查退款会员是否是订单会员
            if (!master.getMemberId().equals(input.getMemberId())) {
                throw new ExceptionErrorParam("不是该会员的订单");
            }

            //更新主订单状态为20.申请退单
            ordMasterMapperExt.applyRefund(orderId);

            //主订单操作历史信息插入
            OrdHistory history = new OrdHistory();
            master.setOrderStatus("20");
            history = this.masterToHistory(master);
            history.setSeqOrderId(UUID.randomUUID().toString());
            history.setInsertUserId(master.getMemberId());
            ordHistoryMapperExt.insertSelective(history);

            //插入退换货订单
            input.setRexOrderId(UUID.randomUUID().toString());
            input.setOrderCode(master.getOrderCode());
            input.setMemberId(master.getMemberId());
            //退换货类型,30:退单
            input.setRexType("30");
            //退换货方式,10:全单
            input.setRexMode("10");
            //退换货状态,10:申请中
            input.setRexStatus("10");
            input.setRexMemberId(input.getMemberId());
            input.setUpdateUserId(input.getMemberId());
            input.setInsertUserId(input.getMemberId());
            ordReturnExchangeMapperExt.insertSelective(input);

            //执行结束返回成功
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    public JSONObject getRefundApproveList(OrdMasterForm form) {
        JSONObject result = new JSONObject();
        try {
            // 参数检查
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setOrderId(form.getOrderId());
            ordMasterExt.setOrderCode(form.getOrderCode());
            //订单状态为20:申请退款
            ordMasterExt.setOrderStatus("20");
            ordMasterExt.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
            ordMasterExt.setStartPoint(Integer.parseInt(form.getiDisplayStart()));

            List<OrdMasterExt> list = ordMasterMapperExt.getAllDatas(ordMasterExt);
            int countAll = ordMasterMapperExt.getAllDatasCount(ordMasterExt);
            result.put("aaData", list);
            result.put("sEcho", form.getsEcho());
            result.put("iTotalRecords", countAll);
            result.put("iTotalDisplayRecords", countAll);
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    public JSONObject getRefundPage(OrdReturnExchangeExt form) {
        JSONObject result = new JSONObject();
        try {

            //取得数据
            //列表
            List<OrdReturnExchangeExt> list = ordReturnExchangeMapperExt.selectPageList(form);
            //总件数
            int countAll = ordReturnExchangeMapperExt.countPageList(form);

            //结果设置
            //列表
            result.put("aaData", list);
            //分页参数
            result.put("sEcho", form.getsEcho());
            result.put("iTotalRecords", countAll);
            result.put("iTotalDisplayRecords", countAll);
            //返回code
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    /**
     * 根据订单状态,发货状态,付款状态判断订单状态文字信息
     *
     * @param orderStatus
     * @param payStatus
     * @param deliverStatus
     * @return
     */
    private String orderStatusNameGet(String orderStatus, String payStatus, String deliverStatus) {
        String result = "";
        //订单未付款场合,传递待付款
        if ("10".equals(orderStatus) && "10".equals(deliverStatus) && "10".equals(payStatus)) {
            result = "待付款";
        } else if ("11".equals(orderStatus) && "10".equals(deliverStatus) && "10".equals(payStatus)) {
            result = "订单取消";
        } else if ("10".equals(orderStatus) && "10".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "待发货";
        } else if ("20".equals(orderStatus) && "10".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "申请退款中";
        } else if (("10".equals(orderStatus) || "23".equals(orderStatus)) && ("20".equals(deliverStatus) || "19".equals(deliverStatus)) && "20".equals(payStatus)) {
            result = "待收货";
        } else if ("21".equals(orderStatus) && "10".equals(deliverStatus) && "30".equals(payStatus)) {
            result = "退款中";
        } else if ("23".equals(orderStatus) && "10".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "待发货";
        } else if ("10".equals(orderStatus) && "21".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "已收货";
        } else if ("23".equals(orderStatus) && "21".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "已收货";
        } else if ("22".equals(orderStatus) && "10".equals(deliverStatus) && "31".equals(payStatus)) {
            result = "退款完成";
        } else if ("90".equals(orderStatus) && "21".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "订单完成";
        } else if ("30".equals(orderStatus) && "21".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "退货申请中";
        } else if ("31".equals(orderStatus) && "30".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "退货待收货";
        } else if ("33".equals(orderStatus) && "21".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "退货驳回";
        } else if ("31".equals(orderStatus) && "31".equals(deliverStatus) && "30".equals(payStatus)) {
            result = "退款中";
        } else if ("32".equals(orderStatus) && "31".equals(deliverStatus) && "31".equals(payStatus)) {
            result = "退货完成";
        } else {
            result = "未知状态(" + orderStatus + deliverStatus + payStatus + ")";
        }
        return result;
    }

    @Override
    public JSONObject getReturnInfoById(String id) {
        JSONObject json = new JSONObject();

        try {
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorParam("缺少参数");
            }
            OrdReturnExchangeExt record = ordReturnExchangeMapperExt.getById(id);
            json.put("data", record);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }

        return json;

    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject refundApprove(OrdReturnExchange input) {
        JSONObject result = new JSONObject();
        try {
            // 参数检查
            String rexOrderId = input.getRexOrderId();
            if (StringUtil.isEmpty(rexOrderId)) {
                throw new ExceptionErrorParam("未指定退款ID");
            }
            String orderId = input.getOrderId();
            if (StringUtil.isEmpty(orderId)) {
                throw new ExceptionErrorParam("未指定订单ID");
            }
            String rexStatus = input.getRexStatus();
            if (StringUtil.isEmpty(rexStatus)) {
                throw new ExceptionErrorParam("未指定审批状态");
            }
            //审批状态:11,驳回,40.通过
            if (!(rexStatus.equals("11") || rexStatus.equals("40"))) {
                throw new ExceptionErrorParam("审批状态不正确");
            }
            //取得退款记录
            OrdReturnExchange record = ordReturnExchangeMapper.selectByPrimaryKey(rexOrderId);
            if (record == null) {
                throw new ExceptionErrorParam("退款申请不存在");
            }
            // 退货类型是否为:30,退款
            if (!record.getRexType().equals("30")) {
                throw new ExceptionErrorParam("不是退款订单");
            }
            // 退货状态是否为:10,申请中
            if (!record.getRexStatus().equals("10")) {
                throw new ExceptionErrorParam("退款状态不正确");
            }
            input.setOrderId(null);
            // 查询主订单
            OrdMaster master = ordMasterMapper.selectByPrimaryKey(orderId);
            if (master == null) {
                throw new ExceptionErrorParam("订单不存在");
            }
            // 检查订单状态是否为20.申请退单
            if (!master.getOrderStatus().equals("20")) {
                throw new ExceptionErrorParam("订单状态不正确");
            }
            //审批状态:11,驳回,40.通过
            if (rexStatus.equals("40")) {
                // 检查订单支付状态是否为支付成功
                if (!master.getPayStatus().equals("20")) {
                    throw new ExceptionErrorParam("订单付款未成功");
                }
                // 检查订单发货状态是否为未发货
                if (!master.getDeliverStatus().equals("10")) {
                    throw new ExceptionErrorParam("订单已发货");
                }
            }

            OrdMaster ordMaster = new OrdMaster();
            ordMaster.setOrderId(orderId);
            ordMaster.setUpdateUserId(input.getUpdateUserId());
            //审批状态:11,驳回,40.通过
            if (rexStatus.equals("11")) {
                //订单状态为23退单驳回
                ordMaster.setOrderStatus("23");
                master.setOrderStatus("23");
            } else {
                //订单状态为21：退单中
                ordMaster.setOrderStatus("21");
                master.setOrderStatus("21");
                //付款状态为30：退款中
                ordMaster.setPayStatus("30");
                master.setPayStatus("30");
                //退款状态为10:等待付款
                input.setRefundStatus("10");
            }
            //更新主订单状态
            ordMasterMapper.updateByPrimaryKeySelective(master);

            //主订单操作历史信息插入
            OrdHistory history = new OrdHistory();
            history = this.masterToHistory(master);
            history.setSeqOrderId(UUID.randomUUID().toString());
            history.setInsertUserId(input.getUpdateUserId());
            ordHistoryMapperExt.insertSelective(history);

            //更新退换货订单
            ordReturnExchangeMapperExt.updateById(input);

            //执行结束返回成功
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject refundPay(OrdReturnExchange input) {
        JSONObject result = new JSONObject();
        try {
            // 参数检查
            String rexOrderId = input.getRexOrderId();
            if (StringUtil.isEmpty(rexOrderId)) {
                throw new ExceptionErrorParam("未指定退款ID");
            }
            String orderId = input.getOrderId();
            if (StringUtil.isEmpty(orderId)) {
                throw new ExceptionErrorParam("未指定订单ID");
            }
            input.setOrderId(null);
            //退款金额
            BigDecimal refundInfact = input.getRefundInfact();
            if (refundInfact == null || refundInfact.equals(0)) {
                throw new ExceptionErrorParam("未指定退款金额");
            }
            //取得退款记录
            OrdReturnExchange record = ordReturnExchangeMapper.selectByPrimaryKey(rexOrderId);
            if (record == null) {
                throw new ExceptionErrorParam("退款申请不存在");
            }
            // 退货类型是否为:30,退款
            if (!record.getRexType().equals("30")) {
                throw new ExceptionErrorParam("不是退款订单");
            }
            // 退货状态是否为:40.通过
            if (!record.getRexStatus().equals("40")) {
                throw new ExceptionErrorParam("退款状态不正确");
            }
            // 付款状态是否为:10:等待付款
            if (!record.getRefundStatus().equals("10")) {
                throw new ExceptionErrorParam("付款状态不正确");
            }

            // 查询主订单
            OrdMaster master = ordMasterMapper.selectByPrimaryKey(orderId);
            if (master == null) {
                throw new ExceptionErrorParam("订单不存在");
            }
            //TODO 退款金额检查:暂不做
            // 检查订单状态是否为21：退单中
            if (!master.getOrderStatus().equals("21")) {
                throw new ExceptionErrorParam("订单状态不正确");
            }
            // 检查订单支付状态是否为30：退款中
            if (!master.getPayStatus().equals("30")) {
                throw new ExceptionErrorParam("订单付款状态不正确");
            }
            if (!master.getDeliverStatus().equals("10")) {
                throw new ExceptionErrorParam("订单已发货");
            }

            OrdMaster ordMaster = new OrdMaster();
            ordMaster.setOrderId(orderId);
            ordMaster.setUpdateUserId(input.getUpdateUserId());
            //订单状态为22：退单完成
            ordMaster.setOrderStatus("22");
            master.setOrderStatus("22");
            //付款状态为31：退款完成
            ordMaster.setPayStatus("31");
            master.setPayStatus("31");

            //更新主订单状态
            ordMasterMapper.updateByPrimaryKeySelective(master);

            //主订单操作历史信息插入
            OrdHistory history = new OrdHistory();
            history = this.masterToHistory(master);
            history.setSeqOrderId(UUID.randomUUID().toString());
            history.setInsertUserId(input.getUpdateUserId());
            ordHistoryMapperExt.insertSelective(history);

            //更新退换货订单
            //付款状态为20:退款已付
            input.setRefundStatus("20");
            //退款状态:41.已退款
            input.setRexStatus("41");
            ordReturnExchangeMapperExt.updateById(input);

            //退积分
            //积分履历清除购买积分
            MmbPointRecord point = new MmbPointRecord();
            point.setScoreSource(orderId);
            point.setUpdateUserId(input.getUpdateUserId());
            mmbPointRecordMapperExt.clearByOrderId(point);

            //TODO 退代金券暂不做

            //执行结束返回成功
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    /**
     * 退货待收货列表
     *
     * @param form
     * @return
     */
    @Override
    public JSONObject getReturnGoodsReceiveList(OrdReturnExchangeExt form) {
        JSONObject result = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getRexStoreId())) {
                //列表
                List<OrdMasterExt> list = new ArrayList<OrdMasterExt>();
                //总件数
                int countAll = 0;
                //结果设置
                //列表
                result.put("aaData", list);
                //分页参数
                result.put("sEcho", form.getsEcho());
                result.put("iTotalRecords", countAll);
                result.put("iTotalDisplayRecords", countAll);
            } else {
                //列表
                List<OrdMasterExt> list = ordReturnExchangeMapperExt.selectReceiveGoodsList(form);
                //总件数
                int countAll = ordReturnExchangeMapperExt.selectReceiveGoodsListCount(form);

                //为前台准备数据
                for (OrdMasterExt ome : list) {
                    //传递前台显示订单状态
                    String orderStatusName = orderStatusNameGet(ome.getOrderStatus(), ome.getPayStatus(), ome.getDeliverStatus());
                    ome.setOrderStatusName(orderStatusName);
                }

                //结果设置
                //列表
                result.put("aaData", list);
                //分页参数
                result.put("sEcho", form.getsEcho());
                result.put("iTotalRecords", countAll);
                result.put("iTotalDisplayRecords", countAll);

            }
            //返回code
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 退货审批,子订单
     *
     * @param ordReturnExchange
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject returnGoodsApprove(OrdReturnExchangeExt ordReturnExchange) {
        JSONObject result = new JSONObject();
        //更新审批结果
        try {
            // 检验参数
            if (StringUtil.isEmpty(ordReturnExchange.getRexOrderId())) {
                throw new ExceptionErrorParam("缺少参数");
            }

            if (StringUtil.isEmpty(ordReturnExchange.getRexStatus())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 查询退货数据
            OrdReturnExchange oe = ordReturnExchangeMapper.selectByPrimaryKey(ordReturnExchange.getRexOrderId());
            if (oe == null) {
                throw new ExceptionErrorData("退货申请不存在或已经撤回");
            }
            // 10 为申请中
            if (!"10".equals(oe.getRexStatus())) {
                throw new ExceptionErrorData("申请状态已经改变");
            }
            // 更新审批状态
            oe.setRexStatus(ordReturnExchange.getRexStatus());
            oe.setApplyAnswerComment(ordReturnExchange.getApplyAnswerComment());
            oe.setApplyAnswerUserId(ordReturnExchange.getUpdateUserId());
            oe.setApplyAnswerTime(new Date());
            oe.setUpdateUserId(ordReturnExchange.getUpdateUserId());
            oe.setUpdateTime(new Date());
            ordReturnExchangeMapper.updateByPrimaryKeySelective(oe);


            if ("20".equals(ordReturnExchange.getRexStatus())) {
                //审批通过场合
                //更新子订单发货状态
                OrdSubList ordSubList = new OrdSubList();
                ordSubList.setSubOrderId(oe.getSubOrderId());
                ordSubList.setUpdateUserId(ordReturnExchange.getUpdateUserId());
                // 30：退货待收货;
                ordSubList.setDeliverStatus("30");
                ordSubListMapper.updateByPrimaryKeySelective(ordSubList);
            } else if ("11".equals(ordReturnExchange.getRexStatus())) {
                //审批驳回场合
            } else {
                throw new ExceptionErrorParam("审批状态参数错误");
            }

            // 判断所属订单的子订单状态,判断是否还有没有审批的
            int applyingCount = ordReturnExchangeMapperExt.getReturnExchangeApplyingCountByOrdId(oe.getOrderId());
            if (applyingCount == 0) {
                //更新订单历史表
                OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(oe.getOrderId());
                // 都审批过了,根据审批状态设置订单状态,规则为,
                // 只要有审批通过的,订单主表中,订单状态变为:31退货中,发货状态变为30：退货待收货;
                // 如果全部为驳回的,订单主表中,订单状态变为33：退货驳回,发货状态不变
                int approvedCount = ordReturnExchangeMapperExt.getReturnExchangeApprovedCountByOrdId(oe.getOrderId());
                if (approvedCount > 0) {
                    ordMaster.setOrderStatus("31");
                    ordMaster.setDeliverStatus("30");
                } else {
                    ordMaster.setOrderStatus("33");
                }
                ordMaster.setUpdateTime(new Date());
                ordMaster.setUpdateUserId(ordReturnExchange.getUpdateUserId());
                ordMasterMapper.updateByPrimaryKeySelective(ordMaster);
                //插入订单历史记录表
                OrdHistory ordHistory = new OrdHistory();
                ordHistory = this.masterToHistory(ordMaster);
                ordHistory.setSeqOrderId(UUID.randomUUID().toString());
                ordHistory.setInsertUserId(ordReturnExchange.getUpdateUserId());
                //主订单操作历史信息插入
                ordHistoryMapperExt.insertSelective(ordHistory);
            }
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    /**
     * 退货已收货确认
     *
     * @param ordReturnExchange
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject returnGoodsReceive(OrdReturnExchangeExt ordReturnExchange) {
        JSONObject result = new JSONObject();
        //更新审批结果
        try {
            // 检验参数
            if (StringUtil.isEmpty(ordReturnExchange.getRexOrderId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 查询退货数据
            OrdReturnExchange oe = ordReturnExchangeMapper.selectByPrimaryKey(ordReturnExchange.getRexOrderId());
            if (oe == null) {
                throw new ExceptionErrorData("退货申请不存在或已经撤回");
            }
            // 20 为退货待收货
            if (!"20".equals(oe.getRexStatus())) {
                throw new ExceptionErrorData("退货状态已经改变");
            }
            // 更新审批状态 代付款
            oe.setRexStatus("40");
            // 10:等待退款
            oe.setRefundStatus("10");
            oe.setReturnAcceptTime(new Date());
            oe.setReturnAcceptUserId(ordReturnExchange.getUpdateUserId());
            oe.setUpdateUserId(ordReturnExchange.getUpdateUserId());
            oe.setUpdateTime(new Date());
            ordReturnExchangeMapper.updateByPrimaryKeySelective(oe);

            //更新子订单发货状态
            OrdSubList ordSubList = new OrdSubList();
            ordSubList.setSubOrderId(oe.getSubOrderId());
            ordSubList.setUpdateUserId(ordReturnExchange.getUpdateUserId());
            // 31：退货已收货;
            ordSubList.setDeliverStatus("31");
            ordSubListMapper.updateByPrimaryKeySelective(ordSubList);

            // 判断所属订单的子订单状态,如果审批通过的退货,全部为"退货已收货",则将订单发货状态改为退货已收货
            // 查询还没有收到退货的个数
            int unReceivedCount = ordReturnExchangeMapperExt.getReturnExchangeApprovedCountByOrdId(oe.getOrderId());
            if (unReceivedCount == 0) {
                //更新订单表
                OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(oe.getOrderId());
                // 都收到货了,根据设置订单发货状态为31：退货已收货
                // 设置订单付款状态为:30：退款中
                ordMaster.setDeliverStatus("31");
                ordMaster.setPayStatus("30");
                ordMaster.setUpdateTime(new Date());
                ordMaster.setUpdateUserId(ordReturnExchange.getUpdateUserId());
                ordMasterMapper.updateByPrimaryKeySelective(ordMaster);
                //插入订单历史记录表
                OrdHistory ordHistory = new OrdHistory();
                ordHistory = this.masterToHistory(ordMaster);
                ordHistory.setSeqOrderId(UUID.randomUUID().toString());
                ordHistory.setInsertUserId(ordReturnExchange.getUpdateUserId());
                //主订单操作历史信息插入
                ordHistoryMapperExt.insertSelective(ordHistory);

            }

            ordSubList = ordSubListMapper.selectByPrimaryKey(oe.getSubOrderId());
            //库存增加
            BnkMaster bnkMaster = new BnkMaster();

            if ("10".equals(ordSubList.getType())) {
                bnkMaster.setGoodsId(ordSubList.getGoodsCode());
            } else {
                bnkMaster.setGoodsId(ordSubList.getGoodsId());
            }

            if (!StringUtil.isEmpty(oe.getRexStoreId())) {
                bnkMaster.setErpStoreId(oe.getRexStoreId());
            } else {
                if (!StringUtil.isEmpty(ordSubList.getErpStoreId())) {
                    bnkMaster.setErpStoreId(ordSubList.getErpStoreId());
                }
            }
            bnkMaster.setSku(ordSubList.getSkuId());

            //获取库存信息
            BnkMasterExt bnkMasterExt = bnkMasterMapperExt.selectByGoodSkuId(bnkMaster);
            if (bnkMasterExt == null) {
                bnkMasterExt = new BnkMasterExt();
                //退货的时候有可能存在库存主表没有数据的时候,如果没有要插入
                bnkMasterExt.setErpStoreId(bnkMaster.getErpStoreId());
                bnkMasterExt.setShopId(Constants.ORGID);
                bnkMasterExt.setGoodsId(bnkMaster.getGoodsId());
                bnkMasterExt.setErpGoodsCode(bnkMaster.getGoodsId());
                bnkMasterExt.setGoodsCode(bnkMaster.getGoodsId());
                bnkMasterExt.setSku(bnkMaster.getSku());
                bnkMasterExt.setErpSku(bnkMaster.getSku());
                bnkMasterExt.setNewCount(1);
                bnkMasterExt.setLastCount(0);
                bnkMasterExt.setDeleted("0");
                bnkMasterExt.setUpdateUserId(ordReturnExchange.getUpdateUserId());
                bnkMasterExt.setInsertUserId(ordReturnExchange.getUpdateUserId());
                Date date = new Date();
                bnkMasterExt.setInsertTime(date);
                bnkMasterExt.setUpdateTime(date);
                bnkMasterMapper.insertSelective(bnkMasterExt);

            } else {
                //变更原库存数
                bnkMasterExt.setLastCount(bnkMasterExt.getNewCount());
                //变更最新库存数
                bnkMasterExt.setNewCount(bnkMasterExt.getNewCount() + ordSubList.getQuantity());
                //更新人
                bnkMasterExt.setUpdateUserId(ordReturnExchange.getUpdateUserId());
                //更新库存主表
                bnkMasterMapperExt.cancelOrderAddBank(bnkMasterExt);
            }
            //插入出入库履历表
            BnkRecords bnkRecords = new BnkRecords();
            //数据准备
            bnkRecords.setShopId(bnkMasterExt.getShopId());
            bnkRecords.setGoodsType(bnkMasterExt.getGoodsType());
            bnkRecords.setTypeType(bnkMasterExt.getTypeType());
            bnkRecords.setGoodsId(bnkMasterExt.getGoodsId());
            bnkRecords.setGoodsCode(bnkMasterExt.getGoodsCode());
            bnkRecords.setSku(bnkMasterExt.getSku());
            if(!StringUtil.isEmpty(bnkMaster.getErpStoreId())){
                bnkRecords.setErpStoreId(bnkMaster.getErpStoreId());
            }
            bnkRecords.setBankType(bnkMasterExt.getBankType());
            bnkRecords.setInoutCount(ordSubList.getQuantity());
            bnkRecords.setComment(bnkMasterExt.getComment());
            bnkRecords.setDeleted(bnkMasterExt.getDeleted());
            bnkRecords.setUpdateUserId(ordReturnExchange.getUpdateUserId());
            bnkRecords.setInsertUserId(ordReturnExchange.getUpdateUserId());
            bnkRecordsMapper.insertSelective(bnkRecords);

            //发送ERP
//            ErpSendUtil.ReturnInputById(oe.getOrderId(),
//                    ordSubListMapperExt, ordReturnExchangeMapperExt, ordMasterMapperExt);

            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 退货已退款确认
     *
     * @param ordReturnExchange
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject returnGoodsRefund(OrdReturnExchangeExt ordReturnExchange) {
        JSONObject result = new JSONObject();
        //更新审批结果
        try {
            // 检验参数
            if (StringUtil.isEmpty(ordReturnExchange.getRexOrderId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (ordReturnExchange.getRefundInfact() == null) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 查询退货数据
            OrdReturnExchange oe = ordReturnExchangeMapper.selectByPrimaryKey(ordReturnExchange.getRexOrderId());
            if (oe == null) {
                throw new ExceptionErrorData("退货申请不存在或已经撤回");
            }
            // 40 为待付款
            if (!"40".equals(oe.getRexStatus())) {
                throw new ExceptionErrorData("退货状态已经改变");
            }
            // 更新审批状态 已退款
            oe.setRexStatus("41");
            // 20:退款已付
            oe.setRefundStatus("20");
            oe.setRefundInfact(ordReturnExchange.getRefundInfact());
            oe.setUpdateUserId(ordReturnExchange.getUpdateUserId());
            oe.setUpdateTime(new Date());
            ordReturnExchangeMapper.updateByPrimaryKeySelective(oe);

            // 判断所属订单的子订单状态,如果审批通过的退货,全部为"已退款",则将订单状态改为退货完成付款状态改为已退款,
            // 查询还没有收到退货的个数
            int unRefundCount = ordReturnExchangeMapperExt.getReturnExchangeUnRefundCountByOrdId(oe.getOrderId());
            if (unRefundCount == 0) {
                //更新订单表
                OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(oe.getOrderId());
                // 都收到货了,根据设置订单状态为32：退货完成
                // 设置订单付款状态为:31：退款完成
                ordMaster.setOrderStatus("32");
                ordMaster.setPayStatus("31");
                ordMaster.setUpdateTime(new Date());
                ordMaster.setUpdateUserId(ordReturnExchange.getUpdateUserId());
                ordMasterMapper.updateByPrimaryKeySelective(ordMaster);
                //插入订单历史记录表
                OrdHistory ordHistory = new OrdHistory();
                ordHistory = this.masterToHistory(ordMaster);
                ordHistory.setSeqOrderId(UUID.randomUUID().toString());
                ordHistory.setInsertUserId(ordReturnExchange.getUpdateUserId());
                //主订单操作历史信息插入
                ordHistoryMapperExt.insertSelective(ordHistory);

                //发送ERP
//                sendReturnOrder(ordMaster.getOrderId());
//                ErpSendUtil.getInstance().ReturnInputById(ordMaster.getOrderId());
                ErpSendUtil.ReturnInputById(ordMaster.getOrderId(),
                        ordSubListMapperExt, ordReturnExchangeMapperExt, ordMasterMapperExt);
            }
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 确认电话号码是否存在
     *
     * @param tel
     * @return
     */
    @Override
    public JSONObject checkInputTel(String tel) {
        JSONObject result = new JSONObject();
        try {
            // 检验参数
            if (StringUtil.isEmpty(tel)) {
                throw new ExceptionErrorParam("缺少参数");
            }
            MmbMasterExt ext = new MmbMasterExt();
            ext.setTelephone(tel);
            List<MmbMasterExt> list = mmbMasterMapperExt.selectAllByTel(ext);
            result.put("results", list);
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @Override
    public List<OrdHistoryExt> getOrderHistoryList(String orderId) {
        List<OrdHistoryExt> list = ordHistoryMapperExt.selectByOrderId(orderId);
        for (OrdHistoryExt ome : list) {
            //传递前台显示订单状态
            String orderStatusName = orderStatusNameGet(ome.getOrderStatus(), ome.getPayStatus(), ome.getDeliverStatus());
            ome.setOrderStatusName(orderStatusName);
        }
        return list;
    }

    @Override
    public JSONObject getSelfOrderList(OrdMasterForm form, SysUser sysUser) {

        JSONObject result = new JSONObject();
        try {

            MmbMasterExt ext = new MmbMasterExt();
            ext.setTelephone(sysUser.getLoginId());
            List<MmbMasterExt> list = null;
            if (!StringUtil.isTelephone(ext.getTelephone())) {
                MmbSalerExt mmbSalerExt = new MmbSalerExt();
                BeanUtils.copyProperties(ext,mmbSalerExt);
                List<MmbSalerExt> salerlist = mmbSalerMapperExt.selectAllByTel(mmbSalerExt);
                list = new ArrayList<MmbMasterExt>();
                for(MmbSalerExt item : salerlist){
                    MmbMasterExt mmbMasterExt = new MmbMasterExt();
                    BeanUtils.copyProperties(item,mmbMasterExt);
                    list.add(mmbMasterExt);
                }
            }else{
                list = mmbMasterMapperExt.selectAllByTel(ext);
            }
            //List<MmbMasterExt> list = mmbMasterMapperExt.selectAllByTel(ext);
            //会员信息获取
            if (list == null || list.size() == 0) {
                throw new ExceptionBusiness("会员信息获取失败!");
            }
            String memberId = list.get(0).getMemberId();

            //获取查询条件数据
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setShopId(Constants.ORGID);
//            ordMasterExt.setErpStoreId(sysUser.getOrgId());
            ordMasterExt.setOrderId(form.getOrderId());
            ordMasterExt.setOrderCode(form.getOrderCode());
            ordMasterExt.setOrderStatus(form.getOrderStatus());
            ordMasterExt.setPayStatus(form.getPayStatus());
            ordMasterExt.setDeliverStatus(form.getDeliverStatus());
            ordMasterExt.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
            ordMasterExt.setStartPoint(Integer.parseInt(form.getiDisplayStart()));
            ordMasterExt.setInsertUserId(memberId);

            //获取订单数据
            List<OrdMasterExt> ordList = getAllDatas(ordMasterExt);
            //获取订单条数
            int ordCounts = getAllDatasCount(ordMasterExt);
            result.put("aaData", ordList);
            result.put("sEcho", form.getsEcho());
            result.put("iTotalRecords", ordCounts);
            result.put("iTotalDisplayRecords", ordCounts);


            result.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;

    }

    @Override
    public JSONObject editSubOrderStore(OrdReturnExchange form) {
        JSONObject result = new JSONObject();
        try {

            OrdReturnExchange ordReturnExchange = ordReturnExchangeMapper.selectByPrimaryKey(form.getRexOrderId());
            if (null == ordReturnExchange || !"0".equals(ordReturnExchange.getDeleted())) {
                throw new ExceptionErrorData("退货单信息不存在");
            }

            if (!"10".equals(ordReturnExchange.getRexStatus())) {
                throw new ExceptionErrorData("退货单信息已更新");
            }

            OrdSubList ordSub = ordSubListMapper.selectByPrimaryKey(ordReturnExchange.getSubOrderId());
            if (null == ordSub || !"0".equals(ordSub.getDeleted())) {
                throw new ExceptionErrorData("订单信息不存在");
            }

            ErpStore erpStore = erpStoreMapper.selectByPrimaryKey(form.getRexStoreId());
            if (null == erpStore || !"0".equals(erpStore.getDeleted())) {
                throw new ExceptionErrorData("门店信息不存在");
            }

//            ordSub.setErpStoreId(erpStore.getStoreCode());
//            ordSub.setStoreName(erpStore.getStoreNameCn());
//            ordSub.setStorePhone(erpStore.getPhone());
            ordReturnExchange.setRexStoreId(erpStore.getStoreCode());
            ordReturnExchange.setRexStoreName(erpStore.getStoreNameCn());
            int count = ordReturnExchangeMapper.updateByPrimaryKeySelective(ordReturnExchange);
            //int count = ordSubListMapper.updateByPrimaryKeySelective(ordSub);
            if (1 == count) {
                result.put("resultCode", Constants.NORMAL);
            } else {
                result.put("resultCode", Constants.FAIL);
                result.put("message", "提交失败");
            }

        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @Override
    public JSONObject getOrderCountByMemberId(OrdMasterExt ordMasterExt) {
        JSONObject result = new JSONObject();
        try {
            // 检验参数
            if (StringUtil.isEmpty(ordMasterExt.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            int allCount = ordMasterMapperExt.getAllCountByMemberId(ordMasterExt.getMemberId());
            int waitPayCount = ordMasterMapperExt.getWaitPayCountByMemberId(ordMasterExt.getMemberId());
            int waitDeliveryCount = ordMasterMapperExt.getWaitDeliveryCountByMemberId(ordMasterExt.getMemberId());
            int waitReceiveCount = ordMasterMapperExt.getWaitReceiveCountByMemberId(ordMasterExt.getMemberId());
            int completedCount = ordMasterMapperExt.getCompletedCountByMemberId(ordMasterExt.getMemberId());
            OrdMasterExt counts = new OrdMasterExt();
            counts.setAllCount(allCount);
            counts.setWaitPayCount(waitPayCount);
            counts.setWaitDeliveryCount(waitDeliveryCount);
            counts.setWaitReceiveCount(waitReceiveCount);
            counts.setCompletedCount(completedCount);
            result.put("results", counts);
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @Override
    public JSONObject getOrderListByMemberId(OrdMasterExt ordMasterExt) {
        JSONObject result = new JSONObject();
        try {
            // 检验参数
            if (StringUtil.isEmpty(ordMasterExt.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (ordMasterExt.getNeedColumns() == 0) {
                ordMasterExt.setNeedColumns(10);
            }
            int allCount = ordMasterMapperExt.getAllCountByMemberId(ordMasterExt.getMemberId());
            int waitPayCount = ordMasterMapperExt.getWaitPayCountByMemberId(ordMasterExt.getMemberId());
            int waitDeliveryCount = ordMasterMapperExt.getWaitDeliveryCountByMemberId(ordMasterExt.getMemberId());
            int waitReceiveCount = ordMasterMapperExt.getWaitReceiveCountByMemberId(ordMasterExt.getMemberId());
            int completedCount = ordMasterMapperExt.getCompletedCountByMemberId(ordMasterExt.getMemberId());

            List<OrdMasterExt> list = null;
            int totalPage = 0;
            if ("1".equals(ordMasterExt.getType()) || StringUtil.isEmpty(ordMasterExt.getType())) {
                list = ordMasterMapperExt.getAllOrderByMemberId(ordMasterExt);
                if (allCount % ordMasterExt.getNeedColumns() == 0) {
                    totalPage = allCount / ordMasterExt.getNeedColumns();
                } else {
                    totalPage = allCount / ordMasterExt.getNeedColumns() + 1;
                }

            } else if ("2".equals(ordMasterExt.getType())) {
                list = ordMasterMapperExt.getWaitPayOrderByMemberId(ordMasterExt);
                if (waitPayCount % ordMasterExt.getNeedColumns() == 0) {
                    totalPage = waitPayCount / ordMasterExt.getNeedColumns();
                } else {
                    totalPage = waitPayCount / ordMasterExt.getNeedColumns() + 1;
                }
            } else if ("3".equals(ordMasterExt.getType())) {
                list = ordMasterMapperExt.getWaitDeliveryOrderByMemberId(ordMasterExt);
                if (waitDeliveryCount % ordMasterExt.getNeedColumns() == 0) {
                    totalPage = waitDeliveryCount / ordMasterExt.getNeedColumns();
                } else {
                    totalPage = waitDeliveryCount / ordMasterExt.getNeedColumns() + 1;
                }
            } else if ("4".equals(ordMasterExt.getType())) {
                list = ordMasterMapperExt.getWaitReceiveOrderByMemberId(ordMasterExt);
                if (waitReceiveCount % ordMasterExt.getNeedColumns() == 0) {
                    totalPage = waitReceiveCount / ordMasterExt.getNeedColumns();
                } else {
                    totalPage = waitReceiveCount / ordMasterExt.getNeedColumns() + 1;
                }
            } else if ("5".equals(ordMasterExt.getType())) {
                list = ordMasterMapperExt.getCompletedOrderByMemberId(ordMasterExt);
                if (completedCount % ordMasterExt.getNeedColumns() == 0) {
                    totalPage = completedCount / ordMasterExt.getNeedColumns();
                } else {
                    totalPage = completedCount / ordMasterExt.getNeedColumns() + 1;
                }
            }
            if (list.size() > 0) {
                //获取主订单订单id
                String ord_ids = "";
                for (int i = 0; i < list.size() - 1; i++) {
                    ord_ids += "'" + list.get(i).getOrderId() + "',";
                }
                ord_ids += "'" + list.get(list.size() - 1).getOrderId() + "'";
                //根据订单id获取商品明细信息
                List<OrdList> ordLists = ordListMapperExt.selectOrdListByOrderId(ord_ids);
                //遍历子订单数组,整合数据
                for (int j = 0; j < list.size(); j++) {
                    String order_id = list.get(j).getOrderId();
                    List<OrdList> actOrdList = new ArrayList<OrdList>();
                    for (int m = 0; m < ordLists.size(); m++) {
                        if (order_id.equals(ordLists.get(m).getOrderId())) {
                            OrdList ordList = new OrdList();
                            ordList = ordLists.get(m);
                            actOrdList.add(ordList);
                        }
                    }
                    list.get(j).setOrdList(actOrdList);
                    list.get(j).setOrderStatusName(orderStatusNameGet(list.get(j).getOrderStatus(), list.get(j).getPayStatus(), list.get(j).getDeliverStatus()));
                    // 获取优惠券信息
                    if (!StringUtil.isEmpty(list.get(j).getCouponId())) {
                        // 获取活动信息
                        CouponMember couponMember = couponMemberMapper.selectByPrimaryKey(list.get(j).getCouponId());
                        if (couponMember != null) {
                            CouponMaster couponMaster = couponMasterMapper.selectByPrimaryKey(couponMember.getCouponId());
                            list.get(j).setCoupon(couponMaster);
                        }
                    }
                }
            }
            result.put("totalPage", totalPage);
            result.put("allCount", allCount);
            result.put("waitPayCount", waitPayCount);
            result.put("waitDeliveryCount", waitDeliveryCount);
            result.put("waitReceiveCount", waitReceiveCount);
            result.put("completedCount", completedCount);
            result.put("results", list);
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @Override
    public JSONObject getOrderListOffLineByMemberId(OrdMasterExt ordMasterExt) {
        JSONObject result = new JSONObject();
        try {
            // 检验参数
            if (StringUtil.isEmpty(ordMasterExt.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (ordMasterExt.getNeedColumns() == 0) {
                ordMasterExt.setNeedColumns(10);
            }
            int allCount = erpOrdMasterMapperExt.getAllCountByMemberId(ordMasterExt.getMemberId());

            List<ErpOrderMasterExt> list = null;
            int totalPage = 0;
            list = erpOrdMasterMapperExt.getAllOrderByMemberId(ordMasterExt);
            if (allCount % ordMasterExt.getNeedColumns() == 0) {
                totalPage = allCount / ordMasterExt.getNeedColumns();
            } else {
                totalPage = allCount / ordMasterExt.getNeedColumns() + 1;
            }

            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    List<ErpOrderSub> sublist = erpOrderSubMapperExt.selectByKey(list.get(i).getOrderCode());
                    list.get(i).setSubList(sublist);
                }
            }

            result.put("totalPage", totalPage);
            result.put("allCount", allCount);
            result.put("results", list);
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;
    }


    @Override
    public JSONObject getOrderListOffLineByMemberIdForWeb(String memberId) {
        JSONObject result = new JSONObject();
        try {
            // 检验参数
            if (StringUtil.isEmpty(memberId)) {
                throw new ExceptionErrorParam("缺少参数");
            }

            List<ErpOrderMasterExt> list = null;
            list = erpOrdMasterMapperExt.getAllOrderByMemberIdForWeb(memberId);
            result.put("results", list);
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;
    }

//    private void sendReturnOrder(String orderId) throws Exception {
//        OrdReturnExchange ordReturn = new OrdReturnExchange();
//        ordReturn.setErpSendStatus("10");
//        ordReturn.setOrderId(orderId);
//        try {
//            //取得主订单
//            OrdMasterExt master = ordMasterMapperExt.selectSendReturnById(orderId);
//            if (master == null) {
//                throw new ExceptionErrorData("指定订单不存在");
//            }
//            //子订单
//            List<OrdSubList> subList = ordSubListMapperExt.selectErpReturnByOrder(orderId);
//            //没有子订单数据
//            if (subList == null || subList.size() == 0) throw new ExceptionErrorData("没有退货子订单数据");
//
//            BigDecimal amountTotle = new BigDecimal(0);
//            BigDecimal amountDiscount = new BigDecimal(0);
//            List<SaleList> saleList = new ArrayList<>();
//            for (OrdSubList sub : subList) {
//                SaleList sale = new SaleList();
//                sale.setOrderId(sub.getOrderId());
//                sale.setSubOrderId(sub.getSubOrderId());
//                sale.setErpSku(sub.getSkuId());
//                sale.setPrice(String.valueOf(sub.getPrice()));
//                BigDecimal priceDiscount = sub.getPriceDiscount();
//                priceDiscount = priceDiscount == null || priceDiscount.equals(0) ? sub.getPrice() : priceDiscount;
//                sale.setPriceDiscount(String.valueOf(priceDiscount));
//                sale.setDeliverTime(formatTimeStampToYMDHMS(sub.getDeliverTime()));
//                sale.setExpressName(changStringNull(sub.getExpressName()));
//                sale.setExpressNo(changStringNull(sub.getExpressNo()));
//                sale.setErpStoreId(sub.getErpStoreId());
//                sale.setStoreDeliveryName(sub.getStoreDeliveryId());
//                saleList.add(sale);
//                amountTotle = amountTotle.add(sub.getPrice());
//                amountDiscount = amountDiscount.add(priceDiscount);
//            }
//
//            //订单主表
//            SaleMaster m = new SaleMaster();
//            m.setOrderId(master.getOrderId());
//            m.setMemberId(master.getTelephone());
//            m.setMemberName(master.getMemberName());
//            m.setAmountTotle(String.valueOf(amountTotle));
//            m.setAmountDiscount(String.valueOf(amountDiscount));
////            m.setPayTime(sdf.format(master.getPayTime()));
//            m.setPayTime(formatTimeStampToYMDHMS(subList.get(subList.size() - 1).getUpdateTime()));
//            m.setQuantity(String.valueOf(saleList.size()));
//            m.setMessage(master.getMessage());
//            m.setDeliveryFree(master.getDeliveryFree());
//            m.setDeliceryFee(String.valueOf(master.getDeliveryFee()));
//            m.setDeliveryType(master.getDeliverType());
//            m.setPayType(master.getPayType());
//            m.setPayDeliveryType(master.getPayDeliveryType());
//            m.setSalerID(master.getInsertUserId());
//
//            Orders order = new Orders();
//            ArrayOfSaleList ll = new ArrayOfSaleList();
//            ll.setSaleList(saleList);
//            order.setSaleMaster(m);
//            order.setSaleList(ll);
//
//            String result = soap.returnInput(getKeyOrderInput(order), order);
//            log.debug("ERP ReturnInput result code:" + result + ",param:" + JSON.toJSONString(order));
//            if (result.equals("00")) {
//            } else if (result.equals("11")) {
//                throw new ExceptionNoPower("ERP数据库连接失败");
//            } else if (result.equals("12")) {
//                throw new ExceptionBusiness("ERP数据库更新失败");
//            } else if (result.equals("21")) {
//                throw new ExceptionErrorParam("ERP验证失败");
//            } else {
//                throw new ExceptionBusiness("ERP未知错误");
//            }
//        } catch (Exception e) {
//            // 若通讯失败,记录失败原因及通讯信息
//            ordReturn.setErpSendStatus("20");
////            throw e;
//        } finally {
//            ordReturnExchangeMapperExt.updateSendById(ordReturn);
//        }
//    }

    @Override
    public JSONObject getProofInfo(String orderId, Map<String, Object> userMap) {
        JSONObject result = new JSONObject();
        try {
            String orgId = (String) userMap.get("orgId");
            //取得用户及门店信息
            ErpStore erpStore = erpStoreMapper.selectByPrimaryKey(orgId);
            if (null == erpStore || !"0".equals(erpStore.getDeleted())) {
                throw new ExceptionErrorData("门店信息不存在");
            }
            //取得主订单
            OrdMaster master = ordMasterMapper.selectByPrimaryKey(orderId);
            if (master == null) {
                throw new ExceptionErrorData("指定订单不存在");
            }
            //子订单
            OrdSubList ordSubList = new OrdSubList();
            ordSubList.setOrderId(orderId);
            ordSubList.setErpStoreId(orgId);
            List<OrdSubListExt> subList = ordSubListMapperExt.selectProof(ordSubList);
            //没有子订单数据
            if (subList == null || subList.size() == 0)
                throw new ExceptionErrorData("没有本店收货数据");
            BigDecimal amountTotle = new BigDecimal(0);
            BigDecimal amountDiscount = new BigDecimal(0);
            String subDetail = "";
            for (OrdSubList sub : subList) {
                BigDecimal priceDiscount = sub.getPriceDiscount();
                priceDiscount = priceDiscount == null || priceDiscount.equals(0) ? sub.getPrice() : priceDiscount;
                subDetail += "商品: " + sub.getGoodsCode() + "&nbsp;&nbsp;" + sub.getGoodsName() + "&nbsp;&nbsp;" + sub.getSkuId() + "<BR>";
                subDetail += "颜色: " + sub.getColoreName() + "&nbsp;&nbsp;尺码: " + sub.getSizeName() + "<BR>";
                subDetail += "价格: " + priceDiscount + "<BR>";
                subDetail += "收货时间: " + DataUtils.formatTimeStampToYMDHMS(sub.getDeliverTime()) + "<BR>";
                subDetail += "收货人: " + sub.getStoreDeliveryName() + "<BR><BR>";
                amountTotle = amountTotle.add(sub.getPrice());
                amountDiscount = amountDiscount.add(priceDiscount);
            }
            //合计金额
            master.setAmount(amountTotle);
            master.setAmountDiscount(amountDiscount);
            result.put("store", erpStore);
            result.put("master", master);
            result.put("sublist", subDetail);
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 处理自发货之日起15天后未收货的，自动设置为已收货
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject receiveOrder15DaysAgo() {
        JSONObject result = new JSONObject();
        try {
            //获取15天以发货的订单，条件为订单状态为10.订单未完成/23.退单驳回,发货状态为20.已发货
            List<OrdMaster> list = ordMasterMapperExt.getUnreceivedOrder15DaysAgo();
            for (int i = 0; i < list.size(); i++) {
                confirmReceiveOrderGoods(list.get(i).getOrderId(), "SYSTEM");
            }
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;
    }

    private void confirmReceiveOrderGoods(String orderId, String updateUserId) {
        OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(orderId);
        // 处理主单数据
        ordMaster.setDeliverStatus("21");
        ordMaster.setReceiptTime(new Date());
        ordMaster.setUpdateTime(new Date());
        ordMaster.setUpdateUserId(updateUserId);
        ordMasterMapper.updateByPrimaryKeySelective(ordMaster);
        //更新子订单收货状态为已收货
        OrdSubList sub = new OrdSubList();
        sub.setOrderId(ordMaster.getOrderId());
        sub.setUpdateUserId("SYSTEM");
        ordSubListMapperExt.receiveSubOrder(sub);
        //插入订单历史记录表
        OrdHistory ordHistory = new OrdHistory();
        ordHistory = this.masterToHistory(ordMaster);
        ordHistory.setSeqOrderId(UUID.randomUUID().toString());
        ordHistory.setInsertUserId("SYSTEM");
        //主订单操作历史信息插入
        ordHistoryMapperExt.insertSelective(ordHistory);
    }

    /**
     * 处理自缺认收货之日起7天后未申请退货的
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject finishOrder7DaysAgo() {
        JSONObject result = new JSONObject();
        try {
            //获取7天以前收货的订单，条件为订单状态为10.订单未完成/23.退单驳回,发货状态为21.已收货
            List<OrdMaster> list = ordMasterMapperExt.getUnfinishedOrder7DaysAgo();
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setOrderStatus("90");
                list.get(i).setUpdateTime(new Date());
                list.get(i).setUpdateUserId("SYSTEM");
                ordMasterMapper.updateByPrimaryKeySelective(list.get(i));
                //插入订单历史记录表
                OrdHistory ordHistory = new OrdHistory();
                ordHistory = this.masterToHistory(list.get(i));
                ordHistory.setSeqOrderId(UUID.randomUUID().toString());
                ordHistory.setInsertUserId("SYSTEM");
                //主订单操作历史信息插入
                ordHistoryMapperExt.insertSelective(ordHistory);
            }
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 处理自订单创建之日起30天以后流程完毕的订单
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject closeOrder30DaysAgo() {
        JSONObject result = new JSONObject();
        try {
            //获取30天以前创建的订单条件为：交易状态非"交易完成"或"交易关闭"，订单状态为：订单取消/退款完成/订单完成/退货完成/退货驳回
            List<OrdMaster> list = ordMasterMapperExt.getUnclosedOrder30DaysAgo();
            for (int i = 0; i < list.size(); i++) {
                if ("11".equals(list.get(i).getOrderStatus())
                        || "22".equals(list.get(i).getOrderStatus())
                        || "32".equals(list.get(i).getOrderStatus())) {
                    //交易关闭
                    list.get(i).setTradeStatus("30");
                } else if ("90".equals(list.get(i).getOrderStatus())
                        || "33".equals(list.get(i).getOrderStatus())) {
                    //交易成功
                    list.get(i).setTradeStatus("20");
                }
                list.get(i).setUpdateTime(new Date());
                list.get(i).setUpdateUserId("SYSTEM");
                ordMasterMapper.updateByPrimaryKeySelective(list.get(i));
                //插入订单历史记录表
                OrdHistory ordHistory = new OrdHistory();
                ordHistory = this.masterToHistory(list.get(i));
                ordHistory.setSeqOrderId(UUID.randomUUID().toString());
                ordHistory.setInsertUserId("SYSTEM");
                //主订单操作历史信息插入
                ordHistoryMapperExt.insertSelective(ordHistory);
            }
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;
    }


//    private static String changStringNull(String data) {
//        return data == null ? "" : data;
//    }

    /**
     * 销售订单对账单列表
     *
     * @param form
     * @return
     */
    @Override
    public JSONObject getReportSale(OrdMasterExt form) {
        JSONObject result = new JSONObject();
        try {
            //列表
            List<OrdSubList> list = ordSubListMapperExt.getReportSale(form);

            //结果设置
            //列表
            result.put("data", list);
            //返回code
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 退货订单对账单列表
     *
     * @param form
     * @return
     */
    @Override
    public JSONObject getReportReturn(OrdMasterExt form) {
        JSONObject result = new JSONObject();
        try {
            //列表
            List<OrdSubList> list = ordSubListMapperExt.getReportReturn(form);

            //结果设置
            //列表
            result.put("data", list);
            //返回code
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @Override
    public List<OrdMasterExt> excelExport(OrdMasterExt form) {
        List<OrdMasterExt> list = ordMasterMapperExt.excelExport(form);
        if(list != null && list.size() > 0){
            for(int i=0;i<list.size();i++){
                List<OrdSubListExt> subList = ordSubListMapperExt.excelSubExport(list.get(i).getOrderId());
                list.get(i).setOrdSubListExtList(subList);
            }

        }
        return list;
    }
}
