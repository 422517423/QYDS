package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ShpMaster;
import net.dlyt.qyds.common.dto.ShpMasterExt;
import net.dlyt.qyds.dao.ext.ShpMasterMapperExt;
import net.dlyt.qyds.dao.ShpMasterMapper;
import net.dlyt.qyds.web.service.ShpMasterService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by wy on 2016/7/18.
 */
@Service("shpMasterService")
public class ShpMasterServiceImpl implements ShpMasterService {
    @Autowired
    private ShpMasterMapperExt shpMasterMapperExt;
    @Autowired
    private ShpMasterMapper shpMasterMapper;

    /*获取店铺列表*/
    public  JSONObject selectAll(String data) {
            JSONObject json = new JSONObject();
            try {
                JSONArray array = new JSONArray();
                JSONObject jsonObject = null;
                List<ShpMasterExt> list = shpMasterMapperExt.selectAll();
                for (ShpMasterExt master : list) {
                    jsonObject = new JSONObject();
                    jsonObject.put("shopId", master.getShopId());
                    jsonObject.put("shopCode", master.getShopCode());
                    jsonObject.put("shopShortName", master.getShopShortName());
                    jsonObject.put("shopName", master.getShopName());
                    jsonObject.put("isValid", master.getIsValid());
                    jsonObject.put("comment", master.getComment());
                    jsonObject.put("deleted", master.getDeleted());
                    jsonObject.put("updateUserId", master.getUpdateUserId());
                    jsonObject.put("updateTime", master.getUpdateTime());
                    jsonObject.put("userName", master.getUserName());
                    jsonObject.put("insertUserId", master.getInsertUserId());
                    jsonObject.put("insertTime", DataUtils.formatTimeStampToYMD(master.getInsertTime()));
                    array.add(jsonObject);
                }
                json.put("data", array);
                json.put("resultCode", Constants.NORMAL);
            } catch (Exception e) {
                json.put("resultCode", Constants.FAIL);
            }
            return json;
    }

    /*根据ID获取店铺列表*/
    public JSONObject edit( String shopId) {
            JSONObject json = new JSONObject();
            try {
                if (!StringUtils.isEmpty(shopId)) {
                    ShpMaster master = shpMasterMapper.selectByPrimaryKey(shopId);
                    if (master != null) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("shopId", master.getShopId());
                        jsonObject.put("shopCode", master.getShopCode());
                        jsonObject.put("shopShortName", master.getShopShortName());
                        jsonObject.put("shopName", master.getShopName());
                        jsonObject.put("isValid", master.getIsValid());
                        jsonObject.put("comment", master.getComment());
                        jsonObject.put("deleted", master.getDeleted());
                        jsonObject.put("updateUserId", master.getUpdateUserId());
                        jsonObject.put("updateTime", master.getUpdateTime());
                        jsonObject.put("insertUserId", master.getInsertUserId());
                        jsonObject.put("insertTime", DataUtils.formatTimeStampToYMD(master.getInsertTime()));
                        json.put("data", jsonObject);
                    }
                }
                json.put("resultCode", Constants.NORMAL);
            } catch (Exception e) {
                json.put("resultCode", Constants.FAIL);
            }
            return json;

    }

//    /*编辑*/
//    @Transactional(readOnly = false,rollbackFor = Exception.class)
//    public  ShpMaster updateByPrimaryKeySelective(ShpMaster shpmaster) {
//        shpMasterMapper.updateByPrimaryKey(shpmaster);
//        shpmaster=shpMasterMapper.selectByPrimaryKey(shpmaster.getShopId());
//        return shpmaster;
//    }

    /*根据ID屋里删除店铺信息*/
    public JSONObject delete(String shopId) {
            JSONObject json = new JSONObject();
            try {
                if (!StringUtils.isEmpty(shopId)) {
                    ShpMaster shpmaster = new ShpMaster();
                    shpmaster.setShopId(shopId);
                    shpmaster.setDeleted("1");
                    shpmaster.setIsValid("1");
                    shpMasterMapper.updateByPrimaryKeySelective(shpmaster);
                }
                json.put("resultCode", Constants.NORMAL);
            } catch (Exception e) {
                json.put("resultCode", Constants.FAIL);
            }
            return json;
        }

    /*新建*/
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public
        JSONObject save(String data) {
            JSONObject json = new JSONObject();
            try {
                ShpMaster shpmaster = JSON.parseObject(data, ShpMaster.class);
                ShpMaster master = null;
                JSONObject jsonObject = new JSONObject();
                if (StringUtils.isEmpty(shpmaster.getShopId())) {
                    shpmaster.setDeleted(Constants.DELETED_NO);
                    shpmaster.setIsValid(Constants.DELETED_NO);
                    shpmaster.setInsertTime(new Date());
                    shpmaster.setUpdateTime(new Date());
                    shpmaster.setShopId(UUID.randomUUID().toString());
                   shpMasterMapper.insertSelective(shpmaster);
                } else {
                    shpmaster.setUpdateTime(new Date());
                  shpMasterMapper.updateByPrimaryKeySelective(shpmaster);
                }
                if (master != null) {
                    jsonObject.put("shopId", master.getShopId());
                    jsonObject.put("shopCode", master.getShopCode());
                    jsonObject.put("shopShortName", master.getShopShortName());
                    jsonObject.put("shopName", master.getShopName());
                    jsonObject.put("isValid", master.getIsValid());
                    jsonObject.put("comment", master.getComment());
                    jsonObject.put("deleted", master.getDeleted());
                    jsonObject.put("updateUserId", master.getUpdateUserId());
                    jsonObject.put("updateTime", master.getUpdateTime());
                    jsonObject.put("insertUserId", master.getInsertUserId());
                    jsonObject.put("insertTime", DataUtils.formatTimeStampToYMD(master.getInsertTime()));
                    json.put("data", jsonObject);
                }
                json.put("resultCode", Constants.NORMAL);
            } catch (Exception e) {
                json.put("resultCode", Constants.FAIL);
            }
            return json;
        }
}
