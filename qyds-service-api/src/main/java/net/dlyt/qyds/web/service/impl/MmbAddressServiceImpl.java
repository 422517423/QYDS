package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbAddress;
import net.dlyt.qyds.common.dto.ext.MmbAddressExt;
import net.dlyt.qyds.common.form.MmbAddressForm;
import net.dlyt.qyds.dao.MmbAddressMapper;
import net.dlyt.qyds.dao.ext.MmbAddressMapperExt;
import net.dlyt.qyds.web.service.MmbAddressService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by YiLian on 16/8/2.
 */
@Service("mmbAddressService")
public class MmbAddressServiceImpl implements MmbAddressService {

    @Autowired
    private MmbAddressMapperExt mmbAddressMapperExt;

    @Autowired
    private MmbAddressMapper mmbAddressMapper;

    /**
     * 会员送货地址一览(一次性全部取得,不分页)
     *
     * @param form memberId:会员ID
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject getList(MmbAddressForm form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("请登录用户");
            }

            List<MmbAddressExt> list = mmbAddressMapperExt.queryList(form);

            if (list != null && list.size() > 0) {
                MmbAddressExt first = list.get(0);
                if (!"0".equals(first.getIsDefault())) {

                    MmbAddress mmbAddress = new MmbAddress();
                    mmbAddress.setAddressId(first.getAddressId());
                    mmbAddress.setIsDefault("0");
                    mmbAddress.setUpdateTime(new Date());
                    mmbAddress.setUpdateUserId(form.getMemberId());
                    mmbAddressMapper.updateByPrimaryKeySelective(mmbAddress);

                    first.setIsDefault("0");

                    list.set(0, first);
                }
            }

            json.put("results", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 取得会员默认送货地址
     *
     * @param form memberId:会员ID
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject getDefaultAddress(MmbAddressForm form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("请登录用户");
            }

            List<MmbAddressExt> result = mmbAddressMapperExt.selectDefaultAddress(form);

            if (result!=null||result.size()>0) {
                json.put("result", result.get(0));
            }else{
                json.put("result", null);
            }
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 添加送货地址
     *
     * @param form memberId:会员ID
     *             districtidProvince:省
     *             districtidCity:市
     *             districtidDistrict:区
     *             districtidStreet:街道
     *             address:地址
     *             postcode:邮编
     *             contactor:联系人
     *             phone:联系电话
     *             isDefault:是否默认地址
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject add(MmbAddressForm form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("请登录用户");
            }

            if (StringUtil.isEmpty(form.getContactor())) {
                throw new ExceptionErrorParam("请输入收货人");
            }

            if (StringUtil.isEmpty(form.getPhone())) {
                throw new ExceptionErrorParam("请输入联系电话");
            }

            if(!StringUtil.isTelephone(form.getPhone())){
                throw new ExceptionErrorParam("联系电话输入格式不正确");
            }

            if (StringUtil.isEmpty(form.getDistrictidProvince())) {
                throw new ExceptionErrorParam("请选择省");
            }

            if (StringUtil.isEmpty(form.getDistrictidCity())) {
                throw new ExceptionErrorParam("请选择市");
            }

            if (StringUtil.isEmpty(form.getDistrictidDistrict())) {
                throw new ExceptionErrorParam("请选择区/县");
            }

            // districtidStreet 街道可能为空

            if (StringUtil.isEmpty(form.getAddress())) {
                throw new ExceptionErrorParam("请输入详细地址");
            }

//            if (StringUtil.isEmpty(form.getPostcode())) {
//                throw new ExceptionErrorParam("缺少参数邮编");
//            }

            Date date = new Date();
            String id = UUID.randomUUID().toString();

            MmbAddress record = new MmbAddress();

            // 如果没有默认地址,则添加的地址自动设为默认地址 默认地址标记为0,其他标记为1
            List<MmbAddressExt> mmbAddressExt = mmbAddressMapperExt.selectDefaultAddress(form);
            if (mmbAddressExt == null || mmbAddressExt.size()==0) {
                record.setIsDefault("0");
            } else if ("0".equals(form.getIsDefault())) {
                for(int i=0;i<mmbAddressExt.size();i++){
                    mmbAddressExt.get(i).setIsDefault("1");
                    mmbAddressExt.get(i).setUpdateTime(date);
                    mmbAddressExt.get(i).setUpdateUserId(form.getMemberId());
                    mmbAddressMapper.updateByPrimaryKeySelective(mmbAddressExt.get(i));
                }
                record.setIsDefault("0");
            }

            //地址ID
            record.setAddressId(id);
            //会员ID
            record.setMemberId(form.getMemberId());
            //序号
            record.setSort((short) 0);
            //省
            record.setDistrictidProvince(form.getDistrictidProvince());
            //市
            record.setDistrictidCity(form.getDistrictidCity());
            //区
            record.setDistrictidDistrict(form.getDistrictidDistrict());
            // 街道
            record.setDistrictidStreet(form.getDistrictidStreet());
            //地址
            record.setAddress(form.getAddress());
            //邮编
            record.setPostcode(form.getPostcode());
            //联系人
            record.setContactor(form.getContactor());
            //联系电话
            record.setPhone(form.getPhone());
            //删除标记
            record.setDeleted("0");
            //修改人
            record.setUpdateUserId(form.getMemberId());

            //修改时间
            record.setUpdateTime(date);
            //创建人
            record.setInsertUserId(form.getMemberId());
            //创建时间
            record.setInsertTime(date);

            // 添加新地址
            mmbAddressMapper.insertSelective(record);

            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 修改送货地址
     *
     * @param form addressId:地址ID
     *             memberId:会员ID
     *             districtidProvince:省
     *             districtidCity:市
     *             districtidDistrict:区
     *             districtidStreet:街道
     *             address:地址
     *             postcode:邮编
     *             contactor:联系人
     *             phone:联系电话
     *             isDefault:是否默认地址
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject edit(MmbAddressForm form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getAddressId())) {
                throw new ExceptionErrorParam("请登录用户");
            }

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("请登录用户");
            }

            if (StringUtil.isEmpty(form.getContactor())) {
                throw new ExceptionErrorParam("请输入联系人");
            }

            if (StringUtil.isEmpty(form.getPhone())) {
                throw new ExceptionErrorParam("请输入联系电话");
            }

            if(!StringUtil.isTelephone(form.getPhone())){
                throw new ExceptionErrorParam("联系电话格式不正确");
            }

            if (StringUtil.isEmpty(form.getDistrictidProvince())) {
                throw new ExceptionErrorParam("请选择省");
            }

            if (StringUtil.isEmpty(form.getDistrictidCity())) {
                throw new ExceptionErrorParam("请选择市");
            }

            if (StringUtil.isEmpty(form.getDistrictidDistrict())) {
                throw new ExceptionErrorParam("请选择区/县");
            }
            // districtidStreet 街道数据可能为空

            if (StringUtil.isEmpty(form.getAddress())) {
                throw new ExceptionErrorParam("请输入详细地址");
            }

//            if (StringUtil.isEmpty(form.getPostcode())) {
//                throw new ExceptionErrorParam("缺少参数邮编");
//            }

            Date date = new Date();

            Boolean isAdd = false;
            MmbAddress record = mmbAddressMapper.selectByPrimaryKey(form.getAddressId());
            if (record == null || !"0".equals(record.getDeleted())) {
                String id = UUID.randomUUID().toString();

                record = new MmbAddress();
                //地址ID
                record.setAddressId(id);
                //会员ID
                record.setMemberId(form.getMemberId());
                //序号
                record.setSort((short) 0);
                //删除标记
                record.setDeleted("0");
                //创建人
                record.setInsertUserId(form.getMemberId());
                //创建时间
                record.setInsertTime(date);

                isAdd = true;
            }

            // 如果没有默认地址,则添加的地址自动设为默认地址 默认地址标记为0,其他标记为1
            List<MmbAddressExt> mmbAddressExt = mmbAddressMapperExt.selectDefaultAddress(form);
            if (mmbAddressExt == null || mmbAddressExt.size()==0) {
                record.setIsDefault("0");
            } else if ("0".equals(form.getIsDefault())) {
                for(int i=0;i<mmbAddressExt.size();i++){
                    mmbAddressExt.get(i).setIsDefault("1");
                    mmbAddressExt.get(i).setUpdateTime(date);
                    mmbAddressExt.get(i).setUpdateUserId(form.getMemberId());
                    mmbAddressMapper.updateByPrimaryKeySelective(mmbAddressExt.get(i));
                }
                record.setIsDefault("0");
            }

            //省
            record.setDistrictidProvince(form.getDistrictidProvince());
            //市
            record.setDistrictidCity(form.getDistrictidCity());
            //区
            record.setDistrictidDistrict(form.getDistrictidDistrict());
            // 街道
            record.setDistrictidStreet(form.getDistrictidStreet());
            //地址
            record.setAddress(form.getAddress());
            //邮编
            record.setPostcode(form.getPostcode());
            //联系人
            record.setContactor(form.getContactor());
            //联系电话
            record.setPhone(form.getPhone());
            //修改人
            record.setUpdateUserId(form.getMemberId());
            //修改时间
            record.setUpdateTime(date);

            if (isAdd) {
                // 添加新地址
                mmbAddressMapper.insertSelective(record);
            } else {
                // 更新地址
                mmbAddressMapper.updateByPrimaryKeySelective(record);
            }

            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 删除送货地址
     *
     * @param form addressId:地址ID
     *             memberId:会员ID
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject delete(MmbAddressForm form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getAddressId())) {
                throw new ExceptionErrorParam("请登录用户");
            }

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("请登录用户");
            }

            Date date = new Date();

            MmbAddress record = mmbAddressMapper.selectByPrimaryKey(form.getAddressId());
            if (record != null && "0".equals(record.getDeleted())) {

                if ("0".equals(record.getIsDefault())) {

                    // 如果是默认地址,需要设置一条默认地址
                    List<MmbAddressExt> list = mmbAddressMapperExt.queryList(form);
                    for (MmbAddressExt item : list) {
                        if (!form.getAddressId().equals(item.getAddressId())) {

                            // 排除当前条目,选择list中的第一条作为默认地址
                            MmbAddress mmbAddress = new MmbAddress();
                            mmbAddress.setAddressId(item.getAddressId());
                            mmbAddress.setIsDefault("0");
                            mmbAddress.setUpdateTime(new Date());
                            mmbAddress.setUpdateUserId(form.getMemberId());
                            mmbAddressMapper.updateByPrimaryKeySelective(mmbAddress);

                            break;
                        }
                    }

                }

                // 删除更新
                record.setDeleted("1");
                //修改人
                record.setUpdateUserId(form.getMemberId());
                //修改时间
                record.setUpdateTime(date);
                mmbAddressMapper.updateByPrimaryKeySelective(record);
            }

            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 设置默认送货地址
     *
     * @param form addressId:地址ID
     *             memberId:会员ID
     * @return
     */
    public JSONObject changeDefault(MmbAddressForm form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getAddressId())) {
                throw new ExceptionErrorParam("请登录用户");
            }

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("请登录用户");
            }

            MmbAddress record = mmbAddressMapper.selectByPrimaryKey(form.getAddressId());
            if (record == null || !"0".equals(record.getDeleted())) {
                throw new ExceptionErrorData("地址信息已经变更");
            }

            List<MmbAddressExt> result = mmbAddressMapperExt.selectDefaultAddress(form);
            for(int i=0;i<result.size();i++){
                result.get(i).setIsDefault("1");
                result.get(i).setUpdateTime(new Date());
                result.get(i).setUpdateUserId(form.getMemberId());
                mmbAddressMapper.updateByPrimaryKeySelective(result.get(i));
            }

            record.setIsDefault("0");
            record.setUpdateTime(new Date());
            record.setUpdateUserId(form.getMemberId());
            mmbAddressMapper.updateByPrimaryKeySelective(record);

            json = getList(form);

        } catch (ExceptionBusiness e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }


    /**
     * 获取地址详细
     *
     * @param form memberId:会员ID
     *             addressId:地址ID
     * @return
     */
    public JSONObject getDetail(MmbAddressForm form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getAddressId())) {
                throw new ExceptionErrorParam("请登录用户");
            }

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("请登录用户");
            }

            MmbAddressExt record = mmbAddressMapperExt.selectAddressByPK(form);
            if (record == null || !"0".equals(record.getDeleted())) {
                throw new ExceptionErrorData("地址信息已经变更");
            }

            json.put("result", record);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }
}
