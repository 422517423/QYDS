package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ErpOrderMaster;
import net.dlyt.qyds.common.dto.ErpOrderSub;
import net.dlyt.qyds.common.dto.ext.ErpBankRecordExt;
import net.dlyt.qyds.common.form.ErpOrderMasterForm;
import net.dlyt.qyds.common.form.ErpOrderSubForm;
import net.dlyt.qyds.dao.ErpOrderMasterMapper;
import net.dlyt.qyds.dao.ErpOrderSubMapper;
import net.dlyt.qyds.dao.ext.ErpOrderMasterMapperExt;
import net.dlyt.qyds.dao.ext.ErpOrderSubMapperExt;
import net.dlyt.qyds.web.service.ErpOrderService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * Created by zlh on 2016/7/29.
 */
@Service("erpOrderService")
@Transactional(readOnly = true)
public class ErpOrderServiceImpl implements ErpOrderService {

    @Autowired
    private ErpOrderMasterMapper erpOrderMasterMapper;

    @Autowired
    private ErpOrderSubMapper erpOrderSubMapper;

    @Autowired
    private ErpOrderMasterMapperExt erpOrderMasterMapperExt;

    @Autowired
    private ErpOrderSubMapperExt erpOrderSubMapperExt;

    /**
     * 批量导入ERP库存主表信息
     *
     * @param data
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject inputMaster(String data){
        JSONObject json = new JSONObject();
        try{
            List<ErpOrderMaster> list = JSONArray.parseArray(data, ErpOrderMaster.class);
            for (ErpOrderMaster record : list) {
                if(StringUtil.isEmpty(record.getOrderCode())) {
                    throw new ExceptionBusiness("没有订单编码");
                }
                if(StringUtil.isEmpty(record.getMemberCode())) {
                    throw new ExceptionBusiness("没有会员编码");
                }
                //插入
                erpOrderMasterMapper.insertSelective(record);
            }
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 批量导入ERP库存主表信息
     *
     * @param data
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject inputSub(String data){
        JSONObject json = new JSONObject();
        try{
            List<ErpOrderSub> list = JSONArray.parseArray(data, ErpOrderSub.class);
            for (ErpOrderSub record : list) {
                if(StringUtil.isEmpty(record.getSubCode())) {
                    throw new ExceptionBusiness("没有子订单编码");
                }
                if(StringUtil.isEmpty(record.getOrderCode())) {
                    throw new ExceptionBusiness("没有订单编码");
                }
                //插入
                erpOrderSubMapper.insertSelective(record);
            }
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 根据条件获取ERP订单主表
     *
     * @param record
     * @return
     */
    public JSONObject getMasterPage(String record){
        JSONObject json = new JSONObject();
        try{
            ErpOrderMasterForm form = (ErpOrderMasterForm) JSON.parseObject(record, ErpOrderMasterForm.class);
            List<ErpOrderMaster> list = (List<ErpOrderMaster>) erpOrderMasterMapperExt.selectByPage(form);
            int countAll = erpOrderMasterMapperExt.getCountByPage(form);
            json.put("aaData", list);
            json.put("sEcho",form.getSEcho());
            json.put("iTotalRecords",countAll);
            json.put("iTotalDisplayRecords",countAll);
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 根据条件获取ERP订单主表
     *
     * @param record
     * @return
     */
    public JSONObject getSubPage(String record){
        JSONObject json = new JSONObject();
        try{
            ErpOrderSubForm form = (ErpOrderSubForm) JSON.parseObject(record, ErpOrderSubForm.class);
            List<ErpOrderSub> list = (List<ErpOrderSub>) erpOrderSubMapperExt.selectByPage(form);
            int countAll = erpOrderSubMapperExt.getCountByPage(form);
            json.put("aaData", list);
            json.put("sEcho",form.getSEcho());
            json.put("iTotalRecords",countAll);
            json.put("iTotalDisplayRecords",countAll);
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }
}
