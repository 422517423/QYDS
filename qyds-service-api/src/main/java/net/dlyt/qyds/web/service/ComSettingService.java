package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ComSetting;
import net.dlyt.qyds.common.dto.ext.ComSettingExt;

/**
 * Created by cjk on 2016/12/14.
 */
public interface ComSettingService {
    JSONObject getDetail();
    JSONObject edit(ComSettingExt form);
}
