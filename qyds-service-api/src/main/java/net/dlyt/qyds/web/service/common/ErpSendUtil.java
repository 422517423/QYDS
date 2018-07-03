package net.dlyt.qyds.web.service.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.common.util.StringUtils;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.MmbPointRecordExt;
import net.dlyt.qyds.dao.*;
import net.dlyt.qyds.dao.ext.*;
import net.dlyt.qyds.web.service.erp.*;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import net.dlyt.qyds.web.service.exception.ExceptionNoPower;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static net.dlyt.qyds.web.service.common.DataUtils.formatTimeStampToYMDHMS;
import static net.dlyt.qyds.web.service.common.ErpKeyUtil.*;

/**
 * Created by ZLH on 2016/11/10.
 */
@Component("erpSendUtil")
public class ErpSendUtil {
    //成功
    // 正式
    static public final String WSDL_LOCATION = "http://dsweb.dealuna.cn:27676/Service.asmx?wsdl";
    // 测试
//    static public final String WSDL_LOCATION = "http://dsweb.dealuna.cn:24444/Service.asmx?wsdl";
    static private final Logger log = LoggerFactory.getLogger(ErpSendUtil.class);
    static private Service service;
    static private ServiceSoap soap;

    static private ErpSendUtil instance = null;

    public static ErpSendUtil getInstance() {
        if (instance == null) {
            instance = new ErpSendUtil();
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    @Autowired
    static private GdsMasterMapper gdsMasterMapper;

    @Autowired
    static private MmbMasterMapperExt mmbMasterMapperExt;

    @Autowired
    static private MmbMasterMapper mmbMasterMapper;

    @Autowired
    static private MmbPointRecordMapper mmbPointRecordMapper;

    @Autowired
    static private MmbPointRecordMapperExt mmbPointRecordMapperExt;

    @Autowired
    static private CouponGoodsMapperExt couponGoodsMapperExt;

    @Autowired
    static private GdsBrandMapper gdsBrandMapper;

    @Autowired
    static private GdsTypeMapper gdsTypeMapper;

    @Autowired
    static private GdsMasterMapperExt gdsMasterMapperExt;

    @Autowired
    static private CouponMasterMapperExt couponMasterMapperExt;

    @Autowired
    static private CouponMasterMapper couponMasterMapper;

    @Autowired
    static private CouponMemberMapper couponMemberMapper;

    @Autowired
    static private OrdSubListMapperExt ordSubListMapperExt;

    @Autowired
    static private OrdMasterMapper ordMasterMapper;

    @Autowired
    static private OrdMasterMapperExt ordMasterMapperExt;

    @Autowired
    static private OrdReturnExchangeMapperExt ordReturnExchangeMapperExt;

    @Autowired
    static private OrdTransferListMapper ordTransferListMapper;

    @Autowired
    static private OrdLogisticStatusMapper ordLogisticStatusMapper;

    static private void init() throws Exception {
        try {
            if (service == null) service = new net.dlyt.qyds.web.service.erp.Service(new URL(WSDL_LOCATION));
            if (soap == null) soap = service.getServiceSoap();
        } catch (Exception e) {
            log.debug("ErpSendUtil 初期化失败");
            destroy();
            throw e;
        }
    }

    static private void destroy() {
        service = null;
        soap = null;
    }

    static public void initGoodsUpdate(GdsMasterMapper gdsMasterMapperIns) {
        if (gdsMasterMapper == null && gdsMasterMapperIns != null) {
            gdsMasterMapper = gdsMasterMapperIns;
        }
    }

    static public JSONObject GoodsUpdate(GdsMaster master, GdsMasterMapper gdsMasterMapperIns) {
        initGoodsUpdate(gdsMasterMapperIns);
        return GoodsUpdate(master);
    }

    static public JSONObject GoodsUpdateById(String id, GdsMasterMapper gdsMasterMapperIns) {
        initGoodsUpdate(gdsMasterMapperIns);
        return GoodsUpdateById(id);
    }

    static public JSONObject GoodsUpdate(GdsMaster master) {
        JSONObject result = new JSONObject();
        result.put("resultCode", Constants.NORMAL);

        GdsMaster masterSend = new GdsMaster();
        masterSend.setGoodsId(master.getGoodsId());
        masterSend.setErpSendTypeStatus("10");
        try {
            String[] typeList = master.getGoodsTypeNamePath().split("_");
            Goods goods = new Goods();
            goods.setGoodsCode(master.getErpGoodsCode());
            if (typeList.length > 0) goods.setGoodstype1(typeList[0]);
            if (typeList.length > 1) goods.setGoodstype2(typeList[0]);
            if (typeList.length > 2) goods.setGoodstype3(typeList[0]);
            BaseDate baseDate = new BaseDate();
            baseDate.setGoods(goods);
            log.debug("ERP GoodsUpdate input param:" + JSON.toJSONString(baseDate));
            init();
            String resultCode = "";
            try {
                resultCode = soap.goodsUpdate(getKeyGoodsUpdate(baseDate), baseDate);
            } catch (Exception e) {
                log.debug("ERP GoodsUpdate 接口调用失败");
                destroy();
                throw e;
            }
            log.debug("ERP GoodsUpdate result code:" + resultCode);
            if (resultCode.equals("00")) {
            } else if (resultCode.equals("11")) {
                throw new ExceptionNoPower("ERP数据库连接失败");
            } else if (resultCode.equals("12")) {
                throw new ExceptionBusiness("ERP数据库更新失败");
            } else if (resultCode.equals("21")) {
                throw new ExceptionErrorParam("ERP验证失败");
            } else {
                throw new ExceptionBusiness("ERP未知错误");
            }
        } catch (MalformedURLException e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendTypeStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", "ERP接口连接失败:" + e.getMessage());
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendTypeStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        try {
            masterSend.setUpdateTime(new Date());
            gdsMasterMapper.updateByPrimaryKeySelective(masterSend);
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    static public JSONObject GoodsUpdateById(String id) {
        JSONObject result = new JSONObject();
        try {
            if (StringUtil.isEmpty(id)) throw new ExceptionErrorParam("参数商品ID未指定");
            GdsMaster master = gdsMasterMapper.selectByPrimaryKey(id);
            if (master == null) throw new ExceptionErrorData("指定商品不存在");
            result = GoodsUpdate(master);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    static public void initVIPUpdate(MmbMasterMapperExt mmbMasterMapperExtIns, MmbMasterMapper mmbMasterMapperIns) {
        if (mmbMasterMapperExt == null && mmbMasterMapperExtIns != null) {
            mmbMasterMapperExt = mmbMasterMapperExtIns;
        }
        if (mmbMasterMapper == null && mmbMasterMapperIns != null) {
            mmbMasterMapper = mmbMasterMapperIns;
        }
    }

    static public JSONObject VIPUpdate(MmbMaster master, MmbMasterMapperExt mmbMasterMapperExtIns, MmbMasterMapper mmbMasterMapperIns) {
        initVIPUpdate(mmbMasterMapperExtIns, mmbMasterMapperIns);
        return VIPUpdate(master);
    }

    static public JSONObject VIPUpdateById(String id, MmbMasterMapperExt mmbMasterMapperExtIns, MmbMasterMapper mmbMasterMapperIns) {
        initVIPUpdate(mmbMasterMapperExtIns, mmbMasterMapperIns);
        return VIPUpdateById(id);
    }

    static public JSONObject VIPUpdate(MmbMaster master) {
        JSONObject result = new JSONObject();
        result.put("resultCode", Constants.NORMAL);
        MmbMaster masterSend = new MmbMaster();
        masterSend.setMemberId(master.getMemberId());
        masterSend.setErpSendStatus("10");
        try {
            Vip vip = new Vip();
            vip.setMemberName(master.getMemberName());
            vip.setSexName("1".equals(master.getSex()) ? "男" : "女");
            boolean changeTel = !StringUtil.isEmpty(master.getOldphone())
                    && !master.getOldphone().equals(master.getTelephone());
            if (changeTel) {
                //修改电话重复检查
//                MmbMaster mmbMasterCheck = new MmbMaster();
//                mmbMasterCheck.setTelephone(master.getTelephone());
//                int hvCount = mmbMasterMapperExt.getCountBySelective(mmbMasterCheck);
//                if (hvCount>0) {
//                    throw new ExceptionErrorParam("该号码已注册");
//                }
                vip.setMemberCode(master.getOldphone());
                vip.setMobil(master.getOldphone());
                vip.setMemberCodeAlter(master.getTelephone());
            } else {
                vip.setMemberCode(master.getTelephone());
                vip.setMobil(master.getTelephone());
            }
            vip.setBirthday(DataUtils.formatTimeStampToYMD(master.getBirthdate()));
            vip.setProvinceName(master.getProvinceName());
            vip.setCityName(master.getCityName());
            vip.setDistrictName(master.getDistrictName());
            vip.setEmail(master.getEmail());
//            vip.setStoreCode("");
//            vip.setStoreName("");
            vip.setSellerName(master.getReferrerId());
            vip.setAddress(master.getAddress());
            vip.setPostCode(master.getPostCode());
            vip.setIncome(master.getIncome());
            vip.setRegistTime(DataUtils.formatTimeStampToYMD(master.getInsertTime()));
            vip.setMemberGrade(master.getMemberLevelId());

            BaseDate baseDate = new BaseDate();
            baseDate.setVip(vip);
            log.debug("ERP VIPUpdate input param:" + JSON.toJSONString(baseDate));
            init();
            String resultCode = "";
            try {
                resultCode = soap.vipUpdate(getKeyVIPUpdate(baseDate), baseDate);
            } catch (Exception e) {
                log.debug("ERP VIPUpdate 接口调用失败");
                destroy();
                throw e;
            }
            log.debug("ERP VIPUpdate result code:" + resultCode);
            if (resultCode.equals("00")) {
                //清除旧号码
                if (!StringUtil.isEmpty(master.getOldphone())) {
                    mmbMasterMapperExt.clearOldphoneById(master.getMemberId());
                }
            } else if (resultCode.equals("11")) {
                throw new ExceptionNoPower("ERP数据库连接失败");
            } else if (resultCode.equals("12")) {
                throw new ExceptionBusiness("ERP数据库更新失败");
            } else if (resultCode.equals("13")) {
                masterSend.setErpSendStatus("13");
                result.put("resultCode", Constants.FAIL);
                result.put("resultMessage", "ERP数据重复");
//                throw new ExceptionBusiness("ERP数据重复");
            } else if (resultCode.equals("21")) {
                throw new ExceptionErrorParam("ERP验证失败");
            } else {
                throw new ExceptionBusiness("ERP未知错误");
            }
        } catch (MalformedURLException e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", "ERP接口连接失败:" + e.getMessage());
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        try {
            masterSend.setUpdateTime(new Date());
            mmbMasterMapper.updateByPrimaryKeySelective(masterSend);
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    static public void initVIPUsedUpdate(MmbMasterMapper mmbMasterMapperIns) {
        if (mmbMasterMapper == null && mmbMasterMapperIns != null) {
            mmbMasterMapper = mmbMasterMapperIns;
        }
    }

    static public JSONObject VIPUsedUpdate(MmbMaster master) {
        JSONObject result = new JSONObject();
        result.put("resultCode", Constants.NORMAL);
        MmbMaster masterSend = new MmbMaster();
        masterSend.setMemberId(master.getMemberId());
        masterSend.setUnUsedTime(master.getUnUsedTime());
        masterSend.setErpSendCancleStatus("10");

        try {
            Vip vip = new Vip();
            vip.setMemberCode(master.getTelephone());
            vip.setUnUsed(master.getIsValid());
            vip.setUnUsedTime(master.getUnUsedTime());

            BaseDate baseDate = new BaseDate();
            baseDate.setVip(vip);
            log.debug("ERP VIPUsedUpdate input param:" + JSON.toJSONString(baseDate));
            init();
            String resultCode = "";
            try {
                resultCode = soap.vipUsedUpdate(getKeyVIPUsedUpdate(baseDate), baseDate);
            } catch (Exception e) {
                log.debug("ERP VIPUsedUpdate 接口调用失败");
                destroy();
                throw e;
            }
            log.debug("ERP VIPUsedUpdate result code:" + resultCode);
            if (resultCode.equals("00")) {
            } else if (resultCode.equals("11")) {
                throw new ExceptionNoPower("ERP数据库连接失败");
            } else if (resultCode.equals("12")) {
                throw new ExceptionBusiness("ERP数据库更新失败");
            } else if (resultCode.equals("21")) {
                throw new ExceptionErrorParam("ERP验证失败");
            } else {
                throw new ExceptionBusiness("ERP未知错误");
            }
        } catch (MalformedURLException e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendCancleStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", "ERP接口连接失败:" + e.getMessage());
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        try {
            masterSend.setUpdateTime(new Date());
            mmbMasterMapper.updateByPrimaryKeySelective(masterSend);
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    static public JSONObject VIPUpdateById(String id) {
        JSONObject result = new JSONObject();
        try {
            if (StringUtil.isEmpty(id)) throw new ExceptionErrorParam("参数会员ID未指定");
            MmbMaster master = mmbMasterMapper.selectByPrimaryKey(id);
            if (master == null) throw new ExceptionErrorData("指定会员不存在");
            result = VIPUpdate(master);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    static public void initVIPPointUpdate(MmbPointRecordMapper mmbPointRecordMapperIns, MmbMasterMapper mmbMasterMapperIns) {
        if (mmbPointRecordMapper == null && mmbPointRecordMapperIns != null) {
            mmbPointRecordMapper = mmbPointRecordMapperIns;
        }
        if (mmbMasterMapper == null && mmbMasterMapperIns != null) {
            mmbMasterMapper = mmbMasterMapperIns;
        }
    }

    static public void initVIPPointUpdateById(MmbPointRecordMapper mmbPointRecordMapperIns,
                                              MmbPointRecordMapperExt mmbPointRecordMapperExtIns, MmbMasterMapper mmbMasterMapperIns) {
        initVIPPointUpdate(mmbPointRecordMapperIns, mmbMasterMapperIns);
        if (mmbPointRecordMapperExt == null && mmbPointRecordMapperExtIns != null) {
            mmbPointRecordMapperExt = mmbPointRecordMapperExtIns;
        }
    }

    static public JSONObject VIPPointUpdate(MmbPointRecordExt master, MmbPointRecordMapper mmbPointRecordMapperIns, MmbMasterMapper mmbMasterMapperIns) {
        initVIPPointUpdate(mmbPointRecordMapperIns, mmbMasterMapperIns);
        return VIPPointUpdate(master);
    }

    static public JSONObject VIPPointUpdate(MmbPointRecord master, MmbPointRecordMapper mmbPointRecordMapperIns, MmbMasterMapper mmbMasterMapperIns) {
        MmbPointRecordExt ext = new MmbPointRecordExt();
        BeanUtils.copyProperties(master, ext);
        return VIPPointUpdate(ext, mmbPointRecordMapperIns, mmbMasterMapperIns);
    }

    static public JSONObject VIPPointUpdateById(Integer id, MmbPointRecordMapper mmbPointRecordMapperIns,
                                                MmbPointRecordMapperExt mmbPointRecordMapperExtIns, MmbMasterMapper mmbMasterMapperIns) {
        initVIPPointUpdateById(mmbPointRecordMapperIns, mmbPointRecordMapperExtIns, mmbMasterMapperIns);
        return VIPPointUpdateById(id);
    }

    static public JSONObject VIPPointUpdate(MmbPointRecordExt master) {
        JSONObject result = new JSONObject();
        result.put("resultCode", Constants.NORMAL);
        if (!StringUtil.isEmpty(master.getErpSendStatus()) && master.getErpSendStatus().equals("10")) {
//            throw new ExceptionErrorData("指定积分已经发送成功,不可再次发送");
            return result;
        }
        MmbPointRecord masterSend = new MmbPointRecord();
        masterSend.setRecordNo(master.getRecordNo());
        masterSend.setErpSendStatus("10");
        try {
            VipPoint point = new VipPoint();
            if (StringUtil.isEmpty(master.getMemberCode())) {
                MmbMaster member = mmbMasterMapper.selectByPrimaryKey(master.getMemberId());
                master.setMemberCode(member.getTelephone());
            }
            point.setMemberCode(master.getMemberCode());
            point.setPoint(master.getPoint() == null ? "" : master.getPoint().toString());
            point.setPointTime(DataUtils.formatTimeStampToYMD(master.getPointTime()));
            //追加使用标记
            point.setUsed((master.getType().equals("60") || master.getType().equals("70")) ? "1" : "0");

            BaseDate baseDate = new BaseDate();
            baseDate.setVipPoint(point);
            log.debug("ERP VIPPointUpdate input param:" + JSON.toJSONString(baseDate));
            init();
            String resultCode = "";
            try {
                resultCode = soap.vipPointUpdate(getKeyVIPPointUpdate(baseDate), baseDate);
            } catch (Exception e) {
                log.debug("ERP VIPPointUpdate 接口调用失败");
                destroy();
                throw e;
            }
            log.debug("ERP VIPPointUpdate result code:" + resultCode);
            if (resultCode.equals("00")) {
            } else if (resultCode.equals("11")) {
                throw new ExceptionNoPower("ERP数据库连接失败");
            } else if (resultCode.equals("12")) {
                throw new ExceptionBusiness("ERP数据库更新失败");
            } else if (resultCode.equals("13")) {
                masterSend.setErpSendStatus("13");
//                throw new ExceptionBusiness("ERP数据重复");
            } else if (resultCode.equals("21")) {
                throw new ExceptionErrorParam("ERP验证失败");
            } else {
                throw new ExceptionBusiness("ERP未知错误");
            }
        } catch (MalformedURLException e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", "ERP接口连接失败:" + e.getMessage());
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        try {
            masterSend.setUpdateTime(new Date());
            mmbPointRecordMapper.updateByPrimaryKeySelective(masterSend);
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    static public JSONObject VIPPointUpdateById(Integer id) {
        JSONObject result = new JSONObject();
        try {
            if (id == null) throw new ExceptionErrorParam("参数记录编号未指定");
            MmbPointRecordExt master = mmbPointRecordMapperExt.selectSendById(id);
            if (master == null) {
                throw new ExceptionErrorData("指定积分记录不存在");
            }
            if (!StringUtil.isEmpty(master.getErpSendStatus()) && master.getErpSendStatus().equals("10"))
                throw new ExceptionErrorData("指定积分记录已经发送成功,不可再次发送");
            result = VIPPointUpdate(master);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    static public void initCouponissueUpdate(CouponMasterMapperExt couponMasterMapperExtIns, CouponGoodsMapperExt couponGoodsMapperExtIns,
                                             GdsTypeMapper gdsTypeMapperIns, GdsBrandMapper gdsBrandMapperIns, GdsMasterMapperExt gdsMasterMapperExtIns) {
        if (couponMasterMapperExt == null && couponMasterMapperExtIns != null) {
            couponMasterMapperExt = couponMasterMapperExtIns;
        }
        if (couponGoodsMapperExt == null && couponGoodsMapperExtIns != null) {
            couponGoodsMapperExt = couponGoodsMapperExtIns;
        }
        if (gdsTypeMapper == null && gdsTypeMapperIns != null) {
            gdsTypeMapper = gdsTypeMapperIns;
        }
        if (gdsBrandMapper == null && gdsBrandMapperIns != null) {
            gdsBrandMapper = gdsBrandMapperIns;
        }
        if (gdsMasterMapperExt == null && gdsMasterMapperExtIns != null) {
            gdsMasterMapperExt = gdsMasterMapperExtIns;
        }
    }

    static public void initCouponissueUpdateById(CouponMasterMapperExt couponMasterMapperExtIns, CouponGoodsMapperExt couponGoodsMapperExtIns,
                                                 GdsTypeMapper gdsTypeMapperIns, GdsBrandMapper gdsBrandMapperIns,
                                                 GdsMasterMapperExt gdsMasterMapperExtIns, CouponMasterMapper couponMasterMapperIns) {
        initCouponissueUpdate(couponMasterMapperExtIns, couponGoodsMapperExtIns, gdsTypeMapperIns,
                gdsBrandMapperIns, gdsMasterMapperExtIns);
        if (couponMasterMapper == null && couponMasterMapperIns != null) {
            couponMasterMapper = couponMasterMapperIns;
        }
    }

    static public JSONObject CouponissueUpdate(CouponMaster master, CouponMasterMapperExt couponMasterMapperExtIns, CouponGoodsMapperExt couponGoodsMapperExtIns,
                                               GdsTypeMapper gdsTypeMapperIns, GdsBrandMapper gdsBrandMapperIns,
                                               GdsMasterMapperExt gdsMasterMapperExtIns) {
        initCouponissueUpdate(couponMasterMapperExtIns, couponGoodsMapperExtIns, gdsTypeMapperIns, gdsBrandMapperIns, gdsMasterMapperExtIns);
        return CouponissueUpdate(master);
    }

    static public JSONObject CouponissueUpdateById(String id, CouponMasterMapperExt couponMasterMapperExtIns, CouponGoodsMapperExt couponGoodsMapperExtIns,
                                                   GdsTypeMapper gdsTypeMapperIns, GdsBrandMapper gdsBrandMapperIns,
                                                   GdsMasterMapperExt gdsMasterMapperExtIns, CouponMasterMapper couponMasterMapperIns) {
        initCouponissueUpdateById(couponMasterMapperExtIns, couponGoodsMapperExtIns, gdsTypeMapperIns, gdsBrandMapperIns,
                gdsMasterMapperExtIns, couponMasterMapperIns);
        return CouponissueUpdateById(id);
    }

    static public JSONObject CouponissueUpdate(CouponMaster master) {
        JSONObject result = new JSONObject();
        result.put("resultCode", Constants.NORMAL);
        if (!StringUtil.isEmpty(master.getErpSendStatus()) && master.getErpSendStatus().equals("10")) {
//            throw new ExceptionErrorData("指定优惠券已经发送成功,不可再次发送");
            return result;
        }
        CouponMaster masterSend = new CouponMaster();
        masterSend.setCouponId(master.getCouponId());
        masterSend.setErpSendStatus("10");
        try {
            //优惠券主表
            CouponIssue couponIssue = new CouponIssue();
            couponIssue.setCouponId(master.getCouponId());
            couponIssue.setCouponName(master.getCouponName());
            couponIssue.setCouponStyle(master.getCouponStyle());
            couponIssue.setCouponType(master.getCouponType());
            String goodsType = master.getGoodsType();
            goodsType = (goodsType.equals("60") || goodsType.equals("70") || goodsType.equals("80")) ? "50" : goodsType;
            couponIssue.setGoodsType(goodsType);
            couponIssue.setDistributeType(master.getDistributeType());
            couponIssue.setIsOriginPrice(master.getIsOriginPrice());
            couponIssue.setMinGoodsCount(master.getMinGoodsCount());
            couponIssue.setMinOrderPrice(master.getMinOrderPrice());
            couponIssue.setWorth(master.getWorth() == null ? "" : master.getWorth().toString());
            couponIssue.setDiscount(master.getDiscount() == null ? "" : master.getDiscount().toString());
            couponIssue.setSendStartTime(DataUtils.formatTimeStampToYMD(master.getSendStartTime()));
            couponIssue.setSendEndTime(DataUtils.formatTimeStampToYMD(master.getSendEndTime()));
            couponIssue.setStartTime(DataUtils.formatTimeStampToYMD(master.getStartTime()));
            couponIssue.setEndTime(DataUtils.formatTimeStampToYMD(master.getEndTime()));
            couponIssue.setMaxCount(master.getMaxCount() == null ? "" : master.getMaxCount().toString());
            couponIssue.setValidDays(master.getValidDays() == null ? "" : master.getValidDays().toString());
            couponIssue.setDistributedCount(master.getDistributedCount() == null ? "" : master.getDistributedCount().toString());
            couponIssue.setComment(master.getComment());

            //商品绑定
            List<CouponGoods> list = couponGoodsMapperExt.selectByCouponIdS(master.getCouponId());
            //没有商品绑定,继续
//            if (list == null || list.size() == 0) throw new ExceptionErrorData("没有优惠券商品绑定数据");
            Coupon coupon = new Coupon();
            coupon.setCouponIssue(couponIssue);
            coupon.setCouponSku(new ArrayOfCouponSku());
            if (list != null && list.size() > 0) {
                List<CouponSku> skuList = new ArrayList<CouponSku>();
                for (CouponGoods goods : list) {
                    CouponSku sku = new CouponSku();
                    sku.setCouponId(goods.getCouponId());
//                    goodsType = goods.getGoodsType();
                    //GoodsId
                    String goodsId = goods.getGoodsId();
                    if (goodsType.equals("10")) {
                        //全部
                    } else if (goodsType.equals("20")) {
                        //商品分类
                        GdsType type = gdsTypeMapper.selectByPrimaryKey(goodsId);
                        if (type == null || !type.getType().equals("10")) continue;
                        goodsId = type.getGoodsTypeNameCn();
                    } else if (goodsType.equals("30")) {
                        //品牌
                        GdsBrand brand = gdsBrandMapper.selectByPrimaryKey(goodsId);
                        if (brand == null || !brand.getType().equals("10")) continue;
                    } else if (goodsType.equals("40")) {
                        //商品
                        GdsMaster m = gdsMasterMapperExt.getErpGoodsCodeByGoodsId(goodsId);
                        if (m == null || StringUtil.isEmpty(m.getErpGoodsCode())) continue;
                        goodsId = m.getErpGoodsCode();
                    } else if (goodsType.equals("50")) {
                        //SKU
                        if (StringUtil.isEmpty(goods.getSkuId())) continue;
                    } else {
                        continue;
                    }
                    sku.setGoodsType(goodsType);
                    sku.setGoodsId(goodsId);
                    sku.setSkuId(goods.getSkuId());
                    skuList.add(sku);
                }
                coupon.getCouponSku().setCouponSku(skuList);
            }
            log.debug("ERP CouponissueUpdate input param:" + JSON.toJSONString(coupon));
            init();
            String resultCode = "";
            try {
                resultCode = soap.couponissueUpdate(getKeyCouponissueUpdate(coupon), coupon);
            } catch (Exception e) {
                log.debug("ERP CouponissueUpdate 接口调用失败");
                destroy();
                throw e;
            }
            log.debug("ERP CouponissueUpdate result code:" + resultCode);
            if (resultCode.equals("00")) {
            } else if (resultCode.equals("11")) {
                throw new ExceptionNoPower("ERP数据库连接失败");
            } else if (resultCode.equals("12")) {
                throw new ExceptionBusiness("ERP数据库更新失败");
            } else if (resultCode.equals("13")) {
                masterSend.setErpSendStatus("13");
//                throw new ExceptionBusiness("ERP数据重复");
            } else if (resultCode.equals("21")) {
                throw new ExceptionErrorParam("ERP验证失败");
            } else {
                throw new ExceptionBusiness("ERP未知错误");
            }
        } catch (MalformedURLException e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", "ERP接口连接失败:" + e.getMessage());
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        try {
            masterSend.setUpdateTime(new Date());
            couponMasterMapperExt.updateSendById(masterSend);
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    static public JSONObject CouponissueUpdateById(String id) {
        JSONObject result = new JSONObject();
        try {
            if (StringUtil.isEmpty(id)) throw new ExceptionErrorParam("优惠券ID未指定");
            CouponMaster master = couponMasterMapper.selectByPrimaryKey(id);
            if (master == null) {
                throw new ExceptionErrorData("指定优惠券不存在");
            }
            if (!StringUtil.isEmpty(master.getErpSendStatus()) && master.getErpSendStatus().equals("10"))
                throw new ExceptionErrorData("指定优惠券已经发送成功,不可再次发送");
            result = CouponissueUpdate(master);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }


    static public void initCouponSendUpdate(CouponMemberMapper couponMemberMapperIns, MmbMasterMapper mmbMasterMapperIns) {
        if (couponMemberMapper == null && couponMemberMapperIns != null) {
            couponMemberMapper = couponMemberMapperIns;
        }
        if (mmbMasterMapper == null && mmbMasterMapperIns != null) {
            mmbMasterMapper = mmbMasterMapperIns;
        }
    }

    static public JSONObject CouponSendUpdate(CouponMember master, CouponMemberMapper couponMemberMapperIns, MmbMasterMapper mmbMasterMapperIns) {
        initCouponSendUpdate(couponMemberMapperIns, mmbMasterMapperIns);
        return CouponSendUpdate(master);
    }

    static public JSONObject CouponSendUpdateById(String id, CouponMemberMapper couponMemberMapperIns, MmbMasterMapper mmbMasterMapperIns) {
        initCouponSendUpdate(couponMemberMapperIns, mmbMasterMapperIns);
        return CouponSendUpdateById(id);
    }

    static public JSONObject CouponSendUpdate(CouponMember master) {
        JSONObject result = new JSONObject();
        result.put("resultCode", Constants.NORMAL);
        if (!StringUtil.isEmpty(master.getErpSendStatus()) && master.getErpSendStatus().equals("10")) {
//            throw new ExceptionErrorData("指定会员优惠券已经发送成功,不可再次发送");
            result.put("resultMessage", "指定会员优惠券已经发送成功,不可再次发送");
            return result;
        }
        CouponMember masterSend = new CouponMember();
        masterSend.setCouponMemberId(master.getCouponMemberId());
        masterSend.setErpSendStatus("10");
        try {
            MmbMaster mmb = mmbMasterMapper.selectByPrimaryKey(master.getMemberId());
            if (mmb == null) throw new ExceptionBusiness("指定会员不存在");
            CouponSend send = new CouponSend();
            send.setCouponMemberId(master.getCouponMemberId());
            send.setCouponId(master.getCouponId());
            send.setMemberId(mmb.getTelephone());
            send.setStatus("10");
            send.setStartTime(DataUtils.formatTimeStampToYMD(master.getStartTime()));
            send.setEndTime(DataUtils.formatTimeStampToYMD(master.getEndTime()));
            send.setSendTime(DataUtils.formatTimeStampToYMD(master.getSendTime()));

            Coupon coupon = new Coupon();
            coupon.setCouponSend(send);
            log.debug("ERP CouponSendUpdate input param:" + JSON.toJSONString(coupon));
            init();
            String resultCode = "";
            try {
                resultCode = soap.couponSendUpdate(getKeyCouponSendUpdate(coupon), coupon);
            } catch (Exception e) {
                log.debug("ERP CouponSendUpdate 接口调用失败");
                destroy();
                throw e;
            }
            log.debug("ERP CouponSendUpdate result code:" + resultCode);
            if (resultCode.equals("00")) {
            } else if (resultCode.equals("11")) {
                throw new ExceptionNoPower("ERP数据库连接失败");
            } else if (resultCode.equals("12")) {
                throw new ExceptionBusiness("ERP数据库更新失败");
            } else if (resultCode.equals("13")) {
                masterSend.setErpSendStatus("13");
//                throw new ExceptionBusiness("ERP数据重复");
            } else if (resultCode.equals("21")) {
                throw new ExceptionErrorParam("ERP验证失败");
            } else {
                throw new ExceptionBusiness("ERP未知错误");
            }
        } catch (MalformedURLException e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", "ERP接口连接失败:" + e.getMessage());
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        try {
            masterSend.setUpdateTime(new Date());
            couponMemberMapper.updateByPrimaryKeySelective(masterSend);
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    static public JSONObject CouponSendUpdateById(String id) {
        JSONObject result = new JSONObject();
        try {
            if (StringUtil.isEmpty(id)) throw new ExceptionErrorParam("会员优惠券ID未指定");
            CouponMember master = couponMemberMapper.selectByPrimaryKey(id);
            if (master == null) {
                throw new ExceptionErrorData("指定会员优惠券不存在");
            }
            if ((!StringUtil.isEmpty(master.getErpSendStatus())) && master.getErpSendStatus().equals("10"))
                throw new ExceptionErrorData("指定优惠券已经发送成功,不可再次发送");
            result = CouponSendUpdate(master);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }


    static public void initCouponUsedUpdate(CouponMemberMapper couponMemberMapperIns) {
        if (couponMemberMapper == null && couponMemberMapperIns != null) {
            couponMemberMapper = couponMemberMapperIns;
        }
    }

    static public JSONObject CouponUsedUpdate(CouponMember master, CouponMemberMapper couponMemberMapperIns) {
        initCouponUsedUpdate(couponMemberMapperIns);
        return CouponUsedUpdate(master);
    }

    static public JSONObject CouponUsedUpdateById(String id, CouponMemberMapper couponMemberMapperIns) {
        initCouponUsedUpdate(couponMemberMapperIns);
        return CouponUsedUpdateById(id);
    }

    static public JSONObject CouponUsedUpdate(CouponMember master) {
        JSONObject result = new JSONObject();
        result.put("resultCode", Constants.NORMAL);
        if (!StringUtil.isEmpty(master.getErpSendUsedStatus()) && master.getErpSendUsedStatus().equals("10")) {
//            throw new ExceptionErrorData("指定会员优惠券已经发送成功,不可再次发送");
            result.put("resultMessage", "指定会员优惠券已经发送成功,不可再次发送");
            return result;
        }
        CouponMember masterSend = new CouponMember();
        masterSend.setCouponMemberId(master.getCouponMemberId());
        masterSend.setErpSendUsedStatus("10");
        try {
            CouponUsed send = new CouponUsed();
            send.setCouponMemberId(master.getCouponMemberId());
            send.setCouponId(master.getCouponId());
            send.setMemberId(master.getMemberId());
            send.setOrderId(master.getOrderId());
            send.setStatus(master.getStatus());
            send.setUsedTime(DataUtils.formatTimeStampToYMD(master.getUsedTime()));

            Coupon coupon = new Coupon();
            coupon.setCouponUsed(send);
            log.debug("ERP CouponUsedUpdate input param:" + JSON.toJSONString(coupon));
            init();
            String resultCode = "";
            try {
                resultCode = soap.couponUsedUpdate(getKeyCouponUsedUpdate(coupon), coupon);
            } catch (Exception e) {
                log.debug("ERP CouponUsedUpdate 接口调用失败");
                destroy();
                throw e;
            }
            log.debug("ERP CouponUsedUpdate result code:" + resultCode);
            if (resultCode.equals("00")) {
            } else if (resultCode.equals("11")) {
                throw new ExceptionNoPower("ERP数据库连接失败");
            } else if (resultCode.equals("12")) {
                throw new ExceptionBusiness("ERP数据库更新失败");
//            } else if (resultCode.equals("13")) {
//                masterSend.setErpSendUsedStatus("13");
//                throw new ExceptionBusiness("ERP数据重复");
            } else if (resultCode.equals("21")) {
                throw new ExceptionErrorParam("ERP验证失败");
            } else {
                throw new ExceptionBusiness("ERP未知错误");
            }
        } catch (MalformedURLException e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", "ERP接口连接失败:" + e.getMessage());
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendUsedStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        try {
            masterSend.setUpdateTime(new Date());
            couponMemberMapper.updateByPrimaryKeySelective(masterSend);
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    static public JSONObject CouponUsedUpdateById(String id) {
        JSONObject result = new JSONObject();
        try {
            if (StringUtil.isEmpty(id)) throw new ExceptionErrorParam("会员优惠券ID未指定");
            CouponMember master = couponMemberMapper.selectByPrimaryKey(id);
            if (master == null) {
                throw new ExceptionErrorData("指定会员优惠券不存在");
            }
            if (!StringUtil.isEmpty(master.getErpSendUsedStatus()) && master.getErpSendUsedStatus().equals("10"))
                throw new ExceptionErrorData("指定优惠券已经发送成功,不可再次发送");
            result = CouponUsedUpdate(master);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    static public void initSaleInput(OrdSubListMapperExt ordSubListMapperExtIns, OrdMasterMapper ordMasterMapperIns) {
        if (ordSubListMapperExt == null && ordSubListMapperExtIns != null) {
            ordSubListMapperExt = ordSubListMapperExtIns;
        }
        if (ordMasterMapper == null && ordMasterMapperIns != null) {
            ordMasterMapper = ordMasterMapperIns;
        }
    }

    static public void initSaleInputById(OrdSubListMapperExt ordSubListMapperExtIns, OrdMasterMapper ordMasterMapperIns, OrdMasterMapperExt ordMasterMapperExtIns) {
        initSaleInput(ordSubListMapperExtIns, ordMasterMapperIns);
        if (ordMasterMapperExt == null && ordMasterMapperExtIns != null) {
            ordMasterMapperExt = ordMasterMapperExtIns;
        }
    }

    static public JSONObject SaleInput(OrdMasterExt master, OrdSubListMapperExt ordSubListMapperExtIns, OrdMasterMapper ordMasterMapperIns) {
        initSaleInput(ordSubListMapperExtIns, ordMasterMapperIns);
        return SaleInput(master);
    }

    static public JSONObject SaleInputById(String id, OrdSubListMapperExt ordSubListMapperExtIns, OrdMasterMapper ordMasterMapperIns, OrdMasterMapperExt ordMasterMapperExtIns) {
        initSaleInputById(ordSubListMapperExtIns, ordMasterMapperIns, ordMasterMapperExtIns);
        return SaleInputById(id);
    }

    static public JSONObject SaleInput(OrdMasterExt master) {
        JSONObject result = new JSONObject();
        result.put("resultCode", Constants.NORMAL);
        if (!StringUtil.isEmpty(master.getErpSendStatus()) && master.getErpSendStatus().equals("10")) {
//            throw new ExceptionErrorData("指定订单已经发送成功,不可再次发送");
            return result;
        }
        OrdMaster masterSend = new OrdMaster();
        masterSend.setOrderId(master.getOrderId());
        masterSend.setErpSendStatus("10");
        try {
            //订单表
            List<OrdSubListExt> subList = ordSubListMapperExt.selectErpByOrder(master.getOrderId());
            //没有子订单数据
            if (subList == null || subList.size() == 0) throw new ExceptionErrorData("没有子订单数据");

            BigDecimal amountTotle = new BigDecimal(0);
            BigDecimal amountDiscount = new BigDecimal(0);
            List<SaleList> saleList = new ArrayList<>();
            for (OrdSubListExt sub : subList) {
                SaleList sale = new SaleList();
                sale.setOrderId(sub.getOrderId());
                sale.setSubOrderId(sub.getSubOrderId());
                sale.setErpSku(sub.getSkuId());
                sale.setPrice(String.valueOf(sub.getPrice()));
                BigDecimal priceDiscount = sub.getPriceShare();
                priceDiscount = priceDiscount == null || priceDiscount.equals(0) ? sub.getPrice() : priceDiscount;
                sale.setPriceDiscount(String.valueOf(priceDiscount));
                sale.setDeliverTime(formatTimeStampToYMDHMS(sub.getDeliverTime()));
                sale.setExpressName(changStringNull(sub.getExpressName()));
                sale.setExpressNo(changStringNull(sub.getExpressNo()));
                sale.setErpStoreId(sub.getErpStoreId());
                sale.setStoreDeliveryName(sub.getStoreDeliveryId());
                //是否内购活动Add 2016/11/17
                sale.setInnerBuy(sub.getInnerBuy());
                saleList.add(sale);
                amountTotle = amountTotle.add(sub.getPrice());
                amountDiscount = amountDiscount.add(priceDiscount);
            }

            //订单主表
            SaleMaster m = new SaleMaster();

            m.setOrderId(master.getOrderId());
            m.setMemberId(master.getTelephone());
            m.setMemberName(master.getMemberName());
            m.setAmountTotle(String.valueOf(amountTotle));
            m.setAmountDiscount(String.valueOf(amountDiscount));
//            m.setPayTime(formatTimeStampToYMDHMS(master.getPayTime()));
            m.setPayTime(formatTimeStampToYMDHMS(master.getOrderTime()));
            m.setQuantity(String.valueOf(saleList.size()));
//            m.setPreOrderId(master.getOrderId());
            m.setMessage(master.getMessage());
            m.setDeliveryFree(master.getDeliveryFree());
            m.setDeliceryFee(String.valueOf(master.getDeliveryFee()));
            m.setDeliveryType(master.getDeliverType());
            m.setPayType(master.getPayType());
//            m.setPayStatus(master.getPayStatus());
            m.setPayDeliveryType(master.getPayDeliveryType());
//            m.setDeliveryPostcode(master.getDeliveryPostcode());
//            m.setDistrictidProvince(master.getDistrictidProvince());
//            m.setDistrictidCity(master.getDistrictidCity());
//            m.setDistrictidDistrict(master.getDistrictidDistrict());
//            m.setDeliveryAddress(master.getDeliveryAddress());
//            m.setDeliveryContactor(master.getDeliveryContactor());
//            m.setDeliveryPhone(master.getDeliveryPhone());
//            m.setErpStoreId(master.getErpStoreId());
//            m.setStoreName(master.getStoreName());
            m.setSalerID(master.getInsertUserId());
            //20180209,添加优惠劵金额和id
            m.setCouponMemberId(changStringNull(master.getCouponId()));
            //stripTrailingZeros：去末尾多余的0 ；toPlainString：拒绝科学计数法
            if (master.getAmountCoupon()!=null&& !Objects.equals(master.getAmountCoupon(), new BigDecimal(0))){
                m.setWorth(master.getAmountCoupon().stripTrailingZeros().toPlainString());
            }else {
                m.setWorth("0");
            }

            Orders order = new Orders();
            ArrayOfSaleList ll = new ArrayOfSaleList();
            ll.setSaleList(saleList);
            order.setSaleMaster(m);
            order.setSaleList(ll);

            log.debug("ERP SaleInput input param:" + JSON.toJSONString(order));
            init();
            String resultCode = "";
            try {
                resultCode = soap.saleInput(getKeyOrderInput(order), order);
                System.out.println(resultCode);
            } catch (Exception e) {
                log.debug("ERP SaleInput 接口调用失败");
                destroy();
                throw e;
            }
            log.debug("ERP SaleInput result code:" + resultCode);
            if (resultCode.equals("00")) {
            } else if (resultCode.equals("11")) {
                throw new ExceptionNoPower("ERP数据库连接失败");
            } else if (resultCode.equals("12")) {
                throw new ExceptionBusiness("ERP数据库更新失败");
            } else if (resultCode.equals("13")) {
                masterSend.setErpSendStatus("13");
//                throw new ExceptionBusiness("ERP数据重复");
            } else if (resultCode.equals("21")) {
                throw new ExceptionErrorParam("ERP验证失败");
            } else {
                throw new ExceptionBusiness("ERP未知错误");
            }
        } catch (MalformedURLException e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", "ERP接口连接失败:" + e.getMessage());
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        try {
            masterSend.setUpdateTime(new Date());
            ordMasterMapper.updateByPrimaryKeySelective(masterSend);
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    static public JSONObject SaleInputById(String id) {
        JSONObject result = new JSONObject();
        try {
            if (StringUtil.isEmpty(id)) throw new ExceptionErrorParam("参数订单ID未指定");
            OrdMasterExt master = ordMasterMapperExt.selectSendById(id);
            if (master == null) {
                throw new ExceptionErrorData("指定订单不存在");
            }
            if (!StringUtil.isEmpty(master.getErpSendStatus()) && master.getErpSendStatus().equals("10"))
                throw new ExceptionErrorData("指定订单已经发送成功,不可再次发送");
            result = SaleInput(master);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }


    static public void initReturnInput(OrdSubListMapperExt ordSubListMapperExtIns, OrdReturnExchangeMapperExt ordReturnExchangeMapperExtIns) {
        if (ordSubListMapperExt == null && ordSubListMapperExtIns != null) {
            ordSubListMapperExt = ordSubListMapperExtIns;
        }
        if (ordReturnExchangeMapperExt == null && ordReturnExchangeMapperExtIns != null) {
            ordReturnExchangeMapperExt = ordReturnExchangeMapperExtIns;
        }
    }

    static public void initReturnInputById(OrdSubListMapperExt ordSubListMapperExtIns, OrdReturnExchangeMapperExt ordReturnExchangeMapperExtIns, OrdMasterMapperExt ordMasterMapperExtIns) {
        initReturnInput(ordSubListMapperExtIns, ordReturnExchangeMapperExtIns);
        if (ordMasterMapperExt == null && ordMasterMapperExtIns != null) {
            ordMasterMapperExt = ordMasterMapperExtIns;
        }
    }

    static public JSONObject ReturnInput(OrdMasterExt master, OrdSubListMapperExt ordSubListMapperExtIns, OrdReturnExchangeMapperExt ordReturnExchangeMapperExtIns) {
        initReturnInput(ordSubListMapperExtIns, ordReturnExchangeMapperExtIns);
        return ReturnInput(master);
    }

    static public JSONObject ReturnInputById(String id, OrdSubListMapperExt ordSubListMapperExtIns, OrdReturnExchangeMapperExt ordReturnExchangeMapperExtIns, OrdMasterMapperExt ordMasterMapperExtIns) {
        initReturnInputById(ordSubListMapperExtIns, ordReturnExchangeMapperExtIns, ordMasterMapperExtIns);
        return ReturnInputById(id);
    }

    static public JSONObject ReturnInput(OrdMasterExt master) {
        JSONObject result = new JSONObject();
        result.put("resultCode", Constants.NORMAL);
        OrdReturnExchange masterSend = new OrdReturnExchange();
        masterSend.setOrderId(master.getOrderId());
        masterSend.setErpSendStatus("10");
//        if(master.getErpSendStatus() != null && master.getErpSendStatus().equals("10")) {
////            throw new ExceptionErrorData("指定订单已经发送成功,不可再次发送");
//            result.put("resultMessage", "指定订单已经发送成功,不可再次发送");
//            return result;
//        }
        try {
            //子订单
            List<OrdSubListExt> subList = ordSubListMapperExt.selectErpReturnByOrder(master.getOrderId());
            //没有子订单数据
            if (subList == null || subList.size() == 0) {
//                throw new ExceptionErrorData("没有退货子订单数据");
                return result;
            }

            BigDecimal amountTotle = new BigDecimal(0);
            BigDecimal amountDiscount = new BigDecimal(0);
            List<SaleList> saleList = new ArrayList<>();
            for (OrdSubListExt sub : subList) {
                if (!StringUtil.isEmpty(sub.getErpSendStatus()) && sub.getErpSendStatus().equals("10")) {
                    //            throw new ExceptionErrorData("指定订单已经发送成功,不可再次发送");
                    return result;
                }
                SaleList sale = new SaleList();
                sale.setOrderId(sub.getOrderId());
                sale.setSubOrderId(sub.getSubOrderId());
                sale.setErpSku(sub.getSkuId());
                sale.setPrice(String.valueOf(sub.getPrice()));
                BigDecimal priceDiscount = sub.getPriceDiscount();
                priceDiscount = priceDiscount == null || priceDiscount.equals(0) ? sub.getPrice() : priceDiscount;
                sale.setPriceDiscount(String.valueOf(priceDiscount));
                sale.setDeliverTime(formatTimeStampToYMDHMS(sub.getDeliverTime()));
                sale.setExpressName(changStringNull(sub.getExpressName()));
                sale.setExpressNo(changStringNull(sub.getExpressNo()));
                sale.setErpStoreId(sub.getErpStoreId());
                sale.setStoreDeliveryName(sub.getStoreDeliveryId());
                //是否内购活动Add 2016/11/17
                sale.setInnerBuy(sub.getInnerBuy());
                saleList.add(sale);
                amountTotle = amountTotle.add(sub.getPrice());
                amountDiscount = amountDiscount.add(priceDiscount);
            }

            //订单主表
            SaleMaster m = new SaleMaster();
            m.setOrderId(master.getOrderId());
            m.setMemberId(master.getTelephone());
            m.setMemberName(master.getMemberName());
            m.setAmountTotle(String.valueOf(amountTotle));
            m.setAmountDiscount(String.valueOf(amountDiscount));
//            m.setPayTime(formatTimeStampToYMDHMS(master.getPayTime()));
            m.setPayTime(formatTimeStampToYMDHMS(subList.get(subList.size() - 1).getUpdateTime()));
            m.setQuantity(String.valueOf(saleList.size()));
//            m.setPreOrderId(master.getOrderId());
            m.setMessage(master.getMessage());
            m.setDeliveryFree(master.getDeliveryFree());
            m.setDeliceryFee(String.valueOf(master.getDeliveryFee()));
            m.setDeliveryType(master.getDeliverType());
            m.setPayType(master.getPayType());
//            m.setPayStatus(master.getPayStatus());
            m.setPayDeliveryType(master.getPayDeliveryType());
//            m.setDeliveryPostcode(master.getDeliveryPostcode());
//            m.setDistrictidProvince(master.getDistrictidProvince());
//            m.setDistrictidCity(master.getDistrictidCity());
//            m.setDistrictidDistrict(master.getDistrictidDistrict());
//            m.setDeliveryAddress(master.getDeliveryAddress());
//            m.setDeliveryContactor(master.getDeliveryContactor());
//            m.setDeliveryPhone(master.getDeliveryPhone());
//            m.setErpStoreId(master.getErpStoreId());
//            m.setStoreName(master.getStoreName());
            m.setSalerID(master.getInsertUserId());

            Orders order = new Orders();
            ArrayOfSaleList ll = new ArrayOfSaleList();
            ll.setSaleList(saleList);
            order.setSaleMaster(m);
            order.setSaleList(ll);

            log.debug("ERP ReturnInput input param:" + JSON.toJSONString(order));
            init();
            String resultCode = "";
            try {
                resultCode = soap.returnInput(getKeyOrderInput(order), order);
            } catch (Exception e) {
                log.debug("ERP ReturnInput 接口调用失败");
                destroy();
                throw e;
            }
            log.debug("ERP ReturnInput result code:" + resultCode);
            if (resultCode.equals("00")) {
            } else if (resultCode.equals("11")) {
                throw new ExceptionNoPower("ERP数据库连接失败");
            } else if (resultCode.equals("12")) {
                throw new ExceptionBusiness("ERP数据库更新失败");
            } else if (resultCode.equals("13")) {
                masterSend.setErpSendStatus("13");
//                throw new ExceptionBusiness("ERP数据重复");
            } else if (resultCode.equals("21")) {
                throw new ExceptionErrorParam("ERP验证失败");
            } else {
                throw new ExceptionBusiness("ERP未知错误");
            }
        } catch (MalformedURLException e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", "ERP接口连接失败:" + e.getMessage());
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            masterSend.setErpSendStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        try {
            masterSend.setUpdateTime(new Date());
            ordReturnExchangeMapperExt.updateSendById(masterSend);
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    static public JSONObject ReturnInputById(String id) {
        JSONObject result = new JSONObject();
        try {
            if (StringUtil.isEmpty(id)) throw new ExceptionErrorParam("参数订单ID未指定");
            OrdMasterExt master = ordMasterMapperExt.selectSendReturnById(id);
            if (master == null) {
                throw new ExceptionErrorData("指定订单不存在或者没有未发送的退货信息");
            }
            result = ReturnInput(master);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    static public void initReturnInputBySubOrderId(OrdSubListMapperExt ordSubListMapperExtIns, OrdReturnExchangeMapperExt ordReturnExchangeMapperExtIns, OrdMasterMapperExt ordMasterMapperExtIns) {
        initReturnInputById(ordSubListMapperExtIns, ordReturnExchangeMapperExtIns, ordMasterMapperExtIns);

    }

    static public JSONObject ReturnInputBySubOrderId(String id, OrdSubListMapperExt ordSubListMapperExtIns, OrdReturnExchangeMapperExt ordReturnExchangeMapperExtIns, OrdMasterMapperExt ordMasterMapperExtIns) {
        initReturnInputBySubOrderId(ordSubListMapperExtIns, ordReturnExchangeMapperExtIns, ordMasterMapperExtIns);
        return ReturnInputBySubOrderId(id);
    }

    /**
     * 发送指定的退货子订单
     */
    static public JSONObject ReturnInputBySubOrderId(String id) {
        JSONObject result = new JSONObject();
        result.put("resultCode", Constants.NORMAL);
        try {
            if (StringUtil.isEmpty(id)) throw new ExceptionErrorParam("参数子订单ID未指定");
            //子订单
            OrdSubListExt sub = ordSubListMapperExt.selectErpReturnBySubOrder(id);
            //没有子订单数据
            if (sub == null) {
//                throw new ExceptionErrorData("指定的退货子订单不存在");
                return result;
            }
            if (!StringUtil.isEmpty(sub.getErpSendStatus()) && sub.getErpSendStatus().equals("10")) {
                //            throw new ExceptionErrorData("指定订单已经发送成功,不可再次发送");
                return result;
            }
            result = ReturnInputById(sub.getOrderId());
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    static public void initBankUpdate(OrdTransferListMapper ordTransferListMapperIns) {
        if (ordTransferListMapper == null && ordTransferListMapperIns != null) {
            ordTransferListMapper = ordTransferListMapperIns;
        }
    }

    static public JSONObject BankUpdate(OrdTransferList master, OrdTransferListMapper ordTransferListMapperIns) {
        initBankUpdate(ordTransferListMapperIns);
        return BankUpdate(master);
    }

    static public JSONObject BankUpdateById(String id, OrdTransferListMapper ordTransferListMapperIns) {
        initBankUpdate(ordTransferListMapperIns);
        return BankUpdateById(id);
    }

    static public JSONObject BankUpdate(OrdTransferList master) {
        JSONObject result = new JSONObject();
        result.put("resultCode", Constants.NORMAL);
        String status = master.getTransferStatus();
        if (StringUtil.isEmpty(status)) {
//            throw new ExceptionErrorData("状态不正确");
            return result;
        }
        //不是已发货或者已收货
        if ((!status.equals("31")) && (!status.equals("32"))) {
//            throw new ExceptionErrorData("状态不正确");
            return result;
        }
        OrdTransferList masterSend = new OrdTransferList();
        masterSend.setOrderTransferId(master.getOrderTransferId());
        //已发货
        if (status.equals("31")) {
            if (master.getErpSendStatusDelivery() != null && master.getErpSendStatusDelivery().equals("10")) {
//            throw new ExceptionErrorData("已经发送");
                return result;
            }
            masterSend.setErpSendStatusDelivery("10");
        } else {
            //已收货
            if (master.getErpSendStatusReceive() != null && master.getErpSendStatusReceive().equals("10")) {
//            throw new ExceptionErrorData("已经发送");
                return result;
            }
            masterSend.setErpSendStatusReceive("10");
        }
        try {
            Bank bank = new Bank();
            bank.setSendStoreId(master.getDispatchStore());
            bank.setTakeStoreId(master.getApplyStore());
            bank.setErpSku(master.getSku());
            bank.setQuantity(master.getQuantity().toString());
            //发货
            if (status.equals("31")) {
                bank.setBankTime(formatTimeStampToYMDHMS(master.getDeliveryTime()));
                bank.setSalerID(master.getDeliveryUser());
                bank.setStoreKind("1");
            } else {
                //收货
                bank.setBankTime(formatTimeStampToYMDHMS(master.getReceiveTime()));
                bank.setSalerID(master.getReceiveUser());
                bank.setStoreKind("2");
            }

            Banktransfer m = new Banktransfer();
            m.setBank(bank);
            log.debug("ERP BankUpdate input param:" + JSON.toJSONString(bank));
            init();
            String resultCode = "";
            try {
                resultCode = soap.bankUpdate(getKeyBankUpdate(bank), m);
            } catch (Exception e) {
                log.debug("ERP BankUpdate 接口调用失败");
                destroy();
                throw e;
            }
            log.debug("ERP BankUpdate result code:" + resultCode);
            if (resultCode.equals("00")) {
            } else if (resultCode.equals("11")) {
                throw new ExceptionNoPower("ERP数据库连接失败");
            } else if (resultCode.equals("12")) {
                throw new ExceptionBusiness("ERP数据库更新失败");
            } else if (resultCode.equals("13")) {
                throw new ExceptionBusiness("ERP数据重复");
            } else if (resultCode.equals("21")) {
                throw new ExceptionErrorParam("ERP验证失败");
            } else {
                throw new ExceptionBusiness("ERP未知错误");
            }
        } catch (MalformedURLException e) {
            // 若通讯失败,记录失败原因及通讯信息
            //已发货
            if (status.equals("31")) {
                masterSend.setErpSendStatusDelivery("20");
            } else {
                masterSend.setErpSendStatusReceive("20");
            }
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", "ERP接口连接失败:" + e.getMessage());
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            if (status.equals("31")) {
                masterSend.setErpSendStatusDelivery("20");
            } else {
                masterSend.setErpSendStatusReceive("20");
            }
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        try {
            masterSend.setUpdateTime(new Date());
            ordTransferListMapper.updateByPrimaryKeySelective(masterSend);
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    static public JSONObject BankUpdateById(String id) {
        JSONObject result = new JSONObject();
        try {
            if (StringUtil.isEmpty(id)) throw new ExceptionErrorParam("参数订单ID未指定");
            OrdTransferList master = ordTransferListMapper.selectByPrimaryKey(id);
            if (master == null) {
                throw new ExceptionErrorData("指定调货信息不存在");
            }
            result = BankUpdate(master);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }

    static public void initYTOUpdate(OrdLogisticStatusMapper ordLogisticStatusMapperIns) {
        if (ordLogisticStatusMapper == null && ordLogisticStatusMapperIns != null) {
            ordLogisticStatusMapper = ordLogisticStatusMapperIns;
        }
    }

    static public JSONObject YTOUpdate(OrdLogisticStatus ordLogisticStatus, OrdLogisticStatusMapper ordLogisticStatusMapperIns) {
        initYTOUpdate(ordLogisticStatusMapperIns);
        return YTOUpdate(ordLogisticStatus);
    }

    /**
     * 圆通快递状态更新接口
     *
     * @param ordLogisticStatus
     * @return
     */
    static public JSONObject YTOUpdate(OrdLogisticStatus ordLogisticStatus) {
        ordLogisticStatus.setErpSendStatus("10");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject result = new JSONObject();
        result.put("resultCode", Constants.NORMAL);
        // 物流号
        String txLogisticID = ordLogisticStatus.getTxLogisticId();
        // 物流状态
        String infoContent = ordLogisticStatus.getInfoContent();
        // 事件发生时间
        String acceptTime =  sdf.format(ordLogisticStatus.getAcceptTime());

        if (StringUtil.isEmpty(txLogisticID)) throw new ExceptionErrorParam("参数物流号未指定");
        if (StringUtil.isEmpty(infoContent)) throw new ExceptionErrorParam("参数物流状态未指定");
        if (StringUtil.isEmpty(acceptTime)) throw new ExceptionErrorParam("参数发生时间未指定");

        try {
            YTOStates ytoPushInfo = new YTOStates();
            // 物流号
            ytoPushInfo.setTxLogisticID(txLogisticID);
            // 运单号
            if (StringUtils.isNotBlank(ordLogisticStatus.getMailNo())) {
                ytoPushInfo.setMailNo(ordLogisticStatus.getMailNo());
            } else {
                ytoPushInfo.setMailNo("");
            }
            // 物流状态
            ytoPushInfo.setInfoContent(ordLogisticStatus.getInfoContent());
            // 事件发生时间
            ytoPushInfo.setAcceptTime(acceptTime);
            // 备注
            if (StringUtils.isNotBlank(ordLogisticStatus.getRemark())) {
                ytoPushInfo.setRemark(ordLogisticStatus.getRemark());
            } else {
                ytoPushInfo.setRemark("");
            }
            // 签收人
            if (StringUtils.isNotBlank(ordLogisticStatus.getSignedName())) {
                ytoPushInfo.setSignedName(ordLogisticStatus.getSignedName());
            } else {
                ytoPushInfo.setSignedName("");
            }
            // 操作人员
            if (StringUtils.isNotBlank(ordLogisticStatus.getDeliveryName())) {
                ytoPushInfo.setDeliveryName(ordLogisticStatus.getDeliveryName());
            } else {
                ytoPushInfo.setDeliveryName("");
            }
            // 联系方式
            if (StringUtils.isNotBlank(ordLogisticStatus.getContactInfo())) {
                ytoPushInfo.setContactInfo(ordLogisticStatus.getContactInfo());
            } else {
                ytoPushInfo.setContactInfo("");
            }

            YTO m = new YTO();
            m.setYtoPushInfo(ytoPushInfo);
            init();
            String resultCode = "";
            try {
                resultCode = soap.YTOUpdate(getYTOUpdate(m), m);
            } catch (Exception e) {
                System.out.print(e);
                log.debug("ERP YTOUpdate 接口调用失败");
                System.out.println("==============失败了==================");
                destroy();
                throw e;
            }
            log.debug("ERP YTOUpdate result code:" + resultCode);
            if (resultCode.equals("00")) {
                System.out.println("==============成功了==================");
            } else if (resultCode.equals("11")) {
                throw new ExceptionNoPower("ERP数据库连接失败");
            } else if (resultCode.equals("12")) {
                throw new ExceptionBusiness("ERP数据库更新失败");
            } else if (resultCode.equals("13")) {
                throw new ExceptionBusiness("ERP数据重复");
            } else if (resultCode.equals("21")) {
                throw new ExceptionErrorParam("ERP验证失败");
            } else {
                throw new ExceptionBusiness("ERP未知错误");
            }
        } catch (MalformedURLException e) {
            // 若通讯失败,记录失败原因及通讯信息
            ordLogisticStatus.setErpSendStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", "ERP接口连接失败:" + e.getMessage());
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            ordLogisticStatus.setErpSendStatus("20");
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        try {
            ordLogisticStatus.setUpdateTime(new Date());
            ordLogisticStatusMapper.updateByPrimaryKeySelective(ordLogisticStatus);
        } catch (Exception e) {
            // 若通讯失败,记录失败原因及通讯信息
            result.put("resultCode", Constants.FAIL);
            result.put("resultMessage", e.getMessage());
        }
        return result;
    }
}
