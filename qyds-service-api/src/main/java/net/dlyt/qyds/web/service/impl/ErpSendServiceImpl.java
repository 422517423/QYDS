package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.MmbPointRecordExt;
import net.dlyt.qyds.dao.*;
import net.dlyt.qyds.dao.ext.*;
import net.dlyt.qyds.web.service.ErpSendService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.DataUtils;
import net.dlyt.qyds.web.service.common.ErpSendUtil;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.erp.*;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import net.dlyt.qyds.web.service.exception.ExceptionNoPower;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.dlyt.qyds.web.service.common.DataUtils.formatTimeStampToYMDHMS;
import static net.dlyt.qyds.web.service.common.ErpKeyUtil.*;

/**
 * Created by zlh on 2016/9/4.
 */
@Service("erpSendService")
@Transactional(readOnly = false)
public class ErpSendServiceImpl implements ErpSendService {

    @Autowired
    private GdsMasterMapper gdsMasterMapper;

    @Autowired
    private GdsMasterMapperExt gdsMasterMapperExt;

    @Autowired
    private MmbMasterMapper mmbMasterMapper;

    @Autowired
    private MmbMasterMapperExt mmbMasterMapperExt;

    @Autowired
    private MmbPointRecordMapper mmbPointRecordMapper;

    @Autowired
    private MmbPointRecordMapperExt mmbPointRecordMapperExt;

    @Autowired
    private OrdMasterMapper ordMasterMapper;

    @Autowired
    private OrdMasterMapperExt ordMasterMapperExt;

    @Autowired
    private OrdSubListMapperExt ordSubListMapperExt;

    @Autowired
    private OrdReturnExchangeMapperExt ordReturnExchangeMapperExt;

    @Autowired
    private OrdTransferListMapper ordTransferListMapper;

    @Autowired
    private OrdTransferListMapperExt ordTransferListMapperExt;

    @Autowired
    private CouponMasterMapper couponMasterMapper;

    @Autowired
    private CouponMasterMapperExt couponMasterMapperExt;

    @Autowired
    private CouponGoodsMapper couponGoodsMapper;

    @Autowired
    private CouponGoodsMapperExt couponGoodsMapperExt;

    @Autowired
    private CouponMemberMapper couponMemberMapper;

    @Autowired
    private CouponMemberMapperExt couponMemberMapperExt;

    @Autowired
    private GdsBrandMapper gdsBrandMapper;

    @Autowired
    private GdsTypeMapper gdsTypeMapper;

    protected final Logger log = LoggerFactory.getLogger(ErpSendServiceImpl.class);

