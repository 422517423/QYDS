package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ComConfig;

/**
 * Created by YiLian on 2016/12/27.
 */
public interface ComConfigService {

    JSONObject getLevelRatio();

    JSONObject edit(ComConfig form);
}
