package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbContact;
import net.dlyt.qyds.common.form.MmbAddressForm;
import net.dlyt.qyds.common.form.MmbContactExt;
import net.dlyt.qyds.common.form.MmbLevelManagerForm;

/**
 * Created by Congky on 16/8/2.
 */
public interface MmbContactService {

    /**
     * 添加联系我们
     *
     * @param form contactId:会员ID
     *             theme:省
     *             address:市
     *             telephone:区
     *             userName:街道
     *             comment:地址
     * @return
     */
    JSONObject add(MmbContact form);


    /**
     * 联系我们一览
     *
     * @param form
     * @return
     */
    JSONObject getList(MmbContactExt form);


}
