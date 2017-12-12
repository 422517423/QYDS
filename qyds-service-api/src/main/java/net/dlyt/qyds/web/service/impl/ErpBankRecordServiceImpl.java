package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.BnkMaster;
import net.dlyt.qyds.common.dto.ErpBankRecord;
import net.dlyt.qyds.common.dto.ext.ErpBankRecordExt;
import net.dlyt.qyds.common.form.ErpBankRecordForm;
import net.dlyt.qyds.dao.ErpBankRecordMapper;
import net.dlyt.qyds.dao.ext.BnkMasterMapperExt;
import net.dlyt.qyds.dao.ext.ErpBankRecordMapperExt;
import net.dlyt.qyds.web.service.ErpBankRecordService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * Created by zlh on 2016/7/29.
 */
@Service("erpBankRecordService")
@Transactional(readOnly = true)
public class ErpBankRecordServiceImpl implements ErpBankRecordService {

    @Autowired
    private ErpBankRecordMapper mapper;

    @Autowired
    private ErpBankRecordMapperExt mapperExt;

    @Autowired
    private BnkMasterMapperExt mapperBnkExt;

    /**
     * 根据Id获取库存列表
     *
     * @return
     */
    public JSONObject selectAll(){
        JSONObject json = new JSONObject();
        try{
            List<ErpBankRecordExt> list = (List<ErpBankRecordExt>) mapperExt.selectAll();
            json.put("data", list);
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 根据条件获取库存列表
     *
     * @param record
     * @return
     */
    public JSONObject selectBySelective(String record){
        JSONObject json = new JSONObject();
        try{
            ErpBankRecordForm form = (ErpBankRecordForm) JSON.parseObject(record, ErpBankRecordForm.class);
            List<ErpBankRecordExt> list = (List<ErpBankRecordExt>) mapperExt.selectByPage(form);
            json.put("data", list);
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 根据条件获取库存合计列表
     *
     * @param record
     * @return
     */
    public JSONObject selectSumByPage(String record) {
        JSONObject json = new JSONObject();
        try{
            ErpBankRecordForm form = (ErpBankRecordForm) JSON.parseObject(record, ErpBankRecordForm.class);
            List<ErpBankRecordExt> list = (List<ErpBankRecordExt>) mapperExt.selectSumByPage(form);
            int countAll = mapperExt.getSumCountByPage(form);
            json.put("aaData", list);
            json.put("sEcho",form.getSEcho());
            json.put("iTotalRecords",countAll);
            json.put("iTotalDisplayRecords",countAll);
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 根据条件获取库存合计件数
     *
     * @param record
     * @return
     */
    public JSONObject getCountSumBySelective(String record){
        JSONObject json = new JSONObject();
        try{
            ErpBankRecordForm form = (ErpBankRecordForm) JSON.parseObject(record, ErpBankRecordForm.class);
            int count = mapperExt.getSumCountByPage(form);
            json.put("data", count);
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 根据条件获取库存记录列表
     *
     * @param record
     * @return
     */
    public JSONObject selectRecordByPage(String record){
        JSONObject json = new JSONObject();
        try{
            ErpBankRecordForm form = (ErpBankRecordForm) JSON.parseObject(record, ErpBankRecordForm.class);
            List<ErpBankRecordExt> list = (List<ErpBankRecordExt>) mapperExt.selectRecordByPage(form);
            int countAll = mapperExt.getRecordCountByPage(form);
            json.put("aaData", list);
            json.put("sEcho",form.getSEcho());
            json.put("iTotalRecords",countAll);
            json.put("iTotalDisplayRecords",countAll);
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 根据条件获取库存记录件数
     *
     * @param record
     * @return
     */
    public JSONObject getCountSumByPage(String record){
        JSONObject json = new JSONObject();
        try{
            ErpBankRecordForm form = (ErpBankRecordForm) JSON.parseObject(record, ErpBankRecordForm.class);
            int count = mapperExt.getRecordCountByPage(form);
            json.put("data", count);
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 根据ID获取库存信息
     *
     * @param id
     * @return
     */
    public JSONObject getById(String id){
        JSONObject json = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorParam("缺少参数库存ID");
            }
            mapper.selectByPrimaryKey(Integer.parseInt(id));
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 根据ID更新库存信息
     *
     * @param record
     */
    public JSONObject updateById(String record){
        JSONObject json = new JSONObject();
        try{
            ErpBankRecord form = (ErpBankRecord) JSON.parseObject(record, ErpBankRecord.class);
            if (null == form.getRecordid()) {
                throw new ExceptionErrorParam("缺少参数库存ID");
            }
            mapper.updateByPrimaryKey(form);
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 新建库存信息
     *
     * @param record
     */
    public JSONObject insert(String record){
        JSONObject json = new JSONObject();
        try{
            ErpBankRecord form = (ErpBankRecord) JSON.parseObject(record, ErpBankRecord.class);
            mapper.insertSelective(form);
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 根据ID物理删除库存信息
     *
     * @param id
     */
    public JSONObject deleteByPrimaryKey(String id){
        JSONObject json = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorParam("缺少参数库存ID");
            }
            mapper.deleteByPrimaryKey(Integer.parseInt(id));
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 物理删除所有库存信息
     *
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject deleteAll(){
        JSONObject json = new JSONObject();
        try{
            mapperExt.deleteAll();
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 批量导入库存信息
     *
     * @param data
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject inputAll(String data){
        JSONObject json = new JSONObject();
        try{
            List<ErpBankRecord> list = JSONArray.parseArray(data, ErpBankRecord.class);
            for (ErpBankRecord record : list) {
                if(StringUtil.isEmpty(record.getErpSku())) {
                    throw new ExceptionBusiness("没有sku");
                }
                if(StringUtil.isEmpty(record.getErpStoreid())) {
                    throw new ExceptionBusiness("没有门店ID");
                }
                //插入
                mapper.insertSelective(record);
                //检查库存主表
                BnkMaster master = mapperBnkExt.getByErp(record);
                if (master == null) {
                    //插入库存数据
                    mapperBnkExt.insertByErp(record);
                } else {
                    //更新库存数据
                    master.setLastCount(master.getNewCount());
                    master.setNewCount(master.getNewCount() + record.getInoutCount());
                    mapperBnkExt.updateCountById(master);
                }
            }
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }
}
