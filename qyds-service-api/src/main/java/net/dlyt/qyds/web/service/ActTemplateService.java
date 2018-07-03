package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ActTemplate;
import net.dlyt.qyds.common.form.ActTemplateForm;
import net.dlyt.qyds.common.form.GoodsCodeColorNameDto;

import java.util.List;

/**
 * Created by cjk on 16/7/27.
 */
public interface ActTemplateService {
    JSONObject getTemplateList(ActTemplateForm form);

    JSONObject delete(ActTemplateForm form);

    JSONObject add(ActTemplateForm form);

    JSONObject edit(ActTemplateForm form);

    JSONObject apply(ActTemplateForm form);

    JSONObject getDetail(ActTemplateForm form);

    JSONObject getTemplateApproveList(ActTemplateForm form);

    JSONObject approve(ActTemplateForm form);

    JSONObject reject(ActTemplateForm form);

    JSONObject getSkuListById(String id);

    JSONObject importSkuList(String loginId, String tempId, List<GoodsCodeColorNameDto> skuList);
}
