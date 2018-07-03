package net.dlyt.qyds.web.controller.com_district;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ComDistrict;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.ComDistrictService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wy on 2016/7/18.
 */
@Controller
@RequestMapping("/com_discrict")
public class ComDistrictController {

    @Resource
    private ComDistrictService comDistrictService;

    /**
     * 初始省份地址取得
     */
    @RequestMapping("getProvinces")
    public @ResponseBody JSONObject getProvinces() {
        return comDistrictService.selectByParentId(null);
    }

    /**
     * 下一级地址列表取得
     * @param parentId 上级地址编码
     * @return 下一级地址列表
     */
    @RequestMapping("getSubAddresses")
    public @ResponseBody JSONObject getSubAddresses(String parentId) {
        if(StringUtils.isEmpty(parentId)){
            JSONObject json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
        }
        return comDistrictService.selectByParentId(parentId);
    }

    /*详细页面根据ID取值*/
    @RequestMapping("edit")
    public
    @ResponseBody
    JSONObject edit(@RequestParam(required = true) String districtId) {
        if(StringUtils.isEmpty(districtId)){
            JSONObject json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
        }
        return comDistrictService.selectByPrimaryKey(districtId);
    }

    /*编辑和新建保存*/
    @RequestMapping("save")
    public
    @ResponseBody
    JSONObject save(String data) {
        JSONObject json = new JSONObject();
        try {
            ComDistrict comDistrict = JSON.parseObject(data, ComDistrict.class);

            JSONObject districtRemote = comDistrictService.selectByPrimaryKey(comDistrict.getDistrictId());
            JSONObject comCodeResult;

            String loginId = (String)PamsDataContext.getLoginId();
            if (districtRemote == null || Constants.FAIL.equals(districtRemote.getString("resultCode"))) {
                comDistrict.setDeleted(Constants.VALID);
                comCodeResult = comDistrictService.insertSelective(comDistrict, loginId);
            } else {
                comCodeResult = comDistrictService.updateByPrimaryKeySelective(comDistrict, loginId);
            }
            if (comCodeResult != null && Constants.NORMAL.equals(districtRemote.getString("resultCode"))) {
                return comCodeResult;
            }else {
                json.put("resultCode", Constants.FAIL);
            }
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }


    /*删除*/
    @RequestMapping("delete")
    public @ResponseBody JSONObject delete(String districtId) {
        if(StringUtils.isEmpty(districtId)){
            JSONObject json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
        }
        String loginId = (String)PamsDataContext.getLoginId();
        return comDistrictService.deleteByPrimaryKey(districtId, loginId);
    }

    /**
     * 初始省份地址取得
     */
    @RequestMapping("getErpProvince")
    public @ResponseBody JSONObject getErpProvinces() {
        return comDistrictService.selectErpProvinceAll();
    }

    /**
     * 初始市份地址取得
     */
    @RequestMapping("getErpCity")
    public @ResponseBody JSONObject getErpCity(String pCode) {
        return comDistrictService.selectErpCityByProvince(pCode);
    }

    /**
     * 初始市份地址取得
     */
    @RequestMapping("getErpDistrict")
    public @ResponseBody JSONObject getErpDistrict(String cCode) {
        return comDistrictService.selectErpDistrictByCity(cCode);
    }
}
