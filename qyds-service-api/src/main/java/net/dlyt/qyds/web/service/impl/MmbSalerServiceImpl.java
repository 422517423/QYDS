package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.common.dto.ext.MmbSalerExt;
import net.dlyt.qyds.common.form.MmbSalerForm;
import net.dlyt.qyds.dao.ErpCityMapper;
import net.dlyt.qyds.dao.ErpDistrictMapper;
import net.dlyt.qyds.dao.ErpProvinceMapper;
import net.dlyt.qyds.dao.MmbSalerMapper;
import net.dlyt.qyds.dao.ext.*;
import net.dlyt.qyds.web.service.MmbSalerService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.QRCodeUtil;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by ZLH on 16/12/16.
 */
@Service("mmbSalerService")
public class MmbSalerServiceImpl implements MmbSalerService {

    protected final Logger log = LoggerFactory.getLogger(MmbSalerServiceImpl.class);
    @Autowired
    private MmbSalerMapperExt mmbSalerMapperExt;
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

    public List<MmbSalerExt> selectAll(MmbSalerExt ext) {
        return mmbSalerMapperExt.selectAll(ext);
    }

    public int getAllDataCount(MmbSalerExt ext) {
        return mmbSalerMapperExt.getAllDataCount(ext);
    }


    public JSONObject registerUser(MmbSaler sysUser) {
        JSONObject json = new JSONObject();
        try {

            // 获取信息
            if (!StringUtil.isEmpty(sysUser.getOpenId())) {
                MmbSaler Saler = new MmbSaler();
                Saler.setOpenId(sysUser.getOpenId());
                MmbSalerExt rMmbSaler = mmbSalerMapperExt.selectBySelective(Saler);
                if (rMmbSaler != null) {
                    throw new ExceptionErrorParam("微信号已经被注册");
                }
            }

            if (!StringUtil.isTelephone(sysUser.getTelephone())) {
                throw new ExceptionErrorParam("注册手机号码不正确");
            } else {

                MmbSaler Saler = new MmbSaler();
                Saler.setTelephone(sysUser.getTelephone());
                MmbSalerExt rMmbSaler = mmbSalerMapperExt.selectBySelective(Saler);
                if (rMmbSaler != null) {
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
    public JSONObject changePassword(MmbSaler sysUser) {
        JSONObject json = new JSONObject();
        try {
            if (!StringUtil.isTelephone(sysUser.getTelephone())) {
                throw new ExceptionErrorParam("注册手机号码不正确");
            }

            MmbSaler Saler = new MmbSaler();
            Saler.setTelephone(sysUser.getTelephone());
            MmbSalerExt rMmbSaler = mmbSalerMapperExt.selectBySelective(Saler);
            if (rMmbSaler == null || !"0".equals(rMmbSaler.getDeleted())) {
                throw new ExceptionErrorParam("请输入正确的手机号码");
            }

            MmbSaler record = new MmbSaler();
            record.setMemberId(rMmbSaler.getMemberId());
            record.setPassword(sysUser.getPassword());
            mmbSalerMapper.updateByPrimaryKeySelective(record);

            // 获取信息
            MmbSaler user = mmbSalerMapper.selectByPrimaryKey(rMmbSaler.getMemberId());
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

    public JSONObject insertSelective(MmbSaler user) {
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

            int retInsert = mmbSalerMapper.insertSelective(user);
            json.put("data", user);
//            if (retInsert == 1) {
//                try{
//                    // TODO @崔 注册券发送调用在此处
//                    // 获取注册劵
//                    CouponSaler regCoupon = couponSalerMapperExt.selectRegisterCoupon();
//                    if (regCoupon == null) {
//                        throw new ExceptionErrorData("注册劵不存在!");
//                    }
//                    if (regCoupon.getSendStartTime().compareTo(new Date()) > 0 || regCoupon.getSendEndTime().compareTo(new Date()) < 0) {
//                        throw new ExceptionErrorData("当前不在发放期间内!");
//                    }
//                    CouponMemberExt form1 = new CouponMemberExt();
//                    form1.setMemberId(user.getMemberId());
//                    couponMemberService.addRegisterCouponsForUser(form1, regCoupon);
//
//                    if (null != user.getBirthdate() && checkBirthdate(user.getBirthdate())) {
//                        // 获取生日劵
//                        if(StringUtil.isEmpty(user.getMemberLevelId())){
//                            throw new ExceptionErrorData("会员级别不存在，无法取得生日劵!");
//                        }
//                        CouponSaler coupon = couponSalerMapperExt.selectBirthdayCoupon(user.getMemberLevelId());
//                        if (coupon == null) {
//                            throw new ExceptionErrorData("生日券不存在!");
//                        }
//                        if (coupon.getSendStartTime().compareTo(new Date()) > 0 || coupon.getSendEndTime().compareTo(new Date()) < 0) {
//                            throw new ExceptionErrorData("当前不在发放期间内!");
//                        }
//                        CouponMemberExt form = new CouponMemberExt();
//                        form.setMemberId(user.getMemberId());
//                        Calendar c = Calendar.getInstance();
//                        int nowYear = c.get(Calendar.YEAR);
//                        c.setTime(user.getBirthdate());
//                        c.set(Calendar.YEAR, nowYear);
//                        form.setStartTime(c.getTime());
//                        couponMemberService.addBirthdayCouponsForUser(form, coupon);
//                    }
//                }catch (Exception e){
//                    System.out.println("发放注册劵或生日劵失败,原因:"+e.getMessage());
//                }
//                // 写入ERP
////                json = updateSalerToERP(user.getMemberId());
////                json = ErpSendUtil.getInstance().VIPUpdateById(user.getMemberId());
//                ErpSendUtil.VIPUpdate(user,mmbSalerMapperExt,mmbSalerMapper);
//            } else {
//                json.put("resultCode", Constants.FAIL);
//                json.put("resultMessage", "注册失败,服务器异常");
//            }
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    public JSONObject selectByPrimaryKey(String memberId) {
        JSONObject json = new JSONObject();
        if (!StringUtils.isEmpty(memberId)) {
            MmbSaler rMmbSaler = mmbSalerMapper.selectByPrimaryKey(memberId);
            if (rMmbSaler == null) {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "用户信息不存在");
            } else {
                json.put("data", rMmbSaler);
                json.put("resultCode", Constants.NORMAL);
            }
        } else {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", "缺少主键参数");
        }
        return json;
    }

    public JSONObject updateByPrimaryKeySelective(MmbSaler user) {
        JSONObject json = new JSONObject();
        json.put("resultCode", Constants.NORMAL);
        if (user != null && !StringUtils.isEmpty(user.getMemberId())) {

            MmbSaler Saler = mmbSalerMapper.selectByPrimaryKey(user.getMemberId());

            if (Saler == null || !"0".equals(Saler.getDeleted())) {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "用户信息不存在");
                return json;
//            } else if (null == Saler.getBirthdate() && null != user.getBirthdate()) {
//                if (checkBirthdate(user.getBirthdate())) {
//                    // 生日是今天
//                    try {
//                        // 获取生日劵
//                        if(StringUtil.isEmpty(user.getMemberLevelId())){
//                            throw new ExceptionErrorData("会员级别不存在，无法取得生日劵!");
//                        }
//                        CouponSaler coupon = couponSalerMapperExt.selectBirthdayCoupon(user.getMemberLevelId());
//                        if (coupon == null) {
//                            throw new ExceptionErrorData("生日券不存在!");
//                        }
//                        if (coupon.getSendStartTime().compareTo(new Date()) > 0 || coupon.getSendEndTime().compareTo(new Date()) < 0) {
//                            throw new ExceptionErrorData("当前不在发放期间内!");
//                        }
//                        CouponMemberExt form = new CouponMemberExt();
//                        form.setMemberId(user.getMemberId());
//                        Calendar c = Calendar.getInstance();
//                        int nowYear = c.get(Calendar.YEAR);
//                        c.setTime(user.getBirthdate());
//                        c.set(Calendar.YEAR, nowYear);
//                        form.setStartTime(c.getTime());
//                        couponMemberService.addBirthdayCouponsForUser(form, coupon);
//                    } catch (Exception e) {
//
//                    }
//                }
            }

            user = getAddressInfo(user);
            //修改电话号码
            //修改电话重复检查
            if (!StringUtil.isEmpty(user.getTelephone()) && !StringUtil.isEmpty(Saler.getTelephone())
                    && !Saler.getTelephone().equals(user.getTelephone())) {
                MmbSaler mmbSalerCheck = new MmbSaler();
                mmbSalerCheck.setTelephone(user.getTelephone());
                int hvCount = mmbSalerMapperExt.getCountBySelective(mmbSalerCheck);
                if (hvCount > 0) {
//                    throw new ExceptionErrorParam("该号码已注册");
                    json.put("resultCode", Constants.FAIL);
                    json.put("resultMessage", "该号码已注册");
                    return json;
                }
                user.setOldphone(Saler.getTelephone());
            }

            int ret = mmbSalerMapper.updateByPrimaryKeySelective(user);
            if (ret == 1) {
                // 写入ERP
//                json = updateSalerToERP(user.getMemberId());
//                json = ErpSendUtil.getInstance().VIPUpdateById(user.getMemberId());
//                ErpSendUtil.VIPUpdateById(user.getMemberId(),mmbSalerMapperExt,mmbSalerMapper);
                user = mmbSalerMapper.selectByPrimaryKey(user.getMemberId());
                if (user == null) {
                    json.put("resultCode", Constants.FAIL);
                    json.put("resultMessage", "用户信息不存在");
//                } else {
//                    json.put("data", user);
//                    json.put("resultCode", Constants.NORMAL);
////                json = updateSalerToERP(user.getMemberId());
////                json = ErpSendUtil.getInstance().VIPUpdateById(user.getMemberId());
//                    ErpSendUtil.VIPUpdate(user,mmbSalerMapperExt,mmbSalerMapper);
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

    public JSONObject deleteByPrimaryKey(MmbSaler user) {
        JSONObject json = new JSONObject();

        try {

            if (user == null || StringUtils.isEmpty(user.getMemberId())) {
                throw new ExceptionErrorParam("缺少主键参数");
            }

            user.setIsValid("1");
            user.setDeleted("1");
            int ret = mmbSalerMapper.updateByPrimaryKeySelective(user);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }

        return json;
    }

    public JSONObject selectBySelective(MmbSaler user) {
        JSONObject json = new JSONObject();
        try {
            if (user != null) {
                // 获取信息
                MmbSalerExt rMmbSaler = mmbSalerMapperExt.selectBySelective(user);
                if (rMmbSaler == null) {
                    json.put("resultCode", Constants.FAIL);
                    json.put("resultMessage", "用户信息不存在");
                } else {
                    json.put("data", rMmbSaler);
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

    public JSONObject loginSelective(MmbSaler sysUser) {
        JSONObject json = new JSONObject();
        if (sysUser != null) {
            MmbMasterExt mmbMasterExt = new MmbMasterExt();
            if (!StringUtil.isEmpty(sysUser.getTelephone()) && !StringUtil.isEmpty(sysUser.getPassword())) {
                MmbSaler record = new MmbSaler();
                record.setTelephone(sysUser.getTelephone());
                MmbSalerExt rMmbSaler = mmbSalerMapperExt.selectBySelective(record);
                BeanUtils.copyProperties(rMmbSaler, mmbMasterExt);

                //获取用户的组织信息
                MmbSalerExt mmbSalerExt = mmbSalerMapperExt.getUserOrgInfo(record.getTelephone());
                if (mmbSalerExt != null) {
                    mmbMasterExt.setOperate(mmbSalerExt.getOperate());
                    mmbMasterExt.setStoreid(mmbSalerExt.getStoreid());
                    mmbMasterExt.setStoresubid(mmbSalerExt.getStoresubid());
                }

                if (rMmbSaler == null || !"0".equals(rMmbSaler.getDeleted())) {
                    json.put("resultCode", Constants.FAIL_NO_DATA);
                    json.put("resultMessage", "请输入正确的用户信息");
                } else if (null == rMmbSaler.getPassword() || StringUtil.isEmpty(rMmbSaler.getPassword())) {
                    json.put("resultCode", Constants.FAIL_NO_DATA);
                    json.put("resultMessage", "请重新设置登录密码");
                } else if (sysUser.getPassword().toLowerCase().equals(rMmbSaler.getPassword().toLowerCase())) {
                    json.put("data", mmbMasterExt);
                    json.put("resultCode", Constants.NORMAL);
                } else {
                    json.put("resultCode", Constants.FAIL_NO_DATA);
                    json.put("resultMessage", "请输入正确的登录密码");
                }
            } else {
                // 获取信息
                MmbSalerExt rMmbSaler = mmbSalerMapperExt.selectBySelective(sysUser);
                BeanUtils.copyProperties(rMmbSaler, mmbMasterExt);
                if (rMmbSaler == null) {
                    json.put("resultCode", Constants.FAIL_NO_DATA);
                    json.put("resultMessage", "请输入正确的用户信息");
                } else {
                    json.put("data", mmbMasterExt);
                    json.put("resultCode", Constants.NORMAL);
                }
            }
        } else {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", "用户信息不存在");
        }
        return json;
    }

    private JSONObject selectResponseMmbSaler(String memberId) {
        JSONObject json = new JSONObject();
        try {
            // 获取信息
            MmbSaler rMmbSaler = mmbSalerMapper.selectByPrimaryKey(memberId);
            if (rMmbSaler == null) {
                json.put("resultCode", Constants.FAIL);
            } else {
                json.put("data", rMmbSaler);
                json.put("resultCode", Constants.NORMAL);
            }
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    public JSONObject getAddressList(MmbSaler mmbSaler) {

        JSONObject json = new JSONObject();
        try {

            if (mmbSaler == null || (StringUtil.isEmpty(mmbSaler.getProvinceCode()) && StringUtil.isEmpty(mmbSaler.getCityCode()))) {
                // 取得省
                List<ErpProvince> list = erpProvinceMapperExt.queryAllProvince();
                json.put("data", list);
            } else if (!StringUtil.isEmpty(mmbSaler.getProvinceCode()) &&
                    StringUtil.isEmpty(mmbSaler.getCityCode())) {
                List<ErpCity> list = erpCityMapperExt.queryCityOfProvince(mmbSaler.getProvinceCode());
                json.put("data", list);
            } else if (!StringUtil.isEmpty(mmbSaler.getCityCode())) {
                List<ErpDistrict> list = erpDistrictMapperExt.querypDistrictOfCity(mmbSaler.getCityCode());
                json.put("data", list);
            }

            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    private MmbSaler getAddressInfo(MmbSaler mmbSaler) {

        if (null == mmbSaler) {
            return mmbSaler;
        }

        if (!StringUtil.isEmpty(mmbSaler.getProvinceCode())) {
            ErpProvince provice = erpProvinceMapper.selectByPrimaryKey(mmbSaler.getProvinceCode());
            if (provice != null) {
                mmbSaler.setProvinceName(provice.getPname());
            }
        }

        if (!StringUtil.isEmpty(mmbSaler.getCityCode())) {
            ErpCity city = erpCityMapper.selectByPrimaryKey(mmbSaler.getCityCode());
            if (city != null) {
                mmbSaler.setCityName(city.getCname());
            }
        }

        if (!StringUtil.isEmpty(mmbSaler.getDistrictCode())) {
            ErpDistrict district = erpDistrictMapper.selectByPrimaryKey(mmbSaler.getDistrictCode());
            if (district != null) {
                mmbSaler.setDistrictName(district.getDname());
            }
        }

        return mmbSaler;
    }

    /**
     * 通过手机好获取用户列表
     *
     * @param form
     * @return
     */
    public JSONObject getListByPhone(MmbSalerForm form) {
        JSONObject json = new JSONObject();
        try {
            MmbSalerExt ext = new MmbSalerExt();
            ext.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
            ext.setStartPoint(Integer.parseInt(form.getiDisplayStart()));
//            ext.setMemberName(form.getMemberName());
//            ext.setType(form.getType());
            ext.setTelephone(form.getTelephone());
//            ext.setMemberLevelId(form.getMemberLevelId());

            List<MmbSalerExt> list = mmbSalerMapperExt.selectAllByPhone(ext);
            int allCount = mmbSalerMapperExt.getAllDataCountByPhone(ext);

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

    public JSONObject changeTelephone(MmbSaler user) {
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
            MmbSaler Saler = mmbSalerMapper.selectByPrimaryKey(user.getMemberId());
            if (Saler == null || !"0".equals(Saler.getDeleted())) {
                throw new ExceptionErrorParam("用户信息不存在");
            }
            String telephone = Saler.getTelephone();
            if (telephone.equals(newphone)) {
                throw new ExceptionErrorParam("用户手机号码没变");
            }
            //重复检查
            MmbSaler mmbSalerCheck = new MmbSaler();
            mmbSalerCheck.setTelephone(newphone);
            int hvCount = mmbSalerMapperExt.getCountBySelective(mmbSalerCheck);
            if (hvCount > 0) {
                throw new ExceptionErrorParam("该号码已注册");
            }
            //号码更新
            user.setOldphone(telephone);
            int ret = mmbSalerMapper.updateByPrimaryKeySelective(user);
            if (ret != 1) {
                throw new Exception("更新错误");
            }
//            updatePhoneToERP(Saler,newphone);
            Saler.setOldphone(Saler.getTelephone());
            Saler.setTelephone(newphone);
//            ErpSendUtil.getInstance().VIPUpdate(Saler);
//            json = ErpSendUtil.VIPUpdate(Saler,mmbSalerMapperExt,mmbSalerMapper);
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
            List<MmbSalerExt> list = mmbSalerMapperExt.selectReport();
            json.put("data", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    @Override
    public JSONObject makeQrCodeForAllSaler() {
        JSONObject json = new JSONObject();
        try {
            List<MmbSaler> salerList = mmbSalerMapper.selectAll();
            if (salerList != null && salerList.size() != 0) {
                for (MmbSaler mmbSaler : salerList) {
                    String qrCodeUrlForSaler = QRCodeUtil.getCodeForSaler(mmbSaler.getMemberId());
                    if (qrCodeUrlForSaler != null && !"99".equals(qrCodeUrlForSaler)) {
                        mmbSaler.setQrCodeUrl(qrCodeUrlForSaler);
                        mmbSalerMapper.updateByPrimaryKeySelective(mmbSaler);
                        System.out.println("成功");
                    } else {
                        System.out.println("失败");
                    }
                }
            } else {
                System.out.println("失败了");
            }
//            json.put("data", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }
}