    /**
     * 发送未成功的商品分类
     *
     */
    public JSONObject sendFailGoodsType() {
        JSONObject result = new JSONObject();
        try{
            //取得发送失败商品列表
            List<GdsMaster> list = gdsMasterMapperExt.selectSendFail();
//            if (list == null || list.size() == 0) throw new ExceptionNoPower("没有发送数据");
            for (GdsMaster master : list) {
//                ErpSendUtil.getInstance().GoodsUpdate(master);
                ErpSendUtil.GoodsUpdate(master,gdsMasterMapper);
            }
            result.put("resultCode", Constants.NORMAL);
            result.put("resultMessage", "发送完成");
        }catch(Exception e){
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    /**
     * 发送指定的商品分类
     *
     */
    public JSONObject sendGoodsTypeById(String id) {
//        return ErpSendUtil.getInstance().GoodsUpdateById(id);
        return ErpSendUtil.GoodsUpdateById(id,gdsMasterMapper);
    }

    /**
     * 发送未成功的会员信息
     *
     */
    public JSONObject sendFailMember() {
        JSONObject result = new JSONObject();
        try{
            //取得发送失败会员列表
            List<MmbMaster> list = mmbMasterMapperExt.selectSendFail();
//            if (list == null || list.size() == 0) throw new ExceptionNoPower("没有发送数据");
            ErpSendUtil.initVIPUpdate(mmbMasterMapperExt,mmbMasterMapper);
            for (MmbMaster master : list) {
                ErpSendUtil.VIPUpdate(master);
            }
            result.put("resultCode", Constants.NORMAL);
            result.put("resultMessage", "发送完成");
        }catch(Exception e){
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    /**
     * 发送指定的会员信息
     *
     */
    public JSONObject sendMemberById(String id) {
//        return ErpSendUtil.getInstance().VIPUpdateById(id);
        return ErpSendUtil.VIPUpdateById(id,mmbMasterMapperExt,mmbMasterMapper);
    }


    /**
     * 发送未成功的会员积分记录
     *
     */
    public JSONObject sendFailPointRecord() {
        JSONObject result = new JSONObject();
        try{
            //取得发送失败会员积分记录
            List<MmbPointRecordExt> list = mmbPointRecordMapperExt.selectSendFail();
//            if (list == null || list.size() == 0) throw new ExceptionNoPower("没有发送数据");
            for (MmbPointRecordExt master : list) {
                //没有会员代码
                if (StringUtil.isEmpty(master.getMemberCode())) continue;
//                ErpSendUtil.getInstance().VIPPointUpdate(master);
                ErpSendUtil.VIPPointUpdate(master,mmbPointRecordMapper,mmbMasterMapper);            }
            result.put("resultCode", Constants.NORMAL);
            result.put("resultMessage", "发送完成");
        }catch(Exception e){
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    /**
     * 发送指定的会员积分记录
     *
     */
    public JSONObject sendPointRecordById(Integer id) {
//        return ErpSendUtil.getInstance().VIPPointUpdateById(id);
        return ErpSendUtil.VIPPointUpdateById(id,mmbPointRecordMapper,mmbPointRecordMapperExt,mmbMasterMapper);
    }

    /**
     * 发送未成功的优惠券
     *
     */
    public JSONObject sendFailCoupon() {
        JSONObject result = new JSONObject();
        try{
            //取得发送失败的代金券
            List<CouponMaster> list = couponMasterMapperExt.selectSendFail();
//            if (list == null || list.size() == 0) throw new ExceptionNoPower("没有发送数据");
            ErpSendUtil.initCouponissueUpdate(couponMasterMapperExt,couponGoodsMapperExt,gdsTypeMapper,gdsBrandMapper,gdsMasterMapperExt);
            for (CouponMaster master : list) {
//                ErpSendUtil.getInstance().CouponissueUpdate(master);
                ErpSendUtil.CouponissueUpdate(master);
            }
            result.put("resultCode", Constants.NORMAL);
            result.put("resultMessage", "发送完成");
        }catch(Exception e){
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    /**
     * 发送指定的优惠券
     *
     */
    public JSONObject sendFailCouponById(String id) {
        JSONObject result = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) throw new ExceptionErrorParam("优惠券ID未指定");
            CouponMaster master = couponMasterMapperExt.selectSendFailById(id);
            if (master == null) {
                throw new ExceptionErrorData("指定优惠券不存在或者已发放");
            }
//            result = ErpSendUtil.getInstance().CouponissueUpdate(master);
            result = ErpSendUtil.CouponissueUpdate(master,couponMasterMapperExt,couponGoodsMapperExt,gdsTypeMapper,gdsBrandMapper,gdsMasterMapperExt);
        }catch(Exception e){
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    /**
     * 发送指定的优惠券
     *
     */
    public JSONObject sendCouponById(String id) {
//        return ErpSendUtil.getInstance().CouponissueUpdateById(id);
        return ErpSendUtil.CouponissueUpdateById(id,couponMasterMapperExt,couponGoodsMapperExt,gdsTypeMapper,gdsBrandMapper,gdsMasterMapperExt,couponMasterMapper);
    }

    /**
     * 发送未成功的会员优惠券
     *
     */
    public JSONObject sendFailCouponMember() {
        JSONObject result = new JSONObject();
        try{
            //取得发送失败的已发代金券
            List<CouponMember> list = couponMemberMapperExt.selectSendFail();
//            if (list == null || list.size() == 0) throw new ExceptionNoPower("没有发送数据");
            ErpSendUtil.initCouponSendUpdate(couponMemberMapper,mmbMasterMapper);
            for (CouponMember master : list) {
//                ErpSendUtil.getInstance().CouponSendUpdate(master);
                ErpSendUtil.getInstance().CouponSendUpdate(master);
            }
            result.put("resultCode", Constants.NORMAL);
            result.put("resultMessage", "发送完成");
        }catch(Exception e){
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    /**
     * 发送指定的会员优惠券
     *
     */
    public JSONObject sendFailCouponMemberById(String id) {
        JSONObject result = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) throw new ExceptionErrorParam("会员优惠券ID未指定");
            CouponMember master = couponMemberMapperExt.selectSendFailById(id);
            if (master == null) {
                throw new ExceptionErrorData("指定优惠券不存在或者已经使用过");
            }
//            result = ErpSendUtil.getInstance().CouponSendUpdate(master);
            result = ErpSendUtil.CouponSendUpdate(master,couponMemberMapper,mmbMasterMapper);
        }catch(Exception e){
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    /**
     * 发送指定的会员优惠券
     *
     */
    public JSONObject sendCouponMemberById(String id) {
//        return ErpSendUtil.getInstance().CouponSendUpdateById(id);
        return ErpSendUtil.CouponSendUpdateById(id,couponMemberMapper,mmbMasterMapper);
    }

    /**
     * 发送未成功的已使用优惠券
     *
     */
    public JSONObject sendFailCouponUsed() {
        JSONObject result = new JSONObject();
        try{
            //取得发送失败的已发代金券
            List<CouponMember> list = couponMemberMapperExt.selectSendUsedFail();
//            if (list == null || list.size() == 0) throw new ExceptionNoPower("没有发送数据");

            ErpSendUtil.initCouponUsedUpdate(couponMemberMapper);
            for (CouponMember master : list) {
//                ErpSendUtil.getInstance().CouponUsedUpdate(master);
                ErpSendUtil.CouponUsedUpdate(master);
            }
            result.put("resultCode", Constants.NORMAL);
            result.put("resultMessage", "发送完成");
        }catch(Exception e){
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    /**
     * 发送指定的发送失败的已使用优惠券
     *
     */
    public JSONObject sendFailCouponUsedById(String id) {
        JSONObject result = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) throw new ExceptionErrorParam("会员优惠券ID未指定");
            CouponMember master = couponMemberMapperExt.selectSendUsedFailById(id);
            if (master == null) {
                throw new ExceptionErrorData("指定优惠券不存在或者已经使用过");
            }
//            result = ErpSendUtil.getInstance().CouponUsedUpdate(master);
            result = ErpSendUtil.CouponUsedUpdate(master,couponMemberMapper);
        }catch(Exception e){
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    /**
     * 发送指定的发送失败的已使用优惠券
     *
     */
    public JSONObject sendCouponUsedById(String id) {
//        return ErpSendUtil.getInstance().CouponUsedUpdateById(id);
        return ErpSendUtil.CouponUsedUpdateById(id,couponMemberMapper);
    }

    /**
     * 发送未成功的新订单
     *
     */
    public JSONObject sendFailOrder() {
        JSONObject result = new JSONObject();
        try{
            //取得发送失败订单
            List<OrdMasterExt> list = ordMasterMapperExt.selectSendFail();
//            if (list == null || list.size() == 0) throw new ExceptionNoPower("没有发送数据");
            ErpSendUtil.initSaleInput(ordSubListMapperExt,ordMasterMapper);
            for (OrdMasterExt master : list) {
//                ErpSendUtil.getInstance().SaleInput(master);
                ErpSendUtil.SaleInput(master);
            }
            result.put("resultCode", Constants.NORMAL);
            result.put("resultMessage", "发送完成");
        }catch(Exception e){
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    /**
     * 发送指定的新订单
     *
     */
    public JSONObject sendOrderById(String id) {
//        return ErpSendUtil.getInstance().SaleInputById(id);
        return ErpSendUtil.SaleInputById(id,ordSubListMapperExt,ordMasterMapper,ordMasterMapperExt);
    }

    /**
     * 发送未成功的退货订单
     *
     */
    public JSONObject sendFailReturnOrder() {
        JSONObject result = new JSONObject();
        try{
            //取得发送失败的订单
            List<OrdMasterExt> list = ordMasterMapperExt.selectSendFailReturn();
//            if (list == null || list.size() == 0) throw new ExceptionNoPower("没有发送数据");
            ErpSendUtil.initReturnInputById(ordSubListMapperExt,ordReturnExchangeMapperExt,ordMasterMapperExt);
            for (OrdMasterExt master : list) {
//                ErpSendUtil.getInstance().ReturnInput(master);
                ErpSendUtil.ReturnInput(master,ordSubListMapperExt,ordReturnExchangeMapperExt);
            }
            result.put("resultCode", Constants.NORMAL);
            result.put("resultMessage", "发送完成");
        }catch(Exception e){
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    /**
     * 发送指定的退货订单
     *
     */
    public JSONObject sendReturnOrderByOrderId(String id) {
//        return ErpSendUtil.getInstance().ReturnInputById(id);
        return ErpSendUtil.ReturnInputById(id);
    }

    /**
     * 发送指定的退货子订单
     *
     */
    public JSONObject sendReturnOrderBySubOrderId(String id) {
//        return ErpSendUtil.getInstance().ReturnInputBySubOrderId(id);
        return ErpSendUtil.ReturnInputBySubOrderId(id,ordSubListMapperExt,ordReturnExchangeMapperExt,ordMasterMapperExt);
    }

    /**
     * 发送指定的调货订单
     *
     */
    public JSONObject sendFailBankUpdate() {
        JSONObject result = new JSONObject();
        try{
            //取得发送失败的订单
            List<OrdTransferList> list = ordTransferListMapperExt.selectSendFail();
//            if (list == null || list.size() == 0) throw new ExceptionNoPower("没有发送数据");
            ErpSendUtil.initBankUpdate(ordTransferListMapper);
            for (OrdTransferList master : list) {
                if (master.getErpSendStatusDelivery().equals("20")) {
                    master.setTransferStatus("31");
                    ErpSendUtil.BankUpdate(master);
                }
                if (master.getErpSendStatusReceive().equals("20")) {
                    master.setTransferStatus("32");
                    ErpSendUtil.BankUpdate(master);
                }
            }
            result.put("resultCode", Constants.NORMAL);
            result.put("resultMessage", "发送完成");
        }catch(Exception e){
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    /**
     * 发送指定的退货子订单
     *
     */
    public JSONObject sendFailBankUpdateById(String id) {
        JSONObject result = new JSONObject();
        try{
            OrdTransferList master = ordTransferListMapper.selectByPrimaryKey(id);
            if(master==null) throw new ExceptionErrorData("没有调货记录");
            ErpSendUtil.initBankUpdate(ordTransferListMapper);
            if (master.getErpSendStatusDelivery().equals("20")) {
                master.setTransferStatus("31");
                ErpSendUtil.BankUpdate(master);
            }
            if (master.getErpSendStatusReceive().equals("20")) {
                master.setTransferStatus("32");
                ErpSendUtil.BankUpdate(master);
            }
            result.put("resultCode", Constants.NORMAL);
            result.put("resultMessage", "发送完成");
        }catch(Exception e){
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    /**
     * 发送未成功的新订单
     *
     */
    public JSONObject selectSendFailOrder() {
        JSONObject map = new JSONObject();
        try {
            //取得发送失败订单记录
            List<OrdMasterExt> list = ordMasterMapperExt.selectSendFail();
            map.put("data", list);
            map.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 发送未成功的禁用会员
     *
     */
    public JSONObject sendFailMemberUsed() {
        JSONObject result = new JSONObject();
        try{
            //取得发送失败会员列表
            List<MmbMaster> list = mmbMasterMapperExt.selectSendUsedFail();
//            if (list == null || list.size() == 0) throw new ExceptionNoPower("没有发送数据");
            ErpSendUtil.initVIPUsedUpdate(mmbMasterMapper);
            for (MmbMaster master : list) {
                ErpSendUtil.VIPUsedUpdate(master);
            }
            result.put("resultCode", Constants.NORMAL);
            result.put("resultMessage", "发送完成");
        }catch(Exception e){
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }
}
