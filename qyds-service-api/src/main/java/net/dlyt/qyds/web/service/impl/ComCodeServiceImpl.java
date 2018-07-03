package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ComCode;
import net.dlyt.qyds.common.dto.ComCodeExt;
import net.dlyt.qyds.dao.ComCodeMapper;
import net.dlyt.qyds.common.dto.ComCodeKey;
import net.dlyt.qyds.dao.ext.ComCodeMapperExt;
import net.dlyt.qyds.web.service.ComCodeService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.DataUtils;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by wy on 2016/7/18.
 */
@Service("comCodeService")
public class ComCodeServiceImpl implements ComCodeService {
    @Autowired
    private ComCodeMapperExt comCodeMapperExt;
    @Autowired
    private ComCodeMapper comCodeMapper;

    /**
     * 获取码表列表
     *
     * @return
     */
    public JSONObject selectAll() {
        JSONObject json = new JSONObject();
        try {
            List<ComCodeExt> list = comCodeMapperExt.selectAll();
            JSONArray array = new JSONArray();
            JSONObject jsonObject = null;
            for (ComCodeExt comcoext : list) {
                jsonObject = new JSONObject();
                jsonObject.put("code", comcoext.getCode());
                jsonObject.put("name", comcoext.getName());
                jsonObject.put("value", comcoext.getValue());
                jsonObject.put("displayCn", comcoext.getDisplayCn());
                jsonObject.put("displayEn", comcoext.getDisplayEn());
                jsonObject.put("comment", comcoext.getComment());
                jsonObject.put("deleted", comcoext.getDeleted());
                jsonObject.put("updateUserId", comcoext.getUpdateUserId());
                jsonObject.put("updateTime", comcoext.getInsertTime());
                jsonObject.put("userName", comcoext.getUserName());
                jsonObject.put("insertUserId", comcoext.getInsertUserId());
                jsonObject.put("insertTime", DataUtils.formatTimeStampToYMD(comcoext.getInsertTime()));
                array.add(jsonObject);
            }
            json.put("data", array);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 根据code value 获取码表信息
     *
     * @return
     */
    public JSONObject edit(String code, String value) {
        JSONObject json = new JSONObject();
        try {
            if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(value)) {
                JSONObject jsonObject = new JSONObject();
                ComCodeKey key = new ComCodeKey();
                key.setCode(code);
                key.setValue(value);
                ComCode comcode = comCodeMapper.selectByPrimaryKey(key);
                if (comcode != null) {
                    jsonObject.put("value", comcode.getValue());
                    jsonObject.put("code", comcode.getCode());
                    jsonObject.put("name", comcode.getName());
                    jsonObject.put("displayCn", comcode.getDisplayCn());
                    jsonObject.put("displayEn", comcode.getDisplayEn());
                    jsonObject.put("comment", comcode.getComment());
                    jsonObject.put("insertUserId", comcode.getInsertUserId());
                    jsonObject.put("insertTime", comcode.getInsertTime());
                    jsonObject.put("updateUserId", comcode.getUpdateUserId());
                    jsonObject.put("updateTime", DataUtils.formatTimeStampToYMD(comcode.getUpdateTime()));
                    json.put("data", jsonObject);
                }
            }
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 插入码表信息
     *
     * @param data
     * @return
     */
    public JSONObject save(String data, String type) {
        JSONObject json = new JSONObject();
        try {
            ComCode comCode = JSON.parseObject(data, ComCode.class);
            JSONObject jsonObject = new JSONObject();

            //检索数据库来判断是否是更新还是插入
            ComCodeKey key = new ComCodeKey();
            key.setCode(comCode.getCode());
            key.setValue(comCode.getValue());
            ComCode com = comCodeMapper.selectByPrimaryKey(key);

            if (com == null && "insert".equals(type)) {
                comCode.setInsertTime(new Date());
                comCode.setUpdateTime(new Date());
                comCode.setDeleted(Constants.DELETED_NO);
                comCodeMapper.insertSelective(comCode);
            } else if(com != null && "update".equals(type)){
                comCode.setUpdateTime(new Date());
                comCode.setInsertUserId(null);
                comCodeMapperExt.updateByPrimaryKeySelective(comCode);
            } else if ("insert".equals(type)){
                throw new ExceptionErrorData("同样的编码已经存在");
            } else {
                throw new ExceptionErrorData("编码不存在,请更新后重试");
            }
            if (comCode != null) {
                jsonObject.put("code", comCode.getCode());
                jsonObject.put("name", comCode.getName());
                jsonObject.put("value", comCode.getValue());
                jsonObject.put("displayCn", comCode.getDisplayCn());
                jsonObject.put("displayEn", comCode.getDisplayEn());
                jsonObject.put("comment", comCode.getComment());
                jsonObject.put("deleted", comCode.getDeleted());
                jsonObject.put("updateUserId", comCode.getUpdateUserId());
                jsonObject.put("updateTime", comCode.getUpdateTime());
                jsonObject.put("insertUserId", comCode.getInsertUserId());
                jsonObject.put("insertTime", DataUtils.formatTimeStampToYMD(comCode.getUpdateTime()));
                json.put("data", jsonObject);
            }
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 删除码表信息
     *
     * @param data
     * @return
     */
    public JSONObject delete(String data) {
        JSONObject json = new JSONObject();
        try {
            ComCode comCode = (ComCode) JSON.parseObject(data, ComCode.class);
            ComCode key = new ComCode();
            key.setCode(comCode.getCode());
            key.setValue(comCode.getValue());
            key.setDeleted("1");
            comCodeMapper.updateByPrimaryKeySelective(key);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;

    }

    /**
     * 获取码表列表
     *
     * @return
     */
    public JSONObject selectByValueString(String code, String valueString) {
        JSONObject json = new JSONObject();
        try {
            ComCode input = new ComCode();
            input.setCode(code);
            input.setValue(valueString);
            List<ComCode> list = comCodeMapperExt.selectByNeed(input);
            json.put("data", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }
}
