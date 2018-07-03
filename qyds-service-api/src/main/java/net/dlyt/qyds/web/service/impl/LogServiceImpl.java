package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.SysLogLogon;
import net.dlyt.qyds.common.dto.SysLogOperation;
import net.dlyt.qyds.dao.SysLogLogonMapper;
import net.dlyt.qyds.dao.ext.SysLogLogonMapperExt;
import net.dlyt.qyds.dao.SysLogOperationMapper;
import net.dlyt.qyds.dao.ext.SysLogOperationMapperExt;
import net.dlyt.qyds.web.service.LogService;
import net.dlyt.qyds.web.service.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dkzhang on 2016/7/22.
 */
@Service("logService")
public class LogServiceImpl implements LogService {

    @Autowired
    private SysLogLogonMapper sysLogLogonMapper;

    @Autowired
    private SysLogOperationMapper sysLogOperationMapper;

    @Autowired
    private SysLogLogonMapperExt sysLogLogonMapperExt;

    @Autowired
    private SysLogOperationMapperExt sysLogOperationMapperExt;

    public JSONObject getOperationLogs(SysLogOperation record) {
        return null;
    }

    public JSONObject getLogonLogs(SysLogLogon record) {
        return null;
    }

    public JSONObject insertOperationLog(SysLogOperation record) {
        JSONObject json = new JSONObject();
        int ret = sysLogOperationMapperExt.insertSelective(record);
        if(ret == 1) {
            json.put("resultCode", Constants.NORMAL);
        }else {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    public JSONObject insertLogonLog(SysLogLogon record) {
        JSONObject json = new JSONObject();
        int ret = sysLogLogonMapperExt.insertSelective(record);
        if(ret == 1) {
            json.put("resultCode", Constants.NORMAL);
        }else {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }
}
