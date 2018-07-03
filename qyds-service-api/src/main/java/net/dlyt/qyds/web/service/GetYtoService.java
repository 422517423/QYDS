package net.dlyt.qyds.web.service;

import net.dlyt.qyds.common.dto.YtoForm;

public interface GetYtoService {
    boolean getYtoInfo(YtoForm ytoForm) throws Exception;

    String getTxLogisticID(YtoForm ytoForm) throws Exception;



}
