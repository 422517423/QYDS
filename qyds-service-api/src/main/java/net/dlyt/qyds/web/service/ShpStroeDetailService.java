package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ShpStroeDetail;
import net.dlyt.qyds.common.dto.ext.ShpStroeDetailExt;
import net.dlyt.qyds.common.form.ShpStroeDetailForm;

import java.util.List;

/**
 * Created by wy on 2016/8/10.
 */
public interface ShpStroeDetailService {

    JSONObject selectAll(ShpStroeDetailForm form) ;
}
