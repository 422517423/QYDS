package net.dlyt.qyds.web.controller.mmb_address;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.MmbAddressForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.MmbAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by YiLian on 16/8/2.
 */
@Controller
@RequestMapping("/mmb_address")
public class MmbAddressController {

    @Autowired
    private MmbAddressService mmbAddressService;


    /**
     * 会员送货地址一览(一次性全部取得,不分页)
     * 默认地址标记为0,其他标记为1
     *
     * @param data memberId:会员ID
     * @return
     */
    @RequestMapping("getList")
    @ResponseBody
    public JSONObject getList(String data) {

        JSONObject json = new JSONObject();
        try {
            MmbAddressForm form = (MmbAddressForm) JSON.parseObject(data, MmbAddressForm.class);
            json = mmbAddressService.getList(form);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 获取地址详细
     *
     * @param data memberId:会员ID
     *             addressId:地址ID
     * @return
     */
    @RequestMapping("getDetail")
    @ResponseBody
    public JSONObject getDetail(String data) {

        JSONObject json = new JSONObject();
        try {
            MmbAddressForm form = (MmbAddressForm) JSON.parseObject(data, MmbAddressForm.class);
            json = mmbAddressService.getDetail(form);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }


    /**
     * 取得会员默认送货地址
     * 默认地址标记为0,其他标记为1
     *
     * @param data memberId:会员ID
     * @return
     */
    @RequestMapping("getDefaultAddress")
    @ResponseBody
    public JSONObject getDefaultAddress(String data) {

        JSONObject json = new JSONObject();
        try {

            MmbAddressForm form = (MmbAddressForm) JSON.parseObject(data, MmbAddressForm.class);
            json = mmbAddressService.getDefaultAddress(form);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 添加送货地址
     * 默认地址标记为0,其他标记为1
     *
     * @param data memberId:会员ID
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
    @RequestMapping("add")
    @ResponseBody
    public JSONObject add(String data) {

        JSONObject json = new JSONObject();
        try {

            MmbAddressForm form = (MmbAddressForm) JSON.parseObject(data, MmbAddressForm.class);
            json = mmbAddressService.add(form);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 修改送货地址
     * 默认地址标记为0,其他标记为1
     *
     * @param data addressId:地址ID
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
    @RequestMapping("edit")
    @ResponseBody
    public JSONObject edit(String data) {

        JSONObject json = new JSONObject();
        try {

            MmbAddressForm form = (MmbAddressForm) JSON.parseObject(data, MmbAddressForm.class);
            json = mmbAddressService.edit(form);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 设置默认送货地址
     * 默认地址标记为0,其他标记为1
     *
     * @param data addressId:地址ID
     *             memberId:会员ID
     * @return
     */
    @RequestMapping("changeDefault")
    @ResponseBody
    public JSONObject changeDefault(String data) {

        JSONObject json = new JSONObject();
        try {

            MmbAddressForm form = (MmbAddressForm) JSON.parseObject(data, MmbAddressForm.class);
            json = mmbAddressService.changeDefault(form);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 删除送货地址
     * 默认地址标记为0,其他标记为1
     *
     * @param data addressId:地址ID
     *             memberId:会员ID
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public JSONObject delete(String data) {

        JSONObject json = new JSONObject();
        try {
            MmbAddressForm form = (MmbAddressForm) JSON.parseObject(data, MmbAddressForm.class);
            json = mmbAddressService.delete(form);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }
}
