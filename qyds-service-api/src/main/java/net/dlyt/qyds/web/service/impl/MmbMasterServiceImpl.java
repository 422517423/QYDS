package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.mp.api.WxMpService;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.CouponMemberExt;
import net.dlyt.qyds.common.dto.ext.MmbGroupMemberExt;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.common.dto.ext.MmbSalerExt;
import net.dlyt.qyds.common.form.MmbMasterForm;
import net.dlyt.qyds.config.FileServerConfig;
import net.dlyt.qyds.dao.*;
import net.dlyt.qyds.dao.ext.*;
import net.dlyt.qyds.web.service.CouponMemberService;
import net.dlyt.qyds.web.service.MmbMasterService;
import net.dlyt.qyds.web.service.PrizeDrawService;
import net.dlyt.qyds.web.service.common.*;
import net.dlyt.qyds.web.service.common.ComCode;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by pancd on 16/7/27.
 */
@Service("mmbMasterService")
public class MmbMasterServiceImpl implements MmbMasterService {

    protected final Logger log = LoggerFactory.getLogger(MmbMasterServiceImpl.class);
    @Autowired
    private MmbMasterMapperExt mmbMasterMapperExt;
    @Autowired
    private MmbGroupMemberMapperExt mmbGroupMemberMapper;
    @Autowired
    private MmbGroupMapper mmbGroupMapper;
    @Autowired
    private MmbSalerMapperExt mmbSalerMapperExt;
    @Autowired
    private MmbMasterMapper mmbMasterMapper;
    @Autowired
    private MmbSalerMapper mmbSalerMapper;
    @Autowired
    private ErpCityMapper erpCityMapper;
    @Autowired
    private ErpProvinceMapper erpProvinceMapper;
    @Autowired
    private ErpDistrictMapper erpDistrictMapper;
    @Autowired
    private ErpCityMapperExt erpCityMapperExt;
    @Autowired
    private ErpProvinceMapperExt erpProvinceMapperExt;
    @Autowired
    private ErpDistrictMapperExt erpDistrictMapperExt;
    @Autowired
    private CouponMasterMapperExt couponMasterMapperExt;
    @Autowired
    private CouponMemberService couponMemberService;
    @Autowired
    private FileServerConfig fileServerConfig;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private PrizeDrawService prizeDrawService;
    // TODO: 2017/12/15 临时
    @Autowired
    private OrdMasterMapperExt ordMasterMapperExt;

    public List<MmbMasterExt> selectAll(MmbMasterExt ext) {
        return mmbMasterMapperExt.selectAll(ext);
    }

    public int getAllDataCount(MmbMasterExt ext) {
        return mmbMasterMapperExt.getAllDataCount(ext);
    }


