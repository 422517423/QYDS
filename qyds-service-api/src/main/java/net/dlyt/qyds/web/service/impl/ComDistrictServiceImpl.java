package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ComDistrict;
import net.dlyt.qyds.common.dto.ErpCity;
import net.dlyt.qyds.common.dto.ErpDistrict;
import net.dlyt.qyds.common.dto.ErpProvince;
import net.dlyt.qyds.dao.ComDistrictMapper;
import net.dlyt.qyds.dao.ext.ComDistrictMapperExt;
import net.dlyt.qyds.dao.ext.ErpCityMapperExt;
import net.dlyt.qyds.dao.ext.ErpDistrictMapperExt;
import net.dlyt.qyds.dao.ext.ErpProvinceMapperExt;
import net.dlyt.qyds.web.service.ComDistrictService;
import net.dlyt.qyds.web.service.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**c
 * Created by dkzhang on 16/7/29.
 */
@Service("comDistrictService")
public class ComDistrictServiceImpl implements ComDistrictService{

    @Autowired
    private ComDistrictMapper comDistrictMapper;

    @Autowired
    private ComDistrictMapperExt comDistrictMapperExt;

    @Autowired
    private ErpProvinceMapperExt erpProvinceMapperExt;

    @Autowired
    private ErpCityMapperExt erpCityMapperExt;

    @Autowired
    private ErpDistrictMapperExt erpDistrictMapperExt;

    @Cacheable(value="${web_address_cache_name}", key="#parentId")
    public JSONObject selectByParentId(String parentId) {
        JSONObject json = new JSONObject();
        try {
            List<ComDistrict> list = comDistrictMapperExt.selectByParentId(parentId);
            json.put("results", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    public JSONObject selectByPrimaryKey(String districtId) {
        JSONObject json = new JSONObject();
        try {
            if (!StringUtils.isEmpty(districtId)) {
                ComDistrict comDistrict = comDistrictMapper.selectByPrimaryKey(districtId);
                if (comDistrict != null) {
                    json.put("results", JSON.toJSON(comDistrict));
                }
            }
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    public JSONObject insertSelective(ComDistrict record, String userId) {
        JSONObject json = new JSONObject();
        ComDistrict newComDistrict = null;
        Date now = new Date();
        record.setInsertUserId(userId);
        record.setInsertTime(now);
        record.setUpdateUserId(userId);
        record.setUpdateTime(now);
        int ret = comDistrictMapper.insertSelective(record);
        if(ret == 1) {
            newComDistrict = comDistrictMapper.selectByPrimaryKey(record.getDistrictId());
            json.put("data", newComDistrict);
            json.put("resultCode", Constants.NORMAL);
        }else{
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    public JSONObject updateByPrimaryKeySelective(ComDistrict record, String userId) {
        JSONObject json = new JSONObject();
        ComDistrict newComDistrict = null;
        Date now = new Date();
        record.setUpdateUserId(userId);
        record.setUpdateTime(now);
        int ret = comDistrictMapper.updateByPrimaryKeySelective(record);
        if(ret == 1) {
            newComDistrict = comDistrictMapper.selectByPrimaryKey(record.getDistrictId());
            json.put("data", newComDistrict);
            json.put("resultCode", Constants.NORMAL);
        }else{
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    public JSONObject deleteByPrimaryKey(String districtId, String userId) {
        JSONObject json = new JSONObject();
        ComDistrict record = new ComDistrict();
        record.setDistrictId(districtId);
        record.setDeleted("1");
        Date now = new Date();
        record.setUpdateUserId(userId);
        record.setUpdateTime(now);
        int ret = comDistrictMapper.updateByPrimaryKey(record);
        if(ret == 1) {
            json.put("resultCode", Constants.NORMAL);
        }else{
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    public JSONObject selectErpProvinceAll() {
        JSONObject json = new JSONObject();
        try {
            List<ErpProvince> list = erpProvinceMapperExt.queryAllProvince();
            json.put("data", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    public JSONObject selectErpCityByProvince(String pCode) {
        JSONObject json = new JSONObject();
        try {
            List<ErpCity> list = erpCityMapperExt.queryCityOfProvince(pCode);
            json.put("data", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    public JSONObject selectErpDistrictByCity(String cCode) {
        JSONObject json = new JSONObject();
        try {
            List<ErpDistrict> list = erpDistrictMapperExt.querypDistrictOfCity(cCode);
            json.put("data", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }
}
