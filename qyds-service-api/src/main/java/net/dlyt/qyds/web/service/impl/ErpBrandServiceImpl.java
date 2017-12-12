package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ErpBrand;
import net.dlyt.qyds.common.dto.GdsBrand;
import net.dlyt.qyds.common.dto.ext.ErpBrandExt;
import net.dlyt.qyds.dao.ErpBrandMapper;
import net.dlyt.qyds.dao.GdsBrandMapper;
import net.dlyt.qyds.dao.ext.ErpBrandMapperExt;
import net.dlyt.qyds.dao.ext.GdsBrandMapperExt;
import net.dlyt.qyds.web.service.ErpBrandService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.UUID;

/**
 * Created by zlh on 2016/7/29.
 */
@Service("erpBrandService")
@Transactional(readOnly = true)
public class ErpBrandServiceImpl implements ErpBrandService {

    @Autowired
    private ErpBrandMapper mapper;

    @Autowired
    private ErpBrandMapperExt mapperExt;

    @Autowired
    private GdsBrandMapper mapperGds;

    @Autowired
    private GdsBrandMapperExt mapperGdsExt;

    /**
     * 根据Id获取品牌列表
     *
     * @return
     */
    public JSONObject selectAll(){
        JSONObject map = new JSONObject();
        try{
            List<ErpBrand> list = mapperExt.selectAll();
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
     * 根据ID获取品牌信息
     *
     * @param id
     * @return
     */
    public JSONObject getById(String id){
        JSONObject map = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorParam("缺少参数品牌ID");
            }
            ErpBrand data = mapper.selectByPrimaryKey(id);
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
     * 根据ID更新品牌信息
     *
     * @param record
     */
    public JSONObject updateById(String record){
        JSONObject json = new JSONObject();
        try{
            ErpBrand form = (ErpBrand) JSON.parseObject(record, ErpBrand.class);
            if (StringUtil.isEmpty(form.getBrandCode())) {
                throw new ExceptionErrorParam("缺少参数品牌ID");
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
     * 新建品牌信息
     *
     * @param record
     */
    public JSONObject insert(String record){
        JSONObject json = new JSONObject();
        try{
            ErpBrand form = (ErpBrand) JSON.parseObject(record, ErpBrand.class);
            form.setBrandCode(UUID.randomUUID().toString());
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
     * 根据ID物理删除品牌信息
     *
     * @param id
     */
    public JSONObject deleteByPrimaryKey(String id){
        JSONObject json = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorParam("缺少参数品牌ID");
            }
            mapper.deleteByPrimaryKey(id);
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
     * 物理删除所有品牌信息
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
     * 删除原有数据,批量导入品牌信息
     *
     * @param data
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject inputAll(String data){
        JSONObject json = new JSONObject();
        try{
            //全部删除
            mapperExt.deleteAll();
            mapperGdsExt.deleteAllErp();
            List<ErpBrand> list = JSON.parseArray(data, ErpBrand.class);
            for (ErpBrand record : list) {
                //插入
                mapper.insertSelective(record);
                GdsBrand gdsBrand = new GdsBrand();
                gdsBrand.setBrandId(record.getBrandCode());
                gdsBrand.setShopId("00000000");
                gdsBrand.setType("10");
                gdsBrand.setBrandCode(record.getBrandCode());
                gdsBrand.setErpBrandCode(record.getBrandCode());
                gdsBrand.setBrandName(record.getBrandNameCn());
                gdsBrand.setInsertUserId("ERP");
                mapperGds.insertSelective(gdsBrand);
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

    /**
     * 批量更新品牌信息
     *
     * @param data
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject updateByList(String data){
        JSONObject json = new JSONObject();
        try{
            List<ErpBrandExt> list = JSON.parseArray(data, ErpBrandExt.class);
            for (ErpBrandExt record : list) {
                String style = record.getStyle();
                GdsBrand gdsBrand = new GdsBrand();
                if(style.equals("10")) {
                    //新增
                    mapper.insertSelective(record);
                    gdsBrand.setBrandId(record.getBrandCode());
                    gdsBrand.setShopId("00000000");
                    gdsBrand.setType("10");
                    gdsBrand.setBrandCode(record.getBrandCode());
                    gdsBrand.setErpBrandCode(record.getBrandCode());
                    gdsBrand.setBrandName(record.getBrandNameCn());
                    gdsBrand.setInsertUserId("ERP");
                    mapperGds.insertSelective(gdsBrand);
                } else if (style.equals("20")) {
                    //修改
                    mapperExt.updateNoTime(record);
                    gdsBrand.setBrandId(record.getBrandCode());
                    gdsBrand.setBrandName(record.getBrandNameCn());
                    gdsBrand.setUpdateUserId("ERP");
                    mapperGdsExt.updateErpName(gdsBrand);
                } else if (style.equals("30")) {
                    //删除
                    mapper.deleteByPrimaryKey(record.getBrandCode());
                    mapperGds.deleteByPrimaryKey(record.getBrandCode());
                } else {
                }
            }
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
}