    public JSONObject registerUser(MmbMaster sysUser) {
        JSONObject json = new JSONObject();
        try {

            // 获取信息
            if (!StringUtil.isEmpty(sysUser.getOpenId())) {
                MmbMaster master = new MmbMaster();
                master.setOpenId(sysUser.getOpenId());
                MmbMasterExt rMmbMaster = mmbMasterMapperExt.selectBySelective(master);
                if (rMmbMaster != null) {
                    throw new ExceptionErrorParam("微信号已经被注册");
                }
            }

            if (!StringUtil.isTelephone(sysUser.getTelephone())) {
                throw new ExceptionErrorParam("注册手机号码不正确");
            } else {

                MmbMaster master = new MmbMaster();
                master.setTelephone(sysUser.getTelephone());
                MmbMasterExt rMmbMaster = mmbMasterMapperExt.selectBySelective(master);
                if (rMmbMaster != null) {
                    throw new ExceptionErrorParam("手机号已经被注册");
                }
            }

            json = insertSelective(sysUser);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    @Override
    public JSONObject changePassword(MmbMaster sysUser) {
        JSONObject json = new JSONObject();
        try {
            if (!StringUtil.isTelephone(sysUser.getTelephone())) {
                throw new ExceptionErrorParam("注册手机号码不正确");
            }

            MmbMaster master = new MmbMaster();
            master.setTelephone(sysUser.getTelephone());
            MmbMasterExt rMmbMaster = mmbMasterMapperExt.selectBySelective(master);
            if (rMmbMaster == null || !"0".equals(rMmbMaster.getDeleted())) {
                throw new ExceptionErrorParam("请输入正确的手机号码");
            }

            MmbMaster record = new MmbMaster();
            record.setMemberId(rMmbMaster.getMemberId());
            record.setPassword(sysUser.getPassword());
            mmbMasterMapper.updateByPrimaryKeySelective(record);

            // 获取信息
            MmbMaster user = mmbMasterMapper.selectByPrimaryKey(rMmbMaster.getMemberId());
            if (user == null) {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "用户信息不存在");
            } else {
                json.put("data", user);
                json.put("resultCode", Constants.NORMAL);
            }

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    public JSONObject insertSelective(MmbMaster user) {
        JSONObject json = new JSONObject();
        json.put("resultCode", Constants.NORMAL);
        try {
            String randomUUID = UUID.randomUUID().toString();
            user.setMemberId(randomUUID);
            user.setIsValid("0");
            user.setDeleted("0");
            user.setPoint(0);
            user.setAllPoint(0);
            user.setInsertUserId(Constants.ORGID);
            user.setUpdateUserId(Constants.ORGID);
            user.setInsertTime(new Date());
            user.setUpdateTime(new Date());

            user = getAddressInfo(user);

            int retInsert = mmbMasterMapper.insertSelective(user);
            json.put("data", user);
            if (retInsert == 1) {
                try {
                    // TODO @崔 注册券发送调用在此处
                    // 获取注册劵
                    CouponMaster regCoupon = couponMasterMapperExt.selectRegisterCoupon();
                    if (regCoupon == null) {
                        throw new ExceptionErrorData("注册劵不存在!");
                    }
                    if (regCoupon.getSendStartTime().compareTo(new Date()) > 0 || regCoupon.getSendEndTime().compareTo(new Date()) < 0) {
                        throw new ExceptionErrorData("当前不在发放期间内!");
                    }
                    CouponMemberExt form1 = new CouponMemberExt();
                    form1.setMemberId(user.getMemberId());
                    couponMemberService.addRegisterCouponsForUser(form1, regCoupon);

                    if (null != user.getBirthdate() && checkBirthdate(user.getBirthdate())) {
                        // 获取生日劵
                        if (StringUtil.isEmpty(user.getMemberLevelId())) {
                            throw new ExceptionErrorData("会员级别不存在，无法取得生日劵!");
                        }
                        CouponMaster coupon = couponMasterMapperExt.selectBirthdayCoupon(user.getMemberLevelId());
                        if (coupon == null) {
                            throw new ExceptionErrorData("生日券不存在!");
                        }
                        if (coupon.getSendStartTime().compareTo(new Date()) > 0 || coupon.getSendEndTime().compareTo(new Date()) < 0) {
                            throw new ExceptionErrorData("当前不在发放期间内!");
                        }
                        CouponMemberExt form = new CouponMemberExt();
                        form.setMemberId(user.getMemberId());
                        Calendar c = Calendar.getInstance();
                        int nowYear = c.get(Calendar.YEAR);
                        c.setTime(user.getBirthdate());
                        c.set(Calendar.YEAR, nowYear);
                        form.setStartTime(c.getTime());
                        couponMemberService.addBirthdayCouponsForUser(form, coupon);
                    }
                } catch (Exception e) {
                    System.out.println("发放注册劵或生日劵失败,原因:" + e.getMessage());
                }

                // 发放注册抽奖活动
                try {
                    prizeDrawService.addPrizeDrawOppo(user.getMemberId(), null, ComCode.PrizeDrawOppoType.REGISTER, null);
                } catch (Exception e) {
                    System.out.println("发放注册抽奖活动失败,原因:" + e.getMessage());
                }

                // 写入ERP
//                json = updateMasterToERP(user.getMemberId());
//                json = ErpSendUtil.getInstance().VIPUpdateById(user.getMemberId());
                ErpSendUtil.VIPUpdate(user, mmbMasterMapperExt, mmbMasterMapper);
            } else {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "注册失败,服务器异常");
            }
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

//    private JSONObject updateMasterToERP(String memberId) {
//
//        JSONObject json = new JSONObject();
//
//        // 获取信息
//        MmbMaster user = mmbMasterMapper.selectByPrimaryKey(memberId);
//        if (user == null) {
//            json.put("resultCode", Constants.FAIL);
//            json.put("resultMessage", "用户信息不存在");
//        } else {
//            json.put("data", user);
//            json.put("resultCode", Constants.NORMAL);
//        }
//
//        MmbMaster master = new MmbMaster();
//        master.setMemberId(memberId);
//        master.setErpSendStatus("20");
//        try {
//            Vip vip = new Vip();
//
//            vip.setMemberCode(user.getTelephone());
//            vip.setMemberName(user.getMemberName());
//            // 1：男   2：女
//            String sexName = "";
//            if ("1".equals(user.getSex())) {
//                sexName = "男";
//            } else if ("2".equals(user.getSex())) {
//                sexName = "女";
//            }
//            vip.setSexName(sexName);
//            vip.setMobil(user.getTelephone());
//            vip.setBirthday(DataUtils.formatTimeStampToYMD(user.getBirthdate()));
//            vip.setProvinceName(user.getProvinceName());
//            vip.setCityName(user.getCityName());
//            vip.setDistrictName(user.getDistrictName());
//            vip.setEmail(user.getEmail());
////            vip.setStoreCode("");
////            vip.setStoreName("");
//            vip.setSellerName(user.getReferrerId());
//            vip.setAddress(user.getAddress());
//            vip.setPostCode(user.getPostCode());
//            vip.setIncome(user.getIncome());
//            vip.setRegistTime(DataUtils.formatTimeStampToYMD(user.getInsertTime()));
//            vip.setMemberGrade(user.getMemberLevelId());
////            vip.setMemberCodeAlter("");
//
//            BaseDate date = new BaseDate();
//            date.setVip(vip);
//            String result = soap.vipUpdate(getKeyVIPUpdate(date), date);
//            log.debug("ERP vipUpdate result code:"+result+",param:"+ JSON.toJSONString(date));
//            master.setUpdateTime(new Date());
//            if (result.equals("00")) {
//                master.setErpSendStatus("10");
//            }
//            mmbMasterMapper.updateByPrimaryKeySelective(master);
//            if (result.equals("00")) {
//            } else if (result.equals("11")) {
//                throw new ExceptionErrorData("ERP数据库连接失败");
//            } else if (result.equals("12")) {
//                throw new ExceptionErrorData("ERP数据库更新失败");
//            } else if (result.equals("21")) {
//                throw new ExceptionErrorData("ERP验证失败");
//            } else {
//                throw new ExceptionErrorData("ERP未知错误");
//            }
//        } catch (Exception e) {
//            System.out.println("用户信息同步ERP失败,原因:"+e.getMessage());
//        }
//
//        return json;
//    }

    public JSONObject selectByPrimaryKey(String memberId) {
        JSONObject json = new JSONObject();
        if (!StringUtils.isEmpty(memberId)) {
            MmbMaster rMmbMaster = mmbMasterMapper.selectByPrimaryKey(memberId);
            if (rMmbMaster == null) {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "用户信息不存在");
            } else {
                json.put("data", rMmbMaster);
                json.put("resultCode", Constants.NORMAL);
            }
        } else {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", "缺少主键参数");
        }
        return json;
    }

    public JSONObject updateByPrimaryKeySelective(MmbMaster user) {
        JSONObject json = new JSONObject();
        json.put("resultCode", Constants.NORMAL);
        if (user != null && !StringUtils.isEmpty(user.getMemberId())) {

            MmbMaster master = mmbMasterMapper.selectByPrimaryKey(user.getMemberId());
            if (master == null) {
                MmbSaler mmbSaler = mmbSalerMapper.selectByPrimaryKey(user.getMemberId());
                master = new MmbMaster();
                BeanUtils.copyProperties(mmbSaler, master);
            }

            if (master == null || !"0".equals(master.getDeleted())) {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "用户信息不存在");
                return json;
            } else if (null == master.getBirthdate() && null != user.getBirthdate()) {
                if (checkBirthdate(user.getBirthdate())) {
                    // 生日是今天
                    try {
                        // 获取生日劵
                        if (StringUtil.isEmpty(user.getMemberLevelId())) {
                            throw new ExceptionErrorData("会员级别不存在，无法取得生日劵!");
                        }
                        CouponMaster coupon = couponMasterMapperExt.selectBirthdayCoupon(user.getMemberLevelId());
                        if (coupon == null) {
                            throw new ExceptionErrorData("生日券不存在!");
                        }
                        if (coupon.getSendStartTime().compareTo(new Date()) > 0 || coupon.getSendEndTime().compareTo(new Date()) < 0) {
                            throw new ExceptionErrorData("当前不在发放期间内!");
                        }
                        CouponMemberExt form = new CouponMemberExt();
                        form.setMemberId(user.getMemberId());
                        Calendar c = Calendar.getInstance();
                        int nowYear = c.get(Calendar.YEAR);
                        c.setTime(user.getBirthdate());
                        c.set(Calendar.YEAR, nowYear);
                        form.setStartTime(c.getTime());
                        couponMemberService.addBirthdayCouponsForUser(form, coupon);
                    } catch (Exception e) {

                    }
                }
            }

            user = getAddressInfo(user);
            //修改电话号码
            //修改电话重复检查
            if (!StringUtil.isEmpty(user.getTelephone()) && !StringUtil.isEmpty(master.getTelephone())
                    && !master.getTelephone().equals(user.getTelephone())) {
                MmbMaster mmbMasterCheck = new MmbMaster();
                mmbMasterCheck.setTelephone(user.getTelephone());
                int hvCount = mmbMasterMapperExt.getCountBySelective(mmbMasterCheck);
                if (hvCount > 0) {
//                    throw new ExceptionErrorParam("该号码已注册");
                    json.put("resultCode", Constants.FAIL);
                    json.put("resultMessage", "该号码已注册");
                    return json;
                }
                user.setOldphone(master.getTelephone());
            }

            int ret = mmbMasterMapper.updateByPrimaryKeySelective(user);
            boolean isSendErp = true;
            if (ret == 0) {
                MmbSaler record = new MmbSaler();
                BeanUtils.copyProperties(user, record);
                ret = mmbSalerMapper.updateByPrimaryKeySelective(record);
                isSendErp = false;
            }
            if (ret == 1) {
                // 写入ERP
//                json = updateMasterToERP(user.getMemberId());
//                json = ErpSendUtil.getInstance().VIPUpdateById(user.getMemberId());
//                ErpSendUtil.VIPUpdateById(user.getMemberId(),mmbMasterMapperExt,mmbMasterMapper);
                String memberId = user.getMemberId();
                user = mmbMasterMapper.selectByPrimaryKey(memberId);
                if (user == null) {
                    MmbSaler mmbSaler = mmbSalerMapper.selectByPrimaryKey(memberId);
                    user = new MmbMaster();
                    BeanUtils.copyProperties(mmbSaler, user);
                }
                if (user == null) {
                    json.put("resultCode", Constants.FAIL);
                    json.put("resultMessage", "用户信息不存在");
                } else {

                    MmbMasterExt rMmbMaster = new MmbMasterExt();
                    BeanUtils.copyProperties(user, rMmbMaster);
                    //内购组获取
                    rMmbMaster.setIsSaller("0");
                    MmbGroupMember mmbGroupMember = new MmbGroupMember();
                    mmbGroupMember.setMemberId(rMmbMaster.getMemberId());
                    List<MmbGroupMemberExt> list = mmbGroupMemberMapper.select(mmbGroupMember);
                    for (MmbGroupMemberExt mmbGroupMemberExt : list) {
                        String groupId = mmbGroupMemberExt.getGroupId();
                        if ("0b2df2d0-af2c-49e0-ad24-6c0447f634f3".equals(groupId)
                                || "8822a72b-3e37-43cb-8413-0f5c41a77b63".equals(groupId)) {
                            rMmbMaster.setIsSaller("1");
                            break;
                        }
                    }

                    json.put("data", rMmbMaster);
                    json.put("resultCode", Constants.NORMAL);
//                json = updateMasterToERP(user.getMemberId());
//                json = ErpSendUtil.getInstance().VIPUpdateById(user.getMemberId());
                    if (isSendErp) {
                        ErpSendUtil.VIPUpdate(user, mmbMasterMapperExt, mmbMasterMapper);
                    }
                    //ErpSendUtil.VIPUpdate(user,mmbMasterMapperExt,mmbMasterMapper);
                }
            } else {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "ERP数据连接异常");
            }
        } else {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", "用户信息不存在");
        }
        return json;
    }

    /**
     * 判断近期是否为会员生日
     *
     * @param date
     * @return
     */
    private Boolean checkBirthdate(Date date) {

        if (null == date) {
            return false;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month_bd = cal.get(Calendar.MONTH);
        int day_bd = cal.get(Calendar.DAY_OF_MONTH);

        boolean result = false;
        int max = Constants.DISTRIBUTE_BIRTHDAY_COUPON_INTERVAL;
        for (int i = 0; i <= max; i++) {
            Calendar current = Calendar.getInstance();
            current.setTime(new Date());
            if (i > 0) {
                current.add(Calendar.DATE, i);
            }
            int month = current.get(Calendar.MONTH);
            int day = current.get(Calendar.DAY_OF_MONTH);

            if (month_bd == month && day_bd == day) {
                result = true;
                break;
            }
        }

        return result;
    }

    public JSONObject deleteByPrimaryKey(MmbMaster user) {
        JSONObject json = new JSONObject();

        try {

            if (user == null || StringUtils.isEmpty(user.getMemberId())) {
                throw new ExceptionErrorParam("缺少主键参数");
            }

            user.setIsValid("1");
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String str = sdf.format(date);
            user.setUnUsedTime(str);
            int ret = mmbMasterMapper.updateByPrimaryKeySelective(user);
            MmbMaster mmbMaster = mmbMasterMapper.selectByPrimaryKey(user.getMemberId());
            user.setTelephone(mmbMaster.getTelephone());
            ErpSendUtil.VIPUsedUpdate(user);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }

        return json;
    }

    public JSONObject undeleteByPrimaryKey(MmbMaster user) {
        JSONObject json = new JSONObject();

        try {

            if (user == null || StringUtils.isEmpty(user.getMemberId())) {
                throw new ExceptionErrorParam("缺少主键参数");
            }

            user.setIsValid("0");
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String str = sdf.format(date);
            user.setUnUsedTime(str);
            int ret = mmbMasterMapper.updateByPrimaryKeySelective(user);
            MmbMaster mmbMaster = mmbMasterMapper.selectByPrimaryKey(user.getMemberId());
            user.setTelephone(mmbMaster.getTelephone());
            ErpSendUtil.VIPUsedUpdate(user);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }

        return json;
    }

    public JSONObject selectBySelective(MmbMaster user) {
        JSONObject json = new JSONObject();
        try {
            if (user != null) {
                // 获取信息
                MmbMasterExt rMmbMaster = mmbMasterMapperExt.selectBySelective(user);
                // 如果获取不到去店员表中去获取
                if (rMmbMaster == null) {
                    MmbSaler mmbSaler = new MmbSaler();
                    mmbSaler.setMemberId(user.getMemberId());
                    MmbSalerExt ext = mmbSalerMapperExt.selectBySelective(mmbSaler);
                    rMmbMaster = new MmbMasterExt();
                    BeanUtils.copyProperties(ext, rMmbMaster);

                    //获取用户的组织信息
                    MmbSalerExt mmbSalerExt = mmbSalerMapperExt.getUserOrgInfo(ext.getTelephone());
                    rMmbMaster.setOperate(mmbSalerExt.getOperate());
                    rMmbMaster.setStoreid(mmbSalerExt.getStoreid());
                    rMmbMaster.setStoresubid(mmbSalerExt.getStoresubid());
                }

                if (rMmbMaster == null) {
                    json.put("resultCode", Constants.FAIL);
                    json.put("resultMessage", "用户信息不存在");
                } else {
                    json.put("data", rMmbMaster);
                    json.put("resultCode", Constants.NORMAL);
                }
            } else {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "用户信息不存在");
            }
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }

        return json;
    }

    public JSONObject getCurrentPoints(MmbMaster sysUser) {
        JSONObject json = new JSONObject();
        if (sysUser != null) {
            // 获取信息
            MmbMasterExt rMmbMaster = mmbMasterMapperExt.selectBySelective(sysUser);
            if (rMmbMaster == null) {
                json.put("resultCode", Constants.FAIL_NO_DATA);
                json.put("resultMessage", "请输入正确的用户信息");
            } else {

                //内购组获取
                rMmbMaster.setIsSaller("0");
                MmbGroupMember mmbGroupMember = new MmbGroupMember();
                mmbGroupMember.setMemberId(rMmbMaster.getMemberId());
                List<MmbGroupMemberExt> list = mmbGroupMemberMapper.select(mmbGroupMember);
                for (MmbGroupMemberExt mmbGroupMemberExt : list) {
                    String groupId = mmbGroupMemberExt.getGroupId();
                    if ("0b2df2d0-af2c-49e0-ad24-6c0447f634f3".equals(groupId)
                            || "8822a72b-3e37-43cb-8413-0f5c41a77b63".equals(groupId)) {
                        rMmbMaster.setIsSaller("1");
                        break;
                    }
                }

                json.put("data", rMmbMaster);
                json.put("resultCode", Constants.NORMAL);
            }
        } else {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", "用户信息不存在");
        }
        return json;
    }

    public JSONObject loginSelective(MmbMaster sysUser) {
        JSONObject json = new JSONObject();
        if (sysUser != null) {

            if (!StringUtil.isEmpty(sysUser.getTelephone()) && !StringUtil.isEmpty(sysUser.getPassword())) {
                MmbMaster record = new MmbMaster();
                record.setTelephone(sysUser.getTelephone());
                MmbMasterExt rMmbMaster = mmbMasterMapperExt.selectBySelective(record);

                if (rMmbMaster == null || !"0".equals(rMmbMaster.getDeleted())) {
                    json.put("resultCode", Constants.FAIL_NO_DATA);
                    json.put("resultMessage", "请输入正确的用户信息");
                } else if (null == rMmbMaster.getPassword() || StringUtil.isEmpty(rMmbMaster.getPassword())) {
                    json.put("resultCode", Constants.FAIL_NO_DATA);
                    json.put("resultMessage", "请重新设置登录密码");
                } else if (sysUser.getPassword().toLowerCase().equals(rMmbMaster.getPassword().toLowerCase())) {
                    //内购组获取
                    rMmbMaster.setIsSaller("0");
                    MmbGroupMember mmbGroupMember = new MmbGroupMember();
                    mmbGroupMember.setMemberId(rMmbMaster.getMemberId());
                    List<MmbGroupMemberExt> list = mmbGroupMemberMapper.select(mmbGroupMember);
                    for (MmbGroupMemberExt mmbGroupMemberExt : list) {
                        String groupId = mmbGroupMemberExt.getGroupId();
                        if ("0b2df2d0-af2c-49e0-ad24-6c0447f634f3".equals(groupId)
                                || "8822a72b-3e37-43cb-8413-0f5c41a77b63".equals(groupId)) {
                            rMmbMaster.setIsSaller("1");
                            break;
                        }
                    }
                    json.put("data", rMmbMaster);
                    json.put("resultCode", Constants.NORMAL);
                } else {
                    json.put("resultCode", Constants.FAIL_NO_DATA);
                    json.put("resultMessage", "请输入正确的登录密码");
                }
            } else {
                // 获取信息
                MmbMasterExt rMmbMaster = mmbMasterMapperExt.selectBySelective(sysUser);
                if (rMmbMaster == null) {
                    json.put("resultCode", Constants.FAIL_NO_DATA);
                    json.put("resultMessage", "请输入正确的用户信息");
                } else {
                    //内购组获取
                    rMmbMaster.setIsSaller("0");
                    MmbGroupMember mmbGroupMember = new MmbGroupMember();
                    mmbGroupMember.setMemberId(rMmbMaster.getMemberId());
                    List<MmbGroupMemberExt> list = mmbGroupMemberMapper.select(mmbGroupMember);
                    for (MmbGroupMemberExt mmbGroupMemberExt : list) {
                        String groupId = mmbGroupMemberExt.getGroupId();
                        if ("0b2df2d0-af2c-49e0-ad24-6c0447f634f3".equals(groupId)
                                || "8822a72b-3e37-43cb-8413-0f5c41a77b63".equals(groupId)) {
                            rMmbMaster.setIsSaller("1");
                            break;
                        }
                    }
                    json.put("data", rMmbMaster);
                    json.put("resultCode", Constants.NORMAL);
                }
            }
        } else {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", "用户信息不存在");
        }
        return json;
    }

    public JSONObject bindingSelective(MmbMaster sysUser) {
        JSONObject json = new JSONObject();

        try {

            if (sysUser != null) {
                // 获取信息
                // 微信号已经被绑定
                MmbMaster user = new MmbMaster();
                user.setOpenId(sysUser.getOpenId());
                user.setDeleted("0");
                user.setIsValid("0");
                MmbMasterExt rMmbMaster = mmbMasterMapperExt.selectBySelective(user);
                if (rMmbMaster != null) {
                    throw new ExceptionErrorData("微信号已经被绑定");
                }

                // 电话号码已经被绑定
                user = new MmbMaster();
                user.setTelephone(sysUser.getTelephone());
                user.setDeleted("0");
                user.setIsValid("0");
                rMmbMaster = mmbMasterMapperExt.selectBySelective(user);

                if (rMmbMaster == null) {
                    throw new ExceptionErrorData("手机账号不存在");
                } else if (rMmbMaster != null && !StringUtil.isEmpty(rMmbMaster.getOpenId())) {
                    throw new ExceptionErrorData("手机账号已绑定微信账号");
                }

                rMmbMaster.setOpenId(sysUser.getOpenId());
                int ret = mmbMasterMapper.updateByPrimaryKeySelective(rMmbMaster);
                if (ret == 1) {

                    //内购组获取
                    rMmbMaster.setIsSaller("0");
                    MmbGroupMember mmbGroupMember = new MmbGroupMember();
                    mmbGroupMember.setMemberId(rMmbMaster.getMemberId());
                    List<MmbGroupMemberExt> list = mmbGroupMemberMapper.select(mmbGroupMember);
                    for (MmbGroupMemberExt mmbGroupMemberExt : list) {
                        String groupId = mmbGroupMemberExt.getGroupId();
                        if ("0b2df2d0-af2c-49e0-ad24-6c0447f634f3".equals(groupId)
                                || "8822a72b-3e37-43cb-8413-0f5c41a77b63".equals(groupId)) {
                            rMmbMaster.setIsSaller("1");
                            break;
                        }
                    }

                    json.put("data", rMmbMaster);
                    json.put("resultCode", Constants.NORMAL);
                } else {
                    throw new ExceptionErrorData("服务器异常,绑定出错");
                }

            } else {
                throw new ExceptionErrorData("绑定出错,参数异常");
            }

        } catch (Exception e) {
            json.put("resultMessage", e.getMessage());
            json.put("resultCode", Constants.FAIL);
        }

        return json;
    }


    private JSONObject selectResponseMmbMaster(String memberId) {
        JSONObject json = new JSONObject();
        try {
            // 获取信息
            MmbMaster rMmbMaster = mmbMasterMapper.selectByPrimaryKey(memberId);
            if (rMmbMaster == null) {
                json.put("resultCode", Constants.FAIL);
            } else {
                json.put("data", rMmbMaster);
                json.put("resultCode", Constants.NORMAL);
            }
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    public JSONObject getAddressList(MmbMaster mmbMaster) {

        JSONObject json = new JSONObject();
        try {

            if (mmbMaster == null || (StringUtil.isEmpty(mmbMaster.getProvinceCode()) && StringUtil.isEmpty(mmbMaster.getCityCode()))) {
                // 取得省
                List<ErpProvince> list = erpProvinceMapperExt.queryAllProvince();
                json.put("data", list);
            } else if (!StringUtil.isEmpty(mmbMaster.getProvinceCode()) &&
                    StringUtil.isEmpty(mmbMaster.getCityCode())) {
                List<ErpCity> list = erpCityMapperExt.queryCityOfProvince(mmbMaster.getProvinceCode());
                json.put("data", list);
            } else if (!StringUtil.isEmpty(mmbMaster.getCityCode())) {
                List<ErpDistrict> list = erpDistrictMapperExt.querypDistrictOfCity(mmbMaster.getCityCode());
                json.put("data", list);
            }

            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    private MmbMaster getAddressInfo(MmbMaster mmbMaster) {

        if (null == mmbMaster) {
            return mmbMaster;
        }

        if (!StringUtil.isEmpty(mmbMaster.getProvinceCode())) {
            ErpProvince provice = erpProvinceMapper.selectByPrimaryKey(mmbMaster.getProvinceCode());
            if (provice != null) {
                mmbMaster.setProvinceName(provice.getPname());
            }
        }

        if (!StringUtil.isEmpty(mmbMaster.getCityCode())) {
            ErpCity city = erpCityMapper.selectByPrimaryKey(mmbMaster.getCityCode());
            if (city != null) {
                mmbMaster.setCityName(city.getCname());
            }
        }

        if (!StringUtil.isEmpty(mmbMaster.getDistrictCode())) {
            ErpDistrict district = erpDistrictMapper.selectByPrimaryKey(mmbMaster.getDistrictCode());
            if (district != null) {
                mmbMaster.setDistrictName(district.getDname());
            }
        }

        return mmbMaster;
    }

    /**
     * 通过手机好获取用户列表
     *
     * @param form
     * @return
     */
    public JSONObject getListByPhone(MmbMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            MmbMasterExt ext = new MmbMasterExt();
            ext.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
            ext.setStartPoint(Integer.parseInt(form.getiDisplayStart()));
//            ext.setMemberName(form.getMemberName());
//            ext.setType(form.getType());
            ext.setTelephone(form.getTelephone());
//            ext.setMemberLevelId(form.getMemberLevelId());

            List<MmbMasterExt> list = mmbMasterMapperExt.selectAllByPhone(ext);
            int allCount = mmbMasterMapperExt.getAllDataCountByPhone(ext);

            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);
            json.put("aaData", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    public JSONObject changeTelephone(MmbMaster user) {
        JSONObject json = new JSONObject();
        try {
            //参数检查
            if (user == null) {
                throw new ExceptionErrorParam("参数错误");
            }
            String memberId = user.getMemberId();
            if (StringUtil.isEmpty(memberId)) {
                throw new ExceptionErrorParam("未传会员ID");
            }
            String newphone = user.getTelephone();
            if (StringUtil.isEmpty(newphone)) {
                throw new ExceptionErrorParam("未传新手机号码");
            }
            MmbMaster master = mmbMasterMapper.selectByPrimaryKey(user.getMemberId());
            if (master == null || !"0".equals(master.getDeleted())) {
                throw new ExceptionErrorParam("用户信息不存在");
            }
            String telephone = master.getTelephone();
            if (telephone.equals(newphone)) {
                throw new ExceptionErrorParam("用户手机号码没变");
            }
            //重复检查
            MmbMaster mmbMasterCheck = new MmbMaster();
            mmbMasterCheck.setTelephone(newphone);
            int hvCount = mmbMasterMapperExt.getCountBySelective(mmbMasterCheck);
            if (hvCount > 0) {
                throw new ExceptionErrorParam("该号码已注册");
            }
            //号码更新
            user.setOldphone(telephone);
            int ret = mmbMasterMapper.updateByPrimaryKeySelective(user);
            if (ret != 1) {
                throw new Exception("更新错误");
            }
//            updatePhoneToERP(master,newphone);
            master.setOldphone(master.getTelephone());
            master.setTelephone(newphone);
//            ErpSendUtil.getInstance().VIPUpdate(master);
            json = ErpSendUtil.VIPUpdate(master, mmbMasterMapperExt, mmbMasterMapper);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

//    private void updatePhoneToERP(MmbMaster user,String newphone) {
//
////        JSONObject json = new JSONObject();
//
//        // 获取信息
//        String memberId = user.getMemberId();
//        MmbMaster master = new MmbMaster();
//        master.setMemberId(memberId);
//        master.setErpSendStatus("20");
//        try {
//            Vip vip = new Vip();
//
//            vip.setMemberCode(user.getTelephone());
//            vip.setMemberName(user.getMemberName());
//            // 1：男   2：女
//            String sexName = "";
//            if ("1".equals(user.getSex())) {
//                sexName = "男";
//            } else if ("2".equals(user.getSex())) {
//                sexName = "女";
//            }
//            vip.setSexName(sexName);
//            vip.setMobil(user.getTelephone());
//            vip.setBirthday(DataUtils.formatTimeStampToYMD(user.getBirthdate()));
//            vip.setProvinceName(user.getProvinceName());
//            vip.setCityName(user.getCityName());
//            vip.setDistrictName(user.getDistrictName());
//            vip.setEmail(user.getEmail());
////            vip.setStoreCode("");
////            vip.setStoreName("");
//            vip.setSellerName(user.getReferrerId());
//            vip.setAddress(user.getAddress());
//            vip.setPostCode(user.getPostCode());
//            vip.setIncome(user.getIncome());
//            vip.setRegistTime(DataUtils.formatTimeStampToYMD(user.getInsertTime()));
//            vip.setMemberGrade(user.getMemberLevelId());
//            vip.setMemberCodeAlter(newphone);
//
//            BaseDate date = new BaseDate();
//            date.setVip(vip);
//            String result = soap.vipUpdate(getKeyVIPUpdate(date), date);
//            log.debug("ERP vipUpdate result code:"+result+",param:"+ JSON.toJSONString(date));
//            master.setUpdateTime(new Date());
//            if (result.equals("00")) {
//                mmbMasterMapperExt.clearOldphoneById(master.getMemberId());
//                master.setErpSendStatus("10");
//            }
//            mmbMasterMapper.updateByPrimaryKeySelective(master);
//            if (result.equals("00")) {
//            } else if (result.equals("11")) {
//                throw new ExceptionErrorData("ERP数据库连接失败");
//            } else if (result.equals("12")) {
//                throw new ExceptionErrorData("ERP数据库更新失败");
//            } else if (result.equals("21")) {
//                throw new ExceptionErrorData("ERP验证失败");
//            } else {
//                throw new ExceptionErrorData("ERP未知错误");
//            }
//        } catch (Exception e) {
//            System.out.println("用户信息同步ERP失败,原因:"+e.getMessage());
//        }
//
////        return json;
//    }

    public JSONObject changeGrade(MmbMaster user) {
        JSONObject json = new JSONObject();
        try {
            //参数检查
            if (user == null) {
                throw new ExceptionErrorParam("参数错误");
            }
            String memberId = user.getMemberId();
            if (StringUtil.isEmpty(memberId)) {
                throw new ExceptionErrorParam("未传会员ID");
            }
            if (StringUtils.isEmpty(user.getMemberLevelId())) {
                throw new ExceptionErrorParam("会员级别未指定");
            }
            MmbMaster master = mmbMasterMapper.selectByPrimaryKey(user.getMemberId());
            if (master == null || !"0".equals(master.getDeleted())) {
                throw new ExceptionErrorParam("用户信息不存在");
            }
            if (user.getMemberLevelId().equals(master.getMemberLevelId())) {
                throw new ExceptionErrorParam("用户级别未发生变化");
            }
            //会员级别更新
            int ret = mmbMasterMapper.updateByPrimaryKeySelective(user);
            if (ret != 1) {
                throw new Exception("更新错误");
            }
            //ERP传送
            master.setMemberLevelId(user.getMemberLevelId());
            master.setUpdateTime(new Date());
//            updateLevelToERP(master);
//            ErpSendUtil.getInstance().VIPUpdate(master);
            ErpSendUtil.VIPUpdate(master, mmbMasterMapperExt, mmbMasterMapper);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    @Override
    public JSONObject modifyMemberAvatarFromWX(MmbMasterExt mmbMaster) {
        JSONObject json = new JSONObject();
        try {
            //参数检查
            if (StringUtil.isEmpty(mmbMaster.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(mmbMaster.getServerId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            //从微信服务器下载文件,下载后的件为targetFile
            String id = UUID.randomUUID().toString();
            String suffix = ".jpg";
            String savedPath = fileServerConfig.getStoragePath() + "/orignal";
            String subPath = "/" + "CMS_MASTER" + "/" + id.substring(0, 2) + "/";
            String fileName = id + suffix;

            File parentFile = new File(savedPath + subPath);
            if (!parentFile.exists()
                    && !parentFile.isDirectory()) {
                parentFile.mkdir();
            }

            String targetFile = savedPath + subPath + fileName;
            File file = wxMpService.mediaDownload(mmbMaster.getServerId());
            FileOperator.copyFile(file.getAbsolutePath(), targetFile);

            // 更用户表
            MmbMaster master = new MmbMaster();
            master.setMemberId(mmbMaster.getMemberId());
            master.setMemberPic(subPath + fileName);
            mmbMasterMapper.updateByPrimaryKeySelective(master);
            json.put("url", subPath + fileName);
            json.put("imageId", id);
            json.put("data", subPath + fileName);

            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    @Override
    public JSONObject selectReport() {
        JSONObject json = new JSONObject();
        try {
            List<MmbMasterExt> list = mmbMasterMapperExt.selectReport();
            json.put("data", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

////    private void updateLevelToERP(MmbMaster user) {
////
//////        JSONObject json = new JSONObject();
////
////        // 获取信息
////        String memberId = user.getMemberId();
////        MmbMaster master = new MmbMaster();
////        master.setMemberId(memberId);
////        master.setErpSendStatus("20");
////        try {
////            Vip vip = new Vip();
////
////            vip.setMemberCode(user.getTelephone());
////            vip.setMemberName(user.getMemberName());
////            // 1：男   2：女
////            String sexName = "";
////            if ("1".equals(user.getSex())) {
////                sexName = "男";
////            } else if ("2".equals(user.getSex())) {
////                sexName = "女";
////            }
////            vip.setSexName(sexName);
////            vip.setMobil(user.getTelephone());
////            vip.setBirthday(DataUtils.formatTimeStampToYMD(user.getBirthdate()));
////            vip.setProvinceName(user.getProvinceName());
////            vip.setCityName(user.getCityName());
////            vip.setDistrictName(user.getDistrictName());
////            vip.setEmail(user.getEmail());
//////            vip.setStoreCode("");
//////            vip.setStoreName("");
////            vip.setSellerName(user.getReferrerId());
////            vip.setAddress(user.getAddress());
////            vip.setPostCode(user.getPostCode());
////            vip.setIncome(user.getIncome());
////            vip.setRegistTime(DataUtils.formatTimeStampToYMD(user.getInsertTime()));
////            vip.setMemberGrade(user.getMemberLevelId());
//////            vip.setMemberCodeAlter("");
////
////            BaseDate date = new BaseDate();
////            date.setVip(vip);
////            String result = soap.vipUpdate(getKeyVIPUpdate(date), date);
////            log.debug("ERP vipUpdate result code:"+result+",param:"+ JSON.toJSONString(date));
////            master.setUpdateTime(new Date());
////            if (result.equals("00")) {
////                master.setErpSendStatus("10");
////            }
////            mmbMasterMapper.updateByPrimaryKeySelective(master);
////            if (result.equals("00")) {
////            } else if (result.equals("11")) {
////                throw new ExceptionErrorData("ERP数据库连接失败");
////            } else if (result.equals("12")) {
////                throw new ExceptionErrorData("ERP数据库更新失败");
////            } else if (result.equals("21")) {
////                throw new ExceptionErrorData("ERP验证失败");
////            } else {
////                throw new ExceptionErrorData("ERP未知错误");
////            }
////        } catch (Exception e) {
////            System.out.println("用户信息同步ERP失败,原因:"+e.getMessage());
////        }
//
////        return json;
//    }


    @Override
    public List<MmbMasterExt> export(MmbMasterExt ext) {
        return mmbMasterMapperExt.export(ext);
    }
}
