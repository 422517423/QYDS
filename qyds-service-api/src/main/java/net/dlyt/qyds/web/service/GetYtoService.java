package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.YtoForm;
import org.dom4j.DocumentException;

public interface GetYtoService {
    boolean getYtoInfo(YtoForm ytoForm) throws Exception;

    String getTxLogisticID(YtoForm ytoForm) throws Exception;
}
