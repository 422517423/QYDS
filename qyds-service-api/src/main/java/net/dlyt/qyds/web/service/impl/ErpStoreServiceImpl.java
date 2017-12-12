package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ErpStore;
import net.dlyt.qyds.common.dto.ShpOrg;
import net.dlyt.qyds.common.dto.ext.ErpStoreExt;
import net.dlyt.qyds.dao.ErpStoreMapper;
import net.dlyt.qyds.dao.ShpOrgMapper;
import net.dlyt.qyds.dao.ext.ErpStoreMapperExt;
import net.dlyt.qyds.dao.ext.ShpOrgMapperExt;
import net.dlyt.qyds.web.service.ErpStoreService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zlh on 2016/7/29.
 */
@Service("erpStoreService")
@Transactional(readOnly = true)
public class ErpStoreServiceImpl implements ErpStoreService {

    @Autowired
    private ErpStoreMapper mapper;

    @Autowired
    private ErpStoreMapperExt mapperExt;

    @Autowired
    private ShpOrgMapperExt mapperShpOrgExt;

    @Autowired
    private ShpOrgMapper mapperShpOrg;

    /**
     * 根据Id获取门店列表
     *
     * @return
     */
    public JSONObject selectAll(){
        JSONObject map = new JSONObject();
        try{
            List<ErpStore> list = (List<ErpStore>) mapperExt.selectAll();
            map.put("data", list);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 根据ID获取门店信息
     *
     * @param id
     * @return
     */
    public JSONObject getById(String id){
        JSONObject map = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorParam("缺少参数门店ID");
            }
            ErpStore data = mapper.selectByPrimaryKey(id);
            map.put("data", data);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 根据ID更新门店信息
     *
     * @param record
     */
    public JSONObject updateById(String record){
        JSONObject map = new JSONObject();
        try{
            ErpStore rec = JSON.parseObject(record, ErpStore.class);
            if (StringUtil.isEmpty(rec.getStoreCode())) {
                throw new ExceptionErrorParam("缺少参数门店ID");
            }
            mapper.updateByPrimaryKey(rec);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 新建门店信息
     *
     * @param record
     */
    public JSONObject insert(String record){
        JSONObject map = new JSONObject();
        try{
            ErpStore rec = JSON.parseObject(record, ErpStore.class);
            if (StringUtil.isEmpty(rec.getStoreCode())) {
                throw new ExceptionErrorParam("缺少参数门店ID");
            }
            mapper.insertSelective(rec);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 根据ID物理删除门店信息
     *
     * @param id
     */
    public JSONObject deleteByPrimaryKey(String id){
        JSONObject map = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorParam("缺少参数门店ID");
            }
            mapper.deleteByPrimaryKey(id);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 物理删除所有门店信息
     *
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject deleteAll(){
        JSONObject map = new JSONObject();
        try{
            mapperExt.deleteAll();
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 删除原有数据,批量导入门店信息
     *
     * @param list
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject inputAll(String list){
        JSONObject map = new JSONObject();
        try{
            //全部删除
            mapperExt.deleteAll();
            mapperShpOrgExt.deleteAllStore();
            List<ErpStore> ll = JSON.parseArray(list, ErpStore.class);
            for (ErpStore record : ll) {
                //插入
                mapper.insertSelective(record);
                ShpOrg store = new ShpOrg();
                store.setOrgId(record.getStoreCode());
                store.setOrgCode(record.getStoreCode());
                store.setErpStoreId(record.getStoreCode());
                store.setOrgShortName(record.getStoreNameCn());
                store.setOrgName(record.getStoreNameCn());
                String comment = "电话:"+record.getPhone()+"\r\n地址:"+record.getAddress();
                store.setComment(comment);

                //add by congkeyan start
                store.setOperate(record.getOperator());
                store.setStoreid(record.getStoreid());
                store.setStoresubid(record.getStoresubid());

                mapperShpOrgExt.insertStore(store);
            }
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 批量更新门店信息
     *
     * @param list
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject updateByList(String list){
        JSONObject map = new JSONObject();
        try{
            List<ErpStoreExt> ll = JSON.parseArray(list, ErpStoreExt.class);
            ShpOrg store = new ShpOrg();
            for (ErpStoreExt record : ll) {
                String style = record.getStyle();
                if(style.equals("10")) {
                    //新增
                    mapper.insertSelective(record);
                    store.setOrgId(record.getStoreCode());
                    store.setOrgCode(record.getStoreCode());
                    store.setErpStoreId(record.getStoreCode());
                    store.setOrgShortName(record.getStoreNameCn());
                    store.setOrgName(record.getStoreNameCn());
                    String comment = "电话:"+record.getPhone()+"\r\n地址:"+record.getAddress();
                    store.setComment(comment);

                    //add by congkeyan start
                    store.setOperate(record.getOperator());
                    store.setStoreid(record.getStoreid());
                    store.setStoresubid(record.getStoresubid());

                    mapperShpOrgExt.insertStore(store);
                } else if (style.equals("20")) {
                    //修改
                    mapperExt.updateNoTime(record);
                    store.setOrgId(record.getStoreCode());
                    store.setOrgShortName(record.getStoreNameCn());
                    store.setOrgName(record.getStoreNameCn());
                    String comment = "电话:"+record.getPhone()+"\r\n地址:"+record.getAddress();
                    store.setComment(comment);

                    //add by congkeyan start
                    store.setOperate(record.getOperator());
                    store.setStoreid(record.getStoreid());
                    store.setStoresubid(record.getStoresubid());

                    mapperShpOrgExt.updateStoreName(store);
                } else if (style.equals("30")) {
                    //删除
                    mapper.deleteByPrimaryKey(record.getStoreCode());
                    mapperShpOrg.deleteByPrimaryKey(record.getStoreCode());
                } else {
                }
            }
            map.put("resultCode", Constants.NORMAL);
        }catch(ExceptionBusiness e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }
}
