package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ComConfig;
import net.dlyt.qyds.common.dto.ComConfigKey;
import net.dlyt.qyds.dao.ComConfigMapper;
import net.dlyt.qyds.web.service.ComConfigService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by YiLian on 2016/12/27.
 */
@Service("comConfigService")
public class ComConfigServiceImpl implements ComConfigService {

    private static final String MEMBER_LEVEL_RATIO_CODE = "MEMBER_LEVEL_RATIO";
    private static final String MEMBER_LEVEL_RATIO_TYPE = "0";

    @Autowired
    private ComConfigMapper comConfigMapper;

    @Override
    public JSONObject getLevelRatio() {
        JSONObject json = new JSONObject();
        try {
            ComConfigKey key = new ComConfigKey();
            key.setCode(MEMBER_LEVEL_RATIO_CODE);
            key.setType(MEMBER_LEVEL_RATIO_TYPE);
            ComConfig result = comConfigMapper.selectByPrimaryKey(key);

            if (result == null || StringUtil.isEmpty(result.getParam())) {
                result = new ComConfig();

                result.setCode(MEMBER_LEVEL_RATIO_CODE);
                result.setType(MEMBER_LEVEL_RATIO_TYPE);
                result.setParam("0.8");
            }

            json.put("data", result);
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

    @Override
    public JSONObject edit(ComConfig form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getCode())
                    || StringUtil.isEmpty(form.getType())) {
                throw new ExceptionBusiness("参数不正确");
            }

            ComConfigKey key = new ComConfigKey();
            key.setCode(form.getCode());
            key.setType(form.getType());
            ComConfig result = comConfigMapper.selectByPrimaryKey(key);

            if (result != null) {
                result.setParam(form.getParam());
                result.setUpdateUserId(form.getUpdateUserId());
                result.setUpdateTime(new Date());
                comConfigMapper.updateByPrimaryKeySelective(result);
            } else {
                form.setDeleted("0");
                form.setUpdateTime(new Date());
                form.setInsertUserId(form.getUpdateUserId());
                form.setInsertTime(form.getUpdateTime());
                comConfigMapper.insertSelective(form);
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
}
