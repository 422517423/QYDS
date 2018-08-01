package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.AllController;
import net.dlyt.qyds.common.dto.ext.AllControllerExt;

import java.util.List;

public interface AllControllerService {
    List<AllControllerExt> selectAll(AllControllerExt ext);

    int getAllDataCount(AllControllerExt ext);

    JSONObject selectBySelective(AllController allController);

    JSONObject updateByPrimaryKeySelective(AllController allController);

    JSONObject insertSelective(AllController allController);
}
