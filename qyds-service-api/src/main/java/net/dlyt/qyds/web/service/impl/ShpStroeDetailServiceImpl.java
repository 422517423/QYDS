package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ShpStroeDetail;
import net.dlyt.qyds.common.dto.ext.ShpStroeDetailExt;
import net.dlyt.qyds.common.form.ShpStroeDetailForm;
import net.dlyt.qyds.dao.ShpStroeDetailMapper;
import net.dlyt.qyds.dao.ext.ShpStroeDetailMapperExt;
import net.dlyt.qyds.web.service.ShpStroeDetailService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wy on 2016/8/10.
 */
@Service("shpStroeDetailService")
@Transactional(readOnly = true)
public class ShpStroeDetailServiceImpl  implements ShpStroeDetailService {
    @Autowired
    private ShpStroeDetailMapper mapper;
    @Autowired
    private ShpStroeDetailMapperExt mapperExt;

        public JSONObject selectAll(ShpStroeDetailForm form) {
            JSONObject json = new JSONObject();
                try {
                    ShpStroeDetailExt detail = new ShpStroeDetailExt();
                    detail.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
                    detail.setStartPoint(Integer.parseInt(form.getiDisplayStart()));
                    //数据库检索 -- 过滤数据
                    List<ShpStroeDetailExt> list = mapperExt.selectAll(detail);
                    //获取总数
                    int allCount = mapperExt.getAllDataCount(detail);
                    json.put("aaData", list);
                    json.put("sEcho", form.getsEcho());
                    json.put("iTotalRecords", allCount);
                    json.put("iTotalDisplayRecords", allCount);
                    json.put("resultCode", Constants.NORMAL);
                } catch (Exception e) {
                    json.put("resultCode", Constants.FAIL);
                }
                return json;

        }
    }
