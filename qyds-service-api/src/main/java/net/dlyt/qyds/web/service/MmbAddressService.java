package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.MmbAddressForm;

/**
 * Created by YiLian on 16/8/2.
 */
public interface MmbAddressService {

    /**
     * 会员送货地址一览(一次性全部取得,不分页)
     *
     * @param form memberId:会员ID
     * @return
     */
    JSONObject getList(MmbAddressForm form);

    /**
     * 取得会员默认送货地址
     *
     * @param form memberId:会员ID
     * @return
     */
    JSONObject getDefaultAddress(MmbAddressForm form);

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
    JSONObject add(MmbAddressForm form);

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
    JSONObject edit(MmbAddressForm form);

    /**
     * 删除送货地址
     *
     * @param form addressId:地址ID
     *             memberId:会员ID
     * @return
     */
    JSONObject delete(MmbAddressForm form);

    /**
     * 设置默认送货地址
     *
     * @param form addressId:地址ID
     *             memberId:会员ID
     * @return
     */
    JSONObject changeDefault(MmbAddressForm form);

    /**
     * 获取地址详细
     *
     * @param form memberId:会员ID
     *             addressId:地址ID
     * @return
     */
    JSONObject getDetail(MmbAddressForm form);
}
