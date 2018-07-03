package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.SysLogLogon;
import net.dlyt.qyds.common.dto.SysLogOperation;

import java.util.List;

/**
 * Created by C_Nagai on 2016/7/19.
 */
public interface LogService {

    JSONObject getOperationLogs(SysLogOperation record);

    JSONObject getLogonLogs(SysLogLogon record);

    JSONObject insertOperationLog(SysLogOperation record);

    JSONObject insertLogonLog(SysLogLogon record);

}
