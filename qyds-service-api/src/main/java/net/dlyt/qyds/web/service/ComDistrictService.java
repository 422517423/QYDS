package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ComCode;
import net.dlyt.qyds.common.dto.ComCodeExt;
import net.dlyt.qyds.common.dto.ComCodeKey;
import net.dlyt.qyds.common.dto.ComDistrict;

import java.util.List;

/**
 * Created by C_Nagai on 2016/7/14.
 */
public interface ComDistrictService {

    /*获取地址列表*/
    JSONObject selectByParentId(String parentId);

    /* 获取地址信息*/
    JSONObject selectByPrimaryKey(String districtId);

    /*新建地址*/
    JSONObject insertSelective(ComDistrict record, String userId);

    /* 更新地址信息*/
    JSONObject updateByPrimaryKeySelective(ComDistrict record, String userId);

    /*删除地址信息*/
    JSONObject deleteByPrimaryKey(String districtId, String userId);

    /*获取ERP省列表*/
    JSONObject selectErpProvinceAll();

    /*获取ERP市列表*/
    JSONObject selectErpCityByProvince(String pCode);

    /*获取ERP区列表*/
    JSONObject selectErpDistrictByCity(String cCode);

}
