package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.MmbLevelManagerForm;

/**
 * Created by YiLian on 16/8/8.
 */
public interface MmbLevelManagerService {
    /**
     * 待升级审批会员一览
     *
     * @param form
     * @return
     */
    JSONObject getList(MmbLevelManagerForm form);

    /**
     * 确认升级会员等级
     *
     * @param form
     * @param userId
     * @return
     */
    JSONObject approval(MmbLevelManagerForm form, String userId);

    /**
     * 会员等级降级
     * <p/>
     * 计算上一自然年度会员消费累计积分是否达到对应级别的80%
     *
     * @return
     */
    JSONObject relegation();
}
