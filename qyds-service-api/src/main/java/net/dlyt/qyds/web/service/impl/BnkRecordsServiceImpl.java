package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.BnkMaster;
import net.dlyt.qyds.common.dto.BnkRecords;
import net.dlyt.qyds.common.dto.ext.BnkRecordsExt;
import net.dlyt.qyds.common.form.BnkRecordsForm;
import net.dlyt.qyds.dao.BnkMasterMapper;
import net.dlyt.qyds.dao.BnkRecordsMapper;
import net.dlyt.qyds.dao.ext.BnkRecordsMapperExt;
import net.dlyt.qyds.web.service.BnkRecordsService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wy on 2016/8/3.
 */
@Service("bnkRecordsService")
public class BnkRecordsServiceImpl implements BnkRecordsService {

    @Autowired
    private BnkRecordsMapperExt mapperExt;

    @Autowired
    private BnkMasterMapper bnkMasterMapper;

    /**
     * 获取商品履历列表
     * @param form
     * @return
     */
    public JSONObject getList(BnkRecordsForm form) {
        JSONObject json = new JSONObject();
        try{
            BnkRecordsExt ext = new BnkRecordsExt();
            ext.setGoodsId(form.getGoodsId());
            ext.setSku(form.getSku());
            ext.setBankType(form.getBankType());
            ext.setGoodsType(form.getGoodsType());
            ext.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
            ext.setStartPoint(Integer.parseInt(form.getiDisplayStart()));

            if(!StringUtil.isEmpty(form.getErpStoreId())){
                BnkMaster bnkMaster = bnkMasterMapper.selectByPrimaryKey(Integer.parseInt(form.getErpStoreId()));
                ext.setErpStoreId(bnkMaster.getErpStoreId());
            }

            //数据库检索 -- 过滤数据
            List<BnkRecordsExt> list = mapperExt.selectAll(ext);
            //获取总数
            int allCount = mapperExt.getAllDataCount(ext);
            json.put("aaData", list);
            json.put("sEcho",form.getsEcho());
            json.put("iTotalRecords",allCount);
            json.put("iTotalDisplayRecords",allCount);
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode",Constants.FAIL);
        }
        return json;
    }

}
