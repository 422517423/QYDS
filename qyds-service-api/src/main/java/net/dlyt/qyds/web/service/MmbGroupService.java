package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ext.MmbGroupExt;
import net.dlyt.qyds.common.form.MmbGroupForm;

import java.util.List;

/**
 * Created by YiLian on 16/7/29.
 */
public interface MmbGroupService {

    JSONObject getList(MmbGroupForm form);

    JSONObject add(MmbGroupForm form, String userId);

    JSONObject edit(MmbGroupForm form, String userId);

    JSONObject delete(MmbGroupForm form, String userId);

    JSONObject getDetail(MmbGroupForm form);

    JSONObject getMemberList(MmbGroupForm form);

    JSONObject updateMemberList(MmbGroupForm form, String userId);
}
