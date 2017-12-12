package net.dlyt.qyds.web.service;


import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.CmsItems;
import net.dlyt.qyds.common.dto.CmsMaster;
import net.dlyt.qyds.common.dto.ext.CmsMasterExt;
import net.dlyt.qyds.common.form.CmsMsterForm;

import java.util.List;

/**
 * Created by C_Nagai on 2016/7/30.
 */
public interface CmsMasterService {

    // 回去数据列表
    JSONObject getList(CmsMsterForm form);

    // 新建CMS主表
    int insertSelective(CmsMaster record);

    // 更新CMS主表
    int updateByPrimaryKeySelective(CmsMaster record);

    // 获取CMS主表信息
    JSONObject selectCmsMasterByCmsId(CmsMaster record);

    // 保存CMS数据
    JSONObject save(CmsMasterExt record);

    // cms排序列表取得
    JSONObject orderList();

    // cms管理排序
    JSONObject updateSort(String data);

    // cms管理删除
    JSONObject delete(String cmsId);

    boolean checkGdsTypeId(String gdsTypeId, String itemId, String cmsId);

    JSONObject getMasterGoodsByCmsId(String cmsId);

    //上移操作
    JSONObject resortCmsGdsIds(String data);

    //重置序号
    JSONObject resetCmsGdsIds(String data);
}
