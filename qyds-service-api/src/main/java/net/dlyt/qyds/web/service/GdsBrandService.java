package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsBrand;
import net.dlyt.qyds.common.dto.GdsBrandExt;
import net.dlyt.qyds.common.form.GdsBrandForm;
import net.dlyt.qyds.common.form.UploadImageForm;

import java.util.List;

/**
 * Created by congkeyan on 2016/7/19.
 */
public interface GdsBrandService {

    //获取所有的商品品牌列表没有分页,没有检索条件
    JSONObject getAllList(String type);

    //获取商品品牌列表
    JSONObject selectAll(GdsBrandForm form);

    JSONObject save(GdsBrand gdsBrand);

    //获取详细信息
    JSONObject selectByPrimaryKey(String brandId);

    //删除品牌信息根据ID
    JSONObject delete(String brandId);

}
